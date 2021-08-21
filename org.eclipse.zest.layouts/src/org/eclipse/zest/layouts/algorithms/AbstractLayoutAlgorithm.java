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
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.eclipse.zest.layouts.Filter;
import org.eclipse.zest.layouts.InvalidLayoutConfiguration;
import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.LayoutEntity;
import org.eclipse.zest.layouts.LayoutItem;
import org.eclipse.zest.layouts.LayoutRelationship;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.Stoppable;
import org.eclipse.zest.layouts.constraints.BasicEntityConstraint;
import org.eclipse.zest.layouts.dataStructures.BendPoint;
import org.eclipse.zest.layouts.dataStructures.DisplayIndependentDimension;
import org.eclipse.zest.layouts.dataStructures.DisplayIndependentPoint;
import org.eclipse.zest.layouts.dataStructures.DisplayIndependentRectangle;
import org.eclipse.zest.layouts.dataStructures.InternalNode;
import org.eclipse.zest.layouts.dataStructures.InternalRelationship;
import org.eclipse.zest.layouts.progress.ProgressEvent;
import org.eclipse.zest.layouts.progress.ProgressListener;

/**
 * Handles common elements in all layout algorithms
 * [irbull] Refactored into a template pattern.  ApplyLayout now delegates the
 * task to ApplyLayoutInternal
 * 
 * [irbull] Included asynchronous layouts
 * 
 * @version 1.0
 * @author Casey Best
 * @author Ian Bull
 * @author Chris Callendar
 * @author Rob Lintern
 * @author Chris Bennett
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class AbstractLayoutAlgorithm implements LayoutAlgorithm, Stoppable {

    public void removeRelationships(Collection collection) {

    }

    public final static int MIN_ENTITY_SIZE = 5;
    private final static int MIN_TIME_DELAY_BETWEEN_PROGRESS_EVENTS = 1;

    private Thread creationThread = null;
    protected Comparator comparator;
    protected Filter filter;
    private List progressListeners;
    private Calendar lastProgressEventFired;
    private double widthToHeightRatio;

    class InternalComparator implements Comparator {
        Comparator externalComparator = null;

        public InternalComparator(Comparator externalComparator) {
            this.externalComparator = externalComparator;
        }

        @Override
        public int compare(Object o1, Object o2) {
            InternalNode internalNode1 = (InternalNode) o1;
            InternalNode internalNode2 = (InternalNode) o2;

            return this.externalComparator.compare(internalNode1.getLayoutEntity(), internalNode2.getLayoutEntity());
        }

    }

    /*
     * Internal Nodes.   
     */
    private InternalNode[] internalNodes;
    private InternalRelationship[] internalRelationships;
    private double internalX;
    private double internalY;
    private double internalWidth;
    private double internalHeight;
    protected boolean internalContinuous;
    protected boolean internalAsynchronous;

    /*
     * A queue of entities and relationships to add or remove.  Each layout 
     * algorithm should check these and update their internal lists.
     */

    /** A list of LayoutEntity objects to be removed from the layout. */
    private List entitiesToRemove;
    /** A list of LayoutRelationship objects to be removed. */
    private List relationshipsToRemove;
    /** A list of LayoutEntity objects to be added to the layout. */
    private List entitiesToAdd;
    /** A list of LayoutRelationship objects to be added. */
    private List relationshipsToAdd;

    //protected boolean cancelled = false;

    protected boolean layoutStopped = true;

    protected int layout_styles = 0;

    // Child classes can set to false to retain node shapes and sizes
    protected boolean resizeEntitiesAfterLayout = true;

    /**
     * Initializes the abstract layout algorithm.
     * @see LayoutStyles
     */
    public AbstractLayoutAlgorithm(int styles) {
        this.creationThread = Thread.currentThread();
        this.progressListeners = new ArrayList();
        this.lastProgressEventFired = Calendar.getInstance();
        this.widthToHeightRatio = 1.0;

        this.entitiesToRemove = new ArrayList();
        this.relationshipsToRemove = new ArrayList();
        this.entitiesToAdd = new ArrayList();
        this.relationshipsToAdd = new ArrayList();
        this.layout_styles = styles;
    }

    /**
     * Queues up the given entity (if it isn't in the list) to be added to the algorithm.
     * @param entity
     */
    @Override
    public void addEntity(LayoutEntity entity) {
        if ((entity != null) && !entitiesToAdd.contains(entity)) {
            entitiesToAdd.add(entity);
        }
    }

    /**
     * Queues up the given relationshp (if it isn't in the list) to be added to the algorithm.
     * @param relationship
     */
    @Override
    public void addRelationship(LayoutRelationship relationship) {
        if ((relationship != null) && !relationshipsToAdd.contains(relationship)) {
            relationshipsToAdd.add(relationship);
        }
    }

    /**
     * Queues up the given entity to be removed from the algorithm next time it runs.
     * @param entity The entity to remove
     */
    @Override
    public void removeEntity(LayoutEntity entity) {
        if ((entity != null) && !entitiesToRemove.contains(entity)) {
            entitiesToRemove.add(entity);
        }
    }

    /**
     * Queues up the given relationship to be removed from the algorithm next time it runs.
     * @param relationship    The relationship to remove.
     */
    @Override
    public void removeRelationship(LayoutRelationship relationship) {
        if ((relationship != null) && !relationshipsToRemove.contains(relationship)) {
            relationshipsToRemove.add(relationship);
        }
    }

    /**
     * Queues up all the relationships in the list to be removed.
     * @param relationships
     */
    @Override
    public void removeRelationships(List relationships) {
        // note we don't check if the relationshipsToRemove contains
        // any of the objects in relationships.
        relationshipsToRemove.addAll(relationships);
    }

    /**
     * Sets the current layout style.  This overwrites all other layout styles.
     * Use getStyle to get the current style.
     * @param style
     */
    @Override
    public void setStyle(int style) {
        this.layout_styles = style;
    }

    /**
     * Gets the current layout style
     * @return
     */
    @Override
    public int getStyle() {
        return this.layout_styles;
    }

    public abstract void setLayoutArea(double x, double y, double width, double height);

    /**
     * Determines if the configuration is valid for this layout
     * @param asynchronous
     * @param continuous
     */
    protected abstract boolean isValidConfiguration(boolean asynchronous, boolean continuous);

    /**
     * Apply the layout to the given entities.  The entities will be moved and resized based
     * on the algorithm.
     * 
     * @param entitiesToLayout Apply the algorithm to these entities
     * @param relationshipsToConsider Only consider these relationships when applying the algorithm.
     * @param x The left side of the bounds in which the layout can place the entities.
     * @param y The top side of the bounds in which the layout can place the entities.
     * @param width The width of the bounds in which the layout can place the entities.
     * @param height The height of the bounds in which the layout can place the entities.
     */
    abstract protected void applyLayoutInternal(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider, double boundsX, double boundsY, double boundsWidth, double boundsHeight);

    /**
     * Updates the given array of entities checking if any need to be removed or added.
     * @param entities the current entities
     * @return the updated entities array
     */
    protected InternalNode[] updateEntities(InternalNode[] entities) {
        if ((entitiesToRemove.size() > 0) || (entitiesToAdd.size() > 0)) {
            List internalNodesList = new ArrayList(Arrays.asList(entities));

            // remove nodes
            for (Iterator iter = entitiesToRemove.iterator(); iter.hasNext();) {
                LayoutEntity entity = (LayoutEntity) iter.next();
                if (entity.getLayoutInformation() != null) {
                    internalNodesList.remove(entity.getLayoutInformation());
                }
            }

            // Also remove from _internalNodes
            ArrayList updatedEntities = new ArrayList(internalNodes.length - entitiesToRemove.size() + entitiesToAdd.size());
            for (int i = 0; i < internalNodes.length; i++) {
                InternalNode node = internalNodes[i];
                if (entitiesToRemove.contains(node.getLayoutEntity())) {
                    entitiesToRemove.remove(node.getLayoutEntity());
                } else {
                    updatedEntities.add(node);
                }
            }
            entitiesToRemove.clear();

            // Add any new nodes
            LayoutEntity[] entitiesArray = new LayoutEntity[entitiesToAdd.size()];
            entitiesArray = (LayoutEntity[]) entitiesToAdd.toArray(entitiesArray);
            InternalNode[] newNodes = createInternalNodes(entitiesArray);
            for (int i = 0; i < newNodes.length; i++) {
                internalNodesList.add(newNodes[i]);
                updatedEntities.add(newNodes[i]);
            }
            entitiesToAdd.clear();

            entities = new InternalNode[internalNodesList.size()];
            entities = (InternalNode[]) internalNodesList.toArray(entities);

            internalNodes = new InternalNode[updatedEntities.size()];
            internalNodes = (InternalNode[]) updatedEntities.toArray(internalNodes);
        }

        return entities;
    }

    /**
     * Updates the given array of relationships checking if any need to be removed or added.
     * Also updates the original array of relationships.
     * @param relationships the current relationships
     * @return the update relationships array
     */
    protected InternalRelationship[] updateRelationships(InternalRelationship[] relationships) {
        if ((relationshipsToRemove.size() > 0) || (relationshipsToAdd.size() > 0)) {
            List internalRelsList = new ArrayList(Arrays.asList(relationships));

            // remove relationships
            if (relationshipsToRemove.size() > 0) {
                for (Iterator iter = relationshipsToRemove.iterator(); iter.hasNext();) {
                    LayoutRelationship relation = (LayoutRelationship) iter.next();
                    if (relation.getLayoutInformation() != null) {
                        internalRelsList.remove(relation.getLayoutInformation());
                    }
                }
            }

            // Also remove from _internalRelationships
            ArrayList updatedRelationships = new ArrayList(internalRelationships.length - relationshipsToRemove.size() + relationshipsToAdd.size());
            for (int i = 0; i < internalRelationships.length; i++) {
                InternalRelationship relation = internalRelationships[i];
                if (relationshipsToRemove.contains(relation.getLayoutRelationship())) {
                    relationshipsToRemove.remove(relation.getLayoutRelationship());
                } else {
                    updatedRelationships.add(relation);
                }
            }
            relationshipsToRemove.clear();

            // add relationships
            if (relationshipsToAdd.size() > 0) {
                LayoutRelationship[] relsArray = new LayoutRelationship[relationshipsToAdd.size()];
                relsArray = (LayoutRelationship[]) relationshipsToAdd.toArray(relsArray);
                InternalRelationship[] newRelationships = createInternalRelationships(relsArray);
                for (int i = 0; i < newRelationships.length; i++) {
                    internalRelsList.add(newRelationships[i]);
                    updatedRelationships.add(newRelationships[i]);
                }
            }
            relationshipsToAdd.clear();

            relationships = new InternalRelationship[internalRelsList.size()];
            relationships = (InternalRelationship[]) internalRelsList.toArray(relationships);

            internalRelationships = new InternalRelationship[updatedRelationships.size()];
            internalRelationships = (InternalRelationship[]) updatedRelationships.toArray(internalRelationships);
        }

        return relationships;
    }

    /**
     * Moves all the entities by the given amount.  
     * @param dx    the amount to shift the entities in the x-direction 
     * @param dy    the amount to shift the entities in the y-direction
     */
    /*
     public void moveAllEntities(double dx, double dy) {
     if ((dx != 0) || (dy != 0)) {
     synchronized (_internalNodes) {
     for (int i = 0; i < _internalNodes.length; i++) {
     InternalNode node = _internalNodes[i];
     node.setInternalLocation(node.getInternalX()+dx, node.getInternalY()+dy);
     node.setLocation(node.getX()+dx, node.getY()+dy);
     }
     }
     }
     }
     */

    /**
     * Returns true if the layout algorithm is running
     * @return boolean if the layout algorithm is running
     */
    @Override
    public synchronized boolean isRunning() {
        return !layoutStopped;
    }

    /**
     * Stops the current layout from running.
     * All layout algorithms should constantly check isLayoutRunning
     */
    @Override
    public synchronized void stop() {
        layoutStopped = true;
        postLayoutAlgorithm(internalNodes, internalRelationships);
        fireProgressEnded(getTotalNumberOfLayoutSteps());
    }

    //    /**
    //     * Sleeps while the algorithm is paused.
    //     */
    //    protected void sleepWhilePaused() {
    //        // do nothing while the algorithm is paused
    //        boolean wasPaused = false;
    //        while (isPaused()) {
    //            try {
    //                Thread.sleep(200);
    //            } catch (InterruptedException e) {
    //            }
    //            wasPaused = true;
    //        }
    //        // update the node positions (they might have been moved while paused)
    //        if (wasPaused) {
    //            for (int i = 0; i < internalNodes.length; i++) {
    //                InternalNode node = internalNodes[i];
    //                node.setInternalLocation(node.getPreferredX(), node.getPreferredY());
    //            }
    //        }
    //    }

    private void setupLayout(LayoutEntity[] entitiesToLayout, LayoutRelationship[] relationshipsToConsider, double x, double y, double width, double height) {
        internalX = x;
        internalY = y;
        internalHeight = height;
        internalWidth = width;
        // Filter all the unwanted entities and relationships
        entitiesToLayout = (LayoutEntity[]) filterUnwantedObjects(entitiesToLayout);
        relationshipsToConsider = (LayoutRelationship[]) filterUnwantedObjects(relationshipsToConsider);

        // Check that the input is valid
        if (!verifyInput(entitiesToLayout, relationshipsToConsider)) {
            layoutStopped = true;
            throw new RuntimeException("The relationships in relationshipsToConsider don't contain the entities in entitiesToLayout"); //$NON-NLS-1$
        }

        // Create the internal nodes and relationship
        internalNodes = createInternalNodes(entitiesToLayout);
        internalRelationships = createInternalRelationships(relationshipsToConsider);
    }

    //    public synchronized Stoppable getLayoutThread(LayoutEntity[] entitiesToLayout, LayoutRelationship[] relationshipsToConsider, double x, double y, double width, double height, boolean continuous) {
    //        //setupLayout( entitiesToLayout, relationshipsToConsider, x, y, width, height );
    //        this.layoutStopped = false;
    //        this.runContinuously = continuous;
    //        setupLayout(entitiesToLayout, relationshipsToConsider, x, y, width, height);
    //        preLayoutAlgorithm(internalNodes, internalRelationships, internalX, internalY, internalWidth, internalHeight);
    //        fireProgressStarted(getTotalNumberOfLayoutSteps());
    //        return this;
    //    }

    /**
     * Code called before the layout algorithm starts
     */
    protected abstract void preLayoutAlgorithm(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider, double x, double y, double width, double height);

    /**
     * Code called after the layout algorithm ends
     */
    protected abstract void postLayoutAlgorithm(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider);

    /**
     *  Gets the total number of steps in this layout
     */
    protected abstract int getTotalNumberOfLayoutSteps();

    /**
     * Gets the current layout step
     * @return
     */
    protected abstract int getCurrentLayoutStep();

    /**
     * This actually applies the layout
     */
    @Override
    public synchronized void applyLayout(final LayoutEntity[] entitiesToLayout, final LayoutRelationship[] relationshipsToConsider, final double x, final double y, final double width, final double height, boolean asynchronous, boolean continuous) throws InvalidLayoutConfiguration {
        checkThread();
        this.internalAsynchronous = asynchronous;
        this.internalContinuous = continuous;

        if (!isValidConfiguration(asynchronous, continuous)) {
            throw new InvalidLayoutConfiguration();
        }

        clearBendPoints(relationshipsToConsider);

        this.layoutStopped = false;

        // when an algorithm starts, reset the progress event
        lastProgressEventFired = Calendar.getInstance();
        if (asynchronous) {

            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    setupLayout(entitiesToLayout, relationshipsToConsider, x, y, width, height);
                    preLayoutAlgorithm(internalNodes, internalRelationships, internalX, internalY, internalWidth, internalHeight);
                    fireProgressStarted(getTotalNumberOfLayoutSteps());

                    applyLayoutInternal(internalNodes, internalRelationships, internalX, internalY, internalWidth, internalHeight);
                    stop();
                }

            });
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.start();
        } else {

            // If we are running synchronously then we have to stop this at some
            // point? right?
            setupLayout(entitiesToLayout, relationshipsToConsider, x, y, width, height);
            preLayoutAlgorithm(internalNodes, internalRelationships, internalX, internalY, internalWidth, internalHeight);
            fireProgressStarted(getTotalNumberOfLayoutSteps());

            applyLayoutInternal(internalNodes, internalRelationships, internalX, internalY, internalWidth, internalHeight);
            stop();
        }

    }

    /**
     * Clear out all old bend points before doing a layout
     */
    private void clearBendPoints(LayoutRelationship[] relationships) {
        for (int i = 0; i < relationships.length; i++) {
            LayoutRelationship rel = relationships[i];
            rel.clearBendPoints();
        }
    }

    /**
     * Update external bend points from the internal bendpoints list. Save the 
     * source and destination points for later use in scaling and translating
     * @param relationshipsToConsider
     */
    protected void updateBendPoints(InternalRelationship[] relationshipsToConsider) {
        for (int i = 0; i < relationshipsToConsider.length; i++) {
            InternalRelationship relationship = relationshipsToConsider[i];
            List bendPoints = relationship.getBendPoints();
            if (bendPoints.size() > 0) {
                // We will assume that source/dest coordinates are for center of node
                BendPoint[] externalBendPoints = new BendPoint[bendPoints.size() + 2];
                InternalNode sourceNode = relationship.getSource();
                externalBendPoints[0] = new BendPoint(sourceNode.getInternalX(), sourceNode.getInternalY());
                InternalNode destNode = relationship.getDestination();
                externalBendPoints[externalBendPoints.length - 1] = new BendPoint(destNode.getInternalX(), destNode.getInternalY());

                for (int j = 0; j < bendPoints.size(); j++) {
                    BendPoint bp = (BendPoint) bendPoints.get(j);
                    externalBendPoints[j + 1] = new BendPoint(bp.x, bp.y, bp.getIsControlPoint());
                }
                relationship.getLayoutRelationship().setBendPoints(externalBendPoints);
            }
        }
    }

    //    public void run() {
    //
    //        if (started == true) {
    //            throw new RuntimeException("Layout has already run!");
    //        }
    //        started = true;
    //        //layoutStopped = false;
    //        isLayoutPaused = false;
    //        applyLayoutInternal(internalNodes, internalRelationships, internalX, internalY, internalWidth, internalHeight);
    //        stop();
    //        layoutStopped = true;
    //        isLayoutPaused = false;
    //    }

    /**
     * Creates a list of InternalNode objects from the list of LayoutEntity objects the user
     * wants layed out. Sets the internal nodes' positions and sizes from the
     * external entities.
     */
    private InternalNode[] createInternalNodes(LayoutEntity[] nodes) {
        InternalNode[] internalNodes = new InternalNode[nodes.length];
        BasicEntityConstraint basicEntityConstraint = new BasicEntityConstraint();
        for (int i = 0; i < nodes.length; i++) {
            basicEntityConstraint.clear();
            LayoutEntity externalNode = nodes[i];
            InternalNode internalNode = new InternalNode(externalNode);
            externalNode.populateLayoutConstraint(basicEntityConstraint);
            internalNode.setInternalLocation(externalNode.getXInLayout(), externalNode.getYInLayout());
            internalNodes[i] = internalNode;
        } // end of for
        return internalNodes;
    }

    /**
     * Creates a list of InternalRelationship objects from the given list of LayoutRelationship objects.
     * @param rels
     * @return List of internal relationships
     */
    private InternalRelationship[] createInternalRelationships(LayoutRelationship[] rels) {
        ArrayList listOfInternalRelationships = new ArrayList(rels.length);
        for (int i = 0; i < rels.length; i++) {
            LayoutRelationship relation = rels[i];
            InternalNode src = (InternalNode) relation.getSourceInLayout().getLayoutInformation();
            InternalNode dest = (InternalNode) relation.getDestinationInLayout().getLayoutInformation();
            if ((src != null) && (dest != null)) {
                InternalRelationship internalRelationship = new InternalRelationship(relation, src, dest);
                listOfInternalRelationships.add(internalRelationship);
            } else {
                throw new RuntimeException("Error creating internal relationship, one of the nodes is null: src=" + src + ", dest=" + dest); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
        InternalRelationship[] internalRelationships = new InternalRelationship[listOfInternalRelationships.size()];
        listOfInternalRelationships.toArray(internalRelationships);
        return internalRelationships;
    }

    /**
     * Removes any objects that are currently filtered
     */
    private Object[] filterUnwantedObjects(LayoutItem[] objects) {
        // first remove any entities or relationships that are filtered.
        List unfilteredObjsList = new ArrayList();
        if (filter != null) {
            for (int i = 0; i < objects.length; i++) {
                LayoutItem object = objects[i];
                if (!filter.isObjectFiltered(object)) {
                    unfilteredObjsList.add(object);
                }
            }
            //@tag bug.156266-ClassCast.fix : use reflection to create the array.
            Object[] unfilteredObjs = (Object[]) java.lang.reflect.Array.newInstance(objects.getClass().getComponentType(), unfilteredObjsList.size());
            unfilteredObjsList.toArray(unfilteredObjs);
            return unfilteredObjs;
        }
        return objects;
    }

    /**
     * Filters the entities and relationships to apply the layout on
     */
    @Override
    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    /**
     * Determines the order in which the objects should be displayed.
     * Note: Some algorithms force a specific order.
     */
    @Override
    public void setComparator(Comparator comparator) {
        this.comparator = new InternalComparator(comparator);
    }

    /**
     * Verifies the endpoints of the relationships are entities in the entitiesToLayout list.
     * Allows other classes in this package to use this method to verify the input
     */
    public static boolean verifyInput(LayoutEntity[] entitiesToLayout, LayoutRelationship[] relationshipsToConsider) {
        boolean stillValid = true;
        for (int i = 0; i < relationshipsToConsider.length; i++) {
            LayoutRelationship relationship = relationshipsToConsider[i];
            LayoutEntity source = relationship.getSourceInLayout();
            LayoutEntity destination = relationship.getDestinationInLayout();
            boolean containsSrc = false;
            boolean containsDest = false;
            int j = 0;
            while (j < entitiesToLayout.length && !(containsSrc && containsDest)) {
                if (entitiesToLayout[j].equals(source)) {
                    containsSrc = true;
                }
                if (entitiesToLayout[j].equals(destination)) {
                    containsDest = true;
                }
                j++;
            }
            stillValid = containsSrc && containsDest;
        }
        return stillValid;
    }

    /**
     * Gets the location in the layout bounds for this node
     * @param x
     * @param y
     * @return
     */
    protected DisplayIndependentPoint getLocalLocation(InternalNode[] entitiesToLayout, double x, double y, DisplayIndependentRectangle realBounds) {

        double screenWidth = realBounds.width;
        double screenHeight = realBounds.height;
        DisplayIndependentRectangle layoutBounds = getLayoutBounds(entitiesToLayout, false);
        double localX = (x / screenWidth) * layoutBounds.width + layoutBounds.x;
        double localY = (y / screenHeight) * layoutBounds.height + layoutBounds.y;
        return new DisplayIndependentPoint(localX, localY);
    }

    /**
     * Find an appropriate size for the given nodes, then fit them into the given bounds.
     * The relative locations of the nodes to each other must be preserved.
     * Child classes should set flag reresizeEntitiesAfterLayout to false if they 
     * want to preserve node sizes.
     */
    protected void defaultFitWithinBounds(InternalNode[] entitiesToLayout, DisplayIndependentRectangle realBounds) {
        defaultFitWithinBounds(entitiesToLayout, new InternalRelationship[0], realBounds);
    }

    /**
     * Find an appropriate size for the given nodes, then fit them into the given bounds.
     * The relative locations of the nodes to each other must be preserved.
     * Child classes should set flag reresizeEntitiesAfterLayout to false if they 
     * want to preserve node sizes.
     */
    protected void defaultFitWithinBounds(InternalNode[] entitiesToLayout, InternalRelationship[] relationships, DisplayIndependentRectangle realBounds) {

        DisplayIndependentRectangle layoutBounds;

        if (resizeEntitiesAfterLayout) {
            layoutBounds = getLayoutBounds(entitiesToLayout, false);

            // Convert node x,y to be in percent rather than absolute coords
            convertPositionsToPercentage(entitiesToLayout, relationships, layoutBounds, false /*do not update size*/);

            // Resize and shift nodes
            resizeAndShiftNodes(entitiesToLayout);
        }

        // Recalculate layout, allowing for the node width, which we now know
        layoutBounds = getLayoutBounds(entitiesToLayout, true);

        // adjust node positions again, to the new coordinate system (still as a percentage)
        convertPositionsToPercentage(entitiesToLayout, relationships, layoutBounds, true /*update node size*/);

        DisplayIndependentRectangle screenBounds = calcScreenBounds(realBounds, layoutBounds);

        // Now convert to real screen coordinates
        convertPositionsToCoords(entitiesToLayout, relationships, screenBounds);
    }

    /**
     * Calculate the screen bounds, maintaining the  
     * @param realBounds
     * @return
     */
    private DisplayIndependentRectangle calcScreenBounds(DisplayIndependentRectangle realBounds, DisplayIndependentRectangle layoutBounds) {
        if (resizeEntitiesAfterLayout) { // OK to alter aspect ratio 
            double borderWidth = Math.min(realBounds.width, realBounds.height) / 10.0; // use 10% for the border - 5% on each side
            return new DisplayIndependentRectangle(realBounds.x + borderWidth / 2.0, realBounds.y + borderWidth / 2.0, realBounds.width - borderWidth, realBounds.height - borderWidth);
        } else { // retain layout aspect ratio 
            double heightAdjustment = realBounds.height / layoutBounds.height;
            double widthAdjustment = realBounds.width / layoutBounds.width;
            double ratio = Math.min(heightAdjustment, widthAdjustment);
            double adjustedHeight = layoutBounds.height * ratio;
            double adjustedWidth = layoutBounds.width * ratio;
            double adjustedX = realBounds.x + (realBounds.width - adjustedWidth) / 2.0;
            double adjustedY = realBounds.y + (realBounds.height - adjustedHeight) / 2.0;
            double borderWidth = Math.min(adjustedWidth, adjustedHeight) / 10.0; // use 10% for the border - 5% on each side
            return new DisplayIndependentRectangle(adjustedX + borderWidth / 2.0, adjustedY + borderWidth / 2.0, adjustedWidth - borderWidth, adjustedHeight - borderWidth);
        }
    }

    /**
     * Find and set the node size - shift the nodes to the right and down to make 
     * room for the width and height. 
     * @param entitiesToLayout
     * @param relationships
     */
    private void resizeAndShiftNodes(InternalNode[] entitiesToLayout) {
        // get maximum node size as percent of screen dimmensions
        double nodeSize = getNodeSize(entitiesToLayout);
        double halfNodeSize = nodeSize / 2;

        // Resize and shift nodes
        for (int i = 0; i < entitiesToLayout.length; i++) {
            InternalNode node = entitiesToLayout[i];
            node.setInternalSize(nodeSize, nodeSize);
            node.setInternalLocation(node.getInternalX() + halfNodeSize, node.getInternalY() + halfNodeSize);
        }
    }

    /**
     * Convert all node positions into a percentage of the screen. If includeNodeSize
     * is true then this also updates the node's internal size. 
     * @param entitiesToLayout
     */
    private void convertPositionsToPercentage(InternalNode[] entitiesToLayout, InternalRelationship[] relationships, DisplayIndependentRectangle layoutBounds, boolean includeNodeSize) {

        // Adjust node positions and sizes
        for (int i = 0; i < entitiesToLayout.length; i++) {
            InternalNode node = entitiesToLayout[i];
            DisplayIndependentPoint location = node.getInternalLocation().convertToPercent(layoutBounds);
            node.setInternalLocation(location.x, location.y);
            if (includeNodeSize) { // adjust node sizes
                double width = node.getInternalWidth() / layoutBounds.width;
                double height = node.getInternalHeight() / layoutBounds.height;
                node.setInternalSize(width, height);
            }
        }

        // Adjust bendpoint positions
        for (int i = 0; i < relationships.length; i++) {
            InternalRelationship rel = relationships[i];
            for (int j = 0; j < rel.getBendPoints().size(); j++) {
                BendPoint bp = (BendPoint) rel.getBendPoints().get(j);
                DisplayIndependentPoint toPercent = bp.convertToPercent(layoutBounds);
                bp.setX(toPercent.x);
                bp.setY(toPercent.y);
            }
        }
    }

    /**
     * Convert the positions from a percentage of bounds area to fixed
     * coordinates. NOTE: ALL OF THE POSITIONS OF NODES UNTIL NOW WERE FOR THE
     * CENTER OF THE NODE - Convert it to the left top corner.
     * 
     * @param entitiesToLayout
     * @param relationships
     * @param realBounds
     */
    private void convertPositionsToCoords(InternalNode[] entitiesToLayout, InternalRelationship[] relationships, DisplayIndependentRectangle screenBounds) {

        // Adjust node positions and sizes
        for (int i = 0; i < entitiesToLayout.length; i++) {
            InternalNode node = entitiesToLayout[i];
            double width = node.getInternalWidth() * screenBounds.width;
            double height = node.getInternalHeight() * screenBounds.height;
            DisplayIndependentPoint location = node.getInternalLocation().convertFromPercent(screenBounds);
            node.setInternalLocation(location.x - width / 2, location.y - height / 2);
            if (resizeEntitiesAfterLayout) {
                adjustNodeSizeAndPos(node, height, width);
            } else {
                node.setInternalSize(width, height);
            }
        }

        // Adjust bendpoint positions and shift based on source node size
        for (int i = 0; i < relationships.length; i++) {
            InternalRelationship rel = relationships[i];
            for (int j = 0; j < rel.getBendPoints().size(); j++) {
                BendPoint bp = (BendPoint) rel.getBendPoints().get(j);
                DisplayIndependentPoint fromPercent = bp.convertFromPercent(screenBounds);
                bp.setX(fromPercent.x);
                bp.setY(fromPercent.y);
            }
        }
    }

    /**
     * Adjust node size to take advantage of space. Reset position to top left corner of node. 
     * @param node
     * @param height
     * @param width
     */
    private void adjustNodeSizeAndPos(InternalNode node, double height, double width) {
        double widthUsingHeight = height * widthToHeightRatio;
        if (widthToHeightRatio <= 1.0 && widthUsingHeight <= width) {
            double widthToUse = height * widthToHeightRatio;
            double leftOut = width - widthToUse;
            node.setInternalSize(Math.max(height * widthToHeightRatio, MIN_ENTITY_SIZE), Math.max(height, MIN_ENTITY_SIZE));
            node.setInternalLocation(node.getInternalX() + leftOut / 2, node.getInternalY());

        } else {
            double heightToUse = height / widthToHeightRatio;
            double leftOut = height - heightToUse;

            node.setInternalSize(Math.max(width, MIN_ENTITY_SIZE), Math.max(width / widthToHeightRatio, MIN_ENTITY_SIZE));
            node.setInternalLocation(node.getInternalX(), node.getInternalY() + leftOut / 2);
        }

    }

    /**
     * Returns the maximum possible node size as a percentage of the width or height in current coord system.
     */
    private double getNodeSize(InternalNode[] entitiesToLayout) {
        double width, height;
        if (entitiesToLayout.length == 1) {
            width = 0.8;
            height = 0.8;
        } else {
            DisplayIndependentDimension minimumDistance = getMinimumDistance(entitiesToLayout);
            width = 0.8 * minimumDistance.width;
            height = 0.8 * minimumDistance.height;
        }
        return Math.max(width, height);
    }

    /**
     * Find the bounds in which the nodes are located.  Using the bounds against the real bounds
     * of the screen, the nodes can proportionally be placed within the real bounds.
     * The bounds can be determined either including the size of the nodes or not.  If the size
     * is not included, the bounds will only be guaranteed to include the center of each node.
     */
    protected DisplayIndependentRectangle getLayoutBounds(InternalNode[] entitiesToLayout, boolean includeNodeSize) {
        double rightSide = Double.MIN_VALUE;
        double bottomSide = Double.MIN_VALUE;
        double leftSide = Double.MAX_VALUE;
        double topSide = Double.MAX_VALUE;
        for (int i = 0; i < entitiesToLayout.length; i++) {
            InternalNode entity = entitiesToLayout[i];
            if (entity.hasPreferredLocation()) {
                continue;
            }

            if (includeNodeSize) {
                leftSide = Math.min(entity.getInternalX() - entity.getInternalWidth() / 2, leftSide);
                topSide = Math.min(entity.getInternalY() - entity.getInternalHeight() / 2, topSide);
                rightSide = Math.max(entity.getInternalX() + entity.getInternalWidth() / 2, rightSide);
                bottomSide = Math.max(entity.getInternalY() + entity.getInternalHeight() / 2, bottomSide);
            } else {
                leftSide = Math.min(entity.getInternalX(), leftSide);
                topSide = Math.min(entity.getInternalY(), topSide);
                rightSide = Math.max(entity.getInternalX(), rightSide);
                bottomSide = Math.max(entity.getInternalY(), bottomSide);
            }
        }
        return new DisplayIndependentRectangle(leftSide, topSide, rightSide - leftSide, bottomSide - topSide);
    }

    /** 
     * minDistance is the closest that any two points are together.
     * These two points become the center points for the two closest nodes, 
     * which we wish to make them as big as possible without overlapping.
     * This will be the maximum of minDistanceX and minDistanceY minus a bit, lets say 20%
     * 
     * We make the recommended node size a square for convenience.
     * 
     * 
     *    _______
     *   |       | 
     *   |       |
     *   |   +   |
     *   |   |\  |
     *   |___|_\_|_____
     *       | | \     |
     *       | |  \    |
     *       +-|---+   |
     *         |       |
     *         |_______|
     * 
     *  
     * 
     */
    private DisplayIndependentDimension getMinimumDistance(InternalNode[] entitiesToLayout) {
        DisplayIndependentDimension horAndVertdistance = new DisplayIndependentDimension(Double.MAX_VALUE, Double.MAX_VALUE);
        double minDistance = Double.MAX_VALUE; // the minimum distance between all the nodes
        //TODO: Very Slow!
        for (int i = 0; i < entitiesToLayout.length; i++) {
            InternalNode layoutEntity1 = entitiesToLayout[i];
            double x1 = layoutEntity1.getInternalX();
            double y1 = layoutEntity1.getInternalY();
            for (int j = i + 1; j < entitiesToLayout.length; j++) {
                InternalNode layoutEntity2 = entitiesToLayout[j];
                double x2 = layoutEntity2.getInternalX();
                double y2 = layoutEntity2.getInternalY();
                double distanceX = Math.abs(x1 - x2);
                double distanceY = Math.abs(y1 - y2);
                double distance = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));

                if (distance < minDistance) {
                    minDistance = distance;
                    horAndVertdistance.width = distanceX;
                    horAndVertdistance.height = distanceY;
                }
            }
        }
        return horAndVertdistance;
    }

    /**
     * Set the width to height ratio you want the entities to use
     */
    @Override
    public void setEntityAspectRatio(double ratio) {
        widthToHeightRatio = ratio;
    }

    /**
     * Returns the width to height ratio this layout will use to set the size of the entities.
     */
    @Override
    public double getEntityAspectRatio() {
        return widthToHeightRatio;
    }

    /**
     * A layout algorithm could take an uncomfortable amout of time to complete.  To relieve some of
     * the mystery, the layout algorithm will notify each ProgressListener of its progress. 
     */
    @Override
    public void addProgressListener(ProgressListener listener) {
        if (!progressListeners.contains(listener)) {
            progressListeners.add(listener);
        }
    }

    /**
     * Removes the given progress listener, preventing it from receiving any more updates.
     */
    @Override
    public void removeProgressListener(ProgressListener listener) {
        if (progressListeners.contains(listener)) {
            progressListeners.remove(listener);
        }
    }

    /**
     * Updates the layout locations so the external nodes know about the new locations
     */
    protected void updateLayoutLocations(InternalNode[] nodes) {
        for (int i = 0; i < nodes.length; i++) {
            InternalNode node = nodes[i];
            if (!node.hasPreferredLocation()) {
                node.setLocation(node.getInternalX(), node.getInternalY());

                if ((layout_styles & LayoutStyles.NO_LAYOUT_NODE_RESIZING) != 1) {
                    // Only set the size if we are supposed to
                    node.setSize(node.getInternalWidth(), node.getInternalHeight());
                }
            }
        }
    }

    protected void fireProgressStarted(int totalNumberOfSteps) {
        ProgressEvent event = new ProgressEvent(0, totalNumberOfSteps);
        for (int i = 0; i < progressListeners.size(); i++) {
            ProgressListener listener = (ProgressListener) progressListeners.get(i);

            listener.progressStarted(event);
        }
    }

    protected void fireProgressEnded(int totalNumberOfSteps) {
        ProgressEvent event = new ProgressEvent(totalNumberOfSteps, totalNumberOfSteps);
        for (int i = 0; i < progressListeners.size(); i++) {
            ProgressListener listener = (ProgressListener) progressListeners.get(i);
            listener.progressEnded(event);
        }

    }

    /**
     * Fires an event to notify all of the registered ProgressListeners that another step
     * has been completed in the algorithm.
     * @param currentStep The current step completed.
     * @param totalNumberOfSteps The total number of steps in the algorithm.
     */
    protected void fireProgressEvent(int currentStep, int totalNumberOfSteps) {

        // Update the layout locations to the external nodes
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MILLISECOND, -MIN_TIME_DELAY_BETWEEN_PROGRESS_EVENTS);

        if (now.after(lastProgressEventFired) || currentStep == totalNumberOfSteps) {
            ProgressEvent event = new ProgressEvent(currentStep, totalNumberOfSteps);

            for (int i = 0; i < progressListeners.size(); i++) {

                ProgressListener listener = (ProgressListener) progressListeners.get(i);
                listener.progressUpdated(event);
            }
            lastProgressEventFired = Calendar.getInstance();
        }

    }

    protected int getNumberOfProgressListeners() {
        return progressListeners.size();
    }

    private void checkThread() {
        if (this.creationThread != Thread.currentThread()) {
            throw new RuntimeException("Invalid Thread Access."); //$NON-NLS-1$
        }
    }

}
