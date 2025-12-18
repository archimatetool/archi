/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.EditPart;
import org.junit.jupiter.params.provider.Arguments;

import com.archimatetool.editor.ParamsTest;
import com.archimatetool.editor.diagram.editparts.diagram.DiagramImageEditPart;
import com.archimatetool.editor.ui.factory.diagram.DiagramImageUIProvider;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelImage;
import com.archimatetool.model.IDiagramModelObject;

public class DiagramModelImageUIProviderTests extends AbstractGraphicalObjectUIProviderTests {
    
    static Stream<Arguments> getParams() {
        return Stream.of(
                getParam(new DiagramImageUIProvider(), IArchimatePackage.eINSTANCE.getDiagramModelImage())
        );
    }

    @Override
    @ParamsTest
    public void testCreateEditPart(IObjectUIProvider provider) {
        EditPart editPart = provider.createEditPart();
        assertTrue(editPart instanceof DiagramImageEditPart);
    }

    @Override
    @ParamsTest
    public void testGetDefaultSize(IGraphicalObjectUIProvider provider) {
        assertEquals(new Dimension(200, 150), provider.getDefaultSize());
    }

    @Override
    @ParamsTest
    public void testShouldExposeFeature(IObjectUIProvider provider) {
        super.testShouldExposeFeature(provider);
        assertTrue(provider.shouldExposeFeature(IArchimatePackage.Literals.PROPERTIES__PROPERTIES.getName()));
        assertTrue(provider.shouldExposeFeature(IArchimatePackage.Literals.LINE_OBJECT__LINE_WIDTH.getName()));
        assertTrue(provider.shouldExposeFeature(IArchimatePackage.Literals.BORDER_OBJECT__BORDER_COLOR.getName()));
        assertTrue(provider.shouldExposeFeature(IArchimatePackage.Literals.DIAGRAM_MODEL_OBJECT__ALPHA.getName()));
        assertTrue(provider.shouldExposeFeature(IDiagramModelObject.FEATURE_LINE_ALPHA));
        assertTrue(provider.shouldExposeFeature(IDiagramModelObject.FEATURE_LINE_STYLE));
        
        assertFalse(provider.shouldExposeFeature(IDiagramModelObject.FEATURE_GRADIENT));
    }
    
    @Override
    @ParamsTest
    public void testGetFeatureValue(IObjectUIProvider provider) {
        super.testGetFeatureValue(provider);
        IDiagramModelImage dmi = IArchimateFactory.eINSTANCE.createDiagramModelImage();
        ((AbstractObjectUIProvider)provider).setInstance(dmi);
        
        assertEquals(IDiagramModelObject.LINE_STYLE_SOLID, provider.getFeatureValue(IDiagramModelObject.FEATURE_LINE_STYLE));
    }

}
