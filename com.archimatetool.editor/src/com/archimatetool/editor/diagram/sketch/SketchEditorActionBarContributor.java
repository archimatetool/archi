/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.sketch;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;

import com.archimatetool.editor.diagram.AbstractDiagramEditorActionBarContributor;
import com.archimatetool.editor.diagram.actions.TextPositionAction;


/**
 * Action Bar contributor for Sketch Editor
 * 
 * @author Phillip Beauvoir
 */
public class SketchEditorActionBarContributor
extends AbstractDiagramEditorActionBarContributor {

    @Override
    protected IMenuManager contributeToEditMenu(IMenuManager menuManager) {
        IMenuManager editMenu = super.contributeToEditMenu(menuManager);
        
        // Text Positions
        IMenuManager textPositionMenu = new MenuManager(Messages.SketchEditorActionBarContributor_0);
        for(String id : TextPositionAction.ACTION_IDS) {
            textPositionMenu.add(getAction(id));
        }
        editMenu.appendToGroup(GROUP_EDIT_MENU, textPositionMenu);
                
        return editMenu;
    }

}
