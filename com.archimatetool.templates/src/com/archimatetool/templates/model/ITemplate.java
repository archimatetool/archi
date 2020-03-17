/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.templates.model;

import java.io.File;
import java.io.IOException;

import org.eclipse.swt.graphics.Image;


/**
 * Interface for All Templates
 * 
 * @author Phillip Beauvoir
 */
public interface ITemplate {
    
    // Limit max amount of thumbnails as too many is slow to save and load
    int MAX_THUMBNAILS = 50;
    
    /**
     * @return The ID of this template
     */
    String getID();
    
    /**
     * @param id
     */
    void setID(String id);
    
    /**
     * Set name
     * @param name
     */
    void setName(String name);

    /**
     * @return The Name of the template
     */
    String getName();
    
    /**
     * @return The description of the template
     */
    String getDescription();
    
    /**
     * Set description
     * @param description
     */
    void setDescription(String description);
    
    /**
     * @return An image to represent this template
     */
    Image getImage();

    /**
     * @return The Key Thumbnail Image to use
     */
    Image getKeyThumbnail();
    
    /**
     * Get a thumbnail by index
     * @param index zero based index number
     * @return The thumbnail image
     */
    Image getThumbnail(int index);
    
    /**
     * @return The amount of thumbnails in the template
     */
    int getThumbnailCount();
    
    /**
     * @return The File
     */
    File getFile();
    
    /**
     * @param file
     */
    void setFile(File file);
    
    /**
     * @return
     */
    String getType();
    
    /**
     * Save the template file
     * @throws IOException 
     */
    void save() throws IOException;
    
    /**
     * Dispose
     */
    void dispose();
}
