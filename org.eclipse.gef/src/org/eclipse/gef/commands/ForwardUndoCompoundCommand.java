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
package org.eclipse.gef.commands;

import java.util.ListIterator;

/**
 * A CompoundCommand that performs undo on its contained Commands in the same
 * order in which they were executed.
 */
@SuppressWarnings("rawtypes")
public class ForwardUndoCompoundCommand extends CompoundCommand {

    /**
     * Constructs a ForwardUndoCompoundCommand with no label
     */
    public ForwardUndoCompoundCommand() {
    }

    /**
     * Constructs a ForwardUndoCompoundCommand with the specified label
     * 
     * @param label
     *            the label
     */
    public ForwardUndoCompoundCommand(String label) {
        super(label);
    }

    /**
     * @see org.eclipse.gef.commands.Command#getDebugLabel()
     */
    @Override
    public String getDebugLabel() {
        return "ForwardUndoCommand: " + super.getDebugLabel();//$NON-NLS-1$
    }

    /**
     * Undo the command. For a Preorder compound command this means undoing all
     * of the commands that it contains. Do it in the same order as applied.
     */
    @Override
    public void undo() {
        ListIterator itr = getCommands().listIterator();
        try {
            while (itr.hasNext()) {
                ((Command) itr.next()).undo();
            }
        } catch (RuntimeException e) {
            itr.previous(); // Skip over the one that failed. It cleaned itself
                            // up.
            while (itr.hasPrevious()) {
                ((Command) itr.previous()).redo();
            }
            throw e;
        }
    }

}
