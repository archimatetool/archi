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

/**
 * Constants representing cardinal directions and relative positions. Some of
 * these constants can be grouped as follows:
 * <TABLE border="1" cellpadding="5" cellspacing="0">
 * <TBODY>
 * <TR>
 * <TD>LEFT, CENTER, RIGHT</TD>
 * <TD>Used to describe horizontal position.</TD>
 * </TR>
 * <TR>
 * <TD>TOP, MIDDLE, BOTTOM</TD>
 * <TD>Used to describe vertical position.</TD>
 * </TR>
 * <TR>
 * <TD>NORTH, SOUTH, EAST, WEST</TD>
 * <TD>Used to describe the four positions relative to an object's center point.
 * May also be used when describing which direction an object is facing.<BR>
 * NOTE: If you have a use for all four of these possibilities, do not use TOP,
 * BOTTOM, RIGHT, LEFT in place of NORTH, SOUTH, EAST, WEST.</TD>
 * </TR>
 * </TBODY>
 * </TABLE>
 */
public interface PositionConstants {

    /** None */
    int NONE = 0;

    /** Left */
    int LEFT = 1;
    /** Center (Horizontal) */
    int CENTER = 2;
    /** Right */
    int RIGHT = 4;
    /** Bit-wise OR of LEFT, CENTER, and RIGHT */
    int LEFT_CENTER_RIGHT = LEFT | CENTER | RIGHT;
    /**
     * Used to signify left alignment regardless of orientation (i.e., LTR or
     * RTL)
     */
    int ALWAYS_LEFT = 64;
    /**
     * Used to signify right alignment regardless of orientation (i.e., LTR or
     * RTL)
     */
    int ALWAYS_RIGHT = 128;

    /** Top */
    int TOP = 8;
    /** Middle (Vertical) */
    int MIDDLE = 16;
    /** Bottom */
    int BOTTOM = 32;
    /** Bit-wise OR of TOP, MIDDLE, and BOTTOM */
    int TOP_MIDDLE_BOTTOM = TOP | MIDDLE | BOTTOM;

    /** North */
    int NORTH = 1;
    /** South */
    int SOUTH = 4;
    /** West */
    int WEST = 8;
    /** East */
    int EAST = 16;

    /** A constant indicating horizontal direction */
    int HORIZONTAL = 64;
    /** A constant indicating vertical direction */
    int VERTICAL = 128;

    /** North-East: a bit-wise OR of {@link #NORTH} and {@link #EAST} */
    int NORTH_EAST = NORTH | EAST;
    /** North-West: a bit-wise OR of {@link #NORTH} and {@link #WEST} */
    int NORTH_WEST = NORTH | WEST;
    /** South-East: a bit-wise OR of {@link #SOUTH} and {@link #EAST} */
    int SOUTH_EAST = SOUTH | EAST;
    /** South-West: a bit-wise OR of {@link #SOUTH} and {@link #WEST} */
    int SOUTH_WEST = SOUTH | WEST;
    /** North-South: a bit-wise OR of {@link #NORTH} and {@link #SOUTH} */
    int NORTH_SOUTH = NORTH | SOUTH;
    /** East-West: a bit-wise OR of {@link #EAST} and {@link #WEST} */
    int EAST_WEST = EAST | WEST;

    /** North-South-East-West: a bit-wise OR of all 4 directions. */
    int NSEW = NORTH_SOUTH | EAST_WEST;

}