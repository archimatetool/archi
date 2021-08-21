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

import java.util.Collection;

/**
 * A group of nodes which are interlocked and cannot be separately placed.
 * 
 * @since 3.1
 */
@SuppressWarnings({"rawtypes", "unchecked"})
class NodeCluster extends NodeList {

    int hashCode = new Object().hashCode();

    boolean isSetMember;
    boolean isDirty;
    boolean leftDirty;
    boolean rightDirty;

    int leftFreedom;
    int rightFreedom;
    int leftNonzero;
    int rightNonzero;
    int leftCount = 0;
    int rightCount = 0;

    CollapsedEdges leftLinks[] = new CollapsedEdges[10];
    CollapsedEdges rightLinks[] = new CollapsedEdges[10];
    NodeCluster leftNeighbors[] = new NodeCluster[10];
    NodeCluster rightNeighbors[] = new NodeCluster[10];

    int effectivePull;
    int weightedTotal;
    int weightedDivisor;
    int unweightedTotal;
    int unweightedDivisor;

    void addLeftNeighbor(NodeCluster neighbor, CollapsedEdges link) {
        // Need to grow array in the following case
        if (leftNeighbors.length == leftCount) {
            int newSize = leftNeighbors.length * 2;

            NodeCluster newNeighbors[] = new NodeCluster[newSize];
            CollapsedEdges newLinks[] = new CollapsedEdges[newSize];

            System.arraycopy(leftNeighbors, 0, newNeighbors, 0,
                    leftNeighbors.length);
            System.arraycopy(leftLinks, 0, newLinks, 0, leftLinks.length);

            leftNeighbors = newNeighbors;
            leftLinks = newLinks;
        }
        leftNeighbors[leftCount] = neighbor;
        leftLinks[leftCount++] = link;
    }

    void addRightNeighbor(NodeCluster neighbor, CollapsedEdges link) {
        if (rightNeighbors.length == rightCount) {
            int newSize = rightNeighbors.length * 2;

            NodeCluster newNeighbors[] = new NodeCluster[newSize];
            CollapsedEdges newLinks[] = new CollapsedEdges[newSize];

            System.arraycopy(rightNeighbors, 0, newNeighbors, 0,
                    rightNeighbors.length);
            System.arraycopy(rightLinks, 0, newLinks, 0, rightLinks.length);

            rightNeighbors = newNeighbors;
            rightLinks = newLinks;
        }
        rightNeighbors[rightCount] = neighbor;
        rightLinks[rightCount++] = link;
    }

    public void adjustRank(int delta, Collection affected) {
        adjustRank(delta);
        NodeCluster neighbor;
        CollapsedEdges edges;
        for (int i = 0; i < leftCount; i++) {
            neighbor = leftNeighbors[i];
            if (neighbor.isSetMember)
                continue;
            edges = leftLinks[i];

            neighbor.weightedTotal += delta * edges.collapsedWeight;
            neighbor.unweightedTotal += delta * edges.collapsedCount;

            weightedTotal -= delta * edges.collapsedWeight;
            unweightedTotal -= delta * edges.collapsedCount;

            neighbor.rightDirty = leftDirty = true;
            if (!neighbor.isDirty) {
                neighbor.isDirty = true;
                affected.add(neighbor);
            }
        }
        for (int i = 0; i < rightCount; i++) {
            neighbor = rightNeighbors[i];
            if (neighbor.isSetMember)
                continue;
            edges = rightLinks[i];

            neighbor.weightedTotal += delta * edges.collapsedWeight;
            neighbor.unweightedTotal += delta * edges.collapsedCount;

            weightedTotal -= delta * edges.collapsedWeight;
            unweightedTotal -= delta * edges.collapsedCount;

            neighbor.leftDirty = rightDirty = true;
            if (!neighbor.isDirty) {
                neighbor.isDirty = true;
                affected.add(neighbor);
            }
        }
        isDirty = true;
        affected.add(this);
    }

    @Override
    public boolean equals(Object o) {
        return o == this;
    }

    CollapsedEdges getLeftNeighbor(NodeCluster neighbor) {
        for (int i = 0; i < leftCount; i++) {
            if (leftNeighbors[i] == neighbor)
                return leftLinks[i];
        }
        return null;
    }

    int getPull() {
        return effectivePull;
    }

    CollapsedEdges getRightNeighbor(NodeCluster neighbor) {
        for (int i = 0; i < rightCount; i++) {
            if (rightNeighbors[i] == neighbor)
                return rightLinks[i];
        }
        return null;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    /**
     * Initializes pull and freedom values.
     */
    void initValues() {
        weightedTotal = 0;
        weightedDivisor = 0;
        unweightedTotal = 0;
        int slack;

        leftNonzero = rightNonzero = leftFreedom = rightFreedom = Integer.MAX_VALUE;
        for (int i = 0; i < leftCount; i++) {
            CollapsedEdges edges = leftLinks[i];
            weightedTotal -= edges.getWeightedPull();
            unweightedTotal -= edges.tightestEdge.getSlack();
            unweightedDivisor += edges.collapsedCount;
            weightedDivisor += edges.collapsedWeight;
            slack = edges.tightestEdge.getSlack();
            leftFreedom = Math.min(slack, leftFreedom);
            if (slack > 0)
                leftNonzero = Math.min(slack, leftNonzero);
        }
        for (int i = 0; i < rightCount; i++) {
            CollapsedEdges edges = rightLinks[i];
            weightedTotal += edges.getWeightedPull();
            unweightedDivisor += edges.collapsedCount;
            unweightedTotal += edges.tightestEdge.getSlack();
            weightedDivisor += edges.collapsedWeight;
            slack = edges.tightestEdge.getSlack();
            rightFreedom = Math.min(slack, rightFreedom);
            if (slack > 0)
                rightNonzero = Math.min(slack, rightNonzero);
        }
        updateEffectivePull();
    }

    /**
     * Refreshes the left and right freedom.
     */
    void refreshValues() {
        int slack;
        isDirty = false;
        if (leftDirty) {
            leftDirty = false;
            leftNonzero = leftFreedom = Integer.MAX_VALUE;
            for (int i = 0; i < leftCount; i++) {
                CollapsedEdges edges = leftLinks[i];
                slack = edges.tightestEdge.getSlack();
                leftFreedom = Math.min(slack, leftFreedom);
                if (slack > 0)
                    leftNonzero = Math.min(slack, leftNonzero);
            }
        }
        if (rightDirty) {
            rightDirty = false;
            rightNonzero = rightFreedom = Integer.MAX_VALUE;
            for (int i = 0; i < rightCount; i++) {
                CollapsedEdges edges = rightLinks[i];
                slack = edges.tightestEdge.getSlack();
                rightFreedom = Math.min(slack, rightFreedom);
                if (slack > 0)
                    rightNonzero = Math.min(slack, rightNonzero);
            }
        }
        updateEffectivePull();
    }

    private void updateEffectivePull() {
        if (weightedDivisor != 0)
            effectivePull = weightedTotal / weightedDivisor;
        else if (unweightedDivisor != 0)
            effectivePull = unweightedTotal / unweightedDivisor;
        else
            effectivePull = 0;
    }

}