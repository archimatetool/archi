/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.model.importer;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.text.rtf.RTFEditorKit;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Text;
import org.jdom.xpath.XPath;

import uk.ac.bolton.archimate.editor.model.DiagramModelUtils;
import uk.ac.bolton.archimate.editor.model.IEditorModelManager;
import uk.ac.bolton.archimate.editor.model.IModelImporter;
import uk.ac.bolton.archimate.editor.ui.ArchimateNames;
import uk.ac.bolton.archimate.editor.ui.ColorFactory;
import uk.ac.bolton.archimate.editor.utils.StringUtils;
import uk.ac.bolton.archimate.model.FolderType;
import uk.ac.bolton.archimate.model.IAccessRelationship;
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IArchimateFactory;
import uk.ac.bolton.archimate.model.IArchimateModel;
import uk.ac.bolton.archimate.model.IArchimateModelElement;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IDiagramModel;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateConnection;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;
import uk.ac.bolton.archimate.model.IDiagramModelConnection;
import uk.ac.bolton.archimate.model.IDiagramModelContainer;
import uk.ac.bolton.archimate.model.IDiagramModelGroup;
import uk.ac.bolton.archimate.model.IDiagramModelObject;
import uk.ac.bolton.archimate.model.IDiagramModelReference;
import uk.ac.bolton.archimate.model.IDocumentable;
import uk.ac.bolton.archimate.model.IFolder;
import uk.ac.bolton.archimate.model.IFontAttribute;
import uk.ac.bolton.archimate.model.IJunctionElement;
import uk.ac.bolton.archimate.model.INameable;
import uk.ac.bolton.archimate.model.IRelationship;
import uk.ac.bolton.jdom.JDOMUtils;


/**
 * BiZZdesign Architect v2 Importer
 * 
 * A rather strange format...
 * 
 * @author Phillip Beauvoir
 */
public class BiZZdesign2Importer implements IModelImporter {
    
    public static final String MM_Application = "MM_Application";
    public static final String MM_Document = "MM_Document";
    public static final String MM_Module = "MM_Module";
    public static final String MM_ProfileValues = "MM_ProfileValues";
    public static final String MM_StorageUnit = "MM_StorageUnit";
    public static final String MM_Value = "MM_Value";
    public static final String MM_Diagram = "MM_Diagram";
    public static final String MM_Compound = "MM_Compound";
    public static final String MM_Node = "MM_Node";
    public static final String MM_Graphics = "MM_Graphics";
    public static final String MM_Rect = "MM_Rect";
    public static final String MM_Colors = "MM_Colors";
    public static final String MM_Color = "MM_Color";
    public static final String MM_RefObjects = "MM_RefObjects";
    public static final String MM_Decorations = "MM_Decorations";
    public static final String MM_Decoration = "MM_Decoration";
    
    public static final String AmberComponent = "AmberComponent";
    public static final String AbstractContainers = "AbstractContainers";
    public static final String Component = "Component";
    public static final String RefObjects = "RefObjects";
    public static final String Relations = "Relations";
    public static final String GroupingScheme = "GroupingScheme";
    public static final String Groupings = "Groupings";
    public static final String Grouping = "Grouping";
    
    public static final String att_name = "name";
    public static final String att_id = "id";
    public static final String att_from = "from";
    public static final String att_to = "to";
    public static final String att_mm_semanticObject = "mm_semanticObject";
    
