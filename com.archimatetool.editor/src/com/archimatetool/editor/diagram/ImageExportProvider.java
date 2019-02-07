/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram;

import java.io.File;

import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

import com.archimatetool.editor.diagram.util.DiagramUtils;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.ui.ImageFactory;



/**
 * Core Export As Image Provider for PNG, JPG, and BMP
 * 
 * @author Phillip Beauvoir
 */
public class ImageExportProvider implements IImageExportProvider {
    
    public static final String BMP_IMAGE_EXPORT_PROVIDER = "com.archimatetool.editor.bmpImageExportProvider"; //$NON-NLS-1$
    public static final String JPEG_IMAGE_EXPORT_PROVIDER = "com.archimatetool.editor.jpegImageExportProvider"; //$NON-NLS-1$
    public static final String PNG_IMAGE_EXPORT_PROVIDER = "com.archimatetool.editor.pngImageExportProvider"; //$NON-NLS-1$
    
    final static String PREFS_IMAGE_SCALE = "imageExportScale"; //$NON-NLS-1$

    private IFigure fFigure;
    
    protected Spinner fScaleSpinner;
    
    @Override
    public void export(String providerID, File file) throws Exception {
        Image image = null;
        
        try {
            image = DiagramUtils.createImage(fFigure, (double)fScaleSpinner.getSelection() / 100, 10);
            ImageData imageData = image.getImageData(ImageFactory.getImageDeviceZoom());
            
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
            // Save Preferences
            savePreferences();

            if(image != null) {
                image.dispose();
            }
        }
    }
    
    @Override
    public void init(IExportDialogAdapter adapter, Composite container, IFigure figure) {
        fFigure = figure;
        
        container.setLayout(new GridLayout(2, false));
        container.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        Label label = new Label(container, SWT.NONE);
        label.setText(Messages.ImageExportProvider_0);
        fScaleSpinner = new Spinner(container, SWT.BORDER);
        fScaleSpinner.setMinimum(25);
        fScaleSpinner.setMaximum(500);
        
        loadPreferences();
    }

    /**
     * Load any user prefs
     */
    protected void loadPreferences() {
        IPreferenceStore store = Preferences.STORE;
        
        // Value of scale
        int scale = store.getInt(PREFS_IMAGE_SCALE);
        fScaleSpinner.setSelection(scale == 0 ? 100 : scale);
    }
    
    /**
     * Save any user prefs
     */
    protected void savePreferences() {
        IPreferenceStore store = Preferences.STORE;
        
        // Value of scale
        int scale = fScaleSpinner.getSelection();
        store.setValue(PREFS_IMAGE_SCALE, scale);
    }
}
