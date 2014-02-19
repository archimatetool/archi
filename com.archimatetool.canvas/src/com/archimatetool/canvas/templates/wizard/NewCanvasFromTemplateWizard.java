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
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;

import com.archimatetool.canvas.model.ICanvasModel;
import com.archimatetool.canvas.templates.model.CanvasTemplateManager;
import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.editor.model.compatibility.CompatibilityHandlerException;
import com.archimatetool.editor.model.compatibility.IncompatibleModelException;
import com.archimatetool.editor.model.compatibility.ModelCompatibility;
import com.archimatetool.editor.utils.ZipUtils;
import com.archimatetool.editor.views.tree.commands.NewDiagramCommand;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.util.ArchimateResourceFactory;
import com.archimatetool.templates.model.ITemplate;
import com.archimatetool.templates.model.TemplateManager;
import com.archimatetool.templates.wizard.TemplateUtils;



/**
 * New Canvas Model From Template Wizard
 * 
 * @author Phillip Beauvoir
 */
public class NewCanvasFromTemplateWizard extends Wizard {
    
    private IFolder fFolder;
    
    private String fErrorMessage;
    
    private NewCanvasFromTemplateWizardPage fMainPage;
    
    private TemplateManager fTemplateManager;
    
    public NewCanvasFromTemplateWizard(IFolder folder) {
        setWindowTitle(Messages.NewCanvasFromTemplateWizard_0);
        fFolder = folder;
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
                        fErrorMessage = null;
                        File tmp = File.createTempFile("~architemplate", null); //$NON-NLS-1$
                        tmp.deleteOnExit();
                        File file = ZipUtils.extractZipEntry(zipFile, TemplateManager.ZIP_ENTRY_MODEL, tmp);
                        if(file != null && file.exists()) {
                            createNewCanvasFromTemplate(file);
                        }
                        else {
                            fErrorMessage = Messages.NewCanvasFromTemplateWizard_1;
                        }
                        tmp.delete();
                    }
                    catch(Exception ex) {
                        ex.printStackTrace();
                        fErrorMessage = Messages.NewCanvasFromTemplateWizard_2 + " " + ex.getMessage(); //$NON-NLS-1$
                    }
                }
            });
        }
        
        if(fErrorMessage != null) {
            MessageDialog.openError(getShell(), Messages.NewCanvasFromTemplateWizard_3, fErrorMessage);
            getContainer().getShell().setVisible(true);
        }
        
        return fErrorMessage == null;
    }

    private void createNewCanvasFromTemplate(File file) throws IncompatibleModelException, IOException {
        // Ascertain if this is a zip file
        boolean isArchiveFormat = IArchiveManager.FACTORY.isArchiveFile(file);
        
        Resource resource = ArchimateResourceFactory.createNewResource(isArchiveFormat ?
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
                fErrorMessage = NLS.bind(Messages.NewCanvasFromTemplateWizard_4, file)
                                + "\n" + ex1.getMessage(); //$NON-NLS-1$
                throw ex1;
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

        // Create New UUIDs for elements...
        TemplateUtils.generateNewUUIDs(canvasModel);
        
        // Load the images from the template model's file now
        if(isArchiveFormat) {
            IArchiveManager archiveManager = (IArchiveManager)fFolder.getAdapter(IArchiveManager.class);
            archiveManager.loadImagesFromModelFile(file); 
        }
        
        Command cmd = new NewDiagramCommand(fFolder, canvasModel, Messages.NewCanvasFromTemplateWizard_5);
        CommandStack commandStack = (CommandStack)fFolder.getAdapter(CommandStack.class);
        commandStack.execute(cmd);
    }
    
    @Override
    public void dispose() {
        super.dispose();
        fTemplateManager.dispose();
    }
}
