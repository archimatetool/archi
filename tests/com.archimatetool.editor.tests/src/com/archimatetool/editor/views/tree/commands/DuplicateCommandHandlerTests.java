/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.viewers.StructuredSelection;
import org.junit.Test;

import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IAssignmentRelationship;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IFolder;
import com.archimatetool.testingtools.ArchimateTestModel;

import junit.framework.JUnit4TestAdapter;


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
    public void testDuplicateArchimateElements() {
        ArchimateTestModel tm = new ArchimateTestModel();
        IArchimateModel model = tm.createNewModel();
        IFolder folder = model.getFolder(FolderType.BUSINESS);
        
        IArchimateElement element1 = (IArchimateElement)tm.createModelElementAndAddToModel(IArchimatePackage.eINSTANCE.getBusinessActor());
        element1.setName("Actor 1");
        element1.setDocumentation("Doc 1");
        assertEquals(1, folder.getElements().size());
        assertSame(element1, folder.getElements().get(0));
        
        IArchimateElement element2 = (IArchimateElement)tm.createModelElementAndAddToModel(IArchimatePackage.eINSTANCE.getBusinessActor());
        element2.setName("Actor 2");
        element2.setDocumentation("Doc 2");
        assertEquals(2, folder.getElements().size());
        assertSame(element2, folder.getElements().get(1));
        
        IArchimateRelationship relation = (IArchimateRelationship)tm.createModelElementAndAddToModel(IArchimatePackage.eINSTANCE.getAssociationRelationship());
        relation.connect(element1, element2);
        assertTrue(element1.getSourceRelationships().contains(relation));
        assertTrue(element2.getTargetRelationships().contains(relation));

        DuplicateCommandHandler handler = new DuplicateCommandHandler(new Object[] { element1, element2 });
        handler.duplicate();
        
        assertEquals(4, folder.getElements().size());
        assertSame(element1, folder.getElements().get(0));
        assertSame(element2, folder.getElements().get(1));

        IArchimateElement copy1 = (IArchimateElement)folder.getElements().get(2);
        assertNotSame(element1, copy1);
        assertNotEquals(element1.getId(), copy1.getId());
        assertEquals(element1.getName() + " (copy)", copy1.getName());
        assertEquals(element1.getDocumentation(), copy1.getDocumentation());
        
        IArchimateElement copy2 = (IArchimateElement)folder.getElements().get(3);
        assertNotSame(element2, copy2);
        assertNotEquals(element2.getId(), copy2.getId());
        assertEquals(element2.getName() + " (copy)", copy2.getName());
        assertEquals(element2.getDocumentation(), copy2.getDocumentation());

        assertTrue(copy1.getSourceRelationships().isEmpty());
        assertTrue(copy1.getTargetRelationships().isEmpty());
        assertTrue(copy2.getSourceRelationships().isEmpty());
        assertTrue(copy2.getTargetRelationships().isEmpty());
    }    
    
    @Test
    public void testDuplicateDiagramModel() {
        ArchimateTestModel tm = new ArchimateTestModel();
        IArchimateModel model = tm.createNewModel();
        IDiagramModel dm = model.getDefaultDiagramModel();
        
        IArchimateElement actor = IArchimateFactory.eINSTANCE.createBusinessActor();
        IDiagramModelArchimateObject dmo1 = tm.createDiagramModelArchimateObjectAndAddToModel(actor);
        dmo1.setName("dm");
        dm.getChildren().add(dmo1);
        
        IArchimateElement role = IArchimateFactory.eINSTANCE.createBusinessRole();
        IDiagramModelArchimateObject dmo2 = tm.createDiagramModelArchimateObjectAndAddToModel(role);
        dm.getChildren().add(dmo2);
        
        IArchimateRelationship relation = IArchimateFactory.eINSTANCE.createAssignmentRelationship();
        relation.setSource(actor);
        relation.setTarget(role);
        IDiagramModelArchimateConnection dmc1 = tm.createDiagramModelArchimateConnectionAndAddToModel(relation);
        dmc1.connect(dmo1, dmo2);
        
        DuplicateCommandHandler handler = new DuplicateCommandHandler(new Object[] { dm });
        handler.duplicate();
        assertEquals(2, model.getDiagramModels().size());
        
        IDiagramModel dmCopy = model.getDiagramModels().get(1);
        assertNotSame(dm, dmCopy);
        assertEquals(dm.getName() + " (copy)", dmCopy.getName());
        assertNotEquals(dmCopy.getId(), dm.getId());
        
        EList<IDiagramModelObject> children = dmCopy.getChildren();
        assertEquals(2, children.size());
        
        IDiagramModelArchimateObject dmo1Copy = (IDiagramModelArchimateObject)children.get(0);
        IDiagramModelArchimateObject dmo2Copy = (IDiagramModelArchimateObject)children.get(1);
        assertNotSame(dmo1, dmo1Copy);
        assertNotSame(dmo2, dmo2Copy);
        assertNotEquals(dmo1.getId(), dmo1Copy.getId());
        assertNotEquals(dmo2.getId(), dmo2Copy.getId());
        assertSame(actor, dmo1Copy.getArchimateConcept());
        assertSame(role, dmo2Copy.getArchimateConcept());
        assertSame(actor.getId(), dmo1Copy.getArchimateConcept().getId());
        assertSame(role.getId(), dmo2Copy.getArchimateConcept().getId());
        
        EList<IDiagramModelConnection> connections = dmo1Copy.getSourceConnections();
        assertEquals(1, connections.size());
        
        IDiagramModelArchimateConnection dmc1Copy = (IDiagramModelArchimateConnection)connections.get(0);
        assertNotSame(dmc1, dmc1Copy);
        assertNotEquals(dmc1.getId(), dmc1Copy.getId());
        assertSame(relation, dmc1Copy.getArchimateConcept());
        assertSame(relation.getId(), dmc1Copy.getArchimateConcept().getId());
    }
    
    @Test
    public void testDuplicateDiagramModel_AddsConnectionsToConnections() {
        ArchimateTestModel tm = new ArchimateTestModel();
        IArchimateModel model = tm.createNewModel();
        IDiagramModel dm = model.getDefaultDiagramModel();
        
        IArchimateElement element1 = IArchimateFactory.eINSTANCE.createBusinessActor();
        IDiagramModelArchimateObject dmo1 = tm.createDiagramModelArchimateObjectAndAddToModel(element1);
        dm.getChildren().add(dmo1);
        
        IArchimateElement element2 = IArchimateFactory.eINSTANCE.createBusinessRole();
        IDiagramModelArchimateObject dmo2 = tm.createDiagramModelArchimateObjectAndAddToModel(element2);
        dm.getChildren().add(dmo2);
        
        IArchimateElement element3 = IArchimateFactory.eINSTANCE.createBusinessActor();
        IDiagramModelArchimateObject dmo3 = tm.createDiagramModelArchimateObjectAndAddToModel(element3);
        dm.getChildren().add(dmo3);

        IArchimateRelationship relation1 = IArchimateFactory.eINSTANCE.createAssignmentRelationship();
        relation1.setSource(element1);
        relation1.setTarget(element2);
        IDiagramModelArchimateConnection dmc1 = tm.createDiagramModelArchimateConnectionAndAddToModel(relation1);
        dmc1.connect(dmo1, dmo2);
        
        IArchimateRelationship relation2 = IArchimateFactory.eINSTANCE.createAssociationRelationship();
        relation2.setSource(element3);
        relation2.setTarget(relation1);
        IDiagramModelArchimateConnection dmc2 = tm.createDiagramModelArchimateConnectionAndAddToModel(relation2);
        dmc2.connect(dmo3, dmc1);
        
        DuplicateCommandHandler handler = new DuplicateCommandHandler(new Object[] { dm });
        handler.duplicate();
        
        IDiagramModel dmCopy = model.getDiagramModels().get(1);
        
        EList<IDiagramModelObject> children = dmCopy.getChildren();
        assertEquals(3, children.size());
        
        IDiagramModelArchimateObject dmo1Copy = (IDiagramModelArchimateObject)children.get(0);
        IDiagramModelArchimateObject dmo2Copy = (IDiagramModelArchimateObject)children.get(1);
        IDiagramModelArchimateObject dmo3Copy = (IDiagramModelArchimateObject)children.get(2);
        
        assertSame(element1, dmo1Copy.getArchimateConcept());
        assertSame(element2, dmo2Copy.getArchimateConcept());
        assertSame(element3, dmo3Copy.getArchimateConcept());
        
        IDiagramModelArchimateConnection dmc1Copy = (IDiagramModelArchimateConnection)dmo1Copy.getSourceConnections().get(0);
        assertSame(relation1, dmc1Copy.getArchimateConcept());
        assertSame(dmo2Copy, dmc1Copy.getTarget());
        
        // Connection to Connection
        assertSame(dmc1Copy, dmo3Copy.getSourceConnections().get(0).getTarget());
    }
    
    @Test
    public void testCorrectDiagramReferencesAfterDuplicateDiagram() {
        ArchimateTestModel tm = new ArchimateTestModel();
        IArchimateModel model = tm.createNewModel();
        IDiagramModel dm = model.getDefaultDiagramModel();
        
        IArchimateElement actor = IArchimateFactory.eINSTANCE.createBusinessActor();
        IDiagramModelArchimateObject dmo1 = tm.createDiagramModelArchimateObjectAndAddToModel(actor);
        dm.getChildren().add(dmo1);
        
        IArchimateElement role = IArchimateFactory.eINSTANCE.createBusinessRole();
        IDiagramModelArchimateObject dmo2 = tm.createDiagramModelArchimateObjectAndAddToModel(role);
        dm.getChildren().add(dmo2);
        
        IAssignmentRelationship relation = IArchimateFactory.eINSTANCE.createAssignmentRelationship();
        relation.setSource(actor);
        relation.setTarget(role);
        IDiagramModelArchimateConnection dmc1 = tm.createDiagramModelArchimateConnectionAndAddToModel(relation);
        dmc1.connect(dmo1, dmo2);
        
        assertEquals(1, actor.getReferencingDiagramObjects().size());
        assertEquals(1, role.getReferencingDiagramObjects().size());
        assertEquals(1, relation.getReferencingDiagramConnections().size());

        // Duplicate
        DuplicateCommandHandler handler = new DuplicateCommandHandler(new Object[] { dm });
        handler.duplicate();
        
        assertEquals(2, actor.getReferencingDiagramObjects().size());
        assertEquals(2, role.getReferencingDiagramObjects().size());
        assertEquals(2, relation.getReferencingDiagramConnections().size());
    }

}
