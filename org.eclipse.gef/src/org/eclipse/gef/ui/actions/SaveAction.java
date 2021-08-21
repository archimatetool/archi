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

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionFactory;

import org.eclipse.gef.internal.GEFMessages;

/**
 * An action to save the editor's current state.
 */
public class SaveAction extends EditorPartAction {

    /**
     * Constructs a <code>SaveAction</code> and associates it with the given
     * editor.
     * 
     * @param editor
     *            the IEditorPart
     */
    public SaveAction(IEditorPart editor) {
        super(editor);
        setLazyEnablementCalculation(false);
    }

    /**
     * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
     */
    @Override
    protected boolean calculateEnabled() {
        return getEditorPart().isDirty();
    }

    /**
     * Initializes this action's text.
     */
    @Override
    protected void init() {
        setId(ActionFactory.SAVE.getId());
        setText(GEFMessages.SaveAction_Label);
        setToolTipText(GEFMessages.SaveAction_Tooltip);
    }

    /**
     * Saves the state of the associated editor.
     */
    @Override
    public void run() {
        getEditorPart().getSite().getPage().saveEditor(getEditorPart(), false);
    }

}
