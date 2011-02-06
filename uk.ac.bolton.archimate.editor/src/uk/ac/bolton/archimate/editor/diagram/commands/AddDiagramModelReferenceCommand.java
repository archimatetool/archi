/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.commands;

import org.eclipse.gef.commands.Command;

import uk.ac.bolton.archimate.model.IArchimateFactory;
import uk.ac.bolton.archimate.model.IDiagramModel;
import uk.ac.bolton.archimate.model.IDiagramModelContainer;
import uk.ac.bolton.archimate.model.IDiagramModelReference;


/**
 * Add Diagram Model Reference Command
 * Use when dragging and dropping a Diagram Model from the tree to the diagram.
 * 
 * @author Phillip Beauvoir
 */
public class AddDiagramModelReferenceCommand extends Command {
    
    private IDiagramModelContainer fParent;
    private IDiagramModel fDiagramModel;
    private IDiagramModelReference fReference;
    private int fX, fY;

    public AddDiagramModelReferenceCommand(IDiagramModelContainer parent, IDiagramModel diagramModel, int x, int y) {
        fParent = parent;
        fDiagramModel = diagramModel;
        fX = x;
        fY = y;
        setLabel("Add view reference");
    }

    @Override
    public void execute() {
        fReference = IArchimateFactory.eINSTANCE.createDiagramModelReference();
        fReference.setReferencedModel(fDiagramModel);
        fReference.setBounds(fX, fY, -1, -1);
        fParent.getChildren().add(fReference);
    }

    @Override
    public void undo() {
        if(fReference != null) {
            fParent.getChildren().remove(fReference);
        }
    }
    
    @Override
    public void redo() {
        if(fReference != null) {
            fParent.getChildren().add(fReference);
        }
    }
    
    @Override
    public void dispose() {
        fParent = null;
        fDiagramModel = null;
        fReference = null;
    }
}