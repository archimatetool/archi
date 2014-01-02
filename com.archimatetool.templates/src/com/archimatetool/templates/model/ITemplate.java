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
     * @return All Thumbnails
     */
    Image[] getThumbnails();
    
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
