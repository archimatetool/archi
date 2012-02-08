/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.actions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;

import uk.ac.bolton.archimate.editor.model.IEditorModelManager;
import uk.ac.bolton.archimate.editor.preferences.IPreferenceConstants;
import uk.ac.bolton.archimate.editor.preferences.Preferences;
import uk.ac.bolton.archimate.editor.utils.StringUtils;
import uk.ac.bolton.archimate.model.IArchimateModel;


/**
 * MRU Files Menu Manager
 * 
 * @author Phillip Beauvoir
 */
public class MRUMenuManager extends MenuManager implements PropertyChangeListener {
    
    private static final String MRU_PREFS_KEY = "MRU"; //$NON-NLS-1$
    
    private List<File> fMRU_List = new ArrayList<File>();
    
    private IWorkbenchWindow fWindow;
    
    private int MAX;
    
    public MRUMenuManager(IWorkbenchWindow window) {
        super(Messages.MRUMenuManager_0, "open_recent_menu"); //$NON-NLS-1$
        
        fWindow = window;
        
        MAX = getCurrentMRUMax();
        
        loadList();
        
        createMenuItems();
        
        IEditorModelManager.INSTANCE.addPropertyChangeListener(this);
        
        // Changed max
        Preferences.STORE.addPropertyChangeListener(new IPropertyChangeListener() {
            @Override
            public void propertyChange(org.eclipse.jface.util.PropertyChangeEvent event) {
                if(IPreferenceConstants.MRU_MAX.equals(event.getProperty())) {
                    MAX = getCurrentMRUMax();
                }
            }
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(IEditorModelManager.PROPERTY_MODEL_OPENED == evt.getPropertyName() || 
                                        IEditorModelManager.PROPERTY_MODEL_SAVED == evt.getPropertyName()) {
            
            IArchimateModel model = (IArchimateModel)evt.getNewValue();
            if(model != null && model.getFile() != null && !isTempFile(model.getFile()) && model.getFile().exists()) {
                addToList(model.getFile());
                createMenuItems();
            }
        }
    }
    
    /**
     * Don't show temp files
     */
    private boolean isTempFile(File file) {
        return file != null && file.getName().startsWith("~"); //$NON-NLS-1$
    }
    
    private void addToList(File file) {
        if(fMRU_List.contains(file)) {
            fMRU_List.remove(file);
            fMRU_List.add(0, file);
        }
        else {
            fMRU_List.add(0, file);
            while(fMRU_List.size() > MAX) {
                fMRU_List.remove(fMRU_List.size() - 1);
            }
        }
    }
    
    private void createMenuItems() {
        removeAll();
        
        for(File file : fMRU_List) {
            add(new RecentFileAction(file));
        }
        
        add(new Separator());
        MRU_ClearAction clearAction = new MRU_ClearAction();
        clearAction.setEnabled(!fMRU_List.isEmpty());
        add(clearAction);
    }
    
    private void clearAll() {
        fMRU_List.clear();
        createMenuItems();
    }
    
    private void loadList() {
        for(int i = 0; i < MAX; i++) {
            String path = Preferences.STORE.getString(MRU_PREFS_KEY + i);
            if(StringUtils.isSet(path)) {
                File file = new File(path);
                fMRU_List.add(file);
            }
        }
    }
    
    private int getCurrentMRUMax() {
        int max = Preferences.STORE.getInt(IPreferenceConstants.MRU_MAX);
        if(max < 3 || max > 15) {
            max = 6;
        }
        return max;
    }
    
    private void saveList() {
        // Clear
        for(int i = 0; i < 50; i++) {
            Preferences.STORE.setValue(MRU_PREFS_KEY + i, ""); //$NON-NLS-1$
        }
        
        // Save
        for(int i = 0; i < fMRU_List.size(); i++) {
            Preferences.STORE.setValue(MRU_PREFS_KEY + i, fMRU_List.get(i).getAbsolutePath());
        }
    }
    
    @Override
    public void dispose() {
        super.dispose();
        IEditorModelManager.INSTANCE.removePropertyChangeListener(this);
        saveList();
    }
    
    private static String getShortPath(File file) {
        String path = file.getAbsolutePath();
        
        try {
            String pathPart = file.getParent();
            final int maxLength = 38;
            if(pathPart.length() > maxLength) {
                pathPart = pathPart.substring(0, maxLength - 3);
                pathPart += "..." + File.separator; //$NON-NLS-1$
                path = pathPart += file.getName();
            }
        }
        catch(Exception ex) { // Catch any exceptions otherwise the app won't load
            ex.printStackTrace();
        }
        
        return path;
    }
    
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
                
                fMRU_List.remove(file);
                createMenuItems();
            }
        }
    }
    
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
