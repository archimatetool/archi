/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.canvas.figures;

import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.SWT;

import uk.ac.bolton.archimate.model.IFontAttribute;

/**
 * Delegate class to handle setting of text position
 * 
 * @author Phillip Beauvoir
 */
public class TextPositionDelegate {
    
    private IFigure fParentFigure;
    private IFigure fChildFigure;
    private IFontAttribute fFontObject;
    
    public TextPositionDelegate(IFigure parentFigure, IFigure childFigure, IFontAttribute fontObject) {
        fParentFigure = parentFigure;
        fChildFigure = childFigure;
        fFontObject = fontObject;
    }
    
    public void updateTextPosition() {
        GridData gd = null;
        
        switch(fFontObject.getTextPosition()) {
            case IFontAttribute.TEXT_POSITION_TOP_LEFT:
                gd = new GridData(SWT.LEFT, SWT.TOP, true, true);
                break;

            case IFontAttribute.TEXT_POSITION_TOP_CENTRE:
                gd = new GridData(SWT.CENTER, SWT.TOP, true, true);
                break;

            case IFontAttribute.TEXT_POSITION_TOP_RIGHT:
                gd = new GridData(SWT.RIGHT, SWT.TOP, true, true);
                break;

            case IFontAttribute.TEXT_POSITION_MIDDLE_LEFT:
                gd = new GridData(SWT.LEFT, SWT.CENTER, true, true);
                break;

            case IFontAttribute.TEXT_POSITION_MIDDLE_CENTRE:
                gd = new GridData(SWT.CENTER, SWT.CENTER, true, true);
                break;

            case IFontAttribute.TEXT_POSITION_MIDDLE_RIGHT:
                gd = new GridData(SWT.RIGHT, SWT.CENTER, true, true);
                break;

            case IFontAttribute.TEXT_POSITION_BOTTOM_LEFT:
                gd = new GridData(SWT.LEFT, SWT.BOTTOM, true, true);
                break;

            case IFontAttribute.TEXT_POSITION_BOTTOM_CENTRE:
                gd = new GridData(SWT.CENTER, SWT.BOTTOM, true, true);
                break;

            case IFontAttribute.TEXT_POSITION_BOTTOM_RIGHT:
                gd = new GridData(SWT.RIGHT, SWT.BOTTOM, true, true);
                break;

            default:
                break;
        }
        
        fParentFigure.setConstraint(fChildFigure, gd);
    }
}
