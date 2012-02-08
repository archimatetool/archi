/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.commands;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.osgi.util.NLS;

import uk.ac.bolton.archimate.editor.ui.ArchimateLabelProvider;
import uk.ac.bolton.archimate.model.IArchimateFactory;
import uk.ac.bolton.archimate.model.IBounds;
import uk.ac.bolton.archimate.model.IDiagramModel;
import uk.ac.bolton.archimate.model.IDiagramModelContainer;
import uk.ac.bolton.archimate.model.IDiagramModelObject;


/**
 * Command for moving/resizing an Object
 * Over-ride canExecute() if you want more control
 * 
 * @author Phillip Beauvoir
 */
public class SetConstraintObjectCommand extends Command implements IAnimatableCommand {

    private IDiagramModelObject fObject;
    private IBounds fNewPos, fOldPos;
    
    public SetConstraintObjectCommand(IDiagramModelObject object, Rectangle bounds) {
        this(object, IArchimateFactory.eINSTANCE.createBounds(bounds.x, bounds.y, bounds.width, bounds.height));
    }

    public SetConstraintObjectCommand(IDiagramModelObject object, IBounds bounds) {
        fObject = object;
        fOldPos = object.getBounds();
        fNewPos = bounds;
        setLabel(NLS.bind(Messages.SetConstraintObjectCommand_0, ArchimateLabelProvider.INSTANCE.getLabel(object)));
    }
    
    @Override
    public boolean canExecute() {
        // If parent is Container but not a Diagram, limit it
        if(fObject.eContainer() instanceof IDiagramModelContainer && !(fObject.eContainer() instanceof IDiagramModel)) {
            if(fNewPos.getX() < 0) {
                return false;
            }
            if(fNewPos.getY() < 0) {
                return false;
            }
            
            // We can't figure out width and height constraints because these can be -1
            // both on the Group and its children
        }
        
        return true;
    }
    
    @Override
    public void execute() {
        fObject.setBounds(fNewPos);
    }

    @Override
    public void undo() {
        fObject.setBounds(fOldPos);
    }
    
    @Override
    public void dispose() {
        fObject = null;
        fNewPos = null;
        fOldPos = null;
    }
}