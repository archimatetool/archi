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
    static final String ID_FACTORY = "com.archimatetool.editor.browser.BrowserEditorInputFactory"; //$NON-NLS-1$
    
    /**
     * Tag for URL
     */
    static final String TAG_URL = "url"; //$NON-NLS-1$
    
    /**
     * Tag for Name
     */
    static final String TAG_TITLE = "title"; //$NON-NLS-1$

    /**
     * Tag for whether to persist the Browser
     */
    static final String TAG_PERSIST_BROWSER = "persistBrowser"; //$NON-NLS-1$

    @Override
    public IAdaptable createElement(IMemento memento) {
        String url = memento.getString(TAG_URL);
        String title = memento.getString(TAG_TITLE);
        
        IBrowserEditorInput input = new BrowserEditorInput(url, title);

        Boolean persistBrowser = memento.getBoolean(TAG_PERSIST_BROWSER);
        if(persistBrowser != null) {
            input.setPersistBrowser(persistBrowser);
        }
        
        return input;
    }
}
