/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.csv.export;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

import com.archimatetool.csv.CSVConstants;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IAccessRelationship;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IAssociationRelationship;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.IInfluenceRelationship;
import com.archimatetool.model.IJunction;
import com.archimatetool.model.IProfile;
import com.archimatetool.model.IProperty;



/**
 * CSV Exporter
 * 
 * @author Phillip Beauvoir
 */
public class CSVExporter implements CSVConstants {
    
    private char fDelimiter = ',';
    private String fFilePrefix = ""; //$NON-NLS-1$
    
    private boolean fStripNewLines = false;
    
    private boolean fExcelCompatible = false;
    
    private String fEncoding = "UTF-8"; //$NON-NLS-1$
    
    /*
     * Internal option. BUT...
     * If one exports to the csv files with a model that has properties, then edits the model and removes all properties,
     * and re-exports to CSV. The original "properties.csv" file would still exist, containing orphans.
     * So it seems better to export all three to be on the safe side, even if one is empty.
     * This way we can be sure that elements, relations, and properties are always a matched tuple.
     */
    private boolean fWriteEmptyFile = true;
    
    private IArchimateModel fModel;
    
    public CSVExporter(IArchimateModel model) {
        fModel = model;
    }
    
    public void export(File folder) throws IOException {
        writeModelAndElements(new File(folder, createElementsFileName()));
        writeRelationships(new File(folder, createRelationsFileName()));
        writeProperties(new File(folder, createPropertiesFileName()));
    }
    
    /**
     * Set the delimiter character.
     * Default is the comma ","
     * @param delimiter
     */
    public void setDelimiter(char delimiter) {
        fDelimiter = delimiter;
    }
    
    /**
     * Set the prefix to use on file names. A null value is ignored.
     * @param prefix
     */
    public void setFilePrefix(String prefix) {
        if(prefix != null) {
            fFilePrefix = prefix;
        }
    }
    
    public void setStripNewLines(boolean set) {
        fStripNewLines = set;
    }
    
    public void setExcelCompatible(boolean set) {
        fExcelCompatible = set;
    }
    
    public void setEncoding(String encoding) {
        fEncoding = encoding;
    }
    
    /**
     * Write the Model and All Elements
     */
    private void writeModelAndElements(File file) throws IOException {
        Writer writer = createOutputStreamWriter(file);
        
        // Write BOM
        writeBOM(writer);
        
        // Write Header
        String header = createHeader(MODEL_ELEMENTS_HEADER);
        writer.write(header);
        
        // CRLF
        writer.write(CRLF);
        
        // Write Model
        String modelRow = createModelRow();
        writer.write(modelRow);
        
        // Write Elements
        writeElementsInFolder(writer, fModel.getFolder(FolderType.STRATEGY));
        writeElementsInFolder(writer, fModel.getFolder(FolderType.BUSINESS));
        writeElementsInFolder(writer, fModel.getFolder(FolderType.APPLICATION));
        writeElementsInFolder(writer, fModel.getFolder(FolderType.TECHNOLOGY));
        writeElementsInFolder(writer, fModel.getFolder(FolderType.MOTIVATION));
        writeElementsInFolder(writer, fModel.getFolder(FolderType.IMPLEMENTATION_MIGRATION));
        writeElementsInFolder(writer, fModel.getFolder(FolderType.OTHER));
        
        writer.close();
    }
    
    /**
     * Write all elements in a given folder and its child folders to Writer
     */
    private void writeElementsInFolder(Writer writer, IFolder folder) throws IOException {
        if(folder == null) {
            return;
        }
        
        List<IArchimateConcept> concepts = getConcepts(folder);
        sort(concepts);
        
        for(IArchimateConcept concept : concepts) {
            if(concept instanceof IArchimateElement) {
                writer.write(CRLF);
                writer.write(createElementRow((IArchimateElement)concept));
            }
        }
    }
    
