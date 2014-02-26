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
 *        Some Draw2d figures (DiagramImageFigure and IconicDelegate) set this
 *        
 *        I also had to make getSWTGraphics() protected in GraphicsToGraphics2DAdaptor
 *
 */
public class ExtendedGraphicsToGraphics2DAdaptor extends GraphicsToGraphics2DAdaptor {

    
    public ExtendedGraphicsToGraphics2DAdaptor(Graphics2D graphics, Rectangle viewPort) {
        super(graphics, viewPort);
    }

    @Override
    public void setInterpolation(int interpolation) {
        getSWTGraphics().setInterpolation(interpolation);
    }

       
    
}
