/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.archimatetool.editor.preferences.ConnectionPreferences;
import com.archimatetool.model.IArchimateComponent;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IDiagramModelReference;
import com.archimatetool.model.IJunctionElement;
import com.archimatetool.model.IRelationship;
import com.archimatetool.model.util.ArchimateModelUtils;



/**
 * Diagram Model Utils
 * 
 * @author Phillip Beauvoir
 */
public class DiagramModelUtils {
    
    /**
     * Find all diagram models that a given Archimate component is currently referenced in (i.e it appears as a graphical entity).
     * @param archimateComponent The component to check on.
     * @return A List of diagram models that archimateComponent is currently referenced in as a node or connection. May be empty, but never null.
     */
    public static List<IDiagramModel> findReferencedDiagramsForArchimateComponent(IArchimateComponent archimateComponent) {
        List<IDiagramModel> models = new ArrayList<IDiagramModel>();
        
        if(archimateComponent != null && archimateComponent.getArchimateModel() != null) {
            List<IDiagramModelComponent> components = findDiagramModelComponentsForArchimateComponent(archimateComponent);
            for(IDiagramModelComponent dmc : components) {
                if(!models.contains(dmc.getDiagramModel())) {
                    models.add(dmc.getDiagramModel());
                }
            }
            
            // Not found, so maybe it's expressed as a nested parent/child relationship
            if(models.isEmpty() && archimateComponent instanceof IRelationship && ConnectionPreferences.useNestedConnections()) {
                for(IDiagramModel diagramModel : archimateComponent.getArchimateModel().getDiagramModels()) {
                    if(!findNestedComponentsForRelationship(diagramModel, (IRelationship)archimateComponent).isEmpty() && !models.contains(diagramModel)) {
                        models.add(diagramModel);
                    }
                }
            }
        }
        
        return models;
    }

    /**
     * Determine if a given Archimate component is currently referenced in any diagram models.
     * @param archimateComponent The element to check on.
     * @return true if archimateComponent is referenced in a node or connection in any diagram model
     */
    public static boolean isArchimateComponentReferencedInDiagrams(IArchimateComponent archimateComponent) {
        if(archimateComponent == null || archimateComponent.getArchimateModel() == null) {
            return false;
        }
        
        boolean found = !findDiagramModelComponentsForArchimateComponent(archimateComponent).isEmpty();
        
        // Not found, so maybe it's expressed as a nested parent/child relationship
        if(!found && archimateComponent instanceof IRelationship && ConnectionPreferences.useNestedConnections()) {
            for(IDiagramModel diagramModel : archimateComponent.getArchimateModel().getDiagramModels()) {
                if(!findNestedComponentsForRelationship(diagramModel, (IRelationship)archimateComponent).isEmpty()) {
                    return true;
                }
            }
        }
        
        return found;
    }

    // ========================================================================================================
    
    /**
     * Find all (visible and extant) Diagram Model Components that reference a given Archimate component.
     * @param archimateComponent The Archimate component to search on.
     * @return The list of active Diagram Model Components. May be empty, but never null.
     */
    public static List<IDiagramModelComponent> findDiagramModelComponentsForArchimateComponent(IArchimateComponent archimateComponent) {
        List<IDiagramModelComponent> list = new ArrayList<IDiagramModelComponent>();
        
        if(archimateComponent instanceof IArchimateElement) {
            list.addAll(findDiagramModelObjectsForElement((IArchimateElement)archimateComponent));
        }
        else if(archimateComponent instanceof IRelationship) {
            list.addAll(findDiagramModelConnectionsForRelation((IRelationship)archimateComponent));
        }
        
        return list;
    }

