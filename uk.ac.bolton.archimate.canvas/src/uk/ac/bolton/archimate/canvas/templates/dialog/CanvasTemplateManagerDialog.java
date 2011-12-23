/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.canvas.templates.dialog;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import uk.ac.bolton.archimate.templates.dialog.TemplateManagerDialog;
import uk.ac.bolton.archimate.templates.model.TemplateManager;


/**
 * Canvas Template Manager Dialog
 * 
 * @author Phillip Beauvoir
 */
public class CanvasTemplateManagerDialog extends TemplateManagerDialog {
    
    public CanvasTemplateManagerDialog(Shell parentShell, TemplateManager templateManager) {
        super(parentShell, templateManager);
    }

    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText("My Canvasses");
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Control control = super.createDialogArea(parent);
        setTitle("Manage Canvas Templates");
        setMessage("Drag and drop Templates into Categories.");
        return control;
    }
    
}
