/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.help.hints;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.internal.cheatsheets.actions.CheatSheetCategoryBasedSelectionAction;


/**
 * Command Action Handler to show Cheat Sheet
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("restriction")
public class ShowCheatSheetsHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        new CheatSheetCategoryBasedSelectionAction().run();
        return null;
    }

}
