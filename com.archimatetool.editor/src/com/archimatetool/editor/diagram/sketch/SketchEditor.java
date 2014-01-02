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
import org.eclipse.help.HelpSystem;
import org.eclipse.help.IContext;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.diagram.AbstractDiagramEditor;
import com.archimatetool.editor.diagram.sketch.dnd.SketchDiagramTransferDropTargetListener;
import com.archimatetool.editor.diagram.sketch.editparts.SketchEditPartFactory;
import com.archimatetool.editor.diagram.util.ExtendedViewportAutoexposeHelper;
import com.archimatetool.editor.ui.IArchimateImages;
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
                Image img = IArchimateImages.ImageFactory.getImage(IArchimateImages.BROWN_PAPER_BACKGROUND);
                fBackgroundImageLayer.setImage(img);
                break;
                
            case 2:
                img = IArchimateImages.ImageFactory.getImage(IArchimateImages.CORK_BACKGROUND);
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
    public void dispose() {
        super.dispose();
        if(fPalette != null) {
            fPalette.dispose();
        }
    }
    
    // =================================================================================
    //                       Contextual Help support
    // =================================================================================
    
    public int getContextChangeMask() {
        return NONE;
    }

    public IContext getContext(Object target) {
        return HelpSystem.getContext(HELP_ID);
    }

    public String getSearchExpression(Object target) {
        return Messages.SketchEditor_0;
    }
}
