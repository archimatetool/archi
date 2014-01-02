/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.browser;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.browser.Browser;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPersistableElement;

/**
 * Browser Editor Input
 * 
 * @author Phillip Beauvoir
 */
public class BrowserEditorInput implements IBrowserEditorInput {

    Browser browser;
    
    private String url;
    
    private String title;

    /**
     * Whether to save the Browser's current URL or the initial one provided
     */
    private boolean fPersistBrowserURL;
    
    /**
     * @param url The Url to display
     */
    public BrowserEditorInput(String url) {
        this(url, null);
    }
    
    /**
     * @param url The Url to display
     * @param title The title to display in the title bar
     */
    public BrowserEditorInput(String url, String title) {
        this.url = url;
        this.title = title;
    }
    
    @Override
    public void setPersistBrowserURL(boolean value) {
        fPersistBrowserURL = value;
    }
    
    @Override
    public boolean exists() {
    	return getURL() != null;
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return null;
    }
    
    @Override
    public String getURL() {
        return url;
    }

    @Override
    public String getName() {
        if(title != null) {
            return title;
        }
        
        return getURL() == null ? Messages.BrowserEditorInput_0 : getURL();
    }

    @Override
    public String getToolTipText() {
        return getName();
    }

    @SuppressWarnings("rawtypes")
    public Object getAdapter(Class adapter) {
        return null;
    }
    
    @Override
    public IPersistableElement getPersistable() {
        if(getURL() == null) {
            return null;
        }

        // OK, we'll handle it
        return this;
    }
    
    @Override
    public void saveState(IMemento memento) {
        String url;
        if(fPersistBrowserURL && browser != null && !browser.isDisposed()) {
            url = browser.getUrl();
        }
        else {
            url = getURL();
        }
        if(url != null) {
            memento.putString(BrowserEditorInputFactory.TAG_URL, url);
            if(fPersistBrowserURL) { // don't save if not set
                memento.putBoolean(BrowserEditorInputFactory.TAG_PERSIST_BROWSER_URL, fPersistBrowserURL);
            }
        }
    }

    @Override
    public String getFactoryId() {
        return BrowserEditorInputFactory.ID_FACTORY;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        
        if(!(obj instanceof BrowserEditorInput)) {
            return false;
        }
        
        if(getURL() == null) {
            return false;
        }
        
        return getURL().equalsIgnoreCase(((BrowserEditorInput)obj).getURL());
    }
}