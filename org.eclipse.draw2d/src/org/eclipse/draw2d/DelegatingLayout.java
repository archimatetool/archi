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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.geometry.Dimension;

/**
 * Figures using a DelegatingLayout as their layout manager give location
 * responsibilities to their children. The children of a Figure using a
 * DelegatingLayout should have a {@link Locator Locator} as a constraint whose
 * {@link Locator#relocate(IFigure target) relocate} method is responsible for
 * placing the child.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class DelegatingLayout extends AbstractLayout {

    private Map constraints = new HashMap();

    /**
     * Calculates the preferred size of the given Figure. For the
     * DelegatingLayout, this is the largest width and height values of the
     * passed Figure's children.
     * 
     * @param parent
     *            the figure whose preferred size is being calculated
     * @param wHint
     *            the width hint
     * @param hHint
     *            the height hint
     * @return the preferred size
     * @since 2.0
     */
    @Override
    protected Dimension calculatePreferredSize(IFigure parent, int wHint,
            int hHint) {
        List children = parent.getChildren();
        Dimension d = new Dimension();
        for (int i = 0; i < children.size(); i++) {
            IFigure child = (IFigure) children.get(i);
            d.union(child.getPreferredSize());
        }
        return d;
    }

    /**
     * @see org.eclipse.draw2d.LayoutManager#getConstraint(org.eclipse.draw2d.IFigure)
     */
    @Override
    public Object getConstraint(IFigure child) {
        return constraints.get(child);
    }

    /**
     * Lays out the given figure's children based on their {@link Locator}
     * constraint.
     * 
     * @param parent
     *            the figure whose children should be layed out
     */
    @Override
    public void layout(IFigure parent) {
        List children = parent.getChildren();
        for (int i = 0; i < children.size(); i++) {
            IFigure child = (IFigure) children.get(i);
            Locator locator = (Locator) constraints.get(child);
            if (locator != null) {
                locator.relocate(child);
            }
        }
    }

    /**
     * Removes the locator for the given figure.
     * 
     * @param child
     *            the child being removed
     */
    @Override
    public void remove(IFigure child) {
        constraints.remove(child);
    }

    /**
     * Sets the constraint for the given figure.
     * 
     * @param figure
     *            the figure whose contraint is being set
     * @param constraint
     *            the new constraint
     */
    @Override
    public void setConstraint(IFigure figure, Object constraint) {
        super.setConstraint(figure, constraint);
        if (constraint != null)
            constraints.put(figure, constraint);
    }

}
