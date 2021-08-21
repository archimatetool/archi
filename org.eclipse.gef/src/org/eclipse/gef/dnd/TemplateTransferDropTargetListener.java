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
package org.eclipse.gef.dnd;

import org.eclipse.swt.dnd.DND;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.requests.SimpleFactory;

/**
 * Performs a native Drop using the {@link TemplateTransfer}. The Drop is
 * performed by using a {@link CreateRequest} to obtain a <code>Command</code>
 * from the targeted <code>EditPart</code>.
 * <P>
 * This class is <code>abstract</code>. Subclasses are responsible for providing
 * the appropriate <code>Factory</code> object based on the template that is
 * being dragged.
 * 
 * @since 2.1
 * @author Eric Bordeau
 */
public class TemplateTransferDropTargetListener extends
        AbstractTransferDropTargetListener {

    /**
     * Constructs a listener on the specified viewer.
     * 
     * @param viewer
     *            the EditPartViewer
     */
    public TemplateTransferDropTargetListener(EditPartViewer viewer) {
        super(viewer, TemplateTransfer.getInstance());
    }

    /**
     * @see org.eclipse.gef.dnd.AbstractTransferDropTargetListener#createTargetRequest()
     */
    @Override
    protected Request createTargetRequest() {
        // Look at the data on templatetransfer.
        // Create factory
        CreateRequest request = new CreateRequest();
        request.setFactory(getFactory(TemplateTransfer.getInstance()
                .getTemplate()));
        return request;
    }

    /**
     * A helper method that casts the target Request to a CreateRequest.
     * 
     * @return CreateRequest
     */
    protected final CreateRequest getCreateRequest() {
        return ((CreateRequest) getTargetRequest());
    }

    /**
     * Returns the appropriate Factory object to be used for the specified
     * template. This Factory is used on the CreateRequest that is sent to the
     * target EditPart.
     * 
     * @param template
     *            the template Object
     * @return a Factory
     */
    @SuppressWarnings("rawtypes")
    protected CreationFactory getFactory(Object template) {
        if (template instanceof CreationFactory) {
            return ((CreationFactory) template);
        } else if (template instanceof Class) {
            return new SimpleFactory((Class) template);
        } else
            return null;
    }

    /**
     * The purpose of a template is to be copied. Therefore, the drop operation
     * can't be anything but <code>DND.DROP_COPY</code>.
     * 
     * @see AbstractTransferDropTargetListener#handleDragOperationChanged()
     */
    @Override
    protected void handleDragOperationChanged() {
        getCurrentEvent().detail = DND.DROP_COPY;
        super.handleDragOperationChanged();
    }

    /**
     * The purpose of a template is to be copied. Therefore, the Drop operation
     * is set to <code>DND.DROP_COPY</code> by default.
     * 
     * @see org.eclipse.gef.dnd.AbstractTransferDropTargetListener#handleDragOver()
     */
    @Override
    protected void handleDragOver() {
        getCurrentEvent().detail = DND.DROP_COPY;
        getCurrentEvent().feedback = DND.FEEDBACK_SCROLL | DND.FEEDBACK_EXPAND;
        super.handleDragOver();
    }

    /**
     * Overridden to select the created object.
     * 
     * @see org.eclipse.gef.dnd.AbstractTransferDropTargetListener#handleDrop()
     */
    @Override
    protected void handleDrop() {
        super.handleDrop();
        selectAddedObject();
    }

    private void selectAddedObject() {
        Object model = getCreateRequest().getNewObject();
        if (model == null)
            return;
        EditPartViewer viewer = getViewer();
        viewer.getControl().forceFocus();
        Object editpart = viewer.getEditPartRegistry().get(model);
        if (editpart instanceof EditPart) {
            // Force a layout first.
            getViewer().flush();
            viewer.select((EditPart) editpart);
        }
    }

    /**
     * Assumes that the target request is a {@link CreateRequest}.
     */
    @Override
    protected void updateTargetRequest() {
        CreateRequest request = getCreateRequest();
        request.setLocation(getDropLocation());
    }

}
