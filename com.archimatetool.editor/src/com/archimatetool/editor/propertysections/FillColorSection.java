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
import com.archimatetool.model.IDiagramModelObject;



/**
 * Property Section for a Fill Color and Gradient
 * 
 * @author Phillip Beauvoir
 */
public class FillColorSection extends AbstractMultiControlSection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return object instanceof IDiagramModelObject dmo && 
                    (shouldExposeFeature(dmo, IArchimatePackage.Literals.DIAGRAM_MODEL_OBJECT__FILL_COLOR.getName())
                            || shouldExposeFeature(dmo, IDiagramModelObject.FEATURE_GRADIENT));
        }

        @Override
        public Class<?> getAdaptableType() {
            return IDiagramModelObject.class;
        }
    }
    
    private FillColorComposite fillColorComposite;
    private GradientComposite gradientComposite;
    
    @Override
    protected void createControls(Composite parent) {
        init(parent, 2);
        
        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    @Override
    protected void notifyChanged(Notification msg) {
        Object feature = msg.getFeature();

        if(feature == IArchimatePackage.Literals.DIAGRAM_MODEL_OBJECT__FILL_COLOR) {
            updateColorControl(); // update also when executing command in case "default" is chosen
        }
        else if(isFeatureNotification(msg, IDiagramModelObject.FEATURE_GRADIENT)) {
            if(!isExecutingCommand()) {
                updateGradientControl();
            }
        }
        else if(feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
            update();
        }
    }

    @Override
    protected void update() {
        updateColorControl();
        updateGradientControl();
    }
    
    private void updateColorControl() {
        boolean show = shouldShowControl(IArchimatePackage.Literals.DIAGRAM_MODEL_OBJECT__FILL_COLOR.getName());
        
        if(show) {
            if(fillColorComposite == null) {
                fillColorComposite = new FillColorComposite(this, parentComposite);
                
                // If we're showing the GradientComposite move the FillColorComposite above/before it
                if(gradientComposite != null) {
                    fillColorComposite.getComposite().moveAbove(gradientComposite.getComposite());
                    layout();
                }
            }
            
            fillColorComposite.updateControl();
        }
        else if(fillColorComposite != null) {
            fillColorComposite.dispose();
            fillColorComposite = null;
            layout();
        }
    }
    
    private void updateGradientControl() {
        boolean show = shouldShowControl(IDiagramModelObject.FEATURE_GRADIENT);
        
        if(show) {
            if(gradientComposite == null) {
                gradientComposite = new GradientComposite(this, parentComposite);
                layout();
            }
            gradientComposite.updateControl();
        }
        else if(gradientComposite != null) {
            gradientComposite.dispose();
            gradientComposite = null;
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
        
        if(fillColorComposite != null) {
            fillColorComposite.dispose();
            fillColorComposite = null;
        }
        
        if(gradientComposite != null) {
            gradientComposite.dispose();
            gradientComposite = null;
        }
    }
}
