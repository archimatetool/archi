/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.model.viewpoints;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;

import uk.ac.bolton.archimate.model.IDiagramModel;
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
     * All Viewpoints. The order of these determones the index number saved in the model.
     */
    private static List<IViewpoint> fViewpoints = new ArrayList<IViewpoint>();
    static {
        fViewpoints.add(new TotalViewpoint()); // Ensure this one is zero
        fViewpoints.add(new ActorCooperationViewpoint());
        fViewpoints.add(new ApplicationBehaviourViewpoint());
        fViewpoints.add(new ApplicationCooperationViewpoint());
        fViewpoints.add(new ApplicationStructureViewpoint());
        fViewpoints.add(new ApplicationUsageViewpoint());
        fViewpoints.add(new BusinessFunctionViewpoint());
        fViewpoints.add(new BusinessProcessViewpoint());
        fViewpoints.add(new BusinessProcessCooperationViewpoint());
        fViewpoints.add(new BusinessProductViewpoint());
        fViewpoints.add(new ImplementationAndDeploymentViewpoint());
        fViewpoints.add(new InformationStructureViewpoint());
        fViewpoints.add(new InfrastructureViewpoint());
        fViewpoints.add(new InfrastructureUsageViewpoint());
        fViewpoints.add(new LayeredViewpoint());
        fViewpoints.add(new OrganisationViewpoint());
        fViewpoints.add(new ServiceRealisationViewpoint());
    }
    
    /**
     * @return A list of all Viewpoints
     */
    public List<IViewpoint> getAllViewpoints() {
        return fViewpoints;
    }
    
    public static ViewpointsManager INSTANCE = new ViewpointsManager();
    
    private ViewpointsManager() {
    }
    
    /**
     * @param index
     * @return A Viewpoint by its index
     */
    public IViewpoint getViewpoint(int index) {
        if(index < 0 || index >= fViewpoints.size()) {
            index = 0;
        }
        return getAllViewpoints().get(index);
    }
    
    /**
     * @param viewPoint
     * @return Index of Viewpoint
     */
    public int getViewpointIndex(IViewpoint viewPoint) {
        return getAllViewpoints().indexOf(viewPoint);
    }
    
    public boolean isAllowedType(IDiagramModelComponent dmo) {
        if(dmo instanceof IDiagramModelArchimateObject) {
            EClass eClass = ((IDiagramModelArchimateObject)dmo).getArchimateElement().eClass();
            return isAllowedType(dmo.getDiagramModel(), eClass);
        }
        if(dmo instanceof IDiagramModelConnection) {
            return isAllowedType(((IDiagramModelConnection)dmo).getSource()) && 
                        isAllowedType(((IDiagramModelConnection)dmo).getTarget());
        }
        return true;
    }
    
    public boolean isAllowedType(IDiagramModel dm, EClass eClass) {
        if(dm != null) {
            IViewpoint viewPoint = getViewpoint(dm.getViewpoint());
            return viewPoint.isAllowedType(eClass);
        }
        return true;
    }
}
