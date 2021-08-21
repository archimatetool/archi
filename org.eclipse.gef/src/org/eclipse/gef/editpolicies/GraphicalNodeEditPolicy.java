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

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.ConnectionRouter;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.DropRequest;
import org.eclipse.gef.requests.ReconnectRequest;

/**
 * A GraphicalNodeEditPolicy is responsible for creating and reconnecting
 * connections graphically.
 * 
 * Created on :Nov 11, 2002
 * 
 * @author hudsonr
 * @since 2.0
 */
public abstract class GraphicalNodeEditPolicy extends GraphicalEditPolicy {

    /**
     * the current FeedbackHelper
     */
    protected FeedbackHelper feedbackHelper;
    /**
     * The connection feedback displayed during creates
     */
    protected Connection connectionFeedback;

    /**
     * Returns a connection to be used as feeback during creates.
     * 
     * @param req
     *            the operation being performed
     * @return a connection to use as feedback
     */
    protected Connection createDummyConnection(Request req) {
        return new PolylineConnection();
    }

    /**
     * @see org.eclipse.gef.EditPolicy#deactivate()
     */
    @Override
    public void deactivate() {
        if (connectionFeedback != null) {
            removeFeedback(connectionFeedback);
            feedbackHelper = null;
            connectionFeedback = null;
        }
        super.deactivate();
    }

    /**
     * Erases connection feedback if necessary. Frees unused fields.
     * 
     * @param request
     *            the CreateConnectionRequest
     */
    protected void eraseCreationFeedback(CreateConnectionRequest request) {
        if (connectionFeedback != null) {
            removeFeedback(connectionFeedback);
            feedbackHelper = null;
            connectionFeedback = null;
        }
    }

    /**
     * Calls {@link #eraseCreationFeedback(CreateConnectionRequest)} when
     * appropriate.
     * 
     * @see org.eclipse.gef.EditPolicy#eraseSourceFeedback(Request)
     */
    @Override
    public void eraseSourceFeedback(Request request) {
        if (REQ_CONNECTION_END.equals(request.getType()))
            eraseCreationFeedback((CreateConnectionRequest) request);
    }

    /**
     * Override to erase target feedback. Does nothing by default.
     * 
     * @param request
     *            the DropRequest
     */
    protected void eraseTargetConnectionFeedback(DropRequest request) {
    }

    /**
     * Calls {@link #eraseTargetConnectionFeedback(DropRequest)} when
     * appropriate.
     * 
     * @see org.eclipse.gef.EditPolicy#eraseTargetFeedback(Request)
     */
    @Override
    public void eraseTargetFeedback(Request request) {
        if (REQ_CONNECTION_START.equals(request.getType())
                || REQ_CONNECTION_END.equals(request.getType())
                || REQ_RECONNECT_SOURCE.equals(request.getType())
                || REQ_RECONNECT_TARGET.equals(request.getType()))
            eraseTargetConnectionFeedback((DropRequest) request);
    }

    /**
     * Factors the request into one of four abstract methods. Subclasses must
     * implement these methods.
     * 
     * @see org.eclipse.gef.EditPolicy#getCommand(Request)
     */
    @Override
    public Command getCommand(Request request) {
        if (REQ_CONNECTION_START.equals(request.getType()))
            return getConnectionCreateCommand((CreateConnectionRequest) request);
        if (REQ_CONNECTION_END.equals(request.getType()))
            return getConnectionCompleteCommand((CreateConnectionRequest) request);
        if (REQ_RECONNECT_TARGET.equals(request.getType()))
            return getReconnectTargetCommand((ReconnectRequest) request);
        if (REQ_RECONNECT_SOURCE.equals(request.getType()))
            return getReconnectSourceCommand((ReconnectRequest) request);

        return null;
    }

    /**
     * Returns the Command that will create the connection. This is second part
     * of creation. {@link CreateConnectionRequest#getStartCommand()} is used
     * here to obtain the contribution from the EditPart from which the User
     * started the <i>creation</i>.
     * 
     * @param request
     *            the CreateConnectionRequest
     * @return the complete command to create a connection
     */
    protected abstract Command getConnectionCompleteCommand(
            CreateConnectionRequest request);

    /**
     * Returns the command that represents the first half of creating a
     * connection. In case the first half of the connection creation was
     * successful (i.e. the returned start command is executable), the target
     * edit part is then responsible of creating a Command that represents the
     * entire creation. In case the target edit part needs to refer to the start
     * command to achieve this, the start command may be registered on the
     * passed in create request (see
     * {@link CreateConnectionRequest#setStartCommand(Command)}) before
     * returning it here.
     * 
     * @param request
     *            the CreateConnectionRequest
     * @see #getConnectionCompleteCommand(CreateConnectionRequest)
     * @return a Command representing half of a connection creation
     */
    protected abstract Command getConnectionCreateCommand(
            CreateConnectionRequest request);

