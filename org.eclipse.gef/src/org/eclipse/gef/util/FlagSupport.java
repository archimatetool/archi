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
package org.eclipse.gef.util;

/**
 * A structure for storing multiple flags compactly using a 32-bit
 * <code>int</code>.
 * 
 * @author hudsonr
 */
public class FlagSupport {

    private int flags;

    /**
     * Returns <code>true</code> the flag (or one of the flags) indicated by the
     * given bitmask is set to true.
     * 
     * @param flag
     *            the bitmask of a flag or flags
     * @return <code>true</code> if one of the flags is true
     */
    protected boolean getFlag(int flag) {
        return (flags & flag) != 0;
    }

    /**
     * Sets the flag (or all of the flags) indicated by the given bitmask to the
     * given value.
     * 
     * @param flag
     *            the bitmask of the flag or flags
     * @param value
     *            the new value
     */
    protected void setFlag(int flag, boolean value) {
        if (value)
            flags |= flag;
        else
            flags &= ~flag;
    }

}
