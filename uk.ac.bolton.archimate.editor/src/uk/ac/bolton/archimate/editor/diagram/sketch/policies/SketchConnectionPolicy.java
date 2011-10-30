/*******************************************************************************
 * Copyright (c) 2010-11 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.sketch.policies;

import org.eclipse.emf.ecore.EClass;

import uk.ac.bolton.archimate.editor.diagram.policies.BasicConnectionPolicy;
import uk.ac.bolton.archimate.model.IDiagramModelGroup;
import uk.ac.bolton.archimate.model.IDiagramModelObject;

/**
 * A policy which allows a component to be connected to another component via a connection
 * 
 * @author Phillip Beauvoir
 */
public class SketchConnectionPolicy extends BasicConnectionPolicy {
    
    @Override
    protected boolean isValidConnectionSource(IDiagramModelObject source, EClass relationshipType) {
        if(source instanceof IDiagramModelGroup) {
            return false;
        }
        return true;
    }
    
    @Override
    protected boolean isValidConnectionTarget(IDiagramModelObject target, EClass relationshipType) {
        if(target instanceof IDiagramModelGroup) {
            return false;
        }
        return true;
    }
    
}
