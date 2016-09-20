/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory;

/**
 * Interface for ArchiMate Element UI Provider
 * 
 * @author Phillip Beauvoir
 */
public interface IArchimateElementUIProvider extends IGraphicalObjectUIProvider {
    
    /**
     * @return True if this has an alternate figure
     */
    boolean hasAlternateFigure();

}