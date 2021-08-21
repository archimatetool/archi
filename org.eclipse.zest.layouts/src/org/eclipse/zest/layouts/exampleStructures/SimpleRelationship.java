/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.zest.layouts.exampleStructures;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.zest.layouts.LayoutBendPoint;
import org.eclipse.zest.layouts.LayoutEntity;
import org.eclipse.zest.layouts.LayoutRelationship;
import org.eclipse.zest.layouts.constraints.BasicEdgeConstraints;
import org.eclipse.zest.layouts.constraints.LabelLayoutConstraint;
import org.eclipse.zest.layouts.constraints.LayoutConstraint;
import org.eclipse.zest.layouts.dataStructures.BendPoint;

/**
 * The SimpleRelation class describes the relationship between
 * two objects: source and destination.  Each relationship
 * has a weight and direction associated with it.
 * Note: The source object is at the beginning of the relationship.
 * Note: The destination object is at the end of the relationship.
 *
 * @version  2.0
 * @author   Casey Best (version 1.0 by Jingwei Wu)
 * @author Chris Bennett
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class SimpleRelationship implements LayoutRelationship {

    private static int DEFAULT_REL_LINE_WIDTH = 1;
    private static int DEFAULT_REL_LINE_WIDTH_SELECTED = DEFAULT_REL_LINE_WIDTH + 2;
    private static Object DEFAULT_RELATIONSHIP_COLOR;
    private static Object DEFAULT_RELATIONSHIP_HIGHLIGHT_COLOR;

    /** The line width for this relationship. */
    private int lineWidth = DEFAULT_REL_LINE_WIDTH;

    /** The color for this relationship. */
    private Object color = DEFAULT_RELATIONSHIP_COLOR;

    /**
     * A list of layout dependent attributes
     */
    private Map attributes;

    /**
     * The sourceEntity of this SimpleRelation.
     */
    protected LayoutEntity sourceEntity;

    /**
     * The object of this SimpleRelation.
     */
    protected LayoutEntity destinationEntity;

    /**
     * If directional, algorithms must note the direction of the relationship.
     * If not directional, algorithms are to ignore which direction the relationship is going.
     * Switching the source and destination should make no difference. 
     */
    protected boolean bidirectional;

    /**
     * The weight given to this relation.
     */
    private double weight;

    private Object internalRelationship;

    private LayoutBendPoint[] bendPoints;

    private String label;

    /**
     * Constructor.
     * @param sourceEntity The sourceEntity of this SimpleRelation.
     * @param destinationEntity The object of this SimpleRelation.
     * @param bidirectional Determines if the <code>sourceEntity</code> and
     * <code>destinationEntity</code> are equal(exchangeable).
     * @throws java.lang.NullPointerException If either <code>sourceEntity
     * </code> or <code>destinationEntity</code> is <code>null</code>.
     */
    public SimpleRelationship(LayoutEntity sourceEntity, LayoutEntity destinationEntity, boolean bidirectional) {
        this(sourceEntity, destinationEntity, bidirectional, 1);
    }

    /**
     * Constructor.
     * @param sourceEntity The sourceEntity of this SimpleRelation.
     * @param destinationEntity The destinationEntity of this SimpleRelation.
     * @param exchangeable Determines if the <code>sourceEntity</code> and
     * <code>destinationEntity</code> are equal(exchangeable).
     * @throws java.lang.NullPointerException If either <code>sourceEntity
     * </code> or <code>destinationEntity</code> is <code>null</code>.
     */
    public SimpleRelationship(LayoutEntity sourceEntity, LayoutEntity destinationEntity, boolean bidirectional, double weight) {
        this.destinationEntity = destinationEntity;
        this.sourceEntity = sourceEntity;
        this.bidirectional = bidirectional;
        this.weight = weight;
        this.attributes = new HashMap();
        this.lineWidth = DEFAULT_REL_LINE_WIDTH;
        this.color = DEFAULT_RELATIONSHIP_COLOR;
    }

    /**
     * Gets the sourceEntity of this SimpleRelation whether the relation is
     * exchangeable or not.
     * @return The sourceEntity.
     */
    @Override
    public LayoutEntity getSourceInLayout() {
        return sourceEntity;
    }

    /**
     * Gets the destinationEntity of this SimpleRelation whether the relation is
     * exchangeable or not.
     * @return The destinationEntity of this SimpleRelation.
     */
    @Override
    public LayoutEntity getDestinationInLayout() {
        return destinationEntity;
    }

    /**
     * If bidirectional, the direction of the relationship doesn't matter.  Switching the source and destination should make no difference.
     * If not bidirectional, layout algorithms need to take into account the direction of the relationship.  The direction is based on the
     * source and destination entities.
     */
    public boolean isBidirectionalInLayout() {
        return bidirectional;
    }

    public void setWeightInLayout(double weight) {
        this.weight = weight;
    }

    public double getWeightInLayout() {
        return weight;
    }

    /**
     * An algorithm may require a place to store information.  Use this structure for that purpose.
     */
    public void setAttributeInLayout(String attribute, Object value) {
        attributes.put(attribute, value);
    }

    /**
     * An algorithm may require a place to store information.  Use this structure for that purpose.
     */
    public Object getAttributeInLayout(String attribute) {
        return attributes.get(attribute);
    }

    @Override
    public String toString() {
        String arrow = (isBidirectionalInLayout() ? " <-> " : " -> "); //$NON-NLS-1$ //$NON-NLS-2$
        return "(" + sourceEntity + arrow + destinationEntity + ")"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    public int getLineWidth() {
        return this.lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public void resetLineWidth() {
        this.lineWidth = DEFAULT_REL_LINE_WIDTH;
    }

    public static void setDefaultSize(int i) {
        DEFAULT_REL_LINE_WIDTH = i;
        DEFAULT_REL_LINE_WIDTH_SELECTED = DEFAULT_REL_LINE_WIDTH + 2;
    }

    public void setSelected() {
        this.color = DEFAULT_RELATIONSHIP_HIGHLIGHT_COLOR;
        this.lineWidth = DEFAULT_REL_LINE_WIDTH_SELECTED;
    }

    public void setUnSelected() {
        this.color = DEFAULT_RELATIONSHIP_COLOR;
        this.lineWidth = DEFAULT_REL_LINE_WIDTH;
    }

    public Object getColor() {
        return color;
    }

    public void setColor(Object c) {
        this.color = c;
    }

    public static void setDefaultColor(Object c) {
        DEFAULT_RELATIONSHIP_COLOR = c;
    }

    public static void setDefaultHighlightColor(Object c) {
        DEFAULT_RELATIONSHIP_HIGHLIGHT_COLOR = c;
    }

    /* (non-Javadoc)
     * @see ca.uvic.cs.chisel.layouts.LayoutRelationship#getInternalRelationship()
     */
    @Override
    public Object getLayoutInformation() {
        return internalRelationship;
    }

    /* (non-Javadoc)
     * @see ca.uvic.cs.chisel.layouts.LayoutRelationship#setInternalRelationship(java.lang.Object)
     */
    @Override
    public void setLayoutInformation(Object layoutInformation) {
        this.internalRelationship = layoutInformation;
    }

    @Override
    public void setBendPoints(LayoutBendPoint[] bendPoints) {
        this.bendPoints = bendPoints;
    }

    public LayoutBendPoint[] getBendPoints() {
        return this.bendPoints;
    }

    @Override
    public void clearBendPoints() {
        this.bendPoints = new BendPoint[0];
    }

    public void setDestinationInLayout(LayoutEntity destination) {
        this.destinationEntity = destination;
    }

    /**
     * Set the label for this edge (available in the label layout constraint). 
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Populate the specified layout constraint
     */
    @Override
    public void populateLayoutConstraint(LayoutConstraint constraint) {
        if (constraint instanceof LabelLayoutConstraint) {
            LabelLayoutConstraint labelConstraint = (LabelLayoutConstraint) constraint;
            labelConstraint.label = this.label;
            labelConstraint.pointSize = 18;
        } else if (constraint instanceof BasicEdgeConstraints) {
            // noop

        }
    }

    @Override
    public Object getGraphData() {
        return null;
    }

    @Override
    public void setGraphData(Object o) {

    }

}
