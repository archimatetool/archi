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
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.FigureUtils;
import com.archimatetool.editor.diagram.figures.FigureUtils.Direction;
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
        
        bounds.width--;
        bounds.height--;
        
        // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
        setLineWidth(graphics, 1, bounds);
        
        graphics.setAlpha(getAlpha());
        
        if(getDiagramModelObject().getBorderType() == IDiagramModelGroup.BORDER_TABBED) {
            // Top Rectangle
            graphics.setBackgroundColor(ColorFactory.getDarkerColor(getFillColor()));
            
            Path path1 = new Path(null);
            path1.moveTo(bounds.x, bounds.y);
            path1.lineTo(bounds.x + (bounds.width / INSET), bounds.y);
            path1.lineTo(bounds.x + (bounds.width / INSET), bounds.y + TOPBAR_HEIGHT);
            path1.lineTo(bounds.x, bounds.y + TOPBAR_HEIGHT);
            path1.lineTo(bounds.x, bounds.y);
            graphics.fillPath(path1);
            path1.dispose();
            
            // Main rectangle
            graphics.setBackgroundColor(getFillColor());
            Pattern gradient = createGradient(graphics);
            
            Path path2 = new Path(null);
            path2.moveTo(bounds.x, bounds.y + TOPBAR_HEIGHT);
            path2.lineTo(bounds.x + bounds.width, bounds.y + TOPBAR_HEIGHT);
            path2.lineTo(bounds.x + bounds.width, bounds.y + bounds.height);
            path2.lineTo(bounds.x, bounds.y + bounds.height);
            graphics.fillPath(path2);
            path2.dispose();
            
            if(gradient != null) {
                gradient.dispose();
            }
            
            // Line
            graphics.setForegroundColor(getLineColor());
            graphics.setAlpha(getLineAlpha());
            
            Path path = new Path(null);
            path.moveTo(bounds.x, bounds.y + TOPBAR_HEIGHT);
            path.lineTo(bounds.x, bounds.y);
            path.lineTo(bounds.x + (bounds.width / INSET), bounds.y);
            path.lineTo(bounds.x + (bounds.width / INSET), bounds.y + TOPBAR_HEIGHT);
            graphics.drawPath(path);
            path.dispose();
            
            graphics.drawRectangle(bounds.x, bounds.y + TOPBAR_HEIGHT, bounds.width, bounds.height - TOPBAR_HEIGHT);
        }
        else {
            graphics.setBackgroundColor(getFillColor());
            Pattern gradient = createGradient(graphics);
            
            graphics.fillRectangle(bounds);
            
            if(gradient != null) {
                gradient.dispose();
            }
            
            // Line
            graphics.setForegroundColor(getLineColor());
            graphics.setAlpha(getLineAlpha());
            graphics.drawRectangle(bounds);
        }

        graphics.popState();
    }
    
    private Pattern createGradient(Graphics graphics) {
        Pattern gradient = null;
        
        if(getGradient() != IDiagramModelObject.GRADIENT_NONE) {
            gradient = FigureUtils.createGradient(graphics, bounds, getFillColor(), getAlpha(), Direction.get(getGradient()));
            graphics.setBackgroundPattern(gradient);
        }
        
        return gradient;
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
