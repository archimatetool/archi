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
import java.beans.PropertyChangeSupport;

/**
 * Generic implementation for a RangeModel.
 * 
 * 
 * <pre>
 *                    |<----extent--->|                     
 *    ----|-----------|---------------|---------------|----
 *       min          |                              max
 *                  value
 * </pre>
 */
public class DefaultRangeModel implements RangeModel {

    /**
     * Listeners interested in the range model's property changes.
     */
    protected PropertyChangeSupport propertyListeners = new PropertyChangeSupport(
            this);
    private int minimum = 0;
    private int maximum = 100;
    private int extent = 20;
    private int value = 0;

    /**
     * Registers the given listener as a PropertyChangeListener.
     * 
     * @param listener
     *            the listener to be added
     * @since 2.0
     */
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyListeners.addPropertyChangeListener(listener);
    }

    /**
     * Notifies any listening PropertyChangeListeners that the property with the
     * given id has changed.
     * 
     * @param string
     *            the property name
     * @param oldValue
     *            the old value
     * @param newValue
     *            the new value
     * @since 2.0
     */
    protected void firePropertyChange(String string, int oldValue, int newValue) {
        propertyListeners.firePropertyChange(string, oldValue, newValue);
    }

    /**
     * @return the extent
     */
    @Override
    public int getExtent() {
        return extent;
    }

    /**
     * @return the maximum value
     */
    @Override
    public int getMaximum() {
        return maximum;
    }

    /**
     * @return the minimum value
     */
    @Override
    public int getMinimum() {
        return minimum;
    }

    /**
     * @return the current value
     */
    @Override
    public int getValue() {
        return value;
    }

    /**
     * @return whether the extent is between the minimum and maximum values
     */
    @Override
    public boolean isEnabled() {
        return (getMaximum() - getMinimum()) > getExtent();
    }

    /**
     * Removes the given PropertyChangeListener from the list of listeners.
     * 
     * @param listener
     *            the listener to be removed
     */
    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyListeners.removePropertyChangeListener(listener);
    }

    /**
     * @see org.eclipse.draw2d.RangeModel#setAll(int, int, int)
     */
    @Override
    public void setAll(int min, int ext, int max) {
        int oldMin = minimum;
        int oldExtent = extent;
        int oldMax = maximum;
        maximum = max;
        minimum = min;
        extent = ext;
        if (oldMax != max)
            firePropertyChange(PROPERTY_MAXIMUM, oldMax, max);
        if (oldExtent != ext)
            firePropertyChange(PROPERTY_EXTENT, oldExtent, ext);
        if (oldMin != min)
            firePropertyChange(PROPERTY_MINIMUM, oldMin, min);
        setValue(getValue());
    }

    /**
     * Sets this RangeModel's extent and fires a property change if the given
     * value is different from the current extent.
     * 
     * @param extent
     *            the new extent value
     */
    @Override
    public void setExtent(int extent) {
        if (this.extent == extent)
            return;
        int oldValue = this.extent;
        this.extent = extent;
        firePropertyChange(PROPERTY_EXTENT, oldValue, extent);
        setValue(getValue());
    }

    /**
     * Sets this RangeModel's maximum value and fires a property change if the
     * given value is different from the current maximum value.
     * 
     * @param maximum
     *            the new maximum value
     */
    @Override
    public void setMaximum(int maximum) {
        if (this.maximum == maximum)
            return;
        int oldValue = this.maximum;
        this.maximum = maximum;
        firePropertyChange(PROPERTY_MAXIMUM, oldValue, maximum);
        setValue(getValue());
    }

    /**
     * Sets this RangeModel's minimum value and fires a property change if the
     * given value is different from the current minimum value.
     * 
     * @param minimum
     *            the new minumum value
     */
    @Override
    public void setMinimum(int minimum) {
        if (this.minimum == minimum)
            return;
        int oldValue = this.minimum;
        this.minimum = minimum;
        firePropertyChange(PROPERTY_MINIMUM, oldValue, minimum);
        setValue(getValue());
    }

    /**
     * Sets this RangeModel's current value. If the given value is greater than
     * the maximum, the maximum value is used. If the given value is less than
     * the minimum, the minimum value is used. If the adjusted value is
     * different from the current value, a property change is fired.
     * 
     * @param value
     *            the new value
     */
    @Override
    public void setValue(int value) {
        value = Math.max(getMinimum(),
                Math.min(getMaximum() - getExtent(), value));
        if (this.value == value)
            return;
        int oldValue = this.value;
        this.value = value;
        firePropertyChange(PROPERTY_VALUE, oldValue, value);
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return super.toString() + " (" + minimum + ", " + maximum //$NON-NLS-2$ //$NON-NLS-1$
                + ", " + extent + ", " + value + ")"; //$NON-NLS-3$ //$NON-NLS-2$ //$NON-NLS-1$
    }

}
