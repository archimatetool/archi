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
 * A generic state listener
 */
public interface ChangeListener {

    /**
     * Called when the listened to object's state has changed.
     * 
     * @param event
     *            the ChangeEvent
     */
    void handleStateChanged(ChangeEvent event);

}
