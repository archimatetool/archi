/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.zest;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.zest.core.viewers.IGraphContentProvider;

import com.archimatetool.editor.model.viewpoints.ActorCooperationViewpoint;
import com.archimatetool.editor.model.viewpoints.ApplicationBehaviourViewpoint;
import com.archimatetool.editor.model.viewpoints.ApplicationCooperationViewpoint;
import com.archimatetool.editor.model.viewpoints.ApplicationStructureViewpoint;
import com.archimatetool.editor.model.viewpoints.ApplicationUsageViewpoint;
import com.archimatetool.editor.model.viewpoints.BusinessFunctionViewpoint;
import com.archimatetool.editor.model.viewpoints.BusinessProcessCooperationViewpoint;
import com.archimatetool.editor.model.viewpoints.BusinessProcessViewpoint;
import com.archimatetool.editor.model.viewpoints.BusinessProductViewpoint;
import com.archimatetool.editor.model.viewpoints.GoalContributionViewpoint;
import com.archimatetool.editor.model.viewpoints.GoalRealisationViewpoint;
import com.archimatetool.editor.model.viewpoints.IViewpoint;
import com.archimatetool.editor.model.viewpoints.ImplementationAndDeploymentViewpoint;
import com.archimatetool.editor.model.viewpoints.ImplementationMigrationViewpoint;
import com.archimatetool.editor.model.viewpoints.InformationStructureViewpoint;
import com.archimatetool.editor.model.viewpoints.InfrastructureUsageViewpoint;
import com.archimatetool.editor.model.viewpoints.InfrastructureViewpoint;
import com.archimatetool.editor.model.viewpoints.LayeredViewpoint;
import com.archimatetool.editor.model.viewpoints.MigrationViewpoint;
import com.archimatetool.editor.model.viewpoints.MotivationViewpoint;
import com.archimatetool.editor.model.viewpoints.OrganisationViewpoint;
import com.archimatetool.editor.model.viewpoints.PrinciplesViewpoint;
import com.archimatetool.editor.model.viewpoints.ProjectViewpoint;
import com.archimatetool.editor.model.viewpoints.RequirementsRealisationViewpoint;
import com.archimatetool.editor.model.viewpoints.ServiceRealisationViewpoint;
import com.archimatetool.editor.model.viewpoints.StakeholderViewpoint;
import com.archimatetool.editor.model.viewpoints.TotalViewpoint;
import com.archimatetool.model.IArchimateComponent;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IRelationship;
import com.archimatetool.model.util.ArchimateModelUtils;


/**
 * Graph Viewer Content Provider
 * 
 * @author Phillip Beauvoir
 */
public class ZestViewerContentProvider implements IGraphContentProvider {
	final static int DIR_BOTH = 1;
	final static int DIR_IN = 2;
	final static int DIR_OUT = 3;
    
    private int fDepth = 0;
    private IViewpoint vp = new TotalViewpoint();
    private int orientation = 1;
    
