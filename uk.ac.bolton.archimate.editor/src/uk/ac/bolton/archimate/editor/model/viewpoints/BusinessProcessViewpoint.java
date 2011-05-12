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
 * Business Process Viewpoint
 * 
 * @author Phillip Beauvoir
 */
public class BusinessProcessViewpoint extends AbstractViewpoint {
    
    EClass[] allowed = new EClass[] {
            IArchimatePackage.eINSTANCE.getRepresentation(),
            IArchimatePackage.eINSTANCE.getBusinessObject(),
            IArchimatePackage.eINSTANCE.getBusinessService(),
            IArchimatePackage.eINSTANCE.getBusinessEvent(),
            IArchimatePackage.eINSTANCE.getBusinessProcess(),
            IArchimatePackage.eINSTANCE.getBusinessInteraction(),
            IArchimatePackage.eINSTANCE.getBusinessCollaboration(),
            IArchimatePackage.eINSTANCE.getBusinessRole(),
            IArchimatePackage.eINSTANCE.getBusinessActor(),
            IArchimatePackage.eINSTANCE.getBusinessActivity(), // Should this be here?
            IArchimatePackage.eINSTANCE.getBusinessInterface(), // Should this be here?
            
            IArchimatePackage.eINSTANCE.getJunction(),
            IArchimatePackage.eINSTANCE.getAndJunction(),
            IArchimatePackage.eINSTANCE.getOrJunction()
    };
    
    @Override
    public String getName() {
        return "Business Process";
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