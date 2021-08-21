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
 * Specializes {@link EditPartViewer} adding the ability to hit-test
 * {@link Handle Handles}.
 * 
 * @see org.eclipse.gef.ui.parts.GraphicalViewerImpl
 */
public interface GraphicalViewer extends EditPartViewer {

    /**
     * Returns the <code>Handle</code> at the specified Point. Returns
     * <code>null</code> if no handle exists at the given Point. The specified
     * point should be relative to the
     * {@link org.eclipse.swt.widgets.Scrollable#getClientArea() client area}
     * for this Viewer's <code>Control</code>.
     * 
     * @param p
     *            the location relative to the Control's client area
     * @return Handle <code>null</code> or a Handle
     */
    Handle findHandleAt(Point p);

}
