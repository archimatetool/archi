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

import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.ImageFactory;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IIconic;



/**
 * FormatPainter Singleton state of format and cursor.
 * 
 * This is a a global class that stores the component to copy formatting from and the cursor to be used
 * by each instance of FormatPainterToolEntry and FormatPainterTool.
 * 
 * Via the listener list it notifies each instance of FormatPainterToolEntry so that each can update
 * labels, icons and cursors.
 * 
 * @author Phillip Beauvoir
 */
public class FormatPainterInfo {
    
    /**
     * Global static instance
     */
    public static FormatPainterInfo INSTANCE = new FormatPainterInfo();
    
    private IDiagramModelComponent sourceComponent;
    private byte[] sourceImageBytes;
    
    private RGB cursorColor;
    private Cursor coloredCursor, defaultCursor;
    private PropertyChangeSupport listeners = new PropertyChangeSupport(this);
    
    private FormatPainterInfo() {}
    
    /**
     * Reset source component to null and notify listeners.
     */
    public void reset() {
        setSourceComponent(null);
        fireUpdated();
    }
    
    /**
     * Set the source component that we will copy formatting from and update the cursor.
     */
    void updateWithSourceComponent(IDiagramModelComponent component) {
        setSourceComponent(component);
        updateColoredCursor();
        fireUpdated();
    }
    
    /**
     * Get the cursor to use for the tool.
     */
    Cursor getCursor() {
        return hasSourceComponent() ? coloredCursor : getDefaultCursor();
    }
    
    private Cursor getDefaultCursor() {
        if(defaultCursor == null) {
            ImageData id = IArchiImages.ImageFactory.getImage(IArchiImages.CURSOR_FORMAT_PAINTER_GREY).getImageData(ImageFactory.getCursorDeviceZoom());
            defaultCursor = new Cursor(null, id, 0, id.height - 1);
        }
        
        return defaultCursor;
    }
    
    /**
     * @return true if we have a source component that we will copy the format from.
     */
    boolean hasSourceComponent() {
        return getSourceComponent() != null;
    }
    
    IDiagramModelComponent getSourceComponent() {
        return sourceComponent;
    }
    
    byte[] getSourceImageBytes() {
        return sourceImageBytes;
    }
    
    RGB getCursorColor() {
        return cursorColor;
    }
    
    /**
     * Set the source component from which we will copy the formatting
     */
    private void setSourceComponent(IDiagramModelComponent component) {
        sourceComponent = null;
        sourceImageBytes = null;
        cursorColor = null;

        if(component != null) {
            // Make a snapshot copy of the source component so we don't reference the original object.
            // Before this change we used to hold a reference to the original object
            // but if the model was closed we still held a reference to it and it couldn't be garbage collected
            // until the user cleared the FormatPainter which they might forget to do.
            // Another side effect of that old way was if the user changed an attribute of the source component
            // the FormatPainter would now hold that new value which might not be what the user expects.
            sourceComponent = (IDiagramModelComponent)component.getCopy();
        }

        // Copy image bytes, if any
        if(component instanceof IIconic iconic && iconic.getImagePath() != null) {
            IArchiveManager sourceArchiveManager = (IArchiveManager)component.getAdapter(IArchiveManager.class);
            if(sourceArchiveManager != null) {
                sourceImageBytes = sourceArchiveManager.getBytesFromEntry(iconic.getImagePath());
            }
        }

        if(sourceComponent instanceof IDiagramModelConnection dmc) {
            // Line color
            String colorValue = dmc.getLineColor();
            cursorColor = ColorFactory.convertStringToRGB(colorValue);
            if(cursorColor == null) {
                cursorColor = ColorFactory.getDefaultLineColor(sourceComponent).getRGB();
            }
        }
        else if(sourceComponent instanceof IDiagramModelObject dmo) {
            // Fill color
            String colorValue = dmo.getFillColor();
            cursorColor = ColorFactory.convertStringToRGB(colorValue);
            if(cursorColor == null) {
                cursorColor = ColorFactory.getDefaultFillColor(sourceComponent).getRGB();
            }
        }
    }
    
    /**
     * Update colored cursor to the selected fill color
     */
    private void updateColoredCursor() {
        if(coloredCursor != null && !coloredCursor.isDisposed()) {
            coloredCursor.dispose();
        }
        
        ImageData cursorImageData = IArchiImages.ImageFactory.getImage(IArchiImages.CURSOR_FORMAT_PAINTER).getImageData(ImageFactory.getCursorDeviceZoom());

        if(getCursorColor() != null) {
            PaletteData pData = cursorImageData.palette;
            int whitePixel = pData.getPixel(new RGB(255, 255, 255));
            int fillColor = pData.getPixel(getCursorColor());

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
