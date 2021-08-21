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
package org.eclipse.gef.requests;

/**
 * A request that requires a location in order to drop an item.
 */
public interface DropRequest {

    /**
     * Returns the current mouse location.
     * 
     * @return the mouse location
     */
    org.eclipse.draw2d.geometry.Point getLocation();

}
