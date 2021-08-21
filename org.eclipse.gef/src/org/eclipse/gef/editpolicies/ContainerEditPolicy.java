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
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.GroupRequest;

/**
 * An EditPolicy for use with container editparts. This policy can be used to
 * contribute commands to add, create, and orphan requests.
 * 
 * @author hudsonr
 */
public abstract class ContainerEditPolicy extends AbstractEditPolicy {

    /**
     * Override to contribute to add requests.
     * 
     * @param request
     *            the add request
     * @return the command contribution to the add
     */
    protected Command getAddCommand(GroupRequest request) {
        return null;
    }

    /**
     * Override to contribute to clone requests.
     * 
     * @param request
     *            the clone request
     * @return the command contribution to the clone
     */
    protected Command getCloneCommand(ChangeBoundsRequest request) {
        return null;
    }

    /**
     * Overridden to check for add, create, and orphan.
     * 
     * @see org.eclipse.gef.EditPolicy#getCommand(org.eclipse.gef.Request)
     */
    @Override
    public Command getCommand(Request request) {
        if (REQ_CREATE.equals(request.getType()))
            return getCreateCommand((CreateRequest) request);
        if (REQ_ADD.equals(request.getType()))
            return getAddCommand((GroupRequest) request);
        if (REQ_CLONE.equals(request.getType()))
            return getCloneCommand((ChangeBoundsRequest) request);
        if (REQ_ORPHAN_CHILDREN.equals(request.getType()))
            return getOrphanChildrenCommand((GroupRequest) request);
        return null;
    }

    /**
     * Clients must implement to contribute to create requests.
     * 
     * @param request
     *            the create request
     * @return <code>null</code> or a command contribution
     */
    protected abstract Command getCreateCommand(CreateRequest request);

    /**
     * Override to contribute to orphan requests.
     * 
     * @param request
     *            the orphan request
     * @return a command contribution for the orphan
     */
    protected Command getOrphanChildrenCommand(GroupRequest request) {
        return null;
    }

}
