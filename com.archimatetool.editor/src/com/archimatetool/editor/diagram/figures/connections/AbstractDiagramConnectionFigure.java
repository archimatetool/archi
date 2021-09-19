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
// line-curves patch by Jean-Baptiste Sarrodie (aka Jaiguru)
// Use alternate PolylineConnection
//import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Path;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.diagram.figures.ToolTipFigure;
import com.archimatetool.editor.diagram.util.AnimationUtil;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.FontFactory;
import com.archimatetool.editor.ui.textrender.TextRenderer;
import com.archimatetool.editor.utils.PlatformUtils;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.ITextAlignment;



/**
 * Abstract implementation of a connection figure.  Subclasses should decide how to draw the line
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractDiagramConnectionFigure
extends RoundedPolylineConnection implements IDiagramConnectionFigure {
//extends PolylineConnection implements IDiagramConnectionFigure {

    private TextFlow fTextFlow;
    
    protected int fTextPosition = -1;
    
    private IDiagramModelConnection fDiagramModelConnection;

    protected Color fFontColor;
    protected Color fLineColor;
    
    private static Color TARGET_FEEDBACK_COLOR = new Color(0, 0, 255);
    
    protected boolean SHOW_TARGET_FEEDBACK = false;
    
	@Override
    public void setModelConnection(IDiagramModelConnection connection) {
	    fDiagramModelConnection = connection;
	    
	    setFigureProperties();
        
        // Have to add this if we want Animation to work!
        AnimationUtil.addConnectionForRoutingAnimation(this);
	}
	
	@Override
    public IDiagramModelConnection getModelConnection() {
	    return fDiagramModelConnection;
	}
	
	protected void setFigureProperties() {
	}
	
    @Override
    public void refreshVisuals() {
        // If the text position has been changed by user update it
        if(getModelConnection().getTextPosition() != fTextPosition) {
            fTextPosition = getModelConnection().getTextPosition();
            setLabelLocator(fTextPosition);
        }
        
        setLabelFont();
        
        setLabelFontColor();
        
        setLineColor();
        
        setText();
        
        setLineWidth();
        
        getFlowPage().setOpaque(ArchiPlugin.PREFERENCES.getInt(IPreferenceConstants.CONNECTION_LABEL_STRATEGY) == CONNECTION_LABEL_OPAQUE);
        
        repaint(); // repaint when figure changes
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
        if(fTextFlow == null) {
            fTextFlow = new TextFlow();
            //fTextFlow.setLayoutManager(new ParagraphTextLayout(fTextFlow, ParagraphTextLayout.WORD_WRAP_HARD));
            
            FlowPage flowPage = new FlowPage();
            flowPage.add(fTextFlow);
            
            add(flowPage);
        }
        
        return fTextFlow;
    }
    
    protected FlowPage getFlowPage() {
        return (FlowPage)getConnectionLabel().getParent();
    }
    
    protected void setTextAlignment(int alignment) {
        getFlowPage().setHorizontalAligment(alignment);
    }

    private void setLabelLocator(int position) {
        Locator locator = null;

        switch(position) {
            case IDiagramModelConnection.CONNECTION_TEXT_POSITION_SOURCE:
                locator = new ArchiConnectionEndpointLocator(this, false);
                setTextAlignment(ITextAlignment.TEXT_ALIGNMENT_LEFT); // Text align left
                break;
            case IDiagramModelConnection.CONNECTION_TEXT_POSITION_MIDDLE:
                locator = new ConnectionLocator(this, ConnectionLocator.MIDDLE);
                setTextAlignment(ITextAlignment.TEXT_ALIGNMENT_CENTER); // Text align center
                break;
            case IDiagramModelConnection.CONNECTION_TEXT_POSITION_TARGET:
                locator = new ArchiConnectionEndpointLocator(this, true);
                setTextAlignment(ITextAlignment.TEXT_ALIGNMENT_LEFT); // Text align left
                break;
        }
        
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
     * Deprecated - use setText() instead
     */
    @Deprecated
    protected void setConnectionText() {
        setText();
    }

    /**
     * Set the font in the label to that in the model, or failing that, as per user's default
     */
    protected void setLabelFont() {
        String fontName = getModelConnection().getFont();
        Font font = FontFactory.get(fontName);
        
        // Adjust for Windows DPI
        if(PlatformUtils.isWindows()) {
            font = FontFactory.getAdjustedWindowsFont(font);
        }

        getConnectionLabel().setFont(font);
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
        boolean doShowViewTooltips = ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.VIEW_TOOLTIPS);
        
        if(super.getToolTip() == null && doShowViewTooltips) {
            setToolTip(new ToolTipFigure());
        }
        
        return doShowViewTooltips ? super.getToolTip() : null;
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
    public void showTargetFeedback() {
        if(!SHOW_TARGET_FEEDBACK) {
            SHOW_TARGET_FEEDBACK = true;
            repaint();
        }
    }
    
    @Override
    public void eraseTargetFeedback() {
        if(SHOW_TARGET_FEEDBACK) {
            SHOW_TARGET_FEEDBACK = false;
            repaint();
        }
    }
    
    @Override
    public void paintFigure(Graphics graphics) {
        if(SHOW_TARGET_FEEDBACK) {
            setForegroundColor(TARGET_FEEDBACK_COLOR);
            setLineWidth(getModelConnection().getLineWidth() + 1);
        }
        else {
            setLineWidth();
            setForegroundColor(fLineColor);
        }

        if(StringUtils.isSet(getConnectionLabel().getText()) && 
                ArchiPlugin.PREFERENCES.getInt(IPreferenceConstants.CONNECTION_LABEL_STRATEGY) == CONNECTION_LABEL_CLIPPED) {
            clipTextLabel(graphics);
        }
        else {
            super.paintFigure(graphics);
        }
    }
    
    /**
     * Clip the text label so it doesn't draw on the connection
     */
    protected void clipTextLabel(Graphics graphics) {
        // Margin around label
        final int labelMargin = 1;
        
        // Save dimensions of original clipping area and label
        Rectangle g = graphics.getClip(new Rectangle());
        Rectangle l = getFlowPage().getBounds().getCopy();
        
        // Label margin
        l.expand(labelMargin, labelMargin);
        
        // Create a Path that fills the clipping area minus the label
        Path path = new Path(null);
        
        path.moveTo(g.x, g.y);
        path.lineTo(l.x, l.y);
        path.lineTo(l.x + l.width, l.y);
        path.lineTo(l.x + l.width, l.y + l.height);
        path.lineTo(l.x, l.y + l.height);
        path.lineTo(l.x, l.y);
        path.lineTo(g.x, g.y);
        path.lineTo(g.x, g.y + g.height);
        path.lineTo(g.x + g.width, g.y + g.height);
        path.lineTo(g.x + g.width, g.y);
        path.lineTo(g.x, g.y);
        
        graphics.clipPath(path);
        
        super.paintFigure(graphics);
        
        path.dispose();
    }
}
