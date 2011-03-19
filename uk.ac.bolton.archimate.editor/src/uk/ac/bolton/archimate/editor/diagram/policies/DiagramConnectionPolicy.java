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

import uk.ac.bolton.archimate.editor.diagram.commands.CreateDiagramConnectionCommand;
import uk.ac.bolton.archimate.editor.diagram.commands.DiagramCommandFactory;
import uk.ac.bolton.archimate.editor.diagram.commands.ReconnectDiagramConnectionSourceCommand;
import uk.ac.bolton.archimate.editor.diagram.commands.ReconnectDiagramConnectionTargetCommand;
import uk.ac.bolton.archimate.editor.model.DiagramModelUtils;
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IDiagramModel;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateConnection;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;
import uk.ac.bolton.archimate.model.IDiagramModelConnection;
import uk.ac.bolton.archimate.model.IDiagramModelNote;
import uk.ac.bolton.archimate.model.IDiagramModelObject;
import uk.ac.bolton.archimate.model.IRelationship;
import uk.ac.bolton.archimate.model.util.ArchimateModelUtils;

/**
 * A policy which allows a component to be connected to another component via a connection
 * 
 * @author Phillip Beauvoir
 */
public class DiagramConnectionPolicy extends GraphicalNodeEditPolicy {
    
    @Override
    protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
        CreateDiagramConnectionCommand cmd = null;
        
        EClass classType = (EClass)request.getNewObjectType();
        IDiagramModelObject source = (IDiagramModelObject)getHost().getModel();
        
        // Plain Connection
        if(classType == IArchimatePackage.eINSTANCE.getDiagramModelConnection()) {
            cmd = new CreatePlainConnectionCommand(request);
        }
        
        // Archimate Model Object Source
        else if(source instanceof IDiagramModelArchimateObject) {
            if(!isValidConnectionSource((IDiagramModelArchimateObject)source, classType)) {
                return null;
            }
            
            cmd = new CreateArchimateConnectionCommand(request);
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
        // ArchiMate Connection
        if(request.getConnectionEditPart().getModel() instanceof IDiagramModelArchimateConnection) {
            CompoundCommand result = new CompoundCommand();
            
            IDiagramModelArchimateConnection connection = (IDiagramModelArchimateConnection)request.getConnectionEditPart().getModel();
            IDiagramModelArchimateObject newSource = (IDiagramModelArchimateObject)getHost().getModel();

            IRelationship relationship = connection.getRelationship();
            IArchimateElement newSourceElement = newSource.getArchimateElement();

            // Command for this re-connection
            result.add(new ReconnectArchimateConnectionSourceCommand(connection, newSource));

            // Check for matching connections in other diagrams
            for(IDiagramModel diagramModel : newSourceElement.getArchimateModel().getDiagramModels()) {
                IDiagramModelArchimateConnection matchingConnection = (IDiagramModelArchimateConnection)DiagramModelUtils.findDiagramModelComponentForElement(diagramModel, relationship);
                if(matchingConnection != null && matchingConnection != connection) { // don't use original one
                    // Does the new source exist on the diagram?
                    IDiagramModelArchimateObject newSource2 = (IDiagramModelArchimateObject)DiagramModelUtils.findDiagramModelComponentForElement(diagramModel, newSourceElement);
                    // Yes, reconnect
                    if(newSource2 != null) {
                        result.add(new ReconnectArchimateConnectionSourceCommand(matchingConnection, newSource2));
                    }
                    // No, so delete the matching connection
                    else {
                        result.add(DiagramCommandFactory.createDeleteDiagramConnectionCommand(matchingConnection));
                    }
                }
            }
            
            return result.unwrap();
        }
        
        // Plain Connection
        else if(request.getConnectionEditPart().getModel() instanceof IDiagramModelConnection) {
            IDiagramModelConnection connection = (IDiagramModelConnection)request.getConnectionEditPart().getModel();
            IDiagramModelObject newSource = (IDiagramModelObject)getHost().getModel();
            return new ReconnectPlainConnectionSourceCommand(connection, newSource);
        }
        
        return null;
    }

