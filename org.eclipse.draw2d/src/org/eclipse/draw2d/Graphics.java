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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.LineAttributes;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.graphics.TextLayout;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * The Graphics class allows you to draw to a surface. The drawXxx() methods
 * that pertain to shapes draw an outline of the shape, whereas the fillXxx()
 * methods fill in the shape. Also provides for drawing text, lines and images.
 * 
 * @noextend This class is not intended to be subclassed by clients.
 */
public abstract class Graphics {

    /**
     * @see SWT#LINE_CUSTOM
     */
    public static final int LINE_CUSTOM = SWT.LINE_CUSTOM;

    /**
     * @see SWT#LINE_DASH
     */
    public static final int LINE_DASH = SWT.LINE_DASH;

    /**
     * @see SWT#LINE_DASHDOT
     */
    public static final int LINE_DASHDOT = SWT.LINE_DASHDOT;

    /**
     * @see SWT#LINE_DASHDOTDOT
     */
    public static final int LINE_DASHDOTDOT = SWT.LINE_DASHDOTDOT;

    /**
     * @see SWT#LINE_DOT
     */
    public static final int LINE_DOT = SWT.LINE_DOT;

    /**
     * @see SWT#LINE_SOLID
     */
    public static final int LINE_SOLID = SWT.LINE_SOLID;

    /**
     * Sets the clip region to the given rectangle. Anything outside this
     * rectangle will not be drawn.
     * 
     * @param r
     *            the clip rectangle
     */
    public abstract void clipRect(Rectangle r);

    /**
     * Sets the clip region to the given rectangle. Anything outside this
     * rectangle will not be drawn. Takes into account current clipping area set
     * on the graphics.
     * 
     * @param path
     *            the clip path
     * 
     * @since 3.6
     */
    public void clipPath(Path path) {
        throwNotImplemented();
    }

    /**
     * Disposes this object, releasing any resources.
     */
    public abstract void dispose();

    /**
     * Draws the outline of an arc located at (x,y) with width <i>w</i> and
     * height <i>h</i>. The starting angle of the arc (specified in degrees) is
     * <i>offset</i> and <i>length</i> is the arc's angle (specified in
     * degrees).
     * 
     * @param x
     *            the x coordinate
     * @param y
     *            the y coordinate
     * @param w
     *            the width
     * @param h
     *            the height
     * @param offset
     *            the start angle
     * @param length
     *            the length of the arc
     */
    public abstract void drawArc(int x, int y, int w, int h, int offset,
            int length);

    /**
     * @see #drawArc(int, int, int, int, int, int)
     */
    public final void drawArc(Rectangle r, int offset, int length) {
        drawArc(r.x, r.y, r.width, r.height, offset, length);
    }

    /**
     * Draws a focus rectangle.
     * 
     * @param x
     *            the x coordinate
     * @param y
     *            the y coordinate
     * @param w
     *            the width
     * @param h
     *            the height
     */
    public abstract void drawFocus(int x, int y, int w, int h);

    /**
     * @see #drawFocus(int, int, int, int)
     */
    public final void drawFocus(Rectangle r) {
        drawFocus(r.x, r.y, r.width, r.height);
    }

    /**
     * Draws the given Image at the location (x,y).
     * 
     * @param srcImage
     *            the Image
     * @param x
     *            the x coordinate
     * @param y
     *            the y coordinate
     */
    public abstract void drawImage(Image srcImage, int x, int y);

    /**
     * Draws a rectangular section of the given Image to the specified
     * rectangular reagion on the canvas. The section of the image bounded by
     * the rectangle (x1,y1,w1,h1) is copied to the section of the canvas
     * bounded by the rectangle (x2,y2,w2,h2). If these two sizes are different,
     * scaling will occur.
     * 
     * @param srcImage
     *            the image
     * @param x1
     *            the x coordinate of the source
     * @param y1
     *            the y coordinate of the source
     * @param w1
     *            the width of the source
     * @param h1
     *            the height of the source
     * @param x2
     *            the x coordinate of the destination
     * @param y2
     *            the y coordinate of the destination
     * @param w2
     *            the width of the destination
     * @param h2
     *            the height of the destination
     */
    public abstract void drawImage(Image srcImage, int x1, int y1, int w1,
            int h1, int x2, int y2, int w2, int h2);

