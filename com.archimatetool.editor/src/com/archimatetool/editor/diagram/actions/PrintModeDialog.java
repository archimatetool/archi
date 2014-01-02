/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.jface.dialogs.Dialog;

import org.eclipse.draw2d.PrintFigureOperation;

/**
 * Print Mode Dialog
 * 
 * @author Phillip Beauvoir
 */
public class PrintModeDialog extends Dialog {

    private Button tile, fitPage, fitWidth, fitHeight;

    public PrintModeDialog(Shell shell) {
        super(shell);
    }

    @Override
    protected void configureShell(Shell newShell) {
        newShell.setText(Messages.PrintModeDialog_0);
        super.configureShell(newShell);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite)super.createDialogArea(parent);

        tile = new Button(composite, SWT.RADIO);
        tile.setText(Messages.PrintModeDialog_1);
        tile.setSelection(true);

        fitPage = new Button(composite, SWT.RADIO);
        fitPage.setText(Messages.PrintModeDialog_2);

        fitWidth = new Button(composite, SWT.RADIO);
        fitWidth.setText(Messages.PrintModeDialog_3);

        fitHeight = new Button(composite, SWT.RADIO);
        fitHeight.setText(Messages.PrintModeDialog_4);

        return composite;
    }

    @Override
    protected void okPressed() {
        int returnCode = -1;
        
        if(tile.getSelection()) {
            returnCode = PrintFigureOperation.TILE;
        }
        else if(fitPage.getSelection()) {
            returnCode = PrintFigureOperation.FIT_PAGE;
        }
        else if(fitHeight.getSelection()) {
            returnCode = PrintFigureOperation.FIT_HEIGHT;
        }
        else if(fitWidth.getSelection()) {
            returnCode = PrintFigureOperation.FIT_WIDTH;
        }
        
        setReturnCode(returnCode);
        
        close();
    }
    
    @Override
    protected void cancelPressed() {
        setReturnCode(-1);
        close();
    }
}
