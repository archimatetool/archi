/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.diagram.figures.connections;

import org.eclipse.draw2d.PolylineDecoration;

import uk.ac.bolton.archimate.model.IDiagramModelArchimateConnection;


/**
 * Used By Connection Figure class
 * 
 * @author Phillip Beauvoir
 */
public class UsedByConnectionFigure extends AbstractArchimateConnectionFigure {
	
    /**
     * @return Decoration to use on Target Node
     */
    public static PolylineDecoration createFigureTargetDecoration() {
        return new PolylineDecoration();
    }

    public UsedByConnectionFigure(IDiagramModelArchimateConnection connection) {
        super(connection);
    }
	
    @Override
    protected void setFigureProperties() {
        setTargetDecoration(createFigureTargetDecoration()); // arrow at target endpoint 
    }
    

}
