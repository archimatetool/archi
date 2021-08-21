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
 * Inverts any edges which are marked as backwards or "feedback" edges.
 * 
 * @author Daniel Lee
 * @since 2.1.2
 */
class InvertEdges extends GraphVisitor {

    /**
     * 
     * @see GraphVisitor#visit(org.eclipse.draw2d.graph.DirectedGraph)
     */
    @SuppressWarnings("deprecation")
    @Override
    public void visit(DirectedGraph g) {
        for (int i = 0; i < g.edges.size(); i++) {
            Edge e = g.edges.getEdge(i);
            if (e.isFeedback)
                e.invert();
        }
    }

}
