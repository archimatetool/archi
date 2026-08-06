/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;

import com.archimatetool.editor.diagram.commands.DiagramCommandFactory;
import com.archimatetool.editor.model.commands.DeleteArchimateElementCommand;
import com.archimatetool.editor.model.commands.DeleteArchimateRelationshipCommand;
import com.archimatetool.editor.model.commands.NonNotifyingCompoundCommand;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IDiagramModelArchimateComponent;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.util.ArchimateModelUtils;



/**
 * Delete from Model Action
 * 
 * @author Phillip Beauvoir
 */
public class DeleteFromModelAction extends SelectionAction {
    
    public static final String ID = "com.archimatetool.editor.action.deleteFromModel"; //$NON-NLS-1$
    public static final String TEXT = Messages.DeleteFromModelAction_0;
    
    public DeleteFromModelAction(IWorkbenchPart part) {
        super(part);
        setText(TEXT);
        setId(ID);
        setActionDefinitionId(ID); // register key binding
    }

    @Override
    protected boolean calculateEnabled() {
        for(EditPart editPart : getSelectedEditParts()) {
            if(editPart.getModel() instanceof IDiagramModelArchimateComponent) {
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public void run() {
        Set<IArchimateConcept> conceptsToDelete = new HashSet<>();
        Set<IDiagramModelArchimateComponent> diagramComponentsToDelete = new HashSet<>();
        boolean hasMultiple = false;
        
        // Gather referenced model concepts and connected relationships
        for(EditPart editPart : getSelectedEditParts()) {
            if(editPart.getModel() instanceof IDiagramModelArchimateComponent component) {
                conceptsToDelete.add(component.getArchimateConcept());
                addRelationshipsToDelete(component.getArchimateConcept(), conceptsToDelete);
            }
        }
        
        // Gather referenced diagram components
        for(IArchimateConcept concept : conceptsToDelete) {
            List<? extends IDiagramModelArchimateComponent> list = concept.getReferencingDiagramComponents();
            diagramComponentsToDelete.addAll(list);
            if(list.size() > 1) {
                hasMultiple = true;
            }
        }
        
        // If any of the concepts is referenced more than once in Views warn the user
        if(hasMultiple) {
            if(!MessageDialog.openQuestion(Display.getDefault().getActiveShell(),
                    Messages.DeleteFromModelAction_0,
                    Messages.DeleteFromModelAction_1 +
                    "\n" + //$NON-NLS-1$
                    Messages.DeleteFromModelAction_2)) {
                return;
            }
        }
        
        // Create commands
        CompoundCommand compoundCommand = new NonNotifyingCompoundCommand(TEXT);
        
        // Concepts to delete
        for(IArchimateConcept archimateConcept : conceptsToDelete) {
            if(archimateConcept instanceof IArchimateElement element) {
                compoundCommand.add(new DeleteArchimateElementCommand(element));
            }
            else if(archimateConcept instanceof IArchimateRelationship relationship) {
                compoundCommand.add(new DeleteArchimateRelationshipCommand(relationship));
            }
        }
        
        // Diagram components to delete
        for(IDiagramModelArchimateComponent component : diagramComponentsToDelete) {
            if(component instanceof IDiagramModelObject dmo) {
                compoundCommand.add(DiagramCommandFactory.createDeleteDiagramObjectCommand(dmo));
            }
            else if(component instanceof IDiagramModelArchimateConnection dmc) {
                compoundCommand.add(DiagramCommandFactory.createDeleteDiagramConnectionCommand(dmc));
            }
        }
        
        BusyIndicator.showWhile(null, new Runnable() {
            @Override
            public void run() {
                execute(compoundCommand);
            }
        });
    }
    
    private void addRelationshipsToDelete(IArchimateConcept concept, Set<IArchimateConcept> conceptsToDelete) {
        for(IArchimateRelationship relationship : ArchimateModelUtils.getAllRelationshipsForConcept(concept)) {
            conceptsToDelete.add(relationship);
            addRelationshipsToDelete(relationship, conceptsToDelete); // recurse
        }
    }
}
