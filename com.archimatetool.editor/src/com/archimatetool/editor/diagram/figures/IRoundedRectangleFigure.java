/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import org.eclipse.draw2d.geometry.Dimension;



/**
 * Rectangle Figure with Rounded edges
 * 
 * @author Phillip Beauvoir
 */
public interface IRoundedRectangleFigure {
    
    /**
     * @return The Rounded arc
     */
    Dimension getArc();
    
    /**
     * Set the arc
     * @param arc
     */
    void setArc(Dimension arc);
}
