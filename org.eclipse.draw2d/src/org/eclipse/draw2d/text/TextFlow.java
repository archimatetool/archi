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
package org.eclipse.draw2d.text;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.TextLayout;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.TextUtilities;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * An inline flow figure that renders a string of text across one or more lines.
 * A TextFlow cannot contain children. All <code>InlineFlow</code> figure's must
 * be parented by a <code>FlowFigure</code>.
 * <p>
 * WARNING: This class is not intended to be subclassed by clients.
 * 
 * @author hudsonr
 * @author Pratik Shah
 * @since 2.1
 */
@SuppressWarnings("rawtypes")
public class TextFlow extends InlineFlow {

    static final String ELLIPSIS = "..."; //$NON-NLS-1$
    private BidiInfo bidiInfo;
    private int selectionEnd = -1;
    private String text;

    /**
     * Constructs a new TextFlow with the empty String.
     * 
     * @see java.lang.Object#Object()
     */
    public TextFlow() {
        this(new String());
    }

    /**
     * Constructs a new TextFlow with the specified String.
     * 
     * @param s
     *            the string
     */
    public TextFlow(String s) {
        text = s;
    }

    /**
     * Returns the width of the text until the first line-break.
     * 
     * @see org.eclipse.draw2d.text.FlowFigure#addLeadingWordRequirements(int[])
     */
    @Override
    public boolean addLeadingWordRequirements(int[] width) {
        return addLeadingWordWidth(getText(), width);
    }

    /**
     * Calculates the width taken up by the given text before a line-break is
     * encountered.
     * 
     * @param text
     *            the text in which the break is to be found
     * @param width
     *            the width before the next line-break (if one's found; the
     *            width of all the given text, otherwise) will be added on to
     *            the first int in the given array
     * @return <code>true</code> if a line-break was found
     * @since 3.1
     */
    boolean addLeadingWordWidth(String text, int[] width) {
        if (text.length() == 0)
            return false;
        if (Character.isWhitespace(text.charAt(0)))
            return true;

        text = 'a' + text + 'a';
        FlowUtilities.LINE_BREAK.setText(text);
        int index = FlowUtilities.LINE_BREAK.next() - 1;
        if (index == 0)
            return true;
        while (Character.isWhitespace(text.charAt(index)))
            index--;
        boolean result = index < text.length() - 1;
        // index should point to the end of the actual text (not including the
        // 'a' that was
        // appended), if there were no breaks
        if (index == text.length() - 1)
            index--;
        text = text.substring(1, index + 1);

        if (bidiInfo == null)
            width[0] += getTextUtilities().getTextExtents(text, getFont()).width;
        else {
            TextLayout textLayout = FlowUtilities.getTextLayout();
            textLayout.setFont(getFont());
            textLayout.setText(text);
            width[0] += textLayout.getBounds().width;
        }
        return result;
    }

    /**
     * A TextFlow contributes its text.
     * 
     * @see org.eclipse.draw2d.text.FlowFigure#contributeBidi(org.eclipse.draw2d.text.BidiProcessor)
     */
    @Override
    protected void contributeBidi(BidiProcessor proc) {
        bidiInfo = null;
        proc.add(this, getText());
    }

    /**
     * @see org.eclipse.draw2d.text.InlineFlow#createDefaultFlowLayout()
     */
    @Override
    protected FlowFigureLayout createDefaultFlowLayout() {
        return new ParagraphTextLayout(this);
    }

    private int findNextLineOffset(Point p, int[] trailing) {
        if (getBounds().bottom() <= p.y)
            return -1;

        TextFragmentBox closestBox = null;
        int index = 0;
        List fragments = getFragmentsWithoutBorder();
        for (int i = fragments.size() - 1; i >= 0; i--) {
            TextFragmentBox box = (TextFragmentBox) fragments.get(i);
            if (box.getBaseline() - box.getLineRoot().getAscent() > p.y
                    && (closestBox == null
                            || box.getBaseline() < closestBox.getBaseline() || (box
                            .getBaseline() == closestBox.getBaseline() && hDistanceBetween(
                            box, p.x) < hDistanceBetween(closestBox, p.x)))) {
                closestBox = box;
                index = i;
            }
        }
        return findOffset(p, trailing, closestBox, index);
    }

    private int findOffset(Point p, int[] trailing, TextFragmentBox box,
            int boxIndex) {
        if (box == null)
            return -1;
        TextLayout layout = FlowUtilities.getTextLayout();
        layout.setFont(getFont());
        layout.setText(getBidiSubstring(box, boxIndex));
        int x = p.x - box.getX();
        if (isMirrored())
            x = box.getWidth() - x;
        int layoutOffset = layout
                .getOffset(x, p.y - box.getTextTop(), trailing);
        return box.offset + layoutOffset - getBidiPrefixLength(box, boxIndex);
    }

