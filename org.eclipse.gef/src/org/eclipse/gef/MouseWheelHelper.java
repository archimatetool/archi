/*******************************************************************************
 * Copyright (c) 2005, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef;

import org.eclipse.swt.widgets.Event;

/**
 * MouseWheelHelpers provide each EditPart to handle mouse-wheel events in their
 * own desired way. By default, when mouse-wheel events are generated that have
 * no stateMask, a default {@link MouseWheelHandler} delegates the task to the
 * MouseWheelHelper that is returned by the getAdapter() method of the EditPart
 * in focus.
 * <p>
 * The most common usage of MouseWheelHelpers involves scrolling of an
 * EditPart's figure. They should be returned in the EditPart's getAdapter()
 * method.
 * 
 * @see org.eclipse.gef.editparts.ViewportMouseWheelHelper
 * @author Pratik Shah
 * @since 3.1
 */
public interface MouseWheelHelper {

    /**
     * Handles mouse-wheel events. If the given event was handled in some way,
     * its {@link Event#doit doit} field should be set to false so as to prevent
     * further processing of that event.
     * 
     * @param event
     *            The SWT event that was generated as a result of the
     *            mouse-wheel scrolling
     */
    void handleMouseWheelScrolled(Event event);

}
