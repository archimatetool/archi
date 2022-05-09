/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import com.archimatetool.editor.model.impl.EditorModelManager;
import com.archimatetool.model.IArchimateModel;



/**
 * IEditorModelHandler
 * 
 * @author Phillip Beauvoir
 */
public interface IEditorModelManager {
    
    String ARCHIMATE_FILE_EXTENSION = ".archimate"; //$NON-NLS-1$
    String ARCHIMATE_FILE_WILDCARD = "*.archimate"; //$NON-NLS-1$
    
    String PROPERTY_MODEL_CREATED = "IEditorModelManager.model.created"; //$NON-NLS-1$
    String PROPERTY_MODEL_OPENED = "IEditorModelManager.model.opened"; //$NON-NLS-1$
    String PROPERTY_MODEL_LOADED = "IEditorModelManager.model.loaded"; //$NON-NLS-1$
    String PROPERTY_MODEL_REMOVED = "IEditorModelManager.model.removed"; //$NON-NLS-1$
    String PROPERTY_MODEL_SAVED = "IEditorModelManager.model.saved"; //$NON-NLS-1$
    
    String COMMAND_STACK_CHANGED = "IEditorModelManager.model.dirty"; //$NON-NLS-1$

    String PROPERTY_ECORE_EVENT = "IEditorModelManager.ecore.event"; //$NON-NLS-1$
    
    /*
     *  Notification that many ECore events will be fired in succession.
     *  Listeners can choose to then ignore the events and then update when notified of end.
     */
    String PROPERTY_ECORE_EVENTS_START = "IEditorModelManager.ecore.events.start"; //$NON-NLS-1$
    String PROPERTY_ECORE_EVENTS_END = "IEditorModelManager.ecore.events.end"; //$NON-NLS-1$
    
    /*
     * If the user creates a new view and it's open and then the user closes the application without first
     * saving the model, then Eclipse tries to restore it again next time. So we don't persist the state
     * of the diagram if the diagram view is not saved.
     */
    String ADAPTER_PROPERTY_MODEL_SAVED = "saved"; //$NON-NLS-1$
    
    /**
     * The singleton instance of the Editor Model Manager
     */
    IEditorModelManager INSTANCE = new EditorModelManager();
    
    /**
     * @return Models
     */
    List<IArchimateModel> getModels();

    /**
     * @return New Model
     */
    IArchimateModel createNewModel();
    
    /**
     * Register a model in the manager
     * @param model
     */
    void registerModel(IArchimateModel model);
    
    /**
     * Open a model by loading it and opening it in the Model Tree
     * @return The newly opened model or null if the file cannot be opened
     */
    IArchimateModel openModel(File file);
    
    /**
     * Open an existing model
     * @param model
     */
    void openModel(IArchimateModel model);
    
    /**
     * Load a model and notify the UI. This is called to load the model and set it in the Model Tree and UI.
     * @param file The file to load
     * @return The newly loaded model or null
     */
    IArchimateModel loadModel(File file);
    
    /**
     * Load a model but not in the UI. Will not send UI notifications or appear in the Models Tree.
     * @param file The file to load
     * @return The newly loaded model or null
     */
    IArchimateModel load(File file) throws IOException;
    
    /**
     * Close a model in the UI and notify the UI. Will ask the user to save the model if it's dirty.
     * @param model The model
     * @return false if user cancels
     * @throws IOException
     */
    boolean closeModel(IArchimateModel model) throws IOException;
    
    /**
     * Close a model in the UI and notify the UI.
     * @param model The model
     * @param askSave if true will ask the user to save the model if it's dirty
     * @return true if successful
     * @throws IOException
     */
    boolean closeModel(IArchimateModel model, boolean askSave) throws IOException;
    
    /**
     * Save model asking user for file name if needed
     * @param model
     * @return false if user cancels
     * @throws IOException
     */
    boolean saveModel(IArchimateModel model) throws IOException;
    
    /**
     * Save model as asking user for file name
     * @param model
     * @return false if user cancels
     * @throws IOException
     */
    boolean saveModelAs(IArchimateModel model) throws IOException;
    
    /**
     * Check if the model needs saving
     * @param model
     * @return True if model has been changed and needs saving
     */
    boolean isModelDirty(IArchimateModel model);
    
    /**
     * Save the state of loaded models
     * @throws IOException
     */
    void saveState() throws IOException;
    
    /**
     * @param file
     * @return True if the model backed by file is already loaded
     */
    boolean isModelLoaded(File file);
    
    /**
     * Add a Property Change Listener
     * @param listener
     */
    void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * Remove a Property Change Listener
     * @param listener
     */
    void removePropertyChangeListener(PropertyChangeListener listener);
    
    /**
     * Fire a Property Change event
     * @param source The object affected
     * @param prop The property that changed
     * @param oldValue Old Value 
     * @param newValue New Value
     */
    void firePropertyChange(Object source, String prop, Object oldValue, Object newValue);
}
