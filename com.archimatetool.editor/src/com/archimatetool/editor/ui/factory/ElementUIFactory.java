/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;

import com.archimatetool.editor.ui.factory.application.ApplicationCollaborationUIProvider;
import com.archimatetool.editor.ui.factory.application.ApplicationComponentUIProvider;
import com.archimatetool.editor.ui.factory.application.ApplicationDataObjectUIProvider;
import com.archimatetool.editor.ui.factory.application.ApplicationFunctionUIProvider;
import com.archimatetool.editor.ui.factory.application.ApplicationInteractionUIProvider;
import com.archimatetool.editor.ui.factory.application.ApplicationInterfaceUIProvider;
import com.archimatetool.editor.ui.factory.application.ApplicationServiceUIProvider;
import com.archimatetool.editor.ui.factory.business.BusinessActivityUIProvider;
import com.archimatetool.editor.ui.factory.business.BusinessActorUIProvider;
import com.archimatetool.editor.ui.factory.business.BusinessCollaborationUIProvider;
import com.archimatetool.editor.ui.factory.business.BusinessContractUIProvider;
import com.archimatetool.editor.ui.factory.business.BusinessEventUIProvider;
import com.archimatetool.editor.ui.factory.business.BusinessFunctionUIProvider;
import com.archimatetool.editor.ui.factory.business.BusinessInteractionUIProvider;
import com.archimatetool.editor.ui.factory.business.BusinessInterfaceUIProvider;
import com.archimatetool.editor.ui.factory.business.BusinessLocationUIProvider;
import com.archimatetool.editor.ui.factory.business.BusinessMeaningUIProvider;
import com.archimatetool.editor.ui.factory.business.BusinessObjectUIProvider;
import com.archimatetool.editor.ui.factory.business.BusinessProcessUIProvider;
import com.archimatetool.editor.ui.factory.business.BusinessProductUIProvider;
import com.archimatetool.editor.ui.factory.business.BusinessRepresentationUIProvider;
import com.archimatetool.editor.ui.factory.business.BusinessRoleUIProvider;
import com.archimatetool.editor.ui.factory.business.BusinessServiceUIProvider;
import com.archimatetool.editor.ui.factory.business.BusinessValueUIProvider;
import com.archimatetool.editor.ui.factory.connections.AccessConnectionUIProvider;
import com.archimatetool.editor.ui.factory.connections.AggregationConnectionUIProvider;
import com.archimatetool.editor.ui.factory.connections.AssignmentConnectionUIProvider;
import com.archimatetool.editor.ui.factory.connections.AssociationConnectionUIProvider;
import com.archimatetool.editor.ui.factory.connections.CompositionConnectionUIProvider;
import com.archimatetool.editor.ui.factory.connections.FlowConnectionUIProvider;
import com.archimatetool.editor.ui.factory.connections.InfluenceConnectionUIProvider;
import com.archimatetool.editor.ui.factory.connections.LineConnectionUIProvider;
import com.archimatetool.editor.ui.factory.connections.RealisationConnectionUIProvider;
import com.archimatetool.editor.ui.factory.connections.SpecialisationConnectionUIProvider;
import com.archimatetool.editor.ui.factory.connections.TriggeringConnectionUIProvider;
import com.archimatetool.editor.ui.factory.connections.UsedByConnectionUIProvider;
import com.archimatetool.editor.ui.factory.diagram.DiagramImageUIProvider;
import com.archimatetool.editor.ui.factory.diagram.DiagramModelReferenceUIProvider;
import com.archimatetool.editor.ui.factory.diagram.GroupUIProvider;
import com.archimatetool.editor.ui.factory.diagram.NoteUIProvider;
import com.archimatetool.editor.ui.factory.extensions.AssessmentUIProvider;
import com.archimatetool.editor.ui.factory.extensions.ConstraintUIProvider;
import com.archimatetool.editor.ui.factory.extensions.DeliverableUIProvider;
import com.archimatetool.editor.ui.factory.extensions.DriverUIProvider;
import com.archimatetool.editor.ui.factory.extensions.GapUIProvider;
import com.archimatetool.editor.ui.factory.extensions.GoalUIProvider;
import com.archimatetool.editor.ui.factory.extensions.PlateauUIProvider;
import com.archimatetool.editor.ui.factory.extensions.PrincipleUIProvider;
import com.archimatetool.editor.ui.factory.extensions.RequirementUIProvider;
import com.archimatetool.editor.ui.factory.extensions.StakeholderUIProvider;
import com.archimatetool.editor.ui.factory.extensions.WorkPackageUIProvider;
import com.archimatetool.editor.ui.factory.junctions.AndJunctionUIProvider;
import com.archimatetool.editor.ui.factory.junctions.JunctionUIProvider;
import com.archimatetool.editor.ui.factory.junctions.OrJunctionUIProvider;
import com.archimatetool.editor.ui.factory.sketch.SketchActorUIProvider;
import com.archimatetool.editor.ui.factory.sketch.SketchStickyUIProvider;
import com.archimatetool.editor.ui.factory.technology.TechnologyArtifactUIProvider;
import com.archimatetool.editor.ui.factory.technology.TechnologyCommunicationPathUIProvider;
import com.archimatetool.editor.ui.factory.technology.TechnologyDeviceUIProvider;
import com.archimatetool.editor.ui.factory.technology.TechnologyInfrastructureFunctionUIProvider;
import com.archimatetool.editor.ui.factory.technology.TechnologyInfrastructureInterfaceUIProvider;
import com.archimatetool.editor.ui.factory.technology.TechnologyInfrastructureServiceUIProvider;
import com.archimatetool.editor.ui.factory.technology.TechnologyNetworkUIProvider;
import com.archimatetool.editor.ui.factory.technology.TechnologyNodeUIProvider;
import com.archimatetool.editor.ui.factory.technology.TechnologySystemSoftwareUIProvider;



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
        registerProvider(new DiagramImageUIProvider());
        
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
