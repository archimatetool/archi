/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.editparts;

import org.eclipse.gef.ConnectionEditPart;

import com.archimatetool.model.IDiagramModelArchimateConnection;

/**
 * IArchimate Relationship EditPart
 * 
 * @author Phillip Beauvoir
 */
public interface IArchimateRelationshipEditPart extends ConnectionEditPart {

    IDiagramModelArchimateConnection getModel();
    
}
