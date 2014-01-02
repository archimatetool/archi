/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.templates.impl.wizard;

import com.archimatetool.templates.model.TemplateManager;
import com.archimatetool.templates.wizard.SaveModelAsTemplateToCollectionWizardPage;


/**
 * Save Model As Template Wizard Page 2
 * 
 * @author Phillip Beauvoir
 */
public class SaveArchimateModelAsTemplateToCollectionWizardPage extends SaveModelAsTemplateToCollectionWizardPage {

    private static String HELP_ID = "com.archimatetool.help.SaveArchimateModelAsTemplateToCollectionWizardPage"; //$NON-NLS-1$

    public SaveArchimateModelAsTemplateToCollectionWizardPage(TemplateManager templateManager) {
        super("SaveModelAsTemplateToCollectionWizardPage", templateManager); //$NON-NLS-1$
    }
    
    @Override
    protected void init() {
        setTitle(Messages.SaveArchimateModelAsTemplateToCollectionWizardPage_1);
        setDescription(Messages.SaveArchimateModelAsTemplateToCollectionWizardPage_2);
    }

    @Override
    protected String getHelpID() {
        return HELP_ID;
    }
}
