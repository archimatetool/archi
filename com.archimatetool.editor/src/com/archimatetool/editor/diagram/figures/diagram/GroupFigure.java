/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.diagram;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.ToolTipFigure;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.model.IDiagramModelGroup;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.ITextPosition;


/**
 * Group Figure
 * 
 * @author Phillip Beauvoir
 */
public class GroupFigure extends AbstractTextControlContainerFigure {
    
    private static final int TOPBAR_HEIGHT = 18;
    private static final float INSET = 2f;
    
    private int tabHeight = TOPBAR_HEIGHT;
    private int tabWidth;
    
    public GroupFigure(IDiagramModelObject diagramModelObject) {
        super(diagramModelObject, TEXT_FLOW_CONTROL);
    }
    
    @Override
    public IDiagramModelGroup getDiagramModelObject() {
        return (IDiagramModelGroup)super.getDiagramModelObject();
    }
    
    @Override
    protected void drawFigure(Graphics graphics) {
        graphics.pushState();
        
        final boolean drawOutline = getLineStyle() != IDiagramModelObject.LINE_STYLE_NONE;
        Rectangle rect = drawOutline ? applyLineWidthOffset(graphics) : getBounds().getCopy();
        
        // Tabbed style
        if(getDiagramModelObject().getBorderType() == IDiagramModelGroup.BORDER_TABBED) {
            tabWidth = (int)(rect.width / INSET);
            tabHeight = TOPBAR_HEIGHT;
            
            if(getDiagramModelObject().getTextPosition() == ITextPosition.TEXT_POSITION_TOP) {
                int textWidth = FigureUtilities.getTextExtents(getText(), getFont()).width;
                tabWidth = Math.min(Math.max(tabWidth, textWidth + 8), rect.width);

                // Tab height is calculated from font height
                int textHeight = FigureUtilities.getFontMetrics(getFont()).getHeight();
                
                // Tab height is calculated from the text control height which includes all text
                // int textHeight = getTextControl().getBounds().height;

                tabHeight = Math.max(TOPBAR_HEIGHT, textHeight);
            }
            
            // Top rectangle fill
            graphics.setAlpha(getAlpha());
            graphics.setBackgroundColor(ColorFactory.getDarkerColor(getFillColor()));
            graphics.fillRectangle(createTopRectangle(rect));
            
            // Main rectangle fill (with gradient)
            graphics.setBackgroundColor(getFillColor());
            Pattern gradient = applyGradientPattern(graphics, rect);
            graphics.fillRectangle(createMainRectangle(rect));
            disposeGradientPattern(graphics, gradient);
            
            // Icon Image
            if(getIconicDelegate() != null) {
                getIconicDelegate().setTopOffset(tabHeight);
                drawIconImage(graphics, getBounds().getCopy());
            }

            // Lines
            if(drawOutline) {
                graphics.setForegroundColor(getLineColor());
                graphics.setAlpha(getLineAlpha());
                graphics.setLineWidth(getLineWidth());
                setLineStyle(graphics);
                
                // Main rectangle
                graphics.drawRectangle(createMainRectangle(rect));

                // Top rectangle
                Path path = new Path(null);
                path.moveTo(rect.x, rect.y + tabHeight);
                path.lineTo(rect.x, rect.y);
                path.lineTo(rect.x + tabWidth - 1, rect.y);
                path.lineTo(rect.x + tabWidth - 1, rect.y + tabHeight);
                graphics.drawPath(path);
                path.dispose();
            }
        }
        // Rectangle style
        else {
            // Fill
            graphics.setAlpha(getAlpha());
            graphics.setBackgroundColor(getFillColor());
            Pattern gradient = applyGradientPattern(graphics, rect);
            graphics.fillRectangle(rect);
            disposeGradientPattern(graphics, gradient);
            
            // Icon Image
            if(getIconicDelegate() != null) {
                getIconicDelegate().setTopOffset(0);
                drawIconImage(graphics, getBounds().getCopy());
            }

            // Lines
            if(drawOutline) {
                graphics.setForegroundColor(getLineColor());
                graphics.setAlpha(getLineAlpha());
                graphics.setLineWidth(getLineWidth());
                setLineStyle(graphics);
                graphics.drawRectangle(rect);
            }
        }

        graphics.popState();
    }
    
    private Rectangle createTopRectangle(Rectangle rect) {
        return new Rectangle(rect.x, rect.y, tabWidth, tabHeight);
    }

    private Rectangle createMainRectangle(Rectangle rect) {
        return new Rectangle(rect.x, rect.y + tabHeight, rect.width, rect.height - tabHeight);
    }
    
    @Override
    protected Rectangle calculateTextControlBounds() {
        Rectangle rect = getBounds().getCopy();
        
        int textPosition = getDiagramModelObject().getTextPosition();
        if(textPosition == ITextPosition.TEXT_POSITION_TOP) {
            rect.y += 5 - getTextControlMarginHeight();
            rect.y -= Math.max(3, FigureUtilities.getFontMetrics(getFont()).getLeading());
        }
        
        return rect;
    }

    @Override
    public IFigure getToolTip() {
        ToolTipFigure tooltip = (ToolTipFigure)super.getToolTip();
        
        if(tooltip == null) {
            return null;
        }
        
        tooltip.setText(Messages.GroupFigure_0);
        
        return tooltip;
    }
    
    /**
     * Connection Anchor adjusts for Group shape
     */
    private class GroupFigureConnectionAnchor extends ChopboxAnchor {
        public GroupFigureConnectionAnchor(IFigure owner) {
            super(owner);
        }
        
        @Override
        public Point getLocation(Point reference) {
            Point pt = super.getLocation(reference);
            
            if(getDiagramModelObject().getBorderType() == IDiagramModelGroup.BORDER_RECTANGLE) {
                return pt;
            }

            Rectangle r = getBox().getCopy();
            r.translate(-1, -1);
            r.resize(1, 1);
            getOwner().translateToAbsolute(r);
            
            int shiftY = tabHeight - (pt.y - r.y);
            Dimension d = new Dimension(0, shiftY);
            getOwner().translateToAbsolute(d); // This will take care of any scaling factor on Windows
            shiftY = d.height();
            
            if(pt.x > r.x + (r.width / INSET) && shiftY > 0) {
                pt.y += shiftY;
            }
            
            return pt;
        }
    }

    @Override
    public ConnectionAnchor getDefaultConnectionAnchor() {
        return new GroupFigureConnectionAnchor(this);
    }

}
