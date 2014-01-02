/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.commands;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;

import com.archimatetool.editor.preferences.ConnectionPreferences;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IDiagramModelArchimateObject;



/**
 * Create New Diagram Archimate Object Command
 * 
 * @author Phillip Beauvoir
 */
public class CreateDiagramArchimateObjectCommand extends CreateDiagramObjectCommand {
    
    /**
     * Create Nested Relation Command
     */
    protected CreateRelationCommand fCreateRelationSubCommand;
    
    public CreateDiagramArchimateObjectCommand(EditPart parentEditPart, CreateRequest request, Rectangle bounds) {
        super(parentEditPart, request, bounds);
    }
    
    @Override
    public String getLabel() {
        String label = super.getLabel();
        if(fCreateRelationSubCommand != null) {
            label = NLS.bind(Messages.CreateDiagramArchimateObjectCommand_0, label, fCreateRelationSubCommand.getRelationshipCreated().getName());
        }
        return label;
    }

    @Override
    public void execute() {
        addChild();
        
        // Create Nested Relation as well
        if(ConnectionPreferences.createRelationWhenAddingNewElement()) {
            // We need to have this on a thread.  There has been a case of the odd glitch causing a NPE.
            Display.getCurrent().asyncExec(new Runnable() {
                @Override
                public void run() {
                    if(fParent instanceof IDiagramModelArchimateObject) {
                        IArchimateElement parentElement = ((IDiagramModelArchimateObject)fParent).getArchimateElement();
                        IArchimateElement childElement = ((IDiagramModelArchimateObject)fChild).getArchimateElement();
                        fCreateRelationSubCommand = (CreateRelationCommand)DiagramCommandFactory.createNewNestedRelationCommandWithDialog(parentElement,
                                new IArchimateElement[] {childElement});
                        if(fCreateRelationSubCommand != null) {
                            fCreateRelationSubCommand.execute();
                        }
                    }

                    // Edit name on this thread
                    editNameOfNewObject();
                }
            });
        }
        else {
            // Edit name on a new thread
            Display.getCurrent().asyncExec(new Runnable() {
                @Override
                public void run() {
                    editNameOfNewObject();
                }
            });
        }
    }
    
    @Override
    public void undo() {
        super.undo();
        
        // Remove the Archimate model object from its containing folder
        ((IDiagramModelArchimateObject)fChild).removeArchimateElementFromModel();
        
        if(fCreateRelationSubCommand != null) {
            fCreateRelationSubCommand.undo();
        }
    }

    @Override
    public void redo() {
        // This first
        super.redo();

        // Add the Archimate model object to a default folder
        ((IDiagramModelArchimateObject)fChild).addArchimateElementToModel(null);
        
        if(fCreateRelationSubCommand != null) {
            fCreateRelationSubCommand.redo();
        }
    }
    
    @Override
    public void dispose() {
        super.dispose();
        if(fCreateRelationSubCommand != null) {
            fCreateRelationSubCommand.dispose();
            fCreateRelationSubCommand = null;
        }
    }
}