/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.example;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.archimatetool.editor.model.DiagramModelUtils;
import com.archimatetool.editor.model.IEditorModelManager;
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
public class MyImporter {

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

    // ID -> Object lookup table
    private Map<String, EObject> idLookup;
    
    /**
     * Load in the file
     * Assuming you load in the data in some way, perhaps with JDOM, or a SAX Parser ot text reader then you will
     * have a representation of it in Java classes that you need to map to Archi elements, relationships and Views.
     * 
     * In this example we won't read from file but will use some example raw data
     */
    public void doImport(File file) {
        // Elements
        List<Element> elements = List.of(
                new Element("BusinessActor", "Actor", "elementID1"),
                new Element("BusinessRole", "Client", "elementID2"),
                new Element("BusinessFunction", "My Function", "elementID3"),
                new Element("BusinessInteraction", "Interaction", "elementID4"));
        
        
        // Relationships
        List<Relationship> relationships = List.of(
                new Relationship("AssignmentRelationship", "Assigned to", "relID1", "elementID1", "elementID2"),
                new Relationship("ServingRelationship", "", "relID2", "elementID1", "elementID3"),
                new Relationship("AssociationRelationship", "", "relID3", "elementID2", "elementID3"),
                new Relationship("AssociationRelationship", "", "relID4", "relID1", "elementID4"));
        
        // Views
        List<View> views = List.of(
                new View("View 1", "view1"),
                new View("View 2", "view2"));
        
        // View elements
        List<ViewElement> viewElements = List.of(
                new ViewElement("view1", "elementID1", 10, 10, -1, -1),
                new ViewElement("view1", "elementID2", 310, 10, -1, -1),
                new ViewElement("view1", "elementID3", 310, 110, -1, -1),
                new ViewElement("view1", "elementID4", 160, 210, -1, -1),
                new ViewElement("view2", "elementID2", 10, 10, -1, -1),
                new ViewElement("view2", "elementID3", 10, 110, -1, -1));
        
        // View connections
        List<ViewConnection> viewConnections = List.of(
                new ViewConnection("view1", "relID1"),
                new ViewConnection("view1", "relID2"),
                new ViewConnection("view1", "relID3"),
                new ViewConnection("view1", "relID4"),
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
        
        // Create and add model relationships
        for(Relationship r : relationships) {
            createAndAddArchimateRelationship(model, (EClass)IArchimatePackage.eINSTANCE.getEClassifier(r.type), r.name, r.id);
        }
        
        // Then set source and target concepts for relations after relations have been created in case of relation->relation connection
        for(Relationship r : relationships) {
            IArchimateRelationship relationship = (IArchimateRelationship)idLookup.get(r.id);
            IArchimateConcept source = (IArchimateConcept)idLookup.get(r.sourceId);
            IArchimateConcept target = (IArchimateConcept)idLookup.get(r.targetId);
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
        
        // Add diagram connections from object -> object to views first
        for(ViewConnection vc : viewConnections) {
            IArchimateRelationship relationship = (IArchimateRelationship)idLookup.get(vc.relationshipId);
            if(relationship.getSource() instanceof IArchimateElement && relationship.getTarget() instanceof IArchimateElement) {
                IDiagramModel diagramModel = (IDiagramModel)idLookup.get(vc.viewId);
                createAndAddConnectionsToView(diagramModel, relationship);
            }
        }
        
        // Then add any connections -> connections to views
        for(ViewConnection vc : viewConnections) {
            IArchimateRelationship relationship = (IArchimateRelationship)idLookup.get(vc.relationshipId);
            if(relationship.getSource() instanceof IArchimateRelationship || relationship.getTarget() instanceof IArchimateRelationship) {
                IDiagramModel diagramModel = (IDiagramModel)idLookup.get(vc.viewId);
                createAndAddConnectionsToView(diagramModel, relationship);
            }
        }
        
        // Set this (and any other class-level objects) to null so it can be garbage collected
        idLookup = null;
        
        // And open the Model in the Models Tree
        IEditorModelManager.INSTANCE.openModel(model);
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
        idLookup.put(concept.getId(), concept); // add to lookup
        return concept;
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
        idLookup.put(diagramModel.getId(), diagramModel); // add to lookup
        return diagramModel;
    }
    
    /**
     * Create and add ArchiMate Connections to a View
     */
    private void createAndAddConnectionsToView(IDiagramModel diagramModel, IArchimateRelationship relationship) {
        List<IDiagramModelArchimateComponent> sources = DiagramModelUtils.findDiagramModelComponentsForArchimateConcept(diagramModel, relationship.getSource());
        List<IDiagramModelArchimateComponent> targets = DiagramModelUtils.findDiagramModelComponentsForArchimateConcept(diagramModel, relationship.getTarget());

        for(IDiagramModelArchimateComponent dmcSource : sources) {
            for(IDiagramModelArchimateComponent dmcTarget : targets) {
                IDiagramModelArchimateConnection dmc = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
                dmc.setArchimateRelationship(relationship);
                dmc.connect(dmcSource, dmcTarget);
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
        return dmo;
    }
}
