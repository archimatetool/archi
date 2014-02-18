/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import com.archimatetool.model.IDiagramModelArchimateObject;


/**
 * Figure for an Archimate Figure
 * 
 * @author Phillip Beauvoir
 */
public class AbstractArchimateFigure
extends AbstractTextFlowFigure implements IDiagramModelArchimateObjectFigure {
    
    public AbstractArchimateFigure(IDiagramModelArchimateObject diagramModelObject) {
        super(diagramModelObject);
    }
    
    @Override
    public IDiagramModelArchimateObject getDiagramModelObject() {
        return (IDiagramModelArchimateObject)super.getDiagramModelObject();
    }
}