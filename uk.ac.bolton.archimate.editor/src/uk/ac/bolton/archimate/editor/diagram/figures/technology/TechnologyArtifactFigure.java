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

import uk.ac.bolton.archimate.editor.diagram.figures.AbstractTextFlowFigure;
import uk.ac.bolton.archimate.editor.ui.ColorFactory;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;



/**
 * Technology Artifact Figure
 * 
 * @author Phillip Beauvoir
 */
public class TechnologyArtifactFigure extends AbstractTextFlowFigure {

    protected int FOLD_HEIGHT = 18;
    protected int SHADOW_OFFSET = 2;

    public TechnologyArtifactFigure(IDiagramModelArchimateObject diagramModelObject) {
        super(diagramModelObject);
    }

    @Override
    public void drawFigure(Graphics graphics) {
        graphics.pushState();
        
        Rectangle bounds = getBounds();
        
        if(isEnabled()) {
            // Shadow
            graphics.setAlpha(100);
            graphics.setBackgroundColor(ColorConstants.black);
            graphics.fillRectangle(bounds.x + SHADOW_OFFSET, bounds.y + SHADOW_OFFSET + FOLD_HEIGHT, bounds.width - SHADOW_OFFSET, bounds.height - SHADOW_OFFSET - FOLD_HEIGHT);
            graphics.setAlpha(255);
        }
        else {
            setDisabledState(graphics);
        }

        // Fill
        PointList points1 = new PointList();
        points1.addPoint(bounds.x, bounds.y);
        points1.addPoint(bounds.x + bounds.width - SHADOW_OFFSET - FOLD_HEIGHT, bounds.y);
        points1.addPoint(bounds.x + bounds.width - SHADOW_OFFSET - FOLD_HEIGHT, bounds.y + FOLD_HEIGHT);
        points1.addPoint(bounds.x + bounds.width - SHADOW_OFFSET - 1, bounds.y + FOLD_HEIGHT);
        points1.addPoint(bounds.x + bounds.width - SHADOW_OFFSET - 1, bounds.y + bounds.height - SHADOW_OFFSET - 1);
        points1.addPoint(bounds.x, bounds.y + bounds.height - SHADOW_OFFSET - 1);
        graphics.setBackgroundColor(getFillColor());
        graphics.fillPolygon(points1);

        // Fold
        PointList points2 = new PointList();
        points2.addPoint(bounds.x + bounds.width - SHADOW_OFFSET - FOLD_HEIGHT, bounds.y);
        points2.addPoint(bounds.x + bounds.width - SHADOW_OFFSET - 1, bounds.y + FOLD_HEIGHT);
        points2.addPoint(bounds.x + bounds.width - SHADOW_OFFSET - FOLD_HEIGHT, bounds.y + FOLD_HEIGHT);
        graphics.setBackgroundColor(ColorFactory.getDarkerColor(getFillColor()));
        graphics.fillPolygon(points2);
        
        // Line
        graphics.setBackgroundColor(ColorConstants.black);
        graphics.drawPolygon(points1);
        graphics.drawLine(points1.getPoint(1), points1.getPoint(3));
        
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
