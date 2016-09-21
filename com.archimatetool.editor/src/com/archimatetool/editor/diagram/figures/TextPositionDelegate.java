/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.SWT;

import com.archimatetool.model.ITextAlignment;
import com.archimatetool.model.ITextPosition;


/**
 * Delegate class to handle setting of text position
 * 
 * @author Phillip Beauvoir
 */
public class TextPositionDelegate {
    
    private IFigure fParentFigure;
    private IFigure fChildFigure;
    private ITextPosition fTextPositionObject;
    
    public TextPositionDelegate(IFigure parentFigure, IFigure childFigure, ITextPosition textPositionObject) {
        fParentFigure = parentFigure;
        fChildFigure = childFigure;
        fTextPositionObject = textPositionObject;
    }
    
    public void updateTextPosition() {
        int textPosition = SWT.CENTER;
        
        switch(fTextPositionObject.getTextPosition()) {
            case ITextPosition.TEXT_POSITION_TOP:
                textPosition = SWT.TOP;
                break;

            case ITextPosition.TEXT_POSITION_CENTRE:
                textPosition = SWT.CENTER;
                break;

            case ITextPosition.TEXT_POSITION_BOTTOM:
                textPosition = SWT.BOTTOM;
                break;

            default:
                break;
        }
        
        int textAlignment = SWT.CENTER;
        
        if(fTextPositionObject instanceof ITextAlignment) {
            switch(((ITextAlignment)fTextPositionObject).getTextAlignment()) {
                case ITextAlignment.TEXT_ALIGNMENT_LEFT:
                    textAlignment = SWT.LEFT;
                    break;

                case ITextAlignment.TEXT_ALIGNMENT_CENTER:
                    textAlignment = SWT.CENTER;
                    break;

                case ITextAlignment.TEXT_ALIGNMENT_RIGHT:
                    textAlignment = SWT.RIGHT;
                    break;

                default:
                    break;
            }
        }
        
        GridData gd = new GridData(textAlignment, textPosition, true, true);
        
        fParentFigure.setConstraint(fChildFigure, gd);
    }
}
