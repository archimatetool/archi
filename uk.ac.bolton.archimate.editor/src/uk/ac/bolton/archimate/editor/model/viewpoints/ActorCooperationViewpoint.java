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
 * Actor Co-operation Viewpoint
 * 
 * @author Phillip Beauvoir
 */
public class ActorCooperationViewpoint extends AbstractViewpoint {
    
    public static final int INDEX = 1;
    
    EClass[] allowed = new EClass[] {
            IArchimatePackage.eINSTANCE.getBusinessActor(),
            IArchimatePackage.eINSTANCE.getBusinessRole(),
            IArchimatePackage.eINSTANCE.getBusinessCollaboration(),
            IArchimatePackage.eINSTANCE.getBusinessService(),
            IArchimatePackage.eINSTANCE.getBusinessInterface(),

            IArchimatePackage.eINSTANCE.getApplicationInterface(),
            IArchimatePackage.eINSTANCE.getApplicationComponent(),
            IArchimatePackage.eINSTANCE.getApplicationService(),

            IArchimatePackage.eINSTANCE.getJunction(),
            IArchimatePackage.eINSTANCE.getAndJunction(),
            IArchimatePackage.eINSTANCE.getOrJunction()
    };
    
    @Override
    public String getName() {
        return "Actor Co-operation";
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