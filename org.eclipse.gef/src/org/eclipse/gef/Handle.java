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
package org.eclipse.gef;

import org.eclipse.draw2d.geometry.Point;

/**
 * An interface used by the {@link org.eclipse.gef.tools.SelectionTool} to
 * obtain a DragTracker. A GraphicalViewer will return a Handle at a given
 * location. The <code>SelectionTool</code> looks for <code>Handles</code> first
 * whenever the User presses the mouse button. If a Handle is found, it usually
 * offers a DragTracker, although <code>null</code> can also be returned.
 * <P>
 * For keyboard accessibility purposes, a Handle can provide a Point at which
 * the SelectionTool should programmatically place the mouse.
 */
public interface Handle {

    /**
     * Returns the DragTracker for dragging this Handle.
     * 
     * @return <code>null</code> or a <code>DragTracker</code>
     */
    DragTracker getDragTracker();

    /**
     * Returns an optional accessibility Point. This returned point is in
     * absolute coordinates.
     * 
     * @return <code>null</code> or the absolute location
     */
    Point getAccessibleLocation();

}
