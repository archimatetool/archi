/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.bizzdesign;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.text.rtf.RTFEditorKit;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.Text;
import org.jdom.xpath.XPath;

import uk.ac.bolton.archimate.editor.model.IEditorModelManager;
import uk.ac.bolton.archimate.editor.model.IModelImporter;
import uk.ac.bolton.archimate.editor.ui.ArchimateNames;
import uk.ac.bolton.archimate.editor.utils.StringUtils;
import uk.ac.bolton.archimate.model.FolderType;
import uk.ac.bolton.archimate.model.IAccessRelationship;
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IArchimateFactory;
import uk.ac.bolton.archimate.model.IArchimateModel;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IDiagramModel;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;
import uk.ac.bolton.archimate.model.IDiagramModelContainer;
import uk.ac.bolton.archimate.model.IDiagramModelObject;
import uk.ac.bolton.archimate.model.IDocumentable;
import uk.ac.bolton.archimate.model.IFolder;
import uk.ac.bolton.archimate.model.INameable;
import uk.ac.bolton.archimate.model.IRelationship;
import uk.ac.bolton.jdom.JDOMUtils;


/**
 * BiZZdesign Architect v3 Importer
 * 
 * This version file can store more than one model. Each View can reference elements in other models.
 * 
 * @author Phillip Beauvoir
 */
public class BiZZdesign3Importer implements IModelImporter {
    
    public static final Namespace NS_MM_ModelPackage = Namespace.getNamespace("MM_ModelPackage", "http://www.bizzdesign.com/metamodels/MM_ModelPackage");
    public static final Namespace NS_MM_Diagram = Namespace.getNamespace("MM_Diagram", "http://www.bizzdesign.com/metamodels/MM_Diagram");
    public static final Namespace NS_ArchiMate = Namespace.getNamespace("ArchiMate", "http://www.bizzdesign.com/metamodels/ArchiMate");

    public static final String MM_Document = "MM_Document";
    public static final String MM_ModelPackage = NS_MM_ModelPackage.getPrefix() + ":MM_ModelPackage";
    public static final String MM_Models = NS_MM_ModelPackage.getPrefix() + ":MM_Models";
    public static final String ArchiMateMM_Model = NS_ArchiMate.getPrefix() + ":ArchiMateMM_Model";
    public static final String MM_Diagram = NS_MM_Diagram.getPrefix() + ":MM_Diagram";
    
    public static final String MM_ModelPackage_MM_Modules = NS_MM_ModelPackage.getPrefix() + ":MM_Modules";
    public static final String MM_ModelPackage_MM_Module = NS_MM_ModelPackage.getPrefix() + ":MM_Module";
    public static final String ArchiMate_MM_Modules = NS_ArchiMate.getPrefix() + ":MM_Modules";
    public static final String ArchiMate_MM_Module = NS_ArchiMate.getPrefix() + ":MM_Module";
    
    public static final String MM_StorageUnit = "MM_StorageUnit";
    public static final String MM_ProfileValues = "MM_ProfileValues";
    public static final String MM_Value = "MM_Value";
    public static final String ArchiMateComponent = NS_ArchiMate.getPrefix() + ":ArchiMateComponent";
    
    public static final String Relations = NS_ArchiMate.getPrefix() + ":Relations";
    
    public static final String RefObjects = "RefObjects";
    public static final String MM_RefObjects = "MM_RefObjects";
    
    
    public static final String ATT_NAME = "name";
    public static final String ATT_ID = "id";
    public static final String ATT_TYPE = "type";
    public static final String ATT_FROM = "from";
    public static final String ATT_TO = "to";

    private static Map<String, EClass> fElementTypeMap = new HashMap<String, EClass>();
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

    private static Map<String, EClass> fRelationTypeMap = new HashMap<String, EClass>();
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
    
    /**
     * Package Name
     */
    private String fPackageName;
    
    /**
     * Models
     */
    private List<IArchimateModel> fModels = new ArrayList<IArchimateModel>();
    
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
    //private Map<String, IDiagramModelConnection> fDiagramConnections = new HashMap<String, IDiagramModelConnection>();

    // Temporary table for adding connections
    // This is the wrong way to do it, fix this in v3 Importer
    private Map<IRelationship, String> fDiagramConnectionsTempMap;
    

    public BiZZdesign3Importer(File file) {
        fFile = file;
    }

