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
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import com.archimatetool.editor.diagram.commands.CreateDiagramArchimateConnectionWithDialogCommand;
import com.archimatetool.editor.diagram.commands.CreateDiagramConnectionCommand;
import com.archimatetool.editor.diagram.commands.DiagramCommandFactory;
import com.archimatetool.editor.diagram.commands.ReconnectDiagramConnectionCommand;
import com.archimatetool.editor.diagram.figures.ITargetFeedbackFigure;
import com.archimatetool.editor.model.DiagramModelUtils;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IConnectable;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateComponent;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
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
        
        // The re-connected object
        IConnectable newObject = (IConnectable)getHost().getModel();

        // Get the type of connection (plain) or relationship (if archimate connection) and check if it is valid
        EClass type = connection.eClass();
        if(connection instanceof IDiagramModelArchimateConnection) {
            type = ((IDiagramModelArchimateConnection)connection).getArchimateRelationship().eClass();
        }

        if(isSourceCommand) {
            if(!isValidConnection(newObject, connection.getTarget(), type)) {
                return null;
            }
        }
        else {
            if(!isValidConnection(connection.getSource(), newObject, type)) {
                return null;
            }
        }
        
        /*
         * Re-connect ArchiMate Connection to Archimate Component
         * In this case we have to check for matching occurences on all diagrams
         */
        if(connection instanceof IDiagramModelArchimateConnection && newObject instanceof IDiagramModelArchimateComponent) {
            IArchimateRelationship relationship = ((IDiagramModelArchimateConnection)connection).getArchimateRelationship();
            IArchimateConcept newConcept = ((IDiagramModelArchimateComponent)newObject).getArchimateConcept();
            
            // Compound Command
            CompoundCommand result = new CompoundCommand();

            // Check for matching connections in this and other diagrams
            for(IDiagramModel diagramModel : newConcept.getArchimateModel().getDiagramModels()) {
                for(IDiagramModelArchimateConnection matchingConnection : DiagramModelUtils.findDiagramModelConnectionsForRelation(diagramModel, relationship)) {
                    IDiagramModelArchimateComponent matchingComponent = null;
                    
                    // Same Diagram so use the new target
                    if(newObject.getDiagramModel() == diagramModel) {
                        matchingComponent = (IDiagramModelArchimateComponent)newObject;
                    }
                    // Different Diagram so find a match
                    else {
                        List<IDiagramModelArchimateComponent> list = DiagramModelUtils.findDiagramModelComponentsForArchimateConcept(diagramModel, newConcept);
                        if(!list.isEmpty()) {
                            matchingComponent = list.get(0);
                        }                            
                    }
                    
                    // Does the new object exist on the diagram? Yes, reconnect
                    if(matchingComponent != null) {
                        ReconnectDiagramConnectionCommand cmd = new ReconnectDiagramConnectionCommand(matchingConnection);
                        if(isSourceCommand) {
                            cmd.setNewSource(matchingComponent);
                        }
                        else {
                            cmd.setNewTarget(matchingComponent);
                        }
                        result.add(cmd);
                    }
                    // No, so delete the matching connection
                    else {
                        result.add(DiagramCommandFactory.createDeleteDiagramConnectionCommand(matchingConnection));
                    }
                }
            }
            
            return result.unwrap();
        }
        
        // Re-connect other cases
        else {
            ReconnectDiagramConnectionCommand cmd = new ReconnectDiagramConnectionCommand(connection);
            
            if(isSourceCommand) {
                cmd.setNewSource(newObject);
            }
            else {
                cmd.setNewTarget(newObject);
            }
            
            return cmd;
        }
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
        
        // Archimate Element source
        if(source instanceof IDiagramModelArchimateObject) {
            IDiagramModelArchimateObject dmo = (IDiagramModelArchimateObject)source;
            return ArchimateModelUtils.isValidRelationshipStart(dmo.getArchimateConcept(), relationshipType);
        }
        
        // Archimate Relationship source
        if(source instanceof IDiagramModelArchimateConnection) {
            return relationshipType == IArchimatePackage.eINSTANCE.getAssociationRelationship();
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
         * 
         */
        if(relationshipType == IArchimatePackage.eINSTANCE.getDiagramModelConnection()) {
            // Not circular
            if(source == target) {
                return false;
            }
            // Notes
            if(source instanceof IDiagramModelNote || target instanceof IDiagramModelNote) {
                return true;
            }
            // Groups
            if(source instanceof IDiagramModelGroup || target instanceof IDiagramModelGroup) {
                return !(source instanceof IDiagramModelArchimateComponent) && !(target instanceof IDiagramModelArchimateComponent);
            }
            // Diagram Refs
            if(source instanceof IDiagramModelReference || target instanceof IDiagramModelReference) {
                return !(source instanceof IDiagramModelArchimateComponent) && !(target instanceof IDiagramModelArchimateComponent);
            }
            
            return false;
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
        
        // TODO: A3 From/To Connections - put this in the relationship matrix?
        
        // Can connect Association relationship from element->relation or relation->element
        if((source instanceof IDiagramModelArchimateConnection && target instanceof IDiagramModelArchimateObject) || 
                (source instanceof IDiagramModelArchimateObject && target instanceof IDiagramModelArchimateConnection)) {
            
            if(relationshipType == IArchimatePackage.eINSTANCE.getAssociationRelationship()) {
                return true;
            }
            
        }
        
        // Source is element and target is relationship
        if(source instanceof IDiagramModelArchimateObject && target instanceof IDiagramModelArchimateConnection) {
            if(relationshipType == IArchimatePackage.eINSTANCE.getAggregationRelationship() ||
                    relationshipType == IArchimatePackage.eINSTANCE.getCompositionRelationship()) {
                
                IArchimateElement sourceElement = ((IDiagramModelArchimateObject)source).getArchimateElement();
                
                // Can connect Aggregation or Composition from Plateau to any kind of relationship
                if(sourceElement.eClass() == IArchimatePackage.eINSTANCE.getPlateau()) {
                    return true;
                }
                
                // Can connect Aggregation or Composition from Grouping to any kind of relationship
                if(sourceElement.eClass() == IArchimatePackage.eINSTANCE.getGrouping()) {
                    return true;
                }
            }
        }
        
        return false;
    }
}
