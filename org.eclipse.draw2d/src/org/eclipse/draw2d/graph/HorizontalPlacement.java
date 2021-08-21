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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Assigns the X and width values for nodes in a directed graph.
 * 
 * @author Randy Hudson
 * @since 2.1.2
 */
@SuppressWarnings({"rawtypes", "unchecked", "deprecation"})
class HorizontalPlacement extends SpanningTreeVisitor {

    class ClusterSet {
        int freedom = Integer.MAX_VALUE;
        boolean isRight;
        public List members = new ArrayList();
        int pullWeight = 0;
        int rawPull = 0;

        boolean addCluster(NodeCluster seed) {
            members.add(seed);
            seed.isSetMember = true;

            rawPull += seed.weightedTotal;
            pullWeight += seed.weightedDivisor;
            if (isRight) {
                freedom = Math.min(freedom, seed.rightNonzero);
                if (freedom == 0 || rawPull <= 0)
                    return true;
                addIncomingClusters(seed);
                if (addOutgoingClusters(seed))
                    return true;
            } else {
                freedom = Math.min(freedom, seed.leftNonzero);
                if (freedom == 0 || rawPull >= 0)
                    return true;
                addOutgoingClusters(seed);
                if (addIncomingClusters(seed))
                    return true;
            }
            return false;
        }

        boolean addIncomingClusters(NodeCluster seed) {
            for (int i = 0; i < seed.leftCount; i++) {
                NodeCluster neighbor = seed.leftNeighbors[i];
                if (neighbor.isSetMember)
                    continue;
                CollapsedEdges edges = seed.leftLinks[i];
                if (!edges.isTight())
                    continue;
                if ((!isRight || neighbor.getPull() > 0)
                        && addCluster(neighbor))
                    return true;
            }
            return false;
        }

        boolean addOutgoingClusters(NodeCluster seed) {
            for (int i = 0; i < seed.rightCount; i++) {
                NodeCluster neighbor = seed.rightNeighbors[i];
                if (neighbor.isSetMember)
                    continue;
                CollapsedEdges edges = seed.rightLinks[i];
                if (!edges.isTight())
                    continue;
                if ((isRight || neighbor.getPull() < 0) && addCluster(neighbor))
                    return true;
            }
            return false;
        }

        boolean build(NodeCluster seed) {
            isRight = seed.weightedTotal > 0;
            if (!addCluster(seed)) {
                int delta = rawPull / pullWeight;
                if (delta < 0)
                    delta = Math.max(delta, -freedom);
                else
                    delta = Math.min(delta, freedom);
                if (delta != 0) {
                    for (int i = 0; i < members.size(); i++) {
                        NodeCluster c = (NodeCluster) members.get(i);
                        c.adjustRank(delta, dirtyClusters);
                    }
                    refreshDirtyClusters();
                    reset();
                    return true;
                }
            }
            reset();
            return false;
        }

        void reset() {
            rawPull = pullWeight = 0;
            for (int i = 0; i < members.size(); i++)
                ((NodeCluster) members.get(i)).isSetMember = false;
            members.clear();
            freedom = Integer.MAX_VALUE;
        }
    }

    static int step;
    private List allClusters;
    private Map clusterMap = new HashMap();
    ClusterSet clusterset = new ClusterSet();
    Collection dirtyClusters = new HashSet();
    DirectedGraph graph;
    Map map = new HashMap();
    DirectedGraph prime;
    Node graphRight;
    Node graphLeft;

    /**
     * Inset the corresponding parts for the given 2 nodes along an edge E. The
     * weight value is a value by which to scale the edges specified weighting
     * factor.
     * 
     * @param u
     *            the source
     * @param v
     *            the target
     * @param e
     *            the edge along which u and v exist
     * @param weight
     *            a scaling for the weight
     */
    void addEdge(Node u, Node v, Edge e, int weight) {
        Node ne = new Node(new NodePair(u, v));
        prime.nodes.add(ne);

        ne.y = (u.y + u.height + v.y) / 2;
        Node uPrime = get(u);
        Node vPrime = get(v);

        int uOffset = e.getSourceOffset();

        int vOffset = e.getTargetOffset();

        Edge eu = new Edge(ne, uPrime, 0, e.weight * weight);
        Edge ev = new Edge(ne, vPrime, 0, e.weight * weight);

        int dw = uOffset - vOffset;
        if (dw < 0)
            eu.delta = -dw;
        else
            ev.delta = dw;

        prime.edges.add(eu);
        prime.edges.add(ev);
    }

