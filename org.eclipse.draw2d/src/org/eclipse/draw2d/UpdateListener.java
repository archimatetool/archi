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

import java.util.Map;

import org.eclipse.draw2d.geometry.Rectangle;

/**
 * An interface used to notify listeners that the listened to object is
 * updating.
 */
@SuppressWarnings("rawtypes")
public interface UpdateListener {

    /**
     * Notifies the listener that the listened to object is painting. The damage
     * rectangle may be null or empty. This indicates the dirty regions were
     * clipped or not visible. But for objects such as the
     * {@link org.eclipse.draw2d.parts.Thumbnail}, notification still needs to
     * occur. The map of dirty regions is passed to allow the listener to
     * determine if it needs to update, for instance when a particular figure is
     * painting.
     * 
     * @param damage
     *            The area being painted
     * @param dirtyRegions
     *            a Map of figures to their dirty regions
     */
    void notifyPainting(Rectangle damage, Map dirtyRegions);

    /**
     * Notifies the listener that the listened to object is validating.
     */
    void notifyValidating();

}
