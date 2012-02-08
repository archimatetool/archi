/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.commands;

import uk.ac.bolton.archimate.editor.model.commands.EObjectFeatureCommand;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IDiagramModelConnection;


/**
 * Connection Line Color Command
 *
 * @author Phillip Beauvoir
 */
public class ConnectionLineColorCommand extends EObjectFeatureCommand {
    
    public ConnectionLineColorCommand(IDiagramModelConnection connection, String rgb) {
        super(Messages.ConnectionLineColorCommand_0, connection, IArchimatePackage.Literals.DIAGRAM_MODEL_CONNECTION__LINE_COLOR, rgb);
    }
}