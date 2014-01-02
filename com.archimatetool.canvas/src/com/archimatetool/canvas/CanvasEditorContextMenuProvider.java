/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.jface.action.IMenuManager;

import com.archimatetool.editor.diagram.AbstractDiagramEditorContextMenuProvider;
import com.archimatetool.editor.diagram.actions.ResetAspectRatioAction;


/**
 * Provides a context menu.
 * 
 * @author Phillip Beauvoir
 */
public class CanvasEditorContextMenuProvider extends AbstractDiagramEditorContextMenuProvider {

    public static final String ID = "CanvasEditorContextMenuProvider"; //$NON-NLS-1$
    
    /**
     * Creates a new ContextMenuProvider associated with the given viewer
     * and action registry.
     * 
     * @param viewer the viewer
     * @param registry the action registry
     */
    public CanvasEditorContextMenuProvider(EditPartViewer viewer, ActionRegistry registry) {
        super(viewer, registry);
    }
    
    @Override
    public void buildContextMenu(IMenuManager menu) {
        super.buildContextMenu(menu);
        
        IMenuManager subMenu = menu.findMenuUsingPath("menu_position"); //$NON-NLS-1$
        subMenu.add(actionRegistry.getAction(ResetAspectRatioAction.ID));
    }
}