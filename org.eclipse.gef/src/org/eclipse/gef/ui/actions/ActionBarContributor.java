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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.RetargetAction;
import org.eclipse.ui.part.EditorActionBarContributor;

/**
 * Contributes actions to the workbench. !!Warning: This class is subject to
 * change.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class ActionBarContributor extends EditorActionBarContributor {

    private ActionRegistry registry = new ActionRegistry();

    /**
     * Contains the {@link RetargetAction}s that are registered as global action
     * handlers. We need to hold on to these so that we can remove them as
     * PartListeners in dispose().
     */
    private List retargetActions = new ArrayList();
    private List globalActionKeys = new ArrayList();

    /**
     * Adds the given action to the action registry.
     * 
     * @param action
     *            the action to add
     */
    protected void addAction(IAction action) {
        getActionRegistry().registerAction(action);
    }

    /**
     * Indicates the existence of a global action identified by the specified
     * key. This global action is defined outside the scope of this contributor,
     * such as the Workbench's undo action, or an action provided by a workbench
     * ActionSet. The list of global action keys is used whenever the active
     * editor is changed ({@link #setActiveEditor(IEditorPart)}). Keys provided
     * here will result in corresponding actions being obtained from the active
     * editor's <code>ActionRegistry</code>, and those actions will be
     * registered with the ActionBars for this contributor. The editor's action
     * handler and the global action must have the same key.
     * 
     * @param key
     *            the key identifying the global action
     */
    protected void addGlobalActionKey(String key) {
        globalActionKeys.add(key);
    }

    /**
     * Adds the specified RetargetAction to this contributors
     * <code>ActionRegistry</code>. The RetargetAction is also added as a
     * <code>IPartListener</code> of the contributor's page. Also, the retarget
     * action's ID is flagged as a global action key, by calling
     * {@link #addGlobalActionKey(String)}.
     * 
     * @param action
     *            the retarget action being added
     */
    protected void addRetargetAction(RetargetAction action) {
        addAction(action);
        retargetActions.add(action);
        getPage().addPartListener(action);
        addGlobalActionKey(action.getId());
    }

    /**
     * Creates and initializes Actions managed by this contributor.
     */
    protected abstract void buildActions();

    /**
     * Subclasses must implement to declare additional global actions IDs. Only
     * IDs which were not already added directly or indirectly using
     * {@link #addGlobalActionKey(String)} need to be added.
     * 
     * @see #addGlobalActionKey(String)
     */
    protected abstract void declareGlobalActionKeys();

    /**
     * Disposes the contributor. Removes all {@link RetargetAction}s that were
     * {@link org.eclipse.ui.IPartListener}s on the
     * {@link org.eclipse.ui.IWorkbenchPage} and disposes them. Also disposes
     * the action registry.
     * <P>
     * Subclasses may extend this method to perform additional cleanup.
     * 
     * @see org.eclipse.ui.part.EditorActionBarContributor#dispose()
     */
    @Override
    public void dispose() {
        for (int i = 0; i < retargetActions.size(); i++) {
            RetargetAction action = (RetargetAction) retargetActions.get(i);
            getPage().removePartListener(action);
            action.dispose();
        }
        registry.dispose();
        retargetActions = null;
        registry = null;
    }

    /**
     * Retrieves an action from the action registry using the given ID.
     * 
     * @param id
     *            the ID of the sought action
     * @return <code>null</code> or the action if found
     */
    protected IAction getAction(String id) {
        return getActionRegistry().getAction(id);
    }

    /**
     * returns this contributor's ActionRegsitry.
     * 
     * @return the ActionRegistry
     */
    protected ActionRegistry getActionRegistry() {
        return registry;
    }

    /**
     * @see EditorActionBarContributor#init(IActionBars)
     */
    @Override
    public void init(IActionBars bars) {
        buildActions();
        declareGlobalActionKeys();
        super.init(bars);
    }

    /**
     * @see org.eclipse.ui.IEditorActionBarContributor#setActiveEditor(IEditorPart)
     */
    @Override
    public void setActiveEditor(IEditorPart editor) {
        ActionRegistry registry = editor
                .getAdapter(ActionRegistry.class);
        IActionBars bars = getActionBars();
        for (int i = 0; i < globalActionKeys.size(); i++) {
            String id = (String) globalActionKeys.get(i);
            IAction handler = registry != null ? registry.getAction(id) : null;
            bars.setGlobalActionHandler(id, handler);
        }
    }

}
