/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.AutoexposeHelper;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.help.HelpSystem;
import org.eclipse.help.IContext;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.diagram.actions.CreateDerivedRelationAction;
import com.archimatetool.editor.diagram.actions.DeleteFromModelAction;
import com.archimatetool.editor.diagram.actions.ShowStructuralChainsAction;
import com.archimatetool.editor.diagram.actions.ViewpointAction;
import com.archimatetool.editor.diagram.dnd.ArchimateDiagramTransferDropTargetListener;
import com.archimatetool.editor.diagram.editparts.ArchimateDiagramEditPartFactory;
import com.archimatetool.editor.diagram.util.ExtendedViewportAutoexposeHelper;
import com.archimatetool.editor.model.DiagramModelUtils;
import com.archimatetool.editor.model.viewpoints.IViewpoint;
import com.archimatetool.editor.model.viewpoints.ViewpointsManager;
import com.archimatetool.editor.preferences.ConnectionPreferences;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.IRelationship;



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
    
    @Override
    protected void applicationPreferencesChanged(PropertyChangeEvent event) {
        // Hide Palette elements on Viewpoint
        if(IPreferenceConstants.VIEWPOINTS_HIDE_PALETTE_ELEMENTS == event.getProperty()) {
            if(Boolean.TRUE == event.getNewValue()) {
                setPaletteViewpoint();
            }
            else {
                getPaletteRoot().setViewpoint(null);
            }
        }
        // Hide Diagram Elements on Viewpoint
        else if(IPreferenceConstants.VIEWPOINTS_HIDE_DIAGRAM_ELEMENTS == event.getProperty()) {
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
     * Set Palette to current Viewpoint in model if Preference set
     */
    protected void setPaletteViewpoint() {
        if(Preferences.STORE.getBoolean(IPreferenceConstants.VIEWPOINTS_HIDE_PALETTE_ELEMENTS)) {
            getPaletteRoot().setViewpoint(ViewpointsManager.INSTANCE.getViewpoint(getModel().getViewpoint()));
        }
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
    public void selectElements(IArchimateElement[] elements) {
        List<EditPart> editParts = new ArrayList<EditPart>();
        
        for(IArchimateElement element : elements) {
            // Find Diagram Components
            for(IDiagramModelComponent dc : DiagramModelUtils.findDiagramModelComponentsForElement(getModel(), element)) {
                EditPart editPart = (EditPart)getGraphicalViewer().getEditPartRegistry().get(dc);
                if(editPart != null && editPart.isSelectable() && !editParts.contains(editPart)) {
                    editParts.add(editPart);
                }
            }
            
            // Find Components from nested connections
            if(ConnectionPreferences.useNestedConnections() && element instanceof IRelationship) {
                for(IDiagramModelArchimateObject[] list : DiagramModelUtils.findNestedComponentsForRelationship(getModel(), (IRelationship)element)) {
                    EditPart editPart1 = (EditPart)getGraphicalViewer().getEditPartRegistry().get(list[0]);
                    EditPart editPart2 = (EditPart)getGraphicalViewer().getEditPartRegistry().get(list[1]);
                    if(editPart1 != null && editPart1.isSelectable() && !editParts.contains(editPart1)) {
                        editParts.add(editPart1);
                    }
                    if(editPart2 != null && editPart2.isSelectable() && !editParts.contains(editPart2)) {
                        editParts.add(editPart2);
                    }
                }
            }
        }
        
        if(!editParts.isEmpty()) {
            getGraphicalViewer().setSelection(new StructuredSelection(editParts));
            getGraphicalViewer().reveal(editParts.get(0));
        }
        else {
            getGraphicalViewer().setSelection(StructuredSelection.EMPTY);
        }
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

        // Show Structural Chains
        action = new ShowStructuralChainsAction(this);
        registry.registerAction(action);
        
        // Create Derived Relation
        action = new CreateDerivedRelationAction(this);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());
        
        // Delete from Model
        action = new DeleteFromModelAction(this);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());
        
        // Viewpoints
        for(IViewpoint viewPoint : ViewpointsManager.INSTANCE.getAllViewpoints()) {
            action = new ViewpointAction(this, viewPoint);
            registry.registerAction(action);
        }
    }
    
    @Override
    protected void eCoreModelChanged(Notification msg) {
        super.eCoreModelChanged(msg);
        
        if(msg.getEventType() == Notification.SET) {
            // Diagram Model Viewpoint changed
            if(msg.getNotifier() == getModel() && msg.getFeature() == IArchimatePackage.Literals.ARCHIMATE_DIAGRAM_MODEL__VIEWPOINT) {
                setViewpoint();
            }
        }
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
        return Messages.ArchimateDiagramEditor_0;
    }
}
