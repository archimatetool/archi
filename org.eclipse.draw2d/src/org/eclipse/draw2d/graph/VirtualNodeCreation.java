/*******************************************************************************
 * Copyright (c) 2004, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.draw2d.graph;

import org.eclipse.draw2d.geometry.Insets;

/**
 * Encapsulates the conversion of a long edge to multiple short edges and back.
 * 
 * @since 3.1
 */
@SuppressWarnings("unchecked")
class VirtualNodeCreation extends RevertableChange {

    private final Edge edge;
    private final DirectedGraph graph;
    private Node nodes[];
    private Edge[] edges;

    private static final int INNER_EDGE_X = 2;
    private static final int LONG_EDGE_X = 8;

    /**
     * Breaks a single edge into multiple edges containing virtual nodes.
     * 
     * @since 3.1
     * @param edge
     *            The edge to convert
     * @param graph
     *            the graph containing the edge
     */
    @SuppressWarnings("deprecation")
    public VirtualNodeCreation(Edge edge, DirectedGraph graph) {
        this.edge = edge;
        this.graph = graph;

        int size = edge.target.rank - edge.source.rank - 1;
        int offset = edge.source.rank + 1;

        Node prevNode = edge.source;
        Node currentNode;
        Edge currentEdge;
        nodes = new Node[size];
        edges = new Edge[size + 1];

        Insets padding = new Insets(0, edge.padding, 0, edge.padding);

        Subgraph s = GraphUtilities.getCommonAncestor(edge.source, edge.target);

        for (int i = 0; i < size; i++) {
            nodes[i] = currentNode = new VirtualNode(
                    "Virtual" + i + ':' + edge, s); //$NON-NLS-1$
            currentNode.width = edge.width;
            if (s != null) {
                currentNode.nestingIndex = s.nestingIndex;
            }

            currentNode.height = 0;
            currentNode.setPadding(padding);
            currentNode.rank = offset + i;
            graph.ranks.getRank(offset + i).add(currentNode);

            currentEdge = new Edge(prevNode, currentNode, 1, edge.weight
                    * LONG_EDGE_X);
            if (i == 0) {
                currentEdge.weight = edge.weight * INNER_EDGE_X;
                currentEdge.offsetSource = edge.offsetSource;
            }
            graph.edges.add(edges[i] = currentEdge);
            graph.nodes.add(currentNode);
            prevNode = currentNode;
        }

        currentEdge = new Edge(prevNode, edge.target, 1, edge.weight
                * INNER_EDGE_X);
        currentEdge.offsetTarget = edge.offsetTarget;
        graph.edges.add(edges[edges.length - 1] = currentEdge);
        graph.removeEdge(edge);
    }

    @SuppressWarnings("deprecation")
    @Override
    void revert() {
        edge.start = edges[0].start;
        edge.end = edges[edges.length - 1].end;
        edge.vNodes = new NodeList();
        for (int i = 0; i < edges.length; i++) {
            graph.removeEdge(edges[i]);
        }
        for (int i = 0; i < nodes.length; i++) {
            edge.vNodes.add(nodes[i]);
            graph.removeNode(nodes[i]);
        }
        edge.source.outgoing.add(edge);
        edge.target.incoming.add(edge);

        graph.edges.add(edge);
    }

}
