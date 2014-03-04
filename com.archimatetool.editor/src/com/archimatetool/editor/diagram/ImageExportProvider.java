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
 * Core Export As Image Provider for PNG, JPG, and BMP
 * 
 * @author Phillip Beauvoir
 */
public class ImageExportProvider implements IImageExportProvider {
    
    public static final String BMP_IMAGE_EXPORT_PROVIDER = "com.archimatetool.editor.bmpImageExportProvider"; //$NON-NLS-1$
    public static final String JPEG_IMAGE_EXPORT_PROVIDER = "com.archimatetool.editor.jpegImageExportProvider"; //$NON-NLS-1$
    public static final String PNG_IMAGE_EXPORT_PROVIDER = "com.archimatetool.editor.pngImageExportProvider"; //$NON-NLS-1$
    
    private IFigure fFigure;
    
    @Override
    public void init(IExportDialogAdapter adapter, Composite container, IFigure figure) {
        fFigure = figure;
    }

    @Override
    public void export(String providerID, File file) throws Exception {
        Image image = null;
        
        try {
            image = DiagramUtils.createImage(fFigure, 1, 10);
            ImageData imageData = image.getImageData();
            
            ImageLoader loader = new ImageLoader();
            loader.data = new ImageData[] { imageData };

            if(BMP_IMAGE_EXPORT_PROVIDER.equals(providerID)) {
                loader.save(file.getPath(), SWT.IMAGE_BMP);
            }
            else if(JPEG_IMAGE_EXPORT_PROVIDER.equals(providerID)) {
                loader.save(file.getPath(), SWT.IMAGE_JPEG);
            }
            else if(PNG_IMAGE_EXPORT_PROVIDER.equals(providerID)) {
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
}
