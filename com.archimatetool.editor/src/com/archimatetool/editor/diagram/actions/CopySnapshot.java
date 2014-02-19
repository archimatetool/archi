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
     * Constructor
     * @param modelObjectsSelected
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
        
        // First copy objects
        List<IDiagramModelObject> objectsToCopy = getTopLevelObjectsToCopy(modelObjectsSelected);
        for(IDiagramModelObject child : objectsToCopy) {
            createSnapshotObjects(fDiagramModelSnapshot, child);
        }
        
        // Then copy connections
        List<IDiagramModelConnection> connections = getConnectionsToCopy();
        for(IDiagramModelConnection originalConnection : connections) {
            // Check with mapping for original source and target
            IDiagramModelObject newSource = fOriginalToSnapshotObjectsMapping.get(originalConnection.getSource());
            IDiagramModelObject newTarget = fOriginalToSnapshotObjectsMapping.get(originalConnection.getTarget());
            // Only add Connections that have both nodes copied as well
            if(newSource != null && newTarget != null) {
                IDiagramModelConnection newConnection = (IDiagramModelConnection)originalConnection.getCopy();
                newConnection.connect(newSource, newTarget);
                fOriginalToSnapshotConnectionsMapping.put(originalConnection, newConnection);
            }
        }
    }
    
    /*
     * Iterate and make copies of objects
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

    /*
     * Create a list of topmost objects to copy.
     * This will eliminate duplicate selected children and give us only the top level objects to copy.
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
    
    /*
     * Copy connections where both nodes are stored in the Snapshot
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
     * @return True if the target diagram model already contains at least one reference to the copied Archimate Elements.
     * If this is true then we need to paste copies.
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
    
    /*
     * Create a single Paste command for an object
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
        
        // Mapping
        tmpSnapshotToNewObjectMapping.put(snapshotObject, newObject);
        
        // New diagram object Command
        result.add(new PasteDiagramObjectCommand(container, newObject, fDoCreateArchimateElementCopies));
        
        // Container
        if(snapshotObject instanceof IDiagramModelContainer) {
            for(IDiagramModelObject child : ((IDiagramModelContainer)snapshotObject).getChildren()) {
                createPasteObjectCommand((IDiagramModelContainer)newObject, child, result, tmpSnapshotToNewObjectMapping);
            }
        }
    }

    /*
     * Create a single Paste command for a connection
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
