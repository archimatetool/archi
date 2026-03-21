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
import com.archimatetool.editor.ui.IIconDelegate;
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
        
        Rectangle rect = getBounds().getCopy();
        
        final boolean drawOutline = getLineStyle() != IDiagramModelObject.LINE_STYLE_NONE;
        
        // Rectangle type
        if(getDiagramModelArchimateObject().getType() == 0) {
            // Fill
            graphics.setBackgroundColor(getFillColor());
            graphics.setAlpha(getAlpha());
            Pattern gradient = applyGradientPattern(graphics, rect);
            graphics.fillRectangle(rect);
            disposeGradientPattern(graphics, gradient);
            
            // Icon Image
            if(getIconicDelegate() != null) {
                getIconicDelegate().setTopOffset(0);
                drawIconImage(graphics, getBounds().getCopy());
            }
            
            // Outline
            if(drawOutline) {
                graphics.setForegroundColor(getLineColor());
                setLineStyle(graphics);
                graphics.setAlpha(getLineAlpha());
                FigureUtils.drawRectangle(graphics, rect, getLineWidth());
            }
        }
        // Tabbed
        else {
            tabWidth = (int)(rect.width / INSET);
            tabHeight = TOPBAR_HEIGHT;
            
            if(getDiagramModelArchimateObject().getTextPosition() == ITextPosition.TEXT_POSITION_TOP) {
                int textWidth = FigureUtilities.getTextExtents(getText(), getFont()).width;
                tabWidth = Math.min(Math.max(tabWidth, textWidth + 8), rect.width);
                
                // Tab height is calculated from font height
                int textHeight = FigureUtilities.getFontMetrics(getFont()).getHeight();
                
                // Tab height is calculated from the text control height which includes all text
                // int textHeight = getTextControl().getBounds().height;
                
                tabHeight = Math.max(TOPBAR_HEIGHT, textHeight);
            }
            
            Rectangle topRectangle = new Rectangle(rect.x, rect.y, tabWidth, tabHeight);
            Rectangle mainRectangle = new Rectangle(rect.x, rect.y + tabHeight, rect.width, rect.height - tabHeight);
            
            // Top rectangle fill
            graphics.setBackgroundColor(getFillColor());
            graphics.setAlpha(getAlpha());
            Pattern gradient = applyGradientPattern(graphics, rect);
            graphics.fillRectangle(topRectangle);
            
            // Main rectangle fill
            graphics.fillRectangle(mainRectangle);
            disposeGradientPattern(graphics, gradient);

            // Icon Image
            if(getIconicDelegate() != null) {
                getIconicDelegate().setTopOffset(tabHeight);
                drawIconImage(graphics, getBounds().getCopy());
            }

            if(drawOutline) {
                graphics.setForegroundColor(getLineColor());
                graphics.setAlpha(getLineAlpha());
                setLineStyle(graphics);
                
                // Main rectangle
                FigureUtils.drawRectangle(graphics, mainRectangle, getLineWidth());
                
                // Top rectangle
                Path path = new Path(null);
                path.moveTo(rect.x, rect.y + tabHeight);
                path.lineTo(rect.x, rect.y);
                path.lineTo(rect.x + tabWidth, rect.y);
                path.lineTo(rect.x + tabWidth, rect.y + tabHeight);
                FigureUtils.drawPath(graphics, path, getLineWidth());
                path.dispose();
            }
        }
        
        graphics.popState();
        
        // Draw this *after* popState
        if(getDiagramModelArchimateObject().getType() == 0) {
            drawIcon(graphics);
        }
    }
    
    @Override
    protected Rectangle calculateTextControlBounds() {
        Rectangle rect = getBounds().getCopy();
        
        int textPosition = ((ITextPosition)getDiagramModelObject()).getTextPosition();
        int textAlignment = getDiagramModelObject().getTextAlignment();
        
        if(textPosition == ITextPosition.TEXT_POSITION_TOP) {
            rect.y += 5 - getTextControlMarginHeight();
            rect.y -= Math.max(3, FigureUtilities.getFontMetrics(getFont()).getLeading());
            
            // Adjust for icon
            if(getIconOffset() != 0 && isIconVisible() && textAlignment == ITextAlignment.TEXT_ALIGNMENT_RIGHT) {
                int iconOffset = getIconOffset() - getTextControlMarginWidth();
                rect.width -= iconOffset;
            }
        }
        
        return rect;
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
            getIconDelegate().drawIcon(graphics, getIconColor(), null, getIconOrigin());
        }
    }
    
    private static IIconDelegate iconDelegate = new IIconDelegate() {
        @Override
        public void drawIcon(Graphics graphics, Color foregroundColor, Color backgroundColor, Point pt) {
            graphics.pushState();
            
            // Ensure this is set
            graphics.setAntialias(SWT.ON);

            graphics.setLineWidth(1);
            
            if(foregroundColor != null) {
                graphics.setForegroundColor(foregroundColor);
            }
            
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
    };
    
    public static IIconDelegate getIconDelegate() {
        return iconDelegate;
    }

    /**
     * @return The icon start position
     */
    private Point getIconOrigin() {
        Rectangle rect = getBounds();
        return new Point(rect.x + rect.width - 17, rect.y + 6);
    }
    
    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0 ? 20 : 0;
    }
}
