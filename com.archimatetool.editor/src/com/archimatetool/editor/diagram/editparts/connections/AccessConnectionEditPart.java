/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.editparts.connections;

import org.eclipse.draw2d.IFigure;

import com.archimatetool.editor.diagram.figures.connections.AccessConnectionFigure;



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
