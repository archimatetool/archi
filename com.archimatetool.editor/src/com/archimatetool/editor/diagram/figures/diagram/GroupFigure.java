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
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.model.IDiagramModelObject;


/**
 * Group Figure
 * 
 * @author Phillip Beauvoir
 */
public class GroupFigure extends AbstractTextControlContainerFigure {
    
    protected static final int TOPBAR_HEIGHT = 18;
    private static final float INSET = 2f;
    
    public GroupFigure(IDiagramModelObject diagramModelObject) {
        super(diagramModelObject, LABEL_CONTROL);
    }
    
    @Override
    protected void drawFigure(Graphics graphics) {
        graphics.pushState();
        
        Rectangle bounds = getBounds().getCopy();
        
        graphics.setAntialias(SWT.ON);
        
        graphics.setAlpha(getAlpha());
        
        int[] topRectangle = new int[] {
                bounds.x, bounds.y,
                (int)(bounds.x + (bounds.width / INSET) - 1), bounds.y,
                (int)(bounds.x + (bounds.width / INSET) - 1), bounds.y + TOPBAR_HEIGHT,
                bounds.x, bounds.y + TOPBAR_HEIGHT,
        };

        int[] mainRectangle = new int[] {
                bounds.x, bounds.y + TOPBAR_HEIGHT,
                bounds.x + bounds.width - 1, bounds.y + TOPBAR_HEIGHT,
                bounds.x + bounds.width - 1, bounds.y + bounds.height - 1,
                bounds.x, bounds.y + bounds.height - 1
        };
        
        graphics.setBackgroundColor(ColorFactory.getDarkerColor(getFillColor()));
        graphics.fillPolygon(topRectangle);
       
        graphics.setBackgroundColor(getFillColor());

        Pattern gradient = null;
        if(Preferences.STORE.getBoolean(IPreferenceConstants.SHOW_GRADIENT)) {
            gradient = FigureUtils.createGradient(graphics, bounds, getFillColor(), getAlpha());
            graphics.setBackgroundPattern(gradient);
        }
        
        graphics.fillPolygon(mainRectangle);
        
        if(gradient != null) {
            gradient.dispose();
        }

        // Line
        graphics.setForegroundColor(getLineColor());
        graphics.drawPolygon(topRectangle);
        graphics.drawPolygon(mainRectangle);
        
        graphics.popState();
    }
    
    @Override
    protected Rectangle calculateTextControlBounds() {
        Rectangle bounds = getBounds().getCopy();
        
        // This first
        bounds.x += 5;
        bounds.y += 2;

        bounds.width = getTextControl().getPreferredSize().width;
        bounds.height = getTextControl().getPreferredSize().height;
        
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
    static class GroupFigureConnectionAnchor extends ChopboxAnchor {
        public GroupFigureConnectionAnchor(IFigure owner) {
            super(owner);
        }
        
        @Override
        public Point getLocation(Point reference) {
            Point pt = super.getLocation(reference);
            
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
