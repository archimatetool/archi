/*******************************************************************************
 * Copyright (c) 2006, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.gef;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.widgets.Control;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;

import org.eclipse.gef.ui.parts.AbstractEditPartViewer;

/**
 * Manages a viewer's selection model. Selection management includes
 * representing a form of selection which is available to clients of a viewer as
 * an ISelection. It also includes managing the notion of focus, which is
 * closely tied to the current selection. The selection manager provides the
 * mechanism for modifying the selection and any validation.
 * <P>
 * WARNING: Subclassing this class is considered experimental at this point.
 * 
 * @since 3.2
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class SelectionManager {

    private EditPart focusPart;
    private Runnable notifier;
    private List selection;
    private EditPartViewer viewer;

    /**
     * Default Constructor
     * 
     * @since 3.2
     */
    protected SelectionManager() {
    }

    /**
     * Creates the default implementation for a selection manager.
     * 
     * @return the default selection manager
     * @since 3.2
     */
    public static SelectionManager createDefault() {
        return new SelectionManager();
    }

    /**
     * Appends the <code>EditPart</code> to the current selection. The EditPart
     * becomes the new primary selection. Fires selection changed to all
     * {@link org.eclipse.jface.viewers.ISelectionChangedListener}s.
     * 
     * @param editpart
     *            the EditPart to append
     * @since 3.2
     */
    public void appendSelection(EditPart editpart) {
        if (editpart != getFocus()) {
            // Fix for 458416: adjust the focus through the viewer only (to give
            // AbstractEditPartViewer a change to update its focusPart field).
            // AbstractEditPartViewer#setFocus() should call back setFocus(null)
            // here, so both focus part values should stay in sync.
            viewer.setFocus(null);
        }
        if (!selection.isEmpty()) {
            EditPart primary = (EditPart) selection.get(selection.size() - 1);
            primary.setSelected(EditPart.SELECTED);
        }
        // if the editpart is already in the list, re-order it to be the last
        // one
        selection.remove(editpart);
        selection.add(editpart);
        editpart.setSelected(EditPart.SELECTED_PRIMARY);

        fireSelectionChanged();
    }

    /**
     * Removes the <code>EditPart</code> from the current selection.
     * 
     * @param editpart
     *            the editpart
     * @since 3.2
     */
    public void deselect(EditPart editpart) {
        editpart.setSelected(EditPart.SELECTED_NONE);

        selection.remove(editpart);
        if (!selection.isEmpty()) {
            // IMPORTANT: it may (temporarily) happen that the selection list
            // contains edit parts, which are not selectable (any more) when
            // this method gets called. Consider e.g. that the selectable state
            // of an edit part may bound to its activation state (by overwriting
            // isSelectable()); in this case, when deleting a selected edit part
            // and its primary selected child simultaneously, the parent edit
            // part may have already become non selectable, while not having
            // been deselected yet (because deselection is performed within
            // removeNotify() after deactivation), when the child edit part gets
            // deselected. Therefore, we do not simply choose the last edit part
            // in the list as the new primary selection, but reverse-search the
            // list for the first that is (still) selectable.
            for (int i = selection.size() - 1; i >= 0; i--) {
                EditPart primaryCandidate = (EditPart) selection.get(i);
                if (primaryCandidate.isSelectable()) {
                    primaryCandidate.setSelected(EditPart.SELECTED_PRIMARY);
                    break;
                }
            }
        }
        fireSelectionChanged();
    }

    /**
     * Deselects everything.
     * 
     * @since 3.2
     */
    public void deselectAll() {
        EditPart part;
        // Fix for 458416: adjust the focus through the viewer only (to give
        // AbstractEditPartViewer a change to update its focusPart field).
        // AbstractEditPartViewer#setFocus() should call back setFocus(null)
        // here, so both focus part values should stay in sync.
        viewer.setFocus(null);
        for (int i = 0; i < selection.size(); i++) {
            part = (EditPart) selection.get(i);
            part.setSelected(EditPart.SELECTED_NONE);
        }
        selection.clear();
        fireSelectionChanged();
    }

    /**
     * Causes the viewer to fire selection changed notification to all
     * listeners.
     * 
     * @since 3.2
     */
    protected final void fireSelectionChanged() {
        notifier.run();
    }

    /**
     * Returns the focus editpart.
     * 
     * @return the focus editpart
     * @since 3.2
     */
    protected EditPart getFocus() {
        return focusPart;
    }

    /**
     * Returns the current selection.
     * 
     * @return the selection
     * @since 3.2
     */
    public ISelection getSelection() {
        if (selection.isEmpty() && viewer.getContents() != null)
            return new StructuredSelection(viewer.getContents());
        return new StructuredSelection(selection);
    }

    /**
     * Returns <code>null</code> or the viewer whose selection is managed.
     * 
     * @return <code>null</code> or the viewer
     * @since 3.2
     */
    protected EditPartViewer getViewer() {
        return viewer;
    }

    /**
     * For internal use only. This API is subject to change.
     * 
     * @param control
     *            the control
     * @since 3.2
     */
    public void internalHookControl(Control control) {
    }

    /**
     * For internal use only. This API is subject to change.
     * 
     * @since 3.2
     */
    public void internalUninstall() {
    }

    /**
     * Provides a hook for when the viewer has been set.
     * 
     * @param viewer
     *            the viewer.
     * @since 3.2
     */
    protected void hookViewer(EditPartViewer viewer) {
    }

    /**
     * For internal use only.
     * 
     * @param viewer
     *            viewer
     * @param selection
     *            selection
     * @param notifier
     *            notifier
     * @since 3.2
     */
    public void internalInitialize(EditPartViewer viewer, List selection,
            Runnable notifier) {
        this.viewer = viewer;
        this.selection = selection;
        this.notifier = notifier;

        hookViewer(viewer);
    }

    /**
     * Sets the focus part.
     * 
     * @param part
     *            the focus part
     * @since 3.2
     */
    public void setFocus(EditPart part) {
        // Fix for 458416: AbstractEditPartViewer provides a protected,
        // deprecated focusPart field, which might get out of sync with the
        // focusPart here, if this method is called directly (and not though the
        // EditPartViewer#setFocus() delegation). We need to make sure it stays
        // synchronized even in this case, so we update the field value
        // accordingly
        if (viewer instanceof AbstractEditPartViewer) {
            try {
                Field focusPartField = AbstractEditPartViewer.class
                        .getDeclaredField("focusPart"); //$NON-NLS-1$
                focusPartField.setAccessible(true);
                if (focusPartField.get(viewer) != part) {
                    focusPartField.set(viewer, part);
                }
            } catch (Exception e) {
                throw new IllegalStateException(
                        "Workaround for bug #458416 became ineffective"); //$NON-NLS-1$
            }
        }
        if (focusPart == part)
            return;
        if (focusPart != null)
            focusPart.setFocus(false);
        focusPart = part;
        if (focusPart != null)
            focusPart.setFocus(true);
    }

    /**
     * Sets the selection.
     * 
     * @param newSelection
     *            the new selection
     * @since 3.2
     */
    public void setSelection(ISelection newSelection) {
        if (!(newSelection instanceof IStructuredSelection))
            return;

        List orderedSelection = ((IStructuredSelection) newSelection).toList();
        // Convert to HashSet to optimize performance.
        Collection hashset = new HashSet(orderedSelection);

        // Fix for 458416: adjust the focus through the viewer only (to give
        // AbstractEditPartViewer a change to update its focusPart field).
        // AbstractEditPartViewer#setFocus() should call back setFocus(null)
        // here, so both focus part values should stay in sync.
        viewer.setFocus(null);
        for (int i = 0; i < selection.size(); i++) {
            EditPart part = (EditPart) selection.get(i);
            if (!hashset.contains(part))
                part.setSelected(EditPart.SELECTED_NONE);
        }
        selection.clear();

        if (!orderedSelection.isEmpty()) {
            Iterator itr = orderedSelection.iterator();
            while (true) {
                EditPart part = (EditPart) itr.next();
                selection.add(part);
                if (!itr.hasNext()) {
                    part.setSelected(EditPart.SELECTED_PRIMARY);
                    break;
                }
                part.setSelected(EditPart.SELECTED);
            }
        }
        fireSelectionChanged();
    }

}
