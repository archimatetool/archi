/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.policies;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import uk.ac.bolton.archimate.editor.diagram.commands.CreateDiagramArchimateConnectionCommand;
import uk.ac.bolton.archimate.editor.diagram.commands.DiagramCommandFactory;
import uk.ac.bolton.archimate.editor.diagram.commands.ReconnectDiagramArchimateConnectionSourceCommand;
import uk.ac.bolton.archimate.editor.diagram.commands.ReconnectDiagramArchimateConnectionTargetCommand;
import uk.ac.bolton.archimate.editor.model.DiagramModelUtils;
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IDiagramModel;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateConnection;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;
import uk.ac.bolton.archimate.model.IRelationship;

/**
 * A policy which allows a component to be connected to another component via a connection
 * 
 * @author Phillip Beauvoir
 */
public class DiagramConnectionPolicy extends GraphicalNodeEditPolicy {
    
    @Override
    protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
        /*
         * Check the rules to see if a connection is allowed to be initiated.
         * If not, we won't even bother to create a Command.
         */
        IDiagramModelArchimateObject source = (IDiagramModelArchimateObject)getHost().getModel();
        EClass relationshipType = (EClass)request.getNewObjectType();
        
        if(!ConnectionManager.isValidConnectionSource(source, relationshipType)) {
            return null;
        }
        
        /*
         * Seems OK, so create a new Connection Command
         */
        CreateDiagramArchimateConnectionCommand cmd = new CreateDiagramArchimateConnectionCommand(request);
        cmd.setSource(source);
        request.setStartCommand(cmd);
        return cmd;
    }

    @Override
    protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
        // Pick up the command that was created in getConnectionCreateCommand(CreateConnectionRequest request)
        CreateDiagramArchimateConnectionCommand cmd = (CreateDiagramArchimateConnectionCommand)request.getStartCommand();
        cmd.setTarget((IDiagramModelArchimateObject)getHost().getModel());
        return cmd;
    }

    @Override
    protected Command getReconnectSourceCommand(ReconnectRequest request) {
        CompoundCommand result = new CompoundCommand();
        
        IDiagramModelArchimateConnection connection = (IDiagramModelArchimateConnection)request.getConnectionEditPart().getModel();
        IDiagramModelArchimateObject newSource = (IDiagramModelArchimateObject)getHost().getModel();
        
        IRelationship relationship = connection.getRelationship();
        IArchimateElement newSourceElement = newSource.getArchimateElement();
        
        // Command for this re-connection
        result.add(new ReconnectDiagramArchimateConnectionSourceCommand(connection, newSource));
        
        // Check for matching connections in other diagrams
        for(IDiagramModel diagramModel : newSourceElement.getArchimateModel().getDiagramModels()) {
            IDiagramModelArchimateConnection matchingConnection = (IDiagramModelArchimateConnection)DiagramModelUtils.findDiagramModelComponentForElement(diagramModel, relationship);
            if(matchingConnection != null && matchingConnection != connection) { // don't use original one
                // Does the new source exist on the diagram?
                IDiagramModelArchimateObject newSource2 = (IDiagramModelArchimateObject)DiagramModelUtils.findDiagramModelComponentForElement(diagramModel, newSourceElement);
                // Yes, reconnect
                if(newSource2 != null) {
                    result.add(new ReconnectDiagramArchimateConnectionSourceCommand(matchingConnection, newSource2));
                }
                // No, so delete the matching connection
                else {
                    result.add(DiagramCommandFactory.createDeleteDiagramConnectionCommand(matchingConnection));
                }
            }
        }
        
        return result.unwrap();
    }

    @Override
    protected Command getReconnectTargetCommand(ReconnectRequest request) {
        CompoundCommand result = new CompoundCommand();
        
        IDiagramModelArchimateConnection connection = (IDiagramModelArchimateConnection)request.getConnectionEditPart().getModel();
        IDiagramModelArchimateObject newTarget = (IDiagramModelArchimateObject)getHost().getModel();
        
        IRelationship relationship = connection.getRelationship();
        IArchimateElement newTargetElement = newTarget.getArchimateElement();
        
        // Command for this re-connection
        result.add(new ReconnectDiagramArchimateConnectionTargetCommand(connection, newTarget));

        // Check for matching connections in other diagrams
        for(IDiagramModel diagramModel : newTargetElement.getArchimateModel().getDiagramModels()) {
            IDiagramModelArchimateConnection matchingConnection = (IDiagramModelArchimateConnection)DiagramModelUtils.findDiagramModelComponentForElement(diagramModel, relationship);
            if(matchingConnection != null && matchingConnection != connection) { // don't use original one
                // Does the new target exist on the diagram?
                IDiagramModelArchimateObject newTarget2 = (IDiagramModelArchimateObject)DiagramModelUtils.findDiagramModelComponentForElement(diagramModel, newTargetElement);
                // Yes, reconnect
                if(newTarget2 != null) {
                    result.add(new ReconnectDiagramArchimateConnectionTargetCommand(matchingConnection, newTarget2));
                }
                // No, so delete the matching connection
                else {
                    result.add(DiagramCommandFactory.createDeleteDiagramConnectionCommand(matchingConnection));
                }
            }
        }
        
        return result.unwrap();
    }
}
