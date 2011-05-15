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
 * Business Product Viewpoint
 * 
 * @author Phillip Beauvoir
 */
public class BusinessProductViewpoint extends AbstractViewpoint {
    
    public static final int INDEX = 9;
    
    EClass[] allowed = new EClass[] {
            IArchimatePackage.eINSTANCE.getProduct(),
            IArchimatePackage.eINSTANCE.getContract(),
            IArchimatePackage.eINSTANCE.getValue(),
            IArchimatePackage.eINSTANCE.getBusinessService(),
            IArchimatePackage.eINSTANCE.getBusinessEvent(),
            IArchimatePackage.eINSTANCE.getBusinessProcess(),
            IArchimatePackage.eINSTANCE.getBusinessInterface(),
            IArchimatePackage.eINSTANCE.getBusinessRole(),
            IArchimatePackage.eINSTANCE.getBusinessActor(),
            IArchimatePackage.eINSTANCE.getBusinessActivity(), // Should this be here?

            IArchimatePackage.eINSTANCE.getApplicationService(),
            IArchimatePackage.eINSTANCE.getApplicationInterface(),
            IArchimatePackage.eINSTANCE.getApplicationComponent(),

            IArchimatePackage.eINSTANCE.getJunction(),
            IArchimatePackage.eINSTANCE.getAndJunction(),
            IArchimatePackage.eINSTANCE.getOrJunction()
    };

    @Override
    public String getName() {
        return "Business Product";
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