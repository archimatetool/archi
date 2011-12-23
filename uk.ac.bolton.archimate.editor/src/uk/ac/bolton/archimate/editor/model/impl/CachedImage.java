/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.model.impl;

import org.eclipse.swt.graphics.Image;

import uk.ac.bolton.archimate.editor.model.ICachedImage;


/**
 * A wrapper class for an Image object that keeps track of its usage count.<p>
 * If the usage count is above zero the image will be retained and cached but once it reaches zero the
 * Image is disposed and removed from the cache.
 * 
 * @author Phillip Beauvoir
 */
public class CachedImage implements ICachedImage {

    private String fPath;
    private Image fImage;
    private int fUsageCount;
    
    CachedImage(String path, Image image) {
        fPath = path; 
        fImage = image;
    }
    
    @Override
    public Image getImage() {
        return fImage;
    }
    
    /**
     * Increases the usage count by one.
     */
    void retain() {
        if(fImage != null) {
            fUsageCount++;
        }
    }
    
    @Override
    public void release() {
        fUsageCount--;
        if(fUsageCount == 0) {
            dispose();
        }
    }

    void dispose() {
        if(fImage != null && !fImage.isDisposed()) {
            fImage.dispose();
            fImage = null;
        }

        ArchiveManager.IMAGE_CACHE.remove(fPath);
    }
}
