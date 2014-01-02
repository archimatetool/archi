/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
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
import com.archimatetool.model.IFontAttribute;
import com.archimatetool.model.ILockable;



/**
 * Property Section for a Diagram Model Object Appearance->Font Section
 * 
 * @author Phillip Beauvoir
 */
public class FontSection extends AbstractArchimatePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$
    
    /*
     * Adapter to listen to changes made elsewhere (including Undo/Redo commands)
     */
    private Adapter eAdapter = new AdapterImpl() {
        @Override
        public void notifyChanged(Notification msg) {
            Object feature = msg.getFeature();
            // Color event (From Undo/Redo and here)
            if(feature == IArchimatePackage.Literals.FONT_ATTRIBUTE__FONT || feature == IArchimatePackage.Literals.FONT_ATTRIBUTE__FONT_COLOR ||
                    feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
                refreshControls();
            }
        }
    };
    
    /**
     * Font listener
     */
    private IPropertyChangeListener fontListener = new IPropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent event) {
            if(isAlive()) {
                if(event.getProperty() == FontChooser.PROP_FONTCHANGE) {
                    if(isAlive()) {
                        FontData selectedFontData = fFontChooser.getFontData();
                        RGB rgb = fFontChooser.getFontRGB();
                        FontCompoundCommand cmd = new FontCompoundCommand(fFontObject, selectedFontData.toString(), rgb);
                        getCommandStack().execute(cmd.unwrap());
                    }
                }
                else if(event.getProperty() == FontChooser.PROP_FONTDEFAULT) {
                    if(isAlive()) {
                        getCommandStack().execute(new FontStyleCommand(fFontObject, null));
                    }
                }
            }
        }
    };

    /**
     * Listen to default font change in Prefs
     */
    private IPropertyChangeListener prefsListener = new IPropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent event) {
            if(event.getProperty().startsWith(IPreferenceConstants.DEFAULT_VIEW_FONT)) {
                refreshControls();
            }
        }
    };

    private IFontAttribute fFontObject;
    
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
    protected void setElement(Object element) {
        if(element instanceof EditPart && ((EditPart)element).getModel() instanceof IFontAttribute) {
            fFontObject = (IFontAttribute)((EditPart)element).getModel();
            if(fFontObject == null) {
                throw new RuntimeException("Font Object was null"); //$NON-NLS-1$
            }
        }
        else {
            throw new RuntimeException("Should have been an IFontAttribute"); //$NON-NLS-1$
        }
        
        refreshControls();
    }
    
    protected void refreshControls() {
        fFontChooser.setFontObject(fFontObject);
        boolean enabled = fFontObject instanceof ILockable ? !((ILockable)fFontObject).isLocked() : true;
        fFontChooser.setEnabled(enabled);
        fFontChooser.setIsDefaultFont(fFontObject.getFont() == null);
    }
    
    @Override
    protected Adapter getECoreAdapter() {
        return eAdapter;
    }

    @Override
    protected EObject getEObject() {
        return fFontObject;
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
