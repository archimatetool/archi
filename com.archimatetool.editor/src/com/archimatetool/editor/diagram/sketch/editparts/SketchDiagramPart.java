/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.sketch.editparts;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editpolicies.SnapFeedbackPolicy;

import com.archimatetool.editor.diagram.editparts.AbstractDiagramPart;
import com.archimatetool.editor.diagram.policies.BasicContainerEditPolicy;
import com.archimatetool.editor.diagram.policies.DiagramLayoutPolicy;
import com.archimatetool.editor.diagram.sketch.ISketchEditor;
import com.archimatetool.editor.diagram.sketch.policies.SketchDNDEditPolicy;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.ISketchModel;




/**
 * Sketch Diagram Part
 * 
 * @author Phillip Beauvoir
 */
public class SketchDiagramPart extends AbstractDiagramPart {
    
    @Override
    public ISketchModel getModel() {
        return (ISketchModel)super.getModel();
    }
    
    @Override
    protected void eCoreChanged(Notification msg) {
        switch(msg.getEventType()) {
            case Notification.SET:
                Object feature = msg.getFeature();
                // Sketch model background
                if(feature == IArchimatePackage.Literals.SKETCH_MODEL__BACKGROUND) {
                    setBackgroundImage(msg.getNewValue());
                }
                break;

            default:
                break;
        }
        
        super.eCoreChanged(msg);
    }

    /**
     * Update the background image
     */
    private void setBackgroundImage(Object newValue) {
        ISketchEditor editor = (ISketchEditor)((DefaultEditDomain)getViewer().getEditDomain()).getEditorPart();
        editor.updateBackgroundImage();
    }

    @Override
    protected void createEditPolicies() {
        // Install a custom layout policy that handles dragging things around
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new DiagramLayoutPolicy());
        
        // Install a policy for DND support
        installEditPolicy("DND", new SketchDNDEditPolicy()); //$NON-NLS-1$
        
        // And we need to install this Group Container Policy here as well as in the GroupEditpart
        installEditPolicy(EditPolicy.CONTAINER_ROLE, new BasicContainerEditPolicy());
        
        // Snap to Geometry feedback
        installEditPolicy("Snap Feedback", new SnapFeedbackPolicy()); //$NON-NLS-1$
    }
}
