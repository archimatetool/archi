/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.reports.html;

import java.io.IOException;

import org.eclipse.jface.dialogs.MessageDialog;

import uk.ac.bolton.archimate.editor.actions.AbstractModelSelectionDelegateAction;
import uk.ac.bolton.archimate.model.IArchimateModel;


/**
 * HTML Report Action
 * 
 * @author Phillip Beauvoir
 */
public class HTMLReportAction extends AbstractModelSelectionDelegateAction {
    
    @Override
    public void run() {
        IArchimateModel model = getActiveArchimateModel();
        if(model != null) {
            try {
                HTMLReportExporter exporter = new HTMLReportExporter();
                exporter.export(model);
            }
            catch(IOException ex) {
                MessageDialog.openError(workbenchWindow.getShell(), Messages.HTMLReportAction_0, ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
    
}
