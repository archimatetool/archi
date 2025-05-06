/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.stream.Stream;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.jupiter.params.provider.Arguments;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.ParamsTest;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.factory.elements.ApplicationCollaborationUIProvider;
import com.archimatetool.editor.ui.factory.elements.ApplicationComponentUIProvider;
import com.archimatetool.editor.ui.factory.elements.ApplicationEventUIProvider;
import com.archimatetool.editor.ui.factory.elements.ApplicationFunctionUIProvider;
import com.archimatetool.editor.ui.factory.elements.ApplicationInteractionUIProvider;
import com.archimatetool.editor.ui.factory.elements.ApplicationInterfaceUIProvider;
import com.archimatetool.editor.ui.factory.elements.ApplicationProcessUIProvider;
import com.archimatetool.editor.ui.factory.elements.ApplicationServiceUIProvider;
import com.archimatetool.editor.ui.factory.elements.ArtifactUIProvider;
import com.archimatetool.editor.ui.factory.elements.AssessmentUIProvider;
import com.archimatetool.editor.ui.factory.elements.BusinessActorUIProvider;
import com.archimatetool.editor.ui.factory.elements.BusinessCollaborationUIProvider;
import com.archimatetool.editor.ui.factory.elements.BusinessEventUIProvider;
import com.archimatetool.editor.ui.factory.elements.BusinessFunctionUIProvider;
import com.archimatetool.editor.ui.factory.elements.BusinessInteractionUIProvider;
import com.archimatetool.editor.ui.factory.elements.BusinessInterfaceUIProvider;
import com.archimatetool.editor.ui.factory.elements.BusinessObjectUIProvider;
import com.archimatetool.editor.ui.factory.elements.BusinessProcessUIProvider;
import com.archimatetool.editor.ui.factory.elements.BusinessRoleUIProvider;
import com.archimatetool.editor.ui.factory.elements.BusinessServiceUIProvider;
import com.archimatetool.editor.ui.factory.elements.CapabilityUIProvider;
import com.archimatetool.editor.ui.factory.elements.CommunicationNetworkUIProvider;
import com.archimatetool.editor.ui.factory.elements.ConstraintUIProvider;
import com.archimatetool.editor.ui.factory.elements.ContractUIProvider;
import com.archimatetool.editor.ui.factory.elements.CourseOfActionUIProvider;
import com.archimatetool.editor.ui.factory.elements.DataObjectUIProvider;
import com.archimatetool.editor.ui.factory.elements.DeliverableUIProvider;
import com.archimatetool.editor.ui.factory.elements.DeviceUIProvider;
import com.archimatetool.editor.ui.factory.elements.DistributionNetworkUIProvider;
import com.archimatetool.editor.ui.factory.elements.DriverUIProvider;
import com.archimatetool.editor.ui.factory.elements.EquipmentUIProvider;
import com.archimatetool.editor.ui.factory.elements.FacilityUIProvider;
import com.archimatetool.editor.ui.factory.elements.GapUIProvider;
import com.archimatetool.editor.ui.factory.elements.GoalUIProvider;
import com.archimatetool.editor.ui.factory.elements.GroupingUIProvider;
import com.archimatetool.editor.ui.factory.elements.ImplementationEventUIProvider;
import com.archimatetool.editor.ui.factory.elements.JunctionUIProvider;
import com.archimatetool.editor.ui.factory.elements.LocationUIProvider;
import com.archimatetool.editor.ui.factory.elements.MaterialUIProvider;
import com.archimatetool.editor.ui.factory.elements.MeaningUIProvider;
import com.archimatetool.editor.ui.factory.elements.NodeUIProvider;
import com.archimatetool.editor.ui.factory.elements.PathUIProvider;
import com.archimatetool.editor.ui.factory.elements.PlateauUIProvider;
import com.archimatetool.editor.ui.factory.elements.PrincipleUIProvider;
import com.archimatetool.editor.ui.factory.elements.ProductUIProvider;
import com.archimatetool.editor.ui.factory.elements.RepresentationUIProvider;
import com.archimatetool.editor.ui.factory.elements.RequirementUIProvider;
import com.archimatetool.editor.ui.factory.elements.ResourceUIProvider;
import com.archimatetool.editor.ui.factory.elements.StakeholderUIProvider;
import com.archimatetool.editor.ui.factory.elements.SystemSoftwareUIProvider;
import com.archimatetool.editor.ui.factory.elements.TechnologyCollaborationUIProvider;
import com.archimatetool.editor.ui.factory.elements.TechnologyEventUIProvider;
import com.archimatetool.editor.ui.factory.elements.TechnologyFunctionUIProvider;
import com.archimatetool.editor.ui.factory.elements.TechnologyInteractionUIProvider;
import com.archimatetool.editor.ui.factory.elements.TechnologyInterfaceUIProvider;
import com.archimatetool.editor.ui.factory.elements.TechnologyProcessUIProvider;
import com.archimatetool.editor.ui.factory.elements.TechnologyServiceUIProvider;
import com.archimatetool.editor.ui.factory.elements.ValueStreamUIProvider;
import com.archimatetool.editor.ui.factory.elements.ValueUIProvider;
import com.archimatetool.editor.ui.factory.elements.WorkPackageUIProvider;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.ITextAlignment;

