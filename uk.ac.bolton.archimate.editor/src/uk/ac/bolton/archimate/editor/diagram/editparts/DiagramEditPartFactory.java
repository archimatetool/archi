/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.editparts;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import uk.ac.bolton.archimate.editor.diagram.editparts.application.ApplicationCollaborationEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.application.ApplicationComponentEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.application.ApplicationDataObjectEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.application.ApplicationFunctionEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.application.ApplicationInteractionEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.application.ApplicationInterfaceEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.application.ApplicationServiceEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.business.BusinessActivityEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.business.BusinessActorEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.business.BusinessCollaborationEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.business.BusinessContractEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.business.BusinessEventEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.business.BusinessFunctionEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.business.BusinessInteractionEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.business.BusinessInterfaceEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.business.BusinessMeaningEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.business.BusinessObjectEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.business.BusinessProcessEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.business.BusinessProductEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.business.BusinessRepresentationEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.business.BusinessRoleEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.business.BusinessServiceEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.business.BusinessValueEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.connections.AccessConnectionEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.connections.AggregationConnectionEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.connections.AssignmentConnectionEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.connections.AssociationConnectionEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.connections.CompositionConnectionEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.connections.FlowConnectionEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.connections.RealisationConnectionEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.connections.SpecialisationConnectionEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.connections.TriggeringConnectionEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.connections.UsedByConnectionEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.diagram.DiagramModelReferenceEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.diagram.EmptyEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.diagram.GroupEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.diagram.LineConnectionEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.diagram.NoteEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.junctions.AndJunctionEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.junctions.JunctionEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.junctions.OrJunctionEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.technology.TechnologyArtifactEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.technology.TechnologyCommunicationPathEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.technology.TechnologyDeviceEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.technology.TechnologyInfrastructureInterfaceEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.technology.TechnologyInfrastructureServiceEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.technology.TechnologyNetworkEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.technology.TechnologyNodeEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.technology.TechnologySystemSoftwareEditPart;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IDiagramModel;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateConnection;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;
import uk.ac.bolton.archimate.model.IDiagramModelConnection;
import uk.ac.bolton.archimate.model.IDiagramModelGroup;
import uk.ac.bolton.archimate.model.IDiagramModelNote;
import uk.ac.bolton.archimate.model.IDiagramModelReference;

/**
 * Factory for creating Edit Parts based on graphical domain model objects
 * 
 * @author Phillip Beauvoir
 */
