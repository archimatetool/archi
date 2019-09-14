/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.editparts.diagram;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.requests.LocationRequest;
import org.eclipse.gef.tools.DirectEditManager;

import com.archimatetool.editor.diagram.directedit.MultiLineTextDirectEditManager;
import com.archimatetool.editor.diagram.editparts.AbstractConnectedEditPart;
import com.archimatetool.editor.diagram.figures.diagram.DiagramModelReferenceFigure;
import com.archimatetool.editor.diagram.policies.ArchimateDiagramConnectionPolicy;
import com.archimatetool.editor.diagram.policies.PartComponentEditPolicy;
import com.archimatetool.editor.diagram.policies.PartDirectEditTitlePolicy;
import com.archimatetool.editor.ui.services.EditorManager;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelReference;



/**
 * Diagram Model Reference Edit Part
 * 
 * @author Phillip Beauvoir
 */
public class DiagramModelReferenceEditPart extends AbstractConnectedEditPart {

    @Override
    protected void addECoreAdapter() {
        super.addECoreAdapter();
        
        // Listen to referenced model
        IDiagramModel ref = ((IDiagramModelReference)getModel()).getReferencedModel();
        if(ref != null) {
            ref.eAdapters().add(getECoreAdapter());
        }
    }
    
    @Override
    protected void removeECoreAdapter() {
        super.removeECoreAdapter();
        
        // Unlisten to referenced model
        IDiagramModel ref = ((IDiagramModelReference)getModel()).getReferencedModel();
        if(ref != null) {
            ref.eAdapters().remove(getECoreAdapter());
        }
    }

    @Override
    protected void createEditPolicies() {
        // Allow parts to be connected
        installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new ArchimateDiagramConnectionPolicy());
        
        // Add a policy to handle directly editing the name
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new PartDirectEditTitlePolicy());

        // Add a policy to handle editing the Parts (for example, deleting a part)
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new PartComponentEditPolicy());
    }

    @Override
    protected IFigure createFigure() {
        DiagramModelReferenceFigure figure = new DiagramModelReferenceFigure(getModel());
        return figure;
    }

    @Override
    protected void refreshFigure() {
        getFigure().refreshVisuals();
    }

    @Override
    public void performRequest(Request request) {
        // REQ_DIRECT_EDIT is Single-click when already selected or a Rename action
        // REQ_OPEN is Double-click
        
        // Open Diagram if not in Full Screen Mode
        if(request.getType() == RequestConstants.REQ_OPEN) {
            if(!isInFullScreenMode()) {
                EditorManager.openDiagramEditor(((IDiagramModelReference)getModel()).getReferencedModel());
            }
        }
        else if(request.getType() == RequestConstants.REQ_DIRECT_EDIT) {
            // Edit the text control if we clicked on it
            if(request instanceof LocationRequest) {
                if(getFigure().didClickTextControl(((LocationRequest)request).getLocation().getCopy())) {
                    createDirectEditManager().show();
                }
            }
            // Rename Action
            else {
                createDirectEditManager().show();
            }
        }
    }

    protected DirectEditManager createDirectEditManager() {
        //return new LabelDirectEditManager(this, getFigure().getTextControl(), getModel().getName());
        return new MultiLineTextDirectEditManager(this, true);
    }
}
