/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelBendpoint;
import com.archimatetool.testingtools.ArchimateTestModel;


public class InvertConnectionCommandTests {
    
    @Test
    public void testInvertedConnection() {
        ArchimateTestModel tm = new ArchimateTestModel();
        IArchimateModel model = tm.createNewModel();
        
        IArchimateElement source = IArchimateFactory.eINSTANCE.createBusinessActor();
        IArchimateElement target = IArchimateFactory.eINSTANCE.createBusinessActor();
        
        IArchimateRelationship relationship = IArchimateFactory.eINSTANCE.createCompositionRelationship();
        relationship.setSource(source);
        relationship.setTarget(target);

        IDiagramModelArchimateObject dmo1 = tm.createDiagramModelArchimateObjectAndAddToModel(source);
        model.getDefaultDiagramModel().getChildren().add(dmo1);

        IDiagramModelArchimateObject dmo2 = tm.createDiagramModelArchimateObjectAndAddToModel(target);
        model.getDefaultDiagramModel().getChildren().add(dmo2);

        IDiagramModelArchimateConnection connection = tm.createDiagramModelArchimateConnectionAndAddToModel(relationship);
        connection.connect(dmo1, dmo2);
        
        IDiagramModelArchimateConnection connection2 = tm.createDiagramModelArchimateConnectionAndAddToModel(relationship);
        connection2.connect(dmo1, dmo2);
        
        IDiagramModelBendpoint bp1 = createBendpoint(1, 2, 3, 4);
        connection.getBendpoints().add(bp1);
        
        IDiagramModelBendpoint bp2 = createBendpoint(5, 6, 7, 8);
        connection.getBendpoints().add(bp2);
        
        IDiagramModelBendpoint bp3 = createBendpoint(9, 10, 11, 12);
        connection2.getBendpoints().add(bp3);
        
        IDiagramModelBendpoint bp4 = createBendpoint(13, 14, 15, 16);
        connection2.getBendpoints().add(bp4);
        
        InvertConnectionCommand cmd = new InvertConnectionCommand(relationship);
        cmd.execute();
        
        // Relationship ends are swapped
        assertEquals(source, relationship.getTarget());
        assertEquals(target, relationship.getSource());
        
        // Connection ends are swapped
        assertEquals(dmo2, connection.getSource());
        assertEquals(dmo1, connection.getTarget());
        assertEquals(dmo2, connection2.getSource());
        assertEquals(dmo1, connection2.getTarget());
        
        assertEquals(2, connection.getBendpoints().size());
        assertEquals(2, connection2.getBendpoints().size());
        
        // Bendpoints are reversed and inverted
        // bp1 now has bp2 values and start/ends are swapped
        testBendpoints(bp1, 7, 8, 5, 6);
        testBendpoints(bp2, 3, 4, 1, 2);
        testBendpoints(bp3, 15, 16, 13, 14);
        testBendpoints(bp4, 11, 12, 9, 10);
        
        // Undo
        cmd.undo();
        
        // Relationship ends are correct
        assertEquals(source, relationship.getSource());
        assertEquals(target, relationship.getTarget());
        
        // Connection ends are correct
        assertEquals(dmo1, connection.getSource());
        assertEquals(dmo2, connection.getTarget());
        assertEquals(dmo1, connection2.getSource());
        assertEquals(dmo2, connection2.getTarget());
        
        // Bendpoints are restored
        assertEquals(2, connection.getBendpoints().size());
        assertEquals(2, connection2.getBendpoints().size());
        testBendpoints(bp1, 1, 2, 3, 4);
        testBendpoints(bp2, 5, 6, 7, 8);
        testBendpoints(bp3, 9, 10, 11, 12);
        testBendpoints(bp4, 13, 14, 15, 16);
    }
    
    private void testBendpoints(IDiagramModelBendpoint bp, int startX, int startY, int endX, int endY) {
        assertEquals(startX, bp.getStartX());
        assertEquals(startY, bp.getStartY());
        assertEquals(endX, bp.getEndX());
        assertEquals(endY, bp.getEndY());
    }

    private IDiagramModelBendpoint createBendpoint(int startX, int startY, int endX, int endY) {
        IDiagramModelBendpoint bp = IArchimateFactory.eINSTANCE.createDiagramModelBendpoint();
        bp.setStartX(startX);
        bp.setStartY(startY);
        bp.setEndX(endX);
        bp.setEndY(endY);
        return bp;
    }
}
