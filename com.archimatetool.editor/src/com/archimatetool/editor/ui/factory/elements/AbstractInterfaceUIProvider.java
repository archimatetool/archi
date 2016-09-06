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
        if(instance instanceof IDiagramModelArchimateObject) {
            int figureType = ((IDiagramModelArchimateObject)instance).getType();
            if(figureType == 1) {
                return new Dimension(60, 60);
            }
        }
        
        return super.getDefaultSize();
    }

    @Override
    public boolean hasAlternateFigure() {
        return true;
    }
}
