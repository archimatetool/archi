/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.policies;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.requests.ChangeBoundsRequest;

import com.archimatetool.editor.diagram.commands.CreateNestedArchimateConnectionsWithDialogCommand;
import com.archimatetool.editor.diagram.commands.DeleteNestedConnectionsCommand;
import com.archimatetool.editor.preferences.ConnectionPreferences;
import com.archimatetool.model.IDiagramModelArchimateObject;



/**
 * Policy for Archimate Object Container
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateContainerLayoutPolicy extends ArchimateDiagramLayoutPolicy {
    
    // Over-ride this method to add a sub-command for nested connections
    @Override
    protected Command getAddCommand(Request generic) {
        Object parent = getHost().getModel();
                
        ChangeBoundsRequest request = (ChangeBoundsRequest)generic;
        
        CompoundCommand command = new CompoundCommand();
        
        // Add relations/connections between parent and child if Prefs set and if parent is an Archimate object
        boolean doAddNestedConnections = ConnectionPreferences.createRelationWhenMovingElement() && parent instanceof IDiagramModelArchimateObject;
        
        List<IDiagramModelArchimateObject> childObjectsForNewConnections = new ArrayList<IDiagramModelArchimateObject>();
        
        // Delete connections between parent and child if Prefs set and if parent is an Archimate object
        boolean doDeleteNestedConnections = ConnectionPreferences.useNestedConnections();
                
        List<IDiagramModelArchimateObject> childObjectsForDeletedConnections = new ArrayList<IDiagramModelArchimateObject>();
        
        for(Object editPart : request.getEditParts()) {
            GraphicalEditPart child = (GraphicalEditPart)editPart;
            AddObjectCommand addCommand = createAddCommand(request, child, translateToModelConstraint(getConstraintFor(request, child)));
            command.add(addCommand);
            
            // If we use nested connections, and child is an Archimate diagram object add it to the list
            if(doAddNestedConnections && addCommand.child instanceof IDiagramModelArchimateObject) {
            	childObjectsForNewConnections.add((IDiagramModelArchimateObject)addCommand.child);
            }
            
            // If we need to delete some nested connections
            if(doDeleteNestedConnections && addCommand.child instanceof IDiagramModelArchimateObject) {
                childObjectsForDeletedConnections.add((IDiagramModelArchimateObject)addCommand.child);
            }
        }
        
        // We have some child objects for deletion connections
        if(!childObjectsForDeletedConnections.isEmpty()) {
            Command cmd = new DeleteNestedConnectionsCommand((IDiagramModelArchimateObject)parent, childObjectsForDeletedConnections);
            command.add(cmd);
        }
        
        // We have some child objects so add the sub command
        if(!childObjectsForNewConnections.isEmpty()) {
            Command cmd = new CreateNestedArchimateConnectionsWithDialogCommand((IDiagramModelArchimateObject)parent, childObjectsForNewConnections);
            command.add(cmd);
        }

        return command.unwrap();
    }
}


