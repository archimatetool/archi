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
package org.eclipse.gef.ui.stackview;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.TreeViewer;

import org.eclipse.gef.commands.CommandStack;

/**
 * Internal class used for a debug view.
 * 
 * @deprecated this class will be deleted
 */
public class CommandStackInspectorPage extends org.eclipse.ui.part.Page {

    CommandStack input;
    TreeViewer treeViewer;

    /**
     * Creates a new CommandStackInspectorPage with the given CommandStack
     * 
     * @param input
     *            the CommandStack
     */
    public CommandStackInspectorPage(CommandStack input) {
        this.input = input;
    }

    /**
     * @see org.eclipse.ui.part.Page#createControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createControl(Composite composite) {
        treeViewer = new TreeViewer(composite);
        treeViewer.setContentProvider(new TreeContentProvider(input));
        treeViewer.setLabelProvider(new TreeLabelProvider(input));
        treeViewer.setInput(input);
    }

    /**
     * @see org.eclipse.ui.part.Page#getControl()
     */
    @Override
    public Control getControl() {
        return treeViewer.getControl();
    }

    /**
     * @see org.eclipse.ui.part.Page#makeContributions(org.eclipse.jface.action.IMenuManager,
     *      org.eclipse.jface.action.IToolBarManager,
     *      org.eclipse.jface.action.IStatusLineManager)
     */
    @Override
    public void makeContributions(IMenuManager menuManager,
            IToolBarManager toolBarManager, IStatusLineManager statusLineManager) {
        super.makeContributions(menuManager, toolBarManager, statusLineManager);
        toolBarManager.add(new CommandStackViewerAction(treeViewer));
    }

    /**
     * @see org.eclipse.ui.part.Page#setFocus()
     */
    @Override
    public void setFocus() {
        getControl().setFocus();
    }

}
