/*******************************************************************************
 * Copyright (c) 2003, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.graph;

import java.util.Iterator;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;

/**
 * A node in a DirectedGraph. A node has 0 or more incoming and outgoing
 * {@link Edge}s. A node is given a width and height by the client. When a
 * layout places the node in the graph, it will determine the node's x and y
 * location. It may also modify the node's height.
 * 
 * A node represents both the <EM>input</EM> and the <EM>output</EM> for a
 * layout algorithm. The following fields are used as input to a graph layout:
 * <UL>
 * <LI>{@link #width} - the node's width.
 * <LI>{@link #height} - the node's height.
 * <LI>{@link #outgoing} - the node's outgoing edges.
 * <LI>{@link #incoming} - the node's incoming edges.
 * <LI>padding - the amount of space to be left around the outside of the node.
 * <LI>{@link #incomingOffset} - the default attachment point for incoming
 * edges.
 * <LI>{@link #outgoingOffset} - the default attachment point for outgoing
 * edges.
 * <LI>parent - the parent subgraph containing this node.
 * </UL>
 * <P>
 * The following fields are calculated by a graph layout and comprise the
 * <EM>output</EM>:
 * <UL>
 * <LI>{@link #x} - the node's x location
 * <LI>{@link #y} - the node's y location
 * <LI>{@link #height} - the node's height may be stretched to match the height
 * of other nodes
 * </UL>
 * 
 * @author Randy Hudson
 * @since 2.1.2
 */
@SuppressWarnings("rawtypes")
public class Node {

    Node left, right;

    Object workingData[] = new Object[3];
    int workingInts[] = new int[4];

    /**
     * Clients may use this field to mark the Node with an arbitrary data
     * object.
     */
    public Object data;

    // used by various graph visitors
    boolean flag;

    /**
     * The height of this node. This value should be set prior to laying out the
     * directed graph. Depending on the layout rules, a node's height may be
     * expanded to match the height of other nodes around it.
     */
    public int height = 40;

    /**
     * @deprecated use {@link #setRowConstraint(int)} and
     *             {@link #getRowConstraint()}
     */
    public int rowOrder = -1;

    /**
     * The edges for which this node is the target.
     */
    public EdgeList incoming = new EdgeList();

    /**
     * The default attachment point for incoming edges. <code>-1</code>
     * indicates that the node's horizontal center should be used.
     */
    public int incomingOffset = -1;

    // A non-decreasing number given to consecutive nodes in a Rank.
    int index;

    // Used in Compound graphs to quickly determine whether a node is inside a
    // subgraph.
    int nestingIndex = -1;

    /**
     * The edges for which this node is the source.
     */
    public EdgeList outgoing = new EdgeList();

    Insets padding;
    private Subgraph parent;
    int rank;

    /**
     * @deprecated for internal use only
     */
    public double sortValue;

    /**
     * The node's outgoing offset attachment point.
     */
    public int outgoingOffset = -1;

    /**
     * The node's width. The default value is 50.
     */
    public int width = 50;

    /**
     * The node's x coordinate.
     */
    public int x;
    /**
     * The node's y coordinate.
     */
    public int y;

    /**
     * Constructs a new node.
     */
    public Node() {
    }

    /**
     * Constructs a node with the given data object
     * 
     * @param data
     *            an arbitrary data object
     */
    public Node(Object data) {
        this(data, null);
    }

    /**
     * Constructs a node inside the given subgraph.
     * 
     * @param parent
     *            the parent subgraph
     */
    public Node(Subgraph parent) {
        this(null, parent);
    }

    /**
     * Constructs a node with the given data object and parent subgraph. This
     * node is added to the set of members for the parent subgraph
     * 
     * @param data
     *            an arbitrary data object
     * @param parent
     *            the parent subgraph or <code>null</code>
     */
    public Node(Object data, Subgraph parent) {
        this.data = data;
        this.parent = parent;
        if (parent != null)
            parent.addMember(this);
    }

