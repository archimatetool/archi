/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

import com.archimatetool.editor.diagram.commands.DeleteBendpointsCommand;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.ILockable;


/**
 * Delete all bendpoints Action
 * 
 * @author Phillip Beauvoir
 */
public class DeleteBendpointsAction extends SelectionAction {

    public static final String ID = "com.archimatetool.editor.action.deleteBendpoints"; //$NON-NLS-1$
    public static final String TEXT = Messages.DeleteBendpointsAction_0;

    public DeleteBendpointsAction(IWorkbenchPart part) {
        super(part);
        setText(TEXT);
        setId(ID);
        // Register for key binding
        setActionDefinitionId(ID);
    }

    @Override
    protected boolean calculateEnabled() {
        return !getValidConnections().isEmpty();
    }

    @Override
    public void run() {
        List<IDiagramModelConnection> connections = getValidConnections();
        if(connections.isEmpty()) {
            return;
        }

        CompoundCommand compoundCommand = new CompoundCommand(TEXT);
        
        for(IDiagramModelConnection connection : connections) {
            compoundCommand.add(new DeleteBendpointsCommand(connection));
        }

        execute(compoundCommand);
    }

    private List<IDiagramModelConnection> getValidConnections() {
        return getSelectedEditParts().stream()
                                     .map(EditPart::getModel)
                                     .filter(IDiagramModelConnection.class::isInstance)
                                     .map(IDiagramModelConnection.class::cast)
                                     .filter(conn -> !conn.getBendpoints().isEmpty())
                                     .filter(conn -> !(conn instanceof ILockable lockable && lockable.isLocked()))
                                     .toList();
    }
}
