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
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Translatable;
import org.eclipse.swt.SWT;

import com.archimatetool.editor.diagram.figures.AbstractLabelContainerFigure;
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
    
    protected static final Dimension DEFAULT_SIZE = new Dimension(400, 140);
    public static final int TOPBAR_HEIGHT = 18;
    protected static final int SHADOW_OFFSET = 2;
    
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
        
        Locator locator = new Locator() {
            public void relocate(IFigure target) {
                boolean drawShadows = Preferences.STORE.getBoolean(IPreferenceConstants.SHOW_SHADOWS);
                int shadow_offset = drawShadows ? SHADOW_OFFSET : 0;
                
                Rectangle bounds = getBounds().getCopy();
                bounds.x = 0;
                bounds.y = TOPBAR_HEIGHT;
                bounds.width -= shadow_offset;
                bounds.height -= TOPBAR_HEIGHT + shadow_offset;
                target.setBounds(bounds);
            }
        };
        
        add(getMainFigure(), locator);
        
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
    public void translateMousePointToRelative(Translatable t) {
        getContentPane().translateToRelative(t);
        // compensate for content pane offset
        t.performTranslate(-getContentPane().getBounds().x, -getContentPane().getBounds().y); 
    }

    @Override
    public Dimension getDefaultSize() {
        return DEFAULT_SIZE;
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
        Rectangle bounds = getBounds().getCopy();
        
        graphics.setAntialias(SWT.ON);
        
        boolean drawShadows = Preferences.STORE.getBoolean(IPreferenceConstants.SHOW_SHADOWS);
        int shadow_offset = drawShadows ? SHADOW_OFFSET : 0;
        
        // Shadow fill
        if(drawShadows) {
            int[] points1 = new int[] {
                    bounds.x + shadow_offset, bounds.y + shadow_offset,
                    bounds.x + shadow_offset + (bounds.width / 2), bounds.y + shadow_offset,
                    bounds.x + shadow_offset + (bounds.width / 2), bounds.y + shadow_offset + TOPBAR_HEIGHT,
                    bounds.x + bounds.width, bounds.y + shadow_offset + TOPBAR_HEIGHT,
                    bounds.x + bounds.width, bounds.y + bounds.height,
                    bounds.x + shadow_offset, bounds.y + bounds.height
            };
            graphics.setAlpha(100);
            graphics.setBackgroundColor(ColorConstants.black);
            graphics.fillPolygon(points1);
            graphics.setAlpha(255);    
        }
        
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
                bounds.x + bounds.width - shadow_offset - 1, bounds.y + TOPBAR_HEIGHT,
                bounds.x + bounds.width - shadow_offset - 1, bounds.y + bounds.height - shadow_offset - 1,
                bounds.x, bounds.y + bounds.height - shadow_offset - 1
        };
        graphics.setBackgroundColor(getFillColor());
        graphics.fillPolygon(points3);
        
        // Line
        graphics.setForegroundColor(getLineColor());
        graphics.drawPolygon(points2);
        graphics.drawPolygon(points3);
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
