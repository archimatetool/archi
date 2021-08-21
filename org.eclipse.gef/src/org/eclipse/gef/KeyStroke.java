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
package org.eclipse.gef;

import org.eclipse.swt.events.KeyEvent;

/**
 * Encapsulates a Keyboard gesture (press or release) from the User. A KeyStroke
 * is matched to a KeyEvent based the {@link KeyEvent#stateMask},
 * {@link KeyEvent#keyCode} or {@link KeyEvent#character}, and whether that
 * KeyEvent was dispatched as a result of a release or press by the User.
 */
public class KeyStroke {

    /**
     * Constructs a KeyStroke that will match the given
     * {@link KeyEvent#character} and {@link KeyEvent#stateMask} during a press
     * event.
     * 
     * @param character
     *            the character to match
     * @param stateMask
     *            the stateMask to match
     * @return a new KeyStroke
     */
    public static KeyStroke getPressed(char character, int stateMask) {
        return new KeyStroke(character, stateMask, true);
    }

    /**
     * Constructs a KeyStroke that will match the given {@link KeyEvent#keyCode}
     * and {@link KeyEvent#stateMask} during a press event.
     * 
     * @param keyCode
     *            the keyCode to match
     * @param stateMask
     *            the stateMask to match
     * @return a new KeyStroke
     */
    public static KeyStroke getPressed(int keyCode, int stateMask) {
        return new KeyStroke(keyCode, stateMask, true);
    }

    /**
     * Constructs a KeyStroke that will match the given
     * {@link KeyEvent#character}, {@link KeyEvent#keyCode}, and
     * {@link KeyEvent#stateMask} during a press event.
     * 
     * @param character
     *            the character to match
     * @param keyCode
     *            the keyCode to match
     * @param stateMask
     *            the stateMask to match
     * @return a new KeyStroke
     */
    public static KeyStroke getPressed(char character, int keyCode,
            int stateMask) {
        return new KeyStroke(character, keyCode, stateMask, true);
    }

    /**
     * Constructs a KeyStroke that will match the given
     * {@link KeyEvent#character} and {@link KeyEvent#stateMask} during a
     * release event.
     * 
     * @param character
     *            the character to match
     * @param stateMask
     *            the stateMask to match
     * @return a new KeyStroke
     */
    public static KeyStroke getReleased(char character, int stateMask) {
        return new KeyStroke(character, stateMask, false);
    }

    /**
     * Constructs a KeyStroke that will match the given {@link KeyEvent#keyCode}
     * and {@link KeyEvent#stateMask} during a release event.
     * 
     * @param keyCode
     *            the keyCode to match
     * @param stateMask
     *            the stateMask to match
     * @return a new KeyStroke
     */
    public static KeyStroke getReleased(int keyCode, int stateMask) {
        return new KeyStroke(keyCode, stateMask, false);
    }

    /**
     * Constructs a KeyStroke that will match the given
     * {@link KeyEvent#character}, {@link KeyEvent#keyCode}, and
     * {@link KeyEvent#stateMask} during a release event.
     * 
     * @param character
     *            the character to match
     * @param keyCode
     *            the keyCode to match
     * @param stateMask
     *            the stateMask to match
     * @return a new KeyStroke
     */
    public static KeyStroke getReleased(char character, int keyCode,
            int stateMask) {
        return new KeyStroke(character, keyCode, stateMask, false);
    }

    private int stateMask;
    private char character;
    private boolean onPressed;
    private int keyCode;

    /**
     * Creates a KeyStroke for the specified KeyEvent and pressed value.
     * 
     * @param event
     *            The KeyEvent
     * @param pressed
     *            true if the KeyStroke is for a press event
     */
    KeyStroke(KeyEvent event, boolean pressed) {
        onPressed = pressed;
        stateMask = event.stateMask;
        character = event.character;
        keyCode = event.keyCode;
    }

    KeyStroke(int keyCode, int stateMask, boolean onPressed) {
        this.keyCode = keyCode;
        this.stateMask = stateMask;
        this.onPressed = onPressed;
    }

    KeyStroke(char character, int stateMask, boolean onPressed) {
        this.character = character;
        this.stateMask = stateMask;
        this.onPressed = onPressed;
    }

    KeyStroke(char character, int keyCode, int stateMask, boolean onPressed) {
        this.character = character;
        this.keyCode = keyCode;
        this.stateMask = stateMask;
        this.onPressed = onPressed;
    }

    /**
     * @return true iff the Object is an equivalent KeyStroke
     * @param obj
     *            the Object being compared
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof KeyStroke) {
            KeyStroke stroke = (KeyStroke) obj;
            return stroke.character == character && stroke.keyCode == keyCode
                    && stroke.onPressed == onPressed
                    && stroke.stateMask == stateMask;
        }
        return false;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (stateMask + 1) * ((character ^ keyCode) + 1) // One of these is
                                                                // always Zero.
                + (onPressed ? 0 : 32);
    }

}
