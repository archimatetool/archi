/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.browser;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

/**
 * An Eclipse Editor containing a Browser component
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
        if(!(input instanceof BrowserEditorInput)) {
            throw new IllegalArgumentException("Editor Input has to be type BrowserEditorInput"); //$NON-NLS-1$
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
        if(fBrowser == null) {
            Label label = new Label(parent, SWT.NONE);
            label.setText(Messages.BrowserEditor_0);
            label.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
            label.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
            return;
        }
        
        BrowserEditorInput input = (BrowserEditorInput)getEditorInput();
        
        // Set URL
        if(input.getURL() != null) {
            fBrowser.setUrl(input.getURL());
        }
        
        setPartName(input.getName());
    }
    
    /**
     * Create the Browser if possible
     */
    protected Browser createBrowser(Composite parent) {
        Browser browser = null;
        try {
            // On Eclipse 3.6 set this
            if(isGTK()) {
                System.setProperty("org.eclipse.swt.browser.UseWebKitGTK", "true"); //$NON-NLS-1$ //$NON-NLS-2$
            }
            browser = new Browser(parent, SWT.NONE);
            browser.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        }
        catch(SWTError error) {
            error.printStackTrace();
        }
        
        return browser;
    }
    
    @Override
    public void setBrowserEditorInput(BrowserEditorInput input) {
        setInput(input);
        
        setPartName(input.getName());
        
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
    
    private boolean isGTK() {
        return Platform.WS_GTK.equals(Platform.getWS());
    }
    
    @Override
    public void dispose() {
        super.dispose();
    }
}
