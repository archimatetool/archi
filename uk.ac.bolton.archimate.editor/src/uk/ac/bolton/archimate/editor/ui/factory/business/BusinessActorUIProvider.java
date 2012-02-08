/*******************************************************************************
 * Copyright (c) 2012 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.ui.factory.business;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import uk.ac.bolton.archimate.editor.diagram.editparts.business.BusinessActorEditPart;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.model.IArchimatePackage;


/**
 * Business Actor UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class BusinessActorUIProvider extends AbstractBusinessUIProvider {

    public EClass providerFor() {
        return IArchimatePackage.eINSTANCE.getBusinessActor();
    }
    
    @Override
    public EditPart createEditPart() {
        return new BusinessActorEditPart();
    }

    @Override
    public String getDefaultName() {
        return Messages.BusinessActorUIProvider_0;
    }

    @Override
    public String getDefaultShortName() {
        return Messages.BusinessActorUIProvider_1;
    }

    @Override
    public Image getImage() {
        return IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_BUSINESS_ACTOR_16);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_BUSINESS_ACTOR_16);
    }
}
