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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
import com.archimatetool.editor.diagram.commands.DiagramCommandFactory;
import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.editor.utils.ZipUtils;
import com.archimatetool.jdom.JDOMUtils;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModelReference;
import com.archimatetool.model.IFeature;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.ModelVersion;
import com.archimatetool.model.util.UUIDFactory;
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
    
    private IArchimateModel fModel;
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
        
        createTempModel(canvasModel); 
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
        // Store Preferences
        fPage1.storePreferences();
        
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
                        @Override
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

            // Model File
            File modelFile = saveModelToTempFile();
            ZipUtils.addFileToZip(modelFile, TemplateManager.ZIP_ENTRY_MODEL, zOut);
            if(modelFile != null) {
                modelFile.delete();
            }
            
            // Manifest
            String manifest = createManifest();
            ZipUtils.addStringToZip(manifest, TemplateManager.ZIP_ENTRY_MANIFEST, zOut, Charset.forName("UTF-8")); //$NON-NLS-1$
            
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
        
        fModel.setFile(tmpFile);
        
        // Use the Archive Manager to save it
        IArchiveManager archiveManager = (IArchiveManager)fModel.getAdapter(IArchiveManager.class);
        archiveManager.saveModel();
        
        return tmpFile;
    }
    
    private void createTempModel(ICanvasModel canvasModel) {
        fModel = IArchimateFactory.eINSTANCE.createArchimateModel();
        fModel.setDefaults();
        fModel.setVersion(ModelVersion.VERSION);
        fModel.setName(Messages.SaveCanvasAsTemplateWizard_4);
        
        // Create a copy of the Canvas and add the copy to a new Views folder
        fCanvasModel = createCanvasCopy(canvasModel);
        IFolder folder = fModel.getDefaultFolderForObject(fCanvasModel);
        folder.getElements().add(fCanvasModel);
        
        // Copy the image features from the source model to this model
        for(IFeature feature : canvasModel.getArchimateModel().getFeatures()) {
            if(IArchiveManager.isImageFeature(feature)) {
                fModel.getFeatures().putString(feature.getName(), feature.getValue());
            }
        }
        
        // Add an Archive Manager for thumbnail generation and saving
        IArchiveManager archiveManager = IArchiveManager.FACTORY.createArchiveManager(fModel);
        fModel.setAdapter(IArchiveManager.class, archiveManager);
    }
    
    private ICanvasModel createCanvasCopy(ICanvasModel canvasModel) {
        ICanvasModel copyCanvas = EcoreUtil.copy(canvasModel);
        
        // Gather up diagram model references
        List<IDiagramModelReference> toRemove = new ArrayList<IDiagramModelReference>();
        
        for(Iterator<EObject> iter = copyCanvas.eAllContents(); iter.hasNext();) {
            EObject eObject = iter.next();
            // Diagram model references and their connections will be orphaned
            if(eObject instanceof IDiagramModelReference) {
                toRemove.add((IDiagramModelReference)eObject);
            }
        }
        
        // Remove them
        for(IDiagramModelReference eObject : toRemove) {
            DiagramCommandFactory.createDeleteDiagramObjectCommand(eObject).execute();
        }
        
        // Generate new IDs
        UUIDFactory.generateNewIDs(copyCanvas);
        
        return copyCanvas;
    }
    
    @Override
    public void dispose() {
        super.dispose();
        
        fTemplateManager.dispose();
        fCanvasModel = null;
        fModel = null;
    }
}
