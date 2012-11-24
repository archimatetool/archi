/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.templates.impl.wizard;

import java.io.File;
import java.io.IOException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;

import uk.ac.bolton.archimate.editor.model.IEditorModelManager;
import uk.ac.bolton.archimate.editor.ui.services.UIRequestManager;
import uk.ac.bolton.archimate.editor.utils.ZipUtils;
import uk.ac.bolton.archimate.editor.views.tree.TreeEditElementRequest;
import uk.ac.bolton.archimate.model.IArchimateModel;
import uk.ac.bolton.archimate.model.ModelVersion;
import uk.ac.bolton.archimate.templates.impl.model.ArchimateTemplateManager;
import uk.ac.bolton.archimate.templates.model.ITemplate;
import uk.ac.bolton.archimate.templates.model.TemplateManager;


/**
 * New Archimate Model From Template Wizard
 * 
 * @author Phillip Beauvoir
 */
public class NewArchimateModelFromTemplateWizard extends Wizard {
    
    private NewArchimateModelFromTemplateWizardPage fMainPage;
    
    private String fErrorMessage;

    private TemplateManager fTemplateManager;
    
    public NewArchimateModelFromTemplateWizard() {
        setWindowTitle(Messages.NewArchimateModelFromTemplateWizard_0);
        fTemplateManager = new ArchimateTemplateManager();
    }
    
    @Override
    public void addPages() {
        fMainPage = new NewArchimateModelFromTemplateWizardPage(fTemplateManager);
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
                        File tmp = File.createTempFile("~architemplate", null); //$NON-NLS-1$
                        tmp.deleteOnExit();
                        File file = ZipUtils.extractZipEntry(zipFile, TemplateManager.ZIP_ENTRY_MODEL, tmp);
                        if(file != null && file.exists()) {
                            IArchimateModel model = IEditorModelManager.INSTANCE.openModel(file);
                            if(model != null) {
                                // New name
                                model.setName(Messages.NewArchimateModelFromTemplateWizard_1 + " " + model.getName()); //$NON-NLS-1$
                                
                                // Set latest model version (need to do this in case we immediately save as Template)
                                model.setVersion(ModelVersion.VERSION);
                                
                                // Set file to null
                                model.setFile(null);
                                
                                // Edit in-place in Tree
                                UIRequestManager.INSTANCE.fireRequest(new TreeEditElementRequest(this, model));
                            }
                            else {
                                fErrorMessage = Messages.NewArchimateModelFromTemplateWizard_2;
                            }
                        }
                        else {
                            fErrorMessage = Messages.NewArchimateModelFromTemplateWizard_2;
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
            MessageDialog.openError(getShell(), Messages.NewArchimateModelFromTemplateWizard_3, fErrorMessage);
            getContainer().getShell().setVisible(true);
        }
        
        return fErrorMessage == null;
    }

    @Override
    public void dispose() {
        super.dispose();
        fTemplateManager.dispose();
    }
}
