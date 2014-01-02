/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.commands;

import com.archimatetool.editor.model.commands.EObjectFeatureCommand;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelConnection;



/**
 * Connection Text Position Command
 *
 * @author Phillip Beauvoir
 */
public class ConnectionTextPositionCommand extends EObjectFeatureCommand {
    
    public ConnectionTextPositionCommand(IDiagramModelConnection connection, int position) {
        super(Messages.ConnectionTextPositionCommand_0, connection, IArchimatePackage.Literals.FONT_ATTRIBUTE__TEXT_POSITION, position);
    }
}