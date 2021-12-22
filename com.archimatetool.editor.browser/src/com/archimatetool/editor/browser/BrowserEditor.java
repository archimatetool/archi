/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.browser;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

/**
 * An EditorPart containing a Browser component
 * 
 * @author Phillip Beauvoir
 */
public class BrowserEditor extends EditorPart implements IBrowserEditor {
    
    /**
     * The Browser component
     */
    private Browser fBrowser;
    

    @Override
    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        if(!(input instanceof IBrowserEditorInput)) {
            throw new IllegalArgumentException("Editor Input has to be type IBrowserEditorInput"); //$NON-NLS-1$
        }

        setSite(site);
        setInput(input);
    }

    @Override
    public void createPartControl(Composite parent) {
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.verticalSpacing = 0;
        parent.setLayout(layout);
        
        fBrowser = createBrowser(parent);
        
        if(fBrowser != null) {
            setupBrowser();
        }
        // No Browser, so show a message
        else {
            Label label = new Label(parent, SWT.NONE);
            label.setText(Messages.BrowserEditor_0);
            label.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
            label.setForeground(new Color(255, 45, 45));
            label.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        }
    }
    
    @Override
    public IBrowserEditorInput getEditorInput() {
        return (IBrowserEditorInput)super.getEditorInput();
    }
    
    /**
     * Create the Browser if possible
     */
    protected Browser createBrowser(Composite parent) {
        Browser browser = null;
        try {
            browser = new Browser(parent, SWT.NONE);
            browser.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
            
            // Don't allow external hosts if set
            browser.addLocationListener(new LocationAdapter() {
                @Override
                public void changing(LocationEvent e) {
                    if(getEditorInput() != null && !getEditorInput().getExternalHostsEnabled()) {
                        e.doit = e.location != null &&
                                (e.location.startsWith("file:") //$NON-NLS-1$
                                || e.location.startsWith("data:") //$NON-NLS-1$
                                || e.location.startsWith("about:")); //$NON-NLS-1$
                    }
                }
            });
        }
        catch(SWTError error) {
            error.printStackTrace();
            
            // Remove junk child controls that might be created with failed load
            for(Control child : parent.getChildren()) {
                child.dispose();
            }
        }
        
        return browser;
    }
    
    @Override
    public void setBrowserEditorInput(IBrowserEditorInput input) {
        setInput(input);
        setupBrowser();
    }
    
    /**
     * Set some settings on the Browser from the IBrowserEditorInput
     */
    private void setupBrowser() {
        IBrowserEditorInput input = getEditorInput();
        
        if(fBrowser == null || input == null) {
            return;
        }
        
        // Part Name
        setPartName(input.getName());

        // Enable JS
        fBrowser.setJavascriptEnabled(input.getJavascriptEnabled());
        
        // URL
        if(input.getURL() != null) {
            fBrowser.setUrl(input.getURL());
        }
    }
    
    /**
     * @return The Browser component
     */
    @Override
    public Browser getBrowser() {
        return fBrowser;
    }
    
    @Override
    public void setFocus() {
        if(fBrowser != null) {
            fBrowser.setFocus();
        }
    }

    @Override
    public void doSave(IProgressMonitor monitor) {
    }

    @Override
    public void doSaveAs() {
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public boolean isSaveAsAllowed() {
        return false;
    }
}
