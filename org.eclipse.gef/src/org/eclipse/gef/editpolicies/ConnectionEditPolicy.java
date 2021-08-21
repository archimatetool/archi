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
package org.eclipse.gef.editpolicies;

import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.GroupRequest;

/**
 * A model-based EditPolicy for connections. A model-based EditPolicy only knows
 * about the host's model and the basic operations it supports. By default,
 * ConnectionEditPolicy understands only DELETE. Subclasses can add support to
 * handle additional behavior specific to the model.
 * <P>
 * This EditPolicy is not a
 * {@link org.eclipse.gef.editpolicies.GraphicalEditPolicy}, and should not be
 * used to show feedback or interact with the host's visuals in any way.
 * 
 * @since 2.0
 */
public abstract class ConnectionEditPolicy extends AbstractEditPolicy {

    /**
     * @see org.eclipse.gef.EditPolicy#getCommand(Request)
     */
    @Override
    public Command getCommand(Request request) {
        if (REQ_DELETE.equals(request.getType()))
            return getDeleteCommand((GroupRequest) request);
        return null;
    }

    /**
     * Subclasses should implement to return the Command to delete the
     * connection.
     * 
     * @param request
     *            the DeleteRequest
     * @return the Command to delete the connection
     */
    protected abstract Command getDeleteCommand(GroupRequest request);

}
