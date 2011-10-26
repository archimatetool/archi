/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.templates.wizard;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipOutputStream;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.jdom.Document;
import org.jdom.Element;

import uk.ac.bolton.archimate.editor.diagram.util.DiagramUtils;
import uk.ac.bolton.archimate.editor.utils.ZipUtils;
import uk.ac.bolton.archimate.model.IArchimateModel;
import uk.ac.bolton.archimate.model.IDiagramModel;
import uk.ac.bolton.archimate.model.util.ArchimateResourceFactory;
import uk.ac.bolton.archimate.templates.model.ITemplate;
import uk.ac.bolton.archimate.templates.model.ITemplateGroup;
import uk.ac.bolton.archimate.templates.model.ITemplateXMLTags;
import uk.ac.bolton.archimate.templates.model.ModelTemplate;
import uk.ac.bolton.archimate.templates.model.TemplateManager;
import uk.ac.bolton.jdom.JDOMUtils;


/**
 * Save Model As Template Wizard
 * 
 * @author Phillip Beauvoir
 */
public class SaveModelAsTemplateWizard extends Wizard {
    
    private IArchimateModel fModel;
    
    private SaveModelAsTemplateWizardPage1 fPage1;
    private SaveModelAsTemplateWizardPage2 fPage2;
    
    private File fZipFile;
    private String fTemplateName;
    private String fTemplateDescription;
    private boolean fIncludeThumbnails;
    private IDiagramModel fSelectedDiagramModel;
    private ITemplateGroup fSelectedTemplateGroup;
    private boolean fDoStoreInCollection;
    
    private TemplateManager fTemplateManager;
    
    public SaveModelAsTemplateWizard(IArchimateModel model) {
        setWindowTitle("Save Model As Template");
        fModel = model;
        fTemplateManager = new TemplateManager();
    }
    
    @Override
    public void addPages() {
        fPage1 = new SaveModelAsTemplateWizardPage1(fModel);
        addPage(fPage1);
        fPage2 = new SaveModelAsTemplateWizardPage2(fTemplateManager);
        addPage(fPage2);
    }

    @Override
    public boolean performFinish() {
        getContainer().getShell().setVisible(false);
        fZipFile = askFileSave();
        if(fZipFile == null) {
            getContainer().getShell().setVisible(true);
            return false;
        }
        
        // This before the thread starts
        fTemplateName = fPage1.getTemplateName();
        fTemplateDescription = fPage1.getTemplateDescription();
        fIncludeThumbnails = fPage1.includeThumbnails();
        fSelectedDiagramModel = fPage1.getSelectedDiagramModel();
        fDoStoreInCollection = fPage2.doStoreInCollection();
        fSelectedTemplateGroup = fPage2.getTemplateGroup();
        
        BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {
            @Override
            public void run() {
                try {
                    createZipFile(fZipFile);
                    addToTemplateManager(fZipFile);
                }
                catch(final IOException ex) {
                    ex.printStackTrace();
                    Display.getDefault().asyncExec(new Runnable() { // Display after wizard closes
                        public void run() {
                            MessageDialog.openError(getShell(), "Save As Template", ex.getMessage());
                        }
                    });
                }
            }
        });
        
