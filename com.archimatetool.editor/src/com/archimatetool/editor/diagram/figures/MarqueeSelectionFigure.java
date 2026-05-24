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
import org.eclipse.swt.widgets.Display;

import com.archimatetool.editor.utils.PlatformUtils;

/**
 * Customised MarqueeRectangleFigure with option for marching ants
 * 
 * @see org.eclipse.gef.tools.MarqueeSelectionTool
 * @author Phillip Beauvoir
 */
public class MarqueeSelectionFigure extends Figure {

    private static final int DELAY = 110; // animation delay in millisecond
    private static boolean isWindows = PlatformUtils.isWindows();
    
    private int offset = 0;
    private boolean schedulePaint = true;
    private int lineWidth = 1;
    
    private boolean useAnts = true;
    
    @Override
    protected void paintFigure(Graphics graphics) {
        graphics.translate(getLocation());

        graphics.setLineDash(new int[]{3, 3});
        graphics.setLineWidth(lineWidth);
        if(useAnts) {
            graphics.setLineDashOffset(-offset);
        }
        
        Rectangle rect = new Rectangle(0, 0, getBounds().width(), getBounds().height());
        
        // Windows doesn't use XOR mode when graphics.setAdvanced(true) has been set
        if(isWindows) {
            graphics.setForegroundColor(ColorConstants.black);
            rect.shrink(lineWidth, lineWidth);
            graphics.drawRectangle(rect);

            graphics.setForegroundColor(ColorConstants.white);
            rect.shrink(lineWidth, lineWidth);
            graphics.drawRectangle(rect);
        }
        else {
            graphics.setXORMode(true);
            graphics.setForegroundColor(ColorConstants.white);
            rect.shrink(lineWidth, lineWidth);
            graphics.drawRectangle(rect);
        }

        graphics.translate(getLocation().getNegated());
        
        if(useAnts && schedulePaint) {
            Display.getCurrent().timerExec(DELAY, () -> {
                offset++;
                if(offset > 5) {
                    offset = 0;
                }
                
                schedulePaint = true;
                repaint();
            });
        }

        schedulePaint = false;
    }
}