/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IFeatures;



/**
 * Wrapper Control for Text or StyledText Control to retrieve and update a text value
 * 
 * @author Phillip Beauvoir
 */
public abstract class PropertySectionTextControl {
    
    private Control fTextControl;

    private EObject fDataElement;
    private EAttribute fEAttribute;
    private String fFeatureName;
    
    private Listener eventListener = this::handleEvent;
    
    /**
     * @param textControl The Text Control
     * @param feature The Eclipse Feature Attribute containing the text
     */
    public PropertySectionTextControl(Control textControl, EAttribute eAttribute) {
        init(textControl);
        fEAttribute = eAttribute;
    }
    
    /**
     * @param textControl The Text Control
     * @param featureName The name of the Archi Feature containing the text
     */
    public PropertySectionTextControl(Control textControl, String featureName) {
        init(textControl);
        fFeatureName = featureName;
    }
    
    private void init(Control textControl) {
        fTextControl = textControl;
        
        // Focus out updates text in data object
        textControl.addListener(SWT.FocusOut, eventListener);
        
        // Listen for Return keypress on Single text control
        if(isSingleTextControl()) {
            textControl.addListener(SWT.DefaultSelection, eventListener);
        }
        
        textControl.addDisposeListener((event)-> {
            textControl.removeListener(SWT.FocusOut, eventListener);
            textControl.removeListener(SWT.DefaultSelection, eventListener);
            fDataElement = null;
        });
    }
    
    /**
     * This no longer does anything.<br>
     * Use Text.setMessage(String) or StyledTextControl.setMessage(String)
     * @see org.eclipse.swt.widgets.Text
     * @param hint
     */
    @Deprecated
    public void setHint(String hint) {
    }
    
    public Control getTextControl() {
        return fTextControl;
    }
    
    public void setEditable(boolean editable) {
        if(isStyledTextControl()) {
            ((StyledText)getTextControl()).setEditable(editable);
        }
        else {
            ((Text)getTextControl()).setEditable(editable);
        }
    }
    
    /**
     * Refresh text control with text from dataElement
     * @param dataElement The data object owning the text
     */
    public void refresh(EObject dataElement) {
        fDataElement = dataElement;
        setText(getTextFromDataElement());
    }
    
    private void handleEvent(Event event) {
        String newText = getText();
        String oldText = getTextFromDataElement();
        
        if(!newText.equals(oldText)) {
            textChanged(oldText, newText);
        }
    }
    
    private String getTextFromDataElement() {
        // Get text from Eclipse Feature attribute
        if(fDataElement != null && fEAttribute != null) {
            return StringUtils.safeString((String)fDataElement.eGet(fEAttribute));
        }
        
        // Get text from Archi Feature
        if(fDataElement instanceof IFeatures && fFeatureName != null) {
            return StringUtils.safeString(((IFeatures)fDataElement).getFeatures().getString(fFeatureName, "")); //$NON-NLS-1$
        }
        
        return ""; //$NON-NLS-1$
    }
    
    private String getText() {
        if(isStyledTextControl()) {
            return ((StyledText)getTextControl()).getText();
        }
        else {
            return ((Text)getTextControl()).getText();
        }
    }
    
    private void setText(String s) {
        if(isStyledTextControl()) {
            ((StyledText)getTextControl()).setText(s);
        }
        else {
            ((Text)getTextControl()).setText(s);
        }
    }
    
    private boolean isSingleTextControl() {
        return (getTextControl().getStyle() & SWT.SINGLE) != 0;
    }
    
    private boolean isStyledTextControl() {
        return getTextControl() instanceof StyledText;
    }

    /**
     * Clients should over-ride this to react to text changes
     * @param oldText The old text
     * @param newText The new changed text
     */
    protected void textChanged(String oldText, String newText) {
    }
}
