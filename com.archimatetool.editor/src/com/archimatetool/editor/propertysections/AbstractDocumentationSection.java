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
 * Property Section for a Documentation element
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractDocumentationSection extends AbstractECorePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$

    private PropertySectionTextControl fTextDocumentation;
    
    @Override
    protected void createControls(Composite parent) {
        fTextDocumentation = createDocumentationControl(parent, Messages.AbstractNameDocumentationSection_1);

        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    @Override
    protected void notifyChanged(Notification msg) {
        if(msg.getNotifier() == getFirstSelectedObject()) {
            Object feature = msg.getFeature();
            
            if(feature == IArchimatePackage.Literals.DOCUMENTABLE__DOCUMENTATION ||
                    feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
                update();
            }
        }
    }

    @Override
    protected void update() {
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
