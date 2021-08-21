/*******************************************************************************
 * Copyright (c) 2005, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.zest.core.viewers;

import org.eclipse.draw2d.IFigure;

/**
 * Allows a user to create a figure for an element in 
 * graph model.  To use this interface, it should 
 * be implemented and passed to {@link GraphViewer#setLabelProvider()}
 */
public interface IFigureProvider {

    /**
     * Creates a custom figure for a graph model element
     */
    public IFigure getFigure(Object element);
}
