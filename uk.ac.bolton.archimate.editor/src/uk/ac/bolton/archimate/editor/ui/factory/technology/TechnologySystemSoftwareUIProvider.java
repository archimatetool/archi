/*******************************************************************************
 * Copyright (c) 2012 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.ui.factory.technology;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import uk.ac.bolton.archimate.editor.diagram.editparts.technology.TechnologySystemSoftwareEditPart;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.model.IArchimatePackage;


/**
 * Technology System Software UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class TechnologySystemSoftwareUIProvider extends AbstractTechnologyUIProvider {

    public EClass providerFor() {
        return IArchimatePackage.eINSTANCE.getSystemSoftware();
    }
    
    @Override
    public EditPart createEditPart() {
        return new TechnologySystemSoftwareEditPart();
    }

    @Override
    public String getDefaultName() {
        return Messages.TechnologySystemSoftwareUIProvider_0;
    }

    @Override
    public Image getImage() {
        return IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_TECHNOLOGY_SYSTEM_SOFTWARE_16);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_TECHNOLOGY_SYSTEM_SOFTWARE_16);
    }
}
