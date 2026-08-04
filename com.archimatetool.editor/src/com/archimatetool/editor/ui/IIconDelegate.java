/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;



/**
 * Icon is drawn in a Draw2d Graphics instance at an origin point
 *
 * @author Phillip Beauvoir
 */
public interface IIconDelegate {

    /**
     * Draw a figure icon
     * @param graphics The graphics to draw on
     * @param foregroundColor
     * @param backgroundColor
     * @param pt The start point to draw the icon
     */
    void drawIcon(Graphics graphics, Color foregroundColor, Color backgroundColor, Point origin);

    /**
     * @return The bounding box of the glyph this delegate draws, as if drawIcon() were called with
     * origin = new Point(0, 0) - i.e. the same geometry drawIcon() draws, relative to that origin.
     * x/y may be negative (or non-zero) when the glyph extends left of or above its own origin point.
     * <p>
     * Used by the Outline shape style's corner icon badge (see FigureUtils.drawOutlineStyleIcon) to size
     * and position itself without hand-deriving the glyph's true extent, which is error-prone for icons
     * built from partial arcs or Bezier curves - implementations should mirror their drawIcon() geometry
     * into an org.eclipse.swt.graphics.Path and return its own getBounds(), letting SWT compute the exact
     * extent of any arcs/curves rather than solving for it by hand.
     */
    Rectangle getBounds();
}
