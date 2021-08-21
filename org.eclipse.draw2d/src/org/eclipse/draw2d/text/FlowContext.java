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
 * The context that a {@link FlowFigureLayout} uses to perform its layout.
 * 
 * <P>
 * WARNING: This interface is not intended to be implemented by clients. It
 * exists to define the API between the layout and its context.
 */
public interface FlowContext {

    /**
     * Adds the given box into the current line.
     * 
     * @param box
     *            the FlowBox to add
     */
    void addToCurrentLine(FlowBox box);

    /**
     * Adds an entire line into the context. If there is a previous line, it is
     * ended.
     * 
     * @param box
     *            the line being added
     * @since 3.1
     */
    void addLine(CompositeBox box);

    /**
     * The current line should be committed if it is occupied, and then set to
     * <code>null</code>. Otherwise, do nothing.
     */
    void endLine();

    /**
     * This method can be used to query the amount of space left on the current
     * line. It can help determine where to wrap during layout.
     * 
     * @return the amount of space left on the current line
     * @since 3.1
     */
    int getRemainingLineWidth();

    /**
     * This method is used to convey layout state to different FlowFigures. This
     * state is cleared when a fragment is added to the current line and once
     * the layout is complete.
     * 
     * @return <code>true</code> if the next fragment should be placed on the
     *         current line
     * @since 3.1
     * @see #setContinueOnSameLine(boolean)
     */
    boolean getContinueOnSameLine();

    /**
     * This method looks ahead for line-breaks. When laying out, this method can
     * be used to determine the next line-break across multiple figures.
     * 
     * @param child
     *            the search will occur starting from the figure after the given
     *            child
     * @param width
     *            the width before the next line-break (if one's found; all the
     *            width, otherwise) will be added on to the first int in the
     *            given array
     * @since 3.1
     */
    void getWidthLookahead(FlowFigure child, int width[]);

    /**
     * @return <code>true</code> if the current line contains any fragments
     */
    boolean isCurrentLineOccupied();

    /**
     * This method is used to convey layout state to different FlowFigures. This
     * state is cleared when a fragment is added and once the layout is
     * complete.
     * 
     * @param value
     *            <code>true</code> indicates that the first fragment of the
     *            next TextFlow should be laid out on the current line, and not
     *            a new one
     * @since 3.1
     * @see #getContinueOnSameLine()
     */
    void setContinueOnSameLine(boolean value);

}
