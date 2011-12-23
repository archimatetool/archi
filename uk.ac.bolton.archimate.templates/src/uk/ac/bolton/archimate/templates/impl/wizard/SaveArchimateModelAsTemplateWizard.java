/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.templates.impl.wizard;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipOutputStream;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.jdom.Document;
import org.jdom.Element;

import uk.ac.bolton.archimate.editor.model.IArchiveManager;
import uk.ac.bolton.archimate.editor.utils.ZipUtils;
import uk.ac.bolton.archimate.model.IArchimateModel;
import uk.ac.bolton.archimate.model.IDiagramModel;
import uk.ac.bolton.archimate.templates.impl.model.ArchimateModelTemplate;
import uk.ac.bolton.archimate.templates.impl.model.ArchimateTemplateManager;
import uk.ac.bolton.archimate.templates.model.ITemplateGroup;
import uk.ac.bolton.archimate.templates.model.ITemplateXMLTags;
import uk.ac.bolton.archimate.templates.model.TemplateManager;
import uk.ac.bolton.archimate.templates.wizard.SaveModelAsTemplateToCollectionWizardPage;
import uk.ac.bolton.archimate.templates.wizard.TemplateUtils;
import uk.ac.bolton.jdom.JDOMUtils;


/**
 * Save Archmate Model As Template Wizard
 * 
 * @author Phillip Beauvoir
 */
public class SaveArchimateModelAsTemplateWizard extends Wizard {
    
    private IArchimateModel fModel;
    
    private SaveArchimateModelAsTemplateWizardPage fPage1;
    private SaveModelAsTemplateToCollectionWizardPage fPage2;
    
    private File fZipFile;
    private String fTemplateName;
    private String fTemplateDescription;
    private boolean fIncludeThumbnails;
    private IDiagramModel fSelectedDiagramModel;
    private ITemplateGroup fSelectedTemplateGroup;
    private boolean fDoStoreInCollection;
    
    private TemplateManager fTemplateManager;
    
    public SaveArchimateModelAsTemplateWizard(IArchimateModel model) {
        setWindowTitle("Save Model As Template");
        fModel = model;
        fTemplateManager = new ArchimateTemplateManager();
    }
    
    @Override
    public void addPages() {
        fPage1 = new SaveArchimateModelAsTemplateWizardPage(fModel, fTemplateManager);
        addPage(fPage1);
        fPage2 = new SaveArchimateModelAsTemplateToCollectionWizardPage(fTemplateManager);
        addPage(fPage2);
    }

    @Override
    public boolean performFinish() {
        // This before the thread starts
        fZipFile = new File(fPage1.getFileName());
        
        // Make sure the file does not already exist
        if(fZipFile.exists()) {
            boolean result = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(), "Save As Template", "'" + fZipFile +
                    "' already exists. Are you sure you want to overwrite it?");
            if(!result) {
                return false;
            }
        }

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
                    
                    if(fDoStoreInCollection) {
                        fTemplateManager.addTemplateEntry(fZipFile, fSelectedTemplateGroup);
                    }
                }
                catch(final IOException ex) {
                    ex.printStackTrace();
                    Display.getCurrent().asyncExec(new Runnable() { // Display after wizard closes
                        public void run() {
                            MessageDialog.openError(getShell(), "Save As Template", ex.getMessage());
                        }
                    });
                }
            }
        });
        
        return true;
    }
    
    private void createZipFile(File zipFile) throws IOException {
        ZipOutputStream zOut = null;
        
        try {
            // Delete any existing zip first
            zipFile.delete();
            
            // Start a zip stream
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(zipFile));
            zOut = new ZipOutputStream(out);

            // Add Manifest
            String manifest = createManifest();
            ZipUtils.addStringToZip(manifest, TemplateManager.ZIP_ENTRY_MANIFEST, zOut);
            
            // Add any thumbnails
            if(fIncludeThumbnails) {
                int i = 1;
                for(IDiagramModel dm : fModel.getDiagramModels()) {
                    Image image = TemplateUtils.createThumbnailImage(dm);
                    ZipUtils.addImageToZip(image, TemplateManager.ZIP_ENTRY_THUMBNAILS + i++ + ".png", zOut, SWT.IMAGE_PNG, null);
                    image.dispose();
                }
            }

            /*
             * Save model to xml temp file and add to Zip.
             * Do this last because we need to dispose the Archive Manager last because its images are re-used
             * several times to create thumbnails.
             */
            File tempFile = saveModelToTempFile();
            ZipUtils.addFileToZip(tempFile, TemplateManager.ZIP_ENTRY_MODEL, zOut);
            tempFile.delete();
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
        
        // Type
        root.setAttribute(ITemplateXMLTags.XML_TEMPLATE_ATTRIBUTE_TYPE, ArchimateModelTemplate.XML_TEMPLATE_ATTRIBUTE_TYPE_MODEL);

        // Timestamp
        root.setAttribute(ITemplateXMLTags.XML_TEMPLATE_ATTRIBUTE_TIMESTAMP, Long.toString(System.currentTimeMillis()));
        
        // Name
        Element elementName = new Element(ITemplateXMLTags.XML_TEMPLATE_ELEMENT_NAME);
        elementName.setText(fTemplateName);
        root.addContent(elementName);
        
        // Description
        Element elementDescription = new Element(ITemplateXMLTags.XML_TEMPLATE_ELEMENT_DESCRIPTION);
        elementDescription.setText(fTemplateDescription);
        root.addContent(elementDescription);
        
        // Thumbnails
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
        File tmpFile = File.createTempFile("architemplate", null);
        tmpFile.deleteOnExit();
        
        // Copy the model
        IArchimateModel tempModel = EcoreUtil.copy(fModel);
        tempModel.eAdapters().clear();
        tempModel.setFile(tmpFile);
        
        // Create a temp Archive Manager to save the temp model
        IArchiveManager archiveManager = IArchiveManager.FACTORY.createArchiveManager(tempModel);
        archiveManager.saveModel();
        archiveManager.dispose();
        
        return tmpFile;
    }
    
    @Override
    public void dispose() {
        super.dispose();
        fTemplateManager.dispose();
    }
}
