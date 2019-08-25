/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.browser;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;


/**
 * Browser Editor Input
 * 
 * @author Phillip Beauvoir
 */
public interface IBrowserEditorInput extends IEditorInput, IPersistableElement {
    
    /**
     * @return The URL
     */
    String getURL();
    
    /**
     * Set the URL
     */
    void setURL(String url);
    
    /**
     * @param value If true the Browser will be persisted when the app closes
     *              The default is set to false.
     */
    void setPersistBrowser(boolean value);
    
    /**
     * This is deprecated use setPersistBrowser API instead
     */
    @Deprecated
    default void setPersistBrowserURL(boolean value) {}
}
