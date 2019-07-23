/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.p2;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;


/**
 * @author Phillip Beauvoir
 */
public class P2UninstallHandler {
    
    private Shell shell;
    private boolean needsRestart = false;

    public boolean execute(Shell shell, List<IInstallableUnit> selected) {
        this.shell = shell;
        
        if(selected.isEmpty()) {
            return false;
        }
        
        boolean ok = MessageDialog.openQuestion(shell,
                Messages.P2UninstallHandler_0,
                Messages.P2UninstallHandler_2);
        
        if(!ok) {
            return false;
        }

        IProgressMonitor monitor = new NullProgressMonitor();
        
        try {
            IStatus status = P2Handler.getInstance().uninstall(selected, monitor);
            
            if(status.isOK()) {
                needsRestart = true;
            }
            else {
                displayErrorDialog(Messages.P2UninstallHandler_1);
            }
        }
        catch(ProvisionException ex) {
            displayErrorDialog(ex.getMessage());
        }
        
        return true;
    }
    
    boolean needsRestart() {
        return needsRestart;
    }
    
    private void displayErrorDialog(String message) {
        MessageDialog.openError(shell,
                Messages.P2UninstallHandler_0,
                message);
    }
    
}
