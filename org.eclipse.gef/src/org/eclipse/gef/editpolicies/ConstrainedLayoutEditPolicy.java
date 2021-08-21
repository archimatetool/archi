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

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Translatable;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.requests.AlignmentRequest;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

/**
 * For use with <code>LayoutManager</code> that require a <i>constraint</i>.
 * ConstrainedLayoutEditPolicy understands
 * {@link RequestConstants#REQ_ALIGN_CHILDREN} in addition to the Requests
 * handled in the superclass.
 * 
 * @author hudsonr
 * @author anyssen
 * @since 2.0
 */
@SuppressWarnings("rawtypes")
public abstract class ConstrainedLayoutEditPolicy extends LayoutEditPolicy {

    /**
     * Constant being used to indicate that upon creation (or during move) a
     * size was not specified.
     * 
     * @since 3.7
     */
    protected static final Dimension UNSPECIFIED_SIZE = new Dimension();

    /**
     * Returns the <code>Command</code> to perform an Add with the specified
     * child and constraint. The constraint has been converted from a draw2d
     * constraint to an object suitable for the model by calling
     * {@link #translateToModelConstraint(Object)}.
     * 
     * @param request
     *            the ChangeBoundsRequest
     * @param child
     *            the EditPart of the child being added
     * @param constraint
     *            the model constraint, after being
     *            {@link #translateToModelConstraint(Object) translated}
     * @return the Command to add the child
     * 
     * @since 3.7
     */
    protected Command createAddCommand(ChangeBoundsRequest request,
            EditPart child, Object constraint) {
        return createAddCommand(child, constraint);
    }

    /**
     * Returns the <code>Command</code> to perform an Add with the specified
     * child and constraint. The constraint has been converted from a draw2d
     * constraint to an object suitable for the model by calling
     * {@link #translateToModelConstraint(Object)}.
     * 
     * @param child
     *            the EditPart of the child being added
     * @param constraint
     *            the model constraint, after being
     *            {@link #translateToModelConstraint(Object) translated}
     * @return the Command to add the child
     * @deprecated Use
     *             {@link #createAddCommand(ChangeBoundsRequest, EditPart, Object)}
     *             instead.
     * @nooverride Overwrite
     *             {@link #createAddCommand(ChangeBoundsRequest, EditPart, Object)}
     *             instead.
     * @noreference Use
     *              {@link #createAddCommand(ChangeBoundsRequest, EditPart, Object)}
     *              instead.
     */
    protected Command createAddCommand(EditPart child, Object constraint) {
        return null;
    }

    /**
     * The request is now made available when creating the change constraint
     * command. By default, this method invokes the old
     * {@link ConstrainedLayoutEditPolicy#createChangeConstraintCommand(EditPart, Object)
     * method}.
     * 
     * @param request
     *            the ChangeBoundsRequest
     * @param child
     *            the EditPart of the child being changed
     * @param constraint
     *            the new constraint, after being
     *            {@link #translateToModelConstraint(Object) translated}
     * @return A Command to change the constraints of the given child as
     *         specified in the given request
     * @see ConstrainedLayoutEditPolicy#createChangeConstraintCommand(EditPart,
     *      Object)
     * @since 3.0
     */
    protected Command createChangeConstraintCommand(
            ChangeBoundsRequest request, EditPart child, Object constraint) {
        return createChangeConstraintCommand(child, constraint);
    }

    /**
     * Returns the <code>Command</code> to change the specified child's
     * constraint. The constraint has been converted from a draw2d constraint to
     * an object suitable for the model. Clients should overwrite
     * {@link #createChangeConstraintCommand(ChangeBoundsRequest, EditPart, Object)}
     * instead.
     * 
     * @param child
     *            the EditPart of the child being changed
     * @param constraint
     *            the new constraint, after being
     *            {@link #translateToModelConstraint(Object) translated}
     * @return Command
     * @see #createChangeConstraintCommand(ChangeBoundsRequest, EditPart,
     *      Object)
     * @deprecated Use
     *             {@link #createChangeConstraintCommand(ChangeBoundsRequest, EditPart, Object)}
     *             instead.
     * @nooverride Overwrite
     *             {@link #createChangeConstraintCommand(ChangeBoundsRequest, EditPart, Object)}
     *             instead.
     * @noreference Use
     *              {@link #createChangeConstraintCommand(ChangeBoundsRequest, EditPart, Object)}
     *              instead.
     */
    protected Command createChangeConstraintCommand(EditPart child,
            Object constraint) {
        return null;
    }

