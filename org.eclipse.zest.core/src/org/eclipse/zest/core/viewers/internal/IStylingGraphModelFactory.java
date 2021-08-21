/*******************************************************************************
 * Copyright 2005-2006, CHISEL Group, University of Victoria, Victoria, BC,
 * Canada. All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.zest.core.viewers.internal;

import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.core.widgets.GraphNode;

/**
 * A Graph model factory that supports the structural and visual refreshing of
 * graph elements based on the content provider and label provider in the viewer
 * that this factory is associated with. Model elements are created using the
 * content provider supplied by getContentProvider(), and styled using the label
 * provider supplied by getLabelProvider(). By the end of creation and
 * refreshing, the graph model elements are expected to be styled according to
 * the given label provider, however, default styles are dependant on the
 * particular implementation of IStylingGraphModelFactory. Unless otherwise
 * documented, clients should expect that the implementation of
 * IStylingGraphModelFactory adheres to the general defaults found in
 * {@link IZestGraphDefaults}.
 * 
 * @author Del Myers
 */

public interface IStylingGraphModelFactory {
    /**
     * Returns the label provider used in this factory.
     * 
     * @return the label provider used in this factory.
     */
    public IBaseLabelProvider getLabelProvider();

    /**
     * Returns the content provider used in this factory.
     * 
     * @return the content provider used in this factory.
     */
    public IStructuredContentProvider getContentProvider();

    /**
     * Creates and returns the graph model from this factory based on the label
     * provider and the label provider returned in getContentProvider() and
     * getLabelProvider().
     * 
     * @return the created graph model.
     */
    public Graph createGraphModel(Graph model);

    /**
     * Creates and returns a node on the given graph based on the user model
     * data, "data", using the content provider returned by
     * getContentProvider(). They node will also be styled according to the
     * information given by the label provider. If the node already exists in
     * the graph, it is restyled and returned; no new node is created.
     * 
     * @param graph
     *            the graph to create or retrieve the node on.
     * @param element
     *            the user model data to use in the node.
     * @return the node created or retrieved for the given graph.
     */
    public GraphNode createNode(Graph graph, Object element);

    /**
     * Creates and returns a connection with the given source and destination
     * objects from the user model. If the source and destination nodes don't
     * exist for the given user model objects, they are created using
     * createNode(GraphModel, Object). If a connection already exists for the
     * given user data, but with different source or destinations, it is
     * disconnected and reconnected to the given source and destination. It is
     * always styled according to the label provider provided by
     * getLabelProvider().
     * 
     * @param graph
     *            the graph to create or retrieve the connection on.
     * @param element
     *            the user model data to use in this connection.
     * @param source
     *            the user model data used for the source node.
     * @param dest
     *            the user model data used for the destination node.
     * @return the created or retrieved connection for the given graph.
     */
    public GraphConnection createConnection(Graph graph, Object element, Object source, Object dest);

    /**
     * Restyles the given graph items according to the label provider supplied
     * by getLabelProvider().
     * 
     * @param items
     *            the items to update.
     */
    public void update(GraphItem[] items);

    /**
     * Restyles the given graph item according to the label provider supplied by
     * getLabelProvider().
     * 
     * @param item
     *            the item to update.
     */
    public void update(GraphItem item);

    /**
     * Structurally refreshes the graph model nodes and connections associated
     * with the given user model element. Does nothing if the element does not
     * currently exist in the view. No restyling is done by default.
     * 
     * @param graph
     * @param element
     *            the element to restructure.
     */
    public void refresh(Graph graph, Object element);

    /**
     * Structurally refreshes the graph model nodes and connections associated
     * with the given user model element. If updateLabels is true, then the
     * labels are updated as well. Does nothing if the element does not
     * currently exist in the view.
     * 
     * @param graph
     *            the graph to find the element on.
     * @param element
     *            the user model element.
     * @param updateLabels
     *            true if the labels should be updated as well.
     */
    public void refresh(Graph graph, Object element, boolean updateLabels);

    /**
     * Structurally refreshes the entire graph.
     * 
     * @param graph
     *            the graph to refresh;
     */
    public void refreshGraph(Graph graph);

    /**
     * Returns the viewer that this factory is building the model for.
     * 
     * @return the viewer that this factory is building the model for.
     */
    public StructuredViewer getViewer();

    public void setConnectionStyle(int style);

    /**
     * @return the connectionStyle
     */
    public int getConnectionStyle();

    public void setNodeStyle(int style);

    /**
     * @return the nodeStyle
     */
    public int getNodeStyle();

}
