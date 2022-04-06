/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.actions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.model.IArchimateModel;


/**
 * Abstract Handler for current model and selection enablement
 * This is the AbstractHandler counterpart of AbstractModelAction
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractModelHandler extends AbstractHandler {
    
    /**
     * @return The active Archimate Model
     */
    protected IArchimateModel getActiveArchimateModel() {
        IWorkbenchPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getPartService().getActivePart();
        return part != null ? part.getAdapter(IArchimateModel.class) : null;
    }
    
    @Override
    public boolean isEnabled() {
        return getActiveArchimateModel() != null;
    }
}