/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.sketch;

import org.eclipse.draw2d.ScalableFreeformLayeredPane;
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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.diagram.AbstractDiagramEditor;
import com.archimatetool.editor.diagram.DiagramEditorFindReplaceProvider;
import com.archimatetool.editor.diagram.actions.FindReplaceAction;
import com.archimatetool.editor.diagram.sketch.dnd.SketchDiagramTransferDropTargetListener;
import com.archimatetool.editor.diagram.sketch.editparts.SketchEditPartFactory;
import com.archimatetool.editor.diagram.util.ExtendedViewportAutoexposeHelper;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.findreplace.IFindReplaceProvider;
import com.archimatetool.model.ISketchModel;



/**
 * Sketch Editor
 * 
 * @author Phillip Beauvoir
 */
public class SketchEditor extends AbstractDiagramEditor
implements ISketchEditor {
    
    /**
     * Palette
     */
    private SketchEditorPalette fPalette;
    
    private ScalableFreeformLayeredPane fScalableFreeformLayeredPane;
    private BackgroundImageLayer fBackgroundImageLayer;
    
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
            fPalette = new SketchEditorPalette();
        }
        return fPalette;
    }
    
    @Override
    public ISketchModel getModel() {
        return (ISketchModel)super.getModel();
    }
    
    @Override
    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();

        GraphicalViewer viewer = getGraphicalViewer();
        
        // Register Edit Part Factory before setting contents
        viewer.setEditPartFactory(new SketchEditPartFactory());

        // Set Model
        viewer.setContents(getModel());
        
        // Native DnD
        viewer.addDropTargetListener(new SketchDiagramTransferDropTargetListener(viewer));
    }
    
    @Override
    protected void createRootEditPart(GraphicalViewer viewer) {
        /*
         * We'll have a Zoom Manager and a background image
         */
        RootEditPart rootPart = new ScalableFreeformRootEditPart() {
            @Override
            protected ScalableFreeformLayeredPane createScaledLayers() {
                // Insert Background Image behind Grid
                // Note - background image is not on a Printable Layer, so won't print!
                fScalableFreeformLayeredPane = super.createScaledLayers();
                updateBackgroundImage();
                return fScalableFreeformLayeredPane;
            }

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
    
    @Override
    public void updateBackgroundImage() {
        ISketchModel model = getModel();
        
        if(fBackgroundImageLayer == null) {
            fBackgroundImageLayer = new BackgroundImageLayer();
            fScalableFreeformLayeredPane.add(fBackgroundImageLayer, BackgroundImageLayer.NAME, 0);
        }
        
        switch(model.getBackground()) {
            case 0:
                fBackgroundImageLayer.setImage(null);
                break;

            case 1:
                Image img = IArchiImages.ImageFactory.getImage(IArchiImages.BROWN_PAPER_BACKGROUND);
                fBackgroundImageLayer.setImage(img);
                break;
                
            case 2:
                img = IArchiImages.ImageFactory.getImage(IArchiImages.CORK_BACKGROUND);
                fBackgroundImageLayer.setImage(img);
                break;
                
            default:
                break;
        }
    }
    
    /**
     * Set up and register the context menu
     */
    @Override
    protected void registerContextMenu(GraphicalViewer viewer) {
        MenuManager provider = new SketchEditorContextMenuProvider(viewer, getActionRegistry());
        viewer.setContextMenu(provider);
        getSite().registerContextMenu(SketchEditorContextMenuProvider.ID, provider, viewer);
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
        return Messages.SketchEditor_0;
    }
}
