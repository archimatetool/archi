/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.figures.technology;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;

import uk.ac.bolton.archimate.editor.diagram.figures.AbstractFigureDelegate;
import uk.ac.bolton.archimate.editor.diagram.figures.IDiagramModelObjectFigure;
import uk.ac.bolton.archimate.editor.ui.ColorFactory;



/**
 * Technology Node Figure Delegate
 * 
 * @author Phillip Beauvoir
 */
public class TechnologyNodeFigureDelegate extends AbstractFigureDelegate {

    protected int FOLD_HEIGHT = 14;
    protected int SHADOW_OFFSET = 2;
    
    private Image fImage;

    public TechnologyNodeFigureDelegate(IDiagramModelObjectFigure owner) {
        super(owner);
    }
    
    @Override
    public void drawFigure(Graphics graphics) {
        graphics.pushState();
        
        Rectangle bounds = getBounds();
        
        if(isEnabled()) {
            // Shadow
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
        else {
            setDisabledState(graphics);
        }
        
        // Fill front rectangle
        graphics.setBackgroundColor(getFillColor());
        graphics.fillRectangle(bounds.x, bounds.y + FOLD_HEIGHT, bounds.width - FOLD_HEIGHT, bounds.height - FOLD_HEIGHT - SHADOW_OFFSET);

        graphics.setBackgroundColor(ColorFactory.getDarkerColor(getFillColor()));

        // Angle 1
        PointList points1 = new PointList();
        points1.addPoint(bounds.x, bounds.y + FOLD_HEIGHT);
        points1.addPoint(bounds.x + FOLD_HEIGHT, bounds.y);
        points1.addPoint(bounds.x + bounds.width - SHADOW_OFFSET, bounds.y);
        points1.addPoint(bounds.x + bounds.width - FOLD_HEIGHT - 1, bounds.y + FOLD_HEIGHT);
        graphics.fillPolygon(points1);
        
        // Angle 2
        PointList points2 = new PointList();
        points2.addPoint(bounds.x + bounds.width - SHADOW_OFFSET, bounds.y);
        points2.addPoint(bounds.x + bounds.width - SHADOW_OFFSET, bounds.y + bounds.height - FOLD_HEIGHT - SHADOW_OFFSET - 1);
        points2.addPoint(bounds.x + bounds.width - FOLD_HEIGHT - 1, bounds.y + bounds.height - SHADOW_OFFSET - 1);
        points2.addPoint(bounds.x + bounds.width - FOLD_HEIGHT - 1, bounds.y + FOLD_HEIGHT);
        graphics.fillPolygon(points2);

        // Line
        graphics.setBackgroundColor(ColorConstants.black);
        graphics.drawRectangle(bounds.x, bounds.y + FOLD_HEIGHT, bounds.width - FOLD_HEIGHT - 1, bounds.height - FOLD_HEIGHT - 3);
        graphics.drawLine(points1.getPoint(0), points1.getPoint(1));
        graphics.drawLine(points1.getPoint(1), points1.getPoint(2));
        graphics.drawLine(points1.getPoint(2), points1.getPoint(3));
        graphics.drawLine(points2.getPoint(0), points2.getPoint(1));
        graphics.drawLine(points2.getPoint(1), points2.getPoint(2));
        
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
