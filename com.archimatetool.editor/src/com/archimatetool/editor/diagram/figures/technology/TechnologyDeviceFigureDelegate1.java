/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.technology;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

import com.archimatetool.editor.diagram.figures.AbstractFigureDelegate;
import com.archimatetool.editor.diagram.figures.IDiagramModelObjectFigure;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.ui.ColorFactory;




/**
 * Technology Device Figure Delegate 1
 * 
 * @author Phillip Beauvoir
 */
public class TechnologyDeviceFigureDelegate1 extends AbstractFigureDelegate {

    protected static final int SHADOW_OFFSET = 2;
    protected static final int INDENT = 15;

    public TechnologyDeviceFigureDelegate1(IDiagramModelObjectFigure owner) {
        super(owner);
    }

    @Override
    public void drawFigure(Graphics graphics) {
        graphics.pushState();
        
        Rectangle bounds = getBounds();
        
        int height_indent = bounds.height / 6;
        
        boolean drawShadows = Preferences.STORE.getBoolean(IPreferenceConstants.SHOW_SHADOWS);
        
        if(isEnabled()) {
            // Shadow
            if(drawShadows) {
                graphics.setAlpha(100);
                graphics.setBackgroundColor(ColorConstants.black);
                
                graphics.fillRoundRectangle(new Rectangle(bounds.x + SHADOW_OFFSET, bounds.y + SHADOW_OFFSET,
                        bounds.width - SHADOW_OFFSET, bounds.height - height_indent - SHADOW_OFFSET), 30, 30);
                
                int[] points = new int[] {
                        bounds.x + SHADOW_OFFSET, bounds.y + bounds.height,
                        bounds.x + INDENT, bounds.y + bounds.height - height_indent - 3,
                        bounds.x + bounds.width - INDENT, bounds.y + bounds.height - height_indent - 3,
                        bounds.x + bounds.width, bounds.y + bounds.height
                };
                graphics.fillPolygon(points);
                
                graphics.setAlpha(255);
            }
        }
        else {
            setDisabledState(graphics);
        }
        
        int shadow_offset = drawShadows ? SHADOW_OFFSET : 0;
        
        // Bottom part
        PointList points1 = new PointList();
        points1.addPoint(bounds.x, bounds.y + bounds.height - 1 - shadow_offset);
        points1.addPoint(bounds.x + INDENT , bounds.y + bounds.height - height_indent - 1);
        points1.addPoint(bounds.x + bounds.width - INDENT, bounds.y + bounds.height - height_indent - 1);
        points1.addPoint(bounds.x + bounds.width, bounds.y + bounds.height - 1 - shadow_offset);
                
        graphics.setBackgroundColor(ColorFactory.getDarkerColor(getFillColor()));
        graphics.fillPolygon(points1);
        
        graphics.setForegroundColor(getLineColor());
        graphics.drawLine(bounds.x, bounds.y + bounds.height - 1 - shadow_offset,
                bounds.x + bounds.width, bounds.y + bounds.height - 1 - shadow_offset);
        graphics.drawLine(bounds.x, bounds.y + bounds.height - 1 - shadow_offset,
                bounds.x + INDENT, bounds.y + bounds.height - height_indent - 1);
        graphics.drawLine(bounds.x + bounds.width, bounds.y + bounds.height - 1 - shadow_offset,
                bounds.x + bounds.width - INDENT, bounds.y + bounds.height - height_indent - 1);

        // Top part
        Rectangle rect = new Rectangle(bounds.x, bounds.y, bounds.width - shadow_offset, bounds.height - height_indent);

        graphics.setBackgroundColor(getFillColor());
        graphics.fillRoundRectangle(rect, 30, 30);
        
        graphics.setForegroundColor(getLineColor());
        rect = new Rectangle(bounds.x, bounds.y, bounds.width - 1 - shadow_offset, bounds.height - height_indent - 1);
        graphics.drawRoundRectangle(rect, 30, 30);
        
        graphics.popState();
    }
    
    @Override
    public Rectangle calculateTextControlBounds() {
        Rectangle bounds = getBounds();
        bounds.x += 20;
        bounds.y += 5;
        bounds.width = bounds.width - 40;
        bounds.height -= 10;
        return bounds;
    }
}
