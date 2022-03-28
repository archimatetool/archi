/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.help.HelpSystem;
import org.eclipse.help.IContext;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.canvas.dnd.CanvasDiagramTransferDropTargetListener;
import com.archimatetool.canvas.dnd.FileTransferDropTargetListener;
import com.archimatetool.canvas.dnd.URLTransferDropTargetListener;
import com.archimatetool.canvas.editparts.CanvasModelEditPartFactory;
import com.archimatetool.editor.diagram.AbstractDiagramEditor;



/**
 * Canvas Editor
 * 
 * @author Phillip Beauvoir
 */
public class CanvasEditor extends AbstractDiagramEditor
implements ICanvasEditor {
    
    @Override
    public void doCreatePartControl(Composite parent) {
        // Register Help Context
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    @Override
    public CanvasEditorPalette getPaletteRoot() {
        if(fPaletteRoot == null) {
            fPaletteRoot = new CanvasEditorPalette();
        }
        return (CanvasEditorPalette)fPaletteRoot;
    }

    @Override
    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();

        GraphicalViewer viewer = getGraphicalViewer();
        
        // Register Edit Part Factory before setting contents
        viewer.setEditPartFactory(new CanvasModelEditPartFactory());

        // Set Model
        viewer.setContents(getModel());
        
        // Native DnD
        viewer.addDropTargetListener(new CanvasDiagramTransferDropTargetListener(viewer));

        // File DnD
        viewer.addDropTargetListener(new FileTransferDropTargetListener(viewer));
        
        // URL DnD
        viewer.addDropTargetListener(new URLTransferDropTargetListener(viewer));
    }
    
    /**
     * Set up and register the context menu
     */
    @Override
    protected void registerContextMenu(GraphicalViewer viewer) {
        MenuManager provider = new CanvasEditorContextMenuProvider(viewer, getActionRegistry());
        viewer.setContextMenu(provider);
        getSite().registerContextMenu(CanvasEditorContextMenuProvider.ID, provider, viewer);
    }

    // =================================================================================
    //                       Contextual Help support
    // =================================================================================
    
    @Override
    public int getContextChangeMask() {
        return NONE;
    }

    @Override
    public IContext getContext(Object target) {
        return HelpSystem.getContext(HELP_ID);
    }

    @Override
    public String getSearchExpression(Object target) {
        return Messages.CanvasEditor_0;
    }
}
