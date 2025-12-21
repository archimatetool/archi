/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Translatable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

import com.archimatetool.editor.diagram.util.AnimationUtil;
import com.archimatetool.model.IDiagramModelObject;



/**
 * Abstract Container Figure
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractContainerFigure extends AbstractDiagramModelObjectFigure
implements IContainerFigure {
    
    protected static Color highlightedColor = new Color(0, 0, 255);

    protected boolean showTargetFeedback = false;
    private IFigure mainFigure;
    
    protected AbstractContainerFigure() {
    }
    
    protected AbstractContainerFigure(IDiagramModelObject diagramModelObject) {
        super(diagramModelObject);
    }

    /**
     * @return The main figure to draw on
     */
    public IFigure getMainFigure() {
        if(mainFigure == null) {
            mainFigure = new FreeformLayer();
            mainFigure.setLayoutManager(new XYLayout());
            
            // Have to add this if we want Animation to work on figures
            AnimationUtil.addFigureForAnimation(mainFigure);
        }
        
        return mainFigure;
    }
    
    @Override
    public IFigure getContentPane() {
        return getMainFigure();
    }

    @Override
    public void translateMousePointToRelative(Translatable t) {
        getContentPane().translateToRelative(t);
    }
    
    @Override
    protected boolean useLocalCoordinates() {
        // Very important!
        return true;
    }
    
    @Override
    public Dimension getDefaultSize() {
        // Calculate default size based on children
        if(getContentPane().getChildren().size() > 0) {
            // Start with zero and build up from that...
            Dimension d = new Dimension();
            
            for(Object child : getContentPane().getChildren()) {
                IFigure figure = (IFigure)child;
                Rectangle bounds = figure.getBounds();
                d.width = Math.max(bounds.x + bounds.width + 10, d.width);
                d.height = Math.max(bounds.y + bounds.height + 10, d.height);
            }
            
            return d.union(super.getDefaultSize());
        }
        
        // No children...
        return super.getDefaultSize();
    }

    @Override
    protected void paintFigure(Graphics graphics) {
        graphics.setAntialias(SWT.ON);
        drawFigure(graphics);
        if(showTargetFeedback) {
            drawTargetFeedback(graphics);
        }
    }
    
    /**
     * Draw hover-over highlighting
     * @param graphics
     */
    protected void drawTargetFeedback(Graphics graphics) {
        graphics.pushState();
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }

        Rectangle rect = getBounds().getCopy();
        
        // Scaling
        double scale = FigureUtils.getFigureScale(this);
        if(scale == 1.5) {
            // Reduce width and height by 1 pixel
            rect.resize(-1, -1);
        }
        else if(scale < 1) {
            // Reduce width and height by 2 pixels
            rect.resize(-2, -2);
        }
        
        rect.shrink(1, 1);
        graphics.setForegroundColor(highlightedColor);
        graphics.setLineWidth(2);
        graphics.drawRectangle(rect);
        
        graphics.popState();
    }
    
    @Override
    public void showTargetFeedback(boolean show) {
        if(showTargetFeedback != show) {
            showTargetFeedback = show;
            repaint();
        }
    }
}
