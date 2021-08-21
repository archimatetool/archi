/*******************************************************************************
 * Copyright 2005-2007, CHISEL Group, University of Victoria, Victoria, BC,
 * Canada. All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 ******************************************************************************/
package org.eclipse.zest.core.widgets;

/**
 * Style constants used in Zest.
 * 
 * @author Chris Callendar
 */
public final class ZestStyles {

    /**
     * A constant known to be zero (0), used in operations which take bit flags
     * to indicate that "no bits are set".
     */
    public static final int NONE = 0;

    /**
     * Style constant indicating that invisible nodes should be ignored for
     * layouts.
     */
    public static final int IGNORE_INVISIBLE_LAYOUT = 1 << 1;

    /**
     * Style constant indicating if the selected node's neighbors should be
     * highlighted. Note: this is a node-level style. It should not be applied
     * to graph views during construction.
     * 
     */
    //public static final int NODES_HIGHLIGHT_ADJACENT = 1 << 1;
    /**
     * Style constant indicating that node labels should be cached. This is
     * important under GTK+ because font drawing is slower than Windows.
     */
    public static final int NODES_CACHE_LABEL = 1 << 1;

    /**
     * Style to specify that the node should contain a fisheye label
     * when the mouse moves over it.  By default the fisheye node is just the
     * label with larger text.
     */
    public static final int NODES_FISHEYE = 1 << 2;

    /**
     * Style to specify that the node should not show its text (only its image).
     * This with the NODES_FISHEYE style should help with large graphs (since the
     * fisheye style will show the text).
     */
    public static final int NODES_HIDE_TEXT = 1 << 3;

    /**
     * Style constant indiciating that nodes should not be resized on layout.
     */
    public static final int NODES_NO_LAYOUT_RESIZE = 1 << 4;

    /**
     * Style constant indiciating the graph should not be animated during layout
     * or refresh.
     */
    public static final int NODES_NO_LAYOUT_ANIMATION = 1 << 8;

    /**
     * Style constant indiciating the graph should not be animated during fisheye.
     */
    public static final int NODES_NO_FISHEYE_ANIMATION = 1 << 16;

    /**
     * Style constant indiciating the graph should not be animated.
     */
    public static final int NODES_NO_ANIMATION = NODES_NO_LAYOUT_ANIMATION | NODES_NO_FISHEYE_ANIMATION;
    /**
     * Style indicating that connections should show their direction by default.
     */
    public static final int CONNECTIONS_DIRECTED = 1 << 1;

    /**
     * Style constant to indicate that connections should be drawn with solid
     * lines (this is the default).
     */
    public static final int CONNECTIONS_SOLID = 1 << 2;
    /**
     * Style constant to indicate that connections should be drawn with dashed
     * lines.
     */
    public static final int CONNECTIONS_DASH = 1 << 3;
    /**
     * Style constant to indicate that connections should be drawn with dotted
     * lines.
     */
    public static final int CONNECTIONS_DOT = 1 << 4;
    /**
     * Style constant to indicate that connections should be drawn with
     * dash-dotted lines.
     */
    public static final int CONNECTIONS_DASH_DOT = 1 << 5;

    /**
     * Bitwise ANDs the styleToCheck integer with the given style.
     * 
     * @param style
     * @param styleToCheck
     * @return boolean if styleToCheck is part of the style
     */
    public static boolean checkStyle(int style, int styleToCheck) {
        return ((style & styleToCheck) == styleToCheck);
    }

    /**
     * Validates the given style for connections to see if it is legal. Returns
     * false if not.
     * 
     * @param style
     *            the style to check.
     * @return true iff the given style is legal.
     */
    public static boolean validateConnectionStyle(int styleToValidate) {
        int style = styleToValidate;
        // int illegal = CONNECTIONS_CURVED | CONNECTIONS_STRAIGHT |
        // CONNECTIONS_BEZIER;
        /*
        int illegal = CONNECTIONS_STRAIGHT;
        style &= illegal;
        int rightBit = style & (-style);
        boolean okay = (style == rightBit);
        if (!okay) {
            return okay;
        }
        */

        int illegal = CONNECTIONS_DASH_DOT | CONNECTIONS_DASH | CONNECTIONS_DOT | CONNECTIONS_SOLID;
        style = styleToValidate;
        style &= illegal;
        int rightBit = style & (-style);
        boolean okay = (style == rightBit);
        if (!okay) {
            return okay;
        }

        return true;

        // @tag zest.bug.160368-ConnectionAlign.fix : must check the connections
        // to make sure that there isnt' an illegal combination of alignments.
        /*
        illegal = CONNECTIONS_VALIGN_BOTTOM | CONNECTIONS_VALIGN_MIDDLE | CONNECTIONS_VALIGN_TOP;
        style = styleToValidate;
        style &= illegal;
        rightBit = style & (-style);
        return (style == rightBit);
        */
    }
}
