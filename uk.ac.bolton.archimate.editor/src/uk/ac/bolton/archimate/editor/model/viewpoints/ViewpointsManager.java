/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.model.viewpoints;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jface.resource.ImageDescriptor;

import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.model.IArchimateDiagramModel;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;
import uk.ac.bolton.archimate.model.IDiagramModelComponent;
import uk.ac.bolton.archimate.model.IDiagramModelConnection;


/**
 * Viewpoints Manager
 * 
 * @author Phillip Beauvoir
 */
public class ViewpointsManager {
    
    /*
     * All Viewpoints
     */
    private static List<IViewpoint> fViewpoints = new ArrayList<IViewpoint>();
    
    static {
        fViewpoints.add(new ActorCooperationViewpoint());
        fViewpoints.add(new ApplicationBehaviourViewpoint());
        fViewpoints.add(new ApplicationCooperationViewpoint());
        fViewpoints.add(new ApplicationStructureViewpoint());
        fViewpoints.add(new ApplicationUsageViewpoint());
        fViewpoints.add(new BusinessFunctionViewpoint());
        fViewpoints.add(new BusinessProcessCooperationViewpoint());
        fViewpoints.add(new BusinessProcessViewpoint());
        fViewpoints.add(new BusinessProductViewpoint());
        fViewpoints.add(new ImplementationAndDeploymentViewpoint());
        fViewpoints.add(new InformationStructureViewpoint());
        fViewpoints.add(new InfrastructureUsageViewpoint());
        fViewpoints.add(new InfrastructureViewpoint());
        fViewpoints.add(new LayeredViewpoint());
        fViewpoints.add(new OrganisationViewpoint());
        fViewpoints.add(new ServiceRealisationViewpoint());
        fViewpoints.add(new TotalViewpoint());
        
        // Sort the Viewpoints by name
        Collections.sort(fViewpoints, new Comparator<IViewpoint>() {
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
            case BusinessFunctionViewpoint.INDEX:
            case BusinessProcessViewpoint.INDEX:
            case OrganisationViewpoint.INDEX:
                return IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_VIEWPOINT_BUSINESS_16);

            case ApplicationBehaviourViewpoint.INDEX:
            case ApplicationCooperationViewpoint.INDEX:
            case ApplicationStructureViewpoint.INDEX:
                return IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_VIEWPOINT_APPLICATION_16);
                
            case InfrastructureViewpoint.INDEX:
                return IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_VIEWPOINT_TECHNOLOGY_16);

            case ActorCooperationViewpoint.INDEX:
            case ApplicationUsageViewpoint.INDEX:
            case BusinessProcessCooperationViewpoint.INDEX:
            case BusinessProductViewpoint.INDEX:
            case ServiceRealisationViewpoint.INDEX:
                return IArchimateImages.ImageFactory.getCompositeImageDescriptor(bus_appNames);
                
            case ImplementationAndDeploymentViewpoint.INDEX:
            case InfrastructureUsageViewpoint.INDEX:
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
        return fViewpoints;
    }
    
    /**
     * @param index
     * @return A Viewpoint by its index
     */
    public IViewpoint getViewpoint(int index) {
        if(index < 0 || index >= fViewpoints.size()) {
            return fViewpoints.get(0);
        }
        
        for(IViewpoint vp : fViewpoints) {
            if(vp.getIndex() == index) {
                return vp;
            }
        }
        
        return fViewpoints.get(0);
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