    /**
     * Returns the ConnectionRouter for the creation feedback's connection.
     * 
     * @param request
     *            the create request
     * @return a connection router
     * @since 3.2
     */
    protected ConnectionRouter getDummyConnectionRouter(
            CreateConnectionRequest request) {
        return ((ConnectionLayer) getLayer(LayerConstants.CONNECTION_LAYER))
                .getConnectionRouter();
    }

    /**
     * Returns the FeedbackHelper that is ready to use. The feedback helper must
     * be configured with the connection that will be used to display feedback,
     * and that connection must be added to the appropriate layer in the
     * diagram.
     * 
     * @param request
     *            the CreateConnectionRequest
     * @return a FeedbackHelper
     */
    protected FeedbackHelper getFeedbackHelper(CreateConnectionRequest request) {
        if (feedbackHelper == null) {
            feedbackHelper = new FeedbackHelper();
            Point p = request.getLocation();
            connectionFeedback = createDummyConnection(request);
            connectionFeedback
                    .setConnectionRouter(getDummyConnectionRouter(request));
            connectionFeedback
                    .setSourceAnchor(getSourceConnectionAnchor(request));
            feedbackHelper.setConnection(connectionFeedback);
            addFeedback(connectionFeedback);
            feedbackHelper.update(null, p);
        }
        return feedbackHelper;
    }

    /**
     * Returns the <code>Command</code> to reconnect a connection's
     * <i>target</i> end to the host.
     * 
     * @param request
     *            the ReconnectRequest
     * @return a Command
     */
    protected abstract Command getReconnectTargetCommand(
            ReconnectRequest request);

    /**
     * Returns the <code>Command</code> to reconnect a connection's
     * <i>source</i> end to the host.
     * 
     * @param request
     *            the ReconnectRequest
     * @return a Command
     */
    protected abstract Command getReconnectSourceCommand(
            ReconnectRequest request);

    /**
     * Called during the display of creation feedback to snap the feedback to
     * the nearest source ConnectionAnchor.
     * 
     * @param request
     *            CreateConnectionRequest
     * @return <code>null</code> or the nearest source ConnectionAnchor
     */
    protected ConnectionAnchor getSourceConnectionAnchor(
            CreateConnectionRequest request) {
        EditPart source = request.getSourceEditPart();
        return source instanceof NodeEditPart ? ((NodeEditPart) source)
                .getSourceConnectionAnchor(request) : null;
    }

    /**
     * Called during the display of creation feedback to snap the feedback to
     * the nearest target ConnectionAnchor.
     * 
     * @param request
     *            CreateConnectionRequest
     * @return <code>null</code> or the nearest target ConnectionAnchor
     */
    protected ConnectionAnchor getTargetConnectionAnchor(
            CreateConnectionRequest request) {
        EditPart target = request.getTargetEditPart();
        return target instanceof NodeEditPart ? ((NodeEditPart) target)
                .getTargetConnectionAnchor(request) : null;
    }

    /**
     * Returns the <i>host</i> for the appropriate <code>Requests</code>.
     * Returns <code>null</code> otherwise.
     * 
     * @see org.eclipse.gef.EditPolicy#getTargetEditPart(Request)
     */
    @Override
    public EditPart getTargetEditPart(Request request) {
        if (REQ_CONNECTION_START.equals(request.getType())
                || REQ_CONNECTION_END.equals(request.getType())
                || REQ_RECONNECT_SOURCE.equals(request.getType())
                || REQ_RECONNECT_TARGET.equals(request.getType()))
            return getHost();
        return null;
    }

    /**
     * Shows feedback during a creation.
     * 
     * @param request
     *            CreateConnectionRequest
     */
    protected void showCreationFeedback(CreateConnectionRequest request) {
        FeedbackHelper helper = getFeedbackHelper(request);
        Point p = new Point(request.getLocation());
        helper.update(getTargetConnectionAnchor(request), p);
    }

    /**
     * calls {@link #showCreationFeedback(CreateConnectionRequest)} when
     * appropriate.
     * 
     * @see org.eclipse.gef.EditPolicy#showSourceFeedback(Request)
     */
    @Override
    public void showSourceFeedback(Request request) {
        if (REQ_CONNECTION_END.equals(request.getType()))
            showCreationFeedback((CreateConnectionRequest) request);
    }

    /**
     * Override to show target connection feedback. Does nothing by default.
     * 
     * @param request
     *            the DropRequest
     */
    protected void showTargetConnectionFeedback(DropRequest request) {
    }

    /**
     * Calls {@link #showTargetConnectionFeedback(DropRequest)} when
     * appropriate.
     * 
     * @see org.eclipse.gef.EditPolicy#showTargetFeedback(Request)
     */
    @Override
    public void showTargetFeedback(Request request) {
        if (REQ_CONNECTION_START.equals(request.getType())
                || REQ_CONNECTION_END.equals(request.getType())
                || REQ_RECONNECT_SOURCE.equals(request.getType())
                || REQ_RECONNECT_TARGET.equals(request.getType()))
            showTargetConnectionFeedback((DropRequest) request);
    }

}
