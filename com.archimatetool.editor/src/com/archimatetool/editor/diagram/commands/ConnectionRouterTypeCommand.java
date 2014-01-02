/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.commands;

import com.archimatetool.editor.model.commands.EObjectFeatureCommand;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModel;



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