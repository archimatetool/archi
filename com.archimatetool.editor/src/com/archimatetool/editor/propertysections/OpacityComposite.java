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
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;

import com.archimatetool.model.IDiagramModelObject;



/**
 * Opacity Composite
 * 
 * @author Phillip Beauvoir
 */
abstract class OpacityComposite {
    
    private Spinner fSpinner;
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
        
        fSpinner = new Spinner(parent, SWT.BORDER);
        
        fSpinner.setMinimum(0);
        fSpinner.setMaximum(255);
        fSpinner.setIncrement(5);
        
        section.getWidgetFactory().adapt(fSpinner, true, true);
        
        Listener listener = event -> {
            int newValue = fSpinner.getSelection();

            CompoundCommand result = new CompoundCommand();

            for(EObject dmo : section.getEObjects()) {
                if(section.isAlive(dmo)) {
                    Command cmd = getCommand((IDiagramModelObject)dmo, newValue);
                    if(cmd.canExecute()) {
                        result.add(cmd);
                    }
                }
            }

            section.executeCommand(result.unwrap());
        };
        
        fSpinner.addListener(SWT.MouseUp, listener);
        fSpinner.addListener(SWT.FocusOut, listener);
        fSpinner.addListener(SWT.DefaultSelection, listener);
        
        fSpinner.addDisposeListener(event -> {
            if(fSpinner != null && !fSpinner.isDisposed()) {
                fSpinner.removeListener(SWT.MouseUp, listener);
                fSpinner.removeListener(SWT.FocusOut, listener);
                fSpinner.removeListener(SWT.DefaultSelection, listener);
            }
        });
    }
    
    abstract Command getCommand(IDiagramModelObject dmo, int newValue);
    
    abstract int getValue();
    
    void updateControl() {
        IDiagramModelObject lastSelected = (IDiagramModelObject)section.getFirstSelectedObject();
        fSpinner.setSelection(getValue());
        fSpinner.setEnabled(!section.isLocked(lastSelected));
    }
    
    void dispose() {
        composite.dispose();
        composite = null;
        fSpinner = null;
        section = null;
    }
}
