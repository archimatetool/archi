/**
 * This program and the accompanying materials are made available under the
 * terms of the License which accompanies this distribution in the file
 * LICENSE.txt
 */
package com.archimatetool.editor.model.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Enumeration;
import java.util.Formatter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.editor.utils.FileUtils;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModelImageProvider;
import com.archimatetool.model.IFeature;
import com.archimatetool.model.util.ArchimateResourceFactory;



/**
 * Archive Manager
 * Handles saving a model to XML file and shared image data and creating images from image data
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class ArchiveManager implements IArchiveManager {
    
    private IArchimateModel fModel;
    
    /**
     * @param model The owning model
     */
    public ArchiveManager(IArchimateModel model) {
        fModel = model;
    }

    @Override
    public String addImageFromFile(File file) throws IOException {
        if(file == null || !file.exists() || !file.canRead()) {
            throw new FileNotFoundException("Cannot find file");
        }
        
        // Get bytes
        byte[] bytes = getBytesFromFile(file);
        if(bytes == null) {
            throw new IOException("Could not get bytes from file");
        }
        
        // Get hashed imagePath
        String imagePath = createArchiveImagePathname(FileUtils.getFileExtension(file), bytes);
        return addByteContentEntry(imagePath, bytes);
    }
    
    @Override
    public Image createImage(String imagePath) {
        byte[] bytes = getBytesFromEntry(imagePath);
        return bytes != null ? new Image(Display.getCurrent(), new ByteArrayInputStream(bytes)) : null;
    }
    
    @Override
    public ImageData createImageData(String imagePath) {
        byte[] bytes = getBytesFromEntry(imagePath);
        return bytes != null ? new ImageData(new ByteArrayInputStream(bytes)) : null;
    }
    
    @Override
    public Set<String> getImagePaths() {
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
        
        return set;
    }
    
    @Override
    public Set<String> getLoadedImagePaths() {
        Set<String> set = new HashSet<>();
        
        for(IFeature feature : getImageFeatures()) {
            set.add(feature.getName());
        }
        
        return set;
    }
    
    @Override
    public boolean hasImages() {
        // List of of actual images loaded
        Set<String> loadedImagePaths = getLoadedImagePaths();
        
        // If no loaded images...
        if(loadedImagePaths.isEmpty()) {
            return false;
        }
        
        // Iterate thru model and find instances of IDiagramModelImageProvider
        for(Iterator<EObject> iter = fModel.eAllContents(); iter.hasNext();) {
            EObject element = iter.next();
            
            if(element instanceof IDiagramModelImageProvider) {
                String imagePath = ((IDiagramModelImageProvider)element).getImagePath();
                
                // If it has an image path and it's loaded we have images
                if(imagePath != null && loadedImagePaths.contains(imagePath)) {
                    return true;
                }
            }
        }
        
        return false;
    }

    @Override
    public byte[] getBytesFromEntry(String imagePath) {
        String s = fModel.getFeatures().getString(imagePath, null);
        return s != null ? Base64.getDecoder().decode(s) : null;
    }

    @Override
    public String addByteContentEntry(String imagePath, byte[] bytes) throws IOException {
        // Is this already in the model?
        if(fModel.getFeatures().has(imagePath)) {
            return imagePath;
        }
        
        String encodedImageBytes = Base64.getEncoder().encodeToString(bytes);
        
        // Do we already have this by another name?
        String existingEntry = getImagePath(encodedImageBytes);
        if(existingEntry != null) {
            return existingEntry;
        }
        
        // Is this actually a valid Image file?
        testImageBytesValid(bytes);

        // Store it as a feature
        fModel.getFeatures().putString(imagePath, encodedImageBytes, null);
        
        return imagePath;
    }
    
    @Override
    public String copyImageBytes(IArchimateModel model, String imagePath) {
        // Is this already in the model?
        if(fModel.getFeatures().has(imagePath)) {
            return imagePath;
        }

        String encodedImageBytes = model.getFeatures().getString(imagePath, null);
        if(encodedImageBytes == null) {
            return null;
        }
        
        // Do we already have this by another name?
        String existingEntry = getImagePath(encodedImageBytes);
        if(existingEntry != null) {
            return existingEntry;
        }
        
        // Copy the feature to this model if we don't have it...
        fModel.getFeatures().putString(imagePath, encodedImageBytes, null);
        
        return imagePath;
    }
    
    @Override
    public void saveModel() throws IOException {
        File file = fModel.getFile();
        if(file == null) {
            return;
        }
        
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
        
        // Remove unused image data
        List<IFeature> removedFeatures = removeUnreferencedImageFeatures();
        
        // Catch *all* exceptions in case of XML errors
        try {
            resource.save(null);
        }
        catch(Exception ex) {
            throw new IOException(ex);
        }
        finally {
            restoreUnreferencedImageFeatures(removedFeatures);
        }
    }
    
    @Override
    public void convertImagesFromLegacyArchive(File file) throws IOException {
        if(file == null || !file.exists() || !FACTORY.isArchiveFile(file)) {
            return;
        }
        
        // Open zip file and get image bytes
        ZipFile zipFile = new ZipFile(file);
        for(Enumeration<? extends ZipEntry> enm = zipFile.entries(); enm.hasMoreElements();) {
            ZipEntry zipEntry = enm.nextElement();
            String entryName = zipEntry.getName();
            if(entryName.startsWith(imageFeaturePrefix) && !fModel.getFeatures().has(entryName)) {
                InputStream in = zipFile.getInputStream(zipEntry);
                byte[] bytes = getBytesFromStream(in);
                
                // Store it as a feature
                String encodedImageBytes = Base64.getEncoder().encodeToString(bytes);
                fModel.getFeatures().putString(entryName, encodedImageBytes, null);
            }
        }
        zipFile.close();
    }
    
    /**
     * Remove all image features that are not referenced so we don't save them
     * Return the removed features so we can restore them
     */
    private List<IFeature> removeUnreferencedImageFeatures() {
        List<IFeature> removed = new ArrayList<>();
        
        // Get image paths that are referenced
        Set<String> imagePathsInUse = getImagePaths();
        
        // Remove any not in the list
        for(IFeature feature : getImageFeatures()) {
            String featureName = feature.getName();
            if(!imagePathsInUse.contains(featureName)) {
                removed.add(feature);
                fModel.getFeatures().remove(featureName);
            }
        }
        
        return removed;
    }
    
    @Override
    public Set<IFeature> getImageFeatures() {
        Set<IFeature> set = new HashSet<>();
        
        for(IFeature feature : fModel.getFeatures()) {
            if(IArchiveManager.isImageFeature(feature)) {
                set.add(feature);
            }
        }
        
        return set;
    }
    
    /**
     * Restore all image features that were previosuly removed so that we can still reference
     * them in the model in case we Undo/Redo
     */
    private void restoreUnreferencedImageFeatures(List<IFeature> features) {
        fModel.getFeatures().addAll(features);
    }
    
    private String createArchiveImagePathname(String suffix, byte[] bytes) throws IOException {
        try {
            return imageFeaturePrefix + createHash(bytes) + suffix;
        }
        catch(NoSuchAlgorithmException ex) {
            throw new IOException(ex);
        }
    }
    
    /**
     * Create a unique hash from bytes. Then we can check for image duplicates
     */
    private String createHash(byte[] bytes) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] hash = md.digest(bytes);

        // Convert to hex
        try(Formatter formatter = new Formatter()) {
            for(byte b : hash) {
                formatter.format("%02x", b);
            }
            return formatter.toString();
        }
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
            throw new IOException("Not a supported image file", ex);
        }
    }
    
    private byte[] getBytesFromFile(File file) throws IOException {
        if(file == null || !file.exists()) {
            return null;
        }
        
        return Files.readAllBytes(file.toPath());
    }
    
    /**
     * Read in a stream and return its contents as a byte array
     */
    private byte[] getBytesFromStream(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[8192];
        try {
            int size;
            while((size = in.read(buf)) != -1) {
                out.write(buf, 0, size);
            }
        }
        finally {
            out.close();
            in.close();
        }
        
        return out.toByteArray();
    }

    /**
     * Return an image path from the the encodedImageBytes
     */
    private String getImagePath(String encodedImageBytes) {
        for(IFeature feature : getImageFeatures()) {
            if(feature.getValue().equals(encodedImageBytes)) {
                return feature.getName();
            }
        }
        
        return null;
    }
}
