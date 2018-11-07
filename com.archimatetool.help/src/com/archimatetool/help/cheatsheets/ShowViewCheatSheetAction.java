/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.help.cheatsheets;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.cheatsheets.ICheatSheetAction;
import org.eclipse.ui.cheatsheets.ICheatSheetManager;

import com.archimatetool.editor.ui.services.ViewManager;



/**
 * Action to programmatically show a View from a Cheat Sheet
 * 
 * @author Phillip Beauvoir
 */
public class ShowViewCheatSheetAction
extends Action
implements ICheatSheetAction {
    
    @Override
    public void run(String[] params, ICheatSheetManager manager) {
        if(params != null && params.length > 0) {
            ViewManager.showViewPart(params[0], true);
        }
    }
}