    private int findPreviousLineOffset(Point p, int[] trailing) {
        if (getBounds().y > p.y)
            return -1;

        TextFragmentBox closestBox = null;
        int index = 0;
        List fragments = getFragmentsWithoutBorder();
        for (int i = fragments.size() - 1; i >= 0; i--) {
            TextFragmentBox box = (TextFragmentBox) fragments.get(i);
            if (box.getBaseline() + box.getLineRoot().getDescent() < p.y
                    && (closestBox == null
                            || box.getBaseline() > closestBox.getBaseline() || (box
                            .getBaseline() == closestBox.getBaseline() && hDistanceBetween(
                            box, p.x) < hDistanceBetween(closestBox, p.x)))) {
                closestBox = box;
                index = i;
            }
        }
        return findOffset(p, trailing, closestBox, index);
    }

    int getAscent() {
        return getTextUtilities().getAscent(getFont());
    }

    /**
     * Returns the BidiInfo for this figure or <code>null</code>.
     * 
     * @return <code>null</code> or the info
     * @since 3.1
     */
    public BidiInfo getBidiInfo() {
        return bidiInfo;
    }

    private int getBidiPrefixLength(TextFragmentBox box, int index) {
        if (box.getBidiLevel() < 1)
            return 0;
        if (index > 0 || !bidiInfo.leadingJoiner)
            return 1;
        return 2;
    }

    /**
     * @param box
     *            which fragment
     * @param index
     *            the fragment index
     * @return the bidi string for that fragment
     * @since 3.1
     */
    protected String getBidiSubstring(TextFragmentBox box, int index) {
        if (box.getBidiLevel() < 1)
            return getText().substring(box.offset, box.offset + box.length);

        StringBuffer buffer = new StringBuffer(box.length + 3);
        buffer.append(box.isRightToLeft() ? BidiChars.RLO : BidiChars.LRO);
        if (index == 0 && bidiInfo.leadingJoiner)
            buffer.append(BidiChars.ZWJ);
        buffer.append(getText().substring(box.offset, box.offset + box.length));
        if (index == getFragmentsWithoutBorder().size() - 1
                && bidiInfo.trailingJoiner)
            buffer.append(BidiChars.ZWJ);
        return buffer.toString();
    }

    /**
     * Returns the CaretInfo in absolute coordinates. The offset must be between
     * 0 and the length of the String being displayed.
     * 
     * @since 3.1
     * @param offset
     *            the location in this figure's text
     * @param trailing
     *            true if the caret is being placed after the offset
     * @exception IllegalArgumentException
     *                If the offset is not between <code>0</code> and the length
     *                of the string inclusively
     * @return the caret bounds relative to this figure
     */
    public CaretInfo getCaretPlacement(int offset, boolean trailing) {
        if (offset < 0 || offset > getText().length())
            throw new IllegalArgumentException("Offset: " + offset //$NON-NLS-1$
                    + " is invalid"); //$NON-NLS-1$

        if (offset == getText().length())
            trailing = false;

        List fragments = getFragmentsWithoutBorder();
        int i = fragments.size();
        TextFragmentBox box;
        do
            box = (TextFragmentBox) fragments.get(--i);
        while (offset < box.offset && i > 0);

        // Cannot be trailing and after the last char, so go to first char in
        // next box
        if (trailing && box.offset + box.length <= offset) {
            box = (TextFragmentBox) fragments.get(++i);
            offset = box.offset;
            trailing = false;
        }

        Point where = getPointInBox(box, offset, i, trailing);
        CaretInfo info = new CaretInfo(where.x, where.y, box.getAscent(),
                box.getDescent(), box.getLineRoot().getAscent(), box
                        .getLineRoot().getDescent());
        translateToAbsolute(info);
        return info;
    }

    Point getPointInBox(TextFragmentBox box, int offset, int index,
            boolean trailing) {
        offset -= box.offset;
        offset = Math.min(box.length, offset);
        Point result = new Point(0, box.getTextTop());
        if (bidiInfo == null) {
            if (trailing && offset < box.length)
                offset++;
            String substring = getText().substring(box.offset,
                    box.offset + offset);
            result.x = getTextUtilities().getTextExtents(substring, getFont()).width;
        } else {
            TextLayout layout = FlowUtilities.getTextLayout();
            layout.setFont(getFont());
            String fragString = getBidiSubstring(box, index);
            layout.setText(fragString);
            offset += getBidiPrefixLength(box, index);
            result.x = layout.getLocation(offset, trailing).x;
            if (isMirrored())
                result.x = box.width - result.x;
        }
        result.x += box.getX();
        return result;
    }

