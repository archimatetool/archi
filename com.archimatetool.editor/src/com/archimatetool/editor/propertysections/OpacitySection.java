/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.diagram.commands.DiagramModelObjectAlphaCommand;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelObject;



/**
 * Property Section for a Fill Alpha Opacity
 * 
 * @author Phillip Beauvoir
 */
public class OpacitySection extends AbstractECorePropertySection {
    
    protected static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$
    
    private static EAttribute FEATURE = IArchimatePackage.Literals.DIAGRAM_MODEL_OBJECT__ALPHA;
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return (object instanceof IDiagramModelObject) && shouldExposeFeature((EObject)object, FEATURE.getName());
        }

        @Override
        public Class<?> getAdaptableType() {
            return IDiagramModelObject.class;
        }
    }
    
    /**
     * Spinner listener
     */
    protected Listener spinnerListener = new Listener() {
        @Override
        public void handleEvent(Event event) {
            int newValue = fSpinner.getSelection();

            CompoundCommand result = new CompoundCommand();

            for(EObject dmo : getEObjects()) {
                if(isAlive(dmo)) {
                    Command cmd = getCommand((IDiagramModelObject)dmo, newValue);
                    if(cmd.canExecute()) {
                        result.add(cmd);
                    }
                }
            }

            executeCommand(result.unwrap());
        }
    };
    
    protected Spinner fSpinner;
    
    @Override
    protected void createControls(Composite parent) {
        createSpinnerControl(parent);
        
        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    protected void createSpinnerControl(Composite parent) {
        createLabel(parent, getLabelString(), ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        
        fSpinner = new Spinner(parent, SWT.BORDER);
        
        fSpinner.setMinimum(0);
        fSpinner.setMaximum(255);
        fSpinner.setIncrement(5);
        
        getWidgetFactory().adapt(fSpinner, true, true);
        
        fSpinner.addListener(SWT.MouseUp, spinnerListener);
        fSpinner.addListener(SWT.FocusOut, spinnerListener);
        fSpinner.addListener(SWT.DefaultSelection, spinnerListener);
    }
    
    protected String getLabelString() {
        return Messages.OpacitySection_0;
    }
    
    protected EAttribute getFeature() {
        return FEATURE;
    }
    
    protected Command getCommand(IDiagramModelObject dmo, int newValue) {
        return new DiagramModelObjectAlphaCommand(dmo, newValue);
    }
    
    @Override
    protected void notifyChanged(Notification msg) {
        if(msg.getNotifier() == getFirstSelectedObject()) {
            Object feature = msg.getFeature();
            
            if(feature == getFeature() || feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
                update();
            }
        }
    }

    @Override
    protected void update() {
        if(fIsExecutingCommand) {
            return; 
        }
        
        IDiagramModelObject lastSelected = (IDiagramModelObject)getFirstSelectedObject();
        
        fSpinner.setSelection(lastSelected.getAlpha());

        fSpinner.setEnabled(!isLocked(lastSelected));
    }
    
    @Override
    protected IObjectFilter getFilter() {
        return new Filter();
    }
    
    @Override
    public void dispose() {
        super.dispose();
        
        if(fSpinner != null && !fSpinner.isDisposed()) {
            fSpinner.removeListener(SWT.MouseUp, spinnerListener);
            fSpinner.removeListener(SWT.FocusOut, spinnerListener);
            fSpinner.removeListener(SWT.DefaultSelection, spinnerListener);
        }
    }
}
