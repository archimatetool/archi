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

import org.eclipse.swt.graphics.Font;

/**
 * The layout for {@link TextFlow}.
 * 
 * @author hudsonr
 * @since 2.1
 */
public class ParagraphTextLayout extends TextLayout {

    /**
     * Wrapping will ONLY occur at valid line breaks
     */
    public static final int WORD_WRAP_HARD = 0;

    /**
     * Wrapping will always occur at the end of the available space, breaking in
     * the middle of a word.
     */
    public static final int WORD_WRAP_SOFT = 1;

    /**
     * Wrapping will always occur at the end of available space, truncating a
     * word if it doesn't fit. Note that truncation is not supported across
     * multiple figures and with BiDi. Undesired effects may result if that is
     * the case.
     */
    public static final int WORD_WRAP_TRUNCATE = 2;

    private int wrappingStyle = WORD_WRAP_HARD;

    /**
     * Constructs a new ParagraphTextLayout on the specified TextFlow.
     * 
     * @param flow
     *            the TextFlow
     */
    public ParagraphTextLayout(TextFlow flow) {
        super(flow);
    }

    /**
     * Constructs the layout with the specified TextFlow and wrapping style. The
     * wrapping style must be one of:
     * <UL>
     * <LI>{@link #WORD_WRAP_HARD}</LI>
     * <LI>{@link #WORD_WRAP_SOFT}</LI>
     * <LI>{@link #WORD_WRAP_TRUNCATE}</LI>
     * </UL>
     * 
     * @param flow
     *            the textflow
     * @param style
     *            the style of wrapping
     */
    public ParagraphTextLayout(TextFlow flow, int style) {
        this(flow);
        wrappingStyle = style;
    }

    /**
     * Given the Bidi levels of the given text, this method breaks the given
     * text up by its level runs.
     * 
     * @param text
     *            the String that needs to be broken up into its level runs
     * @param levelInfo
     *            the Bidi levels
     * @return the requested segment
     */
    private String[] getSegments(String text, int levelInfo[]) {
        if (levelInfo.length == 1)
            return new String[] { text };

        String result[] = new String[levelInfo.length / 2 + 1];

        int i;
        int endOffset;
        int beginOffset = 0;

        for (i = 0; i < result.length - 1; i++) {
            endOffset = levelInfo[i * 2 + 1];
            result[i] = text.substring(beginOffset, endOffset);
            beginOffset = endOffset;
        }
        endOffset = text.length();
        result[i] = text.substring(beginOffset, endOffset);
        return result;
    }

    class SegmentLookahead implements FlowUtilities.LookAhead {
        private int seg = -1;
        private String segs[];
        private int[] width;
        private final int trailingBorderSize;

        SegmentLookahead(String segs[], int trailingBorderSize) {
            this.segs = segs;
            this.trailingBorderSize = trailingBorderSize;
        }

        @Override
        public int getWidth() {
            if (width == null) {
                width = new int[1];
                int startingIndex = seg + 1;
                TextFlow textFlow = (TextFlow) getFlowFigure();

                if (startingIndex == segs.length) {
                    width[0] += trailingBorderSize;
                    getContext().getWidthLookahead(textFlow, width);
                } else {
                    String rest = segs[startingIndex];
                    for (int k = startingIndex + 1; k < segs.length; k++)
                        rest += segs[k];
                    if (!textFlow.addLeadingWordWidth(rest, width)) {
                        width[0] += trailingBorderSize;
                        getContext().getWidthLookahead(textFlow, width);
                    }
                }
            }
            return width[0];
        }

        public void setIndex(int value) {
            this.seg = value;
            width = null;
        }
    }

    /**
     * @see org.eclipse.draw2d.text.FlowFigureLayout#layout()
     */
    @SuppressWarnings("rawtypes")
    @Override
    protected void layout() {
        TextFlow textFlow = (TextFlow) getFlowFigure();
        int offset = 0;

        FlowContext context = getContext();
        List fragments = textFlow.getFragments();
        Font font = textFlow.getFont();
        int fragIndex = 0;
        int advance = 0;

        TextFragmentBox fragment;
        int levelInfo[] = (textFlow.getBidiInfo() == null) ? new int[] { -1 }
                : textFlow.getBidiInfo().levelInfo;

        String segment, segments[] = getSegments(textFlow.getText(), levelInfo);
        FlowBorder border = null;
        if (textFlow.getBorder() instanceof FlowBorder)
            border = (FlowBorder) textFlow.getBorder();

        SegmentLookahead lookahead = new SegmentLookahead(segments,
                border == null ? 0 : border.getRightMargin());
        int seg;

        if (border != null) {
            fragment = getFragment(fragIndex++, fragments);
            fragment.setBidiLevel(levelInfo[0]);
            fragment.setTruncated(false);
            fragment.offset = fragment.length = -1;
            fragment.setWidth(border.getLeftMargin()
                    + border.getInsets(textFlow).left);
            if (context.getRemainingLineWidth() < fragment.getWidth()
                    + lookahead.getWidth())
                context.endLine();
            context.addToCurrentLine(fragment);
        }

        FlowUtilities flowUtilities = textFlow.getFlowUtilities();
        for (seg = 0; seg < segments.length; seg++) {
            segment = segments[seg];
            lookahead.setIndex(seg);

            do {
                fragment = getFragment(fragIndex++, fragments);

                fragment.offset = offset;
                fragment.setBidiLevel(levelInfo[seg * 2]);

                advance = flowUtilities.wrapFragmentInContext(fragment,
                        segment, context, lookahead, font, wrappingStyle);
                segment = segment.substring(advance);
                offset += advance;
                if ((segment.length() > 0 || fragment.length < advance)
                        || fragment.isTruncated())
                    context.endLine();
            } while (segment.length() > 0
                    || (!fragment.isTruncated() && fragment.length < advance));
        }

        if (border != null) {
            fragment = getFragment(fragIndex++, fragments);
            fragment.setBidiLevel(levelInfo[0]);
            fragment.setTruncated(false);
            fragment.offset = fragment.length = -1;
            fragment.setWidth(border.getRightMargin()
                    + border.getInsets(textFlow).right);
            context.addToCurrentLine(fragment);
        }

        // Remove the remaining unused fragments.
        while (fragIndex < fragments.size())
            fragments.remove(fragments.size() - 1);
    }

}
