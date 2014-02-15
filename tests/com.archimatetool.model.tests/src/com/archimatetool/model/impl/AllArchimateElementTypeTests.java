/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import junit.framework.JUnit4TestAdapter;

import org.eclipse.emf.ecore.EClass;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IInterfaceElement;

@RunWith(Parameterized.class)
public class AllArchimateElementTypeTests extends ArchimateElementTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(AllArchimateElementTypeTests.class);
    }
    
    @Parameters
    public static Collection<EClass[]> eObjects() {
        return Arrays.asList(new EClass[][] {
                { IArchimatePackage.eINSTANCE.getBusinessActor() },
                { IArchimatePackage.eINSTANCE.getBusinessRole() },
                { IArchimatePackage.eINSTANCE.getBusinessCollaboration() },
                { IArchimatePackage.eINSTANCE.getBusinessInterface() },
                { IArchimatePackage.eINSTANCE.getBusinessFunction() },
                { IArchimatePackage.eINSTANCE.getBusinessProcess() },
                { IArchimatePackage.eINSTANCE.getBusinessEvent() },
                { IArchimatePackage.eINSTANCE.getBusinessInteraction() },
                { IArchimatePackage.eINSTANCE.getProduct() },
                { IArchimatePackage.eINSTANCE.getContract() },
                { IArchimatePackage.eINSTANCE.getBusinessService() },
                { IArchimatePackage.eINSTANCE.getValue() },
                { IArchimatePackage.eINSTANCE.getMeaning() },
                { IArchimatePackage.eINSTANCE.getRepresentation() },
                { IArchimatePackage.eINSTANCE.getBusinessObject() },
                { IArchimatePackage.eINSTANCE.getLocation() },
                
                { IArchimatePackage.eINSTANCE.getApplicationComponent() },
                { IArchimatePackage.eINSTANCE.getApplicationCollaboration() },
                { IArchimatePackage.eINSTANCE.getApplicationInterface() },
                { IArchimatePackage.eINSTANCE.getApplicationService() },
                { IArchimatePackage.eINSTANCE.getApplicationFunction() },
                { IArchimatePackage.eINSTANCE.getApplicationInteraction() },
                { IArchimatePackage.eINSTANCE.getDataObject() },
                
                { IArchimatePackage.eINSTANCE.getArtifact() },
                { IArchimatePackage.eINSTANCE.getCommunicationPath() },
                { IArchimatePackage.eINSTANCE.getNetwork() },
                { IArchimatePackage.eINSTANCE.getInfrastructureInterface() },
                { IArchimatePackage.eINSTANCE.getInfrastructureFunction() },
                { IArchimatePackage.eINSTANCE.getInfrastructureService() },
                { IArchimatePackage.eINSTANCE.getNode() },
                { IArchimatePackage.eINSTANCE.getSystemSoftware() },
                { IArchimatePackage.eINSTANCE.getDevice() },
                
                { IArchimatePackage.eINSTANCE.getStakeholder() },
                { IArchimatePackage.eINSTANCE.getDriver() },
                { IArchimatePackage.eINSTANCE.getAssessment() },
                { IArchimatePackage.eINSTANCE.getGoal() },
                { IArchimatePackage.eINSTANCE.getPrinciple() },
                { IArchimatePackage.eINSTANCE.getRequirement() },
                { IArchimatePackage.eINSTANCE.getConstraint() },
                
                { IArchimatePackage.eINSTANCE.getWorkPackage() },
                { IArchimatePackage.eINSTANCE.getDeliverable() },
                { IArchimatePackage.eINSTANCE.getPlateau() },
                { IArchimatePackage.eINSTANCE.getGap() },
                
                { IArchimatePackage.eINSTANCE.getJunction() },
                { IArchimatePackage.eINSTANCE.getAndJunction() },
                { IArchimatePackage.eINSTANCE.getOrJunction() }
        });
    }
    
    private EClass eClass;
    
    public AllArchimateElementTypeTests(EClass eClass) {
        this.eClass = eClass;
    }
    
    @Override
    protected IArchimateElement getArchimateElement() {
        return (IArchimateElement)IArchimateFactory.eINSTANCE.create(eClass);
    }

    @Test
    public void testGetInterfaceType() {
        // Only IInterfaceElement types
        Assume.assumeTrue(element instanceof IInterfaceElement);

        IInterfaceElement interfaceElement = (IInterfaceElement)element;
        assertEquals(IInterfaceElement.PROVIDED, interfaceElement.getInterfaceType());
        interfaceElement.setInterfaceType(IInterfaceElement.REQUIRED);
        assertEquals(IInterfaceElement.REQUIRED, interfaceElement.getInterfaceType());
    }
}
