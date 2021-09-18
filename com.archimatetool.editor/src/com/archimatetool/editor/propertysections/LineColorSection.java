/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.diagram.commands.LineColorCommand;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.components.ColorChooser;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.ILineObject;



/**
 * Property Section for a Line Color
 * 
 * @author Phillip Beauvoir
 */
public class LineColorSection extends AbstractECorePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$
    
    private static EAttribute FEATURE = IArchimatePackage.Literals.LINE_OBJECT__LINE_COLOR;
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return (object instanceof ILineObject) && shouldExposeFeature((EObject)object, FEATURE.getName());
        }

        @Override
        public Class<?> getAdaptableType() {
            return ILineObject.class;
        }
    }

    /**
     * Color listener
     */
    private IPropertyChangeListener colorListener = new IPropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent event) {
            CompoundCommand result = new CompoundCommand();
            
            if(event.getProperty() == ColorChooser.PROP_COLORCHANGE) {
                RGB rgb = fColorChooser.getColorValue();
                String newColor = ColorFactory.convertRGBToString(rgb);
                
                for(EObject lineObject : getEObjects()) {
                    if(isAlive(lineObject)) {
                        Command cmd = new LineColorCommand((ILineObject)lineObject, newColor);
                        if(cmd.canExecute()) {
                            result.add(cmd);
                        }
                    }
                }
            }
            else if(event.getProperty() == ColorChooser.PROP_COLORDEFAULT) {
                for(EObject lineObject : getEObjects()) {
                    if(isAlive(lineObject)) {
                        // If user pref to save color is set then save the value, otherwise save as null
                        String rgbValue = null;
                        
                        if(ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.SAVE_USER_DEFAULT_COLOR)) {
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
            
            executeCommand(result.unwrap());
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
                update();
            }
        }
    };

    private ColorChooser fColorChooser;
    
    @Override
    protected void createControls(Composite parent) {
        createColorControl(parent);
        
        ArchiPlugin.PREFERENCES.addPropertyChangeListener(prefsListener);
        
        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    private void createColorControl(Composite parent) {
        createLabel(parent, Messages.LineColorSection_0, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        
        fColorChooser = new ColorChooser(parent, getWidgetFactory());
        fColorChooser.addListener(colorListener);
    }
    
    @Override
    protected void notifyChanged(Notification msg) {
        if(msg.getNotifier() == getFirstSelectedObject()) {
            Object feature = msg.getFeature();
            
            if(feature == FEATURE || feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
                update();
            }
        }
    }

    @Override
    protected void update() {
        ILineObject lineObject = (ILineObject)getFirstSelectedObject();
        
        String colorValue = lineObject.getLineColor();
        RGB rgb = ColorFactory.convertStringToRGB(colorValue);
        if(rgb == null) {
            rgb = ColorFactory.getDefaultLineColor(lineObject).getRGB();
        }
        
        fColorChooser.setColorValue(rgb);

        // Locked
        boolean enabled = !isLocked(lineObject);
        
        fColorChooser.setEnabled(enabled);
        
        if(!enabled) {
            return;
        }
        
        // If the user pref is to save the color in the file, then it's a different meaning of default
        boolean isDefaultColor = (colorValue == null);
        if(ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.SAVE_USER_DEFAULT_COLOR)) {
            isDefaultColor = (colorValue != null) && rgb.equals(ColorFactory.getDefaultLineColor(lineObject).getRGB());
        }
        fColorChooser.setIsDefaultColor(isDefaultColor);
        
        // If this is an element line disable some things
        if(lineObject instanceof IDiagramModelObject) {
            boolean deriveElementLineColor = ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.DERIVE_ELEMENT_LINE_COLOR);
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
    protected IObjectFilter getFilter() {
        return new Filter();
    }
    
    @Override
    public void dispose() {
        super.dispose();
        
        if(fColorChooser != null) {
            fColorChooser.removeListener(colorListener);
        }
        
        ArchiPlugin.PREFERENCES.removePropertyChangeListener(prefsListener);
    }
}