    /**
     * Adds all of the incoming edges to the graph.
     * 
     * @param n
     *            the original node
     * @param nPrime
     *            its corresponding node in the auxilary graph
     */
    void addEdges(Node n) {
        for (int i = 0; i < n.incoming.size(); i++) {
            Edge e = n.incoming.getEdge(i);
            addEdge(e.source, n, e, 1);
        }
    }

    void applyGPrime() {
        Node node;
        for (int n = 0; n < prime.nodes.size(); n++) {
            node = prime.nodes.getNode(n);
            if (node.data instanceof Node)
                ((Node) node.data).x = node.rank;
        }
    }

    private void balanceClusters() {
        findAllClusters();

        step = 0;
        boolean somethingMoved = false;

        for (int i = 0; i < allClusters.size();) {

            NodeCluster c = (NodeCluster) allClusters.get(i);
            int delta = c.getPull();
            if (delta < 0) {
                if (c.leftFreedom > 0) {
                    c.adjustRank(Math.max(delta, -c.leftFreedom), dirtyClusters);
                    refreshDirtyClusters();
                    moveClusterForward(i, c);
                    somethingMoved = true;
                    step++;
                } else if (clusterset.build(c)) {
                    step++;
                    moveClusterForward(i, c);
                    somethingMoved = true;
                }
            } else if (delta > 0) {
                if (c.rightFreedom > 0) {
                    c.adjustRank(Math.min(delta, c.rightFreedom), dirtyClusters);
                    refreshDirtyClusters();
                    moveClusterForward(i, c);
                    somethingMoved = true;
                    step++;
                } else if (clusterset.build(c)) {
                    step++;
                    moveClusterForward(i, c);
                    somethingMoved = true;
                }
            }
            i++;
            if (i == allClusters.size() && somethingMoved) {
                i = 0;
                somethingMoved = false;
            }
        }
    }

    // boolean balanceClusterSets() {
    // for (int i = 0; i < allClusters.size(); i++) {
    // NodeCluster c = (NodeCluster)allClusters.get(i);
    // if (c.weightedTotal < 0 && c.leftFreedom == 0) {
    // if (clusterset.build(c)) {
    // moveClusterForward(i, c);
    // return true;
    // }
    // } else if (c.weightedTotal > 0 && c.rightFreedom == 0) {
    // if (clusterset.build(c)) {
    // moveClusterForward(i, c);
    // return true;
    // }
    // }
    // }
    // return false;
    // }

    void buildGPrime() {
        RankList ranks = graph.ranks;
        buildRankSeparators(ranks);

        Rank rank;
        Node n;
        for (int r = 1; r < ranks.size(); r++) {
            rank = ranks.getRank(r);
            for (int i = 0; i < rank.count(); i++) {
                n = rank.getNode(i);
                addEdges(n);
            }
        }
    }

    void buildRankSeparators(RankList ranks) {
        Rank rank;
        Node n, nPrime, prevNPrime;
        Edge e;
        for (int r = 0; r < ranks.size(); r++) {
            rank = ranks.getRank(r);
            prevNPrime = null;
            for (int i = 0; i < rank.count(); i++) {
                n = rank.getNode(i);
                nPrime = new Node(n);
                if (i == 0) {
                    e = new Edge(graphLeft, nPrime, 0, 0);
                    prime.edges.add(e);
                    e.delta = graph.getPadding(n).left + graph.getMargin().left;
                } else {
                    e = new Edge(prevNPrime, nPrime);
                    e.weight = 0;
                    prime.edges.add(e);
                    rowSeparation(e);
                }
                prevNPrime = nPrime;
                prime.nodes.add(nPrime);
                map(n, nPrime);
                if (i == rank.count() - 1) {
                    e = new Edge(nPrime, graphRight, 0, 0);
                    e.delta = n.width + graph.getPadding(n).right
                            + graph.getMargin().right;
                    prime.edges.add(e);
                }
            }
        }
    }