    /**
     * Draws the given image at a point.
     * 
     * @param image
     *            the image to draw
     * @param p
     *            where to draw the image
     * @see #drawImage(Image, int, int)
     */
    public final void drawImage(Image image, Point p) {
        drawImage(image, p.x, p.y);
    }

    /**
     * @see #drawImage(Image, int, int, int, int, int, int, int, int)
     */
    public final void drawImage(Image srcImage, Rectangle src, Rectangle dest) {
        drawImage(srcImage, src.x, src.y, src.width, src.height, dest.x,
                dest.y, dest.width, dest.height);
    }

    /**
     * Draws a line between the points <code>(x1,y1)</code> and
     * <code>(x2,y2)</code> using the foreground color.
     * 
     * @param x1
     *            the x coordinate for the first point
     * @param y1
     *            the y coordinate for the first point
     * @param x2
     *            the x coordinate for the second point
     * @param y2
     *            the y coordinate for the second point
     */
    public abstract void drawLine(int x1, int y1, int x2, int y2);

    /**
     * @see #drawLine(int, int, int, int)
     */
    public final void drawLine(Point p1, Point p2) {
        drawLine(p1.x, p1.y, p2.x, p2.y);
    }

    /**
     * Draws the outline of an ellipse that fits inside the rectangle with the
     * given properties using the foreground color.
     * 
     * @param x
     *            the x coordinate
     * @param y
     *            the y coordinate
     * @param w
     *            the width
     * @param h
     *            the height
     */
    public abstract void drawOval(int x, int y, int w, int h);

    /**
     * Draws an oval inside the given rectangle using the current foreground
     * color.
     * 
     * @param r
     *            the rectangle circumscribing the oval to be drawn
     * @see #drawOval(int, int, int, int)
     */
    public final void drawOval(Rectangle r) {
        drawOval(r.x, r.y, r.width, r.height);
    }

    /**
     * Draws the given path.
     * 
     * @param path
     *            the path to draw
     * @since 3.1
     */
    public void drawPath(Path path) {
        throwNotImplemented();
    }

    /**
     * Draws a pixel, using the foreground color, at the specified point (
     * <code>x</code>, <code>y</code>).
     * <p>
     * Note that the current line attributes do not affect this operation.
     * </p>
     * 
     * @param x
     *            the point's x coordinate
     * @param y
     *            the point's y coordinate
     * 
     */
    public void drawPoint(int x, int y) {
        drawLine(x, y, x, y);
    }

    /**
     * Draws a closed polygon defined by the given Integer array containing the
     * vertices in x,y order. The first and last points in the list will be
     * connected.
     * 
     * @param points
     *            the vertices
     */
    public void drawPolygon(int[] points) {
        drawPolygon(getPointList(points));
    }

    /**
     * Draws a closed polygon defined by the given <code>PointList</code>
     * containing the vertices. The first and last points in the list will be
     * connected.
     * 
     * @param points
     *            the vertices
     */
    public abstract void drawPolygon(PointList points);

    /**
     * Draws a polyline defined by the given Integer array containing the
     * vertices in x,y order. The first and last points in the list will
     * <b>not</b> be connected.
     * 
     * @param points
     *            the vertices
     */
    public void drawPolyline(int[] points) {
        drawPolyline(getPointList(points));
    }

    /**
     * Draws a polyline defined by the given <code>PointList</code> containing
     * the vertices. The first and last points in the list will <b>not</b> be
     * connected.
     * 
     * @param points
     *            the vertices
     */
    public abstract void drawPolyline(PointList points);

    /**
     * Draws a rectangle whose top-left corner is located at the point (x,y)
     * with the given width and height.
     * 
     * @param x
     *            the x coordinate
     * @param y
     *            the y coordinate
     * @param width
     *            the width
     * @param height
     *            the height
     */
    public abstract void drawRectangle(int x, int y, int width, int height);

    /**
     * Draws the given rectangle using the current foreground color.
     * 
     * @param r
     *            the rectangle to draw
     * @see #drawRectangle(int, int, int, int)
     */
    public final void drawRectangle(Rectangle r) {
        drawRectangle(r.x, r.y, r.width, r.height);
    }

    /**
     * Draws a rectangle with rounded corners using the foreground color.
     * <i>arcWidth</i> and <i>arcHeight</i> represent the horizontal and
     * vertical diameter of the corners.
     * 
     * @param r
     *            the rectangle
     * @param arcWidth
     *            the arc width
     * @param arcHeight
     *            the arc height
     */
    public abstract void drawRoundRectangle(Rectangle r, int arcWidth,
            int arcHeight);

