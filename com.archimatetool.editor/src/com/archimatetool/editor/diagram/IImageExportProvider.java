/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram;

import java.io.File;

import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.widgets.Composite;




/**
 * IImageExportProvider for extensions
 * 
 * @author Phillip Beauvoir
 */
public interface IImageExportProvider {
    
    /**
     * Interface to expose some of the functionality of the dialog being used by the Export dialog/wizard
     */
    public interface IExportDialogAdapter {
        /**
         * Set an error message on the dialog. Setting this will disable the "Save" button.
         * Set this to null to clear the message and enable the "Save" button.
         * @param message The message to show, or null if OK.
         */
        void setErrorMessage(String message);
    }
    
    /**
     * Provider extension ID
     */
    String FORMATPROVIDEREXTENSION_ID = "com.archimatetool.editor.imageExportProvider"; //$NON-NLS-1$
    
    /**
     * Initialise the provider with information for the export and provide a container composite for settings contributions.
     * 
     * @param adapter The owning dialog adapter
     * @param container The container composite to contribute to. Contributing is optional.
     * @param figure The Figure to export as image from
     */
    void init(IExportDialogAdapter adapter, Composite container, IFigure figure);
    
    /**
     * Export to file. The provider should export the figure provided in init()
     * @param providerID The provider's id. This ensures that a provider can use the same class for more than one image format
     * @param file The file to export to
     * @throws Exception
     */
    void export(String providerID, File file) throws Exception;
}
