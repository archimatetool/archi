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
import com.archimatetool.model.IArchimateComponent;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.IProperty;
import com.archimatetool.model.IRelationship;



/**
 * CSV Exporter
 * 
 * @author Phillip Beauvoir
 */
public class CSVExporter implements CSVConstants {
    
    private char fDelimiter = ',';
    private String fFilePrefix = ""; //$NON-NLS-1$
    
    private boolean fStripNewLines = false;
    
    // See http://www.creativyst.com/Doc/Articles/CSV/CSV01.htm#CSVAndExcel
    private boolean fUseLeadingCharsHack = false;
    
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
    
    void export(File folder) throws IOException {
        writeModelAndElements(new File(folder, createElementsFileName()));
        writeRelationships(new File(folder, createRelationsFileName()));
        writeProperties(new File(folder, createPropertiesFileName()));
    }
    
    /**
     * Set the delimiter character.
     * Default is the comma ","
     * @param delimiter
     */
    void setDelimiter(char delimiter) {
        fDelimiter = delimiter;
    }
    
    /**
     * Set the prefix to use on file names. A null value is ignored.
     * @param prefix
     */
    void setFilePrefix(String prefix) {
        if(prefix != null) {
            fFilePrefix = prefix;
        }
    }
    
    void setStripNewLines(boolean set) {
        fStripNewLines = set;
    }
    
    void setUseLeadingCharsHack(boolean set) {
        fUseLeadingCharsHack = set;
    }
    
    /**
     * Write the Model and All Elements
     */
    private void writeModelAndElements(File file) throws IOException {
        Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8"); //$NON-NLS-1$
        
        // Write Header
        String header = createHeader(MODEL_ELEMENTS_HEADER);
        writer.write(header);
        
        // CRLF
        writer.write(CRLF);
        
        // Write Model
        String modelRow = createModelRow();
        writer.write(modelRow);
        
        // Write Elements
        writeElementsInFolder(writer, fModel.getFolder(FolderType.BUSINESS));
        writeElementsInFolder(writer, fModel.getFolder(FolderType.APPLICATION));
        writeElementsInFolder(writer, fModel.getFolder(FolderType.TECHNOLOGY));
        writeElementsInFolder(writer, fModel.getFolder(FolderType.MOTIVATION));
        writeElementsInFolder(writer, fModel.getFolder(FolderType.IMPLEMENTATION_MIGRATION));
        writeElementsInFolder(writer, fModel.getFolder(FolderType.CONNECTORS));
        
        writer.close();
    }
    
    /**
     * Write all elements in a given folder and its child folders to Writer
     */
    private void writeElementsInFolder(Writer writer, IFolder folder) throws IOException {
        if(folder == null) {
            return;
        }
        
        List<IArchimateComponent> components = getComponents(folder);
        sort(components);
        
        for(IArchimateComponent component : components) {
            if(component instanceof IArchimateElement) {
                writer.write(CRLF);
                writer.write(createElementRow((IArchimateElement)component));
            }
        }
    }
    
