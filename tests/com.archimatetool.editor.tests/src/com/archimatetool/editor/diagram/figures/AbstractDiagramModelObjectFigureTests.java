/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.factory.IGraphicalObjectUIProvider;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.testingtools.ArchimateTestEditor;
import com.archimatetool.testingtools.ArchimateTestModel;


@SuppressWarnings("nls")
public abstract class AbstractDiagramModelObjectFigureTests {
    
    protected static ArchimateTestModel tm;
    protected static IArchimateModel model;
    protected static IArchimateDiagramModel dm;
    protected static ArchimateTestEditor editor;

    protected AbstractDiagramModelObjectFigure abstractFigure;
    protected IDiagramModelObject diagramModelObject;
    
    protected abstract AbstractDiagramModelObjectFigure createFigure();
    
    @BeforeClass
    public static void runOnceBeforeAllTests() {
        // Create a new model, get the default DiagramModel and open the test editor
        tm = new ArchimateTestModel();
        model = tm.createNewModel();
        dm = (IArchimateDiagramModel)model.getDefaultDiagramModel();
        
        editor = new ArchimateTestEditor();
        editor.setDiagramModel(dm);
    }
    
    @AfterClass
    public static void runOnceAfterAllTests() {
        editor.dispose();
    }

    @Before
    public void runBeforeEachAbstractTest() {
        // Get the figure and its DiagramModelObject
        abstractFigure = createFigure();
        diagramModelObject = abstractFigure.getDiagramModelObject();
    }

    @Test
    public void testGetBounds() {
        assertEquals(new Rectangle(0, 0, abstractFigure.getDefaultSize().width, abstractFigure.getDefaultSize().height),
                abstractFigure.getBounds());
    }

    @Test
    public void testGetDiagramModelObject() {
        assertNotNull(diagramModelObject);
    }
    
    @Test
    public void testSetFont() {
        assertNotNull(abstractFigure.getFont());
    }
    
    @Test
    public void testSetFillColor() {
        assertEquals(ColorFactory.getDefaultFillColor(diagramModelObject), abstractFigure.getFillColor());
        
        diagramModelObject.setFillColor("#010203");
        abstractFigure.setFillColor();
        Color expected = new Color(1, 2, 3);
        assertEquals(expected, abstractFigure.getFillColor());
    }
    
    @Test
    public void testSetFontColor() {
        IFigure textControl = abstractFigure.getTextControl();
        if(textControl != null) {
            diagramModelObject.setFontColor("#010203");
            abstractFigure.setFontColor();
            Color expected = new Color(1, 2, 3);
            assertEquals(expected, textControl.getForegroundColor());
        }
    }
    
    @Test
    public void testSetLineColor() {
        ArchiPlugin.PREFERENCES.setValue(IPreferenceConstants.DERIVE_ELEMENT_LINE_COLOR, false);
        
        assertEquals(ColorFactory.getDefaultLineColor(diagramModelObject), abstractFigure.getLineColor());
        
        diagramModelObject.setLineColor("#010203");
        abstractFigure.setLineColor();
        Color expected = new Color(1, 2, 3);
        assertEquals(expected, abstractFigure.getLineColor());
    }
    
    @Test
    public void testGetTooltip() {
        ArchiPlugin.PREFERENCES.setValue(IPreferenceConstants.VIEW_TOOLTIPS, true);
        assertTrue(abstractFigure.getToolTip() instanceof ToolTipFigure);
        ArchiPlugin.PREFERENCES.setValue(IPreferenceConstants.VIEW_TOOLTIPS, false);
        assertNull(abstractFigure.getToolTip());
    }
    
    @Test
    public void testDidClickTextControl() {
        assertFalse(abstractFigure.didClickTextControl(new Point(10, 10)));
    }

    @Test
    public void testGetDefaultSize() {
        IGraphicalObjectUIProvider provider = (IGraphicalObjectUIProvider)ObjectUIFactory.INSTANCE.getProvider(abstractFigure.getDiagramModelObject());
        assertEquals(provider.getDefaultSize(), abstractFigure.getDefaultSize());
    }

    @Test
    public void testGetDefaultConnectionAnchor() {
        assertNotNull(abstractFigure.getDefaultConnectionAnchor());
    }
}
