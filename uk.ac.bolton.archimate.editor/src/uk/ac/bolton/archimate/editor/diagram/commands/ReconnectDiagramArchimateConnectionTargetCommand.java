/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.commands;

import uk.ac.bolton.archimate.editor.diagram.policies.ConnectionManager;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateConnection;
import uk.ac.bolton.archimate.model.IDiagramModelObject;

/**
 * Reconnect Target Connection Command
 * 
 * @author Phillip Beauvoir
 */
public class ReconnectDiagramArchimateConnectionTargetCommand
extends ReconnectDiagramConnectionTargetCommand {

    public ReconnectDiagramArchimateConnectionTargetCommand(IDiagramModelArchimateConnection connection, IDiagramModelObject newTarget) {
        super(connection, newTarget);
    }

    @Override
    public boolean canExecute() {
        if(super.canExecute()) {
            return ConnectionManager.isValidConnection(fSource, fNewTarget,
                    ((IDiagramModelArchimateConnection)fConnection).getRelationship().eClass());
        }
        return false;
    }

}
