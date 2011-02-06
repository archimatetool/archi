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

import uk.ac.bolton.archimate.editor.diagram.util.FigureUtils;
import uk.ac.bolton.archimate.editor.ui.ColorFactory;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.model.IDiagramModelConnection;
import uk.ac.bolton.archimate.model.IDiagramModelObject;
import uk.ac.bolton.archimate.model.IFontAttribute;


/**
 * FormatPainter Singleton state of format and cursor
 * 
 * @author Phillip Beauvoir
 */
public class FormatPainterInfo {
    
    private static ImageData cursorImageData = IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.CURSOR_IMG_FORMAT_PAINTER).getImageData();
    
    private static Cursor defaultCursor = new Cursor(
            null,
            IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.CURSOR_IMG_FORMAT_PAINTER_GREY).getImageData(),
            0,
            cursorImageData.height - 1);
    
    private static Cursor coloredCursor;
    
    static interface PaintFormat {
        RGB getCursorColor();
    };
    
    static class ElementPaintFormat implements PaintFormat {
        String fillColor;
        String font;
        String fontColor;
        int textAlignment;
        
        ElementPaintFormat(IDiagramModelObject object) {
            fillColor = object.getFillColor();
            if(fillColor == null) { // If null it's a default color for a IDiagramModelObject
                Color c = ColorFactory.getDefaultColor(object);
                fillColor = ColorFactory.convertRGBToString(c.getRGB());
            }
            
            font = object.getFont();
            fontColor = object.getFontColor();
            textAlignment = object.getTextAlignment();
            if(textAlignment == IFontAttribute.TEXT_ALIGNMENT_NONE) { // default
                textAlignment = FigureUtils.getDefaultTextAlignment(object);
            }
        }
        
        @Override
        public RGB getCursorColor() {
            return ColorFactory.convertStringToRGB(fillColor);
        }
    }
    
    static class ConnectionPaintFormat implements PaintFormat {
        String lineColor;
        int lineWidth;
        String font;
        String fontColor;
        
        ConnectionPaintFormat(IDiagramModelConnection connection) {
            lineColor = connection.getLineColor();
            lineWidth = connection.getLineWidth();
            font = connection.getFont();
            fontColor = connection.getFontColor();
        }
        
        @Override
        public RGB getCursorColor() {
            if(lineColor == null) {
                return new RGB(255, 255, 255);
            }
            return ColorFactory.convertStringToRGB(lineColor);
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
    
    public void updatePaintFormat(Object object) {
        if(object instanceof IDiagramModelObject) {
            pf = new ElementPaintFormat((IDiagramModelObject)object);
        }
        else if(object instanceof IDiagramModelConnection) {
            pf = new ConnectionPaintFormat((IDiagramModelConnection)object);
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
    private void updateColoredCursor() {
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
