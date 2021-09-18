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
import com.archimatetool.editor.diagram.commands.FillColorCommand;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.components.ColorChooser;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelObject;



/**
 * Property Section for a Fill Color
 * 
 * @author Phillip Beauvoir
 */
public class FillColorSection extends AbstractECorePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$
    
    private static EAttribute FEATURE = IArchimatePackage.Literals.DIAGRAM_MODEL_OBJECT__FILL_COLOR;
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return (object instanceof IDiagramModelObject) && shouldExposeFeature((EObject)object, FEATURE.getName());
        }

        @Override
        public Class<?> getAdaptableType() {
            return IDiagramModelObject.class;
        }
    }
    
    /**
     * Color listener
     */
    private IPropertyChangeListener colorListener = new IPropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent event) {
            if(event.getProperty() == ColorChooser.PROP_COLORCHANGE) {
                CompoundCommand result = new CompoundCommand();

                RGB rgb = fColorChooser.getColorValue();
                String newColor = ColorFactory.convertRGBToString(rgb);

                for(EObject dmo : getEObjects()) {
                    if(isAlive(dmo) && !isLocked(dmo)) {
                        Command cmd = new FillColorCommand((IDiagramModelObject)dmo, newColor);
                        if(cmd.canExecute()) {
                            result.add(cmd);
                        }
                    }
                }

                executeCommand(result.unwrap());
            }
            else if(event.getProperty() == ColorChooser.PROP_COLORDEFAULT) {
                CompoundCommand result = new CompoundCommand();

                for(EObject dmo : getEObjects()) {
                    if(isAlive(dmo) && !isLocked(dmo)) {
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

                executeCommand(result.unwrap());
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
        createLabel(parent, Messages.FillColorSection_0, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        
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
        IDiagramModelObject lastSelected = (IDiagramModelObject)getFirstSelectedObject();
        
        String colorValue = lastSelected.getFillColor();
        RGB rgb = ColorFactory.convertStringToRGB(colorValue);
        if(rgb == null) {
            rgb = ColorFactory.getDefaultFillColor(lastSelected).getRGB();
        }
        
        fColorChooser.setColorValue(rgb);
        
        fColorChooser.setEnabled(!isLocked(lastSelected));
        
        // If user pref is to save the color then it's a different meaning of default
        boolean isDefaultColor = (colorValue == null);
        if(ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.SAVE_USER_DEFAULT_COLOR)) {
            isDefaultColor = (colorValue != null) && rgb.equals(ColorFactory.getDefaultFillColor(lastSelected).getRGB());
        }
        fColorChooser.setIsDefaultColor(isDefaultColor);
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
