/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.util;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.swt.graphics.Color;

import uk.ac.bolton.archimate.model.IDiagramModelNote;
import uk.ac.bolton.archimate.model.IFontAttribute;
import uk.ac.bolton.archimate.model.ISketchModelSticky;


/**
 * Useful Utilities
 * 
 * @author Phillip Beauvoir
 */
public final class FigureUtils {

    private static Color ghostFillColor = new Color(null, 31, 31, 31);

    /**
     * TODO Remove this when GEF 3.7 and Eclipse 3.7 are released as it's not needed
     * Create a drag feedback figure suitable for Mac OS X Cocoa.
     * The one for Cocoa shows as a black figure because Cocoa doesn't use XOR mode.
     */
    public static RectangleFigure createMacCocoaDragSourceFeedbackFigure() {
        RectangleFigure figure = new RectangleFigure() {
            @Override
            public void fillShape(Graphics graphics) {
                graphics.setAlpha(40);
                graphics.setBackgroundColor(ghostFillColor);
                graphics.fillRectangle(getBounds());
            }
            
            @Override
            protected void outlineShape(Graphics graphics) {
                graphics.setAlpha(200);
                graphics.setForegroundColor(ColorConstants.black);
                super.outlineShape(graphics);
            }
        };
        
        return figure;
    }

    /**
     * @param fontObject
     * @return The defalut text alignment for an object
     */
    public static int getDefaultTextAlignment(IFontAttribute fontObject) {
        if(fontObject instanceof IDiagramModelNote) {
            return IFontAttribute.TEXT_ALIGNMENT_LEFT;
        }
        if(fontObject instanceof ISketchModelSticky) {
            return IFontAttribute.TEXT_ALIGNMENT_LEFT;
        }
        return IFontAttribute.TEXT_ALIGNMENT_CENTER;
    }}
