/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.dialog;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Error Message Dialog with text area to show a long message in a text box
 * 
 * @author Phillip Beauvoir
 */
public class ErrorMessageDialog extends MessageDialog {
    
    private String longMessage;
    
    public static void open(Shell parentShell, String dialogTitle, String message, String longMessage) {
        ErrorMessageDialog dialog = new ErrorMessageDialog(parentShell, dialogTitle, message, longMessage);
        dialog.open();
    }
    
    public static void open(Shell parentShell, String dialogTitle, String message, Throwable ex) {
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        open(parentShell, dialogTitle, message, sw.toString());
    }

    private ErrorMessageDialog(Shell parentShell, String dialogTitle, String message, String longMessage) {
        super(parentShell, dialogTitle, null, message, MessageDialog.ERROR, 1, 
                new String[] { Messages.ErrorMessageDialog_0, IDialogConstants.OK_LABEL});
        this.longMessage = longMessage;
    }

    @Override
    protected Control createCustomArea(Composite parent) {
        Text text = new Text(parent, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        text.setText(longMessage);
        text.setBackground(text.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        GridDataFactory.defaultsFor(text).applyTo(text);
        return text;
    }
    
    @Override
    protected void buttonPressed(int buttonId) {
        if(buttonId == 0 && longMessage != null) {
            Clipboard clipboard = null;
            try {
                clipboard = new Clipboard(getShell().getDisplay());
                clipboard.setContents(new Object[]{longMessage}, new Transfer[]{TextTransfer.getInstance()});
            }
            finally {
                if(clipboard != null) {
                    clipboard.dispose();
                }
            }
        }
        else {
            super.buttonPressed(buttonId);
        }
    }
    
    @Override
    protected boolean isResizable() {
        return true;
    }
}
