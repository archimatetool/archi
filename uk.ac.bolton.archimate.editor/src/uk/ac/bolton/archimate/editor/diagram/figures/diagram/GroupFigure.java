/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.figures.diagram;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Translatable;
import org.eclipse.swt.SWT;

import uk.ac.bolton.archimate.editor.diagram.figures.AbstractLabelContainerFigure;
import uk.ac.bolton.archimate.editor.diagram.figures.ToolTipFigure;
import uk.ac.bolton.archimate.editor.diagram.util.AnimationUtil;
import uk.ac.bolton.archimate.editor.ui.ColorFactory;
import uk.ac.bolton.archimate.model.IDiagramModelObject;

/**
 * Group Figure
 * 
 * @author Phillip Beauvoir
 */
public class GroupFigure
extends AbstractLabelContainerFigure {
    
    protected static Dimension DEFAULT_SIZE = new Dimension(400, 140);
    protected static int FOLD_HEIGHT = 18;
    protected static int SHADOW_OFFSET = 2;
    
    /**
     * Connection Anchor adjusts for Group shape
     */
    static class GroupFigureConnectionAnchor extends ChopboxAnchor {
        GroupFigureConnectionAnchor(IFigure owner) {
            super(owner);
        }
        
        @Override
        public Point getLocation(Point reference) {
            Point pt = super.getLocation(reference);
            
            Rectangle r = getBox().getCopy();
            getOwner().translateToAbsolute(r);
            
            int shiftY = FOLD_HEIGHT - (pt.y - r.y) - 1;
            
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
                Rectangle bounds = getBounds().getCopy();
                bounds.x = 0;
                bounds.y = FOLD_HEIGHT;
                bounds.width -= SHADOW_OFFSET;
                bounds.height -= FOLD_HEIGHT + SHADOW_OFFSET;
                target.setBounds(bounds);
            }
        };
        
        add(getMainFigure(), locator);
        
        // Have to add this if we want Animation to work on figures!
        AnimationUtil.addFigureForAnimation(getMainFigure());
    }
    
    public ConnectionAnchor createConnectionAnchor() {
        return new GroupFigureConnectionAnchor(this);
    }
    
    @Override
    public IFigure getToolTip() {
        ToolTipFigure tooltip = (ToolTipFigure)super.getToolTip();
        
        if(tooltip == null) {
            return null;
        }
        
        tooltip.setType("Type: Group");
        
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
        
        // Shadow fill
        int[] points1 = new int[] {
                bounds.x + SHADOW_OFFSET, bounds.y + SHADOW_OFFSET,
                bounds.x + SHADOW_OFFSET + (bounds.width / 2), bounds.y + SHADOW_OFFSET,
                bounds.x + SHADOW_OFFSET + (bounds.width / 2), bounds.y + SHADOW_OFFSET + FOLD_HEIGHT,
                bounds.x + bounds.width, bounds.y + SHADOW_OFFSET + FOLD_HEIGHT,
                bounds.x + bounds.width, bounds.y + bounds.height,
                bounds.x + SHADOW_OFFSET, bounds.y + bounds.height
        };
        graphics.setAlpha(100);
        graphics.setBackgroundColor(ColorConstants.black);
        graphics.fillPolygon(points1);
        
        // Fill
        int[] points2 = new int[] {
                bounds.x, bounds.y,
                bounds.x + (bounds.width / 2) - 1, bounds.y,
                bounds.x + (bounds.width / 2) - 1, bounds.y + FOLD_HEIGHT,
                bounds.x, bounds.y + FOLD_HEIGHT,
        };
        graphics.setAlpha(255);
        graphics.setBackgroundColor(ColorFactory.getDarkerColor(getFillColor()));
        graphics.fillPolygon(points2);
       
        int[] points3 = new int[] {
                bounds.x, bounds.y + FOLD_HEIGHT,
                bounds.x + bounds.width - SHADOW_OFFSET - 1, bounds.y + FOLD_HEIGHT,
                bounds.x + bounds.width - SHADOW_OFFSET - 1, bounds.y + bounds.height - SHADOW_OFFSET - 1,
                bounds.x, bounds.y + bounds.height - SHADOW_OFFSET - 1
        };
        graphics.setBackgroundColor(getFillColor());
        graphics.fillPolygon(points3);
        
        // Line
        graphics.setForegroundColor(ColorConstants.black);
        graphics.drawPolygon(points2);
        graphics.drawPolygon(points3);
    }
    
    @Override
    protected void drawTargetFeedback(Graphics graphics) {
        Rectangle bounds = getMainFigure().getBounds().getCopy();
        graphics.pushState();
        graphics.setForegroundColor(ColorConstants.blue);
        graphics.setLineWidth(2);
        bounds.x++;
        translateToParent(bounds);
        graphics.drawRectangle(bounds);
        graphics.popState();
    }
}
