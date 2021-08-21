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
package org.eclipse.gef.ui.palette.customize;

/**
 * An <code>EntryPageContainer</code> allows an <code>EntryPage</code> to report
 * errors to it.
 * 
 * @author Pratik Shah
 */
public interface EntryPageContainer {

    /**
     * Clears the error.
     */
    void clearProblem();

    /**
     * Shows the error to the user.
     * 
     * @param description
     *            A description of the problem. Should be as brief as possible.
     */
    void showProblem(String description);

}
