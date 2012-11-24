/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.canvas.editparts;

import org.eclipse.draw2d.IFigure;

import uk.ac.bolton.archimate.canvas.figures.CanvasLineConnectionFigure;
import uk.ac.bolton.archimate.canvas.model.ICanvasModelConnection;
import uk.ac.bolton.archimate.editor.diagram.editparts.diagram.LineConnectionEditPart;


/**
 * Canvas Line Connection Edit Part
 * 
 * @author Phillip Beauvoir
 */
public class CanvasLineConnectionEditPart extends LineConnectionEditPart {
    
    @Override
    protected IFigure createFigure() {
        return new CanvasLineConnectionFigure(getModel());
    }

    @Override
    public ICanvasModelConnection getModel() {
        return (ICanvasModelConnection)super.getModel();
    }

}
