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
 * Connection Line Type Command
 *
 * @author Phillip Beauvoir
 */
public class ConnectionLineTypeCommand extends EObjectFeatureCommand {
    
    public ConnectionLineTypeCommand(IDiagramModelConnection connection, int lineType) {
        super(Messages.ConnectionLineTypeCommand_0, connection, IArchimatePackage.Literals.DIAGRAM_MODEL_CONNECTION__TYPE, lineType);
    }
}