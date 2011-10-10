/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.tools;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;

import uk.ac.bolton.archimate.editor.ui.ColorFactory;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.model.IDiagramModelComponent;
import uk.ac.bolton.archimate.model.IDiagramModelConnection;
import uk.ac.bolton.archimate.model.IDiagramModelObject;


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
    
    public static class PaintFormat {
        public IDiagramModelComponent sourceComponent;
        public String fillColor;
        
        public PaintFormat(IDiagramModelConnection connection) {
            sourceComponent = connection;
            fillColor = connection.getLineColor();
        }
        
        PaintFormat(IDiagramModelObject object) {
            sourceComponent = object;
            
            fillColor = object.getFillColor();
            if(fillColor == null) { // If null it's a default color for a IDiagramModelObject
                Color c = ColorFactory.getDefaultColor(object);
                fillColor = ColorFactory.convertRGBToString(c.getRGB());
            }
        }
        
        public RGB getCursorColor() {
            if(fillColor == null) {
                return new RGB(255, 255, 255);
            }
            return ColorFactory.convertStringToRGB(fillColor);
        }
    }
    
    public static FormatPainterInfo INSTANCE = new FormatPainterInfo();
    
    /**
     * PropertyChangeSupport
     */
    private PropertyChangeSupport listeners = new PropertyChangeSupport(this);

    protected PaintFormat pf;
    
    public PaintFormat getPaintFormat() {
        return pf;
    }
    
    public void updatePaintFormat(Object object) {
        if(object instanceof IDiagramModelObject) {
            pf = new PaintFormat((IDiagramModelObject)object);
        }
        else if(object instanceof IDiagramModelConnection) {
            pf = new PaintFormat((IDiagramModelConnection)object);
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
        listeners.firePropertyChange("FORMAT_PAINTER_UPDATED", false, true);
    }
}
