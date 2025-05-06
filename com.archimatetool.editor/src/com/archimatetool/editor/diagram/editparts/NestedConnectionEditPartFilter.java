/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.editparts;

import org.eclipse.gef.EditPart;

import com.archimatetool.editor.model.DiagramModelUtils;
import com.archimatetool.model.IDiagramModelConnection;


/**
 * Nested Connection Filter for EditParts
 * 
 * @author Phillip Beauvoir
 */
public class NestedConnectionEditPartFilter implements IConnectionEditPartFilter {
    
    @Override
    public boolean isConnectionVisible(EditPart editPart, IDiagramModelConnection connection) {
        return !DiagramModelUtils.shouldBeHiddenConnection(connection);
    }
}