public class AllArchiMateElementUIProviderTests extends AbstractGraphicalObjectUIProviderTests {
    
    static Stream<Arguments> getParams() {
        return Stream.of(
                getParam(new JunctionUIProvider(), IArchimatePackage.eINSTANCE.getJunction()),
                getParam(new ApplicationCollaborationUIProvider(), IArchimatePackage.eINSTANCE.getApplicationCollaboration()),
                getParam(new ApplicationComponentUIProvider(), IArchimatePackage.eINSTANCE.getApplicationComponent()),
                getParam(new ApplicationEventUIProvider(), IArchimatePackage.eINSTANCE.getApplicationEvent()),
                getParam(new ApplicationFunctionUIProvider(), IArchimatePackage.eINSTANCE.getApplicationFunction()),
                getParam(new ApplicationInteractionUIProvider(), IArchimatePackage.eINSTANCE.getApplicationInteraction()),
                getParam(new ApplicationInterfaceUIProvider(), IArchimatePackage.eINSTANCE.getApplicationInterface()),
                getParam(new ApplicationProcessUIProvider(), IArchimatePackage.eINSTANCE.getApplicationProcess()),
                getParam(new ApplicationServiceUIProvider(), IArchimatePackage.eINSTANCE.getApplicationService()),
                getParam(new ArtifactUIProvider(), IArchimatePackage.eINSTANCE.getArtifact()),
                getParam(new AssessmentUIProvider(), IArchimatePackage.eINSTANCE.getAssessment()),
                getParam(new BusinessActorUIProvider(), IArchimatePackage.eINSTANCE.getBusinessActor()),
                getParam(new BusinessInterfaceUIProvider(), IArchimatePackage.eINSTANCE.getBusinessInterface()),
                getParam(new BusinessCollaborationUIProvider(), IArchimatePackage.eINSTANCE.getBusinessCollaboration()),
                getParam(new BusinessEventUIProvider(), IArchimatePackage.eINSTANCE.getBusinessEvent()),
                getParam(new BusinessFunctionUIProvider(), IArchimatePackage.eINSTANCE.getBusinessFunction()),
                getParam(new BusinessInteractionUIProvider(), IArchimatePackage.eINSTANCE.getBusinessInteraction()),
                getParam(new BusinessObjectUIProvider(), IArchimatePackage.eINSTANCE.getBusinessObject()),
                getParam(new BusinessProcessUIProvider(), IArchimatePackage.eINSTANCE.getBusinessProcess()),
                getParam(new BusinessRoleUIProvider(), IArchimatePackage.eINSTANCE.getBusinessRole()),
                getParam(new BusinessServiceUIProvider(), IArchimatePackage.eINSTANCE.getBusinessService()),
                getParam(new CommunicationNetworkUIProvider(), IArchimatePackage.eINSTANCE.getCommunicationNetwork()),
                getParam(new CapabilityUIProvider(), IArchimatePackage.eINSTANCE.getCapability()),
                getParam(new ConstraintUIProvider(), IArchimatePackage.eINSTANCE.getConstraint()),
                getParam(new ContractUIProvider(), IArchimatePackage.eINSTANCE.getContract()),
                getParam(new CourseOfActionUIProvider(), IArchimatePackage.eINSTANCE.getCourseOfAction()),
                getParam(new DataObjectUIProvider(), IArchimatePackage.eINSTANCE.getDataObject()),
                getParam(new DeliverableUIProvider(), IArchimatePackage.eINSTANCE.getDeliverable()),
                getParam(new DeviceUIProvider(), IArchimatePackage.eINSTANCE.getDevice()),
                getParam(new DistributionNetworkUIProvider(), IArchimatePackage.eINSTANCE.getDistributionNetwork()),
                getParam(new DriverUIProvider(), IArchimatePackage.eINSTANCE.getDriver()),
                getParam(new EquipmentUIProvider(), IArchimatePackage.eINSTANCE.getEquipment()),
                getParam(new FacilityUIProvider(), IArchimatePackage.eINSTANCE.getFacility()),
                getParam(new GapUIProvider(), IArchimatePackage.eINSTANCE.getGap()),
                getParam(new GoalUIProvider(), IArchimatePackage.eINSTANCE.getGoal()),
                getParam(new GroupingUIProvider(), IArchimatePackage.eINSTANCE.getGrouping()),
                getParam(new ImplementationEventUIProvider(), IArchimatePackage.eINSTANCE.getImplementationEvent()),
                getParam(new LocationUIProvider(), IArchimatePackage.eINSTANCE.getLocation()),
                getParam(new MaterialUIProvider(), IArchimatePackage.eINSTANCE.getMaterial()),
                getParam(new MeaningUIProvider(), IArchimatePackage.eINSTANCE.getMeaning()),
                getParam(new NodeUIProvider(), IArchimatePackage.eINSTANCE.getNode()),
                getParam(new PathUIProvider(), IArchimatePackage.eINSTANCE.getPath()),
                getParam(new PlateauUIProvider(), IArchimatePackage.eINSTANCE.getPlateau()),
                getParam(new PrincipleUIProvider(), IArchimatePackage.eINSTANCE.getPrinciple()),
                getParam(new ProductUIProvider(), IArchimatePackage.eINSTANCE.getProduct()),
                getParam(new RepresentationUIProvider(), IArchimatePackage.eINSTANCE.getRepresentation()),
                getParam(new ResourceUIProvider(), IArchimatePackage.eINSTANCE.getResource()),
                getParam(new RequirementUIProvider(), IArchimatePackage.eINSTANCE.getRequirement()),
                getParam(new StakeholderUIProvider(), IArchimatePackage.eINSTANCE.getStakeholder()),
                getParam(new SystemSoftwareUIProvider(), IArchimatePackage.eINSTANCE.getSystemSoftware()),
                getParam(new TechnologyCollaborationUIProvider(), IArchimatePackage.eINSTANCE.getTechnologyCollaboration()),
                getParam(new TechnologyEventUIProvider(), IArchimatePackage.eINSTANCE.getTechnologyEvent()),
                getParam(new TechnologyFunctionUIProvider(), IArchimatePackage.eINSTANCE.getTechnologyFunction()),
                getParam(new TechnologyInterfaceUIProvider(), IArchimatePackage.eINSTANCE.getTechnologyInterface()),
                getParam(new TechnologyInteractionUIProvider(), IArchimatePackage.eINSTANCE.getTechnologyInteraction()),
                getParam(new TechnologyProcessUIProvider(), IArchimatePackage.eINSTANCE.getTechnologyProcess()),
                getParam(new TechnologyServiceUIProvider(), IArchimatePackage.eINSTANCE.getTechnologyService()),
                getParam(new ValueUIProvider(), IArchimatePackage.eINSTANCE.getValue()),
                getParam(new ValueStreamUIProvider(), IArchimatePackage.eINSTANCE.getValueStream()),
                getParam(new WorkPackageUIProvider(), IArchimatePackage.eINSTANCE.getWorkPackage())
        );
     }

