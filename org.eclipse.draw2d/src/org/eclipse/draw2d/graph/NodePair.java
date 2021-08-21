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
 * @author hudsonr
 * @since 2.1
 */
class NodePair {

    public Node n1;
    public Node n2;

    public NodePair() {
    }

    public NodePair(Node n1, Node n2) {
        this.n1 = n1;
        this.n2 = n2;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NodePair) {
            NodePair np = (NodePair) obj;
            return np.n1 == n1 && np.n2 == n2;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return n1.hashCode() ^ n2.hashCode();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "[" + n1 + ", " + n2 + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

}
