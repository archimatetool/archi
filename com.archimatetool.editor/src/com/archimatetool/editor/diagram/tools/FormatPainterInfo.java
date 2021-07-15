/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.tools;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;

import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.ImageFactory;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelObject;



/**
 * FormatPainter Singleton state of format and cursor
 * 
 * @author Phillip Beauvoir
 */
public class FormatPainterInfo {
    
    public static FormatPainterInfo INSTANCE = new FormatPainterInfo();
    
    /**
     * Paint format information containing cursor color and source component
     */
    public static class PaintFormat {
        private IDiagramModelComponent component;
        private RGB cursorColor;
        
        public PaintFormat(IDiagramModelComponent component) {
            this.component = component;
            
            // Default
            cursorColor = new RGB(255, 255, 255);
            
            if(component instanceof IDiagramModelConnection) {
                // Line color
                String colorValue = ((IDiagramModelConnection)component).getLineColor();
                cursorColor = ColorFactory.convertStringToRGB(colorValue);
                if(cursorColor == null) {
                    cursorColor = ColorFactory.getDefaultLineColor(component).getRGB();
                }
            }
            else if(component instanceof IDiagramModelObject) {
                // Fill color
                String colorValue = ((IDiagramModelObject)component).getFillColor();
                cursorColor = ColorFactory.convertStringToRGB(colorValue);
                if(cursorColor == null) {
                    cursorColor = ColorFactory.getDefaultFillColor(component).getRGB();
                }
            }
        }
                
        public IDiagramModelComponent getSourceComponent() {
            return component;
        }
        
        public RGB getCursorColor() {
            return cursorColor;
        }
    }
    
    FormatPainterInfo() {}
    
    private PaintFormat pf;
    private Cursor coloredCursor, defaultCursor;
    private PropertyChangeSupport listeners = new PropertyChangeSupport(this);
    
    PaintFormat getPaintFormat() {
        return pf;
    }
    
    void updatePaintFormat(IDiagramModelComponent component) {
        if(component != null) {
            pf = new PaintFormat(component);
        }
        else {
            pf = null;
        }
        updateColoredCursor();
        fireUpdated();
    }
    
    /**
     * Reset all copied information
     */
    public void reset() {
        pf = null;
        fireUpdated();
    }
    
    Cursor getCursor() {
        return pf == null ? getDefaultCursor() : coloredCursor;
    }
    
    private Cursor getDefaultCursor() {
        if(defaultCursor == null) {
            ImageData id = IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_FORMAT_PAINTER_GREY).getImageData(ImageFactory.getLogicalDeviceZoom());
            defaultCursor = new Cursor(null, id, 0, id.height - 1);
        }
        
        return defaultCursor;
    }
    
    boolean isFat() {
        return pf != null;
    }
    
    /**
     * Update colored cursor to the selected fill color
     */
    private void updateColoredCursor() {
        if(coloredCursor != null && !coloredCursor.isDisposed()) {
            coloredCursor.dispose();
        }
        
        ImageData cursorImageData = IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_FORMAT_PAINTER).getImageData(ImageFactory.getLogicalDeviceZoom());

        if(pf.getCursorColor() != null) {
            PaletteData pData = cursorImageData.palette;
            int whitePixel = pData.getPixel(new RGB(255, 255, 255));
            int fillColor = pData.getPixel(pf.getCursorColor());

            for(int i = 0; i < cursorImageData.width; i++) {
                for(int j = 0; j < cursorImageData.height; j++) {
                    if(cursorImageData.getPixel(i, j) == whitePixel) {
                        cursorImageData.setPixel(i, j, fillColor);
                    }
                }
            }
        }
        
        coloredCursor = new Cursor(null, cursorImageData, 0, cursorImageData.height - 1);
    }
    
    void addPropertyChangeListener(PropertyChangeListener listener) {
        listeners.removePropertyChangeListener(listener);
        listeners.addPropertyChangeListener(listener);
    }
    
    void removePropertyChangeListener(PropertyChangeListener listener) {
        listeners.removePropertyChangeListener(listener);
    }
    
    private void fireUpdated() {
        listeners.firePropertyChange("FORMAT_PAINTER_UPDATED", false, true); //$NON-NLS-1$
    }
    
    void dispose() {
        if(defaultCursor != null) {
            defaultCursor.dispose();
        }
        
        if(coloredCursor != null) {
            coloredCursor.dispose();
        }
    }
}
