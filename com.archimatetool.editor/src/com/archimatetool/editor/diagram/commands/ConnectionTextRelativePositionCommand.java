/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.commands;

import com.archimatetool.editor.model.commands.FeatureCommand;
import com.archimatetool.model.IDiagramModelConnection;



/**
 * Connection Text Relative Position Command
 *
 * @author Phillip Beauvoir
 */
public class ConnectionTextRelativePositionCommand extends FeatureCommand {
    
    public ConnectionTextRelativePositionCommand(IDiagramModelConnection connection, int position) {
        super(Messages.ConnectionTextRelativePositionCommand_0, connection,
                IDiagramModelConnection.FEATURE_TEXT_RELATIVE_POSITION, position, IDiagramModelConnection.FEATURE_TEXT_RELATIVE_POSITION_DEFAULT);
    }
}