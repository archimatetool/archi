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
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.diagram.commands.BorderColorCommand;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.components.ColorChooser;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IBorderObject;



/**
 * Property Section for a Border Color
 * 
 * @author Phillip Beauvoir
 */
public class BorderColorSection extends AbstractECorePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return object instanceof IBorderObject;
        }

        @Override
        public Class<?> getAdaptableType() {
            return IBorderObject.class;
        }
    }

    /**
     * Color listener
     */
    private IPropertyChangeListener colorListener = new IPropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent event) {
            CompoundCommand result = new CompoundCommand();

            for(EObject bo : getEObjects()) {
                if(isAlive(bo)) {
                    RGB rgb = fColorChooser.getColorValue();
                    String newColor = ColorFactory.convertRGBToString(rgb);
                    if(!newColor.equals(((IBorderObject)bo).getBorderColor())) {
                        Command cmd = new BorderColorCommand((IBorderObject)bo, newColor);
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
    
    private IAction fNoBorderAction;
    
    @Override
    protected void createControls(Composite parent) {
        createColorControl(parent);
        
        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    private void createColorControl(Composite parent) {
        createLabel(parent, Messages.BorderColorSection_0, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        
        fColorChooser = new ColorChooser(parent, getWidgetFactory());
        fColorChooser.setDoShowDefaultMenuItem(false);
        fColorChooser.setDoShowPreferencesMenuItem(false);
        
        // No border action
        fNoBorderAction = new Action(Messages.BorderColorSection_1) {
            @Override
            public void run() {
                CompoundCommand result = new CompoundCommand();

                for(EObject bo : getEObjects()) {
                    if(isAlive(bo)) {
                        Command cmd = new BorderColorCommand((IBorderObject)bo, null);
                        if(cmd.canExecute()) {
                            result.add(cmd);
                        }
                    }
                }

                executeCommand(result.unwrap());
            }
        };
        
        fColorChooser.addMenuAction(fNoBorderAction);
        fColorChooser.addListener(colorListener);
    }
    
    @Override
    protected void notifyChanged(Notification msg) {
        if(msg.getNotifier() == getFirstSelectedObject()) {
            Object feature = msg.getFeature();
            
            if(feature == IArchimatePackage.Literals.BORDER_OBJECT__BORDER_COLOR || feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
                update();
            }
        }
    }
    
    @Override
    protected void update() {
        IBorderObject bo = (IBorderObject)getFirstSelectedObject();
        
        String colorValue = bo.getBorderColor();
        RGB rgb = ColorFactory.convertStringToRGB(colorValue);
        if(rgb == null) {
            rgb = new RGB(0, 0, 0);
        }
        
        fColorChooser.setColorValue(rgb);
        
        fColorChooser.setEnabled(!isLocked(bo));
        
        fNoBorderAction.setEnabled(colorValue != null);
        fColorChooser.setDoShowColorImage(colorValue != null);
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
