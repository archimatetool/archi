/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK. All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * License which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.commands;

import org.eclipse.draw2d.geometry.Dimension;

import uk.ac.bolton.archimate.model.IArchimateFactory;
import uk.ac.bolton.archimate.model.IDiagramModelBendpoint;

/**
 * Description
 * 
 * @author Phillip Beauvoir
 */
public class CreateBendpointCommand extends BendpointCommand implements IAnimatableCommand {
    
    private IDiagramModelBendpoint fBendpoint;
    
    public CreateBendpointCommand() {
        super(Messages.CreateBendpointCommand_0);
    }

    @Override
    public void execute() {
        fBendpoint = IArchimateFactory.eINSTANCE.createDiagramModelBendpoint();
        
        Dimension dim1 = getFirstRelativeDimension();
        fBendpoint.setStartX(dim1.width);
        fBendpoint.setStartY(dim1.height);
        
        Dimension dim2 = getSecondRelativeDimension();
        fBendpoint.setEndX(dim2.width);
        fBendpoint.setEndY(dim2.height);
        
        redo();
    }

    @Override
    public void undo() {
        getDiagramModelConnection().getBendpoints().remove(fBendpoint);
    }

    @Override
    public void redo() {
        getDiagramModelConnection().getBendpoints().add(getIndex(), fBendpoint);
    }
}
