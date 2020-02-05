/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree.actions;

import java.util.ArrayList;
import java.util.List;

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
        List<IArchimateModelObject> validObjects = getValidSelection();
        
        if(validObjects.isEmpty()) {
            setEnabled(false);
        }
        else {
            TreeModelCutAndPaste.INSTANCE.setContents(validObjects);
        }
    }

    @Override
    public void update() {
        setEnabled(!getValidSelection().isEmpty());
    }
    
    private List<IArchimateModelObject> getValidSelection() {
        List<IArchimateModelObject> validObjects = new ArrayList<IArchimateModelObject>();
        
        IStructuredSelection selection = getSelection();
        
        if(selection != null) {
            for(Object object : selection.toList()) {
                if(isAllowedToCut(object)) {
                    validObjects.add((IArchimateModelObject)object);
                }
            }
        }
        
        return validObjects;
    }
    
    private boolean isAllowedToCut(Object object) {
        return object instanceof IArchimateConcept || object instanceof IDiagramModel ||
                (object instanceof IFolder && ((IFolder)object).getType() == FolderType.USER);
    }
}
