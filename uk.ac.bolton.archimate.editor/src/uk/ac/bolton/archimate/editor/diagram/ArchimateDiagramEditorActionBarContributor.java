/*******************************************************************************
 * Copyright (c) 2010-11 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.ui.actions.LabelRetargetAction;
import org.eclipse.ui.actions.RetargetAction;

import uk.ac.bolton.archimate.editor.actions.ArchimateEditorActionFactory;
import uk.ac.bolton.archimate.editor.diagram.actions.CreateDerivedRelationAction;
import uk.ac.bolton.archimate.editor.diagram.actions.DeleteFromModelAction;
import uk.ac.bolton.archimate.editor.diagram.actions.ShowStructuralChainsAction;
import uk.ac.bolton.archimate.editor.model.viewpoints.IViewpoint;
import uk.ac.bolton.archimate.editor.model.viewpoints.ViewpointsManager;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;


/**
 * Action Bar contributor for Archimate Diagram Editor
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateDiagramEditorActionBarContributor
extends AbstractDiagramEditorActionBarContributor {
    
    protected String editDeleteMenuGroup = "editDeleteMenuGroup";

    @Override
    protected void buildActions() {
        super.buildActions();
        
        // Show Structural Chains
        RetargetAction retargetAction = new LabelRetargetAction(ShowStructuralChainsAction.ID, ShowStructuralChainsAction.DEFAULT_TEXT);
        retargetAction.setImageDescriptor(IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_DERIVED_16));
        addRetargetAction(retargetAction);
        
        // Create Derived Relation
        retargetAction = new RetargetAction(CreateDerivedRelationAction.ID, CreateDerivedRelationAction.TEXT);
        retargetAction.setImageDescriptor(new DecorationOverlayIcon(IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_DERIVED_SM_16),
                        IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_NEW_OVERLAY_16), IDecoration.TOP_LEFT));
        addRetargetAction(retargetAction);
        
        // Delete From Model
        retargetAction = new RetargetAction(DeleteFromModelAction.ID, DeleteFromModelAction.TEXT);
        addRetargetAction(retargetAction);
        
        // Viewpoints
        for(IViewpoint viewPoint : ViewpointsManager.INSTANCE.getAllViewpoints()) {
            retargetAction = new RetargetAction(viewPoint.getClass().toString(), viewPoint.getName(), IAction.AS_RADIO_BUTTON);
            // Looks better as a checkbox
            //retargetAction.setImageDescriptor(ViewpointsManager.INSTANCE.getImageDescriptor(viewPoint));
            addRetargetAction(retargetAction);
        }
    }
    
    @Override
    protected IMenuManager contributeToEditMenu(IMenuManager menuManager) {
        IMenuManager editMenu = super.contributeToEditMenu(menuManager);
        
        editMenu.insertAfter(ArchimateEditorActionFactory.DELETE.getId(), new Separator(editDeleteMenuGroup));
        editMenu.appendToGroup(editDeleteMenuGroup, getAction(DeleteFromModelAction.ID));
        
        return editMenu;
    }
    
    @Override
    protected IMenuManager createViewMenu(IMenuManager menuManager) {
        IMenuManager viewMenu = super.createViewMenu(menuManager);
        
        // Viewpoints
        IMenuManager viewPointMenu = new MenuManager("Viewpoint");
        viewMenu.add(viewPointMenu);
        for(IViewpoint viewPoint : ViewpointsManager.INSTANCE.getAllViewpoints()) {
            viewPointMenu.add(getAction(viewPoint.getClass().toString()));
        }

        viewMenu.add(new Separator());
        
        // Derived Relations
        IMenuManager derivedRelationsMenu = new MenuManager("Derived Relations");
        viewMenu.add(derivedRelationsMenu);
        derivedRelationsMenu.add(getAction(ShowStructuralChainsAction.ID));
        derivedRelationsMenu.add(getAction(CreateDerivedRelationAction.ID));
        
        return viewMenu;
    }
}
