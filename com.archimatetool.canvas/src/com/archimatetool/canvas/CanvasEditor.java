/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas;

import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.gef.AutoexposeHelper;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.help.HelpSystem;
import org.eclipse.help.IContext;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.canvas.dnd.CanvasDiagramTransferDropTargetListener;
import com.archimatetool.canvas.dnd.FileTransferDropTargetListener;
import com.archimatetool.canvas.editparts.CanvasModelEditPartFactory;
import com.archimatetool.editor.diagram.AbstractDiagramEditor;
import com.archimatetool.editor.diagram.DiagramEditorFindReplaceProvider;
import com.archimatetool.editor.diagram.actions.FindReplaceAction;
import com.archimatetool.editor.diagram.util.ExtendedViewportAutoexposeHelper;
import com.archimatetool.editor.ui.findreplace.IFindReplaceProvider;



/**
 * Canvas Editor
 * 
 * @author Phillip Beauvoir
 */
public class CanvasEditor extends AbstractDiagramEditor
implements ICanvasEditor {
    
    /**
     * Palette
     */
    private CanvasEditorPalette fPalette;
    
    /**
     * Find/Replace Provider
     */
    private DiagramEditorFindReplaceProvider fFindReplaceProvider;

    @Override
    public void doCreatePartControl(Composite parent) {
        // Register Help Context
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    @Override
    public PaletteRoot getPaletteRoot() {
        if(fPalette == null) {
            fPalette = new CanvasEditorPalette();
        }
        return fPalette;
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
    }
    
    @Override
    protected void createRootEditPart(GraphicalViewer viewer) {
        /*
         * We'll have a Zoom Manager using ScalableFreeformRootEditPart
         */
        RootEditPart rootPart = new ScalableFreeformRootEditPart() {
            @SuppressWarnings("rawtypes")
            @Override
            public Object getAdapter(Class adapter) {
                if(adapter == AutoexposeHelper.class) {
                    return new ExtendedViewportAutoexposeHelper(this, new Insets(50), false);
                }
                return super.getAdapter(adapter);
            }

        };
        
        viewer.setRootEditPart(rootPart);
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
    
    @Override
    protected void createActions(GraphicalViewer viewer) {
        super.createActions(viewer);
        
        ActionRegistry registry = getActionRegistry();

        // Find/Replace
        IAction action = new FindReplaceAction(getEditorSite().getWorkbenchWindow());
        registry.registerAction(action);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Object getAdapter(Class adapter) {
        // Find/Replace Provider
        if(adapter == IFindReplaceProvider.class) {
            if(fFindReplaceProvider == null) {
                fFindReplaceProvider = new DiagramEditorFindReplaceProvider(getGraphicalViewer());
            }
            return fFindReplaceProvider;
        }

        return super.getAdapter(adapter);
    }

    @Override
    public void dispose() {
        super.dispose();
        if(fPalette != null) {
            fPalette.dispose();
        }
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
