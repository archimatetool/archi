/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.editparts;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editpolicies.SnapFeedbackPolicy;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.diagram.policies.ArchimateDNDEditPolicy;
import com.archimatetool.editor.diagram.policies.ArchimateDiagramLayoutPolicy;
import com.archimatetool.editor.diagram.policies.BasicContainerEditPolicy;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimatePackage;



/**
 * Archimate Diagram Part
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateDiagramPart extends AbstractDiagramPart {
    
    public ArchimateDiagramPart() {
        // Add a Viewpoint Child EditPart Filter if set in Preferences (hides rather than ghosts)
        if(!ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.VIEWPOINTS_GHOST_DIAGRAM_ELEMENTS)) {
            addEditPartFilter(new ViewpointEditPartFilter());
        }
        // Add a Nested Connection Filter
        addEditPartFilter(new NestedConnectionEditPartFilter());
    }

    @Override
    public IArchimateDiagramModel getModel() {
        return (IArchimateDiagramModel)super.getModel();
    }

    @Override
    protected void eCoreChanged(Notification msg) {
        Object feature = msg.getFeature();
        
        // Viewpoint changed
        if(feature == IArchimatePackage.Literals.ARCHIMATE_DIAGRAM_MODEL__VIEWPOINT &&
                ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.VIEWPOINTS_GHOST_DIAGRAM_ELEMENTS)) {
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
        installEditPolicy("DND", new ArchimateDNDEditPolicy()); //$NON-NLS-1$
        
        // Install a Container Policy for orphaning child parts
        installEditPolicy(EditPolicy.CONTAINER_ROLE, new BasicContainerEditPolicy());
        
        // Snap to Geometry feedback
        installEditPolicy("Snap Feedback", new SnapFeedbackPolicy()); //$NON-NLS-1$
    }

}
