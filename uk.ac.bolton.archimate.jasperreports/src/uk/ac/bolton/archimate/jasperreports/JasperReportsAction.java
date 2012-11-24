/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.jasperreports;

import org.eclipse.jface.wizard.WizardDialog;

import uk.ac.bolton.archimate.editor.actions.AbstractModelSelectionDelegateAction;
import uk.ac.bolton.archimate.editor.ui.components.ExtendedWizardDialog;
import uk.ac.bolton.archimate.model.IArchimateModel;


/**
 * Jasper Reports Action
 * 
 * @author Phillip Beauvoir
 */
public class JasperReportsAction extends AbstractModelSelectionDelegateAction {
    
    @Override
    public void run() {
        IArchimateModel model = getActiveArchimateModel();
        if(model != null) {
            WizardDialog dialog = new ExtendedWizardDialog(workbenchWindow.getShell(),
                    new ExportJasperReportsWizard(model),
                    "ExportJasperReportsWizard"); //$NON-NLS-1$
            dialog.open();
        }
    }
    
}
