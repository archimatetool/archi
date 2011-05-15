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
 * Application Behaviour Viewpoint
 * 
 * @author Phillip Beauvoir
 */
public class ApplicationBehaviourViewpoint extends AbstractViewpoint {
    
    public static final int INDEX = 2;

    EClass[] allowed = new EClass[] {
            IArchimatePackage.eINSTANCE.getDataObject(),
            IArchimatePackage.eINSTANCE.getApplicationService(),
            IArchimatePackage.eINSTANCE.getApplicationFunction(),
            IArchimatePackage.eINSTANCE.getApplicationInteraction(),
            IArchimatePackage.eINSTANCE.getApplicationInterface(),
            IArchimatePackage.eINSTANCE.getApplicationComponent(),
            IArchimatePackage.eINSTANCE.getApplicationCollaboration(),

            IArchimatePackage.eINSTANCE.getJunction(),
            IArchimatePackage.eINSTANCE.getAndJunction(),
            IArchimatePackage.eINSTANCE.getOrJunction()
    };
    
    @Override
    public String getName() {
        return "Application Behaviour";
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