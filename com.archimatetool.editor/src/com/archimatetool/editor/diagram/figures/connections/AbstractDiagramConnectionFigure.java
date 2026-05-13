/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.connections;

import java.util.Arrays;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionLocator;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Path;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.diagram.figures.ToolTipFigure;
import com.archimatetool.editor.diagram.util.AnimationUtil;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.FontFactory;
import com.archimatetool.editor.ui.textrender.TextRenderer;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IDiagramModelConnection;



/**
 * Abstract implementation of a connection figure.  Subclasses should decide how to draw the line
 * 
 * @author Phillip Beauvoir
 * @author jbsarrodie
 */
public abstract class AbstractDiagramConnectionFigure
extends RoundedPolylineConnection implements IDiagramConnectionFigure {

    private TextFlow textFlow;
    private IDiagramModelConnection diagramModelConnection;

    protected int fTextPosition = -1;
    protected int fTextRelativePosition = -1;
    protected Color fFontColor;
    protected Color fLineColor;
    
    protected static Color highlightedColor = new Color(0, 0, 255);

	protected boolean isSelected = false;
	protected boolean showTargetFeedback = false;

    @Override
    public void setModelConnection(IDiagramModelConnection connection) {
	    diagramModelConnection = connection;
	    
	    setFigureProperties();
        
        // Have to add this if we want Animation to work!
        AnimationUtil.addConnectionForRoutingAnimation(this);
    }

    @Override
    public IDiagramModelConnection getModelConnection() {
        return diagramModelConnection;
    }

    protected void setFigureProperties() {
    }

    @Override
    public void refreshVisuals() {
        // If the text position has been changed update it
        if(getModelConnection().getTextPosition() != fTextPosition || getModelConnection().getRelativePosition() != fTextRelativePosition) {
            fTextPosition = getModelConnection().getTextPosition();
            fTextRelativePosition = getModelConnection().getRelativePosition();
            setLabelLocator();
        }
        
        setLabelFont();
        
        setLabelFontColor();
        
        setLineColor();
        
        setText();
        
        setLineWidth();
        
        setTextAlignment(getModelConnection().getTextAlignment());
        
        getFlowPage().setOpaque(ArchiPlugin.getInstance().getPreferenceStore().getInt(IPreferenceConstants.CONNECTION_LABEL_STRATEGY) == CONNECTION_LABEL_OPAQUE);
    }

    /**
     * @param copy
     * @return True if the user clicked on the Relationship edit label
     */
    @Override
    public boolean didClickConnectionLabel(Point requestLoc) {
        IFigure label = getConnectionLabel();
        label.translateToRelative(requestLoc);
        return label.containsPoint(requestLoc);
    }

    @Override
    public TextFlow getConnectionLabel() {
        if(textFlow == null) {
            textFlow = new TextFlow();
            //fTextFlow.setLayoutManager(new ParagraphTextLayout(fTextFlow, ParagraphTextLayout.WORD_WRAP_HARD));
            
            FlowPage flowPage = new FlowPage();
            flowPage.add(textFlow);
            
            add(flowPage);
        }
        
        return textFlow;
    }
    
    protected FlowPage getFlowPage() {
        return (FlowPage)getConnectionLabel().getParent();
    }
    
    protected void setTextAlignment(int alignment) {
        getFlowPage().setHorizontalAligment(alignment);
    }

    private void setLabelLocator() {
        Locator locator = switch (getModelConnection().getTextPosition()) {
            case IDiagramModelConnection.CONNECTION_TEXT_POSITION_SOURCE ->
                new ArchiConnectionEndpointLocator(this, false);

            case IDiagramModelConnection.CONNECTION_TEXT_POSITION_TARGET ->
                new ArchiConnectionEndpointLocator(this, true);

            default -> {
                ConnectionLocator cl = new ConnectionLocator(this, ConnectionLocator.MIDDLE);
                cl.setRelativePosition(getModelConnection().getRelativePosition());
                cl.setGap(5); // Add some clearance if not centre
                yield cl;
            }
        };

        setConstraint(getFlowPage(), locator);
    }
    
    @Override
    public void setText() {
        String text = ""; //$NON-NLS-1$
        
        // If we are showing the label name
        if(getModelConnection().isNameVisible()) {
            // Do we have any rendered text?
            text = TextRenderer.getDefault().render(getModelConnection(),
                                                    StringUtils.safeString(getModelConnection().getName().trim()));
        }
        
        getConnectionLabel().setText(text);
    }
    
    /**
     * Set the font in the label to that in the model, or failing that, as per user's default
     */
    protected void setLabelFont() {
        getConnectionLabel().setFont(FontFactory.getViewFont(getModelConnection().getFont()));
    }

    /**
     * Set the font color to that in the model, or failing that, as per default
     */
    protected void setLabelFontColor() {
        String val = getModelConnection().getFontColor();
        Color c = ColorFactory.get(val);
        if(c == null) {
            c = ColorConstants.black; // have to set default color otherwise it inherits line color
        }
        if(c != fFontColor) {
            fFontColor = c;
            getConnectionLabel().setForegroundColor(c);
        }
    }
    
    /**
     * Set the line color to that in the model, or failing that, as per default
     */
    protected void setLineColor() {
        String val = getModelConnection().getLineColor();
        Color color = ColorFactory.get(val);
        
        if(color == null) {
            color = ColorFactory.getDefaultLineColor(getModelConnection());
        }
        
        if(color != fLineColor) {
            fLineColor = color;
            setForegroundColor(color);
        }
    }
    
    protected void setLineWidth() {
        setLineWidth(getModelConnection().getLineWidth());
    }
    
    @Override
    public IFigure getToolTip() {
        if(!ArchiPlugin.getInstance().getPreferenceStore().getBoolean(IPreferenceConstants.VIEW_TOOLTIPS)) {
            return null;
        }
        
        if(super.getToolTip() == null) {
            setToolTip(new ToolTipFigure());
        }
        
        return super.getToolTip();
    }
    
    /**
     * This is called when the parent scale is changed and set
     * in the figure's ancestor ScalableFreeformLayeredPane#setScale()
     * So we set the line dash information based on the current scale
     */
    @Override
    protected void fireFigureMoved() {
        // Get the connection's line dash information
        float[] ld = getLineDashFloats();
        // If we have some and there has been a change (i.e the scale has changed) then set the line dash to the new information
        if(ld != null && !Arrays.equals(ld, getLineDash())) { 
            setLineDash(ld);
        }
        
        super.fireFigureMoved();
    }
    
    /**
     * @return line dash float information for connections with lines dashes, or null if the connection has no line dashes
     */
    protected float[] getLineDashFloats() {
        return null;
    }
    
    @Override
    public void setSelected(boolean selected) {
        isSelected = selected;
    }
    
    @Override
    public void showTargetFeedback(boolean show) {
        if(showTargetFeedback != show) {
            showTargetFeedback = show;
            repaint();
        }
    }

    @Override
    public void paintFigure(Graphics graphics) {
        if(showTargetFeedback || (isSelected && ArchiPlugin.getInstance().getPreferenceStore().getBoolean(IPreferenceConstants.SHOW_SELECTED_CONNECTIONS))) {
            setLineWidth(getModelConnection().getLineWidth() + 1);
            setForegroundColor(highlightedColor);
        }
        else {
            setLineWidth();
            setForegroundColor(fLineColor);
        }

        // Label strategy is clipped
        if(StringUtils.isSet(getConnectionLabel().getText()) && 
                                         ArchiPlugin.getInstance().getPreferenceStore().getInt(IPreferenceConstants.CONNECTION_LABEL_STRATEGY) == CONNECTION_LABEL_CLIPPED) {
            clipTextLabel(graphics);
        }
        
        super.paintFigure(graphics);
    }
    
    /**
     * Create a clip Path so that the text label doesn't draw over the connection
     */
    private void clipTextLabel(Graphics graphics) {
        // Clipping area of connection figure (the visible part of the connection figure's bounds)
        Rectangle clipRect = graphics.getClip(Rectangle.SINGLETON);
        
        // The label's flow page bounds
        Rectangle labelRect = getFlowPage().getBounds().getCopy();
        
        // Expand label margin's horizontal width
        labelRect.expand(1, 0);
        
        // Create a Path that fills the clipping area minus the label
        Path path = new Path(null);
        
        // Move to clip rect's start x,y
        path.moveTo(clipRect.x, clipRect.y);
        
        // Draw path of label
        drawRectPath(path, labelRect);
        
        // Draw path of clip area 
        drawRectPath(path, clipRect);
        
        // Clip the path
        graphics.clipPath(path);
        
        path.dispose();
    }
    
    private void drawRectPath(Path path, Rectangle rect) {
        path.lineTo(rect.x, rect.y);
        path.lineTo(rect.x + rect.width, rect.y);
        path.lineTo(rect.x + rect.width, rect.y + rect.height);
        path.lineTo(rect.x, rect.y + rect.height);
        path.lineTo(rect.x, rect.y);
    }
}
