/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.extensions;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Path;

import com.archimatetool.editor.diagram.figures.AbstractArchimateFigure;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.model.IDiagramModelArchimateObject;




/**
 * Deliverable Figure
 * 
 * @author Phillip Beauvoir
 */
public class DeliverableFigure
extends AbstractArchimateFigure {
    
    protected static final int SHADOW_OFFSET = 3;

    public DeliverableFigure(IDiagramModelArchimateObject diagramModelObject) {
        super(diagramModelObject);
    }
    
    @Override
    protected void drawFigure(Graphics graphics) {
        graphics.pushState();
        
        Rectangle bounds = getBounds().getCopy();
        
        boolean drawShadows = Preferences.STORE.getBoolean(IPreferenceConstants.SHOW_SHADOWS);
        
        int offset = 11;
        int curve_y = bounds.y + bounds.height - offset;
        
        if(isEnabled()) {
            // Shadow fill
            if(drawShadows) {
                graphics.setAlpha(100);
                graphics.setBackgroundColor(ColorConstants.black);
                Path path = new Path(null);
                path.moveTo(bounds.x + SHADOW_OFFSET, bounds.y + SHADOW_OFFSET);
                path.lineTo(bounds.x + SHADOW_OFFSET, curve_y);

                path.quadTo(bounds.x + SHADOW_OFFSET + (bounds.width / 4), bounds.y + bounds.height + offset - 2,
                        bounds.x + bounds.width / 2 + SHADOW_OFFSET, curve_y);

                path.quadTo(bounds.x + bounds.width - (bounds.width / 4), curve_y - offset - 2,
                        bounds.x + bounds.width, curve_y);

                path.lineTo(bounds.x + bounds.width, bounds.y + SHADOW_OFFSET);
                graphics.fillPath(path);
                path.dispose();
                
                graphics.setAlpha(255);
            }            
        }
        else {
            setDisabledState(graphics);
        }
        
        int shadow_offset = drawShadows ? SHADOW_OFFSET : 1;
        
        // Main Fill
        Path path = new Path(null);
        path.moveTo(bounds.x, bounds.y);
        path.lineTo(bounds.x, curve_y - shadow_offset);
        
        path.quadTo(bounds.x + (bounds.width / 4), bounds.y + bounds.height + offset - shadow_offset,
                bounds.x + bounds.width / 2 + shadow_offset, curve_y - shadow_offset);
        
        path.quadTo(bounds.x + bounds.width - (bounds.width / 4), curve_y - offset - 2,
                bounds.x + bounds.width - shadow_offset, curve_y - shadow_offset);
        
        path.lineTo(bounds.x + bounds.width - shadow_offset, bounds.y);
        
        graphics.setBackgroundColor(getFillColor());
        graphics.fillPath(path);
        
        // Outline
        graphics.setForegroundColor(getLineColor());
        path.lineTo(bounds.x, bounds.y);
        graphics.drawPath(path);
        path.dispose();
        
        // Image icon
        if(getImage() != null) {
            graphics.drawImage(getImage(), calculateImageLocation());
        }
        
        graphics.popState();
    }
    
    protected Image getImage() {
        return null;
    }
    
    protected Point calculateImageLocation() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 22, bounds.y + 5);
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
