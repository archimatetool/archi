/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram;

import java.io.File;

import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Composite;

import com.archimatetool.editor.diagram.util.DiagramUtils;



/**
 * Export As Image Provider for PNG, JPG, BMP
 * 
 * @author Phillip Beauvoir
 */
public class ImageExportProvider implements IImageExportProvider {
    
    public void export(String providerID, File file, IFigure figure) throws Exception {
        Image image = null;
        
        try {
            image = DiagramUtils.createImage(figure, 1, 10);
            ImageData imageData = image.getImageData();
            
            ImageLoader loader = new ImageLoader();
            loader.data = new ImageData[] { imageData };

            if("com.archimatetool.editor.bmpImageExportProvider".equals(providerID)) { //$NON-NLS-1$
                loader.save(file.getPath(), SWT.IMAGE_BMP);
            }
            else if("com.archimatetool.editor.jpegImageExportProvider".equals(providerID)) { //$NON-NLS-1$
                loader.save(file.getPath(), SWT.IMAGE_JPEG);
            }
            else if("com.archimatetool.editor.pngImageExportProvider".equals(providerID)) { //$NON-NLS-1$
                loader.save(file.getPath(), SWT.IMAGE_PNG);
            }
            else {
                loader.save(file.getPath() + ".png", SWT.IMAGE_PNG); //$NON-NLS-1$
            }
        }
        finally {
            if(image != null) {
                image.dispose();
            }
        }
    }

    @Override
    public void contributeSettings(Composite container) {
    }
    
}
