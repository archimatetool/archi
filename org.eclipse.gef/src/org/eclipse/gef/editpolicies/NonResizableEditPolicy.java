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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.FocusBorder;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.DragTracker;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.handles.AbstractHandle;
import org.eclipse.gef.handles.HandleBounds;
import org.eclipse.gef.handles.NonResizableHandleKit;
import org.eclipse.gef.handles.ResizableHandleKit;
import org.eclipse.gef.requests.AlignmentRequest;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.tools.DragEditPartsTracker;
import org.eclipse.gef.tools.ResizeTracker;
import org.eclipse.gef.tools.SelectEditPartTracker;

/**
 * Provide support for selecting and positioning a non-resizable editpart.
 * Selection is indicated via four square handles at each corner of the
 * editpart's figure, and a rectangular handle that outlines the editpart with a
 * 1-pixel black line. All of these handles return
 * {@link org.eclipse.gef.tools.DragEditPartsTracker}s, which allows the current
 * selection to be dragged.
 * <P>
 * During feedback, a rectangle filled using XOR and outlined with dashes is
 * drawn. Subclasses can tailor the feedback.
 * 
 * @author hudsonr
 */
@SuppressWarnings("rawtypes")
public class NonResizableEditPolicy extends SelectionHandlesEditPolicy {

    private IFigure focusRect;
    private IFigure feedback;
    private boolean isDragAllowed = true;

    /**
     * Creates the figure used for feedback.
     * 
     * @return the new feedback figure
     */
    protected IFigure createDragSourceFeedbackFigure() {
        // Use a ghost rectangle for feedback
        RectangleFigure r = new RectangleFigure();
        FigureUtilities.makeGhostShape(r);
        r.setLineStyle(Graphics.LINE_DOT);
        r.setForegroundColor(ColorConstants.white);
        r.setBounds(getInitialFeedbackBounds());
        r.validate();
        addFeedback(r);
        return r;
    }

    /**
     * @see org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy#createSelectionHandles()
     */
    @Override
    protected List createSelectionHandles() {
        List list = new ArrayList();
        createMoveHandle(list);
        createDragHandle(list, PositionConstants.NORTH_EAST);
        createDragHandle(list, PositionConstants.NORTH_WEST);
        createDragHandle(list, PositionConstants.SOUTH_EAST);
        createDragHandle(list, PositionConstants.SOUTH_WEST);
        return list;
    }

    /**
     * Creates a 'resize'/'drag' handle, which uses a
     * {@link DragEditPartsTracker} in case {@link #isDragAllowed()} returns
     * true, and a {@link SelectEditPartTracker} otherwise.
     * 
     * @param handles
     *            The list of handles to add the resize handle to
     * @param direction
     *            A position constant indicating the direction to create the
     *            handle for
     * @since 3.7
     */
    protected void createDragHandle(List handles, int direction) {
        if (isDragAllowed()) {
            // display 'resize' handles to allow dragging (drag tracker)
            NonResizableHandleKit
                    .addHandle((GraphicalEditPart) getHost(), handles,
                            direction, getDragTracker(), Cursors.SIZEALL);
        } else {
            // display 'resize' handles to indicate selection only (selection
            // tracker)
            NonResizableHandleKit
                    .addHandle((GraphicalEditPart) getHost(), handles,
                            direction, getSelectTracker(), Cursors.ARROW);
        }
    }

    /**
     * Returns a selection tracker to use by a selection handle.
     * 
     * @return a new {@link ResizeTracker}
     * @since 3.7
     */
    protected SelectEditPartTracker getSelectTracker() {
        return new SelectEditPartTracker(getHost());
    }

    /**
     * Returns a drag tracker to use by a resize handle.
     * 
     * @return a new {@link ResizeTracker}
     * @since 3.7
     */
    protected DragEditPartsTracker getDragTracker() {
        return new DragEditPartsTracker(getHost());
    }

