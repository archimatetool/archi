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
 * For Internal Use only.
 * 
 * @author hudsonr
 * @since 2.1.2
 */
@SuppressWarnings("unchecked")
public class Rank extends NodeList {

    int bottomPadding;
    int height;
    int location;

    final int hash = new Object().hashCode();
    int topPadding;
    int total;

    void add(Node n) {
        super.add(n);
    }

    void assignIndices() {
        total = 0;
        Node node;

        int mag;
        for (int i = 0; i < size(); i++) {
            node = getNode(i);
            mag = Math.max(1, node.incoming.size() + node.outgoing.size());
            mag = Math.min(mag, 5);
            if (node instanceof SubgraphBoundary)
                mag = 4;
            total += mag;
            node.index = total;
            total += mag;
        }
    }

    /**
     * Returns the number of nodes in this rank.
     * 
     * @return the number of nodes
     */
    public int count() {
        return super.size();
    }

    /**
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object o) {
        return o == this;
    }

    /**
     * @see Object#hashCode() Overridden for speed based on equality.
     */
    @Override
    public int hashCode() {
        return hash;
    }

    void setDimensions(int location, int rowHeight) {
        this.height = rowHeight;
        this.location = location;
        for (int i = 0; i < size(); i++) {
            Node n = getNode(i);
            n.y = location;
            n.height = rowHeight;
        }
    }

    /**
     * @deprecated Do not call
     */
    public void sort() {
    }

}
