/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.diagram.commands.FontColorCommand;
import com.archimatetool.editor.diagram.commands.FontCompoundCommand;
import com.archimatetool.editor.diagram.commands.FontStyleCommand;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.components.ColorChooser;
import com.archimatetool.editor.ui.components.FontChooser;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.IFontAttribute;



/**
 * Property Section for a Diagram Model Object Appearance->Font Section
 * 
 * @author Phillip Beauvoir
 */
public class FontSection extends AbstractECorePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$
    
    /**
     * Filter to show or reject this section depending on being IDiagramModelComponent
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return (object instanceof IFontAttribute) && (shouldExposeFeature((EObject)object, IArchimatePackage.Literals.FONT_ATTRIBUTE__FONT.getName())
                    || shouldExposeFeature((EObject)object, IArchimatePackage.Literals.FONT_ATTRIBUTE__FONT_COLOR.getName()));
        }

        @Override
        public Class<?> getAdaptableType() {
            return IDiagramModelComponent.class;
        }
    }
    
    /**
     * Font listener
     */
    private IPropertyChangeListener fontListener = new IPropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent event) {
            CompoundCommand result = new CompoundCommand();
            
            if(event.getProperty() == FontChooser.PROP_FONTCHANGE) {
                FontData selectedFontData = fFontChooser.getFontData();
                RGB rgb = fFontChooser.getFontRGB();
                
                for(EObject fa : getEObjects()) {
                    if(isAlive(fa)) {
                        Command cmd = new FontCompoundCommand((IFontAttribute)fa, selectedFontData.toString(), rgb);
                        if(cmd.canExecute()) {
                            result.add(cmd);
                        }
                    }
                }
            }
            else if(event.getProperty() == FontChooser.PROP_FONTDEFAULT) {
                for(EObject fa : getEObjects()) {
                    if(isAlive(fa)) {
                        Command cmd = new FontStyleCommand((IFontAttribute)fa, null);
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
     * Color listener
     */
    private IPropertyChangeListener colorListener = new IPropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent event) {
            CompoundCommand result = new CompoundCommand();
            
            if(event.getProperty() == ColorChooser.PROP_COLORCHANGE) {
                RGB rgb = fColorChooser.getColorValue();
                String newColor = ColorFactory.convertRGBToString(rgb);
                
                for(EObject fa : getEObjects()) {
                    if(isAlive(fa)) {
                        Command cmd = new FontColorCommand((IFontAttribute)fa, newColor);
                        if(cmd.canExecute()) {
                            result.add(cmd);
                        }
                    }
                }
            }
            else if(event.getProperty() == ColorChooser.PROP_COLORDEFAULT) {
                for(EObject fa : getEObjects()) {
                    if(isAlive(fa)) {
                        Command cmd = new FontColorCommand((IFontAttribute)fa, null);
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
     * Listen to default font change in Prefs
     */
    private IPropertyChangeListener prefsListener = new IPropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent event) {
            if(event.getProperty().startsWith(IPreferenceConstants.DEFAULT_VIEW_FONT)) {
                update();
            }
        }
    };

    private FontChooser fFontChooser;
    private ColorChooser fColorChooser;
    
    @Override
    protected void createControls(final Composite parent) {
        ((GridLayout)parent.getLayout()).horizontalSpacing = 30;
        
        Composite group1 = createComposite(parent, 2, false);
        createFontControl(group1);
        
        Composite group2 = createComposite(parent, 2, false);
        createColorControl(group2);
        
        // Allow setting 1 or 2 columns
        GridLayoutColumnHandler.create(parent, 2).updateColumns();

        ArchiPlugin.PREFERENCES.addPropertyChangeListener(prefsListener);

        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    private void createFontControl(Composite parent) {
        createLabel(parent, Messages.FontSection_0, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        fFontChooser = new FontChooser(parent, getWidgetFactory());
        fFontChooser.addListener(fontListener);
    }
    
    private void createColorControl(Composite parent) {
        createLabel(parent, Messages.FontColorSection_0, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        fColorChooser = new ColorChooser(parent, getWidgetFactory());
        fColorChooser.setDoShowPreferencesMenuItem(false);
        fColorChooser.addListener(colorListener);
    }
    
    @Override
    protected void notifyChanged(Notification msg) {
        if(msg.getNotifier() == getFirstSelectedObject()) {
            Object feature = msg.getFeature();
            
            if(feature == IArchimatePackage.Literals.FONT_ATTRIBUTE__FONT || 
                    feature == IArchimatePackage.Literals.FONT_ATTRIBUTE__FONT_COLOR ||
                        feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
                update();
            }
        }
    }

    @Override
    protected void update() {
        updateFontControl();
        updateColorControl();
    }
    
    private void updateFontControl() {
        IFontAttribute lastSelected = (IFontAttribute)getFirstSelectedObject();
        
        fFontChooser.setFontObject(lastSelected);
        fFontChooser.setEnabled(!isLocked(lastSelected));
        fFontChooser.setIsDefaultFont(lastSelected.getFont() == null);
    }
    
    private void updateColorControl() {
        String colorValue = ((IFontAttribute)getFirstSelectedObject()).getFontColor();
        RGB rgb = ColorFactory.convertStringToRGB(colorValue);
        
        if(rgb != null) {
            fColorChooser.setColorValue(rgb);
        }
        else {
            // Null is the default system color
            fColorChooser.setColorValue(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_FOREGROUND).getRGB());
        }
        
        fColorChooser.setEnabled(!isLocked(getFirstSelectedObject()));
        fColorChooser.setIsDefaultColor(colorValue == null);
    }

    @Override
    protected IObjectFilter getFilter() {
        return new Filter();
    }

    @Override
    public void dispose() {
        super.dispose();
        
        if(fFontChooser != null) {
            fFontChooser.removeListener(fontListener);
        }
        
        if(fColorChooser != null) {
            fColorChooser.removeListener(colorListener);
        }
        
        ArchiPlugin.PREFERENCES.removePropertyChangeListener(prefsListener);
    }

}
