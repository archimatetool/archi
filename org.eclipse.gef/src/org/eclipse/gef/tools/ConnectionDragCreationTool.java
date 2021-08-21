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

import org.eclipse.gef.requests.CreationFactory;

/**
 * A connection creation tool that implements DragTracker. Using this tool,
 * connections are created by clicking on the source edit part, dragging the
 * mouse to the target edit part and releasing the mouse. A
 * {@link org.eclipse.gef.NodeEditPart} might return this drag tracker if the
 * mouse is located over an appropriate anchor.
 */
public class ConnectionDragCreationTool extends AbstractConnectionCreationTool
        implements org.eclipse.gef.DragTracker {

    /**
     * Default constructor.
     */
    public ConnectionDragCreationTool() {
    }

    /**
     * Constructs a new ConnectionDragCreationTool with the given factory.
     * 
     * @param factory
     *            the creation factory
     */
    public ConnectionDragCreationTool(CreationFactory factory) {
        setFactory(factory);
    }

    /**
     * Calls super and sets the current state to {@link AbstractTool#STATE_DRAG}
     * .
     * 
     * @see AbstractConnectionCreationTool#handleButtonDown(int)
     */
    @Override
    protected boolean handleButtonDown(int button) {
        super.handleButtonDown(button);
        setState(STATE_DRAG);
        return true;
    }

    /**
     * If the connection has started, the button up event attempts to complete
     * the connection.
     * 
     * @param button
     *            the button that was released
     * @return <code>true</code> if this button up event was processed
     */
    @Override
    protected boolean handleButtonUp(int button) {
        if (isInState(STATE_CONNECTION_STARTED))
            handleCreateConnection();
        setState(STATE_TERMINAL);
        handleFinished();
        return true;
    }

    /**
     * When the threshold is passed, transition to CONNECTION_STARTED.
     * 
     * @return <code>true</code> if the state transition completed successfully
     */
    @Override
    protected boolean handleDragStarted() {
        return stateTransition(STATE_DRAG, STATE_CONNECTION_STARTED);
    }

    /**
     * Overridden so that nothing happens when this tool is used as a tracker.
     * If it is the primary tool, then this method behaves normally.
     * 
     * @see AbstractTool#handleFinished()
     */
    @Override
    protected void handleFinished() {
        if (getDomain().getActiveTool() == this)
            super.handleFinished();
    }

}
