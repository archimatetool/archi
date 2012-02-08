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
 * Goal Realisation Viewpoint
 * 
 * @author Phillip Beauvoir
 */
public class GoalRealisationViewpoint extends AbstractViewpoint {
    
    EClass[] allowed = new EClass[] {
            IArchimatePackage.eINSTANCE.getGoal(),
            IArchimatePackage.eINSTANCE.getPrinciple(),
            IArchimatePackage.eINSTANCE.getRequirement(),
            IArchimatePackage.eINSTANCE.getConstraint(),
            
            IArchimatePackage.eINSTANCE.getRealisationRelationship(),
            IArchimatePackage.eINSTANCE.getAggregationRelationship(),
            IArchimatePackage.eINSTANCE.getSpecialisationRelationship()
    };
    
    @Override
    public String getName() {
        return Messages.GoalRealisationViewpoint_0;
    }

    @Override
    public int getIndex() {
        return GOAL_REALISATION_VIEWPOINT;
    }
    
    @Override
    public EClass[] getAllowedTypes() {
        return allowed;
    }
}