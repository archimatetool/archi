/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.diagram.figures.connections;

import uk.ac.bolton.archimate.model.IDiagramModelArchimateConnection;


/**
 * Association Connection Figure
 * 
 * @author Phillip Beauvoir
 */
public class AssociationConnectionFigure extends AbstractArchimateConnectionFigure {
	
    public AssociationConnectionFigure(IDiagramModelArchimateConnection connection) {
        super(connection);
    }
	
    @Override
    protected void setFigureProperties() {
    }
}
