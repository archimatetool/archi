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
import com.archimatetool.model.ILineObject;



/**
 * Property Section for a Line Color and Width
 * 
 * @author Phillip Beauvoir
 */
public class LineSection extends AbstractMultiControlSection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return object instanceof ILineObject lo &&
                    (shouldExposeFeature(lo, IArchimatePackage.Literals.LINE_OBJECT__LINE_COLOR.getName())
                            || shouldExposeFeature(lo, IArchimatePackage.Literals.LINE_OBJECT__LINE_WIDTH.getName()));
        }

        @Override
        public Class<?> getAdaptableType() {
            return ILineObject.class;
        }
    }
    
    private LineColorComposite lineColorComposite;
    private LineWidthComposite lineWidthComposite;

    @Override
    protected void createControls(Composite parent) {
        init(parent, 2);
        
        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    @Override
    protected void notifyChanged(Notification msg) {
        Object feature = msg.getFeature();

        if(feature == IArchimatePackage.Literals.LINE_OBJECT__LINE_COLOR || isFeatureNotification(msg, IDiagramModelObject.FEATURE_DERIVE_ELEMENT_LINE_COLOR)) {
            updateColorControl(); // update also when executing command in case "default" or "derive from fill color" is chosen
        }
        else if(feature == IArchimatePackage.Literals.LINE_OBJECT__LINE_WIDTH) {
            if(!isExecutingCommand()) {
                updateLineWidthControl();
            }
        }
        else if(feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
            update();
        }
    }

    @Override
    protected void update() {
        updateColorControl();
        updateLineWidthControl();
    }
    
    private void updateColorControl() {
        boolean show = shouldShowControl(IArchimatePackage.Literals.LINE_OBJECT__LINE_COLOR.getName());
        
        if(show) {
            if(lineColorComposite == null) {
                lineColorComposite = new LineColorComposite(this, parentComposite);
                
                // If we're showing the LineWidthComposite move the LineColorComposite above/before it
                if(lineWidthComposite != null) {
                    lineColorComposite.getComposite().moveAbove(lineWidthComposite.getComposite());
                    layout();
                }
            }
            lineColorComposite.updateControl();
        }
        else if(lineColorComposite != null) {
            lineColorComposite.dispose();
            lineColorComposite = null;
            layout();
        }
    }
    
    private void updateLineWidthControl() {
        boolean show = shouldShowControl(IArchimatePackage.Literals.LINE_OBJECT__LINE_WIDTH.getName());
        
        if(show) {
            if(lineWidthComposite == null) {
                lineWidthComposite = new LineWidthComposite(this, parentComposite);
                layout();
            }
            lineWidthComposite.updateControl();
        }
        else if(lineWidthComposite != null) {
            lineWidthComposite.dispose();
            lineWidthComposite = null;
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
        
        if(lineColorComposite != null) {
            lineColorComposite.dispose();
            lineColorComposite = null;
        }
        
        if(lineWidthComposite != null) {
            lineWidthComposite.dispose();
            lineWidthComposite = null;
        }
    }
}
