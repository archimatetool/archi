/*******************************************************************************
 * Copyright (c) 2004, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.gef.commands;

import java.util.EventObject;

/**
 * Instances of this class are sent whenever stack events occur. The type of
 * event can be determined by calling {@link #getDetail()}, and comparing the
 * return value to constants defined by {@link CommandStack}.
 * <P>
 * Warning: this class is not intended to be subclassed.
 * 
 * @since 3.1
 */
public class CommandStackEvent extends EventObject {

    private final Command command;
    private final int detail;

    /**
     * Constructs a new event instance. The stack specifies the source of the
     * event. If a command is relevant to the event context, one should be
     * specified. The detail indicates the type of event occurring.
     * 
     * @since 3.1
     * @param stack
     *            the command stack
     * @param c
     *            a command or <code>null</code>
     * @param detail
     *            an integer identifier
     */
    public CommandStackEvent(CommandStack stack, Command c, int detail) {
        super(stack);
        this.command = c;
        this.detail = detail;
    }

    /**
     * Returns <code>null</code> or a Command if a command is relevant to the
     * current event.
     * 
     * @since 3.1
     * @return <code>null</code> or a command
     */
    public Command getCommand() {
        return command;
    }

    /**
     * Returns <code>true</code> if this event is fired prior to the stack
     * changing.
     * 
     * @return <code>true</code> if pre-change event
     * @since 3.2
     */
    public final boolean isPreChangeEvent() {
        return 0 != (getDetail() & CommandStack.PRE_MASK);
    }

    /**
     * Returns <code>true</code> if this event is fired after the stack having
     * changed.
     * 
     * @return <code>true</code> if post-change event
     * @since 3.2
     */
    public final boolean isPostChangeEvent() {
        return 0 != (getDetail() & CommandStack.POST_MASK);
    }

    /**
     * Returns an integer identifying the type of event which has occurred.
     * 
     * @since 3.1
     * @return the detail of the event
     */
    public int getDetail() {
        return detail;
    }

}
