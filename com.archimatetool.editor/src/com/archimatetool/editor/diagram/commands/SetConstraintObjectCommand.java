/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.commands;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.osgi.util.NLS;

import com.archimatetool.editor.ui.ArchimateLabelProvider;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IBounds;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelObject;



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