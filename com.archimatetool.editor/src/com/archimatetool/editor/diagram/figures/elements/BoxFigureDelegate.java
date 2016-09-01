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

import com.archimatetool.editor.diagram.figures.AbstractFigureDelegate;
import com.archimatetool.editor.diagram.figures.FigureUtils;
import com.archimatetool.editor.diagram.figures.IDiagramModelObjectFigure;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.ui.ColorFactory;


/**
 * Box Figure Delegate
 * 
 * @author Phillip Beauvoir
 */
public class BoxFigureDelegate extends AbstractFigureDelegate {

    protected static final int FOLD_HEIGHT = 14;
    
    public BoxFigureDelegate(IDiagramModelObjectFigure owner) {
        super(owner);
    }
    
    @Override
    public void drawFigure(Graphics graphics) {
        graphics.pushState();
        
        Rectangle bounds = getBounds();
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        // Outer shape
        PointList shape = new PointList();
        shape.addPoint(bounds.x, bounds.y + bounds.height - 1);
        shape.addPoint(bounds.x, bounds.y + FOLD_HEIGHT);
        shape.addPoint(bounds.x + FOLD_HEIGHT, bounds.y);
        shape.addPoint(bounds.x + bounds.width - 1, bounds.y);
        shape.addPoint(bounds.x + bounds.width - 1, bounds.y + bounds.height - FOLD_HEIGHT - 1);
        shape.addPoint(bounds.x + bounds.width - FOLD_HEIGHT - 1, bounds.y + bounds.height - 1);
        
        graphics.setBackgroundColor(ColorFactory.getDarkerColor(getFillColor()));
        graphics.fillPolygon(shape);

        // Fill front rectangle
        graphics.setBackgroundColor(getFillColor());
        
        Pattern gradient = null;
        if(Preferences.STORE.getBoolean(IPreferenceConstants.SHOW_GRADIENT)) {
            gradient = FigureUtils.createGradient(graphics, bounds, getFillColor());
            graphics.setBackgroundPattern(gradient);
        }

        graphics.fillRectangle(bounds.x, bounds.y + FOLD_HEIGHT, bounds.width - FOLD_HEIGHT, bounds.height - FOLD_HEIGHT);

        if(gradient != null) {
            gradient.dispose();
        }

        // Outline
        graphics.setForegroundColor(getLineColor());
        graphics.drawPolygon(shape);
        graphics.drawLine(bounds.x, bounds.y + FOLD_HEIGHT, bounds.x + bounds.width - FOLD_HEIGHT - 1, bounds.y + FOLD_HEIGHT);
        graphics.drawLine(bounds.x + bounds.width - FOLD_HEIGHT - 1, bounds.y + FOLD_HEIGHT, bounds.x + bounds.width - 1, bounds.y);
        graphics.drawLine(bounds.x + bounds.width - FOLD_HEIGHT - 1, bounds.y + FOLD_HEIGHT, bounds.x + - FOLD_HEIGHT + bounds.width - 1, bounds.y + bounds.height - 1);
        
        graphics.popState();
    }
    
    @Override
    public Rectangle calculateTextControlBounds() {
        Rectangle bounds = getBounds();
        bounds.x += 12;
        bounds.y += 2 + FOLD_HEIGHT;
        bounds.width = bounds.width - 42;
        bounds.height -= 20;
        return bounds;
    }

}
