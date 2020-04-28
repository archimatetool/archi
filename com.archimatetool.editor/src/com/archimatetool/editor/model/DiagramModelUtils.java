/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.archimatetool.editor.preferences.ConnectionPreferences;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IBounds;
import com.archimatetool.model.IConnectable;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateComponent;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelBendpoint;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IDiagramModelReference;



/**
 * Diagram Model Utils
 * 
 * @author Phillip Beauvoir
 */
public class DiagramModelUtils {
    
    /**
     * Find all diagram models that a given Archimate concept is currently referenced in (i.e it appears as a graphical entity).
     * @param archimateConcept The concept to check on.
     * @return A List of diagram models that archimateConcept is currently referenced in as a node or connection. May be empty, but never null.
     */
    public static List<IDiagramModel> findReferencedDiagramsForArchimateConcept(IArchimateConcept archimateConcept) {
        List<IDiagramModel> models = new ArrayList<IDiagramModel>();
        
        if(archimateConcept != null && archimateConcept.getArchimateModel() != null) {
            for(IDiagramModelArchimateComponent dmc : archimateConcept.getReferencingDiagramComponents()) {
                if(!models.contains(dmc.getDiagramModel())) {
                    models.add(dmc.getDiagramModel());
                }
            }
        }
        
        return models;
    }

    /**
     * Determine if a given Archimate concept is currently referenced in any diagram models.
     * @param archimateConcept The concept to check on.
     * @return true if archimateComponent is referenced in a node or connection in any diagram model
     */
    public static boolean isArchimateConceptReferencedInDiagrams(IArchimateConcept archimateConcept) {
        if(archimateConcept == null || archimateConcept.getArchimateModel() == null) {
            return false;
        }
        
        return !archimateConcept.getReferencingDiagramComponents().isEmpty();
    }


    // ============================ Methods of finding diagram components in a diagram =======================================
    
    /**
     * Find all (visible and extant) Diagram Model Components for a given Archimate concept in a Diagram Model.
     * @param diagramModel The parent diagram model to search in.
     * @param archimateConcept The concept to check on.
     * @return The list of active Diagram Model Components. May be empty, but never null.
     */
    public static List<IDiagramModelArchimateComponent> findDiagramModelComponentsForArchimateConcept(IDiagramModel diagramModel, IArchimateConcept archimateConcept) {
        List<IDiagramModelArchimateComponent> list = new ArrayList<IDiagramModelArchimateComponent>();
        
        if(archimateConcept instanceof IArchimateElement) {
            list.addAll(findDiagramModelObjectsForElement(diagramModel, (IArchimateElement)archimateConcept));
        }
        else if(archimateConcept instanceof IArchimateRelationship) {
            list.addAll(findDiagramModelConnectionsForRelation(diagramModel, (IArchimateRelationship)archimateConcept));
        }
        
        return list;
    }

    /**
     * Find all (visible and extant) Diagram Model Objects for a given Archimate element in a Diagram Model.
     * @param diagramModel The parent diagram model to search in.
     * @param element The element to check on.
     * @return The list of active Diagram Model Objects. May be empty, but never null.
     */
    public static List<IDiagramModelArchimateObject> findDiagramModelObjectsForElement(IDiagramModel diagramModel, IArchimateElement element) {
        // This is the faster method
        //return findDiagramModelObjectsForElementByReference(diagramModel, element);
        
        // Use slower but safer method
        return findDiagramModelObjectsForElementByIterator(diagramModel, element);
    }

    /**
     * Find all (visible and extant) Diagram Model Connections for a given Archimate relationship in a Diagram Model.
     * @param diagramModel The parent diagram model to search in.
     * @param relationship The relationship to check on.
     * @return The list of active Diagram Model Connections. May be empty, but never null.
     */
    public static List<IDiagramModelArchimateConnection> findDiagramModelConnectionsForRelation(IDiagramModel diagramModel, IArchimateRelationship relationship) {
        // This is the faster method
        //return findDiagramModelConnectionsForRelationByReference(diagramModel, relationship);
        
        // Use slower but safer method
        return findDiagramModelConnectionsForRelationByIterator(diagramModel, relationship);
    }

