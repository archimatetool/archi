/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.canvas.templates.wizard;

import uk.ac.bolton.archimate.canvas.templates.dialog.CanvasTemplateManagerDialog;
import uk.ac.bolton.archimate.canvas.templates.model.CanvasTemplateManager;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.templates.dialog.TemplateManagerDialog;
import uk.ac.bolton.archimate.templates.model.TemplateManager;
import uk.ac.bolton.archimate.templates.wizard.NewModelFromTemplateWizardPage;


/**
 * New Canvas Model From Template Wizard Page
 * 
 * @author Phillip Beauvoir
 */
public class NewCanvasFromTemplateWizardPage extends NewModelFromTemplateWizardPage {
    
    private static String HELP_ID = "uk.ac.bolton.archimate.help.NewCanvasFromTemplateWizardPage"; //$NON-NLS-1$

    public NewCanvasFromTemplateWizardPage(TemplateManager templateManager) {
        super("NewCanvasFromTemplateWizardPage", templateManager); //$NON-NLS-1$
    }
    
    @Override
    protected void init() {
        setTitle(Messages.NewCanvasFromTemplateWizardPage_0);
        setDescription(Messages.NewCanvasFromTemplateWizardPage_1);
        setImageDescriptor(IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ECLIPSE_IMAGE_NEW_WIZARD));
    }

    @Override
    protected TemplateManagerDialog createTemplateManagerDialog() {
        // Use a new instance of a Template Manager as a clone in case user cancels
        return new CanvasTemplateManagerDialog(getShell(), new CanvasTemplateManager());
    }
    
    @Override
    protected String getHelpID() {
        return HELP_ID;
    }
 }
