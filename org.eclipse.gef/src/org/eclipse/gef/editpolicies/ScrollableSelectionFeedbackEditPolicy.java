/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Research Group Software Construction,
 *     RWTH Aachen University, Germany - initial API and implementation
 */
package org.eclipse.gef.editpolicies;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.Assert;

import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.GhostImageFigure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.IScrollableFigure;
import org.eclipse.draw2d.LayoutListener;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.ViewportUtilities;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.IScrollableEditPart;
import org.eclipse.gef.util.EditPartUtilities;

/**
 * A {@link SelectionEditPolicy}, which may be registered to an
 * {@link IScrollableEditPart} to provide primary selection feedback by
 * rendering the hidden contents of the host figure's {@link ScrollPane}'s
 * nested {@link Viewport} by means of {@link GhostImageFigure}s.
 * 
 * @author Alexander Nyssen
 * @author Philip Ritzkopf
 * 
 * @since 3.6
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ScrollableSelectionFeedbackEditPolicy extends SelectionEditPolicy {

    private int feedbackAlpha = 100;

    private final List feedbackFigures = new ArrayList();

    private final FigureListener figureListener = new FigureListener() {

        @Override
        public void figureMoved(IFigure source) {
            // react on host figure move
            if (getHost().getSelected() == EditPart.SELECTED_PRIMARY) {
                updateFeedback();
            }
        }
    };

    private final LayoutListener layoutListener = new LayoutListener.Stub() {

        @Override
        public void postLayout(IFigure container) {
            // react to host figure layout changes
            if (getHost().getSelected() == EditPart.SELECTED_PRIMARY) {
                updateFeedback();
            }
        };
    };

    private final PropertyChangeListener viewportViewLocationChangeListener = new PropertyChangeListener() {

        @Override
        public void propertyChange(PropertyChangeEvent event) {
            // Make sure the host edit part is always selected as primary
            // selection, when it fires a property change event from its
            // viewport
            if (event.getSource() == ((IScrollableFigure) getHostFigure())
                    .getScrollPane().getViewport()
                    && getHost().getSelected() != EditPart.SELECTED_PRIMARY) {
                getHost().getViewer().deselectAll();
                getHost().getViewer().select(getHost());
            }
            // update feedback in case the viewport's view location changed
            if (event.getPropertyName().equals(Viewport.PROPERTY_VIEW_LOCATION)) {
                updateFeedback();
            }
        }
    };

    /**
     * @see org.eclipse.gef.editpolicies.SelectionEditPolicy#activate()
     */
    @Override
    public void activate() {
        super.activate();
        // register listeners to all viewports in the host figure's path;
        // listeners
        // to the host figure itself will be registered within showFeedback()
        // and
        // unregistered within hideFeedback()
        for (Iterator iterator = ViewportUtilities.getViewportsPath(
                getHostFigureViewport(),
                ViewportUtilities.getRootViewport(getHostFigure())).iterator(); iterator
                .hasNext();) {
            Viewport viewport = (Viewport) iterator.next();
            viewport.addPropertyChangeListener(viewportViewLocationChangeListener);
        }
    }

    /**
     * Adds a given feedback figure to the feedback layer (using the provided
     * bounds to layout it) and registers it in the local
     * {@link #feedbackFigures} list.
     * 
     * @param feedbackFigure
     *            the feedback figure to add to the feedback layer
     * @param feedbackFigureAbsoluteBounds
     *            the absolute bounds used to layout the feedback figure
     */
    protected void addFeedbackFigure(IFigure feedbackFigure,
            Rectangle feedbackFigureAbsoluteBounds) {
        getFeedbackLayer().translateToRelative(feedbackFigureAbsoluteBounds);
        getFeedbackLayer().translateFromParent(feedbackFigureAbsoluteBounds);
        feedbackFigure.setBounds(feedbackFigureAbsoluteBounds);
        feedbackFigure.validate();
        addFeedback(feedbackFigure);
        feedbackFigures.add(feedbackFigure);
    }

    /**
     * Creates a ghost image feedback figure for the given
     * {@link ConnectionEditPart}'s figure and adds it to the feedback layer.
     * 
     * @param connectionEditPart
     */
    protected void createConnectionFeedbackFigure(
            ConnectionEditPart connectionEditPart) {
        addFeedbackFigure(new GhostImageFigure(connectionEditPart.getFigure(),
                getAlpha(), getLayer(LayerConstants.CONNECTION_LAYER)
                        .getBackgroundColor().getRGB()),
                getAbsoluteBounds(connectionEditPart.getFigure()));
    }

    /**
     * Creates the connection layer feedback figures.
     */
    protected void createConnectionFeedbackFigures() {
        HashSet transitiveNestedConnections = EditPartUtilities
                .getAllNestedConnectionEditParts((GraphicalEditPart) getHost());

        for (Iterator iterator = transitiveNestedConnections.iterator(); iterator
                .hasNext();) {
            Object connection = iterator.next();
            if (connection instanceof ConnectionEditPart) {
                createConnectionFeedbackFigure((ConnectionEditPart) connection);
            }

        }
    }

    /**
     * Creates a ghost image feedback figure for the given
     * {@link GraphicalEditPart}'s figure and adds it to the feedback layer.
     * 
     * @param childEditPart
     */
    protected void createNodeFeedbackFigure(GraphicalEditPart childEditPart) {
        addFeedbackFigure(new GhostImageFigure(childEditPart.getFigure(),
                getAlpha(), null), getAbsoluteBounds(childEditPart.getFigure()));
    }

    /**
     * Creates the primary layer feedback figures.
     */
    protected void createNodeFeedbackFigures() {
        // create ghost feedback for node children
        for (Iterator iterator = getHost().getChildren().iterator(); iterator
                .hasNext();) {
            Object child = iterator.next();
            if (child instanceof GraphicalEditPart) {
                createNodeFeedbackFigure((GraphicalEditPart) child);
            }
        }
    }

    /**
     * @see org.eclipse.gef.editpolicies.SelectionEditPolicy#deactivate()
     */
    @Override
    public void deactivate() {
        // remove viewport listeners; listener to host figure, which were
        // registered during showSelection() will be unregistered during
        // hideSelection(), so they do not have to be unregistered here
        for (Iterator iterator = ViewportUtilities.getViewportsPath(
                getHostFigureViewport(),
                ViewportUtilities.getRootViewport(getHostFigure())).iterator(); iterator
                .hasNext();) {
            Viewport viewport = (Viewport) iterator.next();
            viewport.removePropertyChangeListener(viewportViewLocationChangeListener);

        }
        super.deactivate();
    }

    /**
     * Used to obtain the alpha value used for all feedback figures. The valid
     * range is the one documented for {@link Graphics#setAlpha(int)}.
     * 
     * @return the alpha
     */
    protected int getAlpha() {
        return feedbackAlpha;
    }

    /**
     * @see org.eclipse.gef.editpolicies.SelectionEditPolicy#getFeedbackLayer()
     */
    @Override
    protected IFigure getFeedbackLayer() {
        return getLayer(LayerConstants.SCALED_FEEDBACK_LAYER);
    }

    /**
     * Provides access to the host figure's {@link Viewport}.
     * 
     * @return the nested {@link Viewport} of the host figure's
     *         {@link ScrollPane}
     */
    protected Viewport getHostFigureViewport() {
        return ((IScrollableFigure) getHostFigure()).getScrollPane()
                .getViewport();
    }

    /**
     * Removes all feedback figures from the feedback layer as well as from the
     * {@link #feedbackFigures} list.
     */
    protected void hideFeedback() {
        for (Iterator iterator = feedbackFigures.iterator(); iterator.hasNext();) {
            removeFeedback((IFigure) iterator.next());
        }
        feedbackFigures.clear();
    }

    /**
     * @see org.eclipse.gef.editpolicies.SelectionEditPolicy#hideSelection()
     */
    @Override
    protected void hideSelection() {
        // remove figure and layout listeners
        getHostFigure().removeLayoutListener(layoutListener);
        getHostFigure().removeFigureListener(figureListener);
        // hide any still active feedback
        hideFeedback();
    }

    /**
     * Used to specify the alpha value used for all feedback figures. The valid
     * range is the one documented for {@link Graphics#setAlpha(int)}.
     * 
     * @param alpha
     */
    public void setAlpha(int alpha) {
        this.feedbackAlpha = alpha;
    }

    /**
     * @see org.eclipse.gef.editpolicies.AbstractEditPolicy#setHost(EditPart)
     */
    @Override
    public void setHost(EditPart host) {
        Assert.isLegal(host instanceof IScrollableEditPart);
        super.setHost(host);
    }

    /**
     * Creates feedback figures for all node figures nested within the host
     * figure's viewport, as well as for all incoming and outgoing connections
     * of these nodes. Feedback figures are only created in case there are
     * children or connections, which are not fully visible.
     */
    protected void showFeedback() {
        // ensure primary and connection layer are revalidated,
        // so the bounds of all their child figures, which are
        // used to calculate the feedback figure constraints,
        // are valid
        getLayer(LayerConstants.PRIMARY_LAYER).validate();
        getLayer(LayerConstants.CONNECTION_LAYER).validate();

        // check if there is a node child exceeding the client are
        Rectangle clientArea = getAbsoluteClientArea(getHostFigure());
        boolean primaryLayerChildExceedsViewport = !clientArea
                .equals(getAbsoluteViewportArea(((IScrollableFigure) getHostFigure())
                        .getScrollPane().getViewport()));
        // check if there is a connection exceeding the client area
        boolean connectionLayerChildExceedsClientArea = false;
        List connectionLayerChildren = getLayer(LayerConstants.CONNECTION_LAYER)
                .getChildren();
        for (Iterator iterator = connectionLayerChildren.iterator(); iterator
                .hasNext() && !connectionLayerChildExceedsClientArea;) {
            IFigure connectionLayerChild = (IFigure) iterator.next();
            connectionLayerChildExceedsClientArea = (ViewportUtilities
                    .getNearestEnclosingViewport(connectionLayerChild) == ((IScrollableFigure) getHostFigure())
                    .getScrollPane().getViewport() && !clientArea.getExpanded(
                    new Insets(1, 1, 1, 1)).contains(
                    getAbsoluteBounds(connectionLayerChild)));
        }

        // Only show feedback if there is a child or connection figure whose
        // bounds exceed the client area
        if (primaryLayerChildExceedsViewport
                || connectionLayerChildExceedsClientArea) {
            createNodeFeedbackFigures();
            createConnectionFeedbackFigures();
        }
    }

    /**
     * @see org.eclipse.gef.editpolicies.SelectionEditPolicy#showSelection()
     */
    @Override
    protected void showSelection() {
        // force ViewportExposeHelper to perform auto scrolling before
        // showing the feedback.
        getHost().getViewer().reveal(getHost());
        updateFeedback();
        // register figure and layout listeners needed to update the feedback
        // figures upon changes to the host figure.
        getHostFigure().addLayoutListener(layoutListener);
        getHostFigure().addFigureListener(figureListener);
    }

    /**
     * Removes any existing feedback figures by delegating to
     * {@link #hideFeedback()}. In case the host edit part is the primary
     * selection, recreates feedback figures via {@link #showFeedback()}.
     */
    protected void updateFeedback() {
        hideFeedback();
        if (getHost().getSelected() == EditPart.SELECTED_PRIMARY) {
            showFeedback();
        }
    }

    private static Rectangle getAbsoluteBounds(IFigure figure) {
        Rectangle bounds = figure.getBounds().getCopy();
        figure.translateToAbsolute(bounds);
        return bounds;
    }

    private static Rectangle getAbsoluteClientArea(IFigure figure) {
        Rectangle clientArea = figure.getClientArea().getCopy();
        figure.translateToParent(clientArea);
        figure.translateToAbsolute(clientArea);
        return clientArea;
    }

    private static Rectangle getAbsoluteViewportArea(Viewport viewport) {
        Rectangle viewportParentBounds = viewport.getParent().getBounds()
                .getCopy();

        int widthMax = viewport.getHorizontalRangeModel().getMaximum();
        int widthMin = viewport.getHorizontalRangeModel().getMinimum();
        int heightMax = viewport.getVerticalRangeModel().getMaximum();
        int heightMin = viewport.getVerticalRangeModel().getMinimum();

        viewportParentBounds
                .setSize(widthMax - widthMin, heightMax - heightMin);
        viewportParentBounds.translate(widthMin, heightMin);
        viewportParentBounds.translate(viewport.getViewLocation().getNegated());
        viewport.getParent().translateToAbsolute(viewportParentBounds);
        return viewportParentBounds;
    }
}