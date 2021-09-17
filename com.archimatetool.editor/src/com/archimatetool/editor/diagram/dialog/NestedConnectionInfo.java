/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.dialog;

import org.eclipse.emf.ecore.EClass;

import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelArchimateObject;

/**
 * Describes Nested Connection
 * 
 * Contains source and target objects and relationship class
 * 
 * @author Phillip Beauvoir
 */
public class NestedConnectionInfo {
    private IDiagramModelArchimateObject sourceObject;
    private IDiagramModelArchimateObject targetObject;
    private boolean isReverse;
    private EClass relationshipType;
    
    // Source = parent, Target = child
    public NestedConnectionInfo(IDiagramModelArchimateObject sourceObject, IDiagramModelArchimateObject targetObject, boolean isReverse, EClass relationshipType) {
        this.sourceObject = sourceObject;
        this.targetObject = targetObject;
        this.isReverse = isReverse;
        this.relationshipType = relationshipType;
    }
    
    public IDiagramModelArchimateObject getSourceObject() {
        return isInverted() ? targetObject : sourceObject;
    }

    public IDiagramModelArchimateObject getTargetObject() {
        return isInverted() ? sourceObject : targetObject;
    }
    
    public EClass getRelationshipType() {
        return relationshipType;
    }
    
    private boolean isInverted() {
        return relationshipType == IArchimatePackage.eINSTANCE.getSpecializationRelationship();
    }
    
    public boolean isReverse() {
        return isReverse;
    }
}