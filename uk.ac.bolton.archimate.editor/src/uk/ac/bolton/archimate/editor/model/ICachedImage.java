/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.model;

import org.eclipse.swt.graphics.Image;


/**
 * A wrapper class for an Image object that keeps track of its usage count.<p>
 * If the usage count is above zero the image will be retained and cached but once it reaches zero the
 * Image is disposed and removed from the cache.
 * 
 * @author Phillip Beauvoir
 */
public interface ICachedImage {
    /**
     * @return The Actual Image
     */
    Image getImage();
    
    /**
     * Decrease the usage count by one.
     * Should be called by clients who no longer need to retain a reference to the cached Image.
     */
    void release();
}
