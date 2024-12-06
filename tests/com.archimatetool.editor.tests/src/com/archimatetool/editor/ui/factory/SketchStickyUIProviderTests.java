/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.EditPart;
import org.junit.jupiter.params.provider.Arguments;

import com.archimatetool.editor.ParamsTest;
import com.archimatetool.editor.diagram.sketch.editparts.StickyEditPart;
import com.archimatetool.editor.ui.factory.sketch.SketchStickyUIProvider;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.ISketchModelSticky;
import com.archimatetool.model.ITextAlignment;

public class SketchStickyUIProviderTests extends AbstractGraphicalObjectUIProviderTests {
    
    static Stream<Arguments> getParams() {
        return Stream.of(
                getParam(new SketchStickyUIProvider(), IArchimatePackage.eINSTANCE.getSketchModelSticky())
        );
    }

    @Override
    @ParamsTest
    public void testCreateEditPart(IObjectUIProvider provider) {
        EditPart editPart = provider.createEditPart();
        assertTrue(editPart instanceof StickyEditPart);
    }
    
    @Override
    @ParamsTest
    public void testGetDefaultSize(IGraphicalObjectUIProvider provider) {
        assertEquals(new Dimension(135, 70), provider.getDefaultSize());
    }

    @Override
    @ParamsTest
    public void testGetDefaultTextAlignment(IGraphicalObjectUIProvider provider) {
        assertEquals(ITextAlignment.TEXT_ALIGNMENT_LEFT, provider.getDefaultTextAlignment());
    }
    
    @Override
    @ParamsTest
    public void testGetFeatureValue(IObjectUIProvider provider) {
        super.testGetFeatureValue(provider);
        ISketchModelSticky sticky = IArchimateFactory.eINSTANCE.createSketchModelSticky();
        ((AbstractObjectUIProvider)provider).setInstance(sticky);
        
        assertEquals(IDiagramModelObject.LINE_STYLE_SOLID, provider.getFeatureValue(IDiagramModelObject.FEATURE_LINE_STYLE));
    }
}
