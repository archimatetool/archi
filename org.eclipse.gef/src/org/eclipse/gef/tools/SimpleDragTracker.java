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

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Cursor;

import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.UnexecutableCommand;

/**
 * A simple drag tracker implementation that does not perform targeting. For
 * example, resizing a shape or bendpoint does not involve any target editpart.
 */
@SuppressWarnings("rawtypes")
public abstract class SimpleDragTracker extends AbstractTool implements
        DragTracker {

    private static final int FLAG_SOURCE_FEEDBACK = AbstractTool.MAX_FLAG << 1;

    /**
     * The maximum bit-mask used as a flag constant. Subclasses should start
     * using the next highest bitmask.
     */
    protected static final int MAX_FLAG = FLAG_SOURCE_FEEDBACK;
    private Request sourceRequest;

    /**
     * Null constructor.
     */
    protected SimpleDragTracker() {
    }

    /**
     * @see org.eclipse.gef.tools.AbstractTool#calculateCursor()
     */
    @Override
    protected Cursor calculateCursor() {
        if (isInState(STATE_INITIAL | STATE_DRAG | STATE_ACCESSIBLE_DRAG))
            return getDefaultCursor();
        return super.calculateCursor();
    }

    /**
     * @see DragTracker#commitDrag()
     */
    @Override
    public void commitDrag() {
        eraseSourceFeedback();
        performDrag();
        setState(STATE_TERMINAL);
    }

    /**
     * Creates and returns a new Request that is used during the drag.
     * 
     * @return a new source request
     */
    protected Request createSourceRequest() {
        return new Request();
    }

    /**
     * @see org.eclipse.gef.Tool#deactivate()
     */
    @Override
    public void deactivate() {
        eraseSourceFeedback();
        sourceRequest = null;
        super.deactivate();
    }

    /**
     * Show the source drag feedback for the drag occurring within the viewer.
     */
    protected void eraseSourceFeedback() {
        if (!isShowingFeedback())
            return;
        setFlag(FLAG_SOURCE_FEEDBACK, false);
        List editParts = getOperationSet();
        for (int i = 0; i < editParts.size(); i++) {
            EditPart editPart = (EditPart) editParts.get(i);
            editPart.eraseSourceFeedback(getSourceRequest());
        }
    }

    /**
     * Returns the request for the source of the drag, creating it if necessary.
     * 
     * @return the source request
     */
    protected Request getSourceRequest() {
        if (sourceRequest == null)
            sourceRequest = createSourceRequest();
        return sourceRequest;
    }

    /**
     * Looks for button 1, and goes into the drag state. Any other button is
     * invalid input.
     * 
     * @see org.eclipse.gef.tools.AbstractTool#handleButtonDown(int)
     */
    @Override
    protected boolean handleButtonDown(int button) {
        if (button != 1) {
            setState(STATE_INVALID);
            handleInvalidInput();
        } else
            stateTransition(STATE_INITIAL, STATE_DRAG);
        return true;
    }

    /**
     * If dragging is in progress, cleans up feedback and calls performDrag().
     * 
     * @see org.eclipse.gef.tools.AbstractTool#handleButtonUp(int)
     */
    @Override
    protected boolean handleButtonUp(int button) {
        if (stateTransition(STATE_DRAG_IN_PROGRESS, STATE_TERMINAL)) {
            eraseSourceFeedback();
            performDrag();
        }
        return true;
    }

    /**
     * @see org.eclipse.gef.tools.AbstractTool#handleDragInProgress()
     */
    @Override
    protected boolean handleDragInProgress() {
        if (isInDragInProgress()) {
            updateSourceRequest();
            showSourceFeedback();
            setCurrentCommand(getCommand());
        }
        return true;
    }

    /**
     * Transitions Drag to Drag in progress state.
     * 
     * @see org.eclipse.gef.tools.AbstractTool#handleDragStarted()
     */
    @Override
    protected boolean handleDragStarted() {
        return stateTransition(STATE_DRAG, STATE_DRAG_IN_PROGRESS);
    }

    /**
     * Called when the mouse and/or keyboard input is invalid.
     * 
     * @return <code>true</code>
     */
    @Override
    protected boolean handleInvalidInput() {
        eraseSourceFeedback();
        setCurrentCommand(UnexecutableCommand.INSTANCE);
        return true;
    }

    /**
     * Looks for keys which are used during accessible drags.
     * 
     * @see org.eclipse.gef.tools.AbstractTool#handleKeyDown(org.eclipse.swt.events.KeyEvent)
     */
    @Override
    protected boolean handleKeyDown(KeyEvent e) {
        if (acceptArrowKey(e)) {
            accStepIncrement();
            if (stateTransition(STATE_INITIAL,
                    STATE_ACCESSIBLE_DRAG_IN_PROGRESS))
                setStartLocation(getLocation());
            switch (e.keyCode) {
            case SWT.ARROW_DOWN:
                placeMouseInViewer(getLocation().getTranslated(0, accGetStep()));
                break;
            case SWT.ARROW_UP:
                placeMouseInViewer(getLocation()
                        .getTranslated(0, -accGetStep()));
                break;
            case SWT.ARROW_RIGHT:
                int stepping = accGetStep();
                if (isCurrentViewerMirrored())
                    stepping = -stepping;
                placeMouseInViewer(getLocation().getTranslated(stepping, 0));
                break;
            case SWT.ARROW_LEFT:
                int step = -accGetStep();
                if (isCurrentViewerMirrored())
                    step = -step;
                placeMouseInViewer(getLocation().getTranslated(step, 0));
                break;
            }
            return true;
        }
        return false;
    }

    /**
     * @see org.eclipse.gef.tools.AbstractTool#handleKeyUp(org.eclipse.swt.events.KeyEvent)
     */
    @Override
    protected boolean handleKeyUp(KeyEvent e) {
        if (acceptArrowKey(e)) {
            accStepReset();
            return true;
        }
        return false;
    }

    /**
     * Returns <code>true</code> if feedback is being shown.
     * 
     * @return <code>true</code> if feedback is showing
     */
    protected boolean isShowingFeedback() {
        return getFlag(FLAG_SOURCE_FEEDBACK);
    }

    /**
     * Called once the drag has been interpreted. This is where the real work of
     * the drag is carried out. By default, the current command is executed.
     */
    protected void performDrag() {
        executeCurrentCommand();
    }

    /**
     * Show the source drag feedback for the drag occurring within the viewer.
     */
    protected void showSourceFeedback() {
        List editParts = getOperationSet();
        for (int i = 0; i < editParts.size(); i++) {
            EditPart editPart = (EditPart) editParts.get(i);
            editPart.showSourceFeedback(getSourceRequest());
        }
        setFlag(FLAG_SOURCE_FEEDBACK, true);
    }

    /**
     * Updates the source request.
     */
    protected void updateSourceRequest() {
        getSourceRequest().setType(getCommandName());
    }

}
