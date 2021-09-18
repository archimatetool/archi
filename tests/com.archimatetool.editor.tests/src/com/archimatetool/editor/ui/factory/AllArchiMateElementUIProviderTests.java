/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.archimatetool.editor.ArchiPlugin;
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
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.ITextAlignment;

import junit.framework.JUnit4TestAdapter;

@RunWith(Parameterized.class)
public class AllArchiMateElementUIProviderTests extends AbstractGraphicalObjectUIProviderTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(AllArchiMateElementUIProviderTests.class);
    }
    
    @Parameters
    public static Collection<Object[]> eObjects() {
        return Arrays.asList(new Object[][] {
                
                { new JunctionUIProvider(), IArchimatePackage.eINSTANCE.getJunction() },
                { new ApplicationCollaborationUIProvider(), IArchimatePackage.eINSTANCE.getApplicationCollaboration() },
                { new ApplicationComponentUIProvider(), IArchimatePackage.eINSTANCE.getApplicationComponent() },
                { new ApplicationEventUIProvider(), IArchimatePackage.eINSTANCE.getApplicationEvent() },
                { new ApplicationFunctionUIProvider(), IArchimatePackage.eINSTANCE.getApplicationFunction() },
                { new ApplicationInteractionUIProvider(), IArchimatePackage.eINSTANCE.getApplicationInteraction() },
                { new ApplicationInterfaceUIProvider(), IArchimatePackage.eINSTANCE.getApplicationInterface() },
                { new ApplicationProcessUIProvider(), IArchimatePackage.eINSTANCE.getApplicationProcess() },
                { new ApplicationServiceUIProvider(), IArchimatePackage.eINSTANCE.getApplicationService() },
                { new ArtifactUIProvider(), IArchimatePackage.eINSTANCE.getArtifact() },
                { new AssessmentUIProvider(), IArchimatePackage.eINSTANCE.getAssessment() },
                { new BusinessActorUIProvider(), IArchimatePackage.eINSTANCE.getBusinessActor() },
                { new BusinessInterfaceUIProvider(), IArchimatePackage.eINSTANCE.getBusinessInterface() },
                { new BusinessCollaborationUIProvider(), IArchimatePackage.eINSTANCE.getBusinessCollaboration() },
                { new BusinessEventUIProvider(), IArchimatePackage.eINSTANCE.getBusinessEvent() },
                { new BusinessFunctionUIProvider(), IArchimatePackage.eINSTANCE.getBusinessFunction() },
                { new BusinessInteractionUIProvider(), IArchimatePackage.eINSTANCE.getBusinessInteraction() },
                { new BusinessObjectUIProvider(), IArchimatePackage.eINSTANCE.getBusinessObject() },
                { new BusinessProcessUIProvider(), IArchimatePackage.eINSTANCE.getBusinessProcess() },
                { new BusinessRoleUIProvider(), IArchimatePackage.eINSTANCE.getBusinessRole() },
                { new BusinessServiceUIProvider(), IArchimatePackage.eINSTANCE.getBusinessService() },
                { new CommunicationNetworkUIProvider(), IArchimatePackage.eINSTANCE.getCommunicationNetwork() },
                { new CapabilityUIProvider(), IArchimatePackage.eINSTANCE.getCapability() },
                { new ConstraintUIProvider(), IArchimatePackage.eINSTANCE.getConstraint() },
                { new ContractUIProvider(), IArchimatePackage.eINSTANCE.getContract() },
                { new CourseOfActionUIProvider(), IArchimatePackage.eINSTANCE.getCourseOfAction() },
                { new DataObjectUIProvider(), IArchimatePackage.eINSTANCE.getDataObject() },
                { new DeliverableUIProvider(), IArchimatePackage.eINSTANCE.getDeliverable() },
                { new DeviceUIProvider(), IArchimatePackage.eINSTANCE.getDevice() },
                { new DistributionNetworkUIProvider(), IArchimatePackage.eINSTANCE.getDistributionNetwork() },
                { new DriverUIProvider(), IArchimatePackage.eINSTANCE.getDriver() },
                { new EquipmentUIProvider(), IArchimatePackage.eINSTANCE.getEquipment() },
                { new FacilityUIProvider(), IArchimatePackage.eINSTANCE.getFacility() },
                { new GapUIProvider(), IArchimatePackage.eINSTANCE.getGap() },
                { new GoalUIProvider(), IArchimatePackage.eINSTANCE.getGoal() },
                { new GroupingUIProvider(), IArchimatePackage.eINSTANCE.getGrouping() },
                { new ImplementationEventUIProvider(), IArchimatePackage.eINSTANCE.getImplementationEvent() },
                { new LocationUIProvider(), IArchimatePackage.eINSTANCE.getLocation() },
                { new MaterialUIProvider(), IArchimatePackage.eINSTANCE.getMaterial() },
                { new MeaningUIProvider(), IArchimatePackage.eINSTANCE.getMeaning() },
                { new NodeUIProvider(), IArchimatePackage.eINSTANCE.getNode() },
                { new PathUIProvider(), IArchimatePackage.eINSTANCE.getPath() },
                { new PlateauUIProvider(), IArchimatePackage.eINSTANCE.getPlateau() },
                { new PrincipleUIProvider(), IArchimatePackage.eINSTANCE.getPrinciple() },
                { new ProductUIProvider(), IArchimatePackage.eINSTANCE.getProduct() },
                { new RepresentationUIProvider(), IArchimatePackage.eINSTANCE.getRepresentation() },
                { new ResourceUIProvider(), IArchimatePackage.eINSTANCE.getResource() },
                { new RequirementUIProvider(), IArchimatePackage.eINSTANCE.getRequirement() },
                { new StakeholderUIProvider(), IArchimatePackage.eINSTANCE.getStakeholder() },
                { new SystemSoftwareUIProvider(), IArchimatePackage.eINSTANCE.getSystemSoftware() },
                { new TechnologyCollaborationUIProvider(), IArchimatePackage.eINSTANCE.getTechnologyCollaboration() },
                { new TechnologyEventUIProvider(), IArchimatePackage.eINSTANCE.getTechnologyEvent() },
                { new TechnologyFunctionUIProvider(), IArchimatePackage.eINSTANCE.getTechnologyFunction() },
                { new TechnologyInterfaceUIProvider(), IArchimatePackage.eINSTANCE.getTechnologyInterface() },
                { new TechnologyInteractionUIProvider(), IArchimatePackage.eINSTANCE.getTechnologyInteraction() },
                { new TechnologyProcessUIProvider(), IArchimatePackage.eINSTANCE.getTechnologyProcess() },
                { new TechnologyServiceUIProvider(), IArchimatePackage.eINSTANCE.getTechnologyService() },
                { new ValueUIProvider(), IArchimatePackage.eINSTANCE.getValue() },
                { new ValueStreamUIProvider(), IArchimatePackage.eINSTANCE.getValueStream() },
                { new WorkPackageUIProvider(), IArchimatePackage.eINSTANCE.getWorkPackage() }
                
        });
    }
    
    public AllArchiMateElementUIProviderTests(IObjectUIProvider provider, EClass expectedClass) {
        this.provider = provider;
        this.expectedClass = expectedClass;
    }
    
    @Override
    protected IArchimateElementUIProvider getProvider() {
        return (IArchimateElementUIProvider)provider;
    }
    
    @Override
    public void testCreateEditPart() {
        EditPart editPart = getProvider().createEditPart();
        assertNotNull(editPart);
    }

    @Override
    @Test
    public void testGetDefaultColor() {
        assertNotNull(getProvider().getDefaultColor());
    }

    @Override
    @Test
    public void testGetDefaultSize() {
        // Junctions
        if(getProvider() instanceof JunctionUIProvider) {
            assertEquals(new Dimension(15, 15), getProvider().getDefaultSize());
        }
        
        // Grouping
        else if(getProvider() instanceof GroupingUIProvider) {
            assertEquals(new Dimension(400, 140), getProvider().getDefaultSize());
        }
        
        else {
            assertEquals(IGraphicalObjectUIProvider.defaultSize(), getProvider().getDefaultSize());
        }
    }
    
    @Test
    public void testGetDefaultSize_UserSet() {
        if(getProvider() instanceof JunctionUIProvider || getProvider() instanceof GroupingUIProvider) {
            return;
        }

        IPreferenceStore preferenceStore = ArchiPlugin.PREFERENCES;
        
        // New value via preferences
        preferenceStore.setValue(IPreferenceConstants.DEFAULT_ARCHIMATE_FIGURE_WIDTH, 150);
        preferenceStore.setValue(IPreferenceConstants.DEFAULT_ARCHIMATE_FIGURE_HEIGHT, 90);
        assertEquals(new Dimension(150, 90), getProvider().getDefaultSize());
        
        // Default value in preferences
        preferenceStore.setToDefault(IPreferenceConstants.DEFAULT_ARCHIMATE_FIGURE_WIDTH);
        preferenceStore.setToDefault(IPreferenceConstants.DEFAULT_ARCHIMATE_FIGURE_HEIGHT);
        assertEquals(IGraphicalObjectUIProvider.defaultSize(), getProvider().getDefaultSize());
    }

    @Override
    public void testShouldExposeFeature() {
        // Junctions
        if(getProvider() instanceof JunctionUIProvider) {
            assertFalse(getProvider().shouldExposeFeature((String)null));
        }
        else {
            super.testShouldExposeFeature();
        }
    }
    
    @Override
    @Test
    public void testGetDefaultTextAlignment() {
        if(getProvider() instanceof GroupingUIProvider) {
            assertEquals(ITextAlignment.TEXT_ALIGNMENT_LEFT, getProvider().getDefaultTextAlignment());
        }
        else {
            super.testGetDefaultTextAlignment();
        }
    }
}
