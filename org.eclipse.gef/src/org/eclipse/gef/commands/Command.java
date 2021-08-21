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

/**
 * An Abstract implementation of {@link Command}.
 * 
 * @author hudsonr
 * @since 2.0
 */
public abstract class Command {

    private String label;

    private String debugLabel;

    /**
     * Constructs a Command with no label.
     */
    public Command() {
    }

    /**
     * Constructs a Command with the specified label.
     * 
     * @param label
     *            the Command's label
     */
    public Command(String label) {
        setLabel(label);
    }

    /**
     * @return <code>true</code> if the command can be redone. This method
     *         should only be called after <code>undo()</code> has been called.
     * @since 3.10
     */
    public boolean canRedo() {
        return canExecute();
    }

    /**
     * @return <code>true</code> if the command can be executed
     */
    public boolean canExecute() {
        return true;
    }

    /**
     * @return <code>true</code> if the command can be undone. This method
     *         should only be called after <code>execute()</code> or
     *         <code>redo()</code> has been called.
     */
    public boolean canUndo() {
        return true;
    }

    /**
     * Returns a Command that represents the chaining of a specified Command to
     * this Command. The Command being chained will <code>execute()</code> after
     * this command has executed, and it will <code>undo()</code> before this
     * Command is undone.
     * 
     * @param command
     *            <code>null</code> or the Command being chained
     * @return a Command representing the union
     */
    public Command chain(Command command) {
        if (command == null)
            return this;
        class ChainedCompoundCommand extends CompoundCommand {
            @Override
            public Command chain(Command c) {
                add(c);
                return this;
            }
        }
        CompoundCommand result = new ChainedCompoundCommand();
        result.setDebugLabel("Chained Commands"); //$NON-NLS-1$
        result.add(this);
        result.add(command);
        return result;
    }

    /**
     * This is called to indicate that the <code>Command</code> will not be used
     * again. The Command may be in any state (executed, undone or redone) when
     * dispose is called. The Command should not be referenced in any way after
     * it has been disposed.
     */
    public void dispose() {
    }

    /**
     * Executes the Command. This method should not be called if the Command is
     * not executable.
     */
    public void execute() {
    }

    /**
     * @return an untranslated String used for debug purposes only
     */
    public String getDebugLabel() {
        return debugLabel + ' ' + getLabel();
    }

    /**
     * @return a String used to describe this command to the User
     */
    public String getLabel() {
        return label;
    }

    /**
     * Re-executes the Command. This method should only be called after
     * <code>undo()</code> has been called.
     */
    public void redo() {
        execute();
    }

    /**
     * Sets the debug label for this command
     * 
     * @param label
     *            a description used for debugging only
     */
    public void setDebugLabel(String label) {
        debugLabel = label;
    }

    /**
     * Sets the label used to describe this command to the User.
     * 
     * @param label
     *            the label
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Undoes the changes performed during <code>execute()</code>. This method
     * should only be called after <code>execute</code> has been called, and
     * only when <code>canUndo()</code> returns <code>true</code>.
     * 
     * @see #canUndo()
     */
    public void undo() {
    }

}
