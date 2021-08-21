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
 * @author hudsonr
 * @since 2.1
 */
public class SimpleTextLayout extends TextLayout {

    private static final String[] DELIMITERS = { "\r\n", //$NON-NLS-1$
            "\n", //$NON-NLS-1$
            "\r" };//$NON-NLS-1$

    private static int result;
    private static int delimeterLength;

    /**
     * Creates a new SimpleTextLayout with the given TextFlow
     * 
     * @param flow
     *            the TextFlow
     */
    public SimpleTextLayout(TextFlow flow) {
        super(flow);
    }

    /**
     * @see org.eclipse.draw2d.text.FlowFigureLayout#layout()
     */
    @SuppressWarnings("rawtypes")
    @Override
    protected void layout() {
        TextFlow textFlow = (TextFlow) getFlowFigure();
        String text = textFlow.getText();
        List fragments = textFlow.getFragments();
        Font font = textFlow.getFont();
        TextFragmentBox fragment;
        int i = 0;
        int offset = 0;
        FlowUtilities flowUtilities = textFlow.getFlowUtilities();

        do {
            nextLineBreak(text, offset);
            fragment = getFragment(i++, fragments);
            fragment.length = result - offset;
            fragment.offset = offset;
            fragment.setWidth(-1);
            flowUtilities.setupFragment(fragment, font,
                    text.substring(offset, result));
            getContext().addToCurrentLine(fragment);
            getContext().endLine();
            offset = result + delimeterLength;
        } while (offset < text.length());
        // Remove the remaining unused fragments.
        while (i < fragments.size())
            fragments.remove(i++);
    }

    private int nextLineBreak(String text, int offset) {
        result = text.length();
        delimeterLength = 0;
        int current;
        for (int i = 0; i < DELIMITERS.length; i++) {
            current = text.indexOf(DELIMITERS[i], offset);
            if (current != -1 && current < result) {
                result = current;
                delimeterLength = DELIMITERS[i].length();
            }
        }
        return result;
    }

}
