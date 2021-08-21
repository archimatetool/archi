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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;

/**
 * A collection of cursors.
 */
public class Cursors {

    /**
     * Returns the cursor corresponding to the given direction, defined in
     * {@link PositionConstants}. Note that
     * {@link #getDirectionalCursor(int, boolean)} should be used for
     * applications which want to run properly when running in a mirrored
     * environment. The behavior is the same as calling
     * {@link #getDirectionalCursor(int, boolean)
     * getDirectionalCursor(direction, false)}.
     * 
     * @param direction
     *            the relative direction of the desired cursor
     * @return The appropriate directional cursor
     */
    public static Cursor getDirectionalCursor(int direction) {
        return getDirectionalCursor(direction, false);
    }

    /**
     * Returns the cursor corresponding to the given direction and mirroring.
     * The direction must be one of:
     * <UL>
     * <LI>{@link PositionConstants#NORTH}
     * <LI>{@link PositionConstants#SOUTH}
     * <LI>{@link PositionConstants#EAST}
     * <LI>{@link PositionConstants#WEST}
     * <LI>{@link PositionConstants#NORTH_EAST}
     * <LI>{@link PositionConstants#NORTH_WEST}
     * <LI>{@link PositionConstants#SOUTH_EAST}
     * <LI>{@link PositionConstants#SOUTH_WEST}
     * </UL>
     * <P>
     * The behavior is undefined for other values. If <code>isMirrored</code> is
     * set to <code>true</code>, EAST and WEST will be inverted.
     * 
     * @param direction
     *            the relative direction of the desired cursor
     * @param isMirrored
     *            <code>true</code> if EAST and WEST should be inverted
     * @return The appropriate directional cursor
     */
    public static Cursor getDirectionalCursor(int direction, boolean isMirrored) {
        if (isMirrored && (direction & PositionConstants.EAST_WEST) != 0)
            direction = direction ^ PositionConstants.EAST_WEST;
        switch (direction) {
        case PositionConstants.NORTH:
            return SIZEN;
        case PositionConstants.SOUTH:
            return SIZES;
        case PositionConstants.EAST:
            return SIZEE;
        case PositionConstants.WEST:
            return SIZEW;
        case PositionConstants.SOUTH_EAST:
            return SIZESE;
        case PositionConstants.SOUTH_WEST:
            return SIZESW;
        case PositionConstants.NORTH_EAST:
            return SIZENE;
        case PositionConstants.NORTH_WEST:
            return SIZENW;
        default:
            break;
        }
        return null;
    }

    /**
     * @see SWT#CURSOR_ARROW
     */
    public static final Cursor ARROW;
    /**
     * @see SWT#CURSOR_SIZEN
     */
    public static final Cursor SIZEN;
    /**
     * @see SWT#CURSOR_SIZENE
     */
    public static final Cursor SIZENE;
    /**
     * @see SWT#CURSOR_SIZEE
     */
    public static final Cursor SIZEE;
    /**
     * @see SWT#CURSOR_SIZESE
     */
    public static final Cursor SIZESE;
    /**
     * @see SWT#CURSOR_SIZES
     */
    public static final Cursor SIZES;
    /**
     * @see SWT#CURSOR_SIZESW
     */
    public static final Cursor SIZESW;
    /**
     * @see SWT#CURSOR_SIZEW
     */
    public static final Cursor SIZEW;
    /**
     * @see SWT#CURSOR_SIZENW
     */
    public static final Cursor SIZENW;
    /**
     * @see SWT#CURSOR_APPSTARTING
     */
    public static final Cursor APPSTARTING;
    /**
     * @see SWT#CURSOR_CROSS
     */
    public static final Cursor CROSS;
    /**
     * @see SWT#CURSOR_HAND
     */
    public static final Cursor HAND;
    /**
     * @see SWT#CURSOR_HELP
     */
    public static final Cursor HELP;
    /**
     * @see SWT#CURSOR_IBEAM
     */
    public static final Cursor IBEAM;
    /**
     * @see SWT#CURSOR_NO
     */
    public static final Cursor NO;
    /**
     * @see SWT#CURSOR_SIZEALL
     */
    public static final Cursor SIZEALL;
    /**
     * @see SWT#CURSOR_SIZENESW
     */
    public static final Cursor SIZENESW;
    /**
     * @see SWT#CURSOR_SIZENWSE
     */
    public static final Cursor SIZENWSE;
    /**
     * @see SWT#CURSOR_SIZEWE
     */
    public static final Cursor SIZEWE;
    /**
     * @see SWT#CURSOR_SIZENS
     */
    public static final Cursor SIZENS;
    /**
     * @see SWT#CURSOR_UPARROW
     */
    public static final Cursor UPARROW;
    /**
     * @see SWT#CURSOR_WAIT
     */
    public static final Cursor WAIT;

    static {
        ARROW = new Cursor(null, SWT.CURSOR_ARROW);
        SIZEN = new Cursor(null, SWT.CURSOR_SIZEN);
        SIZENE = new Cursor(null, SWT.CURSOR_SIZENE);
        SIZEE = new Cursor(null, SWT.CURSOR_SIZEE);
        SIZESE = new Cursor(null, SWT.CURSOR_SIZESE);
        SIZES = new Cursor(null, SWT.CURSOR_SIZES);
        SIZESW = new Cursor(null, SWT.CURSOR_SIZESW);
        SIZEW = new Cursor(null, SWT.CURSOR_SIZEW);
        SIZENW = new Cursor(null, SWT.CURSOR_SIZENW);
        SIZENS = new Cursor(null, SWT.CURSOR_SIZENS);
        SIZEWE = new Cursor(null, SWT.CURSOR_SIZEWE);
        APPSTARTING = new Cursor(null, SWT.CURSOR_APPSTARTING);
        CROSS = new Cursor(null, SWT.CURSOR_CROSS);
        HAND = new Cursor(null, SWT.CURSOR_HAND);
        HELP = new Cursor(null, SWT.CURSOR_HELP);
        IBEAM = new Cursor(null, SWT.CURSOR_IBEAM);
        NO = new Cursor(null, SWT.CURSOR_NO);
        SIZEALL = new Cursor(null, SWT.CURSOR_SIZEALL);
        SIZENESW = new Cursor(null, SWT.CURSOR_SIZENESW);
        SIZENWSE = new Cursor(null, SWT.CURSOR_SIZENWSE);
        UPARROW = new Cursor(null, SWT.CURSOR_UPARROW);
        WAIT = new Cursor(null, SWT.CURSOR_WAIT);
    }

}
