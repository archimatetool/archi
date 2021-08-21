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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.AncestorListener;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.ConnectionLocator;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Polygon;
import org.eclipse.draw2d.geometry.PointList;

import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.handles.ConnectionEndpointHandle;
import org.eclipse.gef.requests.ReconnectRequest;

/**
 * A selection handle policy for placing handles at the two ends of a
 * ConnectionEditPart. All ConnectionEditParts should have one of these, even if
 * the ends of the connection aren't draggable, because this is the primary
 * SelectionEditPolicy for showing focus.
 * <P>
 * A connection can receive focus but not selection by pressing
 * <code>Control+/</code> on the keyboard.
 * 
 * @since 2.0
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ConnectionEndpointEditPolicy extends SelectionHandlesEditPolicy {

    private ConnectionAnchor originalAnchor;
    private FeedbackHelper feedbackHelper;
    private ConnectionFocus focus;

    class ConnectionFocus extends Polygon implements PropertyChangeListener {
        AncestorListener ancestorListener = new AncestorListener.Stub() {
            @Override
            public void ancestorMoved(IFigure ancestor) {
                revalidate();
            }
        };

        ConnectionFocus() {
            setFill(false);
            setForegroundColor(ColorConstants.white);
            setXOR(true);
            setOutline(true);
        }

        @Override
        public void addNotify() {
            super.addNotify();
            getConnection().addPropertyChangeListener(
                    Connection.PROPERTY_POINTS, this);
            getConnection().addAncestorListener(ancestorListener);
        }

        @Override
        protected void outlineShape(Graphics g) {
            g.setLineDash(new int[] { 1, 1 });
            super.outlineShape(g);
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            revalidate();
        }

        @Override
        public void removeNotify() {
            getConnection().removePropertyChangeListener(
                    Connection.PROPERTY_POINTS, this);
            getConnection().removeAncestorListener(ancestorListener);
            super.removeNotify();
        }

        @Override
        public void validate() {
            if (isValid())
                return;
            PointList points = getConnection().getPoints().getCopy();
            getConnection().translateToAbsolute(points);
            points = StrokePointList.strokeList(points, 5);
            translateToRelative(points);
            setPoints(points);
        }
    }

    /**
     * @see org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy#createSelectionHandles()
     */
    @Override
    protected List createSelectionHandles() {
        List list = new ArrayList();
        list.add(new ConnectionEndpointHandle((ConnectionEditPart) getHost(),
                ConnectionLocator.SOURCE));
        list.add(new ConnectionEndpointHandle((ConnectionEditPart) getHost(),
                ConnectionLocator.TARGET));
        return list;
    }

    /**
     * Erases connection move feedback. This method is called when a
     * ReconnectRequest is received.
     * 
     * @param request
     *            the reconnect request.
     */
    protected void eraseConnectionMoveFeedback(ReconnectRequest request) {
        if (originalAnchor == null)
            return;
        if (request.isMovingStartAnchor())
            getConnection().setSourceAnchor(originalAnchor);
        else
            getConnection().setTargetAnchor(originalAnchor);
        originalAnchor = null;
        feedbackHelper = null;
    }

    /**
     * @see org.eclipse.gef.EditPolicy#eraseSourceFeedback(org.eclipse.gef.Request)
     */
    @Override
    public void eraseSourceFeedback(Request request) {
        if (REQ_RECONNECT_TARGET.equals(request.getType())
                || REQ_RECONNECT_SOURCE.equals(request.getType()))
            eraseConnectionMoveFeedback((ReconnectRequest) request);
    }

    /**
     * @see org.eclipse.gef.EditPolicy#getCommand(org.eclipse.gef.Request)
     */
    @Override
    public Command getCommand(Request request) {
        return null;
    }

    /**
     * Convenience method for obtaining the host's <code>Connection</code>
     * figure.
     * 
     * @return the Connection figure
     */
    protected Connection getConnection() {
        return (Connection) ((GraphicalEditPart) getHost()).getFigure();
    }

    /**
     * Lazily creates and returns the feedback helper for the given request. The
     * helper will be configured as either moving the source or target end of
     * the connection.
     * 
     * @param request
     *            the reconnect request
     * @return the feedback helper
     */
    protected FeedbackHelper getFeedbackHelper(ReconnectRequest request) {
        if (feedbackHelper == null) {
            feedbackHelper = new FeedbackHelper();
            feedbackHelper.setConnection(getConnection());
            feedbackHelper.setMovingStartAnchor(request.isMovingStartAnchor());
        }
        return feedbackHelper;
    }

    /**
     * Hides the focus indicator. The focus indicator is a dotted outline around
     * the connection.
     * 
     * @see #showFocus()
     * @see org.eclipse.gef.editpolicies.SelectionEditPolicy#hideFocus()
     */
    @Override
    protected void hideFocus() {
        if (focus != null) {
            removeFeedback(focus);
            focus = null;
        }
    }

    /**
     * Shows or updates connection move feedback. Called whenever a show
     * feedback request is received for reconnection.
     * 
     * @param request
     *            the reconnect request
     */
    protected void showConnectionMoveFeedback(ReconnectRequest request) {
        NodeEditPart node = null;
        if (request.getTarget() instanceof NodeEditPart)
            node = (NodeEditPart) request.getTarget();
        if (originalAnchor == null) {
            if (request.isMovingStartAnchor())
                originalAnchor = getConnection().getSourceAnchor();
            else
                originalAnchor = getConnection().getTargetAnchor();
        }
        ConnectionAnchor anchor = null;
        if (node != null) {
            if (request.isMovingStartAnchor())
                anchor = node.getSourceConnectionAnchor(request);
            else
                anchor = node.getTargetConnectionAnchor(request);
        }
        FeedbackHelper helper = getFeedbackHelper(request);
        helper.update(anchor, request.getLocation());
    }

    /**
     * Shows focus around the connection.
     * 
     * @see org.eclipse.gef.editpolicies.SelectionEditPolicy#showFocus()
     */
    @Override
    protected void showFocus() {
        if (focus == null) {
            focus = new ConnectionFocus();
            addFeedback(focus);
        }
    }

    /**
     * @see org.eclipse.gef.EditPolicy#showSourceFeedback(org.eclipse.gef.Request)
     */
    @Override
    public void showSourceFeedback(Request request) {
        if (REQ_RECONNECT_SOURCE.equals(request.getType())
                || REQ_RECONNECT_TARGET.equals(request.getType()))
            showConnectionMoveFeedback((ReconnectRequest) request);
    }

}
