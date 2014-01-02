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
    
    public AbstractContainerFigure(IDiagramModelObject diagramModelObject) {
        super(diagramModelObject);
    }

    /**
     * @return The main figure to draw on
     */
    public IFigure getMainFigure() {
        if(fMainFigure == null) {
            fMainFigure = new FreeformLayer();
            fMainFigure.setLayoutManager(new XYLayout());
        }
        
        return fMainFigure;
    }
    
    public IFigure getContentPane() {
        return getMainFigure();
    }

    public void translateMousePointToRelative(Translatable t) {
        getContentPane().translateToRelative(t);
    }
    
    @Override
    protected boolean useLocalCoordinates() {
        // Very important!
        return true;
    }
    
    @Override
    public Dimension getPreferredSize(int wHint, int hHint) {
        // Calculate preferred size based on children
        if(getContentPane().getChildren().size() > 0) {
            // Start with zero and build up from that...
            Dimension d = new Dimension();
            
            for(Object child : getContentPane().getChildren()) {
                IFigure figure = (IFigure)child;
                Rectangle bounds = figure.getBounds();
                d.width = Math.max(bounds.x + bounds.width + 10, d.width);
                d.height = Math.max(bounds.y + bounds.height + 10, d.height);
            }
            
            return d.union(getDefaultSize());
        }
        
        // No children...
        return getDefaultSize();
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
        bounds.shrink(1, 1);
        graphics.setForegroundColor(ColorConstants.blue);
        graphics.setLineWidth(2);
        graphics.drawRectangle(bounds);
        
        graphics.popState();
    }
    
    public void eraseTargetFeedback() {
        if(SHOW_TARGET_FEEDBACK) {
            SHOW_TARGET_FEEDBACK = false;
            repaint();
        }
    }

    public void showTargetFeedback() {
        if(!SHOW_TARGET_FEEDBACK) {
            SHOW_TARGET_FEEDBACK = true;
            repaint();
        }
    }
    
}
