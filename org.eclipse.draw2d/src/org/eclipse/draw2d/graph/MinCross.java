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
 * Sweeps up and down the ranks rearranging them so as to reduce edge crossings.
 * 
 * @author Randy Hudson
 * @since 2.1.2
 */
@SuppressWarnings("deprecation")
class MinCross extends GraphVisitor {

    static final int MAX = 45;

    private DirectedGraph g;
    private RankSorter sorter = new RankSorter();

    public MinCross() {
    }

    /**
     * @since 3.1
     */
    public MinCross(RankSorter sorter) {
        setRankSorter(sorter);
    }

    public void setRankSorter(RankSorter sorter) {
        this.sorter = sorter;
    }

    void solve() {
        Rank rank;
        for (int loop = 0; loop < MAX; loop++) {
            for (int row = 1; row < g.ranks.size(); row++) {
                rank = g.ranks.getRank(row);
                sorter.sortRankIncoming(g, rank, row, (double) loop / MAX);
            }
            if (loop == MAX - 1)
                continue;
            for (int row = g.ranks.size() - 2; row >= 0; row--) {
                rank = g.ranks.getRank(row);
                sorter.sortRankOutgoing(g, rank, row, (double) loop / MAX);
            }
        }
    }

    /**
     * @see GraphVisitor#visit(org.eclipse.draw2d.graph.DirectedGraph)
     */
    @Override
    public void visit(DirectedGraph g) {
        sorter.init(g);
        this.g = g;
        solve();
        sorter.optimize(g);
    }

}
