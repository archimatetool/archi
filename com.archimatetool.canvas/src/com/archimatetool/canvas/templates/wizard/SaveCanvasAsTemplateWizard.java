/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas.templates.wizard;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.zip.ZipOutputStream;

import org.eclipse.emf.ecore.EObject;
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

import com.archimatetool.canvas.model.ICanvasModel;
import com.archimatetool.canvas.templates.model.CanvasModelTemplate;
import com.archimatetool.canvas.templates.model.CanvasTemplateManager;
import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.editor.utils.ZipUtils;
import com.archimatetool.jdom.JDOMUtils;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModelReference;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.ModelVersion;
import com.archimatetool.templates.model.ITemplateGroup;
import com.archimatetool.templates.model.ITemplateXMLTags;
import com.archimatetool.templates.model.TemplateManager;
import com.archimatetool.templates.wizard.TemplateUtils;



/**
 * Save Canvas As Template Wizard
 * 
 * @author Phillip Beauvoir
 */
public class SaveCanvasAsTemplateWizard extends Wizard {
    
    private ICanvasModel fCanvasModel;
    
    private SaveCanvasAsTemplateWizardPage fPage1;
    private SaveCanvasAsTemplateToCollectionWizardPage fPage2;
    
    private File fZipFile;
    private String fTemplateName;
    private String fTemplateDescription;
    private boolean fIncludeThumbnail;
    private ITemplateGroup fSelectedTemplateGroup;
    private boolean fDoStoreInCollection;
    
    private TemplateManager fTemplateManager;
    
    public SaveCanvasAsTemplateWizard(ICanvasModel canvasModel) {
        setWindowTitle(Messages.SaveCanvasAsTemplateWizard_0);
        fCanvasModel = canvasModel;
        fTemplateManager = new CanvasTemplateManager();
    }
    
    @Override
    public void addPages() {
        fPage1 = new SaveCanvasAsTemplateWizardPage(fCanvasModel, fTemplateManager);
        addPage(fPage1);
        fPage2 = new SaveCanvasAsTemplateToCollectionWizardPage(fTemplateManager);
        addPage(fPage2);
    }

    @Override
    public boolean performFinish() {
        // This before the thread starts
        fZipFile = new File(fPage1.getFileName());
        
        // Make sure the file does not already exist
        if(fZipFile.exists()) {
            boolean result = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(),
                    Messages.SaveCanvasAsTemplateWizard_1,
                    NLS.bind(Messages.SaveCanvasAsTemplateWizard_2, fZipFile));
            if(!result) {
                return false;
            }
        }
        
        fTemplateName = fPage1.getTemplateName();
        fTemplateDescription = fPage1.getTemplateDescription();
        fIncludeThumbnail = fPage1.includeThumbnail();
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
                            MessageDialog.openError(getShell(), Messages.SaveCanvasAsTemplateWizard_3, ex.getMessage());
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

            // Model File
            File modelFile = saveModelToTempFile();
            ZipUtils.addFileToZip(modelFile, TemplateManager.ZIP_ENTRY_MODEL, zOut);
            if(modelFile != null) {
                modelFile.delete();
            }
            
            // Manifest
            String manifest = createManifest();
            ZipUtils.addStringToZip(manifest, TemplateManager.ZIP_ENTRY_MANIFEST, zOut);
            
            // Thumbnail
            if(fIncludeThumbnail) {
                Image image = TemplateUtils.createThumbnailImage(fCanvasModel);
                ZipUtils.addImageToZip(image, TemplateManager.ZIP_ENTRY_THUMBNAILS + "1.png", zOut, SWT.IMAGE_PNG, null); //$NON-NLS-1$
                image.dispose();
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
        
        // Type
        root.setAttribute(ITemplateXMLTags.XML_TEMPLATE_ATTRIBUTE_TYPE, CanvasModelTemplate.XML_CANVAS_TEMPLATE_ATTRIBUTE_TYPE_MODEL);

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
        
        // Thumbnail
        if(fIncludeThumbnail) {
            String keyThumb = TemplateManager.ZIP_ENTRY_THUMBNAILS + "1.png"; //$NON-NLS-1$
            Element elementKeyThumb = new Element(ITemplateXMLTags.XML_TEMPLATE_ELEMENT_KEY_THUMBNAIL);
            elementKeyThumb.setText(keyThumb);
            root.addContent(elementKeyThumb);
        }
        
        return JDOMUtils.write2XMLString(doc);
    }

    private File saveModelToTempFile() throws IOException {
        File tmpFile = File.createTempFile("architemplate", null); //$NON-NLS-1$
        tmpFile.deleteOnExit();
        
        // Create a new container Archimate model
        IArchimateModel tempModel = IArchimateFactory.eINSTANCE.createArchimateModel();
        tempModel.setDefaults();
        tempModel.eAdapters().clear(); // Remove this after default folders have been added, as we'll generate our own IDs
        tempModel.setId(EcoreUtil.generateUUID());
        tempModel.setFile(tmpFile);
        tempModel.setVersion(ModelVersion.VERSION);
        tempModel.setName(Messages.SaveCanvasAsTemplateWizard_4);

        // Get the Canvas copy
        ICanvasModel copyCanvas = EcoreUtil.copy(fCanvasModel);
        
        // Remove any unsupported elements
        for(Iterator<EObject> iter = copyCanvas.eAllContents(); iter.hasNext();) {
            EObject eObject = iter.next();
            if(eObject instanceof IDiagramModelReference) {
                EcoreUtil.delete(eObject);
            }
        }
        
        // Generate new IDs
        TemplateUtils.generateNewUUIDs(copyCanvas);
        
        // Add the canvas copy to a new Views folder
        IFolder folder = tempModel.getDefaultFolderForElement(copyCanvas);
        folder.getElements().add(copyCanvas);
        
        // Use an Archive Manager to save it
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
