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
     * @param value If true the Browser's current URL will be persisted when the app closes
     *              otherwise the inital URL provided will be persisted.
     *              The default is set to false.
     */
    void setPersistBrowserURL(boolean value);
}
