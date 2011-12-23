/*******************************************************************************
 * Copyright (c) 2010-11 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.policies;

import java.util.List;

import org.eclipse.gef.editpolicies.NonResizableEditPolicy;


/**
 * A Resize Edit Policy to limit resizing of Locked parts
 * 
 * @author Phillip Beauvoir
 */
public class LockedResizableEditPolicy extends NonResizableEditPolicy {
    
    @Override
    protected void createDragHandle(@SuppressWarnings("rawtypes") List handles, int direction) {
    }

    @Override
    public boolean isDragAllowed() {
        return false;
    }
}
