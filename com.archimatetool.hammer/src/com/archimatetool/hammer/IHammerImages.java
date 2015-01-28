/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.hammer;

import com.archimatetool.editor.ui.ImageFactory;




/**
 * Image Factory for this application
 * 
 * @author Phillip Beauvoir
 */
public interface IHammerImages {
    
    ImageFactory ImageFactory = new ImageFactory(ArchiHammerPlugin.INSTANCE);

    String IMGPATH = "img/"; //$NON-NLS-1$
    
    String ICON_APP_16 = IMGPATH + "icon.gif"; //$NON-NLS-1$
    String ICON_ADVICE_16 = IMGPATH + "info.png"; //$NON-NLS-1$
    String ICON_ERROR_16 = IMGPATH + "error.png"; //$NON-NLS-1$
    String ICON_OK_16 = IMGPATH + "greentick.gif"; //$NON-NLS-1$
    String ICON_WARNING_16 = IMGPATH + "warning.png"; //$NON-NLS-1$
}
