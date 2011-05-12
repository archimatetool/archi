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
 * Infrastructure Viewpoint
 * 
 * @author Phillip Beauvoir
 */
public class InfrastructureViewpoint extends AbstractViewpoint {
    
    EClass[] allowed = new EClass[] {
            IArchimatePackage.eINSTANCE.getDevice(),
            IArchimatePackage.eINSTANCE.getNode(),
            IArchimatePackage.eINSTANCE.getInfrastructureInterface(),
            IArchimatePackage.eINSTANCE.getInfrastructureService(),
            IArchimatePackage.eINSTANCE.getCommunicationPath(),
            IArchimatePackage.eINSTANCE.getSystemSoftware(),
            IArchimatePackage.eINSTANCE.getNetwork(),
            IArchimatePackage.eINSTANCE.getArtifact(),

            IArchimatePackage.eINSTANCE.getJunction(),
            IArchimatePackage.eINSTANCE.getAndJunction(),
            IArchimatePackage.eINSTANCE.getOrJunction()
    };
    
    @Override
    public String getName() {
        return "Infrastructure";
    }

    @Override
    public EClass[] getAllowedTypes() {
        return allowed;
    }

    @Override
    public Image getImage() {
        String[] imageNames = { IArchimateImages.ICON_VIEWPOINT_TECHNOLOGY_16 };
        return IArchimateImages.ImageFactory.getCompositeImage(imageNames);
    }
}