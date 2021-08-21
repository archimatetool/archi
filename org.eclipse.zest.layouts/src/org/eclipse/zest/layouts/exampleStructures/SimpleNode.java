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

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.eclipse.zest.layouts.LayoutEntity;
import org.eclipse.zest.layouts.constraints.BasicEntityConstraint;
import org.eclipse.zest.layouts.constraints.EntityPriorityConstraint;
import org.eclipse.zest.layouts.constraints.LabelLayoutConstraint;
import org.eclipse.zest.layouts.constraints.LayoutConstraint;

/**
 * Rerpresents a simple node that can be used in the layout algorithms.
 * 
 * @author Ian Bull
 * @author Casey Best (Version 1 by Rob Lintern)
 * @version 2
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class SimpleNode implements LayoutEntity {
    private static Object NODE_NORMAL_COLOR;
    private static Object NODE_SELECTED_COLOR;
    private static Object NODE_ADJACENT_COLOR;
    private static Object BORDER_NORMAL_COLOR;
    private static Object BORDER_SELECTED_COLOR;
    private static Object BORDER_ADJACENT_COLOR;

    private static final int BORDER_NORMAL_STROKE = 1;
    private static final int BORDER_STROKE_SELECTED = 2;

    /**
     * A list of layout dependent attributes
     */
    private Map attributes;

    protected double x, y, width, height;
    protected Object realObject;
    private boolean ignoreInLayout = false;

    private Object colour = null;
    private Object borderColor = null;
    private int borderWidth;

    private TreeSet listOfRels = null;

    private Object internalNode;

    /**
     * Constructs a new SimpleNode.
     */
    public SimpleNode(Object realObject) {
        this(realObject, -1, -1, 110, 110);
    }

    class UniqueCompare implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            // TODO this may not always be a unique comparison
            return o1.toString().compareTo(o2.toString());
        }
    }

    /**
     * Constructs a new SimpleNode.
     */
    public SimpleNode(Object realObject, double x, double y, double width, double height) {
        this.realObject = realObject;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.attributes = new HashMap();
        this.borderWidth = BORDER_NORMAL_STROKE;
        listOfRels = new TreeSet(new UniqueCompare());
        this.colour = NODE_NORMAL_COLOR;
        this.borderColor = BORDER_NORMAL_COLOR;
    }

    public static void setNodeColors(Object nodeNormalColor, Object borderNormalColor, Object nodeSelectedColor, Object nodeAdjacentColor, Object borderSelectedColor, Object borderAdjacentColor) {
        NODE_NORMAL_COLOR = nodeNormalColor;
        BORDER_NORMAL_COLOR = borderNormalColor;
        NODE_SELECTED_COLOR = nodeSelectedColor;
        NODE_ADJACENT_COLOR = nodeAdjacentColor;
        BORDER_SELECTED_COLOR = borderSelectedColor;
        BORDER_ADJACENT_COLOR = borderAdjacentColor;
    }

    public void addRelationship(SimpleRelationship rel) {
        listOfRels.add(rel);
    }

    public SimpleRelationship[] getRelationships() {
        int size = listOfRels.size();
        return (SimpleRelationship[]) this.listOfRels.toArray(new SimpleRelationship[size]);
    }

    public List getRelatedEntities() {
        int size = listOfRels.size();
        SimpleRelationship[] a_listOfRels = (SimpleRelationship[]) this.listOfRels.toArray(new SimpleRelationship[size]);
        LinkedList listOfRelatedEntities = new LinkedList();
        for (int i = 0; i < a_listOfRels.length; i++) {
            SimpleRelationship rel = a_listOfRels[i];
            if (rel.sourceEntity != this && rel.destinationEntity != this) {
                throw new RuntimeException("Problem, we have a relationship and we are not the source or the dest"); //$NON-NLS-1$
            }
            if (rel.sourceEntity != this) {
                listOfRelatedEntities.add(rel.sourceEntity);
            }
            if (rel.destinationEntity != this) {
                listOfRelatedEntities.add(rel.destinationEntity);
            }

        }
        return listOfRelatedEntities;
    }

    /**
     * Ignores this entity in the layout
     * @param ignore Should this entity be ignored
     */
    public void ignoreInLayout(boolean ignore) {
        this.ignoreInLayout = ignore;
    }

    public Object getRealObject() {
        return realObject;
    }

    public boolean hasPreferredLocation() {
        return this.ignoreInLayout;
    }

    /**
     * Gets the x position of this SimpleNode.
     */
    public double getX() {
        return x;
    }

    /**
     * Gets the y position of this SimpleNode.
     */
    public double getY() {
        return y;
    }

    /**
     * Get the size of this node
     */
    public double getWidth() {
        return width;
    }

    /**
     * Get the size of this node
     */
    public double getHeight() {
        return height;
    }

    @Override
    public void setSizeInLayout(double width, double height) {
        if (!ignoreInLayout) {
            this.width = width;
            this.height = height;
        }
    }

    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void setLocationInLayout(double x, double y) {
        if (!ignoreInLayout) {
            this.x = x;
            this.y = y;
        }
    }

    /**
     * An algorithm may require a place to store information.  Use this structure for that purpose.
     */
    public void setAttributeInLayout(Object attribute, Object value) {
        attributes.put(attribute, value);
    }

    /**
     * An algorithm may require a place to store information.  Use this structure for that purpose.
     */
    public Object getAttributeInLayout(Object attribute) {
        return attributes.get(attribute);
    }

    @Override
    public boolean equals(Object object) {
        boolean result = false;
        if (object instanceof SimpleNode) {
            SimpleNode node = (SimpleNode) object;
            result = realObject.equals(node.getRealObject());
        }
        return result;
    }

    @Override
    public int hashCode() {
        return realObject.hashCode();
    }

    // all objects are equal
    @Override
    public int compareTo(Object arg0) {
        return 0;
    }

    @Override
    public String toString() {
        return realObject.toString();
    }

    public void setSelected() {
        this.colour = NODE_SELECTED_COLOR;
        this.borderColor = BORDER_SELECTED_COLOR;
        this.borderWidth = BORDER_STROKE_SELECTED;
    }

    public void setUnSelected() {
        this.colour = NODE_NORMAL_COLOR;
        this.borderColor = BORDER_NORMAL_COLOR;
        this.borderWidth = BORDER_NORMAL_STROKE;
    }

    public void setAdjacent() {
        this.colour = NODE_ADJACENT_COLOR;
        this.borderColor = BORDER_ADJACENT_COLOR;
        this.borderWidth = BORDER_STROKE_SELECTED;
    }

    public Object getColor() {
        return this.colour;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public Object getBorderColor() {
        return borderColor;
    }

    /* (non-Javadoc)
     * @see ca.uvic.cs.chisel.layouts.LayoutEntity#getInternalEntity()
     */
    @Override
    public Object getLayoutInformation() {
        return internalNode;
    }

    /* (non-Javadoc)
     * @see ca.uvic.cs.chisel.layouts.LayoutEntity#setInternalEntity(java.lang.Object)
     */
    @Override
    public void setLayoutInformation(Object internalEntity) {
        this.internalNode = internalEntity;
    }

    /**
     * Populate the specified layout constraint
     */
    @Override
    public void populateLayoutConstraint(LayoutConstraint constraint) {
        if (constraint instanceof LabelLayoutConstraint) {
            LabelLayoutConstraint labelConstraint = (LabelLayoutConstraint) constraint;
            labelConstraint.label = realObject.toString();
            labelConstraint.pointSize = 18;
        } else if (constraint instanceof BasicEntityConstraint) {
            // noop
        } else if (constraint instanceof EntityPriorityConstraint) {
            EntityPriorityConstraint priorityConstraint = (EntityPriorityConstraint) constraint;
            priorityConstraint.priority = Math.random() * 10 + 1;
        }
    }

    @Override
    public double getHeightInLayout() {
        return this.height;
    }

    @Override
    public double getWidthInLayout() {
        return this.width;
    }

    @Override
    public double getXInLayout() {
        return this.x;
    }

    @Override
    public double getYInLayout() {
        return this.y;
    }

    @Override
    public Object getGraphData() {
        return null;
    }

    @Override
    public void setGraphData(Object o) {

    }

}
