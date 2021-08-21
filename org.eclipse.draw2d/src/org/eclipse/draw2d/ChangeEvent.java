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
 * An event for property changes. Includes the source of the event as well as
 * the name of the property that has changed.
 */
public class ChangeEvent extends java.util.EventObject {

    private String property;

    /**
     * Constructs a new ChangeEvent with the given object as the source of the
     * event.
     * 
     * @param source
     *            The source of the event
     */
    public ChangeEvent(Object source) {
        super(source);
    }

    /**
     * Constructs a new ChangeEvent with the given source object and property
     * name.
     * 
     * @param source
     *            The source of the event
     * @param property
     *            The property name
     */
    public ChangeEvent(Object source, String property) {
        super(source);
        setPropertyName(property);
    }

    /**
     * Returns the name of the property that has changed.
     * 
     * @return String the name of the property that has changed
     */
    public String getPropertyName() {
        return property;
    }

    /**
     * Sets the name of the property that has changed.
     * 
     * @param string
     *            The property name
     */
    protected void setPropertyName(String string) {
        property = string;
    }

}
