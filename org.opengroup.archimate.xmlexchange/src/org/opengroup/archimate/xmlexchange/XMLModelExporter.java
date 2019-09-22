/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package org.opengroup.archimate.xmlexchange;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;

import com.archimatetool.editor.model.DiagramModelUtils;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.FontFactory;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.jdom.JDOMUtils;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IAccessRelationship;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IAssociationRelationship;
import com.archimatetool.model.IBounds;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelGroup;
import com.archimatetool.model.IDiagramModelNote;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IDiagramModelReference;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.IFontAttribute;
import com.archimatetool.model.IIdentifier;
import com.archimatetool.model.IInfluenceRelationship;
import com.archimatetool.model.ILineObject;
import com.archimatetool.model.IProperties;
import com.archimatetool.model.IProperty;



/**
 * Export Archi Model to Open Exchange XML Format using JDOM
 * 
 * @author Phillip Beauvoir
 */
public class XMLModelExporter implements IXMLExchangeGlobals {
    
    // ArchiMate model
    private IArchimateModel fModel;
    
    // Properties
    private Map<String, String> fPropertyDefsList;

    /**
     * A map of DC metadata element tags mapped to values
     */
    private Map<String, String> fMetadata;
    
    /**
     * Whether to save organisation of folders
     */
    private boolean fDoSaveOrganisation;
    
    /**
     * Whether to copy XSD files
     */
    private boolean fIncludeXSD;

    /**
     * The language code
     */
    private String fLanguageCode;

    public void exportModel(IArchimateModel model, File outputFile) throws IOException {
        fModel = model;
        
        // JDOM Document
        Document doc = createDocument();
        
        // Root Element
        Element rootElement = createRootElement(doc);

        // Persist model
        writeModel(rootElement);
        
        // Save
        JDOMUtils.write2XMLFile(doc, outputFile);
        
        // XSD
        if(fIncludeXSD) {
            File file1 = new File(outputFile.getParentFile(), XMLExchangePlugin.ARCHIMATE3_MODEL_XSD);
            XMLExchangePlugin.INSTANCE.copyXSDFile(XMLExchangePlugin.ARCHIMATE3_MODEL_XSD, file1);
            
            File file2 = new File(outputFile.getParentFile(), XMLExchangePlugin.ARCHIMATE3_VIEW_XSD);
            XMLExchangePlugin.INSTANCE.copyXSDFile(XMLExchangePlugin.ARCHIMATE3_VIEW_XSD, file2);
            
            File file3 = new File(outputFile.getParentFile(), XMLExchangePlugin.ARCHIMATE3_DIAGRAM_XSD);
            XMLExchangePlugin.INSTANCE.copyXSDFile(XMLExchangePlugin.ARCHIMATE3_DIAGRAM_XSD, file3);
        }
    }
    
    /**
     * Set DC Metadata
     * @param metadata A map of DC metadata element tags mapped to values
     */
    public void setMetadata(Map<String, String> metadata) {
        fMetadata = metadata;
    }
    
