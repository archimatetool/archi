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

import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.NumberFormat;

import org.eclipse.swt.widgets.Text;

/**
 * This is an internal class used for debugging
 * 
 * @deprecated in 3.1 This class will be removed in future releases.
 */
public final class GEF {

    static final String TAB = "  ";//$NON-NLS-1$

    static Text text;
    static int msgCount;
    static int tab;
    static NumberFormat formatter = new DecimalFormat();

    /**
     * @deprecated
     */
    public static boolean DebugTools = false;
    /**
     * @deprecated
     */
    public static boolean DebugEvents = false;
    /**
     * @deprecated
     */
    public static boolean DebugEditParts = false;
    /**
     * @deprecated
     */
    public static boolean DebugPainting = false;
    /**
     * @deprecated
     */
    public static boolean DebugFeedback = false;
    /**
     * @deprecated
     */
    public static boolean GlobalDebug = false;
    /**
     * @deprecated
     */
    public static boolean DebugToolStates = false;
    /**
     * @deprecated
     */
    public static boolean DebugDND = false;

    /**
     * Clears the trace console if active
     * 
     * @since 1.0
     */
    public static void clearConsole() {
        if (text == null)
            return;
        text.setText("");//$NON-NLS-1$
    }

    /**
     * Sets a text control to be used as a console.
     * 
     * @since 1.0
     * @param textBox
     *            the text control for streaming
     */
    public static void setConsole(Text textBox) {
        msgCount = 0;
        formatter.setMinimumIntegerDigits(2);
        formatter.setMaximumFractionDigits(0);
        text = textBox;
    }

    /**
     * decrements the tracing indentation
     * 
     * @since 2.0
     */
    public static void debugPop() {
        tab--;
    }

    /**
     * Prints the given string to a trace window and increments indentation.
     * 
     * @since 2.0
     * @param heading
     *            the message describing the indented text to follow
     */
    public static void debugPush(String heading) {
        debug(heading);
        tab++;
    }

    /**
     * Prints the given message to a trace window if available.
     * 
     * @since 1.0
     * @param message
     *            a debug message
     */
    public static void debug(String message) {
        String lineNumber = formatter.format(new Long(msgCount++));
        msgCount %= 100;
        String indent = "";//$NON-NLS-1$
        for (int i = 0; i < tab; i++)
            indent += TAB;
        if (text != null)
            text.append('\n' + lineNumber + '\t' + indent + message);
    }

}
