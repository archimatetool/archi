/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model.viewpoints;

import org.eclipse.emf.ecore.EClass;

import com.archimatetool.model.IArchimatePackage;


/**
 * Stakeholder Viewpoint
 * 
 * @author Phillip Beauvoir
 */
public class StakeholderViewpoint extends AbstractViewpoint {
    
    EClass[] allowed = new EClass[] {
            IArchimatePackage.eINSTANCE.getStakeholder(),
            IArchimatePackage.eINSTANCE.getDriver(),
            IArchimatePackage.eINSTANCE.getGoal(),
            IArchimatePackage.eINSTANCE.getAssessment(),
            
            IArchimatePackage.eINSTANCE.getAssociationRelationship(),
            IArchimatePackage.eINSTANCE.getAggregationRelationship(),
            IArchimatePackage.eINSTANCE.getSpecialisationRelationship(),
    };
    
    @Override
    public String getName() {
        return Messages.StakeholderViewpoint_0;
    }

    @Override
    public int getIndex() {
        return STAKEHOLDER_VIEWPOINT;
    }
    
    @Override
    public EClass[] getAllowedTypes() {
        return allowed;
    }
}