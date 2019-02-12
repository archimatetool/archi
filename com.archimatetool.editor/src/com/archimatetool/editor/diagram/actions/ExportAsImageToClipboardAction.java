/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.ImageTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

import com.archimatetool.editor.diagram.util.DiagramUtils;
import com.archimatetool.editor.ui.ImageFactory;
import com.archimatetool.editor.ui.PngTransfer;
import com.archimatetool.editor.utils.PlatformUtils;



/**
 * Export As Image to clipboard Action
 * 
 * @author Phillip Beauvoir
 */
public class ExportAsImageToClipboardAction extends Action {
    
    public static final String ID = "com.archimatetool.editor.action.exportAsImageToClipboard"; //$NON-NLS-1$
    public static final String TEXT = Messages.ExportAsImageToClipboardAction_0;

    private GraphicalViewer fDiagramViewer;

    public ExportAsImageToClipboardAction(GraphicalViewer diagramViewer) {
        super(TEXT);
        fDiagramViewer = diagramViewer;
        setId(ID);
        setActionDefinitionId(getId()); // register key binding
    }

    @Override
    public void run() {
        BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {
            @Override
            public void run() {
                Image image = null;
                Clipboard cb = null;
                
                try {
                    image = DiagramUtils.createImage(fDiagramViewer, 1, 10);
                    ImageData imageData = image.getImageData(ImageFactory.getImageDeviceZoom());
                    
                    cb = new Clipboard(Display.getDefault());
                    
                    // Use different Transfer for Linux64
                    Transfer transfer = (PlatformUtils.isLinux() && PlatformUtils.is64Bit()) ? PngTransfer.getInstance() : ImageTransfer.getInstance(); 
                    
                    cb.setContents(new Object[] { imageData }, new Transfer[] { transfer });
                }
                catch(Throwable ex) { // Catch Throwable for SWT errors
                    ex.printStackTrace();
                    
                    MessageDialog.openError(Display.getCurrent().getActiveShell(),
                            Messages.ExportAsImageToClipboardAction_0,
                            Messages.ExportAsImageToClipboardAction_3 + " " + ex.getMessage()); //$NON-NLS-1$
                }
                finally {
                    if(image != null && !image.isDisposed()) {
                        image.dispose();
                    }
                    
                    if(cb != null) {
                        cb.dispose(); // If memory is low this will crash the JVM
                    }
                }
            }
        });          
    }
}