    /**
     * A {@link ResizableEditPolicy} is used by default for children. Subclasses
     * may override this method to supply a different EditPolicy.
     * 
     * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#createChildEditPolicy(EditPart)
     */
    @Override
    protected EditPolicy createChildEditPolicy(EditPart child) {
        return new ResizableEditPolicy();
    }

    /**
     * Overrides <code>getAddCommand()</code> to generate the proper constraint
     * for each child being added. Once the constraint is calculated,
     * {@link #createAddCommand(EditPart,Object)} is called. Subclasses must
     * implement this method.
     * 
     * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#getAddCommand(Request)
     */
    @Override
    protected Command getAddCommand(Request generic) {
        ChangeBoundsRequest request = (ChangeBoundsRequest) generic;
        List editParts = request.getEditParts();
        CompoundCommand command = new CompoundCommand();
        command.setDebugLabel("Add in ConstrainedLayoutEditPolicy");//$NON-NLS-1$
        GraphicalEditPart child;

        for (int i = 0; i < editParts.size(); i++) {
            child = (GraphicalEditPart) editParts.get(i);
            command.add(createAddCommand(
                    request,
                    child,
                    translateToModelConstraint(getConstraintFor(request, child))));
        }
        return command.unwrap();
    }

    /**
     * Returns the command to align a group of children. By default, this is
     * treated the same as a resize, and
     * {@link #getResizeChildrenCommand(ChangeBoundsRequest)} is returned.
     * 
     * @param request
     *            the AligmentRequest
     * @return the command to perform alignment
     */
    protected Command getAlignChildrenCommand(AlignmentRequest request) {
        return getResizeChildrenCommand(request);
    }

    /**
     * Factors out RESIZE and ALIGN requests, otherwise calls <code>super</code>
     * .
     * 
     * @see org.eclipse.gef.EditPolicy#getCommand(Request)
     */
    @Override
    public Command getCommand(Request request) {
        if (REQ_RESIZE_CHILDREN.equals(request.getType()))
            return getResizeChildrenCommand((ChangeBoundsRequest) request);
        if (REQ_ALIGN_CHILDREN.equals(request.getType()))
            return getAlignChildrenCommand((AlignmentRequest) request);

        return super.getCommand(request);
    }

    /**
     * Generates a draw2d constraint object for the given
     * <code>ChangeBoundsRequest</code> and child EditPart by delegating to
     * {@link #getConstraintFor(Request, GraphicalEditPart, Rectangle)}.
     * 
     * The rectangle being passed over to
     * {@link #getConstraintFor(Request, GraphicalEditPart, Rectangle)} is
     * calculated based on the child figure's current bounds and the
     * ChangeBoundsRequest's move and resize deltas. It is made layout-relative
     * by using {@link #translateFromAbsoluteToLayoutRelative(Translatable)}
     * before calling
     * {@link #getConstraintFor(Request, GraphicalEditPart, Rectangle)}.
     * 
     * @param request
     *            the ChangeBoundsRequest
     * @param child
     *            the child EditPart for which the constraint should be
     *            generated
     * @return the draw2d constraint
     */
    protected Object getConstraintFor(ChangeBoundsRequest request,
            GraphicalEditPart child) {
        Rectangle locationAndSize = new PrecisionRectangle(child.getFigure()
                .getBounds());
        child.getFigure().translateToAbsolute(locationAndSize);
        locationAndSize = request.getTransformedRectangle(locationAndSize);
        translateFromAbsoluteToLayoutRelative(locationAndSize);
        return getConstraintFor(request, child, locationAndSize);
    }

    /**
     * Responsible of generating a draw2d constraint for the given Rectangle,
     * which represents the already transformed (layout-relative) position and
     * size of the given Request.
     * 
     * By default, this method delegates to {@link #getConstraintFor(Point)} or
     * {@link #getConstraintFor(Rectangle)}, dependent on whether the size of
     * the rectangle is an {@link #UNSPECIFIED_SIZE} or not.
     * 
     * Subclasses may overwrite this method in case they need the request or the
     * edit part (which will of course not be set during creation) to calculate
     * a layout constraint for the request.
     * 
     * @param rectangle
     *            the Rectangle relative to the {@link #getLayoutOrigin() layout
     *            origin}
     * @return the constraint
     * @since 3.7
     */
    protected Object getConstraintFor(Request request, GraphicalEditPart child,
            Rectangle rectangle) {
        if (UNSPECIFIED_SIZE.equals(rectangle.getSize())) {
            return getConstraintFor(rectangle.getLocation());
        }
        return getConstraintFor(rectangle);
    }

