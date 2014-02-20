/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model.viewpoints;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

/**
 * IViewpoint
 * 
 * @author Phillip Beauvoir
 */
public interface IViewpoint {
    
    /*
     * Index numbers of Viewpoints
     */
    int TOTAL_VIEWPOINT = 0;
    int ACTOR_COOPERATION_VIEWPOINT = 1;
    int APPLICATION_BEHAVIOUR_VIEWPOINT = 2;
    int APPLICATION_COOPERATION_VIEWPOINT = 3;
    int APPLICATION_STRUCTURE_VIEWPOINT = 4;
    int APPLICATION_USAGE_VIEWPOINT = 5;
    int BUSINESS_FUNCTION_VIEWPOINT = 6;
    int BUSINESS_PROCESS_COOPERATION_VIEWPOINT = 7;
    int BUSINESS_PROCESS_VIEWPOINT = 8;
    int BUSINESS_PRODUCT_VIEWPOINT = 9;
    int IMPLEMENTATION_DEPLOYMENT_VIEWPOINT = 10;
    int INFORMATION_STRUCTURE_VIEWPOINT = 11;
    int INFRASTRUCTURE_USAGE_VIEWPOINT = 12;
    int INFRASTRUCTURE_VIEWPOINT = 13;
    int LAYERED_VIEWPOINT = 14;
    int ORGANISATION_VIEWPOINT = 15;
    int SERVICE_REALISATION_VIEWPOINT = 16;
    
    int STAKEHOLDER_VIEWPOINT = 17;
    int GOAL_REALISATION_VIEWPOINT = 18;
    int GOAL_CONTRIBUTION_VIEWPOINT = 19;
    int PRINCIPLES_VIEWPOINT = 20;
    int REQUIREMENTS_REALISATION_VIEWPOINT = 21;
    int MOTIVATION_VIEWPOINT = 22;
    
    int PROJECT_VIEWPOINT = 23;
    int MIGRATION_VIEWPOINT = 24;
    int IMPLEMENTATION_MIGRATION_VIEWPOINT = 25;

    /**
     * @return The index number used when persisting the Viewpoint
     */
    int getIndex();

    /**
     * @param object
     * @return Whether the object is visible
     */
    boolean isElementVisible(EObject object);

    /**
     * @return Allowed types
     */
    EClass[] getAllowedTypes();
    
    /**
     * @return true if type is an allowed Archimate element type class
     */
    boolean isAllowedType(EClass type);
    
    /**
     * @return The name of the Viewpoint
     */
    String getName();
}