    int getDescent() {
        return getTextUtilities().getDescent(getFont());
    }

    /**
     * Returns the minimum character offset which is on the given baseline
     * y-coordinate. The y location should be relative to this figure. The
     * return value will be between 0 and N-1. If no fragment is located on the
     * baseline, <code>-1</code> is returned.
     * 
     * @since 3.1
     * @param baseline
     *            the relative baseline coordinate
     * @return -1 or the lowest offset for the line
     */
    public int getFirstOffsetForLine(int baseline) {
        TextFragmentBox box;
        List fragments = getFragmentsWithoutBorder();
        for (int i = 0; i < fragments.size(); i++) {
            box = (TextFragmentBox) fragments.get(i);
            if (baseline == box.getBaseline())
                return box.offset;
        }
        return -1;
    }

    /**
     * Returns the <code>TextFragmentBox</code> fragments contained in this
     * TextFlow, not including the border fragments. The returned list should
     * not be modified.
     * 
     * @return list of fragments without the border fragments
     * @since 3.4
     */
    protected List getFragmentsWithoutBorder() {
        List fragments = getFragments();
        if (getBorder() != null)
            fragments = fragments.subList(1, fragments.size() - 1);
        return fragments;
    }

    /**
     * Returns the maximum offset for a character which is on the given baseline
     * y-coordinate. The y location should be relative to this figure. The
     * return value will be between 0 and N-1. If no fragment is located on the
     * baseline, <code>-1</code> is returned.
     * 
     * @since 3.1
     * @param baseline
     *            the relative baseline coordinate
     * @return -1 or the highest offset at the given baseline
     */
    public int getLastOffsetForLine(int baseline) {
        TextFragmentBox box;
        List fragments = getFragmentsWithoutBorder();
        for (int i = fragments.size() - 1; i >= 0; i--) {
            box = (TextFragmentBox) fragments.get(i);
            if (baseline == box.getBaseline())
                return box.offset + box.length - 1;
        }
        return -1;
    }

    /**
     * Returns the offset nearest the given point either up or down one line. If
     * no offset is found, -1 is returned. <code>trailing[0]</code> will be set
     * to 1 if the reference point is closer to the trailing edge of the offset
     * than it is to the leading edge.
     * 
     * @since 3.1
     * @param p
     *            a reference point
     * @param down
     *            <code>true</code> if the search is down
     * @param trailing
     *            an int array
     * @return the next offset or <code>-1</code>
     */
    public int getNextOffset(Point p, boolean down, int[] trailing) {
        return down ? findNextLineOffset(p, trailing) : findPreviousLineOffset(
                p, trailing);
    }

    /**
     * Returns the next offset which is visible in at least one fragment or -1
     * if there is not one. A visible offset means that the character or the one
     * preceding it is displayed, which implies that a caret can be positioned
     * at such an offset. This is useful for advancing a caret past characters
     * which resulted in a line wrap.
     * 
     * @param offset
     *            the reference offset
     * @return the next offset which is visible
     * @since 3.1
     */
    public int getNextVisibleOffset(int offset) {
        TextFragmentBox box;
        List fragments = getFragmentsWithoutBorder();
        for (int i = 0; i < fragments.size(); i++) {
            box = (TextFragmentBox) fragments.get(i);
            if (box.offset + box.length <= offset)
                continue;
            return Math.max(box.offset, offset + 1);
        }
        return -1;
    }

