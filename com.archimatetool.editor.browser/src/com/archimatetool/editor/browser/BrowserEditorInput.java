/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.browser;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPersistableElement;

/**
 * Browser Editor Input
 * 
 * @author Phillip Beauvoir
 */
public class BrowserEditorInput implements IBrowserEditorInput {

    private String url;
    private String title;
    private boolean jsEnabled = true;
    private boolean externalHostsEnabled = true;

    /**
     * Whether to persist and restore the Browser when closing the app
     */
    private boolean fPersistBrowser;
    
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
    public void setPersistBrowser(boolean value) {
        fPersistBrowser = value;
    }
    
    @Override
    public void setJavascriptEnabled(boolean value) {
        jsEnabled = value;
    }
    
    @Override
    public boolean getJavascriptEnabled() {
        return jsEnabled;
    }
    
    @Override
    public void setExternalHostsEnabled(boolean value) {
        externalHostsEnabled = value;
    }
    
    @Override
    public boolean getExternalHostsEnabled() {
        return externalHostsEnabled;
    }
    
    @Override
    public boolean exists() {
    	return false;
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
    public void setURL(String url) {
        this.url = url;
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

    @Override
    public <T> T getAdapter(Class<T> adapter) {
        return null;
    }
    
    @Override
    public IPersistableElement getPersistable() {
        if(!fPersistBrowser || getURL() == null) {
            return null;
        }

        // OK, we'll handle it
        return this;
    }
    
    @Override
    public void saveState(IMemento memento) {
        if(url != null) {
            memento.putString(BrowserEditorInputFactory.TAG_URL, url);
            memento.putString(BrowserEditorInputFactory.TAG_TITLE, title);
            memento.putBoolean(BrowserEditorInputFactory.TAG_PERSIST_BROWSER, fPersistBrowser);
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