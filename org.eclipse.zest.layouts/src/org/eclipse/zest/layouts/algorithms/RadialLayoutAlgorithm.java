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

import java.util.Iterator;
import java.util.List;

import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.dataStructures.DisplayIndependentPoint;
import org.eclipse.zest.layouts.dataStructures.DisplayIndependentRectangle;
import org.eclipse.zest.layouts.dataStructures.InternalNode;
import org.eclipse.zest.layouts.dataStructures.InternalRelationship;

/**
 * This layout will take the given entities, apply a tree layout to them, and then display the 
 * tree in a circular fashion with the roots in the center.
 * 
 * @author Casey Best
 * @auhtor Rob Lintern
 */
@SuppressWarnings("rawtypes")
public class RadialLayoutAlgorithm extends TreeLayoutAlgorithm {
    private static final double MAX_DEGREES = Math.PI * 2;
    private double startDegree;
    private double endDegree;
    private TreeLayoutAlgorithm treeLayout;
    private List roots;

    /**
     * Creates a radial layout with no style.
     */
    public RadialLayoutAlgorithm() {
        this(LayoutStyles.NONE);
    }

    //TODO: This is a really strange pattern.  It extends tree layout and it contains a tree layout ? 
    public RadialLayoutAlgorithm(int styles) {
        super(styles);
        treeLayout = new TreeLayoutAlgorithm(styles);
        startDegree = 0;
        endDegree = MAX_DEGREES;
    }

    @Override
    public void setLayoutArea(double x, double y, double width, double height) {
        throw new RuntimeException("Operation not implemented"); //$NON-NLS-1$
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

    DisplayIndependentRectangle layoutBounds = null;

    @Override
    protected void preLayoutAlgorithm(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider, double x, double y, double width, double height) {
        // TODO Auto-generated method stub
        layoutBounds = new DisplayIndependentRectangle(x, y, width, height);
        super.preLayoutAlgorithm(entitiesToLayout, relationshipsToConsider, x, y, width, height);
    }

    @Override
    protected void postLayoutAlgorithm(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider) {
        roots = treeLayout.getRoots();
        computeRadialPositions(entitiesToLayout, layoutBounds);

        defaultFitWithinBounds(entitiesToLayout, layoutBounds);

        super.postLayoutAlgorithm(entitiesToLayout, relationshipsToConsider);

    }

    /**
     * Set the range the radial layout will use when applyLayout is called.
     * Both values must be in radians.
     */
    public void setRangeToLayout(double startDegree, double endDegree) {
        this.startDegree = startDegree;
        this.endDegree = endDegree;
    }

    /**
     * Take the tree and make it round.  This is done by determining the location of each entity in terms
     * of its percentage in the tree layout.  Then apply that percentage to the radius and distance from
     * the center.
     */
    protected void computeRadialPositions(InternalNode[] entities, DisplayIndependentRectangle bounds2) { //TODO TODO TODO
        DisplayIndependentRectangle bounds = new DisplayIndependentRectangle(getLayoutBounds(entities, true));
        bounds.height = bounds2.height;
        bounds.y = bounds2.y;
        for (int i = 0; i < entities.length; i++) {
            InternalNode entity = entities[i];
            double percentTheta = (entity.getInternalX() - bounds.x) / bounds.width;
            double distance = (entity.getInternalY() - bounds.y) / bounds.height;
            double theta = startDegree + Math.abs(endDegree - startDegree) * percentTheta;
            double newX = distance * Math.cos(theta);
            double newY = distance * Math.sin(theta);

            entity.setInternalLocation(newX, newY);
        }
    }

    /**
     * Find the bounds in which the nodes are located.  Using the bounds against the real bounds
     * of the screen, the nodes can proportionally be placed within the real bounds.
     * The bounds can be determined either including the size of the nodes or not.  If the size
     * is not included, the bounds will only be guaranteed to include the center of each node.
     */
    @Override
    protected DisplayIndependentRectangle getLayoutBounds(InternalNode[] entitiesToLayout, boolean includeNodeSize) {
        DisplayIndependentRectangle layoutBounds = super.getLayoutBounds(entitiesToLayout, includeNodeSize);
        DisplayIndependentPoint centerPoint = (roots != null) ? determineCenterPoint(roots) : new DisplayIndependentPoint(layoutBounds.x + layoutBounds.width / 2, layoutBounds.y + layoutBounds.height / 2);
        //    The center entity is determined in applyLayout
        double maxDistanceX = Math.max(Math.abs(layoutBounds.x + layoutBounds.width - centerPoint.x), Math.abs(centerPoint.x - layoutBounds.x));
        double maxDistanceY = Math.max(Math.abs(layoutBounds.y + layoutBounds.height - centerPoint.y), Math.abs(centerPoint.y - layoutBounds.y));
        layoutBounds = new DisplayIndependentRectangle(centerPoint.x - maxDistanceX, centerPoint.y - maxDistanceY, maxDistanceX * 2, maxDistanceY * 2);
        return layoutBounds;
    }

    /**
     * Find the center point between the roots
     */
    private DisplayIndependentPoint determineCenterPoint(List roots) {
        double totalX = 0, totalY = 0;
        for (Iterator iterator = roots.iterator(); iterator.hasNext();) {
            InternalNode entity = (InternalNode) iterator.next();
            totalX += entity.getInternalX();
            totalY += entity.getInternalY();
        }
        return new DisplayIndependentPoint(totalX / roots.size(), totalY / roots.size());
    }
}
