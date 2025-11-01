/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.csv.importer;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.osgi.util.NLS;

import com.archimatetool.csv.CSVConstants;
import com.archimatetool.csv.CSVParseException;
import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.editor.utils.FileUtils;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IAccessRelationship;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateModelObject;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IAssociationRelationship;
import com.archimatetool.model.IInfluenceRelationship;
import com.archimatetool.model.IJunction;
import com.archimatetool.model.IProfile;
import com.archimatetool.model.IProperties;
import com.archimatetool.model.IProperty;
import com.archimatetool.model.util.ArchimateModelUtils;
import com.archimatetool.model.util.UUIDFactory;
import com.archimatetool.modelimporter.ImportException;
import com.archimatetool.modelimporter.ModelImporter;


/**
 * CSV Importer
 * 
 * This uses the Model Importer to do the work.
 * 
 * 1. Create a copy of the target mode
 * 2. Remove the diagram folders from the copied model as we are only importing concepts and properties
 * 3. Add an ArchiveManager to the copied model - the copied model may have images in it
 * 4. Import the elements, relations and properties from CSV into the copied model
 * 5. Import the copied model into the target model using the Model Importer
 * 6. The Model Importer will diff the copied and target models and create the undo/redo commmands
 * 
 * @author Phillip Beauvoir
 */
public class CSVImporter implements CSVConstants {
    
    private IArchimateModel targetModel;
    
    IArchimateModel newModel;
    
    // New relations
    Map<String, IArchimateRelationship> newRelations = new HashMap<>();
    
    // New properties
    Map<IProperties, List<IProperty>> newProperties = new HashMap<>();

    // IArchimateRelationship -> Source/Target IDs in two String array objects [0] and [1]
    Map<IArchimateRelationship, String[]> relationshipSourceTargets = new HashMap<>();
    
    // Lookup cache
    Map<String, IArchimateModelObject> objectLookup = new HashMap<>();

    // Error message that we expect to see when attempting to import with different delimiters
    final static String EXPECTED_ERROR_MESSAGE = "Invalid character between encapsulated token and delimiter"; //$NON-NLS-1$
    
    public CSVImporter(IArchimateModel model) {
        targetModel = model;
    }
    
    public void doImport(File file) throws IOException, CSVParseException, ImportException {
        copyModel();
        
        // What file is it?
        
        File elementsFile = getMatchingFile(file, ELEMENTS_FILENAME);
        if(elementsFile != null && elementsFile.exists()) {
            importElements(elementsFile);
        }

        File relationsFile = getMatchingFile(file, RELATIONS_FILENAME);
        if(relationsFile != null && relationsFile.exists()) {
            importRelations(relationsFile);
        }
        
        File propertiesFile = getMatchingFile(file, PROPERTIES_FILENAME);
        if(propertiesFile != null && propertiesFile.exists()) {
            importProperties(propertiesFile);
        }

        // Model Importer
        ModelImporter importer = new ModelImporter();
        importer.setUpdateAll(true);
        importer.doImport(newModel, targetModel);
    }
    
    void copyModel() {
        // Copy the model
        newModel = EcoreUtil.copy(targetModel);
        
        // Add an Archive Manager - this is needed by the model importer if the target model has images
        IArchiveManager archiveManager = IArchiveManager.FACTORY.createArchiveManager(newModel);
        newModel.setAdapter(IArchiveManager.class, archiveManager);
        
        // Clear the diagrams as we are only importing concepts and properties
        newModel.getFolder(FolderType.DIAGRAMS).getFolders().clear();
        newModel.getFolder(FolderType.DIAGRAMS).getElements().clear();
        
        // Create lookup table
        for(Iterator<EObject> iter = newModel.eAllContents(); iter.hasNext();) {
            EObject element = iter.next();
            if(element instanceof IArchimateModelObject) {
                objectLookup.put(((IArchimateModelObject)element).getId(), (IArchimateModelObject)element);
            }
        }
    }
    
    // -------------------------------- Import Model and Elements --------------------------------
    
