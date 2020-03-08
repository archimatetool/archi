/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.FigureUtils;
import com.archimatetool.editor.diagram.figures.FigureUtils.Direction;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.model.IDiagramModelObject;




/**
 * Artifact Figure
 * 
 * @author Phillip Beauvoir
 */
public class ArtifactFigure extends AbstractTextControlContainerFigure {

    protected static final int FOLD_HEIGHT = 18;

    public ArtifactFigure() {
        super(TEXT_FLOW_CONTROL);
    }

    @Override
    public void drawFigure(Graphics graphics) {
        graphics.pushState();
        
        Rectangle bounds = getBounds().getCopy();
        
        graphics.setAlpha(getAlpha());
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }

        // Fill
        PointList points1 = new PointList();
        points1.addPoint(bounds.x, bounds.y);
        points1.addPoint(bounds.x + bounds.width - FOLD_HEIGHT, bounds.y);
        points1.addPoint(bounds.x + bounds.width - FOLD_HEIGHT, bounds.y + FOLD_HEIGHT);
        points1.addPoint(bounds.x + bounds.width - 1, bounds.y + FOLD_HEIGHT);
        points1.addPoint(bounds.x + bounds.width - 1, bounds.y + bounds.height - 1);
        points1.addPoint(bounds.x, bounds.y + bounds.height - 1);

        graphics.setBackgroundColor(getFillColor());

        Pattern gradient = null;
        if(getGradient() != IDiagramModelObject.GRADIENT_NONE) {
            gradient = FigureUtils.createGradient(graphics, bounds, getFillColor(), getAlpha(), Direction.get(getGradient()));
            graphics.setBackgroundPattern(gradient);
        }
        
        graphics.fillPolygon(points1);
        
        if(gradient != null) {
            gradient.dispose();
        }
        
        // Fold
        PointList points2 = new PointList();
        points2.addPoint(bounds.x + bounds.width - FOLD_HEIGHT, bounds.y);
        points2.addPoint(bounds.x + bounds.width - 1, bounds.y + FOLD_HEIGHT);
        points2.addPoint(bounds.x + bounds.width - FOLD_HEIGHT, bounds.y + FOLD_HEIGHT);
        graphics.setBackgroundColor(ColorFactory.getDarkerColor(getFillColor()));
        graphics.fillPolygon(points2);
        
        // Line
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        graphics.drawPolygon(points1);
        graphics.drawLine(points1.getPoint(1), points1.getPoint(3));
        
        graphics.popState();
    }
}
