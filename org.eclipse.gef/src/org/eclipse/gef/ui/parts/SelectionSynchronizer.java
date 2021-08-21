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
package org.eclipse.gef.ui.parts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;

/**
 * A utility for synchronizing the selection of multiple EditPartViewers. This
 * class performs selection synchronization by taking the selection from one
 * viewer, and mapping it to the selection in another viewer. The mapping is
 * performed by matching the models of the selected EditParts from one viewer to
 * the EditParts with the same models in another. It can be customized by
 * overriding the {@link #convert(EditPartViewer, EditPart)} method.
 * 
 * @author hudsonr
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class SelectionSynchronizer implements ISelectionChangedListener {

    private List viewers = new ArrayList();
    private boolean isDispatching = false;
    private int disabled = 0;
    private EditPartViewer pendingSelection;

    /**
     * Adds a viewer to the set of synchronized viewers
     * 
     * @param viewer
     *            the viewer
     */
    public void addViewer(EditPartViewer viewer) {
        viewer.addSelectionChangedListener(this);
        viewers.add(viewer);
    }

    /**
     * Maps the given editpart from one viewer to an editpart in another viewer.
     * It returns <code>null</code> if there is no corresponding part. This
     * method can be overridden to provide custom mapping.
     * 
     * @param viewer
     *            the viewer being mapped to
     * @param part
     *            a part from another viewer
     * @return <code>null</code> or a corresponding editpart
     */
    protected EditPart convert(EditPartViewer viewer, EditPart part) {
        Object temp = viewer.getEditPartRegistry().get(part.getModel());
        EditPart newPart = null;
        if (temp != null) {
            newPart = (EditPart) temp;
        }
        return newPart;
    }

    /**
     * Removes the viewer from the set of synchronized viewers
     * 
     * @param viewer
     *            the viewer to remove
     */
    public void removeViewer(EditPartViewer viewer) {
        viewer.removeSelectionChangedListener(this);
        viewers.remove(viewer);
        if (pendingSelection == viewer)
            pendingSelection = null;
    }

    /**
     * Receives notification from one viewer, and maps selection to all other
     * members.
     * 
     * @param event
     *            the selection event
     */
    @Override
    public void selectionChanged(SelectionChangedEvent event) {
        if (isDispatching)
            return;
        EditPartViewer source = (EditPartViewer) event.getSelectionProvider();
        if (disabled > 0) {
            pendingSelection = source;
        } else {
            ISelection selection = event.getSelection();
            syncSelection(source, selection);
        }
    }

    /**
     * Synchronizes the given selection to all registered viewers except the one
     * serving as selectionSource, by delegating to
     * {@link #applySelection(EditPartViewer, ISelection)}.
     * 
     * @param selectionSource
     *            The viewer from which the selection originated.
     * @param selection
     *            The selection to apply to the other viewers.
     * @since 3.10
     */
    protected void syncSelection(EditPartViewer selectionSource,
            ISelection selection) {
        isDispatching = true;
        for (int i = 0; i < viewers.size(); i++) {
            if (viewers.get(i) != selectionSource) {
                EditPartViewer viewer = (EditPartViewer) viewers.get(i);
                applySelection(viewer, selection);
            }
        }
        isDispatching = false;
    }

    /**
     * Enables or disabled synchronization between viewers.
     * 
     * @since 3.1
     * @param value
     *            <code>true</code> if synchronization should occur
     */
    public void setEnabled(boolean value) {
        if (!value)
            disabled++;
        else if (--disabled == 0 && pendingSelection != null) {
            syncSelection(pendingSelection, pendingSelection.getSelection());
            pendingSelection = null;
        }
    }

    /**
     * Applies the given EditPart selection from another viewer to the given
     * viewer. It will first compute a new selection of {@link EditPart}s for
     * the given viewer by searching those that control the same model elements
     * as the {@link EditPart}s in the given selection (via
     * {@link #convert(EditPartViewer, EditPart)}), apply this new selection to
     * the given viewer, and reveal the last part in the new selection.
     * 
     * @param viewer
     *            The viewer to apply the given selection to.
     * @param selection
     *            The selection to apply, which has to be an
     *            {@link IStructuredSelection} of {@link EditPart}s of another
     *            viewer.
     * @since 3.10
     */
    protected void applySelection(EditPartViewer viewer, ISelection selection) {
        ArrayList result = new ArrayList();
        Iterator iter = ((IStructuredSelection) selection).iterator();
        while (iter.hasNext()) {
            EditPart part = convert(viewer, (EditPart) iter.next());
            if (part != null && part.isSelectable())
                result.add(part);
        }
        viewer.setSelection(new StructuredSelection(result));
        if (result.size() > 0)
            viewer.reveal((EditPart) result.get(result.size() - 1));
    }

}
