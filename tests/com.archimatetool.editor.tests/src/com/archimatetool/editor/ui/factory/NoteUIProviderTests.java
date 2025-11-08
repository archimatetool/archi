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
import com.archimatetool.editor.diagram.editparts.diagram.LegendEditPart;
import com.archimatetool.editor.diagram.editparts.diagram.NoteEditPart;
import com.archimatetool.editor.ui.factory.diagram.NoteUIProvider;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelNote;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.ITextAlignment;

public class NoteUIProviderTests extends AbstractGraphicalObjectUIProviderTests {
    
    static Stream<Arguments> getParams() {
        return Stream.of(
                getParam(new NoteUIProvider(), IArchimatePackage.eINSTANCE.getDiagramModelNote())
        );
    }

    @Override
    @ParamsTest
    public void testCreateEditPart(IObjectUIProvider provider) {
        EditPart editPart = provider.createEditPart();
        assertTrue(editPart instanceof NoteEditPart);
    }
    
    @Override
    @ParamsTest
    public void testGetDefaultSize(IGraphicalObjectUIProvider provider) {
        assertEquals(new Dimension(185, 80), provider.getDefaultSize());
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
        setProviderInstance(provider, false);
        assertEquals(IDiagramModelObject.LINE_STYLE_SOLID, provider.getFeatureValue(IDiagramModelObject.FEATURE_LINE_STYLE));
    }
    
    // Legend tests
    
    @ParamsTest
    public void testLegendCreateEditPart(IObjectUIProvider provider) {
        setProviderInstance(provider, true);
        EditPart editPart = provider.createEditPart();
        assertTrue(editPart instanceof LegendEditPart);
    }
    
    @ParamsTest
    public void testLegendGetDefaultSize(IGraphicalObjectUIProvider provider) {
        setProviderInstance(provider, true);
        assertEquals(new Dimension(210, 320), provider.getDefaultSize());
    }
    
    //---------------------------------------------------------------
    
    // Set the provider instance to a Note or Note with legend
    private void setProviderInstance(IObjectUIProvider provider, boolean isLegend) {
        IDiagramModelNote note = IArchimateFactory.eINSTANCE.createDiagramModelNote();
        note.setIsLegend(isLegend);
        ((AbstractObjectUIProvider)provider).setInstance(note);
    }
}
