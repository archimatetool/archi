/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IMemento;

import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateModelObject;
import com.archimatetool.model.util.ArchimateModelUtils;
import com.archimatetool.model.util.Logger;



/**
 * Helper class to manage persistence of Tree state
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class TreeStateHelper {
    
    public static TreeStateHelper INSTANCE = new TreeStateHelper();
    
    private static final String ELEMENT_SEP_CHAR = " ";
    
    private static final String MEMENTO_EXPANDED = "expanded";
    private static final String MEMENTO_MODEL = "model";
    private static final String MEMENTO_FILE = "file";
    private static final String MEMENTO_ELEMENTS = "elements";
    
    // Expanded tree elements
    private List<Object> expandedElements;
    
    // State has been restored from memento the first time the Tree is created
    private boolean restoredFromMemento;
    
    private TreeStateHelper() {}

    /**
     * Set the Memento when the tree is first created.
     * We store expanded elements now, as the tree has not yet been created.
     * We will use the expanded elements later in restoreExpandedTreeElements.
     * This is called from {@link TreeModelView#init(org.eclipse.ui.IViewSite, IMemento)}, but we only want to do this once.
     */
    void setMemento(IMemento memento) {
        if(restoredFromMemento || memento == null) {
            return;
        }
        
        restoredFromMemento = true;
        
        IMemento expandedMem = memento.getChild(MEMENTO_EXPANDED);
        if(expandedMem != null) {
            expandedElements = new ArrayList<>();
            
            try {
                for(IMemento elementMem : expandedMem.getChildren(MEMENTO_MODEL)) {
                    String filePath = elementMem.getString(MEMENTO_FILE);
                    String elements = elementMem.getString(MEMENTO_ELEMENTS);
                    if(filePath != null && elements != null) {
                        File file = new File(filePath);
                        for(IArchimateModel model : IEditorModelManager.INSTANCE.getModels()) {
                            // Get model from file
                            if(file.equals(model.getFile())) {
                                // Get object ID map
                                Map<String, EObject> objectMap = ArchimateModelUtils.getObjectIDMap(model);
                                // Get objects from IDs
                                for(String id : elements.split(ELEMENT_SEP_CHAR)) {
                                    EObject object = objectMap.get(id);
                                    if(object != null) {
                                        expandedElements.add(object);
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            }
            // We don't want to fail at this point
            catch(Exception ex) {
                Logger.logError("Error restoring tree state", ex);
            }
        }
    }

    /**
     * Restore expanded elements on TreeView part creation.
     * This is called from {@link TreeModelView#doCreatePartControl(org.eclipse.swt.widgets.Composite)
     */
    void restoreExpandedTreeElements(TreeViewer viewer) {
        // Store expanded tree elements if the TreeViewer is closed so they can be restored when it's re-opened
        // We could restore from the memento but this is more efficient and the drill-down is reset when closing the Tree.
        viewer.getTree().addDisposeListener(e -> {
            expandedElements = new ArrayList<>();
            for(Object element : viewer.getVisibleExpandedElements()) {
                expandedElements.add(element);
            }
        });

        if(expandedElements != null) {
            for(Object element : expandedElements) {
                viewer.expandToLevel(element, 1);
            }
            expandedElements = null;
        }
    }
    
    /**
     * Save expanded state of tree elements to a memento.
     * This is called from {@link TreeModelView#saveState(IMemento)}
     */
    void saveStateToMemento(TreeViewer viewer, IMemento memento) {
        Map<File, String> map = new HashMap<>();
        
        for(Object object : viewer.getVisibleExpandedElements()) {
            if(object instanceof IArchimateModelObject modelObject) {
                // Only store if model has been saved to file
                File file = modelObject.getArchimateModel().getFile();
                if(file != null) {
                    // Create a string of character separated IDs
                    String id = modelObject.getId();
                    String string = map.get(file);
                    string = string == null ? id : string + ELEMENT_SEP_CHAR + id;
                    map.put(file, string);
                }
            }
        }
        
        if(map.isEmpty()) {
            return;
        }
        
        IMemento expandedMem = memento.createChild(MEMENTO_EXPANDED);

        for(Entry<File, String> entry : map.entrySet()) {
            IMemento elementMem = expandedMem.createChild(MEMENTO_MODEL);
            elementMem.putString(MEMENTO_FILE, entry.getKey().getAbsolutePath());
            elementMem.putString(MEMENTO_ELEMENTS, entry.getValue());
        }
    }
}
