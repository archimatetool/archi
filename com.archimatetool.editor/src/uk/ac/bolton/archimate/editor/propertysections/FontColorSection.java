/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.propertysections;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import uk.ac.bolton.archimate.editor.diagram.commands.FontColorCommand;
import uk.ac.bolton.archimate.editor.ui.ColorFactory;
import uk.ac.bolton.archimate.editor.ui.components.ColorChooser;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IFontAttribute;
import uk.ac.bolton.archimate.model.ILockable;


/**
 * Property Section for a Font Color Section
 * 
 * @author Phillip Beauvoir
 */
public class FontColorSection extends AbstractArchimatePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$
    
    /*
     * Adapter to listen to changes made elsewhere (including Undo/Redo commands)
     */
    private Adapter eAdapter = new AdapterImpl() {
        @Override
        public void notifyChanged(Notification msg) {
            Object feature = msg.getFeature();
            // Color event (From Undo/Redo and here)
            if(feature == IArchimatePackage.Literals.FONT_ATTRIBUTE__FONT_COLOR ||
                    feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
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
                    if(!newColor.equals(fFontObject.getFontColor())) {
                        getCommandStack().execute(new FontColorCommand(fFontObject, newColor));
                    }
                }
                else if(event.getProperty() == ColorChooser.PROP_COLORDEFAULT) {
                    getCommandStack().execute(new FontColorCommand(fFontObject, null));
                }
            }
        }
    };
    
    private IFontAttribute fFontObject;

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
    protected void setElement(Object element) {
        if(element instanceof EditPart) {
            fFontObject = (IFontAttribute)((EditPart)element).getModel();
            if(fFontObject == null) {
                throw new RuntimeException("Diagram Model Object was null"); //$NON-NLS-1$
            }
        }
        else {
            throw new RuntimeException("Should have been an EditPart"); //$NON-NLS-1$
        }
        
        refreshControls();
    }
    
    protected void refreshControls() {
        String colorValue = fFontObject.getFontColor();
        RGB rgb = ColorFactory.convertStringToRGB(colorValue);
        if(rgb != null) {
            fColorChooser.setColorValue(rgb);
        }
        else {
            // Default color
            fColorChooser.setColorValue(new RGB(0, 0, 0));
        }
        
        boolean enabled = fFontObject instanceof ILockable ? !((ILockable)fFontObject).isLocked() : true;
        fColorChooser.setEnabled(enabled);
        fColorChooser.setIsDefaultColor(colorValue == null);
    }
    
    @Override
    public void dispose() {
        super.dispose();
        
        if(fColorChooser != null) {
            fColorChooser.removeListener(colorListener);
        }
    }

    @Override
    protected Adapter getECoreAdapter() {
        return eAdapter;
    }

    @Override
    protected EObject getEObject() {
        return fFontObject;
    }
}
