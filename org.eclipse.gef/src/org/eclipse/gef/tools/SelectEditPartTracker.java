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
package org.eclipse.gef.tools;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;

import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.gef.requests.SelectionRequest;

/**
 * DragTracker used to select, edit, and open {@link org.eclipse.gef.EditPart
 * EditParts}.
 */
public class SelectEditPartTracker extends TargetingTool implements DragTracker {

    /** Flag to indicate selection has been performed. */
    protected static final int FLAG_SELECTION_PERFORMED = TargetingTool.MAX_FLAG << 1;
    private static final int FLAG_ENABLE_DIRECT_EDIT = TargetingTool.MAX_FLAG << 2;
    /** Max flag */
    protected static final int MAX_FLAG = FLAG_ENABLE_DIRECT_EDIT;

    private EditPart editpart;

    /**
     * Constructs a new SelectEditPartTracker with the given edit part as the
     * source.
     * 
     * @param owner
     *            the source edit part
     */
    public SelectEditPartTracker(EditPart owner) {
        setSourceEditPart(owner);
    }

    /**
     * @see org.eclipse.gef.tools.AbstractTool#calculateCursor()
     */
    @Override
    protected Cursor calculateCursor() {
        if (isInState(STATE_INITIAL | STATE_DRAG | STATE_ACCESSIBLE_DRAG))
            return getDefaultCursor();
        return super.calculateCursor();
    }

    /**
     * @see org.eclipse.gef.tools.AbstractTool#getCommandName()
     */
    @Override
    protected String getCommandName() {
        return "Select Tracker";//$NON-NLS-1$
    }

    /**
     * @see org.eclipse.gef.tools.AbstractTool#getDebugName()
     */
    @Override
    protected String getDebugName() {
        return "Select Tracker";//$NON-NLS-1$
    }

    /**
     * Returns the source edit part.
     * 
     * @return the source edit part
     */
    protected EditPart getSourceEditPart() {
        return editpart;
    }

    /**
     * Performs a conditional selection if needed (if right or left mouse button
     * have been pressed) and goes into the drag state. If any other button has
     * been pressed, the tool goes into the invalid state.
     * 
     * @see org.eclipse.gef.tools.AbstractTool#handleButtonDown(int)
     */
    @Override
    protected boolean handleButtonDown(int button) {
        if ((button == 3 || button == 1) && isInState(STATE_INITIAL))
            performConditionalSelection();

        if (button != 1) {
            setState(STATE_INVALID);
            if (button == 3)
                setState(STATE_TERMINAL);
            handleInvalidInput();
        } else
            stateTransition(STATE_INITIAL, STATE_DRAG);
        return true;
    }

    /**
     * If in the drag state, the tool selects the source edit part. If the edit
     * part was already selected, {@link #performDirectEdit()} is called. If the
     * edit part is newly selected and not completely visible,
     * {@link EditPartViewer#reveal(EditPart)} is called to show the selected
     * edit part.
     * 
     * @see org.eclipse.gef.tools.AbstractTool#handleButtonUp(int)
     */
    @Override
    protected boolean handleButtonUp(int button) {
        if (isInState(STATE_DRAG)) {
            performSelection();
            if (getFlag(FLAG_ENABLE_DIRECT_EDIT))
                performDirectEdit();
            if (button == 1
                    && getSourceEditPart().getSelected() != EditPart.SELECTED_NONE)
                getCurrentViewer().reveal(getSourceEditPart());
            setState(STATE_TERMINAL);
            return true;
        }
        return false;
    }

    /**
     * Calls {@link #performOpen()} if the double click was with mouse button 1.
     * 
     * @see org.eclipse.gef.tools.AbstractTool#handleDoubleClick(int)
     */
    @Override
    protected boolean handleDoubleClick(int button) {
        setFlag(FLAG_ENABLE_DIRECT_EDIT, false);
        if (button == 1) {
            // Prevent selection from happening later on mouse up
            setFlag(FLAG_SELECTION_PERFORMED, true);
            performOpen();
        }
        return true;
    }