    // ==================================== Slower, but safe, methods of finding a concept ========================================
    
    private static List<IDiagramModelArchimateObject> findDiagramModelObjectsForElementByIterator(IDiagramModel diagramModel, IArchimateElement element) {
        Set<IDiagramModelArchimateObject> set = new HashSet<>();
        
        for(Iterator<EObject> iter = diagramModel.eAllContents(); iter.hasNext();) {
            EObject eObject = iter.next();
            if(eObject instanceof IDiagramModelArchimateObject) {
                IDiagramModelArchimateObject dmo = (IDiagramModelArchimateObject)eObject;
                if(dmo.getArchimateElement() == element) {
                    set.add(dmo);
                }
            }
        }
        
        return new ArrayList<>(set);
    }

    private static List<IDiagramModelArchimateConnection> findDiagramModelConnectionsForRelationByIterator(IDiagramModel diagramModel, IArchimateRelationship relationship) {
        Set<IDiagramModelArchimateConnection> set = new HashSet<>();
        
        for(Iterator<EObject> iter = diagramModel.eAllContents(); iter.hasNext();) {
            EObject eObject = iter.next();
            if(eObject instanceof IDiagramModelArchimateConnection) {
                IDiagramModelArchimateConnection connection = (IDiagramModelArchimateConnection)eObject;
                if(connection.getArchimateRelationship() == relationship) {
                    set.add(connection);
                }
            }
        }
        
        return new ArrayList<>(set);
    }

    // ============================= Fast methods of finding components using reference list ==============================

    @SuppressWarnings("unused")
    private static List<IDiagramModelArchimateObject> findDiagramModelObjectsForElementByReference(IDiagramModel diagramModel, IArchimateElement element) {
        Set<IDiagramModelArchimateObject> set = new HashSet<>();
        
        for(IDiagramModelArchimateObject dmo : element.getReferencingDiagramObjects()) {
            if(dmo.getDiagramModel() == diagramModel) {
                set.add(dmo);
            }
        }
        
        return new ArrayList<>(set);
    }

    @SuppressWarnings("unused")
    private static List<IDiagramModelArchimateConnection> findDiagramModelConnectionsForRelationByReference(IDiagramModel diagramModel, IArchimateRelationship relationship) {
        Set<IDiagramModelArchimateConnection> set = new HashSet<>();
        
        for(IDiagramModelArchimateConnection dmc : relationship.getReferencingDiagramConnections()) {
            if(dmc.getDiagramModel() == diagramModel) {
                set.add(dmc);
            }
        }
        
        return new ArrayList<>(set);
    }

    // ========================================================================================================

    /**
     * Find any Referenced Diagram Objects to other Diagram Models in a given Diagram Model Container.
     * @param container The Diagram Model Container in which to search, usually a IDiagramModel
     * @param diagramModel The Diagram Model to look for references
     * @return The list of Diagram Model References. May be empty, but never null.
     */
    public static List<IDiagramModelReference> findDiagramModelReferences(IDiagramModelContainer container, IDiagramModel diagramModel) {
        List<IDiagramModelReference> list = new ArrayList<IDiagramModelReference>();
        
        for(IDiagramModelObject object : container.getChildren()) {
            if(object instanceof IDiagramModelReference) {
                if(((IDiagramModelReference)object).getReferencedModel() == diagramModel) {
                    list.add((IDiagramModelReference)object);
                }
            }
            if(object instanceof IDiagramModelContainer) {
                list.addAll(findDiagramModelReferences((IDiagramModelContainer)object, diagramModel));
            }
        }
        
        return list;
    }
    
