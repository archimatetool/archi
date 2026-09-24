/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.util;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.widgets.Control;

/**
 * Extension of ImagePrintFigureOperation for use in DiagramUtils
 */
public class ImagePrintDiagramOperation extends ImagePrintFigureOperation {
    private final Rectangle bounds;
    private final double scale;

    /**
     * @param control control to to print on. Must not be {@code null}.
     * @param printSource figure to print
     * @param scale The scale to use for the image.
     * @param bounds The bounds of the figure
     */
    public ImagePrintDiagramOperation(Control control, IFigure printSource, double scale, Rectangle bounds) {
        super(control, printSource);
        this.scale = scale;
        this.bounds = bounds.getCopy(); // Important to use a copy in case bounds is changed elsewhere
    }

    @Override
    protected Dimension getImageSize() {
        // Scale the size (width and height) of the bounds
        return bounds.getSize().scale(scale);
    }

    @Override
    protected void preparePrintSource(Graphics graphics) {
        // Scale
        if(scale != 1) {
            graphics.scale(scale);
        }
        
        // Compensate for negative co-ordinates
        graphics.translate(bounds.x * -1, bounds.y * -1);
    }
}