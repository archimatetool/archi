/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory.elements;

import org.eclipse.draw2d.geometry.Dimension;

import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.ui.factory.AbstractGraphicalObjectUIProvider;
import com.archimatetool.editor.ui.factory.IArchimateElementUIProvider;



/**
 * Abstract Archimate Element UI Provider
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractArchimateElementUIProvider extends AbstractGraphicalObjectUIProvider
implements IArchimateElementUIProvider {
    
    protected AbstractArchimateElementUIProvider() {
    }
    
    @Override
    public Dimension getDefaultSize() {
        return DefaultRectangularSize;
    }

    @Override
    public Dimension getUserDefaultSize() {
        int width = Preferences.STORE.getInt(IPreferenceConstants.DEFAULT_ARCHIMATE_FIGURE_WIDTH);
        int height = Preferences.STORE.getInt(IPreferenceConstants.DEFAULT_ARCHIMATE_FIGURE_HEIGHT);
        return new Dimension(width, height);
    }
    
    @Override
    public boolean hasAlternateFigure() {
        return false;
    }
    
    @Override
    public int getDefaultTextAlignment() {
        return Preferences.STORE.getInt(IPreferenceConstants.DEFAULT_ARCHIMATE_FIGURE_TEXT_ALIGNMENT);
    }
    
    @Override
    public int getDefaultTextPosition() {
        return Preferences.STORE.getInt(IPreferenceConstants.DEFAULT_ARCHIMATE_FIGURE_TEXT_POSITION);
    }

    @Override
    public boolean hasIcon() {
        return true;
    }
}
