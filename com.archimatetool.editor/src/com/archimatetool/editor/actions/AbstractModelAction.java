/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.archimatetool.model.IArchimateModel;


/**
 * Abstract Action for current model selection.
 * 
 * This Action is suitable for menu items that update their enabled state when they are shown
 * but not suitable for toolbar items that don't actively update their state.
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractModelAction extends Action implements IWorkbenchAction {
    
    /**
     * The workbench window this action is registered with.
     */
    protected IWorkbenchWindow workbenchWindow;
    
    /**
     * Default Constructor
     * @param text
     * @param window
     */
    protected AbstractModelAction(String text, IWorkbenchWindow window) {
        super(text);
        workbenchWindow = window;
    }
    
    /**
     * @return The Active Archimate Model in the workbench
     */
    protected IArchimateModel getActiveArchimateModel() {
        IWorkbenchPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getPartService().getActivePart();
        return part != null ? part.getAdapter(IArchimateModel.class) : null;
    }

    @Override
    public boolean isEnabled() {
        return getActiveArchimateModel() != null;
    }
    
    @Override
    public void dispose() {
    }
}