/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.jasperreports;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.archimatetool.editor.ui.components.ExtendedWizardDialog;
import com.archimatetool.model.IArchimateModel;



/**
 * Command Action Handler for Jasper Reports
 * 
 * @author Phillip Beauvoir
 */
public class JasperReportsHandler extends AbstractHandler {
    
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IWorkbenchPart part = HandlerUtil.getActivePart(event);
        IArchimateModel model = part != null ? part.getAdapter(IArchimateModel.class) : null;
        
        if(model != null) {
            ExportJasperReportsWizard wizard = new ExportJasperReportsWizard(model);
            
            WizardDialog dialog = new ExtendedWizardDialog(HandlerUtil.getActiveShell(event),
                    wizard,
                    "ExportJasperReportsWizard"); //$NON-NLS-1$
            
            if(dialog.open() == Window.OK) {
                try {
                    wizard.runWithProgress();
                }
                catch(InvocationTargetException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }

        return null;
    }
    
}
