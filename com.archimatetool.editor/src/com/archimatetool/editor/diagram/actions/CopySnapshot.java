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
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.jface.viewers.StructuredSelection;

import com.archimatetool.editor.diagram.figures.diagram.GroupFigure;
import com.archimatetool.editor.model.DiagramModelUtils;
import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.editor.model.commands.NonNotifyingCompoundCommand;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IBounds;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelGroup;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IDiagramModelReference;
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
 * This is truly horrible code. @FIXME !!!!!
 *
 * @author Phillip Beauvoir
 */
public final class CopySnapshot {
    
    /**
     * Bi-directional Hashtable
     * Can get key from value
     */
    public static class BidiHashtable<K, V> extends Hashtable<K, V> {
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
     * A new Diagram Model container that contains a copy of all copied diagram model objects.
     * We take a snapshot so that we can maintain the integrity of the copied objects if the user
     * edits or deleted the originals of if a Cut action is performed (which will delete the originals).
     */
    private IDiagramModel fDiagramModelSnapshot;
    
    /**
     * Mapping of original objects to new copied objects in the Snapshot
     */
    private BidiHashtable<IDiagramModelObject, IDiagramModelObject> fOriginalToSnapshotObjectsMapping;
    
    /**
     * Mapping of original connections to new copied Snapshot connections
     */
    private BidiHashtable<IDiagramModelConnection, IDiagramModelConnection> fOriginalToSnapshotConnectionsMapping;
    
    /**
     * x, y mouse click offset for pasting in same diagram
     */
    private int fXOffSet, fYOffSet;
    
    /**
     * Whether or not we paste new copies of the copied Archimate Elements
     */
    private boolean fDoCreateArchimateElementCopies;
    
    /**
     * The source Archimate Model of the copied objects
     */
    private IArchimateModel fSourceArchimateModel;
    
    /**
     * The target Archimate Model of the copied objects
     */
    private IArchimateModel fTargetArchimateModel;
    
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
    
    /**
     * Construct based on a list of the diagram model objects the user selected.
     * <p>
     * The instance represents the selection the user made, but for various reasons that are 
     * not clear to me (Mads), only the objects from the selection is passed, but not the connections.
     * Also, the selection may contain the same object several times.
     * <p> 
     * The constructor has to make a copy of the selection, or perhaps more correctly, has to 
     * build a corrected copy; any duplicate elements are removed. Also, any connection from the
     * underlying diagram model between any two elements in the copy, is included. This means
     * that the copy may in some cases differ from what the user included in the selection.
     * <p>
     * The constructor checks that all objects are part of the same diagram model, that the selection
     * is not null, and that it actually contains some elements. If not, the constructor silently aborts. 
     * 
     * @param modelObjectsSelected The list of objects to use for the snapshot copy/build process.
     */
    public CopySnapshot(List<IDiagramModelObject> modelObjectsSelected) {
        // Mappings of original objects to snapshot objects
        fOriginalToSnapshotObjectsMapping = new BidiHashtable<IDiagramModelObject, IDiagramModelObject>();
        fOriginalToSnapshotConnectionsMapping = new BidiHashtable<IDiagramModelConnection, IDiagramModelConnection>();
        
        if(modelObjectsSelected == null || modelObjectsSelected.isEmpty()) {
            return;
        }
        
        // Assume that all objects belong to the same source diagram model
        IDiagramModel diagramModel = modelObjectsSelected.get(0).getDiagramModel();
        
        /*
         *  Create a new Diagram Model object snapshot based on source diagram model type.
         *  Create the instance from the registered factory to get the correct type of diagram model.
         */
        fDiagramModelSnapshot = (IDiagramModel)diagramModel.eClass().getEPackage().getEFactoryInstance().create(diagramModel.eClass());

        // Sanity check...
        for(IDiagramModelObject object : modelObjectsSelected) {
            if(object.getDiagramModel() != diagramModel) {
                System.err.println("Different diagram models in " + getClass()); //$NON-NLS-1$
                return;
            }
        }
        
        fSourceArchimateModel = diagramModel.getArchimateModel();
        
        // First copy objects, and make sure the mapping from the original objects to the copied
        // gets filled (fOriginalToSnapshotObjectsMapping)
        List<IDiagramModelObject> objectsToCopy = getTopLevelObjectsToCopy(modelObjectsSelected);
        for(IDiagramModelObject child : objectsToCopy) {
            createSnapshotObjects(fDiagramModelSnapshot, child);
        }
        
        // At this point, objectsToCopy is sort of a multirooted tree of objects (and leafs) that is a snapshot
        // of the elements to copy.
        // fOriginalToSnapshotObjectsMapping, on the other hand, is sort of a mapping from this tree, 
        // to and from, the original diagram elements.
        
        // Add all the connections to the snapshot.
        // First, get all the connections  between all original elements in fOriginalToSnapshotObjectsMapping 
        List<IDiagramModelConnection> connections = getConnectionsToCopy();
        // For each connection, check if the elements are present in the snapshot (the new elements).
        for(IDiagramModelConnection originalConnection : connections) {
            // Check with mapping for original source and target
            IDiagramModelObject newSource = fOriginalToSnapshotObjectsMapping.get(originalConnection.getSource());
            IDiagramModelObject newTarget = fOriginalToSnapshotObjectsMapping.get(originalConnection.getTarget());
            // Only add Connections that have both nodes copied as well
            // MBD: 2014-04-14: I am a bit pressed to as when this test would fail?
            // fOriginalToSnapshotConnectionsMapping was just setup to have a 1-to-1 correspondance between 
            // a set of original elements, and elements to be created. getConnectionsToCopy looks at all 
            // the connections between the originals, and only returns connections where both source and target is
            // present. We now check that we can locate the new elements - in our 1-to-1 hash?
            // In other words: If this check fails, I think some error information should logged?
            // Wouldn't it be a problem?
            if(newSource != null && newTarget != null) {
                IDiagramModelConnection newConnection = (IDiagramModelConnection)originalConnection.getCopy();
                newConnection.connect(newSource, newTarget);
                fOriginalToSnapshotConnectionsMapping.put(originalConnection, newConnection);
            }
        }
    }
    
