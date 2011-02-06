/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.commands;

import org.eclipse.gef.commands.Command;

import uk.ac.bolton.archimate.editor.model.DiagramModelUtils;
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IArchimateFactory;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;
import uk.ac.bolton.archimate.model.IDiagramModelContainer;


/**
 * Add Object Command
 * Use when dragging and dropping an ArchimateElement from the tree to the diagram.
 * 
 * This is to be only used as part of a CompoundCommand because it might not execute if the object
 * has been added already to the diagram from a previous Command in the CompoundCommand
 * 
 * @author Phillip Beauvoir
 */
public class AddDiagramObjectCommand extends Command {
    
    private IDiagramModelContainer fParent;
    private IArchimateElement fElement;
    private IDiagramModelArchimateObject fChild;
    private int fX, fY;

    public AddDiagramObjectCommand(IDiagramModelContainer parent, IArchimateElement element, int x, int y) {
        fParent = parent;
        fElement = element;
        fX = x;
        fY = y;
        setLabel("Add" + " " + element.getName());
    }

    @Override
    public void execute() {
        if(DiagramModelUtils.findDiagramModelComponentForElement(fParent.getDiagramModel(), fElement) == null) { // already added?
            fChild = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
            fChild.setArchimateElement(fElement);
            fChild.setBounds(fX, fY, -1, -1);
            fParent.getChildren().add(fChild);
        }
        else {
            System.err.println(fElement + " already there in" + getClass());
        }
    }

    @Override
    public void undo() {
        if(fChild != null) {
            fParent.getChildren().remove(fChild);
        }
    }
    
    @Override
    public void redo() {
        if(fChild != null) {
            fParent.getChildren().add(fChild);
        }
    }
    
    @Override
    public void dispose() {
        fParent = null;
        fElement = null;
        fChild = null;
    }
}