/*******************************************************************************
 * Copyright (c) 2010-2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import uk.ac.bolton.archimate.editor.preferences.ConnectionPreferences;
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IDiagramModel;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateConnection;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;
import uk.ac.bolton.archimate.model.IDiagramModelComponent;
import uk.ac.bolton.archimate.model.IDiagramModelConnection;
import uk.ac.bolton.archimate.model.IDiagramModelContainer;
import uk.ac.bolton.archimate.model.IDiagramModelObject;
import uk.ac.bolton.archimate.model.IDiagramModelReference;
import uk.ac.bolton.archimate.model.IJunctionElement;
import uk.ac.bolton.archimate.model.IRelationship;
import uk.ac.bolton.archimate.model.util.ArchimateModelUtils;


/**
 * Diagram Model Utils
 * 
 * @author Phillip Beauvoir
 */
public class DiagramModelUtils {
    
    /**
     * Find all diagram models that element is refrenced in (appears as graphical entity).
     * @param element The element to check on.
     * @return A List of diagram models (may be empty, but never null)
     */
    public static List<IDiagramModel> findReferencedDiagramsForElement(IArchimateElement element) {
        List<IDiagramModel> models = new ArrayList<IDiagramModel>();
        
        if(element != null && element.getArchimateModel() != null) {
            for(IDiagramModel diagramModel : element.getArchimateModel().getDiagramModels()) {
                if(findDiagramModelComponentForElement(diagramModel, element) != null) {
                    if(!models.contains(diagramModel)) {
                        models.add(diagramModel);
                    }
                }
                // Expressed as a nested parent/child
                else if(element instanceof IRelationship && ConnectionPreferences.useNestedConnections()) {
                    if(isRelationReferencedInDiagramAsNestedConnection(diagramModel, (IRelationship)element)) {
                        if(!models.contains(diagramModel)) {
                            models.add(diagramModel);
                        }
                    }
                }
            }
        }
        
        return models;
    }
    
    /**
     * @param element
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
     * @param element
     * @return true if element is referenced in diagramModel
     */
    public static boolean isElementReferencedInDiagram(IDiagramModel diagramModel, IArchimateElement element) {
        if(findDiagramModelComponentForElement(diagramModel, element) != null) {
            return true;
        }
        
        // Expressed as a nested parent/child
        if(element instanceof IRelationship && ConnectionPreferences.useNestedConnections()) {
            if(isRelationReferencedInDiagramAsNestedConnection(diagramModel, (IRelationship)element)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * @param diagramModel
     * @param relation
     * @return true if relation is referenced in diagramModel visually as a parent/child
     *         container relationship between source (parent) and target (child) of relationship
     */
    public static boolean isRelationReferencedInDiagramAsNestedConnection(IDiagramModel diagramModel, IRelationship relation) {
        if(diagramModel == null || relation == null || relation.getArchimateModel() == null
                || !isNestedConnectionTypeRelationship(relation)) {
            return false;
        }
        
        IArchimateElement src = relation.getSource();
        IArchimateElement tgt = relation.getTarget();
        
        IDiagramModelArchimateObject dmo1 = findDiagramModelObjectForElement(diagramModel, src);
        IDiagramModelArchimateObject dmo2 = findDiagramModelObjectForElement(diagramModel, tgt);
        
        if(dmo1 != null && dmo2 != null) {
            return dmo1.getChildren().contains(dmo2);
        }
        
        return false;
    }
    
    /**
     * Check if there is already a nested type relationship between parent (source) and child (target)
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
     * @param relation
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
     * @param element
     * @return true if element can be used to calculate an nested type connection as one end of the relation
     */
    public static boolean isNestedConnectionTypeElement(IArchimateElement element) {
        return !(element instanceof IJunctionElement);
    }
    
    /**
     * Find a Diagram Model Component in a Diagram for a given element or relation
     * @param diagramModel
     * @param element
     * @return 
     */
    public static IDiagramModelComponent findDiagramModelComponentForElement(IDiagramModel diagramModel, IArchimateElement element) {
        if(element instanceof IRelationship) {
            return findDiagramModelConnectionForRelation(diagramModel, (IRelationship)element);
        }
        else {
            return findDiagramModelObjectForElement(diagramModel, element);
        }
    }

    /**
     * Find a Diagram Model Connection in a Container for a given relation
     * @param parent
     * @param relationship
     * @return
     */
    public static IDiagramModelArchimateConnection findDiagramModelConnectionForRelation(IDiagramModelContainer parent, IRelationship relationship) {
        for(IDiagramModelObject object : parent.getChildren()) {
            for(IDiagramModelConnection connection : object.getSourceConnections()) {
                if(connection instanceof IDiagramModelArchimateConnection &&
                        ((IDiagramModelArchimateConnection)connection).getRelationship() == relationship) {
                    return (IDiagramModelArchimateConnection)connection;
                }
            }
            if(object instanceof IDiagramModelContainer) {
                IDiagramModelArchimateConnection result = findDiagramModelConnectionForRelation((IDiagramModelContainer)object, relationship);
                if(result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    /**
     * Find a Diagram Model Object in a Container for a given element
     * @param parent
     * @param element
     * @return The Diagram Model Object or null
     */
    public static IDiagramModelArchimateObject findDiagramModelObjectForElement(IDiagramModelContainer parent, IArchimateElement element) {
        for(IDiagramModelObject object : parent.getChildren()) {
            if(object instanceof IDiagramModelArchimateObject) {
                if(((IDiagramModelArchimateObject)object).getArchimateElement() == element) {
                    return (IDiagramModelArchimateObject)object;
                }
            }
            if(object instanceof IDiagramModelContainer) {
                IDiagramModelArchimateObject result = findDiagramModelObjectForElement((IDiagramModelContainer)object, element);
                if(result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    /**
     * Find any references to other Diagram Models
     * @param parent The Diagram Model to search
     * @param diagramModel The Diagram Model to look for references
     * @return
     */
    public static List<IDiagramModelReference> findDiagramModelReferences(IDiagramModel parent, IDiagramModel diagramModel) {
        List<IDiagramModelReference> list = new ArrayList<IDiagramModelReference>();
        findDiagramModelReferences(list, parent, diagramModel);
        return list;
    }
    
    private static void findDiagramModelReferences(List<IDiagramModelReference> list, IDiagramModelContainer parent, IDiagramModel diagramModel) {
        for(IDiagramModelObject object : parent.getChildren()) {
            if(object instanceof IDiagramModelReference) {
                if(((IDiagramModelReference)object).getReferencedModel() == diagramModel) {
                    list.add((IDiagramModelReference)object);
                }
            }
            if(object instanceof IDiagramModelContainer) {
                findDiagramModelReferences(list, (IDiagramModelContainer)object, diagramModel);
            }
        }
    }
    
    /**
     * @param object
     * @return The topmost ancestor container for a diagram object that is *not* the diagram model, or null.
     */
    public static IDiagramModelContainer getAncestorContainer(IDiagramModelObject object) {
        EObject container = object.eContainer();
        while(!(container instanceof IDiagramModel) && !(container.eContainer() instanceof IDiagramModel)) {
            container = container.eContainer();
        }
        return (IDiagramModelContainer)container;
    }

 }
