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
 * Similar to a {@link org.eclipse.draw2d.ChopboxAnchor}, except this anchor is
 * located on the ellipse defined by the owners bounding box.
 * 
 * @author Alex Selkov Created 31.08.2002 23:11:43
 */
public class EllipseAnchor extends AbstractConnectionAnchor {

    /**
     * @see org.eclipse.draw2d.AbstractConnectionAnchor#AbstractConnectionAnchor()
     */
    public EllipseAnchor() {
    }

    /**
     * @see org.eclipse.draw2d.AbstractConnectionAnchor#AbstractConnectionAnchor(IFigure)
     */
    public EllipseAnchor(IFigure owner) {
        super(owner);
    }

    /**
     * Returns a point on the ellipse (defined by the owner's bounding box)
     * where the connection should be anchored.
     * 
     * @see org.eclipse.draw2d.ConnectionAnchor#getLocation(Point)
     */
    @Override
    public Point getLocation(Point reference) {
        Rectangle r = Rectangle.SINGLETON;
        r.setBounds(getOwner().getBounds());
        r.translate(-1, -1);
        r.resize(1, 1);
        getOwner().translateToAbsolute(r);

        Point ref = r.getCenter().negate().translate(reference);

        if (ref.x == 0)
            return new Point(reference.x, (ref.y > 0) ? r.bottom() : r.y);
        if (ref.y == 0)
            return new Point((ref.x > 0) ? r.right() : r.x, reference.y);

        float dx = (ref.x > 0) ? 0.5f : -0.5f;
        float dy = (ref.y > 0) ? 0.5f : -0.5f;

        // ref.x, ref.y, r.width, r.height != 0 => safe to proceed

        float k = (float) (ref.y * r.width) / (ref.x * r.height);
        k = k * k;

        return r.getCenter().translate((int) (r.width * dx / Math.sqrt(1 + k)),
                (int) (r.height * dy / Math.sqrt(1 + 1 / k)));
    }

    /**
     * Returns <code>true</code> if the other anchor is an EllipseAnchor with
     * the same owner.
     * 
     * @param o
     *            the other anchor
     * @return <code>true</code> if equal
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof EllipseAnchor) {
            EllipseAnchor other = (EllipseAnchor) o;
            return other.getOwner() == getOwner();
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
