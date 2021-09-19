/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.graphics.Color;

import com.archimatetool.model.ITextAlignment;
import com.archimatetool.model.ITextPosition;

/**
 * Abstract Object UI Provider
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractGraphicalObjectUIProvider extends AbstractObjectUIProvider
implements IGraphicalObjectUIProvider {
    
    private static Color defaultColor = new Color(255, 255, 255);
    private static Color defaultLineColor = new Color(92, 92, 92);
    
    protected AbstractGraphicalObjectUIProvider() {
    }

    @Override
    public Color getDefaultColor() {
        return defaultColor;
    }
    
    @Override
    public Color getDefaultLineColor() {
        return defaultLineColor;
    }
    
    @Override
    public Dimension getDefaultSize() {
        return new Dimension(-1, -1);
    }
    
    /**
     * @deprecated Use {@link #getDefaultSize()}
     */
    @Override
    public Dimension getUserDefaultSize() {
        return getDefaultSize();
    }

    @Override
    public int getDefaultTextAlignment() {
        return ITextAlignment.TEXT_ALIGNMENT_CENTER;
    }
    
    @Override
    public int getDefaultTextPosition() {
        return ITextPosition.TEXT_POSITION_TOP;
    }
    
    @Override
    public boolean hasIcon() {
        return false;
    }
}
