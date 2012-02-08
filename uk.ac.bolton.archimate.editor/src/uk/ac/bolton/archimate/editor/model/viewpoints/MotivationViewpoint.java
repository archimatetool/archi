/*******************************************************************************
 * Copyright (c) 2012 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.model.viewpoints;

import org.eclipse.emf.ecore.EClass;

import uk.ac.bolton.archimate.model.IArchimatePackage;

/**
 * Motivation Viewpoint
 * 
 * @author Phillip Beauvoir
 */
public class MotivationViewpoint extends AbstractViewpoint {
    
    EClass[] allowed = new EClass[] {
            IArchimatePackage.eINSTANCE.getStakeholder(),
            IArchimatePackage.eINSTANCE.getDriver(),
            IArchimatePackage.eINSTANCE.getAssessment(),
            IArchimatePackage.eINSTANCE.getGoal(),
            IArchimatePackage.eINSTANCE.getPrinciple(),
            IArchimatePackage.eINSTANCE.getRequirement(),
            IArchimatePackage.eINSTANCE.getConstraint(),
            
            IArchimatePackage.eINSTANCE.getAssociationRelationship(),
            IArchimatePackage.eINSTANCE.getRealisationRelationship(),
            IArchimatePackage.eINSTANCE.getAggregationRelationship(),
            IArchimatePackage.eINSTANCE.getSpecialisationRelationship(),
            IArchimatePackage.eINSTANCE.getInfluenceRelationship(),
    };
    
    @Override
    public String getName() {
        return Messages.MotivationViewpoint_0;
    }

    @Override
    public int getIndex() {
        return MOTIVATION_VIEWPOINT;
    }
    
    @Override
    public EClass[] getAllowedTypes() {
        return allowed;
    }
}