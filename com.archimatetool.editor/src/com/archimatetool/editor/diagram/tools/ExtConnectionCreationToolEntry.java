/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.tools;

import org.eclipse.gef.Tool;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.tools.AbstractTool;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Cursor;

import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.ImageFactory;

/**
 * Extended ConnectionCreationToolEntry so we can provide our own cursors
 * The image masking of cursors on Linux is broken, see https://bugs.eclipse.org/bugs/show_bug.cgi?id=575425
 * Also, we need 2x cursors for Windows 200% scale
 * 
 * @author Phillip Beauvoir
 */
public class ExtConnectionCreationToolEntry extends ConnectionCreationToolEntry implements KeyboardAccessibleToolEntry {
    
    private static Cursor cursorAdd = new Cursor(
            null,
            IArchiImages.ImageFactory.getImage(IArchiImages.CURSOR_IMG_ADD_CONNECTION).getImageData(ImageFactory.getCursorDeviceZoom()),
            0,
            0);

    private static Cursor cursorAddNot = new Cursor(
            null,
            IArchiImages.ImageFactory.getImage(IArchiImages.CURSOR_IMG_ADD_NOT_CONNECTION).getImageData(ImageFactory.getCursorDeviceZoom()),
            0,
            0);
    
    private static String combineLabelAndKeyChord(String label, String keyChord) {
    	if (keyChord == null || keyChord.equals("")) {
    		return label;
    	} else {
    		return String.format("%s (%s)", label, keyChord);
    	}
    }
    
    private String keyChord = "";
    
    public ExtConnectionCreationToolEntry(String label, String shortDesc, CreationFactory factory, ImageDescriptor iconSmall, ImageDescriptor iconLarge, String keyChord) {
        super(combineLabelAndKeyChord(label, keyChord), shortDesc, factory, iconSmall, iconLarge);
        this.keyChord = keyChord;
    }
    
    public String getKeyChord() {
    	return this.keyChord;
    }
    
    @Override
    public Tool createTool() {
        AbstractTool tool = (AbstractTool)super.createTool();
        tool.setDefaultCursor(cursorAdd);
        tool.setDisabledCursor(cursorAddNot);
        return tool;
    }
}