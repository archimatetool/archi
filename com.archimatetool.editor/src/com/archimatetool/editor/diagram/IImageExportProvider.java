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
     * Provider extension ID
     */
    String FORMATPROVIDEREXTENSION_ID = "com.archimatetool.editor.imageExportProvider"; //$NON-NLS-1$
    
    /**
     * Export the contents of the GraphicalViewer to file
     * @param providerID The provider's id. This ensures that a provider can use the same class for more than one image format
     * @param file The file to export to
     * @param figure The Figure to export as image from
     * @throws Exception
     */
    void export(String providerID, File file, IFigure figure) throws Exception;
    
    /**
     * Contribute bespoke settings to the UI.
     * @param container The container composite
     */
    void contributeSettings(Composite container);
}
