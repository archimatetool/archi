/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.model.IArchimatePackage;



/**
 * Property Section for Name and Documentation fields
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractNameDocumentationSection extends AbstractECorePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$
    
    protected PropertySectionTextControl fTextName;
    protected PropertySectionTextControl fTextDocumentation;
    
    @Override
    protected void createControls(Composite parent) {
        fTextName = createNameControl(parent, Messages.AbstractNameDocumentationSection_0);
        fTextDocumentation = createDocumentationControl(parent, Messages.AbstractNameDocumentationSection_1);

        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }

    @Override
    protected void notifyChanged(Notification msg) {
        if(msg.getNotifier() == getFirstSelectedObject()) {
            Object feature = msg.getFeature();
            
            // Name event
            if(feature == IArchimatePackage.Literals.NAMEABLE__NAME) {
                refreshNameField();
                updatePropertiesLabel();
            }
            // Documentation event
            else if(feature == IArchimatePackage.Literals.DOCUMENTABLE__DOCUMENTATION) {
                refreshDocumentationField();
            }
            // Locked
            else if(feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
                update();
            }
        }
    }
    
    @Override
    protected void update() {
        refreshNameField();
        refreshDocumentationField();
    }
    
    protected void refreshNameField() {
        if(fIsExecutingCommand) {
            return; 
        }
        
        fTextName.refresh(getFirstSelectedObject());
        fTextName.setEditable(!isLocked(getFirstSelectedObject()));
    }
    
    protected void refreshDocumentationField() {
        if(fIsExecutingCommand) {
            return; 
        }
        
        fTextDocumentation.refresh(getFirstSelectedObject());
        fTextDocumentation.setEditable(!isLocked(getFirstSelectedObject()));
    }

    @Override
    public boolean shouldUseExtraSpace() {
        return true;
    }
}
