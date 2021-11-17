/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.hammer;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.ui.services.ViewManager;
import com.archimatetool.hammer.view.IValidatorView;


/**
 * Validate
 * 
 * @author Phillip Beauvoir
 */
public class ValidateHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IValidatorView view = (IValidatorView)page.findView(IValidatorView.ID);
        if(view != null) {
            if(!page.isPartVisible(view)) {
                ViewManager.showViewPart(IValidatorView.ID, true);
            }
            view.validateModel();
        }
        else {
            ViewManager.showViewPart(IValidatorView.ID, false);
        }
        
        return null;
    }

}
