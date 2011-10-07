/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.actions;

import java.io.File;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
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
    public static final String TEXT = "View As Image...";

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

            if(file.endsWith(".bmp")) {
                loader.save(file, SWT.IMAGE_BMP);
            }
            else if(file.endsWith(".jpg") || file.endsWith(".jpeg")) {
                loader.save(file, SWT.IMAGE_JPEG);
            }
            else if(file.endsWith(".png")) {
                loader.save(file, SWT.IMAGE_PNG);
            }
            else {
                file = file + ".png";
                loader.save(file, SWT.IMAGE_PNG);
            }
        }
        catch(Exception ex) {
            MessageDialog.openError(Display.getCurrent().getActiveShell(), "Export As Image", ex.getMessage());
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
        dialog.setText("Export View As Image");
        dialog.setFilterExtensions(new String[] { "*.png", "*.jpg;*.jpeg", "*.bmp" } );
        
        String path = dialog.open();
        if(path == null) {
            return null;
        }
        
        // Only Windows adds the extension by default
        switch(dialog.getFilterIndex()) {
            case 0:
                if(!path.endsWith(".png")) {
                    path += ".png";
                }
                break;
            case 1:
                if(!path.endsWith(".jpg") && !path.endsWith(".jpeg")) {
                    path += ".jpg";
                }
                break;
            case 2:
                if(!path.endsWith(".bmp")) {
                    path += ".bmp";
                }
                break;

            default:
                break;
        }
        
        // Make sure the file does not already exist
        File file = new File(path);
        if(file.exists()) {
            boolean result = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(), "Export As Image",
                    "'" + file +
                    "' already exists. Are you sure you want to overwrite it?");
            if(!result) {
                return null;
            }
        }
        
        return path;
    }

    
}
