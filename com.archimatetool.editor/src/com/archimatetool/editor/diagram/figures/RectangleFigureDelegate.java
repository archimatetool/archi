/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;



/**
 * Rectangle Figure Delegate
 * 
 * @author Phillip Beauvoir
 */
public class RectangleFigureDelegate extends AbstractFigureDelegate {
    
    protected static final int SHADOW_OFFSET = 2;
    protected static final int TEXT_INDENT = 20;
    
    private Image fImage;
    
    public RectangleFigureDelegate(IDiagramModelObjectFigure owner) {
        super(owner);
    }
    
    @Override
    public void drawFigure(Graphics graphics) {
        graphics.pushState();

        Rectangle bounds = getBounds();
        
        boolean drawShadows = Preferences.STORE.getBoolean(IPreferenceConstants.SHOW_SHADOWS);
        
        if(isEnabled()) {
            // Shadow
            if(drawShadows) {
                graphics.setAlpha(100);
                graphics.setBackgroundColor(ColorConstants.black);
                graphics.fillRectangle(new Rectangle(bounds.x + SHADOW_OFFSET, bounds.y + SHADOW_OFFSET, bounds.width - SHADOW_OFFSET, bounds.height - SHADOW_OFFSET));
                graphics.setAlpha(255);
            }
        }
        else {
            setDisabledState(graphics);
        }

        int shadow_offset = drawShadows ? SHADOW_OFFSET : 0;

        bounds.width -= shadow_offset;
        bounds.height -= shadow_offset;
        
        // Fill
        graphics.setBackgroundColor(getFillColor());
        graphics.fillRectangle(bounds);
        
        // Outline
        bounds.width--;
        bounds.height--;
        graphics.setForegroundColor(getLineColor());
        graphics.drawRectangle(bounds);
        
        // Image icon
        if(getImage() != null) {
            graphics.drawImage(getImage(), calculateImageLocation());
        }
        
        graphics.popState();
    }
    
    public void setImage(Image image) {
        fImage = image;
    }
    
    public Image getImage() {
        return fImage;
    }

    protected Point calculateImageLocation() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - TEXT_INDENT - 1, bounds.y + 5);
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
