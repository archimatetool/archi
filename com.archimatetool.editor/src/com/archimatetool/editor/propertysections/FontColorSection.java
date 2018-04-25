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
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.diagram.commands.FontColorCommand;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.components.ColorChooser;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.IFontAttribute;



/**
 * Property Section for a Font Color Section
 * 
 * @author Phillip Beauvoir
 */
public class FontColorSection extends AbstractECorePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$
    
    private static EAttribute FEATURE = IArchimatePackage.Literals.FONT_ATTRIBUTE__FONT_COLOR;
    
    /**
     * Filter to show or reject this section depending on being IDiagramModelComponent
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return (object instanceof IFontAttribute) && shouldExposeFeature((EObject)object, FEATURE);
        }

        @Override
        public Class<?> getAdaptableType() {
            return IDiagramModelComponent.class;
        }
    }
    
    /**
     * Color listener
     */
    private IPropertyChangeListener colorListener = new IPropertyChangeListener() {
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
    
    private ColorChooser fColorChooser;
    
    @Override
    protected void createControls(Composite parent) {
        createColorControl(parent);
        
        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    private void createColorControl(Composite parent) {
        createLabel(parent, Messages.FontColorSection_0, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        
        fColorChooser = new ColorChooser(parent);
        fColorChooser.setDoShowPreferencesMenuItem(false);
        getWidgetFactory().adapt(fColorChooser.getControl(), true, true);
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
        
        if(fColorChooser != null) {
            fColorChooser.removeListener(colorListener);
        }
    }
}
