/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.browser;

import org.eclipse.swt.browser.Browser;
import org.eclipse.ui.IEditorPart;


/**
 * An Eclipse Editor containing a Browser component
 * 
 * @author Phillip Beauvoir
 */
public interface IBrowserEditor extends IEditorPart {
    
    String ID = BrowserPlugin.PLUGIN_ID + ".browserEditor"; //$NON-NLS-1$
    
    /**
     * @return The Browser
     */
    Browser getBrowser();
    
    /**
     * Set new BrowserEditorInput
     * @param input
     */
    void setBrowserEditorInput(IBrowserEditorInput input);
}