        return true;
    }
    
    private File askFileSave() {
        FileDialog dialog = new FileDialog(getShell(), SWT.SAVE);
        dialog.setText("Choose a file name");
        dialog.setFilterExtensions(new String[] { TemplateManager.ARCHIMATE_TEMPLATE_FILE_WILDCARD, "*.*" } );
        String path = dialog.open();
        if(path != null) {
            // Only Windows adds the extension by default
            if(dialog.getFilterIndex() == 0 && !path.endsWith(TemplateManager.ARCHIMATE_TEMPLATE_FILE_EXTENSION)) {
                path += TemplateManager.ARCHIMATE_TEMPLATE_FILE_EXTENSION;
            }
            
            File file = new File(path);
            
            // Make sure the file does not already exist
            if(file.exists()) {
                boolean result = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(), "Save As Template", "'" + file +
                        "' already exists. Are you sure you want to overwrite it?");
                if(!result) {
                    return null;
                }
            }
            
            return file;
        }
        return null;
    }
    
    private void addToTemplateManager(File zipFile) throws IOException {
        // Add to Template Manager if selected
        if(fDoStoreInCollection) {
            ITemplate template = new ModelTemplate(null);
            template.setFile(zipFile);
            fTemplateManager.addUserTemplate(template);
            // Add to user group
            if(fSelectedTemplateGroup != null) {
                fSelectedTemplateGroup.addTemplate(template);
            }
            fTemplateManager.saveUserTemplatesManifest();
        }
    }

    private void createZipFile(File zipFile) throws IOException {
        ZipOutputStream zOut = null;
        
        try {
            // Delete any existing zip first
            zipFile.delete();
            
            // Start a zip stream
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(zipFile));
            zOut = new ZipOutputStream(out);

            // Model File
            File modelFile = saveModelToTempFile();
            ZipUtils.addFileToZip(modelFile, TemplateManager.ZIP_ENTRY_MODEL, zOut);
            if(modelFile != null) {
                modelFile.delete();
            }
            
            // Manifest
            String manifest = createManifest();
            ZipUtils.addStringToZip(manifest, TemplateManager.ZIP_ENTRY_MANIFEST, zOut);
            
            // Thumbnails
            if(fIncludeThumbnails) {
                int i = 1;
                for(IDiagramModel dm : fModel.getDiagramModels()) {
                    Image image = createThumbnailImage(dm);
                    ZipUtils.addImageToZip(image, TemplateManager.ZIP_ENTRY_THUMBNAILS + i++ + ".png", zOut, SWT.IMAGE_PNG, null);
                    image.dispose();
                }
            }
        }
        finally {
            if(zOut != null) {
                try {
                    zOut.flush();
                    zOut.close();
                }
                catch(IOException ex) {
                }
            }
        }
    }
    
    private String createManifest() throws IOException {
        Document doc = new Document();
        Element root = new Element(ITemplateXMLTags.XML_TEMPLATE_ELEMENT_MANIFEST);
        doc.setRootElement(root);
        
        Element elementName = new Element(ITemplateXMLTags.XML_TEMPLATE_ELEMENT_NAME);
        elementName.setText(fTemplateName);
        root.addContent(elementName);
        
        Element elementDescription = new Element(ITemplateXMLTags.XML_TEMPLATE_ELEMENT_DESCRIPTION);
        elementDescription.setText(fTemplateDescription);
        root.addContent(elementDescription);
        
        if(fIncludeThumbnails) {
            if(fSelectedDiagramModel != null) {
                int i = 1;
                for(IDiagramModel dm : fModel.getDiagramModels()) {
                    if(dm == fSelectedDiagramModel) {
                        String keyThumb = TemplateManager.ZIP_ENTRY_THUMBNAILS + i + ".png";
                        Element elementKeyThumb = new Element(ITemplateXMLTags.XML_TEMPLATE_ELEMENT_KEY_THUMBNAIL);
                        elementKeyThumb.setText(keyThumb);
                        root.addContent(elementKeyThumb);
                        break;
                    }
                    i++;
                }
            }
        }
        
        return JDOMUtils.write2XMLString(doc);
    }

    private File saveModelToTempFile() throws IOException {
        File tmp = File.createTempFile("architemplate", null);
        tmp.deleteOnExit();
        Resource resource = ArchimateResourceFactory.createResource(tmp);
        resource.getContents().add(fModel);
        resource.save(null);
        resource.getContents().remove(fModel);
        return tmp;
    }
    
    private Image createThumbnailImage(IDiagramModel dm) {
        Shell shell = new Shell();
        GraphicalViewer diagramViewer = DiagramUtils.createViewer(dm, shell);
        Rectangle bounds = DiagramUtils.getDiagramExtents(diagramViewer);
        double ratio = Math.min(1, Math.min((double)TemplateManager.THUMBNAIL_WIDTH / bounds.width,
                (double)TemplateManager.THUMBNAIL_HEIGHT / bounds.height));
        Image image = DiagramUtils.createScaledImage(diagramViewer, ratio);
        shell.dispose();

        // Draw a border
        GC gc = new GC(image);
        Color c = new Color(null, 64, 64, 64);
        gc.setForeground(c);
        gc.drawRectangle(0, 0, image.getBounds().width - 1, image.getBounds().height - 1);
        gc.dispose();
        c.dispose();

        return image;
    }
    
    @Override
    public void dispose() {
        super.dispose();
        fTemplateManager.dispose();
    }
}
