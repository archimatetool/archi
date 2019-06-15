/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelGroup;
import com.archimatetool.model.IFolder;

import junit.framework.JUnit4TestAdapter;


/**
 * Extra tests on objects that don't fit anywhere else
 * 
 * @author Phillip Beauvoir
 */
public class DiagramModelExtraTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(DiagramModelExtraTests.class);
    }
    
    /**
     * See {@link DiagramModelArchimateObject#eInverseAdd(org.eclipse.emf.ecore.InternalEObject, int, Class, org.eclipse.emf.common.notify.NotificationChain)}
     * See {@link DiagramModelArchimateObject#eInverseRemove(org.eclipse.emf.ecore.InternalEObject, int, Class, org.eclipse.emf.common.notify.NotificationChain)}
     */
    @Test
    public void testDiagramModelArchimateObject_eInverseAdd_ArchimateElement_getReferencingDiagramObjects() {
        IDiagramModelArchimateObject dmo1 = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        IDiagramModelArchimateObject dmo2 = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        IDiagramModelArchimateObject dmo3 = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        IDiagramModelArchimateObject dmo3a = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        
        ArchimateElement e1 = (ArchimateElement)IArchimateFactory.eINSTANCE.createBusinessActor();
        ArchimateElement e2 = (ArchimateElement)IArchimateFactory.eINSTANCE.createBusinessActor();
        ArchimateElement e3 = (ArchimateElement)IArchimateFactory.eINSTANCE.createBusinessActor();
        
        dmo1.setArchimateElement(e1);
        dmo2.setArchimateElement(e2);
        dmo3.setArchimateElement(e3);
        // dmo3a shares an archimate element with dmo3
        dmo3a.setArchimateElement(e3);
        
        // Set up model and diagram model
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        model.setDefaults();
        IDiagramModel dm = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        model.getDefaultFolderForObject(dm).getElements().add(dm);
        
        // Add some parents
        IDiagramModelGroup parent1 = IArchimateFactory.eINSTANCE.createDiagramModelGroup();
        dm.getChildren().add(parent1);
        
        IDiagramModelArchimateObject parent2 = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        parent1.getChildren().add(parent2);

        // public method returns zero objects
        assertEquals(0, e1.getReferencingDiagramObjects().size());
        assertEquals(0, e2.getReferencingDiagramObjects().size());
        assertEquals(0, e3.getReferencingDiagramObjects().size());
        assertEquals(0, dmo3a.getArchimateElement().getReferencingDiagramObjects().size());
        
        // But internally is more
        assertEquals(1, e1.diagramObjects.size());
        assertEquals(1, e2.diagramObjects.size());
        assertEquals(2, e3.diagramObjects.size()); // beacause e3 is in dmo3 and dmo3a
        assertEquals(2, ((ArchimateElement)dmo3a.getArchimateElement()).diagramObjects.size());
        
        // Add dmo1 to dm, should be same
        dm.getChildren().add(dmo1);
        assertEquals(1, e1.getReferencingDiagramObjects().size());
        assertEquals(1, e1.diagramObjects.size());
        
        // Add dmo2 to parent 1, should be same
        parent1.getChildren().add(dmo2);
        assertEquals(1, dmo2.getArchimateElement().getReferencingDiagramObjects().size());
        assertEquals(1, e2.diagramObjects.size());
        
        // Add dmo3 to parent 2, dmo3 and dmo3a should be same
        parent2.getChildren().add(dmo3);
        assertEquals(1, e3.getReferencingDiagramObjects().size());
        assertEquals(1, dmo3a.getArchimateElement().getReferencingDiagramObjects().size());
        assertEquals(2, e3.diagramObjects.size());
        assertEquals(2, ((ArchimateElement)dmo3a.getArchimateElement()).diagramObjects.size());
        
        // Add dmo3a to dm, dmo3.getArchimateElement() should be more in public method
        dm.getChildren().add(dmo3a);
        assertEquals(2, e3.getReferencingDiagramObjects().size());
        assertEquals(2, dmo3a.getArchimateElement().getReferencingDiagramObjects().size());
        // But internally the same
        assertEquals(2, e3.diagramObjects.size());
        assertEquals(2, ((ArchimateElement)dmo3a.getArchimateElement()).diagramObjects.size());
        
        // Remove diagram model
        ((IFolder)dm.eContainer()).getElements().remove(dm);
        
        // Public methods should return zero
        assertEquals(0, e1.getReferencingDiagramObjects().size());
        assertEquals(0, e2.getReferencingDiagramObjects().size());
        assertEquals(0, e3.getReferencingDiagramObjects().size());
        assertEquals(0, dmo3a.getArchimateElement().getReferencingDiagramObjects().size());
        
        // But internally, referenced
        assertEquals(1, e1.diagramObjects.size());
        assertEquals(1, e2.diagramObjects.size());
        assertEquals(2, e3.diagramObjects.size()); // beacause e3 is in dmo3 and dmo3a
        assertEquals(2, ((ArchimateElement)dmo3a.getArchimateElement()).diagramObjects.size());
        
        // Add diagram model back again, should be the same as before
        model.getDefaultFolderForObject(dm).getElements().add(dm);
        
        assertEquals(1, e1.getReferencingDiagramObjects().size());
        assertEquals(1, e2.getReferencingDiagramObjects().size());
        assertEquals(2, e3.getReferencingDiagramObjects().size());
        assertEquals(2, dmo3a.getArchimateElement().getReferencingDiagramObjects().size());
        
        // And internally the same
        assertEquals(1, e1.diagramObjects.size());
        assertEquals(1, e2.diagramObjects.size());
        assertEquals(2, e3.diagramObjects.size()); // beacause e3 is in dmo3 and dmo3a
        assertEquals(2, ((ArchimateElement)dmo3a.getArchimateElement()).diagramObjects.size());
        
        // Simple remove from direct parent
        dm.getChildren().remove(dmo1);
        parent1.getChildren().remove(dmo2);
        parent2.getChildren().remove(dmo3);
        
        // Public
        assertEquals(0, e1.getReferencingDiagramObjects().size());
        assertEquals(0, e2.getReferencingDiagramObjects().size());
        assertEquals(1, e3.getReferencingDiagramObjects().size());
        assertEquals(1, dmo3a.getArchimateElement().getReferencingDiagramObjects().size());
        
        // And internally the same
        assertEquals(0, e1.diagramObjects.size());
        assertEquals(0, e2.diagramObjects.size());
        assertEquals(1, e3.diagramObjects.size()); // beacause e3 is in dmo3 and dmo3a
        assertEquals(1, ((ArchimateElement)dmo3a.getArchimateElement()).diagramObjects.size());

        // Remove dmo3a
        dm.getChildren().remove(dmo3a);

        // Public
        assertEquals(0, e3.getReferencingDiagramObjects().size());
        assertEquals(0, dmo3a.getArchimateElement().getReferencingDiagramObjects().size());
        
        // Internal
        assertEquals(0, e3.diagramObjects.size()); // beacause e3 is in dmo3 and dmo3a
        assertEquals(0, ((ArchimateElement)dmo3a.getArchimateElement()).diagramObjects.size());
    }
    
    /**
     * See {@link DiagramModelArchimateConnection#eInverseAdd(org.eclipse.emf.ecore.InternalEObject, int, Class, org.eclipse.emf.common.notify.NotificationChain)}
     * See {@link DiagramModelArchimateConnection#eInverseRemove(org.eclipse.emf.ecore.InternalEObject, int, Class, org.eclipse.emf.common.notify.NotificationChain)}
     */
    @Test
    public void testDiagramModelArchimateConnection_eInverseAdd_Relationship_getReferencingDiagramObjects() {
        IArchimateElement e1 = IArchimateFactory.eINSTANCE.createBusinessActor();
        IArchimateElement e2 = IArchimateFactory.eINSTANCE.createBusinessActor();
        IArchimateElement e3 = IArchimateFactory.eINSTANCE.createBusinessActor();
        IArchimateElement e4 = IArchimateFactory.eINSTANCE.createBusinessActor();
        
        IDiagramModelArchimateObject dmo1 = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        IDiagramModelArchimateObject dmo2 = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        IDiagramModelArchimateObject dmo3 = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        IDiagramModelArchimateObject dmo4 = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        
        dmo1.setArchimateElement(e1);
        dmo2.setArchimateElement(e2);
        dmo3.setArchimateElement(e3);
        dmo4.setArchimateElement(e4);
        
        ArchimateRelationship r1 = (ArchimateRelationship)IArchimateFactory.eINSTANCE.createAssociationRelationship();
        ArchimateRelationship r2 = (ArchimateRelationship)IArchimateFactory.eINSTANCE.createAssociationRelationship();
        ArchimateRelationship r3 = (ArchimateRelationship)IArchimateFactory.eINSTANCE.createAssociationRelationship();
        
        r1.setSource(e1);
        r1.setTarget(e2);
        
        r2.setSource(e2);
        r2.setTarget(e3);
        
        r3.setSource(e3);
        r3.setTarget(e4);

        IDiagramModelArchimateConnection conn1 = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
        IDiagramModelArchimateConnection conn2 = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
        IDiagramModelArchimateConnection conn3 = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
        // conn3a shares a relationship with conn3
        IDiagramModelArchimateConnection conn3a = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
        
        conn1.setArchimateRelationship(r1);
        conn2.setArchimateRelationship(r2);
        conn3.setArchimateRelationship(r3);
        conn3a.setArchimateRelationship(r3); // same
        
        // Set up model and diagram model
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        model.setDefaults();
        IDiagramModel dm = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        model.getDefaultFolderForObject(dm).getElements().add(dm);
        
        // Add some parents
        IDiagramModelGroup parent1 = IArchimateFactory.eINSTANCE.createDiagramModelGroup();
        dm.getChildren().add(parent1);
        
        IDiagramModelArchimateObject parent2 = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        parent1.getChildren().add(parent2);
        
        // Add the dmos
        dm.getChildren().add(dmo1);
        parent1.getChildren().add(dmo2);
        parent2.getChildren().add(dmo3);
        dm.getChildren().add(dmo4);

        // Public methods should return zero
        assertEquals(0, r1.getReferencingDiagramConnections().size());
        assertEquals(0, r2.getReferencingDiagramConnections().size());
        assertEquals(0, r3.getReferencingDiagramConnections().size());
        assertEquals(0, conn3a.getArchimateRelationship().getReferencingDiagramConnections().size());
        
        // But internally, referenced
        assertEquals(1, r1.diagramConnections.size());
        assertEquals(1, r2.diagramConnections.size());
        assertEquals(2, r3.diagramConnections.size()); // beacause r3 is in conn3 and conn3a
        assertEquals(2, ((ArchimateRelationship)conn3a.getArchimateRelationship()).diagramConnections.size());

        
        // Connect, should be the same
        conn1.connect(dmo1, dmo2);
        assertEquals(1, r1.getReferencingDiagramConnections().size());
        assertEquals(1, r1.diagramConnections.size());

        conn2.connect(dmo2, dmo3);
        assertEquals(1, r2.getReferencingDiagramConnections().size());
        assertEquals(1, r2.diagramConnections.size());
        
        conn3.connect(dmo3, dmo4);
        assertEquals(1, r3.getReferencingDiagramConnections().size());
        assertEquals(2, r3.diagramConnections.size()); // beacause r3 is in conn3 and conn3a
        
        conn3a.connect(dmo3, dmo4);
        assertEquals(2, r3.getReferencingDiagramConnections().size());
        assertEquals(2, conn3a.getArchimateRelationship().getReferencingDiagramConnections().size());
        assertEquals(2, ((ArchimateRelationship)conn3a.getArchimateRelationship()).diagramConnections.size());
        
        // Remove diagram model
        ((IFolder)dm.eContainer()).getElements().remove(dm);
        
        // Public methods should return zero
        assertEquals(0, r1.getReferencingDiagramConnections().size());
        assertEquals(0, r2.getReferencingDiagramConnections().size());
        assertEquals(0, r3.getReferencingDiagramConnections().size());
        assertEquals(0, conn3a.getArchimateRelationship().getReferencingDiagramConnections().size());
        
        // But internally still referenced
        assertEquals(1, r1.diagramConnections.size());
        assertEquals(1, r2.diagramConnections.size());
        assertEquals(2, r3.diagramConnections.size()); // beacause r3 is in conn3 and conn3a
        assertEquals(2, ((ArchimateRelationship)conn3a.getArchimateRelationship()).diagramConnections.size());
       
        // Add diagram model back again
        model.getDefaultFolderForObject(dm).getElements().add(dm);
        
        // Public methods should be as before
        assertEquals(1, r1.getReferencingDiagramConnections().size());
        assertEquals(1, r2.getReferencingDiagramConnections().size());
        assertEquals(2, r3.getReferencingDiagramConnections().size());
        assertEquals(2, conn3a.getArchimateRelationship().getReferencingDiagramConnections().size());
        
        // And internally still referenced
        assertEquals(1, r1.diagramConnections.size());
        assertEquals(1, r2.diagramConnections.size());
        assertEquals(2, r3.diagramConnections.size()); // beacause r3 is in conn3 and conn3a
        assertEquals(2, ((ArchimateRelationship)conn3a.getArchimateRelationship()).diagramConnections.size());
        
        // Disconnect
        conn1.disconnect();
        conn2.disconnect();
        conn3.disconnect();
        conn3a.disconnect();

        // Public methods should return zero again
        assertEquals(0, conn1.getArchimateRelationship().getReferencingDiagramConnections().size());
        assertEquals(0, conn2.getArchimateRelationship().getReferencingDiagramConnections().size());
        assertEquals(0, conn3.getArchimateRelationship().getReferencingDiagramConnections().size());
        assertEquals(0, conn3a.getArchimateRelationship().getReferencingDiagramConnections().size());
        
        // And internally zero
        assertEquals(0, r1.diagramConnections.size());
        assertEquals(0, r2.diagramConnections.size());
        assertEquals(0, r3.diagramConnections.size()); // beacause r3 is in conn3 and conn3a
        assertEquals(0, ((ArchimateRelationship)conn3a.getArchimateRelationship()).diagramConnections.size());
    }
    
}
