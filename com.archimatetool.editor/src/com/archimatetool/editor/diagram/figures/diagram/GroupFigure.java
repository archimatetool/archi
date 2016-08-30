/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.diagram;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.figures.AbstractLabelContainerFigure;
import com.archimatetool.editor.diagram.figures.GradientUtils;
import com.archimatetool.editor.diagram.figures.ToolTipFigure;
import com.archimatetool.editor.diagram.util.AnimationUtil;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.model.IDiagramModelObject;


/**
 * Group Figure
 * 
 * @author Phillip Beauvoir
 */
public class GroupFigure
extends AbstractLabelContainerFigure {
    
    protected static final int TOPBAR_HEIGHT = 18;
    
    /**
     * Connection Anchor adjusts for Group shape
     */
    public static class GroupFigureConnectionAnchor extends ChopboxAnchor {
        public GroupFigureConnectionAnchor(IFigure owner) {
            super(owner);
        }
        
        @Override
        public Point getLocation(Point reference) {
            Point pt = super.getLocation(reference);
            
            Rectangle r = getBox().getCopy();
            getOwner().translateToAbsolute(r);
            
            int shiftY = TOPBAR_HEIGHT - (pt.y - r.y) - 1;
            
            if(pt.x > r.x + (r.width / 2) && shiftY > 0) {
                pt.y += shiftY;
            }
            
            return pt;
        };
    }

    public GroupFigure(IDiagramModelObject diagramModelObject) {
        super(diagramModelObject);
    }
    
    @Override
    protected void setUI() {
        super.setUI();
        
        Locator mainLocator = new Locator() {
            public void relocate(IFigure target) {
                Rectangle bounds = getBounds().getCopy();
                translateFromParent(bounds);
                target.setBounds(bounds);
            }
        };

        add(getMainFigure(), mainLocator);
        
        // Have to add this if we want Animation to work on figures!
        AnimationUtil.addFigureForAnimation(getMainFigure());
    }
    
    @Override
    public IFigure getToolTip() {
        ToolTipFigure tooltip = (ToolTipFigure)super.getToolTip();
        
        if(tooltip == null) {
            return null;
        }
        
        tooltip.setType(Messages.GroupFigure_0);
        
        return tooltip;
    }
    
    @Override
    protected Rectangle calculateTextControlBounds() {
        Rectangle bounds = getBounds().getCopy();
        
        // This first
        bounds.x += 5;
        bounds.y += 2;

        bounds.width = getLabel().getPreferredSize().width;
        bounds.height = getLabel().getPreferredSize().height;
        
        return bounds;
    }

    @Override
    protected void drawFigure(Graphics graphics) {
        graphics.pushState();
        
        Rectangle bounds = getBounds().getCopy();
        
        graphics.setAntialias(SWT.ON);
        
        // Fill
        int[] points2 = new int[] {
                bounds.x, bounds.y,
                bounds.x + (bounds.width / 2) - 1, bounds.y,
                bounds.x + (bounds.width / 2) - 1, bounds.y + TOPBAR_HEIGHT,
                bounds.x, bounds.y + TOPBAR_HEIGHT,
        };

        graphics.setBackgroundColor(ColorFactory.getDarkerColor(getFillColor()));
        graphics.fillPolygon(points2);
       
        int[] points3 = new int[] {
                bounds.x, bounds.y + TOPBAR_HEIGHT,
                bounds.x + bounds.width - 1, bounds.y + TOPBAR_HEIGHT,
                bounds.x + bounds.width - 1, bounds.y + bounds.height - 1,
                bounds.x, bounds.y + bounds.height - 1
        };
        
        graphics.setBackgroundColor(getFillColor());
        
        Pattern gradient = null;
        if(Preferences.STORE.getBoolean(IPreferenceConstants.SHOW_GRADIENT)) {
            gradient = GradientUtils.createScaledPattern(graphics, bounds, getFillColor());
            graphics.setBackgroundPattern(gradient);
        }
        
        graphics.fillPolygon(points3);
        
        if(gradient != null) {
            gradient.dispose();
        }

        // Line
        graphics.setForegroundColor(getLineColor());
        graphics.drawPolygon(points2);
        graphics.drawPolygon(points3);
        
        graphics.popState();
    }
    
    @Override
    protected void drawTargetFeedback(Graphics graphics) {
        Rectangle bounds = getMainFigure().getBounds().getCopy();
        graphics.pushState();
        graphics.setForegroundColor(ColorConstants.blue);
        graphics.setLineWidth(2);
        bounds.shrink(1, 1);
        translateToParent(bounds);
        graphics.drawRectangle(bounds);
        graphics.popState();
    }
}
