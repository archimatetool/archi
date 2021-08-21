/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 *******************************************************************************/

package org.eclipse.zest.layouts.algorithms.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.eclipse.zest.layouts.LayoutEntity;
import org.eclipse.zest.layouts.LayoutRelationship;
import org.eclipse.zest.layouts.algorithms.AbstractLayoutAlgorithm;
import org.eclipse.zest.layouts.exampleStructures.SimpleRelationship;

/**
 * Checks for cycles in the given graph.
 * 
 * @author Casey Best
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class CycleChecker {
    /**
     * Tests if there is a directed cirlce in the graph formed by the given entities and relationships.
     * @param entities The entities in the graph to check
     * @param relationships The relationships in the graph to check
     * @param cycle Populated with the cycle encountered, if there is one.
     * @throws RuntimeException Thrown if entities doesn't contain all of the endpoints for each relationship in relationships
     * @return <code>true</code> if there is a directed circle.
     * Otherwise, <code>false</code>.
     */
    public static boolean hasDirectedCircles(LayoutEntity[] entities, LayoutRelationship[] relationships, List cycle) {
        if (!AbstractLayoutAlgorithm.verifyInput(entities, relationships)) {
            throw new RuntimeException("The endpoints of the relationships aren't contained in the entities list."); //$NON-NLS-1$
        }
        //Enumeration enum;
        //Iterator iterator;

        Hashtable endPoints = new Hashtable();

        // Initialize the relation(transitive) vector.
        for (int i = 0; i < relationships.length; i++) {
            LayoutRelationship rel = relationships[i];

            //Add the relationship to the source endpoint
            Object subject = rel.getSourceInLayout();
            List rels = (List) endPoints.get(subject);
            if (rels == null) {
                rels = new ArrayList();
                endPoints.put(subject, rels);
            }
            if (!rels.contains(rel))
                rels.add(rel);
        }
        boolean hasCyle = hasCycle(new ArrayList(Arrays.asList(entities)), endPoints, cycle);
        return hasCyle;
    }

    /**
     * Check passed in nodes for a cycle
     */
    private static boolean hasCycle(List nodesToCheck, Hashtable endPoints, List cycle) {
        while (nodesToCheck.size() > 0) {
            Object checkNode = nodesToCheck.get(0);
            List checkedNodes = new ArrayList();
            if (hasCycle(checkNode, new ArrayList(), null, endPoints, checkedNodes, cycle)) {
                return true;
            }
            nodesToCheck.removeAll(checkedNodes);
        }
        return false;
    }

    /**
     * Checks all the nodes attached to the nodeToCheck node for a cycle.  All nodes
     * checked are placed in nodePathSoFar.
     *
     * @returns true if there is a cycle
     */
    private static boolean hasCycle(Object nodeToCheck, List nodePathSoFar, SimpleRelationship cameFrom, Hashtable endPoints, List nodesChecked, List cycle) {
        if (nodePathSoFar.contains(nodeToCheck)) {
            cycle.addAll(nodePathSoFar);
            cycle.add(nodeToCheck);
            return true;
        }
        nodePathSoFar.add(nodeToCheck);
        nodesChecked.add(nodeToCheck);

        List relations = (List) endPoints.get(nodeToCheck);
        if (relations != null) {
            for (Iterator iter = relations.iterator(); iter.hasNext();) {
                SimpleRelationship rel = (SimpleRelationship) iter.next();

                if (cameFrom == null || !rel.equals(cameFrom)) {
                    Object currentNode = null;
                    currentNode = rel.getDestinationInLayout();
                    if (hasCycle(currentNode, nodePathSoFar, rel, endPoints, nodesChecked, cycle)) {
                        return true;
                    }
                }

            }
        }

        nodePathSoFar.remove(nodeToCheck);

        return false;
    }

}