    /**
     * Draws the given string using the current font and foreground color. No
     * tab expansion or carriage return processing will be performed. The
     * background of the string will be transparent.
     * 
     * @param s
     *            the string
     * @param x
     *            the x coordinate
     * @param y
     *            the y coordinate
     */
    public abstract void drawString(String s, int x, int y);

    /**
     * @see #drawString(String, int, int)
     */
    public final void drawString(String s, Point p) {
        drawString(s, p.x, p.y);
    }

    /**
     * Draws the given string using the current font and foreground color. Tab
     * expansion and carriage return processing are performed. The background of
     * the text will be transparent.
     * 
     * @param s
     *            the text
     * @param x
     *            the x coordinate
     * @param y
     *            the y coordinate
     */
    public abstract void drawText(String s, int x, int y);

    /**
     * Draws a string using the specified styles. The styles are defined by
     * {@link GC#drawText(String, int, int, int)}.
     * 
     * @param s
     *            the String to draw
     * @param x
     *            the x location
     * @param y
     *            the y location
     * @param style
     *            the styles used to render the string
     * @since 3.0
     */
    public void drawText(String s, int x, int y, int style) {
        throwNotImplemented();
    }

    /**
     * @see #drawText(String, int, int)
     */
    public final void drawText(String s, Point p) {
        drawText(s, p.x, p.y);
    }

    /**
     * Draws a string using the specified styles. The styles are defined by
     * {@link GC#drawText(String, int, int, int)}.
     * 
     * @param s
     *            the String to draw
     * @param p
     *            the point at which to draw the string
     * @param style
     *            the styles used to render the string
     * @since 3.0
     */
    public final void drawText(String s, Point p, int style) {
        drawText(s, p.x, p.y, style);
    }

    /**
     * Renders the specified TextLayout to this Graphics.
     * 
     * @since 3.0
     * @param layout
     *            the TextLayout
     * @param x
     *            the x coordinate
     * @param y
     *            the y coordinate
     */
    public final void drawTextLayout(TextLayout layout, int x, int y) {
        drawTextLayout(layout, x, y, -1, -1, null, null);
    }

    /**
     * @param x
     *            the x location
     * @param y
     *            the y location
     * @param layout
     *            the TextLayout being rendered
     * @param selectionStart
     *            the start of selection
     * @param selectionEnd
     *            the end of selection
     * @param selectionForeground
     *            the foreground selection color
     * @param selectionBackground
     *            the background selection color
     * @see Graphics#drawTextLayout(TextLayout, int, int)
     */
    public void drawTextLayout(TextLayout layout, int x, int y,
            int selectionStart, int selectionEnd, Color selectionForeground,
            Color selectionBackground) {
        throwNotImplemented();
    }

    /**
     * Fills the interior of an arc located at (<i>x</i>,<i>y</i>) with width
     * <i>w</i> and height <i>h</i>. The starting angle of the arc (specified in
     * degrees) is <i>offset</i> and <i>length</i> is the arc's angle (specified
     * in degrees).
     * 
     * @param x
     *            the x coordinate
     * @param y
     *            the y coordinate
     * @param w
     *            the width
     * @param h
     *            the height
     * @param offset
     *            the start angle
     * @param length
     *            the length of the arc
     */
    public abstract void fillArc(int x, int y, int w, int h, int offset,
            int length);

    /**
     * @see #fillArc(int, int, int, int, int, int)
     */
    public final void fillArc(Rectangle r, int offset, int length) {
        fillArc(r.x, r.y, r.width, r.height, offset, length);
    }

    /**
     * Fills the the given rectangle with a gradient from the foreground color
     * to the background color. If <i>vertical</i> is <code>true</code>, the
     * gradient will go from top to bottom. Otherwise, it will go from left to
     * right. background color.
     * 
     * @param x
     *            the x coordinate
     * @param y
     *            the y coordinate
     * @param w
     *            the width
     * @param h
     *            the height
     * @param vertical
     *            whether the gradient should be vertical
     */
    public abstract void fillGradient(int x, int y, int w, int h,
            boolean vertical);

    /**
     * @see #fillGradient(int, int, int, int, boolean)
     */
    public final void fillGradient(Rectangle r, boolean vertical) {
        fillGradient(r.x, r.y, r.width, r.height, vertical);
    }

