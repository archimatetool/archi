/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.editparts.diagram;

import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;

import com.archimatetool.editor.diagram.editparts.AbstractConnectedEditPart;
import com.archimatetool.editor.diagram.editparts.IConstrainedSizeEditPart;
import com.archimatetool.editor.diagram.figures.IDiagramModelObjectFigure;
import com.archimatetool.editor.diagram.figures.diagram.DiagramImageFigure;
import com.archimatetool.editor.diagram.policies.BasicConnectionPolicy;
import com.archimatetool.editor.diagram.policies.PartComponentEditPolicy;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelImage;



/**
 * Diagram Image Edit Part
 * 
 * @author Phillip Beauvoir
 */
public class DiagramImageEditPart extends AbstractConnectedEditPart
implements IConstrainedSizeEditPart {
    
    @Override
    protected void eCoreChanged(Notification msg) {
        Object feature = msg.getFeature();

        // Reset Image
        if(msg.getEventType() == Notification.SET && (feature == IArchimatePackage.Literals.DIAGRAM_MODEL_IMAGE_PROVIDER__IMAGE_PATH)) {
            getFigure().updateImage();
        }
        else {
            super.eCoreChanged(msg);
        }
    }

    @Override
    protected IFigure createFigure() {
        return new DiagramImageFigure(getModel());
    }
    
    @Override
    public DiagramImageFigure getFigure() {
        return (DiagramImageFigure)super.getFigure();
    }

    @Override
    public IDiagramModelImage getModel() {
        return (IDiagramModelImage)super.getModel();
    }

    @Override
    protected List<?> getModelChildren() {
        return Collections.EMPTY_LIST;
    }

    @Override
    protected void createEditPolicies() {
        // Allow parts to be joined together
        installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new BasicConnectionPolicy());
        
        // Add a policy to handle editing the Parts (for example, deleting a part)
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new PartComponentEditPolicy());

        updateEditPolicies();
    }
    
    @Override
    public void performRequest(Request req) {
        if(req.getType() == RequestConstants.REQ_OPEN) {
            showPropertiesView();
        }
    }

    @Override
    protected void refreshFigure() {
        ((IDiagramModelObjectFigure)getFigure()).refreshVisuals();
    }
}
