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

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionFactory;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.PaletteTemplateEntry;

/**
 * Copies the currently selected template in the palatte to the system
 * clipboard.
 * 
 * @author Eric Bordeau
 */
public class CopyTemplateAction extends WorkbenchPartAction implements
        ISelectionChangedListener {

    private Object template;

    /**
     * Constructs a new CopyTemplateAction. You must manually add this action to
     * the palette viewer's list of selection listeners. Otherwise, this
     * action's enabled state won't be updated properly.
     * 
     * @param editor
     *            the workbench part
     * @see org.eclipse.gef.ui.actions.EditorPartAction#EditorPartAction(IEditorPart)
     */
    public CopyTemplateAction(IEditorPart editor) {
        super(editor);
        setId(ActionFactory.COPY.getId());
        setText(GEFMessages.CopyAction_Label);
    }

    /**
     * Returns whether the selected EditPart is a TemplateEditPart.
     * 
     * @return whether the selected EditPart is a TemplateEditPart
     */
    @Override
    protected boolean calculateEnabled() {
        return template != null;
    }

    /**
     * @see org.eclipse.gef.ui.actions.EditorPartAction#dispose()
     */
    @Override
    public void dispose() {
        template = null;
    }

    /**
     * Sets the default {@link Clipboard Clipboard's} contents to be the
     * currently selected template.
     */
    @Override
    public void run() {
        Clipboard.getDefault().setContents(template);
    }

    /**
     * Sets the selected EditPart and refreshes the enabled state of this
     * action.
     * 
     * @see ISelectionChangedListener#selectionChanged(SelectionChangedEvent)
     */
    @Override
    public void selectionChanged(SelectionChangedEvent event) {
        ISelection s = event.getSelection();
        if (!(s instanceof IStructuredSelection))
            return;
        IStructuredSelection selection = (IStructuredSelection) s;
        template = null;
        if (selection != null && selection.size() == 1) {
            Object obj = selection.getFirstElement();
            if (obj instanceof EditPart) {
                Object model = ((EditPart) obj).getModel();
                if (model instanceof CombinedTemplateCreationEntry)
                    template = ((CombinedTemplateCreationEntry) model)
                            .getTemplate();
                else if (model instanceof PaletteTemplateEntry)
                    template = ((PaletteTemplateEntry) model).getTemplate();
            }
        }
        refresh();
    }

}
