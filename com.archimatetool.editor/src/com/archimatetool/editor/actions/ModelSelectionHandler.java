/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.actions;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;

import com.archimatetool.editor.views.IModelSelectionView;
import com.archimatetool.model.IArchimateModel;


/**
 * Handler for tracking the current model in Workbench Parts.
 * This is a delegate class so that menu Actions and Handlers can set their enabled state and also
 * retrieve the currently selected model.
 * 
 * @author Phillip Beauvoir
 */
public class ModelSelectionHandler implements IPartListener, ISelectionChangedListener {
    
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
     * Active Editor or Models Tree
     */
    private IWorkbenchPart fActivePart;
    
    /**
     * ModelSelectionHandler Listener that will be notified of updates
     */
    private IModelSelectionHandlerListener fListener;
    
    public ModelSelectionHandler(IModelSelectionHandlerListener listener, IWorkbenchWindow workbenchWindow) {
        fListener = listener;
        fWorkbenchWindow = workbenchWindow;
        fWorkbenchWindow.getPartService().addPartListener(this);
    }
    
    /**
     * Refresh the state based on the current active part in the workbench
     */
    public void refresh() {
        setActivePart(fWorkbenchWindow.getPartService().getActivePart());
    }

    /**
     * @return The Active Archimate Model
     */
    public IArchimateModel getActiveArchimateModel() {
        return fActivePart != null ? fActivePart.getAdapter(IArchimateModel.class) : null;
    }

    public void dispose() {
        fWorkbenchWindow.getPartService().removePartListener(this);
    }
    
    // ------------------ Part and Selection Listeners --------------------------

    @Override
    public void partActivated(IWorkbenchPart part) {
        setActivePart(part);
    }
    
    @Override
    public void partDeactivated(IWorkbenchPart part) {
        if(part instanceof IModelSelectionView) {
            ((IModelSelectionView)part).getSelectionProvider().removeSelectionChangedListener(this);
        }
    }
    
    @Override
    public void partClosed(IWorkbenchPart part) {
        // If the active part is closed we need to refresh with the new active part
        if(part == fActivePart) {
            refresh();
        }
    }

    @Override
    public void partOpened(IWorkbenchPart part) {
    }

    @Override
    public void partBroughtToTop(IWorkbenchPart part) {
    }
    
    private void setActivePart(IWorkbenchPart part) {
        // Listen to selections in IModelSelectionView because these can return different models
        if(part instanceof IModelSelectionView) {
            ((IModelSelectionView)part).getSelectionProvider().addSelectionChangedListener(this);
        }
        
        fActivePart = part;
        fListener.updateState();
    }
    
    /*
     * Listen to Tree Selections to update state
     */
    @Override
    public void selectionChanged(SelectionChangedEvent event) {
        fListener.updateState();
    }
}