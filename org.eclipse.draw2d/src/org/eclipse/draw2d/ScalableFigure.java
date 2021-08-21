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
package org.eclipse.draw2d;

/**
 * A figure that can be scaled.
 * 
 * @author Eric Bordeau
 * @since 2.1.1
 */
public interface ScalableFigure extends IFigure {

    /**
     * Returns the current scale.
     * 
     * @return the current scale
     */
    double getScale();

    /**
     * Sets the new scale factor.
     * 
     * @param scale
     *            the scale
     */
    void setScale(double scale);

}
