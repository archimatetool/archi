/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import com.archimatetool.editor.utils.StringUtils;



/**
 * UI Utils
 * 
 * @author Phillip Beauvoir
 */
public final class UIUtils {

    /**
     * Add a Verify listener to a Text control with the SWT.SINGLE style so that CRLF
     * characters are replaced with a " " character. This is necessary on Mac and Linux as these
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
                        event.text = event.text.replaceAll("(\\r\\n|\\r|\\n)", " "); //$NON-NLS-1$ //$NON-NLS-2$
                    }
                }
            });
        }
    }

    // Pattern for removing characters that are illegal in XML 1.0:
    // #x9 | #xA | #xD | [#x20-#xD7FF] | [#xE000-#xFFFD] | [#x10000-#x10FFFF]
    // Taken from http://stackoverflow.com/questions/4237625/removing-invalid-xml-characters-from-a-string-in-java
    static final String xml10pattern = "[^" //$NON-NLS-1$
            + "\u0009\r\n" //$NON-NLS-1$
            + "\u0020-\uD7FF" //$NON-NLS-1$
            + "\uE000-\uFFFD" //$NON-NLS-1$
            + "\ud800\udc00-\udbff\udfff" //$NON-NLS-1$
            + "]"; //$NON-NLS-1$

    
    /**
     * Filter out any invalid characters from the textControl
     * This can happen if user copies and pastes binary characters.
     * @param textControl
     */
    public static void applyInvalidCharacterFilter(Text textControl) {
        textControl.addVerifyListener(new VerifyListener() {
            public void verifyText(VerifyEvent e) {
                e.text = e.text.replaceAll(xml10pattern, ""); //$NON-NLS-1$
            }
        });
    }
    
    /**
     * Filter out any invalid characters from the textControl
     * This can happen if user copies and pastes binary characters.
     * @param textControl
     */
    public static void applyInvalidCharacterFilter(StyledText textControl) {
        textControl.addVerifyListener(new VerifyListener() {
            public void verifyText(VerifyEvent e) {
                e.text = e.text.replaceAll(xml10pattern, ""); //$NON-NLS-1$
            }
        });
    }

}
