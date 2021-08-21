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

import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.PositionConstants;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.handles.ResizableHandleKit;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.tools.ResizeTracker;

/**
 * Provides support for selecting, positioning, and resizing an edit part.
 * Selection is indicated via eight square handles along the editpart's figure,
 * and a rectangular handle that outlines the edit part with a 1-pixel black
 * line. The eight square handles will resize the current selection in the eight
 * primary directions. The rectangular handle will drag the current selection
 * using a {@link org.eclipse.gef.tools.DragEditPartsTracker}.
 * <p>
 * During feedback, a rectangle filled using XOR and outlined with dashes is
 * drawn. Subclasses may tailor the feedback.
 * 
 * @author hudsonr
 * @author msorens
 * @author anyssen
 */
@SuppressWarnings("rawtypes")
public class ResizableEditPolicy extends NonResizableEditPolicy {

    private int resizeDirections = PositionConstants.NSEW;

    /**
     * Constructs a new {@link ResizableEditPolicy}.
     * 
     * @since 3.7
     */
    public ResizableEditPolicy() {
    }

    /**
     * @see org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy#createSelectionHandles()
     */
    @Override
    protected List createSelectionHandles() {
        if (resizeDirections == PositionConstants.NONE) {
            // non resizable, so delegate to super implementation
            return super.createSelectionHandles();
        }

        // resizable in at least one direction
        List list = new ArrayList();
        createMoveHandle(list);
        createResizeHandle(list, PositionConstants.NORTH);
        createResizeHandle(list, PositionConstants.EAST);
        createResizeHandle(list, PositionConstants.SOUTH);
        createResizeHandle(list, PositionConstants.WEST);
        createResizeHandle(list, PositionConstants.SOUTH_EAST);
        createResizeHandle(list, PositionConstants.SOUTH_WEST);
        createResizeHandle(list, PositionConstants.NORTH_WEST);
        createResizeHandle(list, PositionConstants.NORTH_EAST);
        return list;
    }

    /**
     * Creates a 'resize' handle, which uses a {@link ResizeTracker} in case
     * resizing is allowed in the respective direction, otherwise returns a drag
     * handle by delegating to
     * {@link NonResizableEditPolicy#createDragHandle(List, int)}.
     * 
     * @param handles
     *            The list of handles to add the resize handle to
     * @param direction
     *            A position constant indicating the direction to create the
     *            handle for
     * @since 3.7
     */
    protected void createResizeHandle(List handles, int direction) {
        if ((resizeDirections & direction) == direction) {
            ResizableHandleKit.addHandle((GraphicalEditPart) getHost(),
                    handles, direction, getResizeTracker(direction), Cursors
                            .getDirectionalCursor(direction, getHostFigure()
                                    .isMirrored()));
        } else {
            // display 'resize' handle to allow dragging or indicate selection
            // only
            createDragHandle(handles, direction);
        }
    }

    /**
     * Returns a resize tracker for the given direction to be used by a resize
     * handle.
     * 
     * @param direction
     *            the resize direction for the {@link ResizeTracker}.
     * @return a new {@link ResizeTracker}
     * @since 3.7
     */
    protected ResizeTracker getResizeTracker(int direction) {
        return new ResizeTracker((GraphicalEditPart) getHost(), direction);
    }

    /**
     * Dispatches erase requests to more specific methods.
     * 
     * @see org.eclipse.gef.EditPolicy#eraseSourceFeedback(org.eclipse.gef.Request)
     */
    @Override
    public void eraseSourceFeedback(Request request) {
        if (REQ_RESIZE.equals(request.getType()))
            eraseChangeBoundsFeedback((ChangeBoundsRequest) request);
        else
            super.eraseSourceFeedback(request);
    }

    /**
     * @see org.eclipse.gef.EditPolicy#getCommand(org.eclipse.gef.Request)
     */
    @Override
    public Command getCommand(Request request) {
        if (REQ_RESIZE.equals(request.getType())) {
            return getResizeCommand((ChangeBoundsRequest) request);
        }
        return super.getCommand(request);
    }

    /**
     * Returns the command contribution for the given resize request. By
     * default, the request is re-dispatched to the host's parent as a
     * {@link org.eclipse.gef.RequestConstants#REQ_RESIZE_CHILDREN}. The
     * parent's edit policies determine how to perform the resize based on the
     * layout manager in use.
     * 
     * @param request
     *            the resize request
     * @return the command contribution obtained from the parent
     */
    protected Command getResizeCommand(ChangeBoundsRequest request) {
        ChangeBoundsRequest req = new ChangeBoundsRequest(REQ_RESIZE_CHILDREN);
        req.setEditParts(getHost());
        req.setCenteredResize(request.isCenteredResize());
        req.setConstrainedMove(request.isConstrainedMove());
        req.setConstrainedResize(request.isConstrainedResize());
        req.setSnapToEnabled(request.isSnapToEnabled());
        req.setMoveDelta(request.getMoveDelta());
        req.setSizeDelta(request.getSizeDelta());
        req.setLocation(request.getLocation());
        req.setExtendedData(request.getExtendedData());
        req.setResizeDirection(request.getResizeDirection());
        return getHost().getParent().getCommand(req);
    }

    /**
     * Sets the directions in which handles should allow resizing. Valid values
     * are bit-wise combinations of:
     * <UL>
     * <LI>{@link PositionConstants#NORTH}
     * <LI>{@link PositionConstants#SOUTH}
     * <LI>{@link PositionConstants#EAST}
     * <LI>{@link PositionConstants#WEST}
     * </UL>
     * 
     * @param newDirections
     *            the direction in which resizing is allowed
     */
    public void setResizeDirections(int newDirections) {
        resizeDirections = newDirections;
    }

    /**
     * @see org.eclipse.gef.EditPolicy#showSourceFeedback(org.eclipse.gef.Request)
     */
    @Override
    public void showSourceFeedback(Request request) {
        if (REQ_RESIZE.equals(request.getType())) {
            showChangeBoundsFeedback((ChangeBoundsRequest) request);
        } else {
            super.showSourceFeedback(request);
        }
    }

    /**
     * @see org.eclipse.gef.EditPolicy#understandsRequest(org.eclipse.gef.Request)
     */
    @Override
    public boolean understandsRequest(Request request) {
        if (REQ_RESIZE.equals(request.getType())) {
            // check all resize directions of the request are supported
            int resizeDirections = ((ChangeBoundsRequest) request)
                    .getResizeDirection();
            return (resizeDirections & getResizeDirections()) == resizeDirections;
        }
        return super.understandsRequest(request);
    }

    /**
     * Returns the directions in which resizing should be allowed
     * 
     * Valid values are bit-wise combinations of:
     * <UL>
     * <LI>{@link PositionConstants#NORTH}
     * <LI>{@link PositionConstants#SOUTH}
     * <LI>{@link PositionConstants#EAST}
     * <LI>{@link PositionConstants#WEST}
     * </UL>
     * or {@link PositionConstants#NONE}.
     * 
     */
    public int getResizeDirections() {
        return resizeDirections;
    }
}
