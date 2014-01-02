/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.editparts.connections;

import org.eclipse.draw2d.IFigure;

import com.archimatetool.editor.diagram.figures.connections.UsedByConnectionFigure;



/**
 * Used By Connection Edit Part
 * 
 * @author Phillip Beauvoir
 */
public class UsedByConnectionEditPart extends AbstractArchimateConnectionEditPart {
	
    @Override
    protected IFigure createFigure() {
		return new UsedByConnectionFigure(getModel());
	}
	
}
