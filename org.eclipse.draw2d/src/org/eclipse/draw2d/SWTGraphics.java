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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.LineAttributes;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.PathData;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Display;

import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A concrete implementation of <code>Graphics</code> using an SWT
 * <code>GC</code>. There are 2 states contained in this graphics class -- the
 * applied state which is the actual state of the GC and the current state which
 * is the current state of this graphics object. Certain properties can be
 * changed multiple times and the GC won't be updated until it's actually used.
 * <P>
 * WARNING: This class is not intended to be subclassed.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class SWTGraphics extends Graphics {
    
    private double scale = 1.0;

    /**
     * An internal type used to represent and update the GC's clipping.
     * 
     * @since 3.1
     */
    interface Clipping {
        /**
         * Sets the clip's bounding rectangle into the provided argument and
         * returns it for convenince.
         * 
         * @param rect
         *            the rect
         * @return the given rect
         * @since 3.1
         */
        Rectangle getBoundingBox(Rectangle rect);

        Clipping getCopy();

        void intersect(int left, int top, int right, int bottom);

        void scale(float horizontal, float vertical);

        void setOn(GC gc, int translateX, int translateY);

        void translate(float dx, float dy);
    }

    /**
     * Any state stored in this class is only applied when it is needed by a
     * specific graphics call.
     * 
     * @since 3.1
     */
    static class LazyState {
        Color bgColor;
        Color fgColor;
        Font font;
        int graphicHints;
        LineAttributes lineAttributes;
        Clipping relativeClip;
    }

    static class RectangleClipping implements Clipping {

        private float top, left, bottom, right;

        RectangleClipping(float left, float top, float right, float bottom) {
            this.left = left;
            this.right = right;
            this.bottom = bottom;
            this.top = top;
        }

        RectangleClipping(org.eclipse.swt.graphics.Rectangle rect) {
            left = rect.x;
            top = rect.y;
            right = rect.x + rect.width;
            bottom = rect.y + rect.height;
        }

        RectangleClipping(Rectangle rect) {
            left = rect.x;
            top = rect.y;
            right = rect.right();
            bottom = rect.bottom();
        }

        @Override
        public Rectangle getBoundingBox(Rectangle rect) {
            rect.x = (int) left;
            rect.y = (int) top;
            rect.width = (int) Math.ceil(right) - rect.x;
            rect.height = (int) Math.ceil(bottom) - rect.y;
            return rect;
        }

        @Override
        public Clipping getCopy() {
            return new RectangleClipping(left, top, right, bottom);
        }

        @Override
        public void intersect(int left, int top, final int right,
                final int bottom) {
            this.left = Math.max(this.left, left);
            this.right = Math.min(this.right, right);
            this.top = Math.max(this.top, top);
            this.bottom = Math.min(this.bottom, bottom);
            // use left/top -1 to ensure ceiling function doesn't add a pixel
            if (this.right < this.left || this.bottom < this.top) {
                this.right = this.left - 1;
                this.bottom = this.top - 1;
            }
        }

        @Override
        public void scale(float horz, float vert) {
            left /= horz;
            right /= horz;
            top /= vert;
            bottom /= vert;
        }

        @Override
        public void setOn(GC gc, int translateX, int translateY) {
            int xInt = (int) Math.floor(left);
            int yInt = (int) Math.floor(top);
            gc.setClipping(xInt + translateX, yInt + translateY,
                    (int) Math.ceil(right) - xInt, (int) Math.ceil(bottom)
                            - yInt);
        }

        @Override
        public void translate(float dx, float dy) {
            left += dx;
            right += dx;
            top += dy;
            bottom += dy;
        }
    }

    /**
     * Contains the entire state of the Graphics.
     */
    static class State extends LazyState implements Cloneable {
        float affineMatrix[];
        int alpha;
        Pattern bgPattern;
        int dx, dy;

        Pattern fgPattern;

        @Override
        public Object clone() throws CloneNotSupportedException {
            State clone = (State) super.clone();
            clone.lineAttributes = SWTGraphics.clone(clone.lineAttributes);
            return clone;
        }

        /**
         * Copies all state information from the given State to this State
         * 
         * @param state
         *            The State to copy from
         */
        public void copyFrom(State state) {
            bgColor = state.bgColor;
            fgColor = state.fgColor;
            lineAttributes = SWTGraphics.clone(state.lineAttributes);
            dx = state.dx;
            dy = state.dy;
            bgPattern = state.bgPattern;
            fgPattern = state.fgPattern;
            font = state.font;
            graphicHints = state.graphicHints;
            affineMatrix = state.affineMatrix;
            relativeClip = state.relativeClip;
            alpha = state.alpha;
        }
    }

    static final int AA_MASK;
    static final int AA_SHIFT;
    static final int AA_WHOLE_NUMBER = 1;
    static final int ADVANCED_GRAPHICS_MASK;
    static final int ADVANCED_HINTS_DEFAULTS;
    static final int ADVANCED_HINTS_MASK;
    static final int ADVANCED_SHIFT;
    static final int FILL_RULE_MASK;
    static final int FILL_RULE_SHIFT;
    static final int FILL_RULE_WHOLE_NUMBER = -1;
    static final int INTERPOLATION_MASK;
    static final int INTERPOLATION_SHIFT;
    static final int INTERPOLATION_WHOLE_NUMBER = 1;

    static final int TEXT_AA_MASK;
    static final int TEXT_AA_SHIFT;
    static final int XOR_MASK;
    static final int XOR_SHIFT;

    static {
        XOR_SHIFT = 3;
        AA_SHIFT = 8;
        TEXT_AA_SHIFT = 10;
        INTERPOLATION_SHIFT = 12;
        FILL_RULE_SHIFT = 14;
        ADVANCED_SHIFT = 15;

        AA_MASK = 3 << AA_SHIFT;
        FILL_RULE_MASK = 1 << FILL_RULE_SHIFT; // If changed to more than 1-bit,
                                                // check references!
        INTERPOLATION_MASK = 3 << INTERPOLATION_SHIFT;
        TEXT_AA_MASK = 3 << TEXT_AA_SHIFT;
        XOR_MASK = 1 << XOR_SHIFT;
        ADVANCED_GRAPHICS_MASK = 1 << ADVANCED_SHIFT;

        ADVANCED_HINTS_MASK = TEXT_AA_MASK | AA_MASK | INTERPOLATION_MASK;
        ADVANCED_HINTS_DEFAULTS = ((SWT.DEFAULT + AA_WHOLE_NUMBER) << TEXT_AA_SHIFT)
                | ((SWT.DEFAULT + AA_WHOLE_NUMBER) << AA_SHIFT)
                | ((SWT.DEFAULT + INTERPOLATION_WHOLE_NUMBER) << INTERPOLATION_SHIFT);
    }

    private final LazyState appliedState = new LazyState();
    private final State currentState = new State();

    private boolean elementsNeedUpdate;
    private GC gc;

    private boolean sharedClipping;
    private List stack = new ArrayList();

    private int stackPointer = 0;
    Transform transform;
    private int translateX = 0;
    private int translateY = 0;

    /**
     * Constructs a new SWTGraphics that draws to the Canvas using the given GC.
     * 
     * @param gc
     *            the GC
     */
    public SWTGraphics(GC gc) {
        this.gc = gc;
        init();
    }

    /**
     * If the background color has changed, this change will be pushed to the
     * GC. Also calls {@link #checkGC()}.
     */
    protected final void checkFill() {
        if (!currentState.bgColor.equals(appliedState.bgColor)
                && currentState.bgPattern == null) {
            gc.setBackground(appliedState.bgColor = currentState.bgColor);
        }
        checkGC();
    }

    /**
     * If the rendering hints or the clip region has changed, these changes will
     * be pushed to the GC. Rendering hints include anti-alias, xor, join, cap,
     * line style, fill rule, interpolation, and other settings.
     */
    protected final void checkGC() {
        if (appliedState.relativeClip != currentState.relativeClip) {
            appliedState.relativeClip = currentState.relativeClip;
            currentState.relativeClip.setOn(gc, translateX, translateY);
        }

        if (appliedState.graphicHints != currentState.graphicHints) {
            reconcileHints(gc, appliedState.graphicHints,
                    currentState.graphicHints);
            appliedState.graphicHints = currentState.graphicHints;
        }
    }

    /**
     * If the line width, line style, foreground or background colors have
     * changed, these changes will be pushed to the GC. Also calls
     * {@link #checkGC()}.
     */
    protected final void checkPaint() {
        checkGC();
        if (!currentState.fgColor.equals(appliedState.fgColor)
                && currentState.fgPattern == null) {
            gc.setForeground(appliedState.fgColor = currentState.fgColor);
        }

        LineAttributes lineAttributes = currentState.lineAttributes;
        if (!appliedState.lineAttributes.equals(lineAttributes)) {
            if (getAdvanced()) {
                gc.setLineAttributes(lineAttributes);
            } else {
                gc.setLineWidth((int) lineAttributes.width);
                gc.setLineCap(lineAttributes.cap);
                gc.setLineJoin(lineAttributes.join);
                gc.setLineStyle(lineAttributes.style);
                if (lineAttributes.dash != null) {
                    gc.setLineDash(convertFloatArrayToInt(lineAttributes.dash));
                }
            }
            appliedState.lineAttributes = clone(lineAttributes);
        }

        if (!currentState.bgColor.equals(appliedState.bgColor)
                && currentState.bgPattern == null) {
            gc.setBackground(appliedState.bgColor = currentState.bgColor);
        }
    }

    /**
     * @since 3.1
     */
    private void checkSharedClipping() {
        if (sharedClipping) {
            sharedClipping = false;

            boolean previouslyApplied = (appliedState == currentState.relativeClip);
            // Phillipus Fix: currentState.relativeClip can be null and lead to NPE
            // See https://github.com/archimatetool/archi/issues/431
            // currentState.relativeClip = currentState.relativeClip.getCopy();
            currentState.relativeClip = currentState.relativeClip != null ? currentState.relativeClip.getCopy() : null;
            if (previouslyApplied) {
                appliedState.relativeClip = currentState.relativeClip;
            }
        }
    }

    /**
     * If the font has changed, this change will be pushed to the GC. Also calls
     * {@link #checkPaint()} and {@link #checkFill()}.
     */
    protected final void checkText() {
        checkPaint();
        if (!appliedState.font.equals(currentState.font)) {
            gc.setFont(appliedState.font = currentState.font);
        }
    }

    /**
     * @see Graphics#clipRect(Rectangle)
     */
    @Override
    public void clipRect(Rectangle rect) {
        if (currentState.relativeClip == null) {
            throw new IllegalStateException(
                    "The current clipping area does not " + //$NON-NLS-1$
                            "support intersection."); //$NON-NLS-1$
        }

        checkSharedClipping();
        currentState.relativeClip.intersect(rect.x, rect.y, rect.right(),
                rect.bottom());
        appliedState.relativeClip = null;
    }

    /**
     * @see Graphics#dispose()
     */
    @Override
    public void dispose() {
        while (stackPointer > 0) {
            popState();
        }

        if (transform != null) {
            transform.dispose();
        }
    }

    /**
     * @see Graphics#drawArc(int, int, int, int, int, int)
     */
    @Override
    public void drawArc(int x, int y, int width, int height, int offset,
            int length) {
        checkPaint();
        gc.drawArc(x + translateX, y + translateY, width, height, offset,
                length);
    }

    /**
     * @see Graphics#drawFocus(int, int, int, int)
     */
    @Override
    public void drawFocus(int x, int y, int w, int h) {
        checkPaint();
        gc.drawFocus(x + translateX, y + translateY, w + 1, h + 1);
    }

    /**
     * @see Graphics#drawImage(Image, int, int)
     */
    @Override
    public void drawImage(Image srcImage, int x, int y) {
        checkGC();
        gc.drawImage(srcImage, x + translateX, y + translateY);
    }

    /**
     * @see Graphics#drawImage(Image, int, int, int, int, int, int, int, int)
     */
    @Override
    public void drawImage(Image srcImage, int x1, int y1, int w1, int h1,
            int x2, int y2, int w2, int h2) {
        checkGC();
        gc.drawImage(srcImage, x1, y1, w1, h1, x2 + translateX,
                y2 + translateY, w2, h2);
    }

    /**
     * @see Graphics#drawLine(int, int, int, int)
     */
    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        checkPaint();
        gc.drawLine(x1 + translateX, y1 + translateY, x2 + translateX, y2
                + translateY);
    }

    /**
     * @see Graphics#drawOval(int, int, int, int)
     */
    @Override
    public void drawOval(int x, int y, int width, int height) {
        checkPaint();
        gc.drawOval(x + translateX, y + translateY, width, height);
    }

    /**
     * This method requires advanced graphics support. A check should be made to
     * ensure advanced graphics is supported in the user's environment before
     * calling this method. See {@link GC#getAdvanced()}.
     * 
     * @see Graphics#drawPath(Path)
     */
    @Override
    public void drawPath(Path path) {
        checkPaint();
        initTransform(false);
        gc.drawPath(path);
    }

    /**
     * @see Graphics#drawPoint(int, int)
     */
    @Override
    public void drawPoint(int x, int y) {
        checkPaint();
        gc.drawPoint(x + translateX, y + translateY);
    }

    /**
     * @see Graphics#drawPolygon(int[])
     */
    @Override
    public void drawPolygon(int[] points) {
        checkPaint();
        try {
            translatePointArray(points, translateX, translateY);
            gc.drawPolygon(points);
        } finally {
            translatePointArray(points, -translateX, -translateY);
        }
    }

    /**
     * @see Graphics#drawPolygon(PointList)
     */
    @Override
    public void drawPolygon(PointList points) {
        drawPolygon(points.toIntArray());
    }

    /**
     * @see Graphics#drawPolyline(int[])
     */
    @Override
    public void drawPolyline(int[] points) {
        checkPaint();
        try {
            translatePointArray(points, translateX, translateY);
            gc.drawPolyline(points);
        } finally {
            translatePointArray(points, -translateX, -translateY);
        }
    }

    /**
     * @see Graphics#drawPolyline(PointList)
     */
    @Override
    public void drawPolyline(PointList points) {
        drawPolyline(points.toIntArray());
    }

    /**
     * @see Graphics#drawRectangle(int, int, int, int)
     */
    @Override
    public void drawRectangle(int x, int y, int width, int height) {
        checkPaint();
        gc.drawRectangle(x + translateX, y + translateY, width, height);
    }

    /**
     * @see Graphics#drawRoundRectangle(Rectangle, int, int)
     */
    @Override
    public void drawRoundRectangle(Rectangle r, int arcWidth, int arcHeight) {
        checkPaint();
        gc.drawRoundRectangle(r.x + translateX, r.y + translateY, r.width,
                r.height, arcWidth, arcHeight);
    }

    /**
     * @see Graphics#drawString(String, int, int)
     */
    @Override
    public void drawString(String s, int x, int y) {
        checkText();
        gc.drawString(s, x + translateX, y + translateY, true);
    }

    /**
     * @see Graphics#drawText(String, int, int)
     */
    @Override
    public void drawText(String s, int x, int y) {
        checkText();
        gc.drawText(s, x + translateX, y + translateY, true);
    }

    /**
     * @see Graphics#drawTextLayout(TextLayout, int, int, int, int, Color,
     *      Color)
     */
    @Override
    public void drawTextLayout(TextLayout layout, int x, int y,
            int selectionStart, int selectionEnd, Color selectionForeground,
            Color selectionBackground) {
        // $TODO probably just call checkPaint since Font and BG color don't
        // apply
        checkText();
        layout.draw(gc, x + translateX, y + translateY, selectionStart,
                selectionEnd, selectionForeground, selectionBackground);
    }

    /**
     * @see Graphics#fillArc(int, int, int, int, int, int)
     */
    @Override
    public void fillArc(int x, int y, int width, int height, int offset,
            int length) {
        checkFill();
        gc.fillArc(x + translateX, y + translateY, width, height, offset,
                length);
    }

    /**
     * @see Graphics#fillGradient(int, int, int, int, boolean)
     */
    @Override
    public void fillGradient(int x, int y, int w, int h, boolean vertical) {
        checkPaint();
        gc.fillGradientRectangle(x + translateX, y + translateY, w, h, vertical);
    }

    /**
     * @see Graphics#fillOval(int, int, int, int)
     */
    @Override
    public void fillOval(int x, int y, int width, int height) {
        checkFill();
        gc.fillOval(x + translateX, y + translateY, width, height);
    }

    /**
     * This method requires advanced graphics support. A check should be made to
     * ensure advanced graphics is supported in the user's environment before
     * calling this method. See {@link GC#getAdvanced()}.
     * 
     * @see Graphics#fillPath(Path)
     */
    @Override
    public void fillPath(Path path) {
        checkFill();
        initTransform(false);
        gc.fillPath(path);
    }

    /**
     * @see Graphics#fillPolygon(int[])
     */
    @Override
    public void fillPolygon(int[] points) {
        checkFill();
        try {
            translatePointArray(points, translateX, translateY);
            gc.fillPolygon(points);
        } finally {
            translatePointArray(points, -translateX, -translateY);
        }
    }

    /**
     * @see Graphics#fillPolygon(PointList)
     */
    @Override
    public void fillPolygon(PointList points) {
        fillPolygon(points.toIntArray());
    }

    /**
     * @see Graphics#fillRectangle(int, int, int, int)
     */
    @Override
    public void fillRectangle(int x, int y, int width, int height) {
        checkFill();
        gc.fillRectangle(x + translateX, y + translateY, width, height);
    }

    /**
     * @see Graphics#fillRoundRectangle(Rectangle, int, int)
     */
    @Override
    public void fillRoundRectangle(Rectangle r, int arcWidth, int arcHeight) {
        checkFill();
        gc.fillRoundRectangle(r.x + translateX, r.y + translateY, r.width,
                r.height, arcWidth, arcHeight);
    }

    /**
     * @see Graphics#fillString(String, int, int)
     */
    @Override
    public void fillString(String s, int x, int y) {
        checkText();
        gc.drawString(s, x + translateX, y + translateY, false);
    }

    /**
     * @see Graphics#fillText(String, int, int)
     */
    @Override
    public void fillText(String s, int x, int y) {
        checkText();
        gc.drawText(s, x + translateX, y + translateY, false);
    }

    /**
     * @see Graphics#getAlpha()
     */
    @Override
    public int getAlpha() {
        return currentState.alpha;
    }

    /**
     * @see Graphics#getAntialias()
     */
    @Override
    public int getAntialias() {
        return ((currentState.graphicHints & AA_MASK) >> AA_SHIFT)
                - AA_WHOLE_NUMBER;
    }

    @Override
    public boolean getAdvanced() {
        return (currentState.graphicHints & ADVANCED_GRAPHICS_MASK) != 0;
    }

    /**
     * @see Graphics#getBackgroundColor()
     */
    @Override
    public Color getBackgroundColor() {
        return currentState.bgColor;
    }

    /**
     * @see Graphics#getClip(Rectangle)
     */
    @Override
    public Rectangle getClip(Rectangle rect) {
        if (currentState.relativeClip != null) {
            currentState.relativeClip.getBoundingBox(rect);
            return rect;
        } else {
            throw new IllegalStateException(
                    "Clipping can no longer be queried due to transformations"); //$NON-NLS-1$
        }
    }

    /**
     * @see Graphics#getFillRule()
     */
    @Override
    public int getFillRule() {
        return ((currentState.graphicHints & FILL_RULE_MASK) >> FILL_RULE_SHIFT)
                - FILL_RULE_WHOLE_NUMBER;
    }

    /**
     * @see Graphics#getFont()
     */
    @Override
    public Font getFont() {
        return currentState.font;
    }

    /**
     * @see Graphics#getFontMetrics()
     */
    @Override
    public FontMetrics getFontMetrics() {
        checkText();
        return gc.getFontMetrics();
    }

    /**
     * @see Graphics#getForegroundColor()
     */
    @Override
    public Color getForegroundColor() {
        return currentState.fgColor;
    }

    /**
     * @see Graphics#getInterpolation()
     */
    @Override
    public int getInterpolation() {
        return ((currentState.graphicHints & INTERPOLATION_MASK) >> INTERPOLATION_SHIFT)
                - INTERPOLATION_WHOLE_NUMBER;
    }

    /**
     * @since 3.5
     */
    public void getLineAttributes(LineAttributes lineAttributes) {
        copyLineAttributes(lineAttributes, currentState.lineAttributes);
    }

    /**
     * @see Graphics#getLineCap()
     */
    @Override
    public int getLineCap() {
        return currentState.lineAttributes.cap;
    }

    /**
     * @see Graphics#getLineJoin()
     */
    @Override
    public int getLineJoin() {
        return currentState.lineAttributes.join;
    }

    /**
     * @see Graphics#getLineStyle()
     */
    @Override
    public int getLineStyle() {
        return currentState.lineAttributes.style;
    }

    /**
     * @see Graphics#getLineWidth()
     */
    @Override
    public int getLineWidth() {
        return (int) currentState.lineAttributes.width;
    }

    @Override
    public float getLineWidthFloat() {
        return currentState.lineAttributes.width;
    }

    @Override
    public float getLineMiterLimit() {
        return currentState.lineAttributes.miterLimit;
    }

    /**
     * @since 3.5
     */
    public float[] getLineDash() {
        return currentState.lineAttributes.dash.clone();
    }

    /**
     * @since 3.5
     */
    public float getLineDashOffset() {
        return currentState.lineAttributes.dashOffset;
    }

    public double getScale() {
        return scale;
    }

    /**
     * @see Graphics#getTextAntialias()
     */
    @Override
    public int getTextAntialias() {
        return ((currentState.graphicHints & TEXT_AA_MASK) >> TEXT_AA_SHIFT)
                - AA_WHOLE_NUMBER;
    }

    /**
     * @see Graphics#getXORMode()
     */
    @Override
    public boolean getXORMode() {
        return (currentState.graphicHints & XOR_MASK) != 0;
    }

    /**
     * Called by constructor, initializes all State information for currentState
     */
    protected void init() {
        currentState.bgColor = appliedState.bgColor = gc.getBackground();
        currentState.fgColor = appliedState.fgColor = gc.getForeground();
        currentState.font = appliedState.font = gc.getFont();
        currentState.lineAttributes = gc.getLineAttributes();
        appliedState.lineAttributes = clone(currentState.lineAttributes);
        currentState.graphicHints |= gc.getLineStyle();
        currentState.graphicHints |= gc.getAdvanced() ? ADVANCED_GRAPHICS_MASK
                : 0;
        currentState.graphicHints |= gc.getXORMode() ? XOR_MASK : 0;

        appliedState.graphicHints = currentState.graphicHints;

        currentState.relativeClip = new RectangleClipping(gc.getClipping());
        currentState.alpha = gc.getAlpha();
    }

    private void initTransform(boolean force) {
        if (!force && translateX == 0 && translateY == 0) {
            return;
        }

        if (transform == null) {
            transform = new Transform(Display.getCurrent());
            elementsNeedUpdate = true;
            transform.translate(translateX, translateY);
            translateX = 0;
            translateY = 0;
            gc.setTransform(transform);
            currentState.graphicHints |= ADVANCED_GRAPHICS_MASK;
        }
    }

    /**
     * @see Graphics#popState()
     */
    @Override
    public void popState() {
        stackPointer--;
        restoreState((State) stack.get(stackPointer));
    }

    /**
     * @see Graphics#pushState()
     */
    @Override
    public void pushState() {
        if (currentState.relativeClip == null) {
            throw new IllegalStateException(
                    "The clipping has been modified in" + //$NON-NLS-1$
                            "a way that cannot be saved and restored."); //$NON-NLS-1$
        }

        try {
            State s;
            currentState.dx = translateX;
            currentState.dy = translateY;

            if (elementsNeedUpdate) {
                elementsNeedUpdate = false;
                transform.getElements(currentState.affineMatrix = new float[6]);
            }
            if (stack.size() > stackPointer) {
                s = (State) stack.get(stackPointer);
                s.copyFrom(currentState);
            } else {
                stack.add(currentState.clone());
            }
            sharedClipping = true;
            stackPointer++;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void reconcileHints(GC gc, int applied, int hints) {
        int changes = hints ^ applied;

        if ((changes & XOR_MASK) != 0) {
            gc.setXORMode((hints & XOR_MASK) != 0);
        }

        // Check to see if there is anything remaining
        changes &= ~XOR_MASK;
        if (changes != 0) {
            if ((changes & INTERPOLATION_MASK) != 0) {
                gc.setInterpolation(((hints & INTERPOLATION_MASK) >> INTERPOLATION_SHIFT)
                        - INTERPOLATION_WHOLE_NUMBER);
            }

            if ((changes & FILL_RULE_MASK) != 0) {
                gc.setFillRule(((hints & FILL_RULE_MASK) >> FILL_RULE_SHIFT)
                        - FILL_RULE_WHOLE_NUMBER);
            }

            if ((changes & AA_MASK) != 0) {
                gc.setAntialias(((hints & AA_MASK) >> AA_SHIFT)
                        - AA_WHOLE_NUMBER);
            }

            if ((changes & TEXT_AA_MASK) != 0) {
                gc.setTextAntialias(((hints & TEXT_AA_MASK) >> TEXT_AA_SHIFT)
                        - AA_WHOLE_NUMBER);
            }

            // If advanced was flagged, but none of the conditions which trigger
            // advanced
            // actually got applied, force advanced graphics on.
            if ((changes & ADVANCED_GRAPHICS_MASK) != 0) {
                if ((hints & ADVANCED_GRAPHICS_MASK) != 0 && !gc.getAdvanced()) {
                    gc.setAdvanced(true);
                }
            }
        }
    }

    /**
     * @see Graphics#restoreState()
     */
    @Override
    public void restoreState() {
        restoreState((State) stack.get(stackPointer - 1));
    }

    /**
     * Sets all State information to that of the given State, called by
     * restoreState()
     * 
     * @param s
     *            the State
     */
    protected void restoreState(State s) {
        /*
         * We must set the transformation matrix first since it affects things
         * like clipping regions and patterns.
         */
        setAffineMatrix(s.affineMatrix);
        currentState.relativeClip = s.relativeClip;
        sharedClipping = true;

        // If the GC is currently advanced, but it was not when pushed, revert
        if (gc.getAdvanced() && (s.graphicHints & ADVANCED_GRAPHICS_MASK) == 0) {
            // Set applied clip to null to force a re-setting of the clipping.
            appliedState.relativeClip = null;
            gc.setAdvanced(false);
            appliedState.graphicHints &= ~ADVANCED_HINTS_MASK;
            appliedState.graphicHints |= ADVANCED_HINTS_DEFAULTS;
        }

        setBackgroundColor(s.bgColor);
        setBackgroundPattern(s.bgPattern);

        setForegroundColor(s.fgColor);
        setForegroundPattern(s.fgPattern);

        setAlpha(s.alpha);
        setLineAttributes(s.lineAttributes);
        setFont(s.font);

        // This method must come last because above methods will incorrectly set
        // advanced state
        setGraphicHints(s.graphicHints);

        translateX = currentState.dx = s.dx;
        translateY = currentState.dy = s.dy;
    }

    /**
     * This method requires advanced graphics support. A check should be made to
     * ensure advanced graphics is supported in the user's environment before
     * calling this method. See {@link GC#getAdvanced()}.
     * 
     * @see Graphics#rotate(float)
     */
    @Override
    public void rotate(float degrees) {
        // Flush clipping, patter, etc., before applying transform
        checkGC();
        initTransform(true);
        transform.rotate(degrees);
        gc.setTransform(transform);
        elementsNeedUpdate = true;

        // Can no longer operate or maintain clipping
        appliedState.relativeClip = currentState.relativeClip = null;
    }

    /**
     * @see Graphics#scale(double)
     */
    @Override
    public void scale(double factor) {
        scale((float) factor, (float) factor);
        scale = factor;
    }

    /**
     * This method requires advanced graphics support. A check should be made to
     * ensure advanced graphics is supported in the user's environment before
     * calling this method. See {@link GC#getAdvanced()}.
     * 
     * @see org.eclipse.draw2d.Graphics#scale(float, float)
     */
    @Override
    public void scale(float horizontal, float vertical) {
        // Flush any clipping before scaling
        checkGC();

        initTransform(true);
        transform.scale(horizontal, vertical);
        gc.setTransform(transform);
        elementsNeedUpdate = true;

        checkSharedClipping();
        if (currentState.relativeClip != null)
            currentState.relativeClip.scale(horizontal, vertical);
    }

    private void setAffineMatrix(float[] m) {
        if (!elementsNeedUpdate && currentState.affineMatrix == m)
            return;
        currentState.affineMatrix = m;
        if (m != null)
            transform.setElements(m[0], m[1], m[2], m[3], m[4], m[5]);
        else if (transform != null) {
            transform.dispose();
            transform = null;
            elementsNeedUpdate = false;
        }
        gc.setTransform(transform);
    }

    /**
     * This method requires advanced graphics support. A check should be made to
     * ensure advanced graphics is supported in the user's environment before
     * calling this method. See {@link GC#getAdvanced()}.
     * 
     * @see Graphics#setAlpha(int)
     */
    @Override
    public void setAlpha(int alpha) {
        currentState.graphicHints |= ADVANCED_GRAPHICS_MASK;
        if (currentState.alpha != alpha)
            gc.setAlpha(this.currentState.alpha = alpha);
    }

    /**
     * This method requires advanced graphics support. A check should be made to
     * ensure advanced graphics is supported in the user's environment before
     * calling this method. See {@link GC#getAdvanced()}.
     * 
     * @see Graphics#setAntialias(int)
     */
    @Override
    public void setAntialias(int value) {
        currentState.graphicHints &= ~AA_MASK;
        currentState.graphicHints |= ADVANCED_GRAPHICS_MASK
                | (value + AA_WHOLE_NUMBER) << AA_SHIFT;
    }

    @Override
    public void setAdvanced(boolean value) {
        if (value) {
            currentState.graphicHints |= ADVANCED_GRAPHICS_MASK;
        } else {
            currentState.graphicHints &= ~ADVANCED_GRAPHICS_MASK;
        }
    }

    /**
     * @see Graphics#setBackgroundColor(Color)
     */
    @Override
    public void setBackgroundColor(Color color) {
        currentState.bgColor = color;
        if (currentState.bgPattern != null) {
            currentState.bgPattern = null;
            // Force the BG color to be stale
            appliedState.bgColor = null;
        }
    }

    /**
     * @see Graphics#setBackgroundPattern(Pattern)
     */
    @Override
    public void setBackgroundPattern(Pattern pattern) {
        currentState.graphicHints |= ADVANCED_GRAPHICS_MASK;
        if (currentState.bgPattern == pattern) {
            return;
        }
        currentState.bgPattern = pattern;

        if (pattern != null) {
            initTransform(true);
        }
        gc.setBackgroundPattern(pattern);
    }

    /**
     * This method requires advanced graphics support. A check should be made to
     * ensure advanced graphics is supported in the user's environment before
     * calling this method. See {@link GC#getAdvanced()}.
     * 
     * @see Graphics#setClip(Path)
     */
    @Override
    public void setClip(Path path) {
        initTransform(false);
        if (((appliedState.graphicHints ^ currentState.graphicHints) & FILL_RULE_MASK) != 0) {
            // If there is a pending change to the fill rule, apply it first.
            gc.setFillRule(((currentState.graphicHints & FILL_RULE_MASK) >> FILL_RULE_SHIFT)
                    - FILL_RULE_WHOLE_NUMBER);
            // As long as the FILL_RULE is stored in a single bit, just toggling
            // it works.
            appliedState.graphicHints ^= FILL_RULE_MASK;
        }
        gc.setClipping(path);
        appliedState.relativeClip = currentState.relativeClip = null;
    }

    /**
     * Simple implementation of clipping a Path within the context of current
     * clipping rectangle for now (not region) <li>Note that this method wipes
     * out the clipping rectangle area, hence if clients need to reset it call
     * {@link #restoreState()}
     * 
     * @see org.eclipse.draw2d.Graphics#clipPath(org.eclipse.swt.graphics.Path)
     */
    @Override
    public void clipPath(Path path) {
        initTransform(false);
        if (((appliedState.graphicHints ^ currentState.graphicHints) & FILL_RULE_MASK) != 0) {
            // If there is a pending change to the fill rule, apply it first.
            gc.setFillRule(((currentState.graphicHints & FILL_RULE_MASK) >> FILL_RULE_SHIFT)
                    - FILL_RULE_WHOLE_NUMBER);
            // As long as the FILL_RULE is stored in a single bit, just toggling
            // it works.
            appliedState.graphicHints ^= FILL_RULE_MASK;
        }
        Rectangle clipping = currentState.relativeClip != null ? getClip(new Rectangle())
                : new Rectangle();
        if (!clipping.isEmpty()) {
            Path flatPath = new Path(path.getDevice(), path, 0.01f);
            PathData pathData = flatPath.getPathData();
            flatPath.dispose();
            Region region = new Region(path.getDevice());
            loadPath(region, pathData.points, pathData.types);
            region.intersect(new org.eclipse.swt.graphics.Rectangle(clipping.x,
                    clipping.y, clipping.width, clipping.height));
            gc.setClipping(region);
            appliedState.relativeClip = currentState.relativeClip = null;
            region.dispose();
        }
    }

    /**
     * @see Graphics#setClip(Rectangle)
     */
    @Override
    public void setClip(Rectangle rect) {
        currentState.relativeClip = new RectangleClipping(rect);
    }

    /**
     * @see Graphics#setFillRule(int)
     */
    @Override
    public void setFillRule(int rule) {
        currentState.graphicHints &= ~FILL_RULE_MASK;
        currentState.graphicHints |= (rule + FILL_RULE_WHOLE_NUMBER) << FILL_RULE_SHIFT;
    }

    /**
     * @see Graphics#setFont(Font)
     */
    @Override
    public void setFont(Font f) {
        currentState.font = f;
    }

    /**
     * @see Graphics#setForegroundColor(Color)
     */
    @Override
    public void setForegroundColor(Color color) {
        currentState.fgColor = color;
        if (currentState.fgPattern != null) {
            currentState.fgPattern = null;
            // Force fgColor to be stale
            appliedState.fgColor = null;
        }
    }

    /**
     * @see Graphics#setForegroundPattern(Pattern)
     */
    @Override
    public void setForegroundPattern(Pattern pattern) {
        currentState.graphicHints |= ADVANCED_GRAPHICS_MASK;
        if (currentState.fgPattern == pattern) {
            return;
        }
        currentState.fgPattern = pattern;

        if (pattern != null) {
            initTransform(true);
        }
        gc.setForegroundPattern(pattern);
    }

    private void setGraphicHints(int hints) {
        currentState.graphicHints = hints;
    }

    /**
     * This method requires advanced graphics support. A check should be made to
     * ensure advanced graphics is supported in the user's environment before
     * calling this method. See {@link GC#getAdvanced()}.
     * 
     * @see Graphics#setInterpolation(int)
     */
    @Override
    public void setInterpolation(int interpolation) {
        // values range [-1, 3]
        currentState.graphicHints &= ~INTERPOLATION_MASK;
        currentState.graphicHints |= ADVANCED_GRAPHICS_MASK
                | (interpolation + INTERPOLATION_WHOLE_NUMBER) << INTERPOLATION_SHIFT;
    }

    @Override
    public void setLineAttributes(LineAttributes lineAttributes) {
        copyLineAttributes(currentState.lineAttributes, lineAttributes);
    }

    /**
     * @see Graphics#setLineCap(int)
     */
    @Override
    public void setLineCap(int value) {
        currentState.lineAttributes.cap = value;
    }

    /**
     * @see Graphics#setLineDash(int[])
     */
    @Override
    public void setLineDash(int[] dashes) {
        float[] fArray = null;
        if (dashes != null) {
            fArray = new float[dashes.length];
            for (int i = 0; i < dashes.length; i++) {
                fArray[i] = dashes[i];
            }
        }
        setLineDash(fArray);
    }

    /**
     * @param value
     * @since 3.5
     */
    @Override
    public void setLineDash(float[] value) {
        if (value != null) {
            // validate dash values
            for (int i = 0; i < value.length; i++) {
                if (value[i] <= 0) {
                    SWT.error(SWT.ERROR_INVALID_ARGUMENT);
                }
            }

            currentState.lineAttributes.dash = value.clone();
            currentState.lineAttributes.style = SWT.LINE_CUSTOM;
        } else {
            currentState.lineAttributes.dash = null;
            currentState.lineAttributes.style = SWT.LINE_SOLID;
        }
    }

    /**
     * @since 3.5
     */
    @Override
    public void setLineDashOffset(float value) {
        currentState.lineAttributes.dashOffset = value;
    }

    /**
     * @see Graphics#setLineJoin(int)
     */
    @Override
    public void setLineJoin(int value) {
        currentState.lineAttributes.join = value;
    }

    /**
     * @see Graphics#setLineStyle(int)
     */
    @Override
    public void setLineStyle(int value) {
        currentState.lineAttributes.style = value;
    }

    /**
     * @see Graphics#setLineWidth(int)
     */
    @Override
    public void setLineWidth(int width) {
        currentState.lineAttributes.width = width;
    }

    @Override
    public void setLineWidthFloat(float value) {
        currentState.lineAttributes.width = value;
    }

    @Override
    public void setLineMiterLimit(float value) {
        currentState.lineAttributes.miterLimit = value;
    }

    /**
     * This method requires advanced graphics support. A check should be made to
     * ensure advanced graphics is supported in the user's environment before
     * calling this method. See {@link GC#getAdvanced()}.
     * 
     * @see Graphics#setTextAntialias(int)
     */
    @Override
    public void setTextAntialias(int value) {
        currentState.graphicHints &= ~TEXT_AA_MASK;
        currentState.graphicHints |= ADVANCED_GRAPHICS_MASK
                | (value + AA_WHOLE_NUMBER) << TEXT_AA_SHIFT;
    }

    /**
     * @see Graphics#setXORMode(boolean)
     */
    @Override
    public void setXORMode(boolean xor) {
        currentState.graphicHints &= ~XOR_MASK;
        if (xor) {
            currentState.graphicHints |= XOR_MASK;
        }
    }

    /**
     * This method requires advanced graphics support. A check should be made to
     * ensure advanced graphics is supported in the user's environment before
     * calling this method. See {@link GC#getAdvanced()}.
     * 
     * @see Graphics#shear(float, float)
     */
    @Override
    public void shear(float horz, float vert) {
        // Flush any clipping changes before shearing
        checkGC();
        initTransform(true);
        float matrix[] = new float[6];
        transform.getElements(matrix);
        transform.setElements(matrix[0] + matrix[2] * vert, matrix[1]
                + matrix[3] * vert, matrix[0] * horz + matrix[2], matrix[1]
                * horz + matrix[3], matrix[4], matrix[5]);

        gc.setTransform(transform);
        elementsNeedUpdate = true;
        // Can no longer track clipping changes
        appliedState.relativeClip = currentState.relativeClip = null;
    }

    /**
     * This method may require advanced graphics support if using a transform,
     * in this case, a check should be made to ensure advanced graphics is
     * supported in the user's environment before calling this method. See
     * {@link GC#getAdvanced()}.
     * 
     * @see Graphics#translate(int, int)
     */
    @Override
    public void translate(int dx, int dy) {
        if (dx == 0 && dy == 0)
            return;
        if (transform != null) {
            // Flush clipping, pattern, etc. before applying transform
            checkGC();
            transform.translate(dx, dy);
            elementsNeedUpdate = true;
            gc.setTransform(transform);
        } else {
            translateX += dx;
            translateY += dy;
        }
        checkSharedClipping();
        if (currentState.relativeClip != null)
            currentState.relativeClip.translate(-dx, -dy);
    }

    /**
     * This method requires advanced graphics support. A check should be made to
     * ensure advanced graphics is supported in the user's environment before
     * calling this method. See {@link GC#getAdvanced()}.
     * 
     * @see Graphics#translate(float, float)
     */
    @Override
    public void translate(float dx, float dy) {
        initTransform(true);
        checkGC();
        transform.translate(dx, dy);
        elementsNeedUpdate = true;
        gc.setTransform(transform);
        checkSharedClipping();
        if (currentState.relativeClip != null)
            currentState.relativeClip.translate(-dx, -dy);
    }

    private void translatePointArray(int[] points, int translateX,
            int translateY) {
        if (translateX == 0 && translateY == 0)
            return;
        for (int i = 0; (i + 1) < points.length; i += 2) {
            points[i] += translateX;
            points[i + 1] += translateY;
        }
    }

    /**
     * Countermeasure against LineAttributes class not having its own clone()
     * method.
     * 
     * @since 3.6
     */
    public static LineAttributes clone(LineAttributes src) {
        float[] dashClone = null;
        if (src.dash != null) {
            dashClone = new float[src.dash.length];
            System.arraycopy(src.dash, 0, dashClone, 0, dashClone.length);
        }
        return new LineAttributes(src.width, src.cap, src.join, src.style,
                dashClone, src.dashOffset, src.miterLimit);
    }

    /**
     * Countermeasure against LineAttributes class not having a copy by value
     * function.
     * 
     * @since 3.6
     */
    public static void copyLineAttributes(LineAttributes dest,
            LineAttributes src) {
        if (dest != src) {
            dest.cap = src.cap;
            dest.join = src.join;
            dest.miterLimit = src.miterLimit;
            dest.style = src.style;
            dest.width = src.width;
            dest.dashOffset = src.dashOffset;

            if (src.dash == null) {
                dest.dash = null;
            } else {
                if ((dest.dash == null)
                        || (dest.dash.length != src.dash.length)) {
                    dest.dash = new float[src.dash.length];
                }
                System.arraycopy(src.dash, 0, dest.dash, 0, src.dash.length);
            }
        }
    }

    /**
     * Utility method for use with countermeasure against passing line
     * attributes to SWT forcing advanced graphics.
     * 
     * @return
     * @since 3.2
     */
    private static int[] convertFloatArrayToInt(float[] fArray) {
        int[] iArray = null;
        if (fArray != null) {
            int arrayLen = fArray.length;
            iArray = new int[arrayLen];
            for (int i = 0; i < arrayLen; i++) {
                iArray[i] = (int) fArray[i];
            }
        }
        return iArray;
    }

    static void loadPath(Region region, float[] points, byte[] types) {
        int start = 0, end = 0;
        for (int i = 0; i < types.length; i++) {
            switch (types[i]) {
            case SWT.PATH_MOVE_TO: {
                if (start != end) {
                    int n = 0;
                    int[] temp = new int[end - start];
                    for (int k = start; k < end; k++) {
                        temp[n++] = Math.round(points[k]);
                    }
                    region.add(temp);
                }
                start = end;
                end += 2;
                break;
            }
            case SWT.PATH_LINE_TO: {
                end += 2;
                break;
            }
            case SWT.PATH_CLOSE: {
                if (start != end) {
                    int n = 0;
                    int[] temp = new int[end - start];
                    for (int k = start; k < end; k++) {
                        temp[n++] = Math.round(points[k]);
                    }
                    region.add(temp);
                }
                start = end;
                break;
            }
            }
        }
        if (start != end) {
            int n = 0;
            int[] temp = new int[end - start];
            for (int k = start; k < end; k++) {
                temp[n++] = Math.round(points[k]);
            }
            region.add(temp);
        }
    }
}