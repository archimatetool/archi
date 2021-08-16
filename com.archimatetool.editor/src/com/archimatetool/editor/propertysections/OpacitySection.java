/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.diagram.commands.DiagramModelObjectAlphaCommand;
import com.archimatetool.editor.diagram.commands.DiagramModelObjectOutlineAlphaCommand;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelObject;



/**
 * Property Section for Opacity
 * 
 * @author Phillip Beauvoir
 */
public class OpacitySection extends AbstractECorePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return (object instanceof IDiagramModelObject) && (shouldExposeFeature((EObject)object, IArchimatePackage.Literals.DIAGRAM_MODEL_OBJECT__ALPHA.getName())
                    || shouldExposeFeature((EObject)object, IDiagramModelObject.FEATURE_LINE_ALPHA));
        }

        @Override
        public Class<?> getAdaptableType() {
            return IDiagramModelObject.class;
        }
    }
    
    private Spinner fFillSpinner;
    private Spinner fOutlineSpinner;
    
    @Override
    protected void createControls(Composite parent) {
        ((GridLayout)parent.getLayout()).horizontalSpacing = 30;
        
        Composite group1 = createComposite(parent, 2, false);
        fFillSpinner = createSpinnerControl(group1, Messages.OpacitySection_0, 0);
        
        Composite group2 = createComposite(parent, 2, false);
        fOutlineSpinner = createSpinnerControl(group2, Messages.OutlineOpacitySection_0, 1);
        
        // Allow setting 1 or 2 columns
        GridLayoutColumnHandler.create(parent, 2).updateColumns();
        
        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    private Spinner createSpinnerControl(Composite parent, String label, int type) {
        createLabel(parent, label, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        
        final Spinner spinner = new Spinner(parent, SWT.BORDER);
        
        spinner.setMinimum(0);
        spinner.setMaximum(255);
        spinner.setIncrement(5);
        
        getWidgetFactory().adapt(spinner, true, true);
        
        Listener listener = (e) -> {
            int newValue = spinner.getSelection();

            CompoundCommand result = new CompoundCommand();

            for(EObject dmo : getEObjects()) {
                if(isAlive(dmo)) {
                    Command cmd;
                    if(type == 0) {
                        cmd = new DiagramModelObjectAlphaCommand((IDiagramModelObject)dmo, newValue);
                    }
                    else {
                        cmd = new DiagramModelObjectOutlineAlphaCommand((IDiagramModelObject)dmo, newValue);
                    }
                    if(cmd.canExecute()) {
                        result.add(cmd);
                    }
                }
            }

            executeCommand(result.unwrap());
        };
        
        spinner.addListener(SWT.MouseUp, listener);
        spinner.addListener(SWT.FocusOut, listener);
        spinner.addListener(SWT.DefaultSelection, listener);
        
        spinner.addDisposeListener((e) -> {
            if(spinner != null && !spinner.isDisposed()) {
                spinner.removeListener(SWT.MouseUp, listener);
                spinner.removeListener(SWT.FocusOut, listener);
                spinner.removeListener(SWT.DefaultSelection, listener);
            }
        });
        
        return spinner;
    }
    
    @Override
    protected void notifyChanged(Notification msg) {
        if(msg.getNotifier() == getFirstSelectedObject()) {
            Object feature = msg.getFeature();
            
            if(feature == IArchimatePackage.Literals.DIAGRAM_MODEL_OBJECT__ALPHA 
                    || isFeatureNotification(msg, IDiagramModelObject.FEATURE_LINE_ALPHA)
                    || feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
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
        
        fFillSpinner.setSelection(lastSelected.getAlpha());
        fOutlineSpinner.setSelection(lastSelected.getLineAlpha());

        fFillSpinner.setEnabled(!isLocked(lastSelected) && getFilter().shouldExposeFeature(lastSelected, IArchimatePackage.Literals.DIAGRAM_MODEL_OBJECT__ALPHA.getName()));
        fOutlineSpinner.setEnabled(!isLocked(lastSelected) && getFilter().shouldExposeFeature(lastSelected, IDiagramModelObject.FEATURE_LINE_ALPHA));
    }
    
    @Override
    protected IObjectFilter getFilter() {
        return new Filter();
    }
}
