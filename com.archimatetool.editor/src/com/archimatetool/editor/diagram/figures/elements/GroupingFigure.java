/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.FigureUtils;
import com.archimatetool.editor.diagram.figures.FigureUtils.Direction;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.ITextPosition;




/**
 * Grouping Figure
 * 
 * @author Phillip Beauvoir
 */
public class GroupingFigure extends AbstractTextControlContainerFigure {
    
    private static final int TOPBAR_HEIGHT = 18;
    private static final float INSET = 1.4f;
    
    public GroupingFigure() {
        super(TEXT_FLOW_CONTROL);
    }
    
    @Override
    protected void drawFigure(Graphics graphics) {
        graphics.pushState();
        
        Rectangle bounds = getBounds().getCopy();
        
        bounds.width--;
        bounds.height--;
        
        graphics.setAntialias(SWT.ON);
        
        graphics.setAlpha(getAlpha());
        
        // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
        setLineWidth(graphics, 1, bounds);

        graphics.setLineDash(new float[] { (float)(8 * graphics.getAbsoluteScale()), (float)(4 * graphics.getAbsoluteScale()) });
        
        graphics.setBackgroundColor(getFillColor());
        graphics.setForegroundColor(getLineColor());
        
        Pattern gradient = null;
        if(getGradient() != IDiagramModelObject.GRADIENT_NONE) {
            gradient = FigureUtils.createGradient(graphics, bounds, getFillColor(), getAlpha(), Direction.get(getGradient()));
            graphics.setBackgroundPattern(gradient);
        }
        
        int type = getDiagramModelObject().getType();
        
        int[] mainRectangle;
        
        if(type == 1) {
            mainRectangle = new int[] {
                    bounds.x, bounds.y,
                    bounds.x + bounds.width, bounds.y,
                    bounds.x + bounds.width, bounds.y + bounds.height,
                    bounds.x, bounds.y + bounds.height
            };
            
            Path path = FigureUtils.createPathFromPoints(mainRectangle);
            graphics.fillPath(path);
            path.dispose();
        }
        else {
            mainRectangle = new int[] {
                    bounds.x, bounds.y + TOPBAR_HEIGHT,
                    bounds.x + bounds.width, bounds.y + TOPBAR_HEIGHT,
                    bounds.x + bounds.width, bounds.y + bounds.height,
                    bounds.x, bounds.y + bounds.height
            };
            
            int[] fillShape = new int[] {
                    bounds.x, bounds.y,
                    (int)(bounds.x + (bounds.width / INSET)), bounds.y,
                    (int)(bounds.x + (bounds.width / INSET)), bounds.y + TOPBAR_HEIGHT,
                    bounds.getRight().x, bounds.y + TOPBAR_HEIGHT,
                    bounds.getRight().x, bounds.getBottom().y,
                    bounds.x, bounds.getBottom().y
            };
            
            Path path = FigureUtils.createPathFromPoints(fillShape);
            graphics.fillPath(path);
            path.dispose();
            
            graphics.setAlpha(getLineAlpha());
            graphics.drawLine(bounds.x, bounds.y, bounds.x, bounds.y + TOPBAR_HEIGHT);
            graphics.drawLine(bounds.x, bounds.y, (int)(bounds.x + (bounds.width / INSET)), bounds.y);
            graphics.drawLine((int)(bounds.x + (bounds.width / INSET)), bounds.y, (int)(bounds.x + (bounds.width / INSET)), bounds.y + TOPBAR_HEIGHT);
        }
        
        if(gradient != null) {
            gradient.dispose();
        }

        // Outlines
        graphics.setAlpha(getLineAlpha());
        graphics.drawPolygon(mainRectangle);
        
        graphics.popState();
    }
    
    @Override
    protected Rectangle calculateTextControlBounds() {
        Rectangle bounds = getBounds().getCopy();
        
        int textPosition = ((ITextPosition)getDiagramModelObject()).getTextPosition();
        if(textPosition == ITextPosition.TEXT_POSITION_TOP) {
            bounds.y -= 3;
        }
        
        return bounds;
    }
    
    /**
     * Connection Anchor adjusts for Group shape
     */
    class GroupingFigureConnectionAnchor extends ChopboxAnchor {
        public GroupingFigureConnectionAnchor(IFigure owner) {
            super(owner);
        }
        
        @Override
        public Point getLocation(Point reference) {
            Point pt = super.getLocation(reference);
            
            int type = getDiagramModelObject().getType();

            if(type == 1) {
                return pt;
            }
            
            Rectangle r = getBox().getCopy();
            getOwner().translateToAbsolute(r);
            
            int shiftY = TOPBAR_HEIGHT - (pt.y - r.y) - 1;
            
            if(pt.x > r.x + (r.width / INSET) && shiftY > 0) {
                pt.y += shiftY;
            }
            
            return pt;
        };
    }
    
    @Override
    public IDiagramModelArchimateObject getDiagramModelObject() {
        return (IDiagramModelArchimateObject)super.getDiagramModelObject();
    }

    @Override
    public ConnectionAnchor getDefaultConnectionAnchor() {
        return new GroupingFigureConnectionAnchor(this);
    }
}
