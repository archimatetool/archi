/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.util;

import uk.ac.bolton.archimate.model.IDiagramModelNote;
import uk.ac.bolton.archimate.model.IFontAttribute;
import uk.ac.bolton.archimate.model.ISketchModelSticky;


/**
 * Useful Utilities
 * 
 * @author Phillip Beauvoir
 */
public final class FigureUtils {

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
    }
}
