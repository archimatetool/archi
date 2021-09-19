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
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.zest.core.viewers.ISelfStyleProvider;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;

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
public class ZestViewerLabelProvider
implements IBaseLabelProvider, ISelfStyleProvider {
    
    private Color HIGHLIGHT_COLOR = new Color(255, 255, 255);
    private Color FOCUS_COLOR = new Color(200, 200, 255);
    
    private Object focusObject;
    
    /**
     * Set the element to have the focus
     * @param object
     */
    public void setFocusElement(Object object) {
        focusObject = object;
    }
    
    private Image getImage(Object element) {
        return ArchiLabelProvider.INSTANCE.getImage(element);
    }

    private String getText(Object element) {
        if(element instanceof IInfluenceRelationship) {
            IInfluenceRelationship rel = (IInfluenceRelationship)element;
            if(StringUtils.isSet(rel.getStrength())) {
                return ((INameable)element).getName() + " " + rel.getStrength(); //$NON-NLS-1$
            }
        }
        if(element instanceof INameable) {
            return ((INameable)element).getName();
        }
        return null;
    }

    @Override
    public void dispose() {
    }

    @Override
    public void addListener(ILabelProviderListener listener) {
    }

    @Override
    public boolean isLabelProperty(Object element, String property) {
        return false;
    }

    @Override
    public void removeListener(ILabelProviderListener listener) {
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
        if(entity instanceof IArchimateConcept) {
            ToolTipFigure l = new ToolTipFigure();
            String type = ArchiLabelProvider.INSTANCE.getDefaultName(((EObject)entity).eClass());
            l.setText(ArchiLabelProvider.INSTANCE.getLabel(entity));
            l.setType(Messages.ZestViewerLabelProvider_0 + " " + type); //$NON-NLS-1$
            if(entity instanceof IArchimateRelationship) {
                l.setRubric(ArchiLabelProvider.INSTANCE.getRelationshipSentence((IArchimateRelationship)entity));
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
        connection.setLineWidth(0);
        connection.setTooltip(getTooltip(element));
        connection.setLineColor(ColorConstants.black);
        connection.setText(getText(element));
        
        PolylineConnection conn = (PolylineConnection)connection.getConnectionFigure();
        
        if(element instanceof ISpecializationRelationship) {
            conn.setTargetDecoration(SpecializationConnectionFigure.createFigureTargetDecoration());
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
        else if(element instanceof IRealizationRelationship) {
            conn.setTargetDecoration(RealizationConnectionFigure.createFigureTargetDecoration());
            connection.setLineStyle(SWT.LINE_CUSTOM);
            conn.setLineDash(new float[] { 2 });
        }
        else if(element instanceof ITriggeringRelationship) {
            conn.setTargetDecoration(TriggeringConnectionFigure.createFigureTargetDecoration());
        }
        else if(element instanceof IFlowRelationship) {
            conn.setTargetDecoration(FlowConnectionFigure.createFigureTargetDecoration());
            connection.setLineStyle(SWT.LINE_CUSTOM);
            conn.setLineDash(new float[] { 6, 3 });
        }
        else if(element instanceof IServingRelationship) {
            conn.setTargetDecoration(ServingConnectionFigure.createFigureTargetDecoration());
        }
        else if(element instanceof IAccessRelationship) {
            switch(((IAccessRelationship)element).getAccessType()) {
                case IAccessRelationship.WRITE_ACCESS:
                default:
                    conn.setSourceDecoration(null);
                    conn.setTargetDecoration(AccessConnectionFigure.createFigureTargetDecoration()); // arrow at target endpoint
                    break;

                case IAccessRelationship.READ_ACCESS:
                    conn.setSourceDecoration(AccessConnectionFigure.createFigureTargetDecoration()); // arrow at source endpoint
                    conn.setTargetDecoration(null);
                    break;

                case IAccessRelationship.UNSPECIFIED_ACCESS:
                    conn.setSourceDecoration(null);  // no arrows
                    conn.setTargetDecoration(null);
                    break;

                case IAccessRelationship.READ_WRITE_ACCESS:
                    conn.setSourceDecoration(AccessConnectionFigure.createFigureTargetDecoration()); // both arrows
                    conn.setTargetDecoration(AccessConnectionFigure.createFigureTargetDecoration());
                    break;
            }
            connection.setLineStyle(SWT.LINE_CUSTOM);
            conn.setLineDash(new float[] { 2 });
        }
        else if(element instanceof IInfluenceRelationship) {
            conn.setTargetDecoration(InfluenceConnectionFigure.createFigureTargetDecoration());
            connection.setLineStyle(SWT.LINE_CUSTOM);
            conn.setLineDash(new float[] { 6, 3 });
        }
        else if(element instanceof IAssociationRelationship) {
            if(((IAssociationRelationship)element).isDirected()) {
                conn.setTargetDecoration(AssociationConnectionFigure.createFigureTargetDecoration());
            }
            else {
                conn.setTargetDecoration(null);
            }
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
        
        node.setText(getText(element));
        node.setImage(getImage(element));
    }
}