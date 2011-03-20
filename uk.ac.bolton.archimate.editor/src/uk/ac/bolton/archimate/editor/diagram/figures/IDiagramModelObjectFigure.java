/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.figures;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;




/**
 * Figure that is backed by an IDiagramModelObject
 * 
 * @author Phillip Beauvoir
 */
public interface IDiagramModelObjectFigure extends IFigure {
    
    /**
     * Refresh the visual figure according to the model
     */
    void refreshVisuals();
    
    /**
     * @return The figure used to show text
     */
    IFigure getTextControl();
    
    /**
     * Dispose of any resources
     */
    void dispose();
    
    /**
     * @return The default Size for this object
     */
    Dimension getDefaultSize();
}
