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

/**
 * A transparent figure intended to be added exclusively to a
 * {@link LayeredPane}, who has the responsibilty of managing its layers.
 */
public class Layer extends Figure {

    /**
     * Overridden to implement transparent behavior.
     * 
     * @see IFigure#containsPoint(int, int)
     * 
     */
    @Override
    public boolean containsPoint(int x, int y) {
        if (isOpaque())
            return super.containsPoint(x, y);
        Point pt = Point.SINGLETON;
        pt.setLocation(x, y);
        translateFromParent(pt);
        x = pt.x;
        y = pt.y;
        for (int i = 0; i < getChildren().size(); i++) {
            IFigure child = (IFigure) getChildren().get(i);
            if (child.containsPoint(x, y))
                return true;
        }
        return false;
    }

    /**
     * Overridden to implement transparency.
     * 
     * @see IFigure#findFigureAt(int, int, TreeSearch)
     */
    @Override
    public IFigure findFigureAt(int x, int y, TreeSearch search) {
        if (!isEnabled())
            return null;
        if (isOpaque())
            return super.findFigureAt(x, y, search);

        IFigure f = super.findFigureAt(x, y, search);
        if (f == this)
            return null;
        return f;
    }

}
