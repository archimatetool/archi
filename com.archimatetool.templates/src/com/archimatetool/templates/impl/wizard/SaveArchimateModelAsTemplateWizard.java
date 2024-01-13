/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.templates.impl.wizard;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.zip.ZipOutputStream;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.jdom2.Document;
import org.jdom2.Element;

import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.editor.utils.ZipUtils;
import com.archimatetool.jdom.JDOMUtils;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.templates.impl.model.ArchimateModelTemplate;
import com.archimatetool.templates.impl.model.ArchimateTemplateManager;
import com.archimatetool.templates.model.ITemplate;
import com.archimatetool.templates.model.ITemplateGroup;
import com.archimatetool.templates.model.ITemplateXMLTags;
import com.archimatetool.templates.model.TemplateManager;
import com.archimatetool.templates.wizard.SaveModelAsTemplateToCollectionWizardPage;
import com.archimatetool.templates.wizard.TemplateUtils;



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
        setWindowTitle(Messages.SaveArchimateModelAsTemplateWizard_0);
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
        // Store Preferences
        fPage1.storePreferences();

        // This before the thread starts
        fZipFile = new File(fPage1.getFileName());
        
        // Make sure the file does not already exist
        if(fZipFile.exists()) {
            boolean result = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(),
                    Messages.SaveArchimateModelAsTemplateWizard_1,
                    NLS.bind(Messages.SaveArchimateModelAsTemplateWizard_2, fZipFile.getPath()));
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
                        @Override
                        public void run() {
                            MessageDialog.openError(getShell(), Messages.SaveArchimateModelAsTemplateWizard_3, ex.getMessage());
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
            // Make sure parent folder exists
            File parent = zipFile.getParentFile();
            if(parent != null) {
                parent.mkdirs();
            }

            // Delete any existing zip first
            zipFile.delete();
            
            // Start a zip stream
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(zipFile));
            zOut = new ZipOutputStream(out);

            // Add Manifest
            String manifest = createManifest();
            ZipUtils.addStringToZip(manifest, TemplateManager.ZIP_ENTRY_MANIFEST, zOut, Charset.forName("UTF-8")); //$NON-NLS-1$
            
            // Add Thumbnails but no more than MAX_THUMBNAILS and the key thumb as well
            if(fIncludeThumbnails) {
                int count = 2;
                
                for(IDiagramModel dm : fModel.getDiagramModels()) {
                    int index = -1;
                    
                    if(fSelectedDiagramModel == dm) { // key thumbnail
                        index = 1;
                    }
                    else if(count <= ITemplate.MAX_THUMBNAILS) { //others
                        index = count++;
                    }
                    
                    if(index != -1) {
                        Image image = TemplateUtils.createThumbnailImage(dm);
                        ZipUtils.addImageToZip(image, TemplateManager.ZIP_ENTRY_THUMBNAILS + index + ".png", zOut, SWT.IMAGE_PNG, null); //$NON-NLS-1$
                        image.dispose();
                    }
                }
            }

            // Save model to xml temp file and add to Zip
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
        
        // Key thumbnail
        if(fIncludeThumbnails) {
            Element elementKeyThumb = new Element(ITemplateXMLTags.XML_TEMPLATE_ELEMENT_KEY_THUMBNAIL);
            elementKeyThumb.setText(TemplateManager.ZIP_ENTRY_THUMBNAILS + "1.png");  //$NON-NLS-1$
            root.addContent(elementKeyThumb);
        }
        
        return JDOMUtils.write2XMLString(doc);
    }

    private File saveModelToTempFile() throws IOException {
        File tmpFile = File.createTempFile("architemplate", null); //$NON-NLS-1$
        tmpFile.deleteOnExit();
        
        // Copy the model
        IArchimateModel tempModel = EcoreUtil.copy(fModel);
        tempModel.eAdapters().clear();
        tempModel.setFile(tmpFile);
        
        // Use an Archive Manager for saving
        IArchiveManager archiveManager = IArchiveManager.FACTORY.createArchiveManager(tempModel);
        archiveManager.saveModel();
        
        return tmpFile;
    }
    
    @Override
    public void dispose() {
        super.dispose();
        
        fTemplateManager.dispose();
        
        fTemplateManager = null;
        fModel = null;
        fSelectedDiagramModel = null;
    }
}
