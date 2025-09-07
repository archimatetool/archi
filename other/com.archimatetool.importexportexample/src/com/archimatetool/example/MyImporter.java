/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.example;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;

import com.archimatetool.editor.model.DiagramModelUtils;
import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.editor.model.IModelImporter;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateComponent;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IFolder;



/**
 * Example Model Importer
 * Shows basic import routines.
 * You will need to read in the file somehow and model it in JDOM or DOM Sax parser or some representation and map
 * this to Archi elements.
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class MyImporter implements IModelImporter {

    private static final String MY_EXTENSION_WILDCARD = "*.mex";
    
    // ID -> Object lookup table
    private Map<String, EObject> idLookup;
    
    // Representation of an Element
    private record Element(String type, String name, String id) {}

    // Representation of a Relationship
    private record Relationship(String type, String name, String id, String sourceId, String targetId) {}
    
    // Representation of a View
    private record View(String name, String id) {}

    // Representation of a View element
    private record ViewElement(String viewId, String elementId, int x, int y, int width, int height) {}

    // Representation of a View connection
    private record ViewConnection(String viewId, String relationshipId) {}

    @Override
    public void doImport() throws IOException {
        // Ask to open the file
        File file = askOpenFile();
        if(file == null) {
            return;
        }
        
        // Load in the file and get its information here.
        // Assuming you load in the data in some way, perhaps with JDOM, or a SAX Parser ot text reader then you will
        // have a representation of it in Java classes that you need to map to Archi elements, relationships and Views.
        
        // Here is some example raw data...
        
        // Elements
        List<Element> elements = List.of(
                new Element("BusinessActor", "Actor", "elementID1"),
                new Element("BusinessRole", "Client", "elementID2"),
                new Element("BusinessFunction", "My Function", "elementID3"));
        
        
        // Relationships
        List<Relationship> relationships = List.of(
                new Relationship("AssignmentRelationship", "Assigned to", "relID1", "elementID1", "elementID2"),
                new Relationship("ServingRelationship", "", "relID2", "elementID1", "elementID3"),
                new Relationship("AssociationRelationship", "", "relID3", "elementID2", "elementID3"));
        
        // Views
        List<View> views = List.of(
                new View("A View", "view1"),
                new View("Another View", "view2"));
        
        // View elements
        List<ViewElement> viewElements = List.of(
                new ViewElement("view1", "elementID1", 10, 10, -1, -1),
                new ViewElement("view1", "elementID2", 310, 10, -1, -1),
                new ViewElement("view1", "elementID3", 310, 110, -1, -1),
                new ViewElement("view2", "elementID2", 10, 10, -1, -1),
                new ViewElement("view2", "elementID3", 10, 110, -1, -1));
        
        // View connections
        List<ViewConnection> viewConnections = List.of(
                new ViewConnection("view1", "relID1"),
                new ViewConnection("view1", "relID2"),
                new ViewConnection("view2", "relID3"));
        
        // Create the model from this data...
        
        // Create a new Archimate Model and set defaults
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        model.setDefaults();
        model.setName("My Model");
        
        // Create and add elements matching the data above
        // If an ID is not provided for an element then a unique ID will be generated when the model element is added to a parent
        // model element, otherwise you can use your own IDs provided in the input data.
        
        // Let's use an ID -> EObject mapping table for convenience
        idLookup = new HashMap<String, EObject>();
        
        // Create and add model elements
        for(Element e : elements) {
            createAndAddArchimateElement(model, (EClass)IArchimatePackage.eINSTANCE.getEClassifier(e.type), e.name, e.id);
        }
        
        // Create and add model relationships and set source and target elements
        for(Relationship r : relationships) {
            IArchimateRelationship relationship = createAndAddArchimateRelationship(model, (EClass)IArchimatePackage.eINSTANCE.getEClassifier(r.type), r.name, r.id);
            
            // Find source and target elements from their IDs in the lookup table and set them
            IArchimateElement source = (IArchimateElement)idLookup.get(r.sourceId);
            IArchimateElement target = (IArchimateElement)idLookup.get(r.targetId);
            relationship.setSource(source);
            relationship.setTarget(target);
        }
        
        // Create and add diagram views
        for(View v : views) {
            createAndAddView(model, v.name, v.id);
        }

        // Add diagram elements to views
        for(ViewElement ve : viewElements) {
            IDiagramModel diagramModel = (IDiagramModel)idLookup.get(ve.viewId);
            IArchimateElement element = (IArchimateElement)idLookup.get(ve.elementId);
            createAndAddElementToView(diagramModel, element, ve.x, ve.y, ve.width, ve.height);
        }
        
        // Add diagram connections to views
        for(ViewConnection vc : viewConnections) {
            IDiagramModel diagramModel = (IDiagramModel)idLookup.get(vc.viewId);
            IArchimateRelationship relationship = (IArchimateRelationship)idLookup.get(vc.relationshipId);
            createAndAddConnectionsToView(diagramModel, relationship);
        }
        
        // And open the Model in the Models Tree
        IEditorModelManager.INSTANCE.openModel(model);
    }
    
    /**
     * Create and add ArchiMate Connections to a View
     */
    private void createAndAddConnectionsToView(IDiagramModel diagramModel, IArchimateRelationship relationship) {
        List<IDiagramModelArchimateComponent> sources = DiagramModelUtils.findDiagramModelComponentsForArchimateConcept(diagramModel, relationship.getSource());
        List<IDiagramModelArchimateComponent> targets = DiagramModelUtils.findDiagramModelComponentsForArchimateConcept(diagramModel, relationship.getTarget());

        for(IDiagramModelComponent dmcSource : sources) {
            for(IDiagramModelComponent dmcTarget : targets) {
                IDiagramModelArchimateConnection dmc = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
                dmc.setArchimateRelationship(relationship);
                dmc.connect((IDiagramModelObject)dmcSource, (IDiagramModelObject)dmcTarget);
                idLookup.put(dmc.getId(), dmc);
            }
        }
    }

    /**
     * Create and add an ArchiMate Element to a View
     */
    private IDiagramModelArchimateObject createAndAddElementToView(IDiagramModel diagramModel, IArchimateElement element, int x, int y, int width, int height) {
        IDiagramModelArchimateObject dmo = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        dmo.setArchimateElement(element);
        dmo.setBounds(x, y, width, height);
        diagramModel.getChildren().add(dmo);
        idLookup.put(dmo.getId(), dmo);
        return dmo;
    }

    /**
     * Create and add an ArchiMate View to its folder
     */
    private IDiagramModel createAndAddView(IArchimateModel model, String name, String id) {
        IDiagramModel diagramModel = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        diagramModel.setName(name);
        diagramModel.setId(id);
        IFolder folder = model.getDefaultFolderForObject(diagramModel);
        folder.getElements().add(diagramModel);
        idLookup.put(diagramModel.getId(), diagramModel);
        return diagramModel;
    }
    
    /**
     * Create and add an ArchiMate relationship to its folder
     */
    private IArchimateRelationship createAndAddArchimateRelationship(IArchimateModel model, EClass type, String name, String id) {
        if(!IArchimatePackage.eINSTANCE.getArchimateRelationship().isSuperTypeOf(type)) {
            throw new IllegalArgumentException("Eclass type should be of relationship type");
        }
        
        return (IArchimateRelationship)createAndAddArchimateConcept(model, type, name, id);
    }
    
    /**
     * Create and add an ArchiMate element to its folder
     */
    private IArchimateElement createAndAddArchimateElement(IArchimateModel model, EClass type, String name, String id) {
        if(!IArchimatePackage.eINSTANCE.getArchimateElement().isSuperTypeOf(type)) {
            throw new IllegalArgumentException("Eclass type should be of element type");
        }

        return (IArchimateElement)createAndAddArchimateConcept(model, type, name, id);
    }
    
    /**
     * Create and add an ArchiMate concept to its folder
     */
    private IArchimateConcept createAndAddArchimateConcept(IArchimateModel model, EClass type, String name, String id) {
        IArchimateConcept concept = (IArchimateConcept)IArchimateFactory.eINSTANCE.create(type);
        concept.setName(name);
        concept.setId(id);
        IFolder folder = model.getDefaultFolderForObject(concept);
        folder.getElements().add(concept);
        idLookup.put(concept.getId(), concept);
        return concept;
    }

    /**
     * Ask to open a file.
     */
    private File askOpenFile() {
        FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
        dialog.setFilterExtensions(new String[] { MY_EXTENSION_WILDCARD, "*.*" } );
        String path = dialog.open();
        return path != null ? new File(path) : null;
    }
}