    /**
     * Returns the offset of the character directly below or nearest the given
     * location. The point must be relative to this figure. The return value
     * will be between 0 and N-1. If the proximity argument is not
     * <code>null</code>, the result may also be <code>-1</code> if no offset
     * was found within the proximity.
     * <P>
     * For a typical character, the trailing argument will be filled in to
     * indicate whether the point is closer to the leading edge (0) or the
     * trailing edge (1). When the point is over a cluster composed of multiple
     * characters, the trailing argument will be filled with the position of the
     * character in the cluster that is closest to the point.
     * <P>
     * If the proximity argument is not <code>null</code>, then the location may
     * be no further than the proximity given. Passing <code>null</code> is
     * equivalent to passing <code>new
     * Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE)</code>. The
     * <code>width</code> field of the proximity will contain the horizontal
     * distance, <code>height</code> will contain vertical. Vertical proximity
     * is more important than horizontal. The returned offset is the lowest
     * index with minimum vertical proximity not exceeding the given limit, with
     * horizontal proximity not exceeding the given limit. If an offset that is
     * within the proximity is found, then the given <code>Dimension</code> will
     * be updated to reflect the new proximity.
     * 
     * 
     * @since 3.1
     * @param p
     *            the point relative to this figure
     * @param trailing
     *            the trailing buffer
     * @param proximity
     *            restricts and records the distance of the returned offset
     * @return the nearest offset in this figure's text
     */
    public int getOffset(Point p, int trailing[], Dimension proximity) {
        if (proximity == null)
            proximity = new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
        TextFragmentBox closestBox = null;
        int index = 0;
        int dy;
        int dx;
        int i = 0;
        int size = fragments.size();
        if (getBorder() instanceof FlowBorder) {
            i++;
            size--;
        }
        for (; i < size; i++) {
            TextFragmentBox box = (TextFragmentBox) fragments.get(i);
            dy = vDistanceBetween(box, p.y);
            if (dy > proximity.height)
                continue;
            dx = hDistanceBetween(box, p.x);
            if (dy == proximity.height && dx >= proximity.width)
                continue;
            proximity.height = dy;
            proximity.width = dx;
            closestBox = box;
            index = i;
        }
        return findOffset(p, trailing, closestBox, index);
    }

    /**
     * Returns the previous offset which is visible in at least one fragment or
     * -1 if there is not one. See {@link #getNextVisibleOffset(int)} for more.
     * 
     * @param offset
     *            a reference offset
     * @return -1 or the previous offset which is visible
     * @since 3.1
     */

    public int getPreviousVisibleOffset(int offset) {
        TextFragmentBox box;
        if (offset == -1)
            offset = Integer.MAX_VALUE;
        List fragments = getFragmentsWithoutBorder();
        for (int i = fragments.size() - 1; i >= 0; i--) {
            box = (TextFragmentBox) fragments.get(i);
            if (box.offset >= offset)
                continue;
            return Math.min(box.offset + box.length, offset - 1);
        }
        return -1;
    }

    /**
     * @return the String being displayed; will not be <code>null</code>
     */
    public String getText() {
        return text;
    }

    int getVisibleAscent() {
        if (getBorder() instanceof FlowBorder) {
            FlowBorder border = (FlowBorder) getBorder();
            return border.getInsets(this).top + getAscent();
        }
        return getAscent();
    }

    int getVisibleDescent() {
        if (getBorder() instanceof FlowBorder) {
            FlowBorder border = (FlowBorder) getBorder();
            return border.getInsets(this).bottom + getDescent();
        }
        return getDescent();
    }

    private int hDistanceBetween(TextFragmentBox box, int x) {
        if (x < box.getX())
            return box.getX() - x;
        return Math.max(0, x - (box.getX() + box.getWidth()));
    }

    /**
     * Returns <code>true</code> if a portion if the text is truncated using
     * ellipses ("...").
     * 
     * @return <code>true</code> if the text is truncated with ellipses
     */
    public boolean isTextTruncated() {
        for (int i = 0; i < fragments.size(); i++) {
            if (((TextFragmentBox) fragments.get(i)).isTruncated())
                return true;
        }
        return false;
    }

    /**
     * @see org.eclipse.draw2d.Figure#paintFigure(Graphics)
     */
    @Override
    protected void paintFigure(Graphics g) {
        TextFragmentBox frag;
        g.getClip(Rectangle.SINGLETON);
        int yStart = Rectangle.SINGLETON.y;
        int yEnd = Rectangle.SINGLETON.bottom();

        for (int i = 0; i < fragments.size(); i++) {
            frag = (TextFragmentBox) fragments.get(i);
            // g.drawLine(frag.getX(), frag.getLineRoot().getVisibleTop(),
            // frag.getWidth() + frag.getX(),
            // frag.getLineRoot().getVisibleTop());
            // g.drawLine(frag.getX(), frag.getBaseline(), frag.getWidth() +
            // frag.getX(), frag.getBaseline());
            if (frag.offset == -1)
                continue;
            // Loop until first visible fragment
            if (yStart > frag.getLineRoot().getVisibleBottom() + 1)// The + 1 is
                                                                    // for
                                                                    // disabled
                                                                    // text
                continue;
            // Break loop at first non-visible fragment
            if (yEnd < frag.getLineRoot().getVisibleTop())
                break;

            String draw = getBidiSubstring(frag, i);

            if (frag.isTruncated())
                draw += ELLIPSIS;

            if (!isEnabled()) {
                Color fgColor = g.getForegroundColor();
                g.setForegroundColor(ColorConstants.buttonLightest);
                paintText(g, draw, frag.getX() + 1, frag.getBaseline()
                        - getAscent() + 1, frag.getBidiLevel());
                g.setForegroundColor(ColorConstants.buttonDarker);
                paintText(g, draw, frag.getX(), frag.getBaseline()
                        - getAscent(), frag.getBidiLevel());
                g.setForegroundColor(fgColor);
            } else {
                paintText(g, draw, frag.getX(), frag.getBaseline()
                        - getAscent(), frag.getBidiLevel());
            }
        }
    }

