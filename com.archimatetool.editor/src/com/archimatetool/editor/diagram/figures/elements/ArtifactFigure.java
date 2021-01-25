/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.ui.ColorFactory;




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
        
        bounds.width--;
        bounds.height--;
        
        // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
        int lineWidth = 1;
        setLineWidth(graphics, lineWidth, bounds);
        
        graphics.setAlpha(getAlpha());
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }

        // Fill
        graphics.setBackgroundColor(getFillColor());

        Pattern gradient = applyGradientPattern(graphics, bounds);
        
        float lineOffset = (float)lineWidth / 2;

        Path path1 = new Path(null);
        path1.moveTo(bounds.x - lineOffset, bounds.y);
        path1.lineTo(bounds.x + bounds.width - FOLD_HEIGHT, bounds.y);
        path1.lineTo(bounds.x + bounds.width, bounds.y + FOLD_HEIGHT);
        path1.lineTo(bounds.x + bounds.width, bounds.y + bounds.height);
        path1.lineTo(bounds.x, bounds.y + bounds.height);
        path1.lineTo(bounds.x, bounds.y);
        graphics.fillPath(path1);
        
        disposeGradientPattern(graphics, gradient);
        
        // Icon
        drawIconImage(graphics, bounds);

        // Fold
        graphics.setBackgroundColor(ColorFactory.getDarkerColor(getFillColor()));
        
        Path path2 = new Path(null);
        path2.moveTo(bounds.x + bounds.width - FOLD_HEIGHT, bounds.y);
        path2.lineTo(bounds.x + bounds.width, bounds.y + FOLD_HEIGHT);
        path2.lineTo(bounds.x + bounds.width - FOLD_HEIGHT, bounds.y + FOLD_HEIGHT);
        path2.lineTo(bounds.x + bounds.width - FOLD_HEIGHT, bounds.y);
        graphics.fillPath(path2);
        path2.dispose();
        
        // Lines
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        
        graphics.drawPath(path1);
        path1.dispose();
        
        Path path3 = new Path(null);
        path3.moveTo(bounds.x + bounds.width, bounds.y + FOLD_HEIGHT);
        path3.lineTo(bounds.x + bounds.width - FOLD_HEIGHT, bounds.y + FOLD_HEIGHT);
        path3.lineTo(bounds.x + bounds.width - FOLD_HEIGHT, bounds.y);
        graphics.drawPath(path3);
        path3.dispose();
        
        graphics.popState();
    }
}
