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
import org.eclipse.swt.graphics.RGB;

import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.IArchimateImages;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelObject;



/**
 * FormatPainter Singleton state of format and cursor
 * 
 * @author Phillip Beauvoir
 */
public class FormatPainterInfo {
    
    protected static ImageData cursorImageData = IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.CURSOR_IMG_FORMAT_PAINTER).getImageData();
    
    protected static Cursor defaultCursor = new Cursor(
            null,
            IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.CURSOR_IMG_FORMAT_PAINTER_GREY).getImageData(),
            0,
            cursorImageData.height - 1);
    
    protected static Cursor coloredCursor;
    
    /**
     * Paint format information containing cursor color and source component
     */
    public static class PaintFormat {
        private IDiagramModelComponent sourceComponent;
        private RGB cursorColor;
        
        public PaintFormat(IDiagramModelComponent component) {
            sourceComponent = component;
            
            if(sourceComponent instanceof IDiagramModelConnection) {
                // Line color
                String colorValue = ((IDiagramModelConnection)sourceComponent).getLineColor();
                cursorColor = ColorFactory.convertStringToRGB(colorValue);
                if(cursorColor == null) {
                    cursorColor = ColorFactory.getDefaultLineColor(sourceComponent).getRGB();
                }
            }
            else if(sourceComponent instanceof IDiagramModelObject) {
                // Fill color
                String colorValue = ((IDiagramModelObject)sourceComponent).getFillColor();
                cursorColor = ColorFactory.convertStringToRGB(colorValue);
                if(cursorColor == null) {
                    cursorColor = ColorFactory.getDefaultFillColor(sourceComponent).getRGB();
                }
            }
            else {
                cursorColor = new RGB(255, 255, 255);
            }
        }
                
        public IDiagramModelComponent getSourceComponent() {
            return sourceComponent;
        }
        
        public RGB getCursorColor() {
            return cursorColor;
        }
    }
    
    public static FormatPainterInfo INSTANCE = new FormatPainterInfo();
    
    /**
     * PropertyChangeSupport
     */
    private PropertyChangeSupport listeners = new PropertyChangeSupport(this);

    private PaintFormat pf;
    
    public PaintFormat getPaintFormat() {
        return pf;
    }
    
    public void updatePaintFormat(IDiagramModelComponent component) {
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
    
    public Cursor getCursor() {
        return pf == null ? defaultCursor : coloredCursor;
    }
    
    public boolean isFat() {
        return pf != null;
    }
    
    /**
     * Update colored cursor to selected fill color
     */
    protected void updateColoredCursor() {
        if(coloredCursor != null && !coloredCursor.isDisposed()) {
            coloredCursor.dispose();
        }
        
        if(pf.getCursorColor() != null) {
            cursorImageData.palette.colors[1] = pf.getCursorColor();
        }
        
        coloredCursor = new Cursor(
                null,
                cursorImageData,
                0,
                cursorImageData.height - 1);
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        listeners.removePropertyChangeListener(listener);
        listeners.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        listeners.removePropertyChangeListener(listener);
    }
    
    private void fireUpdated() {
        listeners.firePropertyChange("FORMAT_PAINTER_UPDATED", false, true); //$NON-NLS-1$
    }
}
