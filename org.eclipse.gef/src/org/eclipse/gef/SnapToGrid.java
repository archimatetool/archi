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

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionRectangle;

/**
 * A helper used to perform snapping to a grid, which is specified on the
 * graphical viewer via the various properties defined in this class. This
 * helper can be used in conjunction with the
 * {@link org.eclipse.gef.tools.DragEditPartsTracker DragEditPartsTracker} when
 * dragging editparts within a graphical viewer. When snapping a rectangle, the
 * edges of the rectangle will snap along gridlines.
 * <P>
 * This helper does not keep up with changes made to the graphical viewer's
 * properties. Clients should instantiate a new helper each time one is
 * requested and not hold on to instances of the helper, if the grid properties
 * specified on the viewer are subject to change.
 * 
 * @author Randy Hudson
 * @author Pratik Shah
 * @since 3.0
 * @see org.eclipse.gef.editparts.GridLayer
 */
public class SnapToGrid extends SnapToHelper {

    /**
     * A viewer property indicating whether the snap function is enabled. The
     * value must be a Boolean.
     */
    public static final String PROPERTY_GRID_ENABLED = "SnapToGrid.isEnabled"; //$NON-NLS-1$

    /**
     * A viewer property indicating whether the grid should be displayed. The
     * value must be a Boolean.
     */
    public static final String PROPERTY_GRID_VISIBLE = "SnapToGrid.isVisible"; //$NON-NLS-1$
    /**
     * A viewer property indicating the grid spacing. The value must be a
     * {@link Dimension}.
     */
    public static final String PROPERTY_GRID_SPACING = "SnapToGrid.GridSpacing"; //$NON-NLS-1$
    /**
     * A viewer property indicating the grid's origin. The value must be a
     * {@link Point}.
     */
    public static final String PROPERTY_GRID_ORIGIN = "SnapToGrid.GridOrigin"; //$NON-NLS-1$

    /**
     * The default grid size if the viewer does not specify a size.
     * 
     * @see #PROPERTY_GRID_SPACING
     */
    public static final int DEFAULT_GRID_SIZE = 12;

    /**
     * @deprecated use DEFAULT_GRID_SIZE
     */
    public static final int DEFAULT_GAP = DEFAULT_GRID_SIZE;

    /**
     * The graphical part whose content's figure defines the grid.
     */
    protected GraphicalEditPart container;

    /**
     * The horizontal interval for the grid
     */
    protected int gridX;

    /**
     * The vertical interval for the grid
     */
    protected int gridY;

    /**
     * The origin of the grid.
     */
    protected Point origin;

    /**
     * Constructs a gridded snap helper on the given editpart. The editpart
     * should be the graphical editpart whose contentspane figure is used as the
     * reference for the grid.
     * 
     * @param container
     *            the editpart which the grid is on
     */
    public SnapToGrid(GraphicalEditPart container) {
        this.container = container;
        Dimension spacing = (Dimension) container.getViewer().getProperty(
                PROPERTY_GRID_SPACING);
        if (spacing != null) {
            gridX = spacing.width;
            gridY = spacing.height;
        }
        if (gridX == 0)
            gridX = DEFAULT_GRID_SIZE;
        if (gridY == 0)
            gridY = DEFAULT_GRID_SIZE;
        Point loc = (Point) container.getViewer().getProperty(
                PROPERTY_GRID_ORIGIN);
        if (loc != null)
            origin = loc;
        else
            origin = new Point();
    }

    /**
     * @see SnapToHelper#snapRectangle(Request, int, PrecisionRectangle,
     *      PrecisionRectangle)
     */
    @Override
    public int snapRectangle(Request request, int snapLocations,
            PrecisionRectangle rect, PrecisionRectangle result) {

        rect = rect.getPreciseCopy();
        makeRelative(container.getContentPane(), rect);
        PrecisionRectangle correction = new PrecisionRectangle();
        makeRelative(container.getContentPane(), correction);

        if (gridX > 0 && (snapLocations & EAST) != 0) {
            correction.setPreciseWidth(correction.preciseWidth()
                    - Math.IEEEremainder(rect.preciseRight() - origin.x - 1,
                            gridX));
            snapLocations &= ~EAST;
        }

        if ((snapLocations & (WEST | HORIZONTAL)) != 0 && gridX > 0) {
            double leftCorrection = Math.IEEEremainder(rect.preciseX()
                    - origin.x, gridX);
            correction.setPreciseX(correction.preciseX() - leftCorrection);
            if ((snapLocations & HORIZONTAL) == 0) {
                correction.setPreciseWidth(correction.preciseWidth()
                        + leftCorrection);
            }
            snapLocations &= ~(WEST | HORIZONTAL);
        }

        if ((snapLocations & SOUTH) != 0 && gridY > 0) {
            correction.setPreciseHeight(correction.preciseHeight()
                    - Math.IEEEremainder(rect.preciseBottom() - origin.y - 1,
                            gridY));
            snapLocations &= ~SOUTH;
        }

        if ((snapLocations & (NORTH | VERTICAL)) != 0 && gridY > 0) {
            double topCorrection = Math.IEEEremainder(rect.preciseY()
                    - origin.y, gridY);
            correction.setPreciseY(correction.preciseY() - topCorrection);
            if ((snapLocations & VERTICAL) == 0) {
                correction.setPreciseHeight(correction.preciseHeight()
                        + topCorrection);
            }
            snapLocations &= ~(NORTH | VERTICAL);
        }

        makeAbsolute(container.getContentPane(), correction);
        result.setPreciseX(result.preciseX() + correction.preciseX());
        result.setPreciseY(result.preciseY() + correction.preciseY());
        result.setPreciseWidth(result.preciseWidth()
                + correction.preciseWidth());
        result.setPreciseHeight(result.preciseHeight()
                + correction.preciseHeight());
        return snapLocations;
    }

}
