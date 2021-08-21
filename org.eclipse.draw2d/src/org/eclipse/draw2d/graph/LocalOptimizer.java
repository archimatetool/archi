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
 * This graph visitor examines all adjacent pairs of nodes and determines if
 * swapping the two nodes provides improved graph aesthetics.
 * 
 * @author Daniel Lee
 * @since 2.1.2
 */
@SuppressWarnings({"unchecked", "deprecation"})
class LocalOptimizer extends GraphVisitor {

    boolean shouldSwap(Node current, Node next) {
        if (GraphUtilities.isConstrained(current, next))
            return false;
        int crossCount = 0;
        int invertedCrossCount = 0;

        EdgeList currentEdges = current.incoming;
        EdgeList nextEdges = next.incoming;
        int rank = current.rank - 1;
        int iCurrent, iNext;

        for (int i = 0; i < currentEdges.size(); i++) {
            Edge currentEdge = currentEdges.getEdge(i);
            iCurrent = currentEdge.getIndexForRank(rank);
            for (int j = 0; j < nextEdges.size(); j++) {
                iNext = nextEdges.getEdge(j).getIndexForRank(rank);
                if (iNext < iCurrent)
                    crossCount++;
                else if (iNext > iCurrent)
                    invertedCrossCount++;
                else {
                    // edges go to the same location
                    int offsetDiff = nextEdges.getEdge(j).getSourceOffset()
                            - currentEdge.getSourceOffset();
                    if (offsetDiff < 0)
                        crossCount++;
                    else if (offsetDiff > 0)
                        invertedCrossCount++;
                }
            }
        }

        currentEdges = current.outgoing;
        nextEdges = next.outgoing;
        rank = current.rank + 1;

        for (int i = 0; i < currentEdges.size(); i++) {
            Edge currentEdge = currentEdges.getEdge(i);
            iCurrent = currentEdge.getIndexForRank(rank);
            for (int j = 0; j < nextEdges.size(); j++) {
                iNext = nextEdges.getEdge(j).getIndexForRank(rank);
                if (iNext < iCurrent)
                    crossCount++;
                else if (iNext > iCurrent)
                    invertedCrossCount++;
                else {
                    // edges go to the same location
                    int offsetDiff = nextEdges.getEdge(j).getTargetOffset()
                            - currentEdge.getTargetOffset();
                    if (offsetDiff < 0)
                        crossCount++;
                    else if (offsetDiff > 0)
                        invertedCrossCount++;
                }
            }
        }
        if (invertedCrossCount < crossCount)
            return true;
        return false;
    }

    private void swapNodes(Node current, Node next, Rank rank) {
        int index = rank.indexOf(current);
        rank.set(index + 1, current);
        rank.set(index, next);
        index = current.index;
        current.index = next.index;
        next.index = index;
    }

    /**
     * @see GraphVisitor#visit(org.eclipse.draw2d.graph.DirectedGraph)
     */
    @Override
    public void visit(DirectedGraph g) {
        boolean flag;
        do {
            flag = false;
            for (int r = 0; r < g.ranks.size(); r++) {
                Rank rank = g.ranks.getRank(r);
                for (int n = 0; n < rank.count() - 1; n++) {
                    Node currentNode = rank.getNode(n);
                    Node nextNode = rank.getNode(n + 1);
                    if (shouldSwap(currentNode, nextNode)) {
                        swapNodes(currentNode, nextNode, rank);
                        flag = true;
                        n = Math.max(0, n - 2);
                    }
                }
            }
        } while (flag);
    }

}
