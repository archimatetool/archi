/*******************************************************************************
 * Copyright 2005-2006, CHISEL Group, University of Victoria, Victoria, BC,
 * Canada. All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.zest.core.viewers;

/**
 * An interface that can be added to IWorkbenchParts based on ZEST views so that zooming
 * is supported.
 * @author Del Myers
 *
 */
//@tag bug.156286-Zooming.fix : experimental
public interface IZoomableWorkbenchPart {
    /**
     * Returns the viewer that is zoomable.
     * @return the viewer that is zoomable.
     */
    AbstractZoomableViewer getZoomableViewer();
}
