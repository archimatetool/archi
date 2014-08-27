/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.ILockable;



/**
 * Copy Action
 * 
 * @author Phillip Beauvoir
 */
public class CopyAction extends SelectionAction {
    
    protected PasteAction fPasteAction;
    protected MergeAction fMergeAction;
    
    public CopyAction(IWorkbenchPart part, PasteAction pasteAction, MergeAction mergeAction) {
        super(part);
        fPasteAction = pasteAction;
        fMergeAction = mergeAction;
    }
    
    @Override
    protected void init() {
        setText(Messages.CopyAction_0);
        setId(ActionFactory.COPY.getId());
        ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
        setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
        setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY_DISABLED));
        setEnabled(false);
    }

    @Override
    protected boolean calculateEnabled() {
        List<?> selected = getSelectedObjects();
        
        if(selected.isEmpty()) {
            return false;
        }

        // Must select at least one Diagram Model Object
        // We don't copy connections or the Root Edit Part
        for(Object object : selected) {
            if(object instanceof EditPart) {
                Object model = ((EditPart)object).getModel();
                if(model instanceof ILockable && ((ILockable)model).isLocked()) {
                    continue;
                }
                if(model instanceof IDiagramModelObject) {
                    return true;
                }
            }
        }
        
        return false;
    }

    @Override
    public void run() {
        List<IDiagramModelObject> modelObjectsSelected = new ArrayList<IDiagramModelObject>();
        
        for(Object object : getSelectedObjects()) {
            if(object instanceof EditPart) {
                Object model = ((EditPart)object).getModel();
                if(model instanceof ILockable && ((ILockable)model).isLocked()) {
                    continue;
                }
                if(model instanceof IDiagramModelObject) {
                    modelObjectsSelected.add((IDiagramModelObject)model);
                }
            }
        }
        
        CopySnapshot clipBoardCopy = new CopySnapshot(modelObjectsSelected);
        Clipboard.getDefault().setContents(clipBoardCopy);
        
        // Reset Paste and Merge Actions
        fPasteAction.reset();
        fMergeAction.reset();
    }
}