    /**
     * @see InlineFlow#paintSelection(org.eclipse.draw2d.Graphics)
     */
    @Override
    protected void paintSelection(Graphics graphics) {
        if (selectionStart == -1)
            return;
        graphics.setXORMode(true);
        graphics.setBackgroundColor(ColorConstants.white);

        TextFragmentBox frag;
        for (int i = 0; i < fragments.size(); i++) {
            frag = (TextFragmentBox) fragments.get(i);
            // Loop until first visible fragment
            if (frag.offset + frag.length <= selectionStart)
                continue;
            if (frag.offset > selectionEnd)
                return;
            if (selectionStart <= frag.offset
                    && selectionEnd >= frag.offset + frag.length) {
                int y = frag.getLineRoot().getVisibleTop();
                int height = frag.getLineRoot().getVisibleBottom() - y;
                graphics.fillRectangle(frag.getX(), y, frag.getWidth(), height);
            } else if (selectionEnd > frag.offset
                    && selectionStart < frag.offset + frag.length) {
                Point p1 = getPointInBox(frag,
                        Math.max(frag.offset, selectionStart), i, false);
                Point p2 = getPointInBox(frag,
                        Math.min(frag.offset + frag.length, selectionEnd) - 1,
                        i, true);
                Rectangle rect = new Rectangle(p1, p2);
                rect.width--;
                rect.y = frag.getLineRoot().getVisibleTop();
                rect.height = frag.getLineRoot().getVisibleBottom() - rect.y;
                graphics.fillRectangle(rect);
            }
        }
    }

    protected void paintText(Graphics g, String draw, int x, int y,
            int bidiLevel) {
        if (bidiLevel == -1) {
            g.drawText(draw, x, y);
        } else {
            TextLayout tl = FlowUtilities.getTextLayout();
            if (isMirrored())
                tl.setOrientation(SWT.RIGHT_TO_LEFT);
            tl.setFont(g.getFont());
            tl.setText(draw);
            g.drawTextLayout(tl, x, y);
        }
    }

    /**
     * @see org.eclipse.draw2d.text.FlowFigure#setBidiInfo(org.eclipse.draw2d.text.BidiInfo)
     */
    @Override
    public void setBidiInfo(BidiInfo info) {
        this.bidiInfo = info;
    }

    /**
     * Sets the extent of selection. The selection range is inclusive. For
     * example, the range [0, 0] indicates that the first character is selected.
     * 
     * @param start
     *            the start offset
     * @param end
     *            the end offset
     * @since 3.1
     */
    @Override
    public void setSelection(int start, int end) {
        boolean repaint = false;

        if (selectionStart == start) {
            if (selectionEnd == end)
                return;
            repaint = true;
        } else
            repaint = selectionStart != selectionEnd || start != end;

        selectionStart = start;
        selectionEnd = end;
        if (repaint)
            repaint();
    }

    /**
     * Sets the text being displayed. The string may not be <code>null</code>.
     * 
     * @param s
     *            The new text
     */
    public void setText(String s) {
        if (s != null && !s.equals(text)) {
            text = s;
            revalidateBidi(this);
            repaint();
        }
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return text;
    }

    private int vDistanceBetween(TextFragmentBox box, int y) {
        int top = box.getBaseline() - box.getLineRoot().getAscent();
        if (y < top)
            return top - y;
        return Math.max(0, y
                - (box.getBaseline() + box.getLineRoot().getDescent()));
    }

    /**
     * Gets the <code>FlowUtilities</code> instance to be used in measurement
     * calculations.
     * 
     * @return a <code>FlowUtilities</code> instance
     * @since 3.4
     */
    protected FlowUtilities getFlowUtilities() {
        return FlowUtilities.INSTANCE;
    }

    /**
     * Gets the <code>TextUtilities</code> instance to be used in measurement
     * calculations.
     * 
     * @return a <code>TextUtilities</code> instance
     * @since 3.4
     */
    protected TextUtilities getTextUtilities() {
        return TextUtilities.INSTANCE;
    }

}