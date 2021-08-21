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

import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Translatable;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartListener;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

/**
 * Provides support for interacting with children
 * <code>GraphicalEditParts</code> with the host figure's current
 * {@link org.eclipse.draw2d.LayoutManager}.
 * <P>
 * LayoutEditPolicies are responsible for moving, resizing, re-parenting, and
 * creating children. The should provide <code>Commands</code> for all of these
 * operations. Feedback on the container can also be useful for some layouts
 * like grids.
 * <P>
 * LayoutEditPolicies will decorate the host's children with "satellite"
 * EditPolicies. These policies are installed using the
 * {@link EditPolicy#PRIMARY_DRAG_ROLE}. Simple layouts will use either
 * {@link ResizableEditPolicy} or {@link NonResizableEditPolicy}, depending on
 * how the LayoutManager works, and/or attributes of the child EditPart.
 * 
 * @author rhudson
 * @author msorens
 * @author anyssen
 */
@SuppressWarnings("rawtypes")
public abstract class LayoutEditPolicy extends GraphicalEditPolicy {

    private IFigure sizeOnDropFeedback;

    private EditPartListener listener;

    /**
     * Extends activate() to allow proper decoration of children.
     * 
     * @see org.eclipse.gef.EditPolicy#activate()
     */
    @Override
    public void activate() {
        setListener(createListener());
        decorateChildren();
        super.activate();
    }

    /**
     * Returns the "satellite" EditPolicy used to decorate the child.
     * 
     * @param child
     *            the child EditPart
     * @return an EditPolicy to be installed as the
     *         {@link EditPolicy#PRIMARY_DRAG_ROLE}
     */
    protected abstract EditPolicy createChildEditPolicy(EditPart child);

    /**
     * creates the EditPartListener for observing when children are added to the
     * host.
     * 
     * @return EditPartListener
     */
    protected EditPartListener createListener() {
        return new EditPartListener.Stub() {
            @Override
            public void childAdded(EditPart child, int index) {
                decorateChild(child);
            }
        };
    }

    /**
     * Override to provide custom feedback figure for the given create request.
     * 
     * @param createRequest
     *            the create request
     * @return custom feedback figure
     */
    protected IFigure createSizeOnDropFeedback(CreateRequest createRequest) {
        return null;
    }

    /**
     * Overrides deactivate to remove the EditPartListener.
     * 
     * @see org.eclipse.gef.EditPolicy#deactivate()
     */
    @Override
    public void deactivate() {
        if (sizeOnDropFeedback != null) {
            removeFeedback(sizeOnDropFeedback);
            sizeOnDropFeedback = null;
        }
        setListener(null);
        super.deactivate();
    }

