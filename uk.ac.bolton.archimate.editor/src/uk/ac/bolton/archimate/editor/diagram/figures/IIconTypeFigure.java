/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.figures;

import org.eclipse.draw2d.Label;
import org.eclipse.swt.graphics.Image;

/**
 * IIconTypeFigure
 * 
 * @author Phillip Beauvoir
 */
public interface IIconTypeFigure {

    /**
     * @return The Image Label
     */
    Label getImageLabel();

    /**
     * @return The Image to use in the Image Label
     */
    Image getImage();
    
    /**
     * Set the image
     * @param image
     */
    void setImage(Image image);
}