    protected static Map<String, EClass> fElementTypeMap = new HashMap<String, EClass>();
    static {
        fElementTypeMap.put("Junction", IArchimatePackage.eINSTANCE.getJunction());
        fElementTypeMap.put("AndJunction", IArchimatePackage.eINSTANCE.getAndJunction());
        fElementTypeMap.put("OrJunction", IArchimatePackage.eINSTANCE.getOrJunction());
        
        fElementTypeMap.put("BusinessActivity", IArchimatePackage.eINSTANCE.getBusinessActivity());
        fElementTypeMap.put("BusinessEvent", IArchimatePackage.eINSTANCE.getBusinessEvent());
        fElementTypeMap.put("BusinessInteraction", IArchimatePackage.eINSTANCE.getBusinessInteraction());
        fElementTypeMap.put("BusinessProcess", IArchimatePackage.eINSTANCE.getBusinessProcess());
        fElementTypeMap.put("BusinessActor", IArchimatePackage.eINSTANCE.getBusinessActor());
        fElementTypeMap.put("BusinessInterface", IArchimatePackage.eINSTANCE.getBusinessInterface());
        fElementTypeMap.put("BusinessCollaboration", IArchimatePackage.eINSTANCE.getBusinessCollaboration());
        fElementTypeMap.put("BusinessRole", IArchimatePackage.eINSTANCE.getBusinessRole());
        fElementTypeMap.put("BusinessFunction", IArchimatePackage.eINSTANCE.getBusinessFunction());
        fElementTypeMap.put("BusinessContract", IArchimatePackage.eINSTANCE.getContract());
        fElementTypeMap.put("BusinessProduct", IArchimatePackage.eINSTANCE.getProduct());
        fElementTypeMap.put("BusinessService", IArchimatePackage.eINSTANCE.getBusinessService());
        fElementTypeMap.put("BusinessValue", IArchimatePackage.eINSTANCE.getValue());
        fElementTypeMap.put("BusinessObject", IArchimatePackage.eINSTANCE.getBusinessObject());
        fElementTypeMap.put("BusinessRepresentation", IArchimatePackage.eINSTANCE.getRepresentation());
        fElementTypeMap.put("BusinessMeaning", IArchimatePackage.eINSTANCE.getMeaning());

        fElementTypeMap.put("ApplicationCollaboration", IArchimatePackage.eINSTANCE.getApplicationCollaboration());
        fElementTypeMap.put("ApplicationComponent", IArchimatePackage.eINSTANCE.getApplicationComponent());
        fElementTypeMap.put("ApplicationFunction", IArchimatePackage.eINSTANCE.getApplicationFunction());
        fElementTypeMap.put("ApplicationInteraction", IArchimatePackage.eINSTANCE.getApplicationInteraction());
        fElementTypeMap.put("ApplicationInterface", IArchimatePackage.eINSTANCE.getApplicationInterface());
        fElementTypeMap.put("ApplicationService", IArchimatePackage.eINSTANCE.getApplicationService());
        fElementTypeMap.put("ApplicationDataObject", IArchimatePackage.eINSTANCE.getDataObject());
        
        fElementTypeMap.put("InfrastructureArtifact", IArchimatePackage.eINSTANCE.getArtifact());
        fElementTypeMap.put("InfrastructureCommunicationPath", IArchimatePackage.eINSTANCE.getCommunicationPath());
        fElementTypeMap.put("InfrastructureDevice", IArchimatePackage.eINSTANCE.getDevice());
        fElementTypeMap.put("InfrastructureNode", IArchimatePackage.eINSTANCE.getNode());
        fElementTypeMap.put("InfrastructureInterface", IArchimatePackage.eINSTANCE.getInfrastructureInterface());
        fElementTypeMap.put("InfrastructureNetwork", IArchimatePackage.eINSTANCE.getNetwork());
        fElementTypeMap.put("InfrastructureService", IArchimatePackage.eINSTANCE.getInfrastructureService());
        fElementTypeMap.put("InfrastructureSystemSoftware", IArchimatePackage.eINSTANCE.getSystemSoftware());
    }
    
    protected static Map<String, EClass> fRelationTypeMap = new HashMap<String, EClass>();
    static {
        fRelationTypeMap.put("Access", IArchimatePackage.eINSTANCE.getAccessRelationship());
        fRelationTypeMap.put("Composition", IArchimatePackage.eINSTANCE.getCompositionRelationship());
        fRelationTypeMap.put("Flow", IArchimatePackage.eINSTANCE.getFlowRelationship());
        fRelationTypeMap.put("Aggregation", IArchimatePackage.eINSTANCE.getAggregationRelationship());
        fRelationTypeMap.put("Assignment", IArchimatePackage.eINSTANCE.getAssignmentRelationship());
        fRelationTypeMap.put("Association", IArchimatePackage.eINSTANCE.getAssociationRelationship());
        fRelationTypeMap.put("Realisation", IArchimatePackage.eINSTANCE.getRealisationRelationship());
        fRelationTypeMap.put("Specialization", IArchimatePackage.eINSTANCE.getSpecialisationRelationship());
        fRelationTypeMap.put("Triggering", IArchimatePackage.eINSTANCE.getTriggeringRelationship());
        fRelationTypeMap.put("Use", IArchimatePackage.eINSTANCE.getUsedByRelationship());
    }

    
    private File fFile;
    
