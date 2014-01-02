/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.application;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

import com.archimatetool.editor.diagram.figures.AbstractFigureDelegate;
import com.archimatetool.editor.diagram.figures.IDiagramModelObjectFigure;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.ui.ColorFactory;


/**
 * Figure for an Application Component
 * 
 * @author Phillip Beauvoir
 */
public class ApplicationComponentFigureDelegate extends AbstractFigureDelegate {
    
    protected static final int SHADOW_OFFSET = 2;
    protected static final int INDENT = 10;
    protected static final int TEXT_INDENT = 25;
    
    public ApplicationComponentFigureDelegate(IDiagramModelObjectFigure owner) {
        super(owner);
    }

    @Override
    public void drawFigure(Graphics graphics) {
        graphics.pushState();
        
        Rectangle bounds = getBounds();
        
        boolean drawShadows = Preferences.STORE.getBoolean(IPreferenceConstants.SHOW_SHADOWS);
        
        if(isEnabled()) {
            if(drawShadows) {
                graphics.setAlpha(100);
                graphics.setBackgroundColor(ColorConstants.black);
                
                // Main Shadow
                graphics.fillRectangle(bounds.x + INDENT + SHADOW_OFFSET, bounds.y + SHADOW_OFFSET, bounds.width - SHADOW_OFFSET - INDENT, bounds.height - SHADOW_OFFSET);
    
                // Nubs Shadow
                graphics.fillRectangle(bounds.x + SHADOW_OFFSET, bounds.y + 10 + SHADOW_OFFSET, INDENT, 12);
                graphics.fillRectangle(bounds.x + SHADOW_OFFSET, bounds.y + 30 + SHADOW_OFFSET, INDENT, 12);
                
                graphics.setAlpha(255);
            }
        }
        else {
            setDisabledState(graphics);
        }
        
        int shadow_offset = drawShadows ? SHADOW_OFFSET : 0;
        
        // Main Fill
        graphics.setBackgroundColor(getFillColor());
        graphics.fillRectangle(bounds.x + INDENT, bounds.y, bounds.width - shadow_offset - INDENT, bounds.height - shadow_offset);
        
        // Outline
        graphics.setForegroundColor(getLineColor());
        PointList points = new PointList();
        Point pt1 = new Point(bounds.x + INDENT, bounds.y + 10);
        points.addPoint(pt1);
        Point pt2 = new Point(pt1.x, bounds.y);
        points.addPoint(pt2);
        Point pt3 = new Point(bounds.x + bounds.width - shadow_offset - 1, bounds.y);
        points.addPoint(pt3);
        Point pt4 = new Point(pt3.x, bounds.y + bounds.height - shadow_offset - 1);
        points.addPoint(pt4);
        Point pt5 = new Point(pt1.x, pt4.y);
        points.addPoint(pt5);
        Point pt6 = new Point(pt1.x, bounds.y + 42);
        points.addPoint(pt6);
        graphics.drawPolyline(points);
        graphics.drawLine(bounds.x + INDENT, bounds.y + 22, bounds.x + INDENT, bounds.y + 30);
        
        // Nubs Fill
        graphics.setBackgroundColor(ColorFactory.getDarkerColor(getFillColor()));
        graphics.fillRectangle(bounds.x, bounds.y + 10, INDENT * 2 + 1, 13);
        graphics.fillRectangle(bounds.x, bounds.y + 30, INDENT * 2 + 1, 13);
        
        // Nubs Outline
        graphics.setForegroundColor(getLineColor());
        graphics.drawRectangle(bounds.x, bounds.y + 10, INDENT * 2, 12);
        graphics.drawRectangle(bounds.x, bounds.y + 30, INDENT * 2, 12);
        
        graphics.popState();
    }
    
    @Override
    public Rectangle calculateTextControlBounds() {
        Rectangle bounds = getBounds();
        bounds.x += TEXT_INDENT;
        bounds.y += 5;
        bounds.width -= 35;
        bounds.height -= 10;
        return bounds;
    }
}