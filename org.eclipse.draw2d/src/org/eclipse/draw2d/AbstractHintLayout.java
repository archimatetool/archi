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

/**
 * The foundation for layout managers which are sensitive to width and/or height
 * hints. This class will cache preferred and minimum sizes for a given set of
 * hints. If the hints change in a meaningful way, the cached size is thrown out
 * and redetermined.
 * <P>
 * Subclasses may be sensitive to one or both hints. By default, this class
 * assumes both hints are important. Subclasses may override this behavior in
 * {@link #isSensitiveHorizontally(IFigure)} and
 * {@link #isSensitiveVertically(IFigure)}. At least one of these method should
 * return <code>true</code>.
 * 
 * @author hudsonr
 * @since 2.0
 */
public abstract class AbstractHintLayout extends AbstractLayout {

    private Dimension minimumSize = null;
    private Dimension cachedPreferredHint = new Dimension(-1, -1);
    private Dimension cachedMinimumHint = new Dimension(-1, -1);

    /**
     * Calculates the minimum size using the given width and height hints. This
     * method is called from {@link #getMinimumSize(IFigure, int, int)} whenever
     * the cached minimum size has been flushed.
     * <P>
     * By default, this method just calls
     * {@link #getPreferredSize(IFigure, int, int)}, meaning minimum and
     * preferres sizes will be the same unless this method is overridden.
     * 
     * @param container
     *            the Figure on which this layout is installed
     * @param wHint
     *            the width hint
     * @param hHint
     *            the height hint
     * 
     * @return the layout's minimum size
     */
    protected Dimension calculateMinimumSize(IFigure container, int wHint,
            int hHint) {
        return getPreferredSize(container, wHint, hHint);
    }

    /**
     * @see org.eclipse.draw2d.LayoutManager#getMinimumSize(IFigure, int, int)
     */
    @Override
    public Dimension getMinimumSize(IFigure container, int w, int h) {
        boolean flush = cachedMinimumHint.width != w
                && isSensitiveHorizontally(container);
        flush |= cachedMinimumHint.height != h
                && isSensitiveVertically(container);
        if (flush) {
            minimumSize = null;
            cachedMinimumHint.width = w;
            cachedMinimumHint.height = h;
        }
        if (minimumSize == null)
            minimumSize = calculateMinimumSize(container, w, h);
        return minimumSize;
    }

    /**
     * @see org.eclipse.draw2d.LayoutManager#getPreferredSize(IFigure, int, int)
     */
    @Override
    public final Dimension getPreferredSize(IFigure container, int w, int h) {
        boolean flush = cachedPreferredHint.width != w
                && isSensitiveHorizontally(container);
        flush |= cachedPreferredHint.height != h
                && isSensitiveVertically(container);
        if (flush) {
            preferredSize = null;
            cachedPreferredHint.width = w;
            cachedPreferredHint.height = h;
        }
        return super.getPreferredSize(container, w, h);
    }

    /**
     * Extends the superclass implementation to flush the cached minimum size.
     * 
     * @see org.eclipse.draw2d.LayoutManager#invalidate()
     */
    @Override
    public void invalidate() {
        minimumSize = null;
        super.invalidate();
    }

    /**
     * Returns whether this layout manager is sensitive to changes in the
     * horizontal hint. By default, this method returns <code>true</code>.
     * 
     * @param container
     *            the layout's container
     * @return <code>true</code> if this layout is sensite to horizontal hint
     *         changes
     */
    protected boolean isSensitiveHorizontally(IFigure container) {
        return true;
    }

    /**
     * Returns whether this layout manager is sensitive to changes in the
     * vertical hint. By default, this method returns <code>true</code>.
     * 
     * @param container
     *            the layout's container
     * @return <code>true</code> if this layout is sensite to vertical hint
     *         changes
     */
    protected boolean isSensitiveVertically(IFigure container) {
        return true;
    }

}
