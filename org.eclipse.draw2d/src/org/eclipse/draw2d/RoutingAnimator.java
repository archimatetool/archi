/*******************************************************************************
 * Copyright (c) 2005, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.draw2d;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;

/**
 * Animates the routing of a connection. The animator will capture the effects
 * of the connection's router, and the play back the placement of the routing,
 * interpolating the intermediate routes.
 * <P>
 * To use a routing animator, hook it as a routing listener for the connection
 * whose points are to be animated, by calling
 * {@link PolylineConnection#addRoutingListener(RoutingListener)}. An animator
 * is active only when the Animation utility is activated.
 * 
 * @since 3.2
 */
public class RoutingAnimator extends Animator implements RoutingListener {

    static final RoutingAnimator INSTANCE = new RoutingAnimator();

    /**
     * Constructs a routing animator for use with one or more connections. The
     * default instance ({@link #getDefault()} can be used on any number of
     * connections.
     * 
     * @since 3.2
     */
    protected RoutingAnimator() {
    }

    /**
     * Overridden to sync initial and final states.
     * 
     * @see Animator#playbackStarting(IFigure)
     */
    @Override
    public void playbackStarting(IFigure connection) {
        reconcileStates((Connection) connection);
    }

    /**
     * Returns the current state of the connection. Currently, this is a copy of
     * the list of points. However this Object could change in future releases
     * and should not be considered API.
     * 
     * @see Animator#getCurrentState(IFigure)
     */
    @Override
    protected Object getCurrentState(IFigure connection) {
        return ((Connection) connection).getPoints().getCopy();
    }

    /**
     * Returns the default instance.
     * 
     * @return the default instance
     * @since 3.2
     */
    public static RoutingAnimator getDefault() {
        return INSTANCE;
    }

    /**
     * Hooks invalidate for animation purposes.
     * 
     * @see RoutingListener#invalidate(Connection)
     */
    @Override
    public final void invalidate(Connection conn) {
        if (Animation.isInitialRecording())
            Animation.hookAnimator(conn, this);
    }

    /**
     * Plays back the interpolated state.
     * 
     * @see Animator#playback(IFigure)
     */
    @Override
    protected boolean playback(IFigure figure) {
        Connection conn = (Connection) figure;

        PointList list1 = (PointList) Animation.getInitialState(this, conn);
        PointList list2 = (PointList) Animation.getFinalState(this, conn);
        if (list1 == null) {
            conn.setVisible(false);
            return true;
        }

        float progress = Animation.getProgress();
        if (list1.size() == list2.size()) {
            Point pt1 = new Point(), pt2 = new Point();
            PointList points = conn.getPoints();
            points.removeAllPoints();
            for (int i = 0; i < list1.size(); i++) {
                list1.getPoint(pt2, i);
                list2.getPoint(pt1, i);
                pt1.x = Math.round(pt1.x * progress + (1 - progress) * pt2.x);
                pt1.y = Math.round(pt1.y * progress + (1 - progress) * pt2.y);
                points.addPoint(pt1);
            }
            conn.setPoints(points);
        }
        return true;
    }

    /**
     * Hooks post routing for animation purposes.
     * 
     * @see RoutingListener#postRoute(Connection)
     */
    @Override
    public final void postRoute(Connection connection) {
        if (Animation.isFinalRecording())
            Animation.hookNeedsCapture(connection, this);
    }

    private void reconcileStates(Connection conn) {
        PointList points1 = (PointList) Animation.getInitialState(this, conn);
        PointList points2 = (PointList) Animation.getFinalState(this, conn);

        if (points1 != null && points1.size() != points2.size()) {
            Point p = new Point(), q = new Point();

            int size1 = points1.size() - 1;
            int size2 = points2.size() - 1;

            int i1 = size1;
            int i2 = size2;

            double current1 = 1.0;
            double current2 = 1.0;

            double prev1 = 1.0;
            double prev2 = 1.0;

            while (i1 > 0 || i2 > 0) {
                if (Math.abs(current1 - current2) < 0.1 && i1 > 0 && i2 > 0) {
                    // Both points are the same, use them and go on;
                    prev1 = current1;
                    prev2 = current2;
                    i1--;
                    i2--;
                    current1 = (double) i1 / size1;
                    current2 = (double) i2 / size2;
                } else if (current1 < current2) {
                    // 2 needs to catch up
                    // current1 < current2 < prev1
                    points1.getPoint(p, i1);
                    points1.getPoint(q, i1 + 1);

                    p.x = (int) (((q.x * (current2 - current1) + p.x
                            * (prev1 - current2)) / (prev1 - current1)));
                    p.y = (int) (((q.y * (current2 - current1) + p.y
                            * (prev1 - current2)) / (prev1 - current1)));

                    points1.insertPoint(p, i1 + 1);

                    prev1 = prev2 = current2;
                    i2--;
                    current2 = (double) i2 / size2;

                } else {
                    // 1 needs to catch up
                    // current2< current1 < prev2

                    points2.getPoint(p, i2);
                    points2.getPoint(q, i2 + 1);

                    p.x = (int) (((q.x * (current1 - current2) + p.x
                            * (prev2 - current1)) / (prev2 - current2)));
                    p.y = (int) (((q.y * (current1 - current2) + p.y
                            * (prev2 - current1)) / (prev2 - current2)));

                    points2.insertPoint(p, i2 + 1);

                    prev2 = prev1 = current1;
                    i1--;
                    current1 = (double) i1 / size1;
                }
            }
        }
    }

    /**
     * This callback is unused. Reserved for possible future use.
     * 
     * @see RoutingListener#remove(Connection)
     */
    @Override
    public final void remove(Connection connection) {
    }

    /**
     * Hooks route to intercept routing during animation playback.
     * 
     * @see RoutingListener#route(Connection)
     */
    @Override
    public final boolean route(Connection conn) {
        return Animation.isAnimating() && Animation.hookPlayback(conn, this);
    }

    /**
     * This callback is unused. Reserved for possible future use.
     * 
     * @see RoutingListener#setConstraint(Connection, Object)
     */
    @Override
    public final void setConstraint(Connection connection, Object constraint) {
    }

}
