/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.commands;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.swt.widgets.Display;

import com.archimatetool.model.IConnectable;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelObject;


/**
 * A command to create a connection between two diagram objects
 * 
 * @author Phillip Beauvoir
 */
public class CreateDiagramConnectionCommand
extends Command {

    protected CreateConnectionRequest fRequest;
    
    protected IDiagramModelConnection fConnection;
    protected IConnectable fSource;
    protected IConnectable fTarget;

    /**
     * Instantiate a command that can create a connection between two shapes.
     * @param request the type of connection request
     */
    public CreateDiagramConnectionCommand(CreateConnectionRequest request) {
        fRequest = request;
        setLabel(Messages.CreateDiagramConnectionCommand_0);
    }
    
    /**
     * Set the source endpoint for the connection.
     * @param source that source endpoint
     * @throws IllegalArgumentException if source is null
     */
    public void setSource(IConnectable source) {
        if(source == null) {
            throw new IllegalArgumentException("Source connected model object cannot be null"); //$NON-NLS-1$
        }
        fSource = source;
    }

    /**
     * Set the target endpoint for the connection.
     * @param target that target endpoint
     * @throws IllegalArgumentException if target is null
     */
    public void setTarget(IConnectable target) {
        if(target == null) {
            throw new IllegalArgumentException("Target connected model object cannot be null"); //$NON-NLS-1$
        }
        fTarget = target;
    }

    @Override
    public boolean canExecute() {
        if(fTarget == null || fSource == null) {
            return false;
        }
        
        return true;
    }

    @Override
    public void execute() {
        // If null create new one
        if(fConnection == null) {
            fConnection = createNewConnection();
        }
        
        // Connect
        fConnection.connect(fSource, fTarget);
        
        // If it's a circular connection, add some bendpoints
        if(fConnection.getSource() == fConnection.getTarget()) {
            Command cmd = createBendPointsForCircularConnectionCommand(fConnection);
            if(cmd != null) {
                cmd.execute();
            }
        }
        
        // Select connection edit part
        selectConnection();
    }

    @Override
    public void redo() {
        fConnection.reconnect();
    }
    
    @Override
    public void undo() {
        fConnection.disconnect();
    }
    
    /**
     * Create a new connection from the request
     * @return The new connection
     */
    protected IDiagramModelConnection createNewConnection() {
        return (IDiagramModelConnection)fRequest.getNewObject();
    }
    
    /**
     * Select the connection
     */
    protected void selectConnection() {
        if(fRequest.getSourceEditPart() != null && fRequest.getSourceEditPart().getViewer() != null) {
            EditPartViewer viewer = fRequest.getSourceEditPart().getViewer();
            EditPart editPart = (EditPart)viewer.getEditPartRegistry().get(fConnection);
            if(editPart != null) {
                // Async this so that the Properties view can catch up
                Display.getCurrent().asyncExec(() -> {
                    viewer.select(editPart);
                });
            }
        }
    }
    
    /**
     * Create a Command for adding bendpoints to a circular connection
     * So it looks good
     */
    public static Command createBendPointsForCircularConnectionCommand(IDiagramModelConnection connection) {
        // Only works for IDiagramModelObject as source and target objects not for connections
        if(!(connection.getSource() instanceof IDiagramModelObject) && !(connection.getTarget() instanceof IDiagramModelObject)) {
            return null;
        }
        
        IDiagramModelObject source = (IDiagramModelObject)connection.getSource();
        IDiagramModelObject target = (IDiagramModelObject)connection.getTarget();
        
        int width = source.getBounds().getWidth();
        if(width == -1) {
            width = 100;
        }
        int height = target.getBounds().getHeight();
        if(height == -1) {
            height = 60;
        }
        
        width = (int)Math.max(100, width * 0.6);
        height = (int)Math.max(60, height * 0.6);
        
        CompoundCommand result = new CompoundCommand();
        
        CreateBendpointCommand cmd = new CreateBendpointCommand();
        cmd.setDiagramModelConnection(connection);
        cmd.setRelativeDimensions(new Dimension(width, 0), new Dimension(width, 0));
        result.add(cmd);
        
        cmd = new CreateBendpointCommand();
        cmd.setDiagramModelConnection(connection);
        cmd.setRelativeDimensions(new Dimension(width, height), new Dimension(width, height));
        result.add(cmd);
        
        cmd = new CreateBendpointCommand();
        cmd.setDiagramModelConnection(connection);
        cmd.setRelativeDimensions(new Dimension(0, height), new Dimension(0, height));
        result.add(cmd);
        
        return result;
    }
    
    @Override
    public void dispose() {
        fRequest = null;
        fConnection = null;
        fSource = null;
        fTarget = null;
    }
}
