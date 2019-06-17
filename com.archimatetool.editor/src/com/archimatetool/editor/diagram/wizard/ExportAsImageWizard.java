/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.wizard;

import java.io.File;
import java.io.IOException;

import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;

import com.archimatetool.editor.diagram.ImageExportProviderManager.ImageExportProviderInfo;



/**
 * Export as Image Wizard
 * 
 * @author Phillip Beauvoir
 */
public class ExportAsImageWizard extends Wizard {
    
    private IFigure fFigure;
    private String fName;
    
    private ExportAsImagePage fPage;
    
    public ExportAsImageWizard(IFigure figure, String name) {
        fFigure = figure;
        fName = name;
        setWindowTitle(Messages.ExportAsImageWizard_0);
    }
    
    @Override
    public void addPages() {
        fPage = new ExportAsImagePage(fFigure, fName);
        addPage(fPage);
    }
    
    @Override
    public boolean performFinish() {
        final ImageExportProviderInfo provider = fPage.getSelectedProvider();
        if(provider == null) {
            return false;
        }

        final File file = new File(fPage.getFileName());
            
        // Check valid file name
        try {
            file.getCanonicalPath();
        }
        catch(IOException ex) {
            MessageDialog.openError(getShell(), Messages.ExportAsImageWizard_1, Messages.ExportAsImageWizard_2);
            return false;
        }
        
        // Make sure the file does not already exist
        if(file.exists()) {
            boolean result = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(),
                    Messages.ExportAsImageWizard_3,
                    NLS.bind(Messages.ExportAsImageWizard_4, file.getAbsolutePath()));
            if(!result) {
                return false;
            }
        }
        
        // Make sure parent folder exists
        File parent = file.getParentFile();
        if(parent != null) {
            parent.mkdirs();
        }
        
        BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {
            @Override
            public void run() {
                try {
                    provider.getProvider().export(provider.getID(), file);
                    
                    fPage.storePreferences();
                }
                catch(Throwable ex) {
                    ex.printStackTrace();
                    MessageDialog.openError(Display.getCurrent().getActiveShell(),
                            Messages.ExportAsImageWizard_5,
                            Messages.ExportAsImageWizard_6 + " " + ex.getMessage()); //$NON-NLS-1$
                }
            }
        });
        
        return true;
    }

}
