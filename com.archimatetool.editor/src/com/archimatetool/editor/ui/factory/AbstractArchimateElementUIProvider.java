/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory;

import org.eclipse.draw2d.geometry.Dimension;

import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;





/**
 * Abstract Element UI Provider
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractArchimateElementUIProvider extends AbstractElementUIProvider {
    
    public Dimension getDefaultSize() {
        int width = Preferences.STORE.getInt(IPreferenceConstants.DEFAULT_ARCHIMATE_FIGURE_WIDTH);
        int height = Preferences.STORE.getInt(IPreferenceConstants.DEFAULT_ARCHIMATE_FIGURE_HEIGHT);
        return new Dimension(width, height);
    }
    
}
