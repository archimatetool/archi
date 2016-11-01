/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model.compatibility;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;

import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.editor.utils.FileUtils;
import com.archimatetool.editor.utils.ZipUtils;
import com.archimatetool.jdom.JDOMUtils;
import com.archimatetool.model.ModelVersion;

/**
 * Convert ArchiMate 2.x to ArchiMate 3 models
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class Archimate2To3Converter {
    
    static Namespace ARCHI_NAMESPACE = Namespace.getNamespace("http://www.archimatetool.com/archimate");
    
    private Document document;
    
    private Element rootElement;
    
    public Archimate2To3Converter() {
    }
    
    /*
     * Old concept names mapped to new concept names
     */
    static Map<String, String> ConceptNameMapping = new Hashtable<String, String>();
    static {
        ConceptNameMapping.put("archimate:UsedByRelationship", "archimate:ServingRelationship");
        ConceptNameMapping.put("archimate:RealisationRelationship", "archimate:RealizationRelationship");
        ConceptNameMapping.put("archimate:SpecialisationRelationship", "archimate:SpecializationRelationship");
        ConceptNameMapping.put("archimate:AndJunction", "archimate:Junction");
        ConceptNameMapping.put("archimate:OrJunction", "archimate:Junction");
        ConceptNameMapping.put("archimate:CommunicationPath", "archimate:Path");
        ConceptNameMapping.put("archimate:Network", "archimate:DistributionNetwork");
        ConceptNameMapping.put("archimate:InfrastructureInterface", "archimate:TechnologyInterface");
        ConceptNameMapping.put("archimate:InfrastructureFunction", "archimate:TechnologyFunction");
        ConceptNameMapping.put("archimate:InfrastructureService", "archimate:TechnologyService");
    }

    public void convert(File existingFile, File newFile) throws IOException, JDOMException {
        boolean isArchiveFile = IArchiveManager.FACTORY.isArchiveFile(existingFile);
        
        File tmpFolder = new File(System.getProperty("java.io.tmpdir"), "archi-converter");
        if(tmpFolder.exists()) {
            FileUtils.deleteFolder(tmpFolder);
        }
        File tmpFile = new File(tmpFolder, "model.xml");
        
        if(isArchiveFile) {
            ZipUtils.unpackZip(existingFile, tmpFolder);
            document = JDOMUtils.readXMLFile(tmpFile);
        }
        else {
            document = JDOMUtils.readXMLFile(existingFile);
        }
        
        rootElement = document.getRootElement();
        
        // Version number
        setAttributeValue(rootElement, "version", ModelVersion.VERSION);

        // Connectors Folder
        convertConnectorsFolder();
        
        // Derived relations Folder
        moveDerivedRelationsFolderContents();
        
        // Traverse all content
        traverseAllElements(rootElement);
        
        // Write back out
        if(isArchiveFile) {
            JDOMUtils.write2XMLFile(document, tmpFile);
            
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(newFile));
            ZipOutputStream zOut = new ZipOutputStream(out);

            ZipUtils.addFolderToZip(tmpFolder, zOut, null, null);

            zOut.flush();
            zOut.close();
            
            FileUtils.deleteFolder(tmpFolder);
        }
        else {
            JDOMUtils.write2XMLFile(document, newFile);
        }
    }

    /**
     * Convert "Connectors" Folder to "Other"
     */
    private void convertConnectorsFolder() {
        Element eConnectorsFolder = getModelFolder("connectors");
        setAttributeValue(eConnectorsFolder, "type", "other");
        setAttributeValue(eConnectorsFolder, "name", "Other");
    }
    
    /**
     * Remove "Derived" folder and move contents to "Relations" folder
     */
    private void moveDerivedRelationsFolderContents() {
        Element eDerivedFolder = getModelFolder("derived");
        if(eDerivedFolder != null) {
            eDerivedFolder.detach();
            
            Element eRelationsFolder = getModelFolder("relations");
            if(eRelationsFolder != null) {
                List<Element> copy = new ArrayList<>(eDerivedFolder.getChildren());
                for(Element eChild : copy) {
                    eChild.detach();
                    eRelationsFolder.addContent(eChild);
                }
            }
        }
    }
    
    private void traverseAllElements(Element element) {
        // "relationship" type
        convertRelationshipType(element);
        
        // concept type name
        convertConceptTypeName(element);
        
        // move concepts
        moveConceptType(element);

        // Children
        List<Element> copy = new ArrayList<>(element.getChildren());
        for(Element eChild : copy) {
            traverseAllElements(eChild);
        }
    }
    
    /**
     * Convert "relationship" attribute name to "archimateRelationship"
     */
    private void convertRelationshipType(Element element) {
        Attribute attRelationship = element.getAttribute("relationship");
        if(attRelationship != null) {
            attRelationship.setName("archimateRelationship");
        }
    }
    
    /**
     * Convert concept name types
     */
    private void convertConceptTypeName(Element element) {
        Attribute attType = element.getAttribute("type", JDOMUtils.XSI_Namespace);
        
        if(attType != null) {
            String oldValue = attType.getValue();
            String newValue = ConceptNameMapping.get(oldValue);
            
            if(newValue != null) {
                attType.setValue(newValue);
            }
            
            // Or Junction has type set
            if("archimate:OrJunction".equals(oldValue)) {
                element.setAttribute("type", "or");
            }
        }
    }
    
    /**
     * Move concepts to different folders
     */
    private void moveConceptType(Element element) {
        Attribute attType = element.getAttribute("type", JDOMUtils.XSI_Namespace);
        
        if(attType != null) {
            String value = attType.getValue();
        
            // Location
            if("archimate:Location".equals(value)) {
                Element eFolder = getModelFolder("other");
                if(eFolder != null) {
                    element.detach();
                    eFolder.addContent(element);
                }
            }
            
            // Value / Meaning
            if("archimate:Value".equals(value) || "archimate:Meaning".equals(value)) {
                Element eFolder = getModelFolder("motivation");
                if(eFolder != null) {
                    element.detach();
                    eFolder.addContent(element);
                }
            }
        }
    }
    
    private Element getModelFolder(String folderType) {
        for(Element elementFolder : rootElement.getChildren("folder")) {
            Attribute attType = elementFolder.getAttribute("type");
            if(attType != null && folderType.equals(attType.getValue())) {
                return elementFolder;
            }
        }
        
        return null;
    }
    
    private void setAttributeValue(Element element, String attributeName, String newValue) {
        if(element != null) {
            Attribute attribute = element.getAttribute(attributeName);
            if(attribute != null) {
                attribute.setValue(newValue);
            }
        }
    }
}
