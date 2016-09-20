/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory.elements;

import org.eclipse.draw2d.geometry.Dimension;

import com.archimatetool.model.IDiagramModelArchimateObject;



/**
 * Interface UI Provider
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractInterfaceUIProvider extends AbstractArchimateElementUIProvider {

    @Override
    public Dimension getDefaultSize() {
        // Square size
        if(instance instanceof IDiagramModelArchimateObject) {
            int figureType = ((IDiagramModelArchimateObject)instance).getType();
            if(figureType == 1) {
                return DefaultSquareSize;
            }
        }
        
        return super.getDefaultSize();
    }
    
    @Override
    public Dimension getUserDefaultSize() {
        /*
         * User default size needs to be converted to a square height/width for the alternate figure
         */
        Dimension userSize = super.getUserDefaultSize();
        
        if(getDefaultSize() == DefaultSquareSize) {
            int length = Math.min(userSize.width, userSize.height);
            userSize = new Dimension(length, length);
        }
        
        return userSize;
    }

    @Override
    public boolean hasAlternateFigure() {
        return true;
    }
}
