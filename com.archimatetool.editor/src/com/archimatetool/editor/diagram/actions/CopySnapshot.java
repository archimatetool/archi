/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.jface.viewers.StructuredSelection;

import com.archimatetool.editor.model.DiagramModelUtils;
import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.model.IArchimateComponent;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IBounds;
import com.archimatetool.model.IConnectable;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateComponent;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IDiagramModelReference;
import com.archimatetool.model.IIdentifier;
import com.archimatetool.model.IRelationship;



/**
 * Snapshot Copy of Diagram objects.
 * <p>
 * This involves taking a snapshot copy of selected objects when the user does a Copy Action.
 * We take a snapshot so that we can maintain the integrity of the copied objects if the user
 * edits or deleted the originals of if a Cut action is performed (which will delete the originals).
 * <p>
 * When the user comes to Paste the objects, a new copy is made from the Snapshot.
 * Then a set of Undoable Commands is created for each newly created object.
 * 
 * This is very horrible code!
 *
 * @author Phillip Beauvoir
 */
public final class CopySnapshot {
    
    /**
     * Two types of behaviour
     * 1. Only copy/paste connections that are actually selected
     * 2. Copy/paste selected objects and automatically create connections between them where possible
     */
    static boolean COPY_SELECTED_CONNECTIONS = false;
    
    /**
     * Bi-directional Hashtable
     * Can get key from value
     */
    static class BidiHashtable<K, V> extends Hashtable<K, V> {
        public synchronized K getKey(Object value) {
            for(Entry<K, V> entry : entrySet()) {
                V v = entry.getValue();
                if(v.equals(value)) {
                    return entry.getKey();
                }
            }
            return null;
        }
    }
    
    /**
     * A list of all copied top-level diagram model objects.
     * We take a snapshot so that we can maintain the integrity of the copied objects if the user
     * edits or deleted the originals of if a Cut action is performed (which will delete the originals).
     */
    private List<IDiagramModelObject> fSnapshotObjects;
    
    /**
     * Mapping of original components to new copied components in the Snapshot
     */
    private BidiHashtable<IConnectable, IConnectable> fOriginalToSnapshotComponentsMapping;
    
    /**
     * The source Archimate Model of the copied objects
     */
    private IArchimateModel fSourceArchimateModel;
    
    /**
     * The class type of source diagram model
     */
    private EClass fSourceDiagramClass;
    
    
    
