/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.diagram.commands.LineColorCommand;
import com.archimatetool.editor.model.commands.FeatureCommand;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.components.ColorChooser;
import com.archimatetool.model.IArchimateModelObject;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IFeatures;
import com.archimatetool.model.ILineObject;



/**
 * Line Color Composite
 * 
 * @author Phillip Beauvoir
 */
class LineColorComposite {
    
    private ColorChooser fColorChooser;
    private Action fDeriveLineColorAction;
    private AbstractECorePropertySection section;
    private Composite composite;
    
    LineColorComposite(AbstractECorePropertySection section, Composite parent) {
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
            
            for(EObject lineObject : section.getEObjects()) {
                if(isValidLineColorObject(lineObject)) {
                    Command cmd = new LineColorCommand((ILineObject)lineObject, newColor);
                    if(cmd.canExecute()) {
                        result.add(cmd);
                    }
                }
            }
        }
        else if(event.getProperty() == ColorChooser.PROP_COLORDEFAULT) {
            for(EObject lineObject : section.getEObjects()) {
                if(isValidLineColorObject(lineObject)) {
                    // If user pref to save color is set then save the value, otherwise save as null
                    String rgbValue = null;
                    
                    if(ArchiPlugin.getInstance().getPreferenceStore().getBoolean(IPreferenceConstants.SAVE_USER_DEFAULT_COLOR)) {
                        Color color = ColorFactory.getDefaultLineColor(lineObject);
                        rgbValue = ColorFactory.convertColorToString(color);
                    }

                    Command cmd = new LineColorCommand((ILineObject)lineObject, rgbValue);
                    if(cmd.canExecute()) {
                        result.add(cmd);
                    }
                }
            }
        }
        
        section.executeCommand(result.unwrap());
    };
    
    /**
     * Listen to default line colour changes in Prefs
     */
    private IPropertyChangeListener prefsListener = event -> {
        if(event.getProperty().equals(IPreferenceConstants.DEFAULT_ELEMENT_LINE_COLOR) ||
                event.getProperty().equals(IPreferenceConstants.DEFAULT_CONNECTION_LINE_COLOR) ||
                event.getProperty().equals(IPreferenceConstants.SAVE_USER_DEFAULT_COLOR)) { // This will affect the "Default" menu in color chooser
            updateControl();
        }
    };
    
    
    private void createColorControl(Composite parent) {
        section.createLabel(parent, Messages.LineColorSection_0, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        
        fColorChooser = new ColorChooser(parent, section.getWidgetFactory());
        
        // Derive line color from fill color action
        fDeriveLineColorAction = new Action(Messages.LineColorSection_3, IAction.AS_CHECK_BOX) {
            @Override
            public void run() {
                CompoundCommand result = new CompoundCommand();

                for(EObject object : section.getEObjects()) {
                    if(isValidDerivedLineColorObject(object)) {
                        Command cmd = new FeatureCommand(Messages.LineColorSection_4, (IFeatures)object, IDiagramModelObject.FEATURE_DERIVE_ELEMENT_LINE_COLOR,
                                fDeriveLineColorAction.isChecked(), IDiagramModelObject.FEATURE_DERIVE_ELEMENT_LINE_COLOR_DEFAULT);
                        if(cmd.canExecute()) {
                            result.add(cmd);
                        }
                    }
                }

                section.executeCommand(result.unwrap());
            }
        };
    }
    
    void updateControl() {
        ILineObject firstSelected = (ILineObject)section.getFirstSelectedObject();
        
        RGB rgb = ColorFactory.convertStringToRGB(firstSelected.getLineColor());
        if(rgb == null) {
            rgb = ColorFactory.getDefaultLineColor(firstSelected).getRGB();
        }
        
        fColorChooser.setColorValue(rgb);
        fColorChooser.setEnabled(!section.isLocked(firstSelected));
        
        // Set default enabled based on all selected objects.
        // Note that the default button might not show the correct enabled state depending on what's selected at the time of the action.
        boolean isDefaultColor = true;
        // If user pref is to save the color then it's a different meaning of default
        boolean saveUserDefaultColor = ArchiPlugin.getInstance().getPreferenceStore().getBoolean(IPreferenceConstants.SAVE_USER_DEFAULT_COLOR);
        
        for(IArchimateModelObject object : section.getEObjects()) {
            if(object instanceof ILineObject lo) {
                if(saveUserDefaultColor) {
                    isDefaultColor &= (lo.getLineColor() != null && rgb.equals(ColorFactory.getDefaultLineColor(lo).getRGB()));
                }
                else {
                    isDefaultColor &= lo.getLineColor() == null;
                }
            }
        }
        
        fColorChooser.setIsDefaultColor(isDefaultColor);
        
        // If this is an object line enable or disable some things
        if(firstSelected instanceof IDiagramModelObject dmo) {
            boolean deriveElementLineColor = dmo.getDeriveElementLineColor();
            
            fColorChooser.setDoShowColorImage(!deriveElementLineColor);
            fColorChooser.getColorButton().setEnabled(!deriveElementLineColor);
            fColorChooser.setDoShowDefaultMenuItem(!deriveElementLineColor);
            
            fColorChooser.addMenuAction(fDeriveLineColorAction);
            fDeriveLineColorAction.setChecked(deriveElementLineColor);
        }
        // Connection line
        else {
            fColorChooser.setDoShowColorImage(true);
            fColorChooser.getColorButton().setEnabled(true);
            fColorChooser.setDoShowDefaultMenuItem(true);
        }
    }
    
    /**
     * In case of multi-selection we should check this
     */
    private boolean isValidLineColorObject(EObject eObject) {
        return section.isAlive(eObject) && 
                section.getFilter().shouldExposeFeature(eObject, IArchimatePackage.Literals.LINE_OBJECT__LINE_COLOR.getName());
    }
    
    /**
     * In case of multi-selection we should check this
     */
    private boolean isValidDerivedLineColorObject(EObject eObject) {
        return isValidLineColorObject(eObject) &&
                eObject instanceof IDiagramModelObject && 
                section.getFilter().shouldExposeFeature(eObject, IDiagramModelObject.FEATURE_DERIVE_ELEMENT_LINE_COLOR);
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