    /**
     * Return true if diagramModel is referenced anywhere in its model as a diagram model reference
     * @param diagramModel The diagram model
     * @return true if referenced
     */
    public static boolean hasDiagramModelReference(IDiagramModel diagramModel) {
        if(diagramModel.getArchimateModel() != null) {
            for(Iterator<EObject> iter = diagramModel.getArchimateModel().getFolder(FolderType.DIAGRAMS).eAllContents(); iter.hasNext();) {
                EObject eObject = iter.next();
                if(eObject instanceof IDiagramModelReference && ((IDiagramModelReference)eObject).getReferencedModel() == diagramModel) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Find whether there is a DiagramModelArchimateConnection containing relation between srcObject and tgtObject
     * @param srcObject The source IConnectable
     * @param tgtObject The target IConnectable
     * @param relation The relation to check for
     * @return True if there is an IDiagramModelArchimateConnection containing relation between srcObject and tgtObject
     */
    public static boolean hasDiagramModelArchimateConnection(IConnectable srcObject, IConnectable tgtObject, IArchimateRelationship relation) {

        for(IDiagramModelConnection conn : srcObject.getSourceConnections()) {
            if(conn instanceof IDiagramModelArchimateConnection) {
                IArchimateRelationship r = ((IDiagramModelArchimateConnection)conn).getArchimateRelationship();
                if(r == relation && conn.getSource() == srcObject && conn.getTarget() == tgtObject) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    // ========================================= NESTED CONNECTIONS FUNCTIONS ==========================================
    
    
    /**
     * @param relation The relationship to check
     * @return true if relation is of a type that can be represented by a nested container 
     */
    public static boolean isNestedConnectionTypeRelationship(IArchimateRelationship relation) {
        for(EClass eClass : ConnectionPreferences.getRelationsClassesForHiding()) {
            if(relation.eClass() == eClass) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * @param connection The connection to check
     * @return true if a connection should be hidden when its source (parent) element contains its target (child) element
     */
    public static boolean shouldBeHiddenConnection(IDiagramModelArchimateConnection connection) {
        if(!ConnectionPreferences.useNestedConnections()) {
            return false;
        }

        // Only if the connection's source and target are both ArchiMate concepts
        if(!(connection.getSource() instanceof IDiagramModelArchimateComponent) && !(connection.getTarget() instanceof IDiagramModelArchimateComponent)) {
            return false;
        }
            
        IDiagramModelArchimateComponent source = (IDiagramModelArchimateComponent)connection.getSource();
        IDiagramModelArchimateComponent target = (IDiagramModelArchimateComponent)connection.getTarget();
        
        // If the connection's source element contains the target element, or the connection's target element contains the source element
        if(source instanceof IDiagramModelArchimateObject && target instanceof IDiagramModelArchimateObject) {
            if(((IDiagramModelContainer)source).getChildren().contains((IDiagramModelArchimateObject)target) 
                    || ((IDiagramModelContainer)target).getChildren().contains((IDiagramModelArchimateObject)source)) {
                // And it's a relationship type we have chosen to hide
                for(EClass eClass : ConnectionPreferences.getRelationsClassesForHiding()) {
                    if(connection.getArchimateRelationship().eClass() == eClass) {
                        return true;
                    }
                }
            }
        }

        // If connection's source is an element and target is a connection
        if(source instanceof IDiagramModelArchimateObject && target instanceof IDiagramModelArchimateConnection) {
            IDiagramModelArchimateObject parent = (IDiagramModelArchimateObject)source;
            IConnectable connectionSource = ((IDiagramModelArchimateConnection)target).getSource();
            IConnectable connectionTarget = ((IDiagramModelArchimateConnection)target).getTarget();

            if(parent.getChildren().contains(connectionSource) && parent.getChildren().contains(connectionTarget)) {
                // And it's a relationship type we have chosen to hide
                for(EClass eClass : ConnectionPreferences.getRelationsClassesForHiding()) {
                    if(connection.getArchimateRelationship().eClass() == eClass) {
                        return true;
                    }
                }
            }
        }

        // If connection's target is an element and source is a connection
        // TODO: Not sure if this directionality should be allowed
        if(target instanceof IDiagramModelArchimateObject && source instanceof IDiagramModelArchimateConnection) {
            IDiagramModelArchimateObject parent = (IDiagramModelArchimateObject)target;
            IConnectable connectionSource = ((IDiagramModelArchimateConnection)source).getSource();
            IConnectable connectionTarget = ((IDiagramModelArchimateConnection)source).getTarget();

            if(parent.getChildren().contains(connectionSource) && parent.getChildren().contains(connectionTarget)) {
                // And it's a relationship type we have chosen to hide
                for(EClass eClass : ConnectionPreferences.getRelationsClassesForHiding()) {
                    if(connection.getArchimateRelationship().eClass() == eClass) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
    
    // ========================================================================================================
    
    /**
     * @param object The IDiagramModelObject to check on
     * @return The topmost ancestor container for a diagram object that is *not* the diagram model, or null.
     */
    public static IDiagramModelContainer getAncestorContainer(IDiagramModelObject object) {
        EObject container = object.eContainer();
        while(container != null && !(container instanceof IDiagramModel) && !(container.eContainer() instanceof IDiagramModel)) {
            container = container.eContainer();
        }
        return (IDiagramModelContainer)container;
    }

    /**
     * Check if there is an existing connection between source and target of a certain relationship type
     * @param source The source object
     * @param target The target object
     * @param relationshipType the relationship type to check
     * @return True if connection type exists between source and target
     */
    public static boolean hasExistingConnectionType(IDiagramModelObject source, IDiagramModelObject target, EClass relationshipType) {
        for(IDiagramModelConnection connection : source.getSourceConnections()) {
            if(connection instanceof IDiagramModelArchimateConnection && connection.getTarget().equals(target)) {
                EClass type = ((IDiagramModelArchimateConnection)connection).getArchimateRelationship().eClass();
                return type.equals(relationshipType);
            }
        }
        return false;
    }
    
    /**
     * Check for a connection cycle between source and target objects
     * @param source The source object
     * @param target The target object
     * @return true if there is a connection cycle between source and target
     *         i.e. source is connected to target, and target is connected to source
     */
    public static boolean hasCycle(IConnectable source, IConnectable target) {
        for(IDiagramModelConnection connection : source.getTargetConnections()) {
            if(connection.getSource().equals(target)) {
                return true;
            }
            if(hasCycle(connection.getSource(), target)) {
                return true;
            }
        }
        return false;
    }

    
    // ========================================================================================================

    /**
     * Return the absolute bounds of a diagram model component
     * TODO for connections
     * @param dmc
     * @return
     */
    public static final IBounds getAbsoluteBounds(IDiagramModelComponent dmc) {
        if(dmc instanceof IDiagramModelObject) {
            return getAbsoluteBounds((IDiagramModelObject)dmc);
        }
        
        // TODO - how to calculate the bounds from this?????
        else if(dmc instanceof IDiagramModelConnection) {
            
        }
        
        return null;
    }

    /**
     * Return the absolute bounds of a diagram model object
     * @param dmo The DiagramModelObject
     * @return The absolute bounds of a diagram model object
     */
    public static final IBounds getAbsoluteBounds(IDiagramModelObject dmo) {
        IBounds bounds = dmo.getBounds().getCopy();
        
        EObject container = dmo.eContainer();
        while(container instanceof IDiagramModelObject) {
            IDiagramModelObject parent = (IDiagramModelObject)container;
            IBounds parentBounds = parent.getBounds().getCopy();
            
            bounds.setX(bounds.getX() + parentBounds.getX());
            bounds.setY(bounds.getY() + parentBounds.getY());
            
            container = container.eContainer();
        }

        return bounds;
    }

    /**
     * Convert the given absolute bounds to the relative bounds in relation to its parent IDiagramModelObject
     * @param absoluteBounds The absolute bounds
     * @param parent The DiagramModelObject that is the parent into which we want to get the relative bounds for
     * @return the relative bounds of the diagram model object
     */
    public static final IBounds getRelativeBounds(IBounds absoluteBounds, IDiagramModelObject parent) {
        IBounds bounds = absoluteBounds.getCopy();
        
        do {
            IBounds parentBounds = parent.getBounds();
            
            bounds.setX(bounds.getX() - parentBounds.getX());
            bounds.setY(bounds.getY() - parentBounds.getY());
            
            parent = (parent.eContainer() instanceof IDiagramModelObject) ? (IDiagramModelObject)parent.eContainer() : null;
        }
        while(parent != null);

        return bounds;
    }
    
    /**
     * Get the absolute bendpoint positions from a connection
     * @param connection This has to be already connected to source and target objects
     * @return A list of points. Never null but might be empty if connection's source or target is not a IDiagramModelObject
     */
    public static List<Point> getAbsoluteBendpointPositions(IDiagramModelConnection connection) {
        List<Point> points = new ArrayList<Point>();
        
        // Has to be connected to objects
        if(!(connection.getSource() instanceof IDiagramModelObject) || !(connection.getTarget() instanceof IDiagramModelObject)) {
            return points;
        }
        
        double bpindex = 1; // index count + 1
        double bpcount = connection.getBendpoints().size() + 1; // number of bendpoints + 1
        
        for(IDiagramModelBendpoint bendpoint : connection.getBendpoints()) {
            // The weight of this Bendpoint should use to calculate its location.
            // The weight should be between 0.0 and 1.0. A weight of 0.0 will
            // cause the Bendpoint to follow the start point, while a weight
            // of 1.0 will cause the Bendpoint to follow the end point
            double bpweight = bpindex / bpcount;
            
            IBounds srcBounds = getAbsoluteBounds(connection.getSource()); // get bounds of source node
            double startX = (srcBounds.getX() + (srcBounds.getWidth() / 2)) + bendpoint.getStartX();
            startX *= (1.0 - bpweight);
            double startY = (srcBounds.getY() + (srcBounds.getHeight() / 2)) + bendpoint.getStartY();
            startY *= (1.0 - bpweight);
            
            IBounds tgtBounds = getAbsoluteBounds(connection.getTarget()); // get bounds of target node
            double endX = (tgtBounds.getX() + (tgtBounds.getWidth() / 2)) + bendpoint.getEndX();
            endX *= bpweight;
            double endY = (tgtBounds.getY() + (tgtBounds.getHeight() / 2)) + bendpoint.getEndY();
            endY *= bpweight;
            
            int x = (int)(startX + endX);
            int y = (int)(startY + endY);
            
            points.add(new Point(x, y));
            
            bpindex++;
        }
        
        return points;
    }
    
    /**
     * Create a bendpoint given absolute x,y positions for a connection
     * @param connection This has to be already connected to source and target objects
     * @param x The absolute x position of the bendpoint
     * @param y The absolute y position of the bendpoint
     * @return The bendpoint or null if connection's source or target is not a IDiagramModelObject
     *         The bendpoint is not added to the connection
     */
    public static IDiagramModelBendpoint createBendPointFromAbsolutePosition(IDiagramModelConnection connection, int x, int y) {
        // Has to be connected to objects
        if(!(connection.getSource() instanceof IDiagramModelObject) || !(connection.getTarget() instanceof IDiagramModelObject)) {
            return null;
        }
        
        IDiagramModelBendpoint bendpoint = IArchimateFactory.eINSTANCE.createDiagramModelBendpoint();
        
        IBounds srcBounds = getAbsoluteBounds(connection.getSource());
        IBounds tgtBounds = getAbsoluteBounds(connection.getTarget());
        
        int startX = x - (srcBounds.getX() + (srcBounds.getWidth() / 2));
        int startY = y - (srcBounds.getY() + (srcBounds.getHeight() / 2));
        bendpoint.setStartX(startX);
        bendpoint.setStartY(startY);

        int endX = x - (tgtBounds.getX() + (tgtBounds.getWidth() / 2));
        int endY = y - (tgtBounds.getY() + (tgtBounds.getHeight() / 2));
        bendpoint.setEndX(endX);
        bendpoint.setEndY(endY);
        
        return bendpoint;
    }
    
    /**
     * @param outer
     * @param inner
     * @return True if outer bounds contains inner bounds
     */
    public static boolean outerBoundsContainsInnerBounds(IBounds outer, IBounds inner) {
        return (outer.getX() <= inner.getX()) && (inner.getX() + inner.getWidth() <= outer.getX() + outer.getWidth())
                && (outer.getY() <= inner.getY()) && (inner.getY() + inner.getHeight() <= outer.getY() + outer.getHeight());
    }

}
