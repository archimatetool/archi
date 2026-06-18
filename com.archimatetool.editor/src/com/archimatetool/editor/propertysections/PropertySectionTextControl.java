/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import java.util.Objects;
import java.util.function.BiConsumer;

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
public class PropertySectionTextControl {
    
    private Control fTextControl;

    private EObject fDataElement;
    private EAttribute fEAttribute;
    private String fFeatureName;
    
    private Listener eventListener = this::handleEvent;
    
    /**
     * Consumer for textChanged
     */
    private BiConsumer<String, String> onTextChanged;
    
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
        if(!(textControl instanceof Text || textControl instanceof StyledText)) {
            throw new IllegalArgumentException("Control must be Text or StyledText"); //$NON-NLS-1$
        }
        
        fTextControl = textControl;
        
        // FocusOut updates the text
        textControl.addListener(SWT.FocusOut, eventListener);
        
        // Listen for Enter (or Ctrl+Enter) keypress
        textControl.addListener(SWT.DefaultSelection, eventListener);
        
        textControl.addDisposeListener(e -> {
            textControl.removeListener(SWT.FocusOut, eventListener);
            textControl.removeListener(SWT.DefaultSelection, eventListener);
            fDataElement = null;
        });
    }
    
    public Control getTextControl() {
        return fTextControl;
    }
    
    /**
     * Set consumer to handle when text changes
     * First param is oldText, second param is newText
     * @param onTextChanged The consumer
     * @since 5.10
     */
    public void setOnTextChanged(BiConsumer<String, String> onTextChanged) {
        this.onTextChanged = Objects.requireNonNull(onTextChanged);
    }

    public void setEditable(boolean editable) {
        if(getTextControl() instanceof StyledText styledText) {
            styledText.setEditable(editable);
        }
        else if(getTextControl() instanceof Text text) {
            text.setEditable(editable);
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
        
        if(!Objects.equals(newText, oldText)) {
            textChanged(oldText, newText);
        }
    }
    
    private String getTextFromDataElement() {
        // Get text from Eclipse Feature attribute
        if(fDataElement != null && fEAttribute != null) {
            return StringUtils.safeString((String)fDataElement.eGet(fEAttribute));
        }
        
        // Get text from Archi Feature
        if(fDataElement instanceof IFeatures features && fFeatureName != null) {
            return StringUtils.safeString(features.getFeatures().getString(fFeatureName, "")); //$NON-NLS-1$
        }
        
        return ""; //$NON-NLS-1$
    }
    
    private String getText() {
        if(getTextControl() instanceof StyledText styledText) {
            return styledText.getText();
        }
        else if(getTextControl() instanceof Text text) {
            return text.getText();
        }
        return ""; //$NON-NLS-1$
    }
    
    private void setText(String s) {
        if(getTextControl() instanceof StyledText styledText) {
            styledText.setText(s);
        }
        else if(getTextControl() instanceof Text text) {
            text.setText(s);
        }
    }
    
    /**
     * Clients can over-ride this to react to text changes
     * @param oldText The old text
     * @param newText The new changed text
     * @deprecated since 5.10 use {@link #setOnTextChanged(BiConsumer)}
     */
    protected void textChanged(String oldText, String newText) {
        if(onTextChanged != null) {
            onTextChanged.accept(oldText, newText);
        }
    }
}
