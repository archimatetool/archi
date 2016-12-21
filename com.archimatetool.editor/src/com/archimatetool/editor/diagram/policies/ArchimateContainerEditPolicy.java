/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.policies;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.requests.GroupRequest;

import com.archimatetool.editor.model.DiagramModelUtils;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelObject;



/**
 * Archimate Type Container EditPolicy
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateContainerEditPolicy extends BasicContainerEditPolicy {

    /*
     * Over-ride this to add an explicit connection when removing from an Archimate container if nested
     */
	@Override
    public Command getOrphanChildrenCommand(GroupRequest request) {
	    CompoundCommand command = (CompoundCommand)super.getOrphanChildrenCommand(request);
	    
	    /* This behavior has been disabled because connections have already been created
	     * in a hiden state when nesting the child inside its parent.
	     * TODO: Clean-up if no side effects have been found after some time...
	     */
	    // If we use nested connections and the EditPart model is an Archimate type object
//	    if(ConnectionPreferences.useNestedConnections() && (getHost().getModel() instanceof IDiagramModelArchimateObject)) {
//	        createNewConnectionCommands((IDiagramModelArchimateObject)getHost().getModel(), request.getEditParts(), command);
//	    }
	    
        return command;
    }
	
	/**
	 * When Archimate child objects are dragged out of a parent Archimate object check to see if new connections should be created
	 * 
	 * TODO A3: If O1--C1--O2 and in parent. C1 is also connected to parent (and hidden).
	 *          O1 or O2 is removed from parent - should add connection from C1 to parent?
	 *          Or should it be only when O1 AND O2 are removed from parent?
	 */
	void createNewConnectionCommands(IDiagramModelArchimateObject parentObject, List<?> childEditParts, CompoundCommand command) {
	    IArchimateElement parentElement = parentObject.getArchimateElement();

	    for(Object o : childEditParts) {
	        IDiagramModelObject child = (IDiagramModelObject)((EditPart)o).getModel();

	        // If it's an Archimate type child object...
	        if(child instanceof IDiagramModelArchimateObject) {
	            IDiagramModelArchimateObject childObject = (IDiagramModelArchimateObject)child;
	            IArchimateElement childElement = childObject.getArchimateElement();

	            // See if there are any (nested type) relationships between parent element and child element...
	            for(IArchimateRelationship relation : parentElement.getSourceRelationships()) {
	                if(relation.getTarget() == childElement && DiagramModelUtils.isNestedConnectionTypeRelationship(relation)) {
	                    // And there's not a connection already there then add one
	                    if(!DiagramModelUtils.hasDiagramModelArchimateConnection(parentObject, childObject, relation)) {
	                        command.add(new CreateDiagramArchimateConnectionCommand(parentObject, childObject, relation));
	                    }
	                }
	            }
	        }
	    }
	}
	
	/**
     * Create New Connection Command based on existing relation
     */
    static class CreateDiagramArchimateConnectionCommand extends Command {
        IDiagramModelArchimateConnection fConnection;
        IDiagramModelArchimateObject fSource;
        IDiagramModelArchimateObject fTarget;
        IArchimateRelationship fRelation;
        
        CreateDiagramArchimateConnectionCommand(IDiagramModelArchimateObject source, IDiagramModelArchimateObject target, IArchimateRelationship relation) {
            fSource = source;
            fTarget = target;
            fRelation = relation;
        }
        
        @Override
        public void execute() {
            fConnection = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
            fConnection.setArchimateRelationship(fRelation);
            fConnection.connect(fSource, fTarget);
        }
        
        @Override
        public void redo() {
            fConnection.reconnect();
        }
        
        @Override
        public void undo() {
            fConnection.disconnect();
        }

        @Override
        public void dispose() {
            fConnection = null;
            fSource = null;
            fTarget = null;
            fRelation = null;
        }
    }

}