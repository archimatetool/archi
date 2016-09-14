/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;

import com.archimatetool.editor.actions.ArchiActionFactory;
import com.archimatetool.editor.diagram.actions.DeleteFromModelAction;
import com.archimatetool.editor.diagram.actions.SelectElementInTreeAction;
import com.archimatetool.model.viewpoints.IViewpoint;
import com.archimatetool.model.viewpoints.ViewpointManager;


/**
 * Provides a context menu.
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateDiagramEditorContextMenuProvider extends AbstractDiagramEditorContextMenuProvider {

    public static final String ID = "ArchimateDiagramEditorContextMenuProvider"; //$NON-NLS-1$
    
    public static final String GROUP_VIEWPOINTS = "group_viewpoints"; //$NON-NLS-1$
    
    /**
     * Creates a new ContextMenuProvider assoicated with the given viewer
     * and action registry.
     * 
     * @param viewer the viewer
     * @param registry the action registry
     */
    public ArchimateDiagramEditorContextMenuProvider(EditPartViewer viewer, ActionRegistry registry) {
        super(viewer, registry);
    }

    /**
     * @see ContextMenuProvider#buildContextMenu(org.eclipse.jface.action.IMenuManager)
     */
    @Override
    public void buildContextMenu(IMenuManager menu) {
        super.buildContextMenu(menu);

        // Delete from Model
        menu.appendToGroup(GROUP_EDIT, actionRegistry.getAction(DeleteFromModelAction.ID));
        
        // Select Element in Tree
        menu.appendToGroup(GROUP_RENAME, new Separator());
        menu.appendToGroup(GROUP_RENAME, actionRegistry.getAction(SelectElementInTreeAction.ID));
 
        // Generate View For Element
        menu.appendToGroup(GROUP_RENAME, actionRegistry.getAction(ArchiActionFactory.GENERATE_VIEW.getId()));

        // Viewpoints
        menu.appendToGroup(GROUP_CONNECTIONS, new Separator(GROUP_VIEWPOINTS));
        IMenuManager viewPointMenu = new MenuManager(Messages.ArchimateDiagramEditorContextMenuProvider_0);
        menu.appendToGroup(GROUP_VIEWPOINTS, viewPointMenu);
        for(IViewpoint viewPoint : ViewpointManager.INSTANCE.getAllViewpoints()) {
            viewPointMenu.add(actionRegistry.getAction(viewPoint.toString()));
        }
    }
}