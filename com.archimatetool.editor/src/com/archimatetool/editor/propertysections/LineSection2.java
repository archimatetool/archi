/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.diagram.commands.DiagramModelObjectOutlineAlphaCommand;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelObject;



/**
 * Property Section for Line Opacity and Line Style
 * 
 * @author Phillip Beauvoir
 */
public class LineSection2 extends AbstractMultiControlSection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return object instanceof IDiagramModelObject dmo && 
                    (shouldExposeFeature(dmo, IDiagramModelObject.FEATURE_LINE_ALPHA) ||
                    shouldExposeFeature(dmo, IDiagramModelObject.FEATURE_LINE_STYLE));
        }

        @Override
        public Class<?> getAdaptableType() {
            return IDiagramModelObject.class;
        }
    }
    
    private OpacityComposite opacityComposite;
    private LineStyleComposite linestyleComposite;
    
    @Override
    protected void createControls(Composite parent) {
        init(parent, 2);
        
        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    @Override
    protected void notifyChanged(Notification msg) {
        Object feature = msg.getFeature();

        if(isFeatureNotification(msg, IDiagramModelObject.FEATURE_LINE_ALPHA)) {
            if(!isExecutingCommand()) {
                updateOpacityControl();
            }
        }
        else if(isFeatureNotification(msg, IDiagramModelObject.FEATURE_LINE_STYLE)) {
            updateLineStyleControl();
        }
        else if(feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
            update();
        }
    }

    @Override
    protected void update() {
        updateOpacityControl();
        updateLineStyleControl();
    }
    
    private void updateOpacityControl() {
        boolean show = shouldShowControl(IDiagramModelObject.FEATURE_LINE_ALPHA);
        
        if(show) {
            if(opacityComposite == null) {
                opacityComposite = new OpacityComposite(this, parentComposite, Messages.LineOpacitySection_0) {
                    @Override
                    Command getCommand(IDiagramModelObject dmo, int newValue) {
                        return new DiagramModelObjectOutlineAlphaCommand(dmo, newValue);
                    }

                    @Override
                    int getValue() {
                        IDiagramModelObject lastSelected = (IDiagramModelObject)getFirstSelectedObject();
                        return lastSelected.getLineAlpha();
                    }

                    @Override
                    boolean isValidObject(EObject eObject) {
                        return getFilter().shouldExposeFeature(eObject, IDiagramModelObject.FEATURE_LINE_ALPHA);
                    }
                };

                // If we're showing the LineStyleComposite move the OpacityComposite above/before it
                if(linestyleComposite != null) {
                    opacityComposite.getComposite().moveAbove(linestyleComposite.getComposite());
                    layout();
                }
            }
            
            opacityComposite.updateControl();
        }
        else if(opacityComposite != null) {
            opacityComposite.dispose();
            opacityComposite = null;
            layout();
        }
    }
    
    private void updateLineStyleControl() {
        boolean show = shouldShowControl(IDiagramModelObject.FEATURE_LINE_STYLE);
        
        if(show) {
            if(linestyleComposite == null) {
                linestyleComposite = new LineStyleComposite(this, parentComposite);
                layout();
            }
            linestyleComposite.updateControl();
        }
        else if(linestyleComposite != null) {
            linestyleComposite.dispose();
            linestyleComposite = null;
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
        
        if(opacityComposite != null) {
            opacityComposite.dispose();
            opacityComposite = null;
        }
        
        if(linestyleComposite != null) {
            linestyleComposite.dispose();
            linestyleComposite = null;
        }
    }
}
