/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
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
import uk.ac.bolton.archimate.model.IDiagramModelContainer;
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

        IDiagramModelContainer parent = (IDiagramModelContainer)getHost().getModel();

        for(Object o : request.getEditParts()) {
            EditPart editPart = (EditPart)o;
            IDiagramModelObject child = (IDiagramModelObject)editPart.getModel();
            
            if(parent instanceof IDiagramModelArchimateObject && child instanceof IDiagramModelArchimateObject) {
                IDiagramModelArchimateObject parentObject = (IDiagramModelArchimateObject)parent;
                IDiagramModelArchimateObject childObject = (IDiagramModelArchimateObject)child;
                
                IArchimateElement parentElement = parentObject.getArchimateElement();
                IArchimateElement childElement = childObject.getArchimateElement();

                if(ConnectionPreferences.useNestedConnections()) {
                    for(IRelationship relation : ArchimateModelUtils.getSourceRelationships(parentElement)) {
                        if(relation.getTarget() == childElement && DiagramModelUtils.isNestedConnectionTypeRelationship(relation)) {
                            IDiagramModelArchimateConnection connection = DiagramModelUtils.findDiagramModelConnectionForRelation(parent.getDiagramModel(), relation);
                            // There's not one already there
                            if(connection == null) {
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