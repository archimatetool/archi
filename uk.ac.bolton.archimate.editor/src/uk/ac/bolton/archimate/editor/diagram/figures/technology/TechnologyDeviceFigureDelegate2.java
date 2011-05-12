/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.figures.technology;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;

import uk.ac.bolton.archimate.editor.diagram.figures.IDiagramModelObjectFigure;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;



/**
 * Technology Device Figure Delegate 2
 * 
 * @author Phillip Beauvoir
 */
public class TechnologyDeviceFigureDelegate2 extends TechnologyNodeFigureDelegate {

    public TechnologyDeviceFigureDelegate2(IDiagramModelObjectFigure owner) {
        super(owner);
    }
    
    @Override
    public void drawFigure(Graphics graphics) {
        super.drawFigure(graphics);
        
        // Image icon
        if(getImage() != null) {
            graphics.drawImage(getImage(), calculateImageLocation());
        }
    }

    protected Image getImage() {
        return IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_DEVICE_16);
    }
    
    protected Point calculateImageLocation() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - FOLD_HEIGHT * 2 - 5, bounds.y + FOLD_HEIGHT + 2);
    }

}
