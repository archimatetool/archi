/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.diagram.commands.LineColorCommand;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.components.ColorChooser;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.ILineObject;
import com.archimatetool.model.ILockable;



/**
 * Property Section for a Line Color
 * 
 * @author Phillip Beauvoir
 */
public class LineColorSection extends AbstractArchimatePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$
    
    private static EAttribute FEATURE = IArchimatePackage.Literals.LINE_OBJECT__LINE_COLOR;
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        protected boolean isRequiredType(Object object) {
            boolean result = (object instanceof ILineObject);
            if(object instanceof IDiagramModelComponent) {
                result &= ((IDiagramModelComponent)object).shouldExposeFeature(FEATURE);
            }
            return result;
        }

        @Override
        protected Class<?> getAdaptableType() {
            return ILineObject.class;
        }
    }

    /*
     * Adapter to listen to changes made elsewhere (including Undo/Redo commands)
     */
    private Adapter eAdapter = new AdapterImpl() {
        @Override
        public void notifyChanged(Notification msg) {
            Object feature = msg.getFeature();
            // Color event (From Undo/Redo and here)
            if(feature == FEATURE || feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
                refreshControls();
            }
        }
    };
    
    /**
     * Color listener
     */
    private IPropertyChangeListener colorListener = new IPropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent event) {
            if(isAlive()) {
                if(event.getProperty() == ColorChooser.PROP_COLORCHANGE) {
                    RGB rgb = fColorChooser.getColorValue();
                    String newColor = ColorFactory.convertRGBToString(rgb);
                    if(!newColor.equals(fLineObject.getLineColor())) {
                        getCommandStack().execute(new LineColorCommand(fLineObject, newColor));
                    }
                }
                else if(event.getProperty() == ColorChooser.PROP_COLORDEFAULT) {
                    // If user pref to save color is set then save the value, otherwise save as null
                    String rgbValue = null;
                    if(Preferences.STORE.getBoolean(IPreferenceConstants.SAVE_USER_DEFAULT_COLOR)) {
                        Color color = ColorFactory.getDefaultLineColor(fLineObject);
                        rgbValue = ColorFactory.convertColorToString(color);
                    }
                    getCommandStack().execute(new LineColorCommand(fLineObject, rgbValue));
                }
            }
        }
    };
    
    /**
     * Listen to default element line colour change in Prefs
     */
    private IPropertyChangeListener prefsListener = new IPropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent event) {
            if(event.getProperty().equals(IPreferenceConstants.DEFAULT_ELEMENT_LINE_COLOR) ||
                    event.getProperty().equals(IPreferenceConstants.DEFAULT_CONNECTION_LINE_COLOR) ||
                    event.getProperty().equals(IPreferenceConstants.DERIVE_ELEMENT_LINE_COLOR) ||
                    event.getProperty().equals(IPreferenceConstants.SAVE_USER_DEFAULT_COLOR)) { // This will affect the "Default" menu in color chooser
                refreshControls();
            }
        }
    };

    private ILineObject fLineObject;

    private ColorChooser fColorChooser;
    
    @Override
    protected void createControls(Composite parent) {
        createColorControl(parent);
        
        Preferences.STORE.addPropertyChangeListener(prefsListener);
        
        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    private void createColorControl(Composite parent) {
        createLabel(parent, Messages.LineColorSection_0, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        
        fColorChooser = new ColorChooser(parent);
        getWidgetFactory().adapt(fColorChooser.getControl(), true, true);
        fColorChooser.addListener(colorListener);
    }
    
    @Override
    protected void setElement(Object element) {
        fLineObject = (ILineObject)new Filter().adaptObject(element);
        if(fLineObject == null) {
            System.err.println(getClass() + " failed to get element for " + element); //$NON-NLS-1$
        }
        
        refreshControls();
    }
    
    protected void refreshControls() {
        String colorValue = fLineObject.getLineColor();
        RGB rgb = ColorFactory.convertStringToRGB(colorValue);
        if(rgb == null) {
            rgb = ColorFactory.getDefaultLineColor(fLineObject).getRGB();
        }
        
        fColorChooser.setColorValue(rgb);

        // Locked
        boolean enabled = fLineObject instanceof ILockable ? !((ILockable)fLineObject).isLocked() : true;
        fColorChooser.setEnabled(enabled);
        
        // If the user pref is to save the color in the file, then it's a different meaning of default
        boolean isDefaultColor = (colorValue == null);
        if(Preferences.STORE.getBoolean(IPreferenceConstants.SAVE_USER_DEFAULT_COLOR)) {
            isDefaultColor = (colorValue != null) && rgb.equals(ColorFactory.getDefaultLineColor(fLineObject).getRGB());
        }
        fColorChooser.setIsDefaultColor(isDefaultColor);
        
        // If this is an element line disable some things
        if(fLineObject instanceof IDiagramModelObject) {
            boolean deriveElementLineColor = Preferences.STORE.getBoolean(IPreferenceConstants.DERIVE_ELEMENT_LINE_COLOR);
            fColorChooser.setDoShowColorImage(!deriveElementLineColor);
            fColorChooser.getColorButton().setEnabled(!deriveElementLineColor);
            fColorChooser.setDoShowDefaultMenuItem(!deriveElementLineColor);
        }
        else {
            fColorChooser.setDoShowColorImage(true);
            fColorChooser.getColorButton().setEnabled(true);
            fColorChooser.setDoShowDefaultMenuItem(true);
        }
    }
    
    @Override
    public void dispose() {
        super.dispose();
        
        if(fColorChooser != null) {
            fColorChooser.removeListener(colorListener);
        }
        
        Preferences.STORE.removePropertyChangeListener(prefsListener);
    }

    @Override
    protected Adapter getECoreAdapter() {
        return eAdapter;
    }

    @Override
    protected EObject getEObject() {
        return fLineObject;
    }
}