    /**
     * Write All Relationships
     */
    private void writeRelationships(File file) throws IOException {
        List<IArchimateComponent> components = getComponents(fModel.getFolder(FolderType.RELATIONS));
        sort(components);
        
        // Are there any to write?
        if(!fWriteEmptyFile && components.isEmpty()) {
            return;
        }
        
        Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8"); //$NON-NLS-1$
        
        // Write Header
        String header = createHeader(RELATIONSHIPS_HEADER);
        writer.write(header);
        
        // Write Relationships
        for(IArchimateComponent component : components) {
            if(component instanceof IRelationship) {
                writer.write(CRLF);
                writer.write(createRelationshipRow((IRelationship)component));
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
        
        Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8"); //$NON-NLS-1$
        
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
            if(eObject instanceof IArchimateComponent) {
                IArchimateComponent component = (IArchimateComponent)eObject;
                for(IProperty property : component.getProperties()) {
                    writer.write(CRLF);
                    writer.write(createPropertyRow(component.getId(), property));
                }
            }
        }
        
        writer.close();
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
            if(eObject instanceof IArchimateComponent) {
                IArchimateComponent component = (IArchimateComponent)eObject;
                if(!component.getProperties().isEmpty()) {
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
        
        return sb.toString();
    }

    /**
     * Create a String Row for an Element
     */
    String createElementRow(IArchimateElement element) {
        StringBuffer sb = new StringBuffer();
        
        String id = element.getId();
        sb.append(surroundWithQuotes(id));
        sb.append(fDelimiter);
        
        sb.append(surroundWithQuotes(element.eClass().getName()));
        sb.append(fDelimiter);
        
        String name = normalise(element.getName());
        sb.append(surroundWithQuotes(name));
        sb.append(fDelimiter);
        
        String documentation = normalise(element.getDocumentation());
        sb.append(surroundWithQuotes(documentation));
        
        return sb.toString();
    }
    
    /**
     * Create a String Row for a Relationship
     */
    String createRelationshipRow(IRelationship relationship) {
        StringBuffer sb = new StringBuffer();
        
        String id = relationship.getId();
        sb.append(surroundWithQuotes(id));
        sb.append(fDelimiter);
        
        sb.append(surroundWithQuotes(relationship.eClass().getName()));
        sb.append(fDelimiter);
        
        String name = normalise(relationship.getName());
        sb.append(surroundWithQuotes(name));
        sb.append(fDelimiter);
        
        String documentation = normalise(relationship.getDocumentation());
        sb.append(surroundWithQuotes(documentation));
        sb.append(fDelimiter);
        
        if(relationship.getSource() != null) {
            String sourceID = relationship.getSource().getId();
            sb.append(surroundWithQuotes(sourceID));
        }
        else {
            sb.append("\"\""); //$NON-NLS-1$
        }
        sb.append(fDelimiter);
        
        if(relationship.getTarget() != null) {
            String targetID = relationship.getTarget().getId();
            sb.append(surroundWithQuotes(targetID));
        }
        else {
            sb.append("\"\""); //$NON-NLS-1$
        }
        
        return sb.toString();
    }

    /**
     * Create a String Row for a Property
     */
    String createPropertyRow(String elementID, IProperty property) {
        StringBuffer sb = new StringBuffer();
        
        String id = elementID;
        sb.append(surroundWithQuotes(id));
        sb.append(fDelimiter);
        
        String key = normalise(property.getKey());
        sb.append(surroundWithQuotes(key));
        sb.append(fDelimiter);
        
        String value = normalise(property.getValue());
        sb.append(surroundWithQuotes(value));
        
        return sb.toString();
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
        return "\"" + s + "\""; //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    boolean needsLeadingCharHack(String s) {
        return s != null && fUseLeadingCharsHack && (s.startsWith(" ") || s.startsWith("0"));  //$NON-NLS-1$//$NON-NLS-2$
    }
    
    /**
     * Return a list of all elements/relations in a given folder and its child folders
     */
    private List<IArchimateComponent> getComponents(IFolder folder) {
        List<IArchimateComponent> list = new ArrayList<IArchimateComponent>();
        
        if(folder == null) {
            return list;
        }
        
        for(EObject object : folder.getElements()) {
            if(object instanceof IArchimateComponent) {
                list.add((IArchimateComponent)object);
            }
        }
        
        for(IFolder f : folder.getFolders()) {
            list.addAll(getComponents(f));
        }
        
        return list;
    }

    /**
     * Sort a list of ArchimateElement/Relationship types
     * Sort by class name then element name
     */
    void sort(List<IArchimateComponent> list) {
        if(list == null || list.size() < 2) {
            return;
        }
        
        Collections.sort(list, new Comparator<IArchimateComponent>() {
            @Override
            public int compare(IArchimateComponent o1, IArchimateComponent o2) {
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
}
