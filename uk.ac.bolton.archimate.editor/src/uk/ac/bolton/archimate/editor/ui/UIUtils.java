/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import uk.ac.bolton.archimate.editor.utils.StringUtils;


/**
 * UI Utils
 * 
 * @author Phillip Beauvoir
 */
public final class UIUtils {

    /**
     * Add a Verify listener to a Text control with the SWT.SINGLE style so that CRLF
     * characters are replaced with a " " character. This is necessary on Mac Cocoa as these
     * are added to text control with horrible results.<p>
     * See <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=273470">Eclipse Bug #273470</a>
     * 
     * @param textControl
     */
    public static void conformSingleTextControl(Text textControl) {
        if(textControl == null) {
            return;
        }
        
        if((textControl.getStyle() & SWT.SINGLE) != 0) {
            textControl.addListener(SWT.Verify, new Listener() {
                public void handleEvent(Event event) {
                    if(StringUtils.isSet(event.text)) {
                        event.text = event.text.replaceAll("(\\r\\n|\\r|\\n)", " ");
                    }
                }
            });
        }
    }
}