    /**
     * Fills an ellipse that fits inside the rectangle with the given properties
     * using the background color.
     * 
     * @param x
     *            the x coordinate
     * @param y
     *            the y coordinate
     * @param w
     *            the width
     * @param h
     *            the height
     */
    public abstract void fillOval(int x, int y, int w, int h);

    /**
     * @see #fillOval(int, int, int, int)
     */
    public final void fillOval(Rectangle r) {
        fillOval(r.x, r.y, r.width, r.height);
    }

    /**
     * Fills the given path.
     * 
     * @param path
     *            the path to fill
     * @since 3.1
     */
    public void fillPath(Path path) {
        throwNotImplemented();
    }

    /**
     * Fills a closed polygon defined by the given Integer array containing the
     * vertices in x,y order. The first and last points in the list will be
     * connected.
     * 
     * @param points
     *            the vertices
     */
    public void fillPolygon(int[] points) {
        fillPolygon(getPointList(points));
    }

    /**
     * Fills a closed polygon defined by the given <code>PointList</code>
     * containing the vertices. The first and last points in the list will be
     * connected.
     * 
     * @param points
     *            the vertices
     */
    public abstract void fillPolygon(PointList points);

    /**
     * Fills a rectangle whose top-left corner is located at the point (x,y)
     * with the given width and height.
     * 
     * @param x
     *            the x coordinate
     * @param y
     *            the y coordinate
     * @param width
     *            the width
     * @param height
     *            the height
     */
    public abstract void fillRectangle(int x, int y, int width, int height);

    /**
     * Fills the given rectangle using the current background color.
     * 
     * @param r
     *            the rectangle to fill
     * @see #fillRectangle(int, int, int, int)
     */
    public final void fillRectangle(Rectangle r) {
        fillRectangle(r.x, r.y, r.width, r.height);
    }

    /**
     * Fills a rectangle with rounded corners using the background color.
     * <i>arcWidth</i> and <i>arcHeight</i> represent the horizontal and
     * vertical diameter of the corners.
     * 
     * @param r
     *            the rectangle
     * @param arcWidth
     *            the arc width
     * @param arcHeight
     *            the arc height
     */
    public abstract void fillRoundRectangle(Rectangle r, int arcWidth,
            int arcHeight);

    /**
     * Draws the given string using the current font and foreground color. No
     * tab expansion or carriage return processing will be performed. The
     * background of the string will be filled with the current background
     * color.
     * 
     * @param s
     *            the string
     * @param x
     *            the x coordinate
     * @param y
     *            the y coordinate
     */
    public abstract void fillString(String s, int x, int y);

    /**
     * @see #fillString(String, int, int)
     */
    public final void fillString(String s, Point p) {
        fillString(s, p.x, p.y);
    }

    /**
     * Draws the given string using the current font and foreground color. Tab
     * expansion and carriage return processing are performed. The background of
     * the text will be filled with the current background color.
     * 
     * @param s
     *            the text
     * @param x
     *            the x coordinate
     * @param y
     *            the y coordinate
     */
    public abstract void fillText(String s, int x, int y);

    /**
     * @see #fillText(String, int, int)
     */
    public final void fillText(String s, Point p) {
        fillText(s, p.x, p.y);
    }

    /**
     * Returns the current absolute scaling which will be applied to the
     * underlying Device when painting to this Graphics. The default value is
     * 1.0.
     * 
     * @since 3.0
     * @return the effective absolute scaling factor
     */
    public double getAbsoluteScale() {
        return 1.0;
    }

    /**
     * @return true if the underlying graphics device is using advanced graphics
     *         mode.
     * @since 3.5
     */
    public boolean getAdvanced() {
        throwNotImplemented();
        return false;
    }

    /**
     * Returns the current alpha value of the graphics.
     * 
     * @return the alpha value
     * @since 3.1
     */
    public int getAlpha() {
        throwNotImplemented();
        return 255;
    }

    /**
     * Returns the anti-aliasing setting value, which will be one of
     * <code>SWT.DEFAULT</code>, <code>SWT.OFF</code> or <code>SWT.ON</code>.
     * Note that this controls anti-aliasing for all <em>non-text</em> drawing
     * operations.
     * 
     * @see #getTextAntialias()
     * @return the anti-alias setting
     * @since 3.1
     */
    public int getAntialias() {
        throwNotImplemented();
        return SWT.DEFAULT;
    }

    /**
     * Returns the background color used for filling.
     * 
     * @return the background color
     */
    public abstract Color getBackgroundColor();