    private IArchimateModel fModel;
    
    // Sub-Folders
    private IFolder fBusinessActorsFolder;
    private IFolder fBusinessFunctionsFolder;
    private IFolder fBusinessInformationFolder;
    private IFolder fBusinessProcessesFolder;
    private IFolder fBusinessProductsFolder;
    private IFolder fApplicationApplicationsFolder;
    private IFolder fApplicationDataFolder;
    
    // Lookup tables
    private Map<String, IArchimateElement> fElements = new HashMap<String, IArchimateElement>();
    private Map<String, IRelationship> fRelations = new HashMap<String, IRelationship>();
    private Map<String, IDiagramModel> fViews = new HashMap<String, IDiagramModel>();
    private Map<String, IDiagramModelObject> fDiagramObjects = new HashMap<String, IDiagramModelObject>();
    private Map<String, IDiagramModelConnection> fDiagramConnections = new HashMap<String, IDiagramModelConnection>();
    
    // Temporary table for adding connections
    // This is the wrong way to do it, fix this in v3 Importer
    private Map<IRelationship, String> fDiagramConnectionsTempMap;
    
    private String AMBER_COMPONENT_ID;
    
    public BiZZdesign2Importer(File file) {
        fFile = file;
    }

    @Override
    public void doImport() throws IOException {
        if(fFile == null || !fFile.exists()) {
            return;
        }
        
        Document doc = null;
        
        fModel = IArchimateFactory.eINSTANCE.createArchimateModel();
        fModel.setDefaults();
        fModel.setName("Imported from Architect");
        
        createFolders();
        
        try {
            doc = JDOMUtils.readXMLFile(fFile);
            
            for(Object o : XPath.selectNodes(doc, "/" + MM_Document + "/" + MM_StorageUnit + "/" + MM_Module)) {
                Element eMM_Module = (Element)o;
                String name = eMM_Module.getAttributeValue(att_name);
                // Amber Component
                if(AmberComponent.equals(name)) {
                    parseAmberComponent(eMM_Module);
                }
                else {
                    parseMM_Module(eMM_Module);
                }
            }
            
            IEditorModelManager.INSTANCE.openModel(fModel);
        }
        // Catch any exception
        catch(Exception ex) {
            ex.printStackTrace();
            throw new IOException(ex);
        }
    }

    /**
     * The Model
     */
    private void parseAmberComponent(Element eMM_Module) throws JDOMException {
        AMBER_COMPONENT_ID = "#" + eMM_Module.getAttributeValue(att_id);
        
        Element eComponent = (Element)XPath.selectSingleNode(eMM_Module, Component);
        if(eComponent == null) {
            return;
        }
        
        // Model Name
        setName(fModel, eComponent);
        
        // Elements
        importElements(eComponent);
        
        // Relations
        importRelations(eComponent);
        
        // Views
        importViews(eComponent);
    }
    
    /**
     * Import Elements
     */
    private void importElements(Element eComponent) throws JDOMException {
        for(String tag : fElementTypeMap.keySet()) {
            for(Object o : XPath.selectNodes(eComponent, ".//" + tag)) {
                Element eElement = (Element)o;
                String id = eElement.getAttributeValue(att_id);
                if(StringUtils.isSet(id)) {
                    IArchimateElement eObject = (IArchimateElement)IArchimateFactory.eINSTANCE.create(fElementTypeMap.get(tag));
                    eObject.setId(id);
                    IFolder folder = getDefaultFolderForElement(eObject);
                    if(folder != null) {
                        // Add to folder
                        folder.getElements().add(eObject);
                        // Name
                        setName(eObject, eElement);
                        // Documentation
                        setDocumentation(eObject, eElement);
                        // Map
                        fElements.put(id, eObject);
                    }
                }
            }
        }
    }
    