    /**
     * Import Elements from CSV file
     * @param file The file to import
     * @throws IOException
     * @throws CSVParseException
     */
    void importElements(File file) throws IOException, CSVParseException {
        List<CSVRecord> records = getRecords(file);
        
        // Should have at least one record
        if(records.isEmpty()) {
            throw new CSVParseException(Messages.CSVImporter_1);
        }
        
        // Header
        if(!isHeaderRecord(records.get(0), MODEL_ELEMENTS_HEADER)) {
            throw new CSVParseException(Messages.CSVImporter_13);
        }
        
        // Header size is what we'll use to check the rest of the records
        int headerSize = records.get(0).size();
        
        for(int i = 1; i < records.size(); i++) {
            CSVRecord csvRecord = records.get(i);
            
            // Wrong record length
            if(csvRecord.size() != headerSize) {
                throw new CSVParseException(NLS.bind(Messages.CSVImporter_2, csvRecord.toString()) );
            }

            // Model (this is optional)
            if(isModelRecord(csvRecord)) {
                parseModelRecord(records.get(i));
            }
            // Element
            else {
                createElementFromRecord(csvRecord);
            }
        }
    }
    
    /**
     * @return True if csvRecord is a model record
     */
    private boolean isModelRecord(CSVRecord csvRecord) {
        return ARCHIMATE_MODEL_TYPE.equals(csvRecord.get(1));
    }
    
    /**
     * Add a model record's information
     */
    private void parseModelRecord(CSVRecord csvRecord) {
        // If the id is set for the model the properties CSV file might use this as the reference id for adding model properties
        String id = csvRecord.get(0);
        if(StringUtils.isSet(id)) {
            objectLookup.put(id, newModel);
        }
        
        // Model Name
        String modelName = csvRecord.get(2);
        if(modelName != null) {
            newModel.setName(modelName);
        }

        // Model Purpose
        String modelPurpose = csvRecord.get(3);
        if(modelPurpose != null) {
            newModel.setPurpose(modelPurpose);
        }
    }

    /**
     * Create an Archimate Element from a given CSVRecord
     */
    private void createElementFromRecord(CSVRecord csvRecord) throws CSVParseException {
        // ID
        String id = csvRecord.get(0);
        
        if(!StringUtils.isSet(id)) {
            id = generateID();
        }
        else {
            checkIDForInvalidCharacters(id);
        }
        
        // Class type
        String type = csvRecord.get(1);
        EClass eClass = (EClass)IArchimatePackage.eINSTANCE.getEClassifier(type);
        // Can only be Archimate element type
        if(!isArchimateElementEClass(eClass)) {
            throw new CSVParseException(Messages.CSVImporter_3);
        }

        IArchimateElement element = (IArchimateElement)findObjectInModel(id, eClass);
        if(element == null) {
            element = (IArchimateElement)IArchimateFactory.eINSTANCE.create(eClass);
            element.setId(id);
            newModel.getDefaultFolderForObject(element).getElements().add(element);
            objectLookup.put(element.getId(), element);
        }

        String name = normalise(csvRecord.get(2));
        element.setName(name);

        String documentation = csvRecord.get(3);
        element.setDocumentation(documentation);

        // If we have a specialization name then create and add a new Profile
        // Specialization is optional
        String specializationName = csvRecord.size() > 4 ? csvRecord.get(4) : null;
        if(StringUtils.isSet(specializationName)) {
            setProfileForNewConcept(element, specializationName);
        }
    }
    
    // -------------------------------- Import Relations --------------------------------
    
    /**
     * Import Relations from CSV file
     * @param file The file to import
     * @throws IOException
     * @throws CSVParseException
     */
    void importRelations(File file) throws IOException, CSVParseException {
        List<CSVRecord> records = getRecords(file);
        
        // Header
        if(!isHeaderRecord(records.get(0), RELATIONSHIPS_HEADER)) {
            throw new CSVParseException(Messages.CSVImporter_14);
        }
        
        // Header size is what we'll use to check the rest of the records
        int headerSize = records.get(0).size();
        
        for(int i = 1; i < records.size(); i++) {
            CSVRecord csvRecord = records.get(i);
            
            // Wrong record length
            if(csvRecord.size() != headerSize) {
                throw new CSVParseException(NLS.bind(Messages.CSVImporter_2, csvRecord.toString()) );
            }

            // Relation
            createRelationFromRecord(csvRecord);
        }

        // Now connect the relations
        for(Entry<String, IArchimateRelationship> entry : newRelations.entrySet()) {
            IArchimateRelationship relation = entry.getValue();

            // Get the source and target ids from the lookup table
            String[] sourceTargets = relationshipSourceTargets.get(relation);
            IArchimateConcept source = findReferencedConcept(sourceTargets[0]);
            IArchimateConcept target = findReferencedConcept(sourceTargets[1]);

            // Is it a valid relationship?
            if(!ArchimateModelUtils.isValidRelationship(source.eClass(), target.eClass(), relation.eClass())) {
                throw new CSVParseException(Messages.CSVImporter_5 + relation.getId());
            }

            // Connect
            relation.connect(source, target);
        }
    }
    