    /**
     * Find all (visible and extant) Diagram Model Objects that reference a given Archimate element.
     * @param element The Archimate element to search on.
     * @return The list of active Diagram Model Objects. May be empty, but never null.
     */
    public static List<IDiagramModelArchimateObject> findDiagramModelObjectsForElement(IArchimateElement element) {
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
    public static List<IDiagramModelArchimateConnection> findDiagramModelConnectionsForRelation(IRelationship relationship) {
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
        
        // Could do it this way via the relation's source element
        /*
        if(relationship.getSource() != null) {
            List<IDiagramModelArchimateObject> dmos = findDiagramModelObjectsForElement(relationship.getSource());
            for(IDiagramModelArchimateObject dmo : dmos) {
                for(IDiagramModelConnection conn : dmo.getSourceConnections()) {
                    if(conn instanceof IDiagramModelArchimateConnection && ((IDiagramModelArchimateConnection)conn).getRelationship() == relationship) {
                        list.add((IDiagramModelArchimateConnection)conn);
                    }
                }
            }
        }
        */
        
        return list;
    }

    // ========================================================================================================
    
    /**
     * Find all (visible and extant) Diagram Model Components for a given Archimate component in a Diagram Model.
     * @param diagramModel The parent diagram model to search in.
     * @param archimateComponent The component to check on.
     * @return The list of active Diagram Model Components. May be empty, but never null.
     */
    public static List<IDiagramModelComponent> findDiagramModelComponentsForArchimateComponent(IDiagramModel diagramModel, IArchimateComponent archimateComponent) {
        List<IDiagramModelComponent> list = new ArrayList<IDiagramModelComponent>();
        
        if(archimateComponent instanceof IArchimateElement) {
            list.addAll(findDiagramModelObjectsForElement(diagramModel, (IArchimateElement)archimateComponent));
        }
        else if(archimateComponent instanceof IRelationship) {
            list.addAll(findDiagramModelConnectionsForRelation(diagramModel, (IRelationship)archimateComponent));
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
     * @param diagramModel The parent diagram model to search in.
     * @param relationship The relationship to check on.
     * @return The list of active Diagram Model Connections. May be empty, but never null.
     */
    public static List<IDiagramModelArchimateConnection> findDiagramModelConnectionsForRelation(IDiagramModel diagramModel, IRelationship relationship) {
        List<IDiagramModelArchimateConnection> list = new ArrayList<IDiagramModelArchimateConnection>();

        List<IDiagramModelArchimateConnection> connections = findDiagramModelConnectionsForRelation(relationship);
        for(IDiagramModelArchimateConnection connection : connections) {
            if(connection.getDiagramModel() == diagramModel) {
                list.add(connection);
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
     * @param srcObject The source IDiagramModelArchimateObject
     * @param tgtObject The target IDiagramModelArchimateObject
     * @param relation The relation to check for
     * @return True if there is an IDiagramModelArchimateConnection containing relation between srcObject and tgtObject
     */
    public static boolean hasDiagramModelArchimateConnection(IDiagramModelArchimateObject srcObject, IDiagramModelArchimateObject tgtObject,
            IRelationship relation) {

        for(IDiagramModelConnection conn : srcObject.getSourceConnections()) {
            if(conn instanceof IDiagramModelArchimateConnection) {
                IRelationship r = ((IDiagramModelArchimateConnection)conn).getRelationship();
                if(r == relation && conn.getSource() == srcObject && conn.getTarget() == tgtObject) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    // ========================================= NESTED CONNECTIONS FUNCTIONS ==========================================
    
    // These depend on the user's preferences in ConnectionPreferences

    /**
     * Find matching pairs of source/target IDiagramModelArchimateObject types that are nested in a Diagram Model
     * @param diagramModel The diagram model to search
     * @param relation The relation to search for
     * @return A list of IDiagramModelArchimateObject types paired as IDiagramModelArchimateObject[] arrays
     *         in the form of { sourceDiagramModelArchimateObject , targetDiagramModelArchimateObject }
     */
    public static List<IDiagramModelArchimateObject[]> findNestedComponentsForRelationship(IDiagramModel diagramModel, IRelationship relation) {
        IArchimateElement src = relation.getSource();
        IArchimateElement tgt = relation.getTarget();
        
        // Find all diagram objects that are source of this relationship
        List<IDiagramModelArchimateObject> srcList = findDiagramModelObjectsForElement(diagramModel, src);
        
        // Find all diagram objects that are target of this relationship
        List<IDiagramModelArchimateObject> tgtList = findDiagramModelObjectsForElement(diagramModel, tgt);
        
        List<IDiagramModelArchimateObject[]> list = new ArrayList<IDiagramModelArchimateObject[]>();
        
        // If diagram object 1 is the parent of diagram object 2 AND it's deemed to be a valid relationship, add them to the list
        for(IDiagramModelArchimateObject dmo1 : srcList) {
            for(IDiagramModelArchimateObject dmo2 : tgtList) {
                if(isNestedRelationship(dmo1, dmo2)) {
                    list.add(new IDiagramModelArchimateObject[] {dmo1, dmo2});
                }
            }
        }
        
        return list;
    }
    
    /**
     * @param parent The parent IDiagramModelArchimateObject
     * @param child The child IDiagramModelArchimateObject
     * @return True if there is any nested relationship type between parent and child
     */
    public static boolean isNestedRelationship(IDiagramModelArchimateObject parent, IDiagramModelArchimateObject child) {
        if(parent.getChildren().contains(child)) {
            return hasNestedConnectionTypeRelationship(parent.getArchimateElement(), child.getArchimateElement());
        }
        
        return false;
    }
    
    /**
     * Check if there is any nested type relationship between parent (source) and child (target)
     */
    public static boolean hasNestedConnectionTypeRelationship(IArchimateElement parent, IArchimateElement child) {
        for(IRelationship relation : ArchimateModelUtils.getSourceRelationships(parent)) {
            if(relation.getTarget() == child && isNestedConnectionTypeRelationship(relation)) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * @param relation The realtionship to check
     * @return true if relation is of a type that can be represented by a nested container 
     */
    public static boolean isNestedConnectionTypeRelationship(IRelationship relation) {
        // Some element types are not allowed
        if(!isNestedConnectionTypeElement(relation.getSource()) || !isNestedConnectionTypeElement(relation.getTarget())) {
            return false;
        }
        
        for(EClass eClass : ConnectionPreferences.getRelationsClassesForHiding()) {
            if(relation.eClass() == eClass) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * @param element The element to check
     * @return true if element can be used to calculate an nested type connection as one end of the relation
     */
    public static boolean isNestedConnectionTypeElement(IArchimateElement element) {
        return !(element instanceof IJunctionElement);
    }
    
    /**
     * @param connection The connection to check
     * @return true if a connection should be hidden when its source (parent) element contains its target (child) element
     */
    public static boolean shouldBeHiddenConnection(IDiagramModelArchimateConnection connection) {
        if(!ConnectionPreferences.useNestedConnections()) {
            return false;
        }
        
        // Only if source and target elements are ArchiMate elements
        if(connection.getSource() instanceof IDiagramModelArchimateObject && connection.getTarget() instanceof IDiagramModelArchimateObject) {
            IDiagramModelArchimateObject source = (IDiagramModelArchimateObject)connection.getSource();
            IDiagramModelArchimateObject target = (IDiagramModelArchimateObject)connection.getTarget();
            
            // Junction types are excluded
            if(!isNestedConnectionTypeElement(source.getArchimateElement()) || !isNestedConnectionTypeElement(target.getArchimateElement())) {
                return false;
            }
            
            // If The Source Element contains the Target Element
            if(source.getChildren().contains(target)) {
                // And it's a relationship type we have chosen to hide
                for(EClass eClass : ConnectionPreferences.getRelationsClassesForHiding()) {
                    if(connection.getRelationship().eClass() == eClass) {
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
                EClass type = ((IDiagramModelArchimateConnection)connection).getRelationship().eClass();
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
    public static boolean hasCycle(IDiagramModelObject source, IDiagramModelObject target) {
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
