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
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.model.commands.FeatureCommand;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IFeatures;



/**
 * Property Section to set the Image source.
 * Currently can be set from a Specialization Profile or Custom Image
 * 
 * @author Phillip Beauvoir
 */
public class ImageSourceSection extends AbstractECorePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return object instanceof IDiagramModelArchimateObject &&
                    shouldExposeFeature((EObject)object, IDiagramModelArchimateObject.FEATURE_IMAGE_SOURCE);
        }

        @Override
        public Class<?> getAdaptableType() {
            return IDiagramModelArchimateObject.class;
        }
    }

    private Combo fImageSourceCombo;
    
    private String[] IMAGE_SOURCE_CHOICES = {
            Messages.ImageSourceSection_0,
            Messages.ImageSourceSection_1
    };
    
    @Override
    protected void createControls(Composite parent) {
        createLabel(parent, Messages.ImageSourceSection_3, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        
        fImageSourceCombo = new Combo(parent, SWT.READ_ONLY);
        getWidgetFactory().adapt(fImageSourceCombo, true, true);
        fImageSourceCombo.setItems(IMAGE_SOURCE_CHOICES);
        
        fImageSourceCombo.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                CompoundCommand result = new CompoundCommand();

                for(EObject selected : getEObjects()) {
                    if(isAlive(selected)) {
                        Command cmd = new FeatureCommand(Messages.ImageSourceSection_2, (IFeatures)selected,
                                IDiagramModelArchimateObject.FEATURE_IMAGE_SOURCE, mapFromComboToValue(), IDiagramModelArchimateObject.FEATURE_IMAGE_SOURCE_DEFAULT);
                        if(cmd.canExecute()) {
                            result.add(cmd);
                        }
                    }
                }

                executeCommand(result.unwrap());
            }
        });
        
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    @Override
    protected IObjectFilter getFilter() {
        return new Filter();
    }

    @Override
    protected void notifyChanged(Notification msg) {
        if(msg.getNotifier() == getFirstSelectedObject()) {
            if(isFeatureNotification(msg, IDiagramModelArchimateObject.FEATURE_IMAGE_SOURCE)) {
                refreshCombo();
            }
        }
    }
    
    @Override
    protected void update() {
        refreshCombo();
    }
    
    protected void refreshCombo() {
        if(fIsExecutingCommand) {
            return; 
        }
        
        IDiagramModelArchimateObject lastSelectedObject = (IDiagramModelArchimateObject)getFirstSelectedObject();
        
        fImageSourceCombo.select(mapFromValueToCombo(lastSelectedObject.getImageSource()));
        
        fImageSourceCombo.setEnabled(!isLocked(lastSelectedObject));
    }
    
    /**
     * map from combo index value to model value
     */
    private int mapFromComboToValue() {
        switch(fImageSourceCombo.getSelectionIndex()) {
            case 0:
            default:
                return IDiagramModelArchimateObject.IMAGE_SOURCE_PROFILE;

            case 1:
                return IDiagramModelArchimateObject.IMAGE_SOURCE_CUSTOM;
        }
    }
    
    /**
     * map from model value to combo index value
     */
    private int mapFromValueToCombo(int value) {
        switch(value) {
            case IDiagramModelArchimateObject.IMAGE_SOURCE_PROFILE:
            default:
                return 0;

            case IDiagramModelArchimateObject.IMAGE_SOURCE_CUSTOM:
                return 1;
        }
    }

}
