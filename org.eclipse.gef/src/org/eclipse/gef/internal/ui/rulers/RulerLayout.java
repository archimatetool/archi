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
package org.eclipse.gef.internal.ui.rulers;

import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A custom layout manager for rulers. It is not meant to be used externally or
 * with any figure other than a
 * {@link org.eclipse.gef.internal.ui.rulers.RulerFigure ruler}.
 * 
 * @author Pratik Shah
 * @since 3.0
 */
public class RulerLayout extends XYLayout {

    /**
     * @see org.eclipse.draw2d.AbstractLayout#calculatePreferredSize(org.eclipse.draw2d.IFigure,
     *      int, int)
     */
    @Override
    protected Dimension calculatePreferredSize(IFigure container, int wHint,
            int hHint) {
        return new Dimension(1, 1);
    }

    /**
     * @see org.eclipse.draw2d.AbstractLayout#getConstraint(org.eclipse.draw2d.IFigure)
     */
    @Override
    public Object getConstraint(IFigure child) {
        return constraints.get(child);
    }

    /**
     * @see org.eclipse.draw2d.LayoutManager#layout(org.eclipse.draw2d.IFigure)
     */
    @SuppressWarnings("rawtypes")
    @Override
    public void layout(IFigure container) {
        List children = container.getChildren();
        Rectangle rulerSize = container.getClientArea();
        for (int i = 0; i < children.size(); i++) {
            IFigure child = (IFigure) children.get(i);
            Dimension childSize = child.getPreferredSize();
            int position = ((Integer) getConstraint(child)).intValue();
            if (((RulerFigure) container).isHorizontal()) {
                childSize.height = rulerSize.height - 1;
                Rectangle.SINGLETON.setLocation(position
                        - (childSize.width / 2), rulerSize.y);
            } else {
                childSize.width = rulerSize.width - 1;
                Rectangle.SINGLETON.setLocation(rulerSize.x, position
                        - (childSize.height / 2));
            }
            Rectangle.SINGLETON.setSize(childSize);
            child.setBounds(Rectangle.SINGLETON);
        }
    }

}
