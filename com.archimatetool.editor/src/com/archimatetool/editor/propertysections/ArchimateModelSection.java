/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import java.io.File;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.model.commands.EObjectFeatureCommand;
import com.archimatetool.editor.ui.components.StyledTextControl;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;



/**
 * Property Section for an Archimate Model
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateModelSection extends AbstractECorePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.archimateModelSection"; //$NON-NLS-1$

    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return object instanceof IArchimateModel;
        }

        @Override
        public Class<?> getAdaptableType() {
            return IArchimateModel.class;
        }
    }

    private PropertySectionTextControl fTextName;
    private Text fTextFile;
    private PropertySectionTextControl fTextPurpose;
    
    @Override
    protected void createControls(Composite parent) {
        fTextName = createNameControl(parent, Messages.ArchimateModelSection_0);
        createFileControl(parent);
        createPurposeControl(parent);
        
        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    private void createFileControl(Composite parent) {
        // Label
        createLabel(parent, Messages.ArchimateModelSection_1, STANDARD_LABEL_WIDTH, SWT.CENTER);

        // Text
        fTextFile = createSingleTextControl(parent, SWT.READ_ONLY);
    }
    
    private void createPurposeControl(Composite parent) {
        // Label
        createLabel(parent, Messages.ArchimateModelSection_2, STANDARD_LABEL_WIDTH, SWT.NONE);

        // Text
        StyledTextControl styledTextControl = createStyledTextControl(parent, SWT.NONE);
        styledTextControl.setMessage(Messages.ArchimateModelSection_4);
        
        fTextPurpose = new PropertySectionTextControl(styledTextControl.getControl(), IArchimatePackage.Literals.ARCHIMATE_MODEL__PURPOSE) {
            @Override
            protected void textChanged(String oldText, String newText) {
                EObject model = getFirstSelectedObject();

                if(isAlive(model)) {
                    Command cmd = new EObjectFeatureCommand(Messages.ArchimateModelSection_3, getFirstSelectedObject(),
                            IArchimatePackage.Literals.ARCHIMATE_MODEL__PURPOSE, newText);
                    if(cmd.canExecute()) {
                        executeCommand(cmd);
                    }
                }
            }
        };
    }

    @Override
    protected void notifyChanged(Notification msg) {
        if(msg.getNotifier() == getFirstSelectedObject()) {
            Object feature = msg.getFeature();
            
            // Model Name
            if(feature == IArchimatePackage.Literals.NAMEABLE__NAME) {
                refreshNameField();
                updatePropertiesLabel(); // Update Main label
            }
            // Model File
            else if(feature == IArchimatePackage.Literals.ARCHIMATE_MODEL__FILE) {
                refreshFileField();
            }
            // Model Purpose
            else if(feature == IArchimatePackage.Literals.ARCHIMATE_MODEL__PURPOSE) {
                refreshPurposeField();
            }
        }
    }
    
    @Override
    protected void update() {
        refreshNameField();
        refreshFileField();
        refreshPurposeField();
    }
    
    protected void refreshNameField() {
        if(fIsExecutingCommand) {
            return; 
        }
        fTextName.refresh(getFirstSelectedObject());
    }
    
    protected void refreshFileField() {
        File file = ((IArchimateModel)getFirstSelectedObject()).getFile();
        if(file != null) {
            fTextFile.setText(file.getAbsolutePath());
        }
        else{
            fTextFile.setText(Messages.ArchimateModelSection_5);
        }
    }
    
    protected void refreshPurposeField() {
        if(fIsExecutingCommand) {
            return; 
        }
        fTextPurpose.refresh(getFirstSelectedObject());
    }

    @Override
    protected IObjectFilter getFilter() {
        return new Filter();
    }
    
    @Override
    public boolean shouldUseExtraSpace() {
        return true;
    }
}
