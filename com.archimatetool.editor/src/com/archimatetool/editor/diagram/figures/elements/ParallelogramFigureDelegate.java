/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

import com.archimatetool.editor.diagram.figures.AbstractFigureDelegate;
import com.archimatetool.editor.diagram.figures.IDiagramModelObjectFigure;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;


/**
 * Parallelogram Figure Delegate
 * 
 * @author Phillip Beauvoir
 */
public class ParallelogramFigureDelegate extends AbstractFigureDelegate {

    protected static final int SHADOW_OFFSET = 2;
    protected static final int FLANGE = 16;
    protected static final int TEXT_INDENT = 20;
    
    protected boolean fWithSlash;
    
    public ParallelogramFigureDelegate(IDiagramModelObjectFigure owner, boolean withSlash) {
        super(owner);
        fWithSlash = withSlash;
    }
    
    @Override
    public void drawFigure(Graphics graphics) {
        graphics.pushState();

        Rectangle bounds = getBounds();
        
        boolean drawShadows = Preferences.STORE.getBoolean(IPreferenceConstants.SHOW_SHADOWS);
        int shadow_offset = drawShadows ? SHADOW_OFFSET : 1;

        bounds.width -= shadow_offset;
        bounds.height -= shadow_offset;
        
        PointList points = new PointList();
        points.addPoint(bounds.x + FLANGE, bounds.y);
        points.addPoint(bounds.x + bounds.width, bounds.y);
        points.addPoint(bounds.x + bounds.width - FLANGE, bounds.y + bounds.height);
        points.addPoint(bounds.x, bounds.y + bounds.height);
        
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
        
        // Outline
        bounds.width--;
        bounds.height--;
        graphics.setForegroundColor(getLineColor());
        graphics.drawPolygon(points);
        
        // Slash
        if(fWithSlash) {
            graphics.drawLine(bounds.x + FLANGE + 20, bounds.y, bounds.x + 20, bounds.y + bounds.height);
        }
        
        graphics.popState();
    }
    
    @Override
    public Rectangle calculateTextControlBounds() {
        Rectangle bounds = getBounds();
        bounds.x += TEXT_INDENT;
        bounds.y += 5;
        bounds.width = bounds.width - (TEXT_INDENT * 2);
        bounds.height -= 10;
        return bounds;
    }

}
