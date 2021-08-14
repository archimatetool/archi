/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import com.archimatetool.editor.diagram.figures.IDiagramModelObjectFigure;
import com.archimatetool.model.IDiagramModelArchimateObject;

/**
 * IArchimateFigure
 * 
 * @author Phillip Beauvoir
 */
public interface IArchimateFigure extends IDiagramModelObjectFigure {
    
    /**
     * @return The casted getDiagramModelObject()
     */
    default IDiagramModelArchimateObject getDiagramModelArchimateObject() {
        return (IDiagramModelArchimateObject)getDiagramModelObject();
    }
}
