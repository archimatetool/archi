/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.canvas.templates.wizard;

import uk.ac.bolton.archimate.templates.model.TemplateManager;
import uk.ac.bolton.archimate.templates.wizard.SaveModelAsTemplateToCollectionWizardPage;


/**
 * Save Canvas As Template Wizard Page 2
 * 
 * @author Phillip Beauvoir
 */
public class SaveCanvasAsTemplateToCollectionWizardPage extends SaveModelAsTemplateToCollectionWizardPage {

    private static String HELPID = "uk.ac.bolton.archimate.help.SaveCanvasAsTemplateWizardPage2"; //$NON-NLS-1$

    public SaveCanvasAsTemplateToCollectionWizardPage(TemplateManager templateManager) {
        super("SaveCanvasAsTemplateWizardPage2", templateManager);
    }
    
    @Override
    protected void init() {
        setTitle("Save Canvas As Template");
        setDescription("Choose whether to include the template in your collection.");
    }

    @Override
    protected String getHelpID() {
        return HELPID;
    }
}
