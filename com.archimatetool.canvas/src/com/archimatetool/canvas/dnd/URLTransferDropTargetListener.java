/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas.dnd;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.swt.dnd.URLTransfer;


/**
 * URL DnD listener
 * 
 * @author Phillip Beauvoir
 */
public class URLTransferDropTargetListener extends FileTransferDropTargetListener {
    
    public URLTransferDropTargetListener(EditPartViewer viewer) {
        super(viewer);
        setTransfer(URLTransfer.getInstance());
    }
}
