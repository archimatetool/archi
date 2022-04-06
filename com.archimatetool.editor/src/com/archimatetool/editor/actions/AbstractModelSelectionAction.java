/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.archimatetool.editor.actions.ModelSelectionHandler.IModelSelectionHandlerListener;
import com.archimatetool.model.IArchimateModel;


/**
 * Global Action for current selection in either the Tree Model View or an Editor
 * in order to update toolbar/menu item state and get current model
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractModelSelectionAction extends Action implements IWorkbenchAction, IModelSelectionHandlerListener {
    
    /**
     * The Model Selection Handler
     */
    private ModelSelectionHandler fModelSelectionHandler;
    
    /**
     * The workbench window this action is registered with.
     */
    protected IWorkbenchWindow workbenchWindow;
    
    /**
     * Default Constructor
     * @param text
     * @param window
     */
    protected AbstractModelSelectionAction(String text, IWorkbenchWindow window) {
        super(text);
        setWorkbenchWindow(window);
        setEnabled(false);
    }
    
    /**
     * Set the WorkbenchWindow and register this Action with the Part Service Part Listener
     * @param window
     */
    public void setWorkbenchWindow(IWorkbenchWindow window) {
        workbenchWindow = window;
        fModelSelectionHandler = new ModelSelectionHandler(this, window);
    }
    
    @Override
    public void updateState() {
        setEnabled(getActiveArchimateModel() != null);
    }
    
    /**
     * @return The Active Archimate Model
     */
    protected IArchimateModel getActiveArchimateModel() {
        return fModelSelectionHandler.getActiveArchimateModel();
    }

    @Override
    public void dispose() {
        fModelSelectionHandler.dispose();
    }
}