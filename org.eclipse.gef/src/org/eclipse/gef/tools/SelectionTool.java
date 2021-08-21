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

import java.lang.ref.WeakReference;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;

import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.AccessibleHandleProvider;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.Handle;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.requests.LocationRequest;
import org.eclipse.gef.requests.SelectionRequest;

/**
 * Tool to select and manipulate figures. A selection tool is in one of three
 * states, e.g., background selection, figure selection, handle manipulation.
 * The different states are handled by different child tools.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class SelectionTool extends TargetingTool {

    private static final int FLAG_HOVER_FEEDBACK = TargetingTool.MAX_FLAG << 1;
    /** Max flag */
    protected static final int MAX_FLAG = FLAG_HOVER_FEEDBACK;

    /** Traverse handle state */
    protected static final int STATE_TRAVERSE_HANDLE = AbstractTool.MAX_STATE << 1;
    /** Max state */
    protected static final int MAX_STATE = STATE_TRAVERSE_HANDLE;

    private int handleIndex;
    private DragTracker dragTracker;
    private LocationRequest hoverRequest;

    private WeakReference cachedHandlePart;

    /**
     * Default constructor.
     */
    public SelectionTool() {
    }

    private boolean acceptTraverseHandle(KeyEvent e) {
        return (e.character == '.' || e.character == '>')
                && isInState(STATE_INITIAL | STATE_ACCESSIBLE_DRAG
                        | STATE_ACCESSIBLE_DRAG_IN_PROGRESS)
                && ((e.stateMask & (SWT.ALT | SWT.CONTROL)) == 0);
    }

    /**
     * Creates the hover request (a {@link LocationRequest}) and sets its type
     * to {@link RequestConstants#REQ_SELECTION_HOVER}.
     */
    protected void createHoverRequest() {
        hoverRequest = new LocationRequest();
        hoverRequest.setType(RequestConstants.REQ_SELECTION_HOVER);
    }

    /**
     * Creates a {@link SelectionRequest} for the target request.
     * 
     * @see TargetingTool#createTargetRequest()
     */
    @Override
    protected Request createTargetRequest() {
        SelectionRequest request = new SelectionRequest();
        request.setType(getCommandName());
        return request;
    }

    /**
     * Deactivates the tool. This method is called whenever the user switches to
     * another tool. Use this method to do some clean-up when the tool is
     * switched. Sets the drag tracker to <code>null</code>.
     */
    @Override
    public void deactivate() {
        setDragTracker(null); // deactivates the current drag tracker
        super.deactivate();
    }

    /**
     * Erases the hover feedback by calling
     * {@link EditPart#eraseTargetFeedback(Request)}.
     */
    protected void eraseHoverFeedback() {
        if (getTargetEditPart() == null)
            return;
        if (getTargetHoverRequest() == null)
            return;
        getTargetEditPart().eraseTargetFeedback(getTargetHoverRequest());
    }

    /**
     * @see AbstractTool#getCommandName()
     */
    @Override
    protected String getCommandName() {
        return REQ_SELECTION;
    }

    /**
     * @see AbstractTool#getDebugName()
     */
    @Override
    protected String getDebugName() {
        return "Selection Tool";//$NON-NLS-1$
    }

    /**
     * Returns the current drag tracker.
     * 
     * @return the drag tracker
     */
    protected DragTracker getDragTracker() {
        return dragTracker;
    }

    private EditPart getLastHandleProvider() {
        if (cachedHandlePart == null)
            return null;
        EditPart part = (EditPart) cachedHandlePart.get();
        if (cachedHandlePart.isEnqueued())
            return null;
        return part;
    }

    /**
     * Returns a new Conditional that evaluates to <code>true</code> if the
     * queried edit part's {@link EditPart#isSelectable()} method returns
     * <code>true</code>.
     * 
     * @see TargetingTool#getTargetingConditional()
     */
    @Override
    protected EditPartViewer.Conditional getTargetingConditional() {
        return new EditPartViewer.Conditional() {
            @Override
            public boolean evaluate(EditPart editpart) {
                EditPart targetEditPart = editpart
                        .getTargetEditPart(getTargetRequest());
                return targetEditPart != null && targetEditPart.isSelectable();
            }
        };
    }

    /**
     * Returns the target hover request. If <code>null</code>, it will be
     * created via {@link #createHoverRequest()}.
     * 
     * @return the hover request
     */
    protected Request getTargetHoverRequest() {
        if (hoverRequest == null)
            createHoverRequest();
        return hoverRequest;
    }

    /**
     * If there is a {@link Handle} under the mouse, this method sets the drag
     * tracker returned from the handle. If there's an {@link EditPart} under
     * the mouse, this method sets the drag tracker returned from the edit part.
     * 
     * @see AbstractTool#handleButtonDown(int)
     */
    @Override
    protected boolean handleButtonDown(int button) {
        if (!stateTransition(STATE_INITIAL, STATE_DRAG)) {
            resetHover();
            return true;
        }
        resetHover();
        EditPartViewer viewer = getCurrentViewer();
        Point p = getLocation();

        if (getDragTracker() != null)
            getDragTracker().deactivate();

        if (viewer instanceof GraphicalViewer) {
            Handle handle = ((GraphicalViewer) viewer).findHandleAt(p);
            if (handle != null) {
                setDragTracker(handle.getDragTracker());
                return true;
            }
        }
        updateTargetRequest();
        ((SelectionRequest) getTargetRequest()).setLastButtonPressed(button);
        updateTargetUnderMouse();
        EditPart editpart = getTargetEditPart();
        if (editpart != null) {
            setDragTracker(editpart.getDragTracker(getTargetRequest()));
            lockTargetEditPart(editpart);
            return true;
        }
        return false;
    }

    /**
     * Resets this tool when the last button is released.
     * 
     * @see AbstractTool#handleButtonUp(int)
     */
    @Override
    protected boolean handleButtonUp(int button) {
        if (getCurrentInput().isAnyButtonDown())
            return false;
        ((SelectionRequest) getTargetRequest()).setLastButtonPressed(0);
        setDragTracker(null);
        setState(STATE_INITIAL);
        unlockTargetEditPart();
        return true;
    }

    /**
     * @see AbstractTool#handleCommandStackChanged()
     */
    @Override
    protected boolean handleCommandStackChanged() {
        if (getDragTracker() == null)
            return super.handleCommandStackChanged();
        return false;
    }

    /**
     * Sets the drag tracker to <code>null</code> and goes into the initial
     * state when focus is lost.
     * 
     * @see AbstractTool#handleFocusLost()
     */
    @Override
    protected boolean handleFocusLost() {
        if (isInState(STATE_ACCESSIBLE_DRAG | STATE_ACCESSIBLE_DRAG_IN_PROGRESS
                | STATE_DRAG | STATE_DRAG_IN_PROGRESS)) {
            if (getDragTracker() != null)
                setDragTracker(null);
            setState(STATE_INITIAL);
            return true;
        }
        return false;
    }

    /**
     * Called when the mouse hovers. Calls {@link #showHoverFeedback()}.
     * 
     * @see AbstractTool#handleHover()
     */
    @Override
    protected boolean handleHover() {
        setHoverActive(true);
        showHoverFeedback();
        return true;
    }

    /**
     * Called when the mouse hover stops (i.e. the mouse moves or a button is
     * clicked). Calls {@link #eraseHoverFeedback()}.
     * 
     * @see TargetingTool#handleHoverStop()
     */
    @Override
    protected boolean handleHoverStop() {
        eraseHoverFeedback();
        return true;
    }

    /**
     * Processes key down events. Specifically, arrow keys for moving edit
     * parts, the ESC key for aborting a drag, the period '.' key for traversing
     * handles, and the ENTER key for committing a drag. If none of these keys
     * were pressed and the current viewer has a {@link KeyHandler}, it calls
     * {@link KeyHandler#keyPressed(KeyEvent)}.
     * 
     * @see AbstractTool#handleKeyDown(KeyEvent)
     */
    @Override
    protected boolean handleKeyDown(KeyEvent e) {
        resetHover();

        if (acceptArrowKey(e))
            if (stateTransition(STATE_ACCESSIBLE_DRAG,
                    STATE_ACCESSIBLE_DRAG_IN_PROGRESS))
                return true;

        if (acceptAbort(e)) {
            if (getDragTracker() != null)
                setDragTracker(null);
            if (isInState(STATE_TRAVERSE_HANDLE | STATE_ACCESSIBLE_DRAG
                    | STATE_ACCESSIBLE_DRAG_IN_PROGRESS))
                placeMouseInViewer(getStartLocation().getTranslated(6, 6));
            setState(STATE_INITIAL);
            setLastHandleProvider(null);
            return true;
        }

        if (acceptTraverseHandle(e)) {
            if (isInState(STATE_ACCESSIBLE_DRAG_IN_PROGRESS))
                if (getDragTracker() != null)
                    getDragTracker().commitDrag();
            if (isInState(STATE_ACCESSIBLE_DRAG
                    | STATE_ACCESSIBLE_DRAG_IN_PROGRESS)) {
                setDragTracker(null);
                getCurrentViewer().flush();
            }
            if (!handleTraverseHandle(e))
                setState(STATE_INITIAL);
            return true;
        }

        if (acceptDragCommit(e)) {
            if (getDragTracker() != null)
                getDragTracker().commitDrag();
            setDragTracker(null);
            setState(STATE_INITIAL);
            handleIndex--;
            placeMouseInViewer(getLocation().getTranslated(6, 6));
            return true;
        }

        if (isInState(STATE_INITIAL)) {
            if (getCurrentViewer().getKeyHandler() != null)
                return getCurrentViewer().getKeyHandler().keyPressed(e);
        }

        return false;
    }

    /**
     * If in the initial state and the viewer has a {@link KeyHandler}, calls
     * {@link KeyHandler#keyReleased(KeyEvent)} sending it the given key event.
     * 
     * @see AbstractTool#handleKeyUp(KeyEvent)
     */
    @Override
    protected boolean handleKeyUp(KeyEvent e) {
        if (isInState(STATE_INITIAL)
                && getCurrentViewer().getKeyHandler() != null
                && getCurrentViewer().getKeyHandler().keyReleased(e))
            return true;

        return false;
    }

    /**
     * If in the initial state, updates the request and the mouse target and
     * asks to show target feedback. If in the traverse handle state, finds the
     * next handle, moves the mouse cursor to that handle, and gets a drag
     * tracker from the handle.
     * 
     * @see AbstractTool#handleMove()
     */
    @Override
    protected boolean handleMove() {
        if (stateTransition(STATE_ACCESSIBLE_DRAG, STATE_INITIAL))
            setDragTracker(null);
        if (isInState(STATE_INITIAL)) {
            updateTargetRequest();
            updateTargetUnderMouse();
            showTargetFeedback();
            return true;
        } else if (isInState(STATE_TRAVERSE_HANDLE)) {
            EditPartViewer viewer = getCurrentViewer();
            if (viewer instanceof GraphicalViewer) {
                Handle handle = ((GraphicalViewer) viewer)
                        .findHandleAt(getLocation());
                if (handle != null) {
                    setState(STATE_ACCESSIBLE_DRAG);
                    setStartLocation(getLocation());
                    setDragTracker(handle.getDragTracker());
                    return true;
                } else {
                    setState(STATE_INITIAL);
                }
            }
        }
        return false;
    }

    /**
     * If there's a drag tracker, calls handleNativeDragFinished() on the drag
     * tracker and then sets the drag tracker to <code>null</code>.
     * 
     * @see AbstractTool#handleNativeDragFinished(DragSourceEvent)
     */
    @Override
    public boolean handleNativeDragFinished(DragSourceEvent event) {
        if (getDragTracker() != null)
            getDragTracker().nativeDragFinished(event, getCurrentViewer());
        setDragTracker(null);
        unlockTargetEditPart();
        return true;
    }

    /**
     * If there's a drag tracker, calls nativeDragStarted() on the drag tracker.
     * 
     * @see AbstractTool#handleNativeDragStarted(DragSourceEvent)
     */
    @Override
    public boolean handleNativeDragStarted(DragSourceEvent event) {
        if (getDragTracker() != null)
            getDragTracker().nativeDragStarted(event, getCurrentViewer());
        setState(STATE_INITIAL);
        return true;
    }

    private boolean handleTraverseHandle(KeyEvent e) {
        EditPart focus = getCurrentViewer().getFocusEditPart();
        if (focus.getSelected() == EditPart.SELECTED_NONE)
            return false;
        getCurrentViewer().reveal(focus);

        AccessibleHandleProvider provider;
        provider = focus
                .getAdapter(AccessibleHandleProvider.class);
        if (provider == null
                || provider.getAccessibleHandleLocations().isEmpty())
            return false;

        /*
         * At this point, a handle provider with 1 or more handles has been
         * obtained
         */
        setState(STATE_TRAVERSE_HANDLE);
        List locations = provider.getAccessibleHandleLocations();
        // Goto next index, wrapping if necessary
        if (e.character == '.')
            handleIndex = (++handleIndex) % locations.size();
        else
            handleIndex = (--handleIndex + locations.size()) % locations.size();
        if (getLastHandleProvider() != focus) {
            handleIndex = 0;
            setLastHandleProvider(focus);
        }

        Point loc = (Point) locations.get(handleIndex);
        Point current = new Point(getCurrentViewer().getControl().toControl(
                Display.getCurrent().getCursorLocation()));

        if (current.equals(loc)) {
            // The cursor is already at the location that it is to be moved to.
            // So, we
            // move to the next handle instead. If there are no more handles,
            // then we
            // cancel the drag.
            if (locations.size() > 1)
                if (e.character == '.')
                    handleIndex = (++handleIndex) % locations.size();
                else
                    handleIndex = (--handleIndex + locations.size())
                            % locations.size();
            else {
                placeMouseInViewer(loc.getTranslated(6, 6));
                return false;
            }
        }
        placeMouseInViewer((Point) locations.get(handleIndex));
        return true;
    }

    /**
     * If there's a drag tracker, sets it to <code>null</code> and then sets
     * this tool's state to the initial state.
     * 
     * @see AbstractTool#handleViewerExited()
     */
    @Override
    protected boolean handleViewerExited() {
        if (isInState(STATE_ACCESSIBLE_DRAG | STATE_ACCESSIBLE_DRAG_IN_PROGRESS
                | STATE_TRAVERSE_HANDLE | STATE_DRAG | STATE_DRAG_IN_PROGRESS)) {
            if (getDragTracker() != null)
                setDragTracker(null);
            setState(STATE_INITIAL);
        }
        return super.handleViewerExited();
    }

    /**
     * Forwards the key down event to the drag tracker, if one exists.
     * 
     * @see org.eclipse.gef.Tool#keyDown(KeyEvent,
     *      org.eclipse.gef.EditPartViewer)
     */
    @Override
    public void keyDown(KeyEvent evt, EditPartViewer viewer) {
        if (getDragTracker() != null)
            getDragTracker().keyDown(evt, viewer);
        super.keyDown(evt, viewer);
    }

    /**
     * Forwards the key up event to the drag tracker, if one exists.
     * 
     * @see org.eclipse.gef.Tool#keyUp(KeyEvent, org.eclipse.gef.EditPartViewer)
     */
    @Override
    public void keyUp(KeyEvent evt, EditPartViewer viewer) {
        if (getDragTracker() != null)
            getDragTracker().keyUp(evt, viewer);
        super.keyUp(evt, viewer);
    }

    /**
     * Forwards the mouse down event to the drag tracker, if one exists.
     * 
     * @see org.eclipse.gef.Tool#mouseDown(MouseEvent,
     *      org.eclipse.gef.EditPartViewer)
     */
    @Override
    public void mouseDown(MouseEvent e, EditPartViewer viewer) {
        super.mouseDown(e, viewer);
        if (getDragTracker() != null)
            getDragTracker().mouseDown(e, viewer);
    }

    /**
     * Forwards the mouse double clicked event to the drag tracker, if one
     * exists.
     * 
     * @see org.eclipse.gef.Tool#mouseDoubleClick(MouseEvent,
     *      org.eclipse.gef.EditPartViewer)
     */
    @Override
    public void mouseDoubleClick(MouseEvent e, EditPartViewer viewer) {
        super.mouseDoubleClick(e, viewer);
        if (getDragTracker() != null)
            getDragTracker().mouseDoubleClick(e, viewer);
    }

    /**
     * Forwards the mouse drag event to the drag tracker, if one exists.
     * 
     * @see org.eclipse.gef.Tool#mouseDrag(MouseEvent,
     *      org.eclipse.gef.EditPartViewer)
     */
    @Override
    public void mouseDrag(MouseEvent e, EditPartViewer viewer) {
        if (getDragTracker() != null)
            getDragTracker().mouseDrag(e, viewer);
        super.mouseDrag(e, viewer);
    }

    /**
     * Forwards the mouse hover event to the drag tracker, if one exists.
     * 
     * @see org.eclipse.gef.Tool#mouseHover(MouseEvent,
     *      org.eclipse.gef.EditPartViewer)
     */
    @Override
    public void mouseHover(MouseEvent me, EditPartViewer viewer) {
        if (getDragTracker() != null)
            getDragTracker().mouseHover(me, viewer);
        super.mouseHover(me, viewer);
    }

    /**
     * Forwards the mouse move event to the drag tracker, if one exists.
     * 
     * @see org.eclipse.gef.Tool#mouseMove(MouseEvent,
     *      org.eclipse.gef.EditPartViewer)
     */
    @Override
    public void mouseMove(MouseEvent me, EditPartViewer viewer) {
        if (getDragTracker() != null)
            getDragTracker().mouseMove(me, viewer);
        super.mouseMove(me, viewer);
    }

    /**
     * Forwards the mouse up event to the drag tracker, if one exists.
     * 
     * @see org.eclipse.gef.Tool#mouseUp(MouseEvent,
     *      org.eclipse.gef.EditPartViewer)
     */
    @Override
    public void mouseUp(MouseEvent e, EditPartViewer viewer) {
        if (getDragTracker() != null)
            getDragTracker().mouseUp(e, viewer);
        super.mouseUp(e, viewer);
    }

    /**
     * Delegates the scrolling to the DragTracker (if there is one). If not,
     * invokes the super method.
     * 
     * @see org.eclipse.gef.Tool#mouseWheelScrolled(org.eclipse.swt.widgets.Event,
     *      org.eclipse.gef.EditPartViewer)
     */
    @Override
    public void mouseWheelScrolled(Event event, EditPartViewer viewer) {
        if (getDragTracker() != null) {
            getDragTracker().mouseWheelScrolled(event, viewer);
            event.doit = false;
        } else
            super.mouseWheelScrolled(event, viewer);
    }

    /**
     * If there is a drag tracker, this method does nothing so that the drag
     * tracker can take care of the cursor. Otherwise, calls <code>super</code>.
     * 
     * @see AbstractTool#refreshCursor()
     */
    @Override
    protected void refreshCursor() {
        // If we have a DragTracker, let it control the Cursor
        if (getDragTracker() == null)
            super.refreshCursor();
    }

    /**
     * Sets the drag tracker for this SelectionTool. If the current drag tracker
     * is not <code>null</code>, this method deactivates it. If the new drag
     * tracker is not <code>null</code>, this method will activate it and set
     * the {@link EditDomain} and {@link EditPartViewer}.
     * 
     * @param newDragTracker
     *            the new drag tracker
     */
    public void setDragTracker(DragTracker newDragTracker) {
        if (newDragTracker == dragTracker)
            return;
        if (dragTracker != null)
            dragTracker.deactivate();
        dragTracker = newDragTracker;

        // if (!getCurrentInput().isMouseButtonDown(3))
        // setMouseCapture(dragTracker != null);
        if (newDragTracker != null) {
            newDragTracker.setEditDomain(getDomain());
            newDragTracker.activate();
            newDragTracker.setViewer(getCurrentViewer());
        }

        refreshCursor();
    }

    private void setLastHandleProvider(EditPart part) {
        if (part == null)
            cachedHandlePart = null;
        else
            cachedHandlePart = new WeakReference(part);
    }

    /**
     * Asks the target edit part (if there is one) to show hover feedback via
     * {@link EditPart#showTargetFeedback(Request)} with a hover request.
     */
    protected void showHoverFeedback() {
        if (getTargetEditPart() == null)
            return;
        if (getTargetHoverRequest() == null)
            return;
        getTargetEditPart().showTargetFeedback(getTargetHoverRequest());
    }

    /**
     * Updates the location of the hover request.
     */
    protected void updateHoverRequest() {
        LocationRequest request = (LocationRequest) getTargetHoverRequest();
        request.setLocation(getLocation());
    }

    /**
     * Sets the modifiers , type and location of the target request (which is a
     * {@link SelectionRequest}) and then calls {@link #updateHoverRequest()}.
     * 
     * @see TargetingTool#updateTargetRequest()
     */
    @Override
    protected void updateTargetRequest() {
        SelectionRequest request = (SelectionRequest) getTargetRequest();
        request.setModifiers(getCurrentInput().getModifiers());
        request.setType(getCommandName());
        request.setLocation(getLocation());
        updateHoverRequest();
    }

    /**
     * @see AbstractTool#getDebugNameForState(int)
     */
    @Override
    protected String getDebugNameForState(int state) {
        if (state == STATE_TRAVERSE_HANDLE)
            return "Traverse Handle"; //$NON-NLS-1$
        return super.getDebugNameForState(state);
    }

}
