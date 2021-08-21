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
package org.eclipse.gef.editparts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayeredPane;
import org.eclipse.draw2d.FreeformViewport;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.AutoexposeHelper;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.Request;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.tools.MarqueeDragTracker;

/**
 * A graphical root that uses {@link org.eclipse.draw2d.FreeformFigure
 * FreeformFigures} as the layers in the diagram. The
 * {@link EditPartViewer#setContents(EditPart) contents} editpart must provide a
 * FreeformFigure as its figure. Freeform figures are special because they can
 * expand in any direction. This allows the user to drag objects or bendpoints
 * into the negative X and Y coordinates of a diagram. If this feature is not
 * being used, clients should use the {@link FreeformGraphicalRootEditPart} as
 * their viewer's root editpart.
 * <P>
 * <EM>IMPORTANT:</EM> The contents editpart that is added to a freeform root
 * should have a <code>FreeformFigure</code> (such as FreeformLayer) as its
 * Figure. The primary layer is <EM>not</EM> using a draw2d LayoutManager, and
 * will not size the contents' figure properly unless it is a freeform figure.
 * <P>
 * <EM>IMPORTANT:</EM>The freeform root uses a <code>FreeformViewport</code> as
 * its primary figure. This class must be used with the
 * {@link org.eclipse.gef.ui.parts.ScrollingGraphicalViewer}. The viewport gets
 * installed into that viewer's {@link org.eclipse.draw2d.FigureCanvas}, which
 * provides native scrollbars for scrolling the viewport.
 * <P>
 * This root serves as the diagram's
 * {@link org.eclipse.gef.editparts.LayerManager}, providing the following layer
 * structure, in top-to-bottom order:
 * <table cellspacing="0" cellpadding="0">
 * <tr>
 * <td colspan="2">Root Freeform Layered Pane</td>
 * </tr>
 * <tr>
 * <td>&#9500;</td>
 * <td>&nbsp;Feedback Layer</td>
 * </tr>
 * <tr>
 * <td>&#9500;</td>
 * <td>&nbsp;Handle Layer</td>
 * </tr>
 * <tr>
 * <td>&#9492;</td>
 * <td>&nbsp;Printable Layers</td>
 * </tr>
 * <tr>
 * <td>&nbsp;</td>
 * <td>&#9500; Connection Layer</td>
 * </tr>
 * <tr>
 * <td>&nbsp;</td>
 * <td>&#9492; Primary Layer</td>
 * </tr>
 * </table>
 * 
 */
