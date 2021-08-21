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
package org.eclipse.draw2d;

import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleControlListener;
import org.eclipse.swt.accessibility.AccessibleListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;

/**
 * Listens to various SWT events and dispatches these events to interested
 * Draw2d objects.
 */
public abstract class EventDispatcher {

    /**
     * Combines {@link AccessibleControlListener} and {@link AccessibleListener}
     * . Implements
     * {@link AccessibleControlListener#getChild(AccessibleControlEvent)} to do
     * nothing.
     */
    public abstract static class AccessibilityDispatcher implements
            AccessibleControlListener, AccessibleListener {
        /** @see AccessibleControlListener#getChild(AccessibleControlEvent) */
        @Override
        public void getChild(AccessibleControlEvent e) {
        }
    }

    /**
     * Dispatches a focus gained event.
     * 
     * @param e
     *            the event
     */
    public abstract void dispatchFocusGained(FocusEvent e);

    /**
     * Dispatches a focus lost event.
     * 
     * @param e
     *            the event
     */
    public abstract void dispatchFocusLost(FocusEvent e);

    /**
     * Dispatches a key pressed event.
     * 
     * @param e
     *            the event
     */
    public abstract void dispatchKeyPressed(KeyEvent e);

    /**
     * Dispatches a key released event.
     * 
     * @param e
     *            the event
     */
    public abstract void dispatchKeyReleased(KeyEvent e);

    /**
     * Dispatches a key traversed event.
     * 
     * @param e
     *            the event
     */
    public abstract void dispatchKeyTraversed(TraverseEvent e);

    /**
     * Dispatches a mouse double clicked event.
     * 
     * @param me
     *            the event
     */
    public abstract void dispatchMouseDoubleClicked(MouseEvent me);

    /**
     * Dispatches a mouse entered event.
     * 
     * @param e
     *            the event
     */
    public abstract void dispatchMouseEntered(MouseEvent e);

    /**
     * Dispatches a mouse exited event.
     * 
     * @param e
     *            the event
     */
    public abstract void dispatchMouseExited(MouseEvent e);

    /**
     * Dispatches a mouse hover event.
     * 
     * @param me
     *            the event
     */
    public abstract void dispatchMouseHover(MouseEvent me);

    /**
     * Dispatches a moved event event.
     * 
     * @param me
     *            the event
     */
    public abstract void dispatchMouseMoved(MouseEvent me);

    /**
     * Dispatches a mouse pressed event.
     * 
     * @param me
     *            the event
     */
    public abstract void dispatchMousePressed(MouseEvent me);

    /**
     * Dispatches a mouse released event.
     * 
     * @param me
     *            the event
     */
    public abstract void dispatchMouseReleased(MouseEvent me);

    /**
     * Dispatches a MouseWheel event. Does nothing by default.
     * 
     * @param event
     *            the SWT event
     * @since 3.1
     */
    public void dispatchMouseWheelScrolled(Event event) {
    }

    /**
     * Returns the AccessibilityDispatcher.
     * 
     * @return the AccessibilityDispatcher
     */
    protected abstract AccessibilityDispatcher getAccessibilityDispatcher();

    /**
     * Returns the IFigure that currently has focus.
     * 
     * @return the IFigure that currently has focus
     * @since 3.6
     */
    public abstract IFigure getFocusOwner();

    /**
     * @return whether events are captured by a figure
     */
    public abstract boolean isCaptured();

    /**
     * Releases capture initiated by {@link #setCapture(IFigure)}.
     */
    protected abstract void releaseCapture();

    /**
     * Requests focus for the given figure.
     * 
     * @param fig
     *            the figure requesting focus
     */
    public abstract void requestFocus(IFigure fig);

    /**
     * Requests focus to be removed from the given figure.
     * 
     * @param fig
     *            the figure requesting focus be removed
     */
    public abstract void requestRemoveFocus(IFigure fig);

    /**
     * Sets capture to the given figure. All subsequent events will be sent to
     * the given figure until {@link #releaseCapture()} is called.
     * 
     * @param figure
     *            the figure capturing the events
     */
    protected abstract void setCapture(IFigure figure);

    /**
     * Sets the contol associated with this event dispatcher.
     * 
     * @param control
     *            the control
     */
    public abstract void setControl(Control control);

    /**
     * Sets the root figure for this dispatcher.
     * 
     * @param figure
     *            the root figure
     */
    public abstract void setRoot(IFigure figure);

    /**
     * Updates the cursor.
     */
    protected abstract void updateCursor();

}
