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

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * The ChopboxAnchor's location is found by calculating the intersection of a
 * line drawn from the center point of its owner's box to a reference point on
 * that box. Thus {@link Connection Connections} using the ChopBoxAnchor will be
 * oriented such that they point to their owner's center.
 */
public class ChopboxAnchor extends AbstractConnectionAnchor {

    /**
     * Constructs a new ChopboxAnchor.
     */
    protected ChopboxAnchor() {
    }

    /**
     * Constructs a ChopboxAnchor with the given <i>owner</i> figure.
     * 
     * @param owner
     *            The owner figure
     * @since 2.0
     */
    public ChopboxAnchor(IFigure owner) {
        super(owner);
    }

    /**
     * Gets a Rectangle from {@link #getBox()} and returns the Point where a
     * line from the center of the Rectangle to the Point <i>reference</i>
     * intersects the Rectangle.
     * 
     * @param reference
     *            The reference point
     * @return The anchor location
     */
    @Override
    public Point getLocation(Point reference) {
        Rectangle r = Rectangle.SINGLETON;
        r.setBounds(getBox());
        r.translate(-1, -1);
        r.resize(1, 1);

        getOwner().translateToAbsolute(r);
        float centerX = r.x + 0.5f * r.width;
        float centerY = r.y + 0.5f * r.height;

        if (r.isEmpty()
                || (reference.x == (int) centerX && reference.y == (int) centerY))
            return new Point((int) centerX, (int) centerY); // This avoids
                                                            // divide-by-zero

        float dx = reference.x - centerX;
        float dy = reference.y - centerY;

        // r.width, r.height, dx, and dy are guaranteed to be non-zero.
        float scale = 0.5f / Math.max(Math.abs(dx) / r.width, Math.abs(dy)
                / r.height);

        dx *= scale;
        dy *= scale;
        centerX += dx;
        centerY += dy;

        return new Point(Math.round(centerX), Math.round(centerY));
    }

    /**
     * Returns the bounds of this ChopboxAnchor's owner. Subclasses can override
     * this method to adjust the box the anchor can be placed on. For instance,
     * the owner figure may have a drop shadow that should not be included in
     * the box.
     * 
     * @return The bounds of this ChopboxAnchor's owner
     * @since 2.0
     */
    protected Rectangle getBox() {
        return getOwner().getBounds();
    }

    /**
     * Returns the anchor's reference point. In the case of the ChopboxAnchor,
     * this is the center of the anchor's owner.
     * 
     * @return The reference point
     */
    @Override
    public Point getReferencePoint() {
        Point ref = getBox().getCenter();
        getOwner().translateToAbsolute(ref);
        return ref;
    }

    /**
     * Returns <code>true</code> if the other anchor has the same owner and box.
     * 
     * @param obj
     *            the other anchor
     * @return <code>true</code> if equal
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChopboxAnchor) {
            ChopboxAnchor other = (ChopboxAnchor) obj;
            return other.getOwner() == getOwner()
                    && other.getBox().equals(getBox());
        }
        return false;
    }

    /**
     * The owning figure's hashcode is used since equality is approximately
     * based on the owner.
     * 
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        if (getOwner() != null)
            return getOwner().hashCode();
        else
            return super.hashCode();
    }

}
