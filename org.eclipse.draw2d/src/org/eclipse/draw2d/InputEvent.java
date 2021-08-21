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

import org.eclipse.swt.SWT;

/**
 * The base class for Draw2d events.
 */
public abstract class InputEvent extends java.util.EventObject {

    private int state;

    private boolean consumed = false;

    /**
     * @deprecated Use {@link SWT#ALT} instead.
     */
    public static final int ALT = SWT.ALT;
    /**
     * @deprecated Use {@link SWT#CONTROL} instead.
     */
    public static final int CONTROL = SWT.CONTROL;
    /**
     * @deprecated Use {@link SWT#SHIFT} instead.
     */
    public static final int SHIFT = SWT.SHIFT;
    /**
     * @deprecated Use {@link SWT#BUTTON1} instead.
     */
    public static final int BUTTON1 = SWT.BUTTON1;
    /**
     * @deprecated Use {@link SWT#BUTTON2} instead.
     */
    public static final int BUTTON2 = SWT.BUTTON2;
    /**
     * @deprecated Use {@link SWT#BUTTON3} instead.
     */
    public static final int BUTTON3 = SWT.BUTTON3;
    /**
     * @deprecated Use {@link SWT#BUTTON4} instead.
     */
    public static final int BUTTON4 = SWT.BUTTON4;
    /**
     * @deprecated Use {@link SWT#BUTTON5} instead.
     */
    public static final int BUTTON5 = SWT.BUTTON5;
    /**
     * A bitwise OR'ing of {@link #BUTTON1}, {@link #BUTTON2}, {@link #BUTTON3},
     * {@link #BUTTON4} and {@link #BUTTON5}
     * 
     * @deprecated Use {@link SWT#BUTTON_MASK} instead.
     */
    public static final int ANY_BUTTON = SWT.BUTTON_MASK;

    /**
     * Constructs a new InputEvent.
     * 
     * @param dispatcher
     *            the event dispatcher
     * @param source
     *            the source of the event
     * @param state
     *            the state of the keyboard modifier and mouse button mask.
     * 
     * @see org.eclipse.swt.SWT#MODIFIER_MASK
     * @see org.eclipse.swt.SWT#BUTTON_MASK
     */
    public InputEvent(EventDispatcher dispatcher, IFigure source, int state) {
        super(source);
        this.state = state;
    }

    /**
     * Marks this event as consumed so that it doesn't get passed on to other
     * listeners.
     */
    public void consume() {
        consumed = true;
    }

    /**
     * Returns the event state mask, which is a bitwise OR'ing of the keyboard
     * modifier and the mouse button mask.
     * 
     * @see org.eclipse.swt.SWT#MODIFIER_MASK
     * @see org.eclipse.swt.SWT#BUTTON_MASK
     * 
     * @return the state
     */
    public int getState() {
        return state;
    }

    /**
     * @return whether this event has been consumed.
     */
    public boolean isConsumed() {
        return consumed;
    }

}
