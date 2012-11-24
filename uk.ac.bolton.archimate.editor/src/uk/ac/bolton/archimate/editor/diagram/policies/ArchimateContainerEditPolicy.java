/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.diagram.policies;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.requests.GroupRequest;

import uk.ac.bolton.archimate.editor.model.DiagramModelUtils;
import uk.ac.bolton.archimate.editor.preferences.ConnectionPreferences;
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IArchimateFactory;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateConnection;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;
import uk.ac.bolton.archimate.model.IDiagramModelObject;
import uk.ac.bolton.archimate.model.IRelationship;
import uk.ac.bolton.archimate.model.util.ArchimateModelUtils;


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
	    CompoundCommand result = (CompoundCommand)super.getOrphanChildrenCommand(request);
	    
	    // If we use nested connections and the EditPart model is an Archimate type object...
	    if(ConnectionPreferences.useNestedConnections() && getHost().getModel() instanceof IDiagramModelArchimateObject) {
	        IDiagramModelArchimateObject parentObject = (IDiagramModelArchimateObject)getHost().getModel();
	        IArchimateElement parentElement = parentObject.getArchimateElement();

	        // Iterate thru the child EditParts...
	        for(Object o : request.getEditParts()) {
	            IDiagramModelObject child = (IDiagramModelObject)((EditPart)o).getModel();

	            // If it's an Archimate type child object...
	            if(child instanceof IDiagramModelArchimateObject) {
	                IDiagramModelArchimateObject childObject = (IDiagramModelArchimateObject)child;
	                IArchimateElement childElement = childObject.getArchimateElement();

	                // See if there are any (nested type) relationships between parent element and child element...
	                for(IRelationship relation : ArchimateModelUtils.getSourceRelationships(parentElement)) {
	                    if(relation.getTarget() == childElement && DiagramModelUtils.isNestedConnectionTypeRelationship(relation)) {
	                        // And there's not one already there...
	                        if(!DiagramModelUtils.hasDiagramModelArchimateConnection(parentObject, childObject, relation)) {
	                            result.add(new NewConnectionCommand(parentObject, childObject, relation));
	                        }
	                    }
	                }
	            }
	        }
	    }
	    
        return result;
    }
	
	/**
     * Create New Connection Command based on existing relation
     */
    static class NewConnectionCommand extends Command {
        IDiagramModelArchimateConnection fConnection;
        IDiagramModelArchimateObject fSource;
        IDiagramModelArchimateObject fTarget;
        IRelationship fRelation;
        
        NewConnectionCommand(IDiagramModelArchimateObject source, IDiagramModelArchimateObject target, IRelationship relation) {
            fSource = source;
            fTarget = target;
            fRelation = relation;
        }
        
        @Override
        public void execute() {
            fConnection = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
            fConnection.setRelationship(fRelation);
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