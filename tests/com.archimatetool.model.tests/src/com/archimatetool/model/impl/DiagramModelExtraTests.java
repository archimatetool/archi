/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import static org.junit.Assert.assertEquals;
import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelGroup;
import com.archimatetool.model.IFolder;


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
        
        dmo1.setArchimateElement(IArchimateFactory.eINSTANCE.createBusinessActor());
        dmo2.setArchimateElement(IArchimateFactory.eINSTANCE.createBusinessActor());
        dmo3.setArchimateElement(IArchimateFactory.eINSTANCE.createBusinessActor());
        // dmo3a shares an archimate element with dmo3
        dmo3a.setArchimateElement(dmo3.getArchimateElement());
        
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

        // Starts at sizes
        assertEquals(1, dmo1.getArchimateElement().getReferencingDiagramObjects().size());
        assertEquals(1, dmo2.getArchimateElement().getReferencingDiagramObjects().size());
        assertEquals(2, dmo3.getArchimateElement().getReferencingDiagramObjects().size());
        assertEquals(2, dmo3a.getArchimateElement().getReferencingDiagramObjects().size());
        
        // Add to dm
        dm.getChildren().add(dmo1);
        assertEquals(1, dmo1.getArchimateElement().getReferencingDiagramObjects().size());
        
        // Add to parent 1
        parent1.getChildren().add(dmo2);
        assertEquals(1, dmo2.getArchimateElement().getReferencingDiagramObjects().size());
        
        // Add to parent 2
        parent2.getChildren().add(dmo3);
        assertEquals(2, dmo3.getArchimateElement().getReferencingDiagramObjects().size());
        assertEquals(2, dmo3a.getArchimateElement().getReferencingDiagramObjects().size());
        
        // Add dmo3a - dmo3.getArchimateElement() should be same
        dm.getChildren().add(dmo3a);
        assertEquals(2, dmo3.getArchimateElement().getReferencingDiagramObjects().size());
        assertEquals(2, dmo3a.getArchimateElement().getReferencingDiagramObjects().size());
        
        // Remove diagram model, should be the same
        ((IFolder)dm.eContainer()).getElements().remove(dm);
        
        assertEquals(1, dmo1.getArchimateElement().getReferencingDiagramObjects().size());
        assertEquals(1, dmo2.getArchimateElement().getReferencingDiagramObjects().size());
        assertEquals(2, dmo3.getArchimateElement().getReferencingDiagramObjects().size());
        assertEquals(2, dmo3a.getArchimateElement().getReferencingDiagramObjects().size());
        
        // Add diagram model back again, should be the same
        model.getDefaultFolderForObject(dm).getElements().add(dm);
        
        assertEquals(1, dmo1.getArchimateElement().getReferencingDiagramObjects().size());
        assertEquals(1, dmo2.getArchimateElement().getReferencingDiagramObjects().size());
        assertEquals(2, dmo3.getArchimateElement().getReferencingDiagramObjects().size());
        assertEquals(2, dmo3a.getArchimateElement().getReferencingDiagramObjects().size());
        
        // Simple remove from direct parent
        dm.getChildren().remove(dmo1);
        parent1.getChildren().remove(dmo2);
        parent2.getChildren().remove(dmo3);
        
        assertEquals(0, dmo1.getArchimateElement().getReferencingDiagramObjects().size());
        assertEquals(0, dmo2.getArchimateElement().getReferencingDiagramObjects().size());
        assertEquals(1, dmo3.getArchimateElement().getReferencingDiagramObjects().size());
        assertEquals(1, dmo3a.getArchimateElement().getReferencingDiagramObjects().size());
        
        // Remove Again
        dm.getChildren().remove(dmo3a);

        assertEquals(0, dmo3.getArchimateElement().getReferencingDiagramObjects().size());
        assertEquals(0, dmo3a.getArchimateElement().getReferencingDiagramObjects().size());
    }
    
    /**
     * See {@link DiagramModelArchimateConnection#eInverseAdd(org.eclipse.emf.ecore.InternalEObject, int, Class, org.eclipse.emf.common.notify.NotificationChain)}
     * See {@link DiagramModelArchimateConnection#eInverseRemove(org.eclipse.emf.ecore.InternalEObject, int, Class, org.eclipse.emf.common.notify.NotificationChain)}
     */
    @Test
    public void testDiagramModelArchimateConnection_eInverseAdd_Relationship_getReferencingDiagramObjects() {
        IArchimateElement element1 = IArchimateFactory.eINSTANCE.createBusinessActor();
        IArchimateElement element2 = IArchimateFactory.eINSTANCE.createBusinessActor();
        IArchimateElement element3 = IArchimateFactory.eINSTANCE.createBusinessActor();
        IArchimateElement element4 = IArchimateFactory.eINSTANCE.createBusinessActor();
        
        IArchimateRelationship relation1 = IArchimateFactory.eINSTANCE.createAssociationRelationship();
        IArchimateRelationship relation2 = IArchimateFactory.eINSTANCE.createAssociationRelationship();
        IArchimateRelationship relation3 = IArchimateFactory.eINSTANCE.createAssociationRelationship();
        
        relation1.setSource(element1);
        relation1.setTarget(element2);
        
        relation2.setSource(element2);
        relation2.setTarget(element3);
        
        relation3.setSource(element3);
        relation3.setTarget(element4);

        IDiagramModelArchimateObject dmo1 = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        IDiagramModelArchimateObject dmo2 = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        IDiagramModelArchimateObject dmo3 = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        IDiagramModelArchimateObject dmo4 = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        
        dmo1.setArchimateElement(element1);
        dmo2.setArchimateElement(element2);
        dmo3.setArchimateElement(element3);
        dmo4.setArchimateElement(element4);
        
        IDiagramModelArchimateConnection conn1 = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
        IDiagramModelArchimateConnection conn2 = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
        IDiagramModelArchimateConnection conn3 = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
        // conn3a shares a relationship with conn3
        IDiagramModelArchimateConnection conn3a = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
        
        conn1.setArchimateRelationship(relation1);
        conn2.setArchimateRelationship(relation2);
        conn3.setArchimateRelationship(relation3);
        conn3a.setArchimateRelationship(relation3); // same
        
        
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

        // Starts at sizes
        assertEquals(1, conn1.getArchimateRelationship().getReferencingDiagramConnections().size());
        assertEquals(1, conn2.getArchimateRelationship().getReferencingDiagramConnections().size());
        assertEquals(2, conn3.getArchimateRelationship().getReferencingDiagramConnections().size());
        assertEquals(2, conn3a.getArchimateRelationship().getReferencingDiagramConnections().size());
        
        // Connect
        conn1.connect(dmo1, dmo2);
        assertEquals(1, conn1.getArchimateRelationship().getReferencingDiagramConnections().size());

        conn2.connect(dmo2, dmo3);
        assertEquals(1, conn2.getArchimateRelationship().getReferencingDiagramConnections().size());
        
        conn3.connect(dmo3, dmo4);
        assertEquals(1, conn2.getArchimateRelationship().getReferencingDiagramConnections().size());

        conn3a.connect(dmo3, dmo4);
        assertEquals(2, conn3.getArchimateRelationship().getReferencingDiagramConnections().size());
        assertEquals(2, conn3a.getArchimateRelationship().getReferencingDiagramConnections().size());
        
        // Remove diagram model, should be the same
        ((IFolder)dm.eContainer()).getElements().remove(dm);
        
        assertEquals(1, conn1.getArchimateRelationship().getReferencingDiagramConnections().size());
        assertEquals(1, conn2.getArchimateRelationship().getReferencingDiagramConnections().size());
        assertEquals(2, conn3.getArchimateRelationship().getReferencingDiagramConnections().size());
        assertEquals(2, conn3a.getArchimateRelationship().getReferencingDiagramConnections().size());
       
        // Add diagram model back again
        model.getDefaultFolderForObject(dm).getElements().add(dm);
        
        assertEquals(1, conn1.getArchimateRelationship().getReferencingDiagramConnections().size());
        assertEquals(1, conn2.getArchimateRelationship().getReferencingDiagramConnections().size());
        assertEquals(2, conn3.getArchimateRelationship().getReferencingDiagramConnections().size());
        assertEquals(2, conn3a.getArchimateRelationship().getReferencingDiagramConnections().size());
        
        // Disconnect
        conn1.disconnect();
        conn2.disconnect();
        conn3.disconnect();
        conn3a.disconnect();

        assertEquals(0, conn1.getArchimateRelationship().getReferencingDiagramConnections().size());
        assertEquals(0, conn2.getArchimateRelationship().getReferencingDiagramConnections().size());
        assertEquals(0, conn3.getArchimateRelationship().getReferencingDiagramConnections().size());
        assertEquals(0, conn3a.getArchimateRelationship().getReferencingDiagramConnections().size());
    }
    
}
