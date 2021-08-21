/*******************************************************************************
 * Copyright (c) 2003, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Translatable;

/**
 * A helper used temporarily by Tools for snapping certain mouse interactions.
 * SnapToHelpers should not be reused by tools or by the editparts which provide
 * them to the tools. For example, for a move operation, the life-cycle of a
 * SnapToHelper begins when a drag is initiated, and ends when the drag is over.
 * If another drag is initiated right after the first one is completed, new
 * SnapToHelpers are employed. This means that helpers can assume that
 * everything else is static, and there is no need to track changes outside of
 * the helper.
 * 
 * @since 3.0
 * @author Randy Hudson
 * @author Pratik Shah
 */
public abstract class SnapToHelper implements PositionConstants {

    /**
     * Translates from a given figure to absolute coordinates.
     * 
     * @param figure
     *            the reference figure
     * @param t
     *            the object to translate
     */
    protected void makeAbsolute(IFigure figure, Translatable t) {
        figure.translateToAbsolute(t);
    }

    /**
     * Translates from absolute to coordinates relative to the given figure.
     * 
     * @param figure
     *            the reference figure
     * @param t
     *            the object to translate
     */
    protected void makeRelative(IFigure figure, Translatable t) {
        figure.translateToRelative(t);
    }

    /**
     * Applies a snapping correction to the given result. Snapping can occur in
     * the four primary directions: NORTH, SOUTH, EAST, WEST, as defined on
     * {@link PositionConstants}. By default a Point is treated as an empty
     * Rectangle. Only NORTH and WEST should be used in general. But SOUTH and
     * EAST may also be used. Similarly, VERTICAL and HORIZONTAL may be used to
     * allow a point to snap to the "center" or "middle" as defined by the
     * concrete subclass.
     * <P>
     * The returned value should be a subset of the given snapDirections based
     * on what correction was applied to the result. e.g., if the <b>x</b> value
     * was adjusted, the returned value should not contain WEST, EAST, or
     * HORIZONTAL.
     * <P>
     * All coordinate information received and returned by this method should be
     * in absolute coordinates.
     * 
     * @param request
     *            a request or <code>null</code>
     * @param snapDirections
     *            the directions in which snapping should occur.
     * @param where
     *            the rectangle used to determine snapping
     * @param result
     *            the result
     * @return the remaining snap locations
     */
    public int snapPoint(Request request, int snapDirections,
            PrecisionPoint where, PrecisionPoint result) {
        PrecisionRectangle rect = new PrecisionRectangle();
        PrecisionRectangle resultRect = new PrecisionRectangle();
        rect.setPreciseX(where.preciseX());
        rect.setPreciseY(where.preciseY());
        snapDirections = snapRectangle(request, snapDirections, rect,
                resultRect);
        result.setPreciseX(result.preciseX() + resultRect.preciseX());
        result.setPreciseY(result.preciseY() + resultRect.preciseY());
        return snapDirections;
    }

    /**
     * A convenience method for snapping a Point based on an array of
     * rectangles. By default, this method will construct an empty rectangle at
     * the same locations as the provided point, and call
     * {@link #snapRectangle(Request, int, PrecisionRectangle[], PrecisionRectangle)}
     * . The intended usage of this method is when dragging one or more parts in
     * a diagram.
     * <P>
     * The returned value should be a subset of the given snapDirections based
     * on what correction was applied to the result. e.g., if the <b>x</b> value
     * was adjusted, the returned value should not contain WEST, EAST, or
     * HORIZONTAL.
     * <P>
     * All coordinate information received and returned by this method should be
     * in absolute coordinates.
     * 
     * @param request
     *            the request or <code>null</code>
     * @param snapLocations
     *            the types of snapping to perform
     * @param rects
     *            an array of one or more rectangles used to perform the
     *            snapping
     * @param result
     *            the correction will be applied to this point
     * @return the remaining snap locations
     */
    public int snapPoint(Request request, int snapLocations,
            PrecisionRectangle rects[], PrecisionPoint result) {
        PrecisionRectangle resultRect = new PrecisionRectangle();
        snapLocations = snapRectangle(request, snapLocations, rects, resultRect);
        result.setPreciseX(result.preciseX() + resultRect.preciseX());
        result.setPreciseY(result.preciseY() + resultRect.preciseY());
        return snapLocations;
    }

    /**
     * A convenience method for snapping a Rectangle based on one or more
     * rectangles. This method will call
     * {@link #snapRectangle(Request, int, PrecisionRectangle, PrecisionRectangle)}
     * for each rectangle in the array or until no more snap locations remain.
     * <P>
     * All coordinate information received and returned by this method should be
     * in absolute coordinates.
     * 
     * @param request
     *            the request or <code>null</code>
     * @param baseRects
     *            the prioritized rectangles to snap to
     * @param result
     *            the output
     * @param snapOrientation
     *            the input snap locations
     * @return the remaining snap locations
     */
    public int snapRectangle(Request request, int snapOrientation,
            PrecisionRectangle baseRects[], PrecisionRectangle result) {

        for (int i = 0; i < baseRects.length && snapOrientation != 0; i++)
            snapOrientation = snapRectangle(request, snapOrientation,
                    baseRects[i], result);

        return snapOrientation;
    }

    /**
     * Applies a snap correction to a Rectangle based on a given Rectangle. The
     * provided baseRect will be used as a reference for snapping. The types of
     * snapping to be performed are indicated by the snapOrientation parameter.
     * The correction is applied to the result field.
     * <P>
     * The baseRect is not modified. The correction is applied to the result.
     * The request's {@link Request#getExtendedData() extended data} may contain
     * additional information about the snapping which was performed.
     * <P>
     * All coordinate information received and returned by this method should be
     * in absolute coordinates.
     * 
     * @since 3.0
     * @param request
     *            the request or <code>null</code>
     * @param baseRect
     *            the input rectangle
     * @param result
     *            the correction is applied to this rectangle
     * @param snapOrientation
     *            the input snap locations
     * @return the remaining snap locations
     */
    public abstract int snapRectangle(Request request, int snapOrientation,
            PrecisionRectangle baseRect, PrecisionRectangle result);

}
