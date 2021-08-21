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

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.OrderedLayout;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.requests.ChangeBoundsRequest;

/**
 * A LayoutEditPolicy for use with <code>LayoutManagers</code> that take no
 * constraints. Such layout managers typically position children in <x,y>
 * coordinates based on their order in
 * {@link org.eclipse.draw2d.IFigure#getChildren() getChildren()}. Therefore,
 * this EditPolicy must perform the inverse mapping. Given a mouse location from
 * the User, the policy must determine the index at which the child[ren] should
 * be added/created.
 * 
 * @author hudsonr
 * @since 2.0
 */
@SuppressWarnings("rawtypes")
public abstract class OrderedLayoutEditPolicy extends LayoutEditPolicy {

    /**
     * Returns the <code>Command</code> to add the specified child after a
     * reference <code>EditPart</code>. If the reference is <code>null</code>,
     * the child should be added as the first child.
     * 
     * @param child
     *            the child being added
     * @param after
     *            <code>null</code> or a reference EditPart
     * @return a Command to add the child
     */
    protected abstract Command createAddCommand(EditPart child, EditPart after);

    /**
     * Since Ordered layouts generally don't use constraints, a
     * {@link NonResizableEditPolicy} is used by default for children.
     * Subclasses may override this method to supply a different EditPolicy.
     * 
     * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#createChildEditPolicy(EditPart)
     */
    @Override
    protected EditPolicy createChildEditPolicy(EditPart child) {
        return new NonResizableEditPolicy();
    }

    /**
     * Returns the <code>Command</code> to move the specified child before the
     * given reference <code>EditPart</code>. If the reference is
     * <code>null</code>, the child should be moved in front of all children.
     * <P>
     * A move is a change in the order of the children, which indirectly causes
     * a change in location on the screen.
     * 
     * @param child
     *            the child being moved
     * @param after
     *            <code>null</code> or the EditPart that should be after (or to
     *            the right of) the child being moved
     * @return a Command to move the child
     */
    protected abstract Command createMoveChildCommand(EditPart child,
            EditPart after);

    /**
     * This method is overridden from the superclass to calculate the
     * <i>index</i> at which the children should be added. The index is
     * determined by finding a reference EditPart, and adding the new child[ren]
     * <em>after</em> that reference part. <code>null</code> is used to indicate
     * that the child[ren] should be added at the beginning.
     * <P>
     * Subclasses must override {@link #createAddCommand(EditPart, EditPart)},
     * and should not override this method.
     * 
     * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#getAddCommand(Request)
     */
    @Override
    protected Command getAddCommand(Request req) {
        ChangeBoundsRequest request = (ChangeBoundsRequest) req;
        List editParts = request.getEditParts();
        CompoundCommand command = new CompoundCommand();
        for (int i = 0; i < editParts.size(); i++) {
            EditPart child = (EditPart) editParts.get(i);
            command.add(createAddCommand(child, getInsertionReference(request)));
        }
        return command.unwrap();
    }

    /**
     * Calculates a <i>reference</i> <code>EditPart</code> using the specified
     * <code>Request</code>. The EditPart returned is used to mark the index
     * coming <em>after</em> that EditPart. <code>null</code> is used to
     * indicate the index that comes after <em>no</em> EditPart, that is, it
     * indicates the very last index.
     * 
     * @param request
     *            the Request
     * @return <code>null</code> or a reference EditPart
     */
    protected abstract EditPart getInsertionReference(Request request);

    /**
     * A move is interpreted here as a change in order of the children. This
     * method obtains the proper index, and then calls
     * {@link #createMoveChildCommand(EditPart, EditPart)}, which subclasses
     * must implement. Subclasses should not override this method.
     * 
     * @see LayoutEditPolicy#getMoveChildrenCommand(Request)
     */
    @Override
    protected Command getMoveChildrenCommand(Request request) {
        CompoundCommand command = new CompoundCommand();
        List editParts = ((ChangeBoundsRequest) request).getEditParts();

        EditPart insertionReference = getInsertionReference(request);
        for (int i = 0; i < editParts.size(); i++) {
            EditPart child = (EditPart) editParts.get(i);
            command.add(createMoveChildCommand(child, insertionReference));
        }
        return command.unwrap();
    }

    /**
     * Returns whether the layout container's layout manager has a horizontal
     * orientation or not.
     * 
     * @return <code>true</code> if the layout container's layout manager has a
     *         horizontal orientation, <code>false</code> otherwise
     * @since 3.7
     */
    protected boolean isLayoutHorizontal() {
        IFigure figure = getLayoutContainer();
        return ((OrderedLayout) figure.getLayoutManager()).isHorizontal();
    }

}
