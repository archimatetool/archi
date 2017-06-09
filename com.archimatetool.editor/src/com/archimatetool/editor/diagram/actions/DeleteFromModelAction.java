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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;

import com.archimatetool.editor.diagram.commands.DiagramCommandFactory;
import com.archimatetool.editor.model.DiagramModelUtils;
import com.archimatetool.editor.model.commands.DeleteArchimateElementCommand;
import com.archimatetool.editor.model.commands.DeleteArchimateRelationshipCommand;
import com.archimatetool.editor.model.commands.NonNotifyingCompoundCommand;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateComponent;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelObject;
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
        List<IArchimateConcept> archimateConcepts = new ArrayList<IArchimateConcept>();
        List<IDiagramModelComponent> diagramObjects = new ArrayList<IDiagramModelComponent>();
        
        // Gather Model elements, relations
        for(Object object : selection) {
            if(object instanceof EditPart) {
                Object model = ((EditPart)object).getModel();
                if(model instanceof IDiagramModelArchimateObject) {
                    IArchimateElement element = ((IDiagramModelArchimateObject)model).getArchimateElement();
                    if(!archimateConcepts.contains(element)) {
                        archimateConcepts.add(element);
                    }
                    // Element's relationships
                    for(IArchimateRelationship relation :  ArchimateModelUtils.getAllRelationshipsForConcept(element)) {
                        if(!archimateConcepts.contains(relation)) {
                            archimateConcepts.add(relation);
                        }
                        // Relation's relationships
                        for(IArchimateRelationship r :  ArchimateModelUtils.getAllRelationshipsForConcept(relation)) {
                            if(!archimateConcepts.contains(r)) {
                                archimateConcepts.add(r);
                            }
                        }
                    }
                }
                else if(model instanceof IDiagramModelArchimateConnection) {
                    IArchimateRelationship relation = ((IDiagramModelArchimateConnection)model).getArchimateRelationship();
                    if(!archimateConcepts.contains(relation)) {
                        archimateConcepts.add(relation);
                    }
                    // Relation's relationships
                    for(IArchimateRelationship r :  ArchimateModelUtils.getAllRelationshipsForConcept(relation)) {
                        if(!archimateConcepts.contains(r)) {
                            archimateConcepts.add(r);
                        }
                    }
                }
            }
        }
        
        // Gather referenced diagram objects
        for(IArchimateConcept archimateConcept : archimateConcepts) {
            for(IDiagramModel diagramModel : archimateConcept.getArchimateModel().getDiagramModels()) {
                for(IDiagramModelComponent dc : DiagramModelUtils.findDiagramModelComponentsForArchimateConcept(diagramModel, archimateConcept)) {
                    diagramObjects.add(dc);
                }
            }
        }

        // Check whether any of these concepts are referenced in other diagrams
        if(hasMoreThanOneReference(archimateConcepts, diagramObjects)) {
            if(!MessageDialog.openQuestion(Display.getDefault().getActiveShell(),
                    Messages.DeleteFromModelAction_0,
                    Messages.DeleteFromModelAction_1 +
                    "\n\n" + //$NON-NLS-1$
                    Messages.DeleteFromModelAction_2)) {
                return;
            }
        }
        
        // Create commands
        
        CompoundCommand compoundCommand = new NonNotifyingCompoundCommand(TEXT);
        
        for(IArchimateConcept archimateConcept : archimateConcepts) {
            if(archimateConcept instanceof IArchimateElement) {
                Command cmd = new DeleteArchimateElementCommand((IArchimateElement)archimateConcept);
                compoundCommand.add(cmd);
            }
            else if(archimateConcept instanceof IArchimateRelationship) {
                Command cmd = new DeleteArchimateRelationshipCommand((IArchimateRelationship)archimateConcept);
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

    private boolean hasMoreThanOneReference(List<IArchimateConcept> archimateConcepts, List<IDiagramModelComponent> diagramObjects) {
        for(IArchimateConcept archimateConcept : archimateConcepts) {
            int count = 0;

            for(IDiagramModelComponent dmc : diagramObjects) {
                if(dmc instanceof IDiagramModelArchimateComponent) {
                    if(((IDiagramModelArchimateComponent)dmc).getArchimateConcept() == archimateConcept) {
                        count++;
                        if(count > 1) {
                            return true;
                        }
                    }
                }
            }
        }
        
        return false;
    }
}
