/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.gef.ui.actions.WorkbenchPartAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;

import com.archimatetool.editor.Logger;
import com.archimatetool.editor.diagram.util.DiagramUtils;
import com.archimatetool.editor.ui.ImageFactory;
import com.archimatetool.editor.ui.ClipboardImageTransfer;
import com.archimatetool.model.IDiagramModel;



/**
 * Export As Image to clipboard Action
 * 
 * We create a new GraphicalViewerImpl instance based on the Diagram Model
 * This means we are guaranteed to be at 100% scale
 * 
 * @author Phillip Beauvoir
 */
public class ExportAsImageToClipboardAction extends WorkbenchPartAction {
    
    public static final String ID = "com.archimatetool.editor.action.exportAsImageToClipboard"; //$NON-NLS-1$
    public static final String TEXT = Messages.ExportAsImageToClipboardAction_0;

    public ExportAsImageToClipboardAction(IWorkbenchPart part) {
        super(part);
        setText(TEXT);
        setId(ID);
        setActionDefinitionId(getId()); // register key binding
    }

    @Override
    public void run() {
        BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {
            @Override
            public void run() {
                Image image = null;
                try {
                    IDiagramModel diagramModel = getWorkbenchPart().getAdapter(IDiagramModel.class);
                    image = DiagramUtils.createImage(diagramModel, 1, 10);
                    ImageData imageData = image.getImageData(ImageFactory.getImageDeviceZoom());
                    ClipboardImageTransfer.copyImageDataToClipboard(imageData);
                }
                catch(Throwable ex) { // Catch Throwable for SWT errors
                    Logger.log(IStatus.ERROR, "Error exporting image", ex); //$NON-NLS-1$
                    
                    MessageDialog.openError(getWorkbenchPart().getSite().getShell(),
                            Messages.ExportAsImageToClipboardAction_0,
                            Messages.ExportAsImageToClipboardAction_3 + " " +  //$NON-NLS-1$
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

    @Override
    protected boolean calculateEnabled() {
        return true;
    }
}