    /**
     * Iterate and recursively make copies of objects.
     * <p>
     * This method creates a deep copy of the diagram model object passed in originalObject, and inserts it into 
     * the container passed in copyContainer. It also fills out the instance variable fOriginalToSnapshotObjectsMapping 
     * with a mapping from the original object to the copy.  
     * <p>
     * @param copyContainer The container to add the new object to
     * @param originalObject The object to copy
     */
    private void createSnapshotObjects(IDiagramModelContainer copyContainer, IDiagramModelObject originalObject) {
        IDiagramModelObject newObject = (IDiagramModelObject)originalObject.getCopy();
        copyContainer.getChildren().add(newObject);
        
        // Add to mapping
        fOriginalToSnapshotObjectsMapping.put(originalObject, newObject);
        
        if(newObject instanceof IDiagramModelContainer) {
            for(IDiagramModelObject child : ((IDiagramModelContainer)originalObject).getChildren()) {
                createSnapshotObjects((IDiagramModelContainer)newObject, child);
            }
        }
    }

    /**
     * Create a list of topmost objects to copy, based on the selection from the user.
     * <p>
     * This method is used to eliminate duplicate selected children and return 
     * the top level objects to copy into the snapshot during construction.
     * <p>
     * @param selected The list of the selected elements, as passed by the client
     * @return A list of the objects from the selection, that does not have any ancestors. 
     */
    private List<IDiagramModelObject> getTopLevelObjectsToCopy(List<IDiagramModelObject> selected) {
        List<IDiagramModelObject> objects = new ArrayList<IDiagramModelObject>();
        
        for(IDiagramModelObject object : selected) {
            if(!hasAncestorSelected(object, selected)) { // if an ancestor is selected don't add that
                objects.add(object);
            }
        }
        
        /*
         * Maintain relative Z-Order in list by original Z-order in original model
         * If each has same container parent
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
    
    /**
     * Get a list of connections to copy, based on the Snapshot.
     * 
     * This method traverses all the objects that are part of the snapshot and identifies the connections 
     * where both source and target are part of the snapshot of objects. These connections are then added to the 
     * list that is returned. 
     * <p>
     * This approach have the slightly interesting effect, that if any two objects (a and b) are copied from 
     * a diagram all connections present in the view between a and b is copied as well. In effect, the user
     * can not specify which connections (or none) to copy between views, because only the objects from the selection
     * is passed to CopySnapshots constructor, not the connections.
     * <p> 
     * The method assumes that the instance variable fOriginalToSnapshotObjectsMapping have been filled 
     * with objects that are part of the snapshot, e.g. by calling getToplevelObjectsToCopy.
     * 
     *  @returns A list of the connections where both source and target is present in snapshot of the original objects.
     */
    private List<IDiagramModelConnection> getConnectionsToCopy() {
        List<IDiagramModelConnection> connections = new ArrayList<IDiagramModelConnection>();
        
        // Copy Connections that have both nodes copied as well
        for(IDiagramModelObject originalObject : fOriginalToSnapshotObjectsMapping.keySet()) {
            for(IDiagramModelConnection originalSourceConnection : originalObject.getSourceConnections()) {
                IDiagramModelObject originalTarget = originalSourceConnection.getTarget();
                if(fOriginalToSnapshotObjectsMapping.containsKey(originalTarget)) {
                    connections.add(originalSourceConnection);
                }
            }
        }
        
        return connections;
    }

    /**
     * Check if an object has an ancestor container that has been selected to be copied and pasted.
     * <p>
     * The "chain" of containers are followed for the object (until the toplevel), and for each "parent", it 
     * is checked if this container is also part of the objects in the list. If that is the case, then 
     * true is returned, otherwise false. 
     * 
     * @param object The object to check for ancestors
     * @param selected List of potential objects to check for
     * 
     * @return true if the object had an ancestor in list of selected objects
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
     * Checks if any of the objects in this CopySnapshot instance can be pasted to a target diagram model.
     * 
     * The targetDiagramModel most be the same type as the objects was copied from.   
     * 
     * @param targetDiagramModel The diagram model to paste to
     * @return true if at least one copied object can be pasted to target diagram model
     */
    public boolean canPasteToDiagram(IDiagramModel targetDiagramModel) {
        if(fOriginalToSnapshotObjectsMapping.isEmpty()) {
            return false;
        }
        
        // Different diagram model types, so no...
        if(targetDiagramModel.eClass() != fDiagramModelSnapshot.eClass()) {
            return false;
        }
        
        for(Entry<IDiagramModelObject, IDiagramModelObject> entry : fOriginalToSnapshotObjectsMapping.entrySet()) {
            if(isValidPasteObject(targetDiagramModel, entry.getValue())) { // at least one selected object is valid
                return true;
            }
        }

        return false;
    }
    
    private boolean isValidPasteObject(IDiagramModel targetDiagramModel, IDiagramModelObject object) {
        // Can't paste IDiagramModelReference to another Archimate model
        if(object instanceof IDiagramModelReference) {
            IDiagramModel ref = ((IDiagramModelReference)object).getReferencedModel();
            for(IDiagramModel diagramModel : targetDiagramModel.getArchimateModel().getDiagramModels()) {
                if(ref == diagramModel) {
                    return true;
                }
            }
            return false;
        }
        
        return true;
    }
    
    /*
     * Check if the paste need to copy the diagram elements in a selection, instead of referencing the existing (archimate?) elements.
     * 
     * A copy must be made if the target and source archimate models are different (as references can not be made between archimate models).
     * A copy must also be made if one or more of the reference archimate model elements have been deleted, or if one or elements are already referenced in the target diagram model.  
     * 
     * @return True if a copy must be made, false otherwise
     */
    private boolean needsCopiedArchimateElements(IDiagramModel targetDiagramModel) {
        // If different Archimate Models then yes!
        if(fTargetArchimateModel != fSourceArchimateModel) {
            return true;
        }
        
        for(IDiagramModelObject object : fOriginalToSnapshotObjectsMapping.keySet()) {
            if(object instanceof IDiagramModelArchimateObject) {
                IArchimateElement originalElement = ((IDiagramModelArchimateObject)object).getArchimateElement();
                if(originalElement == null || originalElement.eContainer() == null) { // archimate element was deleted
                    return true;
                }
                if(!DiagramModelUtils.findDiagramModelObjectsForElement(targetDiagramModel, originalElement).isEmpty()) { // already on diagram
                    return true;
                }
            }
        }
        
        for(IDiagramModelConnection connection : fOriginalToSnapshotConnectionsMapping.keySet()) {
            if(connection instanceof IDiagramModelArchimateConnection) {
                IRelationship originalRelationship = ((IDiagramModelArchimateConnection)connection).getRelationship();
                if(originalRelationship == null || originalRelationship.eContainer() == null) { // archimate relationship was deleted
                    return true;
                }
                if(!DiagramModelUtils.findDiagramModelConnectionsForRelation(targetDiagramModel, originalRelationship).isEmpty()) { // already on diagram
                    return true;
                }
            }
        }

        return false;
    }
    
    /**
     * Create a Command to paste the contents of this CopySnapShot instance to the target diagram model.
     * 
     * This is command used by the PasteAction to actually perform the paste.
     * 
     * @param targetDiagramModel The diagram model to paste to
     * @param viewer An optional GraphicalViewer to select the pasted object
     * @param mousePosition position of mouse clicked in viewer or null if no mouse click
     * @return A command or null if targetDiagramModel is null
     */
    public Command getPasteCommand(IDiagramModel targetDiagramModel, GraphicalViewer viewer, Point mousePosition) {
        if(targetDiagramModel == null) {
            return null;
        }
        
        fTargetArchimateModel = targetDiagramModel.getArchimateModel();
        
        // Create copies of Archimate Elements or not
        fDoCreateArchimateElementCopies = needsCopiedArchimateElements(targetDiagramModel);

        // Find x,y origin offset to paste at
        calculateXYOffset(mousePosition);
        
        // Mapping of snapshot objects to new copy, used for connections
        Hashtable<IDiagramModelObject, IDiagramModelObject> tmpSnapshotToNewObjectMapping = new Hashtable<IDiagramModelObject, IDiagramModelObject>();
        
        CompoundCommand result = new PasteCompoundCommand(Messages.CopySnapshot_0, tmpSnapshotToNewObjectMapping, viewer);
        
        // Diagram objects first
        for(IDiagramModelObject object : fDiagramModelSnapshot.getChildren()) {
            if(isValidPasteObject(targetDiagramModel, object)) {
                createPasteObjectCommand(targetDiagramModel, object, result, tmpSnapshotToNewObjectMapping);
            }
        }

        // Then Connections
        for(Entry<IDiagramModelConnection, IDiagramModelConnection> entry : fOriginalToSnapshotConnectionsMapping.entrySet()) {
            createPasteConnectionCommand(entry.getValue(), result, tmpSnapshotToNewObjectMapping);
        }
        
        return result; // Don't return unwrap() as we want the CompoundCommand to execute to select the objects
    }
    
    /**
     * Create a single Paste command for an object.
     * <p>
     * This method creates a single paste command for a single object. However, for objects that are containers
     * this method recursively calls itself, to also create commands for the child objects. The results are inserted into 
     * the CompoundCommand instance in the result variable.
     * <p>
     * The instance variable fDoCreateArchimateElementCopies is used to determine if the paste should
     * create a new archimate model object or not.
     * 
     * @param container The diagram (or container) to paste to
     * @param snapshotObject The new object - from the snapshot - to paste
     * @param result The CompoundCommand to insert the result into
     * @param tmpSnapshotToNewObjectMapping A hash with mappings from the snapshot elements to the pasted elements.
     */
    private void createPasteObjectCommand(IDiagramModelContainer container, IDiagramModelObject snapshotObject,
                                        CompoundCommand result, Hashtable<IDiagramModelObject, IDiagramModelObject> tmpSnapshotToNewObjectMapping) {
        
        IDiagramModelObject newObject = (IDiagramModelObject)snapshotObject.getCopy();
        
        // Offset top level objects
        if(container instanceof IDiagramModel) {
            IDiagramModelObject originalObject = fOriginalToSnapshotObjectsMapping.getKey(snapshotObject);
            IBounds bounds = originalObject.getBounds().getCopy();
            
            Point pt = new Point(bounds.getX(), bounds.getY());
            translateToAbsolute(originalObject, pt);
            
            bounds.setX(pt.x + fXOffSet);
            bounds.setY(pt.y + fYOffSet);
            
            newObject.setBounds(bounds);
        }
        
        if(newObject instanceof IDiagramModelArchimateObject) {
            IDiagramModelArchimateObject dmo = (IDiagramModelArchimateObject)newObject;
            // Use a copy so provide a new name
            if(fDoCreateArchimateElementCopies) {
                String name = dmo.getArchimateElement().getName();
                dmo.getArchimateElement().setName(name + " " + Messages.CopySnapshot_1); //$NON-NLS-1$
            }
            // Else re-use original ArchiMate element
            else {
                IDiagramModelArchimateObject originalDiagramObject = (IDiagramModelArchimateObject)fOriginalToSnapshotObjectsMapping.getKey(snapshotObject);
                IArchimateElement element = originalDiagramObject.getArchimateElement();
                dmo.setArchimateElement(element);
            }
        }
        
        // Insert references into the map from the snapshot to the pasted elements.
        tmpSnapshotToNewObjectMapping.put(snapshotObject, newObject);
        
        // New diagram object Command
        result.add(new PasteDiagramObjectCommand(container, newObject, fDoCreateArchimateElementCopies));
        
        // If container, recurse
        if(snapshotObject instanceof IDiagramModelContainer) {
            for(IDiagramModelObject child : ((IDiagramModelContainer)snapshotObject).getChildren()) {
                createPasteObjectCommand((IDiagramModelContainer)newObject, child, result, tmpSnapshotToNewObjectMapping);
            }
        }
    }

    /**
     * Create a single Paste command for a connection.
     * <p>
     * This method creates the needed paste command for inserting a single connection. In order for a paste command to be added, 
     * both the source and target object must be in the set of objects that gets pasted, represented in the 
     * tmpSnapshotToNewObjectMapping variable. If not, no paste command is generated for this connection.
     * <p>
     * If the connection is an diagram archimate model connection, and the instance variable fDoCreateArchimateElementCopies is
     * false, then the connection is set to reference the existing archimate model connection use by the source element.
     * <p>
     * @param snapshotConnection The connection to create a paste command for
     * @param result The CompoundCommand reference to insert the paste command into.
     * @param tmpSnapshotToNewObjectMapping
     */
    private void createPasteConnectionCommand(IDiagramModelConnection snapshotConnection, CompoundCommand result,
                                            Hashtable<IDiagramModelObject, IDiagramModelObject> tmpSnapshotToNewObjectMapping) {
        
        // Check with mapping for original source and target
        IDiagramModelObject newSource = tmpSnapshotToNewObjectMapping.get(snapshotConnection.getSource());
        IDiagramModelObject newTarget = tmpSnapshotToNewObjectMapping.get(snapshotConnection.getTarget());
        
        // Only add Connections that have both nodes copied as well
        if(newSource != null && newTarget != null) {
            IDiagramModelConnection newConnection = (IDiagramModelConnection)snapshotConnection.getCopy();
            
            // Re-use original Archimate relationship
            if(!fDoCreateArchimateElementCopies && snapshotConnection instanceof IDiagramModelArchimateConnection) {
                IDiagramModelArchimateConnection originalDiagramConnection = (IDiagramModelArchimateConnection)fOriginalToSnapshotConnectionsMapping.getKey(snapshotConnection);
                IRelationship relationship = originalDiagramConnection.getRelationship();
                ((IDiagramModelArchimateConnection)newConnection).setRelationship(relationship);
            }
            
            result.add(new PasteDiagramConnectionCommand(newConnection, newSource, newTarget, fDoCreateArchimateElementCopies));
        }
    }
    
    // ================================================================================================================
    
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
        Point ptSmallest = getMinimumPoint(fOriginalToSnapshotObjectsMapping.keySet());

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
            
            // This is a kludge that I hate with all my heart.
            // Group figures co-ords are offset horizontally by the height of the top bar
            // I wish it wasn't so but it is
            if(parent instanceof IDiagramModelGroup) {
                pt.translate(0, GroupFigure.TOPBAR_HEIGHT);
            }
        }
    }
    
