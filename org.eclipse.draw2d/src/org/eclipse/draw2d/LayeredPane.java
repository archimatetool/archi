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

/**
 * A figure capable of holding any number of layers. Only layers can be added to
 * this figure. Layers are added to this figure with thier respective keys,
 * which are used to identify them.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class LayeredPane extends Layer {

    private List layerKeys = new ArrayList();

    /**
     * Constructs a new layered pane with no layers in it.
     */
    public LayeredPane() {
        setLayoutManager(new StackLayout());
    }

    /**
     * Adds the given layer figure, identifiable with the given key, at the
     * specified index. While adding the layer, it informs the surrounding
     * layers of the addition.
     * 
     * @param figure
     *            the layer
     * @param layerKey
     *            the layer's key
     * @param index
     *            the index where the layer should be added
     * @since 2.0
     */
    @Override
    public void add(IFigure figure, Object layerKey, int index) {
        if (index == -1)
            index = layerKeys.size();
        super.add(figure, null, index);
        layerKeys.add(index, layerKey);
    }

    /**
     * Adds the given layer, identifiable with the given key, under the
     * <i>after</i> layer provided in the input.
     * 
     * @param layer
     *            the layer
     * @param key
     *            the layer's key
     * @param after
     *            the layer under which the input layer should be added
     * @since 2.0
     */
    public void addLayerAfter(Layer layer, Object key, Object after) {
        int index = layerKeys.indexOf(after);
        add(layer, key, ++index);
    }

    /**
     * Adds the given layer, identifiable with the given key, above the
     * <i>before</i> layer provided in the input.
     * 
     * @param layer
     *            the layer
     * @param key
     *            the layer's key
     * @param before
     *            the layer above which the input layer should be added
     * @since 2.0
     */
    public void addLayerBefore(Layer layer, Object key, Object before) {
        int index = layerKeys.indexOf(before);
        add(layer, key, index);
    }

    /**
     * Returns the layer identified by the key given in the input.
     * 
     * @param key
     *            the key to identify the desired layer
     * @return the desired layer
     * @since 2.0
     */
    public Layer getLayer(Object key) {
        int index = layerKeys.indexOf(key);
        if (index == -1)
            return null;
        return (Layer) getChildren().get(index);
    }

    /**
     * Returns the layer at the specified index in this pane.
     * 
     * @param index
     *            the index of the desired layer
     * @return the desired layer
     * @since 2.0
     */
    protected Layer getLayer(int index) {
        return (Layer) getChildren().get(index);
    }

    /**
     * @see org.eclipse.draw2d.IFigure#remove(org.eclipse.draw2d.IFigure)
     */
    @Override
    public void remove(IFigure figure) {
        int index = getChildren().indexOf(figure);
        if (index != -1)
            layerKeys.remove(index);
        super.remove(figure);
    }

    /**
     * Removes the layer identified by the given key from this layered pane.
     * 
     * @param key
     *            the key of the layer to be removed
     * @since 2.0
     */
    public void removeLayer(Object key) {
        removeLayer(layerKeys.indexOf(key));
    }

    /**
     * Removes the given layer from this layered pane.
     * 
     * @deprecated call {@link IFigure#remove(IFigure)} instead
     * @param layer
     *            the layer to be removed
     * @since 2.0
     */
    public void removeLayer(IFigure layer) {
        remove(layer);
    }

    /**
     * Removes the layer at the specified index from the list of layers in this
     * layered pane. It collapses the layers, occupying the space vacated by the
     * removed layer.
     * 
     * @param index
     *            the index of the layer to be removed
     * @since 2.0
     */
    protected void removeLayer(int index) {
        Layer removeLayer = getLayer(index);
        remove(removeLayer);
    }

}
