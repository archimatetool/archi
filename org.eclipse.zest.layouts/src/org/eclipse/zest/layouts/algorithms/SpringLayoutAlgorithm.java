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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.dataStructures.DisplayIndependentRectangle;
import org.eclipse.zest.layouts.dataStructures.InternalNode;
import org.eclipse.zest.layouts.dataStructures.InternalRelationship;

/**
 * The SpringLayoutAlgorithm has its own data repository and relation
 * repository. A user can populate the repository, specify the layout
 * conditions, do the computation and query the computed results.
 * <p>
 * Instructions for using SpringLayoutAlgorithm: <br>
 * 1. Instantiate a SpringLayout object; <br>
 * 2. Populate the data repository using {@link #add add(...)}; <br>
 * 3. Populate the relation repository using
 * {@link #addRelation addRelation(...)}; <br>
 * 4. Execute {@link #compute compute()}; <br>
 * 5. Execute {@link #fitWithinBounds fitWithinBounds(...)}; <br>
 * 6. Query the computed results(node size and node position).
 * 
 * @version 2.0
 * @author Ian Bull
 * @author Casey Best (version 1.0 by Jingwei Wu/Rob Lintern)
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class SpringLayoutAlgorithm extends ContinuousLayoutAlgorithm {

    private final static boolean DEFAULT_ANCHOR = false;

    /**
     * The default value for the spring layout number of interations.
     */
    public static final int DEFAULT_SPRING_ITERATIONS = 1000;

    /**
     * the default value for the time algorithm runs.
     */
    public static final long MAX_SPRING_TIME = 10000;

    /**
     * The default value for positioning nodes randomly.
     */
    public static final boolean DEFAULT_SPRING_RANDOM = true;

    /**
     * The default value for ignoring unconnected nodes.
     */
    public static final boolean DEFAULT_SPRING_IGNORE_UNCON = true;

    /**
     * The default value for separating connected components.
     */
    public static final boolean DEFAULT_SPRING_SEPARATE_COMPONENTS = true;

    /**
     * The default value for the spring layout move-control.
     */
    public static final double DEFAULT_SPRING_MOVE = 1.0f;

    /**
     * The default value for the spring layout strain-control.
     */
    public static final double DEFAULT_SPRING_STRAIN = 1.0f;

    /**
     * The default value for the spring layout length-control.
     */
    public static final double DEFAULT_SPRING_LENGTH = 1.0f;

    /**
     * The default value for the spring layout gravitation-control.
     */
    public static final double DEFAULT_SPRING_GRAVITATION = 1.0f;

    /**
     * The variable can be customized to set the number of iterations used.
     */
    private static int sprIterations = DEFAULT_SPRING_ITERATIONS;

    /**
     * This variable can be customized to set the max number of MS the algorithm
     * should run
     */
    private static long maxTimeMS = MAX_SPRING_TIME;

    /**
     * The variable can be customized to set whether or not the spring layout
     * nodes are positioned randomly before beginning iterations.
     */
    private static boolean sprRandom = DEFAULT_SPRING_RANDOM;

    /**
     * Minimum distance considered between nodes
     */
    protected static final double MIN_DISTANCE = 0.001d;

    /**
     * An arbitrarily small value in mathematics.
     */
    protected static final double EPSILON = 0.001d;

    /**
     * The variable can be customerized to set the spring layout move-control.
     */
    private static double sprMove = DEFAULT_SPRING_MOVE;

    /**
     * The variable can be customized to set the spring layout strain-control.
     */
    private static double sprStrain = DEFAULT_SPRING_STRAIN;

    /**
     * The variable can be customized to set the spring layout length-control.
     */
    private static double sprLength = DEFAULT_SPRING_LENGTH;

    /**
     * The variable can be customized to set the spring layout
     * gravitation-control.
     */
    private static double sprGravitation = DEFAULT_SPRING_GRAVITATION;

    /**
     * The largest movement of all vertices that has occured in the most recent
     * iteration.
     */
    private double largestMovement = 0;

    /**
     * Maps a src and dest object to the number of relations between them. Key
     * is src.toString() + dest.toString(), value is an Integer
     */
    private Map srcDestToNumRelsMap;

    /**
     * Maps a src and dest object to the average weight of the relations between
     * them. Key is src.toString() + dest.toString(), value is a Double
     */
    private Map srcDestToRelsAvgWeightMap;

    /**
     * Maps a relationship type to a weight. Key is a string, value is a Double
     */
    private static Map relTypeToWeightMap = new HashMap();

    private int iteration;

    private int[][] srcDestToNumRels;

    private double[][] srcDestToRelsAvgWeight;

    private double[] tempLocationsX;

    private double[] tempLocationsY;

    private double[] forcesX;

    private double[] forcesY;

    private boolean[] anchors;

    private DisplayIndependentRectangle bounds = null;

    Date date = null;

    /**
     * Constructor.
     */
    public SpringLayoutAlgorithm(int styles) {
        super(styles);
        srcDestToNumRelsMap = new HashMap();
        srcDestToRelsAvgWeightMap = new HashMap();
        date = new Date();
    }

    /**
     * Creates a sprint layout algoirthm with no style
     *
     */
    public SpringLayoutAlgorithm() {
        this(LayoutStyles.NONE);
    }

    @Override
    public void setLayoutArea(double x, double y, double width, double height) {
        bounds = new DisplayIndependentRectangle(x, y, width, height);
    }

    /**
     * Sets the spring layout move-control.
     * 
     * @param move
     *            The move-control value.
     */
    public void setSpringMove(double move) {
        sprMove = move;
    }

    /**
     * Returns the move-control value of this SpringLayoutAlgorithm in
     * double presion.
     * 
     * @return The move-control value.
     */
    public double getSpringMove() {
        return sprMove;
    }

    /**
     * Sets the spring layout strain-control.
     * 
     * @param strain
     *            The strain-control value.
     */
    public void setSpringStrain(double strain) {
        sprStrain = strain;
    }

    /**
     * Returns the strain-control value of this SpringLayoutAlgorithm in
     * double presion.
     * 
     * @return The strain-control value.
     */
    public double getSpringStrain() {
        return sprStrain;
    }

    /**
     * Sets the spring layout length-control.
     * 
     * @param length
     *            The length-control value.
     */
    public void setSpringLength(double length) {
        sprLength = length;
    }

    /**
     * Gets the max time this algorithm will run for
     * 
     * @return
     */
    public long getSpringTimeout() {
        return maxTimeMS;
    }

    /**
     * Sets the spring timeout
     * 
     * @param timeout
     */
    public void setSpringTimeout(long timeout) {
        maxTimeMS = timeout;
    }

    /**
     * Returns the length-control value of this SpringLayoutAlgorithm in
     * double presion.
     * 
     * @return The length-control value.
     */
    public double getSpringLength() {
        return sprLength;
    }

    /**
     * Sets the spring layout gravitation-control.
     * 
     * @param gravitation
     *            The gravitation-control value.
     */
    public void setSpringGravitation(double gravitation) {
        sprGravitation = gravitation;
    }

    /**
     * Returns the gravitation-control value of this SpringLayoutAlgorithm
     * in double presion.
     * 
     * @return The gravitation-control value.
     */
    public double getSpringGravitation() {
        return sprGravitation;
    }

    /**
     * Sets the number of iterations to be used.
     * 
     * @param gravitation
     *            The number of iterations.
     */
    public void setIterations(int iterations) {
        sprIterations = iterations;
    }

    /**
     * Returns the number of iterations to be used.
     * 
     * @return The number of iterations.
     */
    public int getIterations() {
        return sprIterations;
    }

    /**
     * Sets whether or not this SpringLayoutAlgorithm will layout the
     * nodes randomly before beginning iterations.
     * 
     * @param random
     *            The random placement value.
     */
    public void setRandom(boolean random) {
        sprRandom = random;
    }

    /**
     * Returns whether or not this SpringLayoutAlgorithm will layout the
     * nodes randomly before beginning iterations.
     */
    public boolean getRandom() {
        return sprRandom;
    }

    @SuppressWarnings("deprecation")
    public void setWeight(String relType, double weight) {
        relTypeToWeightMap.put(relType, new Double(weight));
    }

    public double getWeight(String relType) {
        Double weight = (Double) relTypeToWeightMap.get(relType);
        return (weight == null) ? 1 : weight.doubleValue();
    }

    /**
     * Sets the default conditions.
     */
    public void setDefaultConditions() {
        // sprMove = DEFAULT_SPRING_MOVE;
        // sprStrain = DEFAULT_SPRING_STRAIN;
        // sprLength = DEFAULT_SPRING_LENGTH;
        // sprGravitation = DEFAULT_SPRING_GRAVITATION;
        // sprIterations = DEFAULT_SPRING_ITERATIONS;
    }

    /**
     * Clean up after done
     * 
     * @param entitiesToLayout
     */
    private void reset(InternalNode[] entitiesToLayout) {
        tempLocationsX = null;
        tempLocationsY = null;
        forcesX = null;
        forcesY = null;
        anchors = null;
        setDefaultConditions();
        srcDestToNumRelsMap = new HashMap();
        srcDestToRelsAvgWeightMap = new HashMap();
        relTypeToWeightMap = new HashMap();
    }

    private long startTime = 0;

    @Override
    protected void preLayoutAlgorithm(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider, double x, double y, double width, double height) {
        // TODO: Filter out any non-wanted entities and relationships
        // super.applyLayout(entitiesToLayout, relationshipsToConsider, x, y,
        // width, height);
        //InternalNode[] a_entitiesToLayout = (InternalNode[]) entitiesToLayout.toArray(new InternalNode[entitiesToLayout.size()]);
        bounds = new DisplayIndependentRectangle(x, y, width, height);
        tempLocationsX = new double[entitiesToLayout.length];
        tempLocationsY = new double[entitiesToLayout.length];
        forcesX = new double[entitiesToLayout.length];
        forcesY = new double[entitiesToLayout.length];
        anchors = new boolean[entitiesToLayout.length];

        for (int i = 0; i < entitiesToLayout.length; i++) {
            anchors[i] = DEFAULT_ANCHOR;
        }
        for (int i = 0; i < relationshipsToConsider.length; i++) {
            InternalRelationship layoutRelationship = relationshipsToConsider[i];
            addRelation(layoutRelationship);
        }

        // do the calculations
        preCompute(entitiesToLayout);
        startTime = date.getTime();
    }

    @Override
    protected void postLayoutAlgorithm(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider) {
        reset(entitiesToLayout);
    }

    /**
     * Adds a simple relation between two nodes to the relation repository.
     * 
     * @param layoutRelationship
     *            The simple relation to be added
     * @throws java.lang.NullPointerExcetption
     *             If <code>sr</code> is null
     * @see SimpleRelation
     */
    @SuppressWarnings("deprecation")
    private void addRelation(InternalRelationship layoutRelationship) {
        if (layoutRelationship == null) {
            throw new IllegalArgumentException("The arguments can not be null!"); //$NON-NLS-1$
        } else {
            double weight = layoutRelationship.getWeight();
            weight = (weight <= 0 ? 0.1 : weight);
            String key1 = layoutRelationship.getSource().toString() + layoutRelationship.getDestination().toString();
            String key2 = layoutRelationship.getDestination().toString() + layoutRelationship.getSource().toString();
            String[] keys = { key1, key2 };
            for (int i = 0; i < keys.length; i++) {
                String key = keys[i];
                Integer count = (Integer) srcDestToNumRelsMap.get(key);
                Double avgWeight = (Double) srcDestToRelsAvgWeightMap.get(key);
                if (count == null) {
                    count = new Integer(1);
                    avgWeight = new Double(weight);
                } else {
                    int newCount = count.intValue() + 1;
                    double newAverage = (avgWeight.doubleValue() * count.doubleValue() + weight) / newCount;
                    avgWeight = new Double(newAverage);
                    count = new Integer(newCount);
                }
                srcDestToNumRelsMap.put(key, count);
                srcDestToRelsAvgWeightMap.put(key, avgWeight);
            }
        }
    }

    private void preCompute(InternalNode[] entitiesToLayout) {
        // count number of relationships between all nodes and the average
        // weight between them
        srcDestToNumRels = new int[entitiesToLayout.length][entitiesToLayout.length];
        srcDestToRelsAvgWeight = new double[entitiesToLayout.length][entitiesToLayout.length];

        for (int i = 0; i < entitiesToLayout.length - 1; i++) {
            InternalNode layoutEntity1 = entitiesToLayout[i];
            for (int j = i + 1; j < entitiesToLayout.length; j++) {
                InternalNode layoutEntity2 = entitiesToLayout[j];
                srcDestToNumRels[i][j] = numRelations(layoutEntity1, layoutEntity2);
                srcDestToNumRels[i][j] += numRelations(layoutEntity2, layoutEntity1);
                srcDestToRelsAvgWeight[i][j] = avgWeight(layoutEntity1, layoutEntity2);
            }
        }

        if (sprRandom)
            placeRandomly(entitiesToLayout); // put vertices in random places
        else
            convertToUnitCoordinates(entitiesToLayout);

        iteration = 1;
        largestMovement = Double.MAX_VALUE;
    }

    // TODO: This is a complete Clone! (and not in a good way)
    protected DisplayIndependentRectangle getLayoutBoundsTemp(InternalNode[] entitiesToLayout, boolean includeNodeSize) {
        double rightSide = Double.MIN_VALUE;
        double bottomSide = Double.MIN_VALUE;
        double leftSide = Double.MAX_VALUE;
        double topSide = Double.MAX_VALUE;
        for (int i = 0; i < entitiesToLayout.length; i++) {
            double x = tempLocationsX[i];
            double y = tempLocationsY[i];

            leftSide = Math.min(x, leftSide);
            topSide = Math.min(y, topSide);
            rightSide = Math.max(x, rightSide);
            bottomSide = Math.max(y, bottomSide);

        }
        return new DisplayIndependentRectangle(leftSide, topSide, rightSide - leftSide, bottomSide - topSide);
    }

    protected void convertNodePositionsBack(int i, InternalNode entityToConvert, double px, double py, double screenWidth, double screenHeight, DisplayIndependentRectangle layoutBounds) {

        // If the node selected is outside the screen, map it to the boarder
        if (px > screenWidth)
            px = screenWidth;
        if (py > screenHeight)
            py = screenHeight;

        if (px < 0)
            px = 1;
        if (py < 0)
            py = 1;

        double x = (px / screenWidth) * layoutBounds.width + layoutBounds.x;
        double y = (py / screenHeight) * layoutBounds.height + layoutBounds.y;

        tempLocationsX[i] = x;
        tempLocationsY[i] = y;
        //setTempLocation(entityToConvert, new DisplayIndependentPoint(x, y));

        if (entityToConvert.getInternalX() < 0) {
            // System.out.println("We have nodes less than 0 here!");
        }

    }

    private void checkPreferredLocation(InternalNode[] entitiesToLayout, DisplayIndependentRectangle realBounds) {
        // use 10% for the border - 5% on each side
        double borderWidth = Math.min(realBounds.width, realBounds.height) / 10.0;
        DisplayIndependentRectangle screenBounds = new DisplayIndependentRectangle(realBounds.x + borderWidth / 2.0, realBounds.y + borderWidth / 2.0, realBounds.width - borderWidth, realBounds.height - borderWidth);

        DisplayIndependentRectangle layoutBounds = getLayoutBoundsTemp(entitiesToLayout, false);
        for (int i = 0; i < entitiesToLayout.length; i++) {
            InternalNode layoutEntity = entitiesToLayout[i];
            if (layoutEntity.hasPreferredLocation()) {
                convertNodePositionsBack(i, layoutEntity, layoutEntity.getPreferredX(), layoutEntity.getPreferredY(), screenBounds.width, screenBounds.height, layoutBounds);
            }
        }
    }

    /**
     * Scales the current iteration counter based on how long the algorithm has
     * been running for. You can set the MaxTime in maxTimeMS!
     */
    private void setSprIterationsBasedOnTime() {
        if (maxTimeMS <= 0)
            return;

        long currentTime = date.getTime();
        double fractionComplete = ((double) (currentTime - startTime) / ((double) maxTimeMS));
        int currentIteration = (int) (fractionComplete * sprIterations);
        if (currentIteration > iteration) {
            iteration = currentIteration;
        }

    }

    @Override
    protected boolean performAnotherNonContinuousIteration() {
        setSprIterationsBasedOnTime();
        if (iteration <= sprIterations && largestMovement >= sprMove)
            return true;
        else
            return false;
    }

    @Override
    protected int getCurrentLayoutStep() {
        return iteration;
    }

    @Override
    protected int getTotalNumberOfLayoutSteps() {
        return sprIterations;
    }

    @Override
    protected void computeOneIteration(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider, double x, double y, double width, double height) {
        if (bounds == null)
            bounds = new DisplayIndependentRectangle(x, y, width, height);
        checkPreferredLocation(entitiesToLayout, bounds);
        computeForces(entitiesToLayout);
        largestMovement = Double.MAX_VALUE;
        computePositions(entitiesToLayout);

        for (int i = 0; i < entitiesToLayout.length; i++) {
            InternalNode layoutEntity = entitiesToLayout[i];
            layoutEntity.setInternalLocation(tempLocationsX[i], tempLocationsY[i]);
        }

        defaultFitWithinBounds(entitiesToLayout, bounds);

        iteration++;
    }

    /**
     * Puts vertices in random places, all between (0,0) and (1,1).
     */
    public void placeRandomly(InternalNode[] entitiesToLayout) {
        // If only one node in the data repository, put it in the middle
        if (entitiesToLayout.length == 1) {
            // If only one node in the data repository, put it in the middle
            tempLocationsX[0] = 0.5;
            tempLocationsY[0] = 0.5;
        } else {
            for (int i = 0; i < entitiesToLayout.length; i++) {
                if (i == 0) {
                    tempLocationsX[i] = 0.0;
                    tempLocationsY[i] = 0.0;
                } else if (i == 1) {
                    tempLocationsX[i] = 1.0;
                    tempLocationsY[i] = 1.0;
                } else {
                    tempLocationsX[i] = Math.random();
                    tempLocationsY[i] = Math.random();
                }
            }
        }
    }

    // /////////////////////////////////////////////////////////////////
    // /// Protected Methods /////
    // /////////////////////////////////////////////////////////////////

    /**
     * Computes the force for each node in this SpringLayoutAlgorithm. The
     * computed force will be stored in the data repository
     */
    protected void computeForces(InternalNode[] entitiesToLayout) {

        // initialize all forces to zero
        for (int i = 0; i < entitiesToLayout.length; i++) {
            forcesX[i] = 0.0;
            forcesY[i] = 0.0;
        }

        // TODO: Again really really slow!

        for (int i = 0; i < entitiesToLayout.length - 1; i++) {
            InternalNode sourceEntity = entitiesToLayout[i];

            double srcLocationX = tempLocationsX[i];
            double srcLocationY = tempLocationsY[i];
            double fx = forcesX[i]; // force in x direction
            double fy = forcesY[i]; // force in y direction

            for (int j = i + 1; j < entitiesToLayout.length; j++) {
                InternalNode destinationEntity = entitiesToLayout[j];

                if (!destinationEntity.equals(sourceEntity)) {
                    double destLocationX = tempLocationsX[j];
                    double destLocationY = tempLocationsY[j];
                    double dx = srcLocationX - destLocationX;
                    double dy = srcLocationY - destLocationY;
                    double distance = Math.sqrt(dx * dx + dy * dy);
                    double distance_sq = distance * distance;
                    // make sure distance and distance squared not too small
                    distance = Math.max(MIN_DISTANCE, distance);

                    // If there are relationships between srcObj and destObj
                    // then decrease force on srcObj (a pull) in direction of destObj
                    // If no relation between srcObj and destObj then increase
                    // force on srcObj (a push) from direction of destObj.
                    int numRels = srcDestToNumRels[i][j];
                    double avgWeight = srcDestToRelsAvgWeight[i][j];
                    if (numRels > 0) {
                        // nodes are pulled towards each other
                        double f = sprStrain * Math.log(distance / sprLength) * numRels * avgWeight;

                        fx = fx - (f * dx / distance);
                        fy = fy - (f * dy / distance);

                    } else {
                        // nodes are repelled from each other
                        //double f = Math.min(100, sprGravitation / (distance*distance));
                        double f = sprGravitation / (distance_sq);
                        fx = fx + (f * dx / distance);
                        fy = fy + (f * dy / distance);
                    }

                    // According to Newton, "for every action, there is an equal
                    // and opposite reaction."
                    // so give the dest an opposite force
                    forcesX[j] = forcesX[j] - fx;
                    forcesY[j] = forcesY[j] - fy;
                }
            }

            /*
             * //make sure forces aren't too big if (fx > 0 ) fx = Math.min(fx,
             * 10*sprMove); else fx = Math.max(fx, -10*sprMove); if (fy > 0) fy =
             * Math.min(fy, 10*sprMove); else fy = Math.max(fy, -10*sprMove);
             */
            forcesX[i] = fx;
            forcesY[i] = fy;
            // Remove the src object from the list of destinations since
            // we've already calculated the force from it on all other
            // objects.
            // dests.remove(srcObj);

        }
    }

    /**
     * Computes the position for each node in this SpringLayoutAlgorithm.
     * The computed position will be stored in the data repository. position =
     * position + sprMove * force
     */
    protected void computePositions(InternalNode[] entitiesToLayout) {
        for (int i = 0; i < entitiesToLayout.length; i++) {
            if (!anchors[i] || entitiesToLayout[i].hasPreferredLocation()) {
                double oldX = tempLocationsX[i];
                double oldY = tempLocationsY[i];
                double deltaX = sprMove * forcesX[i];
                double deltaY = sprMove * forcesY[i];

                // constrain movement, so that nodes don't shoot way off to the edge
                double maxMovement = 0.2d * sprMove;
                if (deltaX >= 0) {
                    deltaX = Math.min(deltaX, maxMovement);
                } else {
                    deltaX = Math.max(deltaX, -maxMovement);
                }
                if (deltaY >= 0) {
                    deltaY = Math.min(deltaY, maxMovement);
                } else {
                    deltaY = Math.max(deltaY, -maxMovement);
                }

                largestMovement = Math.max(largestMovement, Math.abs(deltaX));
                largestMovement = Math.max(largestMovement, Math.abs(deltaY));

                double newX = oldX + deltaX;
                double newY = oldY + deltaY;
                tempLocationsX[i] = newX;
                tempLocationsY[i] = newY;
            }

        }

    }

    /**
     * Converts the position for each node in this SpringLayoutAlgorithm
     * to unit coordinates in double precision. The computed positions will be
     * still stored in the data repository.
     */
    protected void convertToUnitCoordinates(InternalNode[] entitiesToLayout) {
        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;
        for (int i = 0; i < entitiesToLayout.length; i++) {
            InternalNode layoutEntity = entitiesToLayout[i];
            minX = Math.min(minX, layoutEntity.getInternalX());
            minY = Math.min(minY, layoutEntity.getInternalY());
            maxX = Math.max(maxX, layoutEntity.getInternalX());
            maxY = Math.max(maxY, layoutEntity.getInternalY());
        }

        double spanX = maxX - minX;
        double spanY = maxY - minY;
        double maxSpan = Math.max(spanX, spanY);

        if (maxSpan > EPSILON) {
            for (int i = 0; i < entitiesToLayout.length; i++) {
                InternalNode layoutEntity = entitiesToLayout[i];
                double x = (layoutEntity.getInternalX() - minX) / spanX;
                double y = (layoutEntity.getInternalY() - minY) / spanY;
                tempLocationsX[i] = x;
                tempLocationsY[i] = y;
            }
        } else {
            placeRandomly(entitiesToLayout);
        }
    }

    /**
     * Examines the number of specified relation between the <code>src</code>
     * and the <code>dest</code> that exist in this
     * SpringLayoutAlgorithm's relation repository.
     * 
     * @param src
     *            The source part of the relaton to be examined.
     * @param dest
     *            The destination part of the relation to be examined.
     * @return The number of relations between src and dest.
     */
    private int numRelations(Object src, Object dest) {
        String key = src.toString() + dest.toString();
        Integer count = (Integer) srcDestToNumRelsMap.get(key);
        int intCount = (count == null) ? 0 : count.intValue();
        return intCount;
    }

    /**
     * Returns the average weight between a src and dest object.
     * 
     * @param src
     * @param dest
     * @return The average weight between the given src and dest nodes
     */
    private double avgWeight(Object src, Object dest) {
        String key = src.toString() + dest.toString();
        Double avgWeight = (Double) srcDestToRelsAvgWeightMap.get(key);
        double doubleWeight = (avgWeight == null) ? 1 : avgWeight.doubleValue();
        return doubleWeight;
    }

    @Override
    protected boolean isValidConfiguration(boolean asynchronous, boolean continueous) {
        if (asynchronous && continueous)
            return true;
        else if (asynchronous && !continueous)
            return true;
        else if (!asynchronous && continueous)
            return false;
        else if (!asynchronous && !continueous)
            return true;

        return false;
    }

}
