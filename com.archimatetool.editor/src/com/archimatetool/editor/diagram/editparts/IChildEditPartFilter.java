/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.editparts;

import org.eclipse.gef.EditPart;



/**
 * Filter to show/hide Edit Part's child objects
 * 
 * @author Phillip Beauvoir
 */
public interface IChildEditPartFilter extends IEditPartFilter {
    
    /**
     * Filter child element of parentEditPart
     * @param parentEditPart
     * @param childObject
     * @return True if childObject is to be visible
     */
    boolean isChildElementVisible(EditPart parentEditPart, Object childObject);
}
