/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.model.viewpoints;

import org.eclipse.emf.ecore.EClass;

import uk.ac.bolton.archimate.model.IArchimatePackage;

/**
 * Infrastructure Usage Viewpoint
 * 
 * @author Phillip Beauvoir
 */
public class InfrastructureUsageViewpoint extends AbstractViewpoint {
    
    public static final int INDEX = 12;
    
    EClass[] allowed = new EClass[] {
            IArchimatePackage.eINSTANCE.getApplicationComponent(),

            IArchimatePackage.eINSTANCE.getDevice(),
            IArchimatePackage.eINSTANCE.getNode(),
            IArchimatePackage.eINSTANCE.getInfrastructureService(),
            IArchimatePackage.eINSTANCE.getCommunicationPath(),
            IArchimatePackage.eINSTANCE.getSystemSoftware(),

            IArchimatePackage.eINSTANCE.getJunction(),
            IArchimatePackage.eINSTANCE.getAndJunction(),
            IArchimatePackage.eINSTANCE.getOrJunction()
    };
    
    @Override
    public String getName() {
        return "Infrastructure Usage";
    }

    @Override
    public int getIndex() {
        return INDEX;
    }

    @Override
    public EClass[] getAllowedTypes() {
        return allowed;
    }
}