    /**
     * Create an Archimate relationship from a given CSVRecord
     */
    private void createRelationFromRecord(CSVRecord csvRecord) throws CSVParseException {
        // ID
        String id = csvRecord.get(0);
        
        if(!StringUtils.isSet(id)) {
            id = generateID();
        }
        else {
            checkIDForInvalidCharacters(id);
        }
        
        // Type
        String type = csvRecord.get(1);
        EClass eClass = (EClass)IArchimatePackage.eINSTANCE.getEClassifier(type);
        if(!isArchimateRelationshipEClass(eClass)) {
            throw new CSVParseException(Messages.CSVImporter_4 + id);
        }

        // Source and target ids
        String sourceID = csvRecord.get(4);
        String targetID = csvRecord.get(5);

        // Find relation in model
        IArchimateRelationship relation = (IArchimateRelationship)findObjectInModel(id, eClass);
        
        // Relation does not exist or does exist but source or target id has changed in CSV, so create a new one
        if(relation == null ||
                    !(Objects.equals(sourceID, relation.getSource().getId()) && Objects.equals(targetID, relation.getTarget().getId()))) {
            relation = (IArchimateRelationship)IArchimateFactory.eINSTANCE.create(eClass);
            relation.setId(id);
            newModel.getDefaultFolderForObject(relation).getElements().add(relation);
            newRelations.put(id, relation);
            objectLookup.put(relation.getId(), relation);
        }

        // Store in lookup table
        relationshipSourceTargets.put(relation, new String[] { sourceID, targetID });

        // Name
        String name = normalise(csvRecord.get(2));
        relation.setName(name);

        // Documentation
        String documentation = csvRecord.get(3);
        relation.setDocumentation(documentation);

        // If we have a specialization name then create and add a new Profile
        String specializationName = csvRecord.size() > 6 ? csvRecord.get(6) : null;
        if(StringUtils.isSet(specializationName)) {
            setProfileForNewConcept(relation, specializationName);
        }
    }

    // -------------------------------- Import Properties --------------------------------
    
    /**
     * Import Properties from CSV file
     * @param file The file to import
     * @throws IOException
     * @throws CSVParseException
     */
    void importProperties(File file) throws IOException, CSVParseException {
        for(CSVRecord csvRecord : getRecords(file)) {
            if(!isPropertiesRecordCorrectSize(csvRecord)) {
                throw new CSVParseException(Messages.CSVImporter_2);
            }

            // Header
            if(isHeaderRecord(csvRecord, PROPERTIES_HEADER)) {
                continue;
            }
            // Property
            else {
                createPropertyFromRecord(csvRecord);
            }
        }
        
        // Add properties now
        for(Entry<IProperties, List<IProperty>> entry : newProperties.entrySet()) {
            for(IProperty p : entry.getValue()) {
                entry.getKey().getProperties().add(p);
            }
        }
    }
    
    private boolean isPropertiesRecordCorrectSize(CSVRecord csvRecord) {
        return csvRecord.size() == PROPERTIES_HEADER.length;
    }
    
