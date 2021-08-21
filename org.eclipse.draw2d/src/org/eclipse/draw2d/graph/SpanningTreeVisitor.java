/*******************************************************************************
 * Copyright (c) 2003, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.graph;

/**
 * A base class for visitors which operate on the graphs spanning tree used to
 * induce rank assignments.
 * 
 * @author Randy Hudson
 * @since 2.1.2
 */
abstract class SpanningTreeVisitor extends GraphVisitor {

    Edge getParentEdge(Node node) {
        return (Edge) node.workingData[1];
    }

    EdgeList getSpanningTreeChildren(Node node) {
        return (EdgeList) node.workingData[0];
    }

    protected Node getTreeHead(Edge edge) {
        if (getParentEdge(edge.source) == edge)
            return edge.target;
        return edge.source;
    }

    Node getTreeParent(Node node) {
        Edge e = getParentEdge(node);
        if (e == null)
            return null;
        return e.opposite(node);
    }

    protected Node getTreeTail(Edge edge) {
        if (getParentEdge(edge.source) == edge)
            return edge.source;
        return edge.target;
    }

    void setParentEdge(Node node, Edge edge) {
        node.workingData[1] = edge;
    }

}
