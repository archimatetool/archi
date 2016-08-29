/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;




/**
 * Abstract Object UI Provider
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractObjectUIProvider implements IObjectUIProvider {
    
    @Override
    public String getDefaultName() {
        return ""; //$NON-NLS-1$
    }
    
    @Override
    public EditPart createEditPart() {
        return null;
    }
    
    @Override
    public Image getImage() {
        return null;
    }
    
    @Override
    public Image getImage(EObject instance) {
        return getImage();
    }
    
    @Override
    public ImageDescriptor getImageDescriptor() {
        return null;
    }
    
    @Override
    public boolean shouldExposeFeature(EObject instance, EAttribute feature) {
        return true;
    }
}
