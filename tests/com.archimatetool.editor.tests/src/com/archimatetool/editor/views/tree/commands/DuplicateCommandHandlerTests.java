/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import junit.framework.JUnit4TestAdapter;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.viewers.StructuredSelection;
import org.junit.Test;

import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IAssignmentRelationship;
import com.archimatetool.model.IBusinessActor;
import com.archimatetool.model.IBusinessRole;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IFolder;
import com.archimatetool.testingtools.ArchimateTestModel;


@SuppressWarnings("nls")
public class DuplicateCommandHandlerTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(DuplicateCommandHandlerTests.class);
    }
    
    @Test
    public void testCanDuplicateSelection() {
        // Empty
        assertFalse(DuplicateCommandHandler.canDuplicate(new StructuredSelection()));
        
        List<Object> list = new ArrayList<Object>();
        
        list.add(IArchimateFactory.eINSTANCE.createAssignmentRelationship());
        assertFalse(DuplicateCommandHandler.canDuplicate(new StructuredSelection(list)));

        list.add(IArchimateFactory.eINSTANCE.createFolder());
        assertFalse(DuplicateCommandHandler.canDuplicate(new StructuredSelection(list)));

        list.add(IArchimateFactory.eINSTANCE.createBusinessActor());
        assertTrue(DuplicateCommandHandler.canDuplicate(new StructuredSelection(list)));
        
        list.add(IArchimateFactory.eINSTANCE.createDevice());
        assertTrue(DuplicateCommandHandler.canDuplicate(new StructuredSelection(list)));
        
        list.add(IArchimateFactory.eINSTANCE.createArchimateDiagramModel());
        assertTrue(DuplicateCommandHandler.canDuplicate(new StructuredSelection(list)));
        
        list.add(IArchimateFactory.eINSTANCE.createSketchModel());
        assertTrue(DuplicateCommandHandler.canDuplicate(new StructuredSelection(list)));
    }
    
    @Test
    public void testCanDuplicateObject() {
        // Null
        assertFalse(DuplicateCommandHandler.canDuplicate((Object)null));
        
        // Archimate Element
        assertTrue(DuplicateCommandHandler.canDuplicate(IArchimateFactory.eINSTANCE.createBusinessActor()));
        
        // Diagrams
        assertTrue(DuplicateCommandHandler.canDuplicate(IArchimateFactory.eINSTANCE.createArchimateDiagramModel()));
        assertTrue(DuplicateCommandHandler.canDuplicate(IArchimateFactory.eINSTANCE.createSketchModel()));
        
        // Relation
        assertFalse(DuplicateCommandHandler.canDuplicate(IArchimateFactory.eINSTANCE.createAssignmentRelationship()));
        
        // Folder
        assertFalse(DuplicateCommandHandler.canDuplicate(IArchimateFactory.eINSTANCE.createFolder()));
    }
    
    @Test
    public void testDuplicateArchimateElement() {
        ArchimateTestModel tm = new ArchimateTestModel();
        IArchimateModel model = tm.createNewModel();
        IFolder folder = model.getFolder(FolderType.BUSINESS);
        
        IBusinessActor actor = (IBusinessActor)tm.createModelElementAndAddToModel(IArchimatePackage.eINSTANCE.getBusinessActor());
        actor.setName("Actor 1");
        actor.setDocumentation("Doc 1");
        assertEquals(1, folder.getElements().size());
        assertEquals(actor, folder.getElements().get(0));
        
        DuplicateCommandHandler handler = new DuplicateCommandHandler(new Object[] { actor });
        handler.duplicate();
        
        assertEquals(2, folder.getElements().size());
        assertEquals(actor, folder.getElements().get(0));

        IBusinessActor actorCopy = (IBusinessActor)folder.getElements().get(1);
        assertNotEquals(actor, actorCopy);
        assertNotEquals(actor.getId(), actorCopy.getId());
        assertEquals(actor.getName() + " (copy)", actorCopy.getName());
        assertEquals(actor.getDocumentation(), actorCopy.getDocumentation());
    }    
    
    @Test
    public void testDuplicateDiagramModel() {
        ArchimateTestModel tm = new ArchimateTestModel();
        IArchimateModel model = tm.createNewModel();
        IDiagramModel dm = model.getDefaultDiagramModel();
        
        IBusinessActor actor = IArchimateFactory.eINSTANCE.createBusinessActor();
        IDiagramModelArchimateObject dmo1 = tm.createDiagramModelArchimateObjectAndAddToModel(actor);
        dmo1.setName("dm");
        dm.getChildren().add(dmo1);
        
        IBusinessRole role = IArchimateFactory.eINSTANCE.createBusinessRole();
        IDiagramModelArchimateObject dmo2 = tm.createDiagramModelArchimateObjectAndAddToModel(role);
        dm.getChildren().add(dmo2);
        
        IAssignmentRelationship relation = IArchimateFactory.eINSTANCE.createAssignmentRelationship();
        relation.setSource(actor);
        relation.setTarget(role);
        IDiagramModelArchimateConnection dmc1 = tm.createDiagramModelArchimateConnectionAndAddToModel(relation);
        dmc1.connect(dmo1, dmo2);
        
        DuplicateCommandHandler handler = new DuplicateCommandHandler(new Object[] { dm });
        handler.duplicate();
        assertEquals(2, model.getDiagramModels().size());
        
        IDiagramModel dmCopy = model.getDiagramModels().get(1);
        assertNotEquals(dm, dmCopy);
        assertEquals(dm.getName() + " (copy)", dmCopy.getName());
        
        EList<IDiagramModelObject> children = dmCopy.getChildren();
        assertEquals(2, children.size());
        
        IDiagramModelArchimateObject dmo1Copy = (IDiagramModelArchimateObject)children.get(0);
        IDiagramModelArchimateObject dmo2Copy = (IDiagramModelArchimateObject)children.get(1);
        assertNotEquals(dmo1, dmo1Copy);
        assertNotEquals(dmo2, dmo2Copy);
        assertEquals(actor, dmo1Copy.getArchimateElement());
        assertEquals(role, dmo2Copy.getArchimateElement());
        
        EList<IDiagramModelConnection> connections = dmo1Copy.getSourceConnections();
        assertEquals(1, connections.size());
        
        IDiagramModelArchimateConnection dmc1Copy = (IDiagramModelArchimateConnection)connections.get(0);
        assertNotEquals(dmc1, dmc1Copy);
        assertEquals(relation, dmc1Copy.getRelationship());
    }
}
