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

import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Clipping strategy for connection layer, which takes into account nested view
 * ports and truncates those parts of connections which reach outside and are
 * thus not visible.
 * 
 * @author Alexander Nyssen
 * @author Philip Ritzkopf
 * 
 * @since 3.6
 */
@SuppressWarnings("rawtypes")
public class ViewportAwareConnectionLayerClippingStrategy implements
        IClippingStrategy {

    private static final Insets PRIVATE_INSETS = new Insets(0, 0, 1, 1);

    private ConnectionLayer connectionLayer = null;

    public ViewportAwareConnectionLayerClippingStrategy(
            ConnectionLayer connectionLayer) {
        this.connectionLayer = connectionLayer;
    }

    /**
     * @see org.eclipse.draw2d.IClippingStrategy#getClip(org.eclipse.draw2d.IFigure)
     */
    @Override
    public Rectangle[] getClip(IFigure figure) {
        Rectangle[] clipRect = null;
        if (figure instanceof Connection) {
            clipRect = getEdgeClippingRectangle((Connection) figure);
        } else {
            clipRect = new Rectangle[] { getNodeClippingRectangle(figure) };
        }
        // translate clipping rectangles (which are in absolute coordinates)
        // to be relative to the parent figure's (i.e. the connection
        // layer's) client area
        for (int i = 0; i < clipRect.length; i++) {
            figure.translateToRelative(clipRect[i]);
        }
        return clipRect;
    }

    /**
     * Computes clipping rectangle(s) for a given connection. Will consider all
     * enclosing viewports, excluding the root viewport.
     */
    protected Rectangle[] getEdgeClippingRectangle(Connection connection) {
        // start with clipping the connection at its original bounds
        Rectangle clipRect = getAbsoluteBoundsAsCopy(connection);

        // in case we cannot infer source and target of the connection (e.g.
        // if XYAnchors are used), returning the bounds is all we can do
        ConnectionAnchor sourceAnchor = connection.getSourceAnchor();
        ConnectionAnchor targetAnchor = connection.getTargetAnchor();
        if (sourceAnchor == null || sourceAnchor.getOwner() == null
                || targetAnchor == null || targetAnchor.getOwner() == null) {
            return new Rectangle[] { clipRect };
        }

        // source and target figure are known, see if there is common
        // viewport
        // the connection has to be clipped at.
        IFigure sourceFigure = sourceAnchor.getOwner();
        IFigure targetFigure = targetAnchor.getOwner();
        Viewport nearestEnclosingCommonViewport = ViewportUtilities
                .getNearestCommonViewport(sourceFigure, targetFigure);
        if (nearestEnclosingCommonViewport == null) {
            return new Rectangle[] { clipRect };
        }

        // if the nearest common viewport is not the root viewport, we may
        // start with clipping the connection at this viewport.
        if (nearestEnclosingCommonViewport != getRootViewport()) {
            clipRect.intersect(getNodeClippingRectangle(nearestEnclosingCommonViewport));
        }

        // if the nearest common viewport of source and target is not
        // simultaneously
        // the nearest enclosing viewport of source and target respectively, the
        // connection has to be further clipped (the connection may even not be
        // visible at all)
        Viewport nearestEnclosingSourceViewport = ViewportUtilities
                .getNearestEnclosingViewport(sourceFigure);
        Viewport nearestEnclosingTargetViewport = ViewportUtilities
                .getNearestEnclosingViewport(targetFigure);
        if (nearestEnclosingSourceViewport != nearestEnclosingTargetViewport) {
            // compute if source and target anchor are visible
            // within the nearest common enclosing viewport (which may
            // itself be nested in other viewports).
            Rectangle sourceClipRect = clipRect.getCopy();
            if (nearestEnclosingSourceViewport != nearestEnclosingCommonViewport) {
                clipAtViewports(sourceClipRect,
                        ViewportUtilities.getViewportsPath(
                                nearestEnclosingSourceViewport,
                                nearestEnclosingCommonViewport, false));
            }
            Rectangle targetClipRect = clipRect.getCopy();
            if (nearestEnclosingTargetViewport != nearestEnclosingCommonViewport) {
                clipAtViewports(targetClipRect,
                        ViewportUtilities.getViewportsPath(
                                nearestEnclosingTargetViewport,
                                nearestEnclosingCommonViewport, false));
            }
            PointList absolutePointsAsCopy = getAbsolutePointsAsCopy(connection);
            boolean sourceAnchorVisible = sourceClipRect.getExpanded(
                    PRIVATE_INSETS).contains(
                    absolutePointsAsCopy.getFirstPoint());
            boolean targetAnchorVisible = targetClipRect.getExpanded(
                    PRIVATE_INSETS).contains(
                    absolutePointsAsCopy.getLastPoint());

            if (!sourceAnchorVisible || !targetAnchorVisible) {
                // one (or both) of source or target anchor is invisible
                // within the nearest common viewport, so up to now
                // we regard the edge as invisible.
                return new Rectangle[] {};
                // TODO: We could come up with a more decent strategy here,
                // which also computes clipping fragments in those cases
                // where source/target are not visible but the edge
                // intersects with the enclosing source/target viewport's
                // parents bounds.

            } else {
                // both ends are visible, so just return what we have
                // computed before
                // (clipping at nearest enclosing viewport)
                return new Rectangle[] { clipRect };
            }
        } else {
            // source and target share the same enclosing viewport, so just
            // return what we have computed before (clipping at nearest
            // enclosing viewport)
            return new Rectangle[] { clipRect };
        }
    }

    /**
     * Computes clipping rectangle for a given (node) figure. Will consider all
     * enclosing viewports, excluding the root viewport.
     */
    protected Rectangle getNodeClippingRectangle(IFigure figure) {
        // start with the bounds of the edit part's figure
        Rectangle clipRect = getAbsoluteBoundsAsCopy(figure);

        // now traverse the viewport path of the figure (and reduce clipRect
        // to what is actually visible); process all viewports up to the
        // root viewport
        List enclosingViewportsPath = ViewportUtilities.getViewportsPath(
                ViewportUtilities.getNearestEnclosingViewport(figure),
                getRootViewport(), false);
        clipAtViewports(clipRect, enclosingViewportsPath);
        return clipRect;
    }

    /**
     * Clips the given clipRect at all given viewports.
     */
    protected void clipAtViewports(Rectangle clipRect,
            List enclosingViewportsPath) {
        for (Iterator iterator = enclosingViewportsPath.iterator(); iterator
                .hasNext();) {
            Viewport viewport = (Viewport) iterator.next();
            clipRect.intersect(getAbsoluteViewportAreaAsCopy(viewport));
        }
    }

    /**
     * Returns the root viewport, i.e. the nearest enclosing viewport of the
     * connection layer, which corresponds to the nearest enclosing common
     * viewport of primary and connection layer.
     */
    protected Viewport getRootViewport() {
        return ViewportUtilities.getNearestEnclosingViewport(connectionLayer);
    }

    /**
     * Returns the connection's points in absolute coordinates.
     */
    protected PointList getAbsolutePointsAsCopy(Connection connection) {
        PointList points = connection.getPoints().getCopy();
        connection.translateToAbsolute(points);
        return points;
    }

    /**
     * Returns the area covered by the viewport in absolute coordinates.
     */
    protected Rectangle getAbsoluteViewportAreaAsCopy(Viewport viewport) {
        return getAbsoluteClientAreaAsCopy(viewport);
    }

    /**
     * Returns the viewport's client area in absolute coordinates.
     */
    protected Rectangle getAbsoluteClientAreaAsCopy(IFigure figure) {
        Rectangle absoluteClientArea = figure.getClientArea();
        figure.translateToParent(absoluteClientArea);
        figure.translateToAbsolute(absoluteClientArea);
        return absoluteClientArea;
    }

    /**
     * Returns the figure's bounds in absolute coordinates.
     */
    protected Rectangle getAbsoluteBoundsAsCopy(IFigure figure) {
        Rectangle absoluteFigureBounds = figure.getBounds().getCopy();
        figure.translateToAbsolute(absoluteFigureBounds);
        return absoluteFigureBounds;
    }
}