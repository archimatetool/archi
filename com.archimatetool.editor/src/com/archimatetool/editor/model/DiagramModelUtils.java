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
     * Find all diagram models that element is referenced in (appears as graphical entity).
     * @param element The element to check on.
     * @return A List of diagram models (may be empty, but never null)
     */
    public static List<IDiagramModel> findReferencedDiagramsForElement(IArchimateElement element) {
        List<IDiagramModel> models = new ArrayList<IDiagramModel>();
        
        if(element != null && element.getArchimateModel() != null) {
            for(IDiagramModel diagramModel : element.getArchimateModel().getDiagramModels()) {
                // Find it
                boolean result = !findDiagramModelComponentsForElement(diagramModel, element).isEmpty();
                
                // Not found, maybe it's expressed as a nested parent/child
                if(!result && element instanceof IRelationship && ConnectionPreferences.useNestedConnections()) {
                    result = !findNestedComponentsForRelationship(diagramModel, (IRelationship)element).isEmpty();
                }
                
                if(result && !models.contains(diagramModel)) {
                    models.add(diagramModel);
                }
            }
        }
        
        return models;
    }
    
    /**
     * @param element The element to check on.
     * @return true if element is referenced in any diagram model
     */
    public static boolean isElementReferencedInDiagrams(IArchimateElement element) {
        if(element == null || element.getArchimateModel() == null) {
            return false;
        }
        
        for(IDiagramModel diagramModel : element.getArchimateModel().getDiagramModels()) {
            if(isElementReferencedInDiagram(diagramModel, element)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * @param element The element to check on.
     * @return true if element is referenced in diagramModel
     */
    public static boolean isElementReferencedInDiagram(IDiagramModel diagramModel, IArchimateElement element) {
        if(!findDiagramModelComponentsForElement(diagramModel, element).isEmpty()) {
            return true;
        }
        
        // Expressed as a nested parent/child
        if(element instanceof IRelationship && ConnectionPreferences.useNestedConnections()) {
            if(!findNestedComponentsForRelationship(diagramModel, (IRelationship)element).isEmpty()) {
                return true;
            }
        }
        
        return false;
    }
    
    // ========================================================================================================
    
    /**
     * Find all Diagram Model Components for a given element or relationship in a Diagram Model
     * @param diagramModel The parent diagram model
     * @param element The element to check on.
     * @return The list
     */
    public static List<IDiagramModelComponent> findDiagramModelComponentsForElement(IDiagramModel diagramModel, IArchimateElement element) {
        List<IDiagramModelComponent> list = new ArrayList<IDiagramModelComponent>();
        
        if(element instanceof IRelationship) {
            list.addAll(findDiagramModelConnectionsForRelation(diagramModel, (IRelationship)element));
        }
        else {
            list.addAll(findDiagramModelObjectsForElement(diagramModel, element));
        }
        
        return list;
    }

    /**
     * Find all Diagram Model Objects in a Container for a given element
     * @param parent The parent container
     * @param element The element to check on.
     * @return The list
     */
    public static List<IDiagramModelArchimateObject> findDiagramModelObjectsForElement(IDiagramModelContainer parent, IArchimateElement element) {
        List<IDiagramModelArchimateObject> list = new ArrayList<IDiagramModelArchimateObject>();
        
        for(IDiagramModelObject object : parent.getChildren()) {
            if(object instanceof IDiagramModelArchimateObject) {
                if(((IDiagramModelArchimateObject)object).getArchimateElement() == element && !list.contains(object)) {
                    list.add((IDiagramModelArchimateObject)object);
                }
            }
            if(object instanceof IDiagramModelContainer) {
                list.addAll(findDiagramModelObjectsForElement((IDiagramModelContainer)object, element));
            }
        }
        
        return list;
    }

    /**
     * Find all Diagram Model Connections in a Container for a given relation
     * Connections are only collected for child IDiagramModelObjects not connections coming from the parent
     * @param parent The parent container
     * @param relationship The relationship to check on.
     * @return
     */
    public static List<IDiagramModelArchimateConnection> findDiagramModelConnectionsForRelation(IDiagramModelContainer parent, IRelationship relationship) {
        List<IDiagramModelArchimateConnection> list = new ArrayList<IDiagramModelArchimateConnection>();

        for(IDiagramModelObject object : parent.getChildren()) {
            for(IDiagramModelConnection connection : object.getSourceConnections()) {
                if(connection instanceof IDiagramModelArchimateConnection &&
                                        ((IDiagramModelArchimateConnection)connection).getRelationship() == relationship
                                        && !list.contains(object)) {
                    list.add((IDiagramModelArchimateConnection)connection);
                }
            }
            if(object instanceof IDiagramModelContainer) {
                list.addAll(findDiagramModelConnectionsForRelation((IDiagramModelContainer)object, relationship));
            }
        }
        
        return list;
    }

    /**
     * Find any references to other Diagram Models
     * @param parent The Diagram Model Container in which to search, usually a IDiagramModel
     * @param diagramModel The Diagram Model to look for references
     * @return
     */
    public static List<IDiagramModelReference> findDiagramModelReferences(IDiagramModelContainer parent, IDiagramModel diagramModel) {
        List<IDiagramModelReference> list = new ArrayList<IDiagramModelReference>();
        
        for(IDiagramModelObject object : parent.getChildren()) {
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
        IArchimateElement srcElement = parent.getArchimateElement();
        IArchimateElement tgtElement = child.getArchimateElement();

        // Then see if it's nested
        if(parent.getChildren().contains(child)) {
            return hasNestedConnectionTypeRelationship(srcElement, tgtElement);
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
