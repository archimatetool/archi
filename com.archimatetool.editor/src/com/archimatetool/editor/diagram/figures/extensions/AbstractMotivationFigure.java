/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.extensions;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.diagram.figures.AbstractArchimateFigure;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.model.IDiagramModelArchimateObject;


/**
 * Figure for a Motiviation Element
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractMotivationFigure
extends AbstractArchimateFigure {
    
    protected static final int SHADOW_OFFSET = 3;
    protected static final int FLANGE = 10;
    protected static final int TEXT_INDENT = 20;
    
    public AbstractMotivationFigure(IDiagramModelArchimateObject diagramModelObject) {
        super(diagramModelObject);
    }
    
    @Override
    protected void drawFigure(Graphics graphics) {
        graphics.pushState();
        
        Rectangle bounds = getBounds();
        
        boolean drawShadows = Preferences.STORE.getBoolean(IPreferenceConstants.SHOW_SHADOWS);
        int shadow_offset = drawShadows ? SHADOW_OFFSET : 1;

        PointList points = new PointList();
 
        int x = bounds.x;
        int y = bounds.y;
        int width = bounds.width - shadow_offset;
        int height = bounds.height - shadow_offset;
        
        points.addPoint(x + FLANGE, y);
        points.addPoint(x + width - FLANGE, y);
        points.addPoint(x + width, y + FLANGE);
        points.addPoint(x + width, y + height - FLANGE);
        points.addPoint(x + width - FLANGE, y + height);
        points.addPoint(x + FLANGE, y + height);
        points.addPoint(x, y + height - FLANGE);
        points.addPoint(x, y + FLANGE);
        
        if(isEnabled()) {
            // Shadow
            if(drawShadows) {
                graphics.setAlpha(100);
                graphics.setBackgroundColor(ColorConstants.black);
                points.translate(SHADOW_OFFSET, SHADOW_OFFSET);
                graphics.fillPolygon(points);
                points.translate(-SHADOW_OFFSET, -SHADOW_OFFSET);
                graphics.setAlpha(255);
            }
        }
        else {
            setDisabledState(graphics);
        }
        
        // Fill
        graphics.setBackgroundColor(getFillColor());
        graphics.fillPolygon(points);
        
        // Line
        graphics.setForegroundColor(getLineColor());
        graphics.drawPolygon(points);
        
        // Image icon
        if(getImage() != null) {
            graphics.drawImage(getImage(), calculateImageLocation());
        }
        
        graphics.popState();
    }
    
    @Override
    public Rectangle calculateTextControlBounds() {
        boolean drawShadows = Preferences.STORE.getBoolean(IPreferenceConstants.SHOW_SHADOWS);
        int shadow_offset = drawShadows ? SHADOW_OFFSET : 0;
        
        Rectangle bounds = getBounds().getCopy();
        bounds.x += TEXT_INDENT - shadow_offset;
        bounds.y += 5;
        bounds.width = bounds.width - (TEXT_INDENT * 2);
        bounds.height -= 10;
        return bounds;
    }

    protected Image getImage() {
        return null;
    }
    
    protected Point calculateImageLocation() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - TEXT_INDENT - 2, bounds.y + 5);
    }

}