    @Override
    @ParamsTest
    public void testGetDefaultColor(IGraphicalObjectUIProvider provider) {
        assertNotNull(provider.getDefaultColor());
    }

    @Override
    @ParamsTest
    public void testGetDefaultSize(IGraphicalObjectUIProvider provider) {
        // Junctions
        if(provider instanceof JunctionUIProvider) {
            assertEquals(new Dimension(15, 15), provider.getDefaultSize());
        }
        
        // Grouping
        else if(provider instanceof GroupingUIProvider) {
            assertEquals(new Dimension(400, 140), provider.getDefaultSize());
        }
        
        else {
            assertEquals(IGraphicalObjectUIProvider.defaultSize(), provider.getDefaultSize());
        }
    }
    
    @ParamsTest
    public void testGetDefaultSize_UserSet(IGraphicalObjectUIProvider provider) {
        if(provider instanceof JunctionUIProvider || provider instanceof GroupingUIProvider) {
            return;
        }

        IPreferenceStore preferenceStore = ArchiPlugin.getInstance().getPreferenceStore();
        
        // New value via preferences
        preferenceStore.setValue(IPreferenceConstants.DEFAULT_ARCHIMATE_FIGURE_WIDTH, 150);
        preferenceStore.setValue(IPreferenceConstants.DEFAULT_ARCHIMATE_FIGURE_HEIGHT, 90);
        assertEquals(new Dimension(150, 90), provider.getDefaultSize());
        
        // Default value in preferences
        preferenceStore.setToDefault(IPreferenceConstants.DEFAULT_ARCHIMATE_FIGURE_WIDTH);
        preferenceStore.setToDefault(IPreferenceConstants.DEFAULT_ARCHIMATE_FIGURE_HEIGHT);
        assertEquals(IGraphicalObjectUIProvider.defaultSize(), provider.getDefaultSize());
    }