    /**
     * Modifies the given rectangle to match the clip region and returns that
     * rectangle.
     * 
     * @param rect
     *            the rectangle to hold the clip region
     * @return the clip rectangle
     */
    public abstract Rectangle getClip(Rectangle rect);

    /**
     * Returns the fill rule, which will be one of
     * <code>SWT.FILL_EVEN_ODD</code> or <code>SWT.FILL_WINDING</code>.
     * 
     * @return the fill rule
     * @since 3.1
     */
    public int getFillRule() {
        throwNotImplemented();
        return 0;
    }

    /**
     * Returns the font used to draw and fill text.
     * 
     * @return the font
     */
    public abstract Font getFont();

    /**
     * Returns the font metrics for the current font.
     * 
     * @return the font metrics
     */
    public abstract FontMetrics getFontMetrics();

    /**
     * Returns the foreground color used to draw lines and text.
     * 
     * @return the foreground color
     */
    public abstract Color getForegroundColor();

    /**
     * Returns the interpolation setting.
     * 
     * @see org.eclipse.swt.graphics.GC#getInterpolation()
     * @return the interpolation setting
     * @since 3.1
     */
    public int getInterpolation() {
        throwNotImplemented();
        return 0;
    }

    /**
     * Returns the current collection of line attributes.
     * 
     * @see org.eclipse.swt.graphics.LineAttributes
     * @return all attributes used for line drawing
     * @since 3.5
     */
    public LineAttributes getLineAttributes() {
        throwNotImplemented();
        return null;
    }

    /**
     * Returns the current line cap style.
     * 
     * @see org.eclipse.swt.graphics.LineAttributes#cap
     * @return the cap style used for drawing lines
     * @since 3.1
     */
    public int getLineCap() {
        throwNotImplemented();
        return SWT.CAP_FLAT;
    }

    /**
     * Returns the line join style.
     * 
     * @see org.eclipse.swt.graphics.LineAttributes#join
     * @since 3.1
     * @return the join style used for drawing lines
     */
    public int getLineJoin() {
        throwNotImplemented();
        return SWT.JOIN_MITER;
    }

    /**
     * Returns the line miter limit.
     * 
     * @see org.eclipse.swt.graphics.LineAttributes#miterLimit
     * @return miter limit
     * @since 3.5
     */
    public float getLineMiterLimit() {
        throwNotImplemented();
        return 0;
    }

    /**
     * Returns the line style.
     * 
     * @see org.eclipse.swt.graphics.LineAttributes#style
     * @return the line style
     */
    public abstract int getLineStyle();

    /**
     * Returns the current line width.
     * 
     * @return the line width
     */
    public abstract int getLineWidth();

    /**
     * Returns the current line width.
     * 
     * @see org.eclipse.swt.graphics.LineAttributes#width
     * @return the line width
     * @since 3.5
     */
    public abstract float getLineWidthFloat();

    /**
     * Returns a pointlist containing all the points from the integer array.
     * 
     * @param points
     *            an integer array of x,y points
     * @return the corresponding pointlist
     */
    private PointList getPointList(int[] points) {
        PointList pointList = new PointList(points.length / 2);
        for (int i = 0; (i + 1) < points.length; i += 2)
            pointList.addPoint(points[i], points[i + 1]);
        return pointList;
    }

    /**
     * Returns the textual anti-aliasing setting value, which will be one of
     * <code>SWT.DEFAULT</code>, <code>SWT.OFF</code> or <code>SWT.ON</code>.
     * Note that this controls anti-aliasing <em>only</em> for text drawing
     * operations.
     * 
     * @see #getAntialias()
     * @return the anti-aliasing setting
     * @since 3.1
     */
    public int getTextAntialias() {
        throwNotImplemented();
        return SWT.DEFAULT;
    }

    /**
     * Returns <code>true</code> if this graphics object should use XOR mode
     * with painting.
     * 
     * @return whether XOR mode is turned on
     */
    public abstract boolean getXORMode();

    /**
     * Pops the previous state of this graphics object off the stack (if
     * {@link #pushState()} has previously been called) and restores the current
     * state to that popped state.
     */
    public abstract void popState();

    /**
     * Pushes the current state of this graphics object onto a stack.
     */
    public abstract void pushState();

    /**
     * Restores the previous state of this graphics object.
     */
    public abstract void restoreState();

