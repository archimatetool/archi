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

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

/**
 * An EditPolicy for use with <code>Figures</code> in {@link XYLayout}. The
 * constraint for XYLayout is a {@link org.eclipse.draw2d.geometry.Rectangle}.
 * 
 * Created on :Nov 12, 2002
 * 
 * @author hudsonr
 * @author msorens
 * @author anyssen
 * 
 * @since 2.0
 */
public abstract class XYLayoutEditPolicy extends ConstrainedLayoutEditPolicy {

    private static final Dimension PREFERRED_SIZE = new Dimension(-1, -1);

    private XYLayout xyLayout;

    /**
     * Overridden to preserve existing width and height (as well as preferred
     * sizes) during MOVE requests.
     * 
     * @see org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#getConstraintFor(org.eclipse.gef.Request,
     *      org.eclipse.gef.GraphicalEditPart,
     *      org.eclipse.draw2d.geometry.Rectangle)
     */
    @Override
    protected Object getConstraintFor(Request request, GraphicalEditPart child,
            Rectangle rect) {
        if (request instanceof ChangeBoundsRequest) {
            if (((ChangeBoundsRequest) request).getSizeDelta().width == 0
                    && ((ChangeBoundsRequest) request).getSizeDelta().height == 0) {
                if (getCurrentConstraintFor(child) != null) {
                    // Bug 86473 allows for unintended use of this method
                    rect.setSize(getCurrentConstraintFor(child).getSize());
                }
            } else {
                // for backwards compatibility, ensure minimum size is respected
                rect.setSize(Dimension.max(getMinimumSizeFor(child),
                        rect.getSize()));
            }
        }
        return super.getConstraintFor(request, child, rect);
    }

    /**
     * Returns a Rectangle at the given Point with width and height of -1.
     * <code>XYLayout</code> uses width or height equal to '-1' to mean use the
     * figure's preferred size.
     * 
     * @param p
     *            the input Point
     * @return a Rectangle
     */
    @Override
    public Object getConstraintFor(Point p) {
        return new Rectangle(p, PREFERRED_SIZE);
    }

    /**
     * Returns a new Rectangle equivalent to the passed Rectangle.
     * 
     * @param r
     *            the input Rectangle
     * @return a copy of the input Rectangle
     */
    @Override
    public Object getConstraintFor(Rectangle r) {
        return new Rectangle(r);
    }

    /**
     * Retrieves the child's current constraint from the
     * <code>LayoutManager</code>.
     * 
     * @param child
     *            the child
     * @return the current constraint
     */
    protected Rectangle getCurrentConstraintFor(GraphicalEditPart child) {
        IFigure fig = child.getFigure();
        return (Rectangle) fig.getParent().getLayoutManager()
                .getConstraint(fig);
    }

    /**
     * Returns {@link XYLayout#getOrigin(IFigure)}.
     * 
     * @see ConstrainedLayoutEditPolicy#getLayoutOrigin()
     */
    @Override
    protected Point getLayoutOrigin() {
        return getXYLayout().getOrigin(getLayoutContainer());
    }

    /**
     * @return the XYLayout layout manager set on the
     *         {@link LayoutEditPolicy#getLayoutContainer() container}
     */
    protected XYLayout getXYLayout() {
        if (xyLayout == null) {
            IFigure container = getLayoutContainer();
            xyLayout = (XYLayout) container.getLayoutManager();
        }
        return xyLayout;
    }

    /**
     * @param xyLayout
     *            The xyLayout to set.
     */
    public void setXyLayout(XYLayout xyLayout) {
        this.xyLayout = xyLayout;
    }

    /**
     * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#showSizeOnDropFeedback(org.eclipse.gef.requests.CreateRequest)
     */
    @Override
    protected void showSizeOnDropFeedback(CreateRequest request) {
        Point p = new Point(request.getLocation().getCopy());
        Dimension size = request.getSize().getCopy();
        Rectangle feedbackBounds = new Rectangle(p, size);
        IFigure feedback = getSizeOnDropFeedback(request);
        feedback.translateToRelative(feedbackBounds);
        feedback.setBounds(feedbackBounds
                .expand(getCreationFeedbackOffset(request)));
        feedback.validate();
    }

    /**
     * Determines the <em>minimum</em> size that the specified child can be
     * resized to. Called from
     * {@link #getConstraintFor(ChangeBoundsRequest, GraphicalEditPart)}. By
     * default, a small <code>Dimension</code> is returned.
     * 
     * @param child
     *            the child
     * @return the minimum size
     * 
     * @deprecated Clients should no longer extend this method. Instead, the
     *             resize tracker, constructed by the 'satellite' primary drag
     *             edit policy should be parameterized with max and min size
     *             constraints.
     */
    protected Dimension getMinimumSizeFor(GraphicalEditPart child) {
        return new Dimension(8, 8);
    }

}
