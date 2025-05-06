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
import com.archimatetool.model.IArchimateModelObject;
import com.archimatetool.model.IArchimatePackage;
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
                if(isValidObject(dmo)) {
                    Command cmd = new FillColorCommand((IDiagramModelObject)dmo, newColor);
                    if(cmd.canExecute()) {
                        result.add(cmd);
                    }
                }
            }
        }
        else if(event.getProperty() == ColorChooser.PROP_COLORDEFAULT) {
            for(EObject dmo : section.getEObjects()) {
                if(isValidObject(dmo)) {
                    // If user pref to save color is set then save the value, otherwise save as null
                    String rgbValue = null;

                    if(ArchiPlugin.getInstance().getPreferenceStore().getBoolean(IPreferenceConstants.SAVE_USER_DEFAULT_COLOR)) {
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
     * In case of multi-selection we should check this
     */
    private boolean isValidObject(EObject eObject) {
        return section.isAlive(eObject) && !section.isLocked(eObject) && 
                section.getFilter().shouldExposeFeature(eObject, IArchimatePackage.Literals.DIAGRAM_MODEL_OBJECT__FILL_COLOR.getName());
    }
    
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
        IDiagramModelObject firstSelected = (IDiagramModelObject)section.getFirstSelectedObject();

        RGB rgb = ColorFactory.convertStringToRGB(firstSelected.getFillColor());
        if(rgb == null) {
            rgb = ColorFactory.getDefaultFillColor(firstSelected).getRGB();
        }

        fColorChooser.setColorValue(rgb);
        fColorChooser.setEnabled(!section.isLocked(firstSelected));

        // Set default enabled based on all selected objects.
        // Note that the default button might not show the correct enabled state depending on what's selected at the time of the action.
        boolean isDefaultColor = true;
        // If user pref is to save the color then it's a different meaning of default
        boolean saveUserDefaultColor = ArchiPlugin.getInstance().getPreferenceStore().getBoolean(IPreferenceConstants.SAVE_USER_DEFAULT_COLOR);
        
        for(IArchimateModelObject object : section.getEObjects()) {
            if(object instanceof IDiagramModelObject dmo) {
                if(saveUserDefaultColor) {
                    isDefaultColor &= (dmo.getFillColor() != null && rgb.equals(ColorFactory.getDefaultFillColor(dmo).getRGB()));
                }
                else {
                    isDefaultColor &= dmo.getFillColor() == null;
                }
            }
        }
        
        fColorChooser.setIsDefaultColor(isDefaultColor);
    }
    
    void dispose() {
        removeListeners();
        composite = null;
        section = null;
        fColorChooser = null;
    }
    
    private void addListeners() {
        if(fColorChooser != null) {
            fColorChooser.addListener(colorListener);
        }
        
        ArchiPlugin.getInstance().getPreferenceStore().addPropertyChangeListener(prefsListener);
    }
    
    private void removeListeners() {
        if(fColorChooser != null) {
            fColorChooser.removeListener(colorListener);
        }
        
        ArchiPlugin.getInstance().getPreferenceStore().removePropertyChangeListener(prefsListener);
    }
}
