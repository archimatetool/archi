/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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

public class AllArchimateRelationshipUIProviderTests extends AbstractGraphicalObjectUIProviderTests {
    
    static Stream<Arguments> getParams() {
        return Stream.of(
                getParam(new AccessRelationshipUIProvider(), IArchimatePackage.eINSTANCE.getAccessRelationship()),
                getParam(new AggregationRelationshipUIProvider(), IArchimatePackage.eINSTANCE.getAggregationRelationship()),
                getParam(new AssignmentRelationshipUIProvider(), IArchimatePackage.eINSTANCE.getAssignmentRelationship()),
                getParam(new AssociationRelationshipUIProvider(), IArchimatePackage.eINSTANCE.getAssociationRelationship()),
                getParam(new CompositionRelationshipUIProvider(), IArchimatePackage.eINSTANCE.getCompositionRelationship()),
                getParam(new InfluenceRelationshipUIProvider(), IArchimatePackage.eINSTANCE.getInfluenceRelationship()),
                getParam(new FlowRelationshipUIProvider(), IArchimatePackage.eINSTANCE.getFlowRelationship()),
                getParam(new RealizationRelationshipUIProvider(), IArchimatePackage.eINSTANCE.getRealizationRelationship()),
                getParam(new ServingRelationshipUIProvider(), IArchimatePackage.eINSTANCE.getServingRelationship()),
                getParam(new SpecializationRelationshipUIProvider(), IArchimatePackage.eINSTANCE.getSpecializationRelationship()),
                getParam(new TriggeringRelationshipUIProvider(), IArchimatePackage.eINSTANCE.getTriggeringRelationship())
        );
    }
    
    @Override
    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testCreateEditPart(IObjectUIProvider provider) {
        EditPart editPart = provider.createEditPart();
        assertTrue(editPart instanceof AbstractConnectionEditPart);
    }
    
    @Override
    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testGetDefaultColor(IGraphicalObjectUIProvider provider) {
        assertEquals(ColorConstants.black, provider.getDefaultColor());
    }
    
    @Override
    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testGetDefaultLineColor(IGraphicalObjectUIProvider provider) {
        assertEquals(ColorConstants.black, provider.getDefaultLineColor());
    }

    @Override
    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testGetDefaultSize(IGraphicalObjectUIProvider provider) {
        assertEquals(new Dimension(-1, -1), provider.getDefaultSize());
    }
}