    /**
     * Generates a draw2d constraint given a <code>Point</code>. This method is
     * called during creation, when only a mouse location is available, as well
     * as during move, in case no resizing is involved.
     * 
     * @param point
     *            the Point relative to the {@link #getLayoutOrigin() layout
     *            origin}
     * @return the constraint
     */
    protected abstract Object getConstraintFor(Point point);

    /**
     * Generates a draw2d constraint given a <code>Rectangle</code>. This method
     * is called during most operations.
     * 
     * @param rect
     *            the Rectangle relative to the {@link #getLayoutOrigin() layout
     *            origin}
     * @return the constraint
     */
    protected abstract Object getConstraintFor(Rectangle rect);

    /**
     * Generates a draw2d constraint for the given <code>CreateRequest</code> by
     * delegating to
     * {@link #getConstraintFor(Request, GraphicalEditPart, Rectangle)}.
     * 
     * If the CreateRequest has a size, is used during size-on-drop creation, a
     * Rectangle of the request's location and size is passed with the
     * delegation. Otherwise, a rectangle with the request's location and an
     * empty size (0,0) is passed over.
     * <P>
     * The CreateRequest's location is relative to the Viewer. The location is
     * made layout-relative by using
     * {@link #translateFromAbsoluteToLayoutRelative(Translatable)} before
     * calling {@link #getConstraintFor(Request, GraphicalEditPart, Rectangle)}.
     * 
     * @param request
     *            the CreateRequest
     * @return a draw2d constraint
     */
    protected Object getConstraintFor(CreateRequest request) {
        Rectangle locationAndSize = null;
        if (request.getSize() == null || request.getSize().isEmpty()) {
            locationAndSize = new PrecisionRectangle(request.getLocation(),
                    UNSPECIFIED_SIZE);
        } else {
            locationAndSize = new PrecisionRectangle(request.getLocation(),
                    request.getSize());
        }
        translateFromAbsoluteToLayoutRelative(locationAndSize);
        return getConstraintFor(request, null, locationAndSize);
    }

    /**
     * Returns the correct rectangle bounds for the new clone's location.
     * 
     * @param part
     *            the graphical edit part representing the object to be cloned.
     * @param request
     *            the ChangeBoundsRequest that knows where to place the new
     *            object.
     * @return the bounds that will be used for the new object.
     * @deprecated Use
     *             {@link #getConstraintFor(ChangeBoundsRequest, GraphicalEditPart)}
     *             instead.
     * @nooverride This method is not intended to be re-implemented or extended
     *             by clients.
     * @noreference This method is not intended to be referenced by clients.
     */
    protected Object getConstraintForClone(GraphicalEditPart part,
            ChangeBoundsRequest request) {
        // anyssen: The code executed herein was functionally the same
        // as that in getConstraintFor(ChangeBoundsRequest, GraphicalEditPart),
        // despite it was erroneously missing a call to
        // getLayoutContainer().translateFromParent(), which is needed
        // to translate the part's figure's bounds into a coordinate
        // local to the client area of the layout container.
        return getConstraintFor(request, part);
    }

    /**
     * Converts a constraint from the format used by LayoutManagers, to the form
     * stored in the model.
     * 
     * @param figureConstraint
     *            the draw2d constraint
     * @return the model constraint
     */
    protected Object translateToModelConstraint(Object figureConstraint) {
        return figureConstraint;
    }

    /**
     * Returns the <code>Command</code> to resize a group of children.
     * 
     * @param request
     *            the ChangeBoundsRequest
     * @return the Command
     */
    protected Command getResizeChildrenCommand(ChangeBoundsRequest request) {
        return getChangeConstraintCommand(request);
    }

    /**
     * Returns the <code>Command</code> for changing bounds for a group of
     * children.
     * 
     * @param request
     *            the ChangeBoundsRequest
     * @return the Command
     * 
     * @since 3.7
     */
    protected Command getChangeConstraintCommand(ChangeBoundsRequest request) {
        CompoundCommand resize = new CompoundCommand();
        Command c;
        GraphicalEditPart child;
        List children = request.getEditParts();

        for (int i = 0; i < children.size(); i++) {
            child = (GraphicalEditPart) children.get(i);
            c = createChangeConstraintCommand(
                    request,
                    child,
                    translateToModelConstraint(getConstraintFor(request, child)));
            resize.add(c);
        }
        return resize.unwrap();
    }

    /**
     * Returns the <code>Command</code> to move a group of children. By default,
     * move is treated the same as a resize.
     * 
     * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#getMoveChildrenCommand(Request)
     */
    @Override
    protected Command getMoveChildrenCommand(Request request) {
        // By default, move and resize are treated the same for constrained
        // layouts.
        return getResizeChildrenCommand((ChangeBoundsRequest) request);
    }
}
