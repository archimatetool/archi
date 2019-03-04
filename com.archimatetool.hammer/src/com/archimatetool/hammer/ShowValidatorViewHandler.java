/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.hammer;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.archimatetool.editor.ui.services.ViewManager;
import com.archimatetool.hammer.view.IValidatorView;


/**
 * Show Validator View
 * 
 * @author Phillip Beauvoir
 */
public class ShowValidatorViewHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        // activate = false to keep originating part in focus so we can update current selection
        ViewManager.toggleViewPart(IValidatorView.ID, false); 
        return null;
    }


}