    /**
     * Write All Relationships
     */
    private void writeRelationships(File file) throws IOException {
        List<IArchimateConcept> concepts = getConcepts(fModel.getFolder(FolderType.RELATIONS));
        sort(concepts);
        
        // Are there any to write?
        if(!fWriteEmptyFile && concepts.isEmpty()) {
            return;
        }
        
        Writer writer = createOutputStreamWriter(file);
        
        // Write BOM
        writeBOM(writer);
        
        // Write Header
        String header = createHeader(RELATIONSHIPS_HEADER);
        writer.write(header);
        
        // Write Relationships
        for(IArchimateConcept concept : concepts) {
            if(concept instanceof IArchimateRelationship) {
                writer.write(CRLF);
                writer.write(createRelationshipRow((IArchimateRelationship)concept));
            }
        }
        
        writer.close();
    }
    
    /**
     * Write All Properties
     */
    private void writeProperties(File file) throws IOException {
        // Are there any to write?
        if(!fWriteEmptyFile && !hasProperties()) {
            return;
        }
        
        Writer writer = createOutputStreamWriter(file);
        
        // Write BOM
        writeBOM(writer);
        
        // Write Header
        String header = createHeader(PROPERTIES_HEADER);
        writer.write(header);
        
        // Write Model Properties
        for(IProperty property : fModel.getProperties()) {
            writer.write(CRLF);
            writer.write(createPropertyRow(fModel.getId(), property));
        }
        
        // Write Element and Relationship Properties
        for(Iterator<EObject> iter = fModel.eAllContents(); iter.hasNext();) {
            EObject eObject = iter.next();
            if(eObject instanceof IArchimateConcept) {
                IArchimateConcept concept = (IArchimateConcept)eObject;
                
                for(IProperty property : concept.getProperties()) {
                    writer.write(CRLF);
                    writer.write(createPropertyRow(concept.getId(), property));
                }
                
                // Write special attributes as properties
                writeSpecialProperties(writer, concept);
            }
        }
        
        writer.close();
    }
    
    private void writeSpecialProperties(Writer writer, IArchimateConcept concept) throws IOException {
        // Influence relationship strength
        if(concept instanceof IInfluenceRelationship) {
            String strength = ((IInfluenceRelationship)concept).getStrength();
            if(StringUtils.isSet(strength)) {
                writer.write(CRLF);
                writer.write(createPropertyRow(concept.getId(), INFLUENCE_STRENGTH, strength));
            }
        }
        
        // Access relationship type
        else if(concept instanceof IAccessRelationship) {
            writer.write(CRLF);
            writer.write(createPropertyRow(concept.getId(), ACCESS_TYPE, ACCESS_TYPES.get(((IAccessRelationship)concept).getAccessType())));
        }
        
        // Association relationship directed
        else if(concept instanceof IAssociationRelationship) {
            writer.write(CRLF);
            writer.write(createPropertyRow(concept.getId(), ASSOCIATION_DIRECTED,
                    ((IAssociationRelationship)concept).isDirected() ? "true" : "false")); //$NON-NLS-1$ //$NON-NLS-2$
        }
        
        // Junction Type
        else if(concept instanceof IJunction) {
            String type = ((IJunction)concept).getType();
            if(IJunction.AND_JUNCTION_TYPE.equals(type)) {
                type = JUNCTION_AND;
            }
            else {
                type = JUNCTION_OR;
            }
            writer.write(CRLF);
            writer.write(createPropertyRow(concept.getId(), JUNCTION_TYPE, type));
        }
    }
    
