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

import org.eclipse.jface.viewers.CellEditor;

import org.eclipse.gef.RequestConstants;

/**
 * A request to perform direct editing on the receiver of the Request.
 * 
 * @author hudsonr
 * @since 2.0
 */
public class DirectEditRequest extends LocationRequest {

    private Object feature;
    private CellEditor celleditor;

    /**
     * Constructor for DirectEditRequest.
     */
    public DirectEditRequest() {
        super(RequestConstants.REQ_DIRECT_EDIT);
    }

    /**
     * Constructor for DirectEditRequest.
     * 
     * @param type
     *            the type
     */
    public DirectEditRequest(Object type) {
        super(type);
    }

    /**
     * If the EditPart supports direct editing of multiple features, this
     * parameter can be used to discriminate among them.
     * 
     * @return the direct edit feature
     */
    public Object getDirectEditFeature() {
        return feature;
    }

    /**
     * Returns the cell editor used to perform the direct edit.
     * 
     * @return the cell editor
     */
    public CellEditor getCellEditor() {
        return celleditor;
    }

    /**
     * Sets the cell editor to be used when direct editing.
     * 
     * @param celleditor
     *            the cell editor
     */
    public void setCellEditor(CellEditor celleditor) {
        this.celleditor = celleditor;
    }

    /**
     * Sets the direct edit feature.
     * 
     * @param feature
     *            the direct edit feature
     * @see #getDirectEditFeature()
     */
    public void setDirectEditFeature(Object feature) {
        this.feature = feature;
    }

}
