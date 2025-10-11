/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Spinner;

import com.archimatetool.editor.ui.components.SpinnerListener;
import com.archimatetool.model.IDiagramModelObject;



/**
 * Opacity Composite
 * 
 * @author Phillip Beauvoir
 */
abstract class OpacityComposite {
    
    private SpinnerListener spinnerListener;
    private AbstractECorePropertySection section;
    private Composite composite;
    
    OpacityComposite(AbstractECorePropertySection section, Composite parent, String label) {
        this.section = section;
        composite = section.createComposite(parent, 2, false);
        createSpinnerControl(composite, label);
    }
    
    Composite getComposite() {
        return composite;
    }
    
    private void createSpinnerControl(Composite parent, String label) {
        section.createLabel(parent, label, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        
        Spinner spinner = new Spinner(parent, SWT.BORDER);
        spinner.setMinimum(0);
        spinner.setMaximum(255);
        spinner.setIncrement(5);
        
        section.getWidgetFactory().adapt(spinner, true, true);
        
        spinnerListener = new SpinnerListener(spinner) {
            @Override
            protected void doEvent(Event event) {
                CompoundCommand result = new CompoundCommand();
                
                for(EObject dmo : section.getEObjects()) {
                    if(section.isAlive(dmo) && isValidObject(dmo)) {
                        Command cmd = getCommand((IDiagramModelObject)dmo, spinner.getSelection());
                        if(cmd.canExecute()) {
                            result.add(cmd);
                        }
                    }
                }

                section.executeCommand(result.unwrap());
            }
        };
    }
    
    abstract Command getCommand(IDiagramModelObject dmo, int newValue);
    
    abstract int getValue();
    
    /**
     * In case of multi-selection we should check this
     */
    abstract boolean isValidObject(EObject eObject);
    
    void updateControl() {
        IDiagramModelObject lastSelected = (IDiagramModelObject)section.getFirstSelectedObject();
        spinnerListener.setSelection(getValue());
        spinnerListener.getSpinner().setEnabled(!section.isLocked(lastSelected));
    }
    
    void dispose() {
        composite.dispose();
        composite = null;
        spinnerListener = null;
        section = null;
    }
}
