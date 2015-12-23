/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;

import com.archimatetool.editor.actions.ArchimateEditorActionFactory;
import com.archimatetool.editor.diagram.AbstractDiagramEditorActionBarContributor;
import com.archimatetool.editor.diagram.actions.BorderColorAction;
import com.archimatetool.editor.diagram.actions.LockObjectAction;
import com.archimatetool.editor.diagram.actions.ResetAspectRatioAction;
import com.archimatetool.editor.diagram.actions.TextPositionAction;



/**
 * Action Bar contributor for Canvas Editor
 * 
 * @author Phillip Beauvoir
 */
public class CanvasEditorActionBarContributor
extends AbstractDiagramEditorActionBarContributor {

    @Override
    protected IMenuManager contributeToEditMenu(IMenuManager menuManager) {
        IMenuManager editMenu = super.contributeToEditMenu(menuManager);
        
        // Text Positions
        IMenuManager textPositionMenu = new MenuManager(Messages.CanvasEditorActionBarContributor_0);
        for(String id : TextPositionAction.ACTION_IDS) {
            textPositionMenu.add(getAction(id));
        }
        editMenu.appendToGroup(GROUP_EDIT_MENU, textPositionMenu);
        
        // Border Color
        editMenu.appendToGroup(GROUP_EDIT_MENU, getAction(BorderColorAction.ID));
        
        // Lock
        editMenu.insertAfter(ArchimateEditorActionFactory.RENAME.getId(), new Separator("lock")); //$NON-NLS-1$
        editMenu.appendToGroup("lock", getAction(LockObjectAction.ID)); //$NON-NLS-1$
        
        return editMenu;
    }
    
    @Override
    protected IMenuManager createViewMenu(IMenuManager menuManager) {
        IMenuManager viewMenu = super.createViewMenu(menuManager);
        
        IMenuManager subMenu = viewMenu.findMenuUsingPath("menu_position"); //$NON-NLS-1$
        subMenu.add(getAction(ResetAspectRatioAction.ID));
        
        return viewMenu;
    }
    
    @Override
    public void contributeToToolBar(IToolBarManager toolBarManager) {
        super.contributeToToolBar(toolBarManager);
        toolBarManager.appendToGroup(GROUP_TOOLBAR_END, getAction(ResetAspectRatioAction.ID));
    }
}
