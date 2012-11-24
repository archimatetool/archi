/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.diagram.actions;

import java.io.File;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;

import uk.ac.bolton.archimate.editor.diagram.util.DiagramUtils;


/**
 * Exmport As Image Action
 * 
 * @author Phillip Beauvoir
 */
public class ExportAsImageAction extends Action {
    
    public static final String ID = "ExportAsImageAction"; //$NON-NLS-1$
    public static final String TEXT = Messages.ExportAsImageAction_0;

    private GraphicalViewer fDiagramViewer;

    public ExportAsImageAction(GraphicalViewer diagramViewer) {
        super(TEXT);
        fDiagramViewer = diagramViewer;
        setId(ID);
    }

    @Override
    public void run() {
        String file = askSaveFile();
        if(file == null) {
            return;
        }

        Image image = null;
        
        try {
            image = DiagramUtils.createImage(fDiagramViewer);
            ImageLoader loader = new ImageLoader();
            loader.data = new ImageData[]{ image.getImageData() };

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
                file = file + ".png"; //$NON-NLS-1$
                loader.save(file, SWT.IMAGE_PNG);
            }
        }
        catch(Exception ex) {
            MessageDialog.openError(Display.getCurrent().getActiveShell(), Messages.ExportAsImageAction_1, ex.getMessage());
        }
        finally {
            if(image != null) {
                image.dispose();
            }
        }
    }
    
    /**
     * Ask user for file name to save to
     * @return
     */
    private String askSaveFile() {
        FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
        dialog.setText(Messages.ExportAsImageAction_2);
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
                    Messages.ExportAsImageAction_1,
                    NLS.bind(Messages.ExportAsImageAction_3, file));
            if(!result) {
                return null;
            }
        }
        
        return path;
    }

    
}
