/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.gef.AutoexposeHelper;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.help.HelpSystem;
import org.eclipse.help.IContext;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import uk.ac.bolton.archimate.editor.diagram.actions.CreateDerivedRelationAction;
import uk.ac.bolton.archimate.editor.diagram.actions.DeleteFromModelAction;
import uk.ac.bolton.archimate.editor.diagram.actions.SelectElementInTreeAction;
import uk.ac.bolton.archimate.editor.diagram.actions.ShowStructuralChainsAction;
import uk.ac.bolton.archimate.editor.diagram.dnd.DiagramTransferDropTargetListener;
import uk.ac.bolton.archimate.editor.diagram.editparts.DiagramEditPartFactory;
import uk.ac.bolton.archimate.editor.diagram.util.ExtendedViewportAutoexposeHelper;
import uk.ac.bolton.archimate.editor.model.DiagramModelUtils;
import uk.ac.bolton.archimate.editor.preferences.ConnectionPreferences;
import uk.ac.bolton.archimate.editor.views.tree.TreeSelectionSynchroniser;
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;
import uk.ac.bolton.archimate.model.IDiagramModelComponent;
import uk.ac.bolton.archimate.model.IRelationship;


/**
 * Diagram Editor
 * 
 * @author Phillip Beauvoir
 */
public class DiagramEditor extends AbstractDiagramEditor
implements IDiagramEditor {
    
    /**
     * Palette
     */
    private DiagramEditorPalette fPalette;


    @Override
    public void doCreatePartControl(Composite parent) {
        // Register Help Context
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    @Override
    public PaletteRoot getPaletteRoot() {
        if(fPalette == null) {
            fPalette = new DiagramEditorPalette();
        }
        return fPalette;
    }

    @Override
    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();

        GraphicalViewer viewer = getGraphicalViewer();

        // Register the Edit Part Factory before setting model contents
        viewer.setEditPartFactory(new DiagramEditPartFactory());
        
        // Set Model
        viewer.setContents(getModel());
        
        // Add Selection Sync
        TreeSelectionSynchroniser.INSTANCE.addDiagramEditor(this);
        
        // Native DnD
        viewer.addDropTargetListener(new DiagramTransferDropTargetListener(viewer));
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
        MenuManager provider = new DiagramEditorContextMenuProvider(viewer, getActionRegistry());
        viewer.setContextMenu(provider);
        getSite().registerContextMenu(DiagramEditorContextMenuProvider.ID, provider, viewer);
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

        // Select Element in Tree
        action = new SelectElementInTreeAction(this);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());
        
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
    }
    
    @Override
    public void dispose() {
        super.dispose();
        TreeSelectionSynchroniser.INSTANCE.removeDiagramEditor(this);
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
        return "Diagram View";
    }
}
