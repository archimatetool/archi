/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.csv;

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
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;

import com.archimatetool.editor.model.IModelExporter;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.INameable;
import com.archimatetool.model.IRelationship;



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
        writeFolder(model.getFolder(FolderType.MOTIVATION));
        writeFolder(model.getFolder(FolderType.IMPLEMENTATION_MIGRATION));
        writeFolder(model.getFolder(FolderType.CONNECTORS));
        writeFolder(model.getFolder(FolderType.RELATIONS));
        
        writer.close();
    }
    
    private void writeHeader() throws IOException {
        writer.write(Messages.CSVExporter_0);
    }
    
    private void writeFolder(IFolder folder) throws IOException {
        List<EObject> list = new ArrayList<EObject>();
        getElements(folder, list);
        
        sort(list);
        
        for(EObject eObject : list) {
            if(eObject instanceof IArchimateElement) {
                IArchimateElement element = (IArchimateElement)eObject;
                String name = normalise(element.getName());
                String s = "\"" + element.eClass().getName() + "\"," + "\"" + name + "\"," + "\"" + normalise(element.getDocumentation() + "\","); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
                if(eObject instanceof IRelationship) {
                    IRelationship relationship = (IRelationship)eObject;
                    s += "\"" + relationship.getSource().eClass().getName() + "\","; //$NON-NLS-1$ //$NON-NLS-2$
                    s += "\"" + relationship.getSource().getName() + "\","; //$NON-NLS-1$ //$NON-NLS-2$
                    s += "\"" + relationship.getTarget().eClass().getName() + "\","; //$NON-NLS-1$ //$NON-NLS-2$
                    s += "\"" + relationship.getTarget().getName() + "\","; //$NON-NLS-1$ //$NON-NLS-2$
                }
                else {
                    s += "\"\",\"\",\"\",\"\""; //$NON-NLS-1$
                }
                writer.write(s + "\n"); //$NON-NLS-1$
            }
        }
    }
    
    private String normalise(String s) {
        if(s == null) {
            return ""; //$NON-NLS-1$
        }
        
        s = s.replaceAll("(\r\n|\r|\n|\t)", " "); //$NON-NLS-1$ //$NON-NLS-2$
        
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
        dialog.setText(Messages.CSVExporter_1);
        dialog.setFilterExtensions(new String[] { "*.csv", "*.*" } ); //$NON-NLS-1$ //$NON-NLS-2$
        String path = dialog.open();
        if(path == null) {
            return null;
        }
        
        // Only Windows adds the extension by default
        if(dialog.getFilterIndex() == 0 && !path.endsWith(".csv")) { //$NON-NLS-1$
            path += ".csv"; //$NON-NLS-1$
        }
        
        File file = new File(path);
        
        // Make sure the file does not already exist
        if(file.exists()) {
            boolean result = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(),
                    Messages.CSVExporter_1,
                    NLS.bind(Messages.CSVExporter_2, file));
            if(!result) {
                return null;
            }
        }
        
        return file;
    }
}
