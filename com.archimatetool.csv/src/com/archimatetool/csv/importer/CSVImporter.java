/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.csv.importer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
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
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.IProperties;
import com.archimatetool.model.IProperty;
import com.archimatetool.model.IRelationship;
import com.archimatetool.model.util.ArchimateModelUtils;


/**
 * CSV Importer
 * 
 * @author Phillip Beauvoir
 */
public class CSVImporter implements CSVConstants {
    
    private IArchimateModel fModel;
    
    // ID -> IArchimateElement: new elements and relations added
    Map<String, IArchimateElement> newElements = new HashMap<String, IArchimateElement>();
    
    // IProperty -> IProperties object: new Property added
    Map<IProperty, IProperties> newProperties = new HashMap<IProperty, IProperties>();
    
    // IProperty -> Value: updated Property
    Map<IProperty, String> updatedProperties = new HashMap<IProperty, String>();

    // IArchimateElement -> String[] : Updated elements' name and documentation
    Map<IArchimateElement, String[]> updatedElements = new HashMap<IArchimateElement, String[]>();

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
     * Do the actual import given the elements file
     * @param elementsFile
     */
    void doImport(File elementsFile) throws IOException, CSVParseException {
        // Import Elements into model
        importElements(elementsFile);
        
        // Do we have a matching relations file?
        File relationsFile = CSVImporter.getAccessoryFileFromElementsFile(elementsFile, RELATIONS_FILENAME);
        if(relationsFile.exists() && relationsFile.isFile()) {
            importRelations(relationsFile);
        }
        
        // Do we have a matching properties file?
        File propertiesFile = CSVImporter.getAccessoryFileFromElementsFile(elementsFile, PROPERTIES_FILENAME);
        if(propertiesFile.exists() && propertiesFile.isFile()) {
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
        for(final IArchimateElement element : newElements.values()) {
            Command cmd = new Command() {
                IFolder folder = fModel.getDefaultFolderForElement(element);
                
                @Override
                public void execute() {
                    folder.getElements().add(element);
                }
                
                @Override
                public void undo() {
                    folder.getElements().remove(element);
                }
            };
            
            if(cmd.canExecute()) {
                compoundCommand.add(cmd);
            }
        }
        
        // Updated elements/relations' name and documentation
        for(final Entry<IArchimateElement, String[]> entry : updatedElements.entrySet()) {
            // Name
            Command cmd = new EObjectFeatureCommand(Messages.CSVImporter_0, entry.getKey(), IArchimatePackage.Literals.NAMEABLE__NAME, entry.getValue()[0]);
            if(cmd.canExecute()) {
                compoundCommand.add(cmd);
            }
            
            // Documentation
            cmd = new EObjectFeatureCommand(Messages.CSVImporter_0, entry.getKey(), IArchimatePackage.Literals.DOCUMENTABLE__DOCUMENTATION, entry.getValue()[1]);
            if(cmd.canExecute()) {
                compoundCommand.add(cmd);
            }
        }

        // New Properties
        for(final Entry<IProperty, IProperties> entry : newProperties.entrySet()) {
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
        IArchimateElement element = findElementInModel(id, eClass);
        
        // Yes
        if(element != null) {
            updatedElements.put(element, new String[] { name, documentation });
        }
        // No, create a new one
        else {
            element = (IArchimateElement)IArchimateFactory.eINSTANCE.create(eClass);
            element.setId(id);
            element.setName(name);
            element.setDocumentation(documentation);
            newElements.put(id, element);
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
        
        // Type
        String type = csvRecord.get(1);
        EClass eClass = (EClass)IArchimatePackage.eINSTANCE.getEClassifier(type);
        if(!isRelationshipEClass(eClass)) {
            throw new CSVParseException(Messages.CSVImporter_4 + id);
        }

        String name = normalise(csvRecord.get(2));
        String documentation = csvRecord.get(3);
        
        // Is the relation already in the model?
        IRelationship relation = (IRelationship)findElementInModel(id, eClass);
        
        // Yes
        if(relation != null) {
            updatedElements.put(relation, new String[] { name, documentation});
        }
        // No, create a new one
        else {
            relation = (IRelationship)IArchimateFactory.eINSTANCE.create(eClass);
            
            // Find source and target elements
            String sourceID = csvRecord.get(4);
            IArchimateElement source = findReferencedElement(sourceID);
            
            String targetID = csvRecord.get(5);
            IArchimateElement target = findReferencedElement(targetID);
            
            // Is it a valid relationship?
            if(!ArchimateModelUtils.isValidRelationship(source.eClass(), target.eClass(), eClass)) {
                throw new CSVParseException(Messages.CSVImporter_5 + id);
            }
            
            relation.setSource(source);
            relation.setTarget(target);
            
            relation.setId(id);
            relation.setName(name);
            relation.setDocumentation(documentation);

            newElements.put(id, relation);
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
        
        // Find referenced element in newly created list
        IProperties propertiesObject = newElements.get(id);
        
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
            parser = new CSVParser(new FileReader(file), CSVFormat.DEFAULT);
            records = parser.getRecords();
        }
        catch(IOException ex) {
            if(parser != null) {
                parser.close();
            }
            if(ex.getMessage() != null && ex.getMessage().contains(errorMessage)) {
                try {
                    parser = new CSVParser(new FileReader(file), CSVFormat.DEFAULT.withDelimiter(';'));
                    records = parser.getRecords();
                }
                catch(IOException ex2) {
                    if(parser != null) {
                        parser.close();
                    }
                    if(ex2.getMessage() != null && ex2.getMessage().contains(errorMessage)) {
                        parser = new CSVParser(new FileReader(file), CSVFormat.DEFAULT.withDelimiter('\t'));
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
     * @param one of RELATIONS_FILENAME or PROPERTIES_FILENAME
     * @return A matching acessory file name given the elements file name
     *         For example, given file "prefix-elements.csv" and matching on RELATIONS_FILENAME will return "prefix-relations.csv"
     */
    static File getAccessoryFileFromElementsFile(File file, String targetFilename) {
        String name = file.getPath();
        int index = name.lastIndexOf(ELEMENTS_FILENAME);
        name = new StringBuilder(name).replace(index, index + ELEMENTS_FILENAME.length(), targetFilename).toString();
        return new File(name);
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
    
    /**
     * Check a given id is set, and is not a duplicate of one in the CSV file
     * @throws CSVParseException 
     */
    void checkID(String id) throws CSVParseException {
        if(!StringUtils.isSet(id)) {
            throw new CSVParseException(Messages.CSVImporter_6);
        }
        if(newElements.containsKey(id)) {
            throw new CSVParseException(Messages.CSVImporter_8);
        }
    }
    
    String generateID() {
        String id;
        do {
            id = UUID.randomUUID().toString().split("-")[0]; //$NON-NLS-1$
        }
        while(newElements.containsKey(id));
        
        return id;
    }
    
    /**
     * Find an existing element in the model given its id and class type. Return null if not found.
     * @throws CSVParseException 
     */
    IArchimateElement findElementInModel(String id, EClass eClass) throws CSVParseException {
        EObject eObject = ArchimateModelUtils.getObjectByID(fModel, id);
        
        // Found an element with this id
        if(eObject != null) {
            // class matches
            if(eObject.eClass() == eClass) {
                return (IArchimateElement)eObject;
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
     * Find a referenced element either in the model or in the newly created elements list
     */
    IArchimateElement findReferencedElement(String id) throws CSVParseException {
        // Do we have it as a newly created element?
        EObject eObject = newElements.get(id);
        
        // No. How about in the model?
        if(eObject == null) {
            eObject = ArchimateModelUtils.getObjectByID(fModel, id);
        }
        
        // Not found
        if(eObject == null) {
            throw new CSVParseException(Messages.CSVImporter_10 + id);
        }
        
        // Check eClass type
        if(!isArchimateElementEClass(eObject.eClass())) {
            throw new CSVParseException(Messages.CSVImporter_11 + id);
        }

        return (IArchimateElement)eObject;
    }
    
    boolean isArchimateElementEClass(EClass eClass) {
        return eClass != null && IArchimatePackage.eINSTANCE.getArchimateElement().isSuperTypeOf(eClass)
                && !IArchimatePackage.eINSTANCE.getRelationship().isSuperTypeOf(eClass);
    }
    
    boolean isRelationshipEClass(EClass eClass) {
        return eClass != null && IArchimatePackage.eINSTANCE.getRelationship().isSuperTypeOf(eClass);
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
}
