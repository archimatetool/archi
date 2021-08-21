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

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.TreeEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.DropRequest;

/**
 * An EditPolicy for handling ADDS, MOVES, and CREATES on a {@link TreeEditPart}
 * .
 * <P>
 * This EditPolicy is responsible for displaying the insertion feedback in the
 * Tree during the appropriate interactions.
 * <P>
 * This EditPolicy factors the {@link #getCommand(Request)} into three different
 * abstract methods which subclasses must implement.
 * 
 * @since 2.0
 */
public abstract class TreeContainerEditPolicy extends AbstractEditPolicy {

    /**
     * Returns a Command for adding the children to the container.
     * 
     * @param request
     *            the Request to add.
     * @return Command <code>null</code> or a Command to perform the add
     */
    protected abstract Command getAddCommand(ChangeBoundsRequest request);

    /**
     * Returns a Command for creating the object inside the container.
     * 
     * @param request
     *            the CreateRequest
     * @return Command <code>null</code> or a Command to perform the create
     */
    protected abstract Command getCreateCommand(CreateRequest request);

    /**
     * Returns a Command for moving the children within the container.
     * 
     * @param request
     *            the Request to move
     * @return Command <code>null</code> or a Command to perform the move
     */
    protected abstract Command getMoveChildrenCommand(
            ChangeBoundsRequest request);

    private void eraseDropFeedback(Request req) {
        getTree().setInsertMark(null, true);
    }

    /**
     * @see org.eclipse.gef.EditPolicy#eraseTargetFeedback(Request)
     */
    @Override
    public void eraseTargetFeedback(Request req) {
        if (req.getType().equals(REQ_MOVE) || req.getType().equals(REQ_ADD)
                || req.getType().equals(REQ_CREATE))
            eraseDropFeedback(req);
    }

    /**
     * Calculates the index of the TreeItem at given point.
     * 
     * @param pt
     *            the Point in the Viewer
     * @return the index of the TreeItem
     */
    protected final int findIndexOfTreeItemAt(
            org.eclipse.draw2d.geometry.Point pt) {
        int index = -1;
        TreeItem item = findTreeItemAt(pt);
        if (item != null) {
            index = getHost().getChildren().indexOf(item.getData());
            if (index >= 0 && !isInUpperHalf(item.getBounds(), pt))
                index++;
        }
        return index;
    }

    /**
     * Calculates the <code>TreeItem</code> at a specified
     * {@link org.eclipse.draw2d.geometry.Point}.
     * 
     * @param pt
     *            the draw2d Point
     * @return <code>null</code> or the TreeItem
     */
    protected final TreeItem findTreeItemAt(org.eclipse.draw2d.geometry.Point pt) {
        return getTree().getItem(new Point(pt.x, pt.y));
    }

    /**
     * @see org.eclipse.gef.EditPolicy#getCommand(Request)
     */
    @Override
    public Command getCommand(Request req) {
        if (req.getType().equals(REQ_MOVE_CHILDREN))
            return getMoveChildrenCommand((ChangeBoundsRequest) req);
        if (req.getType().equals(REQ_ADD))
            return getAddCommand((ChangeBoundsRequest) req);
        if (req.getType().equals(REQ_CREATE))
            return getCreateCommand((CreateRequest) req);

        return null;
    }

    /**
     * Returns the host EditPart when appropriate. Targeting is done by checking
     * if the mouse is clearly over the host's TreeItem.
     * 
     * @see org.eclipse.gef.EditPolicy#getTargetEditPart(Request)
     */
    @Override
    public EditPart getTargetEditPart(Request req) {
        if (req.getType().equals(REQ_ADD) || req.getType().equals(REQ_MOVE)
                || req.getType().equals(REQ_CREATE)) {
            DropRequest drop = (DropRequest) req;
            Point where = new Point(drop.getLocation().x, drop.getLocation().y);
            Widget widget = ((TreeEditPart) getHost()).getWidget();
            if (widget instanceof Tree)
                return getHost();
            TreeItem treeitem = (TreeItem) widget;
            Rectangle bounds = treeitem.getBounds();
            int fudge = bounds.height / 5;
            Rectangle inner = new Rectangle(bounds.x, bounds.y + fudge,
                    bounds.width, bounds.height
                            - (treeitem.getExpanded() ? 0 : fudge * 2));
            // Point is either outside the Treeitem, or inside the inner Rect.
            if (!bounds.contains(where) || inner.contains(where))
                return getHost();
        }
        return null;
    }

    private Tree getTree() {
        Widget widget = ((TreeEditPart) getHost()).getWidget();
        if (widget instanceof Tree)
            return (Tree) widget;
        else
            return ((TreeItem) widget).getParent();
    }

    private void insertMarkAfterLastChild(TreeItem[] children) {
        if (children != null && children.length > 0) {
            TreeItem item = children[children.length - 1];
            getTree().setInsertMark(item, false);
        }
    }

    private boolean isInUpperHalf(Rectangle rect,
            org.eclipse.draw2d.geometry.Point pt) {
        Rectangle tempRect = new Rectangle(rect.x, rect.y, rect.width,
                rect.height / 2);
        return tempRect.contains(new Point(pt.x, pt.y));
    }

    private void showDropFeedback(DropRequest request) {
        Widget hostWidget = ((TreeEditPart) getHost()).getWidget();
        Tree tree = getTree();

        org.eclipse.draw2d.geometry.Point pt = request.getLocation();
        TreeItem item = findTreeItemAt(pt);
        if (item == null) {
            if (hostWidget == tree) {
                insertMarkAfterLastChild(tree.getItems());
            }
        } else if (item == hostWidget) {
            tree.setInsertMark(null, true);
        } else {
            boolean before = isInUpperHalf(item.getBounds(), pt);
            tree.setInsertMark(item, before);
        }
    }

    /**
     * @see org.eclipse.gef.EditPolicy#showTargetFeedback(Request)
     */
    @Override
    public void showTargetFeedback(Request req) {
        if (req.getType().equals(REQ_MOVE) || req.getType().equals(REQ_ADD)
                || req.getType().equals(REQ_CREATE))
            showDropFeedback((DropRequest) req);
    }

}
