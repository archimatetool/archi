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

/**
 * Places nodes into ranks for a compound directed graph. If a subgraph spans a
 * rank without any nodes which belong to that rank, a bridge node is inserted
 * to prevent nodes from violating the subgraph boundary.
 * 
 * @author Randy Hudson
 * @since 2.1.2
 */
@SuppressWarnings({"rawtypes", "unchecked"})
class CompoundPopulateRanks extends PopulateRanks {

    @Override
    public void visit(DirectedGraph g) {
        CompoundDirectedGraph graph = (CompoundDirectedGraph) g;

        /**
         * Remove long containment edges at this point so they don't affect
         * MinCross.
         */
        Iterator containment = graph.containment.iterator();
        while (containment.hasNext()) {
            Edge e = (Edge) containment.next();
            if (e.getSlack() > 0) {
                graph.removeEdge(e);
                containment.remove();
            }
        }

        super.visit(g);
        NodeList subgraphs = graph.subgraphs;
        for (int i = 0; i < subgraphs.size(); i++) {
            Subgraph subgraph = (Subgraph) subgraphs.get(i);
            bridgeSubgraph(subgraph, graph);
        }
    }

    /**
     * @param subgraph
     */
    @SuppressWarnings("deprecation")
    private void bridgeSubgraph(Subgraph subgraph, CompoundDirectedGraph g) {
        int offset = subgraph.head.rank;
        boolean occupied[] = new boolean[subgraph.tail.rank
                - subgraph.head.rank + 1];
        Node bridge[] = new Node[occupied.length];

        for (int i = 0; i < subgraph.members.size(); i++) {
            Node n = (Node) subgraph.members.get(i);
            if (n instanceof Subgraph) {
                Subgraph s = (Subgraph) n;
                for (int r = s.head.rank; r <= s.tail.rank; r++)
                    occupied[r - offset] = true;
            } else
                occupied[n.rank - offset] = true;
        }

        for (int i = 0; i < bridge.length; i++) {
            if (!occupied[i]) {
                Node br = bridge[i] = new Node("bridge", subgraph); //$NON-NLS-1$
                br.rank = i + offset;
                br.height = br.width = 0;
                br.nestingIndex = subgraph.nestingIndex;
                g.ranks.getRank(br.rank).add(br);
                g.nodes.add(br);
            }
        }
    }

}
