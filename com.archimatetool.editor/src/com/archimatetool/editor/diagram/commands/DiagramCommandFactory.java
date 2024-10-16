/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.commands;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;

import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelObject;



/**
 * DiagramCommandFactory
 * 
 * @author Phillip Beauvoir
 */
public final class DiagramCommandFactory {

    /**
     * @return A new Delete Diagram Object Command
     */
    public static Command createDeleteDiagramObjectCommand(IDiagramModelObject object) {
        CompoundCommand result = new CompoundCommand();
        addDeleteDiagramObjectCommands(object, result, true);
        return result.unwrap();
    }
    
    /**
     * @return A new Delete Diagram Object Command but don't delete child objects
     */
    public static Command createDeleteDiagramObjectNoChildrenCommand(IDiagramModelObject object) {
        CompoundCommand result = new CompoundCommand();
        addDeleteDiagramObjectCommands(object, result, false);
        return result.unwrap();
    }

    private static void addDeleteDiagramObjectCommands(IDiagramModelObject object, CompoundCommand result, boolean recurse) {
        result.add(new DeleteDiagramObjectCommand(object));
        
        for(IDiagramModelConnection connection : object.getSourceConnections()) {
            result.add(createDeleteDiagramConnectionCommand(connection));
        }

        for(IDiagramModelConnection connection : object.getTargetConnections()) {
            result.add(createDeleteDiagramConnectionCommand(connection));
        }

        if(recurse && object instanceof IDiagramModelContainer container) {
            for(IDiagramModelObject child : container.getChildren()) {
                addDeleteDiagramObjectCommands(child, result, recurse);
            }
        }
    }
    
    /**
     * @param connection
     * @return A new Delete Diagram Connection Command
     */
    public static Command createDeleteDiagramConnectionCommand(IDiagramModelConnection connection) {
        CompoundCommand result = new CompoundCommand();
        addDeleteDiagramConnectionCommands(connection, result);
        return result.unwrap();
    }
    
    private static void addDeleteDiagramConnectionCommands(IDiagramModelConnection connection, CompoundCommand result) {
        for(IDiagramModelConnection conn : connection.getSourceConnections()) {
            result.add(createDeleteDiagramConnectionCommand(conn));
        }

        for(IDiagramModelConnection conn : connection.getTargetConnections()) {
            result.add(createDeleteDiagramConnectionCommand(conn));
        }
        
        result.add(new DeleteDiagramConnectionCommand(connection));
    }
}
