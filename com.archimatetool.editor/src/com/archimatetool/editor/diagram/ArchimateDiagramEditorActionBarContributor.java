/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.actions.RetargetAction;

import com.archimatetool.editor.actions.ArchiActionFactory;
import com.archimatetool.editor.diagram.actions.DeleteFromModelAction;
import com.archimatetool.editor.diagram.actions.TextPositionAction;
import com.archimatetool.model.viewpoints.IViewpoint;
import com.archimatetool.model.viewpoints.ViewpointManager;



/**
 * Action Bar contributor for Archimate Diagram Editor
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateDiagramEditorActionBarContributor
extends AbstractDiagramEditorActionBarContributor {
    
    protected String editDeleteMenuGroup = "editDeleteMenuGroup"; //$NON-NLS-1$

    @Override
    protected void buildActions() {
        super.buildActions();
        
        // Delete From Model
        RetargetAction retargetAction = new RetargetAction(DeleteFromModelAction.ID, DeleteFromModelAction.TEXT);
        addRetargetAction(retargetAction);
        
        // Viewpoints
        for(IViewpoint viewPoint : ViewpointManager.INSTANCE.getAllViewpoints()) {
            retargetAction = new RetargetAction(viewPoint.toString(), viewPoint.getName(), IAction.AS_RADIO_BUTTON);
            addRetargetAction(retargetAction);
        }
    }
    
    @Override
    protected void declareGlobalActionKeys() {
        super.declareGlobalActionKeys();
        
        // Generate View For
        addGlobalActionKey(ArchiActionFactory.GENERATE_VIEW.getId());
    }
    
    @Override
    protected IMenuManager contributeToEditMenu(IMenuManager menuManager) {
        IMenuManager editMenu = super.contributeToEditMenu(menuManager);
        
        // Text Positions
        IMenuManager textPositionMenu = new MenuManager(Messages.ArchimateDiagramEditorActionBarContributor_1);
        for(String id : TextPositionAction.ACTION_IDS) {
            textPositionMenu.add(getAction(id));
        }
        editMenu.appendToGroup(GROUP_EDIT_MENU, textPositionMenu);
        
        editMenu.insertAfter(ArchiActionFactory.DELETE.getId(), new GroupMarker(editDeleteMenuGroup));
        editMenu.appendToGroup(editDeleteMenuGroup, getAction(DeleteFromModelAction.ID));
        
        return editMenu;
    }
    
    @Override
    protected IMenuManager createViewMenu(IMenuManager menuManager) {
        IMenuManager viewMenu = super.createViewMenu(menuManager);
        
        // Viewpoints
        IMenuManager viewPointMenu = new MenuManager(Messages.ArchimateDiagramEditorActionBarContributor_0);
        viewMenu.add(viewPointMenu);
        for(IViewpoint viewPoint : ViewpointManager.INSTANCE.getAllViewpoints()) {
            viewPointMenu.add(getAction(viewPoint.toString()));
        }

        return viewMenu;
    }
}
