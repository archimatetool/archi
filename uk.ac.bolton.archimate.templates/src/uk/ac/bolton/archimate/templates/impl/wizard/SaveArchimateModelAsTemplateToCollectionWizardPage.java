/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.templates.impl.wizard;

import uk.ac.bolton.archimate.templates.model.TemplateManager;
import uk.ac.bolton.archimate.templates.wizard.SaveModelAsTemplateToCollectionWizardPage;


/**
 * Save Model As Template Wizard Page 2
 * 
 * @author Phillip Beauvoir
 */
public class SaveArchimateModelAsTemplateToCollectionWizardPage extends SaveModelAsTemplateToCollectionWizardPage {

    private static String HELP_ID = "uk.ac.bolton.archimate.help.SaveArchimateModelAsTemplateToCollectionWizardPage"; //$NON-NLS-1$

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
