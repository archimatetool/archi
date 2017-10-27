/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.archimatetool.editor.preferences.ConnectionPreferences;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateRelationship;
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
            List<IDiagramModelComponent> components = findDiagramModelComponentsForArchimateConcept(archimateConcept);
            for(IDiagramModelComponent dmc : components) {
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
        
        return !findDiagramModelComponentsForArchimateConcept(archimateConcept).isEmpty();
    }

    // ============================= Fast methods of finding components using reference list ==============================
    
    /**
     * Find all (visible and extant) Diagram Model Components that reference a given Archimate concept.
     * @param archimateConcept The Archimate concept to search on.
     * @return The list of active Diagram Model Components. May be empty, but never null.
     */
    private static List<IDiagramModelComponent> findDiagramModelComponentsForArchimateConcept(IArchimateConcept archimateConcept) {
        List<IDiagramModelComponent> list = new ArrayList<IDiagramModelComponent>();
        
        if(archimateConcept instanceof IArchimateElement) {
            list.addAll(findDiagramModelObjectsForElement((IArchimateElement)archimateConcept));
        }
        else if(archimateConcept instanceof IArchimateRelationship) {
            list.addAll(findDiagramModelConnectionsForRelation((IArchimateRelationship)archimateConcept));
        }
        
        return list;
    }

    /**
     * Find all (visible and extant) Diagram Model Objects that reference a given Archimate element.
     * @param element The Archimate element to search on.
     * @return The list of active Diagram Model Objects. May be empty, but never null.
     */
    static List<IDiagramModelArchimateObject> findDiagramModelObjectsForElement(IArchimateElement element) {
        List<IDiagramModelArchimateObject> list = new ArrayList<IDiagramModelArchimateObject>();
        
        /*
         * It's not simply a case of returning the list of references.
         * If an *ancestor* of a dmo is deleted, or the diagram model itself, but not the direct parent,
         * the dmo will not be removed from the element's dmo reference list,
         * so we check if there is a top model ancestor on the referenced dmo.
         * If there is a top model ancestor, it's used in a diagram model.
         */
        for(IDiagramModelArchimateObject dmo : element.getReferencingDiagramObjects()) {
            if(dmo.getDiagramModel() != null && dmo.getDiagramModel().getArchimateModel() != null) {
                list.add(dmo);
            }
        }
        
        return list;
    }

    /**
     * Find all (visible and extant) Diagram Model Connections that reference a given Archimate relationship.
     * @param relationship The relationship to search on.
     * @return The list of active Diagram Model Connections. May be empty, but never null.
     */
    static List<IDiagramModelArchimateConnection> findDiagramModelConnectionsForRelation(IArchimateRelationship relationship) {
        List<IDiagramModelArchimateConnection> list = new ArrayList<IDiagramModelArchimateConnection>();
        
        /*
         * It's not simply a case of returning the list of references.
         * If an *ancestor* of a dmc is deleted, or the diagram model itself, but not the direct parent,
         * the dmc will not be removed from the relation's dmc reference list,
         * so we check if there is a top model ancestor on the referenced dmc.
         * If there is a top model ancestor, it's used in a diagram model.
         */
        for(IDiagramModelArchimateConnection dmc : relationship.getReferencingDiagramConnections()) {
            if(dmc.getDiagramModel() != null && dmc.getDiagramModel().getArchimateModel() != null) {
                list.add(dmc);
            }
        }
        
        return list;
    }

    /**
     * Find all (visible and extant) Diagram Model Objects for a given Archimate element in a Diagram Model.
     * This is the faster method.
     * @param diagramModel The parent diagram model to search in.
     * @param element The element to check on.
     * @return The list of active Diagram Model Objects. May be empty, but never null.
     */
    static List<IDiagramModelArchimateObject> findDiagramModelObjectsForElementByReference(IDiagramModel diagramModel, IArchimateElement element) {
        // This is the faster method
        List<IDiagramModelArchimateObject> list = new ArrayList<IDiagramModelArchimateObject>();
        
        List<IDiagramModelArchimateObject> dmos = findDiagramModelObjectsForElement(element);
        for(IDiagramModelArchimateObject dmo : dmos) {
            if(dmo.getDiagramModel() == diagramModel) {
                list.add(dmo);
            }
        }
        
        return list;
    }

    /**
     * Find all (visible and extant) Diagram Model Connections for a given Archimate relationship in a Diagram Model.
     * This is the faster method.
     * @param diagramModel The parent diagram model to search in.
     * @param relationship The relationship to check on.
     * @return The list of active Diagram Model Connections. May be empty, but never null.
     */
    static List<IDiagramModelArchimateConnection> findDiagramModelConnectionsForRelationByReference(IDiagramModel diagramModel, IArchimateRelationship relationship) {
        List<IDiagramModelArchimateConnection> list = new ArrayList<IDiagramModelArchimateConnection>();

        List<IDiagramModelArchimateConnection> connections = findDiagramModelConnectionsForRelation(relationship);
        for(IDiagramModelArchimateConnection connection : connections) {
            if(connection.getDiagramModel() == diagramModel) {
                list.add(connection);
            }
        }
        
        return list;
    }

    // ============================ Slower, but safer, methods of recursively finding components =======================================
    
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
        List<IDiagramModelArchimateObject> list = new ArrayList<IDiagramModelArchimateObject>();
        
        for(Iterator<EObject> iter = diagramModel.eAllContents(); iter.hasNext();) {
            EObject eObject = iter.next();
            if(eObject instanceof IDiagramModelArchimateObject) {
                IDiagramModelArchimateObject dmo = (IDiagramModelArchimateObject)eObject;
                if(dmo.getArchimateElement() == element && !list.contains(dmo)) {
                    list.add(dmo);
                }
            }
        }
        
        return list;
    }

    private static List<IDiagramModelArchimateConnection> findDiagramModelConnectionsForRelationByIterator(IDiagramModel diagramModel, IArchimateRelationship relationship) {
        List<IDiagramModelArchimateConnection> list = new ArrayList<IDiagramModelArchimateConnection>();
        
        for(Iterator<EObject> iter = diagramModel.eAllContents(); iter.hasNext();) {
            EObject eObject = iter.next();
            if(eObject instanceof IDiagramModelArchimateConnection) {
                IDiagramModelArchimateConnection connection = (IDiagramModelArchimateConnection)eObject;
                if(connection.getArchimateRelationship() == relationship && !list.contains(connection)) {
                    list.add(connection);
                }
            }
        }
        
        return list;
    }

    // ========================================================================================================

    /**
     * Find any Reference Doiagram Objects to other Diagram Models in a given Diagram Model Container.
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
            if(((IDiagramModelContainer)source).getChildren().contains(target) || ((IDiagramModelContainer)target).getChildren().contains(source)) {
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

}
