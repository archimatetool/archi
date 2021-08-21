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

import java.util.Iterator;
import java.util.Stack;

/**
 * Assigns the final rank assignment for a DirectedGraph with an initial
 * feasible spanning tree.
 * 
 * @author Randy Hudson
 * @since 2.1.2
 */
@SuppressWarnings({"rawtypes", "unchecked"})
class RankAssignmentSolver extends SpanningTreeVisitor {

    DirectedGraph graph;
    EdgeList spanningTree;
    boolean searchDirection;

    int depthFirstCutValue(Edge edge, int count) {
        Node n = getTreeTail(edge);
        setTreeMin(n, count);
        int cutvalue = 0;
        int multiplier = (edge.target == n) ? 1 : -1;
        EdgeList list;

        list = n.outgoing;
        Edge e;
        for (int i = 0; i < list.size(); i++) {
            e = list.getEdge(i);
            if (e.tree && e != edge) {
                count = depthFirstCutValue(e, count);
                cutvalue += (e.cut - e.weight) * multiplier;
            } else {
                cutvalue -= e.weight * multiplier;
            }
        }
        list = n.incoming;
        for (int i = 0; i < list.size(); i++) {
            e = list.getEdge(i);
            if (e.tree && e != edge) {
                count = depthFirstCutValue(e, count);
                cutvalue -= (e.cut - e.weight) * multiplier;
            } else {
                cutvalue += e.weight * multiplier;
            }
        }

        edge.cut = cutvalue;
        if (cutvalue < 0)
            spanningTree.add(edge);
        setTreeMax(n, count);
        return count + 1;
    }

    /**
     * returns the Edge which should be entered.
     * 
     * @param branch
     * @return Edge
     */
    Edge enter(Node branch) {
        Node n;
        Edge result = null;
        int minSlack = Integer.MAX_VALUE;
        boolean incoming = getParentEdge(branch).target != branch;
        // searchDirection = !searchDirection;
        for (int i = 0; i < graph.nodes.size(); i++) {
            if (searchDirection)
                n = graph.nodes.getNode(i);
            else
                n = graph.nodes.getNode(graph.nodes.size() - 1 - i);
            if (subtreeContains(branch, n)) {
                EdgeList edges;
                if (incoming)
                    edges = n.incoming;
                else
                    edges = n.outgoing;
                for (int j = 0; j < edges.size(); j++) {
                    Edge e = edges.getEdge(j);
                    if (!subtreeContains(branch, e.opposite(n)) && !e.tree
                            && e.getSlack() < minSlack) {
                        result = e;
                        minSlack = e.getSlack();
                    }
                }
            }
        }
        return result;
    }

    int getTreeMax(Node n) {
        return n.workingInts[1];
    }

    int getTreeMin(Node n) {
        return n.workingInts[0];
    }

    void initCutValues() {
        Node root = graph.nodes.getNode(0);
        spanningTree = new EdgeList();
        Edge e;
        setTreeMin(root, 1);
        setTreeMax(root, 1);

        for (int i = 0; i < root.outgoing.size(); i++) {
            e = root.outgoing.getEdge(i);
            if (!getSpanningTreeChildren(root).contains(e))
                continue;
            setTreeMax(root, depthFirstCutValue(e, getTreeMax(root)));
        }
        for (int i = 0; i < root.incoming.size(); i++) {
            e = root.incoming.getEdge(i);
            if (!getSpanningTreeChildren(root).contains(e))
                continue;
            setTreeMax(root, depthFirstCutValue(e, getTreeMax(root)));
        }
    }

    Edge leave() {
        Edge result = null;
        Edge e;
        int minCut = 0;
        int weight = -1;
        for (int i = 0; i < spanningTree.size(); i++) {
            e = spanningTree.getEdge(i);
            if (e.cut < minCut) {
                result = e;
                minCut = result.cut;
                weight = result.weight;
            } else if (e.cut == minCut && e.weight > weight) {
                result = e;
                weight = result.weight;
            }
        }
        return result;
    }

