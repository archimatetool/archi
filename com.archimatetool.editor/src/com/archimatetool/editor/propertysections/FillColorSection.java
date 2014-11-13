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

import com.archimatetool.editor.diagram.commands.FillColorCommand;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.components.ColorChooser;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.ILockable;



/**
 * Property Section for a Fill Color
 * 
 * @author Phillip Beauvoir
 */
public class FillColorSection extends AbstractArchimatePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$
    
    private static EAttribute FEATURE = IArchimatePackage.Literals.DIAGRAM_MODEL_OBJECT__FILL_COLOR;
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        boolean isRequiredType(Object object) {
            return object instanceof IDiagramModelObject && ((IDiagramModelObject)object).shouldExposeFeature(FEATURE);
        }

        @Override
        Class<?> getAdaptableType() {
            return IDiagramModelObject.class;
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
                    if(!newColor.equals(fDiagramModelObject.getFillColor())) {
                        getCommandStack().execute(new FillColorCommand(fDiagramModelObject, newColor));
                    }
                }
                else if(event.getProperty() == ColorChooser.PROP_COLORDEFAULT) {
                    // If user pref to save color is set then save the value, otherwise save as null
                    String rgbValue = null;
                    if(Preferences.STORE.getBoolean(IPreferenceConstants.SAVE_USER_DEFAULT_COLOR)) {
                        Color color = ColorFactory.getDefaultFillColor(fDiagramModelObject);
                        rgbValue = ColorFactory.convertColorToString(color);
                    }
                    getCommandStack().execute(new FillColorCommand(fDiagramModelObject, rgbValue));
                }
            }
        }
    };
    
    /**
     * Listen to default fill colour change in Prefs
     */
    private IPropertyChangeListener prefsListener = new IPropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent event) {
            if(event.getProperty().startsWith(IPreferenceConstants.DEFAULT_FILL_COLOR_PREFIX) ||
                    event.getProperty().equals(IPreferenceConstants.SAVE_USER_DEFAULT_COLOR)) { // This will affect the "Default" menu in color chooser
                refreshControls();
            }
        }
    };
    
    private IDiagramModelObject fDiagramModelObject;

    private ColorChooser fColorChooser;
    
    @Override
    protected void createControls(Composite parent) {
        createColorControl(parent);
        
        Preferences.STORE.addPropertyChangeListener(prefsListener);
        
        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    private void createColorControl(Composite parent) {
        createLabel(parent, Messages.FillColorSection_0, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        
        fColorChooser = new ColorChooser(parent);
        getWidgetFactory().adapt(fColorChooser.getControl(), true, true);
        fColorChooser.addListener(colorListener);
    }
    
    @Override
    protected void setElement(Object element) {
        fDiagramModelObject = (IDiagramModelObject)new Filter().adaptObject(element);
        if(fDiagramModelObject == null) {
            System.err.println(getClass() + " failed to get element for " + element); //$NON-NLS-1$
        }
        
        refreshControls();
    }
    
    protected void refreshControls() {
        String colorValue = fDiagramModelObject.getFillColor();
        RGB rgb = ColorFactory.convertStringToRGB(colorValue);
        if(rgb == null) {
            rgb = ColorFactory.getDefaultFillColor(fDiagramModelObject).getRGB();
        }
        
        fColorChooser.setColorValue(rgb);
        
        boolean enabled = fDiagramModelObject instanceof ILockable ? !((ILockable)fDiagramModelObject).isLocked() : true;
        fColorChooser.setEnabled(enabled);
        
        // If user pref is to save the color then it's a different meaning of default
        boolean isDefaultColor = (colorValue == null);
        if(Preferences.STORE.getBoolean(IPreferenceConstants.SAVE_USER_DEFAULT_COLOR)) {
            isDefaultColor = (colorValue != null) && rgb.equals(ColorFactory.getDefaultFillColor(fDiagramModelObject).getRGB());
        }
        fColorChooser.setIsDefaultColor(isDefaultColor);
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
        return fDiagramModelObject;
    }
}
