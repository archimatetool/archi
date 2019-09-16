/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.AutoexposeHelper;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.help.HelpSystem;
import org.eclipse.help.IContext;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.diagram.actions.DeleteFromModelAction;
import com.archimatetool.editor.diagram.actions.FindReplaceAction;
import com.archimatetool.editor.diagram.actions.GenerateViewAction;
import com.archimatetool.editor.diagram.actions.ViewpointAction;
import com.archimatetool.editor.diagram.dnd.ArchimateDiagramTransferDropTargetListener;
import com.archimatetool.editor.diagram.editparts.ArchimateDiagramEditPartFactory;
import com.archimatetool.editor.diagram.util.ExtendedViewportAutoexposeHelper;
import com.archimatetool.editor.model.DiagramModelUtils;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.findreplace.IFindReplaceProvider;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.viewpoints.IViewpoint;
import com.archimatetool.model.viewpoints.ViewpointManager;



/**
 * Archimate Diagram Editor
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateDiagramEditor extends AbstractDiagramEditor
implements IArchimateDiagramEditor {
    
    /**
     * Palette
     */
    private ArchimateDiagramEditorPalette fPalette;
    
    /**
     * Find/Replace Provider
     */
    private DiagramEditorFindReplaceProvider fFindReplaceProvider;
    
    
    @Override
    protected void applicationPreferencesChanged(PropertyChangeEvent event) {
        // Hide/Show Palette elements on Viewpoint
        if(IPreferenceConstants.VIEWPOINTS_HIDE_PALETTE_ELEMENTS == event.getProperty()) {
            setPaletteViewpoint();
        }
        // Hide/Show Diagram Elements on Viewpoint
        else if(IPreferenceConstants.VIEWPOINTS_GHOST_DIAGRAM_ELEMENTS == event.getProperty()) {
            getGraphicalViewer().setContents(getModel()); // refresh the model contents
        }
        else {
            super.applicationPreferencesChanged(event);
        }
    }
    
    /**
     * Set Viewpoint to current Viewpoint in model
     */
    protected void setViewpoint() {
        setPaletteViewpoint();
        getGraphicalViewer().setContents(getModel()); // refresh the model contents
    }
    
    /**
     * Set Palette to current Viewpoint in model
     */
    protected void setPaletteViewpoint() {
        getPaletteRoot().setViewpoint(ViewpointManager.INSTANCE.getViewpoint(getModel().getViewpoint()));
    }
    
    @Override
    public void doCreatePartControl(Composite parent) {
        // Register Help Context
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    @Override
    public ArchimateDiagramEditorPalette getPaletteRoot() {
        if(fPalette == null) {
            fPalette = new ArchimateDiagramEditorPalette();
            setPaletteViewpoint();
        }
        return fPalette;
    }

    @Override
    public IArchimateDiagramModel getModel() {
        return (IArchimateDiagramModel)super.getModel();
    }
    
    @Override
    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();

        GraphicalViewer viewer = getGraphicalViewer();

        // Register the Edit Part Factory before setting model contents
        viewer.setEditPartFactory(new ArchimateDiagramEditPartFactory());
        
        // Set Model
        viewer.setContents(getModel());
        
        // Native DnD
        viewer.addDropTargetListener(new ArchimateDiagramTransferDropTargetListener(viewer));
    }
    
    @Override
    protected void configurePaletteViewer(PaletteViewer viewer) {
        super.configurePaletteViewer(viewer);
        
        // Help for Palette
        PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), PALETTE_HELP_ID);
    }
    
    @Override
    protected void createRootEditPart(GraphicalViewer viewer) {
        // We'll have a Zoom Manager for our Root Edit Part
        viewer.setRootEditPart(new ScalableFreeformRootEditPart() {
            @SuppressWarnings("rawtypes")
            @Override
            public Object getAdapter(Class adapter) {
                if(adapter == AutoexposeHelper.class) {
                    return new ExtendedViewportAutoexposeHelper(this, new Insets(50), false);
                }
                return super.getAdapter(adapter);
            }
        });
    }
    
    /**
     * Set up and register the context menu
     */
    @Override
    protected void registerContextMenu(GraphicalViewer viewer) {
        MenuManager provider = new ArchimateDiagramEditorContextMenuProvider(viewer, getActionRegistry());
        viewer.setContextMenu(provider);
        getSite().registerContextMenu(ArchimateDiagramEditorContextMenuProvider.ID, provider, viewer);
    }
    
    @Override
    @Deprecated
    public void selectArchimateConcepts(IArchimateConcept[] archimateConcepts) {
        selectObjects(archimateConcepts);
    }
    
    @Override
    public void selectObjects(Object[] objects) {
        Set<Object> selection = new HashSet<>();
        
        for(Object object : objects) {
            // If this is an Archimate concept replace it with diagram object instances
            if(object instanceof IArchimateConcept) {
                for(IDiagramModelComponent dc : DiagramModelUtils.findDiagramModelComponentsForArchimateConcept(getModel(), (IArchimateConcept)object)) {
                    selection.add(dc);
                }
            }
            // Else add it
            else {
                selection.add(object);
            }
        }
        
        super.selectObjects(selection.toArray());
    }
    
    /**
     * Add some extra Actions - *after* the graphical viewer has been created
     */
    @Override
    @SuppressWarnings("unchecked")
    protected void createActions(GraphicalViewer viewer) {
        super.createActions(viewer);
        
        ActionRegistry registry = getActionRegistry();
        IAction action;

        // Delete from Model
        action = new DeleteFromModelAction(this);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());
        
        // Viewpoints
        for(IViewpoint viewPoint : ViewpointManager.INSTANCE.getAllViewpoints()) {
            action = new ViewpointAction(this, viewPoint);
            registry.registerAction(action);
        }
        
        // Find/Replace
        action = new FindReplaceAction(getEditorSite().getWorkbenchWindow());
        registry.registerAction(action);
        
        // Generate View For
        action = new GenerateViewAction(this);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());
    }
    
    @Override
    protected void notifyChanged(Notification msg) {
        // Diagram Model Viewpoint changed
        if(msg.getNotifier() == getModel() && msg.getFeature() == IArchimatePackage.Literals.ARCHIMATE_DIAGRAM_MODEL__VIEWPOINT) {
            setViewpoint();
        }
        else {
            super.notifyChanged(msg);
        }
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
        return Messages.ArchimateDiagramEditor_0;
    }
}
