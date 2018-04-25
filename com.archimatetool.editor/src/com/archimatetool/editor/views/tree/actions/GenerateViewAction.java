/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;

import com.archimatetool.editor.tools.GenerateViewCommand;
import com.archimatetool.model.IAdapter;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateModelObject;



/**
 * Generate View Action
 * 
 * @author Phillip Beauvoir
 */
public class GenerateViewAction extends ViewerAction {
    
    public GenerateViewAction(ISelectionProvider selectionProvider) {
        super(selectionProvider);
        setText(Messages.GenerateViewAction_0);
    }
    
    @Override
    public void run() {
        List<IArchimateElement> selected = getValidSelectedObjects(getSelection());
        
        if(!selected.isEmpty()) {
            GenerateViewCommand command = new GenerateViewCommand(selected);
            if(command.openDialog(Display.getCurrent().getActiveShell())) {
                CommandStack commandStack = (CommandStack)((IAdapter)selected.get(0)).getAdapter(CommandStack.class);
                commandStack.execute(command);
            }
        }
    }

    @Override
    public void update() {
        setEnabled(!getValidSelectedObjects(getSelection()).isEmpty());
    }
    
    private List<IArchimateElement> getValidSelectedObjects(IStructuredSelection selection) {
        List<IArchimateElement> list = new ArrayList<IArchimateElement>();
        
        if(isSameModel(selection)) {
            for(Object o : selection.toArray()) {
                // Only Elements
                if(o instanceof IArchimateElement) {
                    if(!list.contains(o)) {
                        list.add((IArchimateElement)o);
                    }
                }
            }
        }

        return list;
    }
    
    /**
     * As this action is for the models tree, it's possible a user could select objects
     * from different models. We don't want this.
     */
    private boolean isSameModel(IStructuredSelection selection) {
        IArchimateModel model = null;
        
        for(Object o : selection.toArray()) {
            if(o instanceof IArchimateModelObject) {
                IArchimateModel nextModel = ((IArchimateModelObject)o).getArchimateModel();
                if(model != null && model != nextModel) {
                    return false;
                }
                model = nextModel;
            }
        }

        return true;
    }
}