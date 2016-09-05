/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.FigureUtils;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;


/**
 * Figure for a Motiviation Element
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractMotivationFigure extends AbstractTextControlContainerFigure {
    
    protected static final int FLANGE = 10;
    protected static final int TEXT_INDENT = 20;
    
    protected AbstractMotivationFigure() {
        super(TEXT_FLOW_CONTROL);
    }
    
    @Override
    protected void drawFigure(Graphics graphics) {
        if(getFigureDelegate() != null) {
            getFigureDelegate().drawFigure(graphics);
            return;
        }

        graphics.pushState();
        
        Rectangle bounds = getBounds();
        
        PointList points = new PointList();
 
        int x = bounds.x;
        int y = bounds.y;
        int width = bounds.width - 1;
        int height = bounds.height - 1;
        
        points.addPoint(x + FLANGE, y);
        points.addPoint(x + width - FLANGE, y);
        points.addPoint(x + width, y + FLANGE);
        points.addPoint(x + width, y + height - FLANGE);
        points.addPoint(x + width - FLANGE, y + height);
        points.addPoint(x + FLANGE, y + height);
        points.addPoint(x, y + height - FLANGE);
        points.addPoint(x, y + FLANGE);
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        // Fill
        graphics.setBackgroundColor(getFillColor());
        
        Pattern gradient = null;
        if(Preferences.STORE.getBoolean(IPreferenceConstants.SHOW_GRADIENT)) {
            gradient = FigureUtils.createGradient(graphics, bounds, getFillColor());
            graphics.setBackgroundPattern(gradient);
        }
        
        graphics.fillPolygon(points);
        
        if(gradient != null) {
            gradient.dispose();
        }

        // Line
        graphics.setForegroundColor(getLineColor());
        graphics.drawPolygon(points);
        
        graphics.popState();
    }
    
    @Override
    public Rectangle calculateTextControlBounds() {
        Rectangle bounds = getBounds().getCopy();
        bounds.x += TEXT_INDENT;
        bounds.y += 5;
        bounds.width = bounds.width - (TEXT_INDENT * 2);
        bounds.height -= 10;
        return bounds;
    }
}