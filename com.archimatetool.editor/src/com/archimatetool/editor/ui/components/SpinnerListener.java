/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.components;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;

/**
 * Listen to Spinner events and update value as required
 * 
 * @author Phillip Beauvoir
 * @since 5.8.0
 */
public abstract class SpinnerListener {
    private Spinner spinner;
    private int lastValue;
    
    public SpinnerListener(Spinner spinner) {
        this.spinner = spinner;
        lastValue = spinner.getSelection();
        
        final Listener listener = event -> {
            if(lastValue != spinner.getSelection()) {
                doEvent(event);
                lastValue = spinner.getSelection();
            }
        };

        // These events trigger a Spinner change
        spinner.addListener(SWT.MouseUp, listener);  // When spinner is spinning and finished
        spinner.addListener(SWT.FocusOut, listener); // Focus out to complete
        spinner.addListener(SWT.DefaultSelection, listener); // Press return
        
        spinner.addDisposeListener(e -> {
            if(spinner != null && !spinner.isDisposed()) {
                spinner.removeListener(SWT.MouseUp, listener);
                spinner.removeListener(SWT.FocusOut, listener);
                spinner.removeListener(SWT.DefaultSelection, listener);
                this.spinner = null;
            }
        });
    }
    
    public Spinner getSpinner() {
        return spinner;
    }
    
    public int getSelection() {
        return spinner.getSelection();
    }
    
    public void setSelection(int value) {
        lastValue = value;
        spinner.setSelection(value);
    }
    
    protected abstract void doEvent(Event event);
}
