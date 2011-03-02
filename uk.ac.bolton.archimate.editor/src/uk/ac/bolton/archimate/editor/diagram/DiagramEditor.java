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
            boolean result = addEditPartToSelection(element, editParts);
            // If element is a relationship that has no diagram connection but is expressed as an implicit nested
            // parent/child container representatation of a connection then select the parent and child elements
            if(!result && element instanceof IRelationship && ConnectionPreferences.useNestedConnections()) {
                if(DiagramModelUtils.isRelationReferencedInDiagramAsNestedConnection(getModel(), (IRelationship)element)) {
                    addEditPartToSelection(((IRelationship)element).getSource(), editParts);
                    addEditPartToSelection(((IRelationship)element).getTarget(), editParts);
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
    /*
     * Find an edit part corresponding to element and it to the list of editParts
     */
    private boolean addEditPartToSelection(IArchimateElement element, List<EditPart> editParts) {
        IDiagramModelComponent dc = DiagramModelUtils.findDiagramModelComponentForElement(getModel(), element);
        if(dc != null) {
            EditPart editPart = (EditPart)getGraphicalViewer().getEditPartRegistry().get(dc);
            if(editPart != null && !editParts.contains(editPart)) {
                editParts.add(editPart);
                return true;
            }
        }
        return false;
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
