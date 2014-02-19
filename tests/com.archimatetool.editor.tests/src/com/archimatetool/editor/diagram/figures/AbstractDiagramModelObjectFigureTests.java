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
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.swt.graphics.Color;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.archimatetool.editor.diagram.IDiagramModelEditor;
import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.editor.model.impl.EditorModelManager;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.services.EditorManager;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.tests.AsyncTestRunner;
import com.archimatetool.tests.TestUtils;


@SuppressWarnings("nls")
public abstract class AbstractDiagramModelObjectFigureTests {
    
    protected static IArchimateModel model;
    protected static IDiagramModel dm;
    protected static IDiagramModelEditor editor;

    protected AbstractDiagramModelObjectFigure abstractFigure;
    protected IDiagramModelObject diagramModelObject;
    
    protected abstract AbstractDiagramModelObjectFigure createFigure();
    
    // Convenience method to find a Figure in an EditPart in a Viewer from the model object
    protected IFigure getFigureFromViewer(Object modelObject) {
        return ((GraphicalEditPart)editor.getGraphicalViewer().getEditPartRegistry().get(modelObject)).getFigure();
    }
    
    @BeforeClass
    public static void runOnceBeforeAllTests() {
        // Create a model with a default DiagramModel and open the editor
        IEditorModelManager editorModeManager = new EditorModelManager();
        model = editorModeManager.createNewModel();
        dm = model.getDefaultDiagramModel();
        editor = EditorManager.openDiagramEditor(dm);
    }

    @Before
    public void runBeforeEachAbstractTest() {
        // Get the figure and its DiagramModelObject
        abstractFigure = createFigure();
        diagramModelObject = abstractFigure.getDiagramModelObject();
    }

    @AfterClass
    public static void runOnceAfterAllTests() {
        AsyncTestRunner runner = new AsyncTestRunner() {
            @Override
            public void run() {
                super.run();
                TestUtils.closeAllEditors();
            }
        };
        runner.start();
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
        Color expected = new Color(null, 1, 2, 3);
        assertEquals(expected, abstractFigure.getFillColor());
        expected.dispose();
    }
    
    @Test
    public void testSetFontColor() {
        IFigure textControl = abstractFigure.getTextControl();
        if(textControl != null) {
            Color expected = new Color(null, 0, 0, 0);
            assertEquals(expected, textControl.getForegroundColor());
            expected.dispose();
            
            diagramModelObject.setFontColor("#010203");
            abstractFigure.setFontColor();
            expected = new Color(null, 1, 2, 3);
            assertEquals(expected, textControl.getForegroundColor());
            expected.dispose();
        }
    }
    
    @Test
    public void testSetLineColor() {
        Preferences.STORE.setValue(IPreferenceConstants.DERIVE_ELEMENT_LINE_COLOR, false);
        
        assertEquals(ColorFactory.getDefaultLineColor(diagramModelObject), abstractFigure.getLineColor());
        
        diagramModelObject.setLineColor("#010203");
        abstractFigure.setLineColor();
        Color expected = new Color(null, 1, 2, 3);
        assertEquals(expected, abstractFigure.getLineColor());
        expected.dispose();
    }
    
    @Test
    public void testGetTooltip() {
        Preferences.STORE.setValue(IPreferenceConstants.VIEW_TOOLTIPS, true);
        assertTrue(abstractFigure.getToolTip() instanceof ToolTipFigure);
        Preferences.STORE.setValue(IPreferenceConstants.VIEW_TOOLTIPS, false);
        assertNull(abstractFigure.getToolTip());
    }
    
    @Test
    public void testDidClickTestControl() {
        assertFalse(abstractFigure.didClickTextControl(new Point(10, 10)));
    }

}