    // Find leftmost and topmost origin of top level objects
    private Point getMinimumPoint(Set<IDiagramModelObject> selectedObjects) {
        int xOrigin = 99999, yOrigin = 99999; // flag values
        
        for(IDiagramModelObject dmo : selectedObjects) {
            Point pt = new Point(dmo.getBounds().getX(), dmo.getBounds().getY());
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
        
        return (xOrigin == 99999 || yOrigin == 99999) ? null : new Point(xOrigin, yOrigin);
    }
    
    
    // ================================================================================================================
    // Commands
    // ================================================================================================================

    /*
     * Compound Command
     */
    private static class PasteCompoundCommand extends NonNotifyingCompoundCommand {
        private GraphicalViewer graphicalViewer;
        private Hashtable<IDiagramModelObject, IDiagramModelObject> tempOriginalToNewMapping;
        
        public PasteCompoundCommand(String title,  Hashtable<IDiagramModelObject, IDiagramModelObject> tempOriginalToNewMapping, GraphicalViewer viewer) {
            super(title);
            this.tempOriginalToNewMapping = tempOriginalToNewMapping;
            graphicalViewer = viewer;
        }

        @Override
        public void execute() {
            super.execute();
            
            // Select the new objects if we are showing a viewer
            selectNewObjects();
        }
        
        @Override
        public void redo() {
            super.redo();
            
            // Select the new objects
            selectNewObjects();
        }
        
        private void selectNewObjects() {
            if(graphicalViewer != null) {
                List<EditPart> selected = new ArrayList<EditPart>();
                for(Enumeration<IDiagramModelObject> enm = tempOriginalToNewMapping.elements(); enm.hasMoreElements();) {
                    IDiagramModelObject object = enm.nextElement();
                    EditPart editPart = (EditPart)graphicalViewer.getEditPartRegistry().get(object);
                    if(editPart != null && editPart.isSelectable()) {
                        selected.add(editPart);
                    }
                }
                
                graphicalViewer.setSelection(new StructuredSelection(selected));
            }
        }
        
        @Override
        public void dispose() {
            super.dispose();
            graphicalViewer = null;
            tempOriginalToNewMapping = null;
        }
    }
    
    /*
     * Paste Diagram Object Command
     */
    private static class PasteDiagramObjectCommand extends Command {
        private IDiagramModelContainer fParent; // Target Parent Container
        private IDiagramModelObject fNewDiagramObject; // New copy
        private boolean fDoCreateArchimateElement;
        
        public PasteDiagramObjectCommand(IDiagramModelContainer parent, IDiagramModelObject modelObject, boolean doCreateArchimateElement) {
            fParent = parent;
            fNewDiagramObject = modelObject;
            fDoCreateArchimateElement = doCreateArchimateElement;
        }
        
        @Override
        public void execute() {
            // This first
            fParent.getChildren().add(fNewDiagramObject);
            
            // If it's an Archimate model type then add the Archimate model object to a default folder
            if(fNewDiagramObject instanceof IDiagramModelArchimateObject && fDoCreateArchimateElement) {
                ((IDiagramModelArchimateObject)fNewDiagramObject).addArchimateElementToModel(null);
            }
        }
        
        @Override
        public void undo() {
            fParent.getChildren().remove(fNewDiagramObject);
            
            // If it's an Archimate model type then remove the Archimate model object from its containing folder
            if(fNewDiagramObject instanceof IDiagramModelArchimateObject && fDoCreateArchimateElement) {
                ((IDiagramModelArchimateObject)fNewDiagramObject).removeArchimateElementFromModel();
            }
        }
        
        @Override
        public void dispose() {
            fParent = null;
            fNewDiagramObject = null;
        }
    }
    
    /*
     * Paste Diagram Connection Command
     */
    private static class PasteDiagramConnectionCommand extends Command {
        private IDiagramModelConnection fConnection;
        private IDiagramModelObject fSource;
        private IDiagramModelObject fTarget;
        private boolean fDoCreateArchimateElement;
        
        public PasteDiagramConnectionCommand(IDiagramModelConnection connection, IDiagramModelObject source,
                IDiagramModelObject target, boolean doCreateArchimateElement) {
            fConnection = connection;
            fSource = source;
            fTarget = target;
            fDoCreateArchimateElement = doCreateArchimateElement;
        }

        @Override
        public void execute() {
            fConnection.connect(fSource, fTarget);
            
            // If it's an Archimate model type Add relationship to default folder
            if(fConnection instanceof IDiagramModelArchimateConnection && fDoCreateArchimateElement) {
                ((IDiagramModelArchimateConnection)fConnection).addRelationshipToModel(null);
            }
        }
        
        @Override
        public void undo() {
            fConnection.disconnect(); // have to do this
            
            // If it's an Archimate model type remove relationship from folder
            if(fConnection instanceof IDiagramModelArchimateConnection && fDoCreateArchimateElement) {
                ((IDiagramModelArchimateConnection)fConnection).removeRelationshipFromModel();
            }
        }
        
        @Override
        public void redo() {
            fConnection.reconnect(); // have to do this
            
            // If it's an Archimate model type Add relationship to default folder
            if(fConnection instanceof IDiagramModelArchimateConnection && fDoCreateArchimateElement) {
                ((IDiagramModelArchimateConnection)fConnection).addRelationshipToModel(null);
            }
        }
        
        @Override
        public void dispose() {
            fConnection = null;
            fSource = null;
            fTarget = null;
        }
    }
    

}
