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
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.DirectEditRequest;

/**
 * Shows DirectEdit feedback and creates the Command to perform a "direct edit".
 * <I>Direct Edit</I> is when the User is editing a property of an EditPart
 * directly (as opposed to in the Properties View) in the Viewer using a
 * {@link org.eclipse.jface.viewers.CellEditor}. This EditPolicy is typically
 * installed using {@link org.eclipse.gef.EditPolicy#DIRECT_EDIT_ROLE}.
 * 
 * @author hudsonr
 * @since 2.0
 */
public abstract class DirectEditPolicy extends GraphicalEditPolicy {

    private boolean showing;

    /**
     * @see org.eclipse.gef.EditPolicy#eraseSourceFeedback(Request)
     */
    @Override
    public void eraseSourceFeedback(Request request) {
        if (RequestConstants.REQ_DIRECT_EDIT == request.getType())
            eraseDirectEditFeedback((DirectEditRequest) request);
    }

    /**
     * If feedback is being shown, this method calls
     * {@link #revertOldEditValue(DirectEditRequest)}.
     * 
     * @param request
     *            the DirectEditRequest
     */
    protected void eraseDirectEditFeedback(DirectEditRequest request) {
        if (showing) {
            revertOldEditValue(request);
            showing = false;
        }
    }

    /**
     * @see org.eclipse.gef.EditPolicy#getCommand(Request)
     */
    @Override
    public Command getCommand(Request request) {
        if (RequestConstants.REQ_DIRECT_EDIT == request.getType())
            return getDirectEditCommand((DirectEditRequest) request);
        return null;
    }

    /**
     * Returns the <code>Command</code> to perform the direct edit.
     * 
     * @param request
     *            the DirectEditRequest
     * @return the command to perform the direct edit
     */
    protected abstract Command getDirectEditCommand(DirectEditRequest request);

    /**
     * Helps erase feedback by reverting the original edit value. The rule when
     * using GEF is that all feedback is removed before changes to the model are
     * made. By default, the host is sent
     * {@link org.eclipse.gef.EditPart#refresh()}, which should cause it to
     * refresh all properties.
     * 
     * Subclasses can override this method to perform a more specific revert.
     * 
     * @see #storeOldEditValue(DirectEditRequest)
     * @param request
     *            the DirectEditRequest
     */
    protected void revertOldEditValue(DirectEditRequest request) {
        getHost().refresh();
    }

    /**
     * @see org.eclipse.gef.EditPolicy#showSourceFeedback(Request)
     */
    @Override
    public void showSourceFeedback(Request request) {
        if (RequestConstants.REQ_DIRECT_EDIT == request.getType())
            showDirectEditFeedback((DirectEditRequest) request);
    }

    /**
     * Pushes the original edit value if necessary, and shows feedback.
     * 
     * @param request
     *            the DirectEditRequest
     */
    protected void showDirectEditFeedback(DirectEditRequest request) {
        if (!showing) {
            storeOldEditValue(request);
            showing = true;
        }
        showCurrentEditValue(request);
    }

    /**
     * Override to show the current direct edit value in the host's Figure.
     * Although the CellEditor will probably cover the figure's display of this
     * value, updating the figure will cause its preferred size to reflect the
     * new value.
     * 
     * @param request
     *            the DirectEditRequest
     */
    protected abstract void showCurrentEditValue(DirectEditRequest request);

    /**
     * Called to remember the old value before direct edit feedback begins.
     * After feedback is over, {@link #revertOldEditValue(DirectEditRequest)} is
     * called to undo the changes done by feedback. By default, this method
     * nothing.
     * 
     * @param request
     *            the DirectEditRequest
     */
    protected void storeOldEditValue(DirectEditRequest request) {
    }

    /**
     * Returns <code>true</code> for {@link RequestConstants#REQ_DIRECT_EDIT}.
     * {@link org.eclipse.gef.ui.actions.DirectEditAction} will determine
     * enablement based on whether the selected EditPart understands
     * "direct edit".
     * 
     * @see org.eclipse.gef.EditPolicy#understandsRequest(Request)
     */
    @Override
    public boolean understandsRequest(Request request) {
        if (RequestConstants.REQ_DIRECT_EDIT.equals(request.getType()))
            return true;
        return super.understandsRequest(request);
    }

}
