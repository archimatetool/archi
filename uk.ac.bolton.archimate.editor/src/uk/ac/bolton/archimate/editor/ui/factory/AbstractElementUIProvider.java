/*******************************************************************************
 * Copyright (c) 2012 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.ui.factory;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;



/**
 * Abstract Element UI Provider
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractElementUIProvider implements IElementUIProvider {
    
    @Override
    public Image getImage(EObject instance) {
        return getImage();
    }
    
    @Override
    public String getDefaultShortName() {
        return getDefaultName();
    }
    
    @Override
    public Color getDefaultColor() {
        return ColorConstants.white;
    }
}
