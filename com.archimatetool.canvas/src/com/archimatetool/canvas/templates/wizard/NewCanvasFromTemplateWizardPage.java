/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas.templates.wizard;

import com.archimatetool.canvas.templates.dialog.CanvasTemplateManagerDialog;
import com.archimatetool.canvas.templates.model.CanvasTemplateManager;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.templates.dialog.TemplateManagerDialog;
import com.archimatetool.templates.model.TemplateManager;
import com.archimatetool.templates.wizard.NewModelFromTemplateWizardPage;



/**
 * New Canvas Model From Template Wizard Page
 * 
 * @author Phillip Beauvoir
 */
public class NewCanvasFromTemplateWizardPage extends NewModelFromTemplateWizardPage {
    
    private static String HELP_ID = "com.archimatetool.help.NewCanvasFromTemplateWizardPage"; //$NON-NLS-1$

    public NewCanvasFromTemplateWizardPage(TemplateManager templateManager) {
        super("NewCanvasFromTemplateWizardPage", templateManager); //$NON-NLS-1$
    }
    
    @Override
    protected void init() {
        setTitle(Messages.NewCanvasFromTemplateWizardPage_0);
        setDescription(Messages.NewCanvasFromTemplateWizardPage_1);
        setImageDescriptor(IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ECLIPSE_IMAGE_NEW_WIZARD));
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
