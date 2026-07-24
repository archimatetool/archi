/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.sketch.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Path;

import com.archimatetool.editor.ui.IIconDelegate;


/**
 * Draw2d Icon delegate for a Sketch model
 * 
 * @author Phillip Beauvoir
 */
public class SketchModelIconDelegate implements IIconDelegate {
    
    private static Color color1 = new Color(255, 247, 173);
    private static Color color2 = new Color(255, 214, 123);

    @Override
    public void drawIcon(Graphics graphics, Color foregroundColor, Color backgroundColor, Point origin) {
        graphics.pushState();
        
        // Ensure this is set
        graphics.setAntialias(SWT.ON);
        
        graphics.setLineWidth(1);
        graphics.setForegroundColor(ColorConstants.darkGray);
        
        // rectangles
        graphics.setBackgroundColor(color1);
        graphics.fillRectangle(origin.x, origin.y, 8, 5);
        graphics.drawRectangle(origin.x, origin.y, 8, 5);
        
        graphics.setBackgroundColor(color2);
        graphics.fillRectangle(origin.x + 4, origin.y + 8, 8, 5);
        graphics.drawRectangle(origin.x + 4, origin.y + 8, 8, 5);

        // line
        Path path = new Path(null);
        path.moveTo(origin.x + 2, origin.y + 5);
        path.lineTo(origin.x + 2, origin.y + 11);
        path.lineTo(origin.x + 4, origin.y + 11);
        graphics.drawPath(path);
        path.dispose();
        
        graphics.popState();
    }

    @Override
    public Rectangle getBounds() {
        // Mirrors the shapes drawn in drawIcon() above (with origin = (0, 0))
        Rectangle bounds = new Rectangle(0, 0, 8, 5); // top rectangle
        bounds = bounds.union(new Rectangle(4, 8, 8, 5)); // bottom rectangle
        bounds = bounds.union(new Rectangle(2, 5, 2, 6)); // line: moveTo(2, 5) -> lineTo(2, 11) -> lineTo(4, 11)
        return bounds;
    }

}
