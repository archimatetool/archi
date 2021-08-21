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

import java.util.Map;

import org.eclipse.draw2d.geometry.PrecisionRectangle;

import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.gef.rulers.RulerProvider;

/**
 * A helper used to perform snapping to guides. The guides are obtained from the
 * viewer's horizontal and vertical {@link RulerProvider RulerProviders}. If
 * snapping is performed, the request's extended data will contain keyed values
 * indicating which guides were snapped to, and which side of the part should be
 * attached. Generally snapping to a guide should attach the part to that guide,
 * but application behavior may differ.
 * <P>
 * Snapping (and attaching) to a guide is only possible if a single part is
 * being dragged. The current implementation will not snap if a request contains
 * multiple parts. This may be relaxed in the future to allow snapping, but
 * without setting the attachment extended data.
 * <P>
 * This helper does not keep up with changes in guides. Clients should
 * instantiate a new helper each time one is requested and not hold on to
 * instances of the helper.
 * 
 * @since 3.0
 * @author Randy Hudson
 * @author Pratik Shah
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class SnapToGuides extends SnapToHelper {

    /**
     * The key used to identify the Vertical Guide. This key is used with the
     * request's extended data map to store an Integer. The integer value is the
     * location of the guide that is being snapped to.
     */
    public static final String KEY_VERTICAL_GUIDE = "SnapToGuides.VerticalGuide"; //$NON-NLS-1$

    /**
     * The key used to identify the Horizontal Guide. This key is used with the
     * request's extended data map to store an Integer. The integer value is the
     * location of the guide that is being snapped to.
     */
    public static final String KEY_HORIZONTAL_GUIDE = "SnapToGuides.HorizontalGuide"; //$NON-NLS-1$

    /**
     * The key used to identify the vertical anchor point. This key is used with
     * the request's extended data map to store an Integer. If the
     * VERTICAL_GUIDE has been set, then this integer is a number identifying
     * which side of the dragged object is being snapped to that guide.
     * <UL>
     * <LI><code>-1</code> indicates the left side should be attached.
     * <LI><code> 0</code> indicates the center should be attached.
     * <LI><code> 1</code> indicates the right side should be attached.
     * </UL>
     */
    public static final String KEY_VERTICAL_ANCHOR = "SnapToGuides.VerticalAttachment"; //$NON-NLS-1$

    /**
     * The key used to identify the horizontal anchor point. This key is used
     * with the request's extended data map to store an Integer. If the
     * HORIZONTAL_GUIDE has been set, then this integer is a number identifying
     * which side of the dragged object is being snapped to that guide.
     * <UL>
     * <LI><code>-1</code> indicates the top side should be attached.
     * <LI><code> 0</code> indicates the middle should be attached.
     * <LI><code> 1</code> indicates the bottom side should be attached.
     * </UL>
     */
    public static final String KEY_HORIZONTAL_ANCHOR = "SnapToGuides.HorizontalAttachment";//$NON-NLS-1$

    /**
     * The threshold for snapping to guides. The rectangle being snapped must be
     * within +/- the THRESHOLD. The default value is 5.001;
     */
    protected static final double THRESHOLD = 5.001;

    private double threshold = THRESHOLD;

    /**
     * The graphical editpart to which guides are relative. This should also the
     * parent of the parts being snapped to guides.
     */
    protected GraphicalEditPart container;

    /**
     * The locations of the vertical guides in the container's coordinates. Use
     * {@link #getVerticalGuides()}.
     */
    protected int[] verticalGuides;

    /**
     * The locations of the horizontal guides in the container's coordinates.
     * Use {@link #getHorizontalGuides()}.
     */
    protected int[] horizontalGuides;

    /**
     * Constructs a new snap-to-guides helper using the given container as the
     * basis.
     * 
     * @param container
     *            the container editpart
     */
    public SnapToGuides(GraphicalEditPart container) {
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
     * Returns the correction for the given near and far sides of a rectangle or
     * {@link #getThreshold()} if no correction was found. The near side
     * represents the top or left side of a rectangle being snapped. Similar for
     * far. If snapping occurs, the extendedData will have the guide and
     * attachment point set.
     * 
     * @param guides
     *            the location of the guides
     * @param near
     *            the top or left location
     * @param far
     *            the bottom or right location
     * @param extendedData
     *            the map for storing snap details
     * @param isVertical
     *            <code>true</code> if for vertical guides, <code>false</code>
     *            for horizontal.
     * @return the correction amount or getThreshold() if no correction was made
     */
    protected double getCorrectionFor(int[] guides, double near, double far,
            Map extendedData, boolean isVertical) {
        far -= 1.0;
        double total = near + far;
        // If the width is even, there is no middle pixel so favor the left -
        // most pixel.
        if ((int) (near - far) % 2 == 0)
            total -= 1.0;
        double result = getCorrectionFor(guides, total / 2, extendedData,
                isVertical, 0);
        if (result == getThreshold())
            result = getCorrectionFor(guides, near, extendedData, isVertical,
                    -1);
        if (result == getThreshold())
            result = getCorrectionFor(guides, far, extendedData, isVertical, 1);
        return result;
    }

    /**
     * Returns the correction for the given location or {@link #getThreshold()}
     * if no correction was found. If correction occurs, the extendedData will
     * have the guide and attachment point set. The attachment point is
     * identified by the <code>side</code> parameter.
     * <P>
     * The correction's magnitude will be less than getThreshold().
     * 
     * @param guides
     *            the location of the guides
     * @param value
     *            the location being tested
     * @param extendedData
     *            the map for storing snap details
     * @param vert
     *            <code>true</code> if for vertical guides, <code>false</code>
     * @param side
     *            the integer indicating which side is being snapped
     * @return a correction amount or getThreshold() if no correction was made
     */
    @SuppressWarnings("deprecation")
    protected double getCorrectionFor(int[] guides, double value,
            Map extendedData, boolean vert, int side) {
        double resultMag = getThreshold();
        double result = getThreshold();

        for (int i = 0; i < guides.length; i++) {
            int offset = guides[i];
            double magnitude;

            magnitude = Math.abs(value - offset);
            if (magnitude < resultMag) {
                extendedData.put(vert ? KEY_VERTICAL_GUIDE
                        : KEY_HORIZONTAL_GUIDE, new Integer(guides[i]));
                extendedData.put(vert ? KEY_VERTICAL_ANCHOR
                        : KEY_HORIZONTAL_ANCHOR, new Integer(side));
                resultMag = magnitude;
                result = offset - value;
            }
        }
        return result;
    }

    /**
     * Returns the horizontal guides in the coordinates of the container's
     * contents pane.
     * 
     * @return the horizontal guides
     */
    protected int[] getHorizontalGuides() {
        if (horizontalGuides == null) {
            RulerProvider rProvider = ((RulerProvider) container.getViewer()
                    .getProperty(RulerProvider.PROPERTY_VERTICAL_RULER));
            if (rProvider != null)
                horizontalGuides = rProvider.getGuidePositions();
            else
                horizontalGuides = new int[0];
        }
        return horizontalGuides;
    }

    /**
     * Returns the vertical guides in the coordinates of the container's
     * contents pane.
     * 
     * @return the vertical guides
     */
    protected int[] getVerticalGuides() {
        if (verticalGuides == null) {
            RulerProvider rProvider = ((RulerProvider) container.getViewer()
                    .getProperty(RulerProvider.PROPERTY_HORIZONTAL_RULER));
            if (rProvider != null)
                verticalGuides = rProvider.getGuidePositions();
            else
                verticalGuides = new int[0];
        }
        return verticalGuides;
    }

    /**
     * @see SnapToHelper#snapRectangle(Request, int, PrecisionRectangle,
     *      PrecisionRectangle)
     */
    @Override
    public int snapRectangle(Request request, int snapOrientation,
            PrecisionRectangle baseRect, PrecisionRectangle result) {
        if (request instanceof GroupRequest
                && ((GroupRequest) request).getEditParts().size() > 1)
            return snapOrientation;

        baseRect = baseRect.getPreciseCopy();
        makeRelative(container.getContentPane(), baseRect);
        PrecisionRectangle correction = new PrecisionRectangle();
        makeRelative(container.getContentPane(), correction);

        if ((snapOrientation & HORIZONTAL) != 0) {
            double xcorrect = getCorrectionFor(getVerticalGuides(),
                    baseRect.preciseX(), baseRect.preciseRight(),
                    request.getExtendedData(), true);
            if (xcorrect != getThreshold()) {
                snapOrientation &= ~HORIZONTAL;
                correction.setPreciseX(correction.preciseX() + xcorrect);
            }
        }

        if ((snapOrientation & VERTICAL) != 0) {
            double ycorrect = getCorrectionFor(getHorizontalGuides(),
                    baseRect.preciseY(), baseRect.preciseBottom(),
                    request.getExtendedData(), false);
            if (ycorrect != getThreshold()) {
                snapOrientation &= ~VERTICAL;
                correction.setPreciseY(correction.preciseY() + ycorrect);
            }
        }

        boolean snapped = false;
        if (!snapped && (snapOrientation & WEST) != 0) {
            double leftCorrection = getCorrectionFor(getVerticalGuides(),
                    baseRect.preciseX(), request.getExtendedData(), true, -1);
            if (leftCorrection != getThreshold()) {
                snapOrientation &= ~WEST;
                correction.setPreciseWidth(correction.preciseWidth()
                        - leftCorrection);
                correction.setPreciseX(correction.preciseX() + leftCorrection);
            }
        }

        if (!snapped && (snapOrientation & EAST) != 0) {
            double rightCorrection = getCorrectionFor(getVerticalGuides(),
                    baseRect.preciseRight() - 1, request.getExtendedData(),
                    true, 1);
            if (rightCorrection != getThreshold()) {
                snapped = true;
                snapOrientation &= ~EAST;
                correction.setPreciseWidth(correction.preciseWidth()
                        + rightCorrection);
            }
        }

        snapped = false;
        if (!snapped && (snapOrientation & NORTH) != 0) {
            double topCorrection = getCorrectionFor(getHorizontalGuides(),
                    baseRect.preciseY(), request.getExtendedData(), false, -1);
            if (topCorrection != getThreshold()) {
                snapOrientation &= ~NORTH;
                correction.setPreciseHeight(correction.preciseHeight()
                        - topCorrection);
                correction.setPreciseY(correction.preciseY() + topCorrection);
            }
        }

        if (!snapped && (snapOrientation & SOUTH) != 0) {
            double bottom = getCorrectionFor(getHorizontalGuides(),
                    baseRect.preciseBottom() - 1, request.getExtendedData(),
                    false, 1);
            if (bottom != getThreshold()) {
                snapped = true;
                snapOrientation &= ~SOUTH;
                correction
                        .setPreciseHeight(correction.preciseHeight() + bottom);
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
