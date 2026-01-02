/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.ui.IMemento;

import com.archimatetool.editor.Logger;
import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateModelObject;
import com.archimatetool.model.util.ArchimateModelUtils;



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
    
    private IMemento memento;
    
    private TreeStateHelper() {}

    /**
     * Set the Memento when the tree is first created.
     * We will restore any expanded elements later in restoreExpandedTreeElements.
     * This is called from {@link TreeModelView#init(org.eclipse.ui.IViewSite, IMemento)}.
     */
    void setMemento(IMemento memento) {
        this.memento = memento;
    }

    /**
     * Restore expanded elements on TreeView part creation.
     * This is called from {@link TreeModelView#doCreatePartControl(org.eclipse.swt.widgets.Composite)
     */
    void restoreExpandedTreeElements(TreeModelViewer viewer) {
        if(memento == null) {
            return;
        }
        
        IMemento expandedMem = memento.getChild(MEMENTO_EXPANDED);
        
        if(expandedMem != null) {
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
                                        viewer.expandToLevel(object, 1);
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
                Logger.error("Error restoring tree state", ex);
            }
        }

        memento = null;
    }
    
    /**
     * Save expanded state of tree elements to a memento.
     * This is called from {@link TreeModelView#saveState(IMemento)}
     */
    void saveStateToMemento(TreeModelViewer viewer, IMemento memento) {
        Map<File, String> map = new HashMap<>();
        
        for(Object object : viewer.getRootVisibleExpandedElements()) {
            if(object instanceof IArchimateModelObject modelObject && modelObject.getArchimateModel() != null) { // Check it wasn't deleted
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
