/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.diagram.commands;

import uk.ac.bolton.archimate.editor.model.commands.EObjectFeatureCommand;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IDiagramModel;


/**
 * Connection Router Type Command
 *
 * @author Phillip Beauvoir
 */
public class ConnectionRouterTypeCommand extends EObjectFeatureCommand {
    
    public ConnectionRouterTypeCommand(IDiagramModel diagramModel, int type) {
        super(Messages.ConnectionRouterTypeCommand_0, diagramModel, IArchimatePackage.Literals.DIAGRAM_MODEL__CONNECTION_ROUTER_TYPE, type);
    }
}