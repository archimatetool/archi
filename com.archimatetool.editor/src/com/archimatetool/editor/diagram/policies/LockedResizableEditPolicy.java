/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.policies;

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
