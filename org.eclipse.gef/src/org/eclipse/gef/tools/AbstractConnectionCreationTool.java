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

import org.eclipse.swt.graphics.Cursor;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartListener;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.SharedCursors;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.CreationFactory;

/**
 * The base implementation for tools which create a connection. A connection is
 * a link between two existing GraphicalEditParts.
 * <P>
 * A connection creation tool uses a {@link CreateConnectionRequest} to perform
 * the creation. This request is sent to both graphical editparts which serve as
 * the "nodes" at each end of the connection. The first node clicked on is the
 * source. The source is asked for a <code>Command</code> that represents
 * creating the first half of the connection. This command is then passed to the
 * target editpart, which is reponsible for creating the final Command that is
 * executed.
 * 
 * @author hudsonr
 */
public class AbstractConnectionCreationTool extends TargetingTool {

    /**
     * The state which indicates that the connection creation has begun. This
     * means that the source of the connection has been identified, and the user
     * is still to determine the target.
     */
    protected static final int STATE_CONNECTION_STARTED = AbstractTool.MAX_STATE << 1;
    /**
     * The max state.
     */
    protected static final int MAX_STATE = STATE_CONNECTION_STARTED;

    private static final int FLAG_SOURCE_FEEDBACK = TargetingTool.MAX_FLAG << 1;

    /**
     * The max flag.
     */
    protected static final int MAX_FLAG = FLAG_SOURCE_FEEDBACK;

    private EditPart connectionSource;
    private CreationFactory factory;
    private EditPartViewer viewer;

    private EditPartListener.Stub deactivationListener = new EditPartListener.Stub() {
        @Override
        public void partDeactivated(EditPart editpart) {
            handleSourceDeactivated();
        }
    };

    /**
     * The default constructor
     */
    public AbstractConnectionCreationTool() {
        setDefaultCursor(SharedCursors.CURSOR_PLUG);
        setDisabledCursor(SharedCursors.CURSOR_PLUG_NOT);
    }

    /**
     * Constructs a new abstract creation tool with the given creation factory.
     * 
     * @param factory
     *            the creation factory
     */
    public AbstractConnectionCreationTool(CreationFactory factory) {
        this();
        setFactory(factory);
    }

    /**
     * @see org.eclipse.gef.tools.AbstractTool#calculateCursor()
     */
    @Override
    protected Cursor calculateCursor() {
        if (isInState(STATE_INITIAL)) {
            if (getCurrentCommand() != null)
                return getDefaultCursor();
        }
        return super.calculateCursor();
    }

    /**
     * @see org.eclipse.gef.tools.TargetingTool#createTargetRequest()
     */
    @Override
    protected Request createTargetRequest() {
        CreateRequest req = new CreateConnectionRequest();
        req.setFactory(getFactory());
        return req;
    }

    /**
     * Erases feedback and sets fields to <code>null</code>.
     * 
     * @see org.eclipse.gef.Tool#deactivate()
     */
    @Override
    public void deactivate() {
        eraseSourceFeedback();
        setConnectionSource(null);
        super.deactivate();
        setState(STATE_TERMINAL);
        viewer = null;
    }

    /**
     * Asks the source editpart to erase connection creation feedback.
     */
    protected void eraseSourceFeedback() {
        if (!isShowingSourceFeedback())
            return;
        setFlag(FLAG_SOURCE_FEEDBACK, false);
        if (connectionSource != null) {
            connectionSource.eraseSourceFeedback(getSourceRequest());
        }
    }

    /**
     * @see org.eclipse.gef.tools.AbstractTool#getCommandName()
     */
    @Override
    protected String getCommandName() {
        if (isInState(STATE_CONNECTION_STARTED
                | STATE_ACCESSIBLE_DRAG_IN_PROGRESS))
            return REQ_CONNECTION_END;
        else
            return REQ_CONNECTION_START;
    }

    /**
     * @see org.eclipse.gef.tools.AbstractTool#getDebugName()
     */
    @Override
    protected String getDebugName() {
        return "Connection Creation Tool";//$NON-NLS-1$
    }

    /**
     * @see org.eclipse.gef.tools.AbstractTool#getDebugNameForState(int)
     */
    @Override
    protected String getDebugNameForState(int s) {
        if (s == STATE_CONNECTION_STARTED
                || s == STATE_ACCESSIBLE_DRAG_IN_PROGRESS)
            return "Connection Started";//$NON-NLS-1$
        return super.getDebugNameForState(s);
    }

    /**
     * Returns the creation factory that will be used with the create connection
     * request.
     * 
     * @return the creation factory
     */
    protected CreationFactory getFactory() {
        return factory;
    }

    /**
     * Returns the request sent to the source node. The source node receives the
     * same request that is used with the target node. The only difference is
     * that at that time the request will be typed as
     * {@link RequestConstants#REQ_CONNECTION_START}.
     * 
     * @return the request used with the source node editpart
     */
    protected Request getSourceRequest() {
        return getTargetRequest();
    }

    /**
     * When the button is first pressed, the source node and its command
     * contribution are determined and locked in. After that time, the tool will
     * be looking for the target node to complete the connection
     * 
     * @see org.eclipse.gef.tools.AbstractTool#handleButtonDown(int)
     * @param button
     *            which button is pressed
     * @return <code>true</code> if the button down was processed
     */
    @Override
    protected boolean handleButtonDown(int button) {
        if (isInState(STATE_INITIAL) && button == 1) {
            updateTargetRequest();
            updateTargetUnderMouse();
            setConnectionSource(getTargetEditPart());
            Command command = getCommand();
            ((CreateConnectionRequest) getTargetRequest())
                    .setSourceEditPart(getTargetEditPart());
            if (command != null) {
                setState(STATE_CONNECTION_STARTED);
                setCurrentCommand(command);
                viewer = getCurrentViewer();
            }
        }

        if (isInState(STATE_INITIAL) && button != 1) {
            setState(STATE_INVALID);
            handleInvalidInput();
        }
        return true;
    }

