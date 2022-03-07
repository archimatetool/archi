/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.connections;

import com.archimatetool.model.IDiagramModelArchimateConnection;

/**
 * IArchimateConnectionFigure
 * 
 * @author Phillip Beauvoir
 */
public interface IArchimateConnectionFigure extends IDiagramConnectionFigure {
    
    /**
     * @return The casted getModelConnection()
     */
    default IDiagramModelArchimateConnection getDiagramModelArchimateConnection() {
        return (IDiagramModelArchimateConnection)getModelConnection();
    }
}
