/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.zest;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

import com.archimatetool.editor.Logger;
import com.archimatetool.editor.diagram.util.DiagramUtils;
import com.archimatetool.editor.ui.ClipboardImageTransfer;
import com.archimatetool.editor.ui.ImageFactory;


/**
 * Copy Zest Diagram As Image to clipboard Action
 * 
 * @author Phillip Beauvoir
 */
public class CopyZestViewAsImageToClipboardAction extends Action {
    
    private ZestGraphViewer fGraphViewer;

    public CopyZestViewAsImageToClipboardAction(ZestGraphViewer graphViewer) {
        super(Messages.CopyZestViewAsImageToClipboardAction_0);
        fGraphViewer = graphViewer;
        setToolTipText(getText());
    }

    @Override
    public void run() {
        BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {
            @Override
            public void run() {
                Image image = null;
                try {
                    image = DiagramUtils.createImage(fGraphViewer.getGraphControl().getContents(), 1, 10);
                    ImageData imageData = image.getImageData(ImageFactory.getImageDeviceZoom());
                    ClipboardImageTransfer.copyImageDataToClipboard(imageData);
                }
                catch(Throwable ex) { // Catch Throwable for SWT errors
                    Logger.log(IStatus.ERROR, "Error exporting image", ex); //$NON-NLS-1$
                    
                    MessageDialog.openError(Display.getCurrent().getActiveShell(),
                            Messages.CopyZestViewAsImageToClipboardAction_0,
                            Messages.CopyZestViewAsImageToClipboardAction_2 + " " + //$NON-NLS-1$
                                    (ex.getMessage() == null ? ex.toString() : ex.getMessage()));
                }
                finally {
                    if(image != null) {
                        image.dispose();
                    }
                }
            }
        });
    }
}
