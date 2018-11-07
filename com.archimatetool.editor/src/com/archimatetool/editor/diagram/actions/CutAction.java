/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

import com.archimatetool.editor.diagram.commands.DiagramCommandFactory;
import com.archimatetool.model.IDiagramModelObject;



/**
 * Cut Action
 * 
 * @author Phillip Beauvoir
 */
public class CutAction extends CopyAction {
    
    public CutAction(IWorkbenchPart part, PasteAction pasteAction) {
        super(part, pasteAction);
    }
    
    @Override
    protected void init() {
        setText(Messages.CutAction_0);
        setId(ActionFactory.CUT.getId());
        ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
        setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
        setDisabledImageDescriptor(sharedImages.getImageDescriptor(
                ISharedImages.IMG_TOOL_CUT_DISABLED));
        setEnabled(false);
    }

    @Override
    public void run() {
        // Copy and then Delete Selected objects
        super.run();
        
        BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {
            @Override
            public void run() {
                CompoundCommand result = new CompoundCommand(Messages.CutAction_1);
                
                for(Object object : getSelectedObjects()) {
                    if(object instanceof EditPart) {
                        Object model = ((EditPart)object).getModel();
                        if(model instanceof IDiagramModelObject) {
                            Command cmd = DiagramCommandFactory.createDeleteDiagramObjectCommand((IDiagramModelObject)model);
                            result.add(cmd);
                        }
                    }
                }
                
                execute(result); // Don't use unwrap because we want the "Cut" label
            }
        });
    }
}
