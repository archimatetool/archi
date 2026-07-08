/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.zest;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.zest.core.viewers.ISelfStyleProvider;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;

import com.archimatetool.editor.diagram.figures.ToolTipFigure;
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
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IAccessRelationship;
import com.archimatetool.model.IAggregationRelationship;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IAssignmentRelationship;
import com.archimatetool.model.IAssociationRelationship;
import com.archimatetool.model.ICompositionRelationship;
import com.archimatetool.model.IFlowRelationship;
import com.archimatetool.model.IInfluenceRelationship;
import com.archimatetool.model.INameable;
import com.archimatetool.model.IRealizationRelationship;
import com.archimatetool.model.IServingRelationship;
import com.archimatetool.model.ISpecializationRelationship;
import com.archimatetool.model.ITriggeringRelationship;


/**
 * Label Provider
 * 
 * @author Phillip Beauvoir
 */
public class ZestViewerLabelProvider extends LabelProvider implements ISelfStyleProvider {
    
    private Color HIGHLIGHT_COLOR = new Color(255, 255, 255);
    private Color FOCUS_COLOR = new Color(200, 200, 255);
    
    private Object focusObject;
    
    @Override
    public String getText(Object element) {
        if(element instanceof IInfluenceRelationship rel && StringUtils.isSet(rel.getStrength())) {
            return rel.getName() + " " + rel.getStrength(); //$NON-NLS-1$
        }
        
        return element instanceof INameable nameable ? nameable.getName() : null;
    }
    
    @Override
    public Image getImage(Object element) {
        return element instanceof IArchimateElement ? ArchiLabelProvider.INSTANCE.getImage(element) : null;
    }

    @Override
    public void selfStyleNode(Object element, GraphNode node) {
        if(element == focusObject) {
            node.setBackgroundColor(FOCUS_COLOR);
        }
        
        node.setHighlightColor(HIGHLIGHT_COLOR);
        node.setTooltip(getTooltip(element));
    }
    
    @Override
    public void selfStyleConnection(Object element, GraphConnection connection) {
        connection.setLineWidth(1);
        connection.setTooltip(getTooltip(element));
        connection.setLineColor(ColorConstants.black);
        
        PolylineConnection conn = (PolylineConnection)connection.getConnectionFigure();
        conn.setAntialias(SWT.ON);
        
        switch(element) {
            case ISpecializationRelationship rel -> {
                conn.setTargetDecoration(SpecializationConnectionFigure.createFigureTargetDecoration());
            }
            case ICompositionRelationship rel -> {
                conn.setSourceDecoration(CompositionConnectionFigure.createFigureSourceDecoration());
            }
            case IAggregationRelationship rel -> {
                conn.setSourceDecoration(AggregationConnectionFigure.createFigureSourceDecoration());
            }
            case IAssignmentRelationship rel -> {
                conn.setSourceDecoration(AssignmentConnectionFigure.createFigureSourceDecoration());
                conn.setTargetDecoration(AssignmentConnectionFigure.createFigureTargetDecoration());
            }
            case IRealizationRelationship rel -> {
                conn.setTargetDecoration(RealizationConnectionFigure.createFigureTargetDecoration());
                connection.setLineStyle(SWT.LINE_CUSTOM);
                conn.setLineDash(new float[] { 2 });
            }
            case ITriggeringRelationship rel -> {
                conn.setTargetDecoration(TriggeringConnectionFigure.createFigureTargetDecoration());
            }
            case IFlowRelationship rel -> {
                conn.setTargetDecoration(FlowConnectionFigure.createFigureTargetDecoration());
                connection.setLineStyle(SWT.LINE_CUSTOM);
                conn.setLineDash(new float[] { 6, 3 });
            }
            case IServingRelationship rel -> {
                conn.setTargetDecoration(ServingConnectionFigure.createFigureTargetDecoration());
            }
            case IAccessRelationship rel -> {
                switch(rel.getAccessType()) {
                    case IAccessRelationship.WRITE_ACCESS -> { // arrow at target endpoint
                        conn.setTargetDecoration(AccessConnectionFigure.createFigureTargetDecoration());
                    }
                    case IAccessRelationship.READ_ACCESS -> { // arrow at source endpoint
                        conn.setSourceDecoration(AccessConnectionFigure.createFigureTargetDecoration());
                    }
                    case IAccessRelationship.READ_WRITE_ACCESS -> { // both arrows
                        conn.setSourceDecoration(AccessConnectionFigure.createFigureTargetDecoration());
                        conn.setTargetDecoration(AccessConnectionFigure.createFigureTargetDecoration());
                    }
                    case IAccessRelationship.UNSPECIFIED_ACCESS -> { // no arrows
                    }
                    default -> { // arrow at target endpoint
                        conn.setTargetDecoration(AccessConnectionFigure.createFigureTargetDecoration());
                    }
                }
                
                connection.setLineStyle(SWT.LINE_CUSTOM);
                conn.setLineDash(new float[] { 2 });
            }
            case IInfluenceRelationship rel -> {
                conn.setTargetDecoration(InfluenceConnectionFigure.createFigureTargetDecoration());
                connection.setLineStyle(SWT.LINE_CUSTOM);
                conn.setLineDash(new float[] { 6, 3 });
            }
            case IAssociationRelationship rel -> {
                if(rel.isDirected()) {
                    conn.setTargetDecoration(AssociationConnectionFigure.createFigureTargetDecoration());
                }
            }
            default -> {}
        }
    }

    /**
     * Set the element that has the focus
     */
    void setFocusElement(Object object) {
        focusObject = object;
    }

    private IFigure getTooltip(Object element) {
        if(element instanceof IArchimateConcept concept) {
            ToolTipFigure tooltip = new ToolTipFigure();
            String type = ArchiLabelProvider.INSTANCE.getDefaultName(concept.eClass());
            tooltip.setText(ArchiLabelProvider.INSTANCE.getLabel(concept));
            tooltip.setType(Messages.ZestViewerLabelProvider_0 + " " + type); //$NON-NLS-1$
            if(element instanceof IArchimateRelationship rel) {
                tooltip.setRubric(ArchiLabelProvider.INSTANCE.getRelationshipSentence(rel));
            }
            return tooltip;
        }
        
        return null;
    }
}