/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.swt.graphics.Color;

import com.archimatetool.editor.diagram.figures.connections.AccessConnectionFigure;
import com.archimatetool.editor.diagram.figures.connections.AggregationConnectionFigure;
import com.archimatetool.editor.diagram.figures.connections.AssignmentConnectionFigure;
import com.archimatetool.editor.diagram.figures.connections.AssociationConnectionFigure;
import com.archimatetool.editor.diagram.figures.connections.CompositionConnectionFigure;
import com.archimatetool.editor.diagram.figures.connections.FlowConnectionFigure;
import com.archimatetool.editor.diagram.figures.connections.InfluenceConnectionFigure;
import com.archimatetool.editor.diagram.figures.connections.RealizationConnectionFigure;
import com.archimatetool.editor.diagram.figures.connections.ServingConnectionFigure;
import com.archimatetool.editor.diagram.figures.connections.SpecializationConnectionFigure;
import com.archimatetool.editor.diagram.figures.connections.TriggeringConnectionFigure;
import com.archimatetool.editor.diagram.figures.elements.ApplicationComponentFigure;
import com.archimatetool.editor.diagram.figures.elements.ArtifactFigure;
import com.archimatetool.editor.diagram.figures.elements.AssessmentFigure;
import com.archimatetool.editor.diagram.figures.elements.BusinessActorFigure;
import com.archimatetool.editor.diagram.figures.elements.BusinessRoleFigure;
import com.archimatetool.editor.diagram.figures.elements.CapabilityFigure;
import com.archimatetool.editor.diagram.figures.elements.CollaborationFigure;
import com.archimatetool.editor.diagram.figures.elements.CommunicationNetworkFigure;
import com.archimatetool.editor.diagram.figures.elements.ConstraintFigure;
import com.archimatetool.editor.diagram.figures.elements.ContractFigure;
import com.archimatetool.editor.diagram.figures.elements.CourseOfActionFigure;
import com.archimatetool.editor.diagram.figures.elements.DeliverableFigure;
import com.archimatetool.editor.diagram.figures.elements.DeviceFigure;
import com.archimatetool.editor.diagram.figures.elements.DistributionNetworkFigure;
import com.archimatetool.editor.diagram.figures.elements.DriverFigure;
import com.archimatetool.editor.diagram.figures.elements.EquipmentFigure;
import com.archimatetool.editor.diagram.figures.elements.EventFigure;
import com.archimatetool.editor.diagram.figures.elements.FacilityFigure;
import com.archimatetool.editor.diagram.figures.elements.FunctionFigure;
import com.archimatetool.editor.diagram.figures.elements.GapFigure;
import com.archimatetool.editor.diagram.figures.elements.GoalFigure;
import com.archimatetool.editor.diagram.figures.elements.GroupingFigure;
import com.archimatetool.editor.diagram.figures.elements.InteractionFigure;
import com.archimatetool.editor.diagram.figures.elements.InterfaceFigure;
import com.archimatetool.editor.diagram.figures.elements.JunctionFigure;
import com.archimatetool.editor.diagram.figures.elements.LocationFigure;
import com.archimatetool.editor.diagram.figures.elements.MaterialFigure;
import com.archimatetool.editor.diagram.figures.elements.MeaningFigure;
import com.archimatetool.editor.diagram.figures.elements.NodeFigure;
import com.archimatetool.editor.diagram.figures.elements.ObjectFigure;
import com.archimatetool.editor.diagram.figures.elements.OutcomeFigure;
import com.archimatetool.editor.diagram.figures.elements.PathFigure;
import com.archimatetool.editor.diagram.figures.elements.PlateauFigure;
import com.archimatetool.editor.diagram.figures.elements.PrincipleFigure;
import com.archimatetool.editor.diagram.figures.elements.ProcessFigure;
import com.archimatetool.editor.diagram.figures.elements.ProductFigure;
import com.archimatetool.editor.diagram.figures.elements.RepresentationFigure;
import com.archimatetool.editor.diagram.figures.elements.RequirementFigure;
import com.archimatetool.editor.diagram.figures.elements.ResourceFigure;
import com.archimatetool.editor.diagram.figures.elements.ServiceFigure;
import com.archimatetool.editor.diagram.figures.elements.StakeholderFigure;
import com.archimatetool.editor.diagram.figures.elements.SystemSoftwareFigure;
import com.archimatetool.editor.diagram.figures.elements.ValueFigure;
import com.archimatetool.editor.diagram.figures.elements.ValueStreamFigure;
import com.archimatetool.editor.diagram.figures.elements.WorkPackageFigure;
import com.archimatetool.model.IArchimatePackage;

/**
 * Factory for drawing figure icons
 * 
 * @author Phillip Beauvoir
 */
public class FigureIconFactory {
    
