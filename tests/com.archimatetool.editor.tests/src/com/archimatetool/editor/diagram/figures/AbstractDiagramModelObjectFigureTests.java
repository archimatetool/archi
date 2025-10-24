/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.params.provider.Arguments;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.ParamsTest;
import com.archimatetool.editor.diagram.figures.elements.GroupingFigure;
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
    
    /**
     * Create an Arguments parameter for a figure
     */
    protected static Arguments getParam(IFigure figure) {
        return Arguments.of(Named.of(figure.getClass().getSimpleName(), figure));
    }

    protected static ArchimateTestEditor editor;

    @BeforeAll
    public static void runOnceBeforeAllTests() {
        // Create a new model
        ArchimateTestModel tm = new ArchimateTestModel();
        IArchimateModel model = tm.createNewModel();
        
        // Create a new test editor and add the default DiagramModel from the model
        editor = new ArchimateTestEditor();
        editor.setDiagramModel((IArchimateDiagramModel)model.getDefaultDiagramModel());
    }
    
    @AfterAll
    public static void runOnceAfterAllTests() {
        editor.dispose();
    }
    
    /**
     * Add a DiagramModelObject to the parent DiagramModel and return the associated figure
     */
    protected static IFigure addDiagramModelObjectToModelAndFindFigure(IDiagramModelObject dmo) {
        // Add to the diagram model
        editor.getDiagramModel().getChildren().add(dmo);
        
        // Layout editor
        editor.layoutPendingUpdates();

        // Find the figure and return it
        return editor.findFigure(dmo);
    }

    @ParamsTest
    public void testGetBounds(AbstractDiagramModelObjectFigure figure) {
        assertEquals(new Rectangle(0, 0, figure.getDefaultSize().width, figure.getDefaultSize().height),
                figure.getBounds());
    }

    @ParamsTest
    public void testGetDiagramModelObject(AbstractDiagramModelObjectFigure figure) {
        assertNotNull(figure.getDiagramModelObject());
    }
    
    @ParamsTest
    public void testSetFont(AbstractDiagramModelObjectFigure figure) {
        assertNotNull(figure.getFont());
    }
    
    @ParamsTest
    public void testSetFillColor(AbstractDiagramModelObjectFigure figure) {
        assertEquals(ColorFactory.getDefaultFillColor(figure.getDiagramModelObject()), figure.getFillColor());
        
        figure.getDiagramModelObject().setFillColor("#010203");
        figure.setFillColor();
        Color expected = new Color(1, 2, 3);
        assertEquals(expected, figure.getFillColor());
    }
    
    @ParamsTest
    public void testSetFontColor(AbstractDiagramModelObjectFigure figure) {
        IFigure textControl = figure.getTextControl();
        if(textControl != null) {
            figure.getDiagramModelObject().setFontColor("#010203");
            figure.setFontColor();
            Color expected = new Color(1, 2, 3);
            assertEquals(expected, textControl.getForegroundColor());
        }
    }
    
    @ParamsTest
    public void testSetLineColor(AbstractDiagramModelObjectFigure figure) {
        figure.getDiagramModelObject().setDeriveElementLineColor(false);
        
        assertEquals(ColorFactory.getDefaultLineColor(figure.getDiagramModelObject()), figure.getLineColor());
        
        figure.getDiagramModelObject().setLineColor("#010203");
        figure.setLineColor();
        Color expected = new Color(1, 2, 3);
        assertEquals(expected, figure.getLineColor());
    }
    
    @ParamsTest
    public void testGetTooltip(AbstractDiagramModelObjectFigure figure) {
        ArchiPlugin.getInstance().getPreferenceStore().setValue(IPreferenceConstants.VIEW_TOOLTIPS, true);
        assertTrue(figure.getToolTip() instanceof ToolTipFigure);
        ArchiPlugin.getInstance().getPreferenceStore().setValue(IPreferenceConstants.VIEW_TOOLTIPS, false);
        assertNull(figure.getToolTip());
    }
    
    @ParamsTest
    public void testDidClickTextControl(AbstractDiagramModelObjectFigure figure) {
        assertFalse(figure.didClickTextControl(new Point(10, 10)));
    }

    @ParamsTest
    public void testGetDefaultSize(AbstractDiagramModelObjectFigure figure) {
        IGraphicalObjectUIProvider provider = (IGraphicalObjectUIProvider)ObjectUIFactory.INSTANCE.getProvider(figure.getDiagramModelObject());
        assertEquals(provider.getDefaultSize(), figure.getDefaultSize());
    }

    @ParamsTest
    public void testGetDefaultConnectionAnchor(AbstractDiagramModelObjectFigure figure) {
        assertNotNull(figure.getDefaultConnectionAnchor());
    }
    
    @ParamsTest
    public void testGetAlpha(AbstractDiagramModelObjectFigure figure) {
        assertEquals(255, figure.getAlpha());
        figure.getDiagramModelObject().setAlpha(10);
        assertEquals(10, figure.getAlpha());
    }
    
    @ParamsTest
    public void testGetGradient(AbstractDiagramModelObjectFigure figure) {
        assertEquals(IDiagramModelObject.FEATURE_GRADIENT_DEFAULT, figure.getGradient());
        figure.getDiagramModelObject().setGradient(2);
        assertEquals(2, figure.getGradient());
    }
    
    @ParamsTest
    public void testGetIconColor(AbstractDiagramModelObjectFigure figure) {
        assertEquals(ColorConstants.black, figure.getIconColor());
        figure.getDiagramModelObject().setIconColor("#010203");
        assertEquals(new Color(1, 2, 3), figure.getIconColor());
    }

    @ParamsTest
    public void testGetLineStyle(AbstractDiagramModelObjectFigure figure) {
        if(figure instanceof GroupingFigure) {
            assertEquals(IDiagramModelObject.LINE_STYLE_DASHED, figure.getLineStyle());
        }
        else {
            assertEquals(IDiagramModelObject.LINE_STYLE_SOLID, figure.getLineStyle());
        }
        
        figure.getDiagramModelObject().setLineStyle(IDiagramModelObject.LINE_STYLE_DOTTED);
        assertEquals(IDiagramModelObject.LINE_STYLE_DOTTED, figure.getLineStyle());
    }

    @ParamsTest
    public void testGetLineWidth(AbstractDiagramModelObjectFigure figure) {
        assertEquals(1, figure.getLineWidth());
        figure.getDiagramModelObject().setLineWidth(2);
        assertEquals(2, figure.getLineWidth());
    }
    
    @ParamsTest
    public void testGetLineAlpha(AbstractDiagramModelObjectFigure figure) {
        assertEquals(255, figure.getLineAlpha());
        figure.getDiagramModelObject().setLineAlpha(23);
        assertEquals(23, figure.getLineAlpha());
    }

    @ParamsTest
    public void testGetIconicDelegate(AbstractDiagramModelObjectFigure figure) {
        assertNotNull(figure.getIconicDelegate());
    }
}
