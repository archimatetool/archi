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

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;

import org.eclipse.gef.EditPartViewer;

/**
 * A adapter for an outline page containing a single EditPartViewer. This class
 * handles selection processing and widget focus. There is no need to manage
 * viewer lifecycle. When <code>dispose()</code> is called in the superclass,
 * the EditPartViewer will receive widget dispose callback, and perform any
 * necessary cleanup. This class is just an adapter.
 * 
 * @author hudsonr
 */
public class ContentOutlinePage extends org.eclipse.ui.part.Page implements
        org.eclipse.ui.views.contentoutline.IContentOutlinePage {

    private EditPartViewer viewer;
    private Control control;

    /**
     * Constructs a ContentOutlinePage for the given viewer.
     * 
     * @param viewer
     *            the viewer
     */
    public ContentOutlinePage(EditPartViewer viewer) {
        this.viewer = viewer;
    }

    /**
     * @see ISelectionProvider#addSelectionChangedListener(ISelectionChangedListener)
     */
    @Override
    public void addSelectionChangedListener(ISelectionChangedListener listener) {
        getViewer().addSelectionChangedListener(listener);
    }

    /**
     * Forwards the createControl request to the editpartviewer.
     * 
     * @see org.eclipse.ui.part.IPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createControl(Composite parent) {
        control = getViewer().createControl(parent);
    }

    /**
     * @see org.eclipse.ui.part.IPage#getControl()
     */
    @Override
    public Control getControl() {
        return control;
    }

    /**
     * Forwards selection request to the viewer.
     * 
     * @see org.eclipse.jface.viewers.ISelectionProvider#getSelection()
     */
    @Override
    public ISelection getSelection() {
        // $TODO when could this even happen?
        if (getViewer() == null)
            return StructuredSelection.EMPTY;
        return getViewer().getSelection();
    }

    /**
     * Returns the EditPartViewer
     * 
     * @return the viewer
     */
    protected EditPartViewer getViewer() {
        return viewer;
    }

    /**
     * @see ISelectionProvider#removeSelectionChangedListener(ISelectionChangedListener)
     */
    @Override
    public void removeSelectionChangedListener(
            ISelectionChangedListener listener) {
        getViewer().removeSelectionChangedListener(listener);
    }

    /**
     * Sets focus to a part in the page.
     */
    @Override
    public void setFocus() {
        if (getControl() != null)
            getControl().setFocus();
    }

    /**
     * @see ISelectionProvider#setSelection(org.eclipse.jface.viewers.ISelection)
     */
    @Override
    public void setSelection(ISelection selection) {
        if (getViewer() != null)
            getViewer().setSelection(selection);
    }

}
