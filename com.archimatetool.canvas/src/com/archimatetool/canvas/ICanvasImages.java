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
    
    String ICON_CANVAS_BLANK = IMGPATH + "canvas-blank.png"; //$NON-NLS-1$
    String ICON_CANVAS_BLOCK = IMGPATH + "block.png"; //$NON-NLS-1$
    String ICON_CANVAS_MODEL = IMGPATH + "canvas.png"; //$NON-NLS-1$
    String ICON_CANVAS_STICKY = IMGPATH + "sticky.png"; //$NON-NLS-1$
    
    String ICON_NEWFILE = IMGPATH + "newfile_wiz.png"; //$NON-NLS-1$
}