    /**
     * @return true if the model has any user properties
     */
    boolean hasProperties() {
        if(!fModel.getProperties().isEmpty()) {
            return true;
        }
        
        for(Iterator<EObject> iter = fModel.eAllContents(); iter.hasNext();) {
            EObject eObject = iter.next();
            if(eObject instanceof IArchimateConcept) {
                IArchimateConcept concept = (IArchimateConcept)eObject;
                if(!concept.getProperties().isEmpty()) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Create a Header from given string elements
     */
    String createHeader(String[] elements) {
        StringBuffer sb = new StringBuffer();
        
        for(int i = 0; i < elements.length; i++) {
            String s = elements[i];
            sb.append("\""); //$NON-NLS-1$
            sb.append(s);
            sb.append("\""); //$NON-NLS-1$
            if(i < elements.length - 1) {
                sb.append(fDelimiter);
            }
        }
        
        return sb.toString();
    }
    
    /**
     * Create a String Row for the Archimate Model
     */
    String createModelRow() {
        StringBuffer sb = new StringBuffer();
        
        String id = fModel.getId();
        sb.append(surroundWithQuotes(id));
        sb.append(fDelimiter);
        
        sb.append(surroundWithQuotes(ARCHIMATE_MODEL_TYPE));
        sb.append(fDelimiter);
        
        String name = normalise(fModel.getName());
        sb.append(surroundWithQuotes(name));
        sb.append(fDelimiter);
        
        String purpose = normalise(fModel.getPurpose());
        sb.append(surroundWithQuotes(purpose));
        sb.append(fDelimiter);
        
        // Specialization dummy entry
        sb.append("\"\""); //$NON-NLS-1$
        
        return sb.toString();
    }

    /**
     * Create a String Row for an Element
     */
    String createElementRow(IArchimateElement element) {
        StringBuffer sb = new StringBuffer();
        
        // ID
        String id = element.getId();
        sb.append(surroundWithQuotes(id));
        sb.append(fDelimiter);
        
        // Class
        sb.append(surroundWithQuotes(element.eClass().getName()));
        sb.append(fDelimiter);
        
        // Name
        String name = normalise(element.getName());
        sb.append(surroundWithQuotes(name));
        sb.append(fDelimiter);
        
        // Documentation
        String documentation = normalise(element.getDocumentation());
        sb.append(surroundWithQuotes(documentation));
        sb.append(fDelimiter);
        
        // Specialization
        IProfile profile = element.getPrimaryProfile();
        String specialization = normalise(profile != null ? profile.getName() : ""); //$NON-NLS-1$
        sb.append(surroundWithQuotes(specialization));
        
        return sb.toString();
    }
    
    /**
     * Create a String Row for a Relationship
     */
    String createRelationshipRow(IArchimateRelationship relationship) {
        StringBuffer sb = new StringBuffer();
        
        // ID
        String id = relationship.getId();
        sb.append(surroundWithQuotes(id));
        sb.append(fDelimiter);
        
        // Class
        sb.append(surroundWithQuotes(relationship.eClass().getName()));
        sb.append(fDelimiter);
        
        // Name
        String name = normalise(relationship.getName());
        sb.append(surroundWithQuotes(name));
        sb.append(fDelimiter);
        
        // Documentation
        String documentation = normalise(relationship.getDocumentation());
        sb.append(surroundWithQuotes(documentation));
        sb.append(fDelimiter);
        
        // Source
        if(relationship.getSource() != null) {
            String sourceID = relationship.getSource().getId();
            sb.append(surroundWithQuotes(sourceID));
        }
        else {
            sb.append("\"\""); //$NON-NLS-1$
        }
        sb.append(fDelimiter);
        
        // Target
        if(relationship.getTarget() != null) {
            String targetID = relationship.getTarget().getId();
            sb.append(surroundWithQuotes(targetID));
        }
        else {
            sb.append("\"\""); //$NON-NLS-1$
        }
        sb.append(fDelimiter);
        
        // Specialization
        IProfile profile = relationship.getPrimaryProfile();
        String specialization = normalise(profile != null ? profile.getName() : ""); //$NON-NLS-1$
        sb.append(surroundWithQuotes(specialization));
        
        return sb.toString();
    }

    /**
     * Create a String Row for a Property
     */
    String createPropertyRow(String conceptID, IProperty property) {
        return createPropertyRow(conceptID, property.getKey(), property.getValue());
    }

    /**
     * Create a String Row for a Key/Value
     */
    String createPropertyRow(String conceptID, String key, String value) {
        StringBuffer sb = new StringBuffer();
        
        sb.append(surroundWithQuotes(conceptID));
        sb.append(fDelimiter);
        
        key = normalise(key);
        sb.append(surroundWithQuotes(key));
        sb.append(fDelimiter);
        
        value = normalise(value);
        sb.append(surroundWithQuotes(value));
        
        return sb.toString();
    }

    /**
     * Write BOM byte to file
     * @param writer
     * @throws IOException
     */
    private void writeBOM(Writer writer) throws IOException {
        if(fEncoding.contains("BOM")) { //$NON-NLS-1$
            writer.write('\ufeff');
        }
    }
    
    /**
     * Return a normalised String.
     * A Null string is returned as an empty string
     * All types of CR and LF and TAB characters are converted to single spaces
     */
    String normalise(String s) {
        if(s == null) {
            return ""; //$NON-NLS-1$
        }
        
        // Newlines (optional)
        if(fStripNewLines) {
            s = s.replaceAll("(\r\n|\r|\n)", " ");  //$NON-NLS-1$//$NON-NLS-2$
        }
        
        // Tabs become a space
        s = s.replace("\t", " "); //$NON-NLS-1$ //$NON-NLS-2$
        
        // Single quotes become double quotes
        s = s.replace("\"", "\"\"");  //$NON-NLS-1$//$NON-NLS-2$
        
        return s;
    }
    
    String surroundWithQuotes(String s) {
        if(needsLeadingCharHack(s)) {
            return "\"=\"\"" + s + "\"\"\""; //$NON-NLS-1$ //$NON-NLS-2$
        }
        else if(needsLeadingCharFormulaHack(s)) {
            s = " " + s; //$NON-NLS-1$
        }
        return "\"" + s + "\""; //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    // See http://www.creativyst.com/Doc/Articles/CSV/CSV01.htm#CSVAndExcel
    boolean needsLeadingCharHack(String s) {
        return s != null && fExcelCompatible && (s.startsWith(" ") || s.startsWith("0"));  //$NON-NLS-1$//$NON-NLS-2$
    }
    
    // If string starts with "=", "+", "-", "@"
    // See https://payatu.com/csv-injection-basic-to-exploit
    //     https://owasp.org/www-community/attacks/CSV_Injection
    boolean needsLeadingCharFormulaHack(String s) {
        return s != null && fExcelCompatible && (s.startsWith("=") || s.startsWith("+") || s.startsWith("-") || s.startsWith("@"));  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    }
    
    /**
     * Return a list of all elements/relations in a given folder and its child folders
     */
    private List<IArchimateConcept> getConcepts(IFolder folder) {
        List<IArchimateConcept> list = new ArrayList<IArchimateConcept>();
        
        if(folder == null) {
            return list;
        }
        
        for(EObject object : folder.getElements()) {
            if(object instanceof IArchimateConcept) {
                list.add((IArchimateConcept)object);
            }
        }
        
        for(IFolder f : folder.getFolders()) {
            list.addAll(getConcepts(f));
        }
        
        return list;
    }

    /**
     * Sort a list of ArchimateElement/Relationship types
     * Sort by class name then element name
     */
    void sort(List<IArchimateConcept> list) {
        if(list == null || list.size() < 2) {
            return;
        }
        
        Collections.sort(list, new Comparator<IArchimateConcept>() {
            @Override
            public int compare(IArchimateConcept o1, IArchimateConcept o2) {
                if(o1.eClass().equals(o2.eClass())) {
                    String name1 = StringUtils.safeString(o1.getName().toLowerCase().trim());
                    String name2 = StringUtils.safeString(o2.getName().toLowerCase().trim());
                    return name1.compareTo(name2);
                }
                
                String name1 = o1.eClass().getName().toLowerCase();
                String name2 = o2.eClass().getName().toLowerCase();
                return name1.compareTo(name2);
            }
        });
    }
    
    String createElementsFileName() {
        return fFilePrefix + ELEMENTS_FILENAME + FILE_EXTENSION;
    }
    
    String createRelationsFileName() {
        return fFilePrefix + RELATIONS_FILENAME + FILE_EXTENSION;
    }
    
    String createPropertiesFileName() {
        return fFilePrefix + PROPERTIES_FILENAME + FILE_EXTENSION;
    }
    
    OutputStreamWriter createOutputStreamWriter(File file) throws IOException {
        if("ANSI".equals(fEncoding)) { //$NON-NLS-1$
            return new OutputStreamWriter(new FileOutputStream(file));
        }
        else if(fEncoding.startsWith("UTF-8")) { //$NON-NLS-1$
            return new OutputStreamWriter(new FileOutputStream(file), "UTF-8"); //$NON-NLS-1$
        }
        else {
            return new OutputStreamWriter(new FileOutputStream(file), fEncoding);
        }
    }
}
