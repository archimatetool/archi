/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.FigureUtils;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.ITextAlignment;
import com.archimatetool.model.ITextPosition;




/**
 * Grouping Figure
 * 
 * @author Phillip Beauvoir
 */
public class GroupingFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    private static final int TOPBAR_HEIGHT = 18;
    private static final float INSET = 1.4f;
    
    private int tabHeight;
    private int tabWidth;
    
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
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        boolean drawOutline = getLineStyle() != IDiagramModelObject.LINE_STYLE_NONE;
        
        if(drawOutline) {
            // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
            setLineWidth(graphics, bounds);
            setLineStyle(graphics);
        }
        
        graphics.setBackgroundColor(getFillColor());
        graphics.setForegroundColor(getLineColor());
        
        Pattern gradient = applyGradientPattern(graphics, bounds);
        
        int[] mainRectangle;
        
        if(getDiagramModelArchimateObject().getType() == 0) {
            mainRectangle = new int[] {
                    bounds.x, bounds.y,
                    bounds.x + bounds.width, bounds.y,
                    bounds.x + bounds.width, bounds.y + bounds.height,
                    bounds.x, bounds.y + bounds.height
            };
            
            Path path = FigureUtils.createPathFromPoints(mainRectangle);
            graphics.fillPath(path);
            path.dispose();
            
            // Icon
            if(getIconicDelegate() != null) {
                getIconicDelegate().setTopOffset(0);
                drawIconImage(graphics, bounds);
            }
        }
        else {
            tabWidth = (int)(bounds.width / INSET);
            tabHeight = TOPBAR_HEIGHT;
            
            if(getDiagramModelArchimateObject().getTextPosition() == ITextPosition.TEXT_POSITION_TOP) {
                int textWidth = FigureUtilities.getTextExtents(getText(), getFont()).width;
                tabWidth = Math.min(Math.max(tabWidth, textWidth + 8), bounds.width);
                
                // Tab height is calculated from font height
                int textHeight = FigureUtilities.getFontMetrics(getFont()).getHeight();
                
                // Tab height is calculated from the text control height which includes all text
                // int textHeight = getTextControl().getBounds().height;
                
                tabHeight = Math.max(TOPBAR_HEIGHT, textHeight);
            }
            
            mainRectangle = new int[] {
                    bounds.x, bounds.y + tabHeight,
                    bounds.x + bounds.width, bounds.y + tabHeight,
                    bounds.x + bounds.width, bounds.y + bounds.height,
                    bounds.x, bounds.y + bounds.height
            };
            
            int[] fillShape = new int[] {
                    bounds.x, bounds.y,
                    bounds.x + tabWidth, bounds.y,
                    bounds.x + tabWidth, bounds.y + tabHeight,
                    bounds.getRight().x, bounds.y + tabHeight,
                    bounds.getRight().x, bounds.getBottom().y,
                    bounds.x, bounds.getBottom().y
            };
            
            Path path = FigureUtils.createPathFromPoints(fillShape);
            graphics.fillPath(path);
            path.dispose();
            
            // Icon
            if(getIconicDelegate() != null) {
                getIconicDelegate().setTopOffset(tabHeight);
                drawIconImage(graphics, bounds);
            }

            if(drawOutline) {
                graphics.setAlpha(getLineAlpha());
                graphics.drawLine(bounds.x, bounds.y, bounds.x, bounds.y + tabHeight);
                graphics.drawLine(bounds.x, bounds.y, bounds.x + tabWidth, bounds.y);
                graphics.drawLine(bounds.x + tabWidth, bounds.y, bounds.x + tabWidth, bounds.y + tabHeight);
            }
        }
        
        disposeGradientPattern(graphics, gradient);

        // Outlines
        if(drawOutline) {
            graphics.setAlpha(getLineAlpha());
            graphics.drawPolygon(mainRectangle);
        }
        
        graphics.popState();
        
        if(getDiagramModelArchimateObject().getType() == 0) {
            drawIcon(graphics);
        }
    }
    
    @Override
    protected Rectangle calculateTextControlBounds() {
        Rectangle bounds = getBounds().getCopy();
        
        int textPosition = ((ITextPosition)getDiagramModelObject()).getTextPosition();
        int textAlignment = getDiagramModelObject().getTextAlignment();
        
        if(textPosition == ITextPosition.TEXT_POSITION_TOP) {
            bounds.y += 5 - getTextControlMarginHeight();
            bounds.y -= Math.max(3, FigureUtilities.getFontMetrics(getFont()).getLeading());
            
            // Adjust for icon
            if(getIconOffset() != 0 && isIconVisible() && textAlignment == ITextAlignment.TEXT_ALIGNMENT_RIGHT) {
                int iconOffset = getIconOffset() - getTextControlMarginWidth();
                bounds.width -= iconOffset;
            }
        }
        
        return bounds;
    }
    
    /**
     * Connection Anchor adjusts for Group shape
     */
    private class GroupingFigureConnectionAnchor extends ChopboxAnchor {
        public GroupingFigureConnectionAnchor(IFigure owner) {
            super(owner);
        }
        
        @Override
        public Point getLocation(Point reference) {
            Point pt = super.getLocation(reference);
            
            if(getDiagramModelArchimateObject().getType() == 0) {
                return pt;
            }
            
            Rectangle r = getBox().getCopy();
            getOwner().translateToAbsolute(r);
            
            int shiftY = tabHeight - (pt.y - r.y) - 1;
            
            if(pt.x > r.x + (r.width / INSET) && shiftY > 0) {
                pt.y += shiftY;
            }
            
            return pt;
        };
    }
    
    @Override
    public ConnectionAnchor getDefaultConnectionAnchor() {
        return new GroupingFigureConnectionAnchor(this);
    }
    
    /**
     * Draw the icon
     */
    private void drawIcon(Graphics graphics) {
        if(isIconVisible()) {
            drawIcon(graphics, getIconColor(), null, getIconOrigin());
        }
    }
    
    public static void drawIcon(Graphics graphics, Color foregroundColor, Color backgroundColor, Point pt) {
        graphics.pushState();
        
        graphics.setLineWidth(1);
        graphics.setForegroundColor(foregroundColor);
        if(backgroundColor != null) {
            graphics.setBackgroundColor(backgroundColor);
        }
        
        if(backgroundColor != null) {
            graphics.fillRectangle(pt.x, pt.y, 6, 3);
        }
        graphics.drawRectangle(pt.x, pt.y, 6, 3);
        
        if(backgroundColor != null) {
            graphics.fillRectangle(pt.x, pt.y + 3, 13, 7);
        }
        graphics.drawRectangle(pt.x, pt.y + 3, 13, 7);
        
        graphics.popState();
    }

    /**
     * @return The icon start position
     */
    private Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 17 - getLineWidth(), bounds.y + 6);
    }
    
    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0 ? 20 : 0;
    }
}
