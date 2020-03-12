/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.tools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import junit.framework.JUnit4TestAdapter;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.junit.Test;

import com.archimatetool.editor.diagram.commands.FillColorCommand;
import com.archimatetool.editor.diagram.tools.FormatPainterInfo.PaintFormat;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.testingtools.ArchimateTestModel;
import com.archimatetool.tests.TestUtils;

@SuppressWarnings("nls")
public class FormatPainterToolTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(FormatPainterToolTests.class);
    }
    
    
    // ---------------------------------------------------------------------------------------------
    // Tests
    // ---------------------------------------------------------------------------------------------

    @Test
    public void testCreateCommandForDiagramModelArchimateObject() throws Exception {
        // Source component
        IDiagramModelArchimateObject sourceComponent =
                ArchimateTestModel.createDiagramModelArchimateObject(IArchimateFactory.eINSTANCE.createBusinessActor());
        
        // Target component
        IDiagramModelArchimateObject targetComponent =
                ArchimateTestModel.createDiagramModelArchimateObject(IArchimateFactory.eINSTANCE.createBusinessActor());
        
        // Set FormatPainterInfo to Source component
        FormatPainterInfo.INSTANCE.updatePaintFormat(sourceComponent);
        PaintFormat pf = FormatPainterInfo.INSTANCE.getPaintFormat();

        // Execute command
        FormatPainterTool tool = new FormatPainterTool();
        CompoundCommand compoundCmd = tool.createCommand(pf, targetComponent);
        
        // Source and Target have same properties except for fill color so only one command
        assertEquals(1, compoundCmd.getCommands().size());
        
        // Fill Color should be set even if fill colour source is null (default)
        Command cmd = (Command)compoundCmd.getCommands().get(0);
        assertTrue(cmd instanceof FillColorCommand);
        Object newValue = TestUtils.getPrivateField(cmd, "fNewValue");
        assertEquals("#ffffb5", newValue);
        
        // Now change some properties on the source component
        sourceComponent.setFont("Consolas");
        sourceComponent.setFontColor("#eeeeee");
        sourceComponent.setLineColor("#eeeeee");
        sourceComponent.setLineWidth(3);
        sourceComponent.setTextAlignment(1);
        sourceComponent.setAlpha(100);
        sourceComponent.setLineAlpha(100);
        sourceComponent.setGradient(IDiagramModelObject.GRADIENT_NONE + 1);
        
        compoundCmd = tool.createCommand(pf, targetComponent);
        assertEquals(9, compoundCmd.getCommands().size());
    }
    
    @Test
    public void testCreateCommandForDiagramModelArchimateConnection() throws Exception {
        // Source component
        IDiagramModelArchimateConnection sourceComponent =
                ArchimateTestModel.createDiagramModelArchimateConnection(IArchimateFactory.eINSTANCE.createAccessRelationship());
        
        // Target component
        IDiagramModelArchimateConnection targetComponent =
                ArchimateTestModel.createDiagramModelArchimateConnection(IArchimateFactory.eINSTANCE.createAccessRelationship());
        
        // Set FormatPainterInfo to Source component
        FormatPainterInfo.INSTANCE.updatePaintFormat(sourceComponent);
        PaintFormat pf = FormatPainterInfo.INSTANCE.getPaintFormat();

        // Execute command
        FormatPainterTool tool = new FormatPainterTool();
        CompoundCommand compoundCmd = tool.createCommand(pf, targetComponent);
        
        // Should be no commands
        assertEquals(0, compoundCmd.getCommands().size());
        
        // Now change some properties on the source component
        sourceComponent.setFont("Consolas");
        sourceComponent.setFontColor("#eeeeee");
        sourceComponent.setLineColor("#eeeeee");
        sourceComponent.setLineWidth(3);
        sourceComponent.setTextPosition(3);
        
        compoundCmd = tool.createCommand(pf, targetComponent);
        assertEquals(5, compoundCmd.getCommands().size());
    }

    @Test
    public void isPaintableObject() {
        FormatPainterTool tool = new FormatPainterTool();
        
        assertTrue(tool.isPaintableObject(IArchimateFactory.eINSTANCE.createDiagramModelConnection()));
        assertTrue(tool.isPaintableObject(IArchimateFactory.eINSTANCE.createDiagramModelNote()));
        assertTrue(tool.isPaintableObject(IArchimateFactory.eINSTANCE.createDiagramModelGroup()));
        
        assertFalse(tool.isPaintableObject(IArchimateFactory.eINSTANCE.createDiagramModelImage()));
        
        IDiagramModelArchimateObject dmao =
                ArchimateTestModel.createDiagramModelArchimateObject(IArchimateFactory.eINSTANCE.createBusinessActor());
        assertTrue(tool.isPaintableObject(dmao));
        
        dmao = ArchimateTestModel.createDiagramModelArchimateObject(IArchimateFactory.eINSTANCE.createJunction());
        assertFalse(tool.isPaintableObject(dmao));
    }
}