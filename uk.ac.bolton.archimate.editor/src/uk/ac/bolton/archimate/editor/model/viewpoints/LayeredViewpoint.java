/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.model.viewpoints;

import org.eclipse.emf.ecore.EClass;

/**
 * Layered Viewpoint
 * 
 * @author Phillip Beauvoir
 */
public class LayeredViewpoint extends AbstractViewpoint {
    
    public static final int INDEX = 14;
    
    @Override
    public String getName() {
        return "Layered";
    }

    @Override
    public int getIndex() {
        return INDEX;
    }
    
    @Override
    public EClass[] getAllowedTypes() {
        return null;
    }
}