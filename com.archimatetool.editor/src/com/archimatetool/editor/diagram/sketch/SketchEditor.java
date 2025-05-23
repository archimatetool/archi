/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.sketch;

import org.eclipse.draw2d.Layer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.help.HelpSystem;
import org.eclipse.help.IContext;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.diagram.AbstractDiagramEditor;
import com.archimatetool.editor.diagram.sketch.dnd.SketchDiagramTransferDropTargetListener;
import com.archimatetool.editor.diagram.sketch.editparts.SketchEditPartFactory;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.model.ISketchModel;



/**
 * Sketch Editor
 * 
 * @author Phillip Beauvoir
 */
public class SketchEditor extends AbstractDiagramEditor
implements ISketchEditor {
    
    private BackgroundImageLayer fBackgroundImageLayer;
    
    @Override
    public void doCreatePartControl(Composite parent) {
        // Register Help Context
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    @Override
    public SketchEditorPalette getPaletteRoot() {
        if(fPaletteRoot == null) {
            fPaletteRoot = new SketchEditorPalette();
        }
        return (SketchEditorPalette)fPaletteRoot;
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
        super.createRootEditPart(viewer);
        
        // Insert the background image layer behind all other layers
        ScalableFreeformRootEditPart rootPart = (ScalableFreeformRootEditPart)viewer.getRootEditPart();
        Layer layer = (Layer)rootPart.getLayer(LayerConstants.SCALABLE_LAYERS);
        fBackgroundImageLayer = new BackgroundImageLayer();
        layer.add(fBackgroundImageLayer, BackgroundImageLayer.NAME, 0);
        updateBackgroundImage();
    }
    
    @Override
    public void updateBackgroundImage() {
        switch(getModel().getBackground()) {
            case 0 -> {
                fBackgroundImageLayer.setImage(null);
            }

            case 1 -> {
                fBackgroundImageLayer.setImage(IArchiImages.ImageFactory.getImage(IArchiImages.BROWN_PAPER_BACKGROUND));
            }
            
            case 2 -> {
                fBackgroundImageLayer.setImage(IArchiImages.ImageFactory.getImage(IArchiImages.CORK_BACKGROUND));
            }
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
