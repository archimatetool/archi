/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.ui.IMemento;

import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateModelObject;
import com.archimatetool.model.IIdentifier;
import com.archimatetool.model.util.ArchimateModelUtils;



/**
 * Helper class to manage persistence of Tree state
 * 
 * @author Phillip Beauvoir
 */
public class TreeStateHelper {
    
    public static TreeStateHelper INSTANCE = new TreeStateHelper();
    
    private static final String ELEMENT_SEP_CHAR = " "; //$NON-NLS-1$
    
    private static final String MEMENTO_EXPANDED = "expanded"; //$NON-NLS-1$
    private static final String MEMENTO_MODEL = "model"; //$NON-NLS-1$
    private static final String MEMENTO_FILE = "file"; //$NON-NLS-1$
    private static final String MEMENTO_ELEMENTS = "elements"; //$NON-NLS-1$
    
    // Expanded tree elements or element ids for the session
    private List<Object> fExpandedElements = new ArrayList<Object>();
    
    /**
     * Flag to show we have restored from Memento first time open
     */
    private boolean fRestoredFromMemento = false;
    
    private class FileMap {
        File file;
        String[] elements;
    }

    /**
     * Set the Memento on Application Open
     * @param memento
     */
    void setMemento(IMemento memento) {
        // This is also called when the TreeView is opened, but we only want to do this once
        if(memento == null || fRestoredFromMemento) {
            return;
        }
        
        // Store expanded elements as ids now, as the tree has not been created yet
        for(IMemento expandedMem : memento.getChildren(MEMENTO_EXPANDED)) {
            for(IMemento elementMem : expandedMem.getChildren(MEMENTO_MODEL)) {
                String file = elementMem.getString(MEMENTO_FILE);
                String elements = elementMem.getString(MEMENTO_ELEMENTS);
                if(file != null && elements != null) {
                    FileMap fm = new FileMap();
                    fm.file = new File(file);
                    fm.elements = elements.split(ELEMENT_SEP_CHAR);
                    fExpandedElements.add(fm);
                }
            }
        }
        
        fRestoredFromMemento = true;
    }

    /**
     * Restore expanded elements on TreeView creation
     */
    void restoreExpandedTreeElements(TreeViewer viewer) {
        // Store expanded tree elements if View is closed
        viewer.getTree().addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent e) {
                fExpandedElements.clear();
                for(Object element : viewer.getVisibleExpandedElements()) {
                    fExpandedElements.add(element);
                }
            }
        });
        
        for(Object o : fExpandedElements) {
            // Actual object
            if(o instanceof IArchimateModelObject) {
                viewer.expandToLevel(o, 1);
            }
            
            // String ids
            if(o instanceof FileMap) {
                try {
                    File file = ((FileMap)o).file;
                    String[] elements = ((FileMap)o).elements;
                    for(IArchimateModel model : IEditorModelManager.INSTANCE.getModels()) {
                        if(file.equals(model.getFile())) {
                            for(String id : elements) {
                                EObject element = ArchimateModelUtils.getObjectByID(model, id);
                                if(element != null) {
                                    viewer.expandToLevel(element, 1);
                                }
                            }
                            break; // found model
                        }
                    }
                }
                catch(Exception ex) {
                    // We don't want to fail just for some stupid string operation
                    ex.printStackTrace();
                }
            }
        }
        
        // Allow the elements to be garbage collected
        fExpandedElements.clear();
    }
    
    /**
     * Save expanded state of tree elements on Application close
     * @param memento
     */
    void saveStateOnApplicationClose(TreeViewer viewer, IMemento memento) {
        Hashtable<File, String> map = new Hashtable<File, String>();
        
        IMemento expandedMem = memento.createChild(MEMENTO_EXPANDED);

        for(Object element : viewer.getVisibleExpandedElements()) {
            if(element instanceof IIdentifier && element instanceof IArchimateModelObject) {
                // Only store if saved in a file
                File file = ((IArchimateModelObject)element).getArchimateModel().getFile();
                if(file != null) {
                    String id = ((IIdentifier)element).getId();
                    String string = map.get(file);
                    if(string == null) {
                        string = id;
                    }
                    else {
                        string += ELEMENT_SEP_CHAR + id;
                    }
                    map.put(file, string);
                }
            }
        }
        
        for(File file : map.keySet()) {
            IMemento elementMem = expandedMem.createChild(MEMENTO_MODEL);
            elementMem.putString(MEMENTO_FILE, file.getAbsolutePath());
            elementMem.putString(MEMENTO_ELEMENTS, map.get(file));
        }
    }
}
