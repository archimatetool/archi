/*******************************************************************************
 * Copyright (c) 2012 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.ui.factory;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;

import uk.ac.bolton.archimate.editor.ui.factory.application.ApplicationCollaborationUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.application.ApplicationComponentUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.application.ApplicationDataObjectUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.application.ApplicationFunctionUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.application.ApplicationInteractionUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.application.ApplicationInterfaceUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.application.ApplicationServiceUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.business.BusinessActivityUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.business.BusinessActorUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.business.BusinessCollaborationUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.business.BusinessContractUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.business.BusinessEventUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.business.BusinessFunctionUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.business.BusinessInteractionUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.business.BusinessInterfaceUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.business.BusinessLocationUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.business.BusinessMeaningUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.business.BusinessObjectUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.business.BusinessProcessUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.business.BusinessProductUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.business.BusinessRepresentationUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.business.BusinessRoleUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.business.BusinessServiceUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.business.BusinessValueUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.connections.AccessConnectionUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.connections.AggregationConnectionUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.connections.AssignmentConnectionUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.connections.AssociationConnectionUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.connections.CompositionConnectionUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.connections.FlowConnectionUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.connections.InfluenceConnectionUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.connections.LineConnectionUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.connections.RealisationConnectionUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.connections.SpecialisationConnectionUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.connections.TriggeringConnectionUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.connections.UsedByConnectionUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.diagram.DiagramModelReferenceUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.diagram.GroupUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.diagram.NoteUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.extensions.AssessmentUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.extensions.ConstraintUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.extensions.DeliverableUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.extensions.DriverUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.extensions.GapUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.extensions.GoalUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.extensions.PlateauUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.extensions.PrincipleUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.extensions.RequirementUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.extensions.StakeholderUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.extensions.WorkPackageUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.junctions.AndJunctionUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.junctions.JunctionUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.junctions.OrJunctionUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.sketch.SketchActorUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.sketch.SketchStickyUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.technology.TechnologyArtifactUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.technology.TechnologyCommunicationPathUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.technology.TechnologyDeviceUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.technology.TechnologyInfrastructureFunctionUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.technology.TechnologyInfrastructureInterfaceUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.technology.TechnologyInfrastructureServiceUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.technology.TechnologyNetworkUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.technology.TechnologyNodeUIProvider;
import uk.ac.bolton.archimate.editor.ui.factory.technology.TechnologySystemSoftwareUIProvider;


/**
 * Factory for Element UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class ElementUIFactory {

    public static final ElementUIFactory INSTANCE = new ElementUIFactory();
    
    private ElementUIFactory() {
        registerProvider(new BusinessActorUIProvider());
        registerProvider(new BusinessInterfaceUIProvider());
        registerProvider(new BusinessActivityUIProvider());
        registerProvider(new BusinessCollaborationUIProvider());
        registerProvider(new BusinessContractUIProvider());
        registerProvider(new BusinessEventUIProvider());
        registerProvider(new BusinessFunctionUIProvider());
        registerProvider(new BusinessInteractionUIProvider());
        registerProvider(new BusinessMeaningUIProvider());
        registerProvider(new BusinessObjectUIProvider());
        registerProvider(new BusinessProcessUIProvider());
        registerProvider(new BusinessProductUIProvider());
        registerProvider(new BusinessRepresentationUIProvider());
        registerProvider(new BusinessRoleUIProvider());
        registerProvider(new BusinessServiceUIProvider());
        registerProvider(new BusinessValueUIProvider());
        registerProvider(new BusinessLocationUIProvider());
        
        registerProvider(new ApplicationCollaborationUIProvider());
        registerProvider(new ApplicationComponentUIProvider());
        registerProvider(new ApplicationFunctionUIProvider());
        registerProvider(new ApplicationInteractionUIProvider());
        registerProvider(new ApplicationInterfaceUIProvider());
        registerProvider(new ApplicationDataObjectUIProvider());
        registerProvider(new ApplicationServiceUIProvider());
        
        registerProvider(new TechnologyArtifactUIProvider());
        registerProvider(new TechnologyCommunicationPathUIProvider());
        registerProvider(new TechnologyNetworkUIProvider());
        registerProvider(new TechnologyInfrastructureInterfaceUIProvider());
        registerProvider(new TechnologyInfrastructureServiceUIProvider());
        registerProvider(new TechnologyNodeUIProvider());
        registerProvider(new TechnologySystemSoftwareUIProvider());
        registerProvider(new TechnologyDeviceUIProvider());
        registerProvider(new TechnologyInfrastructureFunctionUIProvider());
        
        registerProvider(new JunctionUIProvider());
        registerProvider(new AndJunctionUIProvider());
        registerProvider(new OrJunctionUIProvider());
        
        registerProvider(new AccessConnectionUIProvider());
        registerProvider(new AggregationConnectionUIProvider());
        registerProvider(new AssignmentConnectionUIProvider());
        registerProvider(new AssociationConnectionUIProvider());
        registerProvider(new CompositionConnectionUIProvider());
        registerProvider(new FlowConnectionUIProvider());
        registerProvider(new RealisationConnectionUIProvider());
        registerProvider(new SpecialisationConnectionUIProvider());
        registerProvider(new TriggeringConnectionUIProvider());
        registerProvider(new UsedByConnectionUIProvider());
        registerProvider(new InfluenceConnectionUIProvider());
        
        registerProvider(new StakeholderUIProvider());
        registerProvider(new DriverUIProvider());
        registerProvider(new AssessmentUIProvider());
        registerProvider(new GoalUIProvider());
        registerProvider(new PrincipleUIProvider());
        registerProvider(new RequirementUIProvider());
        registerProvider(new ConstraintUIProvider());
        
        registerProvider(new WorkPackageUIProvider());
        registerProvider(new DeliverableUIProvider());
        registerProvider(new PlateauUIProvider());
        registerProvider(new GapUIProvider());
        
        registerProvider(new NoteUIProvider());
        registerProvider(new GroupUIProvider());
        registerProvider(new DiagramModelReferenceUIProvider());
        registerProvider(new LineConnectionUIProvider());
        
        registerProvider(new SketchActorUIProvider());
        registerProvider(new SketchStickyUIProvider());
    }
    
    private Map<EClass, IElementUIProvider> map = new HashMap<EClass, IElementUIProvider>();
    
    public void registerProvider(IElementUIProvider provider) {
        map.put(provider.providerFor(), provider);
    }
    
    public IElementUIProvider getProvider(EClass type) {
        return map.get(type);
    }
}
