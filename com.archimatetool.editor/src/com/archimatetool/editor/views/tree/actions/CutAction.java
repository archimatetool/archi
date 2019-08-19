/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree.actions;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.views.tree.TreeModelCutAndPaste;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateModelObject;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IFolder;



/**
 * Cut Action
 * 
 * @author Phillip Beauvoir
 */
public class CutAction extends ViewerAction {
    
    public CutAction(ISelectionProvider selectionProvider) {
        super(selectionProvider);
        setText(Messages.CutAction_0);
        setActionDefinitionId("org.eclipse.ui.edit.cut"); // Ensures key binding is displayed //$NON-NLS-1$
        setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
        setDisabledImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_CUT_DISABLED));
        setEnabled(false);
    }
    
    @Override
    public void run() {
        if(!hasValidSelection()) {
            setEnabled(false);
            return;
        }
        
        TreeModelCutAndPaste.INSTANCE.clear();
        
        for(Object object : getSelection().toList()) {
            if(isAllowedToCut(object)) {
                TreeModelCutAndPaste.INSTANCE.add((IArchimateModelObject)object);
            }
        }
        
        TreeModelCutAndPaste.INSTANCE.setContentsToClipboard();
    }

    @Override
    public void update() {
        setEnabled(hasValidSelection());
    }
    
    private boolean isAllowedToCut(Object object) {
        return object instanceof IArchimateConcept || object instanceof IDiagramModel ||
                (object instanceof IFolder && ((IFolder)object).getType() == FolderType.USER);
    }
    
    private boolean hasValidSelection() {
        IStructuredSelection selection = getSelection();
        
        if(selection == null || selection.isEmpty()) {
            return false;
        }
        
        for(Object object : selection.toList()) {
            if(isAllowedToCut(object)) {
                return true;
            }
        }
        
        return false;
    }
}
