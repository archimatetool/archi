/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.csv.importer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CompoundCommand;

import com.archimatetool.csv.CSVConstants;
import com.archimatetool.csv.CSVParseException;
import com.archimatetool.editor.model.commands.EObjectFeatureCommand;
import com.archimatetool.editor.model.commands.NonNotifyingCompoundCommand;
import com.archimatetool.editor.utils.FileUtils;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IAccessRelationship;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IAssociationRelationship;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.IInfluenceRelationship;
import com.archimatetool.model.IJunction;
import com.archimatetool.model.IProperties;
import com.archimatetool.model.IProperty;
import com.archimatetool.model.util.ArchimateModelUtils;


/**
 * CSV Importer
 * 
 * @author Phillip Beauvoir
 */
public class CSVImporter implements CSVConstants {
    
    private IArchimateModel fModel;
    
    // ID -> IArchimateConcept: new elements and relations added
    Map<String, IArchimateConcept> newConcepts = new HashMap<String, IArchimateConcept>();
    
    // IProperty -> IProperties object: new Property added
    Map<IProperty, IProperties> newProperties = new HashMap<IProperty, IProperties>();
    
    // IProperty -> Value: updated Property
    Map<IProperty, String> updatedProperties = new HashMap<IProperty, String>();

    // IArchimateConcept -> Map [EAttribute, value] : Updated concepts' features
    Map<IArchimateConcept, Map<EAttribute, Object>> updatedConcepts = new HashMap<IArchimateConcept, Map<EAttribute, Object>>();
    
    // IArchimateRelationship -> Source/Target IDs in two String array objects [0] and [1]
    Map<IArchimateRelationship, String[]> relationshipSourceTargets = new HashMap<IArchimateRelationship, String[]>();

    // CSV Model id. This might be set as a reference for Properties. Might be null.
    private String modelID;
    
    // Model name from CSV. Optional.
    private String modelName;
    
    // Model purpose from CSV. Optional.
    private String modelPurpose;

    public CSVImporter(IArchimateModel model) {
        fModel = model;
    }
    
    /**
     * Do the actual import given the file
     * @param file
     */
    public void doImport(File file) throws IOException, CSVParseException {
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

        // Execute the Commands
        CommandStack stack = (CommandStack)fModel.getAdapter(CommandStack.class);
        stack.execute(createCommands());
    }
    
