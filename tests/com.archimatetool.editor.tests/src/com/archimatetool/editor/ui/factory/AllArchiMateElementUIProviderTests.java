/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import junit.framework.JUnit4TestAdapter;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.EditPart;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.archimatetool.editor.diagram.editparts.AbstractArchimateEditPart;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.factory.application.AbstractApplicationUIProvider;
import com.archimatetool.editor.ui.factory.application.ApplicationCollaborationUIProvider;
import com.archimatetool.editor.ui.factory.application.ApplicationComponentUIProvider;
import com.archimatetool.editor.ui.factory.application.ApplicationDataObjectUIProvider;
import com.archimatetool.editor.ui.factory.application.ApplicationFunctionUIProvider;
import com.archimatetool.editor.ui.factory.application.ApplicationInteractionUIProvider;
import com.archimatetool.editor.ui.factory.application.ApplicationInterfaceUIProvider;
import com.archimatetool.editor.ui.factory.application.ApplicationServiceUIProvider;
import com.archimatetool.editor.ui.factory.business.AbstractBusinessUIProvider;
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
import com.archimatetool.editor.ui.factory.technology.AbstractTechnologyUIProvider;
import com.archimatetool.editor.ui.factory.technology.TechnologyArtifactUIProvider;
import com.archimatetool.editor.ui.factory.technology.TechnologyCommunicationPathUIProvider;
import com.archimatetool.editor.ui.factory.technology.TechnologyDeviceUIProvider;
import com.archimatetool.editor.ui.factory.technology.TechnologyInfrastructureFunctionUIProvider;
import com.archimatetool.editor.ui.factory.technology.TechnologyInfrastructureInterfaceUIProvider;
import com.archimatetool.editor.ui.factory.technology.TechnologyInfrastructureServiceUIProvider;
import com.archimatetool.editor.ui.factory.technology.TechnologyNetworkUIProvider;
import com.archimatetool.editor.ui.factory.technology.TechnologyNodeUIProvider;
import com.archimatetool.editor.ui.factory.technology.TechnologySystemSoftwareUIProvider;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.tests.TestUtils;

