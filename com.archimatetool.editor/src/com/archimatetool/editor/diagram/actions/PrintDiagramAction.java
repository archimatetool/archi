/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.print.PrintGraphicalViewerOperation;
import org.eclipse.gef.ui.actions.WorkbenchPartAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.printing.PrintDialog;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;

import com.archimatetool.editor.diagram.util.DiagramUtils;
import com.archimatetool.editor.utils.PlatformUtils;
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
            
            Shell tempShell = null;
            Printer printer = null;
            
            try {
                tempShell = new Shell();
                GraphicalViewer viewer = DiagramUtils.createViewer(diagramModel, tempShell);
                printer = new Printer(data);
                
                PrintGraphicalViewerOperation op = new PrintGraphicalViewerOperation(printer, viewer);
                op.setPrintMode(printMode);
                
                // Setting scaled graphics off seems to be OK on Mac but not on Windows and Linux
                // "Fit Page" doesn't work. See https://github.com/archimatetool/archi/issues/1133
                if(PlatformUtils.isMac()) {
                    op.setUseScaledGraphics(false);
                }
                
                op.run(getWorkbenchPart().getTitle());
            }
            finally {
                if(tempShell != null) {
                    tempShell.dispose();
                }
                if(printer != null) {
                    printer.dispose();
                }
            }
        }
    }
}
