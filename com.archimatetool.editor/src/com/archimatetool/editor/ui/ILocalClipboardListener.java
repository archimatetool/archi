/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui;


/**
 * Local Clipboard listener
 * 
 * @author Phillip Beauvoir
 */
public interface ILocalClipboardListener {

    /**
     * The contents of the clipboard *may* have changed
     * This event can occur when switching away from the application and back again
     * The System clipboard contents may have changed and some clients may wish to check it.
     * @param clipboardContents
     */
    void clipBoardChanged(Object clipboardContents);
}
