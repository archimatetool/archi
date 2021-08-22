/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import org.eclipse.gef.print.PrintGraphicalViewerOperation;
import org.eclipse.gef.ui.actions.WorkbenchPartAction;
import org.eclipse.gef.ui.parts.GraphicalViewerImpl;
import org.eclipse.swt.SWT;
import org.eclipse.swt.printing.PrintDialog;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;

import com.archimatetool.editor.diagram.util.DiagramUtils;
import com.archimatetool.model.IDiagramModel;



/**
 * Print Diagram Action
 * 
 * @author Phillip Beauvoir
 */
public class PrintDiagramAction extends WorkbenchPartAction {

    public PrintDiagramAction(IWorkbenchPart part) {
        super(part);
        
        setId(ActionFactory.PRINT.getId());
    }

    @Override
    protected boolean calculateEnabled() {
        // Should be enabled at all times in case of print to PDF
        return true;
    }

    @Override
    public void run() {
        PrintModeDialog modeDialog = new PrintModeDialog(getWorkbenchPart().getSite().getShell());
        modeDialog.open();
        int printMode = modeDialog.getPrintMode();
        if(printMode == -1) {
            return;
        }
        
        PrintDialog printDialog = new PrintDialog(getWorkbenchPart().getSite().getShell(), SWT.NULL);
        PrinterData data = printDialog.open();

        if(data != null) {
            IDiagramModel diagramModel = getWorkbenchPart().getAdapter(IDiagramModel.class);

            Shell tempShell = new Shell();
            GraphicalViewerImpl viewer = DiagramUtils.createViewer(diagramModel, tempShell);
            
            PrintGraphicalViewerOperation op = new PrintGraphicalViewerOperation(new Printer(data), viewer);
            op.setUseScaledGraphics(false); // this should stop font clipping
            op.setPrintMode(printMode);
            op.run(getWorkbenchPart().getTitle());

            tempShell.dispose();
        }
    }
}
