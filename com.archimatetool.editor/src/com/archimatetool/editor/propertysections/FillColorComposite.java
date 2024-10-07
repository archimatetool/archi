/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.diagram.commands.FillColorCommand;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.components.ColorChooser;
import com.archimatetool.model.IDiagramModelObject;



/**
 * Fill Color Composite
 * 
 * @author Phillip Beauvoir
 */
class FillColorComposite {
    
    private ColorChooser fColorChooser;
    private AbstractECorePropertySection section;
    private Composite composite;
    
    FillColorComposite(AbstractECorePropertySection section, Composite parent) {
        this.section = section;
        composite = section.createComposite(parent, 2, false);
        createColorControl(composite);
        addListeners();
    }

    Composite getComposite() {
        return composite;
    }
    
    /**
     * Color listener
     */
    private IPropertyChangeListener colorListener = event -> {
        CompoundCommand result = new CompoundCommand();
        
        if(event.getProperty() == ColorChooser.PROP_COLORCHANGE) {
            RGB rgb = fColorChooser.getColorValue();
            String newColor = ColorFactory.convertRGBToString(rgb);

            for(EObject dmo : section.getEObjects()) {
                if(section.isAlive(dmo) && !section.isLocked(dmo)) {
                    Command cmd = new FillColorCommand((IDiagramModelObject)dmo, newColor);
                    if(cmd.canExecute()) {
                        result.add(cmd);
                    }
                }
            }
        }
        else if(event.getProperty() == ColorChooser.PROP_COLORDEFAULT) {
            for(EObject dmo : section.getEObjects()) {
                if(section.isAlive(dmo) && !section.isLocked(dmo)) {
                    // If user pref to save color is set then save the value, otherwise save as null
                    String rgbValue = null;

                    if(ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.SAVE_USER_DEFAULT_COLOR)) {
                        Color color = ColorFactory.getDefaultFillColor(dmo);
                        rgbValue = ColorFactory.convertColorToString(color);
                    }

                    Command cmd = new FillColorCommand((IDiagramModelObject)dmo, rgbValue);
                    if(cmd.canExecute()) {
                        result.add(cmd);
                    }
                }
            }
        }
        
        section.executeCommand(result.unwrap());
    };
    
    /**
     * Listen to default fill colour changes in Prefs
     */
    private IPropertyChangeListener prefsListener = event -> {
        if(event.getProperty().startsWith(IPreferenceConstants.DEFAULT_FILL_COLOR_PREFIX) ||
                event.getProperty().equals(IPreferenceConstants.SAVE_USER_DEFAULT_COLOR)) { // This will affect the "Default" menu in color chooser
            updateControl();
        }
    };
    
    
    private void createColorControl(Composite parent) {
        section.createLabel(parent, Messages.FillColorSection_0, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        fColorChooser = new ColorChooser(parent, section.getWidgetFactory());
    }
    
    void updateControl() {
        IDiagramModelObject lastSelected = (IDiagramModelObject)section.getFirstSelectedObject();

        String colorValue = lastSelected.getFillColor();
        RGB rgb = ColorFactory.convertStringToRGB(colorValue);
        if(rgb == null) {
            rgb = ColorFactory.getDefaultFillColor(lastSelected).getRGB();
        }

        fColorChooser.setColorValue(rgb);

        fColorChooser.setEnabled(!section.isLocked(lastSelected));

        // If user pref is to save the color then it's a different meaning of default
        boolean isDefaultColor = (colorValue == null);
        if(ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.SAVE_USER_DEFAULT_COLOR)) {
            isDefaultColor = (colorValue != null) && rgb.equals(ColorFactory.getDefaultFillColor(lastSelected).getRGB());
        }
        fColorChooser.setIsDefaultColor(isDefaultColor);
    }
    
    void dispose() {
        removeListeners();
        composite.dispose();
        composite = null;
        section = null;
        fColorChooser = null;
    }
    
    private void addListeners() {
        if(fColorChooser != null) {
            fColorChooser.addListener(colorListener);
        }
        
        ArchiPlugin.PREFERENCES.addPropertyChangeListener(prefsListener);
    }
    
    private void removeListeners() {
        if(fColorChooser != null) {
            fColorChooser.removeListener(colorListener);
        }
        
        ArchiPlugin.PREFERENCES.removePropertyChangeListener(prefsListener);
    }
}
