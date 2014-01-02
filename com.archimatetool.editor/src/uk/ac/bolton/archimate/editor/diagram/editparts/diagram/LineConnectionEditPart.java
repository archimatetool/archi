/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.diagram.editparts.diagram;

import org.eclipse.draw2d.IFigure;

import uk.ac.bolton.archimate.editor.diagram.editparts.connections.AbstractDiagramConnectionEditPart;
import uk.ac.bolton.archimate.editor.diagram.figures.diagram.LineConnectionFigure;


/**
 * Line Connection EditPart
 * 
 * @author Phillip Beauvoir
 */
public class LineConnectionEditPart extends AbstractDiagramConnectionEditPart {

    @Override
    protected IFigure createFigure() {
        return new LineConnectionFigure(getModel());
    }

}
