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

/**
 * The listener interface for receiving basic events from an EditPart. Listeners
 * interested in only one type of Event can extend the
 * {@link EditPartListener.Stub} implementation rather than implementing the
 * entire interface.
 */

public interface EditPartListener {

    /**
     * Listeners interested in just a subset of Events can extend this stub
     * implementation. Also, extending the Stub will reduce the impact of new
     * API on this interface.
     */
    public class Stub implements EditPartListener {
        /**
         * @see org.eclipse.gef.EditPartListener#childAdded(EditPart, int)
         */
        @Override
        public void childAdded(EditPart child, int index) {
        }

        /**
         * @see org.eclipse.gef.EditPartListener#partActivated(EditPart)
         */
        @Override
        public void partActivated(EditPart editpart) {
        }

        /**
         * @see org.eclipse.gef.EditPartListener#partDeactivated(EditPart)
         */
        @Override
        public void partDeactivated(EditPart editpart) {
        }

        /**
         * @see org.eclipse.gef.EditPartListener#removingChild(EditPart, int)
         */
        @Override
        public void removingChild(EditPart child, int index) {
        }

        /**
         * @see org.eclipse.gef.EditPartListener#selectedStateChanged(EditPart)
         */
        @Override
        public void selectedStateChanged(EditPart part) {
        }
    };

    /**
     * Called after a child EditPart has been added to its parent.
     * 
     * @param child
     *            the Child
     * @param index
     *            the index at which the child was added
     */
    void childAdded(EditPart child, int index);

    /**
     * Called when the editpart has been activated.
     * 
     * @param editpart
     *            the EditPart
     */
    void partActivated(EditPart editpart);

    /**
     * Called when the editpart has been deactivated.
     * 
     * @param editpart
     *            the EditPart
     */
    void partDeactivated(EditPart editpart);

    /**
     * Called before a child EditPart is removed from its parent.
     * 
     * @param child
     *            the Child being removed
     * @param index
     *            the child's current location
     */
    void removingChild(EditPart child, int index);

    /**
     * Called when the selected state of an EditPart has changed. Focus changes
     * also result in this method being called.
     * 
     * @param editpart
     *            the part whose selection was changed
     * @see EditPart#getSelected()
     */
    void selectedStateChanged(EditPart editpart);

}
