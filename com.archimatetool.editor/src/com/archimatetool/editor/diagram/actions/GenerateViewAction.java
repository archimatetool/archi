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
import org.eclipse.ui.IWorkbenchPart;

import com.archimatetool.editor.actions.ArchimateEditorActionFactory;
import com.archimatetool.editor.tools.GenerateViewCommand;
import com.archimatetool.model.IArchimateComponent;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateModel;



/**
 * Generate View For Action
 * 
 * @author Phillip Beauvoir
 */
public class GenerateViewAction extends SelectionAction {
    
    public GenerateViewAction(IWorkbenchPart part) {
        super(part);
        setId(ArchimateEditorActionFactory.GENERATE_VIEW.getId());
        setText(Messages.GenerateViewAction_0);
    }

    @Override
    protected boolean calculateEnabled() {
        return !getValidSelectedObjects().isEmpty();
    }

    private List<IArchimateElement> getValidSelectedObjects() {
        List<IArchimateElement> list = new ArrayList<IArchimateElement>();
        
        IArchimateModel model = null;
        
        for(Object object : getSelectedObjects()) {
            if(object instanceof EditPart) {
                Object o = ((EditPart)object).getAdapter(IArchimateComponent.class);
                if(o instanceof IArchimateElement) {
                    IArchimateElement element = (IArchimateElement)o;
                    IArchimateModel nextModel = element.getArchimateModel(); // From the same model
                    if(model == null || model == nextModel) {
                        if(!list.contains(element)) {
                            list.add(element);
                        }
                    }
                    model = nextModel;
                }
            }
        }
        
        return list;
    }

    @Override
    public void run() {
        List<IArchimateElement> selected = getValidSelectedObjects();
        if(!selected.isEmpty()) {
            GenerateViewCommand command = new GenerateViewCommand(selected);
            if(command.openDialog(getWorkbenchPart().getSite().getShell())) {
                execute(command);
            }
        }
    }
}
