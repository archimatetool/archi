/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipFile;

import org.eclipse.emf.common.util.URI;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.model.impl.ArchiveManager;
import com.archimatetool.model.IArchimateModel;


/**
 * IArchiveManager
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public interface IArchiveManager {
    
    String imageFeaturePrefix = "images/";
    
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
                return zipFile.getEntry("model.xml") != null;
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
         * Create a URI for the model xml file in the archive file
         * 
         * @param file The archimate archive file
         * @return The URI
         */
        public static URI createArchiveModelURI(File file) {
            return URI.createURI(getArchiveFilePath(file) + "!/model.xml");
        }
        
        /**
         * Get the Archive File Path for the archive file
         * 
         * @param file The archimate archive file
         * @return The path
         */
        public static String getArchiveFilePath(File file) {
            String path = file.getPath();
            // org.eclipse.emf.common.util.URI treats the # character as a separator
            path = path.replace("#", "%23");
            return "archive:file:///" + path;
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
     * Add image bytes keyed by imagePath. This has to follow the same pattern as in createArchiveImagePathname()<p>
     * 
     * @param imagePath The key path entryname
     * @param bytes The image bytes
     * @return the existing imagePath
     * @throws IOException
     */
    String addByteContentEntry(String imagePath, byte[] bytes) throws IOException;
    
    /**
     * Copy image bytes from another model and add them to this model
     * 
     * @param model The source model
     * @param imagePath The image path in the source ArchiveManager
     */
    void copyImageBytes(IArchimateModel model, String imagePath);
    
    /**
     * Get image bytes by entryName
     * 
     * @param imagePath The key path entryname
     * @return The image bytes or null if not found
     */
    byte[] getBytesFromEntry(String imagePath);
    
    /**
     * Create a new Image for this path entry
     * @param imagePath The image imagePath
     * @return the Image object or null
     */
    Image createImage(String imagePath);

    /**
     * Get a live list of Image entry paths as used in the current state of the model.<p>
     * This will not include duplicates. The list is re-calculated each time.
     * 
     * @return A list of image path entries as used in the current state of the model
     */
    List<String> getImagePaths();
    
    /**
     * Save the Model and any images to an archive file
     * @throws IOException
     */
    void saveModel() throws IOException;
    
    /**
     * TODO: Remove this as this does nothing now
     */
    @SuppressWarnings("unused")
    default void loadImages() throws IOException {}
    
    /**
     * Convert image paths from a legacy archive zip file to the new format
     * @param file The archive legacy file
     * @throws IOException
     */
    void convertImagesFromLegacyArchive(File file) throws IOException;

    /**
     * @return True if the model currently has references to images
     */
    boolean hasImages();
}