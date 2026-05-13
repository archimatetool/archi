/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelConnection;



/**
 * Connection Label Position Section
 * 
 * @author Phillip Beauvoir
 */
public class DiagramConnectionLabelPositionSection extends AbstractMultiControlSection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$

    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return object instanceof IDiagramModelConnection;
        }

        @Override
        public Class<?> getAdaptableType() {
            return IDiagramModelConnection.class;
        }
    }

    private DiagramConnectionLabelPositionComposite positionComposite;
    private DiagramConnectionLabelRelativePositionComposite positionRelativeComposite;
    
    @Override
    protected void createControls(Composite parent) {
        init(parent, 2);
        
        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    @Override
    protected void notifyChanged(Notification msg) {
        Object feature = msg.getFeature();

        if(feature == IArchimatePackage.Literals.DIAGRAM_MODEL_CONNECTION__TEXT_POSITION) {
            if(!isExecutingCommand()) {
                updateLabelPositionControl();
            }
            // Update this for enabled state
            if(positionRelativeComposite != null) {
                positionRelativeComposite.updateControl();
            }
        }
        else if(isFeatureNotification(msg, IDiagramModelConnection.FEATURE_TEXT_RELATIVE_POSITION)) {
            if(!isExecutingCommand()) {
                updateLabelRelativePositionControl();
            }
        }
        else if(feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
            update();
        }
    }

    @Override
    protected void update() {
        updateLabelPositionControl();
        updateLabelRelativePositionControl();
    }
    
    private void updateLabelPositionControl() {
        boolean show = shouldShowControl(IArchimatePackage.Literals.DIAGRAM_MODEL_CONNECTION__TEXT_POSITION.getName());
        
        if(show) {
            if(positionComposite == null) {
                positionComposite = new DiagramConnectionLabelPositionComposite(this, parentComposite);
                layout();
            }
            positionComposite.updateControl();
        }
        else if(positionComposite != null) {
            positionComposite.dispose();
            positionComposite = null;
            layout();
        }
    }
    
    private void updateLabelRelativePositionControl() {
        boolean show = shouldShowControl(IDiagramModelConnection.FEATURE_TEXT_RELATIVE_POSITION);
        
        if(show) {
            if(positionRelativeComposite == null) {
                positionRelativeComposite = new DiagramConnectionLabelRelativePositionComposite(this, parentComposite);
                layout();
            }
            positionRelativeComposite.updateControl();
        }
        else if(positionRelativeComposite != null) {
            positionRelativeComposite.dispose();
            positionRelativeComposite = null;
            layout();
        }
    }
    
    @Override
    protected IObjectFilter getFilter() {
        return new Filter();
    }
    
    @Override
    public void dispose() {
        super.dispose();
        
        if(positionComposite != null) {
            positionComposite.dispose();
            positionComposite = null;
        }
        
        if(positionRelativeComposite != null) {
            positionRelativeComposite.dispose();
            positionRelativeComposite = null;
        }
    }
}
