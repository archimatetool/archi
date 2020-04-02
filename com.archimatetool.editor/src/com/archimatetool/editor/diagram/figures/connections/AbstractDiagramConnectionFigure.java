/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.connections;

import java.lang.reflect.Field;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionLocator;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.ScaledGraphics;
// line-curves patch by Jean-Baptiste Sarrodie (aka Jaiguru)
// Use alternate PolylineConnection
//import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.editparts.ZoomListener;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Path;

import com.archimatetool.editor.diagram.figures.ToolTipFigure;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.FontFactory;
import com.archimatetool.editor.utils.PlatformUtils;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IDiagramModelConnection;



/**
 * Abstract implementation of a connection figure.  Subclasses should decide how to draw the line
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractDiagramConnectionFigure
extends RoundedPolylineConnection implements IDiagramConnectionFigure {
//extends PolylineConnection implements IDiagramConnectionFigure {

    private Label fConnectionLabel;
    
    protected int fTextPosition = -1;
    
    private IDiagramModelConnection fDiagramModelConnection;

    protected Color fFontColor;
    protected Color fLineColor;
    
    protected boolean SHOW_TARGET_FEEDBACK = false;
    
    private ZoomListener zoomListener = new ZoomListener() {
        @Override
        public void zoomChanged(double newZoomValue) {
            handleZoomChanged(newZoomValue);
        }
    };
    
    protected ZoomManager zoomManager;
    
    /**
     * Set the Zoom Manager
     * @param manager
     */
    public void setZoomManager(ZoomManager manager) {
        if(zoomManager != manager) {
            if(zoomManager != null) {
                zoomManager.removeZoomListener(zoomListener);
            }
            
            zoomManager = manager;
            
            if(zoomManager != null) {
                zoomManager.addZoomListener(zoomListener);
                handleZoomChanged(zoomManager.getZoom());
            }
        }
    }

    /**
     * Zoom Factor changed - deal with it :-)
     * @param newZoomValue
     */
    protected void handleZoomChanged(double newZoomValue) {
    }
    
	@Override
    public void setModelConnection(IDiagramModelConnection connection) {
	    fDiagramModelConnection = connection;
	    
	    setFigureProperties();
	}
	
	@Override
    public IDiagramModelConnection getModelConnection() {
	    return fDiagramModelConnection;
	}
	
	protected void setFigureProperties() {
		setTargetDecoration(new PolygonDecoration()); // arrow at target endpoint
	}
	
    @Override
    public void refreshVisuals() {
        // If the text position has been changed by user update it
        if(fDiagramModelConnection.getTextPosition() != fTextPosition) {
            fTextPosition = fDiagramModelConnection.getTextPosition();
            setLabelLocator(fTextPosition);
        }
        
        setLabelFont();
        
        setLabelFontColor();
        
        setLineColor();
        
        setConnectionText();
        
        setLineWidth();
        
        getConnectionLabel().setOpaque(Preferences.STORE.getInt(IPreferenceConstants.CONNECTION_LABEL_STRATEGY) == CONNECTION_LABEL_OPAQUE);
        
        repaint(); // repaint when figure changes
    }

    /**
     * @param copy
     * @return True if the user clicked on the Relationship edit label
     */
    @Override
    public boolean didClickConnectionLabel(Point requestLoc) {
        Label label = getConnectionLabel();
        label.translateToRelative(requestLoc);
        return label.containsPoint(requestLoc);
    }

    @Override
    public Label getConnectionLabel() {
        if(fConnectionLabel == null) {
            fConnectionLabel = new Label(""); //$NON-NLS-1$
            add(fConnectionLabel);
        }
        return fConnectionLabel;
    }

    private void setLabelLocator(int position) {
        Locator locator = null;

        switch(position) {
            case IDiagramModelConnection.CONNECTION_TEXT_POSITION_SOURCE:
                locator = new ArchiConnectionEndpointLocator(this, false);
                break;
            case IDiagramModelConnection.CONNECTION_TEXT_POSITION_MIDDLE:
                locator = new ConnectionLocator(this, ConnectionLocator.MIDDLE);
                break;
            case IDiagramModelConnection.CONNECTION_TEXT_POSITION_TARGET:
                locator = new ArchiConnectionEndpointLocator(this, true);
                break;
        }
        
        setConstraint(getConnectionLabel(), locator);
    }
    
    protected void setConnectionText() {
        boolean displayName = fDiagramModelConnection.isNameVisible();
        getConnectionLabel().setText(displayName ? fDiagramModelConnection.getName().trim() : ""); //$NON-NLS-1$
    }

    /**
     * Set the font in the label to that in the model, or failing that, as per user's default
     */
    protected void setLabelFont() {
        String fontName = fDiagramModelConnection.getFont();
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
        String val = fDiagramModelConnection.getFontColor();
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
        String val = fDiagramModelConnection.getLineColor();
        Color color = ColorFactory.get(val);
        
        if(color == null) {
            color = ColorFactory.getDefaultLineColor(fDiagramModelConnection);
        }
        
        if(color != fLineColor) {
            fLineColor = color;
            setForegroundColor(color);
        }
    }
    
    protected void setLineWidth() {
        setLineWidth(fDiagramModelConnection.getLineWidth());
    }
    
    @Override
    public IFigure getToolTip() {
        if(super.getToolTip() == null && Preferences.doShowViewTooltips()) {
            setToolTip(new ToolTipFigure());
        }
        return Preferences.doShowViewTooltips() ? super.getToolTip() : null;
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
            setForegroundColor(ColorFactory.get(0, 0, 255));
            setLineWidth(getModelConnection().getLineWidth() + 1);
        }
        else {
            setLineWidth();
            setForegroundColor(fLineColor);
        }

        if(StringUtils.isSet(fConnectionLabel.getText()) && 
                Preferences.STORE.getInt(IPreferenceConstants.CONNECTION_LABEL_STRATEGY) == CONNECTION_LABEL_CLIPPED) {
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
        Rectangle l = fConnectionLabel.getTextBounds();
        
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
        
        // Monkey patch to fix NPE when connection is disabled when Viewpoint set
        // If we set private field sharedClipping to false then SWTGraphics.checkSharedClipping() is not called
        // See https://github.com/archimatetool/archi/issues/431
        if(!isEnabled()) {
            try {
                Graphics graphicsCopy = graphics;
                
                // ScaledGraphics is a wrapper around SWTGraphics
                if(graphics instanceof ScaledGraphics) {
                    Field f = graphicsCopy.getClass().getDeclaredField("graphics"); //$NON-NLS-1$
                    f.setAccessible(true);
                    graphicsCopy = (Graphics)f.get(graphics);
                }
                
                Field f = graphicsCopy.getClass().getDeclaredField("sharedClipping"); //$NON-NLS-1$
                f.setAccessible(true);
                f.set(graphicsCopy, false);
            }
            catch(NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                // Consume this exception as this only applies to SWTGraphics and ScaledGraphics
                // SVG Image Export uses GraphicsToGraphics2DAdaptor and will throw a NoSuchFieldException
            }
        }

        super.paintFigure(graphics);
        
        path.dispose();
    }
}
