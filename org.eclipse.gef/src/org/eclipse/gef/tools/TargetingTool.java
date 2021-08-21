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

import java.util.Collection;
import java.util.Collections;

import org.eclipse.swt.widgets.Display;

import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.AutoexposeHelper;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.requests.TargetRequest;

/**
 * The base implementation for tools which perform targeting of editparts.
 * Targeting tools may operate using either mouse drags or just mouse moves.
 * Targeting tools work with a <i>target</i> request. This request is used along
 * with the mouse location to obtain an active target from the current
 * EditPartViewer. This target is then asked for the <code>Command</code> that
 * performs the given request. The target is also asked to show target feedback.
 * <P>
 * TargetingTool also provides support for auto-expose (a.k.a. auto-scrolling).
 * Subclasses that wish to commence auto-expose can do so by calling
 * {@link #updateAutoexposeHelper()}. An an AutoExposeHelper is found,
 * auto-scrolling begins. Whenever that helper scrolls the diagram of performs
 * any other change, <code>handleMove</code> will be called as if the mouse had
 * moved. This is because the target has probably moved, but there is no input
 * event to trigger an update of the operation.
 */
public abstract class TargetingTool extends AbstractTool {

    private static final int FLAG_LOCK_TARGET = AbstractTool.MAX_FLAG << 1;
    private static final int FLAG_TARGET_FEEDBACK = AbstractTool.MAX_FLAG << 2;
    /**
     * The max flag.
     */
    protected static final int MAX_FLAG = FLAG_TARGET_FEEDBACK;

    private Request targetRequest;
    private EditPart targetEditPart;
    private AutoexposeHelper exposeHelper;

    /**
     * Creates the target request that will be used with the target editpart.
     * This request will be cached and updated as needed.
     * 
     * @see #getTargetRequest()
     * @return the new target request
     */
    protected Request createTargetRequest() {
        Request request = new Request();
        request.setType(getCommandName());
        return request;
    }

    /**
     * @see org.eclipse.gef.Tool#deactivate()
     */
    @Override
    public void deactivate() {
        if (isHoverActive())
            resetHover();
        eraseTargetFeedback();
        targetEditPart = null;
        targetRequest = null;
        setAutoexposeHelper(null);
        super.deactivate();
    }

    /**
     * Called to perform an iteration of the autoexpose process. If the expose
     * helper is set, it will be asked to step at the current mouse location. If
     * it returns true, another expose iteration will be queued. There is no
     * delay between autoexpose events, other than the time required to perform
     * the step().
     */
    protected void doAutoexpose() {
        if (exposeHelper == null)
            return;
        if (exposeHelper.step(getLocation())) {
            handleAutoexpose();
            Display.getCurrent().asyncExec(new QueuedAutoexpose());
        } else
            setAutoexposeHelper(null);
    }

    /**
     * Asks the current target editpart to erase target feedback using the
     * target request. If target feedback is not being shown, this method does
     * nothing and returns. Otherwise, the target feedback flag is reset to
     * false, and the target editpart is asked to erase target feedback. This
     * methods should rarely be overridden.
     */
    protected void eraseTargetFeedback() {
        if (!isShowingTargetFeedback())
            return;
        setFlag(FLAG_TARGET_FEEDBACK, false);
        if (getTargetEditPart() != null)
            getTargetEditPart().eraseTargetFeedback(getTargetRequest());
    }

    /**
     * Queries the target editpart for a command.
     * 
     * @see org.eclipse.gef.tools.AbstractTool#getCommand()
     */
    @Override
    protected Command getCommand() {
        if (getTargetEditPart() == null)
            return null;
        return getTargetEditPart().getCommand(getTargetRequest());
    }

    /**
     * Returns a List of objects that should be excluded as potential targets
     * for the operation.
     * 
     * @return the list of objects to be excluded as targets
     */
    @SuppressWarnings("rawtypes")
    protected Collection getExclusionSet() {
        return Collections.EMPTY_LIST;
    }

