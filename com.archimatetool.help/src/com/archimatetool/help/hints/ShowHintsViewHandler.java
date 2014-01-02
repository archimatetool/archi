/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.help.hints;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.archimatetool.editor.ui.services.ViewManager;



/**
 * Command Action Handler to show Hints View
 * 
 * @author Phillip Beauvoir
 */
public class ShowHintsViewHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        ViewManager.toggleViewPart(IHintsView.ID, false);
        return null;
    }

}
