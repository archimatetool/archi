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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TreeViewer;

/**
 * An internal class for the command stack inspector tool.
 * 
 * @deprecated since 3.1
 */
public class CommandStackViewerAction extends Action {

    /** The TreeViewer associated with this CommandStackViewerAction **/
    protected TreeViewer viewer;

    /**
     * Creates a new CommandStackViewerAction with the given TreeViewer
     * 
     * @param viewer
     *            the TreeViewer
     */
    public CommandStackViewerAction(TreeViewer viewer) {
        super("Toggle Debug Labels", //$NON-NLS-1$
                ImageDescriptor.createFromFile(CommandStackInspector.class,
                        "icons/stackDebug.gif"));//$NON-NLS-1$

        this.viewer = viewer;
        setChecked(((TreeLabelProvider) viewer.getLabelProvider())
                .getLabelStyle() == TreeLabelProvider.DEBUG_LABEL_STYLE);
    }

    /**
     * @see Action#run()
     */
    @Override
    public void run() {
        if (viewer == null)
            return;
        TreeLabelProvider labelProvider = (TreeLabelProvider) viewer
                .getLabelProvider();
        if (!isChecked()) {
            labelProvider.setLabelStyle(TreeLabelProvider.NORMAL_LABEL_STYLE);
        } else {
            labelProvider.setLabelStyle(TreeLabelProvider.DEBUG_LABEL_STYLE);
        }
        viewer.refresh();
    }

}
