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

/**
 * A Geometric object for representing a TextFragment region on a line of Text.
 */
public class TextFragmentBox extends ContentBox {

    /**
     * The fragment's length in characters.
     */
    public int length;

    /**
     * The character offset at which this fragment begins.
     */
    public int offset;

    private TextFlow textflow;
    private boolean truncated;

    /**
     * Creates a new TextFragmentBox for the given text flow.
     * 
     * @param textflow
     *            the text flow
     */
    public TextFragmentBox(TextFlow textflow) {
        this.textflow = textflow;
    }

    /**
     * @see org.eclipse.draw2d.text.FlowBox#containsPoint(int, int)
     */
    @Override
    public boolean containsPoint(int x, int y) {
        return x >= getX() && x < getX() + getWidth()
                && y >= getBaseline() - getAscentWithBorder()
                && y <= getBaseline() + getDescentWithBorder();
    }

    /**
     * Returns the textflow's font's ascent. The ascent is the same for all
     * fragments in a given TextFlow.
     * 
     * @return the ascent
     */
    @Override
    public int getAscent() {
        return textflow.getAscent();
    }

    @Override
    int getAscentWithBorder() {
        return textflow.getAscent() + FlowUtilities.getBorderAscent(textflow);
    }

    /**
     * Returns the textflow's font's descent. The descent is the same for all
     * fragments in a given TextFlow.
     * 
     * @return the descent
     */
    @Override
    public int getDescent() {
        return textflow.getDescent();
    }

    @Override
    int getDescentWithBorder() {
        return textflow.getDescent() + FlowUtilities.getBorderDescent(textflow);
    }

    @Override
    int getOuterAscent() {
        return textflow.getAscent()
                + FlowUtilities.getBorderAscentWithMargin(textflow);
    }

    @Override
    int getOuterDescent() {
        return textflow.getDescent()
                + FlowUtilities.getBorderDescentWithMargin(textflow);
    }

    final int getTextTop() {
        return getBaseline() - getAscent();
    }

    /**
     * Returns <code>true</code> if the bidi level is odd. Right to left
     * fragments should be queried and rendered with the RLO control character
     * inserted in front.
     * 
     * @return <code>true</code> if right-to-left
     * @since 3.1
     */
    public boolean isRightToLeft() {
        // -1 % 2 == -1
        return getBidiLevel() % 2 == 1;
    }

    /**
     * Returns <code>true</code> if the fragment should be rendered as
     * truncated.
     * 
     * @return <code>true</code> if the fragment is truncated
     * @since 3.1
     */
    public boolean isTruncated() {
        return truncated;
    }

    /**
     * Marks the fragment as having been truncated.
     * 
     * @param value
     *            <code>true</code> if the fragment is truncated
     * @since 3.1
     */
    public void setTruncated(boolean value) {
        this.truncated = value;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "[" + offset + ", " + (offset + length) //$NON-NLS-1$ //$NON-NLS-2$
                + ") = \"" + textflow.getText().substring(offset, offset + length) + '\"'; //$NON-NLS-1$ 
    }

}
