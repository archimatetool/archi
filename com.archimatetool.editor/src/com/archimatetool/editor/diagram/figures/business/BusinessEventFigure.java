/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.business;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Path;

import com.archimatetool.editor.diagram.figures.AbstractArchimateFigure;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.model.IDiagramModelArchimateObject;




/**
 * Business Event Figure
 * 
 * @author Phillip Beauvoir
 */
public class BusinessEventFigure extends AbstractArchimateFigure {

    protected static final int SHADOW_OFFSET = 3;
    
    public BusinessEventFigure(IDiagramModelArchimateObject diagramModelObject) {
        super(diagramModelObject);
    }
    
    @Override
    public void drawFigure(Graphics graphics) {
        graphics.pushState();
        
        Rectangle bounds = getBounds().getCopy();
        
        int indent = Math.min(bounds.height / 3, bounds.width / 3);
        int centre_y = bounds.y + bounds.height / 2 - 1;
        int arc_startx = bounds.x + bounds.width - indent;
        
        boolean drawShadows = Preferences.STORE.getBoolean(IPreferenceConstants.SHOW_SHADOWS);
        
        if(isEnabled()) {
            // Shadow fill
            if(drawShadows) {
                graphics.setAlpha(100);
                graphics.setBackgroundColor(ColorConstants.black);
    
                Path path = new Path(null);
                path.moveTo(bounds.x + SHADOW_OFFSET, bounds.y + SHADOW_OFFSET);
                path.lineTo(bounds.x + indent + SHADOW_OFFSET, centre_y + SHADOW_OFFSET);
                path.lineTo(bounds.x + SHADOW_OFFSET, bounds.y + bounds.height);
                path.lineTo(arc_startx, bounds.y + bounds.height);
                path.addArc(arc_startx - indent - SHADOW_OFFSET, bounds.y + SHADOW_OFFSET,
                        indent * 2 + 2, bounds.height - SHADOW_OFFSET, -90, 180);
                graphics.fillPath(path);
                path.dispose();
                
                graphics.setAlpha(255);
            }
        }
        else {
            setDisabledState(graphics);
        }
        
        int x_offset = drawShadows ? SHADOW_OFFSET : 2;
        int y_offset = drawShadows ? SHADOW_OFFSET : 1;
        
        // Main Fill
        Path path = new Path(null);
        path.moveTo(bounds.x, bounds.y);
        path.lineTo(bounds.x + indent, centre_y);
        path.lineTo(bounds.x, bounds.y + bounds.height - y_offset);
        path.lineTo(arc_startx, bounds.y + bounds.height - y_offset);
        path.addArc(arc_startx - indent - x_offset, bounds.y,
                indent * 2 + 1, bounds.height - y_offset, -90, 180);
        
        graphics.setBackgroundColor(getFillColor());
        graphics.fillPath(path);
        
        // Outline
        graphics.setForegroundColor(getLineColor());
        path.lineTo(bounds.x, bounds.y);
        graphics.drawPath(path);
        path.dispose();
        
        graphics.popState();
    }

    @Override
    public Rectangle calculateTextControlBounds() {
        Rectangle bounds = getBounds().getCopy();
        bounds.x += 20;
        bounds.y += 5;
        bounds.width = bounds.width - 40;
        bounds.height -= 10;
        return bounds;
    }
}
