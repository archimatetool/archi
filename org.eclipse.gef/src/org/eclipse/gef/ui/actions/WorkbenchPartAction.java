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
package org.eclipse.gef.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchPart;

import org.eclipse.gef.Disposable;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;

/**
 * Base class for actions involving a WorkbenchPart. The workbench part is
 * useful for obtaining data needed by the action. For example, selection can be
 * obtained using the part's site. Anything can potentially be obtained using
 * {@link org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)}.
 */
public abstract class WorkbenchPartAction extends Action implements Disposable,
        UpdateAction {

    private IWorkbenchPart workbenchPart;
    private boolean lazyEnablement = true;

    /**
     * Constructs a WorkbenchPartAction for the given part.
     * 
     * @param part
     *            the workbench part
     */
    public WorkbenchPartAction(IWorkbenchPart part) {
        setWorkbenchPart(part);
        init();
    }

    /**
     * Constructs a WorkbenchPartAction for the given part and style.
     * 
     * @param part
     *            the workbench part
     * @param style
     *            one of <code>AS_PUSH_BUTTON</code>, <code>AS_CHECK_BOX</code>,
     *            <code>AS_DROP_DOWN_MENU</code>, <code>AS_RADIO_BUTTON</code>,
     *            and <code>AS_UNSPECIFIED</code>.
     */
    public WorkbenchPartAction(IWorkbenchPart part, int style) {
        super(null, style);
        setWorkbenchPart(part);
        init();
    }

    /**
     * Calculates and returns the enabled state of this action.
     * 
     * @return <code>true</code> if the action is enabled
     */
    protected abstract boolean calculateEnabled();

    /**
     * Disposes the action when it is no longer needed.
     */
    @Override
    public void dispose() {
    }

    /**
     * Executes the given {@link Command} using the command stack. The stack is
     * obtained by calling {@link #getCommandStack()}, which uses
     * <code>IAdapatable</code> to retrieve the stack from the workbench part.
     * 
     * @param command
     *            the command to execute
     */
    protected void execute(Command command) {
        if (command == null || !command.canExecute())
            return;
        getCommandStack().execute(command);
    }

    /**
     * Returns the editor's command stack. This is done by asking the workbench
     * part for its CommandStack via
     * {@link org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)}.
     * 
     * @return the command stack
     */
    protected CommandStack getCommandStack() {
        return getWorkbenchPart().getAdapter(CommandStack.class);
    }

    /**
     * Returns the workbench part given in the constructor
     * 
     * @return the workbench part
     */
    protected IWorkbenchPart getWorkbenchPart() {
        return workbenchPart;
    }

    /**
     * Initializes this action.
     */
    protected void init() {
    }

    /**
     * Returns <code>true</code> if the action is enabled. If the action
     * determines enablemenu lazily, then {@link #calculateEnabled()} is always
     * called. Otherwise, the enablement state set using
     * {@link Action#setEnabled(boolean)} is returned.
     * 
     * @see #setLazyEnablementCalculation(boolean)
     * @return <code>true</code> if the action is enabled
     */
    @Override
    public boolean isEnabled() {
        if (lazyEnablement)
            setEnabled(calculateEnabled());
        return super.isEnabled();
    }

    /**
     * Refreshes the properties of this action.
     */
    protected void refresh() {
        setEnabled(calculateEnabled());
    }

    /**
     * Sets lazy calculation of the isEnabled property. If this value is set to
     * <code>true</code>, then the action will always use
     * {@link #calculateEnabled()} whenever {@link #isEnabled()} is called.
     * <P>
     * Sometimes a value of <code>false</code> can be used to improve
     * performance and reduce the number of times {@link #calculateEnabled()} is
     * called. However, the client must then call the {@link #update()} method
     * at the proper times to force the update of enablement.
     * <P>
     * Sometimes a value of <code>true</code> can be used to improve
     * performance. If an <code>Action</code> only appears in a dynamic context
     * menu, then there is no reason to agressively update its enablement status
     * because the user cannot see the Action. Instead, its enablement only
     * needs to be determined when asked for, or <i>lazily</i>.
     * <P>
     * The default value for this setting is <code>true</code>.
     * 
     * @param value
     *            <code>true</code> if the enablement should be lazy
     */
    public void setLazyEnablementCalculation(boolean value) {
        lazyEnablement = value;
    }

    /**
     * Sets the workbench part.
     * 
     * @param part
     *            the workbench part
     */
    protected void setWorkbenchPart(IWorkbenchPart part) {
        workbenchPart = part;
    }

    /**
     * @see UpdateAction#update()
     */
    @Override
    public void update() {
        refresh();
    }

}
