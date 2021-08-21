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
 * A model-based EditPolicy for <i>components within a </i>container</i>. A
 * model-based EditPolicy only knows about the host's model and the basic
 * operations it supports. A <i>component</i> is anything that is inside a
 * container. By default, ComponentEditPolicy understands being DELETEd from its
 * container, and being ORPHANed from its container. Subclasses can add support
 * to handle additional behavior specific to the model.
 * <P>
 * ORPHAN is forwarded to the <i>parent</i> EditPart for it to handle.
 * <P>
 * DELETE is also forwarded to the <i>parent</i> EditPart, but subclasses may
 * also contribute to the delete by overriding
 * {@link #createDeleteCommand(GroupRequest)}.
 * <P>
 * This EditPolicy is not a
 * {@link org.eclipse.gef.editpolicies.GraphicalEditPolicy}, and should not be
 * used to show feedback or interact with the host's visuals in any way.
 * <P>
 * This EditPolicy should not be used with
 * {@link org.eclipse.gef.ConnectionEditPart}. Connections do not really have a
 * parent; use {@link ConnectionEditPolicy}.
 * 
 * @since 2.0
 */
public abstract class ComponentEditPolicy extends AbstractEditPolicy {

    /**
     * Override to contribute to the component's being deleted.
     * 
     * @param deleteRequest
     *            the DeleteRequest
     * @return Command <code>null</code> or a contribution to the delete
     */
    protected Command createDeleteCommand(GroupRequest deleteRequest) {
        return null;
    }

    /**
     * Factors the incoming Request into ORPHANs and DELETEs.
     * 
     * @see org.eclipse.gef.EditPolicy#getCommand(Request)
     */
    @Override
    public Command getCommand(Request request) {
        if (REQ_ORPHAN.equals(request.getType()))
            return getOrphanCommand();
        if (REQ_DELETE.equals(request.getType()))
            return getDeleteCommand((GroupRequest) request);
        return null;
    }

    /**
     * Calls and returns {@link #createDeleteCommand(GroupRequest)}. This method
     * is here for historical reasons and used to perform additional function.
     * 
     * @param request
     *            the DeleteRequest
     * @return a delete command
     */
    protected Command getDeleteCommand(GroupRequest request) {
        return createDeleteCommand(request);
    }

    /**
     * Returns the command contribution for orphaning this component from its
     * container. By default, ORPHAN is redispatched to the host's parent as an
     * ORPHAN_CHILDREN Request. The parents contribution is then returned.
     * 
     * @return the contribution obtained from the host's parent.
     */
    protected Command getOrphanCommand() {
        GroupRequest req = new GroupRequest(REQ_ORPHAN_CHILDREN);
        req.setEditParts(getHost());
        return getHost().getParent().getCommand(req);
    }

}
