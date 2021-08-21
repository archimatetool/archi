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
 * This visitor eliminates cycles in the graph via a modified implementation of
 * the greedy cycle removal algorithm for directed graphs. The algorithm has
 * been modified to handle the presence of Subgraphs and compound cycles which
 * may result. This algorithm determines a set of edges which can be inverted
 * and result in a graph without compound cycles.
 * 
 * @author Daniel Lee
 * @author Randy Hudson
 * @since 2.1.2
 */
@SuppressWarnings("unchecked")
class CompoundBreakCycles extends GraphVisitor {

    /*
     * Caches all nodes in the graph. Used in identifying cycles and in cycle
     * removal. Flag field indicates "presence". If true, the node has been
     * removed from the list.
     */
    private NodeList graphNodes;
    private NodeList sL = new NodeList();

    private boolean allFlagged(NodeList nodes) {
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.getNode(i).flag == false)
                return false;
        }
        return true;
    }

    private int buildNestingTreeIndices(NodeList nodes, int base) {
        for (int i = 0; i < nodes.size(); i++) {
            Node node = (Node) nodes.get(i);
            if (node instanceof Subgraph) {
                Subgraph s = (Subgraph) node;
                s.nestingTreeMin = base;
                base = buildNestingTreeIndices(s.members, base);
            }
            node.nestingIndex = base++;
        }
        return base++;
    }

    private boolean canBeRemoved(Node n) {
        return !n.flag && getChildCount(n) == 0;
    }

    private boolean changeInDegree(Node n, int delta) {
        return (n.workingInts[1] += delta) == 0;
    }

    private boolean changeOutDegree(Node n, int delta) {
        return (n.workingInts[2] += delta) == 0;
    }

    /*
     * Execution of the modified greedy cycle removal algorithm.
     */
    private void cycleRemove(NodeList children) {
        NodeList sR = new NodeList();
        do {
            findSinks(children, sR);
            findSources(children);

            // all sinks and sources added, find node with highest
            // outDegree - inDegree
            Node max = findNodeWithMaxDegree(children);
            if (max != null) {
                for (int i = 0; i < children.size(); i++) {
                    Node child = (Node) children.get(i);
                    if (child.flag)
                        continue;
                    if (child == max)
                        restoreSinks(max, sR);
                    else
                        restoreSources(child);
                }
                remove(max);
            }
        } while (!allFlagged(children));
        while (!sR.isEmpty())
            sL.add(sR.remove(sR.size() - 1));
    }

    private void findInitialSinks(NodeList children, NodeList sinks) {
        for (int i = 0; i < children.size(); i++) {
            Node node = children.getNode(i);
            if (node.flag)
                continue;
            if (isSink(node) && canBeRemoved(node)) {
                sinks.add(node);
                node.flag = true;
            }
            if (node instanceof Subgraph)
                findInitialSinks(((Subgraph) node).members, sinks);
        }
    }

    private void findInitialSources(NodeList children, NodeList sources) {
        for (int i = 0; i < children.size(); i++) {
            Node node = children.getNode(i);
            if (isSource(node) && canBeRemoved(node)) {
                sources.add(node);
                node.flag = true;
            }
            if (node instanceof Subgraph)
                findInitialSources(((Subgraph) node).members, sources);
        }
    }

    private Node findNodeWithMaxDegree(NodeList nodes) {
        int max = Integer.MIN_VALUE;
        Node maxNode = null;

        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.getNode(i);
            if (node.flag)
                continue;
            int degree = getNestedOutDegree(node) - getNestedInDegree(node);
            if (degree >= max && node.flag == false) {
                max = degree;
                maxNode = node;
            }
        }
        return maxNode;
    }

    /*
     * Finds all sinks in graphNodes and adds them to the passed NodeList
     */
    private void findSinks(NodeList children, NodeList rightList) {
        // NodeList rightList = new NodeList();
        NodeList sinks = new NodeList();
        findInitialSinks(children, sinks);
        while (!sinks.isEmpty()) {
            Node sink = sinks.getNode(sinks.size() - 1);
            rightList.add(sink);
            sinks.remove(sink);
            removeSink(sink, sinks);

            // Check to see if the removal has made the parent node a sink
            if (sink.getParent() != null) {
                Node parent = sink.getParent();
                setChildCount(parent, getChildCount(parent) - 1);
                if (isSink(parent) && canBeRemoved(parent)) {
                    sinks.add(parent);
                    parent.flag = true;
                }
            }
        }
    }

    /*
     * Finds all sources in graphNodes and adds them to the sL NodeList.
     */
    private void findSources(NodeList children) {
        NodeList sources = new NodeList();
        findInitialSources(children, sources);
        while (!sources.isEmpty()) {
            Node source = sources.getNode(sources.size() - 1);
            sL.add(source);
            sources.remove(source);
            removeSource(source, sources);

            // Check to see if the removal has made the parent node a source
            if (source.getParent() != null) {
                Node parent = source.getParent();
                setChildCount(parent, getChildCount(parent) - 1);
                if (isSource(parent) && canBeRemoved(parent)) {
                    sources.add(parent);
                    parent.flag = true;
                }
            }
        }
    }

    private int getChildCount(Node n) {
        return n.workingInts[3];
    }

    private int getInDegree(Node n) {
        return n.workingInts[1];
    }

    private int getNestedInDegree(Node n) {
        int result = getInDegree(n);
        if (n instanceof Subgraph) {
            Subgraph s = (Subgraph) n;
            for (int i = 0; i < s.members.size(); i++)
                if (!s.members.getNode(i).flag)
                    result += getInDegree(s.members.getNode(i));
        }
        return result;
    }

    private int getNestedOutDegree(Node n) {
        int result = getOutDegree(n);
        if (n instanceof Subgraph) {
            Subgraph s = (Subgraph) n;
            for (int i = 0; i < s.members.size(); i++)
                if (!s.members.getNode(i).flag)
                    result += getOutDegree(s.members.getNode(i));
        }
        return result;
    }

    private int getOrderIndex(Node n) {
        return n.workingInts[0];
    }

    private int getOutDegree(Node n) {
        return n.workingInts[2];
    }

    private void initializeDegrees(DirectedGraph g) {
        g.nodes.resetFlags();
        g.edges.resetFlags(false);
        for (int i = 0; i < g.nodes.size(); i++) {
            Node n = g.nodes.getNode(i);
            setInDegree(n, n.incoming.size());
            setOutDegree(n, n.outgoing.size());
            if (n instanceof Subgraph)
                setChildCount(n, ((Subgraph) n).members.size());
            else
                setChildCount(n, 0);
        }
    }

    @SuppressWarnings("deprecation")
    private void invertEdges(DirectedGraph g) {
        // Assign order indices
        int orderIndex = 0;
        for (int i = 0; i < sL.size(); i++) {
            setOrderIndex(sL.getNode(i), orderIndex++);
        }
        // Invert edges that are causing a cycle
        for (int i = 0; i < g.edges.size(); i++) {
            Edge e = g.edges.getEdge(i);
            if (getOrderIndex(e.source) > getOrderIndex(e.target)
                    && !e.source.isNested(e.target)
                    && !e.target.isNested(e.source)) {
                e.invert();
                e.isFeedback = true;
            }
        }
    }

    /**
     * Removes all edges connecting the given subgraph to other nodes outside of
     * it.
     * 
     * @param s
     * @param n
     */
    private void isolateSubgraph(Subgraph subgraph, Node member) {
        Edge edge = null;
        for (int i = 0; i < member.incoming.size(); i++) {
            edge = member.incoming.getEdge(i);
            if (!subgraph.isNested(edge.source) && !edge.flag)
                removeEdge(edge);
        }
        for (int i = 0; i < member.outgoing.size(); i++) {
            edge = member.outgoing.getEdge(i);
            if (!subgraph.isNested(edge.target) && !edge.flag)
                removeEdge(edge);
        }
        if (member instanceof Subgraph) {
            NodeList members = ((Subgraph) member).members;
            for (int i = 0; i < members.size(); i++)
                isolateSubgraph(subgraph, members.getNode(i));
        }
    }

    private boolean isSink(Node n) {
        return getOutDegree(n) == 0
                && (n.getParent() == null || isSink(n.getParent()));
    }

    private boolean isSource(Node n) {
        return getInDegree(n) == 0
                && (n.getParent() == null || isSource(n.getParent()));
    }

    private void remove(Node n) {
        n.flag = true;
        if (n.getParent() != null)
            setChildCount(n.getParent(), getChildCount(n.getParent()) - 1);
        removeSink(n, null);
        removeSource(n, null);
        sL.add(n);
        if (n instanceof Subgraph) {
            Subgraph s = (Subgraph) n;
            isolateSubgraph(s, s);
            cycleRemove(s.members);
        }
    }

    private boolean removeEdge(Edge e) {
        if (e.flag)
            return false;
        e.flag = true;
        changeOutDegree(e.source, -1);
        changeInDegree(e.target, -1);
        return true;
    }

    /**
     * Removes all edges between a parent and any of its children or
     * descendants.
     */
    private void removeParentChildEdges(DirectedGraph g) {
        for (int i = 0; i < g.edges.size(); i++) {
            Edge e = g.edges.getEdge(i);
            if (e.source.isNested(e.target) || e.target.isNested(e.source))
                removeEdge(e);
        }
    }

    private void removeSink(Node sink, NodeList allSinks) {
        for (int i = 0; i < sink.incoming.size(); i++) {
            Edge e = sink.incoming.getEdge(i);
            if (!e.flag) {
                removeEdge(e);
                Node source = e.source;
                if (allSinks != null && isSink(source) && canBeRemoved(source)) {
                    allSinks.add(source);
                    source.flag = true;
                }
            }
        }
    }

    private void removeSource(Node n, NodeList allSources) {
        for (int i = 0; i < n.outgoing.size(); i++) {
            Edge e = n.outgoing.getEdge(i);
            if (!e.flag) {
                e.flag = true;
                changeInDegree(e.target, -1);
                changeOutDegree(e.source, -1);

                Node target = e.target;
                if (allSources != null && isSource(target)
                        && canBeRemoved(target)) {
                    allSources.add(target);
                    target.flag = true;
                }
            }
        }
    }

    /**
     * Restores an edge if it has been removed, and both of its nodes are not
     * removed.
     * 
     * @param e
     *            the edge
     * @return <code>true</code> if the edge was restored
     */
    private boolean restoreEdge(Edge e) {
        if (!e.flag || e.source.flag || e.target.flag)
            return false;
        e.flag = false;
        changeOutDegree(e.source, 1);
        changeInDegree(e.target, 1);
        return true;
    }

    /**
     * Brings back all nodes nested in the given node.
     * 
     * @param node
     *            the node to restore
     * @param sr
     *            current sinks
     */
    private void restoreSinks(Node node, NodeList sR) {
        if (node.flag && sR.contains(node)) {
            node.flag = false;
            if (node.getParent() != null)
                setChildCount(node.getParent(),
                        getChildCount(node.getParent()) + 1);
            sR.remove(node);
            for (int i = 0; i < node.incoming.size(); i++) {
                Edge e = node.incoming.getEdge(i);
                restoreEdge(e);
            }
            for (int i = 0; i < node.outgoing.size(); i++) {
                Edge e = node.outgoing.getEdge(i);
                restoreEdge(e);
            }
        }
        if (node instanceof Subgraph) {
            Subgraph s = (Subgraph) node;
            for (int i = 0; i < s.members.size(); i++) {
                Node member = s.members.getNode(i);
                restoreSinks(member, sR);
            }
        }
    }

    /**
     * Brings back all nodes nested in the given node.
     * 
     * @param node
     *            the node to restore
     * @param sr
     *            current sinks
     */
    private void restoreSources(Node node) {
        if (node.flag && sL.contains(node)) {
            node.flag = false;
            if (node.getParent() != null)
                setChildCount(node.getParent(),
                        getChildCount(node.getParent()) + 1);
            sL.remove(node);
            for (int i = 0; i < node.incoming.size(); i++) {
                Edge e = node.incoming.getEdge(i);
                restoreEdge(e);
            }
            for (int i = 0; i < node.outgoing.size(); i++) {
                Edge e = node.outgoing.getEdge(i);
                restoreEdge(e);
            }
        }
        if (node instanceof Subgraph) {
            Subgraph s = (Subgraph) node;
            for (int i = 0; i < s.members.size(); i++) {
                Node member = s.members.getNode(i);
                restoreSources(member);
            }
        }
    }

    @Override
    public void revisit(DirectedGraph g) {
        for (int i = 0; i < g.edges.size(); i++) {
            Edge e = g.edges.getEdge(i);
            if (e.isFeedback())
                e.invert();
        }
    }

    private void setChildCount(Node n, int count) {
        n.workingInts[3] = count;
    }

    private void setInDegree(Node n, int deg) {
        n.workingInts[1] = deg;
    }

    private void setOrderIndex(Node n, int index) {
        n.workingInts[0] = index;
    }

    private void setOutDegree(Node n, int deg) {
        n.workingInts[2] = deg;
    }

    /**
     * @see GraphVisitor#visit(org.eclipse.draw2d.graph.DirectedGraph)
     */
    @Override
    public void visit(DirectedGraph g) {
        initializeDegrees(g);
        graphNodes = g.nodes;

        NodeList roots = new NodeList();
        for (int i = 0; i < graphNodes.size(); i++) {
            if (graphNodes.getNode(i).getParent() == null)
                roots.add(graphNodes.getNode(i));
        }
        buildNestingTreeIndices(roots, 0);
        removeParentChildEdges(g);
        cycleRemove(roots);
        invertEdges(g);
    }

}
