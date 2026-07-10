/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.util;

import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.swt.graphics.GC;

/**
 * This class as used in DiagramUtils denotes that the figure scale should be retrieved from
 * Graphics#getAbsoluteScale() not from FigureUtils#getFigureScale()
 *
 * @author Phillip Beauvoir
 */
public class ImageGraphics extends SWTGraphics {

    private double scale = 1.0;

    public ImageGraphics(GC gc) {
        super(gc);
    }

    @Override
    public void scale(double factor) {
        super.scale(factor);
        scale = factor;
    }

    /**
     * @return The current scale
     */
    public double getScale() {
        return scale;
    }
}