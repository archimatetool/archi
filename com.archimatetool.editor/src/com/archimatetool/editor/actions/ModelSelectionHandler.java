/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.actions;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;

import com.archimatetool.editor.diagram.IDiagramModelEditor;
import com.archimatetool.editor.views.IModelSelectionView;
import com.archimatetool.model.IArchimateModel;


/**
 * Handler for tracking the current selection and model in either the Tree Model Views or Archimate Editors.
 * This is a delegate class so that menu Actions and Handlers can set their enabled state and also
 * retrieve the currently selected model in the application<br>
 * 
 * @author Phillip Beauvoir
 */
public class ModelSelectionHandler implements IPartListener {
    
    /**
     * Interface for clients to be updated when the enabled state needs to be queried
     */
    public static interface IModelSelectionHandlerListener {
        public void updateState();
    }
    
    /**
     * The workbench window this is registered with.
     */
    private IWorkbenchWindow fWorkbenchWindow;
    
    /**
     * Active View
     */
    private IModelSelectionView fActiveModelView;
    
    /**
     * Active Editor
     */
    private IEditorPart fActiveEditor;
    
    /**
     * ModelSelectionHandler Listener
     */
    private IModelSelectionHandlerListener fListener;
    
    /*
     * Listen to Tree Selections to update state
     */
    protected ISelectionChangedListener selectionListener = new ISelectionChangedListener() {
        public void selectionChanged(SelectionChangedEvent event) {
            fListener.updateState();
        }
    };
    
    public ModelSelectionHandler(IModelSelectionHandlerListener listener, IWorkbenchWindow workbenchWindow) {
        fListener = listener;
        fWorkbenchWindow = workbenchWindow;
        fWorkbenchWindow.getPartService().addPartListener(this);
    }
    
    /**
     * Refresh the state based on the current selection in the workbench
     */
    public void refresh() {
        partActivated(fWorkbenchWindow.getPartService().getActivePart());
    }

    // ------------------ Part Listener --------------------------

    public void partOpened(IWorkbenchPart part) {
    }

    public void partClosed(IWorkbenchPart part) {
        if(part instanceof IModelSelectionView) {
            fActiveModelView = null;
            fListener.updateState();
        }
        else if(part instanceof IDiagramModelEditor) {
            if(fWorkbenchWindow.getActivePage() != null) {
                fActiveEditor = fWorkbenchWindow.getActivePage().getActiveEditor();
                fListener.updateState();
            }
        }
    }

    public void partActivated(IWorkbenchPart part) {
        if(part instanceof IModelSelectionView) {
            fActiveModelView = (IModelSelectionView)part;
            fActiveEditor = null;
            ((IModelSelectionView)part).getSelectionProvider().addSelectionChangedListener(selectionListener);
        }
        else if(part instanceof IDiagramModelEditor) {
            fActiveEditor = (IEditorPart)part;
        }
        fListener.updateState();
    }
    
    public void partDeactivated(IWorkbenchPart part) {
        if(part instanceof IModelSelectionView) {
            ((IModelSelectionView)part).getSelectionProvider().removeSelectionChangedListener(selectionListener);
        }
    }
    
    public void partBroughtToTop(IWorkbenchPart part) {
    }
    
    // -----------------------------------------------------------

    /**
     * @return The Active Archimate Model
     */
    public IArchimateModel getActiveArchimateModel() {
        IArchimateModel model = null;
        
        // Active Editor first
        if(fActiveEditor != null) {
            model = (IArchimateModel)fActiveEditor.getAdapter(IArchimateModel.class);
        }
        // Then Active View
        else if(fActiveModelView != null) {
            model = (IArchimateModel)fActiveModelView.getAdapter(IArchimateModel.class);
        }
        
        return model;
    }

    public void dispose() {
        fWorkbenchWindow.getPartService().removePartListener(this);
    }
}