    /**
     * Create a Property from a given CSVRecord
     */
    private void createPropertyFromRecord(CSVRecord csvRecord) throws CSVParseException {
        // ID
        String id = csvRecord.get(0);
        
        if(!StringUtils.isSet(id)) {
            throw new CSVParseException(Messages.CSVImporter_6);
        }
        else {
            checkIDForInvalidCharacters(id);
        }
        
        IProperties propertiesObject = (IProperties)findObjectInModel(id, IArchimatePackage.eINSTANCE.getProperties());
        
        // Not found
        if(propertiesObject == null) {
            throw new CSVParseException(Messages.CSVImporter_7 + id);
        }
        
        String key = normalise(csvRecord.get(1));
        String value = normalise(csvRecord.get(2));
        
        // Handle special properties for some concepts' attributes
        if(INFLUENCE_STRENGTH.equals(key) && propertiesObject instanceof IInfluenceRelationship) {
            ((IInfluenceRelationship)propertiesObject).setStrength(value);
            return;
        }
        else if(ACCESS_TYPE.equals(key) && propertiesObject instanceof IAccessRelationship) {
            int newvalue = ACCESS_TYPES.indexOf(value);
            ((IAccessRelationship)propertiesObject).setAccessType(newvalue);
            return;
        }
        else if(ASSOCIATION_DIRECTED.endsWith(key) && propertiesObject instanceof IAssociationRelationship) {
            boolean newvalue = "true".equalsIgnoreCase(value); //$NON-NLS-1$
            ((IAssociationRelationship)propertiesObject).setDirected(newvalue);
            return;
        }
        else if(JUNCTION_TYPE.equals(key) && propertiesObject instanceof IJunction) {
            if(JUNCTION_AND.equals(value)) {
                value = IJunction.AND_JUNCTION_TYPE;
            }
            else {
                value = IJunction.OR_JUNCTION_TYPE;
            }
            ((IJunction)propertiesObject).setType(value);
            return;
        }
        
        // Is there already a property with this key?
        IProperty property = getProperty(propertiesObject, key);
        if(property != null) {
            property.setValue(value);
        }
        // Create new one
        else {
            property = IArchimateFactory.eINSTANCE.createProperty();
            property.setKey(key);
            property.setValue(value);
            
            // Add properties in order
            List<IProperty> list = newProperties.get(propertiesObject);
            if(list == null) {
                list = new ArrayList<>();
                newProperties.put(propertiesObject, list);
            }
            list.add(property);
        }
    }

    
    // -------------------------------- Helpers --------------------------------
    
    /**
     * Get all records for a CSV file.
     * This is a brute-force approach to try with a comma delimiter first.
     * If that fails then try a semicolon, and if that fails, a tab.
     * 
     * @param file The file to open
     * @return list of CSVRecord
     * @throws IOException
     */
    List<CSVRecord> getRecords(File file) throws IOException {
        final char[] delimiters = {',', ';', '\t'};
        
        IOException ex = new IOException("Failed to parse CSV!"); //$NON-NLS-1$
        
        // Iterate through these delimiters...
        for(char delimiter : delimiters) {
            // To handle files that start with a Byte Order Mark (BOM), like some Excel CSV files, you need an extra step to deal with the optional BOM bytes.
            BOMInputStream bomIn = BOMInputStream.builder().setFile(file).get();
            InputStreamReader reader = new InputStreamReader(bomIn, "UTF-8"); //$NON-NLS-1$
            CSVFormat csvFormat = CSVFormat.Builder.create().setDelimiter(delimiter).setCommentMarker('#').get();
            
            try(CSVParser parser = CSVParser.parse(reader, csvFormat))  {
                return parser.getRecords();
            }
            catch(UncheckedIOException ex1) { // This will be thrown if the delimiter doesn't match the one in the file
                ex = ex1.getCause(); // Get the actual exception
                // In this case we expect to see this error message.
                // But if it's not the expected exception and message then break and throw the exception
                if(!(ex.getMessage() != null && ex.getMessage().contains(EXPECTED_ERROR_MESSAGE))) {
                    break;
                }
            }
        }
        
        // If we got here then we failed, so throw the exception
        throw ex;
    }
    
    /**
     * @param file
     * @return True if file contains the part "elements" at the end of its name
     */
    static boolean isElementsFileName(File file) {
        String name = FileUtils.getFileNameWithoutExtension(file);
        return name.endsWith(ELEMENTS_FILENAME);
    }
    
    /**
     * @param file
     * @return True if file contains the part "relations" at the end of its name
     */
    static boolean isRelationsFileName(File file) {
        String name = FileUtils.getFileNameWithoutExtension(file);
        return name.endsWith(RELATIONS_FILENAME);
    }

    /**
     * @param file
     * @return True if file contains the part "properties" at the end of its name
     */
    static boolean isPropertiesFileName(File file) {
        String name = FileUtils.getFileNameWithoutExtension(file);
        return name.endsWith(PROPERTIES_FILENAME);
    }
    