    /**
     * Returns the conditional object used for obtaining the target editpart
     * from the current viewer. By default, a conditional is returned that tests
     * whether an editpart at the current mouse location indicates a target for
     * the operation's request, using
     * {@link EditPart#getTargetEditPart(Request)}. If <code>null</code> is
     * returned, then the conditional fails, and the search continues.
     * 
     * @see EditPartViewer#findObjectAtExcluding(Point, Collection,
     *      EditPartViewer.Conditional)
     * @return the targeting conditional
     */
    protected EditPartViewer.Conditional getTargetingConditional() {
        return new EditPartViewer.Conditional() {
            @Override
            public boolean evaluate(EditPart editpart) {
                return editpart.getTargetEditPart(getTargetRequest()) != null;
            }
        };
    }

    /**
     * Returns <code>null</code> or the current target editpart.
     * 
     * @return <code>null</code> or a target part
     */
    protected EditPart getTargetEditPart() {
        return targetEditPart;
    }

    /**
     * Lazily creates and returns the request used when communicating with the
     * target editpart.
     * 
     * @return the target request
     */
    protected Request getTargetRequest() {
        if (targetRequest == null)
            setTargetRequest(createTargetRequest());
        return targetRequest;
    }

    /**
     * This method is called whenever an autoexpose occurs. When an autoexpose
     * occurs, it is possible that everything in the viewer has moved a little.
     * Therefore, by default, {@link AbstractTool#handleMove() handleMove()} is
     * called to simulate the mouse moving even though it didn't.
     */
    protected void handleAutoexpose() {
        handleMove();
    }

    /**
     * Called whenever the target editpart has changed. By default, the target
     * request is updated, and the new target is asked to show feedback.
     * Subclasses may extend this method if needed.
     * 
     * @return <code>true</code>
     */
    protected boolean handleEnteredEditPart() {
        updateTargetRequest();
        showTargetFeedback();
        return true;
    }

    /**
     * Called whenever the target editpart is about to change. By default, hover
     * is reset, in the case that a hover was showing something, and the target
     * being exited is asked to erase its feedback.
     * 
     * @return <code>true</code>
     */
    protected boolean handleExitingEditPart() {
        resetHover();
        eraseTargetFeedback();
        return true;
    }

    /**
     * Called from resetHover() iff hover is active. Subclasses may extend this
     * method to handle the hover stop event. Returns <code>true</code> if
     * something was done in response to the call.
     * 
     * @see AbstractTool#isHoverActive()
     * @return <code>true</code> if the hover stop is processed in some way
     */
    protected boolean handleHoverStop() {
        return false;
    }

    /**
     * Called when invalid input is encountered. By default, feedback is erased,
     * and the current command is set to the unexecutable command. The state
     * does not change, so the caller must set the state to
     * {@link AbstractTool#STATE_INVALID}.
     * 
     * @return <code>true</code>
     */
    @Override
    protected boolean handleInvalidInput() {
        eraseTargetFeedback();
        setCurrentCommand(UnexecutableCommand.INSTANCE);
        return true;
    }

    /**
     * An archaic method name that has been left here to force use of the new
     * name.
     * 
     * @throws Exception
     *             exc
     */
    protected final void handleLeavingEditPart() throws Exception {
    }

    /**
     * Sets the target to <code>null</code>.
     * 
     * @see org.eclipse.gef.tools.AbstractTool#handleViewerExited()
     */
    @Override
    protected boolean handleViewerExited() {
        setTargetEditPart(null);
        return true;
    }

    /**
     * Returns <code>true</code> if target feedback is being shown.
     * 
     * @return <code>true</code> if showing target feedback
     */
    protected boolean isShowingTargetFeedback() {
        return getFlag(FLAG_TARGET_FEEDBACK);
    }

    /**
     * Return <code>true</code> if the current target is locked.
     * 
     * @see #lockTargetEditPart(EditPart)
     * @return <code>true</code> if the target is locked
     */
    protected boolean isTargetLocked() {
        return getFlag(FLAG_LOCK_TARGET);
    }

    /**
     * Locks-in the given editpart as the target. Updating of the target will
     * not occur until {@link #unlockTargetEditPart()} is called.
     * 
     * @param editpart
     *            the target to be locked-in
     */
    protected void lockTargetEditPart(EditPart editpart) {
        if (editpart == null) {
            unlockTargetEditPart();
            return;
        }
        setFlag(FLAG_LOCK_TARGET, true);
        setTargetEditPart(editpart);
    }

