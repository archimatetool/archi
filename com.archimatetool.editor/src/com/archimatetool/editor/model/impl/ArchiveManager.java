/**
 * This program and the accompanying materials are made available under the
 * terms of the License which accompanies this distribution in the file
 * LICENSE.txt
 */
package com.archimatetool.editor.model.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;
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
@SuppressWarnings("nls")
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
    public boolean useArchiveFormat() {
        if(fModel.getFile() == null) {
            return true; // default for safety
        }
        
        // Use archive format if model file is not in a git folder
        File gitFolder = new File(fModel.getFile().getParentFile(), ".git");
        return !(gitFolder.exists() && gitFolder.isDirectory());
    }

    @Override
    public String addImageFromFile(File file) throws IOException {
        if(file == null || !file.exists() || !file.canRead()) {
            throw new FileNotFoundException("Cannot find file");
        }
        
        // Get bytes
        byte[] bytes = byteArrayStorage.getBytesFromFile(file);
        if(bytes == null) {
            throw new IOException("Could not get bytes from file");
        }

        return addByteContentEntry(createArchiveImagePathname(file.getName()), bytes);
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
    
    @Override
    public Image createImage(String imagePath) throws Exception {
        if(imagePath != null && byteArrayStorage.hasEntry(imagePath)) {
            return new Image(Display.getCurrent(), byteArrayStorage.getInputStream(imagePath));
        }
        
        return null;
    }
    
    @Override
    public ImageData createImageData(String imagePath) {
        if(byteArrayStorage.hasEntry(imagePath)) {
            return new ImageData(byteArrayStorage.getInputStream(imagePath));
        }
        
        return null;
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
        return byteArrayStorage.getEntryNames();
    }
    
    /**
     * Load images from model's archive file or folder
     */
    @Override
    public void loadImages() throws IOException {
        if(!fImagesLoaded && fModel.getFile() != null) {
            // Archive format
            if(FACTORY.isArchiveFile(fModel.getFile())) {
                fImagesLoaded = loadImagesFromModelFile(fModel.getFile());
            }
            // Else try and load if there is an "images" folder
            else {
                fImagesLoaded = loadImagesFromModelFolder(fModel.getFile());
            }
        }
    }
    
    @Override
    public boolean loadImagesFromModelFile(File file) throws IOException {
        if(file == null || !file.exists() || !FACTORY.isArchiveFile(file)) {
            return false;
        }
        
        try(ZipFile zipFile = new ZipFile(file)) {
            for(Enumeration<? extends ZipEntry> enm = zipFile.entries(); enm.hasMoreElements();) {
                ZipEntry zipEntry = enm.nextElement();
                String entryName = zipEntry.getName();
                if(entryName.startsWith("images/")) {
                    // Add to ByteArrayStorage
                    if(!byteArrayStorage.hasEntry(entryName)) {
                        InputStream in = zipFile.getInputStream(zipEntry);
                        byteArrayStorage.addStreamEntry(entryName, in);
                    }
                }
            }
        }
        
        return true;
    }
    
    /**
     * Load any images from the "images" folder if this model is in a git repository
     */
    private boolean loadImagesFromModelFolder(File modelFile) throws IOException {
        File imagesFolder = getImagesFolder(modelFile);
        if(FileUtils.isFolderEmpty(imagesFolder)) { // No images in folder
            return false;
        }
        
        // Get image paths in the model
        Set<String> paths = getImagePaths();
        if(paths.isEmpty()) {
            return false;
        }
        
        File[] files = imagesFolder.listFiles();
        if(files == null) {
            return false;
        }

        // Iterate through all image files in the folder
        for(File imageFile : files) {
            String entryName = "images/" + imageFile.getName();
            
            // If the image belongs to the model and it's not loaded, then load it
            // This ensures we don't load any image file that doesn't belong in the model
            if(paths.contains(entryName) && !byteArrayStorage.hasEntry(entryName)) {
                byte[] bytes = Files.readAllBytes(imageFile.toPath());
                byteArrayStorage.addByteContentEntry(entryName, bytes);
            }
        }
        
        return true;
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
    public byte[] getBytesFromEntry(String entryName) {
        return byteArrayStorage.getEntry(entryName);
    }

    @Override
    public String addByteContentEntry(String imagePath, byte[] bytes) throws IOException {
        // Are these bytes already in the storage? If so, return the entry name we have for it.
        String entryName = byteArrayStorage.getKey(bytes);
        if(entryName != null) {
            return entryName;
        }
        
        // So we don't have these bytes
        
        // First check whether the bytes are actually a valid Image file...
        testImageBytesValid(bytes);

        // If we already have an entry name equal to imagePath we need a new entry name because these are different bytes
        // Else we can use the image path provided.
        entryName = byteArrayStorage.hasEntry(imagePath) ? createArchiveImagePathname(imagePath) : imagePath;

        // Add the bytes
        byteArrayStorage.addByteContentEntry(entryName, bytes);

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
        
        // Archive format
        if(useArchiveFormat()) {
            // If the model has images use archive format
            if(hasImages()) {
                saveModelToArchiveFile(file);
            }
            // Else just save the model
            else {
                saveResource(file);
            }
        }
        // Folder format
        else {
            saveModelWithImagesFolder(file);
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
        try(ZipOutputStream zOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
            // Add the model xml file
            saveModelToArchiveFile(zOut);
            
            // Add any images
            saveImagesToArchiveFile(zOut);
        }
    }
    
    /**
     * Save the model not in archive format, with images in an "images" file if the model references any images
     */
    private void saveModelWithImagesFolder(File modelFile) throws IOException {
        // Save the model file
        saveResource(modelFile);
        
        File modelFolder = getModelFolder(modelFile);
        File imagesFolder = getImagesFolder(modelFile);

        Set<String> imagePaths = getImagePaths();             // image paths referenced in the model
        Set<String> loadedImagePaths = getLoadedImagePaths(); // image paths loaded in memory
        
        // Iterate all image files. If we don't reference the file in the model, delete it
        File[] files = imagesFolder.listFiles();
        if(files != null) {
            for(File file : files) {
                if(!file.isDirectory()) {
                    String imagePath = "images/" + file.getName();
                    // If we don't reference the file in the model and it's loaded in memory (this checks that it's a valid image file)
                    if(!imagePaths.contains(imagePath) && loadedImagePaths.contains(imagePath)) {
                        file.delete();
                    }
                }
            }
        }
        
        // Iterate all referenced image paths in the model. If the file doesn't exist, save it
        for(String imagePath : imagePaths) {
            File file = new File(modelFolder, imagePath);
            if(!file.exists()) {
                byte[] bytes = byteArrayStorage.getEntry(imagePath);
                if(bytes != null) {
                    // Ensure images folder exists
                    imagesFolder.mkdirs();
                    // Save
                    Files.write(Path.of(modelFolder.getPath(), imagePath), bytes, StandardOpenOption.CREATE);
                }
            }
        }
    }
    
    /**
     * Save the model xml file in the Archive File
     */
    private void saveModelToArchiveFile(ZipOutputStream zOut) throws IOException {
        // Temp file for xml model file
        File tmpFile = File.createTempFile("archi-", null);
        tmpFile.deleteOnExit();
        saveResource(tmpFile);
        
        ZipEntry zipEntry = new ZipEntry("model.xml");
        zOut.putNextEntry(zipEntry);
        
        final int bufSize = 8192;
        byte buf[] = new byte[bufSize];
        int bytesRead;
        
        try(BufferedInputStream in = new BufferedInputStream(new FileInputStream(tmpFile), bufSize)) {
            while((bytesRead = in.read(buf)) != -1) {
                zOut.write(buf, 0, bytesRead);
            }
            zOut.closeEntry();
        }
        finally {
            tmpFile.delete();
        }
    }
    
    /**
     * Save the model's images to the archive file
     */
    private void saveImagesToArchiveFile(ZipOutputStream zOut) throws IOException {
        for(String imagePath : getImagePaths()) {
            byte[] bytes = byteArrayStorage.getEntry(imagePath);
            if(bytes != null) {
                ZipEntry zipEntry = new ZipEntry(imagePath);
                zOut.putNextEntry(zipEntry);
                zOut.write(bytes);
                zOut.closeEntry();
            }
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
    
    // Regex pattern that matches the image path name generated by EcoreUtil.generateUUID()
    // plus any file extension such as .png.
    // Matches _ad8YQI9BEfCZHKJivCqCyg.png and _ad8YQI9BEfCZHKJivCqCyg
    // Can be used to check if an image file is one generate by us
    private static final Pattern imageFilePattern = Pattern.compile("^_[A-Za-z0-9+_/-]{22}.*$");
    
    static boolean isImageName(String name) {
        return name != null ? imageFilePattern.matcher(name).matches() : false;
    }
    
    private String createArchiveImagePathname(String fileName) {
        String path = "images/" + EcoreUtil.generateUUID();
        
        String extension = getFileNameExtension(fileName);
        if(extension.length() != 0) {
            path += extension;
        }
        
        return path;
    }
    
    private String getFileNameExtension(String fileName) {
        int i = fileName.lastIndexOf('.');
        if(i > 0 && i < fileName.length() - 1) {
            return fileName.substring(i).toLowerCase();
        }
        return "";
    }
    
    private File getModelFolder(File modelFile) {
        return modelFile.getParentFile();
    }
    
    private File getImagesFolder(File modelFile) {
        return modelFile != null ? new File(getModelFolder(modelFile), "images") : null;
    }
    
    @Override
    public void dispose() {
        if(byteArrayStorage != null) {
            byteArrayStorage.dispose();
            byteArrayStorage = null;
        }

        fModel = null;
    }
}
