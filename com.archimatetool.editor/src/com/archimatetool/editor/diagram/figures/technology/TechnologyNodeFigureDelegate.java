/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.technology;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.diagram.figures.AbstractFigureDelegate;
import com.archimatetool.editor.diagram.figures.IDiagramModelObjectFigure;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.ui.ColorFactory;




/**
 * Technology Node Figure Delegate
 * 
 * @author Phillip Beauvoir
 */
public class TechnologyNodeFigureDelegate extends AbstractFigureDelegate {

    protected static final int FOLD_HEIGHT = 14;
    protected static final int SHADOW_OFFSET = 2;
    
    private Image fImage;

    public TechnologyNodeFigureDelegate(IDiagramModelObjectFigure owner) {
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
                
                int[] points = new int[] {
                        bounds.x + SHADOW_OFFSET, bounds.y + FOLD_HEIGHT,
                        bounds.x + FOLD_HEIGHT, bounds.y + SHADOW_OFFSET,
                        bounds.x + bounds.width, bounds.y + SHADOW_OFFSET,
                        bounds.x + bounds.width, bounds.y + bounds.height - FOLD_HEIGHT,
                        bounds.x + bounds.width - FOLD_HEIGHT + SHADOW_OFFSET - 1, bounds.y + bounds.height,
                        bounds.x + SHADOW_OFFSET, bounds.y + bounds.height
                };
                graphics.fillPolygon(points);
                
                graphics.setAlpha(255);
            }
        }
        else {
            setDisabledState(graphics);
        }
        
        int shadow_offset = drawShadows ? SHADOW_OFFSET : 0;
        
        // Outer shape
        PointList shape = new PointList();
        shape.addPoint(bounds.x, bounds.y + bounds.height - shadow_offset - 1);
        shape.addPoint(bounds.x, bounds.y + FOLD_HEIGHT);
        shape.addPoint(bounds.x + FOLD_HEIGHT, bounds.y);
        shape.addPoint(bounds.x + bounds.width - 1, bounds.y);
        shape.addPoint(bounds.x + bounds.width - 1, bounds.y + bounds.height - FOLD_HEIGHT - shadow_offset - 1);
        shape.addPoint(bounds.x + bounds.width - FOLD_HEIGHT - 1, bounds.y + bounds.height - shadow_offset - 1);
        
        graphics.setBackgroundColor(ColorFactory.getDarkerColor(getFillColor()));
        graphics.fillPolygon(shape);

        // Fill front rectangle
        graphics.setBackgroundColor(getFillColor());
        graphics.fillRectangle(bounds.x, bounds.y + FOLD_HEIGHT, bounds.width - FOLD_HEIGHT, bounds.height - FOLD_HEIGHT - shadow_offset);

        // Outline
        graphics.setForegroundColor(getLineColor());
        graphics.drawPolygon(shape);
        graphics.drawLine(bounds.x, bounds.y + FOLD_HEIGHT, bounds.x + bounds.width - FOLD_HEIGHT - 1, bounds.y + FOLD_HEIGHT);
        graphics.drawLine(bounds.x + bounds.width - FOLD_HEIGHT - 1, bounds.y + FOLD_HEIGHT, bounds.x + bounds.width - 1, bounds.y);
        graphics.drawLine(bounds.x + bounds.width - FOLD_HEIGHT - 1, bounds.y + FOLD_HEIGHT, bounds.x + - FOLD_HEIGHT + bounds.width - 1, bounds.y + bounds.height - shadow_offset - 1);
        
        // Image icon
        if(getImage() != null) {
            graphics.drawImage(getImage(), calculateImageLocation());
        }
        
        graphics.popState();
    }
    
    @Override
    public Rectangle calculateTextControlBounds() {
        Rectangle bounds = getBounds();
        bounds.x += 20;
        bounds.y += 2 + FOLD_HEIGHT;
        bounds.width = bounds.width - 40;
        bounds.height -= 20;
        return bounds;
    }
    
    public void setImage(Image image) {
        fImage = image;
    }
    
    public Image getImage() {
        return fImage;
    }

    protected Point calculateImageLocation() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 20 - 1, bounds.y + 5);
    }

}