    /**
     * @see org.eclipse.gef.tools.AbstractTool#handleDragStarted()
     */
    @Override
    protected boolean handleDragStarted() {
        return stateTransition(STATE_DRAG, STATE_DRAG_IN_PROGRESS);
    }

    /**
     * Returns <code>true</code> if selection has already occured.
     * 
     * @return <code>true</code> if selection has occured
     */
    protected boolean hasSelectionOccurred() {
        return getFlag(FLAG_SELECTION_PERFORMED);
    }

    /**
     * Calls {@link #performSelection()} if the source is not selected. If the
     * source is selected and there are no modifier keys pressed (i.e. the user
     * isn't selecting multiple edit parts or deselecting edit parts), sets the
     * direct edit flag so that when the mouse is released, a direct edit will
     * be performed.
     */
    protected void performConditionalSelection() {
        if (getSourceEditPart().getSelected() == EditPart.SELECTED_NONE)
            performSelection();
        else if (getCurrentInput().getModifiers() == 0)
            setFlag(FLAG_ENABLE_DIRECT_EDIT, true);
    }

    /**
     * Creates a {@link DirectEditRequest} and sends it to a
     * DelayedDirectEditHelper to allow the user to directly edit the edit part.
     */
    protected void performDirectEdit() {
        DirectEditRequest req = new DirectEditRequest();
        req.setLocation(getCurrentInput().getMouseLocation());
        new DelayedDirectEditHelper(getSourceEditPart().getViewer(), req,
                getSourceEditPart());
    }

    /**
     * Creates a {@link SelectionRequest} and sends it to the source edit part
     * via {@link EditPart#performRequest(Request)}. Possible uses are to open
     * the selected item in another editor or replace the current editor's
     * contents based on the selected item.
     */
    protected void performOpen() {
        SelectionRequest request = new SelectionRequest();
        request.setLocation(getLocation());
        request.setModifiers(getCurrentInput().getModifiers());
        request.setType(RequestConstants.REQ_OPEN);
        getSourceEditPart().performRequest(request);
    }

    /**
     * Performs the appropriate selection action based on the selection state of
     * the source and the modifiers (CTRL and SHIFT). If no modifier key is
     * pressed, the source will be set as the only selection. If the CTRL key is
     * pressed and the edit part is already selected, it will be deselected. If
     * the CTRL key is pressed and the edit part is not selected, it will be
     * appended to the selection set. If the SHIFT key is pressed, the source
     * will be appended to the selection.
     */
    @SuppressWarnings("rawtypes")
    protected void performSelection() {
        if (hasSelectionOccurred())
            return;
        setFlag(FLAG_SELECTION_PERFORMED, true);
        EditPartViewer viewer = getCurrentViewer();
        List selectedObjects = viewer.getSelectedEditParts();

        if (getCurrentInput().isModKeyDown(SWT.MOD1)) {
            if (selectedObjects.contains(getSourceEditPart()))
                viewer.deselect(getSourceEditPart());
            else
                viewer.appendSelection(getSourceEditPart());
        } else if (getCurrentInput().isShiftKeyDown())
            viewer.appendSelection(getSourceEditPart());
        else
            viewer.select(getSourceEditPart());
    }

    /**
     * @see org.eclipse.gef.tools.AbstractTool#resetFlags()
     */
    @Override
    protected void resetFlags() {
        super.resetFlags();
        setFlag(FLAG_SELECTION_PERFORMED, false);
        setFlag(FLAG_ENABLE_DIRECT_EDIT, false);
    }

    /**
     * Sets the source edit part.
     * 
     * @param part
     *            the source edit part
     */
    protected void setSourceEditPart(EditPart part) {
        this.editpart = part;
    }

}
