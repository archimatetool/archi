/*******************************************************************************
 * Copyright (c) 2005, 2010 IBM Corporation and others.
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
 * Contains the information from all edges going from a given cluster to some
 * other cluster. An edge with minimal slack as chosen to maintain the link
 * between clusters. The weight and any slack more than the minimal edge's slack
 * is tracked for all other edges.
 * 
 * @since 3.1
 */
class CollapsedEdges {

    /**
     * The total weight of the collapsed edges.
     */
    int collapsedWeight;
    int collapsedCount;

    /**
     * The total amount of weighted difference in the collapsed edges slack and
     * the tightest edge's slack.
     */
    int overage;
    int unOverage;
    Edge tightestEdge;

    CollapsedEdges(Edge edge) {
        tightestEdge = edge;
        collapsedWeight = edge.weight;
        collapsedCount++;
    }

    public int getWeightedPull() {
        return tightestEdge.getSlack() * collapsedWeight + overage;
    }

    public boolean isTight() {
        return tightestEdge.getSlack() == 0;
    }

    /**
     * Compares the given edge to the current tightest edge. If the given edge
     * is tighter than the current, the current tightest is returned. Otherwise,
     * the edge itself is returned. The returned edge would be the one to remove
     * from the graph.
     * 
     * @param candidate
     *            another edge
     * @return the edge which is not the tightest edge
     * @since 3.1
     */
    Edge processEdge(Edge candidate) {
        collapsedCount++;
        if (candidate.getSlack() < tightestEdge.getSlack()) {
            overage += collapsedWeight
                    * (tightestEdge.getSlack() - candidate.getSlack());
            Edge temp = tightestEdge;
            tightestEdge = candidate;
            collapsedWeight += candidate.weight;
            return temp;
        } else {
            int over = candidate.getSlack() - tightestEdge.getSlack();
            unOverage += over;
            overage += candidate.weight * over;
            collapsedWeight += candidate.weight;
            return candidate;
        }
    }
}