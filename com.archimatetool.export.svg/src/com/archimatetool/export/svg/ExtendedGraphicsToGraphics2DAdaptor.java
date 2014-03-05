package com.archimatetool.export.svg;

import java.awt.Graphics2D;

import org.eclipse.draw2d.geometry.Rectangle;

import com.archimatetool.export.svg.graphiti.GraphicsToGraphics2DAdaptor;

/**
 *        This over-rides:
 *        
 *        public void setInterpolation(int interpolation) {
 *            throwNotImplemented();
 *        }
 *        
 *        Some Draw2d figures (DiagramImageFigure and IconicDelegate) set interpolation
 *        
 *        I also had to make getSWTGraphics() protected in GraphicsToGraphics2DAdaptor
 *        
 *        And paintNotCompatibleStringsAsBitmaps causes problems for some fonts, especially on Mac
 *
 */
public class ExtendedGraphicsToGraphics2DAdaptor extends GraphicsToGraphics2DAdaptor {

    
    public ExtendedGraphicsToGraphics2DAdaptor(Graphics2D graphics, Rectangle viewPort) {
        super(graphics, viewPort);
        
        // This is mangling some fonts on Mac
        paintNotCompatibleStringsAsBitmaps = false;
    }

    @Override
    public void setInterpolation(int interpolation) {
        getSWTGraphics().setInterpolation(interpolation);
    }

       
    
}
