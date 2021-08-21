/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * University of Victoria - Adapted for XY Scaled Graphics
 *******************************************************************************/
package org.eclipse.zest.core.widgets.internal;

import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.IFigure;

/**
 * The root figure for Zest.  The figure is broken up into four segments, 
 * 1. The Connections
 * 2. The Nodes
 * 3. The Highlighted Connections
 * 4. The Highlighted Nodes
 * 
 * @author Ian Bull
 * 
 */
@SuppressWarnings("unchecked")
public class ZestRootLayer extends FreeformLayer {

    public static final boolean EDGES_ON_TOP = false;
    private int numberOfNodes = 0;
    private int numberOfConnections = 0;
    private int numberOfHighlightedNodes = 0;
    private int numberOfHighlightedConnections = 0;

    /**
     * Adds a node to the ZestRootLayer
     * @param nodeFigure The figure representing the node
     */
    public void addNode(IFigure nodeFigure) {
        int nodePosition = getNodePosition();
        numberOfNodes++;
        add(nodeFigure, nodePosition);
    }

    /**
     * Removes a node from the layer
     * @param nodeFigure
     */
    public void removeNode(IFigure nodeFigure) {
        if (!this.getChildren().contains(nodeFigure)) {
            throw new RuntimeException("Node not contained on the ZestRootLayer"); //$NON-NLS-1$
        }
        int nodePosition = this.getChildren().indexOf(nodeFigure);
        if (nodePosition > getHighlightNodeStartPosition()) {
            // The node is in the highlight node area
            numberOfHighlightedNodes--;
        } else {
            // The node is in the node area
            numberOfNodes--;
        }
        this.remove(nodeFigure);
    }

    public void removeConnection(IFigure connectionFigure) {
        int connectionPosition = this.getChildren().indexOf(connectionFigure);
        if (connectionPosition > getHighlightConnectionStartPosition()) {
            // The connection is in the highlight connection area
            numberOfHighlightedConnections--;
        } else {
            // The connection is in the connection area
            numberOfConnections--;
        }
        this.remove(connectionFigure);
    }

    public void addConnection(IFigure connectionFigure) {
        int connectionPosition = getConnectionPosition();
        numberOfConnections++;
        add(connectionFigure, connectionPosition);
    }

    public void highlightNode(IFigure nodeFigure) {
        this.numberOfNodes--;
        int highlightNodePosition = getHighlightNodePosition();
        this.numberOfHighlightedNodes++;
        this.getChildren().remove(nodeFigure);
        this.getChildren().add(highlightNodePosition, nodeFigure);
        this.invalidate();
        this.repaint();
    }

    public void highlightConnection(IFigure connectionFigure) {
        this.numberOfConnections--;
        int highlightConnectionPosition = getHighlightConnectionPosition();
        this.numberOfHighlightedConnections++;
        this.getChildren().remove(connectionFigure);
        this.getChildren().add(highlightConnectionPosition, connectionFigure);
        this.invalidate();
        this.repaint();
    }

    public void unHighlightNode(IFigure nodeFigure) {
        int nodePosition = this.getChildren().indexOf(nodeFigure);
        if (nodePosition < 0 || nodePosition > getHighlightNodePosition()) {
            //throw new RuntimeException("Node: " + nodeFigure + " not currently Highlighted");
            return;
        }
        this.numberOfHighlightedNodes--;
        nodePosition = getNodePosition();
        this.numberOfNodes++;
        this.getChildren().remove(nodeFigure);
        this.getChildren().add(nodePosition, nodeFigure);
        this.invalidate();
        this.repaint();
    }

    public void unHighlightConnection(IFigure connectionFigure) {
        int connectionPosition = this.getChildren().indexOf(connectionFigure);
        if (connectionPosition < 0 || connectionPosition > getHighlightConnectionPosition()) {
            //throw new RuntimeException("Connection: " + connectionFigure + " not currently Highlighted");
            return;
        }
        this.numberOfHighlightedConnections--;
        this.numberOfConnections++;
        connectionPosition = getConnectionPosition();
        this.getChildren().remove(connectionFigure);
        if (connectionPosition > this.getChildren().size()) {
            this.getChildren().add(connectionFigure);
        } else {
            this.getChildren().add(connectionPosition, connectionFigure);
        }
        this.invalidate();
        this.repaint();
    }

    /*
     * Node position is at the end of the list of nodes
     */
    private int getNodePosition() {
        if (EDGES_ON_TOP) {
            return numberOfNodes;
        }
        return numberOfConnections + numberOfNodes;
    }

    /*
     * Connection position is at the end of the list of connections
     */
    private int getConnectionPosition() {
        if (EDGES_ON_TOP) {
            return 0 + numberOfConnections + numberOfNodes;
        }
        return 0 + numberOfConnections;
    }

    /*
     * Highlight node position is at the end of the list of highlighted nodes
     */
    private int getHighlightNodePosition() {
        if (EDGES_ON_TOP) {
            return numberOfConnections + numberOfNodes + numberOfHighlightedNodes;
        }
        return numberOfConnections + numberOfHighlightedConnections + numberOfNodes + numberOfHighlightedNodes;
    }

    /*
     * Highlighted connection position is at the end of the list of highlighted connections
     */
    private int getHighlightConnectionPosition() {
        if (EDGES_ON_TOP) {
            return numberOfNodes + +numberOfConnections + numberOfHighlightedNodes + numberOfHighlightedConnections;
        }
        return numberOfConnections + numberOfNodes + numberOfHighlightedConnections;
    }

    private int getHighlightConnectionStartPosition() {
        if (EDGES_ON_TOP) {
            return numberOfConnections + numberOfNodes + numberOfHighlightedNodes;

        }
        return numberOfConnections + numberOfNodes;
    }

    private int getHighlightNodeStartPosition() {
        if (EDGES_ON_TOP) {
            return numberOfNodes + numberOfConnections;
        }
        return numberOfConnections + numberOfHighlightedConnections + numberOfNodes;
    }

}
