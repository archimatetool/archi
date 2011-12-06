/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.templates.impl;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IActionDelegate2;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import uk.ac.bolton.archimate.editor.actions.NewDropDownAction;
import uk.ac.bolton.archimate.editor.ui.components.ExtendedWizardDialog;
import uk.ac.bolton.archimate.templates.impl.wizard.NewArchimateModelFromTemplateWizard;


/**
 * New Archimate Model From Template Action
 * 
 * @author Phillip Beauvoir
 */
public class NewArchimateModelFromTemplateAction implements IWorkbenchWindowActionDelegate, IActionDelegate2 {
    
    private IWorkbenchWindow workbenchWindow;
    private IAction action;

    @Override
    public void run(IAction action) {
    }

    @Override
    public void selectionChanged(IAction action, ISelection selection) {
    }

    @Override
    public void init(IAction action) {
        this.action = action;
    }

    @Override
    public void runWithEvent(IAction action, Event event) {
        WizardDialog dialog = new ExtendedWizardDialog(workbenchWindow.getShell(),
                new NewArchimateModelFromTemplateWizard(),
                "NewArchimateModelFromTemplateWizard");
        dialog.open();
    }

    @Override
    public void dispose() {
    }

    @Override
    public void init(IWorkbenchWindow window) {
        workbenchWindow = window;
        addActionToToolbar(window);
    }
    
    private void addActionToToolbar(IWorkbenchWindow window) {
        ICoolBarManager coolBarManager = ((ApplicationWindow)window).getCoolBarManager();
        ToolBarContributionItem fileToolbar = (ToolBarContributionItem)coolBarManager.find("toolbar_file");
        ActionContributionItem item = (ActionContributionItem)fileToolbar.getToolBarManager().find("uk.ac.bolton.archimate.editor.action.newAction");
        NewDropDownAction newDropDown = (NewDropDownAction)item.getAction();
        newDropDown.add(action);
    }
}
