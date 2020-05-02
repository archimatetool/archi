/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.templates.impl.wizard;

import java.io.File;
import java.io.IOException;

import org.eclipse.jface.wizard.Wizard;

import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.editor.ui.services.UIRequestManager;
import com.archimatetool.editor.utils.ZipUtils;
import com.archimatetool.editor.views.tree.TreeEditElementRequest;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.ModelVersion;
import com.archimatetool.model.util.UUIDFactory;
import com.archimatetool.templates.impl.model.ArchimateTemplateManager;
import com.archimatetool.templates.model.ITemplate;
import com.archimatetool.templates.model.TemplateManager;



/**
 * New Archimate Model From Template Wizard
 * 
 * @author Phillip Beauvoir
 */
public class NewArchimateModelFromTemplateWizard extends Wizard {
    
    private NewArchimateModelFromTemplateWizardPage fMainPage;
    
    private TemplateManager fTemplateManager;
    
    private ITemplate fSelectedTemplate;
    
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
        fSelectedTemplate = fMainPage.getSelectedTemplate();
        return fSelectedTemplate != null;
    }
    
    /**
     * Create and open a new model based on the selected template
     * @return The model or null
     * @throws IOException
     */
    public IArchimateModel createNewModel() throws IOException {
        File file = getTempModelFile();
        if(file == null || !file.exists()) {
            return null;
        }
        
        IArchimateModel model = IEditorModelManager.INSTANCE.openModel(file);
        
        if(model != null) {
            // New name
            model.setName(Messages.NewArchimateModelFromTemplateWizard_1 + " " + model.getName()); //$NON-NLS-1$
            
            // Set latest model version (need to do this in case we immediately save as Template)
            model.setVersion(ModelVersion.VERSION);
            
            // Set file to null
            model.setFile(null);
            
            // New IDs
            UUIDFactory.generateNewIDs(model);
            
            // Edit in-place in Tree
            UIRequestManager.INSTANCE.fireRequestAsync(new TreeEditElementRequest(this, model));
        }
        
        file.delete();
        
        return model;
    }
    
    /**
     * @return The extracted model from the canvas template as a temporary file or null
     * @throws IOException
     */
    private File getTempModelFile() throws IOException {
        if(fSelectedTemplate == null) {
            return null;
        }
        
        File zipFile = fSelectedTemplate.getFile();
        
        File tmpFile = File.createTempFile("~architemplate", null); //$NON-NLS-1$
        tmpFile.deleteOnExit();
        
        return ZipUtils.extractZipEntry(zipFile, TemplateManager.ZIP_ENTRY_MODEL, tmpFile);
    }

    @Override
    public void dispose() {
        super.dispose();
        fTemplateManager.dispose();
    }
}
