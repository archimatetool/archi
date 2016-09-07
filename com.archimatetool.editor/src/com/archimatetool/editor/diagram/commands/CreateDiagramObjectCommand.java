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
import com.archimatetool.editor.ui.factory.IGraphicalObjectUIProvider;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
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
        
        // Edit Name on thread
        Display.getCurrent().asyncExec(new Runnable() {
            @Override
            public void run() {
                editNameOfNewObject();
            }
        });
    }
    
    protected void addChild() {
        fChild = (IDiagramModelObject)fRequest.getNewObject();
        
        // Default size
        if(fBounds.width == -1 && fBounds.height == -1) {
            IGraphicalObjectUIProvider provider = (IGraphicalObjectUIProvider)ObjectUIFactory.INSTANCE.getProvider(fChild);
            if(provider != null) {
                Dimension size = provider.getUserDefaultSize();
                fBounds.width = size.width;
                fBounds.height = size.height;
            }
        }
        
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
    
    /**
     * Edit name of new object if set in Preferences
     */
    protected void editNameOfNewObject() {
        if(ArchiPlugin.INSTANCE.getPreferenceStore().getBoolean(IPreferenceConstants.EDIT_NAME_ON_NEW_OBJECT)) {
            EditPartViewer viewer = fParentEditPart.getViewer();
            if(viewer != null) {
                EditPart editPart = (EditPart)viewer.getEditPartRegistry().get(fChild);
                if(editPart != null) {
                    Request directEditRequest = new Request(RequestConstants.REQ_DIRECT_EDIT);
                    editPart.performRequest(directEditRequest);
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