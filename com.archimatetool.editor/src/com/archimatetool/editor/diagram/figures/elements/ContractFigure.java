/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

import com.archimatetool.editor.diagram.figures.AbstractDiagramModelObjectFigure;
import com.archimatetool.editor.diagram.figures.FigureUtils;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.RectangleFigureDelegate;
import com.archimatetool.editor.ui.IIconDelegate;
import com.archimatetool.model.ITextPosition;

/**
 * Contract Figure
 * 
 * @author Phillip Beauvoir
 * @author jbsarrodie
 */
public class ContractFigure extends ObjectFigure {
    
    private IFigureDelegate rectangleDelegate;
    private IFigureDelegate contractDelegate;

    class ContractFigureDelegate extends ObjectFigureDelegate {
        ContractFigureDelegate(AbstractDiagramModelObjectFigure owner) {
            super(owner);
        }
        
        @Override
        public void drawFigure(Graphics graphics) {
            super.drawFigure(graphics);
            
            graphics.pushState();
            
            Rectangle rect = getBounds();
            
            // Line
            graphics.setForegroundColor(getLineColor());
            graphics.setAlpha(getLineAlpha());
            graphics.setLineWidth(getLineWidth());
            graphics.drawLine(rect.x, rect.getBottom().y - TOP_MARGIN, rect.getRight().x, rect.getBottom().y - TOP_MARGIN);
            
            graphics.popState();
        }
        
        @Override
        public Rectangle calculateTextControlBounds() {
            int textPosition = ((ITextPosition)getDiagramModelObject()).getTextPosition();
            if(textPosition == ITextPosition.TEXT_POSITION_BOTTOM) {
                Rectangle rect = getBounds();
                rect.y -= TOP_MARGIN - getTextControlMarginHeight();
                return rect;
            }
            else {
                return super.calculateTextControlBounds();
            }
        }
    }

    public ContractFigure() {
        rectangleDelegate = new RectangleFigureDelegate(this);
        contractDelegate = new ContractFigureDelegate(this);
    }

    // Icon glyph padding/corner radius are the same as ObjectFigure's - inherits ICON_PADDING/
    // ICON_BOX_CORNER_RADIUS from ObjectFigure rather than redeclaring them, so the two stay in sync if
    // ObjectFigure's are ever retuned

    /**
     * Draw the icon. In Outline shape style, on a small containing box colored the same as the outline, with the
     * icon itself drawn as an outline in the view's background color so the box color shows through, and the
     * box's top-right corner flush with the top-right corner of the figure (all corners are square).
     * In Classic shape style, as a plain icon in the figure's icon color.
     */
    @Override
    protected void drawIcon(Graphics graphics) {
        if(isOutlineShapeStyle()) {
            FigureUtils.drawOutlineStyleIcon(graphics, this, getIconDelegate(), ICON_PADDING, ICON_BOX_CORNER_RADIUS);
        }
        else if(isIconVisible()) {
            getIconDelegate().drawIcon(graphics, getIconColor(), null, getClassicIconOrigin());
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

            Rectangle rect = new Rectangle(pt.x, pt.y, 13, 10);
            if(backgroundColor != null) {
                graphics.fillRectangle(rect);
            }
            graphics.drawRectangle(rect);
            graphics.drawLine(pt.x, pt.y + 3, pt.x + 13, pt.y + 3);
            graphics.drawLine(pt.x, pt.y + 7, pt.x + 13, pt.y + 7);

            graphics.popState();
        }

        @Override
        public Rectangle getBounds() {
            // The rectangle itself defines the full extent - both header lines inside it don't extend beyond it
            return new Rectangle(0, 0, 13, 10);
        }
    };

    public static IIconDelegate getIconDelegate() {
        return iconDelegate;
    }

    // getIconDelegate() is static, so it doesn't participate in overriding: if getIconOffset() were left
    // inherited from ObjectFigure, its unqualified getIconDelegate() call would resolve to ObjectFigure's own
    // icon delegate, not this class's (their bounds happen to match today, but that's not guaranteed to hold).
    // Override explicitly so it uses its own.
    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0
                ? (isOutlineShapeStyle() ? FigureUtils.getOutlineIconBoxWidth(getIconDelegate(), ICON_PADDING) : 20) : 0;
    }

    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? rectangleDelegate : contractDelegate;
    }
}
