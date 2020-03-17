/**
 * This program and the accompanying materials are made available under the
 * terms of the License which accompanies this distribution in the file
 * LICENSE.txt
 */
package com.archimatetool.editor.model.impl;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.editor.utils.FileUtils;
import com.archimatetool.editor.utils.ZipUtils;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModelImageProvider;
import com.archimatetool.model.util.ArchimateResourceFactory;



/**
 * Archive Manager
 * Handles saving a model to archive file if model contains images or to XML file if no images
 * Handles image data, saving images, creating images
 * 
 * @author Phillip Beauvoir
 */
public class ArchiveManager implements IArchiveManager {
    
    /**
     * Raw image bytes stored for all images in this model
     */
    private ByteArrayStorage byteArrayStorage = new ByteArrayStorage();
    
    /**
     * The ArchiMate model
     */
    private IArchimateModel fModel;
    
    /**
     * Images are loaded
     */
    private boolean fImagesLoaded = false;
    
    /**
     * @param model The owning model
     */
    public ArchiveManager(IArchimateModel model) {
        fModel = model;
    }

    @Override
    public String addImageFromFile(File file) throws IOException {
        if(file == null || !file.exists() || !file.canRead()) {
            throw new FileNotFoundException("Cannot find file"); //$NON-NLS-1$
        }
        
        // Get bytes
        byte[] bytes = byteArrayStorage.getBytesFromFile(file);
        if(bytes == null) {
            throw new IOException("Could not get bytes from file"); //$NON-NLS-1$
        }

        String entryName = createArchiveImagePathname(file);

        return addByteContentEntry(entryName, bytes);
    }
    
    /**
     * Test that a set of bytes from a file is actually a valid Image
     * @param bytes
     * @throws IOException
     */
    private void testImageBytesValid(byte[] bytes) throws IOException {
        try {
            new ImageData(new ByteArrayInputStream(bytes));
        }
        catch(Throwable ex) {
            throw new IOException("Not a supported image file", ex); //$NON-NLS-1$
        }
    }
    
    @Override
    public Image createImage(String imagePath) throws Exception {
        if(byteArrayStorage.hasEntry(imagePath)) {
            return new Image(Display.getCurrent(), byteArrayStorage.getInputStream(imagePath));
        }
        
        return null;
    }
    
    @Override
    public List<String> getImagePaths() {
        Set<String> set = new HashSet<>();
        
        for(Iterator<EObject> iter = fModel.eAllContents(); iter.hasNext();) {
            EObject element = iter.next();
            if(element instanceof IDiagramModelImageProvider) {
                String imagePath = ((IDiagramModelImageProvider)element).getImagePath();
                if(imagePath != null) {
                    set.add(imagePath);
                }
            }
        }
        
        return new ArrayList<>(set);
    }
    
    @Override
    public List<String> getLoadedImagePaths() {
        return byteArrayStorage.getEntryNames();
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
            if(entryName.startsWith("images/")) { //$NON-NLS-1$
                // Add to ByteArrayStorage
                if(!byteArrayStorage.hasEntry(entryName)) {
                    InputStream in = zipFile.getInputStream(zipEntry);
                    byteArrayStorage.addStreamEntry(entryName, in);
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
    public byte[] getBytesFromEntry(String entryName) {
        return byteArrayStorage.getEntry(entryName);
    }

    @Override
    public String addByteContentEntry(String imagePath, byte[] bytes) throws IOException {
        // Is this already in the cache?
        String entryName = byteArrayStorage.getKey(bytes);
        
        // No
        if(entryName == null) {
            // Is this actually a valid Image file? Test it...
            testImageBytesValid(bytes);
           
            // Add it
            entryName = imagePath;
            byteArrayStorage.addByteContentEntry(imagePath, bytes);
        }

        return entryName;
    }
    
    @Override
    public String copyImageBytes(IArchiveManager archiveManager, String imagePath) throws IOException {
        byte[] bytes = archiveManager.getBytesFromEntry(imagePath);
        if(bytes != null) {
            imagePath = addByteContentEntry(imagePath, bytes);
        }
        return imagePath;
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
            saveResource(file);
        }
    }
    
    @Override
    public IArchiveManager clone(IArchimateModel model) {
        ArchiveManager archiveManager = new ArchiveManager(model);
        
        for(Entry<String, byte[]> entry : byteArrayStorage.getEntrySet()) {
            archiveManager.byteArrayStorage.addByteContentEntry(entry.getKey(), entry.getValue());
        }
        
        return archiveManager;
    }
    
    /**
     * Save the model to Archive File format
     */
    private void saveModelToArchiveFile(File file) throws IOException {
        // Temp file for xml model file
        File tmpFile = File.createTempFile("archi-", null); //$NON-NLS-1$
        tmpFile.deleteOnExit();
        saveResource(tmpFile);
        
        // Create Zip File output stream to model's file
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
        ZipOutputStream zOut = new ZipOutputStream(out);
        
        try {
            // Add the model xml file
            ZipUtils.addFileToZip(tmpFile, "model.xml", zOut); //$NON-NLS-1$
            
            // Add any images
            saveImages(zOut);
        }
        finally {
            tmpFile.delete();
            zOut.close();
        }
    }
    
    /**
     * Save the model to Resource
     */
    private void saveResource(File file) throws IOException {
        Resource resource = fModel.eResource();
        
        // No parent Resource set, so create a new one
        if(resource == null) {
            resource = ArchimateResourceFactory.createNewResource(file);
            resource.getContents().add(fModel);
        }
        // We already have a Resource, re-use it but make sure the URI is updated in case the file path has changed
        else {
            resource.setURI(URI.createFileURI(file.getAbsolutePath()));
        }
        
        // Catch *all* exceptions in case of XML errors
        try {
            resource.save(null);
        }
        catch(Exception ex) {
            throw new IOException(ex);
        }
    }
    
    private void saveImages(ZipOutputStream zOut) throws IOException {
        List<String> added = new ArrayList<String>();
        
        for(Iterator<EObject> iter = fModel.eAllContents(); iter.hasNext();) {
            EObject eObject = iter.next();
            if(eObject instanceof IDiagramModelImageProvider) {
                IDiagramModelImageProvider imageProvider = (IDiagramModelImageProvider)eObject;
                String imagePath = imageProvider.getImagePath();
                if(imagePath != null && !added.contains(imagePath)) {
                    byte[] bytes = byteArrayStorage.getEntry(imagePath);
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
        
        String path = "images/" + unique; //$NON-NLS-1$
        if(ext.length() != 0) {
            path += ext;
        }
        
        return path;
    }
    
    @Override
    public void dispose() {
        byteArrayStorage.dispose();
        byteArrayStorage = null;
        fModel = null;
    }
}
