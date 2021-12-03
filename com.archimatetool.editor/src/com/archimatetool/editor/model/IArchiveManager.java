/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.zip.ZipFile;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

import com.archimatetool.editor.model.impl.ArchiveManager;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.util.ArchimateResourceFactory;


/**
 * IArchiveManager
 * 
 * @author Phillip Beauvoir
 */
public interface IArchiveManager {
    
    static class FACTORY {
        
        /**
         * Return a new IArchiveManager instance
         * 
         * @param model The owning model
         * @return The IArchiveManager instance
         */
        public static IArchiveManager createArchiveManager(IArchimateModel model) {
            return new ArchiveManager(model);
        }
        
        /**
         * @param file The file to test
         * @return True if file is a zip archive file
         */
        public static boolean isArchiveFile(File file) {
            ZipFile zipFile = null;
            
            try {
                zipFile = new ZipFile(file);
                return zipFile.getEntry("model.xml") != null; //$NON-NLS-1$
            }
            catch(Exception ex) {
            }
            finally {
                try {
                    if(zipFile != null) {
                        zipFile.close();
                    }
                }
                catch(IOException ex) {
                }
            }
            
            return false;
        }
        
        /**
         * Create Resource from model file.
         * The Resource will be different if the file is an archive file.
         * @param file The model file
         * @return A new Redource
         */
        public static Resource createResource(File file) {
            return ArchimateResourceFactory.createNewResource(isArchiveFile(file) ?
                                               createArchiveModelURI(file) :
                                               URI.createFileURI(file.getAbsolutePath()));
        }
        
        /**
         * Create a URI for the model xml file in the archive file
         * 
         * @param file The archimate archive file
         * @return The URI
         */
        public static URI createArchiveModelURI(File file) {
            return URI.createURI(getArchiveFilePath(file) + "!/model.xml"); //$NON-NLS-1$
        }
        
        /**
         * Get the Archive File Path for the archive file
         * 
         * @param file The archimate archive file
         * @return The path
         */
        public static String getArchiveFilePath(File file) {
            String path = file.getAbsolutePath();
            // org.eclipse.emf.common.util.URI treats the # character as a separator
            path = path.replace("#", "%23");  //$NON-NLS-1$//$NON-NLS-2$
            return "archive:file:///" + path; //$NON-NLS-1$
        }
    }

    /**
     * Add an image from an image file to this Archive Manager's storage cache.
     * If the image already exists the existing image path is returned.
     * 
     * Once the imagepath has been returned, the caller should set the imagepath:<p>
     * IDiagramModelImageProvider.setImagePath(imagepath)
     * 
     * @param file The image file
     * @return The newly created imagePath, or an existing imagePath if the image already exists
     * @throws IOException
     */
    String addImageFromFile(File file) throws IOException;

    /**
     * Add image bytes keyed by entryName. This has to follow the same pattern as in createArchiveImagePathname()<p>
     * If the image already exists the existing image path is returned, otherwise path is returned
     * 
     * @param imagePath The key path entryname
     * @param bytes The image bytes
     * @return If the image already exists the existing imagePath is returned, otherwise imagePath is returned
     * @throws IOException
     */
    String addByteContentEntry(String imagePath, byte[] bytes) throws IOException;
    
    /**
     * Copy image bytes from another ArchiveManager and add them to this ArchiveManager
     * 
     * Once the imagepath has been returned, the caller should set the imagepath:<p>
     * IDiagramModelImageProvider.setImagePath(imagepath)
     * 
     * @param archiveManager The source ArchiveManager
     * @param imagePath The image path in the source ArchiveManager
     * @return The newly created imagePath name, or an existing imagePath if the image already exists
     * @throws IOException
     */
    String copyImageBytes(IArchiveManager archiveManager, String imagePath) throws IOException;
    
    /**
     * Get image bytes by entryName
     * 
     * @param entryName The key path entryname
     * @return The image bytes or null if not found
     */
    byte[] getBytesFromEntry(String imagePath);
    
    /**
     * Create a new Image for this path entry
     * @param imagePath The image imagePath
     * @return the Image object or null
     * @throws Exception
     */
    Image createImage(String imagePath) throws Exception;
    
    /**
     * Create a new ImageData for this path entry
     * @param imagePath The image imagePath
     * @return The ImageData or null
     */
    ImageData createImageData(String imagePath);

    /**
     * Get a copy of the list of Image entry paths as used in the model.<p>
     * This will not include duplicates. The list is re-calculated each time.
     * 
     * @return A list of image path entries as used in the current state of the model
     */
    Set<String> getImagePaths();
    
    /**
     * @return A copy of the list of image path entries for loaded image data. These may or may not be referenced in the model.
     */
    Set<String> getLoadedImagePaths();

    /**
     * Save the Model and any images to an archive file
     * @throws IOException
     */
    void saveModel() throws IOException;
    
    /**
     * Clone this ArchiveManager with a copy of this one but with the given model
     * 
     * @param model
     * @return The new ArchiveManager
     */
    IArchiveManager clone(IArchimateModel model);
    
    /**
     * Load all images for this model
     * @throws IOException
     */
    void loadImages() throws IOException;

    /**
     * Load all images from another Archimate Model archive file and add to this one
     * 
     * @param file The model file
     * @return if the images could be loaded
     * @throws IOException
     */
    boolean loadImagesFromModelFile(File file) throws IOException;
    
    /**
     * @return True if the model currently has references to at least one image and the image is loaded
     */
    boolean hasImages();
    
    /**
     * Dispose and unload any assets no longer referenced
     */
    void dispose();
}