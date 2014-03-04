/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import junit.framework.JUnit4TestAdapter;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.archimatetool.editor.ArchimateTestModel;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.tests.AsyncTestRunner;

@SuppressWarnings("nls")
@RunWith(Parameterized.class)
public class AllArchimateTypeFigureTests extends AbstractTextFlowFigureTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(AllArchimateTypeFigureTests.class);
    }
    
    @Parameters
    public static Collection<EClass[]> eObjects() {
        return Arrays.asList(new EClass[][] {
                { IArchimatePackage.eINSTANCE.getApplicationCollaboration() },
                { IArchimatePackage.eINSTANCE.getApplicationComponent() },
                { IArchimatePackage.eINSTANCE.getDataObject() },
                { IArchimatePackage.eINSTANCE.getApplicationFunction() },
                { IArchimatePackage.eINSTANCE.getApplicationInteraction() },
                { IArchimatePackage.eINSTANCE.getApplicationInterface() },
                { IArchimatePackage.eINSTANCE.getApplicationService() },
                
                { IArchimatePackage.eINSTANCE.getBusinessActivity() },
                { IArchimatePackage.eINSTANCE.getBusinessActor() },
                { IArchimatePackage.eINSTANCE.getBusinessCollaboration() },
                { IArchimatePackage.eINSTANCE.getContract() },
                { IArchimatePackage.eINSTANCE.getBusinessEvent() },
                { IArchimatePackage.eINSTANCE.getBusinessFunction() },
                { IArchimatePackage.eINSTANCE.getBusinessInteraction() },
                { IArchimatePackage.eINSTANCE.getBusinessInterface() },
                { IArchimatePackage.eINSTANCE.getLocation() },
                { IArchimatePackage.eINSTANCE.getMeaning() },
                { IArchimatePackage.eINSTANCE.getBusinessObject() },
                { IArchimatePackage.eINSTANCE.getBusinessProcess() },
                { IArchimatePackage.eINSTANCE.getProduct() },
                { IArchimatePackage.eINSTANCE.getRepresentation() },
                { IArchimatePackage.eINSTANCE.getBusinessRole() },
                { IArchimatePackage.eINSTANCE.getBusinessService() },
                { IArchimatePackage.eINSTANCE.getValue() },
                
                { IArchimatePackage.eINSTANCE.getArtifact() },
                { IArchimatePackage.eINSTANCE.getCommunicationPath() },
                { IArchimatePackage.eINSTANCE.getDevice() },
                { IArchimatePackage.eINSTANCE.getInfrastructureFunction() },
                { IArchimatePackage.eINSTANCE.getInfrastructureInterface() },
                { IArchimatePackage.eINSTANCE.getInfrastructureService() },
                { IArchimatePackage.eINSTANCE.getNetwork() },
                { IArchimatePackage.eINSTANCE.getNode() },
                { IArchimatePackage.eINSTANCE.getSystemSoftware() },
                
                { IArchimatePackage.eINSTANCE.getAssessment() },
                { IArchimatePackage.eINSTANCE.getConstraint() },
                { IArchimatePackage.eINSTANCE.getDeliverable() },
                { IArchimatePackage.eINSTANCE.getDriver() },
                { IArchimatePackage.eINSTANCE.getGap() },
                { IArchimatePackage.eINSTANCE.getGoal() },
                { IArchimatePackage.eINSTANCE.getPlateau() },
                { IArchimatePackage.eINSTANCE.getPrinciple() },
                { IArchimatePackage.eINSTANCE.getRequirement() },
                { IArchimatePackage.eINSTANCE.getStakeholder() },
                { IArchimatePackage.eINSTANCE.getWorkPackage() }
        });
    }
    
    private EClass eClass;
    
    public AllArchimateTypeFigureTests(EClass eClass) {
        this.eClass = eClass;
    }

    @Override
    protected AbstractDiagramModelObjectFigure createFigure() {
        IDiagramModelArchimateObject dmo =
                ArchimateTestModel.createDiagramModelArchimateObject((IArchimateElement)IArchimateFactory.eINSTANCE.create(eClass));
        dmo.setBounds(IArchimateFactory.eINSTANCE.createBounds());
        dmo.setName("Hello World!");
        dm.getChildren().add(dmo);
        
        return (AbstractDiagramModelObjectFigure)editorHandler.findFigure(dmo);
    }
    
    @Override
    @Test
    public void testDidClickTestControl() {
        AsyncTestRunner runner = new AsyncTestRunner() {
            @Override
            public void run() {
                super.run();
                Rectangle bounds = abstractFigure.getTextControl().getBounds().getCopy();
                abstractFigure.getTextControl().translateToAbsolute(bounds);
                assertTrue(abstractFigure.didClickTextControl(new Point(bounds.x + 3, bounds.y + 3)));
            }
        };
        
        runner.start();
    }
}
