/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

import com.archimatetool.editor.diagram.commands.DiagramCommandFactory;
import com.archimatetool.editor.model.DiagramModelUtils;
import com.archimatetool.editor.model.commands.DeleteElementCommand;
import com.archimatetool.editor.model.commands.NonNotifyingCompoundCommand;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IRelationship;
import com.archimatetool.model.util.ArchimateModelUtils;



/**
 * Delete from Model Action
 * 
 * @author Phillip Beauvoir
 */
public class DeleteFromModelAction extends SelectionAction {
    
    public static final String ID = "DeleteFromModelAction"; //$NON-NLS-1$
    public static final String TEXT = Messages.DeleteFromModelAction_0;
    
    public DeleteFromModelAction(IWorkbenchPart part) {
        super(part);
        setText(TEXT);
        setId(ID);
    }

    @Override
    protected boolean calculateEnabled() {
        List<?> list = getSelectedObjects();
        
        if(list.isEmpty()) {
            return false;
        }
        
        for(Object object : list) {
            if(object instanceof EditPart) {
                Object model = ((EditPart)object).getModel();
                if(model instanceof IDiagramModelArchimateObject || model instanceof IDiagramModelArchimateConnection) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    @Override
    public void run() {
        List<?> selection = getSelectedObjects();
        List<IArchimateElement> elements = new ArrayList<IArchimateElement>();
        List<IDiagramModelComponent> diagramObjects = new ArrayList<IDiagramModelComponent>();
        
        // Gather Model elements, relations
        for(Object object : selection) {
            if(object instanceof EditPart) {
                Object model = ((EditPart)object).getModel();
                if(model instanceof IDiagramModelArchimateObject) {
                    IArchimateElement element = ((IDiagramModelArchimateObject)model).getArchimateElement();
                    if(!elements.contains(element)) {
                        elements.add(element);
                    }
                    // Element's relationships
                    for(IRelationship relation :  ArchimateModelUtils.getRelationships(element)) {
                        if(!elements.contains(relation)) {
                            elements.add(relation);
                        }
                    }
                }
                else if(model instanceof IDiagramModelArchimateConnection) {
                    IRelationship relation = ((IDiagramModelArchimateConnection)model).getRelationship();
                    if(!elements.contains(relation)) {
                        elements.add(relation);
                    }
                }
            }
        }
        
        // Gather referenced diagram objects
        for(IArchimateElement element : elements) {
            for(IDiagramModel diagramModel : element.getArchimateModel().getDiagramModels()) {
                for(IDiagramModelComponent dc : DiagramModelUtils.findDiagramModelComponentsForElement(diagramModel, element)) {
                    diagramObjects.add(dc);
                }
            }
        }
        
        // Create commands
        
        CompoundCommand compoundCommand = new NonNotifyingCompoundCommand(TEXT);
        
        for(IArchimateElement element : elements) {
            Command cmd = new DeleteElementCommand(element);
            compoundCommand.add(cmd);
        }
        
        for(IDiagramModelComponent dc : diagramObjects) {
            if(dc instanceof IDiagramModelObject) {
                Command cmd = DiagramCommandFactory.createDeleteDiagramObjectCommand((IDiagramModelObject)dc);
                compoundCommand.add(cmd);
            }
            else if(dc instanceof IDiagramModelConnection) {
                Command cmd = DiagramCommandFactory.createDeleteDiagramConnectionCommand((IDiagramModelConnection)dc);
                compoundCommand.add(cmd);
            }
        }
        
        execute(compoundCommand);
    }
    
}
