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
package org.eclipse.gef.requests;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;

/**
 * A Request from multiple EditParts.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class GroupRequest extends org.eclipse.gef.Request {

    List parts;

    /**
     * Creates a GroupRequest with the given type.
     * 
     * @param type
     *            The type of Request.
     */
    public GroupRequest(Object type) {
        setType(type);
    }

    /**
     * Default constructor.
     */
    public GroupRequest() {
    }

    /**
     * Returns a List containing the EditParts making this Request.
     * 
     * @return A List containing the EditParts making this Request.
     */
    public List getEditParts() {
        return parts;
    }

    /**
     * Sets the EditParts making this Request to the given List.
     * 
     * @param list
     *            The List of EditParts.
     */
    public void setEditParts(List list) {
        parts = list;
    }

    /**
     * A helper method to set the given EditPart as the requester.
     * 
     * @param part
     *            The EditPart making the request.
     */
    public void setEditParts(EditPart part) {
        parts = new ArrayList(1);
        parts.add(part);
    }

}
