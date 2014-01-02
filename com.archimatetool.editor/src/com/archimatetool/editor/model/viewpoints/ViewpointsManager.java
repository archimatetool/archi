/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model.viewpoints;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jface.resource.ImageDescriptor;

import com.archimatetool.editor.ui.IArchimateImages;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.IDiagramModelConnection;



/**
 * Viewpoints Manager
 * 
 * @author Phillip Beauvoir
 */
public class ViewpointsManager {
    
    /*
     * All Viewpoints
     */
    private static List<IViewpoint> VIEWPOINTS = new ArrayList<IViewpoint>();
    
    static {
        VIEWPOINTS.add(new ActorCooperationViewpoint());
        VIEWPOINTS.add(new ApplicationBehaviourViewpoint());
        VIEWPOINTS.add(new ApplicationCooperationViewpoint());
        VIEWPOINTS.add(new ApplicationStructureViewpoint());
        VIEWPOINTS.add(new ApplicationUsageViewpoint());
        VIEWPOINTS.add(new BusinessFunctionViewpoint());
        VIEWPOINTS.add(new BusinessProcessCooperationViewpoint());
        VIEWPOINTS.add(new BusinessProcessViewpoint());
        VIEWPOINTS.add(new BusinessProductViewpoint());
        VIEWPOINTS.add(new ImplementationAndDeploymentViewpoint());
        VIEWPOINTS.add(new InformationStructureViewpoint());
        VIEWPOINTS.add(new InfrastructureUsageViewpoint());
        VIEWPOINTS.add(new InfrastructureViewpoint());
        VIEWPOINTS.add(new LayeredViewpoint());
        VIEWPOINTS.add(new OrganisationViewpoint());
        VIEWPOINTS.add(new ServiceRealisationViewpoint());
        VIEWPOINTS.add(new TotalViewpoint());
        
        VIEWPOINTS.add(new StakeholderViewpoint());
        VIEWPOINTS.add(new GoalRealisationViewpoint());
        VIEWPOINTS.add(new GoalContributionViewpoint());
        VIEWPOINTS.add(new PrinciplesViewpoint());
        VIEWPOINTS.add(new RequirementsRealisationViewpoint());
        VIEWPOINTS.add(new MotivationViewpoint());
        
        VIEWPOINTS.add(new ProjectViewpoint());
        VIEWPOINTS.add(new MigrationViewpoint());
        VIEWPOINTS.add(new ImplementationMigrationViewpoint());

        // Sort the Viewpoints by name
        Collections.sort(VIEWPOINTS, new Comparator<IViewpoint>() {
            @Override
            public int compare(IViewpoint vp1, IViewpoint vp2) {
                return vp1.getName().compareTo(vp2.getName());
            }
        });
    }
    
    public static ViewpointsManager INSTANCE = new ViewpointsManager();
    
    /**
     * @param viewPoint
     * @return an ImageDesciptor for a Viewpoint
     */
    public ImageDescriptor getImageDescriptor(IViewpoint viewPoint) {
        String[] bus_appNames = { IArchimateImages.ICON_VIEWPOINT_BUSINESS_16,
                IArchimateImages.ICON_VIEWPOINT_APPLICATION_16 };
        
        String[] app_techNames = { IArchimateImages.ICON_VIEWPOINT_APPLICATION_16,
                IArchimateImages.ICON_VIEWPOINT_TECHNOLOGY_16 };
        
        switch(viewPoint.getIndex()) {
            case IViewpoint.BUSINESS_FUNCTION_VIEWPOINT:
            case IViewpoint.BUSINESS_PROCESS_VIEWPOINT:
            case IViewpoint.ORGANISATION_VIEWPOINT:
                return IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_VIEWPOINT_BUSINESS_16);

            case IViewpoint.APPLICATION_BEHAVIOUR_VIEWPOINT:
            case IViewpoint.APPLICATION_COOPERATION_VIEWPOINT:
            case IViewpoint.APPLICATION_STRUCTURE_VIEWPOINT:
                return IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_VIEWPOINT_APPLICATION_16);
                
            case IViewpoint.INFRASTRUCTURE_VIEWPOINT:
                return IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_VIEWPOINT_TECHNOLOGY_16);

            case IViewpoint.ACTOR_COOPERATION_VIEWPOINT:
            case IViewpoint.APPLICATION_USAGE_VIEWPOINT:
            case IViewpoint.BUSINESS_PROCESS_COOPERATION_VIEWPOINT:
            case IViewpoint.BUSINESS_PRODUCT_VIEWPOINT:
            case IViewpoint.SERVICE_REALISATION_VIEWPOINT:
                return IArchimateImages.ImageFactory.getCompositeImageDescriptor(bus_appNames);
                
            case IViewpoint.IMPLEMENTATION_DEPLOYMENT_VIEWPOINT:
            case IViewpoint.INFRASTRUCTURE_USAGE_VIEWPOINT:
                return IArchimateImages.ImageFactory.getCompositeImageDescriptor(app_techNames);
                
            default:
                return IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_VIEWPOINTS_16);
        }
    }
    
    private ViewpointsManager() {
    }
    
    /**
     * @return A list of all Viewpoints
     */
    public List<IViewpoint> getAllViewpoints() {
        return VIEWPOINTS;
    }
    
    /**
     * @param index
     * @return A Viewpoint by its index
     */
    public IViewpoint getViewpoint(int index) {
        if(index < 0 || index >= VIEWPOINTS.size()) {
            return VIEWPOINTS.get(0);
        }
        
        for(IViewpoint vp : VIEWPOINTS) {
            if(vp.getIndex() == index) {
                return vp;
            }
        }
        
        return VIEWPOINTS.get(0);
    }
    
    /**
     * @param dmo
     * @return True if dmo is an allowed component for this Viewpoint
     */
    public boolean isAllowedType(IDiagramModelComponent dmo) {
        if(dmo instanceof IDiagramModelArchimateObject && dmo.getDiagramModel() instanceof IArchimateDiagramModel) {
            EClass eClass = ((IDiagramModelArchimateObject)dmo).getArchimateElement().eClass();
            return isAllowedType((IArchimateDiagramModel)dmo.getDiagramModel(), eClass);
        }
        if(dmo instanceof IDiagramModelConnection) {
            return isAllowedType(((IDiagramModelConnection)dmo).getSource()) && 
                        isAllowedType(((IDiagramModelConnection)dmo).getTarget());
        }
        return true;
    }
    
    /**
     * @param dm
     * @param eClass
     * @return True if eClass is an allowed component for this Viewpoint
     */
    public boolean isAllowedType(IArchimateDiagramModel dm, EClass eClass) {
        if(dm != null) {
            IViewpoint viewPoint = getViewpoint(dm.getViewpoint());
            return viewPoint.isAllowedType(eClass);
        }
        return true;
    }
}
