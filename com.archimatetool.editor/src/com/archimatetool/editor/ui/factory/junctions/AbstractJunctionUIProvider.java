/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory.junctions;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.graphics.Color;

import com.archimatetool.editor.ui.factory.AbstractArchimateElementUIProvider;



/**
 * Junction UI Provider
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractJunctionUIProvider extends AbstractArchimateElementUIProvider {

    @Override
    public Dimension getDefaultSize() {
        return new Dimension(15, 15);
    }

    @Override
    public Color getDefaultColor() {
        return ColorConstants.black;
    }
    
    @Override
    public boolean shouldExposeFeature(EObject instance, EAttribute feature) {
        return false; // Junctions don't expose UI features
    }
}
