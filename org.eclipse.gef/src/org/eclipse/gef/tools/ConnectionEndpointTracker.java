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
package org.eclipse.gef.tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Cursor;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.AccessibleAnchorProvider;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.Tool;
import org.eclipse.gef.requests.ReconnectRequest;

/**
 * A DragTracker that moves the endpoint of a connection to another location.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ConnectionEndpointTracker extends TargetingTool implements
        DragTracker {

    private static final int FLAG_SOURCE_FEEBBACK = TargetingTool.MAX_FLAG << 1;
    /** The max flag */
    protected static final int MAX_FLAG = FLAG_SOURCE_FEEBBACK;

    private String commandName;
    private List exclusionSet;

    private ConnectionEditPart connectionEditPart;

    /**
     * Constructs a new ConnectionEndpointTracker for the given
     * ConnectionEditPart.
     * 
     * @param cep
     *            the ConnectionEditPart
     */
    public ConnectionEndpointTracker(ConnectionEditPart cep) {
        setConnectionEditPart(cep);
        setDisabledCursor(Cursors.NO);
    }

    /**
     * Returns a custom "plug" cursor if this tool is in the initial, drag or
     * accessible drag state. Otherwise defers to <code>super</code>.
     * 
     * @return the cursor
     */
    @Override
    protected Cursor calculateCursor() {
        if (isInState(STATE_INITIAL | STATE_DRAG | STATE_ACCESSIBLE_DRAG))
            return getDefaultCursor();
        return super.calculateCursor();
    }

    /**
     * Erases source and target feedback and executes the current command.
     * 
     * @see DragTracker#commitDrag()
     */
    @Override
    public void commitDrag() {
        eraseSourceFeedback();
        eraseTargetFeedback();
        executeCurrentCommand();
    }

    /**
     * Creates the target request, a {@link ReconnectRequest}.
     * 
     * @return the target request
     */
    @Override
    protected Request createTargetRequest() {
        ReconnectRequest request = new ReconnectRequest(getCommandName());
        request.setConnectionEditPart(getConnectionEditPart());
        return request;
    }

    /**
     * Erases feedback and sets the viewer's focus to <code>null</code>. This
     * will remove any focus rectangles that were painted to show the new target
     * or source edit part.
     * 
     * @see Tool#deactivate()
     */
    @Override
    public void deactivate() {
        eraseSourceFeedback();
        getCurrentViewer().setFocus(null);
        super.deactivate();
    }

    /**
     * Erases the source feedback.
     */
    protected void eraseSourceFeedback() {
        if (!getFlag(FLAG_SOURCE_FEEBBACK))
            return;
        setFlag(FLAG_SOURCE_FEEBBACK, false);
        getConnectionEditPart().eraseSourceFeedback(getTargetRequest());
    }

    /**
     * @see AbstractTool#getCommandName()
     */
    @Override
    protected String getCommandName() {
        return commandName;
    }

    /**
     * Returns the ConnectionEditPart's figure.
     * 
     * @return the connection
     */
    protected Connection getConnection() {
        return (Connection) getConnectionEditPart().getFigure();
    }

    /**
     * Returns the ConnectionEditPart.
     * 
     * @return the ConnectionEditPart
     */
    protected ConnectionEditPart getConnectionEditPart() {
        return connectionEditPart;
    }

    /**
     * @see AbstractTool#getDebugName()
     */
    @Override
    protected String getDebugName() {
        return "Connection Endpoint Tool";//$NON-NLS-1$
    }

    /**
     * @see org.eclipse.gef.tools.TargetingTool#getExclusionSet()
     */
    @Override
    protected Collection getExclusionSet() {
        if (exclusionSet == null) {
            exclusionSet = new ArrayList();
            exclusionSet.add(getConnection());
        }
        return exclusionSet;
    }

    /**
     * If currently in the drag-in-progress state, it goes into the terminal
     * state erases feedback and executes the current command.
     * 
     * @see org.eclipse.gef.tools.AbstractTool#handleButtonUp(int)
     */
    @Override
    protected boolean handleButtonUp(int button) {
        if (stateTransition(STATE_DRAG_IN_PROGRESS, STATE_TERMINAL)) {
            eraseSourceFeedback();
            eraseTargetFeedback();
            executeCurrentCommand();
        }
        return true;
    }

    /**
     * Updates the request and the mouse target, asks to show feedback, and gets
     * the current command.
     * 
     * @return <code>true</code>
     */
    @Override
    protected boolean handleDragInProgress() {
        updateTargetRequest();
        updateTargetUnderMouse();
        showSourceFeedback();
        showTargetFeedback();
        setCurrentCommand(getCommand());
        return true;
    }

    /**
     * @see org.eclipse.gef.tools.AbstractTool#handleDragStarted()
     */
    @Override
    protected boolean handleDragStarted() {
        stateTransition(STATE_INITIAL, STATE_DRAG_IN_PROGRESS);
        return false;
    }

    /**
     * @see org.eclipse.gef.tools.TargetingTool#handleHover()
     */
    @Override
    protected boolean handleHover() {
        if (isInDragInProgress())
            updateAutoexposeHelper();
        return true;
    }

    /**
     * Processes the arrow keys (to choose a different source or target edit
     * part) and forwardslash and backslash keys (to try to connect to another
     * connection).
     * 
     * @see org.eclipse.gef.tools.AbstractTool#handleKeyDown(org.eclipse.swt.events.KeyEvent)
     */
    @Override
    protected boolean handleKeyDown(KeyEvent e) {
        if (acceptArrowKey(e)) {
            if (stateTransition(STATE_INITIAL,
                    STATE_ACCESSIBLE_DRAG_IN_PROGRESS)) {
                // When the drag first starts, set the focus Part to be one end
                // of the connection
                if (isTarget()) {
                    getCurrentViewer().setFocus(
                            getConnectionEditPart().getTarget());
                    getCurrentViewer().reveal(
                            getConnectionEditPart().getTarget());
                } else {
                    getCurrentViewer().setFocus(
                            getConnectionEditPart().getSource());
                    getCurrentViewer().reveal(
                            getConnectionEditPart().getSource());
                }
            }
            int direction = 0;
            switch (e.keyCode) {
            case SWT.ARROW_DOWN:
                direction = PositionConstants.SOUTH;
                break;
            case SWT.ARROW_UP:
                direction = PositionConstants.NORTH;
                break;
            case SWT.ARROW_RIGHT:
                direction = isCurrentViewerMirrored() ? PositionConstants.WEST
                        : PositionConstants.EAST;
                break;
            case SWT.ARROW_LEFT:
                direction = isCurrentViewerMirrored() ? PositionConstants.EAST
                        : PositionConstants.WEST;
                break;
            }

            boolean consumed = false;
            if (direction != 0 && e.stateMask == 0)
                consumed = navigateNextAnchor(direction);
            if (!consumed) {
                e.stateMask |= SWT.CONTROL;
                e.stateMask &= ~SWT.SHIFT;
                if (getCurrentViewer().getKeyHandler().keyPressed(e)) {
                    navigateNextAnchor(0);
                    return true;
                }
            }
        }
        if (e.character == '/' || e.character == '\\') {
            e.stateMask |= SWT.CONTROL;
            if (getCurrentViewer().getKeyHandler().keyPressed(e)) {
                // Do not try to connect to the same connection being dragged.
                if (getCurrentViewer().getFocusEditPart() != getConnectionEditPart())
                    navigateNextAnchor(0);
                return true;
            }
        }

        return false;
    }

    private boolean isTarget() {
        return getCommandName() == RequestConstants.REQ_RECONNECT_TARGET;
    }

    @SuppressWarnings("deprecation")
    boolean navigateNextAnchor(int direction) {
        EditPart focus = getCurrentViewer().getFocusEditPart();
        AccessibleAnchorProvider provider;
        provider = focus
                .getAdapter(AccessibleAnchorProvider.class);
        if (provider == null)
            return false;

        List list;
        if (isTarget())
            list = provider.getTargetAnchorLocations();
        else
            list = provider.getSourceAnchorLocations();

        Point start = getLocation();
        int distance = Integer.MAX_VALUE;
        Point next = null;
        for (int i = 0; i < list.size(); i++) {
            Point p = (Point) list.get(i);
            if (p.equals(start)
                    || (direction != 0 && (start.getPosition(p) != direction)))
                continue;
            int d = p.getDistanceOrthogonal(start);
            if (d < distance) {
                distance = d;
                next = p;
            }
        }

        if (next != null) {
            placeMouseInViewer(next);
            return true;
        }
        return false;
    }

    /**
     * Sets the command name.
     * 
     * @param newCommandName
     *            the new command name
     */
    public void setCommandName(String newCommandName) {
        commandName = newCommandName;
    }

    /**
     * Sets the connection edit part that is being reconnected.
     * 
     * @param cep
     *            the connection edit part
     */
    public void setConnectionEditPart(ConnectionEditPart cep) {
        this.connectionEditPart = cep;
    }

    /**
     * Asks the ConnectionEditPart to show source feedback.
     */
    protected void showSourceFeedback() {
        getConnectionEditPart().showSourceFeedback(getTargetRequest());
        setFlag(FLAG_SOURCE_FEEBBACK, true);
    }

    /**
     * Updates the request location.
     * 
     * @see org.eclipse.gef.tools.TargetingTool#updateTargetRequest()
     */
    @Override
    protected void updateTargetRequest() {
        ReconnectRequest request = (ReconnectRequest) getTargetRequest();
        Point p = getLocation();
        request.setLocation(p);
    }

}