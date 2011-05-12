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
 * Application Usage Viewpoint
 * 
 * @author Phillip Beauvoir
 */
public class ApplicationUsageViewpoint extends AbstractViewpoint {
    
    EClass[] allowed = new EClass[] {
            IArchimatePackage.eINSTANCE.getBusinessActor(),
            IArchimatePackage.eINSTANCE.getBusinessRole(),
            IArchimatePackage.eINSTANCE.getBusinessCollaboration(),
            IArchimatePackage.eINSTANCE.getBusinessProcess(),
            IArchimatePackage.eINSTANCE.getBusinessFunction(),
            IArchimatePackage.eINSTANCE.getBusinessInteraction(),
            IArchimatePackage.eINSTANCE.getBusinessActivity(),
            IArchimatePackage.eINSTANCE.getBusinessObject(),

            IArchimatePackage.eINSTANCE.getApplicationComponent(),
            IArchimatePackage.eINSTANCE.getApplicationCollaboration(),
            IArchimatePackage.eINSTANCE.getApplicationInterface(),
            IArchimatePackage.eINSTANCE.getApplicationFunction(),
            IArchimatePackage.eINSTANCE.getApplicationInteraction(),
            IArchimatePackage.eINSTANCE.getApplicationService(),
            IArchimatePackage.eINSTANCE.getDataObject(),

            IArchimatePackage.eINSTANCE.getJunction(),
            IArchimatePackage.eINSTANCE.getAndJunction(),
            IArchimatePackage.eINSTANCE.getOrJunction()
    };
    
    @Override
    public String getName() {
        return "Application Usage";
    }

    @Override
    public EClass[] getAllowedTypes() {
        return allowed;
    }
    
    @Override
    public Image getImage() {
        String[] imageNames = { IArchimateImages.ICON_VIEWPOINT_BUSINESS_16, IArchimateImages.ICON_VIEWPOINT_APPLICATION_16 };
        return IArchimateImages.ImageFactory.getCompositeImage(imageNames);
    }
}