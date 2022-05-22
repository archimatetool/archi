/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.actions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IArchimateModel;



/**
 * MRU Files Menu Manager
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class MRUMenuManager extends MenuManager implements PropertyChangeListener {
    
    static final String MRU_PREFS_KEY = "MRU";
    
    private List<File> fMRUList;
    
    private IWorkbenchWindow fWindow;
    
    public MRUMenuManager(IWorkbenchWindow window) {
        super(Messages.MRUMenuManager_0, "open_recent_menu");
        
        fWindow = window;
        
        createMenuItems();
        
        IEditorModelManager.INSTANCE.addPropertyChangeListener(this);
    }

    /**
     * Load the MRU list from preferences
     */
    private List<File> loadMRUListFromPreferenceStore() {
        List<File> list = new ArrayList<File>();
        
        for(int i = 0; i < getPreferencesMRUMax(); i++) {
            String path = ArchiPlugin.PREFERENCES.getString(MRU_PREFS_KEY + i);
            if(StringUtils.isSet(path)) {
                list.add(new File(path));
            }
        }
        
        return list;
    }
    
    List<File> getMRUList() {
        if(fMRUList == null) {
            fMRUList = loadMRUListFromPreferenceStore();
        }
        return fMRUList;
    }
    
    void addToList(File file) {
        List<File> list = getMRUList();
        
        // If file is already in list move it to the top of the list
        if(list.contains(file)) {
            list.remove(file);
            list.add(0, file);
        }
        // Else add it to the top of the list and remove excess off the bottom
        else {
            list.add(0, file);
            while(list.size() > getPreferencesMRUMax()) {
                list.remove(list.size() - 1);
            }
        }
    }
    
    private void saveList() {
        // Clear
        for(int i = 0; i < 50; i++) {
            ArchiPlugin.PREFERENCES.setValue(MRU_PREFS_KEY + i, "");
        }
        
        // Save
        for(int i = 0; i < getMRUList().size(); i++) {
            ArchiPlugin.PREFERENCES.setValue(MRU_PREFS_KEY + i, getMRUList().get(i).getAbsolutePath());
        }
    }
    
    private void createMenuItems() {
        removeAll();
        
        for(File file : getMRUList()) {
            add(new RecentFileAction(file));
        }
        
        add(new Separator());
        MRU_ClearAction clearAction = new MRU_ClearAction();
        clearAction.setEnabled(!getMRUList().isEmpty());
        add(clearAction);
        
        // Workaround for e4 bug:
        // [Contributions] MenuManager.dispose() not called when exiting workbench
        // https://bugs.eclipse.org/bugs/show_bug.cgi?id=428297
        saveList();
    }
    
    void clearAll() {
        getMRUList().clear();
        createMenuItems();
    }
    
    int getPreferencesMRUMax() {
        int max = ArchiPlugin.PREFERENCES.getInt(IPreferenceConstants.MRU_MAX);
        if(max < 3) {
            max = 3;
        }
        if(max > 15) {
            max = 15;
        }
        return max;
    }
    
    /**
     * @param file
     * @return a short ellipsis type string for a file
     */
    String getShortPath(File file) {
        final int maxLength = 38;

        String path = file.getAbsolutePath();
        
        try {
            String pathPart = file.getParent();
            if(pathPart != null && pathPart.length() > maxLength) {
                pathPart = pathPart.substring(0, maxLength - 3);
                pathPart += "..." + File.separator;
                path = pathPart += file.getName();
            }
        }
        catch(Exception ex) { // Catch any exceptions otherwise the app won't load
            ex.printStackTrace();
        }
        
        return path;
    }
    
    /** 
     * User opened or saved a model to file.
     * Update list and menu items
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(IEditorModelManager.PROPERTY_MODEL_OPENED == evt.getPropertyName() || 
                                        IEditorModelManager.PROPERTY_MODEL_SAVED == evt.getPropertyName()) {
            
            IArchimateModel model = (IArchimateModel)evt.getNewValue();
            if(model != null && model.getFile() != null && model.getFile().exists() && !isTempFile(model.getFile())) {
                addToList(model.getFile());
                createMenuItems();
            }
        }
    }
    
    /**
     * Don't show temp files
     */
    boolean isTempFile(File file) {
        // File is in temp folder
        try {
            File tmpDir = new File(System.getProperty("java.io.tmpdir"));
            return file.getCanonicalPath().startsWith(tmpDir.getCanonicalPath());
        }
        catch(IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    @Override
    public void dispose() {
        super.dispose();
        IEditorModelManager.INSTANCE.removePropertyChangeListener(this);
        saveList();
    }
    
    // ====================================== Actions =========================================
    
    /**
     * Recent File Action
     */
    private class RecentFileAction extends Action {
        File file;
        
        RecentFileAction(File file) {
            this.file = file;
            setText(getShortPath(file));
        }
        
        @Override
        public void run() {
            if(file.exists()) {
                if(!IEditorModelManager.INSTANCE.isModelLoaded(file)) {
                    BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {
                        @Override
                        public void run() {
                            IEditorModelManager.INSTANCE.openModel(file);
                        }
                    });
                }
            }
            else {
                // File does not exist
                MessageDialog.openInformation(fWindow.getShell(),
                        Messages.MRUMenuManager_1,
                        NLS.bind(Messages.MRUMenuManager_2, file));
                
                getMRUList().remove(file);
                createMenuItems();
            }
        }
    }
    
    /**
     * Clear Action
     */
    private class MRU_ClearAction extends Action {
        MRU_ClearAction() {
            setText(Messages.MRUMenuManager_3);
        }
        
        @Override
        public void run() {
            clearAll();
        }
    }

}