@SuppressWarnings("rawtypes")
public class FreeformGraphicalRootEditPart extends SimpleRootEditPart implements
        LayerConstants, LayerManager {

    private LayeredPane innerLayers;
    private LayeredPane printableLayers;
    private PropertyChangeListener gridListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String property = evt.getPropertyName();
            if (property.equals(SnapToGrid.PROPERTY_GRID_ORIGIN)
                    || property.equals(SnapToGrid.PROPERTY_GRID_SPACING)
                    || property.equals(SnapToGrid.PROPERTY_GRID_VISIBLE))
                refreshGridLayer();
        }
    };

    /**
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
     */
    @Override
    protected IFigure createFigure() {
        FreeformViewport viewport = new FreeformViewport();
        innerLayers = new FreeformLayeredPane();
        createLayers(innerLayers);
        viewport.setContents(innerLayers);
        return viewport;
    }

    /**
     * Creates a {@link GridLayer grid}. Sub-classes can override this method to
     * customize the appearance of the grid. The grid layer should be the first
     * layer (i.e., beneath the primary layer) if it is not to cover up parts on
     * the primary layer. In that case, the primary layer should be transparent
     * so that the grid is visible.
     * 
     * @return the newly created GridLayer
     */
    protected GridLayer createGridLayer() {
        return new GridLayer();
    }

    /**
     * Creates the top-most set of layers on the given layered pane.
     * 
     * @param layeredPane
     *            the parent for the created layers
     */
    protected void createLayers(LayeredPane layeredPane) {
        layeredPane.add(createGridLayer(), GRID_LAYER);
        layeredPane.add(getPrintableLayers(), PRINTABLE_LAYERS);
        layeredPane.add(new FreeformLayer(), HANDLE_LAYER);
        layeredPane.add(new FeedbackLayer(), FEEDBACK_LAYER);
        layeredPane.add(new GuideLayer(), GUIDE_LAYER);
    }

    /**
     * Creates a layered pane and the layers that should be printed.
     * 
     * @see org.eclipse.gef.print.PrintGraphicalViewerOperation
     * @return a new LayeredPane containing the printable layers
     */
    protected LayeredPane createPrintableLayers() {
        FreeformLayeredPane layeredPane = new FreeformLayeredPane();
        layeredPane.add(new FreeformLayer(), PRIMARY_LAYER);
        layeredPane.add(new ConnectionLayer(), CONNECTION_LAYER);
        return layeredPane;
    }

    /**
     * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
     */
    @Override
    public Object getAdapter(Class adapter) {
        if (adapter == AutoexposeHelper.class)
            return new ViewportAutoexposeHelper(this);
        return super.getAdapter(adapter);
    }

    /**
     * The contents' Figure will be added to the PRIMARY_LAYER.
     * 
     * @see org.eclipse.gef.GraphicalEditPart#getContentPane()
     */
    @Override
    public IFigure getContentPane() {
        return getLayer(PRIMARY_LAYER);
    }

    /**
     * Should not be called, but returns a MarqeeDragTracker for good measure.
     * 
     * @see org.eclipse.gef.EditPart#getDragTracker(org.eclipse.gef.Request)
     */
    @Override
    public DragTracker getDragTracker(Request req) {
        /*
         * The root will only be asked for a drag tracker if for some reason the
         * contents editpart says it is neither selector nor opaque.
         */
        return new MarqueeDragTracker();
    }

    /**
     * Returns the layer indicated by the key. Searches all layered panes.
     * 
     * @see LayerManager#getLayer(Object)
     */
    @Override
    public IFigure getLayer(Object key) {
        if (innerLayers == null)
            return null;
        IFigure layer = innerLayers.getLayer(key);
        if (layer != null)
            return layer;
        if (printableLayers == null)
            return null;
        return printableLayers.getLayer(key);
    }

    /**
     * The root editpart does not have a real model. The LayerManager ID is
     * returned so that this editpart gets registered using that key.
     * 
     * @see org.eclipse.gef.EditPart#getModel()
     */
    @Override
    public Object getModel() {
        return LayerManager.ID;
    }

    /**
     * Returns the LayeredPane that should be used during printing. This layer
     * will be identified using {@link LayerConstants#PRINTABLE_LAYERS}.
     * 
     * @return the layered pane containing all printable content
     */
    protected LayeredPane getPrintableLayers() {
        if (printableLayers == null)
            printableLayers = createPrintableLayers();
        return printableLayers;
    }

    /**
     * Updates the {@link GridLayer grid} based on properties set on the
     * {@link #getViewer() graphical viewer}:
     * {@link SnapToGrid#PROPERTY_GRID_VISIBLE},
     * {@link SnapToGrid#PROPERTY_GRID_SPACING}, and
     * {@link SnapToGrid#PROPERTY_GRID_ORIGIN}.
     * <p>
     * This method is invoked initially when the GridLayer is created, and when
     * any of the above-mentioned properties are changed on the viewer.
     */
    protected void refreshGridLayer() {
        boolean visible = false;
        GridLayer grid = (GridLayer) getLayer(GRID_LAYER);
        Boolean val = (Boolean) getViewer().getProperty(
                SnapToGrid.PROPERTY_GRID_VISIBLE);
        if (val != null)
            visible = val.booleanValue();
        grid.setOrigin((Point) getViewer().getProperty(
                SnapToGrid.PROPERTY_GRID_ORIGIN));
        grid.setSpacing((Dimension) getViewer().getProperty(
                SnapToGrid.PROPERTY_GRID_SPACING));
        grid.setVisible(visible);
    }

    /**
     * @see org.eclipse.gef.editparts.AbstractEditPart#register()
     */
    @Override
    protected void register() {
        super.register();
        if (getLayer(GRID_LAYER) != null) {
            getViewer().addPropertyChangeListener(gridListener);
            refreshGridLayer();
        }
    }

    /**
     * @see AbstractEditPart#unregister()
     */
    @Override
    protected void unregister() {
        getViewer().removePropertyChangeListener(gridListener);
        super.unregister();
    }

    class FeedbackLayer extends FreeformLayer {
        FeedbackLayer() {
            setEnabled(false);
        }
    }

}
