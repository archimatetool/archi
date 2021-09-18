/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.osgi.util.NLS;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IBounds;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelObject;



/**
 * Command for moving/resizing an Object
 * 
 * @author Phillip Beauvoir
 */
public class SetConstraintObjectCommand extends CompoundCommand implements IAnimatableCommand {

    private IDiagramModelObject fObject;
    private IBounds fNewPos, fOldPos;
    private ChangeBoundsRequest fRequest;
    
    /**
     * This constructor is used to simply set bounds without regard to children
     */
    public SetConstraintObjectCommand(IDiagramModelObject object, IBounds bounds) {
        fObject = object;
        fOldPos = object.getBounds();
        fNewPos = bounds;
        setLabel(NLS.bind(Messages.SetConstraintObjectCommand_0, ArchiLabelProvider.INSTANCE.getLabel(object)));
    }
    
    /**
     * This constructor is used when the bounds can be resized as well as moved
     * Then we can adjust the bounds of the children
     */
    public SetConstraintObjectCommand(ChangeBoundsRequest request, IDiagramModelObject object, Rectangle bounds) {
        this(object, IArchimateFactory.eINSTANCE.createBounds(bounds.x, bounds.y, bounds.width, bounds.height));
        fRequest = request;
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
    public boolean canRedo() {
        return canExecute();
    }
    
    @Override
    public boolean canUndo() {
        return true;
    }
    
    @Override
    public void execute() {
        fObject.setBounds(fNewPos);
        createChildConstraintCommands();
        super.execute();
    }
    
    @Override
    public void undo() {
        super.undo();
        fObject.setBounds(fOldPos);
    }
    
    @Override
    public void redo() {
        // Don't just call execute() as the user might have changed preference in between undo and redo!
        fObject.setBounds(fNewPos);
        super.execute();
    }
    
    /**
     * If the option is set, move children in relation to new resize x, y
     */
    protected void createChildConstraintCommands() {
        if(ArchiPlugin.PREFERENCES.getInt(IPreferenceConstants.DIAGRAM_OBJECT_RESIZE_BEHAVIOUR) != 0 ||
                fRequest == null ||
                !(fObject instanceof IDiagramModelContainer)) {
            return;
        }
        
        IDiagramModelContainer container = (IDiagramModelContainer)fObject;
        
        if(container.getChildren().isEmpty()) {
            return;
        }
        
        if((fRequest.getResizeDirection() & PositionConstants.NORTH) == 0 &&
                (fRequest.getResizeDirection() & PositionConstants.WEST) == 0) {
            return;
        }
        
        // Use a map of object->bounds so that setting bounds in both north and west cases are cumulative
        Map<IDiagramModelObject, IBounds> map = new HashMap<>();
        for(IDiagramModelObject dmo : container.getChildren()) {
            map.put(dmo, dmo.getBounds().getCopy());
        }
        
        if((fRequest.getResizeDirection() & PositionConstants.NORTH) != 0) {
            int yDiff = fOldPos.getY() - fNewPos.getY();
            if(yDiff != 0) {
                // Calculate offset to move all children if a child y pos < new y pos
                int topOffset = 0;
                for(Entry<IDiagramModelObject, IBounds> dmo : map.entrySet()) {
                    topOffset = Math.min(topOffset, dmo.getValue().getY() + yDiff);
                }
                
                // Children
                for(Entry<IDiagramModelObject, IBounds> dmo : map.entrySet()) {
                    IBounds newbounds = dmo.getValue();
                    newbounds.setY(newbounds.getY() + yDiff - topOffset);
                }
            }
        }
        
        if((fRequest.getResizeDirection() & PositionConstants.WEST) != 0) {
            int xDiff = fOldPos.getX() - fNewPos.getX();
            if(xDiff != 0) {
                // Calculate offset to move all children if a child x pos < new x pos
                int leftOffset = 0;
                for(Entry<IDiagramModelObject, IBounds> dmo : map.entrySet()) {
                    leftOffset = Math.min(leftOffset, dmo.getValue().getX() + xDiff);
                }
                
                // Children
                for(Entry<IDiagramModelObject, IBounds> dmo : map.entrySet()) {
                    IBounds newbounds = dmo.getValue();
                    newbounds.setX(newbounds.getX() + xDiff - leftOffset);
                }
            }
        }
        
        // Add Commands
        for(Entry<IDiagramModelObject, IBounds> dmo : map.entrySet()) {
            add(new SetConstraintObjectCommand(dmo.getKey(), dmo.getValue()));
        }
    }
    
    @Override
    public void dispose() {
        fObject = null;
        fNewPos = null;
        fOldPos = null;
        fRequest = null;
    }
}