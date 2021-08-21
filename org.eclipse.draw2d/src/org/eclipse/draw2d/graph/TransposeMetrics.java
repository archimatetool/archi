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

import java.util.List;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Transposer;

@SuppressWarnings({"rawtypes", "deprecation"})
class TransposeMetrics extends GraphVisitor {

    Transposer t = new Transposer();

    @Override
    public void visit(DirectedGraph g) {
        if (g.getDirection() == PositionConstants.SOUTH)
            return;
        t.setEnabled(true);
        int temp;
        g.setDefaultPadding(t.t(g.getDefaultPadding()));
        for (int i = 0; i < g.nodes.size(); i++) {
            Node node = g.nodes.getNode(i);
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
        int temp;
        g.setDefaultPadding(t.t(g.getDefaultPadding()));
        for (int i = 0; i < g.nodes.size(); i++) {
            Node node = (Node) g.nodes.get(i);
            temp = node.width;
            node.width = node.height;
            node.height = temp;
            temp = node.y;
            node.y = node.x;
            node.x = temp;
            if (node.getPadding() != null)
                node.setPadding(t.t(node.getPadding()));
        }
        for (int i = 0; i < g.edges.size(); i++) {
            Edge edge = g.edges.getEdge(i);
            edge.start.transpose();
            edge.end.transpose();
            edge.getPoints().transpose();
            List bends = edge.vNodes;
            if (bends == null)
                continue;
            for (int b = 0; b < bends.size(); b++) {
                VirtualNode vnode = (VirtualNode) bends.get(b);
                temp = vnode.y;
                vnode.y = vnode.x;
                vnode.x = temp;
                temp = vnode.width;
                vnode.width = vnode.height;
                vnode.height = temp;
            }
        }
        g.size.transpose();
    }

}
