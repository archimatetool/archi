/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.graphics.Color;

import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.model.ITextAlignment;
import com.archimatetool.model.ITextPosition;

/**
 * Abstract Object UI Provider
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractGraphicalObjectUIProvider extends AbstractObjectUIProvider
implements IGraphicalObjectUIProvider {
    
    protected AbstractGraphicalObjectUIProvider() {
    }

    @Override
    public Color getDefaultColor() {
        return ColorConstants.white;
    }
    
    @Override
    public Color getDefaultLineColor() {
        return ColorFactory.get(92, 92, 92);
    }
    
    @Override
    public Dimension getDefaultSize() {
        return new Dimension(-1, -1);
    }
    
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
