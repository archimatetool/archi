/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.List;

import junit.framework.JUnit4TestAdapter;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.ui.IWorkbenchPart;
import org.junit.Test;

import com.archimatetool.model.IAccessRelationship;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.testingtools.ArchimateTestEditor;
import com.archimatetool.testingtools.ArchimateTestModel;

/**
 * SelectAllAction Tests
 * 
 * @author Phillip Beauvoir
 */
public class SelectAllActionTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(SelectAllActionTests.class);
    }
    
    @Test
    public void testSelectAll() {
        ArchimateTestEditor editor = new ArchimateTestEditor();
        
        ArchimateTestModel tm = new ArchimateTestModel();
        tm.createSimpleModel();
        
        IArchimateDiagramModel dm = tm.addNewArchimateDiagramModel();
        
        IDiagramModelArchimateObject dmo1 = ArchimateTestModel.createDiagramModelArchimateObjectAndAddToParent(IArchimateFactory.eINSTANCE.createArtifact(), dm);
        IDiagramModelArchimateObject dmo2 = ArchimateTestModel.createDiagramModelArchimateObjectAndAddToParent(IArchimateFactory.eINSTANCE.createArtifact(), dm);
        ArchimateTestModel.createDiagramModelArchimateObjectAndAddToParent(IArchimateFactory.eINSTANCE.createArtifact(), dmo2);
        IAccessRelationship relationship = IArchimateFactory.eINSTANCE.createAccessRelationship();
        relationship.setSource(dmo1.getArchimateElement());
        relationship.setTarget(dmo2.getArchimateElement());
        IDiagramModelArchimateConnection conn = ArchimateTestModel.createDiagramModelArchimateConnection(relationship);
        conn.connect(dmo1, dmo2);
        
        editor.setDiagramModel(dm);
        
        SelectAllAction action = new SelectAllAction(mock(IWorkbenchPart.class));
        List<GraphicalEditPart> selected = action.getSelectableEditParts(editor.getGraphicalViewer().getContents());
        assertEquals(4, selected.size());
        
        editor.dispose();
    }

    
}