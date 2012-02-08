/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.commands;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;

import uk.ac.bolton.archimate.editor.preferences.ConnectionPreferences;
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;
import uk.ac.bolton.archimate.model.IDiagramModelContainer;


/**
 * Create New Diagram Archimate Object Command
 * 
 * @author Phillip Beauvoir
 */
public class CreateDiagramArchimateObjectCommand extends CreateDiagramObjectCommand {
    
    protected CreateRelationCommand fSubCommand;

    public CreateDiagramArchimateObjectCommand(IDiagramModelContainer parent, CreateRequest request, Rectangle bounds) {
        super(parent, request, bounds);
    }
    
    @Override
    public String getLabel() {
        String label = super.getLabel();
        if(fSubCommand != null) {
            label = NLS.bind(Messages.CreateDiagramArchimateObjectCommand_0, label, fSubCommand.getRelationshipCreated().getName());
        }
        return label;
    }

    @Override
    public void execute() {
        super.execute();
        
        // Create Nested Relation as well
        if(ConnectionPreferences.createRelationWhenAddingNewElement()) {
            if(fParent instanceof IDiagramModelArchimateObject) {
                // TODO - Do we need this out on a thread?  There has been a case of the odd glitch causing a NPE.
                Display.getCurrent().asyncExec(new Runnable() {
                    @Override
                    public void run() {
                        IArchimateElement parentElement = ((IDiagramModelArchimateObject)fParent).getArchimateElement();
                        IArchimateElement childElement = ((IDiagramModelArchimateObject)fChild).getArchimateElement();
                        fSubCommand = (CreateRelationCommand)DiagramCommandFactory.createNewNestedRelationCommandWithDialog(parentElement,
                                            new IArchimateElement[] {childElement});
                        if(fSubCommand != null) {
                            fSubCommand.execute();
                        }
                    }
                });
            }
        }
    }

    @Override
    public void undo() {
        super.undo();
        
        // Remove the Archimate model object from its containing folder
        ((IDiagramModelArchimateObject)fChild).removeArchimateElementFromModel();
        
        if(fSubCommand != null) {
            fSubCommand.undo();
        }
    }

    @Override
    public void redo() {
        // This first
        super.redo();

        // Add the Archimate model object to a default folder
        ((IDiagramModelArchimateObject)fChild).addArchimateElementToModel(null);
        
        if(fSubCommand != null) {
            fSubCommand.redo();
        }
    }
    
    @Override
    public void dispose() {
        super.dispose();
        if(fSubCommand != null) {
            fSubCommand.dispose();
            fSubCommand = null;
        }
    }
}