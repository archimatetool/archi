/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.connections;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Point;


/**
 * Description
 * 
 * @author Phillip Beauvoir
 */
public interface IDiagramConnectionFigure extends Connection {

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
     * @return
     */
    Label getConnectionLabel();
    
    /**
     * Highlight the connection
     * @param set if true highlight
     */
    void highlight(boolean set);
}
