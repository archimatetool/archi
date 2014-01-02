/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.editparts;

import org.eclipse.gef.GraphicalEditPart;

import com.archimatetool.model.IDiagramModelArchimateObject;



/**
 * IArchimateEditPart
 * 
 * @author Phillip Beauvoir
 */
public interface IArchimateEditPart extends GraphicalEditPart {

    IDiagramModelArchimateObject getModel();
    
}
