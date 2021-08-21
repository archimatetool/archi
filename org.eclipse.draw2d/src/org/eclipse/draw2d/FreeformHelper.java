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

import java.util.List;

import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

@SuppressWarnings("rawtypes")
class FreeformHelper implements FreeformListener {

    class ChildTracker implements FigureListener {
        @Override
        public void figureMoved(IFigure source) {
            invalidate();
        }
    }

    private FreeformFigure host;
    private Rectangle freeformExtent;
    private FigureListener figureListener = new ChildTracker();

    FreeformHelper(FreeformFigure host) {
        this.host = host;
    }

    public Rectangle getFreeformExtent() {
        if (freeformExtent != null)
            return freeformExtent;
        Rectangle r;
        List children = host.getChildren();
        for (int i = 0; i < children.size(); i++) {
            IFigure child = (IFigure) children.get(i);
            if (child instanceof FreeformFigure)
                r = ((FreeformFigure) child).getFreeformExtent();
            else
                r = child.getBounds();
            if (freeformExtent == null)
                freeformExtent = r.getCopy();
            else
                freeformExtent.union(r);
        }
        Insets insets = host.getInsets();
        if (freeformExtent == null)
            freeformExtent = new Rectangle(0, 0, insets.getWidth(),
                    insets.getHeight());
        else {
            host.translateToParent(freeformExtent);
            freeformExtent.expand(insets);
        }
        // System.out.println("New extent calculated for " + host + " = " +
        // freeformExtent);
        return freeformExtent;
    }

    public void hookChild(IFigure child) {
        invalidate();
        if (child instanceof FreeformFigure)
            ((FreeformFigure) child).addFreeformListener(this);
        else
            child.addFigureListener(figureListener);
    }

    void invalidate() {
        freeformExtent = null;
        host.fireExtentChanged();
        if (host.getParent() != null)
            host.getParent().revalidate();
        else
            host.revalidate();
    }

    @Override
    public void notifyFreeformExtentChanged() {
        // A childs freeform extent has changed, therefore this extent must be
        // recalculated
        invalidate();
    }

    public void setFreeformBounds(Rectangle bounds) {
        host.setBounds(bounds);
        bounds = bounds.getCopy();
        host.translateFromParent(bounds);
        List children = host.getChildren();
        for (int i = 0; i < children.size(); i++) {
            IFigure child = (IFigure) children.get(i);
            if (child instanceof FreeformFigure)
                ((FreeformFigure) child).setFreeformBounds(bounds);
        }
    }

    public void unhookChild(IFigure child) {
        invalidate();
        if (child instanceof FreeformFigure)
            ((FreeformFigure) child).removeFreeformListener(this);
        else
            child.removeFigureListener(figureListener);
    }

}