public class DiagramEditPartFactory
implements EditPartFactory {

    public EditPart createEditPart(EditPart context, Object model) {
        EditPart child = null;
        
        if(model == null) {
            return null;
        }
        
        // Main Diagram Edit Part
        if(model instanceof IDiagramModel) {
            child = new DiagramPart();
        }
        
        // Note
        else if(model instanceof IDiagramModelNote) {
            child = new NoteEditPart();
        }
        
        // Group
        else if(model instanceof IDiagramModelGroup) {
            child = new GroupEditPart();
        }
        
        // Diagram Model Reference
        else if(model instanceof IDiagramModelReference) {
            child = new DiagramModelReferenceEditPart();
        }
        
        // Archimate Model Element Parts
        else if(model instanceof IDiagramModelArchimateObject) {
            IDiagramModelArchimateObject modelobject = (IDiagramModelArchimateObject)model;
            
            switch(modelobject.getArchimateElement().eClass().getClassifierID()) {
                // Junctions
                case IArchimatePackage.JUNCTION:
                    child = new JunctionEditPart();
                    break;
                case IArchimatePackage.AND_JUNCTION:
                    child = new AndJunctionEditPart();
                    break;
                case IArchimatePackage.OR_JUNCTION:
                    child = new OrJunctionEditPart();
                    break;
                    
                // Business
                case IArchimatePackage.BUSINESS_ACTIVITY:
                    child = new BusinessActivityEditPart();
                    break;
                case IArchimatePackage.BUSINESS_ACTOR:
                    child = new BusinessActorEditPart();
                    break;
                case IArchimatePackage.BUSINESS_COLLABORATION:
                    child = new BusinessCollaborationEditPart();
                    break;
                case IArchimatePackage.CONTRACT:
                    child = new BusinessContractEditPart();
                    break;
                case IArchimatePackage.BUSINESS_EVENT:
                    child = new BusinessEventEditPart();
                    break;
                case IArchimatePackage.BUSINESS_FUNCTION:
                    child = new BusinessFunctionEditPart();
                    break;
                case IArchimatePackage.BUSINESS_INTERACTION:
                    child = new BusinessInteractionEditPart();
                    break;
                case IArchimatePackage.BUSINESS_INTERFACE:
                    child = new BusinessInterfaceEditPart();
                    break;
                case IArchimatePackage.MEANING:
                    child = new BusinessMeaningEditPart();
                    break;
                case IArchimatePackage.BUSINESS_OBJECT:
                    child = new BusinessObjectEditPart();
                    break;
                case IArchimatePackage.BUSINESS_PROCESS:
                    child = new BusinessProcessEditPart();
                    break;
                case IArchimatePackage.PRODUCT:
                    child = new BusinessProductEditPart();
                    break;
                case IArchimatePackage.REPRESENTATION:
                    child = new BusinessRepresentationEditPart();
                    break;
                case IArchimatePackage.BUSINESS_ROLE:
                    child = new BusinessRoleEditPart();
                    break;
                case IArchimatePackage.BUSINESS_SERVICE:
                    child = new BusinessServiceEditPart();
                    break;
                case IArchimatePackage.VALUE:
                    child = new BusinessValueEditPart();
                    break;
                    
                // Application
                case IArchimatePackage.APPLICATION_COMPONENT:
                    child = new ApplicationComponentEditPart();
                    break;
                case IArchimatePackage.APPLICATION_COLLABORATION:
                    child = new ApplicationCollaborationEditPart();
                    break;
                case IArchimatePackage.APPLICATION_FUNCTION:
                    child = new ApplicationFunctionEditPart();
                    break;
                case IArchimatePackage.DATA_OBJECT:
                    child = new ApplicationDataObjectEditPart();
                    break;
                case IArchimatePackage.APPLICATION_INTERACTION:
                    child = new ApplicationInteractionEditPart();
                    break;
                case IArchimatePackage.APPLICATION_INTERFACE:
                    child = new ApplicationInterfaceEditPart();
                    break;
                case IArchimatePackage.APPLICATION_SERVICE:
                    child = new ApplicationServiceEditPart();
                    break;
                    
                // Technology
                case IArchimatePackage.COMMUNICATION_PATH:
                    child = new TechnologyCommunicationPathEditPart();
                    break;
                case IArchimatePackage.NETWORK:
                    child = new TechnologyNetworkEditPart();
                    break;
                case IArchimatePackage.INFRASTRUCTURE_INTERFACE:
                    child = new TechnologyInfrastructureInterfaceEditPart();
                    break;
                case IArchimatePackage.INFRASTRUCTURE_SERVICE:
                    child = new TechnologyInfrastructureServiceEditPart();
                    break;
                case IArchimatePackage.SYSTEM_SOFTWARE:
                    child = new TechnologySystemSoftwareEditPart();
                    break;
                case IArchimatePackage.NODE:
                    child = new TechnologyNodeEditPart();
                    break;
                case IArchimatePackage.DEVICE:
                    child = new TechnologyDeviceEditPart();
                    break;
                case IArchimatePackage.ARTIFACT:
                    child = new TechnologyArtifactEditPart();
                    break;
                    
                default:
                    child = new EmptyEditPart();
            }
            
        }
        
        // Connections
        else if(model instanceof IDiagramModelArchimateConnection) {
            IDiagramModelArchimateConnection modelobject = (IDiagramModelArchimateConnection)model;
            
            switch(modelobject.getRelationship().eClass().getClassifierID()) {
                case IArchimatePackage.ACCESS_RELATIONSHIP:
                    child = new AccessConnectionEditPart();
                    break;
                case IArchimatePackage.AGGREGATION_RELATIONSHIP:
                    child = new AggregationConnectionEditPart();
                    break;
                case IArchimatePackage.ASSIGNMENT_RELATIONSHIP:
                    child = new AssignmentConnectionEditPart();
                    break;
                case IArchimatePackage.ASSOCIATION_RELATIONSHIP:
                    child = new AssociationConnectionEditPart();
                    break;
                case IArchimatePackage.COMPOSITION_RELATIONSHIP:
                    child = new CompositionConnectionEditPart();
                    break;
                case IArchimatePackage.FLOW_RELATIONSHIP:
                    child = new FlowConnectionEditPart();
                    break;
                case IArchimatePackage.REALISATION_RELATIONSHIP:
                    child = new RealisationConnectionEditPart();
                    break;
                case IArchimatePackage.SPECIALISATION_RELATIONSHIP:
                    child = new SpecialisationConnectionEditPart();
                    break;
                case IArchimatePackage.TRIGGERING_RELATIONSHIP:
                    child = new TriggeringConnectionEditPart();
                    break;
                case IArchimatePackage.USED_BY_RELATIONSHIP:
                    child = new UsedByConnectionEditPart();
                    break;
                    
                default:
                    child = new TriggeringConnectionEditPart();
            }
        }
        
        // Plain Connections
        else if(model instanceof IDiagramModelConnection) {
            child = new LineConnectionEditPart();
        }
        
        /*
         * It's better to return an Empty Edit Part in case of a corrupt model.
         * Returning null is disastrous and means the Diagram View won't open.
         */
        else {
            child = new EmptyEditPart();
        }
        
        // Set the Model in the Edit part
        child.setModel(model);
        
        return child;
    }

}