    @SuppressWarnings("null")
    private void calculateCellLocations() {
        graph.cellLocations = new int[graph.ranks.size() + 1][];
        for (int row = 0; row < graph.ranks.size(); row++) {
            Rank rank = graph.ranks.getRank(row);
            int locations[] = graph.cellLocations[row] = new int[rank.size() + 1];
            int cell;
            Node node = null;
            for (cell = 0; cell < rank.size(); cell++) {
                node = rank.getNode(cell);
                locations[cell] = node.x - graph.getPadding(node).left;
            }
            locations[cell] = node.x + node.width
                    + graph.getPadding(node).right;
        }
    }

    private void findAllClusters() {
        Node root = prime.nodes.getNode(0);
        NodeCluster cluster = new NodeCluster();
        allClusters = new ArrayList();
        allClusters.add(cluster);
        growCluster(root, cluster);

        for (int i = 0; i < prime.edges.size(); i++) {
            Edge e = prime.edges.getEdge(i);
            NodeCluster sourceCluster = (NodeCluster) clusterMap.get(e.source);
            NodeCluster targetCluster = (NodeCluster) clusterMap.get(e.target);

            // Ignore cluster internal edges
            if (targetCluster == sourceCluster)
                continue;

            CollapsedEdges link = sourceCluster.getRightNeighbor(targetCluster);
            if (link == null) {
                link = new CollapsedEdges(e);
                sourceCluster.addRightNeighbor(targetCluster, link);
                targetCluster.addLeftNeighbor(sourceCluster, link);
            } else {
                prime.removeEdge(link.processEdge(e));
                i--;
            }
        }
        for (int i = 0; i < allClusters.size(); i++)
            ((NodeCluster) allClusters.get(i)).initValues();
    }

    Node get(Node key) {
        return (Node) map.get(key);
    }

    void growCluster(Node root, NodeCluster cluster) {
        cluster.add(root);
        clusterMap.put(root, cluster);
        EdgeList treeChildren = getSpanningTreeChildren(root);
        for (int i = 0; i < treeChildren.size(); i++) {
            Edge e = treeChildren.getEdge(i);
            if (e.cut != 0)
                growCluster(getTreeTail(e), cluster);
            else {
                NodeCluster newCluster = new NodeCluster();
                allClusters.add(newCluster);
                growCluster(getTreeTail(e), newCluster);
            }
        }
    }

    void map(Node key, Node value) {
        map.put(key, value);
    }

    private void moveClusterForward(int i, NodeCluster c) {
        if (i == 0)
            return;
        int swapIndex = i / 2;
        Object temp = allClusters.get(swapIndex);
        allClusters.set(swapIndex, c);
        allClusters.set(i, temp);
    }

    void refreshDirtyClusters() {
        for (Iterator iter = dirtyClusters.iterator(); iter.hasNext();)
            ((NodeCluster) iter.next()).refreshValues();
        dirtyClusters.clear();
    }

    void rowSeparation(Edge e) {
        Node source = (Node) e.source.data;
        Node target = (Node) e.target.data;
        e.delta = source.width + graph.getPadding(source).right
                + graph.getPadding(target).left;
    }

    @Override
    public void visit(DirectedGraph g) {
        graph = g;
        prime = new DirectedGraph();
        prime.nodes.add(graphLeft = new Node(null));
        prime.nodes.add(graphRight = new Node(null));
        if (g.tensorStrength != 0)
            prime.edges.add(new Edge(graphLeft, graphRight, g.tensorSize,
                    g.tensorStrength));
        buildGPrime();
        new InitialRankSolver().visit(prime);
        new TightSpanningTreeSolver().visit(prime);

        RankAssignmentSolver solver = new RankAssignmentSolver();
        solver.visit(prime);
        graph.size.width = graphRight.rank;
        balanceClusters();

        prime.nodes.adjustRank(-graphLeft.rank);
        applyGPrime();
        calculateCellLocations();
    }

}
