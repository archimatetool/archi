/*******************************************************************************
 * Copyright (c) 2004, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.editparts;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * This is a layer where the guide lines are displayed. The figures in this
 * layer should have a Boolean constraint indicating whether or not they are
 * horizontal guide lines.
 * 
 * @author Pratik Shah
 * @since 3.0
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class GuideLayer extends FreeformLayer {

    private Map constraints;

    /**
     * @param child
     *            the figure whose constraint is to be found
     * @return the constraint (Boolean indicating whether or not it is
     *         horizontal) set for the given IFigure; <code>null</code>, if none
     *         exists
     */
    public Object getConstraint(IFigure child) {
        return getConstraints().get(child);
    }

    /**
     * @return the Map of IFigures to their constraints (Booleans indicating
     *         whether or not they are horizontal guide lines)
     */
    public Map getConstraints() {
        if (constraints == null) {
            constraints = new HashMap();
        }
        return constraints;
    }

    /**
     * @see org.eclipse.draw2d.FreeformFigure#getFreeformExtent()
     */
    @Override
    public Rectangle getFreeformExtent() {
        /*
         * The freeform extents are just big enough to include all the children:
         * tall enough to include the highest and the lowest horizontal guide
         * lines, and wide enough to include the leftmost and the rightmost
         * vertical guide lines. They always include the point 5,5 and are
         * further expanded by 5-pixels in all directions so that none of the
         * guide lines are on the edge.
         */
        /*
         * These ints are initialized to 5, and not 0, so that when the final
         * extent is expanded by 5, the bounds do not go into the negative
         * (unless necessary).
         */
        int maxX = 5, minX = 5, maxY = 5, minY = 5;
        Iterator children = getChildren().iterator();
        while (children.hasNext()) {
            IFigure child = (IFigure) children.next();
            Boolean isHorizontal = (Boolean) getConstraint(child);
            if (isHorizontal != null) {
                if (isHorizontal.booleanValue()) {
                    int position = child.getBounds().y;
                    minY = Math.min(minY, position);
                    maxY = Math.max(maxY, position);
                } else {
                    int position = child.getBounds().x;
                    minX = Math.min(minX, position);
                    maxX = Math.max(maxX, position);
                }
            }
        }
        Rectangle r = new Rectangle(minX, minY, maxX - minX + 1, maxY - minY
                + 1);
        if (r.width > 1)
            r.expand(5, 0);
        if (r.height > 1)
            r.expand(0, 5);
        return r;
    }

    /**
     * @see org.eclipse.draw2d.IFigure#getPreferredSize(int, int)
     */
    @Override
    public Dimension getPreferredSize(int wHint, int hHint) {
        Rectangle extents = getFreeformExtent();
        return new Dimension(extents.right(), extents.bottom());
    }

    /**
     * @see org.eclipse.draw2d.IFigure#remove(org.eclipse.draw2d.IFigure)
     */
    @Override
    public void remove(IFigure child) {
        getConstraints().remove(child);
        super.remove(child);
    }

    /**
     * @see org.eclipse.draw2d.IFigure#setBounds(org.eclipse.draw2d.geometry.Rectangle)
     */
    @Override
    public void setBounds(Rectangle rect) {
        super.setBounds(rect);
        Iterator children = getChildren().iterator();
        while (children.hasNext()) {
            IFigure child = (IFigure) children.next();
            Boolean isHorizontal = (Boolean) getConstraint(child);
            if (isHorizontal != null) {
                if (isHorizontal.booleanValue()) {
                    Rectangle.SINGLETON.setLocation(getBounds().x,
                            child.getBounds().y);
                    Rectangle.SINGLETON.setSize(getBounds().width, 1);
                } else {
                    Rectangle.SINGLETON.setLocation(child.getBounds().x,
                            getBounds().y);
                    Rectangle.SINGLETON.setSize(1, getBounds().height);
                }
                child.setBounds(Rectangle.SINGLETON);
            }
        }
    }

    /**
     * The constraint is expected to be a Boolean indicating whether the given
     * guide line figure is horizontal or not.
     * 
     * @see org.eclipse.draw2d.IFigure#setConstraint(org.eclipse.draw2d.IFigure,
     *      java.lang.Object)
     */
    @Override
    public void setConstraint(IFigure child, Object constraint) {
        getConstraints().put(child, constraint);
    }

}
