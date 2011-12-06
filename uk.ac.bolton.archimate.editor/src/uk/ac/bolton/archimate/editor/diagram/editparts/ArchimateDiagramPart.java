/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.editparts;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editpolicies.SnapFeedbackPolicy;

import uk.ac.bolton.archimate.editor.diagram.policies.ArchimateDNDEditPolicy;
import uk.ac.bolton.archimate.editor.diagram.policies.ArchimateDiagramLayoutPolicy;
import uk.ac.bolton.archimate.editor.diagram.policies.BasicContainerEditPolicy;
import uk.ac.bolton.archimate.model.IArchimateDiagramModel;
import uk.ac.bolton.archimate.model.IArchimatePackage;


/**
 * Archimate Diagram Part
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateDiagramPart extends AbstractDiagramPart {

    @Override
    public IArchimateDiagramModel getModel() {
        return (IArchimateDiagramModel)super.getModel();
    }

    @Override
    protected void eCoreChanged(Notification msg) {
        Object feature = msg.getFeature();
        
        // Viewpoint changed
        if(feature == IArchimatePackage.Literals.ARCHIMATE_DIAGRAM_MODEL__VIEWPOINT) {
            refreshChildrenFigures();
        }
        else {
            super.eCoreChanged(msg);
        }
    }
    
    @Override
    protected void createEditPolicies() {
        // Install a custom layout policy that handles dragging things around
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new ArchimateDiagramLayoutPolicy());
        
        // Install a policy for DND support
        installEditPolicy("DND", new ArchimateDNDEditPolicy());
        
        // Install a Container Policy for orphaning child parts
        installEditPolicy(EditPolicy.CONTAINER_ROLE, new BasicContainerEditPolicy());
        
        // Snap to Geometry feedback
        installEditPolicy("Snap Feedback", new SnapFeedbackPolicy()); //$NON-NLS-1$
    }

}
