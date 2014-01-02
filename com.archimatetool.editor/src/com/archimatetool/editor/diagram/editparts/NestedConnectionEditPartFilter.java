/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.editparts;

import org.eclipse.gef.EditPart;

import com.archimatetool.editor.model.DiagramModelUtils;
import com.archimatetool.editor.preferences.ConnectionPreferences;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelConnection;


/**
 * Nested Connection Filter for EditParts
 * 
 * @author Phillip Beauvoir
 */
public class NestedConnectionEditPartFilter implements IConnectionEditPartFilter {
    
    @Override
    public boolean isConnectionVisible(EditPart editPart, IDiagramModelConnection connection) {
        // If the connection is an Archimate type and its target element is an Archimate type
        // and this box contains that box and that box qualifies, don't show the connection
        if(ConnectionPreferences.useNestedConnections() && connection instanceof IDiagramModelArchimateConnection) {
            return !DiagramModelUtils.shouldBeHiddenConnection((IDiagramModelArchimateConnection)connection);
        }
        
        return true;
    }
}