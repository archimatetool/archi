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
 * A listener interface for receiving notification that an IFigure has moved.
 */
public interface FigureListener {

    /**
     * Called when the given IFigure has moved.
     * 
     * @param source
     *            The IFigure that has moved.
     */
    void figureMoved(IFigure source);

}
