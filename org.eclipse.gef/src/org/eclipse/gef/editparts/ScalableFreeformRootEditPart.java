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

import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.draw2d.ScalableFigure;
import org.eclipse.draw2d.ScalableFreeformLayeredPane;
import org.eclipse.draw2d.Viewport;

/**
 * Adds Zoom support to the standard FreeformGraphicalRootEditPart. This root is
 * just like its superclass, except it inserts a new <code>LayeredPane</code>
 * above the printable layers. This pane is identified with the
 * {@link org.eclipse.gef.LayerConstants#SCALABLE_LAYERS} ID. This root also
 * provides a ZoomManager, for optional use with the
 * {@link org.eclipse.gef.ui.actions.ZoomComboContributionItem}
 * <P>
 * The structure of layers (top-to-bottom) for this root is:
 * <table cellspacing="0" cellpadding="0">
 * <tr>
 * <td colspan="4">Root Freeform Layered Pane</td>
 * </tr>
 * <tr>
 * <td>&#9500;</td>
 * <td colspan="3">&nbsp;Guide Layer</td>
 * </tr>
 * <tr>
 * <td>&#9500;</td>
 * <td colspan="3">&nbsp;Feedback Layer</td>
 * </tr>
 * <tr>
 * <td>&#9500;</td>
 * <td colspan="3">&nbsp;Handle Layer</td>
 * </tr>
 * <tr>
 * <td>&#9492;</td>
 * <td colspan="2">&nbsp;<b>Scalable Layers</b></td>
 * <td>({@link ScalableFreeformLayeredPane})</td>
 * </tr>
 * <tr>
 * <td>&nbsp;</td>
 * <td>&#9500;</td>
 * <td colspan="2">&nbsp;Scaled Feedback Layer</td>
 * </tr>
 * <tr>
 * <td>&nbsp;</td>
 * <td>&#9500;</td>
 * <td colspan="2">&nbsp;Printable Layers</td>
 * </tr>
 * <tr>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&#9500; Connection Layer</td>
 * </tr>
 * <tr>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&#9492;&nbsp;Primary Layer</td>
 * <td>&nbsp;</td>
 * </tr>
 * <tr>
 * <td>&nbsp;</td>
 * <td>&#9492;</td>
 * <td colspan="2">&nbsp;Grid Layer</td>
 * </tr>
 * </table>
 * 
 * @author hudsonr
 * @since 2.1
 */
public class ScalableFreeformRootEditPart extends FreeformGraphicalRootEditPart {

    private ScalableFreeformLayeredPane scaledLayers;
    private ZoomManager zoomManager;
    
    private boolean useScaledGraphics = true;

    /**
     * Constructor for ScalableFreeformRootEditPart
     */
    public ScalableFreeformRootEditPart(boolean useScaledGraphics) {
        this.useScaledGraphics = useScaledGraphics;
        zoomManager = createZoomManager((ScalableFigure) getScaledLayers(),
                ((Viewport) getFigure()));
    }

    /**
     * Constructor for ScalableFreeformRootEditPart
     */
    public ScalableFreeformRootEditPart() {
        this(true);
    }

    /**
     * Responsible of creating a {@link ZoomManager} to be used by this
     * {@link ScalableRootEditPart}.
     * 
     * @return A new {@link ZoomManager} bound to the given
     *         {@link ScalableFigure} and {@link Viewport}.
     * @since 3.10
     */
    protected ZoomManager createZoomManager(ScalableFigure scalableFigure,
            Viewport viewport) {
        return new ZoomManager(scalableFigure, viewport);
    }

    /**
     * @see FreeformGraphicalRootEditPart#createLayers(LayeredPane)
     */
    @Override
    protected void createLayers(LayeredPane layeredPane) {
        layeredPane.add(getScaledLayers(), SCALABLE_LAYERS);
        layeredPane.add(new FreeformLayer(), HANDLE_LAYER);
        layeredPane.add(new FeedbackLayer(), FEEDBACK_LAYER);
        layeredPane.add(new GuideLayer(), GUIDE_LAYER);
    }

    /**
     * Creates a layered pane and the layers that should be scaled.
     * 
     * @return a new freeform layered pane containing the scalable layers
     */
    protected ScalableFreeformLayeredPane createScaledLayers() {
        ScalableFreeformLayeredPane layers = new ScalableFreeformLayeredPane(useScaledGraphics);
        layers.add(createGridLayer(), GRID_LAYER);
        layers.add(getPrintableLayers(), PRINTABLE_LAYERS);
        layers.add(new FeedbackLayer(), SCALED_FEEDBACK_LAYER);
        return layers;
    }

    /**
     * @see FreeformGraphicalRootEditPart#getLayer(Object)
     */
    @Override
    public IFigure getLayer(Object key) {
        IFigure layer = scaledLayers.getLayer(key);
        if (layer != null)
            return layer;
        return super.getLayer(key);
    }

    /**
     * Returns the scalable layers of this EditPart
     * 
     * @return LayeredPane
     */
    protected LayeredPane getScaledLayers() {
        if (scaledLayers == null)
            scaledLayers = createScaledLayers();
        return scaledLayers;
    }

    /**
     * Returns the zoomManager.
     * 
     * @return ZoomManager
     */
    public ZoomManager getZoomManager() {
        return zoomManager;
    }

    /**
     * @see org.eclipse.gef.editparts.AbstractEditPart#register()
     */
    @Override
    protected void register() {
        super.register();
        getViewer().setProperty(ZoomManager.class.toString(), getZoomManager());
    }

    /**
     * @see org.eclipse.gef.editparts.AbstractEditPart#unregister()
     */
    @Override
    protected void unregister() {
        super.unregister();
        getViewer().setProperty(ZoomManager.class.toString(), null);
    }

}
