/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import uk.ac.bolton.archimate.editor.diagram.commands.DiagramCommandFactory;
import uk.ac.bolton.archimate.model.IDiagramModelObject;
import uk.ac.bolton.archimate.model.ILockable;

/**
 * Policy for Deleting and Orphaning Parts
 * 
 * @author Phillip Beauvoir
 */
public class PartComponentEditPolicy extends ComponentEditPolicy {

    @Override
    protected Command createDeleteCommand(GroupRequest request) {
        IDiagramModelObject object = (IDiagramModelObject)getHost().getModel();
        boolean isLocked = object instanceof ILockable && ((ILockable)object).isLocked();
        return isLocked ? null : DiagramCommandFactory.createDeleteDiagramObjectCommand(object);
    }
}