    @Override
    @ParamsTest
    public void testShouldExposeFeature(IObjectUIProvider provider) {
        // Junctions
        if(provider instanceof JunctionUIProvider) {
            assertFalse(provider.shouldExposeFeature((String)null));
        }
        else {
            super.testShouldExposeFeature(provider);
        }
    }
    
    @Override
    @ParamsTest
    public void testGetDefaultTextAlignment(IGraphicalObjectUIProvider provider) {
        if(provider instanceof GroupingUIProvider) {
            assertEquals(ITextAlignment.TEXT_ALIGNMENT_LEFT, provider.getDefaultTextAlignment());
        }
        else {
            super.testGetDefaultTextAlignment(provider);
        }
    }
    
    @Override
    @ParamsTest
    public void testGetDefaultFeatureValue(IObjectUIProvider provider) {
        if(provider instanceof GroupingUIProvider) {
            assertEquals(IDiagramModelObject.LINE_STYLE_DASHED, provider.getDefaultFeatureValue(IDiagramModelObject.FEATURE_LINE_STYLE));
        }
        else {
            super.testGetDefaultFeatureValue(provider);
        }
    }
    
    @Override
    @ParamsTest
    public void testGetFeatureValue(IObjectUIProvider provider) {
        super.testGetFeatureValue(provider);
        
        IDiagramModelArchimateObject dmo = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        dmo.setArchimateElement((IArchimateElement)IArchimateFactory.eINSTANCE.create(provider.providerFor()));
        ((AbstractObjectUIProvider)provider).setInstance(dmo);
        
        if(provider instanceof GroupingUIProvider) {
            assertEquals(IDiagramModelObject.LINE_STYLE_DASHED, provider.getFeatureValue(IDiagramModelObject.FEATURE_LINE_STYLE));
        }
        else {
            assertEquals(IDiagramModelObject.LINE_STYLE_SOLID, provider.getFeatureValue(IDiagramModelObject.FEATURE_LINE_STYLE));
        }
    }
}
