/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.diagram.commands.DiagramModelObjectAlphaCommand;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.ILockable;



/**
 * Property Section for a Fill Alpha Opacity
 * 
 * @author Phillip Beauvoir
 */
public class OpacitySection extends AbstractArchimatePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$
    
    private static EAttribute FEATURE = IArchimatePackage.Literals.DIAGRAM_MODEL_OBJECT__ALPHA;
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        protected boolean isRequiredType(Object object) {
            return (object instanceof IDiagramModelObject) && shouldExposeFeature((EObject)object, FEATURE);
        }

        @Override
        protected Class<?> getAdaptableType() {
            return IDiagramModelObject.class;
        }
    }
    
    /*
     * Adapter to listen to changes made elsewhere (including Undo/Redo commands)
     */
    private Adapter eAdapter = new AdapterImpl() {
        @Override
        public void notifyChanged(Notification msg) {
            Object feature = msg.getFeature();
            // Color event (From Undo/Redo and here)
            if(feature == FEATURE || feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
                refreshControls();
            }
        }
    };
    
    /**
     * Spinner listener
     */
    private Listener spinnerListener = new Listener() {
        public void handleEvent(Event event) {
            int newValue = fSpinner.getSelection();
            if(newValue != fDiagramModelObject.getAlpha()) {
                fIsExecutingCommand = true;
                getCommandStack().execute(new DiagramModelObjectAlphaCommand(fDiagramModelObject, newValue));
                fIsExecutingCommand = false;
            }
        }
    };
    
    private IDiagramModelObject fDiagramModelObject;

    private Spinner fSpinner;
    
    @Override
    protected void createControls(Composite parent) {
        createSpinnerControl(parent);
        
        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    private void createSpinnerControl(Composite parent) {
        createLabel(parent, Messages.OpacitySection_0, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        
        fSpinner = new Spinner(parent, SWT.BORDER);
        
        fSpinner.setMinimum(0);
        fSpinner.setMaximum(255);
        fSpinner.setIncrement(5);
        
        getWidgetFactory().adapt(fSpinner, true, true);
        
        fSpinner.addListener(SWT.MouseUp, spinnerListener);
        fSpinner.addListener(SWT.FocusOut, spinnerListener);
        fSpinner.addListener(SWT.DefaultSelection, spinnerListener);
    }
    
    @Override
    protected void setElement(Object element) {
        fDiagramModelObject = (IDiagramModelObject)new Filter().adaptObject(element);
        if(fDiagramModelObject == null) {
            System.err.println(getClass() + " failed to get element for " + element); //$NON-NLS-1$
        }
        
        refreshControls();
    }
    
    protected void refreshControls() {
        if(fIsExecutingCommand) {
            return; 
        }
        
        fSpinner.setSelection(fDiagramModelObject.getAlpha());
        
        boolean enabled = fDiagramModelObject instanceof ILockable ? !((ILockable)fDiagramModelObject).isLocked() : true;
        fSpinner.setEnabled(enabled);
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

    @Override
    protected Adapter getECoreAdapter() {
        return eAdapter;
    }

    @Override
    protected EObject getEObject() {
        return fDiagramModelObject;
    }
}
