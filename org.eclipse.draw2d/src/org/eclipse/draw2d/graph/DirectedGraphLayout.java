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

import java.util.ArrayList;
import java.util.List;

/**
 * Performs a graph layout of a <code>DirectedGraph</code>. The directed graph
 * must meet the following conditions:
 * <UL>
 * <LI>The graph must be connected.
 * <LI>All edge's must be added to the graph's {@link DirectedGraph#edges edges}
 * list exactly once.
 * <LI>All nodes must be added to the graph's {@link DirectedGraph#nodes nodes}
 * list exactly once.
 * </UL>
 * 
 * This algorithm will:
 * <UL>
 * <LI>break cycles by inverting a set of feedback edges. Feedback edges will
 * have the flag {@link Edge#isFeedback} set to <code>true</code>. The following
 * statements are true with respect to the inverted edge. When the algorithm
 * completes, it will invert the edges again, but will leave the feedback flags
 * set.
 * <LI>for each node <em>n</em>, assign n to a "rank" R(n), such that: for each
 * edge (m, n) in n.incoming, R(m)<=R(n)-(m,n).delta. The total weighted edge
 * lengths shall be no greater than is necessary to meet this requirement for
 * all edges in the graph.
 * <LI>attempt to order the nodes in their ranks as to minimize crossings.
 * <LI>assign <em>y</em> coordinates to each node based on its rank. The spacing
 * between ranks is the sum of the bottom padding of the previous rank, and the
 * top padding of the next rank.
 * <LI>assign <em>x</em> coordinates such that the graph is easily readable. The
 * exact behavior is undefined, but will favor edge's with higher
 * {@link Edge#weight}s. The minimum x value assigned to a node or bendpoint
 * will be 0.
 * <LI>assign <em>bendpoints</em> to all edge's which span more than 1 rank.
 * </UL>
 * <P>
 * For each NODE:
 * <UL>
 * <LI>The x coordinate will be assigned a value >= 0
 * <LI>The y coordinate will be assigned a value >= 0
 * <LI>The rank will be assigned a value >=0
 * <LI>The height will be set to the height of the tallest node on the same row
 * </UL>
 * <P>
 * For each EDGE:
 * <UL>
 * <LI>If an edge spans more than 1 row, it will have a list of
 * {@link org.eclipse.draw2d.graph.Edge#vNodes virtual} nodes. The virtual nodes
 * will be assigned an x coordinate indicating the routing path for that edge.
 * <LI>If an edge is a feedback edge, it's <code>isFeedback</code> flag will be
 * set, and if it has virtual nodes, they will be in reverse order (bottom-up).
 * </UL>
 * <P>
 * This class is not guaranteed to produce the same results for each invocation.
 * 
 * @author Randy Hudson
 * @since 2.1.2
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class DirectedGraphLayout {

    List steps = new ArrayList();

    /**
     * @since 3.1
     */
    public DirectedGraphLayout() {
        init();
    }

    void init() {
        steps.add(new TransposeMetrics());
        steps.add(new BreakCycles());
        steps.add(new RouteEdges());
        steps.add(new InitialRankSolver());
        steps.add(new TightSpanningTreeSolver());
        steps.add(new RankAssignmentSolver());
        steps.add(new PopulateRanks());
        steps.add(new VerticalPlacement());
        steps.add(new MinCross());
        steps.add(new LocalOptimizer());
        steps.add(new HorizontalPlacement());
    }

    /**
     * Lays out the given graph
     * 
     * @param graph
     *            the graph to layout
     */
    public void visit(DirectedGraph graph) {
        if (graph.nodes.isEmpty())
            return;
        for (int i = 0; i < steps.size(); i++) {
            GraphVisitor visitor = (GraphVisitor) steps.get(i);
            visitor.visit(graph);
        }
        for (int i = steps.size() - 1; i >= 0; i--) {
            GraphVisitor visitor = (GraphVisitor) steps.get(i);
            visitor.revisit(graph);
        }
    }

}