    /**
     * Extended to reset the target lock flag.
     * 
     * @see org.eclipse.gef.tools.AbstractTool#resetFlags()
     * @see #lockTargetEditPart(EditPart)
     */
    @Override
    protected void resetFlags() {
        setFlag(FLAG_LOCK_TARGET, false);
        super.resetFlags();
    }

    /**
     * Resets hovering to inactive.
     * 
     * @since 3.4
     */
    protected void resetHover() {
        if (isHoverActive())
            handleHoverStop();
        setHoverActive(false);
    }

    class QueuedAutoexpose implements Runnable {
        @Override
        public void run() {
            if (exposeHelper != null)
                doAutoexpose();
        }
    }

    /**
     * Sets the active autoexpose helper to the given helper, or
     * <code>null</code>. If the helper is not <code>null</code>, a runnable is
     * queued on the event thread that will trigger a subsequent
     * {@link #doAutoexpose()}. The helper is typically updated only on a hover
     * event.
     * 
     * @param helper
     *            the new autoexpose helper or <code>null</code>
     */
    protected void setAutoexposeHelper(AutoexposeHelper helper) {
        exposeHelper = helper;
        if (exposeHelper == null)
            return;
        Display.getCurrent().asyncExec(new QueuedAutoexpose());
    }

    /**
     * Sets the target editpart. If the target editpart is changing, this method
     * will call {@link #handleExitingEditPart()} for the previous target if not
     * <code>null</code>, and {@link #handleEnteredEditPart()} for the new
     * target, if not <code>null</code>.
     * 
     * @param editpart
     *            the new target
     */
    protected void setTargetEditPart(EditPart editpart) {
        if (editpart != targetEditPart) {
            if (targetEditPart != null)
                handleExitingEditPart();
            targetEditPart = editpart;
            if (getTargetRequest() instanceof TargetRequest)
                ((TargetRequest) getTargetRequest())
                        .setTargetEditPart(targetEditPart);
            handleEnteredEditPart();
        }
    }

    /**
     * Sets the target request. This method is typically not called; subclasses
     * normally override {@link #createTargetRequest()}.
     * 
     * @param req
     *            the target request
     */
    protected void setTargetRequest(Request req) {
        targetRequest = req;
    }

    /**
     * Asks the target editpart to show target feedback and sets the target
     * feedback flag.
     */
    protected void showTargetFeedback() {
        if (getTargetEditPart() != null)
            getTargetEditPart().showTargetFeedback(getTargetRequest());
        setFlag(FLAG_TARGET_FEEDBACK, true);
    }

    /**
     * Releases the targeting lock, and updates the target in case the mouse is
     * already over a new target.
     */
    protected void unlockTargetEditPart() {
        setFlag(FLAG_LOCK_TARGET, false);
        updateTargetUnderMouse();
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
        search = new AutoexposeHelper.Search(getLocation());
        getCurrentViewer().findObjectAtExcluding(getLocation(),
                Collections.EMPTY_LIST, search);
        setAutoexposeHelper(search.result);
    }

    /**
     * Subclasses should override to update the target request.
     */
    protected void updateTargetRequest() {
    }

    /**
     * Updates the target editpart and returns <code>true</code> if the target
     * changes. The target is updated by using the target conditional and the
     * target request. If the target has been locked, this method does nothing
     * and returns <code>false</code>.
     * 
     * @return <code>true</code> if the target was changed
     */
    protected boolean updateTargetUnderMouse() {
        if (!isTargetLocked()) {
            EditPart editPart = null;
            if (getCurrentViewer() != null)
                editPart = getCurrentViewer().findObjectAtExcluding(
                        getLocation(), getExclusionSet(),
                        getTargetingConditional());
            if (editPart != null)
                editPart = editPart.getTargetEditPart(getTargetRequest());
            boolean changed = getTargetEditPart() != editPart;
            setTargetEditPart(editPart);
            return changed;
        } else
            return false;
    }

    /**
     * Returns <code>null</code> or the current autoexpose helper.
     * 
     * @return null or a helper
     */
    protected AutoexposeHelper getAutoexposeHelper() {
        return exposeHelper;
    }

}
