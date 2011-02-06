/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.model.export;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;

import uk.ac.bolton.archimate.editor.model.IModelExporter;
import uk.ac.bolton.archimate.editor.utils.StringUtils;
import uk.ac.bolton.archimate.model.FolderType;
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IArchimateModel;
import uk.ac.bolton.archimate.model.IFolder;
import uk.ac.bolton.archimate.model.INameable;
import uk.ac.bolton.archimate.model.IRelationship;


/**
 * Export Model to CSV
 * 
 * @author Phillip Beauvoir
 */
public class CSVExporter implements IModelExporter {
    
    private OutputStreamWriter writer;

    @Override
    public void export(IArchimateModel model) throws IOException {
        File file = askSaveFile();
        if(file == null) {
            return;
        }
        
        writer = new OutputStreamWriter(new FileOutputStream(file));
        
        writeHeader();
        
        writeFolder(model.getFolder(FolderType.BUSINESS));
        writeFolder(model.getFolder(FolderType.APPLICATION));
        writeFolder(model.getFolder(FolderType.TECHNOLOGY));
        writeFolder(model.getFolder(FolderType.CONNECTORS));
        writeFolder(model.getFolder(FolderType.RELATIONS));
        
        writer.close();
    }
    
    private void writeHeader() throws IOException {
        writer.write("\"Type\",\"Element name\",\"Documentation\",\"Source type\",\"Source name\",\"Target type\",\"Target name\"\n");
    }
    
    private void writeFolder(IFolder folder) throws IOException {
        List<EObject> list = new ArrayList<EObject>();
        getElements(folder, list);
        
        sort(list);
        
        for(EObject eObject : list) {
            if(eObject instanceof IArchimateElement) {
                IArchimateElement element = (IArchimateElement)eObject;
                String name = normalise(element.getName());
                String s = "\"" + element.eClass().getName() + "\"," + "\"" + name + "\"," + "\"" + normalise(element.getDocumentation() + "\",");
                if(eObject instanceof IRelationship) {
                    IRelationship relationship = (IRelationship)eObject;
                    s += "\"" + relationship.getSource().eClass().getName() + "\",";
                    s += "\"" + relationship.getSource().getName() + "\",";
                    s += "\"" + relationship.getTarget().eClass().getName() + "\",";
                    s += "\"" + relationship.getTarget().getName() + "\",";
                }
                else {
                    s += "\"\",\"\",\"\",\"\"";
                }
                writer.write(s + "\n");
            }
        }
    }
    
    private String normalise(String s) {
        if(s == null) {
            return "";
        }
        
        s = s.replaceAll("(\r\n|\r|\n|\t)", " ");
        
        return s;
    }
    
    private void getElements(IFolder folder, List<EObject> list) {
        for(EObject object : folder.getElements()) {
            list.add(object);
        }
        
        for(IFolder f : folder.getFolders()) {
            getElements(f, list);
        }
    }

    private void sort(List<EObject> list) {
        if(list.isEmpty()) {
            return;
        }
        
        Collections.sort(list, new Comparator<EObject>() {
            @Override
            public int compare(EObject o1, EObject o2) {
                if(o1.eClass().equals(o2.eClass())) {
                    if(o1 instanceof INameable && o2 instanceof INameable) {
                        String name1 = StringUtils.safeString(((INameable)o1).getName()).toLowerCase().trim();
                        String name2 = StringUtils.safeString(((INameable)o2).getName()).toLowerCase().trim();
                        return name1.compareTo(name2);
                    }
                }
                String name1 = o1.eClass().getName().toLowerCase();
                String name2 = o2.eClass().getName().toLowerCase();
                return name1.compareTo(name2);
            }
        });
    }
    
    /**
     * Ask user for file name to save to
     */
    private File askSaveFile() {
        FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
        dialog.setText("Export Model");
        dialog.setFilterExtensions(new String[] { "*.csv", "*.*" } );
        String path = dialog.open();
        if(path == null) {
            return null;
        }
        
        // Only Windows adds the extension by default
        if(dialog.getFilterIndex() == 0 && !path.endsWith(".csv")) {
            path += ".csv";
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
