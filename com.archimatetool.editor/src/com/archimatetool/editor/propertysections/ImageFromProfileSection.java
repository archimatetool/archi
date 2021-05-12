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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.model.commands.FeatureCommand;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IFeatures;



/**
 * Property Section for to use the Image from a Specialization Profile
 * 
 * @author Phillip Beauvoir
 */
public class ImageFromProfileSection extends AbstractECorePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return object instanceof IDiagramModelArchimateObject;
        }

        @Override
        public Class<?> getAdaptableType() {
            return IDiagramModelArchimateObject.class;
        }
    }

    private Button fUseProfileImageButton;
    
    @Override
    protected void createControls(Composite parent) {
        createLabel(parent, "Specialization:", ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER); //$NON-NLS-1$
        
        fUseProfileImageButton = new Button(parent, SWT.CHECK);
        fUseProfileImageButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                CompoundCommand result = new CompoundCommand();

                for(EObject selected : getEObjects()) {
                    if(isAlive(selected)) {
                        Command cmd = new FeatureCommand("Specialization Image", (IFeatures)selected,
                                IDiagramModelArchimateObject.FEATURE_USE_PROFILE_IMAGE, fUseProfileImageButton.getSelection(),
                                IDiagramModelArchimateObject.FEATURE_USE_PROFILE_IMAGE_DEFAULT);
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
            if(isFeatureNotification(msg, IDiagramModelArchimateObject.FEATURE_USE_PROFILE_IMAGE)) {
                refreshButton();
            }
        }
    }
    
    @Override
    protected void update() {
        refreshButton();
    }
    
    protected void refreshButton() {
        if(fIsExecutingCommand) {
            return; 
        }
        
        IDiagramModelArchimateObject lastSelectedObject = (IDiagramModelArchimateObject)getFirstSelectedObject();
        
        fUseProfileImageButton.setSelection(lastSelectedObject.useProfileImage());
        
        fUseProfileImageButton.setEnabled(!isLocked(lastSelectedObject));
    }
}
