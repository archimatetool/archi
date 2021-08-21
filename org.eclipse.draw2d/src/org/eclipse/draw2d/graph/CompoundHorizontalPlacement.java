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

import java.util.HashSet;
import java.util.Set;

/**
 * Calculates the X-coordinates for nodes in a compound directed graph.
 * 
 * @author Randy Hudson
 * @since 2.1.2
 */
@SuppressWarnings({"rawtypes", "unchecked"})
class CompoundHorizontalPlacement extends HorizontalPlacement {

    class LeftRight {
        // $TODO Delete and use NodePair class, equivalent
        Object left, right;

        LeftRight(Object l, Object r) {
            left = l;
            right = r;
        }

        @Override
        public boolean equals(Object obj) {
            LeftRight entry = (LeftRight) obj;
            return entry.left.equals(left) && entry.right.equals(right);
        }

        @Override
        public int hashCode() {
            return left.hashCode() ^ right.hashCode();
        }
    }

    Set entries = new HashSet();

    /**
     * @see org.eclipse.graph.HorizontalPlacement#applyGPrime()
     */
    @Override
    void applyGPrime() {
        super.applyGPrime();
        NodeList subgraphs = ((CompoundDirectedGraph) graph).subgraphs;
        for (int i = 0; i < subgraphs.size(); i++) {
            Subgraph s = (Subgraph) subgraphs.get(i);
            s.x = s.left.x;
            s.width = s.right.x + s.right.width - s.x;
        }
    }

    /**
     * @see HorizontalPlacement#buildRankSeparators(RankList)
     */
    @SuppressWarnings("deprecation")
    @Override
    void buildRankSeparators(RankList ranks) {
        CompoundDirectedGraph g = (CompoundDirectedGraph) graph;

        Rank rank;
        for (int row = 0; row < g.ranks.size(); row++) {
            rank = g.ranks.getRank(row);
            Node n = null, prev = null;
            for (int j = 0; j < rank.size(); j++) {
                n = rank.getNode(j);
                if (prev == null) {
                    Node left = addSeparatorsLeft(n, null);
                    if (left != null) {
                        Edge e = new Edge(graphLeft, getPrime(left), 0, 0);
                        prime.edges.add(e);
                        e.delta = graph.getPadding(n).left
                                + graph.getMargin().left;
                    }

                } else {
                    Subgraph s = GraphUtilities.getCommonAncestor(prev, n);
                    Node left = addSeparatorsRight(prev, s);
                    Node right = addSeparatorsLeft(n, s);
                    createEdge(left, right);
                }
                prev = n;
            }
            if (n != null)
                addSeparatorsRight(n, null);
        }
    }

    void createEdge(Node left, Node right) {
        LeftRight entry = new LeftRight(left, right);
        if (entries.contains(entry))
            return;
        entries.add(entry);
        int separation = left.width + graph.getPadding(left).right
                + graph.getPadding(right).left;
        prime.edges
                .add(new Edge(getPrime(left), getPrime(right), separation, 0));
    }

    Node addSeparatorsLeft(Node n, Subgraph graph) {
        Subgraph parent = n.getParent();
        while (parent != graph && parent != null) {
            createEdge(getLeft(parent), n);
            n = parent.left;
            parent = parent.getParent();
        }
        return n;
    }

    Node addSeparatorsRight(Node n, Subgraph graph) {
        Subgraph parent = n.getParent();
        while (parent != graph && parent != null) {
            createEdge(n, getRight(parent));
            n = parent.right;
            parent = parent.getParent();
        }
        return n;
    }

    Node getLeft(Subgraph s) {
        if (s.left == null) {
            s.left = new SubgraphBoundary(s, graph.getPadding(s), 1);
            s.left.rank = (s.head.rank + s.tail.rank) / 2;

            Node head = getPrime(s.head);
            Node tail = getPrime(s.tail);
            Node left = getPrime(s.left);
            Node right = getPrime(getRight(s));
            prime.edges.add(new Edge(left, right, s.width, 0));
            prime.edges.add(new Edge(left, head, 0, 1));
            prime.edges.add(new Edge(head, right, 0, 1));
            prime.edges.add(new Edge(left, tail, 0, 1));
            prime.edges.add(new Edge(tail, right, 0, 1));
        }
        return s.left;
    }

    Node getRight(Subgraph s) {
        if (s.right == null) {
            s.right = new SubgraphBoundary(s, graph.getPadding(s), 3);
            s.right.rank = (s.head.rank + s.tail.rank) / 2;
        }
        return s.right;
    }

    Node getPrime(Node n) {
        Node nPrime = get(n);
        if (nPrime == null) {
            nPrime = new Node(n);
            prime.nodes.add(nPrime);
            map(n, nPrime);
        }
        return nPrime;
    }

    @Override
    public void visit(DirectedGraph g) {
        super.visit(g);
    }

}
