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

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.DirectedGraphLayout;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.swt.SWT;
import org.eclipse.zest.layouts.dataStructures.InternalNode;
import org.eclipse.zest.layouts.dataStructures.InternalRelationship;

@SuppressWarnings({"rawtypes", "unchecked"})
public class DirectedGraphLayoutAlgorithm extends AbstractLayoutAlgorithm {

    class ExtendedDirectedGraphLayout extends DirectedGraphLayout {

        @Override
        public void visit(DirectedGraph graph) {
            Field field;
            try {
                field = DirectedGraphLayout.class.getDeclaredField("steps"); //$NON-NLS-1$
                field.setAccessible(true);
                Object object = field.get(this);
                List steps = (List) object;
                steps.remove(10);
                steps.remove(9);
                steps.remove(8);
                steps.remove(2);
                field.setAccessible(false);
                super.visit(graph);
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }

    public DirectedGraphLayoutAlgorithm(int styles) {
        super(styles);
    }

    @Override
    protected void applyLayoutInternal(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider, double boundsX, double boundsY, double boundsWidth, double boundsHeight) {
        HashMap mapping = new HashMap(entitiesToLayout.length);
        DirectedGraph graph = new DirectedGraph();
        for (int i = 0; i < entitiesToLayout.length; i++) {
            InternalNode internalNode = entitiesToLayout[i];
            Node node = new Node(internalNode);
            node.setSize(new Dimension(10, 10));
            mapping.put(internalNode, node);
            graph.nodes.add(node);
        }
        for (int i = 0; i < relationshipsToConsider.length; i++) {
            InternalRelationship relationship = relationshipsToConsider[i];
            Node source = (Node) mapping.get(relationship.getSource());
            Node dest = (Node) mapping.get(relationship.getDestination());
            Edge edge = new Edge(relationship, source, dest);
            graph.edges.add(edge);
        }
        DirectedGraphLayout directedGraphLayout = new ExtendedDirectedGraphLayout();
        directedGraphLayout.visit(graph);

        for (Iterator iterator = graph.nodes.iterator(); iterator.hasNext();) {
            Node node = (Node) iterator.next();
            InternalNode internalNode = (InternalNode) node.data;
            // For horizontal layout transpose the x and y coordinates
            if ((layout_styles & SWT.HORIZONTAL) == SWT.HORIZONTAL) {
                internalNode.setInternalLocation(node.y, node.x);
            } else {
                internalNode.setInternalLocation(node.x, node.y);
            }
        }
        updateLayoutLocations(entitiesToLayout);
    }

    @Override
    protected int getCurrentLayoutStep() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    protected int getTotalNumberOfLayoutSteps() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    protected boolean isValidConfiguration(boolean asynchronous, boolean continuous) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    protected void postLayoutAlgorithm(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void preLayoutAlgorithm(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider, double x, double y, double width, double height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setLayoutArea(double x, double y, double width, double height) {
        // TODO Auto-generated method stub

    }

}
