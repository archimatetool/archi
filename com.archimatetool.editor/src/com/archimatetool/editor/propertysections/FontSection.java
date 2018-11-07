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
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.diagram.commands.FontCompoundCommand;
import com.archimatetool.editor.diagram.commands.FontStyleCommand;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
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
    
    private static EAttribute FEATURE1 = IArchimatePackage.Literals.FONT_ATTRIBUTE__FONT;
    private static EAttribute FEATURE2 = IArchimatePackage.Literals.FONT_ATTRIBUTE__FONT_COLOR;
    
    /**
     * Filter to show or reject this section depending on being IDiagramModelComponent
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return (object instanceof IFontAttribute) && shouldExposeFeature((EObject)object, FEATURE1);
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
    
    @Override
    protected void createControls(final Composite parent) {
        createLabel(parent, Messages.FontSection_0, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        
        fFontChooser = new FontChooser(parent);
        getWidgetFactory().adapt(fFontChooser.getControl(), true, true); // Need to do it this way for Mac
        fFontChooser.addListener(fontListener);
        
        Preferences.STORE.addPropertyChangeListener(prefsListener);

        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    @Override
    protected void notifyChanged(Notification msg) {
        if(msg.getNotifier() == getFirstSelectedObject()) {
            Object feature = msg.getFeature();
            
            if(feature == FEATURE1 || feature == FEATURE2 || feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
                update();
            }
        }
    }

    @Override
    protected void update() {
        IFontAttribute lastSelected = (IFontAttribute)getFirstSelectedObject();
        
        fFontChooser.setFontObject(lastSelected);
        fFontChooser.setEnabled(!isLocked(lastSelected));
        fFontChooser.setIsDefaultFont(lastSelected.getFont() == null);
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
        
        Preferences.STORE.removePropertyChangeListener(prefsListener);
    }

}