    @Override
    public void doImport() throws IOException {
        if(fFile == null || !fFile.exists()) {
            return;
        }
        
        try {
            Document doc = JDOMUtils.readXMLFile(fFile);
            
            parsePackage(doc);
            
            for(IArchimateModel model : fModels) {
                IEditorModelManager.INSTANCE.openModel(model);
            }
        }
        // Catch any exception
        catch(Exception ex) {
            ex.printStackTrace();
            throw new IOException(ex);
        }
    }
    
    private void parsePackage(Document doc) throws JDOMException {
        // Package
        Element eModelPackage = (Element)XPath.selectSingleNode(doc, "/" + MM_Document + "/" + MM_StorageUnit + "/" + MM_ModelPackage);
        
        if(eModelPackage == null) {
            return;
        }
        
        // Package Name
        Element eName = (Element)XPath.selectSingleNode(eModelPackage, "nm");
        fPackageName = eName == null ? "Architect" : eName.getText();
        
        // Models
        for(Object o : XPath.selectNodes(eModelPackage, MM_Models + "/" + ArchiMateMM_Model)) {
            parseArchiMateMM_Model((Element)o);
        }
    }
    
    /**
     * Parse and add an Archimate model
     */
    private void parseArchiMateMM_Model(Element eArchiMateMM_Model) throws JDOMException {
        IArchimateModel model = createArchimateModel();
        
        // Model Name
        String name = getName(eArchiMateMM_Model);
        model.setName(fPackageName + ":" + (name == null ? "Model" : name));
        
        // MM_Modules
        Element eMM_Modules = getMM_ModulesElement(eArchiMateMM_Model);
        if(eMM_Modules == null) {
            return;
        }

        // ArchimateComponent
        Element eArchiMateComponent = getArchiMateComponentElement(eMM_Modules);
        if(eArchiMateComponent == null) {
            return;
        }
        
        fModels.add(model);
                
        // Elements
        importElements(eArchiMateComponent, model);

        // Relations
        importRelations(eArchiMateComponent, model);
        
        // Views
        importViews(eArchiMateComponent, model);
    }
    
    private Element getMM_ModulesElement(Element eArchiMateMM_Model) throws JDOMException {
        // "Archimate" Model
        Element element = (Element)XPath.selectSingleNode(eArchiMateMM_Model, ArchiMate_MM_Modules);
        
        // "Empty" Type Model
        if(element == null) {
            element = (Element)XPath.selectSingleNode(eArchiMateMM_Model, MM_ModelPackage_MM_Modules);
        }
        
        return element;
    }
    
    private Element getArchiMateComponentElement(Element eMM_Modules) throws JDOMException {
        // "Archimate" Model
        Element element = (Element)XPath.selectSingleNode(eMM_Modules, ArchiMate_MM_Module + "/" + ArchiMateComponent);
        
        // "Empty" Type Model
        if(element == null) {
            element = (Element)XPath.selectSingleNode(eMM_Modules, MM_ModelPackage_MM_Module + "/" + ArchiMateComponent);
        }
        
        return element;
    }
    
