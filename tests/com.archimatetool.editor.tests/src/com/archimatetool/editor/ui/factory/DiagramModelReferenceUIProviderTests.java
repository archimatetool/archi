/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.EditPart;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.archimatetool.editor.diagram.editparts.diagram.DiagramModelReferenceEditPart;
import com.archimatetool.editor.ui.factory.diagram.DiagramModelReferenceUIProvider;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelReference;

public class DiagramModelReferenceUIProviderTests extends AbstractGraphicalObjectUIProviderTests {
    
    static Stream<Arguments> getParams() {
        return Stream.of(
                getParam(new DiagramModelReferenceUIProvider(), IArchimatePackage.eINSTANCE.getDiagramModelReference())
        );
    }

    @Override
    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testCreateEditPart(IObjectUIProvider provider) {
        EditPart editPart = provider.createEditPart();
        assertTrue(editPart instanceof DiagramModelReferenceEditPart);
    }
    
    @Override
    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testGetDefaultColor(IGraphicalObjectUIProvider provider) {
        Color color = provider.getDefaultColor();
        assertEquals(new Color(220, 235, 235), color);
    }
    
    @Override
    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testGetDefaultSize(IGraphicalObjectUIProvider provider) {
        assertEquals(new Dimension(120, 55), provider.getDefaultSize());
    }

    @Override
    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testGetImageInstance(IObjectUIProvider provider, EClass expectedClass) {
        IDiagramModelReference ref = (IDiagramModelReference)IArchimateFactory.eINSTANCE.create(expectedClass);
        ref.setReferencedModel(IArchimateFactory.eINSTANCE.createArchimateDiagramModel());
        ((AbstractObjectUIProvider)provider).setInstance(ref);
        
        Image image = provider.getImage();
        assertNotNull(image);
        
        ref.setReferencedModel(IArchimateFactory.eINSTANCE.createSketchModel());
        image = provider.getImage();
        assertNotNull(image);
    }
    
    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testHasIcon(IGraphicalObjectUIProvider provider) {
        assertTrue(provider.hasIcon());
    }

}
