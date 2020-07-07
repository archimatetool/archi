/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.archi.part;

import java.util.ArrayDeque;
import java.util.Deque;


/*
 * A <code>DrillStack</code> manages a stack of DrillFrames.
 * This class is not intended for use beyond the package.
 */

/*
 * Patched by Phillipus
 * See https://github.com/archimatetool/archi/issues/644
 * See https://bugs.eclipse.org/bugs/show_bug.cgi?id=551135
 * 
 * TODO - Remove this and associated classes DrillDownAdapter and DrillFrame when it's fixed
 */

/* package */class DrillStack {
	Deque<DrillFrame> fStack = null;

	/**
	 * Allocates a new DrillStack.
	 */
	public DrillStack() {
		reset();
	}

	/**
	 * Adds a drill frame to the stack.
	 *
	 * @param oRecord the new drill frame
	 */
	public DrillFrame add(DrillFrame oRecord) {
		fStack.push(oRecord);
		return oRecord;
	}

	/**
	 * Returns true if backward navigation is possible. This is only true if the
	 * stack size is greater than 0.
	 *
	 * @return true if backward navigation is possible
	 */
	public boolean canGoBack() {
		return !fStack.isEmpty();
	}

	/**
	 * Returns true if "go home" is possible. This is only true if the stack size is
	 * greater than 0.
	 *
	 * @return true if "go home" is possible
	 */
	public boolean canGoHome() {
		return !fStack.isEmpty();
	}

	/**
	 * Navigate backwards one record.
	 */
	public DrillFrame goBack() {
		return fStack.pop();
	}

	/**
	 * Navigate to the home record.
	 */
	public DrillFrame goHome() {
	    /*
	     * Patched by Phillipus - original version had
	     * DrillFrame aFrame = fStack.getFirst();
	     * See https://github.com/archimatetool/archi/issues/644
	     * See https://bugs.eclipse.org/bugs/show_bug.cgi?id=551135
	     */
		DrillFrame aFrame = fStack.getLast();
		reset();
		return aFrame;
	}

	/**
	 * Clears the navigation stack.
	 */
	public void reset() {
		fStack = new ArrayDeque<>();
	}

	/**
	 * Returns the stack size.
	 *
	 * @return the stack size
	 */
	public int size() {
		return fStack.size();
	}

	/**
	 * Returns the top element on the stack.
	 *
	 * @return the top element on the stack
	 */
	public DrillFrame top() {
		return fStack.peek();
	}
}
