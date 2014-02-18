/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import com.archimatetool.model.IDiagramModelArchimateObject;





/**
 * Figure that is backed by an IDiagramModelObject
 * 
 * @author Phillip Beauvoir
 */
public interface IDiagramModelArchimateObjectFigure extends IDiagramModelObjectFigure {
    
    /**
     * @return The DiagramModelObject
     */
    IDiagramModelArchimateObject getDiagramModelObject();
    
}