    /**
     * Draw a figure icon
     * 
     * TODO: all icons need to be drawn with a common origin point so callers can provide one Point
     * 
     * @param eClass The EClass for the figure
     * @param graphics The grpahics to draw on
     * @param foregroundColor
     * @param backgroundColor
     * @param pt The start point to draw the icon
     */
    public static void drawIcon(EClass eClass, Graphics graphics, Color foregroundColor, Color backgroundColor, Point pt) {
        switch(eClass.getClassifierID()) {
            
            // Elements
            case IArchimatePackage.APPLICATION_COMPONENT -> {
                ApplicationComponentFigure.drawIcon(graphics, foregroundColor, backgroundColor, pt);
            }
            case IArchimatePackage.ARTIFACT -> {
                ArtifactFigure.drawIcon(graphics, foregroundColor, backgroundColor, pt);
            }
            case IArchimatePackage.ASSESSMENT -> {
                AssessmentFigure.drawIcon(graphics, foregroundColor, backgroundColor, pt);
            }
            case IArchimatePackage.BUSINESS_ACTOR -> {
                BusinessActorFigure.drawIcon(graphics, foregroundColor, backgroundColor, pt);
            }
            case IArchimatePackage.BUSINESS_COLLABORATION, IArchimatePackage.APPLICATION_COLLABORATION, IArchimatePackage.TECHNOLOGY_COLLABORATION -> {
                CollaborationFigure.drawIcon(graphics, foregroundColor, backgroundColor, pt);
            }
            case IArchimatePackage.BUSINESS_EVENT, IArchimatePackage.APPLICATION_EVENT, IArchimatePackage.TECHNOLOGY_EVENT, IArchimatePackage.IMPLEMENTATION_EVENT -> {
                EventFigure.drawIcon(graphics, foregroundColor, backgroundColor, pt);
            }
            case IArchimatePackage.BUSINESS_FUNCTION, IArchimatePackage.APPLICATION_FUNCTION, IArchimatePackage.TECHNOLOGY_FUNCTION -> {
                FunctionFigure.drawIcon(graphics, foregroundColor, backgroundColor, pt);
            }
            case IArchimatePackage.BUSINESS_INTERACTION, IArchimatePackage.APPLICATION_INTERACTION, IArchimatePackage.TECHNOLOGY_INTERACTION -> {
                InteractionFigure.drawIcon(graphics, foregroundColor, backgroundColor, pt);
            }
            case IArchimatePackage.BUSINESS_INTERFACE, IArchimatePackage.APPLICATION_INTERFACE, IArchimatePackage.TECHNOLOGY_INTERFACE -> {
                InterfaceFigure.drawIcon(graphics, foregroundColor, backgroundColor, pt);
            }
            case IArchimatePackage.BUSINESS_OBJECT, IArchimatePackage.DATA_OBJECT -> {
                ObjectFigure.drawIcon(graphics, foregroundColor, backgroundColor, pt);
            }
            case IArchimatePackage.BUSINESS_PROCESS, IArchimatePackage.APPLICATION_PROCESS, IArchimatePackage.TECHNOLOGY_PROCESS -> {
                ProcessFigure.drawIcon(graphics, foregroundColor, backgroundColor, pt);
            }
            case IArchimatePackage.BUSINESS_ROLE -> {
                BusinessRoleFigure.drawIcon(graphics, foregroundColor, backgroundColor, pt);
            }
            case IArchimatePackage.BUSINESS_SERVICE, IArchimatePackage.APPLICATION_SERVICE, IArchimatePackage.TECHNOLOGY_SERVICE -> {
                ServiceFigure.drawIcon(graphics, foregroundColor, backgroundColor, pt);
            }
            case IArchimatePackage.CAPABILITY -> {
                CapabilityFigure.drawIcon(graphics, foregroundColor, backgroundColor, pt);
            }
            case IArchimatePackage.COMMUNICATION_NETWORK -> {
                CommunicationNetworkFigure.drawIcon(graphics, foregroundColor, backgroundColor, pt);
            }
            case IArchimatePackage.CONSTRAINT -> {
                ConstraintFigure.drawIcon(graphics, foregroundColor, backgroundColor, pt);
            }
            case IArchimatePackage.CONTRACT -> {
                ContractFigure.drawIcon(graphics, foregroundColor, backgroundColor, pt);
            }
            case IArchimatePackage.COURSE_OF_ACTION -> {
                CourseOfActionFigure.drawIcon(graphics, foregroundColor, backgroundColor, pt);
            }
            case IArchimatePackage.DELIVERABLE -> {
                DeliverableFigure.drawIcon(graphics, foregroundColor, backgroundColor, pt);
            }
            case IArchimatePackage.DEVICE -> {
                DeviceFigure.drawIcon(graphics, foregroundColor, backgroundColor, pt);
            }
            case IArchimatePackage.DISTRIBUTION_NETWORK -> {
                DistributionNetworkFigure.drawIcon(graphics, foregroundColor, backgroundColor, pt);
            }
            case IArchimatePackage.DRIVER -> {
                DriverFigure.drawIcon(graphics, foregroundColor, backgroundColor, pt);
            }
            case IArchimatePackage.EQUIPMENT -> {
                EquipmentFigure.drawIcon(graphics, foregroundColor, backgroundColor, pt);
            }
            case IArchimatePackage.FACILITY -> {
                FacilityFigure.drawIcon(graphics, foregroundColor, backgroundColor, pt);
            }
            case IArchimatePackage.GAP -> {
                GapFigure.drawIcon(graphics, foregroundColor, backgroundColor, pt);
            }
            case IArchimatePackage.GOAL -> {
                GoalFigure.drawIcon(graphics, foregroundColor, backgroundColor, pt);
            }
            case IArchimatePackage.GROUPING -> {
                GroupingFigure.drawIcon(graphics, foregroundColor, backgroundColor, pt);
            }
            case IArchimatePackage.JUNCTION -> {
                JunctionFigure.drawIcon(graphics, foregroundColor, backgroundColor, pt);
            }
            case IArchimatePackage.LOCATION -> {
                LocationFigure.drawIcon(graphics, foregroundColor, backgroundColor, pt);
            }
            case IArchimatePackage.MATERIAL -> {
                MaterialFigure.drawIcon(graphics, foregroundColor, backgroundColor, pt);
            }
            case IArchimatePackage.MEANING -> {
                MeaningFigure.drawIcon(graphics, foregroundColor, backgroundColor, pt);
            }
            case IArchimatePackage.NODE -> {
                NodeFigure.drawIcon(graphics, foregroundColor, backgroundColor, pt);
            }
            case IArchimatePackage.OUTCOME -> {
                OutcomeFigure.drawIcon(graphics, foregroundColor, backgroundColor, pt);
            }
            case IArchimatePackage.PATH -> {
                PathFigure.drawIcon(graphics, foregroundColor, backgroundColor, pt);
            }
            case IArchimatePackage.PLATEAU -> {
                PlateauFigure.drawIcon(graphics, foregroundColor, backgroundColor, pt);
            }
            case IArchimatePackage.PRINCIPLE -> {
                PrincipleFigure.drawIcon(graphics, foregroundColor, backgroundColor, pt);
            }
            case IArchimatePackage.PRODUCT -> {
                ProductFigure.drawIcon(graphics, foregroundColor, backgroundColor, pt);
            }
            case IArchimatePackage.REPRESENTATION -> {
                RepresentationFigure.drawIcon(graphics, foregroundColor, backgroundColor, pt);
            }
            case IArchimatePackage.REQUIREMENT -> {
                RequirementFigure.drawIcon(graphics, foregroundColor, backgroundColor, pt);
            }
            case IArchimatePackage.RESOURCE -> {
                ResourceFigure.drawIcon(graphics, foregroundColor, backgroundColor, pt);
            }
            case IArchimatePackage.STAKEHOLDER -> {
                StakeholderFigure.drawIcon(graphics, foregroundColor, backgroundColor, pt);
            }
            case IArchimatePackage.SYSTEM_SOFTWARE -> {
                SystemSoftwareFigure.drawIcon(graphics, foregroundColor, backgroundColor, pt);
            }
            case IArchimatePackage.VALUE -> {
                ValueFigure.drawIcon(graphics, foregroundColor, backgroundColor, pt);
            }
            case IArchimatePackage.VALUE_STREAM -> {
                ValueStreamFigure.drawIcon(graphics, foregroundColor, backgroundColor, pt);
            }
            case IArchimatePackage.WORK_PACKAGE -> {
                WorkPackageFigure.drawIcon(graphics, foregroundColor, backgroundColor, pt);
            }

            // Relations
            case IArchimatePackage.ACCESS_RELATIONSHIP -> {
                AccessConnectionFigure.drawIcon(graphics, foregroundColor, pt);
            }
            case IArchimatePackage.AGGREGATION_RELATIONSHIP -> {
                AggregationConnectionFigure.drawIcon(graphics, foregroundColor, pt);
            }
            case IArchimatePackage.ASSIGNMENT_RELATIONSHIP -> {
                AssignmentConnectionFigure.drawIcon(graphics, foregroundColor, pt);
            }
            case IArchimatePackage.ASSOCIATION_RELATIONSHIP -> {
                AssociationConnectionFigure.drawIcon(graphics, foregroundColor, pt);
            }
            case IArchimatePackage.COMPOSITION_RELATIONSHIP -> {
                CompositionConnectionFigure.drawIcon(graphics, foregroundColor, pt);
            }
            case IArchimatePackage.FLOW_RELATIONSHIP -> {
                FlowConnectionFigure.drawIcon(graphics, foregroundColor, pt);
            }
            case IArchimatePackage.INFLUENCE_RELATIONSHIP -> {
                InfluenceConnectionFigure.drawIcon(graphics, foregroundColor, pt);
            }
            case IArchimatePackage.REALIZATION_RELATIONSHIP -> {
                RealizationConnectionFigure.drawIcon(graphics, foregroundColor, pt);
            }
            case IArchimatePackage.SERVING_RELATIONSHIP -> {
                ServingConnectionFigure.drawIcon(graphics, foregroundColor, pt);
            }
            case IArchimatePackage.SPECIALIZATION_RELATIONSHIP -> {
                SpecializationConnectionFigure.drawIcon(graphics, foregroundColor, pt);
            }
            case IArchimatePackage.TRIGGERING_RELATIONSHIP -> {
                TriggeringConnectionFigure.drawIcon(graphics, foregroundColor, pt);
            }
        }
    }

}
