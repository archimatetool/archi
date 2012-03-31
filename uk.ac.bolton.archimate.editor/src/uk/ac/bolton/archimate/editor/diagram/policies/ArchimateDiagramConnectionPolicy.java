/*******************************************************************************
 * Copyright (c) 2010-11 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.policies;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import uk.ac.bolton.archimate.editor.diagram.commands.CreateDiagramConnectionCommand;
import uk.ac.bolton.archimate.editor.diagram.commands.DiagramCommandFactory;
import uk.ac.bolton.archimate.editor.diagram.commands.ReconnectDiagramConnectionCommand;
import uk.ac.bolton.archimate.editor.model.DiagramModelUtils;
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IDiagramModel;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateConnection;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;
import uk.ac.bolton.archimate.model.IDiagramModelConnection;
import uk.ac.bolton.archimate.model.IDiagramModelGroup;
import uk.ac.bolton.archimate.model.IDiagramModelNote;
import uk.ac.bolton.archimate.model.IDiagramModelObject;
import uk.ac.bolton.archimate.model.IDiagramModelReference;
import uk.ac.bolton.archimate.model.IRelationship;
import uk.ac.bolton.archimate.model.util.ArchimateModelUtils;

/**
 * Archimate Diagram Connection Policy
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateDiagramConnectionPolicy extends GraphicalNodeEditPolicy {
    
    @Override
    protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
        CreateDiagramConnectionCommand cmd = null;
        
        EClass classType = (EClass)request.getNewObjectType();
        IDiagramModelObject source = (IDiagramModelObject)getHost().getModel();
        
        // Plain Connection
        if(classType == IArchimatePackage.eINSTANCE.getDiagramModelConnection()) {
            cmd = new CreateLineConnectionCommand(request);
        }
        
        // Archimate Model Object Source
        else if(source instanceof IDiagramModelArchimateObject) {
            if(isValidConnectionSource((IDiagramModelArchimateObject)source, classType)) {
                cmd = new CreateArchimateConnectionCommand(request);
            }
        }
        
        if(cmd != null) {
            cmd.setSource(source);
            request.setStartCommand(cmd);
        }

        return cmd;
    }

    @Override
    protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
        // Pick up the command that was created in getConnectionCreateCommand(CreateConnectionRequest request)
        CreateDiagramConnectionCommand cmd = (CreateDiagramConnectionCommand)request.getStartCommand();
        IDiagramModelObject target = (IDiagramModelObject)getHost().getModel();
        cmd.setTarget(target);
        return cmd;
    }

    @Override
    protected Command getReconnectSourceCommand(ReconnectRequest request) {
        IDiagramModelConnection connection = (IDiagramModelConnection)request.getConnectionEditPart().getModel();
        IDiagramModelObject newSource = (IDiagramModelObject)getHost().getModel();
        
        // Re-connect ArchiMate Connection Source to Archimate Element
        if(connection instanceof IDiagramModelArchimateConnection && newSource instanceof IDiagramModelArchimateObject) {
            // Compound Command
            CompoundCommand result = new CompoundCommand();
            
            // Check for matching connections in this and other diagrams
            IRelationship relationship = ((IDiagramModelArchimateConnection)connection).getRelationship();
            IArchimateElement newSourceElement = ((IDiagramModelArchimateObject)newSource).getArchimateElement();

            for(IDiagramModel diagramModel : newSourceElement.getArchimateModel().getDiagramModels()) {
                for(IDiagramModelArchimateConnection matchingConnection : DiagramModelUtils.findDiagramModelConnectionsForRelation(diagramModel, relationship)) {
                    IDiagramModelArchimateObject matchingSource = null;

                    // Same Diagram so use the new source
                    if(newSource.getDiagramModel() == diagramModel) {
                        matchingSource = (IDiagramModelArchimateObject)newSource;
                    }
                    // Different Diagram so find a match
                    else {
                        List<IDiagramModelArchimateObject> list = DiagramModelUtils.findDiagramModelObjectsForElement(diagramModel, newSourceElement);
                        if(!list.isEmpty()) {
                            matchingSource = list.get(0);
                        }                            
                    }

                    // Does the new source exist on the diagram? Yes, reconnect
                    if(matchingSource != null) {
                        ReconnectConnectionCommand cmd2 = new ReconnectConnectionCommand(matchingConnection);
                        cmd2.setNewSource(matchingSource);
                        result.add(cmd2);
                    }
                    // No, so delete the matching connection
                    else {
                        result.add(DiagramCommandFactory.createDeleteDiagramConnectionCommand(matchingConnection));
                    }
                }
            }
            
            return result.unwrap();
        }
        
        // Re-connect Line Connection Source
        else {
            ReconnectConnectionCommand cmd = new ReconnectConnectionCommand(connection);
            cmd.setNewSource(newSource);
            return cmd;
        }
    }

    @Override
    protected Command getReconnectTargetCommand(ReconnectRequest request) {
        IDiagramModelConnection connection = (IDiagramModelConnection)request.getConnectionEditPart().getModel();
        IDiagramModelObject newTarget = (IDiagramModelObject)getHost().getModel();

        // Re-connect ArchiMate Connection Target to Archimate Element
        if(connection instanceof IDiagramModelArchimateConnection && newTarget instanceof IDiagramModelArchimateObject) {
            // Compound Command
            CompoundCommand result = new CompoundCommand();

            // Check for matching connections in this and other diagrams
            IRelationship relationship = ((IDiagramModelArchimateConnection)connection).getRelationship();
            IArchimateElement newTargetElement = ((IDiagramModelArchimateObject)newTarget).getArchimateElement();

            for(IDiagramModel diagramModel : newTargetElement.getArchimateModel().getDiagramModels()) {
                for(IDiagramModelArchimateConnection matchingConnection : DiagramModelUtils.findDiagramModelConnectionsForRelation(diagramModel, relationship)) {
                    IDiagramModelArchimateObject matchingTarget = null;
                    
                    // Same Diagram so use the new target
                    if(newTarget.getDiagramModel() == diagramModel) {
                        matchingTarget = (IDiagramModelArchimateObject)newTarget;
                    }
                    // Different Diagram so find a match
                    else {
                        List<IDiagramModelArchimateObject> list = DiagramModelUtils.findDiagramModelObjectsForElement(diagramModel, newTargetElement);
                        if(!list.isEmpty()) {
                            matchingTarget = list.get(0);
                        }                            
                    }
                    
                    // Does the new target exist on the diagram? Yes, reconnect
                    if(matchingTarget != null) {
                        ReconnectConnectionCommand cmd2 = new ReconnectConnectionCommand(matchingConnection);
                        cmd2.setNewTarget(matchingTarget);
                        result.add(cmd2);
                    }
                    // No, so delete the matching connection
                    else {
                        result.add(DiagramCommandFactory.createDeleteDiagramConnectionCommand(matchingConnection));
                    }
                }
            }
            
            return result.unwrap();
        }
        
        // Re-connect Line Connection Target
        else {
            ReconnectConnectionCommand cmd = new ReconnectConnectionCommand(connection);
            cmd.setNewTarget(newTarget);
            return cmd;
        }
    }

    
    // ==================================================================================================
    // ============================================= Commands ===========================================
    // ==================================================================================================
    
    /*
     * Command to create a line connection for notes
     */
    private class CreateLineConnectionCommand extends CreateDiagramConnectionCommand {
        public CreateLineConnectionCommand(CreateConnectionRequest request) {
            super(request);
        }
        
        @Override
        public boolean canExecute() {
            if(super.canExecute()) {
                EClass classType = (EClass)fRequest.getNewObjectType();
                return isValidConnection(fSource, fTarget, classType);
            }
            return false;
        }  
    }
    
    /*
     * Command to create an Archimate type connection.
     * Will also add and remove the associated Archimate Relationship to the model
     */
    private class CreateArchimateConnectionCommand extends CreateLineConnectionCommand {
        public CreateArchimateConnectionCommand(CreateConnectionRequest request) {
            super(request);
        }
        
        @Override
        public void execute() {
            super.execute();
            
            // Now add the relationship to the model
            ((IDiagramModelArchimateConnection)fConnection).addRelationshipToModel(null);
        }

        @Override
        public void redo() {
            super.redo();
            
            // Now add the relationship to the model
            ((IDiagramModelArchimateConnection)fConnection).addRelationshipToModel(null);
        }
        
        @Override
        public void undo() {
            super.undo();
            
            // Now remove the relationship from its folder
            ((IDiagramModelArchimateConnection)fConnection).removeRelationshipFromModel();
        }
    }
    
    /*
     * Command to reconnect a connection
     */
    private class ReconnectConnectionCommand extends ReconnectDiagramConnectionCommand {
        public ReconnectConnectionCommand(IDiagramModelConnection connection) {
            super(connection);
        }
        
        @Override
        protected boolean checkSourceConnection() {
            if(super.checkSourceConnection()) {
                if(fConnection instanceof IDiagramModelArchimateConnection) {
                    return isValidConnection(fNewSource, fOldTarget, ((IDiagramModelArchimateConnection)fConnection).getRelationship().eClass());
                }
                else {
                    return isValidConnection(fNewSource, fOldTarget, fConnection.eClass());
                }
            }
            
            return false;
        }
        
        @Override
        protected boolean checkTargetConnection() {
            if(super.checkTargetConnection()) {
                if(fConnection instanceof IDiagramModelArchimateConnection) {
                    return isValidConnection(fOldSource, fNewTarget, ((IDiagramModelArchimateConnection)fConnection).getRelationship().eClass());
                }
                else {
                    return isValidConnection(fOldSource, fNewTarget, fConnection.eClass());
                }
            }
            
            return false;
        }
    }

    
    // ==================================================================================================
    // ========================================= Connection Rules =======================================
    // ==================================================================================================
    
    /**
     * @return True if valid connection source for connection type
     */
    private boolean isValidConnectionSource(IDiagramModelArchimateObject source, EClass relationshipType) {
        // Special case if relationshipType == null. Means that the Magic connector is being used
        if(relationshipType == null) {
            return true;
        }

        return ArchimateModelUtils.isValidRelationshipStart(source.getArchimateElement(), relationshipType);
    }
    
    /**
     * @param source
     * @param target
     * @param relationshipType
     * @return True if valid connection source/target for connection type
     */
    private boolean isValidConnection(IDiagramModelObject source, IDiagramModelObject target, EClass relationshipType) {
        // Diagram Connection from/to notes/groups/diagram refs
        if(relationshipType == IArchimatePackage.eINSTANCE.getDiagramModelConnection()) {
            if(source == target) {
                return false;
            }
            if(source instanceof IDiagramModelArchimateObject && target instanceof IDiagramModelArchimateObject) {
                return false;
            }
            if(source instanceof IDiagramModelGroup || source instanceof IDiagramModelReference) {
                return !(target instanceof IDiagramModelArchimateObject);
            }
            if(source instanceof IDiagramModelArchimateObject) {
                return target instanceof IDiagramModelNote;
            }
            
            return true;
        }

        // Connection from Archimate object to Archimate object 
        if(source instanceof IDiagramModelArchimateObject && target instanceof IDiagramModelArchimateObject) {
            
            // Special case if relationshipType == null. Means that the Magic connector is being used
            if(relationshipType == null) {
                return true;
            }
            
            IArchimateElement sourceElement = ((IDiagramModelArchimateObject)source).getArchimateElement();
            IArchimateElement targetElement = ((IDiagramModelArchimateObject)target).getArchimateElement();
            return ArchimateModelUtils.isValidRelationship(sourceElement, targetElement, relationshipType);
        }
        
        return false;
    }
}
