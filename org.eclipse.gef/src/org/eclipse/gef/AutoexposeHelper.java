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
package org.eclipse.gef;

import java.util.Collection;

import org.eclipse.draw2d.geometry.Point;

/**
 * A helper returned from a {@link org.eclipse.gef.GraphicalEditPart}. Certain
 * <code>DragTrackers</code> tools and native drop listeners will make use of
 * autoexpose helpers to reveal any potential drop areas that are currently not
 * visible to the user. An example of this is scrolling a container to reveal
 * unexposed area. Another example is a bunch of stacked containers in a
 * "tab folder" arrangement, whever hovering over a tab should switch which
 * container is on top.
 * <P>
 * Autoexpose helpers are obtained from editparts that are target of whatever
 * operation is being performed. If the target provides no helper, its parent
 * chain is traversed looking for helpers. A helper will be obtained under
 * conditions deemed appropriate by the caller, such as when the mouse has
 * paused for some amount of time in the current location.
 * <P>
 * An autoexpose helper may be short-lived or long-running. A short-lived helper
 * would be something like the example described above when a "page" is being
 * flipped. A long-running example is auto-scrolling. A helper requests to
 * remains active by returning <code>true</code> from its {@link #step(Point)}
 * method for as long as necessary. An active helper can remain active even as
 * the mouse is moving. The client may stop calling <code>step(Point)</code> at
 * any time, even if <code>false</code> was never returned, such as when the
 * user releases the mouse.
 * 
 * @author hudsonr
 */
public interface AutoexposeHelper {

    /**
     * Returns <code>true</code> if the specified location is interesting to the
     * helper. This method gets called as part of the search for an
     * AutoexposeHelper. The helper should do something if it returns
     * <code>true</code>, or it may wait for {@link #step(Point)} to be called
     * later.
     * 
     * @param where
     *            the mouse's current location in the viewer
     * @return <code>true</code> if the location is interesting
     */
    boolean detect(Point where);

    /**
     * Performs the autoexpose and returns a hint indicating that the helper
     * would like to remain active. The client will continue to call step() for
     * as long as it previously returned <code>true</code>, and the conditions
     * are deemed appropriate to continue the autoexpose process.
     * <P>
     * The client may stop calling this method at any time, even if the previous
     * invocation returned <code>true</code>. The return value is a hint.
     * 
     * @param where
     *            the current location of the mouse in the viewer
     * @return a hint indicating whether this helper should continue to be
     *         invoked
     */
    boolean step(Point where);

    /**
     * Used with EditPartViewers to find the AutoexposeHelper at a Point.
     * Clients can instantiate the search, call
     * {@link EditPartViewer#findObjectAtExcluding(Point,Collection, EditPartViewer.Conditional)}
     * , and then check the {@link #result} field.
     */
    class Search implements EditPartViewer.Conditional {
        /**
         * Constructs a new Search at a point on the viewer.
         * 
         * @param pt
         *            the mouse location
         */
        public Search(Point pt) {
            where = pt;
        }

        /**
         * the result of the search.
         */
        private Point where;
        public AutoexposeHelper result;

        @Override
        public boolean evaluate(EditPart editpart) {
            result = editpart
                    .getAdapter(AutoexposeHelper.class);
            if (result != null && result.detect(where))
                return true;
            result = null;
            return false;
        }
    }

}
