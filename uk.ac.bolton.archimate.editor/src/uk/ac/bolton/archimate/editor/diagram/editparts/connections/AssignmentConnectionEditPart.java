/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.diagram.editparts.connections;

import org.eclipse.draw2d.IFigure;

import uk.ac.bolton.archimate.editor.diagram.figures.connections.AssignmentConnectionFigure;


/**
 * Assignment Connection Edit Part
 * 
 * @author Phillip Beauvoir
 */
public class AssignmentConnectionEditPart extends AbstractArchimateConnectionEditPart {
	
    @Override
    protected IFigure createFigure() {
		return new AssignmentConnectionFigure(getModel());
	}
	
}
