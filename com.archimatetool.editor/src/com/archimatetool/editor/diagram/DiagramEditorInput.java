/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPersistableElement;

import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.model.IDiagramModel;



/**
 * Editor Input for a Diagram Editor
 * 
 * @author Phillip Beauvoir
 */
public class DiagramEditorInput 
implements IEditorInput, IPersistableElement {
    
    /**
     * Model
     */
    private IDiagramModel fModel;
    
    public DiagramEditorInput(IDiagramModel model) {
        fModel = model;
    }
    
    public IDiagramModel getDiagramModel() {
        return fModel;
    }
    
    @Override
    public boolean exists() {
        return false;
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_DIAGRAM);
    }

    @Override
    public String getName() {
        return fModel.getArchimateModel() == null ? Messages.DiagramEditorInput_0 : fModel.getArchimateModel().getName() + ": " + fModel.getName(); //$NON-NLS-1$
    }

    @Override
    public String getToolTipText() {
        return getName();
    }

    @Override
    public <T> T getAdapter(Class<T> adapter) {
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        
        if(!(obj instanceof DiagramEditorInput)) {
            return false;
        }
        
        if(fModel != null && ((DiagramEditorInput)obj).fModel != null) {
            return fModel.equals(((DiagramEditorInput)obj).fModel);
        }
        
        return false;
    }

    @Override
    public IPersistableElement getPersistable() {
        if(fModel == null || fModel.getArchimateModel() == null) {
            return null;
        }
        
        // Not saved, or a new file based on a template
        if(fModel.getArchimateModel().getFile() == null) {
            return null;
        }
        
        /*
         * If the user creates a new diagram (in a saved model) and that diagram is open when the user
         * closes the application without saving the model again, then Eclipse tries to restore it again next time.
         * So we don't persist the state of the diagram if the diagram view is not marked as saved.
         */
        if(fModel.getAdapter(IEditorModelManager.ADAPTER_PROPERTY_MODEL_SAVED) == null) {
            return null;
        }
        
        // OK, we'll handle it
        return this;
    }

    @Override
    public String getFactoryId() {
        return DiagramEditorInputFactory.ID_FACTORY;
    }

    @Override
    public void saveState(IMemento memento) {
        DiagramEditorInputFactory.saveState(memento, this);
    }
    
    public void dispose() {
        fModel = null;
    }
}
