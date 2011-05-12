/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.model.viewpoints;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.swt.graphics.Image;

import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.model.IArchimatePackage;

/**
 * Organisation Structure Viewpoint
 * 
 * @author Phillip Beauvoir
 */
public class OrganisationViewpoint extends AbstractViewpoint {
    
    EClass[] allowed = new EClass[] {
            IArchimatePackage.eINSTANCE.getBusinessActor(),
            IArchimatePackage.eINSTANCE.getBusinessCollaboration(),
            IArchimatePackage.eINSTANCE.getBusinessRole(),
            IArchimatePackage.eINSTANCE.getBusinessInterface(),

            IArchimatePackage.eINSTANCE.getJunction(),
            IArchimatePackage.eINSTANCE.getAndJunction(),
            IArchimatePackage.eINSTANCE.getOrJunction()
    };
    
    @Override
    public String getName() {
        return "Organisation";
    }

    @Override
    public EClass[] getAllowedTypes() {
        return allowed;
    }
    
    @Override
    public Image getImage() {
        String[] imageNames = { IArchimateImages.ICON_VIEWPOINT_BUSINESS_16 };
        return IArchimateImages.ImageFactory.getCompositeImage(imageNames);
    }
}