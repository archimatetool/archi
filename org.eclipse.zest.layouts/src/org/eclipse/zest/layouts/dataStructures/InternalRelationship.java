/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.zest.layouts.dataStructures;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.zest.layouts.LayoutBendPoint;
import org.eclipse.zest.layouts.LayoutEntity;
import org.eclipse.zest.layouts.LayoutRelationship;
import org.eclipse.zest.layouts.constraints.BasicEdgeConstraints;
import org.eclipse.zest.layouts.constraints.LayoutConstraint;

/**
 * @author Ian Bull
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class InternalRelationship implements LayoutRelationship {

    private LayoutRelationship externalRelationship;
    private InternalNode source;
    private InternalNode destination;
    private Object layoutInfo;
    private List bendPoints = new LinkedList();
    BasicEdgeConstraints basicEdgeConstraints = new BasicEdgeConstraints();

    public InternalRelationship(LayoutRelationship externalRelationship, InternalNode source, InternalNode destination) {
        this.externalRelationship = externalRelationship;
        this.externalRelationship.setLayoutInformation(this);
        this.source = source;
        this.destination = destination;
        this.externalRelationship.populateLayoutConstraint(basicEdgeConstraints);
    }

    public LayoutRelationship getLayoutRelationship() {
        return externalRelationship;
    }

    public InternalNode getSource() {
        if (this.source == null) {
            throw new RuntimeException("Source is null"); //$NON-NLS-1$
        }
        return this.source;
    }

    public InternalNode getDestination() {
        if (this.destination == null) {
            throw new RuntimeException("Dest is null"); //$NON-NLS-1$
        }
        return this.destination;
    }

    public double getWeight() {
        return this.basicEdgeConstraints.weight;
    }

    public boolean isBidirectional() {
        return this.basicEdgeConstraints.isBiDirectional;
    }

    /**
     * Ensure this is called in order of source to target node position.
     * @param x
     * @param y
     */
    public void addBendPoint(double x, double y) {
        bendPoints.add(new BendPoint(x, y));
    }

    /**
     * Ensure this is called in order of source to target node position.
     * Specifies if the bendpoint is a curve control point
     * @param x
     * @param y
     * @param isControlPoint
     */
    public void addBendPoint(double x, double y, boolean isControlPoint) {
        bendPoints.add(new BendPoint(x, y, isControlPoint));
    }

    public List getBendPoints() {
        return this.bendPoints;
    }

    @Override
    public void clearBendPoints() {
        // TODO Auto-generated method stub

    }

    @Override
    public LayoutEntity getDestinationInLayout() {
        // TODO Auto-generated method stub
        return destination;
    }

    @Override
    public Object getLayoutInformation() {
        // TODO Auto-generated method stub
        return layoutInfo;
    }

    @Override
    public LayoutEntity getSourceInLayout() {
        // TODO Auto-generated method stub
        return source;
    }

    @Override
    public void populateLayoutConstraint(LayoutConstraint constraint) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setBendPoints(LayoutBendPoint[] bendPoints) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setLayoutInformation(Object layoutInformation) {
        this.layoutInfo = layoutInformation;

    }

    @Override
    public Object getGraphData() {
        return null;
    }

    @Override
    public void setGraphData(Object o) {

    }

}
