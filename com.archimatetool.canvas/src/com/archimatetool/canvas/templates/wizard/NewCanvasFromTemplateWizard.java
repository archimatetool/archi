/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas.templates.wizard;

import java.io.File;
import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.osgi.util.NLS;

import com.archimatetool.canvas.model.ICanvasModel;
import com.archimatetool.canvas.templates.model.CanvasTemplateManager;
import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.editor.model.compatibility.CompatibilityHandlerException;
import com.archimatetool.editor.model.compatibility.IncompatibleModelException;
import com.archimatetool.editor.model.compatibility.ModelCompatibility;
import com.archimatetool.editor.utils.ZipUtils;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IFeature;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.util.ArchimateResourceFactory;
import com.archimatetool.model.util.UUIDFactory;
import com.archimatetool.templates.model.ITemplate;
import com.archimatetool.templates.model.TemplateManager;



/**
 * New Canvas Model From Template Wizard
 * 
 * @author Phillip Beauvoir
 */
public class NewCanvasFromTemplateWizard extends Wizard {
    
    private NewCanvasFromTemplateWizardPage fMainPage;
    
    private TemplateManager fTemplateManager;
    
    private ITemplate fSelectedTemplate;
    
    public NewCanvasFromTemplateWizard() {
        setWindowTitle(Messages.NewCanvasFromTemplateWizard_0);
        fTemplateManager = new CanvasTemplateManager();
    }
    
    @Override
    public void addPages() {
        fMainPage = new NewCanvasFromTemplateWizardPage(fTemplateManager);
        addPage(fMainPage);
    }
    
    @Override
    public boolean performFinish() {
        // Get template
        fSelectedTemplate = fMainPage.getSelectedTemplate();
        return fSelectedTemplate != null;
    }

    /**
     * Create a new canvas model from the template
     * @param model The model in which the new canvas view should be created
     * @return the new canvas model or null
     * @throws IOException
     */
    public ICanvasModel getNewCanvasModel(IArchimateModel model) throws IOException {
        File file = getTempModelFile();
        if(file == null || !file.exists()) {
            return null;
        }
        
        // Is this is a legacy archive file?
        boolean isLegacyArchiveFormat = IArchiveManager.FACTORY.isArchiveFile(file);
        
        // Create the Resource
        Resource resource = ArchimateResourceFactory.createNewResource(isLegacyArchiveFormat ?
                                                        IArchiveManager.FACTORY.createArchiveModelURI(file) :
                                                        URI.createFileURI(file.getAbsolutePath()));

        // Check model compatibility
        ModelCompatibility modelCompatibility = new ModelCompatibility(resource);
        
        // Load the template file
        // Wrap in try/catch to load as much as possible
        try {
            resource.load(null);
        }
        catch(IOException ex) {
            // Error occured loading model. Was it a disaster?
            try {
                modelCompatibility.checkErrors();
            }
            // Incompatible
            catch(IncompatibleModelException ex1) {
                throw new IOException(NLS.bind(Messages.NewCanvasFromTemplateWizard_1, file)
                        + "\n" + ex1.getMessage()); //$NON-NLS-1$
            }
        }
        
        // And then fix any backward compatibility issues
        try {
            modelCompatibility.fixCompatibility();
        }
        catch(CompatibilityHandlerException ex) {
        }
        
        // Pull out the Canvas model
        IArchimateModel templateModel = (IArchimateModel)resource.getContents().get(0);
        IFolder folderViews = templateModel.getFolder(FolderType.DIAGRAMS);
        ICanvasModel canvasModel = (ICanvasModel)folderViews.getElements().get(0);

        // Create New IDs for elements...
        UUIDFactory.generateNewIDs(canvasModel);
        
        // Convert from legacy format
        if(isLegacyArchiveFormat) {
            IArchiveManager archiveManager = IArchiveManager.FACTORY.createArchiveManager(templateModel);
            archiveManager.convertImagesFromLegacyArchive(file);
        }

        // Add the images from the template model's file to the target model
        for(IFeature feature : templateModel.getFeatures()) {
            if(feature.getName().startsWith(IArchiveManager.imageFeaturePrefix)) {
                model.getFeatures().putString(feature.getName(), feature.getValue());
            }
        }
        
        file.delete();
        
        return canvasModel;
    }
    
    /**
     * @return The extracted model from the canvas template as a temporary file or null
     * @throws IOException
     */
    public File getTempModelFile() throws IOException {
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
