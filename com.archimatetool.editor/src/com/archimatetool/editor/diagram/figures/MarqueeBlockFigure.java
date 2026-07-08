/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

/**
 * Custom Marquee Figure using transparent colored block
 * 
 * @author Phillip Beauvoir
 */
public class MarqueeBlockFigure extends Figure {

    private static Color blockColor = new Color(0, 123, 255);
    private int lineWidth = 1;
    
    @Override
    protected void paintFigure(Graphics graphics) {
        graphics.translate(getLocation());

        graphics.setLineWidth(1);
        
        Rectangle rect = new Rectangle(0, 0, getBounds().width(), getBounds().height());
        graphics.setAlpha(50);
        graphics.setBackgroundColor(blockColor);
        graphics.fillRectangle(rect);

        graphics.setAlpha(255);
        graphics.setForegroundColor(ColorConstants.blue);
        rect.shrink(lineWidth, lineWidth);
        graphics.drawRectangle(rect);

        graphics.setAlpha(125);
        graphics.setForegroundColor(ColorConstants.white);
        //rect.shrink(lineWidth, lineWidth);
        graphics.drawRectangle(rect);

        graphics.translate(getLocation().getNegated());
    }

}