/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package org.opengroup.archimate.xmlexchange;

import java.io.File;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.jdom2.JDOMException;
import org.xml.sax.SAXException;

import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.editor.model.IModelImporter;
import com.archimatetool.model.IArchimateModel;



/**
 * Import Open Exchange XML Format to Archi Model
 * 
 * @author Phillip Beauvoir
 */
public class XMLExchangeImportProvider implements IModelImporter, IXMLExchangeGlobals {
    
    @Override
    public void doImport() throws IOException {
        File file = askOpenFile();
        if(file == null) {
            return;
        }
        
        Exception[] ex1 = new Exception[1];
        
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
                    ex1[0] = ex;
                    ex.printStackTrace();
                }
            }
        });
        
        if(ex1[0] != null) {
            throw new IOException(ex1[0]);
        }
    }
    
    private File askOpenFile() {
        FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
        dialog.setFilterExtensions(new String[] { FILE_EXTENSION_WILDCARD, "*.*" } ); //$NON-NLS-1$
        String path = dialog.open();
        return path != null ? new File(path) : null;
    }
}