    /**
     * Import Relations
     */
    private void importRelations(Element eComponent) throws JDOMException {
        for(Object ob : XPath.selectNodes(eComponent, ".//" + Relations)) {
            Element eRelations = (Element)ob;
            for(Object o : XPath.selectNodes(eRelations, ".//*")) {
                Element eElement = (Element)o;
                for(String partialName : fRelationTypeMap.keySet()) {
                    String fullName = partialName + "Relation";
                    String elementName = eElement.getName();
                    
                    if(elementName.equals(fullName) || elementName.endsWith(partialName)) {
                        String id = eElement.getAttributeValue(att_id);
                        String from = eElement.getAttributeValue(att_from);
                        String to = eElement.getAttributeValue(att_to);
                        if(StringUtils.isSet(id)) {
                            EObject fromObject = fElements.get(from);
                            EObject toObject = fElements.get(to);
                            
                            // We don't currently support recursive relations
                            if(fromObject == toObject) {
                                System.out.println("Found recursive relationship: " + partialName + " in " + fromObject);
                                continue;
                            }
                            
                            if(fromObject instanceof IArchimateElement && toObject instanceof IArchimateElement) {
                                IRelationship relation = (IRelationship)IArchimateFactory.eINSTANCE.create(fRelationTypeMap.get(partialName));
                                relation.setId(id);
                                relation.setSource((IArchimateElement)fromObject);
                                relation.setTarget((IArchimateElement)toObject);
                                IFolder folder = getDefaultFolderForElement(relation);
                                if(folder != null) {
                                    // Add to folder
                                    folder.getElements().add(relation);
                                    // Name
                                    setName(relation, eElement);
                                    // Documentation
                                    setDocumentation(relation, eElement);
                                    // Map
                                    fRelations.put(id, relation);
                                }
                                
                                // Check for access type in Access relationship ("r" = read, nothing = write)
                                if(relation instanceof IAccessRelationship) {
                                    Element node = (Element)XPath.selectSingleNode(eElement, MM_ProfileValues + "/" + MM_Value + "[@name='accessType'][@type='AccessRelationType']");
                                    if(node != null) {
                                        String type = node.getValue();
                                        if("r".equals(type)) {
                                            ((IAccessRelationship)relation).setAccessType(IAccessRelationship.READ_ACCESS);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Import Views
     */
    private void importViews(Element eComponent) throws JDOMException {
        Element eAbstractContainers = eComponent.getChild(AbstractContainers);
        if(eAbstractContainers != null) {
            addViews(eAbstractContainers);
            addViewContents(eAbstractContainers);
        }
    }
    
    /**
     * Add Views
     */
    private void addViews(Element eAbstractContainers) throws JDOMException {
        for(Object o : XPath.selectNodes(eAbstractContainers, ".//*")) {
            Element eElement = (Element)o;
            String elementName = eElement.getName();
            if(elementName.endsWith("View")) {
                String id = eElement.getAttributeValue(att_id);
                if(StringUtils.isSet(id)) {
                    // Create Empty View
                    IDiagramModel diagramModel = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
                    diagramModel.setId(id);
                    setName(diagramModel, eElement);
                    fModel.getFolder(FolderType.DIAGRAMS).getElements().add(diagramModel);
                    fViews.put(id, diagramModel);
                }
            }
        }
    }
    
    /**
     * Add Views' Contents
     */
    private void addViewContents(Element eAbstractContainers) throws JDOMException {
        for(Object object : XPath.selectNodes(eAbstractContainers, ".//*")) {
            Element eElement = (Element)object;
            String elementName = eElement.getName();
            if(elementName.endsWith("View")) {
                String id = eElement.getAttributeValue(att_id);
                IDiagramModel diagramModel = fViews.get(id);
                if(diagramModel != null) {
                    // Reset this
                    fDiagramConnectionsTempMap = new HashMap<IRelationship, String>();
                    // Add RefObjects
                    addRefObject(diagramModel, eElement.getChild(RefObjects));
                    // Add GroupingScheme
                    addRefObject(diagramModel, eElement.getChild(GroupingScheme));
                    // Add Diagram Connections
                    addDiagramConnections(diagramModel);
                }
            }
        }
    }
    
    /**
     * Add a Refrenced Object
     */
    private void addRefObject(IDiagramModelContainer diagramModelContainer, Element eRefObject) throws JDOMException {
        if(eRefObject == null) {
            return;
        }
        
        String eName = eRefObject.getName();
        
        // View Ref
        if(eName.endsWith("ViewRef")) {
            addViewRef(diagramModelContainer, eRefObject);
        }
        // Object Ref
        else if(eName.endsWith("Ref")) {
            addDiagramObject(diagramModelContainer, eRefObject);
        }
        // Group
        else if(eName.equals(Grouping)) {
            addDiagramGroup(diagramModelContainer, eRefObject);
        }
        // RefObjects / GroupingScheme / Groupings
        else if(eName.equals(RefObjects) || eName.equals(MM_RefObjects) || eName.equals(GroupingScheme) || eName.equals(Groupings)) {
            for(Object o : eRefObject.getChildren()) {
                addRefObject(diagramModelContainer, (Element)o);
            }
        }
    }

    /**
     * Add Diagram Object
     */
    private void addDiagramObject(IDiagramModelContainer diagramModelContainer, Element eRefObject) throws JDOMException {
        String id = eRefObject.getAttributeValue(att_id);
        String to = eRefObject.getAttributeValue(att_to);
        if(StringUtils.isSet(id) && StringUtils.isSet(to)) {
            // Element
            if(fElements.containsKey(to)) {
                IArchimateElement element = fElements.get(to);
                IDiagramModelArchimateObject dmo = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
                dmo.setId(id);
                dmo.setArchimateElement(element);
                dmo.setBounds(10, 10, -1, -1);
                diagramModelContainer.getChildren().add(dmo);
                fDiagramObjects.put(id, dmo);
                
                // Child objects
                for(Object o : eRefObject.getChildren()) {
                    addRefObject(dmo, (Element)o);
                }
            }
            
            // Relation - store info for later
            else if(fRelations.containsKey(to)) {
                IRelationship r = fRelations.get(to);
                fDiagramConnectionsTempMap.put(r, id);
            }
        }
    }
    
    /**
     * Add a Diagram Group
     */
    private void addDiagramGroup(IDiagramModelContainer diagramModelContainer, Element eRefObject) throws JDOMException {
        String id = eRefObject.getAttributeValue(att_id);
        if(StringUtils.isSet(id)) {
            IDiagramModelGroup group = IArchimateFactory.eINSTANCE.createDiagramModelGroup();
            group.setId(id);
            setName(group, eRefObject);
            group.setBounds(10, 10, -1, -1);
            diagramModelContainer.getChildren().add(group);
            fDiagramObjects.put(id, group);
            
            // Child objects
            for(Object o : eRefObject.getChildren()) {
                addRefObject(group, (Element)o);
            }
        }
    }
    
    /**
     * Add View Ref
     */
    private void addViewRef(IDiagramModelContainer diagramModelContainer, Element eRefObject) {
        String id = eRefObject.getAttributeValue(att_id);
        String to = eRefObject.getAttributeValue(att_to);
        if(StringUtils.isSet(id) && StringUtils.isSet(to)) {
            IDiagramModel dm = fViews.get(to);
            if(dm != null) {
                IDiagramModelReference ref = IArchimateFactory.eINSTANCE.createDiagramModelReference();
                ref.setId(id);
                ref.setReferencedModel(dm);
                ref.setBounds(10, 10, -1, -1);
                diagramModelContainer.getChildren().add(ref);
                fDiagramObjects.put(id, ref);
            }
        }
    }
    
    /**
     * Add Diagram Connections
     * This is not the correct way to do this!
     */
    private void addDiagramConnections(IDiagramModel diagramModel) {
        for(Entry<IRelationship, String> entry : fDiagramConnectionsTempMap.entrySet()) {
            IRelationship relationship = entry.getKey();
            
            List<IDiagramModelArchimateObject> sources = DiagramModelUtils.findDiagramModelObjectsForElement(diagramModel, relationship.getSource());
            List<IDiagramModelArchimateObject> targets = DiagramModelUtils.findDiagramModelObjectsForElement(diagramModel, relationship.getTarget());
            
            if(!sources.isEmpty() && !targets.isEmpty()) {
                IDiagramModelArchimateConnection connection = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
                String id = entry.getValue();
                connection.setId(id);
                connection.setRelationship(relationship);
                connection.connect(sources.get(0), targets.get(0));
                addConnectionLabel(connection);
                fDiagramConnections.put(id, connection);
            }
        }
    }
    
    /**
     * If relationship name != default name set label on connection
     */
    private void addConnectionLabel(IDiagramModelArchimateConnection connection) {
        IRelationship relationship = connection.getRelationship();
        if(relationship.getName() != null && !relationship.getName().equals(ArchimateNames.getDefaultName(relationship.eClass()))) {
            connection.setText(relationship.getName());
        }
    }

    /**
     * Parse an MM_Module section for additional graphical info
     */
    private void parseMM_Module(Element eMM_Module) throws JDOMException {
        Element eMM_Compound = (Element)XPath.selectSingleNode(eMM_Module, MM_Diagram + "/" + MM_Compound + "[@name='mm_canvas']");
        if(eMM_Compound == null) {
            return;
        }
        
        for(Object o : XPath.selectNodes(eMM_Compound, "./" + MM_Graphics + "//*")) {
            Element eElement = (Element)o;
            String id = getIDFromSemanticObjectString(eElement.getAttributeValue(att_mm_semanticObject));
            if(id != null) {
                // Object
                IDiagramModelObject dmo = fDiagramObjects.get(id);
                if(dmo != null) {
                    setBounds(dmo, eElement.getChild(MM_Rect));
                    setFillColor(dmo, (Element)XPath.selectSingleNode(eElement, MM_Colors + "[@name='mm_fillColors']/" + MM_Color));
                    setFont(dmo, (Element)XPath.selectSingleNode(eElement, MM_Decorations + "/" + MM_Decoration + "[mm_concept='label']"));
                }
                // Connection
                else {
                    IDiagramModelConnection connection = fDiagramConnections.get(id);
                    if(connection != null) {
                        setLineColor(connection, (Element)XPath.selectSingleNode(eElement, MM_Color + "[@name='mm_lineColor']"));
                        setFont(connection, (Element)XPath.selectSingleNode(eElement, MM_Decorations + "/" + MM_Decoration + "[mm_concept='label']"));
                    }
                }
            }
        }
    }
    
    /**
     * Set a font
     */
    private void setFont(IFontAttribute fa, Element eMM_Decoration) throws JDOMException {
        if(eMM_Decoration == null) {
            return;
        }
        
        int ratio = 20;
        
        String sFontSize = eMM_Decoration.getAttributeValue("mm_fontSize");
        String sFontMode = eMM_Decoration.getAttributeValue("mm_fontMode");

        if(StringUtils.isSet(sFontSize)) {
            int fontSize = 9;
            String fontName = "Arial";
            int style = 0;

            try {
                fontSize = Integer.parseInt(sFontSize) / ratio;
            }
            catch(NumberFormatException ex) {
                ex.printStackTrace();
            }

            if(StringUtils.isSet(sFontMode)) {
                try {
                    style = Integer.parseInt(sFontMode);
                }
                catch(NumberFormatException ex) {
                    ex.printStackTrace();
                }
            }

            Element eFont = eMM_Decoration.getChild("mm_font");
            if(eFont != null && StringUtils.isSet(eFont.getText())) {
                fontName = eFont.getText();
            }

            FontData fd = new FontData(fontName, fontSize, style);
            fa.setFont(fd.toString());
        }

        // Font color
        setFontColor(fa, (Element)XPath.selectSingleNode(eMM_Decoration, MM_Color + "[@name='mm_lineColor']"));
    }

    /**
     * Set a fill colour
     */
    private void setFillColor(IDiagramModelObject dmo, Element eMM_Color) {
        String rgb = getRGB_String(eMM_Color);
        if(rgb != null) {
            dmo.setFillColor(rgb);
        }
    }
    
    /**
     * Set a line colour
     */
    private void setLineColor(IDiagramModelConnection connection, Element eMM_Color) {
        String rgb = getRGB_String(eMM_Color);
        if(rgb != null) {
            connection.setLineColor(rgb);
        }
    }
    
    /**
     * Set a font colour
     */
    private void setFontColor(IFontAttribute fa, Element eMM_Color) {
        String rgb = getRGB_String(eMM_Color);
        if(rgb != null) {
            fa.setFontColor(rgb);
        }
    }
    
    /**
     * Convert RGB String
     */
    private String getRGB_String(Element eMM_Color) {
        if(eMM_Color == null) {
            return null;
        }
        
        int r = 0, g = 0, b = 0;

        try {
            String s = eMM_Color.getAttributeValue("mm_r");
            if(s != null) {
                r = Integer.parseInt(s);
            }
            s = eMM_Color.getAttributeValue("mm_g");
            if(s != null) {
                g = Integer.parseInt(s);
            }
            s = eMM_Color.getAttributeValue("mm_b");
            if(s != null) {
                b = Integer.parseInt(s);
            }
        }
        catch(NumberFormatException ex) {
            ex.printStackTrace();
            return null;
        }

        return ColorFactory.convertRGBToString(new RGB(r, g, b));
    }

    /**
     * Set the Bounds of an object
     */
    private void setBounds(IDiagramModelObject dmo, Element eMM_Rect) {
        if(eMM_Rect == null) {
            return;
        }
        
        
        int ratio = 3;

        try {
            int x = Integer.parseInt(eMM_Rect.getAttributeValue("x")) / ratio;
            int y = Integer.parseInt(eMM_Rect.getAttributeValue("y")) / ratio;
            int w = Integer.parseInt(eMM_Rect.getAttributeValue("w")) / ratio;
            int h = Integer.parseInt(eMM_Rect.getAttributeValue("h")) / ratio;

            // Compensate for Group's title bar
            if(dmo.eContainer() instanceof IDiagramModelGroup) {
                y -= 25;
            }
            
            // Don't set width and height on certain objects
            if(dmo instanceof IDiagramModelArchimateObject) {
                IArchimateModelElement element = ((IDiagramModelArchimateObject)dmo).getArchimateElement();
                if(element instanceof IJunctionElement) {
                    w = -1;
                    h = -1;
                }
            }

            dmo.setBounds(x, y, w, h);
        }
        catch(NumberFormatException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Return the refernced ID of an object
     */
    private String getIDFromSemanticObjectString(String s) {
        String id = null;
        
        if(s != null && s.startsWith(AMBER_COMPONENT_ID)) {
            int index = s.indexOf("/") + 1;
            id = s.substring(index);
        }
        
        return id;
    }

    /**
     * Set a name on an object
     */
    private void setName(INameable eObject, Element eElement) throws JDOMException {
        Text text = (Text)XPath.selectSingleNode(eElement, MM_ProfileValues + "/" + MM_Value + "[@name='nm']/text()");
        if(text != null) {
            eObject.setName(normaliseName(text.getText()));
        }
        else {
            eObject.setName(ArchimateNames.getDefaultName(eObject.eClass()));
        }
    }
    
    /**
     * Set a documentation on an object
     */
    private void setDocumentation(IDocumentable eObject, Element eElement) throws JDOMException {
        Text text = (Text)XPath.selectSingleNode(eElement, MM_ProfileValues + "/" + MM_Value + "[@name='doc']/text()");
        if(text != null) {
            eObject.setDocumentation(convertRTF(text.getText()));
        }
    }
    
    /**
     * Create Archi's folders in a model
     */
    private void createFolders() {
        IFolder businessFolder = fModel.getFolder(FolderType.BUSINESS);
        
        fBusinessActorsFolder = IArchimateFactory.eINSTANCE.createFolder();
        fBusinessActorsFolder.setName("Actors");
        businessFolder.getFolders().add(fBusinessActorsFolder);
        
        fBusinessFunctionsFolder = IArchimateFactory.eINSTANCE.createFolder();
        fBusinessFunctionsFolder.setName("Functions");
        businessFolder.getFolders().add(fBusinessFunctionsFolder);
        
        fBusinessInformationFolder = IArchimateFactory.eINSTANCE.createFolder();
        fBusinessInformationFolder.setName("Information");
        businessFolder.getFolders().add(fBusinessInformationFolder);
        
        fBusinessProcessesFolder = IArchimateFactory.eINSTANCE.createFolder();
        fBusinessProcessesFolder.setName("Processes");
        businessFolder.getFolders().add(fBusinessProcessesFolder);
        
        fBusinessProductsFolder = IArchimateFactory.eINSTANCE.createFolder();
        fBusinessProductsFolder.setName("Products");
        businessFolder.getFolders().add(fBusinessProductsFolder);
        
        IFolder applicationFolder = fModel.getFolder(FolderType.APPLICATION);
        
        fApplicationApplicationsFolder = IArchimateFactory.eINSTANCE.createFolder();
        fApplicationApplicationsFolder.setName("Applications");
        applicationFolder.getFolders().add(fApplicationApplicationsFolder);
        
        fApplicationDataFolder = IArchimateFactory.eINSTANCE.createFolder();
        fApplicationDataFolder.setName("Data");
        applicationFolder.getFolders().add(fApplicationDataFolder);
    }

    /**
     * Return the default folder for an object
     */
    private IFolder getDefaultFolderForElement(IArchimateElement element) {
        IFolder folder = null;

        switch(element.eClass().getClassifierID()) {
            // Business
            case IArchimatePackage.BUSINESS_ACTOR:
            case IArchimatePackage.BUSINESS_COLLABORATION:
            case IArchimatePackage.BUSINESS_INTERFACE:
            case IArchimatePackage.BUSINESS_ROLE:
                return fBusinessActorsFolder;
            case IArchimatePackage.BUSINESS_FUNCTION:
                return fBusinessFunctionsFolder;
            case IArchimatePackage.BUSINESS_OBJECT:
            case IArchimatePackage.MEANING:
            case IArchimatePackage.REPRESENTATION:
                return fBusinessInformationFolder;
            case IArchimatePackage.BUSINESS_ACTIVITY:
            case IArchimatePackage.BUSINESS_EVENT:
            case IArchimatePackage.BUSINESS_INTERACTION:
            case IArchimatePackage.BUSINESS_PROCESS:
                return fBusinessProcessesFolder;
            case IArchimatePackage.BUSINESS_SERVICE:
            case IArchimatePackage.CONTRACT:
            case IArchimatePackage.PRODUCT:
            case IArchimatePackage.VALUE:
                return fBusinessProductsFolder;
                
            // Application
            case IArchimatePackage.APPLICATION_COLLABORATION:
            case IArchimatePackage.APPLICATION_COMPONENT:
            case IArchimatePackage.APPLICATION_FUNCTION:
            case IArchimatePackage.APPLICATION_INTERACTION:
            case IArchimatePackage.APPLICATION_INTERFACE:
            case IArchimatePackage.APPLICATION_SERVICE:
                return fApplicationApplicationsFolder;
            case IArchimatePackage.DATA_OBJECT:
                return fApplicationDataFolder;
                
            // Technology
            case IArchimatePackage.ARTIFACT:
            case IArchimatePackage.COMMUNICATION_PATH:
            case IArchimatePackage.NETWORK:
            case IArchimatePackage.INFRASTRUCTURE_INTERFACE:
            case IArchimatePackage.INFRASTRUCTURE_SERVICE:
            case IArchimatePackage.NODE:
            case IArchimatePackage.SYSTEM_SOFTWARE:
            case IArchimatePackage.DEVICE:
                break;
                
            // Junctions
            case IArchimatePackage.JUNCTION:
            case IArchimatePackage.AND_JUNCTION:
            case IArchimatePackage.OR_JUNCTION:
                break;
                
            // Relationships
            case IArchimatePackage.ACCESS_RELATIONSHIP:
            case IArchimatePackage.AGGREGATION_RELATIONSHIP:
            case IArchimatePackage.ASSIGNMENT_RELATIONSHIP:
            case IArchimatePackage.ASSOCIATION_RELATIONSHIP:
            case IArchimatePackage.COMPOSITION_RELATIONSHIP:
            case IArchimatePackage.FLOW_RELATIONSHIP:
            case IArchimatePackage.REALISATION_RELATIONSHIP:
            case IArchimatePackage.SPECIALISATION_RELATIONSHIP:
            case IArchimatePackage.TRIGGERING_RELATIONSHIP:
            case IArchimatePackage.USED_BY_RELATIONSHIP:
                break;
        }
        
        folder = fModel.getDefaultFolderForElement(element);
        
        return folder;
    }

    /**
     * Remove stuff from a name
     */
    private String normaliseName(String s) {
        if(s == null) {
            return s;
        }
        
        s = removeApostrophes(s);
        s = s.replaceAll("\r\n", " ");
        s = s.replaceAll("\\\\", ""); // single backslashes

        return s;
    }
    
    /**
     * Remove RTF tags from a documentation
     */
    private String convertRTF(String s) {
        if(s == null) {
            return s;
        }
        
        // Convert RTF to string
        s = removeApostrophes(s);
        s = s.replaceAll("\\\\\\\\", "\\\\"); // double to single backslashes
        
        RTFEditorKit kit = new RTFEditorKit();
        javax.swing.text.Document doc = kit.createDefaultDocument();
        try {
            kit.read(new StringReader(s), doc, 0);
            s = doc.getText(0, doc.getLength());
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        
        return s;
    }
    
    /**
     * Remove outer apostrophes
     */
    private String removeApostrophes(String s) {
        if(s == null) {
            return s;
        }
        
        if(s.charAt(0) == '\'' && s.length() > 1) {
            s = s.substring(1);
        }
        if(s.charAt(s.length() - 1) == '\'' && s.length() > 1) {
            s = s.substring(0, s.length() - 1);
        }
        
        return s;
    }
}
