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
package org.eclipse.draw2d;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Places a handle relative to a figure's bounds. The placement is determined by
 * indicating the figure to which the placement is relative, and two
 * floating-point value indicating the horizontal and vertical offset from that
 * figure's top-left corner. The values (0.0, 0.0) would indicate the figure's
 * top-left corner, while the values (1.0, 1.0) would indicate the figure's
 * bottom-right corner.
 * <P>
 * Constants such as {@link PositionConstants#NORTH NORTH} and
 * {@link PositionConstants#SOUTH SOUTH} can be used to set the placement.
 */
public class RelativeLocator implements Locator {

    private double relativeX;
    private double relativeY;
    private IFigure reference;

    /**
     * Null constructor. The reference figure must be set before use. The
     * relative locations will default to (0.0, 0.0).
     * 
     * @since 2.0
     */
    public RelativeLocator() {
        relativeX = 0.0;
        relativeY = 0.0;
    }

    /**
     * Constructs a RelativeLocator with the given reference figure and relative
     * location. The location is a constant from {@link PositionConstants} used
     * as a convenient and readable way to set both the relativeX and relativeY
     * values.
     * 
     * @param reference
     *            the reference figure
     * @param location
     *            one of NORTH, NORTH_EAST, etc.
     * @since 2.0
     */
    public RelativeLocator(IFigure reference, int location) {
        setReferenceFigure(reference);
        switch (location & PositionConstants.NORTH_SOUTH) {
        case PositionConstants.NORTH:
            relativeY = 0;
            break;
        case PositionConstants.SOUTH:
            relativeY = 1.0;
            break;
        default:
            relativeY = 0.5;
        }

        switch (location & PositionConstants.EAST_WEST) {
        case PositionConstants.WEST:
            relativeX = 0;
            break;
        case PositionConstants.EAST:
            relativeX = 1.0;
            break;
        default:
            relativeX = 0.5;
        }
    }

    /**
     * Constructs a RelativeLocator with the given reference Figure and offset
     * ratios.
     * 
     * @param reference
     *            the reference figure
     * @param relativeX
     *            the relative X offset
     * @param relativeY
     *            the relative Y offset
     * @since 2.0
     */
    public RelativeLocator(IFigure reference, double relativeX, double relativeY) {
        setReferenceFigure(reference);
        this.relativeX = relativeX;
        this.relativeY = relativeY;
    }

    /**
     * Returns the Reference Box in the Reference Figure's coordinate system.
     * The returned Rectangle may be by reference, and should <b>not</b> be
     * modified.
     * 
     * @return the reference box
     * @since 2.0
     */
    protected Rectangle getReferenceBox() {
        return getReferenceFigure().getBounds();
    }

    /**
     * Returns the Figure this locator is relative to.
     * 
     * @return the reference figure
     * @since 2.0
     */
    protected IFigure getReferenceFigure() {
        return reference;
    }

    /**
     * Relocates the target using the relative offset locations.
     * 
     * @see org.eclipse.draw2d.Locator#relocate(org.eclipse.draw2d.IFigure)
     */
    @Override
    public void relocate(IFigure target) {
        IFigure reference = getReferenceFigure();
        Rectangle targetBounds = new PrecisionRectangle(getReferenceBox()
                .getResized(-1, -1));
        reference.translateToAbsolute(targetBounds);
        target.translateToRelative(targetBounds);
        targetBounds.resize(1, 1);

        Dimension targetSize = target.getPreferredSize();

        targetBounds.x += (int) (targetBounds.width * relativeX - ((targetSize.width + 1) / 2));
        targetBounds.y += (int) (targetBounds.height * relativeY - ((targetSize.height + 1) / 2));
        targetBounds.setSize(targetSize);
        target.setBounds(targetBounds);
    }

    /**
     * Sets the reference figure this locator uses to place the target figure.
     * 
     * @param reference
     *            the reference figure
     * @since 2.0
     */
    public void setReferenceFigure(IFigure reference) {
        this.reference = reference;
    }

}
