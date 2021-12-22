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
     * @param value If true JS is enabled in the internal browser
     */
    void setJavascriptEnabled(boolean value);
    
    /**
     * @return Whether JS is enabled
     */
    boolean getJavascriptEnabled();
    
    /**
     * @param value If true external hosts are enabled in the internal browser
     */
    void setExternalHostsEnabled(boolean value);
    
    /**
     * @return Whether external hosts are enabled
     */
    boolean getExternalHostsEnabled();
    
    /**
     * This is deprecated use setPersistBrowser API instead
     */
    @Deprecated
    default void setPersistBrowserURL(boolean value) {}
}
