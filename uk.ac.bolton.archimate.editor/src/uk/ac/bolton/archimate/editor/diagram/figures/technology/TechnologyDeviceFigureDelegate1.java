/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.diagram.figures.technology;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

import uk.ac.bolton.archimate.editor.diagram.figures.AbstractFigureDelegate;
import uk.ac.bolton.archimate.editor.diagram.figures.IDiagramModelObjectFigure;
import uk.ac.bolton.archimate.editor.ui.ColorFactory;



/**
 * Technology Device Figure Delegate 1
 * 
 * @author Phillip Beauvoir
 */
public class TechnologyDeviceFigureDelegate1 extends AbstractFigureDelegate {

    protected int SHADOW_OFFSET = 2;
    protected int INDENT = 15;

    public TechnologyDeviceFigureDelegate1(IDiagramModelObjectFigure owner) {
        super(owner);
    }

    @Override
    public void drawFigure(Graphics graphics) {
        graphics.pushState();
        
        Rectangle bounds = getBounds();
        
        int height_indent = bounds.height / 6;
        
        if(isEnabled()) {
            // Shadow
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
        else {
            setDisabledState(graphics);
        }
        
        graphics.setBackgroundColor(getFillColor());
        graphics.fillRoundRectangle(new Rectangle(bounds.x, bounds.y,
                bounds.width - SHADOW_OFFSET, bounds.height - height_indent - SHADOW_OFFSET), 30, 30);
    
        PointList points = new PointList();
        points.addPoint(bounds.x, bounds.y + bounds.height - SHADOW_OFFSET);
        points.addPoint(bounds.x + INDENT - SHADOW_OFFSET, bounds.y + bounds.height - height_indent - 3);
        points.addPoint(bounds.x + bounds.width - INDENT - SHADOW_OFFSET, bounds.y + bounds.height - height_indent - 3);
        points.addPoint(bounds.x + bounds.width - 4, bounds.y + bounds.height - SHADOW_OFFSET);
                
        graphics.setBackgroundColor(ColorFactory.getDarkerColor(getFillColor()));
        graphics.fillPolygon(points);

        graphics.setBackgroundColor(ColorConstants.black);
        graphics.drawRoundRectangle(new Rectangle(bounds.x, bounds.y,
                bounds.width - 3, bounds.height - height_indent - 3), 30, 30);
        graphics.drawLine(points.getPoint(0), points.getPoint(1));
        graphics.drawLine(points.getPoint(2), points.getPoint(3));
        graphics.drawLine(points.getPoint(0), points.getPoint(3));
        
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
