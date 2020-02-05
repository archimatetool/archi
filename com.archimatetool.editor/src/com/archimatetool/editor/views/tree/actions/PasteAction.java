/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree.actions;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.model.commands.NonNotifyingCompoundCommand;
import com.archimatetool.editor.ui.LocalClipboard;
import com.archimatetool.editor.views.tree.TreeModelCutAndPaste;
import com.archimatetool.editor.views.tree.commands.MoveFolderCommand;
import com.archimatetool.editor.views.tree.commands.MoveObjectCommand;
import com.archimatetool.model.IArchimateModelObject;
import com.archimatetool.model.IFolder;



/**
 * Paste Action
 * 
 * @author Phillip Beauvoir
 */
public class PasteAction extends ViewerAction {
    
    public PasteAction(ISelectionProvider selectionProvider) {
        super(selectionProvider);
        setText(Messages.PasteAction_0);
        setActionDefinitionId("org.eclipse.ui.edit.paste"); // Ensures key binding is displayed //$NON-NLS-1$
        setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
        setDisabledImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_PASTE_DISABLED));
        setEnabled(false);
        
        /**
         * Update Paste Action if clipboard contents has changed
         */
        LocalClipboard.getDefault().addListener((clipboardContents) -> {
            update();
        });
    }
    
    @Override
    public void run() {
        if(!hasValidSelection()) {
            setEnabled(false);
            return;
        }
        
        IFolder parent = (IFolder)getSelection().getFirstElement();

        CompoundCommand compoundCommand = new NonNotifyingCompoundCommand() {
            @Override
            public String getLabel() {
                return getCommands().size() > 1 ? Messages.PasteAction_1 : super.getLabel();
            }
        };

        for(IArchimateModelObject selected : TreeModelCutAndPaste.INSTANCE.getObjects()) {
            if(isAllowedToPaste(parent, selected)) {
                if(selected instanceof IFolder) { // This first - folders go in folders
                    compoundCommand.add(new MoveFolderCommand(parent, (IFolder)selected));
                }
                else {
                    compoundCommand.add(new MoveObjectCommand(parent, selected));
                }
            }
        }

        CommandStack commandStack = (CommandStack)parent.getAdapter(CommandStack.class);
        if(commandStack != null) {
            commandStack.execute(compoundCommand);
            TreeModelCutAndPaste.INSTANCE.clear();
            ((TreeViewer)getSelectionProvider()).expandToLevel(parent, 1);
        }
    }

    @Override
    public void update() {
        if(!hasValidSelection()) {
            setEnabled(false);
            return;
        }
        
        IFolder parent = (IFolder)getSelection().getFirstElement();

        for(IArchimateModelObject object : TreeModelCutAndPaste.INSTANCE.getObjects()) {
            if(isAllowedToPaste(parent, object)) {
                setEnabled(true);
                return;
            }
        }
        
        setEnabled(false);
    }

    private boolean hasValidSelection() {
        return TreeModelCutAndPaste.INSTANCE.hasContents() &&
                getSelection() != null && !getSelection().isEmpty() && getSelection().getFirstElement() instanceof IFolder;
    }
    
    private boolean isAllowedToPaste(IFolder parent, IArchimateModelObject object) {
        boolean result = true;
        
        if(object instanceof IFolder) {
            result = isFolderAncestor(parent, (IFolder)object);
        }
        
        return result &&
                object.getArchimateModel() != null &&  // object's model != null (was deleted)
                object.getArchimateModel() == parent.getArchimateModel() && // same model
                object.eContainer() != parent && // not the same parent folder
                hasCommonAncestorFolder(parent, object); // correct folder type
    }
    
    /**
     * If moving a folder check that the target parent folder is an ancestor and not a descendant of the cut folder
     */
    private boolean isFolderAncestor(IFolder targetFolder, IFolder cutFolder) {
        EObject eObject = targetFolder;
        
        while(eObject instanceof IFolder) {
            if(eObject == cutFolder) {
                return false;
            }
            eObject = eObject.eContainer();
        }

        return true;
    }
    
    /**
     * Check if eObject1 and eObject2 have a common ancestor folder
     * i.e objects are only allowed in the same type of folder
     */
    private boolean hasCommonAncestorFolder(EObject eObject1, EObject eObject2) {
        while(eObject1.eContainer() instanceof IFolder) {
            eObject1 = eObject1.eContainer();
        }

        while(eObject2.eContainer() instanceof IFolder) {
            eObject2 = eObject2.eContainer();
        }
        
        return (eObject1 == eObject2);
    }
}
