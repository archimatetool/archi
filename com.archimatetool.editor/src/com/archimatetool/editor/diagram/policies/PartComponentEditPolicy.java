/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import com.archimatetool.editor.diagram.commands.DiagramCommandFactory;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.ILockable;


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