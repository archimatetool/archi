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

import java.util.Arrays;

import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.dataStructures.InternalNode;
import org.eclipse.zest.layouts.dataStructures.InternalRelationship;

/**
 * @version 2.0
 * @author Ian Bull
 * @author Casey Best and Rob Lintern
 */
@SuppressWarnings("unchecked")
public class GridLayoutAlgorithm extends AbstractLayoutAlgorithm {

    private static final double PADDING_PERCENTAGE = 0.95;

    protected int rowPadding = 0;

    @Override
    public void setLayoutArea(double x, double y, double width, double height) {
        throw new RuntimeException("Operation not implemented"); //$NON-NLS-1$
    }

    int rows, cols, numChildren;
    double colWidth, rowHeight, offsetX, offsetY;
    int totalProgress;
    double h, w;

    /**
     * Initializes the grid layout.
     * @param styles
     * @see LayoutStyles
     */
    public GridLayoutAlgorithm(int styles) {
        super(styles);
    }

    /**
     * Inititalizes the grid layout with no style.
     */
    public GridLayoutAlgorithm() {
        this(LayoutStyles.NONE);
    }

    @Override
    protected int getCurrentLayoutStep() {
        // TODO: This isn't right
        return 0;
    }

    @Override
    protected int getTotalNumberOfLayoutSteps() {
        return totalProgress;
    }

    /**
     * 
     */
    @Override
    protected void preLayoutAlgorithm(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider, double x, double y, double width, double height) {

        // TODO: Filter unwanted entities and relationships
        //super.applyLayout (entitiesToLayout, relationshipsToConsider, boundsX, boundsY, boundsWidth, boundsHeight);
        // now begin
        numChildren = entitiesToLayout.length;
        if (numChildren < 1)
            return;

        int[] colsAndRows = calculateNumberOfRowsAndCols(numChildren, x, y, width, height);
        cols = colsAndRows[0];
        rows = colsAndRows[1];

        totalProgress = rows + 2;
        fireProgressEvent(1, totalProgress);

        // sort the entities
        if (comparator != null) {
            Arrays.sort(entitiesToLayout, comparator);
        } else {
            Arrays.sort(entitiesToLayout);
        }
        fireProgressEvent(2, totalProgress);

        // Calculate row height and column width
        colWidth = width / cols;
        rowHeight = height / rows;

        // Calculate amount to scale children
        double[] nodeSize = calculateNodeSize(colWidth, rowHeight);
        w = nodeSize[0];
        h = nodeSize[1];
        offsetX = (colWidth - w) / 2.0; // half of the space between columns
        offsetY = (rowHeight - h) / 2.0; // half of the space between rows
    }

    /**
     * Use this algorithm to layout the given entities, using the given relationships and bounds.
     * The entities will be placed in the same order as they are passed in, unless a comparator
     * is supplied.  
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
    protected synchronized void applyLayoutInternal(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider, double boundsX, double boundsY, double boundsWidth, double boundsHeight) {

        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if ((i * cols + j) < numChildren) {
                    // find new position for child
                    double xmove = boundsX + j * colWidth + offsetX;
                    double ymove = boundsY + i * rowHeight + offsetY;
                    InternalNode sn = entitiesToLayout[index++];
                    sn.setInternalLocation(xmove, ymove);
                    sn.setInternalSize(Math.max(w, MIN_ENTITY_SIZE), Math.max(h, MIN_ENTITY_SIZE));
                }
            }
            fireProgressEvent(2 + i, totalProgress);
        }
        updateLayoutLocations(entitiesToLayout);
        fireProgressEvent(totalProgress, totalProgress);
    }

    @Override
    protected void postLayoutAlgorithm(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider) {

    }

    /**
     * Calculates and returns an array containing the number of columns, followed by the number of rows
     */
    protected int[] calculateNumberOfRowsAndCols(int numChildren, double boundX, double boundY, double boundWidth, double boundHeight) {
        if (getEntityAspectRatio() == 1.0) {
            return calculateNumberOfRowsAndCols_square(numChildren, boundX, boundY, boundWidth, boundHeight);
        } else {
            return calculateNumberOfRowsAndCols_rectangular(numChildren);
        }
    }

    protected int[] calculateNumberOfRowsAndCols_square(int numChildren, double boundX, double boundY, double boundWidth, double boundHeight) {
        int rows = Math.max(1, (int) Math.sqrt(numChildren * boundHeight / boundWidth));
        int cols = Math.max(1, (int) Math.sqrt(numChildren * boundWidth / boundHeight));

        // if space is taller than wide, adjust rows first
        if (boundWidth <= boundHeight) {
            //decrease number of rows and columns until just enough or not enough
            while (rows * cols > numChildren) {
                if (rows > 1)
                    rows--;
                if (rows * cols > numChildren)
                    if (cols > 1)
                        cols--;
            }
            //increase number of rows and columns until just enough
            while (rows * cols < numChildren) {
                rows++;
                if (rows * cols < numChildren)
                    cols++;
            }
        } else {
            //decrease number of rows and columns until just enough or not enough
            while (rows * cols > numChildren) {
                if (cols > 1)
                    cols--;
                if (rows * cols > numChildren)
                    if (rows > 1)
                        rows--;
            }
            //increase number of rows and columns until just enough
            while (rows * cols < numChildren) {
                cols++;
                if (rows * cols < numChildren)
                    rows++;
            }
        }
        int[] result = { cols, rows };
        return result;
    }

    protected int[] calculateNumberOfRowsAndCols_rectangular(int numChildren) {
        int rows = Math.max(1, (int) Math.ceil(Math.sqrt(numChildren)));
        int cols = Math.max(1, (int) Math.ceil(Math.sqrt(numChildren)));
        int[] result = { cols, rows };
        return result;
    }

    protected double[] calculateNodeSize(double colWidth, double rowHeight) {
        double childW = Math.max(MIN_ENTITY_SIZE, PADDING_PERCENTAGE * colWidth);
        double childH = Math.max(MIN_ENTITY_SIZE, PADDING_PERCENTAGE * (rowHeight - rowPadding));
        double whRatio = colWidth / rowHeight;
        if (whRatio < getEntityAspectRatio()) {
            childH = childW / getEntityAspectRatio();
        } else {
            childW = childH * getEntityAspectRatio();
        }
        double[] result = { childW, childH };
        return result;
    }

    /**
     * Increases the padding between rows in the grid
     * @param rowPadding Value will not be set if less than 0.
     */
    public void setRowPadding(int rowPadding) {
        if (rowPadding < 0) {
            return;
        }
        this.rowPadding = rowPadding;
    }

    @Override
    protected boolean isValidConfiguration(boolean asynchronous, boolean continueous) {
        if (asynchronous && continueous)
            return false;
        else if (asynchronous && !continueous)
            return true;
        else if (!asynchronous && continueous)
            return false;
        else if (!asynchronous && !continueous)
            return true;

        return false;
    }

}
