/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.business;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

import com.archimatetool.editor.diagram.figures.AbstractFigureDelegate;
import com.archimatetool.editor.diagram.figures.IDiagramModelObjectFigure;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;




/**
 * Business Process Figure Delegate
 * 
 * @author Phillip Beauvoir
 */
public class BusinessProcessFigureDelegate extends AbstractFigureDelegate {

    public BusinessProcessFigureDelegate(IDiagramModelObjectFigure owner) {
        super(owner);
    }
    
    @Override
    public void drawFigure(Graphics graphics) {
        graphics.pushState();
        
        boolean drawShadows = Preferences.STORE.getBoolean(IPreferenceConstants.SHOW_SHADOWS);
        
        if(isEnabled()) {
            // Shadow
            if(drawShadows) {
                graphics.setAlpha(100);
                graphics.setBackgroundColor(ColorConstants.black);
                graphics.fillPolygon(getPointList(true));
                graphics.setAlpha(255);
            }
        }
        else {
            setDisabledState(graphics);
        }
        
        graphics.setBackgroundColor(getFillColor());
        PointList points = getPointList(false);
        graphics.fillPolygon(points);
        
        // Line
        graphics.setForegroundColor(getLineColor());
        for(int i = 0; i < points.size() - 1; i++) {
            graphics.drawLine(points.getPoint(i), points.getPoint(i + 1));
        }
        
        graphics.popState();
    }
    
    private PointList getPointList(boolean shadow) {
        int shadow_offset = 2;
        
        Rectangle bounds = getBounds();
        bounds.width -= shadow_offset;
        bounds.height -= shadow_offset;

        PointList points = new PointList();
        
        if(shadow) {
            bounds.y += shadow_offset;
            bounds.x += shadow_offset;
        }
        
        points.addPoint(bounds.x, bounds.y + (bounds.height / 5));
        points.addPoint(bounds.x + (int)(bounds.width * 0.7), points.getPoint(0).y);
        points.addPoint(points.getPoint(1).x, bounds.y);
        points.addPoint(bounds.x + bounds.width, bounds.y + (bounds.height / 2));
        points.addPoint(points.getPoint(2).x, bounds.y + bounds.height);
        points.addPoint(points.getPoint(2).x, bounds.y + bounds.height - (bounds.height / 5));
        points.addPoint(points.getPoint(0).x, points.getPoint(5).y);
        points.addPoint(points.getPoint(0).x, points.getPoint(0).y);
        
        return points;
    }
    
    @Override
    public Rectangle calculateTextControlBounds() {
        Rectangle bounds = getBounds();
        bounds.x += 10;
        bounds.y += bounds.height / 4;
        bounds.width = bounds.width - 40;
        bounds.height -= 20;
        return bounds;
    }

}
