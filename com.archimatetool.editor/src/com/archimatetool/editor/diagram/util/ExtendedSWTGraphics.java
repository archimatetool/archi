/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.util;

import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.swt.graphics.GC;

/**
 * Extended SWTGraphics so we can query the scale
 * 
 * @author Phillip Beauvoir
 */
public class ExtendedSWTGraphics extends SWTGraphics {

    private double scale = 1.0;
    
    public ExtendedSWTGraphics(GC gc) {
        super(gc);
    }

    @Override
    public void scale(double factor) {
        super.scale(factor);
        scale = factor;
    }
    
    /**
     * Don't over-ride getAbsoluteScale() as we want that to remain the same
     * 
     * @return The current scale
     */
    public double getScale() {
        return scale;
    }
}
