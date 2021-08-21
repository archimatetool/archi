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

import java.beans.PropertyChangeListener;

/**
 * This interface represents a range of possible values as well as the current
 * values. There is a minumum and maximum value, a current value, and the
 * extent. One use for a RangeModel is a scrollbar. There is a minimum value
 * (the top of the scrollbar), a maximum value (the bottom of the scrollbar), a
 * current value (the top of the thumb), and an extent (the length of the
 * thumb).
 */
public interface RangeModel {

    /** Value property name */
    String PROPERTY_VALUE = "value"; //$NON-NLS-1$
    /** Extent property name */
    String PROPERTY_EXTENT = "extent"; //$NON-NLS-1$
    /** Minimum property name */
    String PROPERTY_MINIMUM = "minimum"; //$NON-NLS-1$
    /** Maximum property name */
    String PROPERTY_MAXIMUM = "maximum"; //$NON-NLS-1$

    /**
     * Registers listener as a PropertyChangeListener of this RangeModel.
     * Listeners will be notified of changes to {@link #PROPERTY_VALUE value},
     * {@link #PROPERTY_EXTENT extent}, {@link #PROPERTY_MINIMUM minimum} and
     * {@link #PROPERTY_MAXIMUM maximum} properties.
     * 
     * @param listener
     *            The listener to add
     */
    void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * Returns the extent.
     * 
     * @return The extent
     */
    int getExtent();

    /**
     * Returns the maximum value in the range.
     * 
     * @return The maximum value
     */
    int getMaximum();

    /**
     * Returns the minimum value in the range.
     * 
     * @return The minimum value
     */
    int getMinimum();

    /**
     * Returns the current value.
     * 
     * @return The current value
     */
    int getValue();

    /**
     * Returns <code>true</code> if this RangeModel is enabled.
     * 
     * @return <code>true</code> if this Rangel Model is enabled
     */
    boolean isEnabled();

    /**
     * Removes the given listener from this RangeModel's list of
     * PropertyChangeListeners.
     * 
     * @param listener
     *            The listener to remove
     */
    void removePropertyChangeListener(PropertyChangeListener listener);

    /**
     * Sets min, extent, and max all at once.
     * 
     * @param min
     *            the new mininum
     * @param extent
     *            the new extent
     * @param max
     *            the new maximum
     */
    void setAll(int min, int extent, int max);

    /**
     * Sets the extent.
     * 
     * @param extent
     *            The extent
     */
    void setExtent(int extent);

    /**
     * Sets the maximum value of the range.
     * 
     * @param max
     *            The maximum value
     */
    void setMaximum(int max);

    /**
     * Sets the minimum value of the range.
     * 
     * @param min
     *            The minimum value
     */
    void setMinimum(int min);

    /**
     * Sets the current value.
     * 
     * @param value
     *            The current value
     */
    void setValue(int value);

}
