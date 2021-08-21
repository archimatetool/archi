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

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * An implementation of {@link Connection} based on Polyline. PolylineConnection
 * adds the following additional features:
 * <UL>
 * <LI>
 * A {@link ConnectionRouter} may be provided which will be used to determine
 * the connections points.
 * <LI>
 * Children may be added. The bounds calculation is extended such that the
 * bounds is the smallest Rectangle which is large enough to display the
 * Polyline and all of its children figures.
 * <LI>
 * A {@link DelegatingLayout} is set as the default layout. A delegating layout
 * allows children to position themselves via {@link Locator Locators}.
 * </UL>
 * <P>
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class PolylineConnection extends Polyline implements Connection,
        AnchorListener {

    private ConnectionAnchor startAnchor, endAnchor;
    private ConnectionRouter connectionRouter = ConnectionRouter.NULL;
    private RotatableDecoration startArrow, endArrow;

    {
        setLayoutManager(new DelegatingLayout());
        addPoint(new Point(0, 0));
        addPoint(new Point(100, 100));
    }

    /**
     * Hooks the source and target anchors.
     * 
     * @see Figure#addNotify()
     */
    @Override
    public void addNotify() {
        super.addNotify();
        hookSourceAnchor();
        hookTargetAnchor();
    }

    /**
     * Appends the given routing listener to the list of listeners.
     * 
     * @param listener
     *            the routing listener
     * @since 3.2
     */
    public void addRoutingListener(RoutingListener listener) {
        if (connectionRouter instanceof RoutingNotifier) {
            RoutingNotifier notifier = (RoutingNotifier) connectionRouter;
            notifier.listeners.add(listener);
        } else
            connectionRouter = new RoutingNotifier(connectionRouter, listener);
    }

    /**
     * Called by the anchors of this connection when they have moved,
     * revalidating this polyline connection.
     * 
     * @param anchor
     *            the anchor that moved
     */
    @Override
    public void anchorMoved(ConnectionAnchor anchor) {
        revalidate();
    }

    /**
     * Returns the bounds which holds all the points in this polyline
     * connection. Returns any previously existing bounds, else calculates by
     * unioning all the children's dimensions.
     * 
     * @return the bounds
     */
    @Override
    public Rectangle getBounds() {
        if (bounds == null) {
            super.getBounds();
            for (int i = 0; i < getChildren().size(); i++) {
                IFigure child = (IFigure) getChildren().get(i);
                bounds.union(child.getBounds());
            }
        }
        return bounds;
    }

    /**
     * Returns the <code>ConnectionRouter</code> used to layout this connection.
     * Will not return <code>null</code>.
     * 
     * @return this connection's router
     */
    @Override
    public ConnectionRouter getConnectionRouter() {
        if (connectionRouter instanceof RoutingNotifier)
            return ((RoutingNotifier) connectionRouter).realRouter;
        return connectionRouter;
    }

    /**
     * Returns this connection's routing constraint from its connection router.
     * May return <code>null</code>.
     * 
     * @return the connection's routing constraint
     */
    @Override
    public Object getRoutingConstraint() {
        if (getConnectionRouter() != null)
            return getConnectionRouter().getConstraint(this);
        else
            return null;
    }

    /**
     * @return the anchor at the start of this polyline connection (may be null)
     */
    @Override
    public ConnectionAnchor getSourceAnchor() {
        return startAnchor;
    }

    /**
     * @return the source decoration (may be null)
     */
    protected RotatableDecoration getSourceDecoration() {
        return startArrow;
    }

    /**
     * @return the anchor at the end of this polyline connection (may be null)
     */
    @Override
    public ConnectionAnchor getTargetAnchor() {
        return endAnchor;
    }

    /**
     * @return the target decoration (may be null)
     * 
     * @since 2.0
     */
    protected RotatableDecoration getTargetDecoration() {
        return endArrow;
    }

    private void hookSourceAnchor() {
        if (getSourceAnchor() != null)
            getSourceAnchor().addAnchorListener(this);
    }

    private void hookTargetAnchor() {
        if (getTargetAnchor() != null)
            getTargetAnchor().addAnchorListener(this);
    }

    /**
     * Layouts this polyline. If the start and end anchors are present, the
     * connection router is used to route this, after which it is laid out. It
     * also fires a moved method.
     */
    @Override
    public void layout() {
        if (getSourceAnchor() != null && getTargetAnchor() != null)
            connectionRouter.route(this);

        Rectangle oldBounds = bounds;
        super.layout();
        bounds = null;

        if (!getBounds().contains(oldBounds)) {
            getParent().translateToParent(oldBounds);
            getUpdateManager().addDirtyRegion(getParent(), oldBounds);
        }

        repaint();
        fireFigureMoved();
    }

    /**
     * Called just before the receiver is being removed from its parent. Results
     * in removing itself from the connection router.
     * 
     * @since 2.0
     */
    @Override
    public void removeNotify() {
        unhookSourceAnchor();
        unhookTargetAnchor();
        connectionRouter.remove(this);
        super.removeNotify();
    }

    /**
     * Removes the first occurence of the given listener.
     * 
     * @param listener
     *            the listener being removed
     * @since 3.2
     */
    public void removeRoutingListener(RoutingListener listener) {
        if (connectionRouter instanceof RoutingNotifier) {
            RoutingNotifier notifier = (RoutingNotifier) connectionRouter;
            notifier.listeners.remove(listener);
            if (notifier.listeners.isEmpty())
                connectionRouter = notifier.realRouter;
        }
    }

    /**
     * @see IFigure#revalidate()
     */
    @Override
    public void revalidate() {
        super.revalidate();
        connectionRouter.invalidate(this);
    }

    /**
     * Sets the connection router which handles the layout of this polyline.
     * Generally set by the parent handling the polyline connection.
     * 
     * @param cr
     *            the connection router
     */
    @Override
    public void setConnectionRouter(ConnectionRouter cr) {
        if (cr == null)
            cr = ConnectionRouter.NULL;
        ConnectionRouter oldRouter = getConnectionRouter();
        if (oldRouter != cr) {
            connectionRouter.remove(this);
            if (connectionRouter instanceof RoutingNotifier)
                ((RoutingNotifier) connectionRouter).realRouter = cr;
            else
                connectionRouter = cr;
            firePropertyChange(Connection.PROPERTY_CONNECTION_ROUTER,
                    oldRouter, cr);
            revalidate();
        }
    }

    /**
     * Sets the routing constraint for this connection.
     * 
     * @param cons
     *            the constraint
     */
    @Override
    public void setRoutingConstraint(Object cons) {
        if (connectionRouter != null)
            connectionRouter.setConstraint(this, cons);
        revalidate();
    }

    /**
     * Sets the anchor to be used at the start of this polyline connection.
     * 
     * @param anchor
     *            the new source anchor
     */
    @Override
    public void setSourceAnchor(ConnectionAnchor anchor) {
        if (anchor == startAnchor)
            return;
        unhookSourceAnchor();
        // No longer needed, revalidate does this.
        // getConnectionRouter().invalidate(this);
        startAnchor = anchor;
        if (getParent() != null)
            hookSourceAnchor();
        revalidate();
    }

    /**
     * Sets the decoration to be used at the start of the {@link Connection}.
     * 
     * @param dec
     *            the new source decoration
     * @since 2.0
     */
    public void setSourceDecoration(RotatableDecoration dec) {
        if (startArrow == dec)
            return;
        if (startArrow != null)
            remove(startArrow);
        startArrow = dec;
        if (startArrow != null)
            add(startArrow, new ArrowLocator(this, ConnectionLocator.SOURCE));
    }

    /**
     * Sets the anchor to be used at the end of the polyline connection. Removes
     * this listener from the old anchor and adds it to the new anchor.
     * 
     * @param anchor
     *            the new target anchor
     */
    @Override
    public void setTargetAnchor(ConnectionAnchor anchor) {
        if (anchor == endAnchor)
            return;
        unhookTargetAnchor();
        // No longer needed, revalidate does this.
        // getConnectionRouter().invalidate(this);
        endAnchor = anchor;
        if (getParent() != null)
            hookTargetAnchor();
        revalidate();
    }

    /**
     * Sets the decoration to be used at the end of the {@link Connection}.
     * 
     * @param dec
     *            the new target decoration
     */
    public void setTargetDecoration(RotatableDecoration dec) {
        if (endArrow == dec)
            return;
        if (endArrow != null)
            remove(endArrow);
        endArrow = dec;
        if (endArrow != null)
            add(endArrow, new ArrowLocator(this, ConnectionLocator.TARGET));
    }

    private void unhookSourceAnchor() {
        if (getSourceAnchor() != null)
            getSourceAnchor().removeAnchorListener(this);
    }

    private void unhookTargetAnchor() {
        if (getTargetAnchor() != null)
            getTargetAnchor().removeAnchorListener(this);
    }

    final class RoutingNotifier implements ConnectionRouter {

        ConnectionRouter realRouter;
        List listeners = new ArrayList(1);

        RoutingNotifier(ConnectionRouter router, RoutingListener listener) {
            realRouter = router;
            listeners.add(listener);
        }

        @Override
        public Object getConstraint(Connection connection) {
            return realRouter.getConstraint(connection);
        }

        @Override
        public void invalidate(Connection connection) {
            for (int i = 0; i < listeners.size(); i++)
                ((RoutingListener) listeners.get(i)).invalidate(connection);

            realRouter.invalidate(connection);
        }

        @Override
        public void route(Connection connection) {
            boolean consumed = false;
            for (int i = 0; i < listeners.size(); i++)
                consumed |= ((RoutingListener) listeners.get(i))
                        .route(connection);

            if (!consumed)
                realRouter.route(connection);

            for (int i = 0; i < listeners.size(); i++)
                ((RoutingListener) listeners.get(i)).postRoute(connection);
        }

        @Override
        public void remove(Connection connection) {
            for (int i = 0; i < listeners.size(); i++)
                ((RoutingListener) listeners.get(i)).remove(connection);
            realRouter.remove(connection);
        }

        @Override
        public void setConstraint(Connection connection, Object constraint) {
            for (int i = 0; i < listeners.size(); i++)
                ((RoutingListener) listeners.get(i)).setConstraint(connection,
                        constraint);
            realRouter.setConstraint(connection, constraint);
        }

    }

}
