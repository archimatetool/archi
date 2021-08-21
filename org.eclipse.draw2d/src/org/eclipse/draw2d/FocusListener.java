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
 * A listener interface for receiving {@link FocusEvent FocusEvents}.
 */
public interface FocusListener {

    /**
     * Called when the listened to object has gained focus.
     * 
     * @param fe
     *            The FocusEvent object
     */
    void focusGained(FocusEvent fe);

    /**
     * Called when the listened to object has lost focus.
     * 
     * @param fe
     *            The FocusEvent object
     */
    void focusLost(FocusEvent fe);

    /**
     * An empty implementation of FocusListener for convenience.
     */
    public class Stub implements FocusListener {
        /**
         * @see org.eclipse.draw2d.FocusListener#focusGained(FocusEvent)
         */
        @Override
        public void focusGained(FocusEvent fe) {
        }

        /**
         * @see org.eclipse.draw2d.FocusListener#focusLost(FocusEvent)
         */
        @Override
        public void focusLost(FocusEvent fe) {
        }
    }

}
