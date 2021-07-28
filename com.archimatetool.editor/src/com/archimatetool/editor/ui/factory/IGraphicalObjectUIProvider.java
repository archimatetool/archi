/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.graphics.Color;


/**
 * Interface for Graphical Object UI Provider
 * 
 * @author Phillip Beauvoir
 */
public interface IGraphicalObjectUIProvider extends IObjectUIProvider {
    
    /**
     * @return the base default size for a figure
     */
    static Dimension defaultSize() {
        return new Dimension(120, 55);
    }
    
    /**
     * @return The default colour to use for this object (usually a fill color)
     */
    Color getDefaultColor();

    /**
     * @return The default line colour to use for this object
     */
    Color getDefaultLineColor();
    
    /**
     * @return The default size width and height for this object as set by the user or inbuilt
     */
    Dimension getDefaultSize();
    
    /**
     * @deprecated Use {@link #getDefaultSize()}
     * @return The default size as set by the user for this object
     */
    Dimension getUserDefaultSize();
    
    /**
     * @return The default text alignment
     */
    int getDefaultTextAlignment();

    /**
     * @return The default text position
     */
    int getDefaultTextPosition();
    
    /**
     * @return true if the figure for this object should have an icon in the top-right position
     */
    boolean hasIcon();
}
