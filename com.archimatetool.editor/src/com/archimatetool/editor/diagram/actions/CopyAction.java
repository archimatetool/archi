/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

import com.archimatetool.editor.ui.LocalClipboard;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.ILockable;



/**
 * Copy Action
 * 
 * @author Phillip Beauvoir
 */
public class CopyAction extends SelectionAction {
    
    protected PasteAction pasteAction;
    
    public CopyAction(IWorkbenchPart part, PasteAction pasteAction) {
        super(part);
        this.pasteAction = pasteAction;
    }
    
    @Override 
    protected void init() {
        setText(Messages.CopyAction_0);
        setId(ActionFactory.COPY.getId());
        setActionDefinitionId(ActionFactory.COPY.getCommandId());
        ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
        setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
        setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY_DISABLED));
        setEnabled(false);
    }

    @Override
    protected boolean calculateEnabled() {
        // Must select at least one Diagram Model Object
        // We don't copy connections or the Root Edit Part
        for(EditPart editPart : getSelectedEditParts()) {
            Object model = editPart.getModel();
            if(model instanceof ILockable lockable && lockable.isLocked()) {
                continue;
            }
            if(model instanceof IDiagramModelObject) {
                return true;
            }
        }
        
        return false;
    }

    @Override
    public void run() {
        List<IDiagramModelComponent> selected = new ArrayList<>();
        
        for(EditPart editPart : getSelectedEditParts()) {
            Object model = editPart.getModel();
            if(model instanceof ILockable lockable && lockable.isLocked()) {
                continue;
            }
            if(model instanceof IDiagramModelComponent dmc) {
                selected.add(dmc);
            }
        }
        
        CopySnapshot clipBoardCopy = new CopySnapshot(selected);
        LocalClipboard.getDefault().setContents(clipBoardCopy);
        
        // Reset Paste Action
        pasteAction.reset();
    }
}