    @Override
    protected Command getReconnectTargetCommand(ReconnectRequest request) {
        // ArchiMate Connection
        if(request.getConnectionEditPart().getModel() instanceof IDiagramModelArchimateConnection) {
            CompoundCommand result = new CompoundCommand();

            IDiagramModelArchimateConnection connection = (IDiagramModelArchimateConnection)request.getConnectionEditPart().getModel();
            IDiagramModelArchimateObject newTarget = (IDiagramModelArchimateObject)getHost().getModel();

            IRelationship relationship = connection.getRelationship();
            IArchimateElement newTargetElement = newTarget.getArchimateElement();

            // Command for this re-connection
            result.add(new ReconnectArchimateConnectionTargetCommand(connection, newTarget));

            // Check for matching connections in other diagrams
            for(IDiagramModel diagramModel : newTargetElement.getArchimateModel().getDiagramModels()) {
                IDiagramModelArchimateConnection matchingConnection = (IDiagramModelArchimateConnection)DiagramModelUtils.findDiagramModelComponentForElement(diagramModel, relationship);
                if(matchingConnection != null && matchingConnection != connection) { // don't use original one
                    // Does the new target exist on the diagram?
                    IDiagramModelArchimateObject newTarget2 = (IDiagramModelArchimateObject)DiagramModelUtils.findDiagramModelComponentForElement(diagramModel, newTargetElement);
                    // Yes, reconnect
                    if(newTarget2 != null) {
                        result.add(new ReconnectArchimateConnectionTargetCommand(matchingConnection, newTarget2));
                    }
                    // No, so delete the matching connection
                    else {
                        result.add(DiagramCommandFactory.createDeleteDiagramConnectionCommand(matchingConnection));
                    }
                }
            }
            
            return result.unwrap();
        }
        
        // Plain Connection
        else if(request.getConnectionEditPart().getModel() instanceof IDiagramModelConnection) {
            IDiagramModelConnection connection = (IDiagramModelConnection)request.getConnectionEditPart().getModel();
            IDiagramModelObject newTarget = (IDiagramModelObject)getHost().getModel();
            return new ReconnectPlainConnectionTargetCommand(connection, newTarget);
        }
        
        return null;
    }


    
    
    
    // ==================================================================================================
    // ============================================= Commands ===========================================
    // ==================================================================================================
    
    /*
     * Command to create a plain connection
     */
    private class CreatePlainConnectionCommand extends CreateDiagramConnectionCommand {
        public CreatePlainConnectionCommand(CreateConnectionRequest request) {
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
    private class CreateArchimateConnectionCommand extends CreatePlainConnectionCommand {
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
     * Command to reconnect a plain connection's source
     */
    private class ReconnectPlainConnectionSourceCommand extends ReconnectDiagramConnectionSourceCommand {
        public ReconnectPlainConnectionSourceCommand(IDiagramModelConnection connection, IDiagramModelObject newSource) {
            super(connection, newSource);
        }

        @Override
        public boolean canExecute() {
            if(super.canExecute()) {
                return isValidConnection(fNewSource, fTarget, fConnection.eClass());
            }
            return false;
        }
    }

    /*
     * Command to reconnect a plain connection's target
     */
    private class ReconnectPlainConnectionTargetCommand extends ReconnectDiagramConnectionTargetCommand {
        public ReconnectPlainConnectionTargetCommand(IDiagramModelConnection connection, IDiagramModelObject newTarget) {
            super(connection, newTarget);
        }

        @Override
        public boolean canExecute() {
            if(super.canExecute()) {
                return isValidConnection(fSource, fNewTarget, fConnection.eClass());
            }
            return false;
        }
    }

    /*
     * Command to reconnect an Archimate type connection's source
     */
    private class ReconnectArchimateConnectionSourceCommand extends ReconnectDiagramConnectionSourceCommand {
        public ReconnectArchimateConnectionSourceCommand(IDiagramModelArchimateConnection connection, IDiagramModelObject newSource) {
            super(connection, newSource);
        }

        @Override
        public boolean canExecute() {
            if(super.canExecute()) {
                return isValidConnection(fNewSource, fTarget, ((IDiagramModelArchimateConnection)fConnection).getRelationship().eClass());
            }
            return false;
        }
    }

    /*
     * Command to reconnect an Archimate type connection's target
     */
    private class ReconnectArchimateConnectionTargetCommand extends ReconnectDiagramConnectionTargetCommand {
        public ReconnectArchimateConnectionTargetCommand(IDiagramModelArchimateConnection connection, IDiagramModelObject newTarget) {
            super(connection, newTarget);
        }

        @Override
        public boolean canExecute() {
            if(super.canExecute()) {
                return isValidConnection(fSource, fNewTarget, ((IDiagramModelArchimateConnection)fConnection).getRelationship().eClass());
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
        
        // Connection from/to notes
        if(source instanceof IDiagramModelNote || target instanceof IDiagramModelNote) {
            return relationshipType == IArchimatePackage.eINSTANCE.getDiagramModelConnection();
        }
        
        return false;
    }
    
    /**
     * @return True if connection type exists between source and target
     */
    @SuppressWarnings("unused")
    private boolean hasExistingConnectionType(IDiagramModelObject source, IDiagramModelObject target, EClass relationshipType) {
        for(IDiagramModelConnection connection : source.getSourceConnections()) {
            if(connection instanceof IDiagramModelArchimateConnection && connection.getTarget().equals(target)) {
                EClass type = ((IDiagramModelArchimateConnection)connection).getRelationship().eClass();
                return type.equals(relationshipType);
            }
        }
        return false;
    }
    
    /**
     * Check for cycles.  Return true if there is a cycle.
     */
    @SuppressWarnings("unused")
    private boolean hasCycle(IDiagramModelObject source, IDiagramModelObject target) {
        for(IDiagramModelConnection connection : source.getTargetConnections()) {
            if(connection.getSource().equals(target)) {
                return true;
            }
            if(hasCycle(connection.getSource(), target)) {
                return true;
            }
        }
        return false;
    }

}
