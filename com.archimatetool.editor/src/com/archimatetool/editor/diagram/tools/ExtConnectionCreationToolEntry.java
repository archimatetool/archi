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
 * Extended ConnectionCreationToolEntry so we can set our own "add" cursors instead of the default "plug" ones which are ugly
 * 
 * @author Phillip Beauvoir
 */
public class ExtConnectionCreationToolEntry extends ConnectionCreationToolEntry {
    
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
    
    public ExtConnectionCreationToolEntry(String label, String shortDesc, CreationFactory factory, ImageDescriptor iconSmall, ImageDescriptor iconLarge) {
        super(label, shortDesc, factory, iconSmall, iconLarge);
    }
    
    @Override
    public Tool createTool() {
        AbstractTool tool = (AbstractTool)super.createTool();
        tool.setDefaultCursor(cursorAdd);
        tool.setDisabledCursor(cursorAddNot);
        return tool;
    }
}