    /**
     * Unloads or resets the tool if the state is in the terminal or invalid
     * state.
     * 
     * @see org.eclipse.gef.tools.AbstractTool#handleButtonUp(int)
     */
    @Override
    protected boolean handleButtonUp(int button) {
        if (isInState(STATE_TERMINAL | STATE_INVALID))
            handleFinished();
        return true;
    }

    /**
     * @see org.eclipse.gef.tools.AbstractTool#handleCommandStackChanged()
     */
    @Override
    protected boolean handleCommandStackChanged() {
        if (!isInState(STATE_INITIAL)) {
            if (getCurrentInput().isMouseButtonDown(1))
                setState(STATE_INVALID);
            else
                setState(STATE_INITIAL);
            handleInvalidInput();
            return true;
        }
        return false;
    }

    /**
     * Method that is called when the gesture to create the connection has been
     * received. Subclasses may extend or override this method to do additional
     * creation setup, such as prompting the user to choose an option about the
     * connection being created. Returns <code>true</code> to indicate that the
     * connection creation succeeded.
     * 
     * @return <code>true</code> if the connection creation was performed
     */
    protected boolean handleCreateConnection() {
        eraseSourceFeedback();
        Command endCommand = getCommand();
        setCurrentCommand(endCommand);
        executeCurrentCommand();
        return true;
    }

    /**
     * @see org.eclipse.gef.tools.AbstractTool#handleDrag()
     */
    @Override
    protected boolean handleDrag() {
        if (isInState(STATE_CONNECTION_STARTED))
            return handleMove();
        return false;
    }

    /**
     * @see org.eclipse.gef.tools.AbstractTool#handleDragInProgress()
     */
    @Override
    protected boolean handleDragInProgress() {
        if (isInState(STATE_ACCESSIBLE_DRAG_IN_PROGRESS))
            return handleMove();
        return false;
    }

    /**
     * @see org.eclipse.gef.tools.AbstractTool#handleFocusLost()
     */
    @Override
    protected boolean handleFocusLost() {
        if (isInState(STATE_CONNECTION_STARTED)) {
            eraseSourceFeedback();
            eraseTargetFeedback();
            setState(STATE_INVALID);
            handleFinished();
        }
        return super.handleFocusLost();
    }

    /**
     * @see org.eclipse.gef.tools.TargetingTool#handleHover()
     */
    @Override
    protected boolean handleHover() {
        if (isInState(STATE_CONNECTION_STARTED))
            updateAutoexposeHelper();
        return true;
    }

    /**
     * @see org.eclipse.gef.tools.TargetingTool#handleInvalidInput()
     */
    @Override
    protected boolean handleInvalidInput() {
        eraseSourceFeedback();
        setConnectionSource(null);
        return super.handleInvalidInput();
    }

    /**
     * @see org.eclipse.gef.tools.AbstractTool#handleMove()
     */
    @Override
    protected boolean handleMove() {
        if (isInState(STATE_CONNECTION_STARTED) && viewer != getCurrentViewer())
            return false;
        if (isInState(STATE_CONNECTION_STARTED | STATE_INITIAL
                | STATE_ACCESSIBLE_DRAG_IN_PROGRESS)) {
            updateTargetRequest();
            updateTargetUnderMouse();
            showSourceFeedback();
            showTargetFeedback();
            setCurrentCommand(getCommand());
        }
        return true;
    }

    /**
     * Called if the source editpart is deactivated for some reason during the
     * creation process. For example, the user performs an Undo while in the
     * middle of creating a connection, which undoes a prior command which
     * created the source.
     */
    protected void handleSourceDeactivated() {
        setState(STATE_INVALID);
        handleInvalidInput();
        handleFinished();
    }

    /**
     * Returns <code>true</code> if feedback is being shown.
     * 
     * @return <code>true</code> if showing source feedback
     */
    protected boolean isShowingSourceFeedback() {
        return getFlag(FLAG_SOURCE_FEEDBACK);
    }

    /**
     * Sets the source editpart for the creation
     * 
     * @param source
     *            the source editpart node
     */
    protected void setConnectionSource(EditPart source) {
        if (connectionSource != null)
            connectionSource.removeEditPartListener(deactivationListener);
        connectionSource = source;
        if (connectionSource != null)
            connectionSource.addEditPartListener(deactivationListener);
    }

    /**
     * Sets the creation factory used in the request.
     * 
     * @param factory
     *            the factory
     */
    public void setFactory(CreationFactory factory) {
        this.factory = factory;
    }

    /**
     * Sends a show feedback request to the source editpart and sets the
     * feedback flag.
     */
    protected void showSourceFeedback() {
        if (connectionSource != null) {
            connectionSource.showSourceFeedback(getSourceRequest());
        }
        setFlag(FLAG_SOURCE_FEEDBACK, true);
    }

    /**
     * @see org.eclipse.gef.tools.TargetingTool#updateTargetRequest()
     */
    @Override
    protected void updateTargetRequest() {
        CreateConnectionRequest request = (CreateConnectionRequest) getTargetRequest();
        request.setType(getCommandName());
        request.setLocation(getLocation());
    }

}