    /**
     * @param file
     * @param target one of ELEMENTS_FILENAME, RELATIONS_FILENAME or PROPERTIES_FILENAME
     * @return A matching acessory file name given the file name
     *         For example, given file "prefix-elements.csv" and matching on RELATIONS_FILENAME will return "prefix-relations.csv"
     */
    File getMatchingFile(File file, String target) {
        String match = null;
        
        if(isElementsFileName(file)) {
            match = ELEMENTS_FILENAME;
        }
        else if(isRelationsFileName(file)) {
            match = RELATIONS_FILENAME;
        }
        else if(isPropertiesFileName(file)) {
            match = PROPERTIES_FILENAME;
        }
        else {
            return null;
        }
        
        String path = file.getPath();
        int index =  path.lastIndexOf(match);
        path = new StringBuilder(path).replace(index, index + match.length(), target).toString();
        return new File(path);
    }

    /**
     * Return a normalised String.
     * Fields such as Name and Properties should not have these characters.
     * A Null string is returned as an empty string
     * All types of CR and LF and TAB characters are converted to single spaces
     */
    String normalise(String s) {
        if(s == null) {
            return ""; //$NON-NLS-1$
        }
        
        // Newlines and Tabs
        s = s.replaceAll("(\r\n|\r|\n|\t)", " ");  //$NON-NLS-1$//$NON-NLS-2$
        
        return s;
    }
    
    /**
     * @param csvRecord
     * @param fields
     * @return True if csvRecord matches a header with given fields
     */
    private boolean isHeaderRecord(CSVRecord csvRecord, String[] fields) {
        if(csvRecord.getRecordNumber() != 1) {
            return false;
        }
        
        for(int i = 0; i < csvRecord.size(); i++) {
            String r = csvRecord.get(i);
            if(!r.equalsIgnoreCase(fields[i])) {
                return false;
            }
        }
        
        return true;
    }
    
    private String generateID() {
        return UUIDFactory.createID(null);
    }
    
    void checkIDForInvalidCharacters(String id) throws CSVParseException {
        if(!id.matches("^[a-zA-Z0-9._-]+$")) { //$NON-NLS-1$
            throw new CSVParseException(Messages.CSVImporter_12 + id);
        }
    }
    
    /**
     * Find an existing object in the target model given its id and class type. Return null if not found.
     */
    EObject findObjectInModel(String id, EClass eClass) throws CSVParseException {
        EObject eObject = objectLookup.get(id);
        
        // Found an element with this id
        if(eObject != null) {
            // class matches
            if(eClass != null && eClass.isInstance(eObject)) {
                return eObject;
            }
            // Not the right class, so that's an error we should report
            else {
                throw new CSVParseException(Messages.CSVImporter_9 + id);
            }
        }
        
        // Not found
        return null;
    }
    
    /**
     * Find a referenced concept in the model
     */
    IArchimateConcept findReferencedConcept(String id) throws CSVParseException {
        EObject eObject = objectLookup.get(id);
        
        // Not found
        if(eObject == null) {
            throw new CSVParseException(Messages.CSVImporter_10 + id);
        }
        
        // Check eClass type
        if(!isArchimateConceptEClass(eObject.eClass())) {
            throw new CSVParseException(Messages.CSVImporter_11 + id);
        }

        return (IArchimateConcept)eObject;
    }
    
    boolean isArchimateConceptEClass(EClass eClass) {
        return eClass != null && IArchimatePackage.eINSTANCE.getArchimateConcept().isSuperTypeOf(eClass);
    }
    
    boolean isArchimateElementEClass(EClass eClass) {
        return eClass != null && IArchimatePackage.eINSTANCE.getArchimateElement().isSuperTypeOf(eClass);
    }
    
    boolean isArchimateRelationshipEClass(EClass eClass) {
        return eClass != null && IArchimatePackage.eINSTANCE.getArchimateRelationship().isSuperTypeOf(eClass);
    }
    
    IProperty getProperty(IProperties propertiesObject, String key) {
        for(IProperty property : propertiesObject.getProperties()) {
            if(property.getKey().equals(key)) {
                return property;
            }
        }
        
        return null;
    }

    /**
     * Set a profile for a newly created concept
     */
    private void setProfileForNewConcept(IArchimateConcept newConcept, String specializationName) {
        // Do we have a matching Profile in the model?
        IProfile profile = ArchimateModelUtils.getProfileByNameAndType(newModel, specializationName, newConcept.eClass().getName());
        
        // No, so create a new Profile
        if(profile == null) {
            profile = IArchimateFactory.eINSTANCE.createProfile();
            profile.setName(specializationName);
            profile.setConceptType(newConcept.eClass().getName());
            newModel.getProfiles().add(profile);
        }
        
        // Assign it
        newConcept.getProfiles().add(profile);
    }
}
