/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.help.hints;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import uk.ac.bolton.archimate.editor.ui.services.ViewManager;


/**
 * ShowHintsViewAction
 * 
 * @author Phillip Beauvoir
 */
public class ShowHintsViewAction implements IWorkbenchWindowActionDelegate {

    public void dispose() {
    }

    public void init(IWorkbenchWindow window) {
    }

    public void run(IAction action) {
        ViewManager.toggleViewPart(IHintsView.ID, false);
    }

    public void selectionChanged(IAction action, ISelection selection) {
    }

}
