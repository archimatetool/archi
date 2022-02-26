/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.commands;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelObject;



/**
 * Create New Diagram Object Command
 * 
 * @author Phillip Beauvoir
 */
public class CreateDiagramObjectCommand extends Command {
    
    protected CreateRequest fRequest;
    protected EditPart fParentEditPart;
    protected IDiagramModelContainer fParent;
    protected IDiagramModelObject fChild;
    protected Rectangle fBounds;

    public CreateDiagramObjectCommand(EditPart parentEditPart, CreateRequest request, Rectangle bounds) {
        fParentEditPart = parentEditPart;
        fParent = (IDiagramModelContainer)fParentEditPart.getModel();
        fRequest = request;
        fBounds = bounds;
    }
    
    @Override
    public String getLabel() {
        return NLS.bind(Messages.CreateDiagramObjectCommand_0, ArchiLabelProvider.INSTANCE.getLabel(fChild));
    }

    @Override
    public void execute() {
        addChild();
        
        // Edit Name
        editNameOfNewObject();
    }
    
    protected void addChild() {
        // Create new object from the factory
        fChild = (IDiagramModelObject)fRequest.getNewObject();
        
        // Set the location from the supplied bounds in the creation request
        fChild.getBounds().setLocation(fBounds.x, fBounds.y);
        
        // Sub-classes might have a preferred size...
        Dimension preferredSize = getPreferredSize();
        if(preferredSize != null) {
            fChild.getBounds().setSize(preferredSize.width, preferredSize.height);
        }
        // Else the new width and height can come from the creation request
        else if(fBounds.width != -1 && fBounds.height != -1) {
            fChild.getBounds().setSize(fBounds.width, fBounds.height);
        }
        // Otherwise the width and height should already be set in the ICreationFactory

        // Redo, so sub-classes can over-ride
        redo();
    }
    
    protected Dimension getPreferredSize() {
        return null;
    }

    @Override
    public void undo() {
        fParent.getChildren().remove(fChild);
    }

    @Override
    public void redo() {
        fParent.getChildren().add(fChild);
    }
    
    /**
     * Edit name of new object if set in Preferences
     */
    protected void editNameOfNewObject() {
        if(ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.EDIT_NAME_ON_NEW_OBJECT)) {
            EditPartViewer viewer = fParentEditPart.getViewer();
            if(viewer != null) {
                EditPart editPart = (EditPart)viewer.getEditPartRegistry().get(fChild);
                if(editPart != null) {
                    // Async this
                    Display.getCurrent().asyncExec(() -> {
                        Request directEditRequest = new Request(RequestConstants.REQ_DIRECT_EDIT);
                        editPart.performRequest(directEditRequest);
                    });
                }
            }
        }
    }
    
    @Override
    public void dispose() {
        fRequest = null;
        fParent = null;
        fChild = null;
        fBounds = null;
    }
}