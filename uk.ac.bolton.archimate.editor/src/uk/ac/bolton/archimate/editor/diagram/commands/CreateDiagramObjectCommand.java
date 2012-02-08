/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.commands;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.osgi.util.NLS;

import uk.ac.bolton.archimate.editor.ui.ArchimateLabelProvider;
import uk.ac.bolton.archimate.model.IDiagramModelContainer;
import uk.ac.bolton.archimate.model.IDiagramModelObject;


/**
 * Create New Diagram Object Command
 * 
 * @author Phillip Beauvoir
 */
public class CreateDiagramObjectCommand extends Command {
    
    protected CreateRequest fRequest;
    protected IDiagramModelContainer fParent;
    protected IDiagramModelObject fChild;
    protected Rectangle fBounds;

    public CreateDiagramObjectCommand(IDiagramModelContainer parent, CreateRequest request, Rectangle bounds) {
        fParent = parent;
        fRequest = request;
        fBounds = bounds;
    }
    
    @Override
    public String getLabel() {
        return NLS.bind(Messages.CreateDiagramObjectCommand_0, ArchimateLabelProvider.INSTANCE.getLabel(fChild));
    }

    @Override
    public void execute() {
        fChild = (IDiagramModelObject)fRequest.getNewObject();
        fChild.setBounds(fBounds.x, fBounds.y, fBounds.width, fBounds.height);
        redo();
    }

    @Override
    public void undo() {
        fParent.getChildren().remove(fChild);
    }

    @Override
    public void redo() {
        fParent.getChildren().add(fChild);
    }
    
    @Override
    public void dispose() {
        fRequest = null;
        fParent = null;
        fChild = null;
        fBounds = null;
    }
}