    boolean hasMetadata() {
        if(fMetadata != null) {
            for(String value : fMetadata.values()) {
                if(StringUtils.isSet(value)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Set whether to save organisation of folders
     * @param set
     */
    public void setSaveOrganisation(boolean set) {
        fDoSaveOrganisation = set;
    }
    
    /**
     * Set whether to copy XSD files to target
     * @param set
     */
    public void setIncludeXSD(boolean set) {
        fIncludeXSD = set;
    }
    
    /**
     * Set the language code to use
     * @param languageCode
     */
    public void setLanguageCode(String languageCode) {
        fLanguageCode = languageCode;
    }

    /**
     * @return A JDOM Document
     */
    Document createDocument() {
        return new Document(); 
    }
    
    /**
     * @param doc
     * @return The Root JDOM Element
     */
    Element createRootElement(Document doc) {
        Element rootElement = new Element(ELEMENT_MODEL, ARCHIMATE3_NAMESPACE);
        doc.setRootElement(rootElement);

        rootElement.addNamespaceDeclaration(JDOMUtils.XSI_Namespace);
        // rootElement.addNamespaceDeclaration(ARCHIMATE3_NAMESPACE_EMBEDDED); // Don't include this
        
        // DC Namespace
        if(hasMetadata()) {
            rootElement.addNamespaceDeclaration(DC_NAMESPACE);
        }

        /* 
         * Add Schema Location Attribute which is constructed from Target Namespaces and file names of Schemas
         */
        StringBuffer schemaLocationURI = new StringBuffer();
        
        // Archimate Schema Location
        schemaLocationURI.append(rootElement.getNamespace().getURI());
        schemaLocationURI.append(" ");  //$NON-NLS-1$
        schemaLocationURI.append(ARCHIMATE3_SCHEMA_LOCATION);
        
        // DC Schema Location
        if(hasMetadata()) {
            schemaLocationURI.append(" ");  //$NON-NLS-1$
            schemaLocationURI.append(DC_NAMESPACE.getURI());
            schemaLocationURI.append(" ");  //$NON-NLS-1$
            schemaLocationURI.append(DC_SCHEMA_LOCATION);
        }
        
        rootElement.setAttribute(JDOMUtils.XSI_SchemaLocation, schemaLocationURI.toString(), JDOMUtils.XSI_Namespace);

        return rootElement;
    }
    
    /**
     * Write the model
     */
    private void writeModel(Element rootElement) {
        rootElement.setAttribute(ATTRIBUTE_IDENTIFIER, createID(fModel));
        
        // Gather all properties now
        fPropertyDefsList = getAllUniquePropertyKeysForModel();
        
        // Name
        writeTextToElement(fModel.getName(), rootElement, ELEMENT_NAME, true);
        
        // Documentation (Purpose) - optional
        writeTextToElement(fModel.getPurpose(), rootElement, ELEMENT_DOCUMENTATION, false);

        // Model Properties
        writeProperties(fModel, rootElement);
        
        // Metadata
        writeMetadata(rootElement);
        
        // Model Elements
        writeModelElements(rootElement);
        
        // Relationships
        writeModelRelationships(rootElement);
        
        // Organizations
        if(fDoSaveOrganisation) {
            writeOrganizations(rootElement);
        }
        
        // Properties Definitions
        writeModelPropertiesDefinitions(rootElement);
        
        // Views
        writeViews(rootElement);
    }
    
    // ========================================= Metadata ======================================
    
    /**
     * Write any DC Metadata
     */
    Element writeMetadata(Element rootElement) {
        if(!hasMetadata()) {
            return null;
        }
        
        Element mdElement = new Element(ELEMENT_METADATA, ARCHIMATE3_NAMESPACE);
        rootElement.addContent(mdElement);
        
        Element schemaElement = new Element(ELEMENT_SCHEMA, ARCHIMATE3_NAMESPACE);
        schemaElement.setText("Dublin Core"); //$NON-NLS-1$
        mdElement.addContent(schemaElement);
        
        Element schemaVersionElement = new Element(ELEMENT_SCHEMAVERSION, ARCHIMATE3_NAMESPACE);
        schemaVersionElement.setText("1.1"); //$NON-NLS-1$
        mdElement.addContent(schemaVersionElement);
        
        for(Entry<String, String> entry : fMetadata.entrySet()) {
            if(StringUtils.isSet(entry.getKey()) && StringUtils.isSet(entry.getValue())) {
                Element element = new Element(entry.getKey(), DC_NAMESPACE);
                element.setText(entry.getValue());
                mdElement.addContent(element);
            }
        }
        
        return null;
    }
    
    // ========================================= Model Elements ======================================

    /**
     * Write the elements from the layers and extensions
     */
    Element writeModelElements(Element rootElement) {
        Element elementsElement = new Element(ELEMENT_ELEMENTS, ARCHIMATE3_NAMESPACE);
        
        writeModelElementsFolder(fModel.getFolder(FolderType.STRATEGY), elementsElement);
        writeModelElementsFolder(fModel.getFolder(FolderType.BUSINESS), elementsElement);
        writeModelElementsFolder(fModel.getFolder(FolderType.APPLICATION), elementsElement);
        writeModelElementsFolder(fModel.getFolder(FolderType.TECHNOLOGY), elementsElement);
        writeModelElementsFolder(fModel.getFolder(FolderType.MOTIVATION), elementsElement);
        writeModelElementsFolder(fModel.getFolder(FolderType.IMPLEMENTATION_MIGRATION), elementsElement);
        writeModelElementsFolder(fModel.getFolder(FolderType.OTHER), elementsElement);
        
        // If there are elements
        if(!elementsElement.getChildren().isEmpty()) {
            rootElement.addContent(elementsElement);
            return elementsElement;
        }
        
        // No children, so return null
        return null;
    }
    
    /**
     * Write the elements from an Archi folder
     */
    private void writeModelElementsFolder(IFolder folder, Element elementsElement) {
        if(folder == null) {
            return;
        }

        List<EObject> list = new ArrayList<EObject>();
        getElements(folder, list);
        for(EObject eObject : list) {
            if(eObject instanceof IArchimateElement) {
                writeModelElement((IArchimateElement)eObject, elementsElement);
             }
        }
    }
    
    /**
     * Write an element
     */
    Element writeModelElement(IArchimateElement element, Element elementsElement) { 
        Element elementElement = new Element(ELEMENT_ELEMENT, ARCHIMATE3_NAMESPACE);
        elementsElement.addContent(elementElement);
        
        // Identifier
        elementElement.setAttribute(ATTRIBUTE_IDENTIFIER, createID(element));
        
        // Type
        elementElement.setAttribute(ATTRIBUTE_TYPE, XMLTypeMapper.getArchimateConceptName(element), XSI_NAMESPACE);
        
        // Name
        writeTextToElement(element.getName(), elementElement, ELEMENT_NAME, true);
        
        // Documentation - optional
        writeTextToElement(element.getDocumentation(), elementElement, ELEMENT_DOCUMENTATION, false);
        
        // Properties
        writeProperties(element, elementElement);
        
        return elementElement;
    }

    /**
     * Return all elements in an Archi folder and its sub-folders
     */
    private void getElements(IFolder folder, List<EObject> list) {
        if(folder == null) {
            return;
        }
        
        for(EObject object : folder.getElements()) {
            list.add(object);
        }
        
        for(IFolder f : folder.getFolders()) {
            getElements(f, list);
        }
    }
    
    // ========================================= Model Relationships ======================================

    /**
     * Write the relationships
     */
    Element writeModelRelationships(Element rootElement) {
        Element relationshipsElement = new Element(ELEMENT_RELATIONSHIPS, ARCHIMATE3_NAMESPACE);
        
        writeModelRelationshipsFolder(fModel.getFolder(FolderType.RELATIONS), relationshipsElement);
        
        // If there are relationships
        if(!relationshipsElement.getChildren().isEmpty()) {
            rootElement.addContent(relationshipsElement);
            return relationshipsElement;
        }
        
        // No children, so return null
        return null;
    }
    
    /**
     * Write the relationships from an Archi folder
     */
    private void writeModelRelationshipsFolder(IFolder folder, Element relationshipsElement) {
        if(folder == null) {
            return;
        }

        List<EObject> list = new ArrayList<EObject>();
        getElements(folder, list);
        for(EObject eObject : list) {
            if(eObject instanceof IArchimateRelationship) {
                writeModelRelationship((IArchimateRelationship)eObject, relationshipsElement);
             }
        }
    }

    /**
     * Write a relationship
     */
    Element writeModelRelationship(IArchimateRelationship relationship, Element relationshipsElement) { 
        Element relationshipElement = new Element(ELEMENT_RELATIONSHIP, ARCHIMATE3_NAMESPACE);
        relationshipsElement.addContent(relationshipElement);
        
        // Identifier
        relationshipElement.setAttribute(ATTRIBUTE_IDENTIFIER, createID(relationship));
        
        // Source ID
        relationshipElement.setAttribute(ATTRIBUTE_SOURCE, createID(relationship.getSource()));
        
        // Target ID
        relationshipElement.setAttribute(ATTRIBUTE_TARGET, createID(relationship.getTarget()));

        // Type
        relationshipElement.setAttribute(ATTRIBUTE_TYPE, XMLTypeMapper.getArchimateConceptName(relationship), JDOMUtils.XSI_Namespace);
        
        // Influence Modifier/Strength
        if(relationship.eClass() == IArchimatePackage.eINSTANCE.getInfluenceRelationship()) {
            String strength = ((IInfluenceRelationship)relationship).getStrength();
            if(hasSomeText(strength)) {
                relationshipElement.setAttribute(ATTRIBUTE_INFLUENCE_MODIFIER, strength);
            }
        }
        // Access direction
        else if(relationship.eClass() == IArchimatePackage.eINSTANCE.getAccessRelationship()) {
            int accessType = ((IAccessRelationship)relationship).getAccessType();
            switch(accessType) {
                case IAccessRelationship.READ_ACCESS:
                    relationshipElement.setAttribute(ATTRIBUTE_ACCESS_TYPE, ACCESS_TYPE_READ);
                    break;

                case IAccessRelationship.READ_WRITE_ACCESS:
                    relationshipElement.setAttribute(ATTRIBUTE_ACCESS_TYPE, ACCESS_TYPE_READ_WRITE);
                    break;

                case IAccessRelationship.UNSPECIFIED_ACCESS:
                    relationshipElement.setAttribute(ATTRIBUTE_ACCESS_TYPE, ACCESS_TYPE_ACCESS);
                    break;

                default:
                    relationshipElement.setAttribute(ATTRIBUTE_ACCESS_TYPE, ACCESS_TYPE_WRITE);
                    break;
            }
        }
        // Association Directed
        else if(relationship.eClass() == IArchimatePackage.eINSTANCE.getAssociationRelationship()) {
            if(((IAssociationRelationship)relationship).isDirected()) {
                relationshipElement.setAttribute(ATTRIBUTE_ASSOCIATION_DIRECTED, "true"); //$NON-NLS-1$
            }
        }

        // Name - optional
        writeTextToElement(relationship.getName(), relationshipElement, ELEMENT_NAME, false);
        
        // Documentation - optional
        writeTextToElement(relationship.getDocumentation(), relationshipElement, ELEMENT_DOCUMENTATION, false);
        
        // Properties
        writeProperties(relationship, relationshipElement);

        return relationshipElement;
    }
    
    // ========================================= Organizations ======================================

    Element writeOrganizations(Element rootElement) {
        Element organizationsElement = new Element(ELEMENT_ORGANIZATIONS, ARCHIMATE3_NAMESPACE);
        
        for(IFolder folder : fModel.getFolders()) {
            // If the top level folder is not empty
            if(!(folder.getElements().isEmpty() && folder.getFolders().isEmpty())) {
                writeFolder(folder, organizationsElement);
            }
        }
        
        // If there are children
        if(!organizationsElement.getChildren().isEmpty()) {
            rootElement.addContent(organizationsElement);
            return organizationsElement;
        }
        
        // No children, so return null
        return null;
    }
    
    Element writeFolder(IFolder folder, Element parentElement) {
        Element itemElement = new Element(ELEMENT_ITEM, ARCHIMATE3_NAMESPACE);
        parentElement.addContent(itemElement);
        
        // Name
        writeTextToElement(folder.getName(), itemElement, ELEMENT_LABEL, false);
        
        // Documentation
        writeTextToElement(folder.getDocumentation(), itemElement, ELEMENT_DOCUMENTATION, false);

        // Sub-folders
        for(IFolder subFolder : folder.getFolders()) {
            writeFolder(subFolder, itemElement);
        }
        
        // Sub-elements
        for(EObject eObject : folder.getElements()) {
            if(eObject instanceof IIdentifier) {
                // Don't write Sketch or Canvas Views
                if(eObject instanceof IDiagramModel && !(eObject instanceof IArchimateDiagramModel)) {
                    continue;
                }
                
                IIdentifier component = (IIdentifier)eObject;
                Element itemChildElement = new Element(ELEMENT_ITEM, ARCHIMATE3_NAMESPACE);
                itemElement.addContent(itemChildElement);
                itemChildElement.setAttribute(ATTRIBUTE_IDENTIFIERREF, createID(component));
            }
        }
        
        return itemElement;
    }
    
    // ========================================= Properties ======================================

    Element writeModelPropertiesDefinitions(Element rootElement) {
        if(fPropertyDefsList.isEmpty()) {
            return null;
        }
        
        Element propertiesDefinitionsElement = new Element(ELEMENT_PROPERTYDEFINITIONS, ARCHIMATE3_NAMESPACE);
        rootElement.addContent(propertiesDefinitionsElement);

        for(Entry<String, String> entry : fPropertyDefsList.entrySet()) {
            Element propertyDefElement = new Element(ELEMENT_PROPERTYDEFINITION, ARCHIMATE3_NAMESPACE);
            propertiesDefinitionsElement.addContent(propertyDefElement);
            
            propertyDefElement.setAttribute(ATTRIBUTE_IDENTIFIER, entry.getValue());
            propertyDefElement.setAttribute(ATTRIBUTE_TYPE, "string"); //$NON-NLS-1$
            
            Element propertyNameElement = new Element(ELEMENT_NAME, ARCHIMATE3_NAMESPACE);
            propertyNameElement.setText(entry.getKey());
            propertyDefElement.addContent(propertyNameElement);
        }
        
        return propertiesDefinitionsElement;
    }
    
    /**
     * @return All unique property types in the model
     */
    Map<String, String> getAllUniquePropertyKeysForModel() {
        Map<String, String> list = new TreeMap<String, String>();
        
        String id = "propid-"; //$NON-NLS-1$
        int idCount = 1;
        
        for(Iterator<EObject> iter = fModel.eAllContents(); iter.hasNext();) {
            EObject element = iter.next();
            if(element instanceof IProperty) {
                String name = ((IProperty)element).getKey();
                if(name != null && !list.containsKey(name)) {
                    list.put(name, id + (idCount++));
                }
            }
        }
        
        return list;
    }
    
    /**
     * Write all property values for a given element
     * @param properties
     * @param parentElement
     * @return The Element or null
     */
    Element writeProperties(IProperties properties, Element parentElement) {
        Element propertiesElement = new Element(ELEMENT_PROPERTIES, ARCHIMATE3_NAMESPACE);
        
        for(IProperty property : properties.getProperties()) {
            String name = property.getKey();
            String value = property.getValue();
            if(hasSomeText(name)) {
                String propertyRefID = fPropertyDefsList.get(name);
                if(propertyRefID != null) {
                    writePropertyValue(propertiesElement, propertyRefID, value);
                }
            }
        }
        
        if(propertiesElement.getChildren().size() > 0) {
            parentElement.addContent(propertiesElement);
        }
        
        return propertiesElement;
    }
    
    /**
     * Write a Property value referencing a property ref id
     */
    Element writePropertyValue(Element propertiesElement, String propertyRefID, String propertyValue) {
        Element propertyElement = new Element(ELEMENT_PROPERTY, ARCHIMATE3_NAMESPACE);
        propertiesElement.addContent(propertyElement);
        propertyElement.setAttribute(ATTRIBUTE_PROPERTY_IDENTIFIERREF, propertyRefID);
        
        Element valueElement = new Element(ELEMENT_VALUE, ARCHIMATE3_NAMESPACE);
        propertyElement.addContent(valueElement);
        writeElementTextWithLanguageCode(valueElement, propertyValue);

        return propertyElement;
    }
    
    // ========================================= Views ======================================
    
    /**
     * The negative offset for the current diagram.
     * The exchange format diagram starts at origin 0,0 with no negative coordinates allowed.
     * Archi diagram nodes can have negative coordinates, so this is the offset to apply to nodes and bendpoints.
     * We calculate it once for each diagram.
     */
    private Point fCurrentDiagramNegativeOffset;
    
    Element writeViews(Element rootElement) {
        // Do we have any views?
        EList<IDiagramModel> views = fModel.getDiagramModels();
        if(views.isEmpty()) {
            return null;
        }
        
        Element viewsElement = new Element(ELEMENT_VIEWS, ARCHIMATE3_NAMESPACE);
        rootElement.addContent(viewsElement);
        
        Element diagramsElement = new Element(ELEMENT_DIAGRAMS, ARCHIMATE3_NAMESPACE);
        viewsElement.addContent(diagramsElement);
        
        for(IDiagramModel dm : views) {
            if(dm instanceof IArchimateDiagramModel) {
                // Calculate negative offset for this diagram
                fCurrentDiagramNegativeOffset = XMLExchangeUtils.getNegativeOffsetForDiagram(dm);
                
                writeView((IArchimateDiagramModel)dm, diagramsElement);
            }
        }
        
        return viewsElement;
    }
    
    Element writeView(IArchimateDiagramModel dm, Element viewsElement) {
        Element viewElement = new Element(ELEMENT_VIEW, ARCHIMATE3_NAMESPACE);
        viewsElement.addContent(viewElement);

        // Identifier
        viewElement.setAttribute(ATTRIBUTE_IDENTIFIER, createID(dm));
        
        // Type
        viewElement.setAttribute(ATTRIBUTE_TYPE, ATTRIBUTE_DIAGRAM_TYPE, XSI_NAMESPACE);

        // Viewpoint
        String viewPointName = XMLTypeMapper.getViewpointName(dm.getViewpoint());
        if(StringUtils.isSet(viewPointName)) {
            viewElement.setAttribute(ATTRIBUTE_VIEWPOINT, viewPointName);
        }

        // Name
        writeTextToElement(dm.getName(), viewElement, ELEMENT_NAME, true);
        
        // Documentation
        writeTextToElement(dm.getDocumentation(), viewElement, ELEMENT_DOCUMENTATION, false);

        // Properties
        writeProperties(dm, viewElement);
        
        // Nodes
        writeNodes(dm, viewElement);
        
        // Connections
        writeConnections(dm, viewElement);
        
        return viewElement;
    }
    
    // ========================================= Nodes ======================================
    
    /**
     * Write all diagram nodes
     */
    void writeNodes(IDiagramModel dm, Element viewElement) {
        for(IDiagramModelObject child : dm.getChildren()) {
            writeNode(child, viewElement);
        }
    }
    
    /**
     * Write a diagram node
     */
    void writeNode(IDiagramModelObject dmo, Element parentElement) {
        if(dmo instanceof IDiagramModelArchimateObject) {
            writeArchimateNode((IDiagramModelArchimateObject)dmo, parentElement);
        }
        // Group
        else if(dmo instanceof IDiagramModelGroup) {
            writeGroupNode((IDiagramModelGroup)dmo, parentElement);
        }
        // Note
        else if(dmo instanceof IDiagramModelNote) {
            writeNoteNode((IDiagramModelNote)dmo, parentElement);
        }
        // View Reference type
        else if(dmo instanceof IDiagramModelReference) {
            writeViewReferenceNode((IDiagramModelReference)dmo, parentElement);
        }
    }
    
    /**
     * Write an ArchiMate node
     */
    Element writeArchimateNode(IDiagramModelArchimateObject dmo, Element parentElement) {
        Element nodeElement = new Element(ELEMENT_NODE, ARCHIMATE3_NAMESPACE);
        parentElement.addContent(nodeElement);
        
        // ID
        nodeElement.setAttribute(ATTRIBUTE_IDENTIFIER, createID(dmo));
        
        // Element Ref
        IArchimateElement element = dmo.getArchimateElement();
        nodeElement.setAttribute(ATTRIBUTE_ELEMENTREF, createID(element));
        
        // Type
        nodeElement.setAttribute(ATTRIBUTE_TYPE, ATTRIBUTE_ELEMENT_TYPE, XSI_NAMESPACE);
        
        // Bounds
        writeAbsoluteBounds(dmo, nodeElement);
        
        // Style
        writeNodeStyle(dmo, nodeElement);

        // Children
        for(IDiagramModelObject child : dmo.getChildren()) {
            writeNode(child, nodeElement);
        }
        
        return nodeElement;
    }
    
    /**
     * Write a Group node
     */
    Element writeGroupNode(IDiagramModelGroup group, Element parentElement) {
        Element nodeElement = new Element(ELEMENT_NODE, ARCHIMATE3_NAMESPACE);
        parentElement.addContent(nodeElement);
        
        // ID
        nodeElement.setAttribute(ATTRIBUTE_IDENTIFIER, createID(group));

        // Bounds
        writeAbsoluteBounds(group, nodeElement);
        
        // Type
        nodeElement.setAttribute(ATTRIBUTE_TYPE, ATTRIBUTE_CONTAINER_TYPE, XSI_NAMESPACE);
        
        // Label
        writeTextToElement(group.getName(), nodeElement, ELEMENT_LABEL, false);
        
        // Documentation
        writeTextToElement(group.getDocumentation(), nodeElement, ELEMENT_DOCUMENTATION, false);

        // Style
        writeNodeStyle(group, nodeElement);
        
        // Children
        for(IDiagramModelObject child : group.getChildren()) {
            writeNode(child, nodeElement);
        }
        
        return nodeElement;
    }
    
    /**
     * Write a Note node
     */
    Element writeNoteNode(IDiagramModelNote note, Element parentElement) {
        Element nodeElement = new Element(ELEMENT_NODE, ARCHIMATE3_NAMESPACE);
        parentElement.addContent(nodeElement);
        
        // ID
        nodeElement.setAttribute(ATTRIBUTE_IDENTIFIER, createID(note));
        
        // Type
        nodeElement.setAttribute(ATTRIBUTE_TYPE, ATTRIBUTE_LABEL_TYPE, XSI_NAMESPACE);

        // Bounds
        writeAbsoluteBounds(note, nodeElement);
        
        // Text
        writeTextToElement(note.getContent(), nodeElement, ELEMENT_LABEL, false);
        
        // Style
        writeNodeStyle(note, nodeElement);
        
        return nodeElement;
    }

    /**
     * Write a View Reference node
     */
    Element writeViewReferenceNode(IDiagramModelReference ref, Element parentElement) {
        Element nodeElement = new Element(ELEMENT_NODE, ARCHIMATE3_NAMESPACE);
        parentElement.addContent(nodeElement);
        
        // ID
        nodeElement.setAttribute(ATTRIBUTE_IDENTIFIER, createID(ref));
        
        // Type
        nodeElement.setAttribute(ATTRIBUTE_TYPE, ATTRIBUTE_LABEL_TYPE, XSI_NAMESPACE);

        // Bounds
        writeAbsoluteBounds(ref, nodeElement);
        
        // Text
        writeTextToElement(ref.getName(), nodeElement, ELEMENT_LABEL, false);
        
        // Style
        writeNodeStyle(ref, nodeElement);
        
        // View Ref
        // Only write view references to ArchiMate diagrams
        // If the view ref is sketch or canvas this will instead appear as a note
        if(ref.getReferencedModel() instanceof IArchimateDiagramModel) {
            Element viewRefElement = new Element(ELEMENT_VIEWREF, ARCHIMATE3_NAMESPACE);
            viewRefElement.setAttribute(ATTRIBUTE_REF, createID(ref.getReferencedModel()));
            nodeElement.addContent(viewRefElement);
        }
        
        return nodeElement;
    }
    
    /**
     * Write a node style
     */
    Element writeNodeStyle(IDiagramModelObject dmo, Element nodeElement) {
        Element styleElement = new Element(ELEMENT_STYLE, ARCHIMATE3_NAMESPACE);
        nodeElement.addContent(styleElement);
        
        // Fill Color
        writeFillColor(dmo, styleElement);
        
        // Line color
        writeLineColor(dmo, styleElement);

        // Font
        writeFont(dmo, styleElement);
        
        return styleElement;
    }
    
    /**
     * Write fill colour of a diagram object
     */
    Element writeFillColor(IDiagramModelObject dmo, Element parentElement) {
        Element fillColorElement = null;
        
        RGB rgb = ColorFactory.convertStringToRGB(dmo.getFillColor());
        if(rgb == null) {
            Color color = ColorFactory.getDefaultFillColor(dmo);
            if(color != null) {
                rgb = color.getRGB();
            }
        }
        
        if(rgb != null) {
            fillColorElement = new Element(ELEMENT_FILLCOLOR, ARCHIMATE3_NAMESPACE);
            parentElement.addContent(fillColorElement);
            writeRGBAttributes(rgb, dmo.getAlpha(), fillColorElement);
        }
        
        return fillColorElement;
    }
    
    // ========================================= Connections ======================================
    
    /**
     * Write all connections
     */
    void writeConnections(IDiagramModel dm, Element parentElement) {
        for(Iterator<EObject> iter = dm.eAllContents(); iter.hasNext();) {
            EObject eObject = iter.next();
            // ArchiMate connection
            if(eObject instanceof IDiagramModelArchimateConnection) {
                // If it's nested don't write a connection
                if(!isNestedConnection((IDiagramModelArchimateConnection)eObject)) {
                    writeConnection((IDiagramModelConnection)eObject, parentElement);
                }
            }
            // Other connection
            else if(eObject instanceof IDiagramModelConnection) {
                writeConnection((IDiagramModelConnection)eObject, parentElement);
            }
        }
    }
    
    /**
     * Check whether this is a nested connection - assume all nested connections should be hidden
     */
    boolean isNestedConnection(IDiagramModelArchimateConnection connection) {
        if(connection.getSource() instanceof IDiagramModelArchimateObject && connection.getTarget() instanceof IDiagramModelArchimateObject) {
            IDiagramModelArchimateObject src = (IDiagramModelArchimateObject)connection.getSource();
            IDiagramModelArchimateObject tgt = (IDiagramModelArchimateObject)connection.getTarget();
            return src.getChildren().contains(tgt) || tgt.getChildren().contains(src);
        }
        return false;
    }
    
    /**
     * Write a connection
     */
    Element writeConnection(IDiagramModelConnection connection, Element parentElement) {
        Element connectionElement = new Element(ELEMENT_CONNECTION, ARCHIMATE3_NAMESPACE);
        parentElement.addContent(connectionElement);
        
        // ID
        connectionElement.setAttribute(ATTRIBUTE_IDENTIFIER, createID(connection));

        // ArchiMate connection has a Relationship ref
        if(connection instanceof IDiagramModelArchimateConnection) {
            connectionElement.setAttribute(ATTRIBUTE_RELATIONSHIPREF, createID(((IDiagramModelArchimateConnection)connection).getArchimateRelationship()));
            // Type
            connectionElement.setAttribute(ATTRIBUTE_TYPE, ATTRIBUTE_RELATIONSHIP_TYPE, XSI_NAMESPACE);
        }
        else {
            // Type
            connectionElement.setAttribute(ATTRIBUTE_TYPE, ATTRIBUTE_LINE_TYPE, XSI_NAMESPACE);
        }
        
        // Source
        connectionElement.setAttribute(ATTRIBUTE_SOURCE, createID(connection.getSource()));
        
        // Target
        connectionElement.setAttribute(ATTRIBUTE_TARGET, createID(connection.getTarget()));
        
        // Style
        writeConnectionStyle(connection, connectionElement);

        // Bendpoints
        writeConnectionBendpoints(connection, connectionElement);
        
        return connectionElement;
    }
    
    /**
     * Write connection bendpoints
     */
    void writeConnectionBendpoints(IDiagramModelConnection connection, Element connectionElement) {
        // TODO: Doesn't work for connection->connection
        if(connection.getSource() instanceof IDiagramModelConnection || connection.getTarget() instanceof IDiagramModelConnection) {
            return;
        }
        
        List<Point> points = DiagramModelUtils.getAbsoluteBendpointPositions(connection);
        
        for(Point pt : points) {
            Element bendpointElement = new Element(ELEMENT_BENDPOINT, ARCHIMATE3_NAMESPACE);
            connectionElement.addContent(bendpointElement);
        
            pt.x -= fCurrentDiagramNegativeOffset.x; // compensate for negative space
            pt.y -= fCurrentDiagramNegativeOffset.y; // compensate for negative space
            
            bendpointElement.setAttribute(ATTRIBUTE_X, Integer.toString(pt.x));
            bendpointElement.setAttribute(ATTRIBUTE_Y, Integer.toString(pt.y));
        }
    }
    
    /**
     * Write a connection style
     */
    Element writeConnectionStyle(IDiagramModelConnection connection, Element parentElement) {
        Element styleElement = new Element(ELEMENT_STYLE, ARCHIMATE3_NAMESPACE);
        parentElement.addContent(styleElement);
        
        // Line Width
        int lineWidth = connection.getLineWidth();
        if(lineWidth != 1) {
            styleElement.setAttribute(ATTRIBUTE_LINEWIDTH, Integer.toString(lineWidth));
        }
        
        // Line color
        writeLineColor(connection, styleElement);
        
        // Font
        writeFont(connection, styleElement);

        return styleElement;
    }

    // ========================================= Helpers ======================================
    
    /**
     * Write line colour of a diagram object
     */
    Element writeLineColor(ILineObject lineObject, Element parentElement) {
        Element lineColorElement = null;
        
        RGB rgb = ColorFactory.convertStringToRGB(lineObject.getLineColor());
        if(rgb == null) {
            Color color = ColorFactory.getDefaultLineColor(lineObject);
            if(color != null) {
                rgb = color.getRGB();
            }
        }
        
        if(rgb != null) {
            lineColorElement = new Element(ELEMENT_LINECOLOR, ARCHIMATE3_NAMESPACE);
            parentElement.addContent(lineColorElement);
            // Use outline alpha if a diagram model object
            if(lineObject instanceof IDiagramModelObject) {
                writeRGBAttributes(rgb, ((IDiagramModelObject)lineObject).getLineAlpha(), lineColorElement);
            }
            else {
                writeRGBAttributes(rgb, -1, lineColorElement);
            }
        }
        
        return lineColorElement;
    }

    /**
     * Write font of a diagram component
     */
    Element writeFont(IFontAttribute fontObject, Element styleElement) {
        Element fontElement = new Element(ELEMENT_FONT, ARCHIMATE3_NAMESPACE);
        
        try {
            FontData fontData = null;
            
            String fontString = fontObject.getFont();
            if(fontString != null) {
                fontData = new FontData(fontString);
            }
            else {
                fontData = FontFactory.getDefaultUserViewFontData();
            }
            
            fontElement.setAttribute(ATTRIBUTE_FONTNAME, fontData.getName());
            fontElement.setAttribute(ATTRIBUTE_FONTSIZE, Integer.toString(fontData.getHeight()));

            int style = fontData.getStyle();
            String styleString = ""; //$NON-NLS-1$

            if((style & SWT.BOLD) == SWT.BOLD) {
                styleString += "bold"; //$NON-NLS-1$
            }
            if((style & SWT.ITALIC) == SWT.ITALIC) {
                if(StringUtils.isSet(styleString)) {
                    styleString += " "; //$NON-NLS-1$
                }
                styleString += "italic"; //$NON-NLS-1$
            }

            if(hasSomeText(styleString)) {
                fontElement.setAttribute(ATTRIBUTE_FONTSTYLE, styleString);
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        
        // Font color
        String fontColorString = fontObject.getFontColor();
        RGB rgb = ColorFactory.convertStringToRGB(fontColorString);
        Element fontColorElement = new Element(ELEMENT_FONTCOLOR, ARCHIMATE3_NAMESPACE);
        fontElement.addContent(fontColorElement);
        writeRGBAttributes(rgb, -1, fontColorElement);
        
        if(hasElementContent(fontElement)) {
            styleElement.addContent(fontElement);
        }

        return fontElement;
    }
    
    /**
     * Write RGB attribute on an Element 
     */
    void writeRGBAttributes(RGB rgb, int alpha, Element colorElement) {
        if(rgb == null) {
            rgb = new RGB(0, 0, 0);
        }
        
        colorElement.setAttribute(ATTRIBUTE_R, Integer.toString(rgb.red));
        colorElement.setAttribute(ATTRIBUTE_G, Integer.toString(rgb.green));
        colorElement.setAttribute(ATTRIBUTE_B, Integer.toString(rgb.blue));
        
        if(alpha != -1) {
            int newValue = Math.round(((float)alpha / 255) * 100);
            colorElement.setAttribute(ATTRIBUTE_A, Integer.toString(newValue));
        }
    }

    /**
     * Write absolute bounds of a diagram object
     */
    void writeAbsoluteBounds(IDiagramModelObject dmo, Element element) {
        IBounds bounds = DiagramModelUtils.getAbsoluteBounds(dmo);
        
        int x = bounds.getX() - fCurrentDiagramNegativeOffset.x; // compensate for negative space
        int y = bounds.getY() - fCurrentDiagramNegativeOffset.y; // compensate for negative space
        
        element.setAttribute(ATTRIBUTE_X, Integer.toString(x));
        element.setAttribute(ATTRIBUTE_Y, Integer.toString(y));
        element.setAttribute(ATTRIBUTE_WIDTH, Integer.toString(bounds.getWidth()));
        element.setAttribute(ATTRIBUTE_HEIGHT, Integer.toString(bounds.getHeight()));
    }

    /**
     * Write some text to a given JDOM Element.
     * If mandatory write at least an empty tag
     */
    Element writeTextToElement(String text, Element parentElement, String childElementName, boolean mandatory) {
        if(text == null) {
            text = ""; //$NON-NLS-1$
        }
        
        Element element = null;
        
        if(mandatory || hasSomeText(text)) {
            element = new Element(childElementName, ARCHIMATE3_NAMESPACE);
            parentElement.addContent(element);
            writeElementTextWithLanguageCode(element, text);
        }
        
        return element;
    }

    private void writeElementTextWithLanguageCode(Element element, String text) {
        element.setText(text);
        
        if(fLanguageCode != null) {
            element.setAttribute(ATTRIBUTE_LANG, fLanguageCode, Namespace.XML_NAMESPACE);
        }
    }

    /**
     * Return true if string has at least some text
     */
    private boolean hasSomeText(String string) {
        return string != null && !string.isEmpty();
    }
    
    /**
     * @return true if element has attributes or a child element
     */
    private boolean hasElementContent(Element element) {
        return element != null && (element.hasAttributes() || !element.getChildren().isEmpty());
    }

    /**
     * Create a uniform id
     */
    private String createID(IIdentifier identifier) {
        if(identifier.getId() != null && identifier.getId().startsWith("id-")) { //$NON-NLS-1$
            return identifier.getId();
        }
        return "id-" + identifier.getId(); //$NON-NLS-1$
    }
}
