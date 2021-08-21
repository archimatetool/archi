/*******************************************************************************
 * Copyright (c) 2003, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.rulers;

/**
 * <code>RulerChangeListener</code>s can be added to <code>RulerProvider</code>s
 * to receive notification about changes in the ruler/guide properties. This
 * interface provides a mechanism for the (client-side) ruler/guide model to
 * communicate changes to GEF's ruler/guide feature.
 * 
 * @see org.eclipse.gef.rulers.RulerProvider
 * 
 * @author Pratik Shah
 * @since 3.0
 */
public interface RulerChangeListener {

    /**
     * Invoked when the ruler's unit of measurement is changed.
     * 
     * @param newUnit
     *            The new unit of measurement (RulerProvider.UNIT_INCES,
     *            UNIT_CENTIMETERS, or UNIT_PIXELS)
     */
    void notifyUnitsChanged(int newUnit);

    /**
     * Invoked when a guide is added to or removed from a ruler.
     * 
     * @param guide
     *            The guide that was added or removed
     */
    void notifyGuideReparented(Object guide);

    /**
     * Invoked when a guide is moved.
     * 
     * @param guide
     *            The guide that was moved
     */
    void notifyGuideMoved(Object guide);

    /**
     * Invoked when a graphical component is attached to a guide.
     * 
     * @param part
     *            The model representation of the graphical compoment that was
     *            attached
     * @param guide
     *            The guide that the part was attached to
     */
    void notifyPartAttachmentChanged(Object part, Object guide);

    /**
     * Stub for the RulerChangeListener interface. Clients not intending to
     * implement all the methods in that interface can extend this class.
     * 
     * @author Pratik Shah
     * @since 3.0
     */
    public class Stub implements RulerChangeListener {
        /**
         * @see RulerChangeListener#notifyUnitsChanged(int)
         */
        @Override
        public void notifyUnitsChanged(int newUnit) {
        }

        /**
         * @see RulerChangeListener#notifyGuideReparented(Object)
         */
        @Override
        public void notifyGuideReparented(Object guide) {
        }

        /**
         * @see RulerChangeListener#notifyGuideMoved(Object)
         */
        @Override
        public void notifyGuideMoved(Object guide) {
        }

        /**
         * @see RulerChangeListener#notifyPartAttachmentChanged(Object, Object)
         */
        @Override
        public void notifyPartAttachmentChanged(Object part, Object guide) {
        }
    }

}
