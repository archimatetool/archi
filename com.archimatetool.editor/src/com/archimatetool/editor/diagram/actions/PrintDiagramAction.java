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
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;

import com.archimatetool.editor.utils.PlatformUtils;



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
        /*
         * On Linux we get the dreaded "Prevented recursive attempt to activate part *.treeModelView
         * while still in the middle of activating part com.archimatetool.diagramEditor" if we open a diagram editor
         * from the tree view. This can be fixed by putting Printer.getPrinterList(); on a thread, but I can't be bothered.
         */
        if(PlatformUtils.isLinux()) {
            return true;
        }
        
        PrinterData[] printers = Printer.getPrinterList();
        return printers != null && printers.length > 0;
    }

    @Override
    public void run() {
        GraphicalViewer viewer = getWorkbenchPart().getAdapter(GraphicalViewer.class);

        int printMode = new PrintModeDialog(viewer.getControl().getShell()).open();
        if(printMode == -1) {
            return;
        }
        
        PrintDialog dialog = new PrintDialog(viewer.getControl().getShell(), SWT.NULL);
        PrinterData data = dialog.open();

        if(data != null) {
            PrintGraphicalViewerOperation op = new PrintGraphicalViewerOperation(new Printer(data), viewer);
            op.setPrintMode(printMode);
            op.run(getWorkbenchPart().getTitle());
        }
    }
}
