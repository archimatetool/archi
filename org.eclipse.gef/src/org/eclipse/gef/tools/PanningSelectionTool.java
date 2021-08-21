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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.geometry.Point;


/**
 * A subclass of the SelectionTool that allows panning by holding down the space
 * bar.
 */
public class PanningSelectionTool extends SelectionTool {

    private boolean isSpaceBarDown = false;
    private Point viewLocation;

    /**
     * The state to indicate that the space bar has been pressed but no drag has
     * been initiated.
     */
    protected static final int PAN = SelectionTool.MAX_STATE << 1;

    /**
     * The state to indicate that a pan is in progress.
     */
    protected static final int PAN_IN_PROGRESS = PAN << 1;

    /** Max state */
    protected static final int MAX_STATE = PAN_IN_PROGRESS;

    /**
     * Returns <code>true</code> if spacebar condition was accepted.
     * 
     * @param e
     *            the key event
     * @return true if the space bar was the key event.
     */
    protected boolean acceptSpaceBar(KeyEvent e) {
        return (e.character == ' ' && (e.stateMask & SWT.MODIFIER_MASK) == 0);
    }

    /**
     * @see org.eclipse.gef.tools.AbstractTool#getDebugName()
     */
    @Override
    protected String getDebugName() {
        return "Panning Tool";//$NON-NLS-1$
    }

    /**
     * @see org.eclipse.gef.tools.AbstractTool#getDebugNameForState(int)
     */
    @Override
    protected String getDebugNameForState(int state) {
        if (state == PAN)
            return "Pan Initial"; //$NON-NLS-1$
        else if (state == PAN_IN_PROGRESS)
            return "Pan In Progress"; //$NON-NLS-1$
        return super.getDebugNameForState(state);
    }

    /**
     * Returns the cursor used under normal conditions.
     * 
     * @see #setDefaultCursor(Cursor)
     * @return the default cursor
     */
    @Override
    protected Cursor getDefaultCursor() {
        if (isInState(PAN | PAN_IN_PROGRESS))
            return Cursors.HAND;
        return super.getDefaultCursor();
    }

    /**
     * @see org.eclipse.gef.tools.SelectionTool#handleButtonDown(int)
     */
    @Override
    protected boolean handleButtonDown(int which) {
        if (which == 1
                && getCurrentViewer().getControl() instanceof FigureCanvas
                && stateTransition(PAN, PAN_IN_PROGRESS)) {
            viewLocation = ((FigureCanvas) getCurrentViewer().getControl())
                    .getViewport().getViewLocation();
            return true;
        }
        return super.handleButtonDown(which);
    }

    /**
     * @see org.eclipse.gef.tools.SelectionTool#handleButtonUp(int)
     */
    @Override
    protected boolean handleButtonUp(int which) {
        if (which == 1 && isSpaceBarDown
                && stateTransition(PAN_IN_PROGRESS, PAN))
            return true;
        else if (which == 1 && stateTransition(PAN_IN_PROGRESS, STATE_INITIAL)) {
            refreshCursor();
            return true;
        }

        return super.handleButtonUp(which);
    }

    /**
     * @see org.eclipse.gef.tools.AbstractTool#handleDrag()
     */
    @Override
    protected boolean handleDrag() {
        if (isInState(PAN_IN_PROGRESS)
                && getCurrentViewer().getControl() instanceof FigureCanvas) {
            FigureCanvas canvas = (FigureCanvas) getCurrentViewer()
                    .getControl();
            canvas.scrollTo(viewLocation.x - getDragMoveDelta().width,
                    viewLocation.y - getDragMoveDelta().height);
            return true;
        } else {
            return super.handleDrag();
        }
    }

    /**
     * @see org.eclipse.gef.tools.SelectionTool#handleFocusLost()
     */
    @Override
    protected boolean handleFocusLost() {
        if (isInState(PAN | PAN_IN_PROGRESS)) {
            setState(STATE_INITIAL);
            refreshCursor();
            return true;
        }
        return super.handleFocusLost();
    }

    /**
     * @see org.eclipse.gef.tools.SelectionTool#handleKeyDown(org.eclipse.swt.events.KeyEvent)
     */
    @Override
    protected boolean handleKeyDown(KeyEvent e) {
        if (acceptSpaceBar(e)) {
            isSpaceBarDown = true;
            if (stateTransition(STATE_INITIAL, PAN))
                refreshCursor();
            return true;
        } else {
            if (stateTransition(PAN, STATE_INITIAL)) {
                refreshCursor();
                isSpaceBarDown = false;
                return true;
            } else if (isInState(PAN_IN_PROGRESS))
                isSpaceBarDown = false;
        }

        return super.handleKeyDown(e);
    }

    /**
     * @see org.eclipse.gef.tools.SelectionTool#handleKeyUp(org.eclipse.swt.events.KeyEvent)
     */
    @Override
    protected boolean handleKeyUp(KeyEvent e) {
        if (acceptSpaceBar(e)) {
            isSpaceBarDown = false;
            if (stateTransition(PAN, STATE_INITIAL))
                refreshCursor();
            return true;
        }

        return super.handleKeyUp(e);
    }

}