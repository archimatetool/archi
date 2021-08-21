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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Sorts nodes in a compound directed graph.
 * 
 * @author Randy Hudson
 * @since 2.1.2
 */
@SuppressWarnings({"rawtypes", "unchecked"})
class CompoundRankSorter extends RankSorter {

    static class RowEntry {
        double contribution;
        int count;

        void reset() {
            count = 0;
            contribution = 0;
        }
    }

    static class RowKey {
        int rank;
        Subgraph s;

        RowKey() {
        }

        RowKey(Subgraph s, int rank) {
            this.s = s;
            this.rank = rank;
        }

        @Override
        public boolean equals(Object obj) {
            RowKey rp = (RowKey) obj;
            return rp.s == s && rp.rank == rank;
        }

        @Override
        public int hashCode() {
            return s.hashCode() ^ (rank * 31);
        }
    }

    boolean init;
    RowKey key = new RowKey();

    Map map = new HashMap();

    void addRowEntry(Subgraph s, int row) {
        key.s = s;
        key.rank = row;
        if (!map.containsKey(key))
            map.put(new RowKey(s, row), new RowEntry());
    }

    @Override
    protected void assignIncomingSortValues() {
        super.assignIncomingSortValues();
    }

    @Override
    protected void assignOutgoingSortValues() {
        super.assignOutgoingSortValues();
    }

    @Override
    void optimize(DirectedGraph g) {
        CompoundDirectedGraph graph = (CompoundDirectedGraph) g;
        Iterator containment = graph.containment.iterator();
        while (containment.hasNext())
            graph.removeEdge((Edge) containment.next());
        graph.containment.clear();
        new LocalOptimizer().visit(graph);
    }

    @Override
    double evaluateNodeOutgoing() {
        double result = super.evaluateNodeOutgoing();
        // result += Math.random() * rankSize * (1.0 - progress) / 3.0;
        if (progress > 0.2) {
            Subgraph s = node.getParent();
            double connectivity = mergeConnectivity(s, node.rank + 1, result,
                    progress);
            result = connectivity;
        }
        return result;
    }

    @Override
    double evaluateNodeIncoming() {
        double result = super.evaluateNodeIncoming();
        // result += Math.random() * rankSize * (1.0 - progress) / 3.0;
        if (progress > 0.2) {
            Subgraph s = node.getParent();
            double connectivity = mergeConnectivity(s, node.rank - 1, result,
                    progress);
            result = connectivity;
        }
        return result;
    }

    double mergeConnectivity(Subgraph s, int row, double result,
            double scaleFactor) {
        while (s != null && getRowEntry(s, row) == null)
            s = s.getParent();
        if (s != null) {
            RowEntry entry = getRowEntry(s, row);
            double connectivity = entry.contribution / entry.count;
            result = connectivity * 0.3 + (0.7) * result;
            s = s.getParent();
        }
        return result;
    }

    RowEntry getRowEntry(Subgraph s, int row) {
        key.s = s;
        key.rank = row;
        return (RowEntry) map.get(key);
    }

    @SuppressWarnings("deprecation")
    void copyConstraints(NestingTree tree) {
        if (tree.subgraph != null)
            tree.sortValue = tree.subgraph.rowOrder;
        for (int i = 0; i < tree.contents.size(); i++) {
            Object child = tree.contents.get(i);
            if (child instanceof Node) {
                Node n = (Node) child;
                n.sortValue = n.rowOrder;
            } else {
                copyConstraints((NestingTree) child);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void init(DirectedGraph g) {
        super.init(g);
        init = true;

        for (int row = 0; row < g.ranks.size(); row++) {
            Rank rank = g.ranks.getRank(row);

            NestingTree tree = NestingTree.buildNestingTreeForRank(rank);
            copyConstraints(tree);
            tree.recursiveSort(true);
            rank.clear();
            tree.repopulateRank(rank);

            for (int j = 0; j < rank.count(); j++) {
                Node n = rank.getNode(j);
                Subgraph s = n.getParent();
                while (s != null) {
                    addRowEntry(s, row);
                    s = s.getParent();
                }
            }
        }
    }

    @Override
    protected void postSort() {
        super.postSort();
        if (init)
            updateRank(rank);
    }

    void updateRank(Rank rank) {
        for (int j = 0; j < rank.count(); j++) {
            Node n = rank.getNode(j);
            Subgraph s = n.getParent();
            while (s != null) {
                getRowEntry(s, currentRow).reset();
                s = s.getParent();
            }
        }
        for (int j = 0; j < rank.count(); j++) {
            Node n = rank.getNode(j);
            Subgraph s = n.getParent();
            while (s != null) {
                RowEntry entry = getRowEntry(s, currentRow);
                entry.count++;
                entry.contribution += n.index;
                s = s.getParent();
            }
        }
    }

}