    /**
     * When a model is closed in the the app clear the system Clipboard of any CopySnapshot objects
     * if the CopySnapshot references thta model that is closed
     */
    static {
        IEditorModelManager.INSTANCE.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if(evt.getPropertyName() == IEditorModelManager.PROPERTY_MODEL_REMOVED) {
                    Object contents = Clipboard.getDefault().getContents();
                    if(contents instanceof CopySnapshot) {
                        CopySnapshot copySnapshot = (CopySnapshot)contents;
                        IArchimateModel model = (IArchimateModel)evt.getNewValue();
                        if(copySnapshot.fSourceArchimateModel == model) {
                            Clipboard.getDefault().setContents(""); //$NON-NLS-1$
                        }
                    }
                }
            }
        });
    }
    

    
    // ================================================================================================================
    // Create a snapshot copy of original objects to use as a template for further copies
    // ================================================================================================================
    
    /**
     * Constructor
     * @param selected
     */
    public CopySnapshot(List<IDiagramModelComponent> selected) {
        // Mappings of original components to snapshot components
        fOriginalToSnapshotComponentsMapping = new BidiHashtable<IConnectable, IConnectable>();
        
        if(selected == null || selected.isEmpty()) {
            return;
        }
        
        // Assume that all selected objects belong to the same source diagram model
        IDiagramModel diagramModel = selected.get(0).getDiagramModel();
        
        // Sanity check to ensure all selected objects belong to this one diagram model
        for(IDiagramModelComponent component : selected) {
            if(component.getDiagramModel() != diagramModel) {
                System.err.println("Different diagram models in " + getClass()); //$NON-NLS-1$
                return;
            }
        }
        
        // Store the Archimate model
        fSourceArchimateModel = diagramModel.getArchimateModel();
        
        // Store the Type of diagram model so we don't paste across different diagram model types
        fSourceDiagramClass = diagramModel.eClass();
        
        // Get top level objects to copy (containers will have their children copied)
        List<IDiagramModelObject> objectsToCopy = getTopLevelObjectsToCopy(selected);
        
        // Create snapshot copies of objects
        fSnapshotObjects = createSnapshotObjects(objectsToCopy);
        
        // Create snapshot copies of connections if selected
        if(COPY_SELECTED_CONNECTIONS) {
            createSnapshotConnectionsForSelectedComponents(selected);
        }
        // Create snapshot copies of inferred connections
        else {
            createAutomaticSnapshotConnections();
        }
    }
    
    /*
     * Create a list of topmost objects to copy.
     * This will eliminate duplicate selected children and give us only the top level objects to copy.
     */
    private List<IDiagramModelObject> getTopLevelObjectsToCopy(List<IDiagramModelComponent> selected) {
        List<IDiagramModelObject> objects = new ArrayList<IDiagramModelObject>();
        
        for(IDiagramModelComponent component : selected) {
            if(component instanceof IDiagramModelObject) {
                if(!hasAncestorSelected((IDiagramModelObject)component, selected)) { // if an ancestor is selected don't add that
                    objects.add((IDiagramModelObject)component);
                }
            }
        }
        
        /*
         * Restore the relative Z-Order in this new list by original Z-order in original model
         * If each has the same container parent
         */
        Collections.sort(objects, new Comparator<Object>() {
            public int compare(Object o1, Object o2) {
                if(o1 instanceof IDiagramModelObject && o2 instanceof IDiagramModelObject) {
                    IDiagramModelContainer parent1 = (IDiagramModelContainer)((IDiagramModelObject)o1).eContainer();
                    IDiagramModelContainer parent2 = (IDiagramModelContainer)((IDiagramModelObject)o2).eContainer();
                    if(parent1 == parent2) {
                        int index1 = parent1.getChildren().indexOf(o1);
                        int index2 = parent2.getChildren().indexOf(o2);
                        return index1 - index2;    
                    }
                }
                return 0;
            }
        });
        
        return objects;
    }
    
    /*
     * Make snapshot copies of top level objects
     */
    private List<IDiagramModelObject> createSnapshotObjects(List<IDiagramModelObject> objectsToCopy) {
        List<IDiagramModelObject> topLevelObjects = new ArrayList<IDiagramModelObject>();
        
        for(IDiagramModelObject dmo : objectsToCopy) {
            IDiagramModelObject snapshotObject = createSnapshotObject(dmo);
            topLevelObjects.add(snapshotObject);
        }
        
        return topLevelObjects;
    }
    
    /*
     * Make snapshot copy of object
     */
    private IDiagramModelObject createSnapshotObject(IDiagramModelObject originalObject) {
        IDiagramModelObject snapshotObject = (IDiagramModelObject)originalObject.getCopy();
        
        // Add to mapping
        fOriginalToSnapshotComponentsMapping.put(originalObject, snapshotObject);
        
        // Object is Container, so recurse
        if(snapshotObject instanceof IDiagramModelContainer) {
            for(IDiagramModelObject child : ((IDiagramModelContainer)originalObject).getChildren()) {
                IDiagramModelObject dmo = createSnapshotObject(child);
                ((IDiagramModelContainer)snapshotObject).getChildren().add(dmo);
            }
        }
        
        return snapshotObject;
    }
    
    /*
     * Create automatic copies of connections (if source and target also have been selected/mapped)
     */
    private void createAutomaticSnapshotConnections() {
        List<IDiagramModelConnection> allConnections = new ArrayList<IDiagramModelConnection>();
        
        // Get all connections from objects
        for(IConnectable connectable : fOriginalToSnapshotComponentsMapping.keySet()) {
            for(IDiagramModelConnection connection : connectable.getSourceConnections()) {
                if(!allConnections.contains(connection)) {
                    allConnections.add(connection);
                }
            }
            for(IDiagramModelConnection connection : connectable.getTargetConnections()) {
                if(!allConnections.contains(connection)) {
                    allConnections.add(connection);
                }
            }
        }
        
        // Create copies of ones that have both ends
        for(IDiagramModelConnection connection : allConnections) {
            if(isSelectedConnectionCopyable(connection, allConnections)) {
                IDiagramModelConnection newConnection = (IDiagramModelConnection)connection.getCopy();
                fOriginalToSnapshotComponentsMapping.put(connection, newConnection);
            }
        }
        
        // Now connect both ends if both ends are also in the mapping
        for(Entry<IConnectable, IConnectable> entry : fOriginalToSnapshotComponentsMapping.entrySet()) {
            if(entry.getKey() instanceof IDiagramModelConnection) {
                IDiagramModelConnection originalConnection = (IDiagramModelConnection)entry.getKey();
                
                IConnectable newSource = fOriginalToSnapshotComponentsMapping.get(originalConnection.getSource());
                IConnectable newTarget = fOriginalToSnapshotComponentsMapping.get(originalConnection.getTarget());
                
                if(newSource != null && newTarget != null) {
                    IDiagramModelConnection newConnection = (IDiagramModelConnection)entry.getValue();
                    newConnection.connect(newSource, newTarget);
                }
                else {
                    System.err.println("No source or target in CopySnapshot!"); //$NON-NLS-1$
                }
            }
        }
    }

    /*
     * Create copies of connections that have been selected (if source and target also have been selected/mapped)
     */
    private void createSnapshotConnectionsForSelectedComponents(List<IDiagramModelComponent> selected) {
        // Create new connection copies for all selected connections, but don't connect them yet
        for(IDiagramModelComponent component : selected) {
            if(component instanceof IDiagramModelConnection) {
                IDiagramModelConnection connection = (IDiagramModelConnection)component;
                
                // The source and the target object must also be selected
                if(isSelectedConnectionCopyable(connection, selected)) {
                    IDiagramModelConnection newConnection = (IDiagramModelConnection)connection.getCopy();
                    fOriginalToSnapshotComponentsMapping.put(connection, newConnection);
                }
            }
        }
        
        // Now connect both ends if both ends are also in the selection/mapping
        for(Entry<IConnectable, IConnectable> entry : fOriginalToSnapshotComponentsMapping.entrySet()) {
            if(entry.getKey() instanceof IDiagramModelConnection) {
                IDiagramModelConnection originalConnection = (IDiagramModelConnection)entry.getKey();
                
                IConnectable newSource = fOriginalToSnapshotComponentsMapping.get(originalConnection.getSource());
                IConnectable newTarget = fOriginalToSnapshotComponentsMapping.get(originalConnection.getTarget());
                
                if(newSource != null && newTarget != null) {
                    IDiagramModelConnection newConnection = (IDiagramModelConnection)entry.getValue();
                    newConnection.connect(newSource, newTarget);
                }
            }
        }
    }
    
    // A selected connection must also have both ends selected/mapped, and each of those ends must also be selected/mapped, and so on
    private boolean isSelectedConnectionCopyable(IDiagramModelConnection connection, List<?> selected) {
        // Yes, we have both ends selected or mapped
        boolean result = (selected.contains(connection.getSource()) || fOriginalToSnapshotComponentsMapping.containsKey(connection.getSource())) && 
                (selected.contains(connection.getTarget()) || fOriginalToSnapshotComponentsMapping.containsKey(connection.getTarget()));
        
        // Recurse
        if(connection.getSource() instanceof IDiagramModelConnection) {
            result &= isSelectedConnectionCopyable((IDiagramModelConnection)connection.getSource(), selected);
        }
        if(connection.getTarget() instanceof IDiagramModelConnection) {
            result &= isSelectedConnectionCopyable((IDiagramModelConnection)connection.getTarget(), selected);
        }
        
        return result;
    }

    /*
     * @return True if object has an ancestor container that has been selected to be copied and pasted
     */
    private boolean hasAncestorSelected(IDiagramModelObject object, List<?> selected) {
        EObject container = object.eContainer();
        
        while(!(container instanceof IDiagramModel)) { // top level
            if(selected.contains(container)) {
                return true;
            }
            container = container.eContainer();
        }
        
        return false;
    }
    
    /**
     * @param targetDiagramModel
     * @return true if at least one copied object can be pasted to target diagram model
     */
    public boolean canPasteToDiagram(IDiagramModel targetDiagramModel) {
        if(fOriginalToSnapshotComponentsMapping.isEmpty()) {
            return false;
        }
        
        // Different diagram model types, so no...
        if(targetDiagramModel.eClass() != fSourceDiagramClass) {
            return false;
        }
        
        for(Entry<IConnectable, IConnectable> entry : fOriginalToSnapshotComponentsMapping.entrySet()) {
            if(isValidPasteComponent(targetDiagramModel, entry.getValue())) { // at least one selected object is valid
                return true;
            }
        }

        return false;
    }
    
    private boolean isValidPasteComponent(IDiagramModel targetDiagramModel, IDiagramModelComponent component) {
        // Can't paste IDiagramModelReference to another Archimate model and the diagram model must exist
        if(component instanceof IDiagramModelReference) {
            IDiagramModel ref = ((IDiagramModelReference)component).getReferencedModel();
            for(IDiagramModel diagramModel : targetDiagramModel.getArchimateModel().getDiagramModels()) {
                if(ref == diagramModel) {
                    return true;
                }
            }
            return false;
        }
        
        return true;
    }
    
    
    // ================================================================================================================
    // Create a new copy instance based on the snapshot template
    // ================================================================================================================
    
    /**
     * Whether or not we create new copies of the originale Archimate Components
     */
    private boolean fDoCreateNewArchimateComponents;
    
    /**
     * x, y mouse click offset for pasting in same diagram
     */
    private int fXOffSet, fYOffSet;
    
    /**
     * Mapping of snapshot objects to new copy
     */
    private Hashtable<IConnectable, IConnectable> fSnapshotToNewComponentMapping;
    
    /**
     * Target Diagram model
     */
    private IDiagramModel fTargetDiagramModel;
    
    /**
     * @param targetDiagramModel The diagram model to paste to
     * @param viewer An optional GraphicalViewer in which to select the pasted objects
     * @param mousePosition position of mouse clicked in viewer or null if no mouse click
     * @return A command or null if targetDiagramModel is null
     */
    public Command getPasteCommand(IDiagramModel targetDiagramModel, GraphicalViewer viewer, Point mousePosition) {
        if(targetDiagramModel == null) {
            return null;
        }
        
        fTargetDiagramModel = targetDiagramModel;
        
        // Create new copies of Archimate components or not
        fDoCreateNewArchimateComponents = needsNewArchimateComponents();

        // Find x,y origin offset to paste at
        calculateXYOffset(mousePosition);
        
        // Mapping of snapshot objects to new copy
        fSnapshotToNewComponentMapping = new Hashtable<IConnectable, IConnectable>();
        
        // Create a new set of objects to be pasted
        List<IDiagramModelObject> pasteObjects = createPasteObjects();
        
        // Create a new set of connections to be pasted
        List<IDiagramModelConnection> pasteConnections = createPasteConnections();
        
        return new PasteCommand(targetDiagramModel, pasteObjects, pasteConnections, viewer, fDoCreateNewArchimateComponents);
    }
    
    /**
     * Create all objects to be pasted
     * @param targetDiagramModel 
     */
    private List<IDiagramModelObject> createPasteObjects() {
        List<IDiagramModelObject> topLevelObjects = new ArrayList<IDiagramModelObject>();

        for(IDiagramModelObject object : fSnapshotObjects) {
            IDiagramModelObject dmo = createPasteObject(fTargetDiagramModel, object);
            if(dmo != null) {
                topLevelObjects.add(dmo);
            }
        }

        return topLevelObjects;
    }
   
    private IDiagramModelObject createPasteObject(IDiagramModelContainer container, IDiagramModelObject snapshotObject) {
        // Don't paste invalid objects
        if(!isValidPasteComponent(fTargetDiagramModel, snapshotObject)) {
            return null;
        }

        IDiagramModelObject pasteObject = (IDiagramModelObject)snapshotObject.getCopy();
        createID(pasteObject);

        // Offset top level objects if container is diagram
        if(container instanceof IDiagramModel) {
            IDiagramModelObject originalObject = (IDiagramModelObject)fOriginalToSnapshotComponentsMapping.getKey(snapshotObject);
            IBounds bounds = originalObject.getBounds().getCopy();

            Point pt = new Point(bounds.getX(), bounds.getY());
            translateToAbsolute(originalObject, pt);

            bounds.setX(pt.x + fXOffSet);
            bounds.setY(pt.y + fYOffSet);

            pasteObject.setBounds(bounds);
        }

        // If Archimate object
        if(pasteObject instanceof IDiagramModelArchimateObject) {
            IDiagramModelArchimateObject dmo = (IDiagramModelArchimateObject)pasteObject;
            
            // Re-use original ArchiMate components
            if(!fDoCreateNewArchimateComponents) {
                IDiagramModelArchimateObject originalDiagramObject = (IDiagramModelArchimateObject)fOriginalToSnapshotComponentsMapping.getKey(snapshotObject);
                IArchimateElement element = originalDiagramObject.getArchimateElement();
                dmo.setArchimateElement(element);
            }
            
            // Provide new names if required
            if(fDoCreateNewArchimateComponents && isSourceAndTargetArchiMateModelSame()) {
                String name = dmo.getArchimateElement().getName();
                dmo.getArchimateElement().setName(name + " " + Messages.CopySnapshot_1); //$NON-NLS-1$
            }
        }

        // Add to Mapping
        fSnapshotToNewComponentMapping.put(snapshotObject, pasteObject);

        // Object is Container, so recurse
        if(snapshotObject instanceof IDiagramModelContainer) {
            for(IDiagramModelObject child : ((IDiagramModelContainer)snapshotObject).getChildren()) {
                IDiagramModelObject dmo = createPasteObject((IDiagramModelContainer)pasteObject, child);
                if(dmo != null) {
                    ((IDiagramModelContainer)pasteObject).getChildren().add(dmo);
                }
            }
        }

        return pasteObject;
    }    

    private List<IDiagramModelConnection> createPasteConnections() {
        List<IDiagramModelConnection> connections = new ArrayList<IDiagramModelConnection>();
        
        // Create new connections from basis of snapshot
        for(IConnectable connectable : fOriginalToSnapshotComponentsMapping.values()) {
            if(connectable instanceof IDiagramModelConnection) {
                IDiagramModelConnection snapshotConnection = (IDiagramModelConnection)connectable;
                IDiagramModelConnection newConnection = (IDiagramModelConnection)snapshotConnection.getCopy();
                createID(newConnection);
                connections.add(newConnection);
                
                // Mapping
                fSnapshotToNewComponentMapping.put(snapshotConnection, newConnection);

                // Re-use original Archimate relationship if required
                if(!fDoCreateNewArchimateComponents && snapshotConnection instanceof IDiagramModelArchimateConnection) {
                    IDiagramModelArchimateConnection originalDiagramConnection = (IDiagramModelArchimateConnection)fOriginalToSnapshotComponentsMapping.getKey(snapshotConnection);
                    IRelationship relationship = originalDiagramConnection.getRelationship();
                    ((IDiagramModelArchimateConnection)newConnection).setRelationship(relationship);
                }
            }
        }

        // Connect them
        for(Entry<IConnectable, IConnectable> entry : fSnapshotToNewComponentMapping.entrySet()) {
            if(entry.getKey() instanceof IDiagramModelConnection) {
                IDiagramModelConnection snapshotConnection = (IDiagramModelConnection)entry.getKey();
                
                IConnectable newSource = fSnapshotToNewComponentMapping.get(snapshotConnection.getSource());
                IConnectable newTarget = fSnapshotToNewComponentMapping.get(snapshotConnection.getTarget());
                
                if(newSource != null && newTarget != null) {
                    IDiagramModelConnection newConnection = (IDiagramModelConnection)entry.getValue();
                    newConnection.connect(newSource, newTarget);
                }
            }
        }
        
        return connections;
    }
    
    /**
     * Components in the copy instance will not have IDs.
     * Normally, IDs are generated when an object is added to a parent object which has an ArchimateModel as root container.
     * But our snapshot copy does not have an ArchimateModel root container. So we need to add new ones.
     * @see com.archimatetool.model.util.IDAdapter
     */
    private void createID(IIdentifier object) {
        String id = object.getId();
        if(id == null) {
            object.setId(fTargetDiagramModel.getArchimateModel().getIDAdapter().getNewID());
        }
    }

    
    // ================================================================================================================
    // Utils
    // ================================================================================================================
    
    /*
     * @return True if the target diagram model already contains at least one reference to the copied Archimate Components.
     * If this is true then we need to paste new copies.
     */
    private boolean needsNewArchimateComponents() {
        // If pasting between different Archimate Models then yes we do!
        if(!isSourceAndTargetArchiMateModelSame()) {
            return true;
        }
        
        for(IConnectable object : fOriginalToSnapshotComponentsMapping.keySet()) {
            if(object instanceof IDiagramModelArchimateComponent) {
                IArchimateComponent originalComponent = ((IDiagramModelArchimateComponent)object).getArchimateComponent();
                
                // Component was deleted
                if(originalComponent == null || originalComponent.eContainer() == null) {
                    return true;
                }
                
                // Component already on diagram
                if(!DiagramModelUtils.findDiagramModelComponentsForArchimateComponent(fTargetDiagramModel, originalComponent).isEmpty()) {
                    return true;
                }
            }
        }
        
        return false;
    }
     
    /**
     * @return true if source and target models are the same
     */
    private boolean isSourceAndTargetArchiMateModelSame() {
        return fSourceArchimateModel == fTargetDiagramModel.getArchimateModel();
    }
    
    /**
     * Calculate x,y origin offset based on mouse click for paste action.
     */
    private void calculateXYOffset(Point mousePosition) {
        // No mouse click, so increment position by 10,10
        if(mousePosition == null) {
            fXOffSet += 10;
            fYOffSet += 10;
            return;
        }
        
        // Find leftmost and topmost origin of top level copied objects
        Point ptSmallest = getMinimumPoint(fOriginalToSnapshotComponentsMapping.keySet());

        if(ptSmallest != null) {
            fXOffSet = mousePosition.x - ptSmallest.x;
            fYOffSet = mousePosition.y - ptSmallest.y;
        }
    }
    
    /**
     * Translate an objects x,y position to absolute co-ordinates
     * @param object
     * @param pt
     */
    private void translateToAbsolute(IDiagramModelObject object, Point pt) {
        if(object.eContainer() instanceof IDiagramModelContainer && !(object.eContainer() instanceof IDiagramModel)) {
            IDiagramModelObject parent = (IDiagramModelObject)object.eContainer();
            pt.performTranslate(parent.getBounds().getX(), parent.getBounds().getY());
            translateToAbsolute(parent, pt);
        }
    }
    
    // Find leftmost and topmost origin of top level objects
    private Point getMinimumPoint(Set<IConnectable> selectedObjects) {
        int xOrigin = 99999, yOrigin = 99999; // flag values
        
        for(IConnectable object : selectedObjects) {
            if(object instanceof IDiagramModelObject) {
                IDiagramModelObject dmo = (IDiagramModelObject)object;
                
                Point pt = new Point(dmo .getBounds().getX(), dmo.getBounds().getY());
                translateToAbsolute(dmo, pt);
                
                // If this object has a parent that is also selected, ignore it
                if(dmo.eContainer() instanceof IDiagramModelObject && selectedObjects.contains(dmo.eContainer())) {
                    continue;
                }
                
                if(pt.x < xOrigin) {
                    xOrigin = pt.x;
                }
                
                if(pt.y < yOrigin) {
                    yOrigin = pt.y;
                }
            }
        }
        
        return (xOrigin == 99999 || yOrigin == 99999) ? null : new Point(xOrigin, yOrigin);
    }
    
    
    
    // ================================================================================================================
    // Commands
    // ================================================================================================================
    
    static class PasteCommand extends Command {
        private IDiagramModel targetDiagramModel;
        private List<IDiagramModelObject> topLevelObjects;
        private List<IDiagramModelConnection> connections;
        private GraphicalViewer viewer;
        private boolean doAddArchimateComponentToModel;
        
        PasteCommand(IDiagramModel targetDiagramModel, List<IDiagramModelObject> topLevelObjects,
                                List<IDiagramModelConnection> connections, GraphicalViewer viewer, boolean doAddArchimateComponentToModel) {
            
            super(Messages.CopySnapshot_0);
            
            this.targetDiagramModel = targetDiagramModel;
            this.topLevelObjects = topLevelObjects;
            this.connections = connections;
            this.viewer = viewer;
            this.doAddArchimateComponentToModel = doAddArchimateComponentToModel;
        }

        @Override
        public void execute() {
            // Add top-level objects to diagram
            for(IDiagramModelObject dmo : topLevelObjects) {
                targetDiagramModel.getChildren().add(dmo);
                
                // If it's an Archimate model type then add the Archimate model object and its children to a default folder
                if(doAddArchimateComponentToModel) {
                    addArchimateComponentsToModel(dmo);
                }
            }
            
            // Add relationships to model if necessary
            if(doAddArchimateComponentToModel) {
                for(IDiagramModelConnection dmc : connections) {
                    // If it's an Archimate model type Add relationship to default folder
                    // This action will assign an ID to the component
                    if(dmc instanceof IDiagramModelArchimateConnection) {
                        ((IDiagramModelArchimateConnection)dmc).addArchimateComponentToModel(null);
                    }
                }
            }
            
            selectNewObjects();
        }
        
        @Override
        public void undo() {
            // Remove top-level objects from diagram
            for(IDiagramModelObject dmo : topLevelObjects) {
                targetDiagramModel.getChildren().remove(dmo);
                
                // If it's an Archimate model type then remove the Archimate model object from its containing folder
                if(doAddArchimateComponentToModel) {
                    removeArchimateComponentsFromModel(dmo);
                }
            }
            
            // Remove relationships to model if necessary
            if(doAddArchimateComponentToModel) {
                for(IDiagramModelConnection dmc : connections) {
                    // If it's an Archimate model type remove relationship from model
                    if(dmc instanceof IDiagramModelArchimateConnection) {
                        ((IDiagramModelArchimateConnection)dmc).removeArchimateComponentFromModel();
                    }
                }
            }
        }
        
        /**
         * Recurse all objects and add to folder in model, thus assigning IDs
         */
        private void addArchimateComponentsToModel(IDiagramModelObject dmo) {
            // If it's an Archimate model type then add the Archimate model object to a default folder
            // This action will assign an ID to the component
            if(dmo instanceof IDiagramModelArchimateObject) {
                ((IDiagramModelArchimateObject)dmo).addArchimateComponentToModel(null);
            }

            // Recurse
            if(dmo instanceof IDiagramModelContainer) {
                for(IDiagramModelObject child : ((IDiagramModelContainer)dmo).getChildren()) {
                    addArchimateComponentsToModel(child);
                }
            }
        }
        
        /**
         * Recurse all objects and remove from folder in model
         */
        private void removeArchimateComponentsFromModel(IDiagramModelObject dmo) {
            // If it's an Archimate model type then remove the Archimate model object from its folder
            if(dmo instanceof IDiagramModelArchimateObject) {
                ((IDiagramModelArchimateObject)dmo).removeArchimateComponentFromModel();
            }

            // Recurse
            if(dmo instanceof IDiagramModelContainer) {
                for(IDiagramModelObject child : ((IDiagramModelContainer)dmo).getChildren()) {
                    removeArchimateComponentsFromModel(child);
                }
            }
        }

        private void selectNewObjects() {
            if(viewer != null) {
                List<EditPart> selected = new ArrayList<EditPart>();
                
                for(EObject object : topLevelObjects) {
                    EditPart editPart = (EditPart)viewer.getEditPartRegistry().get(object);
                    if(editPart != null && editPart.isSelectable()) {
                        selected.add(editPart);
                    }
                }
                
                viewer.setSelection(new StructuredSelection(selected));
            }
        }

        @Override
        public void dispose() {
            targetDiagramModel = null;
            topLevelObjects = null;
            connections = null;
            viewer = null;
        }
    }
 }
