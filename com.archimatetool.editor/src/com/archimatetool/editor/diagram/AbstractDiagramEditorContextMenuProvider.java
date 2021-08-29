/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.actions.ActionFactory;

import com.archimatetool.editor.actions.ArchiActionFactory;
import com.archimatetool.editor.diagram.actions.BringForwardAction;
import com.archimatetool.editor.diagram.actions.BringToFrontAction;
import com.archimatetool.editor.diagram.actions.ConnectionRouterAction;
import com.archimatetool.editor.diagram.actions.DefaultEditPartSizeAction;
import com.archimatetool.editor.diagram.actions.ExportAsImageAction;
import com.archimatetool.editor.diagram.actions.ExportAsImageToClipboardAction;
import com.archimatetool.editor.diagram.actions.LockObjectAction;
import com.archimatetool.editor.diagram.actions.ResetAspectRatioAction;
import com.archimatetool.editor.diagram.actions.SendBackwardAction;
import com.archimatetool.editor.diagram.actions.SendToBackAction;


/**
 * Provides a context menu.
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractDiagramEditorContextMenuProvider extends ContextMenuProvider {
    
    public static final String GROUP_UNDO = "group_undo"; //$NON-NLS-1$
    public static final String GROUP_EDIT = "group_edit"; //$NON-NLS-1$
    public static final String GROUP_RENAME = "group_rename"; //$NON-NLS-1$
    public static final String GROUP_EXPORT = "group_export"; //$NON-NLS-1$
    public static final String GROUP_ORDER = "group_order"; //$NON-NLS-1$
    public static final String GROUP_POSITION = "group_position"; //$NON-NLS-1$
    public static final String GROUP_CONNECTIONS = "group_connections"; //$NON-NLS-1$
    public static final String GROUP_PROPERTIES = "group_properties"; //$NON-NLS-1$

    protected ActionRegistry actionRegistry;

    /**
     * Creates a new ContextMenuProvider assoicated with the given viewer
     * and action registry.
     * 
     * @param viewer the viewer
     * @param registry the action registry
     */
    public AbstractDiagramEditorContextMenuProvider(EditPartViewer viewer, ActionRegistry registry) {
        super(viewer);
        actionRegistry = registry;
    }

    /**
     * @see ContextMenuProvider#buildContextMenu(org.eclipse.jface.action.IMenuManager)
     */
    @Override
    public void buildContextMenu(IMenuManager menu) {
        IAction action;
        
        menu.add(new Separator(GROUP_UNDO));

        action = actionRegistry.getAction(ActionFactory.UNDO.getId());
        menu.appendToGroup(GROUP_UNDO, action);

        action = actionRegistry.getAction(ActionFactory.REDO.getId());
        menu.appendToGroup(GROUP_UNDO, action);

        menu.add(new Separator(GROUP_EDIT));
        
        action = actionRegistry.getAction(ActionFactory.CUT.getId());
        menu.appendToGroup(GROUP_EDIT, action);
        
        action = actionRegistry.getAction(ActionFactory.COPY.getId());
        menu.appendToGroup(GROUP_EDIT, action);
        
        action = actionRegistry.getAction(ExportAsImageToClipboardAction.ID);
        menu.appendToGroup(GROUP_EDIT, action);

        action = actionRegistry.getAction(ActionFactory.PASTE.getId());
        menu.appendToGroup(GROUP_EDIT, action);
        
        action = actionRegistry.getAction(ArchiActionFactory.PASTE_SPECIAL.getId());
        menu.appendToGroup(GROUP_EDIT, action);
        
        action = actionRegistry.getAction(ActionFactory.DELETE.getId());
        menu.appendToGroup(GROUP_EDIT, action);
        
        action = actionRegistry.getAction(LockObjectAction.ID);
        if(action.isEnabled()) {
            menu.appendToGroup(GROUP_EDIT, new Separator());
            menu.appendToGroup(GROUP_EDIT, action);
        }
        
        menu.add(new Separator(GROUP_RENAME));
        
        action = actionRegistry.getAction(ActionFactory.RENAME.getId());
        menu.appendToGroup(GROUP_RENAME, action);

        menu.add(new Separator(GROUP_EXPORT));
        IMenuManager exportMenu = new MenuManager(Messages.AbstractDiagramEditorContextMenuProvider_0, "menu_export"); //$NON-NLS-1$
        menu.add(exportMenu);
        exportMenu.add(actionRegistry.getAction(ExportAsImageAction.ID));
        
        menu.add(new Separator(GROUP_ORDER));
        IMenuManager orderMenu = new MenuManager(Messages.AbstractDiagramEditorContextMenuProvider_1, "menu_order"); //$NON-NLS-1$
        menu.add(orderMenu);
        orderMenu.add(actionRegistry.getAction(BringToFrontAction.ID));
        orderMenu.add(actionRegistry.getAction(BringForwardAction.ID));
        orderMenu.add(actionRegistry.getAction(SendToBackAction.ID));
        orderMenu.add(actionRegistry.getAction(SendBackwardAction.ID));
        
        menu.add(new GroupMarker(GROUP_POSITION));
        IMenuManager alignmentMenu = new MenuManager(Messages.AbstractDiagramEditorContextMenuProvider_2, "menu_position"); //$NON-NLS-1$
        menu.add(alignmentMenu);
        
        alignmentMenu.add(actionRegistry.getAction(GEFActionConstants.ALIGN_LEFT));
        alignmentMenu.add(actionRegistry.getAction(GEFActionConstants.ALIGN_CENTER));
        alignmentMenu.add(actionRegistry.getAction(GEFActionConstants.ALIGN_RIGHT));
        
        alignmentMenu.add(new Separator());
        
        alignmentMenu.add(actionRegistry.getAction(GEFActionConstants.ALIGN_TOP));
        alignmentMenu.add(actionRegistry.getAction(GEFActionConstants.ALIGN_MIDDLE));
        alignmentMenu.add(actionRegistry.getAction(GEFActionConstants.ALIGN_BOTTOM));
        
        alignmentMenu.add(new Separator());

        alignmentMenu.add(actionRegistry.getAction(GEFActionConstants.MATCH_WIDTH));
        alignmentMenu.add(actionRegistry.getAction(GEFActionConstants.MATCH_HEIGHT));
        alignmentMenu.add(actionRegistry.getAction(GEFActionConstants.MATCH_SIZE));
        
        alignmentMenu.add(new Separator());
        alignmentMenu.add(actionRegistry.getAction(DefaultEditPartSizeAction.ID));
        alignmentMenu.add(actionRegistry.getAction(ResetAspectRatioAction.ID));
        
        menu.add(new Separator(GROUP_CONNECTIONS));
        IMenuManager connectionMenu = new MenuManager(Messages.AbstractDiagramEditorContextMenuProvider_3, "menu_connection_router"); //$NON-NLS-1$
        menu.appendToGroup(GROUP_CONNECTIONS, connectionMenu);
        connectionMenu.add(actionRegistry.getAction(ConnectionRouterAction.BendPointConnectionRouterAction.ID));
// Doesn't work with Connection to Connection
//      connectionMenu.add(actionRegistry.getAction(ConnectionRouterAction.ShortestPathConnectionRouterAction.ID));
        connectionMenu.add(actionRegistry.getAction(ConnectionRouterAction.ManhattanConnectionRouterAction.ID));
        
        menu.add(new Separator(GROUP_PROPERTIES));
        action = actionRegistry.getAction(ActionFactory.PROPERTIES.getId());
        menu.add(action);
    }
}