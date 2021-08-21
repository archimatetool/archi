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
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.internal.GEFMessages;

/**
 * An action which selects all edit parts in the active workbench part.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class SelectAllAction extends Action {

    private IWorkbenchPart part;

    /**
     * Constructs a <code>SelectAllAction</code> and associates it with the
     * given part.
     * 
     * @param part
     *            The workbench part associated with this SelectAllAction
     */
    public SelectAllAction(IWorkbenchPart part) {
        this.part = part;
        setText(GEFMessages.SelectAllAction_Label);
        setToolTipText(GEFMessages.SelectAllAction_Tooltip);
        setId(ActionFactory.SELECT_ALL.getId());
    }

    /**
     * Selects all edit parts in the active workbench part.
     */
    @Override
    public void run() {
        GraphicalViewer viewer = part
                .getAdapter(GraphicalViewer.class);
        if (viewer != null) {
            viewer.setSelection(new StructuredSelection(
                    getSelectableEditParts(viewer)));
        }
    }

    /**
     * Retrieves edit parts which can be selected
     * 
     * @param viewer
     *            from which the edit parts are to be retrieved
     * @return list of selectable EditParts
     * @since 3.5
     */
    private List getSelectableEditParts(GraphicalViewer viewer) {
        List selectableChildren = new ArrayList();
        List children = viewer.getContents().getChildren();
        for (Iterator iter = children.iterator(); iter.hasNext();) {
            Object child = iter.next();
            if (child instanceof EditPart) {
                EditPart childPart = (EditPart) child;
                if (childPart.isSelectable() == true) {
                    selectableChildren.add(childPart);
                }
            }
        }
        return selectableChildren;
    }

}