@RunWith(Parameterized.class)
public class AllArchiMateElementUIProviderTests extends AbstractElementUIProviderTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(AllArchiMateElementUIProviderTests.class);
    }
    
    // Need to ensure current display for ImageRegistry
    static {
        TestUtils.ensureDefaultDisplay();
    }
    
    @Parameters
    public static Collection<Object[]> eObjects() {
        return Arrays.asList(new Object[][] {
                { new BusinessActorUIProvider(), IArchimatePackage.eINSTANCE.getBusinessActor() },
                { new BusinessInterfaceUIProvider(), IArchimatePackage.eINSTANCE.getBusinessInterface() },
                { new BusinessCollaborationUIProvider(), IArchimatePackage.eINSTANCE.getBusinessCollaboration() },
                { new BusinessContractUIProvider(), IArchimatePackage.eINSTANCE.getContract() },
                { new BusinessEventUIProvider(), IArchimatePackage.eINSTANCE.getBusinessEvent() },
                { new BusinessFunctionUIProvider(), IArchimatePackage.eINSTANCE.getBusinessFunction() },
                { new BusinessInteractionUIProvider(), IArchimatePackage.eINSTANCE.getBusinessInteraction() },
                { new BusinessMeaningUIProvider(), IArchimatePackage.eINSTANCE.getMeaning() },
                { new BusinessObjectUIProvider(), IArchimatePackage.eINSTANCE.getBusinessObject() },
                { new BusinessProcessUIProvider(), IArchimatePackage.eINSTANCE.getBusinessProcess() },
                { new BusinessProductUIProvider(), IArchimatePackage.eINSTANCE.getProduct() },
                { new BusinessRepresentationUIProvider(), IArchimatePackage.eINSTANCE.getRepresentation() },
                { new BusinessRoleUIProvider(), IArchimatePackage.eINSTANCE.getBusinessRole() },
                { new BusinessServiceUIProvider(), IArchimatePackage.eINSTANCE.getBusinessService() },
                { new BusinessValueUIProvider(), IArchimatePackage.eINSTANCE.getValue() },
                { new BusinessLocationUIProvider(), IArchimatePackage.eINSTANCE.getLocation() },
                
                { new ApplicationCollaborationUIProvider(), IArchimatePackage.eINSTANCE.getApplicationCollaboration() },
                { new ApplicationComponentUIProvider(), IArchimatePackage.eINSTANCE.getApplicationComponent() },
                { new ApplicationFunctionUIProvider(), IArchimatePackage.eINSTANCE.getApplicationFunction() },
                { new ApplicationInteractionUIProvider(), IArchimatePackage.eINSTANCE.getApplicationInteraction() },
                { new ApplicationInterfaceUIProvider(), IArchimatePackage.eINSTANCE.getApplicationInterface() },
                { new ApplicationDataObjectUIProvider(), IArchimatePackage.eINSTANCE.getDataObject() },
                { new ApplicationServiceUIProvider(), IArchimatePackage.eINSTANCE.getApplicationService() },
                
                { new TechnologyArtifactUIProvider(), IArchimatePackage.eINSTANCE.getArtifact() },
                { new TechnologyCommunicationPathUIProvider(), IArchimatePackage.eINSTANCE.getCommunicationPath() },
                { new TechnologyNetworkUIProvider(), IArchimatePackage.eINSTANCE.getNetwork() },
                { new TechnologyInfrastructureInterfaceUIProvider(), IArchimatePackage.eINSTANCE.getInfrastructureInterface() },
                { new TechnologyInfrastructureServiceUIProvider(), IArchimatePackage.eINSTANCE.getInfrastructureService() },
                { new TechnologyNodeUIProvider(), IArchimatePackage.eINSTANCE.getNode() },
                { new TechnologySystemSoftwareUIProvider(), IArchimatePackage.eINSTANCE.getSystemSoftware() },
                { new TechnologyDeviceUIProvider(), IArchimatePackage.eINSTANCE.getDevice() },
                { new TechnologyInfrastructureFunctionUIProvider(), IArchimatePackage.eINSTANCE.getInfrastructureFunction() },
                
                { new JunctionUIProvider(), IArchimatePackage.eINSTANCE.getJunction() },
                { new AndJunctionUIProvider(), IArchimatePackage.eINSTANCE.getAndJunction() },
                { new OrJunctionUIProvider(), IArchimatePackage.eINSTANCE.getOrJunction() },
                
                { new StakeholderUIProvider(), IArchimatePackage.eINSTANCE.getStakeholder() },
                { new DriverUIProvider(), IArchimatePackage.eINSTANCE.getDriver() },
                { new AssessmentUIProvider(), IArchimatePackage.eINSTANCE.getAssessment() },
                { new GoalUIProvider(), IArchimatePackage.eINSTANCE.getGoal() },
                { new PrincipleUIProvider(), IArchimatePackage.eINSTANCE.getPrinciple() },
                { new RequirementUIProvider(), IArchimatePackage.eINSTANCE.getRequirement() },
                { new ConstraintUIProvider(), IArchimatePackage.eINSTANCE.getConstraint() },
                
                { new WorkPackageUIProvider(), IArchimatePackage.eINSTANCE.getWorkPackage() },
                { new DeliverableUIProvider(), IArchimatePackage.eINSTANCE.getDeliverable() },
                { new PlateauUIProvider(), IArchimatePackage.eINSTANCE.getPlateau() },
                { new GapUIProvider(), IArchimatePackage.eINSTANCE.getGap() }
        });
    }
    
    public AllArchiMateElementUIProviderTests(IElementUIProvider provider, EClass expectedClass) {
        this.provider = provider;
        this.expectedClass = expectedClass;
    }
    
    
    @Override
    public void testCreateEditPart() {
        EditPart editPart = provider.createEditPart();
        assertTrue(editPart instanceof AbstractArchimateEditPart);
    }

    @Override
    @Test
    public void testGetDefaultColor() {
        Assume.assumeTrue(provider instanceof AbstractArchimateElementUIProvider);
        
        if(provider instanceof AbstractBusinessUIProvider) {
            assertEquals(ColorFactory.COLOR_BUSINESS, provider.getDefaultColor());
        }
        else if(provider instanceof AbstractApplicationUIProvider) {
            assertEquals(ColorFactory.COLOR_APPLICATION, provider.getDefaultColor());
        }
        else if(provider instanceof AbstractTechnologyUIProvider) {
            assertEquals(ColorFactory.COLOR_TECHNOLOGY, provider.getDefaultColor());
        }
        else if(provider instanceof JunctionUIProvider) {
            assertEquals(ColorConstants.black, provider.getDefaultColor());
        }
    }

    @Override
    @Test
    public void testGetDefaultSize() {
        Assume.assumeTrue(provider instanceof AbstractArchimateElementUIProvider);

        if(provider instanceof JunctionUIProvider) {
            assertEquals(new Dimension(15, 15), provider.getDefaultSize());
        }
        else {
            // Default value in preferences
            Preferences.STORE.setToDefault(IPreferenceConstants.DEFAULT_ARCHIMATE_FIGURE_WIDTH);
            Preferences.STORE.setToDefault(IPreferenceConstants.DEFAULT_ARCHIMATE_FIGURE_HEIGHT);
            assertEquals(new Dimension(120, 55), provider.getDefaultSize());
            
            // New value via preferences
            Preferences.STORE.setValue(IPreferenceConstants.DEFAULT_ARCHIMATE_FIGURE_WIDTH, 150);
            Preferences.STORE.setValue(IPreferenceConstants.DEFAULT_ARCHIMATE_FIGURE_HEIGHT, 90);
            assertEquals(new Dimension(150, 90), provider.getDefaultSize());
        }
    }

}
