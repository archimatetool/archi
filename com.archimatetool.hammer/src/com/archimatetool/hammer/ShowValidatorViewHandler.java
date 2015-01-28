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

    public Object execute(ExecutionEvent event) throws ExecutionException {
        ViewManager.toggleViewPart(IValidatorView.ID, true);
        return null;
    }


}
