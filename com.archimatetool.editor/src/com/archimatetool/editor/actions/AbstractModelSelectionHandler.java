/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.actions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.actions.ModelSelectionHandler.IModelSelectionHandlerListener;
import com.archimatetool.model.IArchimateModel;


/**
 * Global Menu Handler for current selection in either the Tree Model View or an Editor.
 * This is the AbstractHandler counterpart of AbstractModelSelectionAction<br>
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractModelSelectionHandler extends AbstractHandler implements IModelSelectionHandlerListener {
    
    /**
     * The Model Selection Handler
     */
    private ModelSelectionHandler fModelSelectionHandler;
    
    /**
     * The workbench window this action is registered with.
     */
    protected IWorkbenchWindow workbenchWindow;
    
    protected AbstractModelSelectionHandler() {
        workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        
        fModelSelectionHandler = new ModelSelectionHandler(this, workbenchWindow);
        
        // Update enabled state on current active part (if any)
        fModelSelectionHandler.refresh();
    }

    @Override
    public void updateState() {
        setBaseEnabled(getActiveArchimateModel() != null);
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