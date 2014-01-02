/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.zest;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.zest.core.viewers.ISelfStyleProvider;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;

import com.archimatetool.editor.diagram.figures.ToolTipFigure;
import com.archimatetool.editor.diagram.figures.connections.AccessConnectionFigure;
import com.archimatetool.editor.diagram.figures.connections.AggregationConnectionFigure;
import com.archimatetool.editor.diagram.figures.connections.AssignmentConnectionFigure;
import com.archimatetool.editor.diagram.figures.connections.CompositionConnectionFigure;
import com.archimatetool.editor.diagram.figures.connections.FlowConnectionFigure;
import com.archimatetool.editor.diagram.figures.connections.InfluenceConnectionFigure;
import com.archimatetool.editor.diagram.figures.connections.RealisationConnectionFigure;
import com.archimatetool.editor.diagram.figures.connections.SpecialisationConnectionFigure;
import com.archimatetool.editor.diagram.figures.connections.TriggeringConnectionFigure;
import com.archimatetool.editor.diagram.figures.connections.UsedByConnectionFigure;
import com.archimatetool.editor.ui.ArchimateLabelProvider;
import com.archimatetool.model.IAccessRelationship;
import com.archimatetool.model.IAggregationRelationship;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IAssignmentRelationship;
import com.archimatetool.model.ICompositionRelationship;
import com.archimatetool.model.IFlowRelationship;
import com.archimatetool.model.IInfluenceRelationship;
import com.archimatetool.model.INameable;
import com.archimatetool.model.IRealisationRelationship;
import com.archimatetool.model.IRelationship;
import com.archimatetool.model.ISpecialisationRelationship;
import com.archimatetool.model.ITriggeringRelationship;
import com.archimatetool.model.IUsedByRelationship;


/**
 * Label Provider
 * 
 * @author Phillip Beauvoir
 */
public class ZestViewerLabelProvider extends BaseLabelProvider
implements ILabelProvider, ISelfStyleProvider {
    
    Color HIGHLIGHT_COLOR = new Color(Display.getDefault(), 255, 255, 255);
    Color FOCUS_COLOR = new Color(Display.getDefault(), 200, 200, 255);
    
    private Object focusObject;
    
    /**
     * Set the element to have the focus
     * @param object
     */
    public void setFocusElement(Object object) {
        focusObject = object;
    }
    
    @Override
    public Image getImage(Object element) {
        if(element instanceof IRelationship) {
            return null;
        }
        return ArchimateLabelProvider.INSTANCE.getImage(element);
    }

    @Override
    public String getText(Object element) {
        if(element instanceof INameable) {
            return ((INameable)element).getName();
        }
        return null;
    }

    @Override
    public void dispose() {
        super.dispose();
        HIGHLIGHT_COLOR.dispose();
        FOCUS_COLOR.dispose();
    }

    // ========================================================================================
    // IEntityStyleProvider (not used)
    // ========================================================================================

    public boolean fisheyeNode(Object entity) {
        // Very annoying!
        return false;
    }

    public Color getBackgroundColour(Object entity) {
        return null;
    }

    public Color getBorderColor(Object entity) {
        return null;
    }

    public Color getBorderHighlightColor(Object entity) {
        return null;
    }

    public int getBorderWidth(Object entity) {
        return -1;
    }

    public Color getForegroundColour(Object entity) {
        return null;
    }

    public Color getNodeHighlightColor(Object entity) {
        return HIGHLIGHT_COLOR;
    }

    public IFigure getTooltip(Object entity) {
        if(entity instanceof IArchimateElement) {
            ToolTipFigure l = new ToolTipFigure();
            String type = ArchimateLabelProvider.INSTANCE.getDefaultName(((EObject)entity).eClass());
            l.setText(ArchimateLabelProvider.INSTANCE.getLabel(entity));
            l.setType(Messages.ZestViewerLabelProvider_0 + " " + type); //$NON-NLS-1$
            if(entity instanceof IRelationship) {
                l.setRubric(ArchimateLabelProvider.INSTANCE.getRelationshipSentence((IRelationship)entity));
            }
            return l;
        }
        return null;
    }

    // ========================================================================================
    // IConnectionStyleProvider
    // ========================================================================================
    
    public Color getColor(Object rel) {
        return null;
    }

    public int getConnectionStyle(Object rel) {
        return ZestStyles.CONNECTIONS_DIRECTED;
    }

    public Color getHighlightColor(Object rel) {
        return null;
    }

    public int getLineWidth(Object rel) {
        return 0;
    }

    // ========================================================================================
    // ISelfStyleProvider
    // ========================================================================================
    
    @Override
    public void selfStyleConnection(Object element, GraphConnection connection) {
        
        // Connections are not rendered in some cases when curved
        // See https://bugs.eclipse.org/bugs/show_bug.cgi?id=373199
        // Seems to be fixed
        
        connection.setLineWidth(0);
        connection.setTooltip(getTooltip(element));
        connection.setLineColor(ColorConstants.black);
        
        PolylineConnection conn = (PolylineConnection)connection.getConnectionFigure();
        
        if(element instanceof ISpecialisationRelationship) {
            conn.setTargetDecoration(SpecialisationConnectionFigure.createFigureTargetDecoration());
        }
        else if(element instanceof ICompositionRelationship) {
            conn.setSourceDecoration(CompositionConnectionFigure.createFigureSourceDecoration());
        }
        else if(element instanceof IAggregationRelationship) {
            conn.setSourceDecoration(AggregationConnectionFigure.createFigureSourceDecoration());
        }
        else if(element instanceof IAssignmentRelationship) {
            conn.setSourceDecoration(AssignmentConnectionFigure.createFigureSourceDecoration());
            conn.setTargetDecoration(AssignmentConnectionFigure.createFigureTargetDecoration());
        }
        else if(element instanceof IRealisationRelationship) {
            conn.setTargetDecoration(RealisationConnectionFigure.createFigureTargetDecoration());
            connection.setLineStyle(SWT.LINE_CUSTOM);
            conn.setLineDash(new float[] { 4 });
        }
        else if(element instanceof ITriggeringRelationship) {
            conn.setTargetDecoration(TriggeringConnectionFigure.createFigureTargetDecoration());
        }
        else if(element instanceof IFlowRelationship) {
            conn.setTargetDecoration(FlowConnectionFigure.createFigureTargetDecoration());
            connection.setLineStyle(SWT.LINE_CUSTOM);
            conn.setLineDash(new float[] { 6, 3 });
        }
        else if(element instanceof IUsedByRelationship) {
            conn.setTargetDecoration(UsedByConnectionFigure.createFigureTargetDecoration());
        }
        else if(element instanceof IAccessRelationship) {
            conn.setTargetDecoration(AccessConnectionFigure.createFigureSourceDecoration());
            conn.setTargetDecoration(AccessConnectionFigure.createFigureTargetDecoration());
            connection.setLineStyle(SWT.LINE_CUSTOM);
            conn.setLineDash(new float[] { 1.5f, 3 });
        }
        else if(element instanceof IInfluenceRelationship) {
            conn.setTargetDecoration(InfluenceConnectionFigure.createFigureTargetDecoration());
            connection.setLineStyle(SWT.LINE_CUSTOM);
            conn.setLineDash(new float[] { 6, 3 });
        }
        
        conn.setAntialias(SWT.ON);
    }

    @Override
    public void selfStyleNode(Object element, GraphNode node) {
        if(element == focusObject) {
            node.setBackgroundColor(FOCUS_COLOR);
        }
        
        node.setHighlightColor(getNodeHighlightColor(element));
        node.setTooltip(getTooltip(element));
    }
}