    public void setFilter(int vp) {
		switch (vp) {
		case IViewpoint.TOTAL_VIEWPOINT:
			this.vp = new TotalViewpoint();
			break;
		case IViewpoint.ACTOR_COOPERATION_VIEWPOINT:
			this.vp = new ActorCooperationViewpoint();
			break;
		case IViewpoint.APPLICATION_BEHAVIOUR_VIEWPOINT:
			this.vp = new ApplicationBehaviourViewpoint();
			break;
		case IViewpoint.APPLICATION_COOPERATION_VIEWPOINT:
			this.vp = new ApplicationCooperationViewpoint();
			break;
		case IViewpoint.APPLICATION_STRUCTURE_VIEWPOINT:
			this.vp = new ApplicationStructureViewpoint();
			break;
		case IViewpoint.APPLICATION_USAGE_VIEWPOINT:
			this.vp = new ApplicationUsageViewpoint();
			break;
		case IViewpoint.BUSINESS_FUNCTION_VIEWPOINT:
			this.vp = new BusinessFunctionViewpoint();
			break;
		case IViewpoint.BUSINESS_PROCESS_COOPERATION_VIEWPOINT:
			this.vp = new BusinessProcessCooperationViewpoint();
			break;
		case IViewpoint.BUSINESS_PROCESS_VIEWPOINT:
			this.vp = new BusinessProcessViewpoint();
			break;
		case IViewpoint.BUSINESS_PRODUCT_VIEWPOINT:
			this.vp = new BusinessProductViewpoint();
			break;
		case IViewpoint.IMPLEMENTATION_DEPLOYMENT_VIEWPOINT:
			this.vp = new ImplementationAndDeploymentViewpoint();
			break;
		case IViewpoint.INFORMATION_STRUCTURE_VIEWPOINT:
			this.vp = new InformationStructureViewpoint();
			break;
		case IViewpoint.INFRASTRUCTURE_USAGE_VIEWPOINT:
			this.vp = new InfrastructureUsageViewpoint();
			break;
		case IViewpoint.INFRASTRUCTURE_VIEWPOINT:
			this.vp = new InfrastructureViewpoint();
			break;
		case IViewpoint.LAYERED_VIEWPOINT:
			this.vp = new LayeredViewpoint();
			break;
		case IViewpoint.ORGANISATION_VIEWPOINT:
			this.vp = new OrganisationViewpoint();
			break;
		case IViewpoint.SERVICE_REALISATION_VIEWPOINT:
			this.vp = new ServiceRealisationViewpoint();
			break;
		case IViewpoint.STAKEHOLDER_VIEWPOINT:
			this.vp = new StakeholderViewpoint();
			break;
		case IViewpoint.GOAL_REALISATION_VIEWPOINT:
			this.vp = new GoalRealisationViewpoint();
			break;
		case IViewpoint.GOAL_CONTRIBUTION_VIEWPOINT:
			this.vp = new GoalContributionViewpoint();
			break;
		case IViewpoint.PRINCIPLES_VIEWPOINT:
			this.vp = new PrinciplesViewpoint();
			break;
		case IViewpoint.REQUIREMENTS_REALISATION_VIEWPOINT:
			this.vp = new RequirementsRealisationViewpoint();
			break;
		case IViewpoint.MOTIVATION_VIEWPOINT:
			this.vp = new MotivationViewpoint();
			break;
		case IViewpoint.PROJECT_VIEWPOINT:
			this.vp = new ProjectViewpoint();
			break;
		case IViewpoint.MIGRATION_VIEWPOINT:
			this.vp = new MigrationViewpoint();
			break;
		case IViewpoint.IMPLEMENTATION_MIGRATION_VIEWPOINT:
			this.vp = new ImplementationMigrationViewpoint();
			break;
		default:
			// Just in case...
			this.vp = new TotalViewpoint();	
		}
    }
    
    public void setOrientation(int orientation) {
    	if (orientation == DIR_BOTH || orientation == DIR_IN || orientation == DIR_OUT) {
    		this.orientation = orientation;
    	}
    }
    
    public void setDepth(int depth) {
        fDepth = depth;
    }
    
    public int getDepth() {
        return fDepth;
    }
    
    @Override
    public void dispose() {
    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }

    @Override
    public Object[] getElements(Object inputElement) {
        if(inputElement instanceof IArchimateComponent) {
            IArchimateComponent archimateComponent = (IArchimateComponent)inputElement;
            
            // Check if it was deleted
            if(archimateComponent.eContainer() == null) {
                return new Object[0];
            }
            
            // Relationship
            if(archimateComponent instanceof IRelationship) {
                return new Object[]  { inputElement };
            }

            // Element - Get its relationships
            List<IRelationship> mainList = new ArrayList<IRelationship>();
            getRelations(mainList, new ArrayList<IArchimateElement>(), (IArchimateElement)archimateComponent, 0);
            
            return mainList.toArray();
        }
        
        return new Object[0];
    }
    
    /**
     * Get all relations from source and target of element and add to list, no more than DEPTH
     */
    private void getRelations(List<IRelationship> mainList, List<IArchimateElement> checkList, IArchimateElement element, int count) {
        if(checkList.contains(element)) {
            return;
        }
        
        checkList.add(element);
        
        if(count > fDepth) {
            return;
        }
        count++;
        
        List<IRelationship> list = ArchimateModelUtils.getRelationships(element);
        
        for(IRelationship relationship : list) {
        	if (vp.isAllowedType(relationship.eClass())) {
	            //IArchimateElement source = relationship.getSource();
	            //IArchimateElement target = relationship.getTarget();
	            IArchimateElement other = relationship.getSource().equals(element) ? relationship.getTarget() : relationship.getSource();
	            int direction = relationship.getSource().equals(element) ? DIR_OUT : DIR_IN;
	            
	            if (!mainList.contains(relationship) && vp.isAllowedType(other.eClass())) {
	            	if (direction == orientation || orientation == DIR_BOTH)
	            		mainList.add(relationship);
	            }
	            
	            if (vp.isAllowedType(((IArchimateElement)other).eClass())) {
	            	if (direction == orientation || orientation == DIR_BOTH)
	            		getRelations(mainList, checkList, other, count);
	            }
	            /*
	            if (vp.isAllowedType(((IArchimateElement)target).eClass())) {
	            	getRelations(mainList, checkList, target, count);	
	            } */
        	}
        }
    }
    
    @Override
    public Object getSource(Object rel) {
        if(rel instanceof IRelationship) {
            return ((IRelationship)rel).getSource();
        }
        return null;
    }

    @Override
    public Object getDestination(Object rel) {
        if(rel instanceof IRelationship) {
            return ((IRelationship)rel).getTarget();
        }
        return null;
    }
}
