/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 ******************************************************************************/
package org.eclipse.zest.core.viewers.internal;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.zest.core.viewers.EntityConnectionData;
import org.eclipse.zest.core.viewers.IFigureProvider;
import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.core.widgets.GraphNode;

/**
 * 
 * @author Ian Bull
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class GraphModelEntityFactory extends AbstractStylingModelFactory {

    AbstractStructuredGraphViewer viewer = null;

    public GraphModelEntityFactory(AbstractStructuredGraphViewer viewer) {
        super(viewer);
        this.viewer = viewer;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.zest.core.internal.graphmodel.IStylingGraphModelFactory#
     * createGraphModel()
     */
    @Override
    public Graph createGraphModel(Graph model) {
        doBuildGraph(model);
        return model;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.zest.core.internal.graphmodel.AbstractStylingModelFactory
     * #doBuildGraph(org.eclipse.zest.core.internal.graphmodel.GraphModel)
     */
    @Override
    protected void doBuildGraph(Graph model) {
        super.doBuildGraph(model);
        Object inputElement = getViewer().getInput();
        Object entities[] = getContentProvider().getElements(inputElement);
        if (entities == null) {
            return;
        }
        for (int i = 0; i < entities.length; i++) {
            Object data = entities[i];
            IFigureProvider figureProvider = null;
            if (getLabelProvider() instanceof IFigureProvider) {
                figureProvider = (IFigureProvider) getLabelProvider();
            }
            if (!filterElement(inputElement, data)) {
                if (figureProvider != null) {
                    createNode(model, data, figureProvider.getFigure(data));
                } else {
                    createNode(model, data);
                }
            }
        }

        // We may have other entities (such as children of containers)
        Set keySet = ((AbstractStructuredGraphViewer) getViewer())
                .getNodesMap().keySet();
        entities = keySet.toArray();

        for (int i = 0; i < entities.length; i++) {
            Object data = entities[i];

            // If this element is filtered, continue to the next one.
            if (filterElement(inputElement, data)) {
                continue;
            }
            Object[] related = ((IGraphEntityContentProvider) getContentProvider())
                    .getConnectedTo(data);

            if (related != null) {
                for (int j = 0; j < related.length; j++) {
                    // if the node this node is connected to is filtered,
                    // don't display this edge
                    if (filterElement(inputElement, related[j])) {
                        continue;
                    }
                    EntityConnectionData connectionData = new EntityConnectionData(
                            data, related[j]);
                    if (filterElement(inputElement, connectionData)) {
                        continue;
                    }
                    createConnection(model, connectionData, data, related[j]);
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.zest.core.internal.graphmodel.IStylingGraphModelFactory#refresh
     * (org.eclipse.zest.core.internal.graphmodel.GraphModel, java.lang.Object)
     */
    @Override
    public void refresh(Graph graph, Object element, boolean refreshLabels) {
        if (element == null) {
            return;
        }
        GraphNode node = viewer.getGraphModelNode(element);
        if (node == null) {
            // check to make sure that the user didn't send us an edge.
            GraphConnection conn = viewer.getGraphModelConnection(element);
            if (conn != null) {
                // refresh on the connected nodes.
                refresh(graph, conn.getSource().getData(), refreshLabels);
                refresh(graph, conn.getDestination().getData(), refreshLabels);
                return;
            }
        }
        // can only refresh on nodes in this kind of factory.
        if (node == null) {
            // do nothing
            return;
        }
        reconnect(graph, element, refreshLabels);

        if (refreshLabels) {
            update(node);
            for (Iterator it = node.getSourceConnections().iterator(); it
                    .hasNext();) {
                update((GraphItem) it.next());
            }
            for (Iterator it = node.getTargetConnections().iterator(); it
                    .hasNext();) {
                update((GraphItem) it.next());
            }
        }
    }

    /**
     * @param graph
     * @param element
     * @param refreshLabels
     */
    private void reconnect(Graph graph, Object element, boolean refreshLabels) {
        GraphNode node = viewer.getGraphModelNode(element);
        Object[] related = ((IGraphEntityContentProvider) getContentProvider())
                .getConnectedTo(element);
        List connections = node.getSourceConnections();
        LinkedList toAdd = new LinkedList();
        LinkedList toDelete = new LinkedList();
        LinkedList toKeep = new LinkedList();
        HashSet oldExternalConnections = new HashSet();
        HashSet newExternalConnections = new HashSet();
        for (Iterator it = connections.iterator(); it.hasNext();) {
            oldExternalConnections.add(((GraphConnection) it.next())
                    .getExternalConnection());
        }
        for (int i = 0; i < related.length; i++) {
            newExternalConnections.add(new EntityConnectionData(element,
                    related[i]));
        }
        for (Iterator it = oldExternalConnections.iterator(); it.hasNext();) {
            Object next = it.next();
            if (!newExternalConnections.contains(next)) {
                toDelete.add(next);
            } else {
                toKeep.add(next);
            }
        }
        for (Iterator it = newExternalConnections.iterator(); it.hasNext();) {
            Object next = it.next();
            if (!oldExternalConnections.contains(next)) {
                toAdd.add(next);
            }
        }
        for (Iterator it = toDelete.iterator(); it.hasNext();) {
            viewer.removeGraphModelConnection(it.next());
        }
        toDelete.clear();
        LinkedList newNodeList = new LinkedList();
        for (Iterator it = toAdd.iterator(); it.hasNext();) {
            EntityConnectionData data = (EntityConnectionData) it.next();
            GraphNode dest = viewer.getGraphModelNode(data.dest);
            if (dest == null) {
                newNodeList.add(data.dest);
            }
            createConnection(graph, data, data.source, data.dest);
        }
        toAdd.clear();
        if (refreshLabels) {
            for (Iterator i = toKeep.iterator(); i.hasNext();) {
                styleItem(viewer.getGraphModelConnection(i.next()));
            }
        }
        for (Iterator it = newNodeList.iterator(); it.hasNext();) {
            // refresh the new nodes so that we get a fully-up-to-date graph.
            refresh(graph, it.next());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.zest.core.internal.graphmodel.IStylingGraphModelFactory#refresh
     * (org.eclipse.zest.core.internal.graphmodel.GraphModel, java.lang.Object,
     * boolean)
     */
    @Override
    public void refresh(Graph graph, Object element) {
        refresh(graph, element, false);
    }

}
