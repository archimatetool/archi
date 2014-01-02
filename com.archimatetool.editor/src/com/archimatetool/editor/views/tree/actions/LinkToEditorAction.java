/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.ui.IArchimateImages;
import com.archimatetool.editor.views.tree.TreeSelectionSynchroniser;



/**
 * Link To Editor Action
 * 
 * @author Phillip Beauvoir
 */
public class LinkToEditorAction extends Action {
    
    public LinkToEditorAction() {
        super(Messages.LinkToEditorAction_0, IAction.AS_CHECK_BOX);
        setImageDescriptor(IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_LINKED_16));
        setChecked(Preferences.doLinkView());
    }
    
    @Override
    public void run() {
        Preferences.setLinkView(isChecked());
        if(isChecked()) {
            TreeSelectionSynchroniser.INSTANCE.refresh();
        }
    }

}
