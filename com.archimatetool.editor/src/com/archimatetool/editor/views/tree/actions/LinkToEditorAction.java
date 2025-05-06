/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.IArchiImages;



/**
 * Link To Editor Action
 * 
 * @author Phillip Beauvoir
 */
public class LinkToEditorAction extends Action {
    
    public LinkToEditorAction() {
        super(Messages.LinkToEditorAction_0, IAction.AS_CHECK_BOX);
        setImageDescriptor(IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_LINKED));
        setChecked(ArchiPlugin.getInstance().getPreferenceStore().getBoolean(IPreferenceConstants.LINK_VIEW));
    }
    
    @Override
    public void run() {
        ArchiPlugin.getInstance().getPreferenceStore().setValue(IPreferenceConstants.LINK_VIEW, isChecked());
    }
}
