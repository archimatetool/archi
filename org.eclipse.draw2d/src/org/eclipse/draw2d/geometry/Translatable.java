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
package org.eclipse.draw2d.geometry;

/**
 * A translatable object can be translated (or moved) vertically and/or
 * horizontally.
 */
public interface Translatable {

    /**
     * Translates this object horizontally by <code>dx</code> and vertically by
     * <code>dy</code>.
     * 
     * @param dx
     *            The amount to translate horizontally
     * @param dy
     *            The amount to translate vertically
     * @since 2.0
     */
    void performTranslate(int dx, int dy);

    /**
     * Scales this object by the scale factor.
     * 
     * @param factor
     *            The scale factor
     * @since 2.0
     */
    void performScale(double factor);

}
