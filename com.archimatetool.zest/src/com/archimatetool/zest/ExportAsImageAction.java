/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.zest;

import java.io.File;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;


/**
 * Export As Image Action
 * 
 * @author Phillip Beauvoir
 */
public class ExportAsImageAction extends Action {
    
    private ZestGraphViewer fGraphViewer;

    public ExportAsImageAction(ZestGraphViewer graphViewer) {
        super(Messages.ExportAsImageAction_0 + "..."); //$NON-NLS-1$
        setToolTipText(Messages.ExportAsImageAction_0);
        fGraphViewer = graphViewer;
    }
    
    @Override
    public void run() {
        final String file = askSaveFile();
        if(file == null) {
            return;
        }
        
        BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {
            @Override
            public void run() {
                Image image = null;
                
                try {
                    image = fGraphViewer.createImage();
                    ImageData imageData = image.getImageData();
                    
                    ImageLoader loader = new ImageLoader();
                    loader.data = new ImageData[] { imageData };

                    if(file.endsWith(".bmp")) { //$NON-NLS-1$
                        loader.save(file, SWT.IMAGE_BMP);
                    }
                    else if(file.endsWith(".jpg") || file.endsWith(".jpeg")) { //$NON-NLS-1$ //$NON-NLS-2$
                        loader.save(file, SWT.IMAGE_JPEG);
                    }
                    else if(file.endsWith(".png")) { //$NON-NLS-1$
                        loader.save(file, SWT.IMAGE_PNG);
                    }
                    else {
                        loader.save(file + ".png", SWT.IMAGE_PNG); //$NON-NLS-1$
                    }
                }
                catch(Throwable ex) { // Catch Throwable for SWT errors
                    ex.printStackTrace();
                    
                    MessageDialog.openError(Display.getCurrent().getActiveShell(),
                            Messages.ExportAsImageAction_0,
                            Messages.ExportAsImageAction_2 + " " + ex.getMessage()); //$NON-NLS-1$
                }
                finally {
                    if(image != null) {
                        image.dispose();
                    }
                }
            }
        });
    }
    
    /**
     * Ask user for file name to save to
     * @return
     */
    private String askSaveFile() {
        FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
        dialog.setText(Messages.ExportAsImageAction_0);
        dialog.setFilterExtensions(new String[] { "*.png", "*.jpg;*.jpeg", "*.bmp" } );  //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$
        
        String path = dialog.open();
        if(path == null) {
            return null;
        }
        
        // Only Windows adds the extension by default
        switch(dialog.getFilterIndex()) {
            case 0:
                if(!path.endsWith(".png")) { //$NON-NLS-1$
                    path += ".png"; //$NON-NLS-1$
                }
                break;
            case 1:
                if(!path.endsWith(".jpg") && !path.endsWith(".jpeg")) { //$NON-NLS-1$ //$NON-NLS-2$
                    path += ".jpg"; //$NON-NLS-1$
                }
                break;
            case 2:
                if(!path.endsWith(".bmp")) { //$NON-NLS-1$
                    path += ".bmp"; //$NON-NLS-1$
                }
                break;

            default:
                break;
        }
        
        // Make sure the file does not already exist
        File file = new File(path);
        if(file.exists()) {
            boolean result = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(),
                    Messages.ExportAsImageAction_0,
                    NLS.bind(Messages.ExportAsImageAction_1, file));
            if(!result) {
                return null;
            }
        }
        
        return path;
    }

    
}
