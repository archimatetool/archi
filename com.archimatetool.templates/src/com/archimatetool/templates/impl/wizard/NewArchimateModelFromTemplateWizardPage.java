/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.templates.impl.wizard;

import com.archimatetool.templates.model.TemplateManager;
import com.archimatetool.templates.wizard.NewModelFromTemplateWizardPage;


/**
 * New Archimate Model From Template Wizard Page
 * 
 * @author Phillip Beauvoir
 */
public class NewArchimateModelFromTemplateWizardPage extends NewModelFromTemplateWizardPage {
    
    private static String HELP_ID = "com.archimatetool.help.NewArchimateModelFromTemplateWizardPage"; //$NON-NLS-1$

    public NewArchimateModelFromTemplateWizardPage(TemplateManager templateManager) {
        super("NewArchimateModelFromTemplateWizardPage", templateManager); //$NON-NLS-1$
    }

    @Override
    protected void init() {
        setTitle(Messages.NewArchimateModelFromTemplateWizardPage_1);
        setDescription(Messages.NewArchimateModelFromTemplateWizardPage_2);
    }
    
    @Override
    protected String getHelpID() {
        return HELP_ID;
    }

}
