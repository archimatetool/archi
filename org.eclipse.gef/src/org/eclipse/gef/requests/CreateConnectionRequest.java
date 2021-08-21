/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.requests;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;

/**
 * A Request to create a new Connection.
 */
public class CreateConnectionRequest extends CreateRequest implements
        TargetRequest {

    private Command startCommand;
    private EditPart targetEditPart;
    private EditPart sourceEditPart;

    /**
     * Returns the EditPart that the source end of the connection should be
     * connected to.
     * 
     * @return the source EditPart
     */
    public EditPart getSourceEditPart() {
        return sourceEditPart;
    }

    /**
     * Returns the EditPart that the target end of the connection should be
     * connected to.
     * 
     * @return the target EditPart
     */
    public EditPart getTargetEditPart() {
        return targetEditPart;
    }

    /**
     * Returns the start command. This command is only used to pass on
     * information to the target EditPart so it can create the final command.
     * 
     * @return the command
     */
    public Command getStartCommand() {
        return startCommand;
    }

    /**
     * Sets the source of the Connection to the given EditPart.
     * 
     * @param part
     *            the source EditPart
     */
    public void setSourceEditPart(EditPart part) {
        sourceEditPart = part;
    }

    /**
     * Sets the target of the Connection to the given EditPart.
     * 
     * @param part
     *            the target EditPart
     */
    @Override
    public void setTargetEditPart(EditPart part) {
        targetEditPart = part;
    }

    /**
     * Sets the start command. This command is only used to pass on information
     * to the target EditPart so it can create the final command.
     * 
     * @param c
     *            the command
     */
    public void setStartCommand(Command c) {
        startCommand = c;
    }

}
