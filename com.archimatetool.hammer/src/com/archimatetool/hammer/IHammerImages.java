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
    
    String ICON_APP = IMGPATH + "icon.png"; //$NON-NLS-1$
    String ICON_ADVICE = IMGPATH + "info.png"; //$NON-NLS-1$
    String ICON_ERROR = IMGPATH + "error.png"; //$NON-NLS-1$
    String ICON_OK = IMGPATH + "greentick.png"; //$NON-NLS-1$
    String ICON_WARNING = IMGPATH + "warning.png"; //$NON-NLS-1$
}
