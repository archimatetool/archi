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
package org.eclipse.gef.dnd;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;

import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.AutoexposeHelper;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;

/**
 * An abstract implementation of TransferDropTargetListener for use with
 * {@link EditPartViewer}s. The Viewer's <code>Control</code> should be the Drop
 * target. In order to communicate with EditParts in a consistent way,
 * DropTargetEvents are processed into {@link Request Requests}.
 * <P>
 * Dropping is inherently a targeting interaction. This class handles
 * calculating the <i>target</i> EditPart. It also handles common targeting
 * behavior, such as interacting with the target EditPart or its ancestors to
 * achieve things like auto-scroll/auto-expose.
 */
@SuppressWarnings({"rawtypes", "deprecation"})
public abstract class AbstractTransferDropTargetListener implements
        TransferDropTargetListener {

    private DropTargetEvent currentEvent;
    private AutoexposeHelper exposeHelper;
    private boolean hovering = false;
    private boolean askForCommand;
    private long hoverStartTime = -1;
    private Point prevMouseLoc;
    private Request request;
    private boolean showingFeedback;
    private EditPart target;
    private Transfer transfer;
    private EditPartViewer viewer;

    /**
     * Constructs a new AbstractTransferDropTargetListener and sets the
     * EditPartViewer. If you use this constructor, you must set the Transfer
     * yourself using {@link #setTransfer(Transfer)}.
     * 
     * @param viewer
     *            the EditPartViewer
     */
    public AbstractTransferDropTargetListener(EditPartViewer viewer) {
        setViewer(viewer);
    }

    /**
     * Constructs a new AbstractTransferDropTargetListener and sets the
     * EditPartViewer and Transfer. The Viewer's Control should be the Drop
     * target.
     * 
     * @param viewer
     *            the EditPartViewer
     * @param xfer
     *            the Transfer
     */
    public AbstractTransferDropTargetListener(EditPartViewer viewer,
            Transfer xfer) {
        setViewer(viewer);
        setTransfer(xfer);
    }

    private EditPart calculateTargetEditPart() {
        EditPart ep = getViewer().findObjectAtExcluding(getDropLocation(),
                getExclusionSet(), new EditPartViewer.Conditional() {
                    @Override
                    public boolean evaluate(EditPart editpart) {
                        return editpart.getTargetEditPart(getTargetRequest()) != null;
                    }
                });
        if (ep != null)
            ep = ep.getTargetEditPart(getTargetRequest());
        return ep;
    }

    /**
     * Creates and returns the <code>Request</code> that will be sent to the
     * targeted EditPart. Subclasses can override to create specialized
     * requests.
     * 
     * @return the <code>Request</code> to be used with the <i>target</i>
     *         EditPart
     */
    protected Request createTargetRequest() {
        return new Request();
    }

    /**
     * Stores the information about the current DropTargetEvent. This method may
     * not be called on the listener, because the listener may not be made
     * active until after the mouse has entered the drop target.
     * 
     * @see DropTargetListener#dragEnter(DropTargetEvent)
     */
    @Override
    public void dragEnter(DropTargetEvent event) {
        resetHover();
        setCurrentEvent(event);
    }

    /**
     * Stores the information about the current DropTargetEvent and then calls
     * <code>unload()</code>. Subclasses should override {@link #unload()} to
     * perform actions for this event. For some reason, SWT also calls
     * <code>dragLeave()</code> when the actual drop is performed, even though
     * the mouse has not left the drop target.
     * 
     * @see DropTargetListener#dragLeave(DropTargetEvent)
     */
    @Override
    public void dragLeave(DropTargetEvent event) {
        setCurrentEvent(event);
        unload();
    }

    /**
     * Stores the information about the current DropTargetEvent and then calls
     * <code>handleDragOperationChanged()</code>. Subclasses should override
     * {@link #handleDragOperationChanged()} to perform actions for this event.
     * 
     * @see DropTargetListener#dragOperationChanged(DropTargetEvent)
     */
    @Override
    public void dragOperationChanged(DropTargetEvent event) {
        resetHover();
        setCurrentEvent(event);
        handleDragOperationChanged();
    }

    /**
     * Stores the information about the current DropTargetEvent and then calls
     * <code>handleDragOver()</code>. Subclasses should override
     * {@link #handleDragOver()} to perform actions for this event.
     * 
     * @see DropTargetListener#dragOver(org.eclipse.swt.dnd.DropTargetEvent)
     */
    @Override
    public void dragOver(DropTargetEvent event) {
        setCurrentEvent(event);
        handleDragOver();
        if (testAndSet(event)) {
            resetHover();
        } else {
            if (hovering)
                return;
            long currentTime = event.time;
            if (hoverStartTime == -1) {
                hoverStartTime = currentTime;
            } else if (currentTime - hoverStartTime > 400) {
                handleHover();
                hovering = true;
            }
        }
    }

    /**
     * Stores the information about the current DropTargetEvent and then calls
     * {@link #handleDrop()}, followed by {@link #unload()}. Subclasses should
     * override these methods to perform actions for this event.
     * 
     * @see DropTargetListener#drop(DropTargetEvent)
     */
    @Override
    public void drop(DropTargetEvent event) {
        setCurrentEvent(event);
        eraseTargetFeedback();
        handleDrop();
        unload();
    }

    /**
     * Stores the current <code>DropTargetEvent</code> and does nothing. By
     * default, the drop is accepted.
     * 
     * @see DropTargetListener#dropAccept(DropTargetEvent)
     */
    @Override
    public void dropAccept(DropTargetEvent event) {
        setCurrentEvent(event);
    }

    /**
     * Calls <code>eraseTargetFeedback(Request)</code> on the current
     * <i>target</i>, using the target Request. Does nothing if there is no
     * target, or if the target has not been requested to show target feedback.
     */
    protected void eraseTargetFeedback() {
        if (getTargetEditPart() != null && showingFeedback) {
            showingFeedback = false;
            getTargetEditPart().eraseTargetFeedback(getTargetRequest());
        }
    }

    /**
     * Returns the current command from the target EditPart.
     * 
     * @return The current command from the target EditPart
     */
    protected Command getCommand() {
        return getTargetEditPart().getCommand(getTargetRequest());
    }

    /**
     * Returns the current <code>DropTargetEvent</code>.
     * 
     * @return the current event
     */
    public DropTargetEvent getCurrentEvent() {
        return currentEvent;
    }

    /**
     * Returns the current mouse location, as a {@link Point}. The location is
     * relative to the control's client area.
     * 
     * @return the drop location
     */
    protected Point getDropLocation() {
        org.eclipse.swt.graphics.Point swt;
        swt = new org.eclipse.swt.graphics.Point(getCurrentEvent().x,
                getCurrentEvent().y);
        DropTarget target = (DropTarget) getCurrentEvent().widget;
        swt = target.getControl().toControl(swt);
        return new Point(swt.x, swt.y);
    }

    /**
     * Returns a Collection of {@link EditPart EditParts} that are to be
     * excluded when searching for the target EditPart.
     * 
     * @return A Collection of EditParts to be excluded
     */
    protected Collection getExclusionSet() {
        return Collections.EMPTY_LIST;
    }

    /**
     * Returns the current <i>target</i> <code>EditPart</code>.
     * 
     * @return the target EditPart
     */
    protected EditPart getTargetEditPart() {
        return target;
    }

    /**
     * Returns the target <code>Request</code>. If the target Request is
     * <code>null</code>, {@link #createTargetRequest()} is called and the newly
     * created Request is returned.
     * 
     * @return the target Request
     */
    protected Request getTargetRequest() {
        if (request == null)
            request = createTargetRequest();
        return request;
    }

    /**
     * @see TransferDropTargetListener#getTransfer()
     */
    @Override
    public Transfer getTransfer() {
        return transfer;
    }

    /**
     * Returns the <code>EditPartViewer</code> that is the target of the drop.
     * 
     * @return the EditPartViewer
     */
    protected EditPartViewer getViewer() {
        return viewer;
    }

    /**
     * Called when the user changes the Drag operation. By default, target
     * feedback is erased. The target Request and target EditPart are updated,
     * and target feedback is re-displayed on the new target.
     */
    protected void handleDragOperationChanged() {
        // Erase any old feedback now, in case the request changes substantially
        eraseTargetFeedback();

        // Update request based on the new operation type
        updateTargetRequest();

        // Update the target based on the updated request
        updateTargetEditPart();
    }

    /**
     * Called whenever the User drags over the target. By default, the target
     * Request and target EditPart are updated, feedback is shown, and
     * auto-expose occurs.
     */
    protected void handleDragOver() {
        updateTargetRequest();
        updateTargetEditPart();
        showTargetFeedback();
        if (exposeHelper != null) {
            // If the expose helper does not wish to continue, set helper to
            // null.
            if (!exposeHelper.step(getDropLocation()))
                exposeHelper = null;
        }
    }

    /**
     * Updates the target Request and target EditPart, and performs the drop. By
     * default, the drop is performed by asking the target EditPart for a
     * Command using the target Request. This Command is then executed on the
     * CommandStack.
     * <P>
     * If there is no target EditPart or no executable Command, the event's
     * <code>detail</code> field is set to <code>DND.DROP_NONE</code>.
     */
    protected void handleDrop() {
        updateTargetRequest();
        updateTargetEditPart();

        if (getTargetEditPart() != null) {
            Command command = getCommand();
            if (command != null && command.canExecute())
                getViewer().getEditDomain().getCommandStack().execute(command);
            else
                getCurrentEvent().detail = DND.DROP_NONE;
        } else
            getCurrentEvent().detail = DND.DROP_NONE;
    }

    /**
     * Called when a new target EditPart has been entered. By default, the new
     * target is asked to show feedback.
     */
    protected void handleEnteredEditPart() {
    }

    /**
     * Called as the current target EditPart is being exited. By default, the
     * target is asked to erase feedback.
     */
    protected void handleExitingEditPart() {
        eraseTargetFeedback();
    }

    /**
     * Called when the mouse hovers during drag and drop.
     */
    protected void handleHover() {
        updateAutoexposeHelper();
    }

    /**
     * Called when the mouse resumes motion after having hovered.
     */
    protected void handleHoverStop() {
    }

    /**
     * Returns <code>true</code> if this TransferDropTargetListener is enabled
     * for the specified <code>DropTargetEvent</code>. By default, this is
     * calculated by comparing the event's {@link DropTargetEvent#dataTypes
     * dataTypes} with the <code>Transfer's</code> supported types (
     * {@link Transfer#isSupportedType(TransferData)}). If a dataType is
     * supported, an attempt is made to find a <i>target</i>
     * <code>EditPart</code> at the current drop location. If a target
     * <code>EditPart</code> is found, <code>true</code> is returned, and the
     * DropTargetEvent's {@link DropTargetEvent#currentDataType} is set to the
     * dataType that matched.
     * 
     * @param event
     *            the DropTargetEvent
     * @return <code>true</code> if this TransferDropTargetListener is enabled
     *         for the given DropTargetEvent
     */
    @Override
    public boolean isEnabled(DropTargetEvent event) {
        for (int i = 0; i < event.dataTypes.length; i++) {
            if (getTransfer().isSupportedType(event.dataTypes[i])) {
                setCurrentEvent(event);
                event.currentDataType = event.dataTypes[i];
                updateTargetRequest();
                EditPart oldTarget = target;
                updateTargetEditPart();
                boolean result;
                if (target == null)
                    result = false;
                else if (askForCommand) {
                    Command command = getCommand();
                    result = command != null && command.canExecute();
                } else
                    result = true;
                request = null;
                target = oldTarget;
                return result;
            }
        }
        return false;
    }

    /**
     * Returns <code>true</code> if {@link #isEnabled(DropTargetEvent)} is
     * determined by asking the potential target for a Command.
     * 
     * @return <code>true</code> if the target will be queried for a
     *         <code>Command</code>
     * @since 3.1
     */
    protected boolean isEnablementDeterminedByCommand() {
        return askForCommand;
    }

    private void resetHover() {
        if (hovering) {
            handleHoverStop();
            hovering = false;
            hoverStartTime = -1;
            prevMouseLoc = null;
        }
    }

    /**
     * Sets the current autoexpose helper.
     * 
     * @param helper
     *            the autoexpose helper
     */
    protected void setAutoexposeHelper(AutoexposeHelper helper) {
        exposeHelper = helper;
    }

    /**
     * Determines if the target editpart should be asked for a Command during
     * {@link #isEnabled(DropTargetEvent)}. For most DND operations, the data is
     * not available, thus asking for a command would not make sense. The
     * default value is <code>false</code>.
     * 
     * @param value
     *            <code>true</code> if a
     * @since 3.1
     */
    protected void setEnablementDeterminedByCommand(boolean value) {
        askForCommand = value;
    }

    /**
     * Sets the current DropTargetEvent.
     * 
     * @param currentEvent
     *            the DropTargetEvent
     */
    public void setCurrentEvent(DropTargetEvent currentEvent) {
        this.currentEvent = currentEvent;
    }

    /**
     * Sets the <i>target</i> <code>EditPart</code>. If the target is changing,
     * {@link #handleExitingEditPart()} is called before the target changes, and
     * {@link #handleEnteredEditPart()} is called afterwards.
     * 
     * @param ep
     *            the new target EditPart
     */
    protected void setTargetEditPart(EditPart ep) {
        if (ep != target) {
            if (target != null)
                handleExitingEditPart();
            target = ep;
            if (target != null)
                handleEnteredEditPart();
        }
    }

    /**
     * Sets the Tranfer type that this listener can handle.
     * 
     * @param xfer
     *            the Transfer
     */
    protected void setTransfer(Transfer xfer) {
        transfer = xfer;
    }

    /**
     * Sets the EditPartViewer.
     * 
     * @param viewer
     *            the EditPartViewer
     */
    protected void setViewer(EditPartViewer viewer) {
        this.viewer = viewer;
    }

    /**
     * Asks the target <code>EditPart</code> to show target feedback if it is
     * not <code>null</code>.
     * 
     * @see EditPart#showTargetFeedback(Request)
     */
    protected void showTargetFeedback() {
        if (getTargetEditPart() != null) {
            showingFeedback = true;
            getTargetEditPart().showTargetFeedback(getTargetRequest());
        }
    }

    /**
     * Tests whether the given event's location is different than the previous
     * event's location, and sets the remembered location to the current event's
     * location.
     * 
     * @param event
     * @return boolean
     */
    private boolean testAndSet(DropTargetEvent event) {
        boolean result = prevMouseLoc == null
                || !(prevMouseLoc.x == event.x && prevMouseLoc.y == event.y);
        if (prevMouseLoc == null)
            prevMouseLoc = new Point();
        prevMouseLoc.x = event.x;
        prevMouseLoc.y = event.y;
        return result;
    }

    /**
     * Erases target feedback and sets the request to <code>null</code>.
     */
    protected void unload() {
        resetHover();
        eraseTargetFeedback();
        request = null;
        setTargetEditPart(null);
        setCurrentEvent(null);
        setAutoexposeHelper(null);
    }

    /**
     * Updates the active {@link AutoexposeHelper}. Does nothing if there is
     * still an active helper. Otherwise, obtains a new helper (possible
     * <code>null</code>) at the current mouse location and calls
     * {@link #setAutoexposeHelper(AutoexposeHelper)}.
     */
    protected void updateAutoexposeHelper() {
        if (exposeHelper != null)
            return;
        AutoexposeHelper.Search search;
        search = new AutoexposeHelper.Search(getDropLocation());
        getViewer().findObjectAtExcluding(getDropLocation(),
                Collections.EMPTY_LIST, search);
        setAutoexposeHelper(search.result);
    }

    /**
     * Updates the target EditPart.
     */
    protected void updateTargetEditPart() {
        setTargetEditPart(calculateTargetEditPart());
    }

    /**
     * Subclasses must implement this to update the target Request.
     */
    protected abstract void updateTargetRequest();

}
