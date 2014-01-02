/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.diagram.editparts.connections;

import org.eclipse.draw2d.IFigure;

import uk.ac.bolton.archimate.editor.diagram.figures.connections.AccessConnectionFigure;


/**
 * Access Connection Edit Part
 * 
 * @author Phillip Beauvoir
 */
public class AccessConnectionEditPart extends AbstractArchimateConnectionEditPart {
	
    @Override
    protected IFigure createFigure() {
		return new AccessConnectionFigure(getModel());
	}
	
}
