/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

import com.archimatetool.editor.diagram.commands.InvertConnectionCommand;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.util.ArchimateModelUtils;


/**
 * Invert Connection Direction Action
 *
 * Inverts the direction of selected ArchiMate connections by swapping source and target.
 */
public class InvertConnectionAction extends SelectionAction {

    public static final String ID = "com.archimatetool.editor.action.invertConnection"; //$NON-NLS-1$
    public static final String TEXT = Messages.InvertConnectionAction_0;

    public InvertConnectionAction(IWorkbenchPart part) {
        super(part);
        setText(TEXT);
        setId(ID);
        setActionDefinitionId(ID);
    }

    @Override
    protected boolean calculateEnabled() {
        return !getValidRelationships().isEmpty();
    }

    @Override
    public void run() {
        Set<IArchimateRelationship> relationships = getValidRelationships();
        if(relationships.isEmpty()) {
            return;
        }

        CompoundCommand compoundCommand = new CompoundCommand(TEXT);
        for(IArchimateRelationship relationship : relationships) {
            compoundCommand.add(new InvertConnectionCommand(relationship));
        }

        execute(compoundCommand);
    }

    private Set<IArchimateRelationship> getValidRelationships() {
        Set<IArchimateRelationship> result = new HashSet<>();

        for(Object object : getSelectedObjects()) {
            if(!(object instanceof EditPart editPart && editPart.getModel() instanceof IDiagramModelArchimateConnection connection)) {
                continue;
            }

            IArchimateRelationship relationship = connection.getArchimateRelationship();

            // Skip if null or already validated — avoids redundant matrix lookup
            if(relationship == null || result.contains(relationship)) {
                continue;
            }

            // Check if the inverted relationship would be valid
            if(ArchimateModelUtils.isValidRelationship(relationship.getTarget().eClass(),
                    relationship.getSource().eClass(), relationship.eClass())) {
                result.add(relationship);
            }
        }

        return result;
    }
}
