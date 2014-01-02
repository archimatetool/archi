/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.browser;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.IElementFactory;
import org.eclipse.ui.IMemento;


/**
 * Factory for restoring the state of Browser Editor
 * 
 * @author Phillip Beauvoir
 */
public class BrowserEditorInputFactory implements IElementFactory {

    /**
     * Factory id. The workbench plug-in registers a factory by this name
     * with the "org.eclipse.ui.elementFactories" extension point.
     */
    public static final String ID_FACTORY = "com.archimatetool.editor.browser.BrowserEditorInputFactory"; //$NON-NLS-1$
    
    /**
     * Tag for URL
     */
    public static final String TAG_URL = "url"; //$NON-NLS-1$
    
    /**
     * Tag for whether to persist the Browser's current URL
     */
    public static final String TAG_PERSIST_BROWSER_URL = "persist_browser_url"; //$NON-NLS-1$

    @Override
    public IAdaptable createElement(IMemento memento) {
        String url = memento.getString(TAG_URL);
        IBrowserEditorInput input = new BrowserEditorInput(url);

        Boolean persistBrowserURL = memento.getBoolean(TAG_PERSIST_BROWSER_URL);
        if(persistBrowserURL != null) {
            input.setPersistBrowserURL(persistBrowserURL);
        }
        
        return input;
    }
}
