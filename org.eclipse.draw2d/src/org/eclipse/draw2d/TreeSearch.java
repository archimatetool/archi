/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d;

/**
 * A helper used in depth-first searches of a figure subgraph.
 * 
 * @author hudsonr
 * @since 2.1
 */
public interface TreeSearch {

    /**
     * Returns <code>true</code> if the given figure is accepted by the search.
     * 
     * @param figure
     *            the current figure in the traversal
     * @return <code>true</code> if the figure is accepted
     */
    boolean accept(IFigure figure);

    /**
     * Returns <code>true</code> if the figure and all of its contained figures
     * should be pruned from the search.
     * 
     * @param figure
     *            the current figure in the traversal
     * @return <code>true</code> if the subgraph should be pruned
     */
    boolean prune(IFigure figure);

}
