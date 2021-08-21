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

/**
 * Performs some action on a Graph.
 * 
 * @author Randy Hudson
 * @since 2.1.2
 */
abstract class GraphVisitor {

    /**
     * Act on the given directed graph.
     * 
     * @param g
     *            the graph
     */
    void visit(DirectedGraph g) {
    }

    /**
     * Called in reverse order of visit.
     * 
     * @since 3.1
     * @param g
     *            the graph to act upon
     */
    void revisit(DirectedGraph g) {
    }

}