    void networkSimplexLoop() {
        Edge leave, enter;
        int count = 0;
        while ((leave = leave()) != null && count < 900) {

            count++;

            Node leaveTail = getTreeTail(leave);
            Node leaveHead = getTreeHead(leave);

            enter = enter(leaveTail);
            if (enter == null)
                break;

            // Break the "leave" edge from the spanning tree
            getSpanningTreeChildren(leaveHead).remove(leave);
            setParentEdge(leaveTail, null);
            leave.tree = false;
            spanningTree.remove(leave);

            Node enterTail = enter.source;
            if (!subtreeContains(leaveTail, enterTail))
                // Oops, wrong end of the edge
                enterTail = enter.target;
            Node enterHead = enter.opposite(enterTail);

            // Prepare enterTail by making it the root of its sub-tree
            updateSubgraph(enterTail);

            // Add "enter" edge to the spanning tree
            getSpanningTreeChildren(enterHead).add(enter);
            setParentEdge(enterTail, enter);
            enter.tree = true;

            repairCutValues(enter);

            Node commonAncestor = enterHead;

            while (!subtreeContains(commonAncestor, leaveHead)) {
                repairCutValues(getParentEdge(commonAncestor));
                commonAncestor = getTreeParent(commonAncestor);
            }
            while (leaveHead != commonAncestor) {
                repairCutValues(getParentEdge(leaveHead));
                leaveHead = getTreeParent(leaveHead);
            }
            updateMinMax(commonAncestor, getTreeMin(commonAncestor));
            tightenEdge(enter);
        }
    }

    void repairCutValues(Edge edge) {
        spanningTree.remove(edge);
        Node n = getTreeTail(edge);
        int cutvalue = 0;
        int multiplier = (edge.target == n) ? 1 : -1;
        EdgeList list;

        list = n.outgoing;
        Edge e;
        for (int i = 0; i < list.size(); i++) {
            e = list.getEdge(i);
            if (e.tree && e != edge)
                cutvalue += (e.cut - e.weight) * multiplier;
            else
                cutvalue -= e.weight * multiplier;
        }
        list = n.incoming;
        for (int i = 0; i < list.size(); i++) {
            e = list.getEdge(i);
            if (e.tree && e != edge)
                cutvalue -= (e.cut - e.weight) * multiplier;
            else
                cutvalue += e.weight * multiplier;
        }

        edge.cut = cutvalue;
        if (cutvalue < 0)
            spanningTree.add(edge);
    }

    void setTreeMax(Node n, int value) {
        n.workingInts[1] = value;
    }

    void setTreeMin(Node n, int value) {
        n.workingInts[0] = value;
    }

    boolean subtreeContains(Node parent, Node child) {
        return parent.workingInts[0] <= child.workingInts[1]
                && child.workingInts[1] <= parent.workingInts[1];
    }

    void tightenEdge(Edge edge) {
        Node tail = getTreeTail(edge);
        int delta = edge.getSlack();
        if (tail == edge.target)
            delta = -delta;
        Node n;
        for (int i = 0; i < graph.nodes.size(); i++) {
            n = graph.nodes.getNode(i);
            if (subtreeContains(tail, n))
                n.rank += delta;
        }
    }

    int updateMinMax(Node root, int count) {
        setTreeMin(root, count);
        EdgeList edges = getSpanningTreeChildren(root);
        for (int i = 0; i < edges.size(); i++)
            count = updateMinMax(getTreeTail(edges.getEdge(i)), count);
        setTreeMax(root, count);
        return count + 1;
    }

    void updateSubgraph(Node root) {
        Edge flip = getParentEdge(root);
        if (flip != null) {
            Node rootParent = getTreeParent(root);
            getSpanningTreeChildren(rootParent).remove(flip);
            updateSubgraph(rootParent);
            setParentEdge(root, null);
            setParentEdge(rootParent, flip);
            repairCutValues(flip);
            getSpanningTreeChildren(root).add(flip);
        }
    }

    @Override
    public void visit(DirectedGraph graph) {
        this.graph = graph;
        initCutValues();
        networkSimplexLoop();
        if (graph.forestRoot == null)
            graph.nodes.normalizeRanks();
        else
            normalizeForest();
    }

    private void normalizeForest() {
        NodeList tree = new NodeList();
        graph.nodes.resetFlags();
        graph.forestRoot.flag = true;
        EdgeList rootEdges = graph.forestRoot.outgoing;
        Stack stack = new Stack();
        for (int i = 0; i < rootEdges.size(); i++) {
            Node node = rootEdges.getEdge(i).target;
            node.flag = true;
            stack.push(node);
            while (!stack.isEmpty()) {
                node = (Node) stack.pop();
                tree.add(node);
                Iterator neighbors = node.iteratorNeighbors();
                while (neighbors.hasNext()) {
                    Node neighbor = (Node) neighbors.next();
                    if (!neighbor.flag) {
                        neighbor.flag = true;
                        stack.push(neighbor);
                    }
                }
            }
            tree.normalizeRanks();
            tree.clear();
        }
    }

}
