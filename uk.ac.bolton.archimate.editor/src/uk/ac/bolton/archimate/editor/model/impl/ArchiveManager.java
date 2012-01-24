/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.model.impl;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import uk.ac.bolton.archimate.editor.model.IArchiveManager;
import uk.ac.bolton.archimate.editor.model.IEditorModelManager;
import uk.ac.bolton.archimate.editor.utils.FileUtils;
import uk.ac.bolton.archimate.editor.utils.ZipUtils;
import uk.ac.bolton.archimate.model.IArchimateModel;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IDiagramModelImageProvider;
import uk.ac.bolton.archimate.model.util.ArchimateResourceFactory;


/**
 * Archive Manager
 * 
 * @author Phillip Beauvoir
 */
public class ArchiveManager implements IArchiveManager {
    
    /**
     * Raw image bytes loaded for all images in use in this model
     */
    static ByteArrayStorage BYTE_ARRAY_STORAGE = new ByteArrayStorage();
    
    /**
     * The ArchiMate model
     */
    private IArchimateModel fModel;
    
    /**
     * Images are loaded
     */
    private boolean fImagesLoaded = false;
    
    /**
     * Paths of images loaded
     */
    private List<String> fLoadedImagePaths = new ArrayList<String>();
    
    /**
     * Adapter monitors added image components added by user (copy & paste, DND, image set, etc)
     * since images were loaded from archive file.
     */
    private EContentAdapter fModelAdapter = new EContentAdapter() {
        @Override
        public void notifyChanged(Notification msg) {
            super.notifyChanged(msg);

            // IDiagramModelImageProvider added
            if(msg.getEventType() == Notification.ADD) {
                if(msg.getNewValue() instanceof IDiagramModelImageProvider) {
                    IDiagramModelImageProvider imageProvider = (IDiagramModelImageProvider)msg.getNewValue();
                    String imagePath = imageProvider.getImagePath();
                    if(imagePath != null && !fLoadedImagePaths.contains(imagePath)) {
                        fLoadedImagePaths.add(imagePath);
                    }
                }
            }
            // Image path set
            else if(msg.getEventType() == Notification.SET) {
                if(msg.getFeature() == IArchimatePackage.Literals.DIAGRAM_MODEL_IMAGE_PROVIDER__IMAGE_PATH) {
                    String imagePath = (String)msg.getNewValue();
                    if(imagePath != null && !fLoadedImagePaths.contains(imagePath)) {
                        fLoadedImagePaths.add(imagePath);
                    }
                }
            }
        }
    };
    
    /**
     * @param model The owning model
     */
    public ArchiveManager(IArchimateModel model) {
        fModel = model;
        fModel.eAdapters().add(fModelAdapter);
    }

    @Override
    public String addImageFromFile(File file) throws IOException {
        // Get bytes
        byte[] bytes = BYTE_ARRAY_STORAGE.getBytesFromFile(file);
        
        // Is this already in the cache?
        String entryName = BYTE_ARRAY_STORAGE.getKey(bytes);
        // No, so create a new one
        if(entryName == null) {
            // Is this actually a valid Image file? Test it...
            try {
                new Image(Display.getCurrent(), new ByteArrayInputStream(bytes));
            }
            catch(Throwable ex) {
                throw new IOException("Not a supported image file", ex);
            }
            
            // OK, add the bytes
            entryName = createArchiveImagePathname(file);
            BYTE_ARRAY_STORAGE.addByteContentEntry(entryName, bytes);
        }
        
        // (The image path will be added in the Model Adapter...)
        
        return entryName;
    }
    
    @Override
    public Image createImage(String path) throws Exception {
        if(BYTE_ARRAY_STORAGE.hasEntry(path)) {
            return new Image(Display.getCurrent(), BYTE_ARRAY_STORAGE.getInputStream(path));
        }
        
        return null;
    }
    
    @Override
    public List<String> getImagePaths() {
        List<String> list = new ArrayList<String>();
        
        for(Iterator<EObject> iter = fModel.eAllContents(); iter.hasNext();) {
            EObject element = iter.next();
            if(element instanceof IDiagramModelImageProvider) {
                String imagePath = ((IDiagramModelImageProvider)element).getImagePath();
                if(imagePath != null && !list.contains(imagePath)) {
                    list.add(imagePath);
                }
            }
        }
        
        return list;
    }
    
    /**
     * Load images from model's archive file
     */
    @Override
    public void loadImages() throws IOException {
        if(!fImagesLoaded && loadImagesFromModelFile(fModel.getFile())) {
            fImagesLoaded = true;
        }
    }
    
