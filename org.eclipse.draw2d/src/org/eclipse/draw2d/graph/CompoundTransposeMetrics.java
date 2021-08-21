/*******************************************************************************
 * Copyright (c) 2005, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.draw2d.graph;

import org.eclipse.draw2d.PositionConstants;

/**
 * Performs transposing of subgraphics in a compound directed graph.
 * 
 * @since 3.7.1
 */
class CompoundTransposeMetrics extends TransposeMetrics {

    @Override
    public void visit(DirectedGraph g) {
        if (g.getDirection() == PositionConstants.SOUTH)
            return;
        super.visit(g);
        int temp;
        CompoundDirectedGraph cg = (CompoundDirectedGraph) g;
        for (int i = 0; i < cg.subgraphs.size(); i++) {
            Node node = cg.subgraphs.getNode(i);
            temp = node.width;
            node.width = node.height;
            node.height = temp;
            if (node.getPadding() != null)
                node.setPadding(t.t(node.getPadding()));
        }
    }

    @Override
    public void revisit(DirectedGraph g) {
        if (g.getDirection() == PositionConstants.SOUTH)
            return;
        super.revisit(g);
        int temp;
        CompoundDirectedGraph cg = (CompoundDirectedGraph) g;
        for (int i = 0; i < cg.subgraphs.size(); i++) {
            Node node = (Node) cg.subgraphs.get(i);
            temp = node.width;
            node.width = node.height;
            node.height = temp;
            temp = node.y;
            node.y = node.x;
            node.x = temp;
            if (node.getPadding() != null)
                node.setPadding(t.t(node.getPadding()));
        }
    }

}