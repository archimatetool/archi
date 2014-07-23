/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas.factory;

import org.eclipse.emf.ecore.EClass;

import com.archimatetool.canvas.model.ICanvasPackage;
import com.archimatetool.editor.ui.factory.diagram.DiagramImageUIProvider;



/**
 * Canvas Image UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class CanvasImageUIProvider extends DiagramImageUIProvider {

    @Override
    public EClass providerFor() {
        return ICanvasPackage.eINSTANCE.getCanvasModelImage();
    }
    
}
