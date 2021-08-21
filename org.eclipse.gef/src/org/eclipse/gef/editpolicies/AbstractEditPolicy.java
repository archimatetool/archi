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

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;

/**
 * The default implementation of {@link EditPolicy}.
 * <P>
 * Since this is the default implementation of an interface, this document deals
 * with proper sub-classing. This class is not the API. For documentation on
 * proper usage of the public API, see the documentation for the interface
 * itself: {@link EditPolicy}.
 */
public abstract class AbstractEditPolicy implements EditPolicy,
        RequestConstants {

    private EditPart host;

    /**
     * Does nothing by default.
     * 
     * @see org.eclipse.gef.EditPolicy#activate()
     */
    @Override
    public void activate() {
    }

    /**
     * Does nothing by default.
     * 
     * @see org.eclipse.gef.EditPolicy#deactivate()
     */
    @Override
    public void deactivate() {
    }

    /**
     * This method will log the message to GEF's trace/debug system if the
     * corrseponding flag for FEEDBACK is set to true.
     * 
     * @param message
     *            the String to log
     * @deprecated in 3.1 This method will be removed in future releases.
     */
    protected final void debugFeedback(String message) {
    }

    /**
     * Does nothing by default.
     * 
     * @see org.eclipse.gef.EditPolicy#eraseSourceFeedback(Request)
     */
    @Override
    public void eraseSourceFeedback(Request request) {
    }

    /**
     * Does nothing by default.
     * 
     * @see org.eclipse.gef.EditPolicy#eraseTargetFeedback(Request)
     */
    @Override
    public void eraseTargetFeedback(Request request) {
    }

    /**
     * Returns <code>null</code> by default. <code>null</code> is used to
     * indicate that the EditPolicy does not contribute to the specified
     * <code>Request</code>.
     * 
     * @see org.eclipse.gef.EditPolicy#getCommand(Request)
     */
    @Override
    public Command getCommand(Request request) {
        return null;
    }

    /**
     * @see org.eclipse.gef.EditPolicy#getHost()
     */
    @Override
    public EditPart getHost() {
        return host;
    }

    /**
     * Returns <code>null</code> by default. <code>null</code> indicates that
     * this policy is unable to determine the target for the specified
     * <code>Request</code>.
     * 
     * @see org.eclipse.gef.EditPolicy#getTargetEditPart(Request)
     */
    @Override
    public EditPart getTargetEditPart(Request request) {
        return null;
    }

    /**
     * @see org.eclipse.gef.EditPolicy#setHost(EditPart)
     */
    @Override
    public void setHost(EditPart host) {
        this.host = host;
    }

    /**
     * Does nothing by default.
     * 
     * @see org.eclipse.gef.EditPolicy#showSourceFeedback(Request)
     */
    @Override
    public void showSourceFeedback(Request request) {
    }

    /**
     * Does nothing by default.
     * 
     * @see org.eclipse.gef.EditPolicy#showTargetFeedback(Request)
     */
    @Override
    public void showTargetFeedback(Request request) {
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        String c = getClass().getName();
        c = c.substring(c.lastIndexOf('.') + 1);
        if (getHost() != null) {
            return getHost().toString() + "." + c; //$NON-NLS-1$
        } else {
            return c + " (no host for EditPolicy set yet)"; //$NON-NLS-1$
        }
    }

    /**
     * Returns <code>false</code> by default.
     * 
     * @see org.eclipse.gef.EditPolicy#understandsRequest(Request)
     */
    @Override
    public boolean understandsRequest(Request req) {
        return false;
    }

}