    /**
     * Creates a 'move' handle, which uses a {@link DragEditPartsTracker} in
     * case {@link #isDragAllowed()} returns true, and a
     * {@link SelectEditPartTracker} otherwise.
     * 
     * @param handles
     *            The list of handles to add the move handle to.
     * @since 3.7
     */
    protected void createMoveHandle(List handles) {
        if (isDragAllowed()) {
            // display 'move' handle to allow dragging
            ResizableHandleKit.addMoveHandle((GraphicalEditPart) getHost(),
                    handles, getDragTracker(), Cursors.SIZEALL);
        } else {
            // display 'move' handle only to indicate selection
            ResizableHandleKit.addMoveHandle((GraphicalEditPart) getHost(),
                    handles, getSelectTracker(), Cursors.ARROW);
        }
    }

    /**
     * @see org.eclipse.gef.EditPolicy#deactivate()
     */
    @Override
    public void deactivate() {
        if (feedback != null) {
            removeFeedback(feedback);
            feedback = null;
        }
        hideFocus();
        super.deactivate();
    }

    /**
     * Erases drag feedback. This method called whenever an erase feedback
     * request is received of the appropriate type.
     * 
     * @param request
     *            the request
     */
    protected void eraseChangeBoundsFeedback(ChangeBoundsRequest request) {
        if (feedback != null) {
            removeFeedback(feedback);
        }
        feedback = null;
    }

    /**
     * @see org.eclipse.gef.EditPolicy#eraseSourceFeedback(org.eclipse.gef.Request)
     */
    @Override
    public void eraseSourceFeedback(Request request) {
        if ((REQ_MOVE.equals(request.getType()) && isDragAllowed())
                || REQ_CLONE.equals(request.getType())
                || REQ_ADD.equals(request.getType()))
            eraseChangeBoundsFeedback((ChangeBoundsRequest) request);
    }

    /**
     * @see org.eclipse.gef.EditPolicy#getCommand(org.eclipse.gef.Request)
     */
    @Override
    public Command getCommand(Request request) {
        Object type = request.getType();

        if (REQ_MOVE.equals(type) && isDragAllowed())
            return getMoveCommand((ChangeBoundsRequest) request);
        if (REQ_ORPHAN.equals(type))
            return getOrphanCommand(request);
        if (REQ_ALIGN.equals(type))
            return getAlignCommand((AlignmentRequest) request);

        return null;
    }

    /**
     * Lazily creates and returns the feedback figure used during drags.
     * 
     * @return the feedback figure
     */
    protected IFigure getDragSourceFeedbackFigure() {
        if (feedback == null)
            feedback = createDragSourceFeedbackFigure();
        return feedback;
    }

    /**
     * Returns the command contribution to an alignment request
     * 
     * @param request
     *            the alignment request
     * @return the contribution to the alignment
     */
    protected Command getAlignCommand(AlignmentRequest request) {
        AlignmentRequest req = new AlignmentRequest(REQ_ALIGN_CHILDREN);
        req.setEditParts(getHost());
        req.setAlignment(request.getAlignment());
        req.setAlignmentRectangle(request.getAlignmentRectangle());
        return getHost().getParent().getCommand(req);
    }

    /**
     * Returns the bounds of the host's figure by reference to be used to
     * calculate the initial location of the feedback. The returned Rectangle
     * should not be modified. Uses handle bounds if available.
     * 
     * @return the host figure's bounding Rectangle
     */
    protected Rectangle getInitialFeedbackBounds() {
        if (((GraphicalEditPart) getHost()).getFigure() instanceof HandleBounds)
            return ((HandleBounds) ((GraphicalEditPart) getHost()).getFigure())
                    .getHandleBounds();
        return ((GraphicalEditPart) getHost()).getFigure().getBounds();
    }

    /**
     * Returns the command contribution to a change bounds request. The
     * implementation actually redispatches the request to the host's parent
     * editpart as a {@link RequestConstants#REQ_MOVE_CHILDREN} request. The
     * parent's contribution is returned.
     * 
     * @param request
     *            the change bounds request
     * @return the command contribution to the request
     */
    protected Command getMoveCommand(ChangeBoundsRequest request) {
        ChangeBoundsRequest req = new ChangeBoundsRequest(REQ_MOVE_CHILDREN);
        req.setEditParts(getHost());

        req.setMoveDelta(request.getMoveDelta());
        req.setSizeDelta(request.getSizeDelta());
        req.setLocation(request.getLocation());
        req.setExtendedData(request.getExtendedData());
        return getHost().getParent().getCommand(req);
    }

