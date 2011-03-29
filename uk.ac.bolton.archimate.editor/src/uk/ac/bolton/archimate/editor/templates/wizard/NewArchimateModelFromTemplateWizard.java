/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.templates.wizard;

import java.io.File;
import java.io.IOException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;

import uk.ac.bolton.archimate.editor.model.IEditorModelManager;
import uk.ac.bolton.archimate.editor.templates.ITemplate;
import uk.ac.bolton.archimate.editor.templates.TemplateManager;
import uk.ac.bolton.archimate.editor.utils.ZipUtils;
import uk.ac.bolton.archimate.model.IArchimateModel;


/**
 * New Archimate Model From Template Wizard
 * 
 * @author Phillip Beauvoir
 */
public class NewArchimateModelFromTemplateWizard extends Wizard {
    
    private NewArchimateModelFromTemplateWizardPage fMainPage;
    
    private String fErrorMessage;
    
    public NewArchimateModelFromTemplateWizard() {
        setWindowTitle("New ArchiMate Model");
    }
    
    @Override
    public void addPages() {
        fMainPage = new NewArchimateModelFromTemplateWizardPage();
        addPage(fMainPage);
    }

    @Override
    public boolean performFinish() {
        // Get template
        ITemplate template = fMainPage.getSelectedTemplate();
        if(template == null) {
            return false;
        }

        getContainer().getShell().setVisible(false);
        
        fErrorMessage = null;
        final File zipFile = template.getFile();
        if(zipFile != null && zipFile.exists()) {
            BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {
                @Override
                public void run() {
                    try {
                        File tmp = File.createTempFile(TemplateManager.ARCHIMATE_TEMPLATE_FILE_TMP_PREFIX, null);
                        tmp.deleteOnExit();
                        File file = ZipUtils.extractZipEntry(zipFile, TemplateManager.ZIP_ENTRY_MODEL, tmp);
                        if(file != null && file.exists()) {
                            IArchimateModel model = IEditorModelManager.INSTANCE.openModel(file);
                            if(model != null) {
                                model.setName("(new) " + model.getName());
                            }
                            else {
                                fErrorMessage = "Unknown format";
                            }
                        }
                        else {
                            fErrorMessage = "Unknown format";
                        }
                        tmp.delete();
                    }
                    catch(IOException ex) {
                        ex.printStackTrace();
                        fErrorMessage = ex.getMessage();
                    }
                }
            });
        }
        
        if(fErrorMessage != null) {
            MessageDialog.openError(getShell(), "Error opening file", fErrorMessage);
            getContainer().getShell().setVisible(true);
        }
        
        return fErrorMessage == null;
    }

}
