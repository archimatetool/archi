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

import com.archimatetool.editor.ui.factory.diagram.LineConnectionUIProvider;
import com.archimatetool.editor.ui.factory.relationships.AccessRelationshipUIProvider;
import com.archimatetool.editor.ui.factory.relationships.AggregationRelationshipUIProvider;
import com.archimatetool.editor.ui.factory.relationships.AssignmentRelationshipUIProvider;
import com.archimatetool.editor.ui.factory.relationships.AssociationRelationshipUIProvider;
import com.archimatetool.editor.ui.factory.relationships.CompositionRelationshipUIProvider;
import com.archimatetool.editor.ui.factory.relationships.FlowRelationshipUIProvider;
import com.archimatetool.editor.ui.factory.relationships.InfluenceRelationshipUIProvider;
import com.archimatetool.editor.ui.factory.relationships.RealisationRelationshipUIProvider;
import com.archimatetool.editor.ui.factory.relationships.SpecialisationRelationshipUIProvider;
import com.archimatetool.editor.ui.factory.relationships.TriggeringRelationshipUIProvider;
import com.archimatetool.editor.ui.factory.relationships.UsedByRelationshipUIProvider;
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
                { new AccessRelationshipUIProvider(), IArchimatePackage.eINSTANCE.getAccessRelationship() },
                { new AggregationRelationshipUIProvider(), IArchimatePackage.eINSTANCE.getAggregationRelationship() },
                { new AssignmentRelationshipUIProvider(), IArchimatePackage.eINSTANCE.getAssignmentRelationship() },
                { new AssociationRelationshipUIProvider(), IArchimatePackage.eINSTANCE.getAssociationRelationship() },
                { new CompositionRelationshipUIProvider(), IArchimatePackage.eINSTANCE.getCompositionRelationship() },
                { new FlowRelationshipUIProvider(), IArchimatePackage.eINSTANCE.getFlowRelationship() },
                { new RealisationRelationshipUIProvider(), IArchimatePackage.eINSTANCE.getRealisationRelationship() },
                { new SpecialisationRelationshipUIProvider(), IArchimatePackage.eINSTANCE.getSpecialisationRelationship() },
                { new TriggeringRelationshipUIProvider(), IArchimatePackage.eINSTANCE.getTriggeringRelationship() },
                { new UsedByRelationshipUIProvider(), IArchimatePackage.eINSTANCE.getUsedByRelationship() },
                { new InfluenceRelationshipUIProvider(), IArchimatePackage.eINSTANCE.getInfluenceRelationship() },
                
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
