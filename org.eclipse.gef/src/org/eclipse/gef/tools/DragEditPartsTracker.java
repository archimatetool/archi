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
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Cursor;

import org.eclipse.core.runtime.Platform;
import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.PrecisionRectangle;

import org.eclipse.gef.AutoexposeHelper;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.SharedCursors;
import org.eclipse.gef.SnapToHelper;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.handles.HandleBounds;
import org.eclipse.gef.requests.ChangeBoundsRequest;

/**
 * A DragTracker that moves {@link org.eclipse.gef.EditPart EditParts}.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class DragEditPartsTracker extends SelectEditPartTracker {

    /**
     * Key modifier for cloning. It's ALT on Mac, and CTRL on all other
     * platforms.
     */
    static final int MODIFIER_CLONE;

    static {
        if (Platform.OS_MACOSX.equals(Platform.getOS()))
            MODIFIER_CLONE = SWT.ALT;
        else
            MODIFIER_CLONE = SWT.CTRL;
    }

    /**
     * Key modifier for constrained move. It's SHIFT on all platforms.
     */
    static final int MODIFIER_CONSTRAINED_MOVE = SWT.SHIFT;

    private static final int FLAG_SOURCE_FEEDBACK = SelectEditPartTracker.MAX_FLAG << 1;
    /** Max flag */
    protected static final int MAX_FLAG = FLAG_SOURCE_FEEDBACK;
    private List exclusionSet;
    private PrecisionPoint sourceRelativeStartPoint;
    private SnapToHelper snapToHelper;
    private PrecisionRectangle sourceRectangle, compoundSrcRect;
    private boolean cloneActive;

    /**
     * Constructs a new DragEditPartsTracker with the given source edit part.
     * 
     * @param sourceEditPart
     *            the source edit part
     */
    public DragEditPartsTracker(EditPart sourceEditPart) {
        super(sourceEditPart);

        cloneActive = false;
        setDisabledCursor(Cursors.NO);
    }

    /**
     * Returns true if the control key was the key in the key event and the tool
     * is in an acceptable state for this event.
     * 
     * @param e
     *            the key event
     * @return true if the key was control and can be accepted.
     */
    private boolean acceptClone(KeyEvent e) {
        int key = e.keyCode;
        if (!(isInState(STATE_DRAG_IN_PROGRESS | STATE_ACCESSIBLE_DRAG
                | STATE_ACCESSIBLE_DRAG_IN_PROGRESS)))
            return false;
        return (key == MODIFIER_CLONE);
    }

    private boolean acceptSHIFT(KeyEvent e) {
        return isInState(STATE_DRAG_IN_PROGRESS | STATE_ACCESSIBLE_DRAG
                | STATE_ACCESSIBLE_DRAG_IN_PROGRESS)
                && e.keyCode == SWT.SHIFT;
    }

    /**
     * Returns the cursor used under normal conditions.
     * 
     * @see #setDefaultCursor(Cursor)
     * @return the default cursor
     */
    @Override
    protected Cursor getDefaultCursor() {
        if (isCloneActive())
            return SharedCursors.CURSOR_TREE_ADD;
        return super.getDefaultCursor();
    }

    /**
     * Returns the bounds of the {@link #getSourceEditPart() source edit part's}
     * figure in absolute coordinates. In case the source figure implements
     * {@link HandleBounds} the {@link HandleBounds#getHandleBounds() handle
     * bounds} are returned in absolute coordinates, other wise the
     * {@link IFigure#getBounds() figure bounds}.
     * 
     * @return The bounds of the source figure in absolute coordinates.
     * 
     * @since 3.11
     */
    protected PrecisionRectangle getSourceBounds() {
        return this.sourceRectangle;
    }

    /**
     * Returns the unioned bounds of the {@link #getOperationSet() operation set
     * edit parts'} figures in absolute coordinates. In case the figures
     * implement {@link HandleBounds} their
     * {@link HandleBounds#getHandleBounds() handle bounds} will be used,
     * otherwise their {@link IFigure#getBounds() figure bounds}.
     * 
     * @return The unioned bounds of the operation set figures in absolute
     *         coordinates.
     * @since 3.11
     */
    protected PrecisionRectangle getOperationSetBounds() {
        return this.compoundSrcRect;
    }

    /**
     * Returns the {@link SnapToHelper} used by this
     * {@link DragEditPartsTracker}.
     * 
     * @return The {@link SnapToHelper} used by this
     *         {@link DragEditPartsTracker}.
     * @since 3.11
     */
    protected SnapToHelper getSnapToHelper() {
        return snapToHelper;
    }

    /**
     * Erases feedback and calls {@link #performDrag()}. Sets the state to
     * terminal.
     * 
     * @see org.eclipse.gef.tools.AbstractTool#commitDrag()
     */
    @Override
    public void commitDrag() {
        eraseSourceFeedback();
        eraseTargetFeedback();
        performDrag();
        setState(STATE_TERMINAL);
    }

    /**
     * Captures the bounds of the source being dragged, and the unioned bounds
     * of all figures being dragged. These bounds are used for snapping by the
     * snap strategies in <code>updateTargetRequest()</code>.
     */
    @SuppressWarnings("deprecation")
    private void captureSourceDimensions() {
        List editparts = getOperationSet();
        for (int i = 0; i < editparts.size(); i++) {
            GraphicalEditPart child = (GraphicalEditPart) editparts.get(i);
            IFigure figure = child.getFigure();
            PrecisionRectangle bounds = null;
            if (figure instanceof HandleBounds)
                bounds = new PrecisionRectangle(
                        ((HandleBounds) figure).getHandleBounds());
            else
                bounds = new PrecisionRectangle(figure.getBounds());
            figure.translateToAbsolute(bounds);

            if (compoundSrcRect == null)
                compoundSrcRect = new PrecisionRectangle(bounds);
            else
                compoundSrcRect = compoundSrcRect.union(bounds);
            if (child == getSourceEditPart())
                sourceRectangle = bounds;
        }
        if (sourceRectangle == null) {
            IFigure figure = ((GraphicalEditPart) getSourceEditPart())
                    .getFigure();
            if (figure instanceof HandleBounds)
                sourceRectangle = new PrecisionRectangle(
                        ((HandleBounds) figure).getHandleBounds());
            else
                sourceRectangle = new PrecisionRectangle(figure.getBounds());
            figure.translateToAbsolute(sourceRectangle);
        }
    }

    /**
     * Returns a List of top-level edit parts excluding dependants (by calling
     * {@link ToolUtilities#getSelectionWithoutDependants(EditPartViewer)} that
     * understand the current target request (by calling
     * {@link ToolUtilities#filterEditPartsUnderstanding(List, Request)}.
     * 
     * @see org.eclipse.gef.tools.AbstractTool#createOperationSet()
     */
    @Override
    protected List createOperationSet() {
        if (getCurrentViewer() != null) {
            List list = ToolUtilities
                    .getSelectionWithoutDependants(getCurrentViewer());
            ToolUtilities
                    .filterEditPartsUnderstanding(list, getTargetRequest());
            return list;
        }

        return new ArrayList();
    }

    /**
     * Creates a {@link ChangeBoundsRequest}. By default, the type is
     * {@link RequestConstants#REQ_MOVE}. Later on when the edit parts are asked
     * to contribute to the overall command, the request type will be either
     * {@link RequestConstants#REQ_MOVE} or {@link RequestConstants#REQ_ORPHAN},
     * depending on the result of {@link #isMove()}.
     * 
     * @see org.eclipse.gef.tools.TargetingTool#createTargetRequest()
     */
    @Override
    protected Request createTargetRequest() {
        if (isCloneActive())
            return new ChangeBoundsRequest(REQ_CLONE);
        else
            return new ChangeBoundsRequest(REQ_MOVE);
    }

    /**
     * Erases source feedback and sets the autoexpose helper to
     * <code>null</code>.
     * 
     * @see org.eclipse.gef.Tool#deactivate()
     */
    @Override
    public void deactivate() {
        eraseSourceFeedback();
        super.deactivate();
        exclusionSet = null;
        sourceRelativeStartPoint = null;
        sourceRectangle = null;
        compoundSrcRect = null;
        snapToHelper = null;
    }

    /**
     * Asks the edit parts in the {@link AbstractTool#getOperationSet()
     * operation set} to erase their source feedback.
     */
    protected void eraseSourceFeedback() {
        if (!getFlag(FLAG_SOURCE_FEEDBACK))
            return;
        setFlag(FLAG_SOURCE_FEEDBACK, false);
        List editParts = getOperationSet();
        for (int i = 0; i < editParts.size(); i++) {
            EditPart editPart = (EditPart) editParts.get(i);
            editPart.eraseSourceFeedback(getTargetRequest());
        }
    }

    /**
     * Asks each edit part in the {@link AbstractTool#getOperationSet()
     * operation set} to contribute to a {@link CompoundCommand} after first
     * setting the request type to either {@link RequestConstants#REQ_MOVE} or
     * {@link RequestConstants#REQ_ORPHAN}, depending on the result of
     * {@link #isMove()}.
     * 
     * @see org.eclipse.gef.tools.AbstractTool#getCommand()
     */
    @Override
    protected Command getCommand() {
        CompoundCommand command = new CompoundCommand();
        command.setDebugLabel("Drag Object Tracker");//$NON-NLS-1$

        Iterator iter = getOperationSet().iterator();

        Request request = getTargetRequest();

        if (isCloneActive())
            request.setType(REQ_CLONE);
        else if (isMove())
            request.setType(REQ_MOVE);
        else
            request.setType(REQ_ORPHAN);

        if (!isCloneActive()) {
            while (iter.hasNext()) {
                EditPart editPart = (EditPart) iter.next();
                command.add(editPart.getCommand(request));
            }
        }

        if (!isMove() || isCloneActive()) {
            if (!isCloneActive())
                request.setType(REQ_ADD);

            if (getTargetEditPart() == null)
                command.add(UnexecutableCommand.INSTANCE);
            else
                command.add(getTargetEditPart().getCommand(getTargetRequest()));
        }

        return command.unwrap();
    }

    /**
     * @see org.eclipse.gef.tools.AbstractTool#getCommandName()
     */
    @Override
    protected String getCommandName() {
        if (isCloneActive())
            return REQ_CLONE;
        else if (isMove())
            return REQ_MOVE;
        else
            return REQ_ADD;
    }

    /**
     * @see org.eclipse.gef.tools.AbstractTool#getDebugName()
     */
    @Override
    protected String getDebugName() {
        return "DragEditPartsTracker:" + getCommandName();//$NON-NLS-1$
    }

    /**
     * Returns a list of all the edit parts in the
     * {@link AbstractTool#getOperationSet() operation set}, plus the
     * {@link org.eclipse.draw2d.ConnectionLayer}.
     * 
     * @see org.eclipse.gef.tools.TargetingTool#getExclusionSet()
     */
    @Override
    protected Collection getExclusionSet() {
        if (exclusionSet == null) {
            List set = getOperationSet();
            exclusionSet = new ArrayList(set.size() + 1);
            for (int i = 0; i < set.size(); i++) {
                GraphicalEditPart editpart = (GraphicalEditPart) set.get(i);
                exclusionSet.add(editpart.getFigure());
            }
            LayerManager layerManager = (LayerManager) getCurrentViewer()
                    .getEditPartRegistry().get(LayerManager.ID);
            if (layerManager != null) {
                exclusionSet.add(layerManager
                        .getLayer(LayerConstants.CONNECTION_LAYER));
            }
        }
        return exclusionSet;
    }

    /**
     * @see org.eclipse.gef.tools.TargetingTool#handleAutoexpose()
     */
    @Override
    protected void handleAutoexpose() {
        updateTargetRequest();
        updateTargetUnderMouse();
        showTargetFeedback();
        showSourceFeedback();
        setCurrentCommand(getCommand());
    }

    /**
     * Erases feedback and calls {@link #performDrag()}.
     * 
     * @see org.eclipse.gef.tools.AbstractTool#handleButtonUp(int)
     */
    @Override
    protected boolean handleButtonUp(int button) {
        if (stateTransition(STATE_DRAG_IN_PROGRESS, STATE_TERMINAL)) {
            eraseSourceFeedback();
            eraseTargetFeedback();
            performDrag();
            return true;
        }
        return super.handleButtonUp(button);
    }

    /**
     * Updates the target request and mouse target, asks to show feedback, and
     * sets the current command.
     * 
     * @see org.eclipse.gef.tools.AbstractTool#handleDragInProgress()
     */
    @Override
    protected boolean handleDragInProgress() {
        if (isInDragInProgress()) {
            updateTargetRequest();
            if (updateTargetUnderMouse())
                updateTargetRequest();
            showTargetFeedback();
            showSourceFeedback();
            setCurrentCommand(getCommand());
        }
        return true;
    }

    /**
     * Calls {@link TargetingTool#updateAutoexposeHelper()} if a drag is in
     * progress.
     * 
     * @see org.eclipse.gef.tools.TargetingTool#handleHover()
     */
    @Override
    protected boolean handleHover() {
        if (isInDragInProgress())
            updateAutoexposeHelper();
        return true;
    }

    /**
     * Erases source feedback.
     * 
     * @see org.eclipse.gef.tools.TargetingTool#handleInvalidInput()
     */
    @Override
    protected boolean handleInvalidInput() {
        super.handleInvalidInput();
        eraseSourceFeedback();
        return true;
    }

    /**
     * Processes arrow keys used to move edit parts.
     * 
     * @see org.eclipse.gef.tools.AbstractTool#handleKeyDown(org.eclipse.swt.events.KeyEvent)
     */
    @Override
    protected boolean handleKeyDown(KeyEvent e) {
        setAutoexposeHelper(null);
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
        } else if (acceptClone(e)) {
            setCloneActive(true);
            handleDragInProgress();
            return true;
        } else if (acceptSHIFT(e)) {
            handleDragInProgress();
            return true;
        }

        return false;
    }

    /**
     * Interprets and processes clone deactivation, constrained move
     * deactivation, and accessibility navigation reset.
     * 
     * @see org.eclipse.gef.tools.AbstractTool#handleKeyUp(org.eclipse.swt.events.KeyEvent)
     */
    @Override
    protected boolean handleKeyUp(KeyEvent e) {
        if (acceptArrowKey(e)) {
            accStepReset();
            return true;
        } else if (acceptClone(e)) {
            setCloneActive(false);
            handleDragInProgress();
            return true;
        } else if (acceptSHIFT(e)) {
            handleDragInProgress();
            return true;
        }
        return false;
    }

    /**
     * Returns true if the current drag is a clone operation.
     * 
     * @return true if cloning is enabled and is currently active.
     */
    protected boolean isCloneActive() {
        return cloneActive;
    }

    /**
     * Returns <code>true</code> if the source edit part is being moved within
     * its parent. If the source edit part is being moved to another parent,
     * this returns <code>false</code>.
     * 
     * @return <code>true</code> if the source edit part is not being reparented
     */
    protected boolean isMove() {
        EditPart part = getSourceEditPart();
        while (part != getTargetEditPart() && part != null) {
            if (part.getParent() == getTargetEditPart()
                    && part.getSelected() != EditPart.SELECTED_NONE)
                return true;
            part = part.getParent();
        }
        return false;
    }

    /**
     * Calls {@link AbstractTool#executeCurrentCommand()}.
     */
    protected void performDrag() {
        executeCurrentCommand();
    }

    /**
     * If auto scroll (also called auto expose) is being performed, the start
     * location moves during the scroll. This method updates that location.
     */
    protected void repairStartLocation() {
        if (sourceRelativeStartPoint == null)
            return;
        IFigure figure = ((GraphicalEditPart) getSourceEditPart()).getFigure();
        PrecisionPoint newStart = (PrecisionPoint) sourceRelativeStartPoint
                .getCopy();
        figure.translateToAbsolute(newStart);
        Point delta = new Point(newStart.x - getStartLocation().x, newStart.y
                - getStartLocation().y);
        setStartLocation(newStart);
        // sourceRectangle and compoundSrcRect need to be updated as well when
        // auto-scrolling
        if (sourceRectangle != null)
            sourceRectangle.translate(delta);
        if (compoundSrcRect != null)
            compoundSrcRect.translate(delta);
    }

    /**
     * @see org.eclipse.gef.tools.TargetingTool#setAutoexposeHelper(org.eclipse.gef.AutoexposeHelper)
     */
    @Override
    protected void setAutoexposeHelper(AutoexposeHelper helper) {
        super.setAutoexposeHelper(helper);
        if (helper != null && sourceRelativeStartPoint == null
                && isInDragInProgress()) {
            IFigure figure = ((GraphicalEditPart) getSourceEditPart())
                    .getFigure();
            sourceRelativeStartPoint = new PrecisionPoint(getStartLocation());
            figure.translateToRelative(sourceRelativeStartPoint);
        }
    }

    /**
     * Enables cloning if the value is true.
     * 
     * @param cloneActive
     *            <code>true</code> if cloning should be active
     */
    protected void setCloneActive(boolean cloneActive) {
        if (this.cloneActive == cloneActive)
            return;
        eraseSourceFeedback();
        eraseTargetFeedback();
        this.cloneActive = cloneActive;
    }

    /**
     * Extended to update the current snap-to strategy.
     * 
     * @see org.eclipse.gef.tools.TargetingTool#setTargetEditPart(org.eclipse.gef.EditPart)
     */
    @Override
    protected void setTargetEditPart(EditPart editpart) {
        if (getTargetEditPart() == editpart)
            return;
        super.setTargetEditPart(editpart);
        snapToHelper = null;
        if (getTargetEditPart() != null && getOperationSet().size() > 0)
            snapToHelper = getTargetEditPart().getAdapter(
                    SnapToHelper.class);
    }

    /**
     * Asks the edit parts in the {@link AbstractTool#getOperationSet()
     * operation set} to show source feedback.
     */
    protected void showSourceFeedback() {
        List editParts = getOperationSet();
        for (int i = 0; i < editParts.size(); i++) {
            EditPart editPart = (EditPart) editParts.get(i);
            editPart.showSourceFeedback(getTargetRequest());
        }
        setFlag(FLAG_SOURCE_FEEDBACK, true);
    }

    /**
     * Extended to activate cloning and to update the captured source dimensions
     * when applicable.
     * 
     * @see org.eclipse.gef.tools.AbstractTool#setState(int)
     */
    @Override
    protected void setState(int state) {
        boolean check = isInState(STATE_INITIAL);
        super.setState(state);

        if (isInState(STATE_ACCESSIBLE_DRAG | STATE_DRAG_IN_PROGRESS
                | STATE_ACCESSIBLE_DRAG_IN_PROGRESS)) {
            if (getCurrentInput().isModKeyDown(MODIFIER_CLONE)) {
                setCloneActive(true);
                handleDragInProgress();
            }
        }

        if (check
                && isInState(STATE_DRAG | STATE_ACCESSIBLE_DRAG
                        | STATE_ACCESSIBLE_DRAG_IN_PROGRESS))
            captureSourceDimensions();
    }

    /**
     * Calls {@link #repairStartLocation()} in case auto scroll is being
     * performed. Updates the request with the current
     * {@link AbstractTool#getOperationSet() operation set}, move delta,
     * location and type.
     * 
     * @see org.eclipse.gef.tools.TargetingTool#updateTargetRequest()
     */
    @Override
    protected void updateTargetRequest() {
        repairStartLocation();
        ChangeBoundsRequest request = (ChangeBoundsRequest) getTargetRequest();
        request.setEditParts(getOperationSet());
        Dimension delta = getDragMoveDelta();

        request.setConstrainedMove(getCurrentInput().isModKeyDown(
                MODIFIER_CONSTRAINED_MOVE));
        request.setSnapToEnabled(!getCurrentInput().isModKeyDown(
                MODIFIER_NO_SNAPPING));

        // constrains the move to dx=0, dy=0, or dx=dy if shift is depressed
        if (request.isConstrainedMove()) {
            float ratio = 0;

            if (delta.width != 0)
                ratio = (float) delta.height / (float) delta.width;

            ratio = Math.abs(ratio);
            if (ratio > 0.5 && ratio < 1.5) {
                if (Math.abs(delta.height) > Math.abs(delta.width)) {
                    if (delta.height > 0)
                        delta.height = Math.abs(delta.width);
                    else
                        delta.height = -Math.abs(delta.width);
                } else {
                    if (delta.width > 0)
                        delta.width = Math.abs(delta.height);
                    else
                        delta.width = -Math.abs(delta.height);
                }
            } else {
                if (Math.abs(delta.width) > Math.abs(delta.height))
                    delta.height = 0;
                else
                    delta.width = 0;
            }
        }

        Point moveDelta = new Point(delta.width, delta.height);
        request.getExtendedData().clear();
        request.setMoveDelta(moveDelta);
        snapPoint(request);

        request.setLocation(getLocation());
        request.setType(getCommandName());
    }

    /**
     * This method can be overridden by clients to customize the snapping
     * behavior.
     * 
     * @param request
     *            the <code>ChangeBoundsRequest</code> from which the move delta
     *            can be extracted and updated
     * @since 3.4
     */
    protected void snapPoint(ChangeBoundsRequest request) {
        Point moveDelta = request.getMoveDelta();
        if (snapToHelper != null && request.isSnapToEnabled()) {
            PrecisionRectangle baseRect = sourceRectangle.getPreciseCopy();
            PrecisionRectangle jointRect = compoundSrcRect.getPreciseCopy();
            baseRect.translate(moveDelta);
            jointRect.translate(moveDelta);

            PrecisionPoint preciseDelta = new PrecisionPoint(moveDelta);
            snapToHelper.snapPoint(request, PositionConstants.HORIZONTAL
                    | PositionConstants.VERTICAL, new PrecisionRectangle[] {
                    baseRect, jointRect }, preciseDelta);
            request.setMoveDelta(preciseDelta);
        }
    }

}
