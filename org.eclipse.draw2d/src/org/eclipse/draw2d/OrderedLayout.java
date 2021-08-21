/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.draw2d;

import org.eclipse.draw2d.geometry.Transposer;

/**
 * Abstract superclass for layout managers that arrange their children in
 * columns/rows.
 * 
 * @since 3.7
 */
public abstract class OrderedLayout extends AbstractHintLayout {

    /** Constant to specify components to be aligned in the center */
    public static final int ALIGN_CENTER = 0;
    /** Constant to specify components to be aligned on the left/top */
    public static final int ALIGN_TOPLEFT = 1;
    /** Constant to specify components to be aligned on the right/bottom */
    public static final int ALIGN_BOTTOMRIGHT = 2;

    /**
     * Constant to specify components should be layed out horizontally
     * 
     * @deprecated Pulled up from derived layout manager and deprecated here
     *             because unused.
     */
    public static final boolean HORIZONTAL = true;
    /**
     * Constant to specify components should be layed out vertically
     * 
     * @deprecated Pulled up from derived layout manager and deprecated here
     *             because unused.
     */
    public static final boolean VERTICAL = false;

    /**
     * The horizontal property.
     * 
     * @deprecated Use {@link #setHorizontal(boolean)} and
     *             {@link #isHorizontal()} instead.
     */
    protected boolean horizontal;

    /**
     * The alignment along the minor axis.
     * 
     * @deprecated Use {@link #getMinorAlignment()} and
     *             {@link #setMinorAlignment(int)} instead.
     */
    protected int minorAlignment;

    /**
     * Transposer object that may be used in layout calculations. Will be
     * automatically enabled/disabled dependent on the default and the actual
     * orientation.
     * 
     * @noreference This field is not intended to be referenced by clients.
     */
    protected Transposer transposer = new Transposer();

    /**
     * Constructs a new {@link OrderedLayout} with the default orientation and a
     * minor alignment of {@link #ALIGN_TOPLEFT}.
     */
    public OrderedLayout() {
        setHorizontal(getDefaultOrientation() == PositionConstants.HORIZONTAL);
        setMinorAlignment(ALIGN_TOPLEFT);
    }

    /**
     * Returns the default orientation of this layout.
     * 
     * @return one of {@link PositionConstants#HORIZONTAL} or
     *         {@link PositionConstants#VERTICAL}
     */
    protected abstract int getDefaultOrientation();

    /**
     * Returns the minor alignment of the layout. Minor minor axis is the axis
     * perpendicular to the overall orientation set in the constructor.
     * 
     * @return the minor alignment
     */
    public int getMinorAlignment() {
        return minorAlignment;
    }

    /**
     * Returns <code>true</code> if the orientation of the layout is horizontal.
     * 
     * @return <code>true</code> if the orientation of the layout is horizontal
     */
    public boolean isHorizontal() {
        return horizontal;
    }

    /**
     * Returns whether figures should obtain the same height/width in the minor
     * axis.
     * 
     * @return whether children are to be stretched in the minor axis.
     * 
     * @TODO: Made abstract here, so that derived layout managers can guarantee
     *        backwards compatibility for their exposed fields. Can be made
     *        concrete in the next major release, removing implementations in
     *        the derived layout managers.
     */
    public abstract boolean isStretchMinorAxis();

    /**
     * Sets the orientation of the layout.
     * 
     * @param flag
     *            <code>true</code> if this layout should be horizontal,
     *            <code>false</code> otherwise.
     */
    public void setHorizontal(boolean flag) {
        if (horizontal == flag)
            return;
        invalidate();
        horizontal = flag;
        updateTransposerEnabledState();
    }

    /**
     * Sets the alignment of the children contained in the layout. Possible
     * values are {@link #ALIGN_CENTER}, {@link #ALIGN_BOTTOMRIGHT} and
     * {@link #ALIGN_TOPLEFT}.
     * 
     * @param align
     *            the minor alignment
     */
    public void setMinorAlignment(int align) {
        minorAlignment = align;
    }

    /**
     * Causes children that are smaller in the dimension of the minor axis to be
     * stretched to fill the minor axis. The minor axis is the opposite of the
     * orientation. That is, in horizontal orientation, all figures will have
     * the same height. If in vertical orientation, all figures will have the
     * same width.
     * 
     * @param value
     *            whether children should be stretched in the minor axis.
     * @TODO: Made abstract here, so that derived layout managers can guarantee
     *        backwards compatibility for their exposed fields. Can be made
     *        concrete in the next major release, removing implementations in
     *        the derived layout managers.
     */
    public abstract void setStretchMinorAxis(boolean value);

    /**
     * Updates the enabled state of the {@link #transposer} in case the layout
     * has a different orientation that its default one.
     */
    private void updateTransposerEnabledState() {
        // enable transposer if the current orientation differs from the default
        // orientation, disable it otherwise
        transposer.setEnabled(isHorizontal()
                && getDefaultOrientation() == PositionConstants.VERTICAL
                || !isHorizontal()
                && getDefaultOrientation() == PositionConstants.HORIZONTAL);
    }
}