    /**
     * Decorates the child with a {@link EditPolicy#PRIMARY_DRAG_ROLE} such as
     * {@link ResizableEditPolicy}.
     * 
     * @param child
     *            the child EditPart being decorated
     */
    protected void decorateChild(EditPart child) {
        EditPolicy policy = createChildEditPolicy(child);
        child.installEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE, policy);
    }

    /**
     * Decorates all existing children. This method is called on activation.
     */
    protected void decorateChildren() {
        List children = getHost().getChildren();
        for (int i = 0; i < children.size(); i++)
            decorateChild((EditPart) children.get(i));
    }

    /**
     * Erases target layout feedback. This method is the inverse of
     * {@link #showLayoutTargetFeedback(Request)}.
     * 
     * @param request
     *            the Request
     */
    protected void eraseLayoutTargetFeedback(Request request) {
    }

    /**
     * Erases size-on-drop feedback used during creation.
     * 
     * @param request
     *            the Request
     */
    protected void eraseSizeOnDropFeedback(Request request) {
        if (sizeOnDropFeedback != null) {
            removeFeedback(sizeOnDropFeedback);
            sizeOnDropFeedback = null;
        }
    }

    /**
     * Calls two more specific methods depending on the Request.
     * 
     * @see org.eclipse.gef.EditPolicy#eraseTargetFeedback(Request)
     */
    @Override
    public void eraseTargetFeedback(Request request) {
        if (REQ_ADD.equals(request.getType())
                || REQ_MOVE.equals(request.getType())
                || REQ_RESIZE_CHILDREN.equals(request.getType())
                || REQ_CREATE.equals(request.getType())
                || REQ_CLONE.equals(request.getType()))
            eraseLayoutTargetFeedback(request);

        if (REQ_CREATE.equals(request.getType()))
            eraseSizeOnDropFeedback(request);
    }

    /**
     * Override to return the <code>Command</code> to perform an
     * {@link RequestConstants#REQ_ADD ADD}. By default, <code>null</code> is
     * returned.
     * 
     * @param request
     *            the ADD Request
     * @return A command to perform the ADD.
     */
    protected Command getAddCommand(Request request) {
        return null;
    }

    /**
     * Override to contribute to clone requests.
     * 
     * @param request
     *            the clone request
     * @return the command contribution to the clone
     */
    protected Command getCloneCommand(ChangeBoundsRequest request) {
        return null;
    }

    /**
     * Factors incoming requests into various specific methods.
     * 
     * @see org.eclipse.gef.EditPolicy#getCommand(Request)
     */
    @Override
    public Command getCommand(Request request) {
        if (REQ_DELETE_DEPENDANT.equals(request.getType()))
            return getDeleteDependantCommand(request);

        if (REQ_ADD.equals(request.getType()))
            return getAddCommand(request);

        if (REQ_ORPHAN_CHILDREN.equals(request.getType()))
            return getOrphanChildrenCommand(request);

        if (REQ_MOVE_CHILDREN.equals(request.getType()))
            return getMoveChildrenCommand(request);

        if (REQ_CLONE.equals(request.getType()))
            return getCloneCommand((ChangeBoundsRequest) request);

        if (REQ_CREATE.equals(request.getType()))
            return getCreateCommand((CreateRequest) request);

        return null;
    }

    /**
     * Returns the <code>Command</code> to perform a create.
     * 
     * @param request
     *            the CreateRequest
     * @return a Command to perform a create
     */
    protected abstract Command getCreateCommand(CreateRequest request);

    /**
     * Returns any insets that need to be applied to the creation feedback's
     * bounds.
     * 
     * @param request
     *            the create request
     * @return insets, if necessary
     */
    protected Insets getCreationFeedbackOffset(CreateRequest request) {
        return new Insets();
    }

    /**
     * Returns the <code>Command</code> to delete a child. This method does not
     * get called unless the child forwards an additional request to the
     * container editpart.
     * 
     * @param request
     *            the Request
     * @return the Command to delete the child
     */
    protected Command getDeleteDependantCommand(Request request) {
        return null;
    }

    /**
     * Returns the host's {@link GraphicalEditPart#getContentPane() contentPane}
     * . The contentPane is the Figure which parents the childrens' figures. It
     * is also the figure which has the LayoutManager that corresponds to this
     * EditPolicy. All operations should be interpreted with respect to this
     * figure.
     * 
     * @return the Figure that owns the corresponding <code>LayoutManager</code>
     */
    protected IFigure getLayoutContainer() {
        return ((GraphicalEditPart) getHost()).getContentPane();
    }

    /**
     * Returns the <code>Command</code> to move a group of children.
     * 
     * @param request
     *            the Request
     * @return the Command to perform the move
     */
    protected abstract Command getMoveChildrenCommand(Request request);

    /**
     * Returns the <code>Command</code> to orphan a group of children. The
     * contribution to orphan might contain two parts, both of which are
     * optional. The first part is to actually remove the children from their
     * existing parent. Some application models will perform an orphan
     * implicitly when the children are added to their new parent. The second
     * part is to perform some adjustments on the remaining children. For
     * example, a Table layout might simplify itself by collapsing any unused
     * columns and rows.
     * 
     * @param request
     *            the Request
     * @return <code>null</code> or a Command to perform an orphan
     */
    protected Command getOrphanChildrenCommand(Request request) {
        return null;
    }

    /**
     * Lazily creates and returns the Figure to use for size-on-drop feedback.
     * 
     * @param createRequest
     *            the createRequest
     * @return the size-on-drop feedback figure
     */
    protected IFigure getSizeOnDropFeedback(CreateRequest createRequest) {
        if (sizeOnDropFeedback == null)
            sizeOnDropFeedback = createSizeOnDropFeedback(createRequest);

        return getSizeOnDropFeedback();
    }

    /**
     * Lazily creates and returns the Figure to use for size-on-drop feedback.
     * 
     * @return the size-on-drop feedback figure
     */
    protected IFigure getSizeOnDropFeedback() {
        if (sizeOnDropFeedback == null) {
            sizeOnDropFeedback = new RectangleFigure();
            FigureUtilities.makeGhostShape((Shape) sizeOnDropFeedback);
            ((Shape) sizeOnDropFeedback).setLineStyle(Graphics.LINE_DASHDOT);
            sizeOnDropFeedback.setForegroundColor(ColorConstants.white);
            addFeedback(sizeOnDropFeedback);
        }
        return sizeOnDropFeedback;
    }

    /**
     * Returns the <i>host</i> if the Request is an ADD, MOVE, or CREATE.
     * 
     * @see org.eclipse.gef.EditPolicy#getTargetEditPart(Request)
     */
    @Override
    public EditPart getTargetEditPart(Request request) {
        if (REQ_ADD.equals(request.getType())
                || REQ_MOVE.equals(request.getType())
                || REQ_CREATE.equals(request.getType())
                || REQ_CLONE.equals(request.getType()))
            return getHost();
        return null;
    }

    /**
     * Sets the EditPartListener used to decorate new children. If the listener
     * is currently set, it will be unhooked. If the new value is not
     * <code>null</code>, it will be hooked.
     * <P>
     * The listener must be remembered in case this EditPolicy is removed from
     * the host and replaced with another LayoutEditPolicy.
     * 
     * @param listener
     *            <code>null</code> or the listener.
     */
    protected void setListener(EditPartListener listener) {
        if (this.listener != null)
            getHost().removeEditPartListener(this.listener);
        this.listener = listener;
        if (this.listener != null)
            getHost().addEditPartListener(this.listener);
    }

    /**
     * Shows target layout feedback. During <i>moves</i>, <i>reparents</i>, and
     * <i>creation</i>, this method is called to allow the LayoutEditPolicy to
     * temporarily show features of its layout that will help the User
     * understand what will happen if the operation is performed in the current
     * location.
     * <P>
     * By default, no feedback is shown.
     * 
     * @param request
     *            the Request
     * @see #eraseLayoutTargetFeedback(Request)
     */
    protected void showLayoutTargetFeedback(Request request) {
    }

    /**
     * Shows size-on-drop feedback during creation.
     * 
     * @param request
     *            the CreateRequest
     */
    protected void showSizeOnDropFeedback(CreateRequest request) {
    }

    /**
     * Factors feedback requests into two more specific methods.
     * 
     * @see org.eclipse.gef.EditPolicy#showTargetFeedback(Request)
     */
    @Override
    public void showTargetFeedback(Request request) {
        if (REQ_ADD.equals(request.getType())
                || REQ_CLONE.equals(request.getType())
                || REQ_MOVE.equals(request.getType())
                || REQ_RESIZE_CHILDREN.equals(request.getType())
                || REQ_CREATE.equals(request.getType()))
            showLayoutTargetFeedback(request);

        if (REQ_CREATE.equals(request.getType())) {
            CreateRequest createReq = (CreateRequest) request;
            if (createReq.getSize() != null) {
                showSizeOnDropFeedback(createReq);
            }
        }
    }

    /**
     * Removes the decoration added in {@link #decorateChild(EditPart)}.
     * 
     * @param child
     *            the child whose decoration is being removed.
     */
    protected void undecorateChild(EditPart child) {
        child.removeEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE);
    }

    /**
     * Removes all decorations added by {@link #decorateChildren()}.
     */
    protected void undecorateChildren() {
        List children = getHost().getChildren();
        for (int i = 0; i < children.size(); i++)
            undecorateChild((EditPart) children.get(i));
    }

    /**
     * Returns the layout's origin relative to the
     * {@link LayoutEditPolicy#getLayoutContainer()}. In other words, what Point
     * on the parent Figure does the LayoutManager use a reference when
     * generating the child figure's bounds from the child's constraint.
     * <P>
     * By default, it is assumed that the layout manager positions children
     * relative to the client area of the layout container. Thus, when
     * processing Viewer-relative Points or Rectangles, the clientArea's
     * location (top-left corner) will be subtracted from the Point/Rectangle,
     * resulting in an offset from the LayoutOrigin.
     * 
     * @return Point
     * @since 3.7 Moved up from ConstrainedLayoutEditPolicy
     */
    protected Point getLayoutOrigin() {
        return getLayoutContainer().getClientArea().getLocation();
    }

    /**
     * Translates a {@link Translatable} in absolute coordinates to be
     * layout-relative, i.e. relative to the {@link #getLayoutContainer()}'s
     * origin, which is obtained via {@link #getLayoutOrigin()}.
     * 
     * @param t
     *            the Translatable in absolute coordinates to be translated to
     *            layout-relative coordinates.
     * @since 3.7
     */
    protected void translateFromAbsoluteToLayoutRelative(Translatable t) {
        IFigure figure = getLayoutContainer();
        figure.translateToRelative(t);
        figure.translateFromParent(t);
        Point negatedLayoutOrigin = getLayoutOrigin().getNegated();
        t.performTranslate(negatedLayoutOrigin.x, negatedLayoutOrigin.y);
    }

    /**
     * Translates a {@link Translatable} in layout-relative coordinates, i.e.
     * relative to {@link #getLayoutContainer()}'s origin which is obtained via
     * {@link #getLayoutOrigin()}, into absolute coordinates.
     * 
     * @param t
     *            the Translatable in layout-relative coordinates to be
     *            translated into absolute coordinates.
     * @since 3.7
     */
    protected void translateFromLayoutRelativeToAbsolute(Translatable t) {
        IFigure figure = getLayoutContainer();
        Point layoutOrigin = getLayoutOrigin();
        t.performTranslate(layoutOrigin.x, layoutOrigin.y);
        figure.translateToParent(t);
        figure.translateToAbsolute(t);
    }

}
