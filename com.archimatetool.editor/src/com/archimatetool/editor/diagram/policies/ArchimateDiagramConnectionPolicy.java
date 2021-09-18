/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.policies;

import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.diagram.commands.CreateDiagramArchimateConnectionWithDialogCommand;
import com.archimatetool.editor.diagram.commands.CreateDiagramConnectionCommand;
import com.archimatetool.editor.diagram.commands.DiagramCommandFactory;
import com.archimatetool.editor.diagram.commands.ReconnectDiagramConnectionCommand;
import com.archimatetool.editor.diagram.figures.ITargetFeedbackFigure;
import com.archimatetool.editor.model.DiagramModelUtils;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IConnectable;
import com.archimatetool.model.IDiagramModelArchimateComponent;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelGroup;
import com.archimatetool.model.IDiagramModelNote;
import com.archimatetool.model.IDiagramModelReference;
import com.archimatetool.model.util.ArchimateModelUtils;


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
        IConnectable source = (IConnectable)getHost().getModel();
        
        // Plain Connection
        if(classType == IArchimatePackage.eINSTANCE.getDiagramModelConnection()) {
            if(isValidConnectionSource(source, classType)) {
                cmd = new CreateDiagramConnectionCommand(request);
            }
        }
        
        // Archimate Model Component Source
        else if(source instanceof IDiagramModelArchimateComponent) {
            if(isValidConnectionSource(source, classType)) {
                cmd = new CreateDiagramArchimateConnectionWithDialogCommand(request);
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
        IConnectable source = (IConnectable)request.getSourceEditPart().getModel();
        IConnectable target = (IConnectable)getHost().getModel();
        EClass relationshipType = (EClass)request.getNewObjectType();
        
        CreateDiagramConnectionCommand cmd = null;
        
        if(isValidConnection(source, target, relationshipType)) {
            // Pick up the command that was created in getConnectionCreateCommand(CreateConnectionRequest request)
            cmd = (CreateDiagramConnectionCommand)request.getStartCommand();
            cmd.setTarget(target);
        }
        
        return cmd;
    }

    @Override
    protected Command getReconnectSourceCommand(ReconnectRequest request) {
        return getReconnectCommand(request, true);
    }

    @Override
    protected Command getReconnectTargetCommand(ReconnectRequest request) {
        return getReconnectCommand(request, false);
    }

    /**
     * Create a ReconnectCommand
     */
    protected Command getReconnectCommand(ReconnectRequest request, boolean isSourceCommand) {
        IDiagramModelConnection connection = (IDiagramModelConnection)request.getConnectionEditPart().getModel();
        
        // The re-connected end component
        IConnectable newComponent = (IConnectable)getHost().getModel();

        // Get the type of connection (plain) or relationship (if archimate connection) and check if it is valid
        EClass type = connection.eClass();
        if(connection instanceof IDiagramModelArchimateConnection) {
            type = ((IDiagramModelArchimateConnection)connection).getArchimateRelationship().eClass();
        }

        if(isSourceCommand) {
            if(!isValidConnection(newComponent, connection.getTarget(), type)) {
                return null;
            }
        }
        else {
            if(!isValidConnection(connection.getSource(), newComponent, type)) {
                return null;
            }
        }
        
        // Archimate type reconnection
        if(connection instanceof IDiagramModelArchimateConnection && newComponent instanceof IDiagramModelArchimateComponent) {
            return createArchimateReconnectCommand((IDiagramModelArchimateConnection)connection, (IDiagramModelArchimateComponent)newComponent, isSourceCommand);
        }
        
        // Plain reconnection
        return createReconnectCommand(connection, newComponent, isSourceCommand);
    }
    
    private Command createArchimateReconnectCommand(IDiagramModelArchimateConnection connection, IDiagramModelArchimateComponent dmc, boolean isSourceCommand) {
        IArchimateRelationship relationship = connection.getArchimateRelationship();
        IArchimateConcept newConcept = dmc.getArchimateConcept();

        CompoundCommand cmd = new CompoundCommand() {
            boolean affectsOtherViews = false;
            
            @Override
            public void execute() {
                // Lazily create commands
                createCommands();
                
                super.execute();
                
                // Show message that it affected other Viewss
                if(affectsOtherViews && ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.SHOW_WARNING_ON_RECONNECT)) {
                    boolean answer = MessageDialog.openQuestion(
                            Display.getDefault().getActiveShell(),
                            Messages.ArchimateDiagramConnectionPolicy_0,
                            Messages.ArchimateDiagramConnectionPolicy_1
                            + "\n\n" + //$NON-NLS-1$
                            Messages.ArchimateDiagramConnectionPolicy_2);
                    
                    if(!answer) {
                        // We have to call undo() later in the thread as the Command is not yet on the CommandStack
                        Display.getDefault().asyncExec(() -> ((CommandStack)connection.getAdapter(CommandStack.class)).undo());
                    }
                }
            }
            
            @Override
            public boolean canExecute() {
                // Can't reconnect to the same dmc
                return isSourceCommand ? connection.getSource() != dmc : connection.getTarget() != dmc;
            }
            
            // Add commands for all instances of diagram connections
            private void createCommands() {
                for(IDiagramModelArchimateConnection matchingConnection : relationship.getReferencingDiagramConnections()) {
                    // The same diagram
                    if(matchingConnection.getDiagramModel() == connection.getDiagramModel()) {
                        // If we are reconnecting to a dmc with a different concept then reconnect all instances on this diagram
                        if(isNewConnection(matchingConnection, dmc, isSourceCommand)) {
                            add(createReconnectCommand(matchingConnection, dmc, isSourceCommand));
                        }
                        // Else if we are reconnecting to a dmc with the same concept then reconnect only this one instance of the connection
                        else if(connection == matchingConnection) {
                            add(createReconnectCommand(matchingConnection, dmc, isSourceCommand));
                        }
                    }
                    // A different diagram
                    else {
                        // Does the new target concept exist on the diagram?
                        List<IDiagramModelArchimateComponent> list = DiagramModelUtils.findDiagramModelComponentsForArchimateConcept(matchingConnection.getDiagramModel(), newConcept);

                        // Yes, so reconnect to it *if* it is different than the existing concept
                        if(!list.isEmpty()) {
                            // Get the first instance of the new component
                            IDiagramModelArchimateComponent newComponent = list.get(0);

                            // If the instance's concept is different than the original concept then reconnect
                            if(isNewConnection(matchingConnection, newComponent, isSourceCommand)) {
                                Command cmd = createReconnectCommand(matchingConnection, newComponent, isSourceCommand);
                                if(cmd.canExecute()) {
                                    affectsOtherViews = true;
                                    add(cmd);
                                }
                            }
                        }

                        // No, so delete the matching connection
                        else {
                            add(DiagramCommandFactory.createDeleteDiagramConnectionCommand(matchingConnection));
                            affectsOtherViews = true;
                        }
                    }
                }
            }
        };
        
        return cmd;
    }
    
    /**
     * Return true if the concept in dmc is not already connected to the concept in connection
     */
    private boolean isNewConnection(IDiagramModelArchimateConnection connection, IDiagramModelArchimateComponent dmc, boolean isSourceCommand) {
        if(isSourceCommand) {
            return !dmc.getArchimateConcept().getSourceRelationships().contains(connection.getArchimateConcept());
        }
        else {
            return !dmc.getArchimateConcept().getTargetRelationships().contains(connection.getArchimateConcept());
        }
    }

    private Command createReconnectCommand(IDiagramModelConnection connection, IConnectable connectable, boolean isSourceCommand) {
        ReconnectDiagramConnectionCommand cmd = new ReconnectDiagramConnectionCommand(connection);
        
        if(isSourceCommand) {
            cmd.setNewSource(connectable);
        }
        else {
            cmd.setNewTarget(connectable);
        }
        
        return cmd;
    }

    @Override
    public void eraseTargetFeedback(Request request) {
        IFigure figure = ((GraphicalEditPart)getHost()).getFigure();
        if(figure instanceof ITargetFeedbackFigure) {
            ((ITargetFeedbackFigure)figure).eraseTargetFeedback();
        }
    }
    
    @Override
    public void showTargetFeedback(Request request) {
        if(request.getType().equals(RequestConstants.REQ_CONNECTION_START)) {
            if(getConnectionCreateCommand((CreateConnectionRequest)request) != null) {
                showTargetFeedback();
            }
        }
        
        if(request.getType().equals(RequestConstants.REQ_CONNECTION_END)) {
            if(getConnectionCompleteCommand((CreateConnectionRequest)request) != null) {
                showTargetFeedback();
            }
        }

        if(request.getType().equals(RequestConstants.REQ_RECONNECT_SOURCE)) {
            if(getReconnectSourceCommand((ReconnectRequest)request) != null) {
                showTargetFeedback();
            }
        }

        if(request.getType().equals(RequestConstants.REQ_RECONNECT_TARGET)) {
            if(getReconnectTargetCommand((ReconnectRequest)request) != null) {
                showTargetFeedback();
            }
        }
    }
    
    private void showTargetFeedback() {
        IFigure figure = ((GraphicalEditPart)getHost()).getFigure();
        if(figure instanceof ITargetFeedbackFigure) {
            ((ITargetFeedbackFigure)figure).showTargetFeedback();
        }
    }
    
    
    // ==================================================================================================
    // ========================================= Connection Rules =======================================
    // ==================================================================================================
    
    /**
     * @return True if valid source for a connection type
     */
    static boolean isValidConnectionSource(IConnectable source, EClass relationshipType) {
        // Special case if relationshipType == null. Means that the Magic connector is being used
        if(relationshipType == null) {
            return true;
        }

        // This first: Diagram Connection from/to notes/groups/diagram refs
        if(relationshipType == IArchimatePackage.eINSTANCE.getDiagramModelConnection()) {
            return true;
        }
        
        // Archimate Concept source
        if(source instanceof IDiagramModelArchimateComponent) {
            IDiagramModelArchimateComponent dmc = (IDiagramModelArchimateComponent)source;
            return ArchimateModelUtils.isValidRelationshipStart(dmc.getArchimateConcept(), relationshipType);
        }
        
        return false;
    }
    
    /**
     * @param source
     * @param target
     * @param relationshipType
     * @return True if valid connection source/target for connection type
     */
    static boolean isValidConnection(IConnectable source, IConnectable target, EClass relationshipType) {
        /*
         * Diagram Connection from/to notes/groups/diagram refs.
         * Allowed between notes, visual groups, diagram refs and ArchiMate components
         */
        if(relationshipType == IArchimatePackage.eINSTANCE.getDiagramModelConnection()) {
            // Not circular
            // Edit - allowed for JB!
            //if(source == target) {
                //return false;
            //}
            // Notes
            if(source instanceof IDiagramModelNote || target instanceof IDiagramModelNote) {
                return true;
            }
            // Groups
            if(source instanceof IDiagramModelGroup || target instanceof IDiagramModelGroup) {
                // Edit - allowed for JB!
                return true;
                //return !(source instanceof IDiagramModelArchimateComponent) && !(target instanceof IDiagramModelArchimateComponent);
            }
            // Diagram Refs
            if(source instanceof IDiagramModelReference || target instanceof IDiagramModelReference) {
                // Edit - allowed for JB!
                return true;
                //return !(source instanceof IDiagramModelArchimateComponent) && !(target instanceof IDiagramModelArchimateComponent);
            }
            
            return false;
        }

        // Connection from Archimate concept to Archimate concept (but not from relation to relation)
        if((source instanceof IDiagramModelArchimateComponent && target instanceof IDiagramModelArchimateComponent) && 
              !(source instanceof IDiagramModelArchimateConnection && target instanceof IDiagramModelArchimateConnection)) {
            
            // Special case if relationshipType == null. Means that the Magic connector is being used
            if(relationshipType == null) {
                return true;
            }
            
            IArchimateConcept sourceConcept = ((IDiagramModelArchimateComponent)source).getArchimateConcept();
            IArchimateConcept targetConcept = ((IDiagramModelArchimateComponent)target).getArchimateConcept();
            
            return ArchimateModelUtils.isValidRelationship(sourceConcept, targetConcept, relationshipType);
        }
        
        return false;
    }
}
