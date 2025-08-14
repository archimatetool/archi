/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import com.archimatetool.editor.utils.PlatformUtils;
import com.archimatetool.editor.utils.StringUtils;



/**
 * UI Utils
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public final class UIUtils {

    /**
     * Create a single text control that filters out new line characters
     * 
     * @param parent Parent control
     * @param style Style
     * @param filterInvalidCharacters if true the XML invalid chars will also be filtered.
     *        This can be set to false if the text in the control won't be used as part of an XML file.
     * @return a new single Text control
     */
    public static Text createSingleTextControl(Composite parent, int style, boolean filterInvalidCharacters) {
        Text text = new Text(parent, style | SWT.SINGLE);
        
        applyNewlineFilter(text);
        
        if(filterInvalidCharacters) {
            applyInvalidCharacterFilter(text);
        }
        
        return text;
    }
    
    /**
     * Add a Verify listener to a text control with the SWT.SINGLE style so that newline characters are removed.
     * This is necessary on Mac and Linux as newlines can be copied and pasted into single text controls.
     * Windows will truncate the string up to the first newline character.
     * See <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=273470">Eclipse Bug #273470</a>
     * 
     * @param control A Text control with style SWT.SINGLE
     * @deprecated Use {@link #applyNewlineFilter(Control)} instead
     */
    public static void conformSingleTextControl(Text textControl) {
        applyNewlineFilter(textControl);
    }
    
    /**
     * Add a Verify listener to a control (usually with the SWT.SINGLE style) so that newline characters are removed.
     * This is necessary on Mac and Linux as newlines can be copied and pasted into single text controls.
     * Windows will truncate the string up to the first newline character.
     * See <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=273470">Eclipse Bug #273470</a>
     * 
     * @param control This is usually a Text control with style SWT.SINGLE but can be a control
     * such as CCombo or Combo that contains a text control with style SWT.SINGLE.
     */
    public static void applyNewlineFilter(Control control) {
        control.addListener(SWT.Verify, e -> {
            if(StringUtils.isSet(e.text)) {
                e.text = e.text.replaceAll("(\\r\\n|\\r|\\n)", "");
            }
        });
    }

    // Pattern for removing characters that are illegal in XML 1.0:
    // #x9 | #xA | #xD | [#x20-#xD7FF] | [#xE000-#xFFFD] | [#x10000-#x10FFFF]
    // Taken from http://stackoverflow.com/questions/4237625/removing-invalid-xml-characters-from-a-string-in-java
    static final String xml10pattern = "[^" //$NON-NLS-1$
            + "\u0009\r\n"
            + "\u0020-\uD7FF"
            + "\uE000-\uFFFD"
            + "\ud800\udc00-\udbff\udfff"
            + "]";

    
    /**
     * Filter out invalid text characters from the control.
     * This needs to be applied to a control if the control's text string will be saved in XML format.
     * This can happen if user copies and pastes text containing binary characters.
     * 
     * @param control This is usually a Text control but can be a control
     * such as CCombo or Combo that contains a text control
     */
    public static void applyInvalidCharacterFilter(Control control) {
        control.addListener(SWT.Verify, e -> {
            e.text = e.text.replaceAll(xml10pattern, "");
        });
    }
    
    /**
     * Filter out Cmd-Z key presses on Mac to work around crash to desktop.
     * This doesn't need to be applied if the control is in a dialog or wizard.
     * See https://github.com/eclipse-platform/eclipse.platform.swt/issues/1273
     * 
     * @param control This is usually a Text control but can be a control
     * such as CCombo or Combo that contains a text control
     */
    public static void applyMacUndoBugFilter(Control control) {
        if(PlatformUtils.isMac()) {
            control.addListener(SWT.KeyDown, e -> {
                if(e.keyCode == 'z' && (e.stateMask & SWT.COMMAND) != 0) {
                    e.doit = false;
                }
            });
        }
    }
    
    /**
     * On Mac single-click on the Cancel icon in a text control doesn't work.
     * See https://bugs.eclipse.org/bugs/show_bug.cgi?id=551405
     * @param text The Text control
     */
    public static void applyMacCancelIconListener(Text textControl) {
        if(PlatformUtils.isMac() && (textControl.getStyle() & SWT.ICON_CANCEL) != 0) {
            textControl.addMouseListener(new MouseAdapter() {
                boolean mouseDownWasClickedOnCancelIcon;
                
                @Override
                public void mouseDown(MouseEvent e) {
                    mouseDownWasClickedOnCancelIcon = isCancelIconClicked(e);
                }
                
                @Override
                public void mouseUp(MouseEvent e) {
                    if(mouseDownWasClickedOnCancelIcon && isCancelIconClicked(e)) {
                        textControl.setText("");
                    }
                }
                
                private boolean isCancelIconClicked(MouseEvent e) {
                    Rectangle bounds = textControl.getBounds();
                    return !textControl.getText().isEmpty() && e.x > bounds.width - 25 && e.x < bounds.width - 5
                            && e.y > 0 && e.y < bounds.height;
                }
            });
        }
    }

    /**
     * Apply a traverse listener to a Multi-line text control such that tabbing or pressing Ctrl + Enter
     * will cause either a tab out or will complete the text edit in the case of Ctrl + Enter
     * @param textControl Must be Multi-line text control
     * @param flags Or'd flags of SWT.TRAVERSE_RETURN, SWT.TRAVERSE_*
     */
    public static void applyTraverseListener(Text textControl, int flags) {
        // Only applies to multi-line text controls
        if((textControl.getStyle() & SWT.MULTI) == 0) {
            return;
        }
        
        textControl.addTraverseListener(e -> {
            // Ctrl + Enter
            if(e.detail == SWT.TRAVERSE_RETURN) {
                if((e.stateMask & SWT.MOD1) != 0) {
                    e.doit = true;
                }
            }
            // Tabs and other SWT.TRAVERSE_* flags
            else if((e.detail & flags) != 0) {
                e.doit = true;
            }
        });
    }
    
    /**
     * Apply a verify listener to a text control that accepts only numbers
     * @param textControl Single-line text control
     * @param low The low value
     * @param high The high value
     * @since 5.7.0
     */
    public static void applyNumberVerifyListener(Text textControl, int low, int high) {
        textControl.addVerifyListener(event -> {
            String currentText = ((Text)event.widget).getText();
            String newText = currentText.substring(0, event.start) + event.text + currentText.substring(event.end);
            try {
                int val = Integer.valueOf(newText);
                event.doit = val >= low && val <= high;
            }
            catch(NumberFormatException ex) {
                event.doit = newText.length() == 0; // blank string is OK
            }
        });
    }
    
    /**
     * Add an ellipsis to text and shorten it to fit in the width of the given control
     * From https://stackoverflow.com/questions/5993065/org-eclipse-swt-text-automatically-truncate-the-text
     * 
     * @param textValue 
     * @param control The control
     * @param margin The width margin. A value of 8 is about right
     * @return The shortened text
     */
    public static String shortenText(String text, Control control, int margin) {
        if(text == null) {
            return null;
        }
        
        GC gc = new GC(control);
        int maxWidth = control.getBounds().width - margin;
        int maxExtent = gc.textExtent(text).x;

        if(maxExtent < maxWidth) {
            gc.dispose();
            return text;
        }
        
        int length = text.length();
        int charsToClip = Math.round(0.95f * length * (1 - ((float)maxWidth / maxExtent)));
        int pivot = length / 2;
        int start = pivot - (charsToClip / 2);
        int end = pivot + (charsToClip / 2) + 1;
        
        while(start >= 0 && end < length) {
            String s1 = text.substring(0, start);
            String s2 = text.substring(end, length);
            String s = s1 + "..." + s2; //$NON-NLS-1$
            int l = gc.textExtent(s).x;
            if(l < maxWidth) {
                gc.dispose();
                return s;
            }
            start--;
            end++;
        }
        
        gc.dispose();
        
        return text;
    }
    
    /**
     * This is no longer needed with Eclipse 4.31 and later
     * TODO: Remove this
     */
    public static void fixMacSiliconItemHeight(Control control) {
        // Do nothing
    }
}