    /**
     * Rotates the coordinates by the given counter-clockwise angle. All
     * subsequent painting will be performed in the resulting coordinates. Some
     * functions are illegal when a rotated coordinates system is in use. To
     * restore access to those functions, it is necessary to call restore or pop
     * to return to a non rotated state.
     * 
     * @param degrees
     *            the degrees to rotate
     * @since 3.1
     */
    public void rotate(float degrees) {
        throwNotImplemented();
    }

    /**
     * Scales this graphics object by the given amount.
     * 
     * @param amount
     *            the scale factor
     */
    public abstract void scale(double amount);

    /**
     * Scales the graphics by the given horizontal and vertical components.
     * 
     * @param horizontal
     *            the horizontal scaling factor
     * @param vertical
     *            the vertical scaling factor
     * @since 3.1
     */
    public void scale(float horizontal, float vertical) {
        throwNotImplemented();
    }

    /**
     * Sets the alpha to the given value. Values may range from 0 to 255. A
     * value of 0 is completely transparent.
     * 
     * @param alpha
     *            an alpha value (0-255)
     * @since 3.1
     */
    public void setAlpha(int alpha) {
        throwNotImplemented();
    }

    /**
     * Turns advanced graphics mode on or off.
     * 
     * @param advanced
     * @since 3.5
     */
    public void setAdvanced(boolean advanced) {
        throwNotImplemented();
    }

    /**
     * Sets the anti-aliasing value to the parameter, which must be one of
     * <code>SWT.DEFAULT</code>, <code>SWT.OFF</code> or <code>SWT.ON</code>.
     * Note that this controls anti-aliasing for all <em>non-text drawing</em>
     * operations.
     * 
     * @param value
     *            the anti-alias value
     */
    public void setAntialias(int value) {
        throwNotImplemented();
    }

    /**
     * Sets the background color.
     * 
     * @param rgb
     *            the new background color
     */
    public abstract void setBackgroundColor(Color rgb);

    /**
     * Sets the pattern used for fill-type graphics operations. The pattern must
     * not be disposed while it is being used by the graphics.
     * 
     * @param pattern
     *            the background pattern
     * @since 3.1
     */
    public void setBackgroundPattern(Pattern pattern) {
        throwNotImplemented();
    }

    /**
     * Sets the area which can be affected by drawing operations to the
     * specified <code>Path</code>.
     * 
     * @param path
     *            the clipping path
     * @since 3.1
     */
    public void setClip(Path path) {
        throwNotImplemented();
    }

    /**
     * Sets the clip rectangle. Painting will <b>not</b> occur outside this
     * area.
     * 
     * @param r
     *            the new clip rectangle
     */
    public abstract void setClip(Rectangle r);

    /**
     * Sets the fill rule to the given value, which must be one of
     * <code>SWT.FILL_EVEN_ODD</code> or <code>SWT.FILL_WINDING</code>.
     * 
     * @param rule
     *            the fill rule
     * @since 3.1
     */
    public void setFillRule(int rule) {
        throwNotImplemented();
    }

    /**
     * Sets the font.
     * 
     * @param f
     *            the new font
     */
    public abstract void setFont(Font f);

    /**
     * Sets the foreground color.
     * 
     * @param rgb
     *            the new foreground color
     */
    public abstract void setForegroundColor(Color rgb);

    /**
     * Sets the foreground pattern for draw and text operations. The pattern
     * must not be disposed while it is being referenced by the graphics.
     * 
     * @param pattern
     *            the foreground pattern
     * @since 3.1
     */
    public void setForegroundPattern(Pattern pattern) {
        throwNotImplemented();
    }

    /**
     * Sets the interpolation setting to the given value, which must be one of
     * <code>SWT.DEFAULT</code>, <code>SWT.NONE</code>, <code>SWT.LOW</code> or
     * <code>SWT.HIGH</code>. This setting is relevant when working with Images.
     * 
     * @param interpolation
     *            the interpolation
     * @since 3.1
     */
    public void setInterpolation(int interpolation) {
        throwNotImplemented();
    }

    /**
     * Sets all line attributes together
     * 
     * @param attributes
     *            the line attributes
     * @since 3.5
     */
    public void setLineAttributes(LineAttributes attributes) {
        throwNotImplemented();
    }

