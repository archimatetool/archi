/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.tools;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.gef.commands.CompoundCommand;
import org.junit.jupiter.api.Test;

import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.ITextPosition;
import com.archimatetool.testingtools.ArchimateTestModel;

@SuppressWarnings("nls")
public class FormatPainterToolTests {
    
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
        FormatPainterInfo.INSTANCE.updateWithSourceComponent(sourceComponent);

        // Execute command
        FormatPainterTool tool = new FormatPainterTool();
        CompoundCommand compoundCmd = tool.createCommand(targetComponent);
        
        // Should be no commands
        assertEquals(0, compoundCmd.getCommands().size());
        
        // Set the source fill color to its default actual value.
        // The fill color command will not be added because the target's fill color (null) is effectively the same.
        sourceComponent.setFillColor("#ffffb5");
        compoundCmd = tool.createCommand(targetComponent);
        assertEquals(0, compoundCmd.getCommands().size());
        
        // Now change some attributes on the source component
        sourceComponent.setFillColor("#eeeeee");
        sourceComponent.setFont("Consolas");
        sourceComponent.setFontColor("#eeeeee");
        sourceComponent.setLineColor("#eeeeee");
        sourceComponent.setLineWidth(3);
        sourceComponent.setTextAlignment(1);
        sourceComponent.setAlpha(100);
        sourceComponent.setLineAlpha(100);
        sourceComponent.setGradient(IDiagramModelObject.GRADIENT_NONE + 1);
        sourceComponent.setIconColor("#ffeeee");
        sourceComponent.setTextPosition(ITextPosition.TEXT_POSITION_BOTTOM);
        sourceComponent.setDeriveElementLineColor(false);
        
        // But we have to reset the FormatPainterInfo with the source component because it makes a copy of it
        FormatPainterInfo.INSTANCE.updateWithSourceComponent(sourceComponent);
        
        compoundCmd = tool.createCommand(targetComponent);
        assertEquals(12, compoundCmd.getCommands().size());
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
        FormatPainterInfo.INSTANCE.updateWithSourceComponent(sourceComponent);

        // Execute command
        FormatPainterTool tool = new FormatPainterTool();
        CompoundCommand compoundCmd = tool.createCommand(targetComponent);
        
        // Should be no commands
        assertEquals(0, compoundCmd.getCommands().size());
        
        // Now change some properties on the source component
        sourceComponent.setFont("Consolas");
        sourceComponent.setFontColor("#eeeeee");
        sourceComponent.setLineColor("#eeeeee");
        sourceComponent.setLineWidth(3);
        sourceComponent.setTextPosition(3);
        
        // But we have to reset the FormatPainterInfo with the source component because it makes a copy of it
        FormatPainterInfo.INSTANCE.updateWithSourceComponent(sourceComponent);
        
        compoundCmd = tool.createCommand(targetComponent);
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