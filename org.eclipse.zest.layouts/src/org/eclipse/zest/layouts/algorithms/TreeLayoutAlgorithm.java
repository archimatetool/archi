/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.zest.layouts.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.dataStructures.DisplayIndependentRectangle;
import org.eclipse.zest.layouts.dataStructures.InternalNode;
import org.eclipse.zest.layouts.dataStructures.InternalRelationship;
import org.eclipse.zest.layouts.exampleStructures.SimpleRelationship;

/**
 * The TreeLayoutAlgorithm class implements a simple algorithm to
 * arrange graph nodes in a layered vertical tree-like layout. 
 * 
 * This is by no means an efficiently coded algorithm.
 *
 * @version  2.0
 * @author   Casey Best and Rob Lintern (version 1.0 by Jingwei Wu)
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class TreeLayoutAlgorithm extends AbstractLayoutAlgorithm {

    private final static double DEFAULT_WEIGHT = 0;
    private final static boolean DEFAULT_MARKED = false;

    private final static boolean AS_DESTINATION = false;
    private final static boolean AS_SOURCE = true;

    private final static int NUM_DESCENDENTS_INDEX = 0;
    private final static int NUM_LEVELS_INDEX = 1;

    private ArrayList treeRoots;

    private double boundsX;
    private double boundsY;
    private double boundsWidth;
    private double boundsHeight;
    private DisplayIndependentRectangle layoutBounds = null;

    private List[] parentLists;
    private List[] childrenLists;
    private double[] weights;
    private boolean[] markedArr;

    /////////////////////////////////////////////////////////////////////////
    /////                        Constructors                           /////
    /////////////////////////////////////////////////////////////////////////

    /**
     * Constructs a new TreeLayoutAlgorithm object.
     */
    public TreeLayoutAlgorithm(int styles) {
        super(styles);
    }

    /**
     * Tree layout algorithm Constructor with NO Style
     *
     */
    public TreeLayoutAlgorithm() {
        this(LayoutStyles.NONE);
    }

    /////////////////////////////////////////////////////////////////////////
    /////                        Public Methods                         /////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public void setLayoutArea(double x, double y, double width, double height) {
        throw new RuntimeException();
    }

    @Override
    protected int getCurrentLayoutStep() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    protected int getTotalNumberOfLayoutSteps() {
        return 4;
    }

    /**
     * Executes this TreeLayoutAlgorithm layout algorithm by referencing the
     * data stored in the repository system. Once done, the result
     * will be saved to the data repository.
     * 
     * @param entitiesToLayout Apply the algorithm to these entities
     * @param relationshipsToConsider Only consider these relationships when applying the algorithm.
     * @param boundsX The left side of the bounds in which the layout can place the entities.
     * @param boundsY The top side of the bounds in which the layout can place the entities.
     * @param boundsWidth The width of the bounds in which the layout can place the entities.
     * @param boundsHeight The height of the bounds in which the layout can place the entities.
     * @throws RuntimeException Thrown if entitiesToLayout doesn't contain all of the endpoints for each relationship in relationshipsToConsider
     */
    @Override
    protected void preLayoutAlgorithm(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider, double x, double y, double width, double height) {
        // Filter unwanted entities and relationships
        //super.applyLayout (entitiesToLayout, relationshipsToConsider, boundsX, boundsY, boundsWidth, boundsHeight);

        parentLists = new List[entitiesToLayout.length];
        childrenLists = new List[entitiesToLayout.length];
        weights = new double[entitiesToLayout.length];
        markedArr = new boolean[entitiesToLayout.length];
        for (int i = 0; i < entitiesToLayout.length; i++) {
            parentLists[i] = new ArrayList();
            childrenLists[i] = new ArrayList();
            weights[i] = DEFAULT_WEIGHT;
            markedArr[i] = DEFAULT_MARKED;
        }

        this.boundsHeight = height;
        this.boundsWidth = width;
        this.boundsX = x;
        this.boundsY = y;
        layoutBounds = new DisplayIndependentRectangle(boundsX, boundsY, boundsWidth, boundsHeight);

    }

    @Override
    protected void applyLayoutInternal(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider, double boundsX, double boundsY, double boundsWidth, double boundsHeight) {

        if (entitiesToLayout.length > 0) {
            int totalProgress = 4;
            fireProgressEvent(1, totalProgress);

            //List roots = new ArrayList();
            treeRoots = new ArrayList();
            buildForest(treeRoots, entitiesToLayout, relationshipsToConsider);
            fireProgressEvent(2, totalProgress);
            computePositions(treeRoots, entitiesToLayout);
            fireProgressEvent(3, totalProgress);
            defaultFitWithinBounds(entitiesToLayout, layoutBounds);

        }
    }

    @Override
    protected void postLayoutAlgorithm(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider) {
        updateLayoutLocations(entitiesToLayout);
        fireProgressEvent(4, 4);
    }

    /**
     * Returns the last found roots
     */
    public List getRoots() {
        return treeRoots;
    }

    /**
     * Finds all the relationships in which the node <code>obj<code>
     * plays the specified <code>role</code>.
     * @param entity The node that concerns the relations to be found.
     * @param role The role played by the <code>obj</code>. Its type
     * must be of <code>ACTOR_ROLE</code> or <code>ACTEE_ROLE</code>.
     * @see SimpleRelationship
     */
    private Collection findRelationships(Object entity, boolean objectAsSource, InternalRelationship[] relationshipsToConsider) {
        Collection foundRels = new ArrayList();
        for (int i = 0; i < relationshipsToConsider.length; i++) {
            InternalRelationship rel = relationshipsToConsider[i];
            if (objectAsSource && rel.getSource().equals(entity)) {
                foundRels.add(rel);
            } else if (!objectAsSource && rel.getDestination().equals(entity)) {
                foundRels.add(rel);
            }
        }
        return foundRels;
    }

    /**
     * Finds the relation that has the lowest index in the relation
     * repository in which the node <code>obj<code> plays the specified
     * <code>role</code>.
     * @param obj The node that concerns the relations to be found.
     * @param role The role played by the <code>obj</code>. Its type must
     * be of <code>ACTOR_ROLE</code> or <code>ACTEE_ROLE</code>.
     * @see SimpleRelationship
     * @see SimpleRelationship#ACTOR_ROLE
     * @see SimpleRelationship#ACTEE_ROLE
     */
    private InternalRelationship findRelationship(Object entity, boolean objectAsSource, InternalRelationship[] relationshipsToConsider) {
        InternalRelationship relationship = null;
        for (int i = 0; i < relationshipsToConsider.length && relationship == null; i++) {
            InternalRelationship possibleRel = relationshipsToConsider[i];
            if (objectAsSource && possibleRel.getSource().equals(entity)) {
                relationship = possibleRel;
            } else if (!objectAsSource && possibleRel.getDestination().equals(entity)) {
                relationship = possibleRel;
            }
        }
        return relationship;
    }

    /////////////////////////////////////////////////////////////////////////
    /////                        Private Methods                        /////
    /////////////////////////////////////////////////////////////////////////

    /**
     * Builds the tree forest that is used to calculate positions
     * for each node in this TreeLayoutAlgorithm.
     */
    private void buildForest(List roots, InternalNode[] entities, InternalRelationship[] relationships) {
        List unplacedEntities = new ArrayList(Arrays.asList(entities));
        buildForestRecursively(roots, unplacedEntities, entities, relationships);
    }

    /**
     * Builds the forest recursively. All entities
     * will be placed somewhere in the forest. 
     */
    private void buildForestRecursively(List roots, List unplacedEntities, InternalNode[] entities, InternalRelationship[] relationships) {
        if (unplacedEntities.size() == 0) {
            return; // no more entities to place
        }

        // get the first entity in the list of unplaced entities, find its root, and build this root's tree
        InternalNode layoutEntity = (InternalNode) unplacedEntities.get(0);
        InternalNode rootEntity = findRootObjectRecursive(layoutEntity, new HashSet(), relationships);
        int rootEntityIndex = indexOfInternalNode(entities, rootEntity);
        buildTreeRecursively(rootEntity, rootEntityIndex, 0, entities, relationships);
        roots.add(rootEntity);

        // now see which nodes are left to be placed in a tree somewhere
        List unmarkedCopy = new ArrayList(unplacedEntities);
        for (Iterator iter = unmarkedCopy.iterator(); iter.hasNext();) {
            InternalNode tmpEntity = (InternalNode) iter.next();
            int tmpEntityIndex = indexOfInternalNode(entities, tmpEntity);
            boolean isMarked = markedArr[tmpEntityIndex];
            if (isMarked) {
                unplacedEntities.remove(tmpEntity);
            }
        }
        buildForestRecursively(roots, unplacedEntities, entities, relationships);
    }

    /**
     * Finds the root node that can be treated as the root of a tree.
     * The found root node should be one of the unmarked nodes.
     */
    private InternalNode findRootObjectRecursive(InternalNode currentEntity, Set seenAlready, InternalRelationship[] relationshipsToConsider) {
        InternalNode rootEntity = null;
        InternalRelationship rel = findRelationship(currentEntity, AS_DESTINATION, relationshipsToConsider);
        if (rel == null) {
            rootEntity = currentEntity;
        } else {
            InternalNode parentEntity = rel.getSource();
            if (!seenAlready.contains(parentEntity)) {
                seenAlready.add(parentEntity);
                rootEntity = findRootObjectRecursive(parentEntity, seenAlready, relationshipsToConsider);
            } else {
                rootEntity = currentEntity;
            }
        }
        return rootEntity;
    }

    /**
     * Builds a tree of the passed in entity.
     * The entity will pass a weight value to all of its children recursively.
     */
    private void buildTreeRecursively(InternalNode layoutEntity, int i, double weight, InternalNode[] entities, final InternalRelationship[] relationships) {
        // No need to do further computation!
        if (layoutEntity == null) {
            return;
        }

        // A marked entity means that it has been added to the
        // forest, and its weight value needs to be modified.        
        if (markedArr[i]) {
            modifyWeightRecursively(layoutEntity, i, weight, new HashSet(), entities, relationships);
            return; //No need to do further computation.
        }

        // Mark this entity, set its weight value and create a new tree node.
        markedArr[i] = true;
        weights[i] = weight;

        // collect the children of this entity and put them in order
        Collection rels = findRelationships(layoutEntity, AS_SOURCE, relationships);
        List children = new ArrayList();
        for (Iterator iter = rels.iterator(); iter.hasNext();) {
            InternalRelationship layoutRel = (InternalRelationship) iter.next();
            InternalNode childEntity = layoutRel.getDestination();
            children.add(childEntity);
        }

        if (comparator != null) {
            Collections.sort(children, comparator);
        } else {
            // sort the children by level, then by number of descendents, then by number of children
            // TODO: SLOW
            Collections.sort(children, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    InternalNode node1 = (InternalNode) o1;
                    InternalNode node2 = (InternalNode) o2;
                    int[] numDescendentsAndLevel1 = new int[2];
                    int[] numDescendentsAndLevel2 = new int[2];
                    int level1 = numDescendentsAndLevel1[NUM_LEVELS_INDEX];
                    int level2 = numDescendentsAndLevel2[NUM_LEVELS_INDEX];
                    if (level1 == level2) {
                        getNumDescendentsAndLevel(node1, relationships, numDescendentsAndLevel1);
                        getNumDescendentsAndLevel(node2, relationships, numDescendentsAndLevel2);
                        int numDescendents1 = numDescendentsAndLevel1[NUM_DESCENDENTS_INDEX];
                        int numDescendents2 = numDescendentsAndLevel2[NUM_DESCENDENTS_INDEX];
                        if (numDescendents1 == numDescendents2) {
                            int numChildren1 = getNumChildren(node1, relationships);
                            int numChildren2 = getNumChildren(node1, relationships);
                            return numChildren2 - numChildren1;
                        } else {
                            return numDescendents2 - numDescendents1;
                        }
                    } else {
                        return level2 - level1;
                    }
                    //return getNumChildren(node2, relationships) - getNumChildren(node1, relationships);
                }
            });
        }

        // map children to this parent, and vice versa
        for (Iterator iter = children.iterator(); iter.hasNext();) {
            InternalNode childEntity = (InternalNode) iter.next();
            int childEntityIndex = indexOfInternalNode(entities, childEntity);
            if (!childrenLists[i].contains(childEntity)) {
                childrenLists[i].add(childEntity);
            }
            if (!parentLists[childEntityIndex].contains(layoutEntity)) {
                parentLists[childEntityIndex].add(layoutEntity);
            }
        }

        for (Iterator iter = children.iterator(); iter.hasNext();) {
            InternalNode childEntity = (InternalNode) iter.next();
            int childEntityIndex = indexOfInternalNode(entities, childEntity);
            buildTreeRecursively(childEntity, childEntityIndex, weight + 1, entities, relationships);
        }
    }

    private int getNumChildren(InternalNode layoutEntity, InternalRelationship[] relationships) {
        return findRelationships(layoutEntity, AS_SOURCE, relationships).size();
    }

    private void getNumDescendentsAndLevel(InternalNode layoutEntity, InternalRelationship[] relationships, int[] numDescendentsAndLevel) {
        getNumDescendentsAndLevelRecursive(layoutEntity, relationships, new HashSet(), numDescendentsAndLevel, 0);
    }

    private void getNumDescendentsAndLevelRecursive(InternalNode layoutEntity, InternalRelationship[] relationships, Set seenAlready, int[] numDescendentsAndLevel, int currentLevel) {
        if (seenAlready.contains(layoutEntity)) {
            return;
        }
        seenAlready.add(layoutEntity);
        numDescendentsAndLevel[NUM_LEVELS_INDEX] = Math.max(numDescendentsAndLevel[NUM_LEVELS_INDEX], currentLevel);
        Collection rels = findRelationships(layoutEntity, AS_SOURCE, relationships);
        for (Iterator iter = rels.iterator(); iter.hasNext();) {
            InternalRelationship layoutRel = (InternalRelationship) iter.next();
            InternalNode childEntity = layoutRel.getDestination();
            numDescendentsAndLevel[NUM_DESCENDENTS_INDEX]++;
            getNumDescendentsAndLevelRecursive(childEntity, relationships, seenAlready, numDescendentsAndLevel, currentLevel + 1);

        }
    }

    /**
     * Modifies the weight value of the marked node recursively.
     */
    private void modifyWeightRecursively(InternalNode layoutEntity, int i, double weight, Set descendentsSeenSoFar, InternalNode[] entities, InternalRelationship[] relationships) {
        // No need to do further computation!
        if (layoutEntity == null) {
            return;
        }

        if (descendentsSeenSoFar.contains(layoutEntity)) {
            return; //No need to do further computation.
        }

        descendentsSeenSoFar.add(layoutEntity);
        // No need to do further computation!
        if (weight < weights[i]) {
            return;
        }

        weights[i] = weight;
        Collection rels = findRelationships(layoutEntity, AS_SOURCE, relationships);

        for (Iterator iter = rels.iterator(); iter.hasNext();) {
            InternalRelationship tmpRel = (InternalRelationship) iter.next();
            InternalNode tmpEntity = tmpRel.getDestination();
            int tmpEntityIndex = indexOfInternalNode(entities, tmpEntity);
            modifyWeightRecursively(tmpEntity, tmpEntityIndex, weight + 1, descendentsSeenSoFar, entities, relationships);
        }
    }

    /**
     * Gets the maxium weight of a tree in the forest of this TreeLayoutAlgorithm.
     */
    private double getMaxiumWeightRecursive(InternalNode layoutEntity, int i, Set seenAlready, InternalNode[] entities) {
        double result = 0;
        if (seenAlready.contains(layoutEntity)) {
            return result;
        }
        seenAlready.add(layoutEntity);
        List children = childrenLists[i];
        if (children.isEmpty()) {
            result = weights[i];
        } else {
            //TODO: SLOW
            for (Iterator iter = children.iterator(); iter.hasNext();) {
                InternalNode childEntity = (InternalNode) iter.next();
                int childEntityIndex = indexOfInternalNode(entities, childEntity);
                result = Math.max(result, getMaxiumWeightRecursive(childEntity, childEntityIndex, seenAlready, entities));
            }
        }
        return result;
    }

    /**
     * Computes positions for each node in this TreeLayoutAlgorithm by
     * referencing the forest that holds those nodes.
     */
    private void computePositions(List roots, InternalNode[] entities) {
        // No need to do further computation!
        if (roots.size() == 0) {
            return;
        }

        int totalLeafCount = 0;
        double maxWeight = 0;
        for (int i = 0; i < roots.size(); i++) {
            InternalNode rootEntity = (InternalNode) roots.get(i);
            int rootEntityIndex = indexOfInternalNode(entities, rootEntity);
            totalLeafCount = totalLeafCount + getNumberOfLeaves(rootEntity, rootEntityIndex, entities);
            maxWeight = Math.max(maxWeight, getMaxiumWeightRecursive(rootEntity, rootEntityIndex, new HashSet(), entities) + 1.0);
        }

        double width = 1.0 / totalLeafCount;
        double height = 1.0 / maxWeight;

        int leafCountSoFar = 0;

        //TODO: SLOW!
        for (int i = 0; i < roots.size(); i++) {
            InternalNode rootEntity = (InternalNode) roots.get(i);
            int rootEntityIndex = indexOfInternalNode(entities, rootEntity);
            computePositionRecursively(rootEntity, rootEntityIndex, leafCountSoFar, width, height, new HashSet(), entities);
            leafCountSoFar = leafCountSoFar + getNumberOfLeaves(rootEntity, rootEntityIndex, entities);
        }
    }

    /**
     * Computes positions recursively until the leaf nodes are reached.
     */
    private void computePositionRecursively(InternalNode layoutEntity, int i, int relativePosition, double width, double height, Set seenAlready, InternalNode[] entities) {
        if (seenAlready.contains(layoutEntity)) {
            return;
        }
        seenAlready.add(layoutEntity);
        double level = getLevel(layoutEntity, i, entities);
        int breadth = getNumberOfLeaves(layoutEntity, i, entities);
        double absHPosition = relativePosition + breadth / 2.0;
        double absVPosition = (level + 0.5);

        double posx = absHPosition * width;
        double posy = absVPosition * height;
        double weight = weights[i];
        posy = posy + height * (weight - level);
        layoutEntity.setInternalLocation(posx, posy);

        int relativeCount = 0;
        List children = childrenLists[i];
        //TODO: Slow
        for (Iterator iter = children.iterator(); iter.hasNext();) {
            InternalNode childEntity = (InternalNode) iter.next();
            int childEntityIndex = indexOfInternalNode(entities, childEntity);
            computePositionRecursively(childEntity, childEntityIndex, relativePosition + relativeCount, width, height, seenAlready, entities);
            relativeCount = relativeCount + getNumberOfLeaves(childEntity, childEntityIndex, entities);
        }
    }

    private int getNumberOfLeaves(InternalNode layoutEntity, int i, InternalNode[] entities) {
        return getNumberOfLeavesRecursive(layoutEntity, i, new HashSet(), entities);
    }

    private int getNumberOfLeavesRecursive(InternalNode layoutEntity, int i, Set seen, InternalNode[] entities) {
        int numLeaves = 0;
        List children = childrenLists[i];
        if (children.size() == 0) {
            numLeaves = 1;
        } else {
            //TODO: SLOW!
            for (Iterator iter = children.iterator(); iter.hasNext();) {
                InternalNode childEntity = (InternalNode) iter.next();
                if (!seen.contains(childEntity)) {
                    seen.add(childEntity);
                    int childEntityIndex = indexOfInternalNode(entities, childEntity);
                    numLeaves += getNumberOfLeavesRecursive(childEntity, childEntityIndex, seen, entities);
                } else {
                    numLeaves = 1;
                }
            }
        }
        return numLeaves;
    }

    private int getLevel(InternalNode layoutEntity, int i, InternalNode[] entities) {
        return getLevelRecursive(layoutEntity, i, new HashSet(), entities);
    }

    private int getLevelRecursive(InternalNode layoutEntity, int i, Set seen, InternalNode[] entities) {
        if (seen.contains(layoutEntity)) {
            return 0;
        }
        seen.add(layoutEntity);
        List parents = parentLists[i];
        int maxParentLevel = 0;
        for (Iterator iter = parents.iterator(); iter.hasNext();) {
            InternalNode parentEntity = (InternalNode) iter.next();
            int parentEntityIndex = indexOfInternalNode(entities, parentEntity);
            int parentLevel = getLevelRecursive(parentEntity, parentEntityIndex, seen, entities) + 1;
            maxParentLevel = Math.max(maxParentLevel, parentLevel);
        }
        return maxParentLevel;
    }

    /**
     * Note: Use this as little as possible!
     * TODO limit the use of this method 
     * @param nodes
     * @param nodeToFind
     * @return
     */
    private int indexOfInternalNode(InternalNode[] nodes, InternalNode nodeToFind) {
        for (int i = 0; i < nodes.length; i++) {
            InternalNode node = nodes[i];
            if (node.equals(nodeToFind)) {
                return i;
            }
        }
        throw new RuntimeException("Couldn't find index of internal node: " + nodeToFind); //$NON-NLS-1$
    }

    @Override
    protected boolean isValidConfiguration(boolean asynchronous, boolean continueous) {
        if (asynchronous && continueous) {
            return false;
        } else if (asynchronous && !continueous) {
            return true;
        } else if (!asynchronous && continueous) {
            return false;
        } else if (!asynchronous && !continueous) {
            return true;
        }

        return false;
    }

}
