/*******************************************************************************
 * Copyright (c) 2004, 2010 IBM Corporation and others.
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
 * Classes which implement this interface provide callback hooks for various
 * layout related events.
 * <P>
 * Instances can be hooked to figures by calling
 * {@link IFigure#addLayoutListener(LayoutListener)}. Listeners will be made
 * aware of various steps of the layout mechanism, and even have the opportunity
 * to prevent normal layout from occurring.
 * 
 * @since 3.1
 */
public interface LayoutListener {

    /**
     * A stub implementation which implements all of the declared methods.
     * 
     * @since 3.1
     */
    class Stub implements LayoutListener {

        /**
         * Stub which does nothing.
         * 
         * @see LayoutListener#invalidate(IFigure)
         */
        @Override
        public void invalidate(IFigure container) {
        }

        /**
         * Stub which does nothing.
         * 
         * @see LayoutListener#layout(IFigure)
         */
        @Override
        public boolean layout(IFigure container) {
            return false;
        }

        /**
         * Stub which does nothing.
         * 
         * @see LayoutListener#postLayout(IFigure)
         */
        @Override
        public void postLayout(IFigure container) {
        }

        /**
         * Stub which does nothing.
         * 
         * @see LayoutListener#remove(IFigure)
         */
        @Override
        public void remove(IFigure child) {
        }

        /**
         * Stub which does nothing.
         * 
         * @see LayoutListener#setConstraint(IFigure, java.lang.Object)
         */
        @Override
        public void setConstraint(IFigure child, Object constraint) {
        }

    }

    /**
     * Called when a container has been invalidated.
     * 
     * @param container
     *            the invalidated Figure
     * @since 3.1
     */
    void invalidate(IFigure container);

    /**
     * Called prior to layout occurring. A listener may intercept a layout by
     * returning <code>true</code>. If the layout is intercepted, the
     * container's <code>LayoutManager</code> will not receive a layout call.
     * 
     * @param container
     *            the figure incurring a layout
     * @return <code>true</code> if the layout has been intercepted by the
     *         listener
     * @since 3.1
     */
    boolean layout(IFigure container);

    /**
     * Called after layout has occurred.
     * 
     * @since 3.1
     * @param container
     *            the figure incurring a layout
     */
    void postLayout(IFigure container);

    /**
     * Called when a child is about to be removed from its parent.
     * 
     * @since 3.1
     * @param child
     *            the child being removed
     */
    void remove(IFigure child);

    /**
     * Called when a child's constraint is initialized or updated.
     * 
     * @param child
     *            the child being updated
     * @param constraint
     *            the child's new constraint
     * @since 3.1
     */
    void setConstraint(IFigure child, Object constraint);

}
