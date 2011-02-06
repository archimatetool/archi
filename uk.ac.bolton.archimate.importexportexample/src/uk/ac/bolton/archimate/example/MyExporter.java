/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.example;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;

import uk.ac.bolton.archimate.editor.model.IModelExporter;
import uk.ac.bolton.archimate.model.FolderType;
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IArchimateModel;
import uk.ac.bolton.archimate.model.IFolder;


/**
 * Example Exporter of Archimate model
 * 
 * @author Phillip Beauvoir
 */
public class MyExporter implements IModelExporter {
    
    String MY_EXTENSION = ".mex";
    String MY_EXTENSION_WILDCARD = "*.mex";
    
    private OutputStreamWriter writer;
    
    public MyExporter() {
    }

    @Override
    public void export(IArchimateModel model) throws IOException {
        File file = askSaveFile();
        if(file == null) {
            return;
        }
        
        writer = new OutputStreamWriter(new FileOutputStream(file));
        
        writeFolder(model.getFolder(FolderType.BUSINESS));
        writeFolder(model.getFolder(FolderType.APPLICATION));
        writeFolder(model.getFolder(FolderType.TECHNOLOGY));
        writeFolder(model.getFolder(FolderType.CONNECTORS));
        writeFolder(model.getFolder(FolderType.RELATIONS));
        
        writer.close();
    }
    
    private void writeFolder(IFolder folder) throws IOException {
        List<EObject> list = new ArrayList<EObject>();
        getElements(folder, list);
        for(EObject eObject : list) {
            if(eObject instanceof IArchimateElement) {
                String s;
                IArchimateElement element = (IArchimateElement)eObject;
                s = normalise(element.eClass().getName()) + "," + normalise(element.getName()) + "," + normalise(element.getDocumentation());
                writer.write(s + "\n");
            }
        }
    }
    
    private void getElements(IFolder folder, List<EObject> list) {
        for(EObject object : folder.getElements()) {
            list.add(object);
        }
        
        for(IFolder f : folder.getFolders()) {
            getElements(f, list);
        }
    }

    private String normalise(String s) {
        if(s == null) {
            return "";
        }
        
        s = s.replaceAll("\r\n", " ");
        s = "\"" + s + "\"";
        
        return s;
    }

    /**
     * Ask user for file name to save to
     */
    private File askSaveFile() {
        FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
        dialog.setText("Export Model");
        dialog.setFilterExtensions(new String[] { MY_EXTENSION_WILDCARD, "*.*" } );
        String path = dialog.open();
        if(path == null) {
            return null;
        }
        
        // Only Windows adds the extension by default
        if(dialog.getFilterIndex() == 0 && !path.endsWith(MY_EXTENSION)) {
            path += MY_EXTENSION;
        }
        
        File file = new File(path);
        
        // Make sure the file does not already exist
        if(file.exists()) {
            boolean result = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(), "Export Model",
                    "'" + file +
                    "' already exists. Are you sure you want to overwrite it?");
            if(!result) {
                return null;
            }
        }
        
        return file;
    }
}
