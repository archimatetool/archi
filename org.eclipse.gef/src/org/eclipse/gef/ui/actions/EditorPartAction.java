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
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author hudsonr
 */
public abstract class EditorPartAction extends WorkbenchPartAction {

    /**
     * Constructs a new EditorPartAction with the given editor and style.
     * 
     * @param editor
     *            The editor to be associated with this action.
     * @param style
     *            the Action's style as defined in Action
     */
    public EditorPartAction(IEditorPart editor, int style) {
        super(editor, style);
    }

    /**
     * Constructs a new EditorPartAction with the given editor.
     * 
     * @param editor
     *            The editor to be associated with this action.
     */
    public EditorPartAction(IEditorPart editor) {
        super(editor);
    }

    /**
     * Used internally to avoid deprecation warnings in GEF subclasses.
     * 
     * @param part
     *            the part
     */
    EditorPartAction(IWorkbenchPart part) {
        super(part);
    }

    /**
     * Returns the editor associated with this action.
     * 
     * @return the Editor part
     */
    protected IEditorPart getEditorPart() {
        return (IEditorPart) getWorkbenchPart();
    }

    /**
     * Sets the editor.
     * 
     * @param part
     *            the editorpart
     */
    protected void setEditorPart(IEditorPart part) {
        setWorkbenchPart(part);
    }

}
