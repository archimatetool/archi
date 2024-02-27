/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Translatable;
import org.eclipse.swt.SWT;

import com.archimatetool.editor.diagram.util.AnimationUtil;
import com.archimatetool.model.IDiagramModelObject;



/**
 * Abstract Container Figure
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractContainerFigure extends AbstractDiagramModelObjectFigure
implements IContainerFigure {

    protected boolean SHOW_TARGET_FEEDBACK = false;
    
    private IFigure fMainFigure;
    
    protected AbstractContainerFigure() {
    }
    
    protected AbstractContainerFigure(IDiagramModelObject diagramModelObject) {
        super(diagramModelObject);
    }

    /**
     * @return The main figure to draw on
     */
    public IFigure getMainFigure() {
        if(fMainFigure == null) {
            fMainFigure = new FreeformLayer();
            fMainFigure.setLayoutManager(new XYLayout());
            
            // Have to add this if we want Animation to work on figures
            AnimationUtil.addFigureForAnimation(fMainFigure);
        }
        
        return fMainFigure;
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
        if(SHOW_TARGET_FEEDBACK) {
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

        Rectangle bounds = getBounds().getCopy();
        
        // Scaling
        double scale = FigureUtils.getFigureScale(this);
        if(scale == 1.5) {
            bounds.width--;
            bounds.height--;
        }
        else if(scale < 1) {
            bounds.width -= 2;
            bounds.height -= 2;
        }
        
        bounds.shrink(1, 1);
        graphics.setForegroundColor(ColorConstants.blue);
        graphics.setLineWidth(2);
        graphics.drawRectangle(bounds);
        
        graphics.popState();
    }
    
    @Override
    public void eraseTargetFeedback() {
        if(SHOW_TARGET_FEEDBACK) {
            SHOW_TARGET_FEEDBACK = false;
            repaint();
        }
    }

    @Override
    public void showTargetFeedback() {
        if(!SHOW_TARGET_FEEDBACK) {
            SHOW_TARGET_FEEDBACK = true;
            repaint();
        }
    }
    
}
