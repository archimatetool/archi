/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.actions.RetargetAction;

import com.archimatetool.editor.actions.ArchiActionFactory;
import com.archimatetool.editor.diagram.actions.DeleteFromModelAction;
import com.archimatetool.editor.model.viewpoints.IViewpoint;
import com.archimatetool.editor.model.viewpoints.ViewpointsManager;



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
        for(IViewpoint viewPoint : ViewpointsManager.INSTANCE.getAllViewpoints()) {
            retargetAction = new RetargetAction(viewPoint.getClass().toString(), viewPoint.getName(), IAction.AS_RADIO_BUTTON);
            // Looks better as a checkbox
            //retargetAction.setImageDescriptor(ViewpointsManager.INSTANCE.getImageDescriptor(viewPoint));
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
        
        editMenu.insertAfter(ArchiActionFactory.DELETE.getId(), new Separator(editDeleteMenuGroup));
        editMenu.appendToGroup(editDeleteMenuGroup, getAction(DeleteFromModelAction.ID));
        
        return editMenu;
    }
    
    @Override
    protected IMenuManager createViewMenu(IMenuManager menuManager) {
        IMenuManager viewMenu = super.createViewMenu(menuManager);
        
        // Viewpoints
        IMenuManager viewPointMenu = new MenuManager(Messages.ArchimateDiagramEditorActionBarContributor_0);
        viewMenu.add(viewPointMenu);
        for(IViewpoint viewPoint : ViewpointsManager.INSTANCE.getAllViewpoints()) {
            viewPointMenu.add(getAction(viewPoint.getClass().toString()));
        }

        return viewMenu;
    }
}
