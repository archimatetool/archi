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
import com.archimatetool.editor.model.commands.DeleteArchimateElementCommand;
import com.archimatetool.editor.model.commands.DeleteArchimateRelationshipCommand;
import com.archimatetool.editor.model.commands.NonNotifyingCompoundCommand;
import com.archimatetool.model.IArchimateComponent;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateComponent;
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
                if(model instanceof IDiagramModelArchimateComponent) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    @Override
    public void run() {
        List<?> selection = getSelectedObjects();
        List<IArchimateComponent> archimateComponents = new ArrayList<IArchimateComponent>();
        List<IDiagramModelComponent> diagramObjects = new ArrayList<IDiagramModelComponent>();
        
        // Gather Model elements, relations
        for(Object object : selection) {
            if(object instanceof EditPart) {
                Object model = ((EditPart)object).getModel();
                if(model instanceof IDiagramModelArchimateObject) {
                    IArchimateElement element = ((IDiagramModelArchimateObject)model).getArchimateElement();
                    if(!archimateComponents.contains(element)) {
                        archimateComponents.add(element);
                    }
                    // Element's relationships
                    for(IRelationship relation :  ArchimateModelUtils.getAllRelationshipsForComponent(element)) {
                        if(!archimateComponents.contains(relation)) {
                            archimateComponents.add(relation);
                        }
                        // Relation's relationships
                        for(IRelationship r :  ArchimateModelUtils.getAllRelationshipsForComponent(relation)) {
                            if(!archimateComponents.contains(r)) {
                                archimateComponents.add(r);
                            }
                        }
                    }
                }
                else if(model instanceof IDiagramModelArchimateConnection) {
                    IRelationship relation = ((IDiagramModelArchimateConnection)model).getRelationship();
                    if(!archimateComponents.contains(relation)) {
                        archimateComponents.add(relation);
                    }
                    // Relation's relationships
                    for(IRelationship r :  ArchimateModelUtils.getAllRelationshipsForComponent(relation)) {
                        if(!archimateComponents.contains(r)) {
                            archimateComponents.add(r);
                        }
                    }
                }
            }
        }
        
        // Gather referenced diagram objects
        for(IArchimateComponent archimateComponent : archimateComponents) {
            for(IDiagramModel diagramModel : archimateComponent.getArchimateModel().getDiagramModels()) {
                for(IDiagramModelComponent dc : DiagramModelUtils.findDiagramModelComponentsForArchimateComponent(diagramModel, archimateComponent)) {
                    diagramObjects.add(dc);
                }
            }
        }
        
        // Create commands
        
        CompoundCommand compoundCommand = new NonNotifyingCompoundCommand(TEXT);
        
        for(IArchimateComponent archimateComponent : archimateComponents) {
            if(archimateComponent instanceof IArchimateElement) {
                Command cmd = new DeleteArchimateElementCommand((IArchimateElement)archimateComponent);
                compoundCommand.add(cmd);
            }
            else if(archimateComponent instanceof IRelationship) {
                Command cmd = new DeleteArchimateRelationshipCommand((IRelationship)archimateComponent);
                compoundCommand.add(cmd);
            }
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
