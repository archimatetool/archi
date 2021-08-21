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

import java.util.Iterator;
import java.util.List;

import org.eclipse.zest.core.viewers.IFigureProvider;
import org.eclipse.zest.core.viewers.IGraphContentProvider;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;

/**
 * This factory helps make models (nodes & connections).
 * 
 * @author Ian Bull
 * @author Chris Callendar
 */
@SuppressWarnings("rawtypes")
public class GraphModelFactory extends AbstractStylingModelFactory {

    AbstractStructuredGraphViewer viewer = null;

    public GraphModelFactory(AbstractStructuredGraphViewer viewer) {
        super(viewer);
        this.viewer = viewer;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ca.uvic.cs.zest.internal.graphmodel.IGraphModelFactory#createModel()
     */
    @Override
    public Graph createGraphModel(Graph model) {
        doBuildGraph(model);
        return model;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.zest.core.internal.graphmodel.AbstractStylingModelFactory#doBuildGraph(org.eclipse.zest.core.internal.graphmodel.GraphModel)
     */
    @Override
    protected void doBuildGraph(Graph model) {
        super.doBuildGraph(model);
        // make the model have the same styles as the viewer
        Object rels[] = getContentProvider().getElements(getViewer().getInput());
        if (rels != null) {
            IFigureProvider figureProvider = null;
            if (getLabelProvider() instanceof IFigureProvider) {
                figureProvider = (IFigureProvider) getLabelProvider();
            }

            // If rels returns null then just continue
            // @tag zest(bug(134928(fix))) : An empty graph causes an NPE
            for (int i = 0; i < rels.length; i++) {
                // Check the filter on the source
                Object source = getCastedContent().getSource(rels[i]);
                source = filterElement(getViewer().getInput(), source) ? null : source;

                // Check the filter on the dest
                Object dest = getCastedContent().getDestination(rels[i]);
                dest = filterElement(getViewer().getInput(), dest) ? null : dest;

                if (source == null) {
                    // just create the node for the destination
                    if (dest != null) {
                        if (figureProvider != null) {
                            createNode(model, dest, figureProvider.getFigure(dest));
                        } else {
                            createNode(model, dest);
                        }
                    }
                    continue;
                } else if (dest == null) {
                    // just create the node for the source
                    if (source != null) {
                        if (figureProvider != null) {
                            createNode(model, source, figureProvider.getFigure(source));
                        } else {
                            createNode(model, source);
                        }
                    }
                    continue;
                }
                // If any of the source, dest is null or the edge is filtered,
                // don't create the graph.
                if (source != null && dest != null && !filterElement(getViewer().getInput(), rels[i])) {
                    createConnection(model, rels[i], source, dest);
                }
            }
        }

    }

    private IGraphContentProvider getCastedContent() {
        return (IGraphContentProvider) getContentProvider();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.zest.core.internal.graphmodel.IStylingGraphModelFactory#refresh(org.eclipse.zest.core.internal.graphmodel.GraphModel,
     *      java.lang.Object)
     */
    @Override
    public void refresh(Graph graph, Object element) {
        refresh(graph, element, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.zest.core.internal.graphmodel.IStylingGraphModelFactory#refresh(org.eclipse.zest.core.internal.graphmodel.GraphModel,
     *      java.lang.Object, boolean)
     */
    @Override
    public void refresh(Graph graph, Object element, boolean updateLabels) {
        GraphConnection conn = viewer.getGraphModelConnection(element);
        if (conn == null) {
            // did the user send us a node? Check all of the connections on the
            // node.
            GraphNode node = viewer.getGraphModelNode(element);
            if (node != null) {
                List connections = node.getSourceConnections();
                for (Iterator it = connections.iterator(); it.hasNext();) {
                    GraphConnection c = (GraphConnection) it.next();
                    refresh(graph, c.getExternalConnection(), updateLabels);
                }
                connections = node.getTargetConnections();
                for (Iterator it = connections.iterator(); it.hasNext();) {
                    GraphConnection c = (GraphConnection) it.next();
                    refresh(graph, c.getExternalConnection(), updateLabels);
                }
            }
            return;
        }
        Object oldSource = conn.getSource().getData();
        Object oldDest = conn.getDestination().getData();
        Object newSource = getCastedContent().getSource(element);
        Object newDest = getCastedContent().getDestination(element);
        if (!(oldSource.equals(newSource) && oldDest.equals(newDest))) {
            GraphNode internalSource = viewer.getGraphModelNode(newSource);
            GraphNode internalDest = viewer.getGraphModelNode(newDest);
            if (internalSource == null) {
                internalSource = createNode(graph, newSource);
            } else if (updateLabels) {
                styleItem(internalSource);
            }
            if (internalDest == null) {
                internalDest = createNode(graph, newDest);
            } else if (updateLabels) {
                styleItem(internalDest);
            }

            // @tag TODO: Remove these lines
            // conn.disconnect();
            // conn.reconnect(internalSource, internalDest);
            if (updateLabels) {
                styleItem(conn);
            }
        }

    }

}
