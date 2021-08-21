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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.zest.layouts.LayoutEntity;
import org.eclipse.zest.layouts.LayoutGraph;
import org.eclipse.zest.layouts.LayoutRelationship;

/**
 * Create a very simple graph that can be used in the layout algorithms
 * 
 * @author Casey Best
 * @author Chris Callendar
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class SimpleGraph implements LayoutGraph {

    Map objectsToNodes;
    List relationships;

    public SimpleGraph() {
        objectsToNodes = new HashMap();
        relationships = new ArrayList();
    }

    /**
     * Adds the node.
     * @param node    The node to add.
     */
    @Override
    public void addEntity(LayoutEntity node) {
        if (node instanceof SimpleNode) {
            objectsToNodes.put(((SimpleNode) node).getRealObject(), node);
        }
    }

    /**
     * Creates a LayoutEntity containing an object.
     */
    public LayoutEntity addObjectNode(Object object) {
        SimpleNode simpleNode = (SimpleNode) objectsToNodes.get(object);
        if (simpleNode == null) {
            simpleNode = new SimpleNode(object);
            objectsToNodes.put(object, simpleNode);
        }
        return simpleNode;

    }

    /**
     * Add a relationship between two objects.  Layout algorithms need to know
     * whether a relationship is one way or bi-directional.  This method assumes that 
     * all relationships are bi-direcional and have the same weight. 
     */
    public void addObjectRelationship(Object sourceNode, Object destinationNode) {
        addObjectRelationship(sourceNode, destinationNode, true, 1);
    }

    /**
     * Add a relationship between two objects.  Layout algorithms need to know
     * whether a relationship is one way or bi-directional.  
     */
    public void addObjectRelationship(Object sourceObject, Object destinationObject, boolean bidirectional, int weight) {
        addObjectNode(sourceObject);
        addObjectNode(destinationObject);
        SimpleNode sourceNode = (SimpleNode) objectsToNodes.get(sourceObject);
        SimpleNode destinationNode = (SimpleNode) objectsToNodes.get(destinationObject);
        SimpleRelationship simpleRelationship = new SimpleRelationship(sourceNode, destinationNode, bidirectional, weight);
        relationships.add(simpleRelationship);
    }

    /* (non-Javadoc)
     * @see ca.uvic.cs.chisel.layouts.LayoutGraph#addRelationship(ca.uvic.cs.chisel.layouts.LayoutEntity, ca.uvic.cs.chisel.layouts.LayoutEntity)
     */
    public void addRelationship(LayoutEntity srcNode, LayoutEntity destNode) {
        addRelationship(srcNode, destNode, true, 1);
    }

    /* (non-Javadoc)
     * @see ca.uvic.cs.chisel.layouts.LayoutGraph#addRelationship(ca.uvic.cs.chisel.layouts.LayoutEntity, ca.uvic.cs.chisel.layouts.LayoutEntity, boolean, int)
     */
    public void addRelationship(LayoutEntity srcNode, LayoutEntity destNode, boolean bidirectional, int weight) {
        addEntity(srcNode);
        addEntity(destNode);
        SimpleRelationship rel = new SimpleRelationship(srcNode, destNode, bidirectional, weight);
        relationships.add(rel);
    }

    /* (non-Javadoc)
     * @see ca.uvic.cs.chisel.layouts.LayoutGraph#addRelationship(ca.uvic.cs.chisel.layouts.LayoutRelationship)
     */
    @Override
    public void addRelationship(LayoutRelationship relationship) {
        relationships.add(relationship);
    }

    /**
     * Returns a list of SimpleNodes that represent the objects added to this graph using addNode.  Note that
     * any manipulation to this graph was done on the SimpleNodes, not the real objects.  You
     * must still manipulate them yourself.
     */
    @Override
    public List getEntities() {
        return new ArrayList(objectsToNodes.values());
    }

    /**
     * Returns a list of SimpleRelationships that represent the objects added to this graph using addRelationship.
     */
    @Override
    public List getRelationships() {
        return relationships;
    }

    /**
     * Checks the relationships to see if they are all bidirectional. 
     * @return boolean if all edges are bidirectional.
     */
    @Override
    public boolean isBidirectional() {
        boolean isBidirectional = true;
        for (Iterator iter = relationships.iterator(); iter.hasNext();) {
            SimpleRelationship rel = (SimpleRelationship) iter.next();
            if (!rel.isBidirectionalInLayout()) {
                isBidirectional = false;
                break;
            }
        }
        return isBidirectional;
    }
}
