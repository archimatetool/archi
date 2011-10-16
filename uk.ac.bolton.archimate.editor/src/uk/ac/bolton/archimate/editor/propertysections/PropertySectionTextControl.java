/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.propertysections;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import uk.ac.bolton.archimate.editor.utils.StringUtils;


/**
 * Wrapper Control for Text Control to show hints
 * 
 * @author Phillip Beauvoir
 */
public abstract class PropertySectionTextControl implements FocusListener {
    
    private Control fTextControl;
    private String fHint;
    private EObject fDataElement;
    private EStructuralFeature fFeature;
    
    private Color fTextForegroundColor;
    
    private boolean fHintShowing;
    
    private static final Color greyColor = new Color(null, 188, 188, 188);

    public PropertySectionTextControl(Control textControl, EStructuralFeature feature) {
        fTextControl = textControl;
        fTextForegroundColor = fTextControl.getForeground();
        fFeature = feature;
        
        textControl.addFocusListener(this);
        
        // Listen for Return keypress on SINGLE text controls
        if((textControl.getStyle() & SWT.SINGLE) != 0) {
            textControl.addListener(SWT.DefaultSelection, new Listener() {
                public void handleEvent(Event e) {
                    updateText();
                }
            });
        }
    }
    
    public void setHint(String hint) {
        fHint = hint;
    }
    
    public Control getTextControl() {
        return fTextControl;
    }
    
    public void refresh(EObject dataElement) {
        fDataElement = dataElement;
        
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
    public void focusGained(FocusEvent e) {
        if(fHintShowing) {
            showNormalText(""); // clear hint text
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        updateText();
        
        String newText = getText();
        if(!StringUtils.isSet(newText)) {
            showHintText();
        }
    }
    
    private void updateText() {
        String oldText = ""; // Text control has default of ""
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
            fTextControl.setForeground(greyColor);
            setText(fHint);
            fHintShowing = true;
        }
        else {
            setText(""); // clears previous text if no hint text
        }
    }
    
    private void showNormalText(String text) {
        fTextControl.setForeground(fTextForegroundColor);
        setText(StringUtils.safeString(text));
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

    /**
     * Over-ride this to react to text changes
     * @param oldText
     * @param newText
     */
    protected void textChanged(String oldText, String newText) {
    }
}
