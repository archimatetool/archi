/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.canvas.templates.model;

import org.eclipse.swt.graphics.Image;

import uk.ac.bolton.archimate.canvas.ICanvasImages;
import uk.ac.bolton.archimate.templates.model.AbstractTemplate;


/**
 * Canvas Model Template
 * 
 * @author Phillip Beauvoir
 */
public class CanvasModelTemplate extends AbstractTemplate {

    public static final String XML_CANVAS_TEMPLATE_ATTRIBUTE_TYPE_MODEL = "canvas"; //$NON-NLS-1$
    
    public CanvasModelTemplate() {
    }
    
    public CanvasModelTemplate(String id) {
        super(id);
    }

    @Override
    public String getType() {
        return XML_CANVAS_TEMPLATE_ATTRIBUTE_TYPE_MODEL;
    }

    @Override
    public Image getImage() {
        return ICanvasImages.ImageFactory.getImage(ICanvasImages.ICON_CANVAS_MODEL_16);
    }
}
