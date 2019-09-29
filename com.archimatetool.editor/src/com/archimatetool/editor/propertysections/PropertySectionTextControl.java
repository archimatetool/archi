/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.ThemeUtils;
import com.archimatetool.editor.utils.StringUtils;



/**
 * Wrapper Control for Text Control to show hints
 * 
 * @author Phillip Beauvoir
 */
public abstract class PropertySectionTextControl implements Listener {
    
    private Control fTextControl;
    private String fHint;
    private EObject fDataElement;
    private EStructuralFeature fFeature;
    
    private Color fOriginalForeGroundColor;
    
    private boolean fHintShowing;
    
    private static final Color lightGrey = ColorFactory.get(188, 188, 188);
    private static final Color darkGrey = ColorFactory.get(112, 112, 112);

    public PropertySectionTextControl(Control textControl, EStructuralFeature feature) {
        fTextControl = textControl;
        fFeature = feature;
        
        textControl.addListener(SWT.FocusIn, this);
        textControl.addListener(SWT.FocusOut, this);
        
        // Listen for Return keypress on Single text control
        if(isSingleTextControl()) {
            textControl.addListener(SWT.DefaultSelection, this);
        }
        
        textControl.addDisposeListener((event)-> {
            textControl.removeListener(SWT.FocusIn, this);
            textControl.removeListener(SWT.FocusOut, this);
            textControl.removeListener(SWT.DefaultSelection, this);
            fDataElement = null;
            fHint = null;
        });
    }
    
    public void setHint(String hint) {
        fHint = hint;
    }
    
    public Control getTextControl() {
        return fTextControl;
    }
    
    public void setEditable(boolean editable) {
        if(fTextControl instanceof Text) {
            ((Text)fTextControl).setEditable(editable);
        }
        if(fTextControl instanceof StyledText) {
            ((StyledText)fTextControl).setEditable(editable);
        }
    }
    
    public void refresh(EObject dataElement) {
        fDataElement = dataElement;
        
        // The foreground of the text control is set later if we are using a theme
        if(fOriginalForeGroundColor == null) {
            fTextControl.getDisplay().asyncExec(() -> {
                if(!fTextControl.isDisposed()) {
                    fOriginalForeGroundColor = fTextControl.getForeground();
                    refresh();
                }
            });
        }
        else {
            refresh();
        }
    }
    
    private void refresh() {
        String text = null;
        
        if(fDataElement != null) {
            text = (String)fDataElement.eGet(fFeature);
        }
        
        if(!StringUtils.isSet(text) && !fTextControl.isFocusControl()) { // Don't do this if text control has focus
            showHintText();
        }
        else if(!getText().equals(text)) {
            showNormalText(text);
        }
    }

    @Override
    public void handleEvent(Event event) {
        switch(event.type) {
            case SWT.FocusIn:
                focusGained();
                break;
            case SWT.FocusOut:
                focusLost();
                break;
            case SWT.DefaultSelection:
                updateText();
                break;
        }
    }
    
    public void focusGained() {
        if(fHintShowing) {
            // clear hint text 
            showNormalText(""); //$NON-NLS-1$
        }
    }

    public void focusLost() {
        updateText();
        
        String newText = getText();
        if(!StringUtils.isSet(newText)) {
            showHintText();
        }
    }
    
    private void updateText() {
        String oldText = ""; // Text control has default of "" //$NON-NLS-1$
        String newText = getText();
        
        if(fDataElement != null) {
            oldText = StringUtils.safeString((String)fDataElement.eGet(fFeature)); // compare like for like
        }
        
        if(!newText.equals(oldText)) {
            textChanged(oldText, newText);
        }
    }
    
    private void showHintText() {
        if(fHint != null) {
            fTextControl.setForeground(ThemeUtils.isDarkTheme() ? darkGrey : lightGrey);
            // do this before setting text
            fTextControl.setData("hintSet", "true"); //$NON-NLS-1$ //$NON-NLS-2$
            // then set text
            setText(fHint);
            fHintShowing = true;
       }
        else {
            setText(""); // clears previous text if no hint text //$NON-NLS-1$
        }
    }
    
    private void showNormalText(String text) {
        fTextControl.setForeground(fOriginalForeGroundColor);
        setText(StringUtils.safeString(text));
        // do this after setting text
        fTextControl.setData("hintSet", null); //$NON-NLS-1$
        fHintShowing = false;
    }
    
    private String getText() {
        if(fTextControl instanceof Text) {
            return ((Text)fTextControl).getText();
        }
        if(fTextControl instanceof StyledText) {
            return ((StyledText)fTextControl).getText();
        }
        return null;
    }
    
    private void setText(String s) {
        if(fTextControl instanceof Text) {
            ((Text)fTextControl).setText(s);
        }
        if(fTextControl instanceof StyledText) {
            ((StyledText)fTextControl).setText(s);
        }
    }
    
    private boolean isSingleTextControl() {
        return (fTextControl.getStyle() & SWT.SINGLE) != 0;
    }

    /**
     * Over-ride this to react to text changes
     * @param oldText
     * @param newText
     */
    protected void textChanged(String oldText, String newText) {
    }
}
