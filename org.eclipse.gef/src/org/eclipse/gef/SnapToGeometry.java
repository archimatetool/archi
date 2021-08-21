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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.handles.HandleBounds;
import org.eclipse.gef.requests.GroupRequest;

/**
 * A temporary helper used to perform snapping to existing elements. This helper
 * can be used in conjunction with the
 * {@link org.eclipse.gef.tools.DragEditPartsTracker DragEditPartsTracker} when
 * dragging editparts within a graphical viewer. Snapping is based on the
 * existing children of a <I>container</I>. When snapping a rectangle, the edges
 * of the rectangle will snap to edges of other rectangles generated from the
 * children of the given container. Similarly, the centers and middles of
 * rectangles will snap to each other.
 * <P>
 * If the snap request is being made during a Move, Reparent or Resize, then the
 * figures of the participants of that request will not be used for snapping. If
 * the request is a Clone, then the figures for the parts being cloned will be
 * used as possible snap locations.
 * <P>
 * This helper does not keep up with changes made to the container editpart.
 * Clients should instantiate a new helper each time one is requested and not
 * hold on to instances of the helper.
 * 
 * @since 3.0
 * @author Randy Hudson
 * @author Pratik Shah
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class SnapToGeometry extends SnapToHelper {

    /**
     * A property indicating whether this helper should be used. The value
     * should be an instance of Boolean. Currently, this class does not check to
     * see if the viewer property is set to <code>true</code>.
     * 
     * @see EditPartViewer#setProperty(String, Object)
     */
    public static final String PROPERTY_SNAP_ENABLED = "SnapToGeometry.isEnabled"; //$NON-NLS-1$

    /**
     * The key used to identify the North anchor point in the extended data of a
     * request. The north anchor may be set to an {@link Integer} value
     * indicating where the snapping is occurring. This is used for feedback
     * purposes.
     */
    public static final String KEY_NORTH_ANCHOR = "SnapToGeometry.NorthAnchor"; //$NON-NLS-1$

    /**
     * The key used to identify the South anchor point in the extended data of a
     * request. The south anchor may be set to an {@link Integer} value
     * indicating where the snapping is occurring. This is used for feedback
     * purposes.
     */
    public static final String KEY_SOUTH_ANCHOR = "SnapToGeometry.SouthAnchor"; //$NON-NLS-1$

    /**
     * The key used to identify the West anchor point in the extended data of a
     * request. The west anchor may be set to an {@link Integer} value
     * indicating where the snapping is occurring. This is used for feedback
     * purposes.
     */
    public static final String KEY_WEST_ANCHOR = "SnapToGeometry.WestAnchor"; //$NON-NLS-1$

    /**
     * The key used to identify the East anchor point in the extended data of a
     * request. The east anchor may be set to an {@link Integer} value
     * indicating where the snapping is occurring. This is used for feedback
     * purposes.
     */
    public static final String KEY_EAST_ANCHOR = "SnapToGeometry.EastAnchor"; //$NON-NLS-1$

    /**
     * A vertical or horizontal snapping point. since 3.0
     */
    protected static class Entry {
        final int type;
        final int location;

        /**
         * Constructs a new entry of the given type and location.
         * 
         * @param type
         *            an integer between -1 and 1 inclusively
         * @param location
         *            the location
         */
        protected Entry(int type, int location) {
            if (type < -1 || type > 1)
                throw new IllegalArgumentException("Unrecognized snap type"); //$NON-NLS-1$
            this.type = type;
            this.location = location;
        }

        /**
         * Returns the location of the snap entry.
         * 
         * @return the location
         * @since 3.2
         */
        public int getLocation() {
            return location;
        }

        /**
         * Returns the snap type. The following values may be returned.
         * <UL>
         * <LI>-1 indicates left/top
         * <LI>0 indicates middle/center
         * <LI>1 indicates right/bottom
         * </UL>
         * 
         * @return the snap type
         * @since 3.2
         */
        public int getType() {
            return type;
        }
    }

    /**
     * The sensitivity of the snapping. Corrections greater than this value will
     * not occur.
     */
    protected static final double THRESHOLD = 5.0001;

    private double threshold = THRESHOLD;

    boolean cachedCloneBool;

    /**
     * The horizontal rows being snapped to.
     */
    protected Entry rows[];

    /**
     * The vertical columnd being snapped to.
     */
    protected Entry cols[];

    /**
     * The container editpart providing the coordinates and the children to
     * which snapping occurs.
     */
    protected GraphicalEditPart container;

    /**
     * Constructs a helper that will use the given part as its basis for
     * snapping. The part's contents pane will provide the coordinate system and
     * its children determine the existing elements.
     * 
     * @since 3.0
     * @param container
     *            the container editpart
     */
    public SnapToGeometry(GraphicalEditPart container) {
        this.container = container;
    }

    /**
     * Get the sensitivity of the snapping. Corrections greater than this value
     * will not occur.
     * 
     * @return the snapping threshold
     * @since 3.4
     */
    protected double getThreshold() {
        return this.threshold;
    }

    /**
     * Set the sensitivity of the snapping.
     * 
     * @see #getThreshold()
     * @param newThreshold
     *            the new snapping threshold
     * @since 3.4
     */
    protected void setThreshold(double newThreshold) {
        this.threshold = newThreshold;
    }

    /**
     * Generates a list of parts which should be snapped to. The list is the
     * original children, minus the given exclusions, minus and children whose
     * figures are not visible.
     * 
     * @since 3.0
     * @param exclusions
     *            the children to exclude
     * @return a list of parts which should be snapped to
     */
    protected List generateSnapPartsList(List exclusions) {
        // Don't snap to any figure that is being dragged
        List children = new ArrayList(container.getChildren());
        children.removeAll(exclusions);

        // Don't snap to hidden figures
        List hiddenChildren = new ArrayList();
        for (Iterator iter = children.iterator(); iter.hasNext();) {
            GraphicalEditPart child = (GraphicalEditPart) iter.next();
            if (!child.getFigure().isVisible())
                hiddenChildren.add(child);
        }
        children.removeAll(hiddenChildren);

        return children;
    }

    /**
     * Returns the correction value for the given entries and sides. During a
     * move, the left, right, or center is free to snap to a location.
     * 
     * @param entries
     *            the entries
     * @param extendedData
     *            the requests extended data
     * @param vert
     *            <code>true</code> if the correction is vertical
     * @param near
     *            the left/top side of the rectangle
     * @param far
     *            the right/bottom side of the rectangle
     * @return the correction amount or #getThreshold () if no correction was
     *         made
     */
    protected double getCorrectionFor(Entry entries[], Map extendedData,
            boolean vert, double near, double far) {
        far -= 1.0;
        double total = near + far;
        // If the width is even (i.e., odd right now because we have reduced one
        // pixel from
        // far) there is no middle pixel so favor the left-most/top-most pixel
        // (which is what
        // populateRowsAndCols() does by using int precision).
        if ((int) (near - far) % 2 != 0)
            total -= 1.0;
        double result = getCorrectionFor(entries, extendedData, vert,
                total / 2, 0);
        if (result == getThreshold())
            result = getCorrectionFor(entries, extendedData, vert, near, -1);
        if (result == getThreshold())
            result = getCorrectionFor(entries, extendedData, vert, far, 1);
        return result;
    }

    /**
     * Returns the correction value between +/- {@link #getThreshold()}, or the
     * #getThreshold () if no corrections were found.
     * 
     * @param entries
     *            the entries
     * @param extendedData
     *            the map for setting values
     * @param vert
     *            <code>true</code> if vertical
     * @param value
     *            the value being corrected
     * @param side
     *            which sides should be considered
     * @return the correction or #getThreshold () if no correction was made
     */
    @SuppressWarnings("deprecation")
    protected double getCorrectionFor(Entry entries[], Map extendedData,
            boolean vert, double value, int side) {
        double resultMag = getThreshold();
        double result = getThreshold();

        String property;
        if (side == -1)
            property = vert ? KEY_WEST_ANCHOR : KEY_NORTH_ANCHOR;
        else
            property = vert ? KEY_EAST_ANCHOR : KEY_SOUTH_ANCHOR;

        for (int i = 0; i < entries.length; i++) {
            Entry entry = entries[i];
            double magnitude;

            if (entry.type == -1 && side != 0) {
                magnitude = Math.abs(value - entry.location);
                if (magnitude < resultMag) {
                    resultMag = magnitude;
                    result = entry.location - value;
                    extendedData.put(property, new Integer(entry.location));
                }
            } else if (entry.type == 0 && side == 0) {
                magnitude = Math.abs(value - entry.location);
                if (magnitude < resultMag) {
                    resultMag = magnitude;
                    result = entry.location - value;
                    extendedData.put(property, new Integer(entry.location));
                }
            } else if (entry.type == 1 && side != 0) {
                magnitude = Math.abs(value - entry.location);
                if (magnitude < resultMag) {
                    resultMag = magnitude;
                    result = entry.location - value;
                    extendedData.put(property, new Integer(entry.location));
                }
            }
        }
        return result;
    }

    /**
     * Returns the rectangular contribution for the given editpart. This is the
     * rectangle with which snapping is performed.
     * 
     * @since 3.0
     * @param part
     *            the child
     * @return the rectangular guide for that part
     */
    protected Rectangle getFigureBounds(GraphicalEditPart part) {
        IFigure fig = part.getFigure();
        if (fig instanceof HandleBounds)
            return ((HandleBounds) fig).getHandleBounds();
        return fig.getBounds();
    }

    /**
     * Updates the cached row and column Entries using the provided parts.
     * 
     * @since 3.0
     * @param parts
     *            a List of EditParts
     */
    protected void populateRowsAndCols(List parts) {
        rows = new Entry[parts.size() * 3];
        cols = new Entry[parts.size() * 3];
        for (int i = 0; i < parts.size(); i++) {
            GraphicalEditPart child = (GraphicalEditPart) parts.get(i);
            Rectangle bounds = getFigureBounds(child);
            cols[i * 3] = new Entry(-1, bounds.x);
            rows[i * 3] = new Entry(-1, bounds.y);
            cols[i * 3 + 1] = new Entry(0, bounds.x + (bounds.width - 1) / 2);
            rows[i * 3 + 1] = new Entry(0, bounds.y + (bounds.height - 1) / 2);
            cols[i * 3 + 2] = new Entry(1, bounds.right() - 1);
            rows[i * 3 + 2] = new Entry(1, bounds.bottom() - 1);
        }
    }

    /**
     * @see SnapToHelper#snapRectangle(Request, int, PrecisionRectangle,
     *      PrecisionRectangle)
     */
    @Override
    public int snapRectangle(Request request, int snapOrientation,
            PrecisionRectangle baseRect, PrecisionRectangle result) {

        baseRect = baseRect.getPreciseCopy();
        makeRelative(container.getContentPane(), baseRect);
        PrecisionRectangle correction = new PrecisionRectangle();
        makeRelative(container.getContentPane(), correction);

        // Recalculate snapping locations if needed
        boolean isClone = request.getType().equals(RequestConstants.REQ_CLONE);
        if (rows == null || cols == null || isClone != cachedCloneBool) {
            cachedCloneBool = isClone;
            List exclusionSet = Collections.EMPTY_LIST;
            if (!isClone && request instanceof GroupRequest)
                exclusionSet = ((GroupRequest) request).getEditParts();
            populateRowsAndCols(generateSnapPartsList(exclusionSet));
        }

        if ((snapOrientation & HORIZONTAL) != 0) {
            double xcorrect = getThreshold();
            xcorrect = getCorrectionFor(cols, request.getExtendedData(), true,
                    baseRect.preciseX(), baseRect.preciseRight());
            if (xcorrect != getThreshold()) {
                snapOrientation &= ~HORIZONTAL;
                correction.setPreciseX(correction.preciseX() + xcorrect);
            }
        }

        if ((snapOrientation & VERTICAL) != 0) {
            double ycorrect = getThreshold();
            ycorrect = getCorrectionFor(rows, request.getExtendedData(), false,
                    baseRect.preciseY(), baseRect.preciseBottom());
            if (ycorrect != getThreshold()) {
                snapOrientation &= ~VERTICAL;
                correction.setPreciseY(correction.preciseY() + ycorrect);
            }
        }

        if ((snapOrientation & EAST) != 0) {
            double rightCorrection = getCorrectionFor(cols,
                    request.getExtendedData(), true,
                    baseRect.preciseRight() - 1, 1);
            if (rightCorrection != getThreshold()) {
                snapOrientation &= ~EAST;
                correction.setPreciseWidth(correction.preciseWidth()
                        + rightCorrection);
            }
        }

        if ((snapOrientation & WEST) != 0) {
            double leftCorrection = getCorrectionFor(cols,
                    request.getExtendedData(), true, baseRect.preciseX(), -1);
            if (leftCorrection != getThreshold()) {
                snapOrientation &= ~WEST;
                correction.setPreciseWidth(correction.preciseWidth()
                        - leftCorrection);
                correction.setPreciseX(correction.preciseX() + leftCorrection);
            }
        }

        if ((snapOrientation & SOUTH) != 0) {
            double bottom = getCorrectionFor(rows, request.getExtendedData(),
                    false, baseRect.preciseBottom() - 1, 1);
            if (bottom != getThreshold()) {
                snapOrientation &= ~SOUTH;
                correction
                        .setPreciseHeight(correction.preciseHeight() + bottom);
            }
        }

        if ((snapOrientation & NORTH) != 0) {
            double topCorrection = getCorrectionFor(rows,
                    request.getExtendedData(), false, baseRect.preciseY(), -1);
            if (topCorrection != getThreshold()) {
                snapOrientation &= ~NORTH;
                correction.setPreciseHeight(correction.preciseHeight()
                        - topCorrection);
                correction.setPreciseY(correction.preciseY() + topCorrection);
            }
        }

        makeAbsolute(container.getContentPane(), correction);
        result.setPreciseX(result.preciseX() + correction.preciseX());
        result.setPreciseY(result.preciseY() + correction.preciseY());
        result.setPreciseWidth(result.preciseWidth()
                + correction.preciseWidth());
        result.setPreciseHeight(result.preciseHeight()
                + correction.preciseHeight());
        return snapOrientation;
    }

}
