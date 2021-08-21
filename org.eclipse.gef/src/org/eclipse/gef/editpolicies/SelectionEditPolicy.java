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
package org.eclipse.gef.editpolicies;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartListener;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;

/**
 * A GraphicalEditPolicy that is sensitive to the host's selection. The overall
 * selection of the host EditPart includes whether or not it has focus.
 * Subclasses will typically decorate the GraphicalEditPart with things like
 * selection handles and/or focus feedback.
 * <P>
 * This EditPolicy adds itself as an {@link EditPartListener} so that it can
 * observe selection. When selection or focus changes, the EditPolicy will
 * update itself and call the appropriate methods.
 * 
 * @author hudsonr
 * @since 2.0
 */
public abstract class SelectionEditPolicy extends
        org.eclipse.gef.editpolicies.GraphicalEditPolicy {

    private EditPartListener selectionListener;
    private int state = -1;
    boolean focus;

    /**
     * Extends activate to hook the appropriate listener and to initialize the
     * visual changes for representing selection/focus.
     * 
     * @see org.eclipse.gef.EditPolicy#activate()
     */
    @Override
    public void activate() {
        super.activate();
        addSelectionListener();
        setSelectedState(getHost().getSelected());
        setFocus(getHost().hasFocus());
    }

    /**
     * Adds an EditPartListener to the host to observe selection/focus changes.
     */
    protected void addSelectionListener() {
        selectionListener = new EditPartListener.Stub() {
            @Override
            public void selectedStateChanged(EditPart part) {
                setSelectedState(part.getSelected());
                setFocus(part.hasFocus());
            }
        };
        getHost().addEditPartListener(selectionListener);
    }

    /**
     * Extends deactivate to unhook the seleciton listener and to remove the
     * visual changes for representing selection/focus.
     * 
     * @see org.eclipse.gef.EditPolicy#deactivate()
     */
    @Override
    public void deactivate() {
        removeSelectionListener();
        setSelectedState(EditPart.SELECTED_NONE);
        setFocus(false);
        super.deactivate();
    }

    /**
     * @see org.eclipse.gef.EditPolicy#getTargetEditPart(Request)
     */
    @Override
    public EditPart getTargetEditPart(Request request) {
        if (RequestConstants.REQ_SELECTION.equals(request.getType()))
            return getHost();
        return null;
    }

    /**
     * Override to hide focus
     * 
     * @see #showFocus()
     */
    protected void hideFocus() {
    }

    /**
     * Override to hide selection
     */
    protected abstract void hideSelection();

    /**
     * Removes the EditPartListener used to observe selection
     */
    protected void removeSelectionListener() {
        getHost().removeEditPartListener(selectionListener);
    }

    /**
     * Sets the internal focus value. This method is called automatically by the
     * listener. If the focus value is changed, either {@link #showFocus()} or
     * {@link #hideFocus()} will be called.
     * 
     * @param value
     *            <code>true</code> if the EditPolicy should show focus
     */
    protected void setFocus(boolean value) {
        if (focus == value)
            return;
        focus = value;
        if (focus)
            showFocus();
        else
            hideFocus();
    }

    /**
     * Sets the internal selection value. This method is called automatically by
     * the listener. If the selection value is changed, the appropriate method
     * is called to show the specified selection type.
     * 
     * @param type
     *            the type of selection the EditPolicy should display
     */
    protected void setSelectedState(int type) {
        if (state == type)
            return;
        state = type;
        if (type == EditPart.SELECTED_PRIMARY)
            showPrimarySelection();
        else if (type == EditPart.SELECTED)
            showSelection();
        else
            hideSelection();
    }

    /**
     * Override to show focus.
     * 
     * @see #hideFocus()
     */
    protected void showFocus() {
    }

    /**
     * Calls {@link #showSelection()} by default. Override to distinguish
     * between primary and normal selection.
     */
    protected void showPrimarySelection() {
        showSelection();
    }

    /**
     * Override to show selection
     */
    protected abstract void showSelection();
}
