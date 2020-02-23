/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.connections;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.geometry.Point;

import com.archimatetool.editor.diagram.figures.ITargetFeedbackFigure;
import com.archimatetool.model.IDiagramModelConnection;


/**
 * Description
 * 
 * @author Phillip Beauvoir
 */
public interface IDiagramConnectionFigure extends Connection, ITargetFeedbackFigure {
    
    // Strategies for drawing the connection label
    int CONNECTION_LABEL_TRANSPARENT = 0;
    int CONNECTION_LABEL_OPAQUE = 1;
    int CONNECTION_LABEL_CLIPPED = 2;

    /**
     * Set the Model Connection
     * @param connection
     */
    void setModelConnection(IDiagramModelConnection connection);
    
    /**
     * @return the Model Connection
     */
    IDiagramModelConnection getModelConnection();

    /**
     * Refresh the visuals for the model
     */
    void refreshVisuals();
    
    /**
     * @param requestLoc
     * @return
     */
    boolean didClickConnectionLabel(Point requestLoc);
    
    /**
     * @return The label figure
     */
    Figure getConnectionLabel();
}
