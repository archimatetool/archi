/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.zest.layouts.progress;

/**
 * Listens for ProgressEvents which are thrown by layout algorithms at frequent intervals.
 * 
 * @author Ian Bull
 * @author Casey Best
 */
public interface ProgressListener {

    /**
     * 
     * @param e
     */
    public void progressStarted(ProgressEvent e);

    /**
     * Called when the progress of a layout changes
     */
    public void progressUpdated(ProgressEvent e);

    /**
     * 
     * @param e
     */
    public void progressEnded(ProgressEvent e);
}