    /**
     * Sets the line cap style to the argument, which must be one of the
     * constants <code>SWT.CAP_FLAT</code>, <code>SWT.CAP_ROUND</code>, or
     * <code>SWT.CAP_SQUARE</code>.
     * 
     * @param cap
     *            the line cap
     * @since 3.1
     */
    public void setLineCap(int cap) {
        throwNotImplemented();
    }

    /**
     * Sets the dash pattern when the custom line style is in use. Because this
     * feature is rarely used, the dash pattern may not be preserved when
     * calling {@link #pushState()} and {@link #popState()}.
     * 
     * @param dash
     *            the pixel pattern
     * @since 3.1
     */
    public void setLineDash(int dash[]) {
        throwNotImplemented();
    }

    /**
     * Sets the dash pattern when the custom line style is in use.
     * 
     * @param value
     *            the pixel pattern.
     * @since 3.5
     */
    public void setLineDash(float[] value) {
        throwNotImplemented();
    }

    /**
     * Sets the line dash offset.
     * 
     * @param value
     * @since 3.8
     */
    public void setLineDashOffset(float value) {
        throwNotImplemented();
    }

    /**
     * Sets the line join style to the argument, which must be one of the
     * constants <code>SWT.JOIN_MITER</code>, <code>SWT.JOIN_ROUND</code>, or
     * <code>SWT.JOIN_BEVEL</code>.
     * 
     * @param join
     *            the join type
     * @since 3.1
     */
    public void setLineJoin(int join) {
        throwNotImplemented();
    }

    /**
     * Sets the line style to the argument, which must be one of the constants
     * <code>SWT.LINE_SOLID</code>, <code>SWT.LINE_DASH</code>,
     * <code>SWT.LINE_DOT</code>, <code>SWT.LINE_DASHDOT</code> or
     * <code>SWT.LINE_DASHDOTDOT</code>.
     * 
     * @param style
     *            the new style
     */
    public abstract void setLineStyle(int style);

    /**
     * Sets the line width.
     * 
     * @param width
     *            the new width
     */
    public abstract void setLineWidth(int width);

    /**
     * Sets the line width.
     * 
     * @param width
     *            the new width
     * @since 3.5
     */
    public abstract void setLineWidthFloat(float width);

    /**
     * @param miterLimit
     * @since 3.5
     */
    public abstract void setLineMiterLimit(float miterLimit);

    /**
     * Sets the textual anti-aliasing value to the parameter, which must be one
     * of <code>SWT.DEFAULT</code>, <code>SWT.OFF</code> or <code>SWT.ON</code>.
     * Note that this controls anti-aliasing only for all <em>text drawing</em>
     * operations.
     * 
     * @param value
     *            the textual anti-alias setting
     * @since 3.1
     */
    public void setTextAntialias(int value) {
        throwNotImplemented();
    }

    /**
     * Modifies the current transformation by shearing the graphics in the
     * specified horizontal and vertical amounts. Shearing can be used to
     * produce effects like Italic fonts.
     * 
     * @param horz
     *            the horizontal shearing amount
     * @param vert
     *            the vertical shearing amount
     * @since 3.1
     */
    public void shear(float horz, float vert) {
        throwNotImplemented();
    }

    /**
     * Sets the XOR mode.
     * 
     * @param b
     *            the new XOR mode
     */
    public abstract void setXORMode(boolean b);

    /**
     * Throws a runtime exception to indicate a subclass has chosen not to
     * implement the method.
     * 
     * @since 3.2
     */
    private void throwNotImplemented() {
        throw new RuntimeException("The class: " + getClass() //$NON-NLS-1$
                + " has not implemented this new graphics function"); //$NON-NLS-1$
    }

    /**
     * Translates the receiver's coordinates by the specified x and y amounts.
     * All subsequent painting will be performed in the resulting coordinate
     * system. Integer translation used by itself does not require or start the
     * use of the advanced graphics system in SWT. It is emulated until advanced
     * graphics are triggered.
     * 
     * @param dx
     *            the horizontal offset
     * @param dy
     *            the vertical offset
     */
    public abstract void translate(int dx, int dy);

    /**
     * Modifies the current transform by translating the given x and y amounts.
     * All subsequent painting will be performed in the resulting coordinate
     * system.
     * 
     * @param dx
     *            the horizontal offset
     * @param dy
     *            the vertical offset
     */
    public void translate(float dx, float dy) {
        throwNotImplemented();
    }

    /**
     * @see #translate(int, int)
     */
    public final void translate(Point pt) {
        translate(pt.x, pt.y);
    }
}