    /**
     * Returns the incoming attachment point. This is the distance from the left
     * edge to the default incoming attachment point for edges. Each incoming
     * edge may have it's own attachment setting which takes priority over this
     * default one.
     * 
     * @return the incoming offset
     */
    public int getOffsetIncoming() {
        if (incomingOffset == -1)
            return width / 2;
        return incomingOffset;
    }

    /**
     * Returns the outgoing attachment point. This is the distance from the left
     * edge to the default outgoing attachment point for edges. Each outgoing
     * edge may have it's own attachment setting which takes priority over this
     * default one.
     * 
     * @return the outgoing offset
     */
    public int getOffsetOutgoing() {
        if (outgoingOffset == -1)
            return width / 2;
        return outgoingOffset;
    }

    /**
     * Returns the padding for this node or <code>null</code> if the default
     * padding for the graph should be used.
     * 
     * @return the padding or <code>null</code>
     */
    public Insets getPadding() {
        return padding;
    }

    /**
     * Returns the parent Subgraph or <code>null</code> if there is no parent.
     * Subgraphs are only for use in {@link CompoundDirectedGraphLayout}.
     * 
     * @return the parent or <code>null</code>
     */
    public Subgraph getParent() {
        return parent;
    }

    /**
     * For internal use only. Returns <code>true</code> if the given node is
     * equal to this node. This method is implemented for consitency with
     * Subgraph.
     * 
     * @param node
     *            the node in question
     * @return <code>true</code> if nested
     */
    boolean isNested(Node node) {
        return node == this;
    }

    /**
     * Sets the padding. <code>null</code> indicates that the default padding
     * should be used.
     * 
     * @param padding
     *            an insets or <code>null</code>
     */
    public void setPadding(Insets padding) {
        this.padding = padding;
    }

    /**
     * Sets the parent subgraph. This method should not be called directly. The
     * constructor will set the parent accordingly.
     * 
     * @param parent
     *            the parent
     */
    public void setParent(Subgraph parent) {
        this.parent = parent;
    }

    /**
     * Sets the row sorting constraint for this node. By default, a node's
     * constraint is <code>-1</code>. If two nodes have different values both >=
     * 0, the node with the smaller constraint will be placed to the left of the
     * other node. In all other cases no relative placement is guaranteed.
     * 
     * @param value
     *            the row constraint
     * @since 3.2
     */
    public void setRowConstraint(int value) {
        this.rowOrder = value;
    }

    /**
     * Returns the row constraint for this node.
     * 
     * @return the row constraint
     * @since 3.2
     */
    public int getRowConstraint() {
        return rowOrder;
    }

    /**
     * Sets the size of this node to the given dimension.
     * 
     * @param size
     *            the new size
     * @since 3.2
     */
    public void setSize(Dimension size) {
        width = size.width;
        height = size.height;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return "N(" + data + ")"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    Iterator iteratorNeighbors() {
        return new Iterator() {
            int offset;
            EdgeList list = outgoing;

            @Override
            public Object next() {
                Edge edge = list.getEdge(offset++);
                if (offset < list.size())
                    return edge.opposite(Node.this);
                if (list == outgoing) {
                    list = incoming;
                    offset = 0;
                } else
                    list = null;
                return edge.opposite(Node.this);
            }

            @Override
            public boolean hasNext() {
                if (list == null)
                    return false;
                if (offset < list.size())
                    return true;
                if (list == outgoing) {
                    list = incoming;
                    offset = 0;
                }
                return offset < list.size();
            }

            @Override
            public void remove() {
                throw new RuntimeException("Remove not supported"); //$NON-NLS-1$
            }
        };
    }

    /**
     * Returns a reference to a node located left from this one
     * 
     * @return <code>Node</code> on the left from this one
     * @since 3.4
     */
    public Node getLeft() {
        return left;
    }

    /**
     * Returns a reference to a node located right from this one
     * 
     * @return <code>Node</code> on the right from this one
     * @since 3.4
     */
    public Node getRight() {
        return right;
    }

}
