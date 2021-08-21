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

import java.util.List;

import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.Transfer;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.PaletteTemplateEntry;

/**
 * Allows a single {@link PaletteTemplateEntry PaletteTemplateEntry} to be
 * dragged from an EditPartViewer. The PaletteTemplateEntry's <i>template</i>
 * object is the data that is being transfered to the <code>DropTarget</code>.
 * 
 * @since 2.1
 * @author Eric Bordeau
 */
public class TemplateTransferDragSourceListener extends
        AbstractTransferDragSourceListener {

    /**
     * @deprecated Use the constructor without the transfer specified.
     * @param viewer
     *            viewer
     * @param xfer
     *            xfer
     */
    public TemplateTransferDragSourceListener(EditPartViewer viewer,
            Transfer xfer) {
        super(viewer, xfer);
    }

    /**
     * Constructs a new listener for the specified EditPartViewer. The provided
     * Viewer should be one that is displaying a Palette. The
     * TemplateTransferDragSourceListener will only be enabled when a single
     * EditPart is selected, and the EditPart's model is a
     * {@link PaletteTemplateEntry}.
     * 
     * @param viewer
     *            the EditPartViewer that is the drag source
     */
    public TemplateTransferDragSourceListener(EditPartViewer viewer) {
        super(viewer, TemplateTransfer.getInstance());
    }

    /**
     * @see AbstractTransferDragSourceListener#dragFinished(DragSourceEvent)
     */
    @Override
    public void dragFinished(DragSourceEvent event) {
        TemplateTransfer.getInstance().setTemplate(null);
    }

    /**
     * Get the <i>template</i> from the selected {@link PaletteTemplateEntry}
     * and sets it as the event data to be dropped.
     * 
     * @param event
     *            the DragSourceEvent
     */
    @Override
    public void dragSetData(DragSourceEvent event) {
        event.data = getTemplate();
    }

    /**
     * Cancels the drag if the selected item does not represent a
     * PaletteTemplateEntry.
     * 
     * @see org.eclipse.swt.dnd.DragSourceListener#dragStart(DragSourceEvent)
     */
    @Override
    public void dragStart(DragSourceEvent event) {
        Object template = getTemplate();
        if (template == null)
            event.doit = false;
        TemplateTransfer.getInstance().setTemplate(template);
    }

    /**
     * A helper method that returns <code>null</code> or the <i>template</i>
     * Object from the currently selected EditPart.
     * 
     * @return the template
     */
    @SuppressWarnings("rawtypes")
    protected Object getTemplate() {
        List selection = getViewer().getSelectedEditParts();
        if (selection.size() == 1) {
            EditPart editpart = (EditPart) getViewer().getSelectedEditParts()
                    .get(0);
            Object model = editpart.getModel();
            if (model instanceof PaletteTemplateEntry)
                return ((PaletteTemplateEntry) model).getTemplate();
            if (model instanceof CombinedTemplateCreationEntry)
                return ((CombinedTemplateCreationEntry) model).getTemplate();
        }
        return null;
    }

}
