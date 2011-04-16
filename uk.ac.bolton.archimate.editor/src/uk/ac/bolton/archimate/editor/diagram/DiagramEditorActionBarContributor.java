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
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.actions.RetargetAction;

import uk.ac.bolton.archimate.editor.actions.ArchimateEditorActionFactory;
import uk.ac.bolton.archimate.editor.diagram.actions.CreateDerivedRelationAction;
import uk.ac.bolton.archimate.editor.diagram.actions.DeleteFromModelAction;
import uk.ac.bolton.archimate.editor.diagram.actions.ShowStructuralChainsAction;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;


/**
 * Action Bar contributor for Diagram Editor
 * 
 * @author Phillip Beauvoir
 */
public class DiagramEditorActionBarContributor
extends AbstractDiagramEditorActionBarContributor {

    @Override
    protected void buildActions() {
        super.buildActions();
        
        // Show Structural Chains
        RetargetAction retargetAction = new RetargetAction(ShowStructuralChainsAction.ID, ShowStructuralChainsAction.TEXT, IAction.AS_CHECK_BOX);
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
    }
    
    @Override
    public void contributeToMenu(IMenuManager menuManager) {
    	super.contributeToMenu(menuManager);
    	
        IMenuManager editMenu = (IMenuManager)menuManager.find(IWorkbenchActionConstants.M_EDIT);
        String groupName = "group_del";
        editMenu.insertAfter(ArchimateEditorActionFactory.DELETE.getId(), new Separator(groupName));
        editMenu.appendToGroup(groupName, getAction(DeleteFromModelAction.ID));
    }

    @Override
    protected IMenuManager createViewMenu(IMenuManager menuManager) {
        IMenuManager viewMenu = super.createViewMenu(menuManager);
        
        // Derived Relations
        IMenuManager derivedRelationsMenu = new MenuManager("Derived Relations");
        viewMenu.add(derivedRelationsMenu);
        derivedRelationsMenu.add(getAction(ShowStructuralChainsAction.ID));
        derivedRelationsMenu.add(getAction(CreateDerivedRelationAction.ID));
        viewMenu.add(new Separator());

        return viewMenu;
    }
    
}
