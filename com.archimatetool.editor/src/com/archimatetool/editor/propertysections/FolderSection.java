/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.model.FolderType;
import com.archimatetool.model.IFolder;



/**
 * Property Section for a Folder
 * 
 * @author Phillip Beauvoir
 */
public class FolderSection extends AbstractNameDocumentationSection {
    
    private static final String HELP_ID = "com.archimatetool.help.folderSection"; //$NON-NLS-1$
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return object instanceof IFolder;
        }

        @Override
        public Class<?> getAdaptableType() {
            return IFolder.class;
        }
    }

    
    @Override
    protected void createControls(Composite parent) {
        super.createControls(parent);
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    @Override
    protected void refreshNameField() {
        super.refreshNameField();
        fTextName.setEditable(isNameEditable());
    }
    
    private boolean isNameEditable() {
        for(EObject folder : getEObjects()) {
            if(((IFolder)folder).getType() != FolderType.USER) {
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    protected IObjectFilter getFilter() {
        return new Filter();
    }
}
