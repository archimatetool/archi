/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.modelimporter;

import java.io.File;

import org.eclipse.jface.wizard.Wizard;


/**
 * Import Model Wizard
 * 
 * @author Phillip Beauvoir
 */
public class ImportModelWizard extends Wizard {

    private ImportModelPage page;
    
    private File file;
    private boolean update;
    private boolean updateAll;
    private boolean showStatusDialog;

    public ImportModelWizard() {
        setWindowTitle(Messages.ImportModelWizard_0);
    }

    @Override
    public void addPages() {
        page = new ImportModelPage();
        addPage(page);
    }

    @Override
    public boolean performFinish() {
        file = new File(page.getFileName());
        update = page.shouldUpdate();
        updateAll = page.shouldUpdateAll();
        showStatusDialog = page.shouldShowStatusDialog();
        
        page.storePreferences();
        
        return true;
    }

    File getFile() {
        return file;
    }
    
    boolean shouldUpdate() {
        return update;
    }
    
    boolean shouldUpdateAll() {
        return updateAll;
    }
    
    boolean shouldShowStatusDialog() {
        return showStatusDialog;
    }
}
