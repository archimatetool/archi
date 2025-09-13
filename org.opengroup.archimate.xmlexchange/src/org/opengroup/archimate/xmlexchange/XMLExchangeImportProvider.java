/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package org.opengroup.archimate.xmlexchange;

import java.io.File;
import java.io.IOException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.jdom2.JDOMException;
import org.xml.sax.SAXException;

import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.model.IArchimateModel;



/**
 * Import Open Exchange XML Format to Archi Model
 * 
 * @author Phillip Beauvoir
 */
public class XMLExchangeImportProvider implements IXMLExchangeGlobals {
    
    public void doImport(IWorkbenchWindow window) {
        File file = askOpenFile(window);
        if(file == null) {
            return;
        }
        
        Exception[] exception = new Exception[1];
        
        BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {
            @Override
            public void run() {
                // Validate file
                try {
                    XMLValidator validator = new XMLValidator();
                    validator.validateXML(file);
                    
                    XMLModelImporter xmlModelImporter = new XMLModelImporter();
                    IArchimateModel model = xmlModelImporter.createArchiMateModel(file);
                    
                    if(model != null) {
                        IEditorModelManager.INSTANCE.openModel(model);
                    }
                }
                catch(SAXException | IOException | JDOMException | XMLModelParserException ex) {
                    exception[0] = ex;
                    ex.printStackTrace();
                }
            }
        });
        
        if(exception[0] != null) {
            MessageDialog.openError(window.getShell(),
                    Messages.XMLExchangeImportProvider_0,
                    Messages.XMLExchangeImportProvider_1
                    + " " //$NON-NLS-1$
                    + exception[0].getMessage());
        }
    }
    
    private File askOpenFile(IWorkbenchWindow window) {
        FileDialog dialog = new FileDialog(window.getShell(), SWT.OPEN);
        dialog.setFilterExtensions(new String[] { FILE_EXTENSION_WILDCARD, "*.*" } ); //$NON-NLS-1$
        String path = dialog.open();
        return path != null ? new File(path) : null;
    }
}
