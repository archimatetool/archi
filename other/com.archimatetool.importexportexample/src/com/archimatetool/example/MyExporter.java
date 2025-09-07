/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.example;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;

import com.archimatetool.editor.model.IModelExporter;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IFolder;



/**
 * Example Exporter of Archimate model
 * Exports all concepts in a selected model to a text file in format:
 * "Type","Name","Documentation"
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class MyExporter implements IModelExporter {
    
    private static final String MY_EXTENSION = ".mex";
    private static final String MY_EXTENSION_WILDCARD = "*.mex";
    
    public MyExporter() {
    }

    @Override
    public void export(IArchimateModel model) throws IOException {
        // Open dialog to get file to save to
        File file = askSaveFile();
        if(file == null) {
            return;
        }
        
        // Write all concepts in model folders to file
        try(OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file))) {
            writeFolder(model.getFolder(FolderType.STRATEGY), writer);
            writeFolder(model.getFolder(FolderType.BUSINESS), writer);
            writeFolder(model.getFolder(FolderType.APPLICATION), writer);
            writeFolder(model.getFolder(FolderType.TECHNOLOGY), writer);
            writeFolder(model.getFolder(FolderType.MOTIVATION), writer);
            writeFolder(model.getFolder(FolderType.IMPLEMENTATION_MIGRATION), writer);
            writeFolder(model.getFolder(FolderType.OTHER), writer);
            writeFolder(model.getFolder(FolderType.RELATIONS), writer);
        }
    }
    
    /**
     * Write contents of folder to file
     */
    private void writeFolder(IFolder folder, OutputStreamWriter writer) throws IOException {
        List<IArchimateConcept> list = new ArrayList<>();
        getConcepts(folder, list);
        
        // Write concept type, concept name and documentation to text string
        for(IArchimateConcept concept : list) {
            String string = normalise(concept.eClass().getName()) + ","
                            + normalise(concept.getName()) + ","
                            + normalise(concept.getDocumentation());
            writer.write(string + "\n");
        }
    }
    
    /**
     * Get all concepts in a folder and sub-folders
     */
    private void getConcepts(IFolder folder, List<IArchimateConcept> list) {
        for(EObject object : folder.getElements()) {
            if(object instanceof IArchimateConcept concept) {
                list.add(concept);
            }
        }
        
        for(IFolder f : folder.getFolders()) {
            getConcepts(f, list);
        }
    }

    /**
     * Check text for null, replace newlines with a space, and surround text with quotes
     */
    private String normalise(String text) {
        if(text == null) {
            return "";
        }
        
        text = StringUtils.normaliseNewLineCharacters(text);
        text = "\"" + text + "\"";
        
        return text;
    }

    /**
     * Ask user for file name to save to
     */
    private File askSaveFile() {
        FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
        dialog.setText("Export Model");
        dialog.setFilterExtensions(new String[] { MY_EXTENSION_WILDCARD, "*.*" } );

        // Set to true for consistency on all OSs
        dialog.setOverwrite(true);
        
        String path = dialog.open();
        if(path == null) {
            return null;
        }
        
        // Only Windows adds the extension by default
        if(dialog.getFilterIndex() == 0 && !path.endsWith(MY_EXTENSION)) {
            path += MY_EXTENSION;
        }
        
        return new File(path);
    }
}
