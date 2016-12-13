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
     * @param object
     * @return A new Delete Diagram Object Command
     */
    public static Command createDeleteDiagramObjectCommand(IDiagramModelObject object) {
        CompoundCommand result = new CompoundCommand();
        __addDeleteDiagramObjectCommands(object, result);
        return result.unwrap();
    }
    
    /**
     * Recurse and add child delete commands.
     * We have to do this because if the object has children with connections going outside these need explicit Delete Commands too
     * otherwise we end up with trailing connections...
     * @param container
     * @param result
     */
    private static void __addDeleteDiagramObjectCommands(IDiagramModelObject object, CompoundCommand result) {
        result.add(new DeleteDiagramObjectCommand(object));
        
        for(IDiagramModelConnection connection : object.getSourceConnections()) {
            result.add(createDeleteDiagramConnectionCommand(connection));
        }

        for(IDiagramModelConnection connection : object.getTargetConnections()) {
            result.add(createDeleteDiagramConnectionCommand(connection));
        }

        if(object instanceof IDiagramModelContainer) {
            for(IDiagramModelObject child : ((IDiagramModelContainer)object).getChildren()) {
                __addDeleteDiagramObjectCommands(child, result);
            }
        }
    }
    
    /**
     * @param connection
     * @return A new Delete Diagram Connection Command
     */
    public static Command createDeleteDiagramConnectionCommand(IDiagramModelConnection connection) {
        CompoundCommand result = new CompoundCommand();
        __addDeleteDiagramConnectionCommands(connection, result);
        return result.unwrap();
    }
    
    private static void __addDeleteDiagramConnectionCommands(IDiagramModelConnection connection, CompoundCommand result) {
        for(IDiagramModelConnection conn : connection.getSourceConnections()) {
            result.add(createDeleteDiagramConnectionCommand(conn));
        }

        for(IDiagramModelConnection conn : connection.getTargetConnections()) {
            result.add(createDeleteDiagramConnectionCommand(conn));
        }
        
        result.add(new DeleteDiagramConnectionCommand(connection));
    }
}
