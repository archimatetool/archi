/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.ILineObject;



/**
 * Property Section for a Line
 * 
 * @author Phillip Beauvoir
 */
public class LineSection extends AbstractECorePropertySection {
    
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
    
    private Composite parentComposite;
    private LineColorComposite lineColorComposite;
    private LineWidthComposite lineWidthComposite;
    private GridLayoutColumnHandler columnHandler;

    @Override
    protected void createControls(Composite parent) {
        parentComposite = parent;
        
        ((GridLayout)parent.getLayout()).horizontalSpacing = 30;
        
        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    @Override
    protected void notifyChanged(Notification msg) {
        if(msg.getNotifier() == getFirstSelectedObject()) {
            Object feature = msg.getFeature();
            
            if(feature == IArchimatePackage.Literals.LINE_OBJECT__LINE_COLOR || isFeatureNotification(msg, IDiagramModelObject.FEATURE_DERIVE_ELEMENT_LINE_COLOR)) {
                updateColorControl(); // update also when executing command in case "default" or "derive from fill color" is chosen
            }
            else if(feature == IArchimatePackage.Literals.LINE_OBJECT__LINE_WIDTH) {
                if(!fIsExecutingCommand) {
                    updateLineWidthControl();
                }
            }
            else if(feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
                update();
            }
        }
    }

    @Override
    protected void update() {
        updateColorControl();
        updateLineWidthControl();
        
        // Allow setting 1 or 2 columns
        if(columnHandler == null) {
            columnHandler = GridLayoutColumnHandler.create(parentComposite, 2);
            columnHandler.updateColumns();
        }
    }
    
    private void updateColorControl() {
        boolean show = getFilter().shouldExposeFeature(getFirstSelectedObject(), IArchimatePackage.Literals.LINE_OBJECT__LINE_COLOR.getName());
        
        if(show) {
            if(lineColorComposite == null) {
                lineColorComposite = new LineColorComposite(this, parentComposite);
            }
            lineColorComposite.updateControl();
        }
        else if(lineColorComposite != null) {
            lineColorComposite.dispose();
            lineColorComposite = null;
            return;
        }
    }
    
    private void updateLineWidthControl() {
        boolean show = getFilter().shouldExposeFeature(getFirstSelectedObject(), IArchimatePackage.Literals.LINE_OBJECT__LINE_WIDTH.getName());
        
        if(show) {
            if(lineWidthComposite == null) {
                lineWidthComposite = new LineWidthComposite(this, parentComposite);
            }
            lineWidthComposite.updateControl();
        }
        else if(lineWidthComposite != null) {
            lineWidthComposite.dispose();
            lineWidthComposite = null;
            return;
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
        
        columnHandler = null;
    }
}
