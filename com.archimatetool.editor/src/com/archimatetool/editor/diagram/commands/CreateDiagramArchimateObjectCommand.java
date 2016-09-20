/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.commands;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.swt.widgets.Display;

import com.archimatetool.editor.preferences.ConnectionPreferences;
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
    protected CompoundCommand subCommand = new CompoundCommand();
    
    public CreateDiagramArchimateObjectCommand(EditPart parentEditPart, CreateRequest request, Rectangle bounds) {
        super(parentEditPart, request, bounds);
    }
    
    @Override
    public void execute() {
        addChild();
        
        // Create Nested Relation as well if prefs and both types are Archimate
        if(ConnectionPreferences.createRelationWhenAddingNewElement() &&
                                    fParent instanceof IDiagramModelArchimateObject && fChild instanceof IDiagramModelArchimateObject) {
            // We need to have this on a thread.  There has been a case of the odd glitch causing a NPE.
            Display.getCurrent().asyncExec(new Runnable() {
                @Override
                public void run() {
                    Command cmd = new CreateNestedArchimateConnectionsWithDialogCommand((IDiagramModelArchimateObject)fParent,
                                                            (IDiagramModelArchimateObject)fChild);
                    subCommand.add(cmd);
                    subCommand.execute();

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
        ((IDiagramModelArchimateObject)fChild).removeArchimateConceptFromModel();
        
        subCommand.undo();
    }

    @Override
    public void redo() {
        // This first
        super.redo();

        // Add the Archimate model object to a default folder
        ((IDiagramModelArchimateObject)fChild).addArchimateConceptToModel(null);
        
        subCommand.redo();
    }
    
    @Override
    public void dispose() {
        super.dispose();
        subCommand.dispose();
    }
}