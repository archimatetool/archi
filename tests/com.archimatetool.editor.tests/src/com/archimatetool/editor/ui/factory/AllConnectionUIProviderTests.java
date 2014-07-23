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
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

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
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.tests.TestUtils;

@RunWith(Parameterized.class)
public class AllConnectionUIProviderTests extends AbstractElementUIProviderTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(AllConnectionUIProviderTests.class);
    }
    
    // Need to ensure current display for ImageRegistry
    static {
        TestUtils.ensureDefaultDisplay();
    }
    
    @Parameters
    public static Collection<Object[]> eObjects() {
        return Arrays.asList(new Object[][] {
                { new AccessConnectionUIProvider(), IArchimatePackage.eINSTANCE.getAccessRelationship() },
                { new AggregationConnectionUIProvider(), IArchimatePackage.eINSTANCE.getAggregationRelationship() },
                { new AssignmentConnectionUIProvider(), IArchimatePackage.eINSTANCE.getAssignmentRelationship() },
                { new AssociationConnectionUIProvider(), IArchimatePackage.eINSTANCE.getAssociationRelationship() },
                { new CompositionConnectionUIProvider(), IArchimatePackage.eINSTANCE.getCompositionRelationship() },
                { new FlowConnectionUIProvider(), IArchimatePackage.eINSTANCE.getFlowRelationship() },
                { new RealisationConnectionUIProvider(), IArchimatePackage.eINSTANCE.getRealisationRelationship() },
                { new SpecialisationConnectionUIProvider(), IArchimatePackage.eINSTANCE.getSpecialisationRelationship() },
                { new TriggeringConnectionUIProvider(), IArchimatePackage.eINSTANCE.getTriggeringRelationship() },
                { new UsedByConnectionUIProvider(), IArchimatePackage.eINSTANCE.getUsedByRelationship() },
                { new InfluenceConnectionUIProvider(), IArchimatePackage.eINSTANCE.getInfluenceRelationship() },
                
                { new LineConnectionUIProvider(), IArchimatePackage.eINSTANCE.getDiagramModelConnection() }
        });
    }
    
    public AllConnectionUIProviderTests(IElementUIProvider provider, EClass expectedClass) {
        this.provider = provider;
        this.expectedClass = expectedClass;
    }
    
    @Override
    public void testCreateEditPart() {
        EditPart editPart = provider.createEditPart();
        assertTrue(editPart instanceof AbstractConnectionEditPart);
    }
    
    @Override
    @Test
    public void testGetDefaultColor() {
        assertEquals(ColorConstants.black, provider.getDefaultColor());
    }
    
    @Override
    public void testGetDefaultLineColor() {
        assertEquals(ColorConstants.black, provider.getDefaultLineColor());
    }

    @Override
    @Test
    public void testGetDefaultSize() {
        assertEquals(new Dimension(-1, -1), provider.getDefaultSize());
    }

}