    @Override
    public boolean loadImagesFromModelFile(File file) throws IOException {
        if(file == null || !file.exists() || !FACTORY.isArchiveFile(file)) {
            return false;
        }
        
        ZipFile zipFile = new ZipFile(file);
        
        for(Enumeration<? extends ZipEntry> enm = zipFile.entries(); enm.hasMoreElements();) {
            ZipEntry zipEntry = enm.nextElement();
            String entryName = zipEntry.getName();
            if(entryName.startsWith("images/")) {
                // Add to ByteArrayStorage
                if(!BYTE_ARRAY_STORAGE.hasEntry(entryName)) {
                    InputStream in = zipFile.getInputStream(zipEntry);
                    BYTE_ARRAY_STORAGE.addStreamEntry(entryName, in);
                }
                
                // Add to list
                if(!fLoadedImagePaths.contains(entryName)) {
                    fLoadedImagePaths.add(entryName);
                }
            }
        }
        
        zipFile.close();
        
        return true;
    }
    
    @Override
    public boolean hasImages() {
        for(Iterator<EObject> iter = fModel.eAllContents(); iter.hasNext();) {
            EObject element = iter.next();
            if(element instanceof IDiagramModelImageProvider) {
                String imagePath = ((IDiagramModelImageProvider)element).getImagePath();
                if(imagePath != null) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public void saveModel() throws IOException {
        File file = fModel.getFile();
        
        if(file == null) {
            return;
        }
        
        if(hasImages()) {
            saveModelToArchiveFile(file);
        }
        else {
            saveModelToXMLFile(file);
        }
    }
    
    /**
     * Save the model to Archive File format
     */
    private void saveModelToArchiveFile(File file) throws IOException {
        // Temp file for xml model file
        File tmpFile = File.createTempFile("archimate", null);
        tmpFile.deleteOnExit();
        saveModelToXMLFile(tmpFile);
        
        // Create Zip File output stream to model's file
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
        ZipOutputStream zOut = new ZipOutputStream(out);
        
        try {
            // Add the model xml file
            ZipUtils.addFileToZip(tmpFile, "model.xml", zOut);
            
            // Add any images
            saveImages(zOut);
        }
        finally {
            tmpFile.delete();
            zOut.close();
        }
    }
    
    /**
     * Save the model to XML File format
     */
    private void saveModelToXMLFile(File file) throws IOException {
        ResourceSet resourceSet = ArchimateResourceFactory.createResourceSet();
        Resource resource = resourceSet.createResource(URI.createFileURI(file.getAbsolutePath()));
        resource.getContents().add(fModel);
        resource.save(null);
    }
    
    private void saveImages(ZipOutputStream zOut) throws IOException {
        List<String> added = new ArrayList<String>();
        
        for(Iterator<EObject> iter = fModel.eAllContents(); iter.hasNext();) {
            EObject eObject = iter.next();
            if(eObject instanceof IDiagramModelImageProvider) {
                IDiagramModelImageProvider imageProvider = (IDiagramModelImageProvider)eObject;
                String imagePath = imageProvider.getImagePath();
                if(imagePath != null && !added.contains(imagePath)) {
                    byte[] bytes = BYTE_ARRAY_STORAGE.getEntry(imagePath);
                    if(bytes != null) {
                        ZipEntry zipEntry = new ZipEntry(imagePath);
                        zOut.putNextEntry(zipEntry);
                        zOut.write(bytes);
                        zOut.closeEntry();
                        added.add(imagePath);
                    }
                }
            }
        }
    }
    
    private String createArchiveImagePathname(File file) {
        String ext = FileUtils.getFileExtension(file);
        
        String unique = EcoreUtil.generateUUID();
        
        String path = "images/" + unique;
        if(ext.length() != 0) {
            path += ext;
        }
        
        return path;
    }
    
    @Override
    public void dispose() {
        fModel.eAdapters().remove(fModelAdapter);
        
        if(!fLoadedImagePaths.isEmpty()) {
            unloadUnusedImages();
        }
        
        fLoadedImagePaths = null;
    }
    
    /**
     * Unload any images not in use in other models
     */
    private void unloadUnusedImages() {
        // Gather all image paths that are in use in other models
        List<String> allPathsInUse = new ArrayList<String>();
        
        for(IArchimateModel model : IEditorModelManager.INSTANCE.getModels()) {
            if(model != fModel) { // don't bother with this model as we no longer use any images
                ArchiveManager archiveManager = (ArchiveManager)model.getAdapter(IArchiveManager.class);
                for(String imagePath : archiveManager.fLoadedImagePaths) {
                    if(!allPathsInUse.contains(imagePath)) {
                        allPathsInUse.add(imagePath);
                    }
                }
            }
        }
        
        // Release all unused image data and cached images that are not in image paths
        for(String imagePath : fLoadedImagePaths) {
            if(!allPathsInUse.contains(imagePath)) {
                BYTE_ARRAY_STORAGE.removeEntry(imagePath);
            }
        }
    }
}
