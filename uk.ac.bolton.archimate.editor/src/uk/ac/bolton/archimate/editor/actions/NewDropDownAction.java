/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.actions;

import org.eclipse.jface.action.ActionContributionItem;

import uk.ac.bolton.archimate.editor.ui.IArchimateImages;


/**
 * New Dropdown Action
 * 
 * @author Phillip Beauvoir
 */
public class NewDropDownAction
extends AbstractDropDownAction {
    
    public NewDropDownAction() {
        setImageDescriptor(IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_NEW_FILE_16));
        setText(Messages.NewDropDownAction_0);
        setToolTipText(Messages.NewDropDownAction_1);
        setId("uk.ac.bolton.archimate.editor.action.newAction"); //$NON-NLS-1$
    }
    
    @Override
    public void run() {
        ((ActionContributionItem)fItems.get(0)).getAction().run();
    }
}