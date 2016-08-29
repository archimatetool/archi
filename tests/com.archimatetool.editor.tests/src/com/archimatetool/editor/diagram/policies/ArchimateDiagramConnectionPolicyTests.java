/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.policies;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.junit.Test;

import com.archimatetool.editor.diagram.ArchimateDiagramModelFactory;
import com.archimatetool.editor.diagram.commands.CreateDiagramArchimateConnectionWithDialogCommand;
import com.archimatetool.editor.diagram.commands.CreateDiagramConnectionCommand;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelGroup;
import com.archimatetool.model.IDiagramModelNote;
import com.archimatetool.model.IDiagramModelReference;
import com.archimatetool.model.util.ArchimateModelUtils;
import com.archimatetool.tests.TestUtils;

import junit.framework.JUnit4TestAdapter;

@SuppressWarnings("nls")
public class ArchimateDiagramConnectionPolicyTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ArchimateDiagramConnectionPolicyTests.class);
    }

    private ArchimateDiagramConnectionPolicy sourcePolicy;
    private ArchimateDiagramConnectionPolicy targetPolicy;
    private IDiagramModelArchimateObject sourceDiagramObject;
    private IDiagramModelArchimateObject targetDiagramObject;
    
    // Setup source policy
    private void setupSourcePolicy(IArchimateElement sourceElement) {
        sourcePolicy = new ArchimateDiagramConnectionPolicy();
        
        EditPart sourceEditPart = mock(EditPart.class);
        sourcePolicy.setHost(sourceEditPart);
        
        sourceDiagramObject = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        sourceDiagramObject.setArchimateElement(sourceElement);
        when(sourceEditPart.getModel()).thenReturn(sourceDiagramObject);
    }
        
    // Setup target policy
    private void setupTargetPolicy(IArchimateElement targetElement) {
        targetPolicy = new ArchimateDiagramConnectionPolicy();
        
        EditPart targetEditPart = mock(EditPart.class);
        targetPolicy.setHost(targetEditPart);
        
        targetDiagramObject = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        targetDiagramObject.setArchimateElement(targetElement);
        when(targetEditPart.getModel()).thenReturn(targetDiagramObject);
    }
    
    @Test
    public void testGetConnectionCreateCommand_CreatesLineConnectionCommand() throws Exception {
        setupSourcePolicy(IArchimateFactory.eINSTANCE.createBusinessActor());
        
        CreateConnectionRequest request = new CreateConnectionRequest();
        request.setFactory(new ArchimateDiagramModelFactory(IArchimatePackage.eINSTANCE.getDiagramModelConnection()));
        
        Command cmd = sourcePolicy.getConnectionCreateCommand(request);
        assertNotNull(cmd);
        assertTrue(cmd.getClass() == CreateDiagramConnectionCommand.class);
        assertEquals(cmd, request.getStartCommand());
        assertEquals(sourceDiagramObject, TestUtils.getPrivateField(cmd, "fSource"));
    }
    
    @Test
    public void testGetConnectionCreateCommand_CreatesArchimateConnectionCommand() throws Exception {
        setupSourcePolicy(IArchimateFactory.eINSTANCE.createBusinessActor());
        
        CreateConnectionRequest request = new CreateConnectionRequest();
        request.setFactory(new ArchimateDiagramModelFactory(IArchimatePackage.eINSTANCE.getAssignmentRelationship()));
        
        Command cmd = sourcePolicy.getConnectionCreateCommand(request);
        assertNotNull(cmd);
        assertTrue(cmd.getClass() == CreateDiagramArchimateConnectionWithDialogCommand.class);
        assertEquals(cmd, request.getStartCommand());
        assertEquals(sourceDiagramObject, TestUtils.getPrivateField(cmd, "fSource"));
    }
    
    @Test
    public void testGetConnectionCreateCommand_CreatesNullCommand() {
        setupSourcePolicy(IArchimateFactory.eINSTANCE.createAndJunction());
        
        CreateConnectionRequest request = new CreateConnectionRequest();
        
        // Use a non-legal relationship
        request.setFactory(new ArchimateDiagramModelFactory(IArchimatePackage.eINSTANCE.getInfluenceRelationship()));
        
        // Should be null
        Command cmd = sourcePolicy.getConnectionCreateCommand(request);
        assertNull(cmd);
    }
    
    
    @Test
    public void testGetConnectionCompleteCommand() throws Exception {
        setupSourcePolicy(IArchimateFactory.eINSTANCE.createBusinessActor());
        setupTargetPolicy(IArchimateFactory.eINSTANCE.createBusinessRole());
        
        CreateConnectionRequest request = new CreateConnectionRequest();
        request.setFactory(new ArchimateDiagramModelFactory(IArchimatePackage.eINSTANCE.getAssignmentRelationship()));
        EditPart sourceEditPart = mock(EditPart.class);
        when(sourceEditPart.getModel()).thenReturn(sourceDiagramObject);
        request.setSourceEditPart(sourceEditPart);
        
        Command startCommand = sourcePolicy.getConnectionCreateCommand(request);
        assertNotNull(startCommand);
        
        Command endCommand = targetPolicy.getConnectionCompleteCommand(request);
        assertNotNull(endCommand);
        assertEquals(startCommand, endCommand);
        
        assertEquals(targetDiagramObject, TestUtils.getPrivateField(endCommand, "fTarget"));
    }
    
    // TODO @Test
    public void testGetReconnectSourceCommand() {
    }
    
    // TODO @Test
    public void testGetReconnectTargetCommand() {
    }
    
    @Test
    public void testIsValidConnectionSource() {
        IDiagramModelArchimateObject dmo = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        dmo.setArchimateElement(IArchimateFactory.eINSTANCE.createBusinessActor());
        
        for(EClass eClass : ArchimateModelUtils.getRelationsClasses()) {
            assertTrue(ArchimateDiagramConnectionPolicy.isValidConnectionSource(dmo, eClass));
        }
        
        // Bogus one
        dmo.setArchimateElement(IArchimateFactory.eINSTANCE.createAndJunction());
        assertFalse(ArchimateDiagramConnectionPolicy.isValidConnectionSource(dmo, IArchimatePackage.eINSTANCE.getInfluenceRelationship()));
        
        // OK if relationshipType is null (magic connector)
        assertTrue(ArchimateDiagramConnectionPolicy.isValidConnectionSource(dmo, null));
    }
    
    @Test
    public void testIsValidConnection_DiagramModelConnection() {
        EClass relationshipType = IArchimatePackage.eINSTANCE.getDiagramModelConnection(); 
        
        IDiagramModelGroup group = IArchimateFactory.eINSTANCE.createDiagramModelGroup();
        IDiagramModelNote note = IArchimateFactory.eINSTANCE.createDiagramModelNote();
        IDiagramModelArchimateObject dmao1 = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        IDiagramModelArchimateObject dmao2 = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        IDiagramModelReference dmRef = IArchimateFactory.eINSTANCE.createDiagramModelReference();
        
        // Source == Target
        assertFalse(ArchimateDiagramConnectionPolicy.isValidConnection(group, group, relationshipType));

        // Both ArchiMate types
        assertFalse(ArchimateDiagramConnectionPolicy.isValidConnection(dmao1, dmao2, relationshipType));
        
        // Target ArchiMate type
        assertFalse(ArchimateDiagramConnectionPolicy.isValidConnection(group, dmao2, relationshipType));
        
        // Target ArchiMate type
        assertFalse(ArchimateDiagramConnectionPolicy.isValidConnection(dmRef, dmao2, relationshipType));

        // Source ArchiMate type to Note
        assertTrue(ArchimateDiagramConnectionPolicy.isValidConnection(dmao1, note, relationshipType));

        // OK
        assertTrue(ArchimateDiagramConnectionPolicy.isValidConnection(note, group, relationshipType));
    }
    
    @Test
    public void testIsValidConnection_ArchimateDiagramModelConnection() {
        EClass relationshipType = IArchimatePackage.eINSTANCE.getAssociationRelationship(); 
        
        IDiagramModelArchimateObject dmao1 = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        dmao1.setArchimateElement(IArchimateFactory.eINSTANCE.createBusinessActor());
        
        IDiagramModelArchimateObject dmao2 = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        dmao2.setArchimateElement(IArchimateFactory.eINSTANCE.createBusinessActor());
        
        // OK
        assertTrue(ArchimateDiagramConnectionPolicy.isValidConnection(dmao1, dmao2, relationshipType));

        // OK if relationshipType is null (magic connector)
        assertTrue(ArchimateDiagramConnectionPolicy.isValidConnection(dmao1, dmao2, null));
        
        // Nope
        assertFalse(ArchimateDiagramConnectionPolicy.isValidConnection(IArchimateFactory.eINSTANCE.createDiagramModelGroup(), dmao2, relationshipType));
        assertFalse(ArchimateDiagramConnectionPolicy.isValidConnection(IArchimateFactory.eINSTANCE.createDiagramModelNote(), dmao2, relationshipType));
    }
}