    /**
     * Import Elements
     */
    private void importElements(Element eArchiMateComponent, IArchimateModel model) throws JDOMException {
        if(eArchiMateComponent == null) {
            return;
        }
        
        for(String tag : fElementTypeMap.keySet()) {
            for(Object o : XPath.selectNodes(eArchiMateComponent, ".//" + NS_ArchiMate.getPrefix() + ":" + tag)) {
                Element eElement = (Element)o;
                String id = eElement.getAttributeValue(ATT_ID);
                if(StringUtils.isSet(id)) {
                    IArchimateElement eObject = (IArchimateElement)IArchimateFactory.eINSTANCE.create(fElementTypeMap.get(tag));
                    eObject.setId(id);
                    IFolder folder = getDefaultFolderForElement(eObject, model);
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
    private void importRelations(Element eComponent, IArchimateModel model) throws JDOMException {
        for(Object ob : XPath.selectNodes(eComponent, ".//" + Relations)) {
            Element eRelations = (Element)ob;
            for(Object o : XPath.selectNodes(eRelations, ".//*")) {
                Element eElement = (Element)o;
                for(String partialName : fRelationTypeMap.keySet()) {
                    String fullName = partialName + "Relation";
                    String elementName = eElement.getName();
                    
                    if(elementName.equals(fullName) || elementName.endsWith(partialName)) {
                        String id = eElement.getAttributeValue(ATT_ID);
                        String from = eElement.getAttributeValue(ATT_FROM);
                        String to = eElement.getAttributeValue(ATT_TO);
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
                                IFolder folder = getDefaultFolderForElement(relation, model);
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
    private void importViews(Element eArchiMateComponent, IArchimateModel model) throws JDOMException {
        // Add Views first
        for(Object o : XPath.selectNodes(eArchiMateComponent, ".//*")) {
            Element eElement = (Element)o;
            String elementName = eElement.getName();
            if(eElement.getNamespace() == NS_ArchiMate && elementName.endsWith("View")) {
                String id = eElement.getAttributeValue(ATT_ID);
                if(StringUtils.isSet(id)) {
                    // Create Empty View
                    IDiagramModel diagramModel = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
                    diagramModel.setId(id);
                    setName(diagramModel, eElement);
                    model.getFolder(FolderType.DIAGRAMS).getElements().add(diagramModel);
                    fViews.put(id, diagramModel);
                }
            }
        }
        
        // Then References
        for(Object object : XPath.selectNodes(eArchiMateComponent, ".//*")) {
            Element eElement = (Element)object;
            String elementName = eElement.getName();
            if(eElement.getNamespace() == NS_ArchiMate && elementName.endsWith("View")) {
                String id = eElement.getAttributeValue(ATT_ID);
                IDiagramModel diagramModel = fViews.get(id);
                if(diagramModel != null) {
                    // Reset this
                    fDiagramConnectionsTempMap = new HashMap<IRelationship, String>();
                    // Add RefObjects
                    addRefObject(diagramModel, eElement.getChild("RefObjects", NS_ArchiMate));
                    // Add GroupingScheme
                    //addRefObject(diagramModel, eElement.getChild(GroupingScheme));
                    // Add Diagram Connections
                    //addDiagramConnections(diagramModel);
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
            //addViewRef(diagramModelContainer, eRefObject);
        }
        // Object Ref
        else if(eName.endsWith("Ref")) {
            addDiagramObject(diagramModelContainer, eRefObject);
        }
        // Group
//        else if(eName.equals(Grouping)) {
//            addDiagramGroup(diagramModelContainer, eRefObject);
//        }
        // RefObjects / GroupingScheme / Groupings
//      else if(eName.equals(RefObjects) || eName.equals(MM_RefObjects) || eName.equals(GroupingScheme) || eName.equals(Groupings)) {
        else if(eName.equals(RefObjects) || eName.equals(MM_RefObjects)) {
            for(Object o : eRefObject.getChildren()) {
                addRefObject(diagramModelContainer, (Element)o);
            }
        }
    }

    /**
     * Add Diagram Object
     */
    private void addDiagramObject(IDiagramModelContainer diagramModelContainer, Element eRefObject) throws JDOMException {
        String id = eRefObject.getAttributeValue(ATT_ID);
        String to = eRefObject.getAttributeValue(ATT_TO);
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
     * Get a name value for element with XPath MM_ProfileValues + MM_Value where attribute name="nm"
     * Return the name or null
     */
    private String getName(Element eElement) throws JDOMException {
        Text text = (Text)XPath.selectSingleNode(eElement, MM_ProfileValues + "/" + MM_Value + "[@name='nm']/text()");
        return text == null ? null : normaliseName(text.getText());
    }

    /**
     * Set a name on an object
     */
    private void setName(INameable eObject, Element eElement) throws JDOMException {
        String name = getName(eElement);
        if(name != null) {
            eObject.setName(name);
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

    /**
     * Create a blank Archi model
     */
    private IArchimateModel createArchimateModel() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        model.setDefaults();
        model.setName("Imported from Architect");
        
        createFolders(model);
        return model;
    }

    /**
     * Create Archi's folders in a model
     */
    private void createFolders(IArchimateModel model) {
        IFolder businessFolder = model.getFolder(FolderType.BUSINESS);
        
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
        
        IFolder applicationFolder = model.getFolder(FolderType.APPLICATION);
        
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
    private IFolder getDefaultFolderForElement(IArchimateElement element, IArchimateModel model) {
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
        }
        
        // Default
        return model.getDefaultFolderForElement(element);
    }

}
