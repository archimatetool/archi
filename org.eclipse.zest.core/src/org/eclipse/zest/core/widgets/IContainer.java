/*******************************************************************************
 * Copyright 2005-2007, CHISEL Group, University of Victoria, Victoria, BC,
 * Canada. All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.zest.core.widgets;

import java.util.List;

import org.eclipse.zest.layouts.LayoutAlgorithm;

/**
 * This interface describes all Zest components that are Containers. This is an internal interface
 * and thus should not be used outside of Zest.  Implementors of this interface must include the 
 * following two methods
 *   o addNode(GraphNode)
 *   o addNode(GraphContainer)
 *   
 * These are not actually listed here because Java does not allow protected methods in
 * interfaces.
 * 
 * @author Ian Bull
 */
@SuppressWarnings("rawtypes")
public interface IContainer {

    public Graph getGraph();

    // All implementers must include this method
    /* protected void addNode(GraphNode node); */

    // All implementers must include this method
    /* protected void addNode(GraphContainer container); */

    public int getItemType();

    /**
     * Re-applies the current layout algorithm
     */
    public void applyLayout();

    /**
     * Sets the LayoutAlgorithm for this container and optionally applies it.
     *  
     * @param algorithm The layout algorithm to set
     * @param applyLayout 
     */
    public void setLayoutAlgorithm(LayoutAlgorithm algorithm, boolean applyLayout);

    public List getNodes();

    /* protected void highlightNode(GraphNode node); */

    /* protected void highlightNode(GraphContainer container);*/

    /* protected void unhighlightNode(GraphNode node); */

    /* protected void unhighlightNode(GraphContainer container);*/

} // end of IContainer
