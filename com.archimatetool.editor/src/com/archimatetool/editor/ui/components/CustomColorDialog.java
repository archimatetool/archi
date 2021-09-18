/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.components;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Shell;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.utils.PlatformUtils;


/**
 * Colour Dialog wrapper that saves user's custom colors to prefs file
 * Only needed for Windows as Mac and Linux persist the Color Dialog's custom colours
 * 
 * @author Phillip Beauvoir
 */
public class CustomColorDialog {
    
    private static final int CUSTOM_COLOR_COUNT = 16; // from the MS spec for CHOOSECOLOR.lpCustColors
    
    private ColorDialog colorDialog;

    public CustomColorDialog(Shell parent) {
        this(parent, SWT.NONE);
    }
    
    public CustomColorDialog(Shell parent, int style) {
        colorDialog = new ColorDialog(parent, style);
        
        if(PlatformUtils.isWindows()) {
            colorDialog.setRGBs(getColorChooserColorsFromPreferences());
        }
    }

    public void setRGB(RGB rgb) {
        colorDialog.setRGB(rgb);
    }

    public void setRGBs(RGB[] rgbs) {
        colorDialog.setRGBs(rgbs);
    }

    public RGB getRGB() {
        return colorDialog.getRGB();
    }

    public RGB[] getRGBs() {
        return colorDialog.getRGBs();
    }

    public RGB open() {
        RGB rgb = colorDialog.open();
        
        if(rgb != null && PlatformUtils.isWindows()) {
            saveColorChooserColorsToPreferences();
        }
        
        return rgb;
    }
    
    /**
     * Save the user custom colors from a Color Chooser dialog to Prefs
     */
    private void saveColorChooserColorsToPreferences() {
        RGB[] rgbs = getRGBs();
        if(rgbs != null) {
            for(int i = 0; i < rgbs.length; i++) {
                ArchiPlugin.PREFERENCES.setValue("colorChooser_" + i, ColorFactory.convertRGBToString(rgbs[i])); //$NON-NLS-1$
            }
        }
    }
    
    /**
     * @return The user's custom colors for a Color Chooser dialog from Prefs.
     */
    private RGB[] getColorChooserColorsFromPreferences() {
        List<RGB> rgbs = new ArrayList<RGB>();
        
        for(int i = 0; i < CUSTOM_COLOR_COUNT; i++) {
            String value = ArchiPlugin.PREFERENCES.getString("colorChooser_" + i); //$NON-NLS-1$
            RGB rgb = ColorFactory.convertStringToRGB(value);
            if(rgb != null) {
                rgbs.add(rgb);
            }
            // Fix from JB:
            // If no custom colors have been saved in Prefs yet, return white
            // (otherwise Windows color picker would use the one saved internally for the session, even before a "Cancel" action)
            else {
                rgbs.add(new RGB(255, 255, 255));
            }
        }
        
        return rgbs.toArray(new RGB[rgbs.size()]);
    }
}
