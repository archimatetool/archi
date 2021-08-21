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

import java.util.Iterator;

/**
 * This class is intended for internal use only. TODO: If this is for internal
 * use only, we should move it to the internal package.
 */
@SuppressWarnings("rawtypes")
public final class EventListenerList {

    private volatile Object array[];

    /**
     * Adds a listener of type <i>c</i> to the list.
     * 
     * @param c
     *            the class
     * @param listener
     *            the listener
     */
    public synchronized void addListener(Class c, Object listener) {
        if (listener == null || c == null)
            throw new IllegalArgumentException();

        int oldSize = (array == null) ? 0 : array.length;
        Object[] newArray = new Object[oldSize + 2];
        if (oldSize != 0)
            System.arraycopy(array, 0, newArray, 0, oldSize);
        newArray[oldSize++] = c;
        newArray[oldSize] = listener;
        array = newArray;
    }

    /**
     * Returns <code>true</code> if this list of listeners contains a listener
     * of type <i>c</i>.
     * 
     * @param c
     *            the type
     * @return whether this list contains a listener of type <i>c</i>
     */
    public synchronized boolean containsListener(Class c) {
        if (array == null)
            return false;
        for (int i = 0; i < array.length; i += 2)
            if (array[i] == c)
                return true;
        return false;
    }

    static class TypeIterator implements Iterator {
        private final Object[] items;
        private final Class type;
        private int index;

        TypeIterator(Object items[], Class type) {
            this.items = items;
            this.type = type;
        }

        @Override
        public Object next() {
            Object result = items[index + 1];
            index += 2;
            return result;
        }

        @Override
        public boolean hasNext() {
            if (items == null)
                return false;
            while (index < items.length && items[index] != type)
                index += 2;
            return index < items.length;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException(
                    "Iterator removal not supported"); //$NON-NLS-1$
        }
    }

    /**
     * Returns an Iterator of all the listeners of type <i>c</i>.
     * 
     * @param listenerType
     *            the type
     * @return an Iterator of all the listeners of type <i>c</i>
     */
    public synchronized Iterator getListeners(final Class listenerType) {
        return new TypeIterator(array, listenerType);
    }

    /**
     * Removes the first <i>listener</i> of the specified type by identity.
     * 
     * @param c
     *            the type
     * @param listener
     *            the listener
     */
    public synchronized void removeListener(Class c, Object listener) {
        if (array == null || array.length == 0)
            return;
        if (listener == null || c == null)
            throw new IllegalArgumentException();

        int index = 0;
        while (index < array.length) {
            if (array[index] == c && array[index + 1] == listener)
                break;
            index += 2;
        }
        if (index == array.length)
            return; // listener was not found

        Object newArray[] = new Object[array.length - 2];
        System.arraycopy(array, 0, newArray, 0, index);
        System.arraycopy(array, index + 2, newArray, index, array.length
                - index - 2);
        array = newArray;
    }

}
