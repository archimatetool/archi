/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.templates.impl.model;

import org.eclipse.swt.graphics.Image;

import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.templates.model.AbstractTemplate;


/**
 * Archimate Model Template
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateModelTemplate extends AbstractTemplate {
    
    public static final String XML_TEMPLATE_ATTRIBUTE_TYPE_MODEL = "model"; //$NON-NLS-1$
    
    public ArchimateModelTemplate() {
    }

    public ArchimateModelTemplate(String id) {
        super(id);
    }

    @Override
    public Image getImage() {
        return IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_MODELS_16);
    }

    @Override
    public String getType() {
        return XML_TEMPLATE_ATTRIBUTE_TYPE_MODEL;
    }
}
