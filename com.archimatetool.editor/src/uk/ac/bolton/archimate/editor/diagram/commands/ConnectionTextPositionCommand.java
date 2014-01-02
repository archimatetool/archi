/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.diagram.commands;

import uk.ac.bolton.archimate.editor.model.commands.EObjectFeatureCommand;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IDiagramModelConnection;


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