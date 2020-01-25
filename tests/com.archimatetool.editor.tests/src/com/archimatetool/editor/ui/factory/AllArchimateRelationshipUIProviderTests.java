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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.archimatetool.editor.ui.factory.relationships.AccessRelationshipUIProvider;
import com.archimatetool.editor.ui.factory.relationships.AggregationRelationshipUIProvider;
import com.archimatetool.editor.ui.factory.relationships.AssignmentRelationshipUIProvider;
import com.archimatetool.editor.ui.factory.relationships.AssociationRelationshipUIProvider;
import com.archimatetool.editor.ui.factory.relationships.CompositionRelationshipUIProvider;
import com.archimatetool.editor.ui.factory.relationships.FlowRelationshipUIProvider;
import com.archimatetool.editor.ui.factory.relationships.InfluenceRelationshipUIProvider;
import com.archimatetool.editor.ui.factory.relationships.RealizationRelationshipUIProvider;
import com.archimatetool.editor.ui.factory.relationships.ServingRelationshipUIProvider;
import com.archimatetool.editor.ui.factory.relationships.SpecializationRelationshipUIProvider;
import com.archimatetool.editor.ui.factory.relationships.TriggeringRelationshipUIProvider;
import com.archimatetool.model.IArchimatePackage;

import junit.framework.JUnit4TestAdapter;

@RunWith(Parameterized.class)
public class AllArchimateRelationshipUIProviderTests extends AbstractGraphicalObjectUIProviderTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(AllArchimateRelationshipUIProviderTests.class);
    }
    
    @Parameters
    public static Collection<Object[]> eObjects() {
        return Arrays.asList(new Object[][] {
                { new AccessRelationshipUIProvider(), IArchimatePackage.eINSTANCE.getAccessRelationship() },
                { new AggregationRelationshipUIProvider(), IArchimatePackage.eINSTANCE.getAggregationRelationship() },
                { new AssignmentRelationshipUIProvider(), IArchimatePackage.eINSTANCE.getAssignmentRelationship() },
                { new AssociationRelationshipUIProvider(), IArchimatePackage.eINSTANCE.getAssociationRelationship() },
                { new CompositionRelationshipUIProvider(), IArchimatePackage.eINSTANCE.getCompositionRelationship() },
                { new InfluenceRelationshipUIProvider(), IArchimatePackage.eINSTANCE.getInfluenceRelationship() },
                { new FlowRelationshipUIProvider(), IArchimatePackage.eINSTANCE.getFlowRelationship() },
                { new RealizationRelationshipUIProvider(), IArchimatePackage.eINSTANCE.getRealizationRelationship() },
                { new ServingRelationshipUIProvider(), IArchimatePackage.eINSTANCE.getServingRelationship() },
                { new SpecializationRelationshipUIProvider(), IArchimatePackage.eINSTANCE.getSpecializationRelationship() },
                { new TriggeringRelationshipUIProvider(), IArchimatePackage.eINSTANCE.getTriggeringRelationship() },
        });
    }
    
    public AllArchimateRelationshipUIProviderTests(IObjectUIProvider provider, EClass expectedClass) {
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
        assertEquals(ColorConstants.black, getProvider().getDefaultColor());
    }
    
    @Override
    public void testGetDefaultLineColor() {
        assertEquals(ColorConstants.black, getProvider().getDefaultLineColor());
    }

    @Override
    @Test
    public void testGetDefaultSize() {
        assertEquals(new Dimension(-1, -1), getProvider().getDefaultSize());
    }
}
