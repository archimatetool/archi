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
package org.eclipse.gef.ui.actions;

import java.util.Collections;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Superclass for an action needing the current selection.
 */
public abstract class SelectionAction extends WorkbenchPartAction {
    private ISelectionProvider provider;

    /*
     * The current selection.
     */
    private ISelection selection;

    /**
     * Creates a <code>SelectionAction</code> and associates it with the given
     * editor.
     * 
     * @param part
     *            The workbench part associated with this action
     * @param style
     *            the style for this action
     */
    public SelectionAction(IWorkbenchPart part, int style) {
        super(part, style);
    }

    /**
     * Creates a <code>SelectionAction</code> and associates it with the given
     * workbench part.
     * 
     * @param part
     *            the workbench part
     */
    public SelectionAction(IWorkbenchPart part) {
        super(part);
    }

    /**
     * @see org.eclipse.gef.Disposable#dispose()
     */
    @Override
    public void dispose() {
        this.selection = StructuredSelection.EMPTY;
        super.dispose();
    }

    /**
     * Returns a <code>List</code> containing the currently selected objects.
     * 
     * @return A List containing the currently selected objects.
     */
    @SuppressWarnings("rawtypes")
    protected List getSelectedObjects() {
        if (!(getSelection() instanceof IStructuredSelection))
            return Collections.EMPTY_LIST;
        return ((IStructuredSelection) getSelection()).toList();
    }

    /**
     * Gets the current selection.
     * 
     * @return The current selection.
     */
    protected ISelection getSelection() {
        return selection;
    }

    /**
     * Called when the selection is changed.
     */
    protected void handleSelectionChanged() {
        refresh();
    }

    /**
     * Sets the current selection and calls on subclasses to handle the
     * selectionChanged event.
     * 
     * @param selection
     *            The new selection.
     */
    protected void setSelection(ISelection selection) {
        this.selection = selection;
        handleSelectionChanged();
    }

    /**
     * May be used to provide an alternative selection source other than the
     * workbench's selection service. Use of this method is optional. The
     * default value is <code>null</code>, in which case the selection is
     * obtained using the partsite's selection service.
     * 
     * @param provider
     *            <code>null</code> or a selection provider
     */
    public void setSelectionProvider(ISelectionProvider provider) {
        this.provider = provider;
    }

    /**
     * @see org.eclipse.gef.ui.actions.EditorPartAction#update()
     */
    @Override
    public void update() {
        if (provider != null)
            setSelection(provider.getSelection());
        else
            setSelection(getWorkbenchPart().getSite().getWorkbenchWindow()
                    .getSelectionService().getSelection());
    }

}
