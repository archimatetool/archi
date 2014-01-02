/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas;

import com.archimatetool.editor.ui.ImageFactory;




/**
 * Image Factory for this application
 * 
 * @author Phillip Beauvoir
 */
public interface ICanvasImages {
    
    ImageFactory ImageFactory = new ImageFactory(CanvasEditorPlugin.INSTANCE);

    String IMGPATH = "img/"; //$NON-NLS-1$
    
    String ICON_CANVAS_BLANK_16 = IMGPATH + "canvas-blank-16.png"; //$NON-NLS-1$
    String ICON_CANVAS_BLOCK_16 = IMGPATH + "block-16.png"; //$NON-NLS-1$
    String ICON_CANVAS_MODEL_16 = IMGPATH + "canvas-16.png"; //$NON-NLS-1$
    String ICON_CANVAS_STICKY_16 = IMGPATH + "sticky-16.png"; //$NON-NLS-1$
    
    String ICON_NEWFILE_16 = IMGPATH + "newfile.gif"; //$NON-NLS-1$
}
