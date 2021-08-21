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
 * Listens for extent changes in {@link org.eclipse.draw2d.FreeformFigure
 * FreeformFigures}. Created on :Oct 4, 2002
 * 
 * @author hudsonr
 * @since 2.0
 */
public interface FreeformListener {

    /**
     * Called when the extent of the FreeformFigure has changed.
     */
    void notifyFreeformExtentChanged();

}
