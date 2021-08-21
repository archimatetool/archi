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

import java.util.HashMap;
import java.util.Map;

/**
 * An Object used to communicate with EditParts. Request encapsulates the
 * information EditParts need to perform various functions. Requests are used
 * for obtaining commands, showing feedback, and performing generic operations.
 * TODO: This should probably go into the org.eclipse.gef.requests package.
 */
@SuppressWarnings("rawtypes")
public class Request {

    private Object type;
    private Map extendedData;

    /**
     * Constructs an empty Request
     */
    public Request() {
    }

    /**
     * Constructs a Request with the specified <i>type</i>
     * 
     * @param type
     *            the Request type
     * @see #getType()
     */
    public Request(Object type) {
        setType(type);
    }

    /**
     * Returns a Map that can be used to save useful information in this
     * request.
     * 
     * @return a map to store useful information
     */
    public Map getExtendedData() {
        if (extendedData == null) {
            extendedData = new HashMap();
        }
        return extendedData;
    }

    /**
     * Returns the type of the request. The type is often used as a quick way to
     * filter recognized Requests. Once the type is identified, the Request is
     * usually cast to a more specific subclass containing additional data.
     * 
     * @return the Request <i>type</i>
     */
    public Object getType() {
        return type;
    }

    /**
     * Sets the given map to be the new extended data (by reference) for this
     * request.
     * 
     * @param map
     *            The new map
     */
    public void setExtendedData(Map map) {
        extendedData = map;
    }

    /**
     * Sets the type of the Request.
     * 
     * @param type
     *            the Request type
     */
    public void setType(Object type) {
        this.type = type;
    }

}