    /**
     * Subclasses may override to contribute to the orphan request. By default,
     * <code>null</code> is returned to indicate no participation. Orphan
     * requests are not forwarded to the host's parent here. That is done in
     * {@link ComponentEditPolicy}. So, if the host has a component editpolicy,
     * then the parent will already have a chance to contribute.
     * 
     * @param req
     *            the orphan request
     * @return <code>null</code> by default
     */
    protected Command getOrphanCommand(Request req) {
        return null;
    }

    /**
     * Hides the focus rectangle displayed in <code>showFocus()</code>.
     * 
     * @see #showFocus()
     * @see org.eclipse.gef.editpolicies.SelectionEditPolicy#hideFocus()
     */
    @Override
    protected void hideFocus() {
        if (focusRect != null)
            removeFeedback(focusRect);
        focusRect = null;
    }

    /**
     * Returns true if this EditPolicy allows its EditPart to be dragged.
     * 
     * @return true if the EditPart can be dragged.
     */
    public boolean isDragAllowed() {
        return isDragAllowed;
    }

    /**
     * Sets the dragability of the EditPolicy to the given value. If the value
     * is false, the EditPolicy should not allow its EditPart to be dragged.
     * 
     * @param isDragAllowed
     *            whether or not the EditPolicy can be dragged.
     */
    public void setDragAllowed(boolean isDragAllowed) {
        if (isDragAllowed == this.isDragAllowed)
            return;
        this.isDragAllowed = isDragAllowed;
    }

    /**
     * Shows or updates feedback for a change bounds request.
     * 
     * @param request
     *            the request
     */
    protected void showChangeBoundsFeedback(ChangeBoundsRequest request) {
        IFigure feedback = getDragSourceFeedbackFigure();

        PrecisionRectangle rect = new PrecisionRectangle(
                getInitialFeedbackBounds().getCopy());
        getHostFigure().translateToAbsolute(rect);
        rect.translate(request.getMoveDelta());
        rect.resize(request.getSizeDelta());

        feedback.translateToRelative(rect);
        feedback.setBounds(rect);
        feedback.validate();
    }

    /**
     * Shows a focus rectangle around the host's figure. The focus rectangle is
     * expanded by 5 pixels from the figure's bounds.
     * 
     * @see org.eclipse.gef.editpolicies.SelectionEditPolicy#showFocus()
     */
    @Override
    protected void showFocus() {
        focusRect = new AbstractHandle((GraphicalEditPart) getHost(),
                new Locator() {
                    @Override
                    public void relocate(IFigure target) {
                        IFigure figure = getHostFigure();
                        Rectangle r;
                        if (figure instanceof HandleBounds)
                            r = ((HandleBounds) figure).getHandleBounds()
                                    .getCopy();
                        else
                            r = getHostFigure().getBounds().getResized(-1, -1);
                        getHostFigure().translateToAbsolute(r);
                        target.translateToRelative(r);
                        target.setBounds(r.expand(5, 5).resize(1, 1));
                    }
                }) {
            {
                setBorder(new FocusBorder());
            }

            @Override
            protected DragTracker createDragTracker() {
                return null;
            }
        };
        addFeedback(focusRect);
    }

    /**
     * Calls other methods as appropriate.
     * 
     * @see org.eclipse.gef.EditPolicy#showSourceFeedback(org.eclipse.gef.Request)
     */
    @Override
    public void showSourceFeedback(Request request) {
        if ((REQ_MOVE.equals(request.getType()) && isDragAllowed())
                || REQ_ADD.equals(request.getType())
                || REQ_CLONE.equals(request.getType()))
            showChangeBoundsFeedback((ChangeBoundsRequest) request);
    }

    /**
     * Returns <code>true</code> for move, align, add, and orphan request types.
     * This method is never called for some of these types, but they are
     * included for possible future use.
     * 
     * @see org.eclipse.gef.EditPolicy#understandsRequest(org.eclipse.gef.Request)
     */
    @Override
    public boolean understandsRequest(Request request) {
        if (REQ_MOVE.equals(request.getType()))
            return isDragAllowed();
        else if (REQ_CLONE.equals(request.getType())
                || REQ_ADD.equals(request.getType())
                || REQ_ORPHAN.equals(request.getType())
                || REQ_ALIGN.equals(request.getType()))
            return true;
        return super.understandsRequest(request);
    }

}
