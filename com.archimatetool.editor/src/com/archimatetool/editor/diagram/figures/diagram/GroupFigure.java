/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.diagram;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.FigureUtils;
import com.archimatetool.editor.diagram.figures.ToolTipFigure;
import com.archimatetool.editor.diagram.figures.FigureUtils.Direction;
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
    
    protected static final int TOPBAR_HEIGHT = 18;
    private static final float INSET = 2f;
    
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
        
        Rectangle bounds = getBounds().getCopy();
        
        graphics.setAntialias(SWT.ON);
        
        graphics.setAlpha(getAlpha());

        graphics.setBackgroundColor(ColorFactory.getDarkerColor(getFillColor()));
        
        int[] topRectangle = null;
        int[] mainRectangle = null;
        
        if(getDiagramModelObject().getBorderType() == IDiagramModelGroup.BORDER_TABBED) {
            topRectangle = new int[] {
                    bounds.x, bounds.y,
                    (int)(bounds.x + (bounds.width / INSET) - 1), bounds.y,
                    (int)(bounds.x + (bounds.width / INSET) - 1), bounds.y + TOPBAR_HEIGHT,
                    bounds.x, bounds.y + TOPBAR_HEIGHT,
            };
            
            mainRectangle = new int[] {
                    bounds.x, bounds.y + TOPBAR_HEIGHT,
                    bounds.x + bounds.width - 1, bounds.y + TOPBAR_HEIGHT,
                    bounds.x + bounds.width - 1, bounds.y + bounds.height - 1,
                    bounds.x, bounds.y + bounds.height - 1
            };
            
            graphics.fillPolygon(topRectangle);
        }
        else {
            mainRectangle = new int[] {
                    bounds.x, bounds.y,
                    bounds.x + bounds.width - 1, bounds.y,
                    bounds.x + bounds.width - 1, bounds.y + bounds.height - 1,
                    bounds.x, bounds.y + bounds.height - 1
            };
        }

        graphics.setBackgroundColor(getFillColor());

        Pattern gradient = null;
        if(getGradient() != IDiagramModelObject.GRADIENT_NONE) {
            gradient = FigureUtils.createGradient(graphics, bounds, getFillColor(), getAlpha(), Direction.get(getGradient()));
            graphics.setBackgroundPattern(gradient);
        }
        
        graphics.fillPolygon(mainRectangle);
        
        if(gradient != null) {
            gradient.dispose();
        }

        // Line
        graphics.setForegroundColor(getLineColor());
        graphics.setAlpha(getLineAlpha());
        if(topRectangle != null) {
            graphics.drawPolygon(topRectangle);
        }
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
    class GroupFigureConnectionAnchor extends ChopboxAnchor {
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
            getOwner().translateToAbsolute(r);
            
            int shiftY = TOPBAR_HEIGHT - (pt.y - r.y) - 1;
            
            if(pt.x > r.x + (r.width / INSET) && shiftY > 0) {
                pt.y += shiftY;
            }
            
            return pt;
        };
    }

    @Override
    public ConnectionAnchor getDefaultConnectionAnchor() {
        return new GroupFigureConnectionAnchor(this);
    }

}