    /**
     * Create Commands
     */
    Command createCommands() {
        // Create Commands
        CompoundCommand compoundCommand = new NonNotifyingCompoundCommand();
        
        // Model Name
        if(modelName != null) {
            Command cmd = new EObjectFeatureCommand(Messages.CSVImporter_0, fModel, IArchimatePackage.Literals.NAMEABLE__NAME, modelName);
            if(cmd.canExecute()) {
                compoundCommand.add(cmd);
            }
        }
        
        // Model Purpose
        if(modelPurpose != null) {
            Command cmd = new EObjectFeatureCommand(Messages.CSVImporter_0, fModel, IArchimatePackage.Literals.ARCHIMATE_MODEL__PURPOSE, modelPurpose);
            if(cmd.canExecute()) {
                compoundCommand.add(cmd);
            }
        }
        
        // New elements/relations
        for(final IArchimateConcept concept : newConcepts.values()) {
            Command cmd = new Command() {
                IFolder folder = fModel.getDefaultFolderForObject(concept);
                
                @Override
                public void execute() {
                    folder.getElements().add(concept);
                }
                
                @Override
                public void undo() {
                    folder.getElements().remove(concept);
                }
            };
            
            if(cmd.canExecute()) {
                compoundCommand.add(cmd);
            }
        }
        
        // Updated concepts' features
        for(Entry<IArchimateConcept, Map<EAttribute, Object>> conceptEntry : updatedConcepts.entrySet()) {
            for(Entry<EAttribute, Object> entry : conceptEntry.getValue().entrySet()) {
                Command cmd = new EObjectFeatureCommand(Messages.CSVImporter_0, conceptEntry.getKey(), entry.getKey(), entry.getValue());
                if(cmd.canExecute()) {
                    compoundCommand.add(cmd);
                }
            }
        }

        // New Properties
        for(Entry<IProperty, IProperties> entry : newProperties.entrySet()) {
            Command cmd = new Command() {
                IProperty property = entry.getKey();
                IProperties propertiesObject = entry.getValue();
                
                @Override
                public void execute() {
                    propertiesObject.getProperties().add(property);
                }
                
                @Override
                public void undo() {
                    propertiesObject.getProperties().remove(property);
                }
            };
            
            if(cmd.canExecute()) {
                compoundCommand.add(cmd);
            }
        }
        
        // Updated Property Value
        for(Entry<IProperty, String> entry : updatedProperties.entrySet()) {
            Command cmd = new EObjectFeatureCommand(Messages.CSVImporter_0, entry.getKey(), IArchimatePackage.Literals.PROPERTY__VALUE, entry.getValue());
            if(cmd.canExecute()) {
                compoundCommand.add(cmd);
            }
        }
     
        return compoundCommand;
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
        
        for(CSVRecord csvRecord : records) {
            if(!isElementsRecordCorrectSize(csvRecord)) {
                throw new CSVParseException(Messages.CSVImporter_2);
            }

            // Header
            if(isHeaderRecord(csvRecord, MODEL_ELEMENTS_HEADER)) {
                continue;
            }

            // Model (this is optional)
            if(isModelRecord(csvRecord)) {
                parseModelRecord(csvRecord);
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
        // If the id is set for the model we will not set the model's id to it but we will keep a reference to it
        // because the properties CSV file might use this as the reference id for adding model properties
        String id = csvRecord.get(0);
        if(StringUtils.isSet(id)) {
            modelID = id;
        }
        
        modelName = csvRecord.get(2);
        modelPurpose = csvRecord.get(3);
    }

    private boolean isElementsRecordCorrectSize(CSVRecord csvRecord) {
        return csvRecord.size() == MODEL_ELEMENTS_HEADER.length;
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

        String name = normalise(csvRecord.get(2));
        String documentation = csvRecord.get(3);
        
        // Is the element already in the model?
        IArchimateElement element = (IArchimateElement)findArchimateConceptInModel(id, eClass);
        
        // Yes it is, so update values
        if(element != null) {
            storeUpdatedConceptFeature(element, IArchimatePackage.Literals.NAMEABLE__NAME, name);
            storeUpdatedConceptFeature(element, IArchimatePackage.Literals.DOCUMENTABLE__DOCUMENTATION, documentation);
        }
        // No, create a new element
        else {
            element = (IArchimateElement)IArchimateFactory.eINSTANCE.create(eClass);
            element.setId(id);
            element.setName(name);
            element.setDocumentation(documentation);
            newConcepts.put(id, element);
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
        for(CSVRecord csvRecord : getRecords(file)) {
            if(!isRelationsRecordCorrectSize(csvRecord)) {
                throw new CSVParseException(Messages.CSVImporter_2);
            }

            // Header
            if(isHeaderRecord(csvRecord, RELATIONSHIPS_HEADER)) {
                continue;
            }
            // Relation
            else {
                createRelationFromRecord(csvRecord);
            }
        }
        
        // Now connect the relations
        for(Entry<String, IArchimateConcept> entry : newConcepts.entrySet()) {
            if(entry.getValue() instanceof IArchimateRelationship) {
                IArchimateRelationship relation = (IArchimateRelationship)entry.getValue();
                
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
    }
    
    private boolean isRelationsRecordCorrectSize(CSVRecord csvRecord) {
        return csvRecord.size() == RELATIONSHIPS_HEADER.length;
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

        String name = normalise(csvRecord.get(2));
        String documentation = csvRecord.get(3);
        
        // Is the relation already in the model?
        IArchimateRelationship relation = (IArchimateRelationship)findArchimateConceptInModel(id, eClass);
        
        // Yes it is, so updated values
        if(relation != null) {
            storeUpdatedConceptFeature(relation, IArchimatePackage.Literals.NAMEABLE__NAME, name);
            storeUpdatedConceptFeature(relation, IArchimatePackage.Literals.DOCUMENTABLE__DOCUMENTATION, documentation);
        }
        // No, create a new one
        else {
            relation = (IArchimateRelationship)IArchimateFactory.eINSTANCE.create(eClass);
            relation.setId(id);
            relation.setName(name);
            relation.setDocumentation(documentation);
            
            // Get source and target ids and store in lookup table
            String sourceID = csvRecord.get(4);
            String targetID = csvRecord.get(5);
            relationshipSourceTargets.put(relation, new String[] { sourceID, targetID });
            
            newConcepts.put(id, relation);
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
        
        // Find referenced concept in newly created list
        IProperties propertiesObject = newConcepts.get(id);
        
        // Not found, check if it's referencing an existing element in the model
        if(propertiesObject == null) {
            EObject eObject = ArchimateModelUtils.getObjectByID(fModel, id);
            if(eObject instanceof IProperties) {
                propertiesObject = (IProperties)eObject;
            }
        }

        // Not found, check if it's referencing the model
        if(propertiesObject == null && id.equals(modelID)) {
            propertiesObject = fModel;
        }
        
        // Not found at all
        if(propertiesObject == null) {
            throw new CSVParseException(Messages.CSVImporter_7 + id);
        }
        
        String key = normalise(csvRecord.get(1));
        String value = normalise(csvRecord.get(2));
        
        // Handle special properties for some concepts' attributes
        if(INFLUENCE_STRENGTH.equals(key) && propertiesObject instanceof IInfluenceRelationship) {
            storeUpdatedConceptFeature((IArchimateConcept)propertiesObject,
                    IArchimatePackage.Literals.INFLUENCE_RELATIONSHIP__STRENGTH, value);
            return;
        }
        else if(ACCESS_TYPE.equals(key) && propertiesObject instanceof IAccessRelationship) {
            int newvalue = ACCESS_TYPES.indexOf(value);
            storeUpdatedConceptFeature((IArchimateConcept)propertiesObject,
                    IArchimatePackage.Literals.ACCESS_RELATIONSHIP__ACCESS_TYPE, newvalue);
            return;
        }
        else if(ASSOCIATION_DIRECTED.endsWith(key) && propertiesObject instanceof IAssociationRelationship) {
            boolean newvalue = "true".equalsIgnoreCase(value); //$NON-NLS-1$
            storeUpdatedConceptFeature((IArchimateConcept)propertiesObject,
                    IArchimatePackage.Literals.ASSOCIATION_RELATIONSHIP__DIRECTED, newvalue);
            return;
        }
        else if(JUNCTION_TYPE.equals(key) && propertiesObject instanceof IJunction) {
            if(JUNCTION_AND.equals(value)) {
                value = IJunction.AND_JUNCTION_TYPE;
            }
            else {
                value = IJunction.OR_JUNCTION_TYPE;
            }
            storeUpdatedConceptFeature((IArchimateConcept)propertiesObject, IArchimatePackage.Literals.JUNCTION__TYPE, value);
            return;
        }
        
        // Is there already a property with this key?
        IProperty property = getProperty(propertiesObject, key);
        if(property != null) {
            updatedProperties.put(property, value);
        }
        // No, create new one
        else {
            property = IArchimateFactory.eINSTANCE.createProperty();
            property.setKey(key);
            property.setValue(value);
            newProperties.put(property, propertiesObject);
        }
    }

    
    // -------------------------------- Helpers --------------------------------
    
    /**
     * Get all records for a CSV file.
     * This is a brute-force approach to try with a comma delimiter first. If that fails then
     * try a semicolon, and if that fails, a tab.
     * 
     * @param file The file to open
     * @return Records, which may be empty but never null
     * @throws IOException
     */
    List<CSVRecord> getRecords(File file) throws IOException {
        List<CSVRecord> records = new ArrayList<CSVRecord>(); 
        CSVParser parser = null;
        
        String errorMessage = "invalid char between encapsulated token and delimiter"; //$NON-NLS-1$
        
        try {
            InputStreamReader is = new InputStreamReader(new BOMInputStream(new FileInputStream(file)), "UTF-8"); //$NON-NLS-1$
            parser = new CSVParser(is, CSVFormat.DEFAULT);
            records = parser.getRecords();
        }
        catch(IOException ex) {
            if(parser != null) {
                parser.close();
            }
            if(ex.getMessage() != null && ex.getMessage().contains(errorMessage)) {
                try {
                    InputStreamReader is = new InputStreamReader(new BOMInputStream(new FileInputStream(file)), "UTF-8"); //$NON-NLS-1$
                    parser = new CSVParser(is, CSVFormat.DEFAULT.withDelimiter(';'));
                    records = parser.getRecords();
                }
                catch(IOException ex2) {
                    if(parser != null) {
                        parser.close();
                    }
                    if(ex2.getMessage() != null && ex2.getMessage().contains(errorMessage)) {
                        InputStreamReader is = new InputStreamReader(new BOMInputStream(new FileInputStream(file)), "UTF-8"); //$NON-NLS-1$
                        parser = new CSVParser(is, CSVFormat.DEFAULT.withDelimiter('\t'));
                        records = parser.getRecords();
                    }
                    else {
                        throw ex2;
                    }
                }
            }
            else {
                throw ex;
            }
        }
        finally {
            if(parser != null) {
                parser.close();
            }
        }
        
        return records;
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
        if(csvRecord.getRecordNumber() != 1 && csvRecord.size() != fields.length) {
            return false;
        }
        
        for(int i = 0; i < fields.length; i++) {
            String field = fields[i];
            if(!field.equalsIgnoreCase(csvRecord.get(i))) {
                return false;
            }
        }
        
        return true;
    }
    
    String generateID() {
        String id;
        do {
            id = UUID.randomUUID().toString();
        }
        while(newConcepts.containsKey(id));
        
        return id;
    }
    
    void checkIDForInvalidCharacters(String id) throws CSVParseException {
        if(!id.matches("^[a-zA-Z0-9._-]+$")) { //$NON-NLS-1$
            throw new CSVParseException(Messages.CSVImporter_12 + id);
        }
    }
    
    /**
     * Find an existing archimate concept in the model given its id and class type. Return null if not found.
     * @throws CSVParseException 
     */
    IArchimateConcept findArchimateConceptInModel(String id, EClass eClass) throws CSVParseException {
        EObject eObject = ArchimateModelUtils.getObjectByID(fModel, id);
        
        // Found an element with this id
        if(eObject != null) {
            // class matches
            if(eObject.eClass() == eClass) {
                return (IArchimateConcept)eObject;
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
     * Find a referenced concept either in the model or in the newly created elements list
     */
    IArchimateConcept findReferencedConcept(String id) throws CSVParseException {
        // Do we have it as a newly created element?
        EObject eObject = newConcepts.get(id);
        
        // No. How about in the model?
        if(eObject == null) {
            eObject = ArchimateModelUtils.getObjectByID(fModel, id);
        }
        
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
    
    boolean hasProperty(IProperties propertiesObject, String key, String value) {
        for(IProperty property : propertiesObject.getProperties()) {
            if(property.getKey().equals(key) && property.getValue().equals(value)) {
                return true;
            }
        }
        
        return false;
    }
    
    IProperty getProperty(IProperties propertiesObject, String key) {
        for(IProperty property : propertiesObject.getProperties()) {
            if(property.getKey().equals(key)) {
                return property;
            }
        }
        
        return null;
    }
    
    void storeUpdatedConceptFeature(IArchimateConcept concept, EAttribute feature, Object value) {
        Map<EAttribute, Object> map = updatedConcepts.get(concept);
        
        if(map == null) {
            map = new HashMap<EAttribute, Object>();
            updatedConcepts.put(concept, map);
        }
        
        map.put(feature, value);
    }
}
