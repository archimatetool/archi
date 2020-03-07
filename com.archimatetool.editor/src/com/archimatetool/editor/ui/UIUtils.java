/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.utils.StringUtils;



/**
 * UI Utils
 * 
 * @author Phillip Beauvoir
 */
public final class UIUtils {

    /**
     * Create a single text control that filters out new line characters
     * 
     * @param parent Parent control
     * @param style Style
     * @param filterInvalidCharacters if true the XML invalid chars will also be filtered
     * @return a new single Text control
     */
    public static Text createSingleTextControl(Composite parent, int style, boolean filterInvalidCharacters) {
        Text text = new Text(parent, style | SWT.SINGLE);
        
        conformSingleTextControl(text);
        
        if(filterInvalidCharacters) {
            applyInvalidCharacterFilter(text);
        }
        
        return text;
    }
    
    /**
     * Add a Verify listener to a Text control with the SWT.SINGLE style so that newline
     * characters are removed. This is espcially necessary on Mac and Linux as newlines.
     * Windows will truncate the string up to the first newline character.
     * can be copied and pasted into single text controls.<p>
     * See <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=273470">Eclipse Bug #273470</a>
     * 
     * @param textControl
     */
    public static void conformSingleTextControl(Text textControl) {
        if(textControl == null || (textControl.getStyle() & SWT.SINGLE) == 0) {
            return;
        }
        
        textControl.addListener(SWT.Verify, new Listener() {
            @Override
            public void handleEvent(Event event) {
                if(StringUtils.isSet(event.text)) {
                    event.text = event.text.replaceAll("(\\r\\n|\\r|\\n)", ""); //$NON-NLS-1$ //$NON-NLS-2$
                }
            }
        });
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
    public static void applyInvalidCharacterFilter(Control textControl) {
        textControl.addListener(SWT.Verify, new Listener() {
            @Override
            public void handleEvent(Event e) {
                e.text = e.text.replaceAll(xml10pattern, ""); //$NON-NLS-1$
            }
        });
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
        
        textControl.addTraverseListener((e) -> {
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
     * Set the font for the control from the preferences. If prefsKey is blank set font to null
     * @param control
     * @param prefsKey
     * @param updateOnPreferencesChange if true then the font on the control is changed when prefences change
     */
    public static void setFontFromPreferences(Control control, String prefsKey, boolean updateOnPreferencesChange) {
        String fontDetails = Preferences.STORE.getString(prefsKey);
        control.setFont(StringUtils.isSet(fontDetails) ? FontFactory.get(fontDetails) : null);
        
        if(updateOnPreferencesChange) {
            applyFontChangePreferenceListener(control, prefsKey);
        }
    }
    
    /**
     * Apply a font change preference listener on a control. The control will update when preference changes.
     * @param control
     * @param prefsKey
     */
    public static void applyFontChangePreferenceListener(Control control, String prefsKey) {
        IPropertyChangeListener listener = new IPropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent event) {
                if(prefsKey == event.getProperty()) {
                    setFontFromPreferences(control, prefsKey, false);
                    control.getParent().layout();
                }
            }
        };
        
        Preferences.STORE.addPropertyChangeListener(listener);
        
        control.addDisposeListener((e) -> {
            Preferences.STORE.removePropertyChangeListener(listener);
        });
    }
}
